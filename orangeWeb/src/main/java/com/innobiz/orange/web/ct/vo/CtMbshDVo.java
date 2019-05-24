package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtMbshDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4160721770188435299L;
	
	/** 회사 ID */
	private String compId;

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 커뮤니티 ID */
	
	private String ctId;
	
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	
	/** 사용자 ID */
	
	private String userUid;
	
	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	/** 사용자보안등급코드 */
	
	private String userSeculCd;
	
	public String getUserSeculCd() {
		return userSeculCd;
	}

	public void setUserSeculCd(String userSeculCd) {
		this.userSeculCd = userSeculCd;
	}

	
	/** 가입일시 */
	
	private String joinDt;
	
	public String getJoinDt() {
		return joinDt;
	}

	public void setJoinDt(String joinDt) {
		if(joinDt != null && joinDt.endsWith(".0") && joinDt.length()>2)
			 joinDt=joinDt.substring(0, joinDt.length()-2);
		this.joinDt = joinDt;
	}
	
	/** 가입상태 */
	
	private String joinStat;
	
	public String getJoinStat() {
		return joinStat;
	}

	public void setJoinStat(String joinStat) {
		this.joinStat = joinStat;
	}
	
	// 추가 컬럼
	
	/** 사용자명 */
	private String userNm;
	
	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
	/** 부서명 */
	private String deptNm;

	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	
	/** 다국어처리 언어 */
	private String langTyp;

	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 회사명 */
	private String compNm;
	
	public String getCompNm() {
		return compNm;
	}

	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}
	
	/** 커뮤니티 명 */
	private String ctNm;
	
	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	
	/** 사용자보안등급코드 리스트 */
	private List<String> userSeculCdList;

	public List<String> getUserSeculCdList() {
		return userSeculCdList;
	}

	public void setUserSeculCdList(List<String> userSeculCdList) {
		this.userSeculCdList = userSeculCdList;
	}
	
	/** 직책명 */
	private String gradeNm;
	

	public String getGradeNm() {
		return gradeNm;
	}

	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
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
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사 ID):").append(compId).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자 ID):").append(userUid).append('\n'); }
		if(userSeculCd!=null) { if(tab!=null) builder.append(tab); builder.append("userSeculCd(사용자보안등급코드):").append(userSeculCd).append('\n'); }
		if(joinDt!=null) { if(tab!=null) builder.append(tab); builder.append("joinDt(가입일시):").append(joinDt).append('\n'); }
		if(joinStat!=null) { if(tab!=null) builder.append(tab); builder.append("joinStat(가입상태):").append(joinStat).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(deptNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptNm(부서명):").append(deptNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(다국어처리 언어):").append(langTyp).append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(ctNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctNm(커뮤니티 명):").append(ctNm).append('\n'); }
		if(userSeculCdList!=null) { if(tab!=null) builder.append(tab); builder.append("userSeculCdList(사용자보안등급코드 리스트):"); appendStringListTo(builder, userSeculCdList,tab); builder.append('\n'); }
		if(gradeNm!=null) { if(tab!=null) builder.append(tab); builder.append("gradeNm(직책명):").append(gradeNm).append('\n'); }
		super.toString(builder, tab);
	}

}