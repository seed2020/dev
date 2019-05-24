package com.innobiz.orange.web.wf.ctrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.wf.svc.WfFormSvc;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;
import com.innobiz.orange.web.wf.vo.WfMdFormDVo;
import com.innobiz.orange.web.wf.vo.WfWorksLVo;

@Controller
public class WfFormCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfFormCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 모듈 양식 서비스 */
	@Resource(name = "wfMdFormSvc")
	private WfMdFormSvc wfMdFormSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** [팝업] 양식 조회 */
	@RequestMapping(value = {"/cm/form/findFormPop", "/bb/adm/findFormPop", "/ap/env/findFormPop", "/ap/adm/form/findFormPop"})
	public String listReadHstPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/findFormPop");
	}
	
	/** [AJAX] - 업무 번호 생성 [양식 데이터 선 저장후 업무번호 리턴] */
	@RequestMapping(value = "/ap/box/transWorkNoAjx")
	public String transWorkNoAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mdCd = (String) object.get("mdCd"); // 모듈코드
			String genId = (String) object.get("genId"); // 생성ID
			String formNo = (String) object.get("formNo"); // 양식번호
			
			if (mdCd==null || mdCd.isEmpty() || formNo==null || formNo.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			// 양식 데이터 저장 및 파라미터 맵으로 리턴
			Map<String,String> returnMap=null;
			if("AP".equals(mdCd)){
				String workNo = (String) object.get("workNo"); // 업무번호
				returnMap = wfMdFormSvc.saveFormByAP(request, null, genId, formNo, workNo, paramMap);
			}else if("BB".equals(mdCd)){
				String mdRefId = (String) object.get("mdRefId"); // 모듈참조ID
				String mdNo = (String) object.get("mdNo"); // 모듈번호
				returnMap =wfMdFormSvc.saveFormByBB(request, null, genId, formNo, mdRefId, mdNo);
			}
			
			model.put("returnMap", returnMap);
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [SCHEDULED] 본문삽입용 데이터 중에 임시저장(T) 인 데이터 일괄 삭제 */
	//@Scheduled(cron=" 3 30 0 * * *")//매일 3시 30분에 초기화
	//@Scheduled(fixedDelay=20000)
	@SuppressWarnings("unchecked")
	public void deleteTmpWorksDataScheduled() {
		try {
			QueryQueue queryQueue = null;
			
			/** 한번에 처리할 데이터 처리 최대 갯수 */
			final Integer MAX_TRANSACTION_SIZE = 1000;
			
			// 삭제할 모듈코드
			String[] delMds = new String[]{"AP"};
			WfMdFormDVo wfMdFormDVo=null;
			
			// 양식번호 목록
			List<String> formNoList = null;
			List<WfMdFormDVo> wfMdFormDVoList=null;
			Set<String> hs = null;
			// 테이블관리 기본(W) 테이블 - BIND
			WfWorksLVo wfWorksLVo = null;
					
			for(String mdCd : delMds){
				formNoList = new ArrayList<String>();
				
				wfMdFormDVo = new WfMdFormDVo();
				wfMdFormDVo.setMdCd(mdCd);
				
				if(commonSvc.count(wfMdFormDVo)>0){
					wfMdFormDVoList=(List<WfMdFormDVo>)commonSvc.queryList(wfMdFormDVo);
					for(WfMdFormDVo storedWfMdFormDVo : wfMdFormDVoList){
						formNoList.add(storedWfMdFormDVo.getFormNo());
					}
				}
				
				if(formNoList.size()==0) continue;
				// HashSet 으로 중복ID 제거
				hs = new HashSet<String>(formNoList);
				formNoList = new ArrayList<String>(hs);
				
				List<Map<String, Object>> mapList=null;
				for(String formNo : formNoList){
					wfWorksLVo = wfFormSvc.newWfWorksLVo(null, formNo, false);
					wfWorksLVo.setStatCd("T");
					
					// 삭제할 데이터 건수
					int delCount=commonSvc.count(wfWorksLVo);
					int pageCnt = (int)Math.ceil((double)delCount / MAX_TRANSACTION_SIZE.intValue());
					
					if(delCount>0){
						// 1000 건 단위로 나누어 실행함 - 롤백 세크먼트 고려 - 페이징 0부터 시작함
						for(int i=0; i<pageCnt; i++){
							wfWorksLVo.setPageNo(Integer.valueOf(i));
							mapList = (List<Map<String, Object>>) commonSvc.queryList(wfWorksLVo);
							
							if(mapList!=null){
								queryQueue = new QueryQueue();
								for(Map<String, Object> storedWfWorksLVoMap : mapList){
									wfMdFormSvc.deleteFormByAP(queryQueue, formNo, (String)storedWfWorksLVoMap.get("workNo"));
								}
							}
							
						}
					}
				}
			}
			if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	
}
