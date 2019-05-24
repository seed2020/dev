package com.innobiz.orange.web.cm.dao;

import java.io.Reader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.JdbcType;
import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoHolder;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 공통 DAO */
public class CommonDao extends SqlSessionDaoSupport {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CommonDao.class);
	
	/** 입력, insert SQL문 실행 */
	public int insert(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.INSERT);
		try {
			int result = sqlSession.insert(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return -1;
	}
	
	/** 수정, update SQL문 실행 */
	public int update(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.UPDATE);
		try {
			int result = sqlSession.update(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return -1;
	}
	
	/** 삭제, delete SQL문 실행 */
	public int delete(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.DELETE);
		try {
			int result = sqlSession.delete(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return -1;
	}

	/** 레코드 카운트 조회, select SQL문 실행 */
	public Integer count(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.COUNT);
		try {
			Integer result = (Integer) sqlSession.selectOne(queryId, commonVo);
			if(Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** 단건 조회, select SQL문 실행 */
	public CommonVo queryVo(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.SELECT);
		try {
			CommonVo result = (CommonVo) sqlSession.selectOne(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** 단건 조회 - 맵, select SQL문 실행 */
	public Map<String, Object> queryMap(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.SELECT);
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> result = (Map<String, Object>) sqlSession.selectOne(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** 목록 조회, select SQL문 실행 */
	public List<?> queryList(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.SELECT);
		try {
			
			if(commonVo.getPageNo()!=null && commonVo.getPageRowCnt()!=null){
				commonVo.setPageStrt( (commonVo.getPageNo() - 1) * commonVo.getPageRowCnt() );
			}
			
			List<?> result = sqlSession.selectList(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** String 조회, select SQL문 실행 */
	public String queryString(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.SELECT);
		try {
			String result = (String) sqlSession.selectOne(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}

	/** Integer 조회, select SQL문 실행 */
	public Integer queryInt(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(commonVo.getQueryType() == null ? QueryType.SELECT : commonVo.getQueryType());
		try {
			Integer result =  (Integer) sqlSession.selectOne(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}

	/** Long 조회, select SQL문 실행 */
	public Long queryLong(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		String queryId = commonVo.getQueryId(QueryType.SELECT);
		try {
			Long result = (Long) sqlSession.selectOne(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** 현재일시 조회, queryId 날짜의 타입 지정함, <br/>예)YYYY-MM-DD, 디폴트:YYYY-MM-DD HH24:MI:SS */
	public String querySysdate(CommonVo commonVo) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		if(commonVo==null) commonVo = new CommonVoImpl();
		String queryId = commonVo.getInstanceQueryId();
		if(queryId==null){
			queryId = "YYYY-MM-DD HH24:MI:SS";
			commonVo.setInstanceQueryId(queryId);
		}
		try {
			String result = (String) sqlSession.selectOne("com.innobiz.orange.web.cm.dao.CommonDao.queryDate", commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** 시퀀스 조회, queryId 시퀀스명 지정함 */
	public Long nextVal(String tableName) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		CommonVo commonVo = new CommonVoImpl(tableName);
		String queryId = "com.innobiz.orange.web.cm.dao.CommonDao.nextVal";
		try {
			Long result = (Long) sqlSession.selectOne(queryId, commonVo);
			if(ServerConfig.IS_LOC && Logger.getLogger(commonVo.getClass()).isDebugEnabled()){
				traceException(sqlSession, queryId, commonVo, null);
			}
			return result;
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return null;
	}
	
	/** 시퀀스 기반 ID 생성, 0-9A-Z 문자열 생성 */
	public String createId(String tableName, char prefix, int length) throws SQLException {
		Long seq = nextVal(tableName);
		return IdUtil.createId(prefix, seq, length);
	}
	
	/** 실행, QueryQueue에 담겨 있는 쿼리를 실행 함 */
	public int execute(QueryQueue queryQueue) throws SQLException {
		SqlSession sqlSession = getSqlSession();
		int i, result=0, resultSum=0, size = queryQueue.size();
		CommonVo commonVo = null;
		QueryType queryType = null;
		String queryId = null;
		try {
			for(i=0; i<size; i++){
				commonVo = queryQueue.get(i);
				queryType = commonVo.getQueryType();
				if(queryType==null){
					queryId = commonVo.getInstanceQueryId();
					if(queryId==null) continue;
					result = sqlSession.update(queryId, commonVo);
				} else if(QueryType.INSERT == queryType){
					queryId = commonVo.getQueryId(QueryType.INSERT);
					result = sqlSession.insert(queryId, commonVo);
				} else if(QueryType.UPDATE == queryType){
					queryId = commonVo.getQueryId(QueryType.UPDATE);
					result = sqlSession.update(queryId, commonVo);
				} else if(QueryType.DELETE == queryType){
					queryId = commonVo.getQueryId(QueryType.DELETE);
					result = sqlSession.delete(queryId, commonVo);
				} else if(QueryType.STORE == queryType){
					queryId = commonVo.getQueryId(QueryType.UPDATE);
					result = sqlSession.update(queryId, commonVo);
					if(result==0){
						queryId = commonVo.getQueryId(QueryType.INSERT);
						result = sqlSession.insert(queryId, commonVo);
					}
				} else {
					continue;
				}
				resultSum += result;
			}
		} catch (Exception e){
			traceException(sqlSession, queryId, commonVo, e);
		}
		return resultSum;
	}
	
	/** 오류에 대한 트레이스 */
	private void traceException(SqlSession sqlSession, String queryId, CommonVo commonVo, Exception exception)
		throws SQLException {
		
		Exception root = getRootException(exception);
		if(root!=null) VoHolder.set(root, commonVo);
		
		StringBuffer buffer = new StringBuffer(1024);
		buffer.append("/*\n");
		if(root!=null){
			buffer.append(root.getClass().getCanonicalName()).append(": ").append(root.getMessage()).append('\n');
		}
		buffer.append("queryId: ").append(queryId).append('\n');
		buffer.append(commonVo.toString());
		buffer.append("*/\n");
		if(ServerConfig.IS_LOC || exception!=null){
			
			MappedStatement mappedStatement = sqlSession.getConfiguration().getMappedStatement(queryId);
			BoundSql boundSql = mappedStatement==null ? null : mappedStatement.getBoundSql(commonVo);
			if(boundSql != null){
				String executedSQL = boundSql.getSql();
				List<ParameterMapping> paramList = boundSql.getParameterMappings();
				
				String attribute, replacement, stringValue;
				Object value;
				JdbcType jdbcType;
				
				char qmReplace = '㉾';
				
				executedSQL = StringUtil.removeEmptyLine(executedSQL);
				int i, size = paramList==null ? 0 : paramList.size();
				for(i=0;i<size;i++){
					ParameterMapping mapping = paramList.get(i);
					attribute = mapping.getProperty();
					jdbcType = mapping.getJdbcType();
					if(attribute.startsWith("__")){
						value = boundSql.getAdditionalParameter(attribute);
						attribute = "foreach";
					} else {
						value = VoUtil.getValue(commonVo, attribute);
					}
					
					if(value instanceof byte[] || jdbcType == JdbcType.BLOB){
						replacement = "/*"+attribute+"*/'[BYTE ARRAY]'";
					} else if(value instanceof Reader || jdbcType == JdbcType.CLOB) {
						replacement = "/*"+attribute+"*/'[LONG STRING]'";
					} else if(value instanceof String) {
						stringValue = (String)value;
						if(jdbcType == JdbcType.NUMERIC || jdbcType==JdbcType.DECIMAL || jdbcType==JdbcType.BIGINT || jdbcType==JdbcType.INTEGER || jdbcType==JdbcType.DOUBLE || jdbcType==JdbcType.FLOAT){
							replacement = "/*"+attribute+"*/"+stringValue;
						} else {
							replacement = "/*"+attribute+"*/'"+stringValue.replace("'", "''")+"'";
						}
					} else if(value==null){
						replacement = "/*"+attribute+"*/null";
					} else {
						stringValue = value.toString();
						if(jdbcType == JdbcType.NUMERIC || jdbcType==JdbcType.DECIMAL || jdbcType==JdbcType.BIGINT || jdbcType==JdbcType.INTEGER || jdbcType==JdbcType.DOUBLE || jdbcType==JdbcType.FLOAT){
							replacement = "/*"+attribute+"*/"+stringValue;
						} else {
							replacement = "/*"+attribute+"*/'"+stringValue.replace("'", "''")+"'";
						}
					}
					replacement = replacement.replace('?', qmReplace);
					executedSQL = executedSQL.replaceFirst("\\?", replacement);
				}
				executedSQL = executedSQL.replace(qmReplace, '?');
				buffer.append(executedSQL).append('\n');
			}
		}
		tracePackage(exception, buffer);
		LOGGER.error(buffer.toString());
		if(root!=null){
			if(root instanceof SQLException) throw (SQLException) root;
			throw new SQLException(root);
		}
	}
	
	/** Exception 의 root(Exception)를 구해서 리턴함 */
	private Exception getRootException(Throwable throwable){
		if(throwable==null) return null;
		Throwable next, cause = throwable;
		while((next = cause.getCause())!=null){
			cause = next;
		}
		return (Exception)cause;
	}
	
	/** 패키지 내의 정보만 buffer[parameter]에 트레이스 함  */
	private void tracePackage(Throwable throwable, StringBuffer buffer){
		if(throwable==null) return;
		String className;
		for(StackTraceElement element : throwable.getStackTrace()){
			className = element.getClassName();
			if(className.startsWith("com.innobiz.") && className.indexOf("$$")<0){
				buffer.append("	at ").append(className).append('.')
				.append(element.getMethodName()).append('(')
				.append(className.substring(className.lastIndexOf('.')+1)).append(".java:")
				.append(element.getLineNumber()).append(")\n");
			}
		}
	}
}
