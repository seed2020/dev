package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtFncDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4230121515189921102L;
	
	/** 회사 ID */
	
	private String compId;
	
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 커뮤니티 기능  UID */
	private String ctFncUid;
	
	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}

	/** 커뮤니티 ID */
	
	private String ctId;
	
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	
	/** 커뮤니티 기능 ID */
	
	private String ctFncId;
	
	public String getCtFncId() {
		return ctFncId;
	}

	public void setCtFncId(String ctFncId) {
		this.ctFncId = ctFncId;
	}
	
	/** 커뮤니티 기능 부모 ID */
	
	private String ctFncPid;
	
	public String getCtFncPid() {
		return ctFncPid;
	}

	public void setCtFncPid(String ctFncPid) {
		this.ctFncPid = ctFncPid;
	}
	
	/** 커뮤니티 기능 순서 */
	
	private Integer ctFncOrdr;
	
	public Integer getCtFncOrdr() {
		return ctFncOrdr;
	}

	public void setCtFncOrdr(Integer ctFncOrdr) {
		this.ctFncOrdr = ctFncOrdr;
	}
	
	/** 기능 위치 단계 */
	
	private String ctFncLocStep;
	
	public String getCtFncLocStep() {
		return ctFncLocStep;
	}

	public void setCtFncLocStep(String ctFncLocStep) {
		this.ctFncLocStep = ctFncLocStep;
	}

	
	/** 커뮤니티 기능 구분 */
	
	private String ctFncTyp;
	
	public String getCtFncTyp() {
		return ctFncTyp;
	}

	public void setCtFncTyp(String ctFncTyp) {
		this.ctFncTyp = ctFncTyp;
	}
	
	/** 커뮤니티 기능 제목 리소스 ID */
	
	private String ctFncSubjRescId;
	
	public String getCtFncSubjRescId() {
		return ctFncSubjRescId;
	}

	public void setCtFncSubjRescId(String ctFncSubjRescId) {
		this.ctFncSubjRescId = ctFncSubjRescId;
	}
	
	/** 순서 */
	
	private Integer ordr;
	
	public Integer getOrdr() {
		return ordr;
	}

	public void setOrdr(Integer ordr) {
		this.ordr = ordr;
	}
	
	/** 등록일시 */
	
	private String regDt;
	
	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	
	/** 수정일시 */
	
	private String modDt;
	
	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}
	
	/** 등록자 UID */
	
	private String regrUid;
	
	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 수정자 UID */
	
	private String modrUid;
	
	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}
	
	
	//추가 컬럼
	
	/** 커뮤니티 기능 명 */
	
	private String ctFncNm;
	
	public String getCtFncNm() {
		return ctFncNm;
	}

	public void setCtFncNm(String ctFncNm) {
		this.ctFncNm = ctFncNm;
	}
	
	/** 커뮤니티 언어 */
	
	private String langTyp;
	
	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 커뮤니티 기능 URL */
	
	private String ctFncUrl;
	
	public String getCtFncUrl() {
		return ctFncUrl;
	}

	public void setCtFncUrl(String ctFncUrl) {
		this.ctFncUrl = ctFncUrl;
	}
	
	/** 보안등급코드 */
	private String seculCd;
	
	public String getSeculCd() {
		return seculCd;
	}

	public void setSeculCd(String seculCd) {
		this.seculCd = seculCd;
	}
	
	/** 권한코드 */
	private String authCd;
	
	public String getAuthCd() {
		return authCd;
	}

	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}
	
	/** 커뮤니티 기능 부모명 */
	private String ctFncPnm;
	
	public String getCtFncPnm() {
		return ctFncPnm;
	}

	public void setCtFncPnm(String ctFncPnm) {
		this.ctFncPnm = ctFncPnm;
	}
	
	/** 커뮤니티 메뉴 사용여부 (초기페이지 구성 영역메뉴 사용여부 )  */
	private String ctMngYn;
	
	public String getCtMngYn() {
		return ctMngYn;
	}

	public void setCtMngYn(String ctMngYn) {
		this.ctMngYn = ctMngYn;
	}
	
	/** 커뮤니티 기능 URL */
	
	private String ptUrl;
	
	public String getPtUrl() {
		return ptUrl;
	}

	public void setPtUrl(String ptUrl) {
		this.ptUrl = ptUrl;
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
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능  UID):").append(ctFncUid).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(ctFncId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncId(커뮤니티 기능 ID):").append(ctFncId).append('\n'); }
		if(ctFncPid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncPid(커뮤니티 기능 부모 ID):").append(ctFncPid).append('\n'); }
		if(ctFncOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncOrdr(커뮤니티 기능 순서):").append(ctFncOrdr).append('\n'); }
		if(ctFncLocStep!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncLocStep(기능 위치 단계):").append(ctFncLocStep).append('\n'); }
		if(ctFncTyp!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncTyp(커뮤니티 기능 구분):").append(ctFncTyp).append('\n'); }
		if(ctFncSubjRescId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncSubjRescId(커뮤니티 기능 제목 리소스 ID):").append(ctFncSubjRescId).append('\n'); }
		if(ordr!=null) { if(tab!=null) builder.append(tab); builder.append("ordr(순서):").append(ordr).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(ctFncNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncNm(커뮤니티 기능 명):").append(ctFncNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(커뮤니티 언어):").append(langTyp).append('\n'); }
		if(ctFncUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUrl(커뮤니티 기능 URL):").append(ctFncUrl).append('\n'); }
		if(seculCd!=null) { if(tab!=null) builder.append(tab); builder.append("seculCd(보안등급코드):").append(seculCd).append('\n'); }
		if(authCd!=null) { if(tab!=null) builder.append(tab); builder.append("authCd(권한코드):").append(authCd).append('\n'); }
		if(ctFncPnm!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncPnm(커뮤니티 기능 부모명):").append(ctFncPnm).append('\n'); }
		if(ctMngYn!=null) { if(tab!=null) builder.append(tab); builder.append("ctMngYn(커뮤니티 메뉴 사용여부 (초기페이지 구성 영역메뉴 사용여부 )):").append(ctMngYn).append('\n'); }
		if(ptUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ptUrl(커뮤니티 기능 URL):").append(ptUrl).append('\n'); }
		super.toString(builder, tab);
	}

}