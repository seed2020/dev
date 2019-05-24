package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtCatBVo extends CommonVoImpl{

	/** serialVersionUID */
	private static final long serialVersionUID = -750610525824834413L;
	
	/** 회사 ID */
	private String compId;

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 카테고리 ID */
	
	private String catId;
	
	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	/** 카테고리 제목 리소스 ID */
	
	private String catSubjRescId;
	
	public String getCatSubjRescId() {
		return catSubjRescId;
	}

	public void setCatSubjRescId(String catSubjRescId) {
		this.catSubjRescId = catSubjRescId;
	}
	
	/** 카테고리 부모 ID */
	
	private String catPid;
	
	public String getCatPid() {
		return catPid;
	}

	public void setCatPid(String catPid) {
		this.catPid = catPid;
	}
	
	/** 카테고리 부모 명 */
	
	private String catPidNm;
	
	public String getCatPidNm() {
		return catPidNm;
	}

	public void setCatPidNm(String catPidNm) {
		this.catPidNm = catPidNm;
	}
	
	/** 카테고리 위치 단계 */
	
	private Integer catLocStep;
	
	public Integer getCatLocStep() {
		return catLocStep;
	}

	public void setCatLocStep(Integer catLocStep) {
		this.catLocStep = catLocStep;
	}
	/** 자식 여부 */
	
	private String chldYn;
	
	public String getChldYn() {
		return chldYn;
	}

	public void setChldYn(String chldYn) {
		this.chldYn = chldYn;
	}
	
	/** 외부 공개 여부 */
	
	private String extnOpenYn;
	
	public String getExtnOpenYn() {
		return extnOpenYn;
	}

	public void setExtnOpenYn(String extnOpenYn) {
		this.extnOpenYn = extnOpenYn;
	}
	
	/** 폴더 분류 구분 코드 */
	
	private String fldTypCd;
	
	public String getFldTypCd() {
		return fldTypCd;
	}

	public void setFldTypCd(String fldTypCd) {
		this.fldTypCd = fldTypCd;
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
	
	/** 카테고리 순서 */
	
	private Integer catOrdr;
	
	public Integer getCatOrdr() {
		return catOrdr;
	}

	public void setCatOrdr(Integer catOrdr) {
		this.catOrdr = catOrdr;
	}

	//추가 컬럼
	
	/** 카테고리에 포함된 커뮤니티 개수 */
	private Integer catCmntCnt;
	public Integer getCatCmntCnt() {
		
		return catCmntCnt;
	}

	public void setCatCmntCnt(Integer catCmntCnt) {
		this.catCmntCnt = catCmntCnt;
	}
	
	/** 카테고리 언어 */
	private String langTyp;
	
	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 카테고리 ID */
	
	private String catNm;
	
	public String getCatNm() {
		return catNm;
	}

	public void setCatNm(String catNm) {
		this.catNm = catNm;
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
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(카테고리 ID):").append(catId).append('\n'); }
		if(catSubjRescId!=null) { if(tab!=null) builder.append(tab); builder.append("catSubjRescId(카테고리 제목 리소스 ID):").append(catSubjRescId).append('\n'); }
		if(catPid!=null) { if(tab!=null) builder.append(tab); builder.append("catPid(카테고리 부모 ID):").append(catPid).append('\n'); }
		if(catPidNm!=null) { if(tab!=null) builder.append(tab); builder.append("catPidNm(카테고리 부모 명):").append(catPidNm).append('\n'); }
		if(catLocStep!=null) { if(tab!=null) builder.append(tab); builder.append("catLocStep(카테고리 위치 단계):").append(catLocStep).append('\n'); }
		if(chldYn!=null) { if(tab!=null) builder.append(tab); builder.append("chldYn(자식 여부):").append(chldYn).append('\n'); }
		if(extnOpenYn!=null) { if(tab!=null) builder.append(tab); builder.append("extnOpenYn(외부 공개 여부):").append(extnOpenYn).append('\n'); }
		if(fldTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("fldTypCd(폴더 분류 구분 코드):").append(fldTypCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(catOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("catOrdr(카테고리 순서):").append(catOrdr).append('\n'); }
		if(catCmntCnt!=null) { if(tab!=null) builder.append(tab); builder.append("catCmntCnt(카테고리에 포함된 커뮤니티 개수):").append(catCmntCnt).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(카테고리 언어):").append(langTyp).append('\n'); }
		if(catNm!=null) { if(tab!=null) builder.append(tab); builder.append("catNm(카테고리 ID):").append(catNm).append('\n'); }
		super.toString(builder, tab);
	}

}