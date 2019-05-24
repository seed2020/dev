package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtDebrBVo;
import com.innobiz.orange.web.ct.vo.CtDebrOpinDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 토론실 */
@Service
public class CtDebrSvc {
	
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
	
	/** 조회수 증가 */
	public void addReadCnt(Integer opinOrdr)throws SQLException{
		CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
		ctDebrOpinDVo.setOpinOrdr(opinOrdr);
		ctDebrOpinDVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtDebrOpinDDao.updateReadCnt");
		commonDao.update(ctDebrOpinDVo);
	}
	
	/** 토론 게시물 삭제 */
	public void deleteDebr(String debrId, QueryQueue queryQueue) throws Exception{
		CtDebrBVo ctDebrBVo = new CtDebrBVo();
		ctDebrBVo.setDebrId(debrId);
		queryQueue.delete(ctDebrBVo);
	}
	
	/** 토론 게시물 내용 삭제 */
	public void deleteDebrOpin(String debrId, QueryQueue queryQueue) throws Exception{
		CtDebrOpinDVo ctDebrOpinDVo = new CtDebrOpinDVo();
		ctDebrOpinDVo.setDebrId(debrId);
		queryQueue.delete(ctDebrOpinDVo);
	}
	
	/** 조회이력 저장 */
	public boolean saveReadHst(String opinOrdr, String userUid, String compId) throws Exception{
		
		// 조회이력(CT_VISTR_HST_D) 테이블 - INSERT OR UPDATE
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setCompId(compId);
		ctVistrHstDVo.setCtId(opinOrdr);
		ctVistrHstDVo.setUserUid(userUid);
		
		boolean isFirSt = commonDao.update(ctVistrHstDVo) == 0;
		if(isFirSt){
			commonDao.insert(ctVistrHstDVo);
		}
		return isFirSt;
	}
	
	/** 의견 저장 */
	public void setCtOpin(HttpServletRequest request, CtDebrOpinDVo ctDebrOpinDVo, QueryQueue queryQueue) throws Exception{
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String ctId = request.getParameter("ctId");
		String debrId = request.getParameter("debrId");
		String opinOrdr = request.getParameter("opinOrdr");
		String compId=ctDebrOpinDVo.getCompId();
		if(opinOrdr != null && !opinOrdr.isEmpty()){
			ctDebrOpinDVo = (CtDebrOpinDVo) commonDao.queryVo(ctDebrOpinDVo);
		}
		if(opinOrdr != "" && opinOrdr != null){
			ctDebrOpinDVo.setSubj(request.getParameter("opinSubj"));
			ctDebrOpinDVo.setOpin(request.getParameter("opin"));
			ctDebrOpinDVo.setProsConsCd(request.getParameter("fna"));
			ctDebrOpinDVo.setModrUid(userVo.getUserUid());
			ctDebrOpinDVo.setModDt(ctCmntSvc.currentDay());
			
			queryQueue.update(ctDebrOpinDVo);
		}else{
			ctDebrOpinDVo = new CtDebrOpinDVo();
			ctDebrOpinDVo.setCompId(compId);
			ctDebrOpinDVo.setCtId(ctId);
			ctDebrOpinDVo.setDebrId(debrId);
			ctDebrOpinDVo.setOpinOrdr(ctCmSvc.createOpinOrdr("CT_DEBR_OPIN_D"));
			ctDebrOpinDVo.setQueryLang(langTypCd);
			ctDebrOpinDVo.setSubj(request.getParameter("opinSubj"));
			ctDebrOpinDVo.setOpin(request.getParameter("opin"));
			ctDebrOpinDVo.setReadCnt(0);
			ctDebrOpinDVo.setProsConsCd(request.getParameter("fna"));
			ctDebrOpinDVo.setRegrUid(userVo.getUserUid());
			ctDebrOpinDVo.setRegDt(ctCmntSvc.currentDay());
			
			queryQueue.insert(ctDebrOpinDVo);
		}
		
	}
	
	/** 토론실 저장 */
	public void setCtDebr(HttpServletRequest request, CtDebrBVo ctDebrBVo, QueryQueue queryQueue)throws Exception{
		String debrId = request.getParameter("debrId");
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		if(debrId != ""){
			ctDebrBVo.setDebrId(debrId);
			ctDebrBVo.setSubj(request.getParameter("topc"));
			ctDebrBVo.setEstbItnt(request.getParameter("itnt"));
			ctDebrBVo.setModrUid(userVo.getUserUid());
			ctDebrBVo.setModDt(ctCmntSvc.currentDay());
			
			queryQueue.update(ctDebrBVo);
			
		}else{
			CtFncDVo ctFncDVo = new CtFncDVo();
			ctFncDVo.setCtId(ctDebrBVo.getCtId());
			ctFncDVo.setCtFncUid(ctDebrBVo.getCtFncUid());
			ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
			
			ctDebrBVo.setDebrId(ctCmSvc.createId("CT_DEBR_B"));
			ctDebrBVo.setCtFncId(ctFncDVo.getCtFncId());
			ctDebrBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
			ctDebrBVo.setSubj(request.getParameter("topc"));
			ctDebrBVo.setEstbItnt(request.getParameter("itnt"));
			ctDebrBVo.setSitn(2);
			ctDebrBVo.setFinYn("N");
			ctDebrBVo.setRegDt(ctCmntSvc.currentDay());
			ctDebrBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
			ctDebrBVo.setCtFncUid(ctFncDVo.getCtFncUid());
			ctDebrBVo.setCtFncPid(ctFncDVo.getCtFncPid());
			
			queryQueue.insert(ctDebrBVo);
		}
		
	}
	

}
