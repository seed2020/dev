package com.innobiz.orange.web.em.svc;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.em.vo.CmSrchBVo;

/** 검색 인덱싱 서비스 */
@Service
public class EmSrchSvc {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	public CmSrchBVo createVo() throws SQLException{
		CmSrchBVo CmSrchBVo = new CmSrchBVo();
		CmSrchBVo.setSrchNo(commonSvc.nextVal("CM_SRCH_B").toString());
		return CmSrchBVo;
	}
	
}
