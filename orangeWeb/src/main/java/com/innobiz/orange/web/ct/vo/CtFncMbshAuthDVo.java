package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtFncMbshAuthDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 2945662816254029034L;
	/** 회사 ID */
	
	private String compId;
	
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	
	/** 커뮤니티 기능 UID*/
	
	private String ctFncUid;
	
	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}
	
	/** 보안 등급 코드 */
	
	private String seculCd;
	
	public String getSeculCd() {
		return seculCd;
	}

	public void setSeculCd(String seculCd) {
		this.seculCd = seculCd;
	}
	
	/** 권한 코드 */
	
	private String authCd;
	
	public String getAuthCd() {
		return authCd;
	}

	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}
	
	//추가 컬럼
	
	/** 권한 스탭 읽기 */
	private String authSR;
	/** 권한 스탭 쓰기 */
	private String authSW;
	/** 권한 스탭 삭제 */
	private String authSD;
	
	/** 권한 정회원 읽기 */
	private String authRR;
	/** 권한 정회원 쓰기 */
	private String authRW;
	/** 권한 정회원 삭제 */
	private String authRD;
	
	/** 권한 준회원 읽기 */
	private String authAR;
	/** 권한 준회원 쓰기 */
	private String authAW;
	/** 권한 준회원 삭제 */
	private String authAD;
	
	public String getAuthSR() {
		return authSR;
	}

	public void setAuthSR(String authSR) {
		this.authSR = authSR;
	}

	public String getAuthSW() {
		return authSW;
	}

	public void setAuthSW(String authSW) {
		this.authSW = authSW;
	}

	public String getAuthSD() {
		return authSD;
	}

	public void setAuthSD(String authSD) {
		this.authSD = authSD;
	}

	public String getAuthRR() {
		return authRR;
	}

	public void setAuthRR(String authRR) {
		this.authRR = authRR;
	}

	public String getAuthRW() {
		return authRW;
	}

	public void setAuthRW(String authRW) {
		this.authRW = authRW;
	}

	public String getAuthRD() {
		return authRD;
	}

	public void setAuthRD(String authRD) {
		this.authRD = authRD;
	}

	public String getAuthAR() {
		return authAR;
	}

	public void setAuthAR(String authAR) {
		this.authAR = authAR;
	}

	public String getAuthAW() {
		return authAW;
	}

	public void setAuthAW(String authAW) {
		this.authAW = authAW;
	}

	public String getAuthAD() {
		return authAD;
	}

	public void setAuthAD(String authAD) {
		this.authAD = authAD;
	}
	
	/** 권한 마스터 읽기 */
	private String authMR;
	/** 권한 마스터 쓰기 */
	private String authMW;
	/** 권한 마스터 삭제 */
	private String authMD;

	public String getAuthMR() {
		return authMR;
	}

	public void setAuthMR(String authMR) {
		this.authMR = authMR;
	}

	public String getAuthMW() {
		return authMW;
	}

	public void setAuthMW(String authMW) {
		this.authMW = authMW;
	}

	public String getAuthMD() {
		return authMD;
	}

	public void setAuthMD(String authMD) {
		this.authMD = authMD;
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
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		if(seculCd!=null) { if(tab!=null) builder.append(tab); builder.append("seculCd(보안 등급 코드):").append(seculCd).append('\n'); }
		if(authCd!=null) { if(tab!=null) builder.append(tab); builder.append("authCd(권한 코드):").append(authCd).append('\n'); }
		if(authSR!=null) { if(tab!=null) builder.append(tab); builder.append("authSR(권한 스탭 읽기):").append(authSR).append('\n'); }
		if(authSW!=null) { if(tab!=null) builder.append(tab); builder.append("authSW(권한 스탭 쓰기):").append(authSW).append('\n'); }
		if(authSD!=null) { if(tab!=null) builder.append(tab); builder.append("authSD(권한 스탭 삭제):").append(authSD).append('\n'); }
		if(authRR!=null) { if(tab!=null) builder.append(tab); builder.append("authRR(권한 정회원 읽기):").append(authRR).append('\n'); }
		if(authRW!=null) { if(tab!=null) builder.append(tab); builder.append("authRW(권한 정회원 쓰기):").append(authRW).append('\n'); }
		if(authRD!=null) { if(tab!=null) builder.append(tab); builder.append("authRD(권한 정회원 삭제):").append(authRD).append('\n'); }
		if(authAR!=null) { if(tab!=null) builder.append(tab); builder.append("authAR(권한 준회원 읽기):").append(authAR).append('\n'); }
		if(authAW!=null) { if(tab!=null) builder.append(tab); builder.append("authAW(권한 준회원 쓰기):").append(authAW).append('\n'); }
		if(authAD!=null) { if(tab!=null) builder.append(tab); builder.append("authAD(권한 준회원 삭제):").append(authAD).append('\n'); }
		if(authMR!=null) { if(tab!=null) builder.append(tab); builder.append("authMR(권한 마스터 읽기):").append(authMR).append('\n'); }
		if(authMW!=null) { if(tab!=null) builder.append(tab); builder.append("authMW(권한 마스터 쓰기):").append(authMW).append('\n'); }
		if(authMD!=null) { if(tab!=null) builder.append(tab); builder.append("authMD(권한 마스터 삭제):").append(authMD).append('\n'); }
		super.toString(builder, tab);
	}

}