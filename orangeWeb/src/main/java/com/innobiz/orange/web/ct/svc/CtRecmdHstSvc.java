package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtRecmdHstLVo;

/** 게시물 추천 서비스 */
@Service
public class CtRecmdHstSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 게시물 서비스 */
	@Resource(name = "ctBullMastSvc")
	private CtBullMastSvc ctBullMastSvc;
	
	/** 추천이력 존재여부 */
	public boolean isRecmdHstExist(String bullId, String userUid) throws SQLException {
		// 추천이력(BA_RECMD_HST_L) 테이블 - COUNT
		CtRecmdHstLVo ctRecmdHstLVo = new CtRecmdHstLVo();
		ctRecmdHstLVo.setBullId(Integer.parseInt(bullId));
		ctRecmdHstLVo.setUserUid(userUid);
		return commonDao.count(ctRecmdHstLVo) > 0;
	}
	
	/** 추천이력 저장 */
	public void insertRecmdHst(String bullId, String userUid, QueryQueue queryQueue) throws Exception {
		// 추천이력(BA_RECMD_HST_L) 테이블 - INSERT
		CtRecmdHstLVo ctRecmdHstLVo = new CtRecmdHstLVo();
		ctRecmdHstLVo.setBullId(Integer.parseInt(bullId));
		ctRecmdHstLVo.setUserUid(userUid);
		queryQueue.insert(ctRecmdHstLVo);
	}
	
	/** 추천전체 해당 게시글 전체 삭제 */
	public void deleteRecmdHst(Integer bullId, QueryQueue queryQueue) throws Exception{
		CtRecmdHstLVo ctRecmdHstLVo = new CtRecmdHstLVo();
		ctRecmdHstLVo.setBullId(bullId);
		queryQueue.delete(ctRecmdHstLVo);
	}
	
	/** 추천수 증가 */
	public void addRecmdCnt(Integer bullId, QueryQueue queryQueue) throws SQLException {
		// 게시물(BB_X000X_L) 테이블 - UPDATE
		CtBullMastBVo ctBullMastBVo = new CtBullMastBVo();
		ctBullMastBVo.setBullId(bullId);
		ctBullMastBVo.setInstanceQueryId("com.innobiz.orange.web.ct.dao.CtBullMastBDao.updateRecmdCnt");
		queryQueue.update(ctBullMastBVo);
	}

}
