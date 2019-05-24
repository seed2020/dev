package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtRescBVo;
import com.innobiz.orange.web.ct.vo.CtSiteBVo;
import com.innobiz.orange.web.ct.vo.CtSiteCatDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;


/** 커뮤니티 쿨사이트*/
@Service
public class CtSiteSvc {
	
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
//	/** 시스템설정 서비스 */
//	@Autowired
//	private PtSysSvc ptSysSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 리소스기본(CT_RESC_B)에 저장할 리소스 데이터를 QueryQueue 에 저장 */
	public void collectCtRescBVo(HttpServletRequest request, QueryQueue queryQueue, List<PtCdBVo> langTypCdList) throws SQLException, CmException{
		// 세션 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtSiteCatDVo ctSiteCatDVo = new CtSiteCatDVo();
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		ctSiteCatDVo.setCompId(ctEstbBVo.getCompId());
		ctSiteCatDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtSiteCatDVo> siteCatList = (List<CtSiteCatDVo>) commonDao.queryList(ctSiteCatDVo);
		
		//기존에 CT_RESC_B 저장되어 있던 해당 커뮤니티 관련 카테고리 리소스 ID 삭제
		if(siteCatList.size() != 0){
			queryQueue.delete(ctSiteCatDVo);
			
			for(CtSiteCatDVo siteCatRescId : siteCatList){
				String catRescId = siteCatRescId.getCatNmRescId();
				CtRescBVo ctRescBVo = new CtRescBVo();
				ctRescBVo.setRescId(catRescId);
				queryQueue.delete(ctRescBVo);
			}
		}
		
		
		// CT_RESC_B에 저장할 리소스 데이터를 QueryQueue 에 저장
		int serialNo = Integer.parseInt(request.getParameter("serialNo"));
		for(int i=0; i<serialNo;i++){
			//리소스 ID 존재여부 TRUE="없음" FALSE="있음"
			boolean rescYn = false;
			//기존 CT_SITE_CAT_D에 대한 QUERY INSERT 존재여부 TRUE="해야함" FALSE="되어 있음"
			boolean insertYn = true;
			String savedRescId = null;
			String rescId = ctCmSvc.createId("CT_RESC_B");
			
			for(PtCdBVo ptCdBVo : langTypCdList){
				String rescVa = request.getParameter("cat"+i+ptCdBVo.getCd());
				
				if(rescVa != null && rescVa != ""){
					if(siteCatList.size() != 0 && insertYn && i<siteCatList.size()){
						for(int j=0; j<siteCatList.size(); j++){
							if(i == j){
								CtSiteCatDVo catDVo = new CtSiteCatDVo();
								catDVo.setCatId(siteCatList.get(j).getCatId());
								catDVo = (CtSiteCatDVo) commonDao.queryVo(catDVo);
								catDVo.setModDt(ctCmntSvc.currentDay());
								catDVo.setModrUid(userVo.getUserUid());
								queryQueue.insert(catDVo);
								
								savedRescId = catDVo.getCatNmRescId();
								insertYn = false;
							}
							
						}
					}
					
					CtRescBVo ctRescBVo = new CtRescBVo();
					if(savedRescId == null){
						ctRescBVo.setRescId(rescId);
						rescYn = true;
					}else{
						ctRescBVo.setRescId(savedRescId);
					}
					
					ctRescBVo.setLangTypCd(ptCdBVo.getCd());
					ctRescBVo.setRescVa(rescVa);
					queryQueue.insert(ctRescBVo);
					
				}
			}
			if(rescYn){
				CtSiteCatDVo catDVo = new CtSiteCatDVo();
				catDVo.setCompId(ctEstbBVo.getCompId());
				catDVo.setCtId(ctId);
				catDVo.setCatId(ctCmSvc.createId("CT_CAT_B"));
				catDVo.setCatNmRescId(rescId);
				catDVo.setRegDt(ctCmntSvc.currentDay());
				catDVo.setRegrUid(userVo.getUserUid());
				queryQueue.insert(catDVo);
			}
			
		}
		
	}
	
	
	/** 커뮤니티 사이트 등록*/
	public void setCtSite(HttpServletRequest request, CtSiteBVo ctSiteBVo, QueryQueue queryQueue) throws Exception{
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		//String ctId = request.getParameter("ctId");
		String siteId = request.getParameter("siteId");
		String catId = request.getParameter("catId");
		if(siteId != ""){
			ctSiteBVo.setCatId(catId);
			ctSiteBVo.setSubj(request.getParameter("siteNm"));
			ctSiteBVo.setCont(request.getParameter("cont"));
			ctSiteBVo.setUrl(request.getParameter("siteUrl"));
			ctSiteBVo.setModrUid(userVo.getUserUid());
			ctSiteBVo.setModDt(ctCmntSvc.currentDay());
			
			queryQueue.update(ctSiteBVo);
		}else{
			CtFncDVo ctFncDVo = new CtFncDVo();
			ctFncDVo.setCtId(ctSiteBVo.getCtId());
			ctFncDVo.setCtFncUid(ctSiteBVo.getCtFncUid());
			ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
			
			ctSiteBVo.setSiteId(ctCmSvc.createId("CT_SITE_B"));
			ctSiteBVo.setCtFncId(ctFncDVo.getCtFncId());
			ctSiteBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
			ctSiteBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
			ctSiteBVo.setCtFncPid(ctFncDVo.getCtFncPid());
			ctSiteBVo.setSubj(request.getParameter("siteNm"));
			ctSiteBVo.setCont(request.getParameter("cont"));
			ctSiteBVo.setCatId(catId);
			ctSiteBVo.setUrl(request.getParameter("siteUrl"));
			ctSiteBVo.setReadCnt(0);
			ctSiteBVo.setRegDt(ctCmntSvc.currentDay());
			ctSiteBVo.setRegrUid(userVo.getUserUid());
			//ctSiteBVo.setEditorTypCd(0);
			
			queryQueue.insert(ctSiteBVo);
		}
		
	}
	
	
	/** 게시물 삭제 */
	public void deleteSite(String siteId, String ctId ,QueryQueue queryQueue) throws Exception{
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		ctSiteBVo.setCtId(ctId);
		ctSiteBVo.setSiteId(siteId);
		queryQueue.delete(ctSiteBVo);
	}
	
	/** 게시물 삭제 */
	public void deleteAdmSite(String siteId, QueryQueue queryQueue) throws Exception{
		CtSiteBVo ctSiteBVo = new CtSiteBVo();
		ctSiteBVo.setSiteId(siteId);
		queryQueue.delete(ctSiteBVo);
	}
	

}
