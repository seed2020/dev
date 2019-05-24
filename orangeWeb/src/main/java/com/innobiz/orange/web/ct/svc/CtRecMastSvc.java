package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtRecMastBVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

@Service
public class CtRecMastSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "ctfiles";
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 게시물 보내기 저장 */
	public Integer sendCtBullLVo(HttpServletRequest request, String bullId, String selectCtId, 
			String ctFncUid, QueryQueue queryQueue) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCtFncUid(ctFncUid);
		ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
		CtRecMastBVo recMastBVo = new CtRecMastBVo();
		
		recMastBVo.setBullId(Integer.parseInt(bullId));
		recMastBVo = (CtRecMastBVo) commonDao.queryVo(recMastBVo);
		
//		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
//		recMastBVo.setBullStatCd(bullStatCd);
		
		// 게시물ID 생성
		recMastBVo.setBullId(ctCmSvc.createBullId());
		
		// 기본값 세팅
		recMastBVo.setCtId(selectCtId);
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(selectCtId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		recMastBVo.setCtFncUid(ctFncUid);
		recMastBVo.setCtFncPid(ctFncDVo.getCtFncPid());
		recMastBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
		recMastBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
		recMastBVo.setReadCnt(0);
		recMastBVo.setProsCnt(0);
		recMastBVo.setConsCnt(0);
		recMastBVo.setRecmdCnt(0);
		recMastBVo.setReplyOrdr(0);
		recMastBVo.setReplyDpth(0);
		recMastBVo.setRegDt("sysdate");
		// 회사ID
		recMastBVo.setCompId(ctEstbBVo.getCompId());
		// 수정자
		recMastBVo.setModrUid(userVo.getUserUid());
		// 수정일시
		recMastBVo.setModDt("sysdate");
		// 공지사항 게시물
		queryQueue.insert(recMastBVo);
			
		
		return recMastBVo.getBullId();
	}
	
	
	/** 게시물 삭제 */
	public void deleteBoard(Integer bullId, QueryQueue queryQueue) throws Exception{
		CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
		ctRecMastBVo.setBullId(bullId);
		queryQueue.delete(ctRecMastBVo);
	}
	
	/** 게시물 첨부파일 리스트 model에 추가 */
	public void putFileListToModel(String bullId, ModelMap model, String compId)throws Exception{
		List<CommonFileVo> fileVoList = getFileVoList(bullId);
		model.put("fileVoList", fileVoList);
		model.put("filesId", FILES_ID);
		// 확장자 허용제한 
		ptSysSvc.setAttachExtMap(model, compId, "ct");
		
		// 다운로드|문서뷰어 사용여부
		emAttachViewSvc.chkAttachSetup(model, compId);
	}
	
	/** 게시물첨부파일 목록 리턴 (DB) */
	@SuppressWarnings("unchecked")
	public List<CommonFileVo> getFileVoList(String refId) throws Exception {
		if (refId == null) return new ArrayList<CommonFileVo>();
		
		// 게시물첨부파일(CT_FILE_D) 테이블 
		CtFileDVo ctFileDVo = new CtFileDVo();
		ctFileDVo.setRefId(refId);
		return (List<CommonFileVo>) commonDao.queryList(ctFileDVo);
	}
	
	/** 조회이력 저장 */
	public boolean saveReadHst(String bullId, String userUid, String compId) throws Exception{
		
		// 조회이력(CT_VISTR_HST_D) 테이블 - INSERT OR UPDATE
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setCompId(compId);
		ctVistrHstDVo.setCtId(bullId);
		ctVistrHstDVo.setUserUid(userUid);
		
		boolean isFirSt = commonDao.update(ctVistrHstDVo) == 0;
		if(isFirSt){
			commonDao.insert(ctVistrHstDVo);
		}
		return isFirSt;
	}
	
	/** 조회수 증가 */
	public void addReadCnt(Integer bullId)throws SQLException{
		CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
		ctRecMastBVo.setBullId(bullId);
		ctRecMastBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtRecMastBDao.updateReadCnt");
		commonDao.update(ctRecMastBVo);
	}
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			QueryQueue queryQueue) throws Exception {
		return saveCtBullLVo(request, bullId, bullStatCd, null, queryQueue);
	}
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			CtRecMastBVo ctRecMastBVo, QueryQueue queryQueue) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtRecMastBVo recMastBVo = new CtRecMastBVo();
		if (ctRecMastBVo == null) {
			// 게시물(BB_X000X_L) 테이블 - BIND
			//ctAdmNotcBvo = newCtAdmNotcBVo();
			VoUtil.bind(request, recMastBVo);
			
			// 회사ID
		recMastBVo.setCompId(userVo.getCompId());
			// 수정자
		recMastBVo.setModrUid(userVo.getUserUid());
			// 수정일시
		recMastBVo.setModDt("sysdate");
		
		
		} 
