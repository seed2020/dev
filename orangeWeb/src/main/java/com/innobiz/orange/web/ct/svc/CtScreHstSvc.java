package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtScreHstLVo;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;

/** 게시물 점수주기 서비스 */
@Service
public class CtScreHstSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 게시물 서비스 */
	@Resource(name = "ctBullMastSvc")
	private CtBullMastSvc ctBullMastSvc;
	
	/** 점수주기 존재여부 */
	public boolean isScreHstExist(String bullId, String userUid) throws SQLException {
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - COUNT
		CtScreHstLVo ctScreHstLVo = new CtScreHstLVo();
		ctScreHstLVo.setBullId(Integer.parseInt(bullId));
		ctScreHstLVo.setUserUid(userUid);
		return commonDao.count(ctScreHstLVo) > 0;
	}
	
	/** 점수주기 저장 */
	public void insertScreHst(String bullId, String userUid, Integer scre, QueryQueue queryQueue) throws Exception {
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - INSERT
		CtScreHstLVo ctScreHstLVo = new CtScreHstLVo();
		ctScreHstLVo.setBullId(Integer.parseInt(bullId));
		ctScreHstLVo.setUserUid(userUid);
		ctScreHstLVo.setScre(scre);
		queryQueue.insert(ctScreHstLVo);
	}
	
	/** 점수전체  해당 게시글 전체 삭제 */
	public void deleteScreHst(Integer bullId, QueryQueue queryQueue) throws Exception{
		CtScreHstLVo ctScreHstLVo = new CtScreHstLVo();
		ctScreHstLVo.setBullId(bullId);
		queryQueue.delete(ctScreHstLVo);
	}
	
	/** 점수 업데이트 */
	public void updateScre(Integer bullId, QueryQueue queryQueue) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - UPDATE
		
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setBullId(bullId);
		ctBullMastBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.updateScre");
		queryQueue.update(ctBullMastBVo);
	}
	
	/** 평균점수 리턴 */
	public Integer getAvgScre(Integer bullId) throws SQLException {
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - SELECT
		CtScreHstLVo ctScreHstLVo = new CtScreHstLVo();
		ctScreHstLVo.setBullId(bullId);
		ctScreHstLVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtScreHstLDao.selectAvgScre");
		return commonDao.queryInt(ctScreHstLVo);
	}
	
	/** 점수 목록 리턴 */
	@SuppressWarnings("unchecked")
	public List<CtScreHstLVo> getCtScreHstLVoList(HttpServletRequest request, Integer bullId) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 점수주기이력(CT_SCRE_HST_L) 테이블 - SELECT
		CtScreHstLVo ctScreHstLVo = new CtScreHstLVo();
		ctScreHstLVo.setQueryLang(langTypCd);
		ctScreHstLVo.setBullId(bullId);
		List<CtScreHstLVo> ctScreHstLVoList = (List<CtScreHstLVo>) commonDao.queryList(ctScreHstLVo);

		for (CtScreHstLVo screHstVo : ctScreHstLVoList) {
			// 사용자기본(OR_USER_B) 테이블 - SELECT
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setQueryLang(langTypCd);
			orUserBVo.setUserUid(screHstVo.getUserUid());
			orUserBVo = (OrUserBVo) commonDao.queryVo(orUserBVo);
			screHstVo.setOrUserBVo(orUserBVo);

			// 원직자기본(OR_ODUR_B) 테이블 - SELECT
			OrOdurBVo orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
			orOdurBVo = (OrOdurBVo) commonDao.queryVo(orOdurBVo);
			screHstVo.setOrOdurBVo(orOdurBVo);
		}
		
		return ctScreHstLVoList;
	}
	
	

}
