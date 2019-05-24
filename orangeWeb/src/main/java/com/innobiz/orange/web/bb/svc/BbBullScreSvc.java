package com.innobiz.orange.web.bb.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaScreHstLVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;

/** 게시물 점수주기 서비스 */
@Service
public class BbBullScreSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;

	/** 점수주기 존재여부 */
	public boolean isScreHstExist(String bullId, String userUid) throws SQLException {
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - COUNT
		BaScreHstLVo baScreHstLVo = new BaScreHstLVo();
		baScreHstLVo.setBullId(Integer.parseInt(bullId));
		baScreHstLVo.setUserUid(userUid);
		return commonDao.count(baScreHstLVo) > 0;
	}

	/** 점수주기 저장 */
	public void insertScreHst(String bullId, String userUid, Integer scre, QueryQueue queryQueue) {
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - INSERT
		BaScreHstLVo baScreHstLVo = new BaScreHstLVo();
		baScreHstLVo.setBullId(Integer.parseInt(bullId));
		baScreHstLVo.setUserUid(userUid);
		baScreHstLVo.setScre(scre);
		queryQueue.insert(baScreHstLVo);
	}

	/** 점수 업데이트 */
	public void updateScre(BaBrdBVo baBrdBVo, Integer bullId, QueryQueue queryQueue) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - UPDATE
		BbBullLVo bbBullLVo = bbBullSvc.newBbBullLVo(baBrdBVo);
		bbBullLVo.setBullId(bullId);
		bbBullLVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.updateScre");
		queryQueue.update(bbBullLVo);
	}

	/** 평균점수 리턴 */
	public Integer getAvgScre(Integer bullId) throws SQLException {
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - SELECT
		BaScreHstLVo baScreHstLVo = new BaScreHstLVo();
		baScreHstLVo.setBullId(bullId);
		baScreHstLVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaScreHstLDao.selectAvgScre");
		return commonDao.queryInt(baScreHstLVo);
	}

	/** 점수 목록 리턴 */
	@SuppressWarnings("unchecked")
	public List<BaScreHstLVo> getBaScreHstLVoList(HttpServletRequest request, Integer bullId) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 점수주기이력(BA_SCRE_HST_L) 테이블 - SELECT
		BaScreHstLVo baScreHstLVo = new BaScreHstLVo();
		baScreHstLVo.setQueryLang(langTypCd);
		baScreHstLVo.setBullId(bullId);
		List<BaScreHstLVo> baScreHstLVoList = (List<BaScreHstLVo>) commonDao.queryList(baScreHstLVo);

		for (BaScreHstLVo screHstVo : baScreHstLVoList) {
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
		
		return baScreHstLVoList;
	}
}
