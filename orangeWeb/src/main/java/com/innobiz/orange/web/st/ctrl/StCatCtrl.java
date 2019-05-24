package com.innobiz.orange.web.st.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.st.svc.StCmSvc;
import com.innobiz.orange.web.st.svc.StRescSvc;
import com.innobiz.orange.web.st.vo.StCatBVo;
import com.innobiz.orange.web.st.vo.StSiteBVo;

@Controller
public class StCatCtrl {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(StCatCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private StCmSvc stCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "stRescSvc")
	private StRescSvc stRescSvc;
	
	/** 카테고리 조회 */
	@RequestMapping(value = {"/st/site/listCatFrm", "/st/adm/site/listCatFrm", "/st/adm/site/setCatPop"})
	public String listCatFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		String path = stCmSvc.getRequestPath(request, model , "Cat");
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		StCatBVo stCatBVo = new StCatBVo();
		stCatBVo.setQueryLang(langTypCd);
		stCatBVo.setCompId(userVo.getCompId());
		
		// 카테고리조회
		@SuppressWarnings("unchecked")
		List<StCatBVo> stCatBVoList = (List<StCatBVo>)commonSvc.queryList(stCatBVo);
		model.put("stCatBVoList", stCatBVoList);
		if(path.endsWith("Pop")){
			if(path.startsWith("set")){
				// 화면 구성용 2개의 빈 vo 넣음
				stCatBVoList.add(new StCatBVo());
				stCatBVoList.add(new StCatBVo());
				for (StCatBVo storedStSiteCatBVo : stCatBVoList) {
					if (storedStSiteCatBVo.getRescId() != null) {
						// 리소스기본(DM_RESC_B) 테이블 - 조회
						stRescSvc.queryRescBVo(storedStSiteCatBVo.getRescId(), model);
					}
				}
				model.put("isSelect", Boolean.FALSE);
			}else{
				model.put("isSelect", Boolean.TRUE);
			}
			return LayoutUtil.getJspPath("/st/cat/setCatPop");
		}
		
		return LayoutUtil.getJspPath("/st/cat/listCatFrm");
	}
	
	/** [히든프레임] 카테고리 일괄 저장(왼쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/st/adm/site/transCatList")
	public String transCatList(HttpServletRequest request,			
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 카테고리기본(ST_CAT_B) 테이블 VO
			StCatBVo stCatBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String catId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			// 사이트
			StSiteBVo stSiteBVo = null;
			for(i=0;i<delCds.length;i++){
				catId = delCds[i];
				
				// 사이트 삭제
				stSiteBVo = new StSiteBVo();
				stSiteBVo.setCatId(catId);
				stSiteBVo.setCompId(userVo.getCompId());
				queryQueue.delete(stSiteBVo);
				
				// 기본 테이블 - 삭제
				stCatBVo = new StCatBVo();
				stCatBVo.setCatId(catId);
				stCatBVo.setCompId(userVo.getCompId());
				queryQueue.delete(stCatBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 카테고리기본(ST_CAT_B) 테이블
			List<StCatBVo> stCatBVoList = (List<StCatBVo>)VoUtil.bindList(request, StCatBVo.class, new String[]{"valid"});
			size = stCatBVoList==null ? 0 : stCatBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				stRescSvc.collectStRescBVoList(request, queryQueue, stCatBVoList, "valid", "rescId", "catNm");
			}
			
			for(i=0;i<size;i++){
				stCatBVo = stCatBVoList.get(i);
				stCatBVo.setCompId(userVo.getCompId());
				// 등록자, 등록일시
				stCatBVo.setRegrUid(userVo.getUserUid());
				stCatBVo.setRegDt("sysdate");
				// 수정자, 수정일시
				stCatBVo.setModrUid(userVo.getUserUid());
				stCatBVo.setModDt("sysdate");
				
				if(stCatBVo.getCatId() == null || stCatBVo.getCatId().isEmpty()){
					stCatBVo.setCatId(stCmSvc.createId("ST_CAT_B"));
				}
				queryQueue.store(stCatBVo);
			}
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			String menuId = ParamUtil.getRequestParam(request, "menuId", true);
			model.put("todo", "parent.reloadFrm('leftPage','./listCatFrm.do?menuId="+menuId+"','"+(dialog!=null && !dialog.isEmpty() ? dialog : "")+"');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
}
