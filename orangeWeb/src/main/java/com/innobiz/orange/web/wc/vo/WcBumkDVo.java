package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 즐겨찾기 상세(WC_BUMK_D)테이블 VO
 */
public class WcBumkDVo  extends CommonVoImpl {	

	/** serialVersionUID. */
	private static final long serialVersionUID = 6497905046891389879L;

	/** 회사ID */
	private String compId;

	/** 사용자UID */
	private String userUid;

	/** 즐겨찾기ID */
	private String bumkId;

	/** 즐겨찾기표시명 */
	private String bumkDispNm;

	/** 즐겨찾기 대상자 UID */
	private String bumkTgtUid;
	
	/** 즐겨찾기 대상자 명 */
	private String bumkTgtNm;

	/** 즐겨찾기 대상 부서ID */
	private String bumkTgtDeptId;
	
	/** 즐겨찾기 부서 명 */
	private String bumkTgtDeptNm;

	/** 사용자 부서 구분 코드 */
	private String userDeptTypCd;
	
	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 즐겨찾기ID */
	public String getBumkId() {
		return bumkId;
	}

	/** 즐겨찾기ID */
	public void setBumkId(String bumkId) {
		this.bumkId = bumkId;
	}

	/** 즐겨찾기표시명 */
	public String getBumkDispNm() {
		return bumkDispNm;
	}

	/** 즐겨찾기표시명 */
	public void setBumkDispNm(String bumkDispNm) {
		this.bumkDispNm = bumkDispNm;
	}

	/** 즐겨찾기 대상자 UID */
	public String getBumkTgtUid() {
		return bumkTgtUid;
	}

	/** 즐겨찾기 대상자 UID */
	public void setBumkTgtUid(String bumkTgtUid) {
		this.bumkTgtUid = bumkTgtUid;
	}
	
	/** 즐겨찾기 대상자명 */
	public String getBumkTgtNm() {
		return bumkTgtNm;
	}

	/** 즐겨찾기 대상자명 */
	public void setBumkTgtNm(String bumkTgtNm) {
		this.bumkTgtNm = bumkTgtNm;
	}

	/** 즐겨찾기 대상 부서ID */
	public String getBumkTgtDeptId() {
		return bumkTgtDeptId;
	}
	
	/** 즐겨찾기 대상 부서ID */
	public void setBumkTgtDeptId(String bumkTgtDeptId) {
		this.bumkTgtDeptId = bumkTgtDeptId;
	}

	/** 즐겨찾기 대상 부서명 */
	public String getBumkTgtDeptNm() {
		return bumkTgtDeptNm;
	}
	
	/** 즐겨찾기 대상 부서명 */
	public void setBumkTgtDeptNm(String bumkTgtDeptNm) {
		this.bumkTgtDeptNm = bumkTgtDeptNm;
	}
	
	
	/** 사용자 부서 구분 코드 */
	public String getUserDeptTypCd() {
		return userDeptTypCd;
	}
	
	/** 사용자 부서 구분 코드 */
	public void setUserDeptTypCd(String userDeptTypCd) {
		this.userDeptTypCd = userDeptTypCd;
	}

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":즐겨찾기 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(bumkId!=null) { if(tab!=null) builder.append(tab); builder.append("bumkId(즐겨찾기ID):").append(bumkId).append('\n'); }
		if(bumkDispNm!=null) { if(tab!=null) builder.append(tab); builder.append("bumkDispNm(즐겨찾기표시명):").append(bumkDispNm).append('\n'); }
		if(bumkTgtUid!=null) { if(tab!=null) builder.append(tab); builder.append("bumkTgtUid(즐겨찾기 대상자 UID):").append(bumkTgtUid).append('\n'); }
		if(bumkTgtNm!=null) { if(tab!=null) builder.append(tab); builder.append("bumkTgtNm(즐겨찾기 대상자 명):").append(bumkTgtNm).append('\n'); }
		if(bumkTgtDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("bumkTgtDeptId(즐겨찾기 대상 부서ID):").append(bumkTgtDeptId).append('\n'); }
		if(bumkTgtDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("bumkTgtDeptNm(즐겨찾기 부서 명):").append(bumkTgtDeptNm).append('\n'); }
		if(userDeptTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("userDeptTypCd(사용자 부서 구분 코드):").append(userDeptTypCd).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}