package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtBullMastCmdDVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtRecmdHstLVo;
import com.innobiz.orange.web.ct.vo.CtScreHstLVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

@Service
public class CtBullMastSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 추천이력 서비스 */
	@Autowired
	private CtRecmdHstSvc ctRecmdHstSvc;
	
	/** 점수이력 서비스 */
	@Autowired
	private CtScreHstSvc ctScreHstSvc;
	
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
		CtBullMastBVo bullMastBVo = new CtBullMastBVo();
		
		bullMastBVo.setBullId(Integer.parseInt(bullId));
		bullMastBVo.setWithLob(true);
		bullMastBVo = (CtBullMastBVo) commonDao.queryVo(bullMastBVo);
		
//		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
//		recMastBVo.setBullStatCd(bullStatCd);
		
		// 게시물ID 생성
		bullMastBVo.setBullId(ctCmSvc.createBullId());
		
		// 기본값 세팅
		bullMastBVo.setCtId(selectCtId);
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(selectCtId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		bullMastBVo.setCtFncUid(ctFncUid);
		bullMastBVo.setCtFncPid(ctFncDVo.getCtFncPid());
		bullMastBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
		bullMastBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
		bullMastBVo.setReadCnt(0);
		bullMastBVo.setProsCnt(0);
		bullMastBVo.setConsCnt(0);
		bullMastBVo.setRecmdCnt(0);
		bullMastBVo.setReplyOrdr(0);
		bullMastBVo.setReplyDpth(0);
		bullMastBVo.setReplyGrpId(bullMastBVo.getBullId());
		bullMastBVo.setRegDt("sysdate");
		// 회사ID
		bullMastBVo.setCompId(ctEstbBVo.getCompId());
		// 수정자
		bullMastBVo.setModrUid(userVo.getUserUid());
		// 수정일시
		bullMastBVo.setModDt("sysdate");
		// 공지사항 게시물
		queryQueue.insert(bullMastBVo);
			
		
		return bullMastBVo.getBullId();
	}
	
	
	/** 기타(점수, 추천, 찬반투표) 참가여부 model에 추가 */
	public void putEtcToModel(HttpServletRequest request, String bullId, ModelMap model) throws SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		boolean screHstExist = ctScreHstSvc.isScreHstExist(bullId, userVo.getUserUid());
		model.put("screHstExist", screHstExist);
		boolean recmdHstExist = ctRecmdHstSvc.isRecmdHstExist(bullId, userVo.getUserUid());
		model.put("recmdHstExist", recmdHstExist);
	}
	
	
	/** 커뮤니티 목록 개수 */
	public Integer getCtCmntListCnt(CtEstbBVo ctEstbBVo) throws Exception{
		return commonDao.count(ctEstbBVo);
	}
	
	/** 커뮤니티 목록 */
	@SuppressWarnings("unchecked")
	public List<CtEstbBVo> getCtCmntList(CtEstbBVo ctEstbBVo) throws Exception{
		return (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
	}
	
	/** 답변순서 구하기 */
	private int getReplyOrdr(CtBullMastBVo parentBullMastVo, QueryQueue queryQueue)
			throws SQLException {
		// 부모 게시글의 바로 아래 동생 게시글 답변순서
		CtBullMastBVo ordrBullVo = new CtBullMastBVo();
		ordrBullVo.setReplyGrpId(parentBullMastVo.getReplyGrpId());
		ordrBullVo.setReplyOrdr(parentBullMastVo.getReplyOrdr());
		ordrBullVo.setReplyDpth(parentBullMastVo.getReplyDpth());
		ordrBullVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.selectMinReplyOrdr");
		Integer minReplyOrdr = commonDao.queryInt(ordrBullVo);
		
		// 부모 게시글이 동생이 있다면 답변글이 동생 자리로 가기 위해 그 아래 모든 게시글들을 한칸씩 아래로 내린다.
		if (minReplyOrdr != null) {
			// 답변순서 업데이트
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			CtBullMastBVo updateBullVo = new CtBullMastBVo();
			updateBullVo.setReplyGrpId(parentBullMastVo.getReplyGrpId());
			updateBullVo.setReplyOrdr(minReplyOrdr);
			updateBullVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.updateReplyOrdr");
			queryQueue.update(updateBullVo);
			
			return minReplyOrdr;
		} else {
			ordrBullVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.selectMaxReplyOrdr");
			Integer maxReplyOrdr = commonDao.queryInt(ordrBullVo);
			
			return maxReplyOrdr + 1;
		}
	}
	
	/** 게시물 답변 저장 */
	public Integer saveReplyBbBullLVo(HttpServletRequest request, String bullStatCd, String bullPid, QueryQueue queryQueue) throws Exception {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		String bullId = ParamUtil.getRequestParam(request, "bullId", false);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtBullMastBVo bullMastBVo = new CtBullMastBVo();
		VoUtil.bind(request, bullMastBVo);
		
		// 수정자
		bullMastBVo.setModrUid(userVo.getUserUid());
		// 수정일시
		bullMastBVo.setModDt("sysdate");
				
		if(bullId==null || bullId.isEmpty()){
			// 부모 게시글 조회
			CtBullMastBVo parentBullMastVo = getCtBullMastBVo(Integer.parseInt(bullPid), langTypCd, false);
					
			// 회사ID
			bullMastBVo.setCompId(parentBullMastVo.getCompId());
			
			// 부모게시물ID
			bullMastBVo.setBullPid(Integer.parseInt(bullPid));
			
			// 답변그룹ID, 답변단계
			bullMastBVo.setReplyGrpId(parentBullMastVo.getReplyGrpId());
			bullMastBVo.setReplyDpth(parentBullMastVo.getReplyDpth() + 1);
			
			// 답변순서
			bullMastBVo.setReplyOrdr(getReplyOrdr(parentBullMastVo, queryQueue));
			
			bullMastBVo.setCtFncId(parentBullMastVo.getCtFncId());
			bullMastBVo.setCtFncUid(parentBullMastVo.getCtFncUid());
			bullMastBVo.setCtFncPid(parentBullMastVo.getCtFncPid());
			bullMastBVo.setCtFncLocStep(parentBullMastVo.getCtFncLocStep());
			bullMastBVo.setCtFncOrdr(parentBullMastVo.getCtFncOrdr());
			
			bullMastBVo.setReadCnt(0);
			bullMastBVo.setProsCnt(0);
			bullMastBVo.setConsCnt(0);
			bullMastBVo.setRecmdCnt(0);
			
			//답변글 만료기간 삽입(부모게시글) - 게시완료일이 있을경우에만 적용
			String bullExprDt=parentBullMastVo.getBullExprDt();
			if(bullExprDt != null && !"".equals(bullExprDt)){
				if(bullExprDt.length()>19){
					bullExprDt=bullExprDt.substring(0, 19);
				}
				bullMastBVo.setBullExprDt(bullExprDt);
			}
			
			// 게시물ID 생성
			bullMastBVo.setBullId(ctCmSvc.createBullId());
			
			queryQueue.insert(bullMastBVo);
		}else{
			if(bullMastBVo.getBullRezvDt()!=null && bullMastBVo.getBullRezvDt().isEmpty())
				bullMastBVo.setBullRezvDt(null);
			
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			queryQueue.update(bullMastBVo);
		}
		
		return bullMastBVo.getBullId();
	}
	
	/** 게시물 삭제 */
	public void deleteBoard(Integer bullId, QueryQueue queryQueue) throws Exception{
		//해당 게시글 한줄답변 삭제
		CtBullMastCmdDVo ctBullMastCmdDVo = new CtBullMastCmdDVo();
		ctBullMastCmdDVo.setBullId(bullId);
		queryQueue.delete(ctBullMastCmdDVo);
		
		//해당 게시글 조회이력 삭제
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setCtId(String.valueOf(bullId));
		queryQueue.delete(ctVistrHstDVo);
		
		//해당 게시글 추천이력 삭제
		CtRecmdHstLVo ctRecmdHstLVo = new CtRecmdHstLVo();
		ctBullMastCmdDVo.setBullId(bullId);
		queryQueue.delete(ctRecmdHstLVo);
		
		//해당 게시글 점수이력 삭제
		CtScreHstLVo ctScreHstLVo = new CtScreHstLVo();
		ctScreHstLVo.setBullId(bullId);
		queryQueue.delete(ctScreHstLVo);
		
		//해당 게시글 삭제
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setBullId(bullId);
		queryQueue.delete(ctBullMastBVo);
	}
	
	/** 한줄 답변 게시물 삭제 */
	public void deleteCmdBoard(Integer bullId, QueryQueue queryQueue) throws Exception{
		CtBullMastCmdDVo ctBullMastCmdBVo = new CtBullMastCmdDVo();
		ctBullMastCmdBVo.setBullId(bullId);
		queryQueue.delete(ctBullMastCmdBVo);
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
	
	/** 조회수 증가 */
	public void addReadCnt(Integer bullId)throws SQLException{
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setBullId(bullId);
		ctBullMastBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.updateReadCnt");
		commonDao.update(ctBullMastBVo);
	}
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			QueryQueue queryQueue) throws Exception {
		return saveCtBullLVo(request, bullId, bullStatCd, null, queryQueue);
	}
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			CtBullMastBVo ctBullMastBVo, QueryQueue queryQueue) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtBullMastBVo bullMastBVo = new CtBullMastBVo();
		if (ctBullMastBVo == null) {
			// 게시물(BB_X000X_L) 테이블 - BIND
			//ctAdmNotcBvo = newCtAdmNotcBVo();
			VoUtil.bind(request, bullMastBVo);
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", false);
		
		String compId=userVo.getCompId();
		if(ctId!=null && !ctId.isEmpty()){
			CtEstbBVo ctEstbBVo = new CtEstbBVo();
			ctEstbBVo.setCtId(ctId);
			
			ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
			if(ctEstbBVo==null){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			compId=ctEstbBVo.getCompId();
		}
			
			// 회사ID
		bullMastBVo.setCompId(compId);
			// 수정자
		bullMastBVo.setModrUid(userVo.getUserUid());
			// 수정일시
		bullMastBVo.setModDt("sysdate");
		
		
		} 
//		else {
//			if (ctAdmNotcBvo.getTableName() == null) ctAdmNotcBvo.setTableName(bbTblSvc.getFullTblNm(baBrdBVo.getTblNm()));
//		}
		
		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
		bullMastBVo.setBullStatCd(bullStatCd);
		
		if (bullId == null || bullId.isEmpty()) {
			// 게시물ID 생성
			bullMastBVo.setBullId(ctCmSvc.createBullId());
			
			// 기본값 세팅
			bullMastBVo.setReplyGrpId(bullMastBVo.getBullId());
			bullMastBVo.setReplyOrdr(0);
			bullMastBVo.setReplyDpth(0);
			bullMastBVo.setReadCnt(0);
			bullMastBVo.setProsCnt(0);
			bullMastBVo.setConsCnt(0);
			bullMastBVo.setRecmdCnt(0);
			bullMastBVo.setCtId(request.getParameter("ctId"));
			bullMastBVo.setCtFncId(request.getParameter("bullCtFncId"));
			bullMastBVo.setCtFncUid(request.getParameter("bullCtFncUid"));
			bullMastBVo.setCtFncPid(request.getParameter("bullCtFncPid"));
			bullMastBVo.setCtFncLocStep(request.getParameter("bullCtFncLocStep"));
			bullMastBVo.setCtFncOrdr(Integer.parseInt(request.getParameter("bullCtFncOrdr")));
			bullMastBVo.setScre(0);
			
			// 공지사항 게시물
			queryQueue.insert(bullMastBVo);
			
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
			if(bullMastBVo.getBullRezvDt()!=null && bullMastBVo.getBullRezvDt().isEmpty())
				bullMastBVo.setBullRezvDt(null);
			
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			queryQueue.update(bullMastBVo);
			
			//답변글 만료기간 수정(원본글일 경우) - 게시완료일이 있을경우에만 적용
			if(bullMastBVo.getBullExprDt() != null && !"".equals(bullMastBVo.getBullExprDt()) && ( bullMastBVo.getBullPid() == null || "".equals(bullMastBVo.getBullPid()) )){
				CtBullMastBVo updateCtBullMastBVo = new CtBullMastBVo();
				
				//답변글 만료기간 삽입(부모게시글) - 게시완료일이 있을경우에만 적용
				String bullExprDt=bullMastBVo.getBullExprDt();
				if(bullExprDt != null && !"".equals(bullExprDt) && bullExprDt.length()>19){
					bullExprDt=bullExprDt.substring(0, 19);
				}
				
				updateCtBullMastBVo.setBullExprDt(bullExprDt);
				updateCtBullMastBVo.setReplyGrpId(bullMastBVo.getBullId());
				queryQueue.update(updateCtBullMastBVo);
			}

			
			
		}
		
		return bullMastBVo.getBullId();
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
	public void initBullRezvDt(CtBullMastBVo ctBullMastBVo){
		if(ctBullMastBVo.getBullRezvDt() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctBullMastBVo.setBullRezvYn(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}
	
	/** 게시완료일 초기화 */
	public void initBullExprDt(CtBullMastBVo ctBullMastBVo){
		if(ctBullMastBVo.getBullExprDt() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctBullMastBVo.setBullExprDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}
	
	/** 게시물 VO 얻기 */
	public CtBullMastBVo getCtBullMastBVo(int bullId, String langTypCd) throws SQLException{
		return getCtBullMastBVo(bullId, langTypCd, true);
	}
	
	/** 게시물 VO 얻기 */
	public CtBullMastBVo getCtBullMastBVo(int bullId, String langTypCd, boolean withLob) throws SQLException{
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setQueryLang(langTypCd);
		ctBullMastBVo.setBullId(bullId);
		ctBullMastBVo.setWithLob(withLob);
		return (CtBullMastBVo) commonDao.queryVo(ctBullMastBVo);
	}
	
	
	/** 나의 커뮤니티 목록 조회 */
	public Map<String, Object> getMyCtMapList(HttpServletRequest request, CtEstbBVo ctEstbBVo) throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctEstbBVo.getSchWord();//검색어
		
		Integer recodeCount = this.getCtCmntListCnt(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		//목록 조회
		List<CtEstbBVo> myCtVoList = this.getCtCmntList(ctEstbBVo);
		
		Map<String, Object> myCtMap;
		List<Map<String, Object>> myCtMapList = new ArrayList<Map<String,Object>>();
		for(CtEstbBVo storeCtEstbBVo : myCtVoList){
			Map<String, Object> myCtCmntMap = orCmSvc.getUserMap(storeCtEstbBVo.getMastUid(), langTypCd);
			if(myCtCmntMap==null) continue;
			storeCtEstbBVo.setMastNm((String) myCtCmntMap.get("userNm"));
			myCtMap = VoUtil.toMap(storeCtEstbBVo, null);
			myCtMapList.add(myCtMap);
		}
		rsltMap.put("myCtMapList", myCtMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}

	
}
