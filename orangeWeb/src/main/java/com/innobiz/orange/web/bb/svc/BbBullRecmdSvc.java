package com.innobiz.orange.web.bb.svc;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaRecmdHstLVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;

/** 게시물 추천 서비스 */
@Service
public class BbBullRecmdSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;

	/** 추천이력 존재여부 */
	public boolean isRecmdHstExist(String bullId, String userUid) throws SQLException {
		// 추천이력(BA_RECMD_HST_L) 테이블 - COUNT
		BaRecmdHstLVo baRecmdHstLVo = new BaRecmdHstLVo();
		baRecmdHstLVo.setBullId(Integer.parseInt(bullId));
		baRecmdHstLVo.setUserUid(userUid);
		return commonDao.count(baRecmdHstLVo) > 0;
	}

	/** 추천이력 저장 */
	public void insertRecmdHst(String bullId, String userUid, QueryQueue queryQueue) {
		// 추천이력(BA_RECMD_HST_L) 테이블 - INSERT
		BaRecmdHstLVo baRecmdHstLVo = new BaRecmdHstLVo();
		baRecmdHstLVo.setBullId(Integer.parseInt(bullId));
		baRecmdHstLVo.setUserUid(userUid);
		queryQueue.insert(baRecmdHstLVo);
	}

	/** 추천수 증가 */
	public void addRecmdCnt(BaBrdBVo baBrdBVo, Integer bullId, QueryQueue queryQueue) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - UPDATE
		BbBullLVo bbBullLVo = bbBullSvc.newBbBullLVo(baBrdBVo);
		bbBullLVo.setBullId(bullId);
		bbBullLVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BbBullLDao.updateRecmdCnt");
		queryQueue.update(bbBullLVo);
	}
}
