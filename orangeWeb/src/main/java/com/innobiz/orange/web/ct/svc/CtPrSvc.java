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
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtPrBVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

@Service
public class CtPrSvc {
	
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
	
	/** 커뮤니티 홍보마당 목록  */
	@SuppressWarnings("unchecked")
	public List<CtPrBVo> getCtPrVoList(CtPrBVo ctPrBVo)throws Exception{
		return (List<CtPrBVo>) commonDao.queryList(ctPrBVo);
	}
	
	/** 게시물 Vo 얻기 */
	public CtPrBVo getCtPrBVo(int bullId)throws Exception{
		CtPrBVo ctPrBVo = new CtPrBVo();
		ctPrBVo.setBullId(bullId);
		
		return (CtPrBVo) commonDao.queryVo(ctPrBVo);
	}
	
	/** 게시예약일 초기화 */
	public void initBullRezvDt(CtPrBVo ctPrBVo){
		if(ctPrBVo.getBullRezvDt() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctPrBVo.setBullRezvYn(new Timestamp(cal.getTimeInMillis()).toString());
		}
	}
	
	/** 게시완료일 초기화 */
	public void initBullExprDt(CtPrBVo ctPrBVo){
		if(ctPrBVo.getBullExprDt() == null){
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.MONTH, baBrdBVo.getRezvPrd());
			cal.add(Calendar.HOUR, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			ctPrBVo.setBullExprDt(new Timestamp(cal.getTimeInMillis()).toString());
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
	
	/** 게시물 저장 */
	public Integer saveCtBullLVo(HttpServletRequest request, String bullId, String bullStatCd,
			CtPrBVo ctPrBVo, QueryQueue queryQueue) throws Exception{
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		CtPrBVo prBVo = new CtPrBVo();
		if(ctPrBVo == null){
			VoUtil.bind(request, prBVo);
				
				// 회사ID
			prBVo.setCompId(userVo.getCompId());
				// 수정자
			prBVo.setModrUid(userVo.getUserUid());
				// 수정일시
			prBVo.setModDt("sysdate");
		}
		
		// 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시)
		prBVo.setBullStatCd(bullStatCd);
		
		if (bullId == null || bullId.isEmpty()) {
			// 게시물ID 생성
			prBVo.setBullId(ctCmSvc.createBullId());
			
			// 기본값 세팅
			prBVo.setCtId(request.getParameter("selectCtPr"));
			prBVo.setReplyGrpId(prBVo.getBullId());
			prBVo.setReplyOrdr(0);
			prBVo.setReplyDpth(0);
			prBVo.setReadCnt(0);
			prBVo.setProsCnt(0);
			prBVo.setConsCnt(0);
			prBVo.setRecmdCnt(0);
			//prBVo.setScre(0); 점수
			
			// 공지사항 게시물
			queryQueue.insert(prBVo);
		} else {
			if(prBVo.getBullRezvDt()!=null && prBVo.getBullRezvDt().isEmpty())
				prBVo.setBullRezvDt(null);
			
			// 게시물(BB_X000X_L) 테이블 - UPDATE
			prBVo.setCtId(request.getParameter("selectCtPr"));
			queryQueue.update(prBVo);
		}
		return prBVo.getBullId();
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
		CtPrBVo ctPrBVo = new CtPrBVo();
		ctPrBVo.setBullId(bullId);
		ctPrBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtPrBDao.updateReadCnt");
		commonDao.update(ctPrBVo);
	}
	
	/** 게시물 삭제 */
	public void deletePr(Integer bullId, QueryQueue queryQueue) throws Exception{
		CtPrBVo ctPrBVo = new CtPrBVo();
		ctPrBVo.setBullId(bullId);
		queryQueue.delete(ctPrBVo);
	}
	
	

}
