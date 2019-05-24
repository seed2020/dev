package com.innobiz.orange.web.cm.svc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.dao.CommonSeqDao;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;

/** 공통 DB 처리용 서비스 */
public class CommonSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** CommonSeqDao */
	private CommonSeqDao commonSeqDao;
	
	/** dbms */
	private boolean checkDbms = false;
	private boolean isMssql   = false;
//	private boolean isOracle  = false;
	private boolean isMysql   = false;
	
	/** CommonDao 세팅 */
	public void setCommonDao(CommonDao commonDao){
		this.commonDao = commonDao;
	}
	
	/** CommonDao 세팅 */
	public void setCommonSeqDao(CommonSeqDao commonSeqDao){
		this.commonSeqDao = commonSeqDao;
	}

	/** 입력, insert SQL문 실행 */
	@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public int insert(CommonVo commonVo) throws SQLException {
		return commonDao.insert(commonVo);
	}
	
	/** 수정, update SQL문 실행 */
	@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public int update(CommonVo commonVo) throws SQLException {
		return commonDao.update(commonVo);
	}
	
	/** 삭제, delete SQL문 실행 */
	@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public int delete(CommonVo commonVo) throws SQLException {
		return commonDao.delete(commonVo);
	}
	
	/** 레코드 카운트 조회, select SQL문 실행 */
	public Integer count(CommonVo commonVo) throws SQLException{
		return commonDao.count(commonVo);
	}
	
	/** 단건 조회, select SQL문 실행 */
	public CommonVo queryVo(CommonVo commonVo) throws SQLException {
		return commonDao.queryVo(commonVo);
	}
	
	/** 단건 조회, select SQL문 실행 */
	public Map<String, Object> queryMap(CommonVo commonVo) throws SQLException {
		return commonDao.queryMap(commonVo);
	}
	
	/** 목록 조회, select SQL문 실행 */
	public List<?> queryList(CommonVo commonVo) throws SQLException {
		return commonDao.queryList(commonVo);
	}

	/** String 조회, select SQL문 실행 */
	public String queryString(CommonVo commonVo) throws SQLException {
		return commonDao.queryString(commonVo);
	}

	/** Integer 조회, select SQL문 실행 */
	public Integer queryInt(CommonVo commonVo) throws SQLException {
		return commonDao.queryInt(commonVo);
	}

	/** Long 조회, select SQL문 실행 */
	public Long queryLong(CommonVo commonVo) throws SQLException {
		return commonDao.queryLong(commonVo);
	}

	/** 현재일시 조회, queryId 날짜의 타입 지정함, 예)YYYY-MM-DD, 디폴트:YYYY-MM-DD HH24:MI:SS */
	public String querySysdate(CommonVo commonVo) throws SQLException {
		return commonDao.querySysdate(commonVo);
	}

	/** 시퀀스 조회, queryId 시퀀스명 지정함 */
	public Long nextVal(String tableName) throws SQLException {
		if(!checkDbms) setDbms();
		if(commonSeqDao!=null && (isMssql || isMysql)) return commonSeqDao.nextVal(tableName);
		return commonDao.nextVal(tableName);
	}
	
	/** 시퀀스 기반 ID 생성, 0-9A-Z 문자열 생성 */
	public String createId(String tableName, char prefix, int length) throws SQLException {
		if(!checkDbms) setDbms();
		if(commonSeqDao!=null && (isMssql || isMysql)) return commonSeqDao.createId(tableName, prefix, length);
		return commonDao.createId(tableName, prefix, length);
	}

	/** 실행, QueryQueue에 담겨 있는 쿼리를 실행 함 */
	@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public int execute(QueryQueue queryQueue) throws SQLException {
		return commonDao.execute(queryQueue);
	}
	
	/** dbms 체크 */
	private void setDbms(){
		String dbms = contextProperties.getProperty("dbms");
		isMssql   = "mssql".equals(dbms);
//		isOracle  = "oracle".equals(dbms);
		isMysql   = "mysql".equals(dbms);
		checkDbms = true;
	}
}
