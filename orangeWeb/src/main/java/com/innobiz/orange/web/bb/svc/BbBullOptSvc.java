package com.innobiz.orange.web.bb.svc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.vo.BaBullOptDVo;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;

/** 게시옵션 서비스 */
@Service
public class BbBullOptSvc {

	/** 게시옵션 저장 */
	public void saveBullOpt(HttpServletRequest request, Integer bullId, QueryQueue queryQueue) {
		// 게시옵션(BA_BULL_OPT_D) 테이블 - BIND
		BaBullOptDVo baBullOptDVo = new BaBullOptDVo();
		VoUtil.bind(request, baBullOptDVo);
		baBullOptDVo.setBullId(bullId);
		
		// 게시옵션(BA_BULL_OPT_D) 테이블 - INSERT OR UPDATE
		if (baBullOptDVo.getSecuYn() != null || baBullOptDVo.getUgntYn() != null || baBullOptDVo.getNotcYn() != null) {
			if(baBullOptDVo.getSecuYn() == null ) baBullOptDVo.setSecuYn("N");
			if(baBullOptDVo.getUgntYn() == null ) baBullOptDVo.setUgntYn("N");
			if(baBullOptDVo.getNotcYn() == null ) baBullOptDVo.setNotcYn("N");
			queryQueue.store(baBullOptDVo);
		}
		// 게시옵션(BA_BULL_OPT_D) 테이블 - DELETE
		if (baBullOptDVo.getSecuYn() == null && baBullOptDVo.getUgntYn() == null && baBullOptDVo.getNotcYn() == null) {
			queryQueue.delete(baBullOptDVo);
		}
	}

	/** 게시옵션 삭제 */
	public void deleteBullOpt(Integer bullId, QueryQueue queryQueue) {
		// 게시옵션(BA_BULL_OPT_D) 테이블 - DELETE
		BaBullOptDVo baBullOptDVo = new BaBullOptDVo();
		baBullOptDVo.setBullId(bullId);
		queryQueue.delete(baBullOptDVo);
	}

	/** 게시물ID 수정 */
	public void updateBullId(Integer bullId, Integer newBullId, QueryQueue queryQueue) {
		BaBullOptDVo baBullOptDVo = new BaBullOptDVo();
		baBullOptDVo.setBullId(bullId);
		baBullOptDVo.setNewBullId(newBullId);
		queryQueue.update(baBullOptDVo);
	}

}
