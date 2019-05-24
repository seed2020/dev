package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * DB연계상세(OR_DB_INTG_D) 테이블 VO
 */
public class OrDbIntgDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6145305258596145300L;

	/** 회사ID - KEY */
	private String compId;

	/** 데이터베이스사용자ID */
	private String dbUserId;

	/** 데이터베이스비밀번호 */
	private String dbPw;

	/** 데이터베이스드라이버 */
	private String dbDriver;

	/** 데이터베이스URL */
	private String dbUrl;

	/** 데이터베이스테스트SQL */
	private String dbTestSql;

	/** 코드삭제코드 - del:삭제, stat:삭제 상태 */
	private String cdDelCd;

	/** 조직삭제코드 - del:삭제, stat:삭제 상태, move:삭제 상태 후 이동 */
	private String orgDelCd;

	/** 사용자삭제코드 - del:삭제, stat:삭제 상태, move:삭제 상태 후 이동 */
	private String userDelCd;

	/** 삭제조직이동조직ID */
	private String delOrgMoveOrgId;

	/** 삭제사용자이동조직ID */
	private String delUserMoveOrgId;

	/** 부서없음조직ID */
	private String noDeptOrgId;

	/** 연계시간 */
	private String intgTm;

	/** 사용여부 */
	private String useYn;

	/** 조직도SQL */
	private String orgSql;

	/** 코드SQL */
	private String cdSql;

	/** 사용자SQL */
	private String userSql;

	/** 겸직SQL */
	private String aduSql;


	// 추가컬럼
	/** 최종실행년월일 */
	private String lastRunYmd;

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 데이터베이스사용자ID */
	public String getDbUserId() {
		return dbUserId;
	}

	/** 데이터베이스사용자ID */
	public void setDbUserId(String dbUserId) {
		this.dbUserId = dbUserId;
	}

	/** 데이터베이스비밀번호 */
	public String getDbPw() {
		return dbPw;
	}

	/** 데이터베이스비밀번호 */
	public void setDbPw(String dbPw) {
		this.dbPw = dbPw;
	}

	/** 데이터베이스드라이버 */
	public String getDbDriver() {
		return dbDriver;
	}

	/** 데이터베이스드라이버 */
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	/** 데이터베이스URL */
	public String getDbUrl() {
		return dbUrl;
	}

	/** 데이터베이스URL */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	/** 데이터베이스테스트SQL */
	public String getDbTestSql() {
		return dbTestSql;
	}

	/** 데이터베이스테스트SQL */
	public void setDbTestSql(String dbTestSql) {
		this.dbTestSql = dbTestSql;
	}

	/** 코드삭제코드 - del:삭제, stat:삭제 상태 */
	public String getCdDelCd() {
		return cdDelCd;
	}

	/** 코드삭제코드 - del:삭제, stat:삭제 상태 */
	public void setCdDelCd(String cdDelCd) {
		this.cdDelCd = cdDelCd;
	}

	/** 조직삭제코드 - del:삭제, stat:삭제 상태, move:삭제 상태 후 이동 */
	public String getOrgDelCd() {
		return orgDelCd;
	}

	/** 조직삭제코드 - del:삭제, stat:삭제 상태, move:삭제 상태 후 이동 */
	public void setOrgDelCd(String orgDelCd) {
		this.orgDelCd = orgDelCd;
	}

	/** 사용자삭제코드 - del:삭제, stat:삭제 상태, move:삭제 상태 후 이동 */
	public String getUserDelCd() {
		return userDelCd;
	}

	/** 사용자삭제코드 - del:삭제, stat:삭제 상태, move:삭제 상태 후 이동 */
	public void setUserDelCd(String userDelCd) {
		this.userDelCd = userDelCd;
	}

	/** 삭제조직이동조직ID */
	public String getDelOrgMoveOrgId() {
		return delOrgMoveOrgId;
	}

	/** 삭제조직이동조직ID */
	public void setDelOrgMoveOrgId(String delOrgMoveOrgId) {
		this.delOrgMoveOrgId = delOrgMoveOrgId;
	}

	/** 삭제사용자이동조직ID */
	public String getDelUserMoveOrgId() {
		return delUserMoveOrgId;
	}

	/** 삭제사용자이동조직ID */
	public void setDelUserMoveOrgId(String delUserMoveOrgId) {
		this.delUserMoveOrgId = delUserMoveOrgId;
	}

	/** 부서없음조직ID */
	public String getNoDeptOrgId() {
		return noDeptOrgId;
	}

	/** 부서없음조직ID */
	public void setNoDeptOrgId(String noDeptOrgId) {
		this.noDeptOrgId = noDeptOrgId;
	}

	/** 연계시간 */
	public String getIntgTm() {
		return intgTm;
	}

	/** 연계시간 */
	public void setIntgTm(String intgTm) {
		this.intgTm = intgTm;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 조직도SQL */
	public String getOrgSql() {
		return orgSql;
	}

	/** 조직도SQL */
	public void setOrgSql(String orgSql) {
		this.orgSql = orgSql;
	}

	/** 코드SQL */
	public String getCdSql() {
		return cdSql;
	}

	/** 코드SQL */
	public void setCdSql(String cdSql) {
		this.cdSql = cdSql;
	}

	/** 사용자SQL */
	public String getUserSql() {
		return userSql;
	}

	/** 사용자SQL */
	public void setUserSql(String userSql) {
		this.userSql = userSql;
	}

	/** 겸직SQL */
	public String getAduSql() {
		return aduSql;
	}

	/** 겸직SQL */
	public void setAduSql(String aduSql) {
		this.aduSql = aduSql;
	}

	/** 최종실행년월일 */
	public String getLastRunYmd() {
		return lastRunYmd;
	}

	/** 최종실행년월일 */
	public void setLastRunYmd(String lastRunYmd) {
		this.lastRunYmd = lastRunYmd;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrDbIntgDDao.selectOrDbIntgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrDbIntgDDao.insertOrDbIntgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrDbIntgDDao.updateOrDbIntgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrDbIntgDDao.deleteOrDbIntgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrDbIntgDDao.countOrDbIntgD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":DB연계상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(dbUserId!=null) { if(tab!=null) builder.append(tab); builder.append("dbUserId(데이터베이스사용자ID):").append(dbUserId).append('\n'); }
		if(dbPw!=null) { if(tab!=null) builder.append(tab); builder.append("dbPw(데이터베이스비밀번호):").append(dbPw).append('\n'); }
		if(dbDriver!=null) { if(tab!=null) builder.append(tab); builder.append("dbDriver(데이터베이스드라이버):").append(dbDriver).append('\n'); }
		if(dbUrl!=null) { if(tab!=null) builder.append(tab); builder.append("dbUrl(데이터베이스URL):").append(dbUrl).append('\n'); }
		if(dbTestSql!=null) { if(tab!=null) builder.append(tab); builder.append("dbTestSql(데이터베이스테스트SQL):").append(dbTestSql).append('\n'); }
		if(cdDelCd!=null) { if(tab!=null) builder.append(tab); builder.append("cdDelCd(코드삭제코드):").append(cdDelCd).append('\n'); }
		if(orgDelCd!=null) { if(tab!=null) builder.append(tab); builder.append("orgDelCd(조직삭제코드):").append(orgDelCd).append('\n'); }
		if(userDelCd!=null) { if(tab!=null) builder.append(tab); builder.append("userDelCd(사용자삭제코드):").append(userDelCd).append('\n'); }
		if(delOrgMoveOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("delOrgMoveOrgId(삭제조직이동조직ID):").append(delOrgMoveOrgId).append('\n'); }
		if(delUserMoveOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("delUserMoveOrgId(삭제사용자이동조직ID):").append(delUserMoveOrgId).append('\n'); }
		if(noDeptOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("noDeptOrgId(부서없음조직ID):").append(noDeptOrgId).append('\n'); }
		if(intgTm!=null) { if(tab!=null) builder.append(tab); builder.append("intgTm(연계시간):").append(intgTm).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(orgSql!=null) { if(tab!=null) builder.append(tab); builder.append("orgSql(조직도SQL):").append(orgSql).append('\n'); }
		if(cdSql!=null) { if(tab!=null) builder.append(tab); builder.append("cdSql(코드SQL):").append(cdSql).append('\n'); }
		if(userSql!=null) { if(tab!=null) builder.append(tab); builder.append("userSql(사용자SQL):").append(userSql).append('\n'); }
		if(aduSql!=null) { if(tab!=null) builder.append(tab); builder.append("aduSql(겸직SQL):").append(aduSql).append('\n'); }
		if(lastRunYmd!=null) { if(tab!=null) builder.append(tab); builder.append("lastRunYmd(최종실행년월일):").append(lastRunYmd).append('\n'); }
		super.toString(builder, tab);
	}
}
