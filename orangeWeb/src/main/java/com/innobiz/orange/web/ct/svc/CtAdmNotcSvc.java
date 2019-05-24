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
import com.innobiz.orange.web.ct.vo.CtAdmNotcBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

@Service
public class CtAdmNotcSvc {
	
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CtFileSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
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
	
	/** 관리자 커뮤니티 공지사항 목록(관리자, 사용자) */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCtAdmNotcList(HttpServletRequest request, CtAdmNotcBVo ctAdmNotcBVo) throws Exception{
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctAdmNotcBVo.getSchWord();//검색어
		
		// paging
		Integer recodeCount = commonDao.count(ctAdmNotcBVo);
		PersonalUtil.setPaging(request, ctAdmNotcBVo, recodeCount);
		
		// 공지사항 목록
		List<CtAdmNotcBVo> ctAdmNotcList = new ArrayList<CtAdmNotcBVo>();
		ctAdmNotcList = (List<CtAdmNotcBVo>) commonDao.queryList(ctAdmNotcBVo);
		
		Map<String, Object> ctAdmNotcMap;
		List<Map<String, Object>> ctAdmNotcMapList = new ArrayList<Map<String,Object>>();
		
		for(CtAdmNotcBVo storeCtAdmNotcBVo : ctAdmNotcList){
			//CtAdmNotcBVo ctAdmNotcVo = new CtAdmNotcBVo();
			//Map<String, Object> cmntAdmNoctMap = orCmSvc.getUserMap(storeCtAdmNotcBVo.getRegrUid(), langTypCd);
			//storeCtAdmNotcBVo.setRegrNm((String) cmntAdmNoctMap.get("userNm"));
			ctAdmNotcMap = VoUtil.toMap(storeCtAdmNotcBVo, null);
			ctAdmNotcMapList.add(ctAdmNotcMap);
		}
		
		rsltMap.put("recodeCount", recodeCount);
		rsltMap.put("ctAdmNotcMapList", ctAdmNotcMapList);
		
		return rsltMap;
		
	}
	
	/** 게시물 VO 얻기 */
	public CtAdmNotcBVo getCtAdmNotcBVo(int bullId, String langTypCd) throws SQLException{
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setQueryLang(langTypCd);
		ctAdmNotcBVo.setBullId(bullId);
		
		return (CtAdmNotcBVo) commonDao.queryVo(ctAdmNotcBVo);
	}
	
