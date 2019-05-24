package com.innobiz.orange.web.bb.svc;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaFavotHstLVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;

/** 게시물 찬반투표 서비스 */
@Service
public class BbBullFavotSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;

	/** 찬반투표 존재여부 */
	public boolean isFavotHstExist(String bullId, String userUid) throws SQLException {
		// 찬반투표이력(BA_FAVOT_HST_L) 테이블 - COUNT
		BaFavotHstLVo baFavotHstLVo = new BaFavotHstLVo();
		baFavotHstLVo.setBullId(Integer.parseInt(bullId));
		baFavotHstLVo.setUserUid(userUid);
		return commonDao.count(baFavotHstLVo) > 0;
	}

	/** 찬반투표 저장 */
	public void insertFavotHst(String bullId, String userUid, String favotVa, QueryQueue queryQueue) {
		// 찬반투표이력(BA_FAVOT_HST_L) 테이블 - INSERT
		BaFavotHstLVo baFavotHstLVo = new BaFavotHstLVo();
		baFavotHstLVo.setBullId(Integer.parseInt(bullId));
		baFavotHstLVo.setUserUid(userUid);
		baFavotHstLVo.setFavotVa(favotVa);
		queryQueue.insert(baFavotHstLVo);
	}

	/** 찬성수/반대수 증가 */
	public void addFavotCnt(BaBrdBVo baBrdBVo, Integer bullId, String favotVa, QueryQueue queryQueue) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - UPDATE
		BbBullLVo bbBullLVo = bbBullSvc.newBbBullLVo(baBrdBVo);
		bbBullLVo.setBullId(bullId);
		if ("F".equals(favotVa)) {
			bbBullLVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.updateProsCnt");
		} else if ("A".equals(favotVa)) {
			bbBullLVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.updateConsCnt");
		} else {
			return;
		}
		queryQueue.update(bbBullLVo);
	}
}
