package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_SITE_B [커뮤니티 사이트 기본] */
public class CtSiteBVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8354149864979284742L;

	/** 회사 ID */
	private String compId;
	
	/** 커뮤니티 ID */
	private String ctId;
	
	/** COOL SITE ID */
	private String siteId;
	
	/** 커뮤니티 기능 ID */
	private String ctFncId;
	
	/** 커뮤니티 기능 순서 */
	private Integer ctFncOrdr;
	
	/** 제목 */
	private String subj;
	
	/** 카테고리 ID */
	private String catId;
	
	/** URL */
	private String url;
	
	/** 본문내용 */
	private String cont;
	
	/** 조회수 */
	private Integer readCnt;
	
	/** 등록일시 */
	private String regDt;
	
	/** 수정일시 */
	private String modDt;
	
	/** 등록자 UID */
	private String regrUid;
	
	/** 수정자 UID */
	private String modrUid;
	
	/** 에디터 타입 코드 */
	private Integer editorTypCd;
	
	/** 기능 위치 단계 */
	private String ctFncLocStep;
	 
	/** 커뮤니티 기능 UID */
	private String ctFncUid;
	 
	/** 커뮤니티 기능 부모ID */
	private String ctFncPid;
	
	//추가 컬럼
	
	/** 사용자 언어타입 */
	private String langTyp;
	
	/** 등록자명 */
	private String regrNm;
	
	/** 수정자명 */
	private String modrNm;
	
	/** 카테고리명 */
	private String catNm;
	
	/** 커뮤니티  리스트 */
	private List<String> ctIdList;
	
	public List<String> getCtIdList() {
		return ctIdList;
	}

	public void setCtIdList(List<String> ctIdList) {
		this.ctIdList = ctIdList;
	}


	public String getCatNm() {
		return catNm;
	}

	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}
	
	/** 기능명 */
	private String fncNm;
	
	public String getFncNm() {
		return fncNm;
	}

	public void setFncNm(String fncNm) {
		this.fncNm = fncNm;
	}
	
	/** 시작일자 */
	private String strtDt;
	
	public String getStrtDt() {
		return strtDt;
	}

	public void setStrtDt(String strtDt) {
		if(strtDt != null && strtDt.endsWith(".0") && strtDt.length()>2)
			 strtDt=strtDt.substring(0, strtDt.length()-2);
		this.strtDt = strtDt;
	}

	/** 종료일자 */
	private String endDt;
	
	public String getEndDt() {
		return endDt;
	}
	
	public void setEndDt(String endDt) {
		if(endDt != null && endDt.endsWith(".0") && endDt.length()>2)
			 endDt=endDt.substring(0, endDt.length()-2);
		this.endDt = endDt;
	}

	
	/** 커뮤니티 통합기능Uid */
	private String ctComFncUid;
	
	public String getCtComFncUid() {
		return ctComFncUid;
	}

	public void setCtComFncUid(String ctComFncUid) {
		this.ctComFncUid = ctComFncUid;
	}

	/** 커뮤니티 기능 URL */
	private String ctFncUrl;
	
	public String getCtFncUrl() {
		return ctFncUrl;
	}

	public void setCtFncUrl(String ctFncUrl) {
		this.ctFncUrl = ctFncUrl;
	}
	
	/** 커뮤니티명 */
	private String ctNm;
	
	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	
	/** 마스터UID */
	private String mastUid;
	
	public String getMastUid() {
		return mastUid;
	}

	public void setMastUid(String mastUid) {
		this.mastUid = mastUid;
	}
	
	/** 마스터명 */
	private String mastNm;
	
	public String getMastNm() {
		return mastNm;
	}

	public void setMastNm(String mastNm) {
		this.mastNm = mastNm;
	}
	
	/** 관계테이블명 */
	private String relTblSubj;
	

	public String getRelTblSubj() {
		return relTblSubj;
	}

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

	public String getCtFncLocStep() {
		return ctFncLocStep;
	}

	public void setCtFncLocStep(String ctFncLocStep) {
		this.ctFncLocStep = ctFncLocStep;
	}

	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}

	public String getCtFncPid() {
		return ctFncPid;
	}

	public void setCtFncPid(String ctFncPid) {
		this.ctFncPid = ctFncPid;
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

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getCtFncId() {
		return ctFncId;
	}

	public void setCtFncId(String ctFncId) {
		this.ctFncId = ctFncId;
	}

	public Integer getCtFncOrdr() {
		return ctFncOrdr;
	}

	public void setCtFncOrdr(Integer ctFncOrdr) {
		this.ctFncOrdr = ctFncOrdr;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public Integer getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
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
	
	public Integer getEditorTypCd() {
		return editorTypCd;
	}

	public void setEditorTypCd(Integer editorTypCd) {
		this.editorTypCd = editorTypCd;
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
		if(siteId!=null) { if(tab!=null) builder.append(tab); builder.append("siteId(COOL SITE ID):").append(siteId).append('\n'); }
		if(ctFncId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncId(커뮤니티 기능 ID):").append(ctFncId).append('\n'); }
		if(ctFncOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncOrdr(커뮤니티 기능 순서):").append(ctFncOrdr).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(카테고리 ID):").append(catId).append('\n'); }
		if(url!=null) { if(tab!=null) builder.append(tab); builder.append("url(URL):").append(url).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(본문내용):").append(cont).append('\n'); }
		if(readCnt!=null) { if(tab!=null) builder.append(tab); builder.append("readCnt(조회수):").append(readCnt).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(editorTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("editorTypCd(에디터 타입 코드):").append(editorTypCd).append('\n'); }
		if(ctFncLocStep!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncLocStep(기능 위치 단계):").append(ctFncLocStep).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		if(ctFncPid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncPid(커뮤니티 기능 부모ID):").append(ctFncPid).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(사용자 언어타입):").append(langTyp).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		if(catNm!=null) { if(tab!=null) builder.append(tab); builder.append("catNm(카테고리명):").append(catNm).append('\n'); }
		if(ctIdList!=null) { if(tab!=null) builder.append(tab); builder.append("ctIdList(커뮤니티  리스트):"); appendStringListTo(builder, ctIdList,tab); builder.append('\n'); }
		if(fncNm!=null) { if(tab!=null) builder.append(tab); builder.append("fncNm(기능명):").append(fncNm).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일자):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일자):").append(endDt).append('\n'); }
		if(ctComFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctComFncUid(커뮤니티 통합기능Uid):").append(ctComFncUid).append('\n'); }
		if(ctFncUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUrl(커뮤니티 기능 URL):").append(ctFncUrl).append('\n'); }
		if(ctNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctNm(커뮤니티명):").append(ctNm).append('\n'); }
		if(mastUid!=null) { if(tab!=null) builder.append(tab); builder.append("mastUid(마스터UID):").append(mastUid).append('\n'); }
		if(mastNm!=null) { if(tab!=null) builder.append(tab); builder.append("mastNm(마스터명):").append(mastNm).append('\n'); }
		if(relTblSubj!=null) { if(tab!=null) builder.append(tab); builder.append("relTblSubj(관계테이블명):").append(relTblSubj).append('\n'); }
		super.toString(builder, tab);
	}

}