//		else {
//			if (ctAdmNotcBvo.getTableName() == null) ctAdmNotcBvo.setTableName(bbTblSvc.getFullTblNm(baBrdBVo.getTblNm()));
//		}
		
		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
		recMastBVo.setBullStatCd(bullStatCd);
		
		if (bullId == null || bullId.isEmpty()) {
			// 게시물ID 생성
			recMastBVo.setBullId(ctCmSvc.createBullId());
			
			// 기본값 세팅
			recMastBVo.setReplyGrpId(recMastBVo.getBullId());
			recMastBVo.setReplyOrdr(0);
			recMastBVo.setReplyDpth(0);
			recMastBVo.setReadCnt(0);
			recMastBVo.setProsCnt(0);
			recMastBVo.setConsCnt(0);
			recMastBVo.setRecmdCnt(0);
			recMastBVo.setCtId(request.getParameter("ctId"));
			recMastBVo.setCtFncId(request.getParameter("bullCtFncId"));
			recMastBVo.setCtFncUid(request.getParameter("bullCtFncUid"));
			recMastBVo.setCtFncPid(request.getParameter("bullCtFncPid"));
			recMastBVo.setCtFncLocStep(request.getParameter("bullCtFncLocStep"));
			recMastBVo.setCtFncOrdr(Integer.parseInt(request.getParameter("bullCtFncOrdr")));
			//ctAdmNotcBvo.setScre(0); 점수
			
			// 공지사항 게시물
			queryQueue.insert(recMastBVo);
			
			//if ("B".equals(bullStatCd)) {
				// 게시판 복수 지정시
				//if (brdIds != null && brdIds.length > 1) {
				//	ctAdmNotcBvo.setCompId(userVo.getCompId());//전사게시판이 아닐 경우 회사ID를 넣기 위해 세팅한다.
					//ctAdmNotcBvo.setDeptId(userVo.getDeptId());//부서게시판일 경우 부서ID를 넣기 위해 세팅한다.
					// 게시판ID 배열로 게시물 등록
			//		insertBullsByBrdIds(request, admNotcBVo, queryQueue);
				//}
			//}
		} else {
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			queryQueue.update(recMastBVo);
		}
		
		// 게시대상 저장
//		if (bullId == null || bullId.isEmpty()) {
//			bbBullTgtSvc.saveBullTgt(request, ctAdmNotcBvo.getBullId(), queryQueue);
//		} else {
//			CtAdmNotcBVo bullVo = getCtAdmNotcBVo(Integer.parseInt(bullId));
			//saveBullTgtWithReplyGrp(request, bullVo, queryQueue);
//		}
		
//		// 게시옵션 저장
//		bbBullOptSvc.saveBullOpt(request, ctAdmNotcBvo.getBullId(), queryQueue);
		
		return recMastBVo.getBullId();
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);

		// 시스템 설정 조회 - 본문 사이즈
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("ct") * 1024;
		model.put("bodySize", bodySize);
	}
	
	/** 게시예약일 초기화 */
	public void initBullRezvDt(CtRecMastBVo ctRecMastBVo){
		if(ctRecMastBVo.getBullRezvDt() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctRecMastBVo.setBullRezvYn(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}
	
	/** 게시완료일 초기화 */
	public void initBullExprDt(CtRecMastBVo ctRecMastBVo){
		if(ctRecMastBVo.getBullExprDt() == null){
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.MONTH, baBrdBVo.getRezvPrd());
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctRecMastBVo.setBullExprDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}
	
	/** 게시물 VO 얻기 */
	public CtRecMastBVo getCtRecMastBVo(int bullId) throws SQLException{
		CtRecMastBVo ctRecMastBVo = new CtRecMastBVo();
		ctRecMastBVo.setBullId(bullId);
		
		return (CtRecMastBVo) commonDao.queryVo(ctRecMastBVo);
	}

}