	/** 게시예약일 초기화 */
	public void initBullRezvDt(CtAdmNotcBVo ctAdmNotcBVo){
		if(ctAdmNotcBVo.getBullRezvDt() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctAdmNotcBVo.setBullRezvYn(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}
	
	/** 게시완료일 초기화 */
	public void initBullExprDt(CtAdmNotcBVo ctAdmNotcBVo){
		if(ctAdmNotcBVo.getBullExprDt() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctAdmNotcBVo.setBullExprDt(new Timestamp(cal.getTimeInMillis()).toString());
		}
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
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			QueryQueue queryQueue) throws Exception {
		return saveCtBullLVo(request, bullId, bullStatCd, null, queryQueue);
	}
	
//	/** 게시물 저장 */
//	public Integer saveBbBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
//			CtAdmNotcBVo ctAdmNotcBVo, QueryQueue queryQueue) throws Exception {
//		// brdIds
//		//String brdIdList = request.getParameter("brdIdList");
//		//String[] brdIds = null;
//		//if (brdIdList != null && !"".equals(brdIdList)) {
//		//	brdIds = brdIdList.split(",");
//		//}
//		return saveBbBullLVo(request, bullId, bullStatCd, null, queryQueue);
//	}
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			CtAdmNotcBVo ctAdmNotcBvo, QueryQueue queryQueue) throws Exception {
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", false);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
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
				
		CtAdmNotcBVo admNotcBVo = new CtAdmNotcBVo();
		if (ctAdmNotcBvo == null) {
			// 게시물(BB_X000X_L) 테이블 - BIND
			//ctAdmNotcBvo = newCtAdmNotcBVo();
			VoUtil.bind(request, admNotcBVo);
			
			// 회사ID
		admNotcBVo.setCompId(compId);
			// 수정자
		admNotcBVo.setModrUid(userVo.getUserUid());
			// 수정일시
		admNotcBVo.setModDt("sysdate");
		} 
//		else {
//			if (ctAdmNotcBvo.getTableName() == null) ctAdmNotcBvo.setTableName(bbTblSvc.getFullTblNm(baBrdBVo.getTblNm()));
//		}
		
		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
		admNotcBVo.setBullStatCd(bullStatCd);
		
		if (bullId == null || bullId.isEmpty()) {
			// 게시물ID 생성
			admNotcBVo.setBullId(ctCmSvc.createBullId());
			
			// 기본값 세팅
			admNotcBVo.setReplyGrpId(admNotcBVo.getBullId());
			admNotcBVo.setReplyOrdr(0);
			admNotcBVo.setReplyDpth(0);
			admNotcBVo.setReadCnt(0);
			admNotcBVo.setProsCnt(0);
			admNotcBVo.setConsCnt(0);
			admNotcBVo.setRecmdCnt(0);
			//ctAdmNotcBvo.setScre(0); 점수
			
			// 공지사항 게시물
			queryQueue.insert(admNotcBVo);
			
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
			if(admNotcBVo.getBullRezvDt()!=null && admNotcBVo.getBullRezvDt().isEmpty())
				admNotcBVo.setBullRezvDt(null);
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			queryQueue.update(admNotcBVo);
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
		
		return admNotcBVo.getBullId();
	}
	
//	/** 게시판ID 배열로 게시물 등록 */
//	private void insertBullsByBrdIds(HttpServletRequest request, BbBullLVo bbBullLVo, String[] brdIds, QueryQueue queryQueue) throws Exception {
//		// 세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		//for (String brdId : brdIds) {
//			//if (brdId.equals(bbBullLVo.getBrdId())) continue;
//			
////			// 게시판관리(BA_BRD_B) 테이블 - SELECT
////			BaBrdBVo anotherBrdVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
//			
//			// 게시물(BB_X000X_L) 테이블 - BIND
//			BbBullLVo anotherVo = newBbBullLVo(anotherBrdVo);
//			BeanUtils.copyProperties(bbBullLVo, anotherVo, new String[] { "bullId", "brdId", "replyGrpId" });
//			
//			// 게시물ID 생성
//			anotherVo.setBullId(bbCmSvc.createBullId());
//			// 게시판ID
//			anotherVo.setBrdId(brdId);
//			// 기본값 세팅
//			anotherVo.setReplyGrpId(anotherVo.getBullId());
//			
//			if("Y".equals(anotherBrdVo.getDeptBrdYn())) {// 대상 게시판이 부서게시판이면...
//				anotherVo.setDeptId(bbBullLVo.getDeptId());
//			}
//			
//			// 게시물(BB_X000X_L) 테이블 - INSERT
//			queryQueue.insert(anotherVo);
//			
//			// 게시옵션 저장
//			bbBullOptSvc.saveBullOpt(request, anotherVo.getBullId(), queryQueue);
//		//}
//	}
	
//	/** 게시판ID 배열로 게시물 등록 */
//	private void insertBullsByBrdIds(HttpServletRequest request, CtAdmNotcBVo ctAdmNotcBVo, QueryQueue queryQueue) throws Exception {
//		// 세션의 언어코드
//		String langTypCd = LoginSession.getLangTypCd(request);
//		
//		//for (String brdId : brdIds) {
//			//if (brdId.equals(ctAdmNotcBVo.getBrdId())) continue;
//			
//			// 게시판관리(BA_BRD_B) 테이블 - SELECT
//			//BaBrdBVo anotherBrdVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
//			
//			// 게시물(BB_X000X_L) 테이블 - BIND
//			CtAdmNotcBVo anotherVo = new CtAdmNotcBVo();
//			BeanUtils.copyProperties(ctAdmNotcBVo, anotherVo, new String[] { "bullId", "replyGrpId" });
//			
//			// 게시물ID 생성
//			anotherVo.setBullId(ctCmSvc.createBullId());
//			// 게시판ID
//			//anotherVo.setBrdId(brdId);
//			// 기본값 세팅
//			anotherVo.setReplyGrpId(anotherVo.getBullId());
//			
//			//if("Y".equals(anotherBrdVo.getDeptBrdYn())) {// 대상 게시판이 부서게시판이면...
//			//	anotherVo.setDeptId(ctAdmNotcBVo.getDeptId());
//			//}
//			
//			// 게시물(BB_X000X_L) 테이블 - INSERT
//			queryQueue.insert(anotherVo);
//			
//			// 게시옵션 저장
//			//bbBullOptSvc.saveBullOpt(request, anotherVo.getBullId(), queryQueue);
//		//}
//	}
	
	/** 게시물 목록 리턴 */
	@SuppressWarnings("unchecked")
	public List<CtAdmNotcBVo> getCtAdmNotcVoList(CtAdmNotcBVo ctAdmNotcBVo) throws Exception{
		return (List<CtAdmNotcBVo>) commonDao.queryList(ctAdmNotcBVo);
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
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setBullId(bullId);
		ctAdmNotcBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtAdmNotcBDao.updateReadCnt");
		commonDao.update(ctAdmNotcBVo);
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
	
	/** 게시물 삭제 */
	public void deleteAdmNotc(Integer bullId, QueryQueue queryQueue) throws Exception{
		CtAdmNotcBVo ctAdmNotcBVo = new CtAdmNotcBVo();
		ctAdmNotcBVo.setBullId(bullId);
		queryQueue.delete(ctAdmNotcBVo);
	}

}
