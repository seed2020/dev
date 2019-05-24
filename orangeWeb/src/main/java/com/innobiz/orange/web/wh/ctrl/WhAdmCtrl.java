package com.innobiz.orange.web.wh.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtxSortOrdrChnVo;
import com.innobiz.orange.web.wh.svc.WhAdmSvc;
import com.innobiz.orange.web.wh.svc.WhCmSvc;
import com.innobiz.orange.web.wh.svc.WhHpSvc;
import com.innobiz.orange.web.wh.svc.WhRescSvc;
import com.innobiz.orange.web.wh.utils.WhConstant;
import com.innobiz.orange.web.wh.vo.WhCatGrpBVo;
import com.innobiz.orange.web.wh.vo.WhCatGrpLVo;
import com.innobiz.orange.web.wh.vo.WhMdBVo;
import com.innobiz.orange.web.wh.vo.WhMdPichLVo;
import com.innobiz.orange.web.wh.vo.WhPichGrpBVo;
import com.innobiz.orange.web.wh.vo.WhPichGrpLVo;
import com.innobiz.orange.web.wh.vo.WhResEvalBVo;
import com.innobiz.orange.web.wh.vo.WhRescBVo;

@Controller
public class WhAdmCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WhAdmCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WhCmSvc whCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "whRescSvc")
	private WhRescSvc whRescSvc;
	
	/** 관리 서비스 */
	@Resource(name = "whAdmSvc")
	private WhAdmSvc whAdmSvc;
	
	/** 헬프데스크 서비스 */
	@Resource(name = "whHpSvc")
	private WhHpSvc whHpSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 모듈관리 */
	@RequestMapping(value = "/wh/adm/md/setModule")
	public String setModule(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wh/adm/md/setModule");
	}
	
	/** 모듈 트리 조회 */
	@RequestMapping(value = "/wh/adm/md/treeMdFrm")
	public String treeMdFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		model.put("whMdBVoList", whAdmSvc.getMdTreeList(userVo.getCompId(), null, langTypCd));
		
		return LayoutUtil.getJspPath("/wh/adm/md/treeMdFrm");
	}
	
	/** 폴더 목록 조회(오른쪽 프레임) - 일괄 저장용 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wh/adm/md/listMdFrm")
	public String listMdFrm(HttpServletRequest request,
			@Parameter(name="mdPid", required=false) String mdPid,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		List<WhMdBVo> whMdBVoList=null;
		if(mdPid==null || mdPid.isEmpty()){
			whMdBVoList = new ArrayList<WhMdBVo>();
			// 화면 구성용 2개의 빈 vo 넣음
			whMdBVoList.add(new WhMdBVo());
			whMdBVoList.add(new WhMdBVo());
		}else{
			WhMdBVo whMdBVo = new WhMdBVo();
			whMdBVo.setQueryLang(langTypCd);
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			whAdmSvc.setCompAffiliateIdList(userVo.getCompId(), langTypCd, whMdBVo, false);
			whMdBVo.setMdPid(mdPid);
			whMdBVo.setOrderBy("SORT_ORDR");
			whMdBVoList = (List<WhMdBVo>)commonSvc.queryList(whMdBVo);
			// 화면 구성용 2개의 빈 vo 넣음
			whMdBVoList.add(new WhMdBVo());
			whMdBVoList.add(new WhMdBVo());
			for (WhMdBVo storedWhMdBVo : whMdBVoList) {
				if (storedWhMdBVo.getRescId() != null) {
					// 리소스기본(WH_RESC_B) 테이블 - 조회
					whRescSvc.queryRescBVo(storedWhMdBVo.getRescId(), model);
				}
			}
		}
		model.put("whMdBVoList", whMdBVoList);
		
		return LayoutUtil.getJspPath("/wh/adm/md/listMdFrm");
	}
	
	/** 모듈 상세 조회(오른쪽 프레임) - 조회 */
	@RequestMapping(value = "/wh/adm/md/setMdFrm")
	public String setMdFrm(HttpServletRequest request,
			@Parameter(name="mdId", required=false) String mdId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId = userVo.getCompId();
		// 모듈 상세 조회
		WhMdBVo whMdBVo = whAdmSvc.getWhMdBVo(compId, mdId, langTypCd);
		if(whMdBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		if (whMdBVo.getRescId() != null) {
			// 리소스기본(WH_RESC_B) 테이블 - 조회
			whRescSvc.queryRescBVo(whMdBVo.getRescId(), model);
		}
		model.put("whMdBVo", whMdBVo);
		
		// 처리유형그룹 조회
		model.put("whCatGrpBVoList", whAdmSvc.getCatGrpList(compId, langTypCd, "Y"));
		
		List<WhMdPichLVo> whMdPichLVoList = whHpSvc.getMdPichList(langTypCd, mdId, null);
		if(whMdPichLVoList!=null && whMdPichLVoList.size()>0){
			WhPichGrpBVo whPichGrpBVo = null;
			for(WhMdPichLVo vo : whMdPichLVoList){
				if(vo.getPichTypCd()==null || !"G".equals(vo.getPichTypCd())) continue;
				if("G".equals(vo.getPichTypCd()) && (vo.getPichNm()==null || vo.getPichNm().isEmpty())){
					whPichGrpBVo=whAdmSvc.getPichGrpDtl(compId, langTypCd, vo.getIdVa());
					if(whPichGrpBVo!=null) vo.setPichNm(whPichGrpBVo.getPichGrpNm());
				}
			}
		}
		
		// 담당자 목록 조회
		model.put("whMdPichLVoList", whMdPichLVoList);
		
		return LayoutUtil.getJspPath("/wh/adm/md/setMdFrm");
	}
	
	/** [팝업] - 모듈 등록 */
	@RequestMapping(value = "/wh/adm/md/setMdPop")
	public String setMdPop(HttpServletRequest request,
			@Parameter(name="mdId", required=false) String mdId,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		if(mdId!=null && !mdId.isEmpty()){
			WhMdBVo whMdBVo = whAdmSvc.getWhMdBVo(userVo.getCompId(), mdId, langTypCd);
			if(whMdBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			if (whMdBVo.getRescId() != null) {
				// 리소스기본(WL_RESC_B) 테이블 - 조회, 모델에 추가
				whRescSvc.queryRescBVo(whMdBVo.getRescId(), model);
			}
			model.put("whMdBVo", whMdBVo);
			
			if("ROOT".equals(whMdBVo.getMdPid())){ // 최상위 모듈 일경우 전체 회사 목록을 추가한다.
				whAdmSvc.setCompAffiliateVoList(model, userVo.getCompId(), langTypCd);
			}
			
		}
		return LayoutUtil.getJspPath("/wh/adm/md/setMdPop");
	}
	
	/** [AJAX] - 모듈 저장 (단건) */
	@RequestMapping(value = "/wh/adm/md/transMdAjx")
	public String transMdAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String mdId = (String) object.get("mdId");
			Map<String, Object> paramMap = JsonUtil.jsonToMap(object);
			
			QueryQueue queryQueue = new QueryQueue();
						
			WhRescBVo whRescBVo = whRescSvc.collectWhRescBVo(request, "", queryQueue, paramMap);
			
			if (whRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId = userVo.getCompId(); // 회사ID
			WhMdBVo whMdBVo = new WhMdBVo();
			
			// jsonMap => Vo
			whCmSvc.setParamToVo(paramMap, whMdBVo);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
						
			// 회사ID 일괄 변경
			if(mdId!=null && !mdId.isEmpty() && paramMap.containsKey("paramCompId") && paramMap.get("paramCompId")!=null){
				whMdBVo.setCompId(null);
				String paramCompId=(String)paramMap.get("paramCompId");
				
				// 하위 모듈 목록 조회
				List<WhMdBVo> downMdList=whAdmSvc.getDownTreeList(paramCompId, mdId, langTypCd, false, "Y");
				WhMdBVo updateWhMdBVo = new WhMdBVo();
				List<String> mdIdList = new ArrayList<String>();
				for(WhMdBVo storedWhMdBVo : downMdList){
					mdIdList.add(storedWhMdBVo.getMdId());
				}
				if(mdIdList.size()>0){ // 하위 모듈 회사 일괄변경
					updateWhMdBVo.setParamCompId(paramCompId);
					updateWhMdBVo.setMdIdList(mdIdList);
					queryQueue.update(updateWhMdBVo);
				}
				
			}
			
			// 회사 ID 조회조건 추가[계열사 설정 확인]
			//whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whMdBVo, false);
			whMdBVo.setMdNm(whRescBVo.getRescVa());
			if(whMdBVo.getMdTypCd()==null || whMdBVo.getMdTypCd().isEmpty())
				whMdBVo.setMdTypCd("F"); // 폴더
			
			// 폴더여부
			boolean isFolder="F".equals(whMdBVo.getMdTypCd());
			if(mdId!=null && !mdId.isEmpty()){
				whMdBVo.setModrUid(userVo.getUserUid());
				whMdBVo.setModDt("sysdate");
				
				// 모듈 사용여부 조회
				if(whAdmSvc.isMdUse(mdId, null)){
					isFolder=false;
					whMdBVo.setMdTypCd(null);
				}
				
				if(isFolder)
					whMdBVo.setCatGrpId("");
				
				queryQueue.update(whMdBVo);
				
				// 담당자 전체 삭제
				WhMdPichLVo whMdPichLVo = new WhMdPichLVo();
				whMdPichLVo.setMdId(mdId);
				if(commonSvc.count(whMdPichLVo)>0)
					queryQueue.delete(whMdPichLVo);
			}else{
				String mdPid=!paramMap.containsKey("mdPid") || ((String)paramMap.get("mdPid")).isEmpty() ? "ROOT" : (String)paramMap.get("mdPid");
				mdId=whCmSvc.createId("WH_MD_B");
				// 순서 구하기
				WhMdBVo sortVo = new WhMdBVo();
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				whAdmSvc.setCompAffiliateIdList(compId, langTypCd, sortVo, false);
				sortVo.setMdPid(mdPid);
				sortVo.setInstanceQueryId("com.innobiz.orange.web.wh.dao.WhMdBDao.maxWhMdB");
				
				Integer sortOrdr=commonSvc.queryInt(sortVo);
				if(sortOrdr==null)
					sortOrdr=1;
				
				// 부모 모듈ID 조회
				if(mdPid==null || "ROOT".equals(mdPid)){
					whMdBVo.setCompId(compId);
				}else{
					WhMdBVo parentVo = whAdmSvc.getWhMdBVo(mdPid, langTypCd);
					whMdBVo.setCompId(parentVo.getCompId());
				}
				whMdBVo.setMdPid(mdPid);
				whMdBVo.setRescId(whRescBVo.getRescId());
				whMdBVo.setRegrUid(userVo.getUserUid());	
				whMdBVo.setRegDt("sysdate");
				whMdBVo.setMdId(mdId);
				whMdBVo.setSortOrdr(sortOrdr.intValue());
				queryQueue.insert(whMdBVo);
			}
			
			if(!isFolder && paramMap.containsKey("pichList") && paramMap.get("pichList")!=null){
				String pichList=(String)paramMap.get("pichList");
				pichList=pichList.replaceAll("\\\\","");
				try{
					JSONParser parser = new JSONParser();
					JSONArray jsonArray = (JSONArray)parser.parse(pichList);
					if(jsonArray!=null && !jsonArray.isEmpty()){
						WhMdPichLVo whMdPichLVo = null;
						JSONObject json;
						int sortOrdr=0;
						String idVa=null;
						String pichTypCd=null;
						for(int i=0;i<jsonArray.size();i++){
							json=(JSONObject)jsonArray.get(i);
							whMdPichLVo = new WhMdPichLVo();
							if(json.containsKey("userUid")){
								idVa=(String)json.get("userUid");
								pichTypCd="U";
							}else if(json.containsKey("pichGrpId")){
								idVa=(String)json.get("pichGrpId");
								pichTypCd="G";
							}else continue;
							if(idVa==null) continue;
							whMdPichLVo.setMdId(mdId);
							whMdPichLVo.setPichTypCd(pichTypCd); //사용자 [O : 조직, G : 담당자그룹]
							whMdPichLVo.setIdVa(idVa.trim());
							whMdPichLVo.setSortOrdr(++sortOrdr);
							queryQueue.insert(whMdPichLVo);
						}
					}
				}catch(ParseException pe){
					pe.printStackTrace();
				}
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, WhConstant.MD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(WhConstant.MD);
						
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("mdId", mdId);
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** [히든프레임] 모듈목록 일괄 저장(오른쪽 프레임) */
	@RequestMapping(value = "/wh/adm/md/transMdList")
	public String transMdList(HttpServletRequest request,			
			@Parameter(name="mdPid", required=false) String mdPid,
			@Parameter(name="delList", required=false) String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId=userVo.getCompId();
			QueryQueue queryQueue = new QueryQueue();
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String mdId;
			String[] delIds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			// 모듈기본(WH_MD_B) 테이블 VO
			WhMdBVo whMdBVo;
			for(i=0;i<delIds.length;i++){
				mdId = delIds[i];
				whAdmSvc.deleteMdList(queryQueue, compId, mdId);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 모듈기본(WH_MD_B) 테이블
			@SuppressWarnings("unchecked")
			List<WhMdBVo> whMdBVoList = (List<WhMdBVo>)VoUtil.bindList(request, WhMdBVo.class, new String[]{"valid"});
			size = whMdBVoList==null ? 0 : whMdBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				whRescSvc.collectWhRescBVoList(request, queryQueue, whMdBVoList, "useYn", "rescId", "mdNm");
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			WhMdBVo parentVo;
			for(i=0;i<size;i++){
				whMdBVo = whMdBVoList.get(i);
				// 회사 ID 조회조건 추가[계열사 설정 확인]
				//whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whMdBVo, false);
				//whMdBVo.setCompId(compId);
				whMdBVo.setMdPid(mdPid);
				
				if(whMdBVo.getMdId() == null || whMdBVo.getMdId().isEmpty()){
					whMdBVo.setMdId(whCmSvc.createId("WH_MD_B"));
					// 등록자UIO, 등록일시
					whMdBVo.setRegrUid(userVo.getUserUid());
					whMdBVo.setRegDt("sysdate");
					
					// 부모 모듈ID 조회
					if(mdPid==null || "ROOT".equals(mdPid)){
						whMdBVo.setCompId(compId);
					}else{
						parentVo = whAdmSvc.getWhMdBVo(mdPid, langTypCd);
						whMdBVo.setCompId(parentVo.getCompId());
					}
					
				}else{
					// 모듈 사용여부 조회
					if(whAdmSvc.isMdUse(whMdBVo.getMdId(), null))
						whMdBVo.setMdTypCd(null);
					
					if(whMdBVo.getMdTypCd()!=null && "F".equals(whMdBVo.getMdTypCd())){
						whMdBVo.setCatGrpId("");
						// 담당자 전체 삭제
						WhMdPichLVo whMdPichLVo = new WhMdPichLVo();
						whMdPichLVo.setMdId(whMdBVo.getMdId());
						if(commonSvc.count(whMdPichLVo)>0)
							queryQueue.delete(whMdPichLVo);
					}
					// 수정자, 수정일시
					whMdBVo.setModrUid(userVo.getUserUid());
					whMdBVo.setModDt("sysdate");
				}
				queryQueue.store(whMdBVo);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, WhConstant.MD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(WhConstant.MD);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+mdPid+"');");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 삭제 */
	@RequestMapping(value = "/wh/adm/md/transMdDelAjx")
	public String transMdDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("mdIds");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId = userVo.getCompId();
			QueryQueue queryQueue = new QueryQueue();
			
			String mdId;
			for(int i=0;i<jsonArray.size();i++){
				mdId = (String)jsonArray.get(i);
				if(mdId==null || mdId.isEmpty()) continue;				
				whAdmSvc.deleteMdList(queryQueue, compId, mdId);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, WhConstant.MD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(WhConstant.MD);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [AJAX] - 순서변경 */
	@RequestMapping(value = "/wh/adm/md/transMdMoveAjx")
	public String transMdMoveAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {
		
		//
		// 폴더 및 메뉴의 순서 목적으로 만들었는데
		// 메뉴의 순서는 저장하면서 한번에 바뀌므로 실제로
		// mdIds - 여기에 한개의 폴더 아이디만 들어옴
		//
		
		JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
		
		JSONArray jsonArray = (JSONArray)jsonObject.get("mdIds");
		String direction = (String)jsonObject.get("direction");
		
		QueryQueue queryQueue = new QueryQueue();
		PtxSortOrdrChnVo ptxSortOrdrChnVo;
		
		if(direction==null){
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			if(direction==null) LOGGER.error("Menu move(up/down) - direction==null : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}
			
		WhMdBVo whMdBVo = new WhMdBVo(), storedWhMdBVo;
		// 위로 이동
		if("up".equals(direction) || "tup".equals(direction)){
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=1, switchOrdr;
			whMdBVo = new WhMdBVo();
			
			String mdId, mdPid=null;
			for(int i=0;i<jsonArray.size();i++){
				mdId = (String)jsonArray.get(i);
				
				whMdBVo.setMdId(mdId);
				storedWhMdBVo = (WhMdBVo)commonSvc.queryVo(whMdBVo);
				if(storedWhMdBVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-UP - mdId:"+mdId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedWhMdBVo.getSortOrdr());
				mdPid = storedWhMdBVo.getMdPid();
				
				if(stdOrdr==curOrdr){
					stdOrdr++;
					continue;
				}
				switchOrdr = curOrdr-1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("WH_MD_B");
				ptxSortOrdrChnVo.setPkCol("MD_PID");
				ptxSortOrdrChnVo.setPk(storedWhMdBVo.getMdPid());
				ptxSortOrdrChnVo.setChnVa(1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				if("tup".equals(direction)){
					switchOrdr = 1;
					ptxSortOrdrChnVo.setLessThen(curOrdr-1);
				}else{
					switchOrdr = curOrdr-1;
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
				}
				ptxSortOrdrChnVo.setMoreThen(switchOrdr);
				queryQueue.update(ptxSortOrdrChnVo);
				storedWhMdBVo = new WhMdBVo();
				storedWhMdBVo.setMdId(mdId);
				storedWhMdBVo.setSortOrdr(switchOrdr);
				queryQueue.update(storedWhMdBVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, WhConstant.MD);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(WhConstant.MD);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 mdIds 에 세팅함
				whMdBVo = new WhMdBVo();
				whMdBVo.setMdPid(mdPid);
				whMdBVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<WhMdBVo> whMdBVoList = (List<WhMdBVo>)commonSvc.queryList(whMdBVo);
				int i, size = whMdBVoList==null ? 0 : whMdBVoList.size();
				List<String> mdIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					mdIdList.add(whMdBVoList.get(i).getMdId());
				}
				model.put("mdIds", mdIdList);
				
			} else {
				//cm.msg.nodata.moveup=위로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.moveup", request));
			}
			return JsonUtil.returnJson(model);
			
			// 아래로 이동
		} else if("down".equals(direction) || "tdown".equals(direction)){
			
			String mdPid = (String)jsonObject.get("mdPid");
			
			ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
			ptxSortOrdrChnVo.setTabId("WH_MD_B");
			ptxSortOrdrChnVo.setPkCol("MD_PID");
			ptxSortOrdrChnVo.setPk(mdPid);
			ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.selectMaxSortOrdr");
			Integer maxSortOrdr = commonSvc.queryInt(ptxSortOrdrChnVo);
			
			// curOrdr - 현재순번
			// stdOrdr - 기준순번 - 현재순번과 기준순번이 같으면 옮길수 없는 것으로 패스
			Integer curOrdr, stdOrdr=maxSortOrdr, switchOrdr;
			whMdBVo = new WhMdBVo();
			
			String mdId;
			for(int i=jsonArray.size()-1;i>=0;i--){
				mdId = (String)jsonArray.get(i);
				
				whMdBVo.setMdId(mdId);
				storedWhMdBVo = (WhMdBVo)commonSvc.queryVo(whMdBVo);
				if(storedWhMdBVo==null){
					//cm.msg.noData=해당하는 데이터가 없습니다.
					String msg = messageProperties.getMessage("cm.msg.noData", request);
					model.put("message", msg);
					LOGGER.error(msg+" : CANNOT MOVE-DOWN - mdId:"+mdId);
					return JsonUtil.returnJson(model);
				}
				curOrdr = Integer.valueOf(storedWhMdBVo.getSortOrdr());
				
				if(stdOrdr==curOrdr){
					stdOrdr--;
					continue;
				}
				switchOrdr = curOrdr+1;
				
				ptxSortOrdrChnVo = new PtxSortOrdrChnVo();
				ptxSortOrdrChnVo.setTabId("WH_MD_B");
				ptxSortOrdrChnVo.setPkCol("MD_PID");
				ptxSortOrdrChnVo.setPk(storedWhMdBVo.getMdPid());
				ptxSortOrdrChnVo.setChnVa(-1);
				ptxSortOrdrChnVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtxSortOrdrMngDao.updateSortOrdr");
				
				//맨아래 일경우 기준순번을 최대 정렬 순서로 변경한다.			
				if("tdown".equals(direction)){
					switchOrdr = maxSortOrdr.intValue();
					ptxSortOrdrChnVo.setMoreThen(curOrdr+1);
					ptxSortOrdrChnVo.setLessThen(maxSortOrdr.intValue());
				}else{
					switchOrdr = curOrdr+1;
					ptxSortOrdrChnVo.setMoreThen(switchOrdr);
					ptxSortOrdrChnVo.setLessThen(switchOrdr);
				}
				queryQueue.update(ptxSortOrdrChnVo);
				storedWhMdBVo = new WhMdBVo();
				storedWhMdBVo.setMdId(mdId);
				storedWhMdBVo.setSortOrdr(switchOrdr);
				queryQueue.update(storedWhMdBVo);
			}
			
			if(!queryQueue.isEmpty()){

				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, WhConstant.MD);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(WhConstant.MD);
				
				// 리로드 하지 않고 스크립트로 폴더의 순서만 변경하려고 데이터 조회하여 mdIds 에 세팅함
				whMdBVo = new WhMdBVo();
				whMdBVo.setMdPid(mdPid);
				whMdBVo.setOrderBy("SORT_ORDR");
				
				@SuppressWarnings("unchecked")
				List<WhMdBVo> whMdBVoList = (List<WhMdBVo>)commonSvc.queryList(whMdBVo);
				int i, size = whMdBVoList==null ? 0 : whMdBVoList.size();
				List<String> mdIdList = new ArrayList<String>();
				for(i=0;i<size;i++){
					mdIdList.add(whMdBVoList.get(i).getMdId());
				}
				
				model.put("mdIds", mdIdList);
				
			} else {
				//cm.msg.nodata.movedown=아래로 이동할 항목이 없습니다.
				model.put("message",  messageProperties.getMessage("cm.msg.nodata.movedown", request));
			}
			return JsonUtil.returnJson(model);
			
		} else {
			//cm.msg.notValidCall=파라미터가 잘못되었거나 보안상의 이유로 해당 기능을 수행할 수 없습니다.
			String msg = messageProperties.getMessage("cm.msg.notValidCall", request);
			LOGGER.error("Code move(up/down) - direction=="+direction+" : "+msg);
			model.put("message", msg);
			return JsonUtil.returnJson(model);
		}

	}
	
	
	/** 환경설정 */
	@RequestMapping(value = "/wh/adm/env/setEnv")
	public String setEnv(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String compId=userVo.getCompId();
		// 환경설정 세팅
		whAdmSvc.getEnvConfigMap(model, userVo.getCompId());
		
		model.put("months", WhConstant.MONTHS);
		model.put("days", WhConstant.DAYS);
		
		// 처리유형그룹 조회
		model.put("whCatGrpBVoList", whAdmSvc.getCatGrpList(compId, langTypCd, null));
		
		// 담당자그룹 조회
		model.put("whPichGrpBVoList", whAdmSvc.getPichGrpList(compId, langTypCd, null));
		
		// 결과평가 조회
		model.put("whResEvalBVoList", whHpSvc.getResEvalList(compId, langTypCd));
				
		return LayoutUtil.getJspPath("/wh/adm/env/setEnv");
	}
	
	/** [히든프레임] 옵션관리 - 저장 */
	@RequestMapping(value = "/wh/adm/env/transEnv")
	public String transEnv(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			UserVo userVo = LoginSession.getUser(request);
			QueryQueue queryQueue = new QueryQueue();
			
			ptSysSvc.setSysSetup(WhConstant.SYS_CONFIG+userVo.getCompId(), queryQueue, true, request);

			if(!queryQueue.isEmpty()){
				// 캐쉬 삭제
				String dbTime = ptCacheExpireSvc.getDbTime();
				ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.SYS_SETUP);
				commonSvc.execute(queryQueue);
				ptCacheExpireSvc.checkNow(CacheConfig.SYS_SETUP);
				//cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// cm.msg.nodata.toSave=저장할 데이터가 없습니다.
				model.put("message", messageProperties.getMessage("cm.msg.nodata.toSave", request));
			}
			model.put("todo", "parent.location.replace(parent.location.href);");
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
			model.put("todo", "parent.location.replace(parent.location.href);");
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] - 유형그룹 등록 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wh/adm/env/setCatGrpPop")
	public String setCatGrpPop(HttpServletRequest request,
			@Parameter(name="catGrpId", required=false) String catGrpId,
			ModelMap model) throws Exception {
		
		List<WhCatGrpLVo> whCatGrpLVoList=null;
		if(catGrpId!=null && !catGrpId.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);			
			
			WhCatGrpBVo whCatGrpBVo = new WhCatGrpBVo();
			whCatGrpBVo.setQueryLang(langTypCd);
			whCatGrpBVo.setCompId(userVo.getCompId());
			whCatGrpBVo.setCatGrpId(catGrpId);
			whCatGrpBVo=(WhCatGrpBVo)commonSvc.queryVo(whCatGrpBVo);
			if(whCatGrpBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			WhCatGrpLVo whCatGrpLVo = new WhCatGrpLVo();
			whCatGrpLVo.setCatGrpId(catGrpId);
			whCatGrpLVoList = (List<WhCatGrpLVo>)commonSvc.queryList(whCatGrpLVo);
			if(whCatGrpLVoList!=null){
				for (WhCatGrpLVo storedWhCatGrpLVo : whCatGrpLVoList) {
					if (storedWhCatGrpLVo.getRescId() != null) {
						// 리소스기본(WH_RESC_B) 테이블 - 조회
						whRescSvc.queryRescBVo(storedWhCatGrpLVo.getRescId(), model);
					}
				}
			}
			if (whCatGrpBVo.getRescId() != null) {
				// 리소스기본(WH_RESC_B) 테이블 - 조회
				whRescSvc.queryRescBVo(whCatGrpBVo.getRescId(), model);
			}
			model.put("whCatGrpBVo", whCatGrpBVo);
		}
		
		if(whCatGrpLVoList==null)
			whCatGrpLVoList=new ArrayList<WhCatGrpLVo>();
		
		// 화면 구성용 2개의 빈 vo 넣음
		whCatGrpLVoList.add(new WhCatGrpLVo());
		whCatGrpLVoList.add(new WhCatGrpLVo());
		
		model.put("whCatGrpLVoList", whCatGrpLVoList);
		
		return LayoutUtil.getJspPath("/wh/adm/env/setCatGrpPop");
	}
	
	
	/** [히든프레임] 처리유형 일괄 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wh/adm/env/transCatGrpList")
	public String transCatGrpList(HttpServletRequest request,			
			@Parameter(name="catGrpId", required=false) String catGrpId,
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 처리유형기본(ST_CAT_B) 테이블 VO
			WhCatGrpLVo whCatGrpLVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String catNo;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			for(i=0;i<delCds.length;i++){
				catNo = delCds[i];
				whAdmSvc.chkInUseCat(request, null, catNo, null); // 사용여부 검사
				// 기본 테이블 - 삭제
				whCatGrpLVo = new WhCatGrpLVo();
				whCatGrpLVo.setCatNo(catNo);
				queryQueue.delete(whCatGrpLVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			WhRescBVo whRescBVo = whRescSvc.collectWhRescBVo(request, "grp", queryQueue);
			
			if (whRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			WhCatGrpBVo whCatGrpBVo = new WhCatGrpBVo();
			VoUtil.bind(request, whCatGrpBVo);
			whCatGrpBVo.setCompId(userVo.getCompId());
			whCatGrpBVo.setRescId(whRescBVo.getRescId());
			whCatGrpBVo.setCatGrpNm(whRescBVo.getRescVa());
			
			// 기본여부가 'Y'이면 기존 처리유형 기본여부를 'N'으로 일괄 변경
			if(whCatGrpBVo.getUseYn()!=null && "Y".equals(whCatGrpBVo.getUseYn()) && 
					whCatGrpBVo.getDftYn()!=null && "Y".equals(whCatGrpBVo.getDftYn())){
				WhCatGrpBVo updateVo = new WhCatGrpBVo();
				updateVo.setCompId(userVo.getCompId());
				if(commonSvc.count(updateVo)>0){
					updateVo.setDftYn("N");
					queryQueue.update(updateVo);
				}
			}
			
			if(catGrpId!=null && !catGrpId.isEmpty()){
				whCatGrpBVo.setModrUid(userVo.getUserUid());
				whCatGrpBVo.setModDt("sysdate");
				queryQueue.update(whCatGrpBVo);
			}else{
				catGrpId=whCmSvc.createId("WH_CAT_GRP_B").toString();
				whCatGrpBVo.setCatGrpId(catGrpId);
				whCatGrpBVo.setRegrUid(userVo.getUserUid());
				whCatGrpBVo.setRegDt("sysdate");
				queryQueue.insert(whCatGrpBVo);
			}
			// 처리유형기본(ST_CAT_B) 테이블
			List<WhCatGrpLVo> whCatGrpLVoList = (List<WhCatGrpLVo>)VoUtil.bindList(request, WhCatGrpLVo.class, new String[]{"valid"});
			size = whCatGrpLVoList==null ? 0 : whCatGrpLVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				whRescSvc.collectWhRescBVoList(request, queryQueue, whCatGrpLVoList, "valid", "rescId", "catNm");
			}
			for(i=0;i<size;i++){
				whCatGrpLVo = whCatGrpLVoList.get(i);
				whCatGrpLVo.setCatGrpId(catGrpId);
				if(whCatGrpLVo.getCatNo() == null || whCatGrpLVo.getCatNo().isEmpty())
					whCatGrpLVo.setCatNo(whCmSvc.createNo("WH_CAT_GRP_L").toString());
				
				queryQueue.store(whCatGrpLVo);
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(dialog!=null && !dialog.isEmpty())
				model.put("todo", "parent.pageReload('" + dialog + "');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 처리유형그룹 삭제 */
	@RequestMapping(value = "/wh/adm/env/transCatGrpDelAjx")
	public String transCatGrpDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("grpIds");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String catGrpId;
			if(jsonArray!=null && !jsonArray.isEmpty()){
				WhCatGrpBVo whCatGrpBVo = null;
				WhCatGrpLVo whCatGrpLVo = null;
				for(int i=0;i<jsonArray.size();i++){
					catGrpId = (String)jsonArray.get(i);
					whAdmSvc.chkInUseCat(request, catGrpId, null, null); // 사용여부 검사
					
					// 그룹 목록 - 삭제
					whCatGrpLVo = new WhCatGrpLVo();
					whCatGrpLVo.setCatGrpId(catGrpId);
					queryQueue.delete(whCatGrpLVo);
					
					// 그룹 기본 - 삭제
					whCatGrpBVo = new WhCatGrpBVo();
					whCatGrpBVo.setCompId(userVo.getCompId());
					whCatGrpBVo.setCatGrpId(catGrpId);
					queryQueue.delete(whCatGrpBVo);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [팝업] - 결과평가 등록 */
	@RequestMapping(value = "/wh/adm/env/setResEvalPop")
	public String setResEvalPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		WhResEvalBVo whResEvalBVo = new WhResEvalBVo();
		whResEvalBVo.setQueryLang(langTypCd);
		whResEvalBVo.setCompId(userVo.getCompId());
		@SuppressWarnings("unchecked")
		List<WhResEvalBVo> whResEvalBVoList = (List<WhResEvalBVo>)commonSvc.queryList(whResEvalBVo);
		
		if(whResEvalBVoList!=null){
			for (WhResEvalBVo storedWhResEvalBVo : whResEvalBVoList) {
				if (storedWhResEvalBVo.getRescId() != null) {
					// 리소스기본(WH_RESC_B) 테이블 - 조회
					whRescSvc.queryRescBVo(storedWhResEvalBVo.getRescId(), model);
				}
			}
		}
		
		if(whResEvalBVoList==null)
			whResEvalBVoList=new ArrayList<WhResEvalBVo>();
		
		// 화면 구성용 2개의 빈 vo 넣음
		whResEvalBVoList.add(new WhResEvalBVo());
		whResEvalBVoList.add(new WhResEvalBVo());
		
		model.put("whResEvalBVoList", whResEvalBVoList);
		
		return LayoutUtil.getJspPath("/wh/adm/env/setResEvalPop");
	}
	
	
	/** [히든프레임] 결과평가 일괄 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wh/adm/env/transResEvalList")
	public String transResEvalList(HttpServletRequest request,			
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 결과평가(WH_RES_EVAL_B) 테이블 VO
			WhResEvalBVo whResEvalBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			String compId = userVo.getCompId();
			int i, size;
			String evalNo;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			for(i=0;i<delCds.length;i++){
				evalNo = delCds[i];
				// 사용여부
				whAdmSvc.chkInUseCat(request, evalNo);
				
				// 기본 테이블 - 삭제
				whResEvalBVo = new WhResEvalBVo();
				whResEvalBVo.setCompId(compId);
				whResEvalBVo.setEvalNo(evalNo);
				queryQueue.delete(whResEvalBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 결과평가(WH_RES_EVAL_B) 테이블
			List<WhResEvalBVo> whResEvalBVoList = (List<WhResEvalBVo>)VoUtil.bindList(request, WhResEvalBVo.class, new String[]{"valid"});
			size = whResEvalBVoList==null ? 0 : whResEvalBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				whRescSvc.collectWhRescBVoList(request, queryQueue, whResEvalBVoList, "valid", "rescId", "evalNm");
			}
			for(i=0;i<size;i++){
				whResEvalBVo = whResEvalBVoList.get(i);
				whResEvalBVo.setCompId(compId);
				if(whResEvalBVo.getEvalNo() == null || whResEvalBVo.getEvalNo().isEmpty()){
					whResEvalBVo.setEvalNo(whCmSvc.createNo("WH_RES_EVAL_B").toString());
					whResEvalBVo.setRegrUid(userVo.getUserUid());
					whResEvalBVo.setRegDt("sysdate");
				}else{
					whResEvalBVo.setModrUid(userVo.getUserUid());
					whResEvalBVo.setModDt("sysdate");
				}
				
				queryQueue.store(whResEvalBVo);
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(dialog!=null && !dialog.isEmpty())
				model.put("todo", "parent.pageReload('" + dialog + "');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] - 담당자그룹 목록 조회 */
	@RequestMapping(value = "/wh/adm/md/findPichGrpListPop")
	public String findPichGrpListPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);			
					
		// 담당자그룹 조회
		model.put("whPichGrpBVoList", whAdmSvc.getPichGrpList(userVo.getCompId(), langTypCd, "Y"));
		
		return LayoutUtil.getJspPath("/wh/adm/md/findPichGrpListPop");
	}
	
	
	/** [팝업] - 담당자그룹 등록 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wh/adm/env/setPichGrpPop", "/wh/adm/env/viewPichGrpPop", "/wh/adm/md/viewPichGrpPop"})
	public String setPichGrpPop(HttpServletRequest request,
			@Parameter(name="pichGrpId", required=false) String pichGrpId,
			ModelMap model) throws Exception {
		
		// 상세보기 여부
		boolean isView=request.getRequestURI().indexOf("/viewPichGrpPop")>0;
				
		List<WhPichGrpLVo> whPichGrpLVoList=null;
		if(pichGrpId!=null && !pichGrpId.isEmpty()){
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);			
			
			WhPichGrpBVo whPichGrpBVo = new WhPichGrpBVo();
			whPichGrpBVo.setQueryLang(langTypCd);
			whPichGrpBVo.setCompId(userVo.getCompId());
			whPichGrpBVo.setPichGrpId(pichGrpId);
			whPichGrpBVo=(WhPichGrpBVo)commonSvc.queryVo(whPichGrpBVo);
			if(whPichGrpBVo==null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			WhPichGrpLVo whPichGrpLVo = new WhPichGrpLVo();
			whPichGrpLVo.setPichGrpId(pichGrpId);
			whPichGrpLVoList = (List<WhPichGrpLVo>)commonSvc.queryList(whPichGrpLVo);
			if(!isView && whPichGrpBVo.getRescId() != null) {
				// 리소스기본(WH_RESC_B) 테이블 - 조회
				whRescSvc.queryRescBVo(whPichGrpBVo.getRescId(), model);
			}
			model.put("whPichGrpBVo", whPichGrpBVo);
		}
		
		if(!isView){
			if(whPichGrpLVoList==null)
				whPichGrpLVoList=new ArrayList<WhPichGrpLVo>();
			
			// 화면 구성용 1개의 빈 vo 넣음
			whPichGrpLVoList.add(new WhPichGrpLVo());
		}
		
		if(isView) model.put("isView", Boolean.TRUE);
		else model.put("isView", Boolean.FALSE);
		
		model.put("whPichGrpLVoList", whPichGrpLVoList);
		
		return LayoutUtil.getJspPath("/wh/adm/env/setPichGrpPop");
	}
	
	
	/** [히든프레임] 처리담당자 일괄 저장 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wh/adm/env/transPichGrpList")
	public String transPichGrpList(HttpServletRequest request,			
			@Parameter(name="pichGrpId", required=false) String pichGrpId,
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 담당자목록(WH_PICH_GRP_L) 테이블 VO
			WhPichGrpLVo whPichGrpLVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String pichNo;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			for(i=0;i<delCds.length;i++){
				pichNo = delCds[i];
				//whAdmSvc.chkInUsePich(request, null, pichNo, null); // 사용여부 검사
				// 기본 테이블 - 삭제
				whPichGrpLVo = new WhPichGrpLVo();
				whPichGrpLVo.setPichGrpId(pichGrpId);
				whPichGrpLVo.setUserUid(pichNo);				
				queryQueue.delete(whPichGrpLVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			WhRescBVo whRescBVo = whRescSvc.collectWhRescBVo(request, "grp", queryQueue);
			
			if (whRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			WhPichGrpBVo whPichGrpBVo = new WhPichGrpBVo();
			VoUtil.bind(request, whPichGrpBVo);
			whPichGrpBVo.setCompId(userVo.getCompId());
			whPichGrpBVo.setRescId(whRescBVo.getRescId());
			whPichGrpBVo.setPichGrpNm(whRescBVo.getRescVa());
			
			// 기본여부가 'Y'이면 기존 담당자그룹 기본여부를 'N'으로 일괄 변경
			if(whPichGrpBVo.getUseYn()!=null && "Y".equals(whPichGrpBVo.getUseYn()) && 
					whPichGrpBVo.getDftYn()!=null && "Y".equals(whPichGrpBVo.getDftYn())){
				WhPichGrpBVo updateVo = new WhPichGrpBVo();
				updateVo.setCompId(userVo.getCompId());
				if(commonSvc.count(updateVo)>0){
					updateVo.setDftYn("N");
					queryQueue.update(updateVo);
				}
			}
			
			if(pichGrpId!=null && !pichGrpId.isEmpty()){
				whPichGrpBVo.setModrUid(userVo.getUserUid());
				whPichGrpBVo.setModDt("sysdate");
				queryQueue.update(whPichGrpBVo);
			}else{
				pichGrpId=whCmSvc.createId("WH_PICH_GRP_B").toString();
				whPichGrpBVo.setPichGrpId(pichGrpId);
				whPichGrpBVo.setRegrUid(userVo.getUserUid());
				whPichGrpBVo.setRegDt("sysdate");
				queryQueue.insert(whPichGrpBVo);
			}
			// 담당자목록(WH_PICH_GRP_L) 테이블
			List<WhPichGrpLVo> whPichGrpLVoList = (List<WhPichGrpLVo>)VoUtil.bindList(request, WhPichGrpLVo.class, new String[]{"valid"});
			size = whPichGrpLVoList==null ? 0 : whPichGrpLVoList.size();
		
			for(i=0;i<size;i++){
				whPichGrpLVo = whPichGrpLVoList.get(i);
				whPichGrpLVo.setPichGrpId(pichGrpId);
				
				queryQueue.store(whPichGrpLVo);
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			if(dialog!=null && !dialog.isEmpty())
				model.put("todo", "parent.pageReload('" + dialog + "');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] - 담당자그룹 삭제 */
	@RequestMapping(value = "/wh/adm/env/transPichGrpDelAjx")
	public String transPichGrpDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			JSONArray jsonArray = (JSONArray) object.get("grpIds");
			if (jsonArray == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 게시물 복사
			QueryQueue queryQueue = new QueryQueue();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			String pichGrpId;
			if(jsonArray!=null && !jsonArray.isEmpty()){
				WhPichGrpBVo whPichGrpBVo = null;
				WhPichGrpLVo whPichGrpLVo = null;
				for(int i=0;i<jsonArray.size();i++){
					pichGrpId = (String)jsonArray.get(i);
					//whAdmSvc.chkInUsePich(request, pichGrpId, null, null); // 사용여부 검사
					
					// 그룹 목록 - 삭제
					whPichGrpLVo = new WhPichGrpLVo();
					whPichGrpLVo.setPichGrpId(pichGrpId);
					queryQueue.delete(whPichGrpLVo);
					
					// 그룹 기본 - 삭제
					whPichGrpBVo = new WhPichGrpBVo();
					whPichGrpBVo.setCompId(userVo.getCompId());
					whPichGrpBVo.setPichGrpId(pichGrpId);
					queryQueue.delete(whPichGrpBVo);
				}
			}
			
			if(!queryQueue.isEmpty())
				commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	
	/** [팝업] - 엑셀 목록 순서 */
	@RequestMapping(value = "/wh/adm/env/setExcelColListPop")
	public String setExcelColListPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 환경설정 - 첨부파일 사용여부
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(model, userVo.getCompId(), new String[]{"excelColList"});
		if(configMap!=null)		
			model.put("excelColList", configMap.get("excelColList"));
		model.put("colAllList", whHpSvc.getColAllList());
		
		return LayoutUtil.getJspPath("/wh/adm/env/setExcelColListPop");
	}
	
	
}
