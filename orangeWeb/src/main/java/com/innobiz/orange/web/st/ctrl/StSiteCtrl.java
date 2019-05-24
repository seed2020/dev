package com.innobiz.orange.web.st.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.st.svc.StCmSvc;
import com.innobiz.orange.web.st.svc.StRescSvc;
import com.innobiz.orange.web.st.svc.StSiteSvc;
import com.innobiz.orange.web.st.vo.StCatBVo;
import com.innobiz.orange.web.st.vo.StRescBVo;
import com.innobiz.orange.web.st.vo.StSiteBVo;
import com.innobiz.orange.web.st.vo.StSiteImgDVo;

@Controller
public class StSiteCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(StSiteCtrl.class);
	
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
	
	/** 이미지 저장 서비스 */
	@Resource(name = "stSiteSvc")
	private StSiteSvc stSiteSvc;
	
	/** 사이트 관리 - 관리자 */
	@RequestMapping(value = "/st/adm/site/setSite")
	public String setSite(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/st/site/setSite");
	}
	
	/** 등록 수정 */
	@RequestMapping(value = "/st/adm/site/setSitePop")
	public String setSitePop(HttpServletRequest request,
			@Parameter(name="siteId", required=false) String siteId,
			ModelMap model) throws Exception {
		
		if(siteId!=null && !siteId.isEmpty()){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			StSiteBVo stSiteBVo = new StSiteBVo();
			stSiteBVo.setQueryLang(langTypCd);
			stSiteBVo.setCompId(userVo.getCompId());
			stSiteBVo.setSiteId(siteId);
			stSiteBVo = (StSiteBVo)commonSvc.queryVo(stSiteBVo);
			if(stSiteBVo == null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			model.put("stSiteBVo", stSiteBVo);
		}
		return LayoutUtil.getJspPath("/st/site/setSitePop");
	}
	
	/** 조회 */
	@RequestMapping(value = {"/st/site/listSite", "/cm/site/listSiteFrm", "/st/adm/site/listSiteFrm", "/st/site/listSitePltFrm", "/cm/site/listSitePltFrm"})
	public String listSiteFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		StSiteBVo stSiteBVo = new StSiteBVo();
		VoUtil.bind(request, stSiteBVo);
		stSiteBVo.setQueryLang(langTypCd);
		stSiteBVo.setCompId(userVo.getCompId());
		
		// 프레임여부
		boolean isFrm=request.getRequestURI().indexOf("Frm")>-1;
		
		// 포틀릿 여부
		boolean isPlt = request.getRequestURI().indexOf("Plt")>-1;
				
		// 관리자 여부
		boolean isAdm=request.getRequestURI().startsWith("/st/adm/");
		if(stSiteBVo.getCatId()!=null && !stSiteBVo.getCatId().isEmpty()){
			stSiteBVo.setOrderBy("SORT_ORDR ASC");
		}
		
		if(isAdm && isFrm) {
			@SuppressWarnings("unchecked")
			List<StSiteBVo> stSiteBVoList = (List<StSiteBVo>)commonSvc.queryList(stSiteBVo);
			model.put("stSiteBVoList", stSiteBVoList);
			
			// 화면 구성용 2개의 빈 vo 넣음
			stSiteBVoList.add(new StSiteBVo());
			stSiteBVoList.add(new StSiteBVo());
			for (StSiteBVo storedStSiteBVo : stSiteBVoList) {
				if (storedStSiteBVo.getRescId() != null) {
					// 리소스기본(ST_RESC_B) 테이블 - 조회
					stRescSvc.queryRescBVo(storedStSiteBVo.getRescId(), model);
				}
			}
			model.put("mode", "list");
			return LayoutUtil.getJspPath("/st/site/listSite","Frm");
		}else{
			// 카운트 조회
			Integer recodeCount = commonSvc.count(stSiteBVo);
			if(isPlt){
				String pageRowCnt = ParamUtil.getRequestParam(request, "pageRowCnt", false);
				int rowCnt = 0;
				if(pageRowCnt!=null && !pageRowCnt.isEmpty()){
					try{
						rowCnt = Integer.parseInt(pageRowCnt);
						model.addAttribute("pageRowCnt", pageRowCnt);
					} catch(Exception ignore){}
				}
				if(rowCnt==0){
					String hghtPx = ParamUtil.getRequestParam(request, "hghtPx", true);
					String colYn = ParamUtil.getRequestParam(request, "colYn", true);
					// 한 페이지 레코드수 - 높이에 의한 계산
					int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
					int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
					rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
					model.addAttribute("pageRowCnt", Integer.valueOf(rowCnt));
				}
				PersonalUtil.setFixedPaging(request, stSiteBVo, rowCnt, recodeCount);
			}else {
				PersonalUtil.setPaging(request, stSiteBVo, recodeCount);
			}
			
			model.put("recodeCount", recodeCount);
			
			StCatBVo stCatBVo = new StCatBVo();
			stCatBVo.setQueryLang(langTypCd);
			stCatBVo.setCompId(userVo.getCompId());
			
			// 카테고리조회
			@SuppressWarnings("unchecked")
			List<StCatBVo> stCatBVoList = (List<StCatBVo>)commonSvc.queryList(stCatBVo);
			model.put("stCatBVoList", stCatBVoList);			
			
			// 첫번째 카테고리 ID를 조회한다.
			if(stSiteBVo.getCatId()==null && stCatBVoList!=null && stCatBVoList.size()>0){
				stSiteBVo.setOrderBy("SORT_ORDR ASC");
				stSiteBVo.setCatId(stCatBVoList.get(0).getCatId());
				model.put("paramCatId", stSiteBVo.getCatId());
			}
			
			@SuppressWarnings("unchecked")
			List<StSiteBVo> stSiteBVoList = (List<StSiteBVo>)commonSvc.queryList(stSiteBVo);
			model.put("stSiteBVoList", stSiteBVoList);
			
			// 이미지 조회
			for(StSiteBVo storedStSiteBVo : stSiteBVoList){
				stSiteSvc.setSiteImgDVo(storedStSiteBVo);
			}
		}
		model.put("mode", "img");
		if(isPlt){
			return LayoutUtil.getJspPath("/st/plt/listSitePltFrm");
		}else if(isFrm){
			model.put("viewFrm", Boolean.TRUE);
			return LayoutUtil.getJspPath("/st/site/listSite","Frm");
		}
		return LayoutUtil.getJspPath("/st/site/listSite");
	}
	
	/** [히든프레임] 사이트 저장(오른쪽 프레임) */
	@RequestMapping(value = "/st/adm/site/transSite", 
			method = RequestMethod.POST)
	public String transSite(HttpServletRequest request,
			ModelMap model) {
		
		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			StRescBVo stRescBVo = stRescSvc.collectStRescBVo(request, null, queryQueue);
			
			if (stRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 조회조건 매핑
			StSiteBVo stSiteBVo = new StSiteBVo();
			VoUtil.bind(request, stSiteBVo);
			stSiteBVo.setCompId(userVo.getCompId());
			stSiteBVo.setSiteNm(stRescBVo.getRescVa());
			
			String siteId = ParamUtil.getRequestParam(request, "siteId", false);
			
			if (siteId == null || siteId.isEmpty()) {
				// ID 생성
				siteId = stCmSvc.createId("ST_SITE_B");
				stSiteBVo.setSiteId(siteId);
				stSiteBVo.setRescId(stRescBVo.getRescId());
				// 등록자, 등록일시
				stSiteBVo.setRegrUid(userVo.getUserUid());
				stSiteBVo.setRegDt("sysdate");
				// 순서조회
				StSiteBVo sortVo = new StSiteBVo();
				sortVo.setInstanceQueryId("com.innobiz.orange.web.st.dao.StSiteBDao.maxStSiteB");
				Integer maxOrdr = commonSvc.queryInt(sortVo);
				if(maxOrdr==null) maxOrdr=0;
				maxOrdr++;
				stSiteBVo.setSortOrdr(maxOrdr);
				// INSERT
				queryQueue.insert(stSiteBVo);
			} else{
				// 수정자, 수정일시
				stSiteBVo.setModrUid(userVo.getUserUid());
				stSiteBVo.setModDt("sysdate");
				// UPDATE
				queryQueue.update(stSiteBVo);
			}
			
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans(null);");
			
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [히든프레임] 사이트 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/st/adm/site/transSiteList")
	public String transSiteList(HttpServletRequest request,			
			@Parameter(name="delList", required=false) String delList,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 카테고리기본(ST_CAT_B) 테이블 VO
			StSiteBVo stSiteBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String siteId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			StSiteImgDVo stSiteImgDVo = null;
			for(i=0;i<delCds.length;i++){
				siteId = delCds[i];
				
				// 이미지 삭제
				stSiteImgDVo = new StSiteImgDVo();
				stSiteImgDVo.setSiteId(siteId);
				queryQueue.delete(stSiteImgDVo);
				
				// 기본 테이블 - 삭제
				stSiteBVo = new StSiteBVo();
				stSiteBVo.setSiteId(siteId);
				stSiteBVo.setCompId(userVo.getCompId());
				queryQueue.delete(stSiteBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 기본(ST_SITE_B) 테이블
			List<StSiteBVo> stSiteBVoList = (List<StSiteBVo>)VoUtil.bindList(request, StSiteBVo.class, new String[]{"valid"});
			size = stSiteBVoList==null ? 0 : stSiteBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				stRescSvc.collectStRescBVoList(request, queryQueue, stSiteBVoList, "valid", "rescId", "siteNm");
			}
			
			String catId = ParamUtil.getRequestParam(request, "catId", false);
			
			for(i=0;i<size;i++){
				stSiteBVo = stSiteBVoList.get(i);
				stSiteBVo.setCompId(userVo.getCompId());
				// 등록자, 등록일시
				stSiteBVo.setRegrUid(userVo.getUserUid());
				stSiteBVo.setRegDt("sysdate");
				// 수정자, 수정일시
				stSiteBVo.setModrUid(userVo.getUserUid());
				stSiteBVo.setModDt("sysdate");
				if(stSiteBVo.getSiteId() == null || stSiteBVo.getSiteId().isEmpty()){
					stSiteBVo.setSiteId(stCmSvc.createId("ST_SITE_B"));
					if(catId!=null && !catId.isEmpty()) stSiteBVo.setCatId(catId);
				}
				
				// 이미지 저장
				if(stSiteBVo.getTmpImgId() != null && !stSiteBVo.getTmpImgId().isEmpty()) stSiteSvc.setSiteImgDVo(request, queryQueue, stSiteBVo.getSiteId(), stSiteBVo.getTmpImgId());
				
				queryQueue.store(stSiteBVo);
			}
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans();");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [팝업] 이미지 선택 */
	@RequestMapping(value = {"/cm/site/viewImagePop", "/st/adm/site/setImagePop"})
	public String setImagePop(HttpServletRequest request,
			@Parameter(name="siteId", required=false) String siteId,
			ModelMap model) throws Exception {
		
		if(siteId!=null && !siteId.isEmpty()){
			StSiteImgDVo stSiteImgDVo = new StSiteImgDVo();
			stSiteImgDVo.setSiteId(siteId);
			stSiteImgDVo = (StSiteImgDVo)commonSvc.queryVo(stSiteImgDVo);
			model.put("stSiteImgDVo", stSiteImgDVo);
		}
		if(request.getRequestURI().startsWith("/st/adm/")) model.put("isView", Boolean.FALSE);
		return LayoutUtil.getJspPath("/st/site/setImagePop");
	}
	
	/** [팝업] 기타 입력(내용) */
	@RequestMapping(value = "/st/adm/site/setEtcPop")
	public String setEtcPop(HttpServletRequest request,
			@Parameter(name="siteId", required=false) String siteId,
			ModelMap model) throws Exception {
		
		if(siteId!=null && !siteId.isEmpty()){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			StSiteBVo stSiteBVo = new StSiteBVo();
			stSiteBVo.setSiteId(siteId);
			stSiteBVo.setCompId(userVo.getCompId());
			stSiteBVo = (StSiteBVo)commonSvc.queryVo(stSiteBVo);
			model.put("stSiteBVo", stSiteBVo);
		}
		
		return LayoutUtil.getJspPath("/st/site/setEtcPop");
	}
	
	/** [AJAX] 사이트 이미지 개별 삭제 */
	@RequestMapping(value = "/st/adm/site/transSiteImgDelAjx")
	public String transSiteImgDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String siteId = (String) object.get("siteId");
			if ( siteId == null || siteId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 이미지 삭제
			StSiteImgDVo stSiteImgDVo = new StSiteImgDVo();
			stSiteImgDVo.setSiteId(siteId);
			commonSvc.delete(stSiteImgDVo);
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
	
}
