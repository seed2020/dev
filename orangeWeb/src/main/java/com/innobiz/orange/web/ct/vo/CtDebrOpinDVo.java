package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtDebrOpinDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8247598175796706134L;

	/** 회사 ID */
	private String compId;
	
	/** 커뮤니티 ID */
	private String ctId;
	
	/** 토론실 ID */
	private String debrId;
	
	/** 의견 순서 */
	private Integer opinOrdr;
	
	/** 제목 */
	private String subj;
	
	/** 의견 */
	private String opin;
	
	/** 조회수 */
	private Integer readCnt;
	
	/** 찬성/반대 */
	private String prosConsCd;
	
	/** 등록일자 */
	private String regDt;
	
	/** 수정일자 */
	private String modDt;
	
	/** 등록자 UID */
	private String regrUid;
	
	/** 수정자 UID */
	private String modrUid;
	
	//추가 컬럼
	
	/** 사용자 언어타입 */
	private String langTyp;
	
	/** 등록자명 */
	private String regrNm;
	
	/** 수정자명 */
	private String modrNm;
	
	
	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}

	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	public String getModrNm() {
		return modrNm;
	}

	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getDebrId() {
		return debrId;
	}

	public void setDebrId(String debrId) {
		this.debrId = debrId;
	}

	public Integer getOpinOrdr() {
		return opinOrdr;
	}

	public void setOpinOrdr(Integer opinOrdr) {
		this.opinOrdr = opinOrdr;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getOpin() {
		return opin;
	}

	public void setOpin(String opin) {
		this.opin = opin;
	}

	public Integer getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}

	public String getProsConsCd() {
		return prosConsCd;
	}

	public void setProsConsCd(String prosConsCd) {
		this.prosConsCd = prosConsCd;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}

	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
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
		if(debrId!=null) { if(tab!=null) builder.append(tab); builder.append("debrId(토론실 ID):").append(debrId).append('\n'); }
		if(opinOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("opinOrdr(의견 순서):").append(opinOrdr).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(opin!=null) { if(tab!=null) builder.append(tab); builder.append("opin(의견):").append(opin).append('\n'); }
		if(readCnt!=null) { if(tab!=null) builder.append(tab); builder.append("readCnt(조회수):").append(readCnt).append('\n'); }
		if(prosConsCd!=null) { if(tab!=null) builder.append(tab); builder.append("prosConsCd(찬성/반대):").append(prosConsCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일자):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일자):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(사용자 언어타입):").append(langTyp).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}

}