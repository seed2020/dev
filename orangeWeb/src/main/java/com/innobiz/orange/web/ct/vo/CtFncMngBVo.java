package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtFncMngBVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4250092359675978353L;
	
	/** 커뮤니티 기능 ID */
	private String ctFncId;
	
	/** 회사ID */ 
	private String compId;
	
	public String getCtFncId() {
		return ctFncId;
	}

	public void setCtFncId(String ctFncId) {
		this.ctFncId = ctFncId;
	}
	
	/** 커뮤니티 기능 제목 리소스 ID */
	
	private String ctFncSubjRescId;
	
	public String getCtFncSubjRescId() {
		return ctFncSubjRescId;
	}

	public void setCtFncSubjRescId(String ctFncSubjRescId) {
		this.ctFncSubjRescId = ctFncSubjRescId;
	}
	
	/** 커뮤니티 기능 구분 */
	
	private String ctFncTyp;
	
	public String getCtFncTyp() {
		return ctFncTyp;
	}

	public void setCtFncTyp(String ctFncTyp) {
		this.ctFncTyp = ctFncTyp;
	}
	
	/** URL */
	
	private String url;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	/**포틀릿 URL */
	
	private String ptUrl;
	
	public String getPtUrl() {
		return ptUrl;
	}

	public void setPtUrl(String ptUrl) {
		this.ptUrl = ptUrl;
	}
	
	/** 관계 테이블 제목 */
	
	private String relTblSubj;
	
	public String getRelTblSubj() {
		return relTblSubj;
	}

	public void setRelTblSubj(String relTblSubj) {
		this.relTblSubj = relTblSubj;
	}

	
	/** 기본 여부 */
	
	private String dftYn;
	
	public String getDftYn() {
		return dftYn;
	}

	public void setDftYn(String dftYn) {
		this.dftYn = dftYn;
	}
	
	/** 멀티 선택 여부 */
	
	private String mulChoiYn;
	
	public String getMulChoiYn() {
		return mulChoiYn;
	}

	public void setMulChoiYn(String mulChoiYn) {
		this.mulChoiYn = mulChoiYn;
	}
	
	/** 사용 여부 */
	
	private String useYn;
	
	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	/** 커뮤니티 메뉴 여부 */
	
	private String ctMngYn;
	
	public String getCtMngYn() {
		return ctMngYn;
	}

	public void setCtMngYn(String ctMngYn) {
		this.ctMngYn = ctMngYn;
	}

	//추가컬럼
	/** 커뮤니티 기능 명 */
	private String ctFncNm;
	
	public String getCtFncNm() {
		return ctFncNm;
	}

	public void setCtFncNm(String ctFncNm) {
		this.ctFncNm = ctFncNm;
	}
	
	/** 커뮤니티 기능 언어 */
	private String langTyp;
	
	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 기능에서 옮겨진 List 숨기기  */
	private List<String> fncIdHideList;
	
	public List<String> getFncIdHideList() {
		return fncIdHideList;
	}

	public void setFncIdHideList(List<String> fncIdHideList) {
		this.fncIdHideList = fncIdHideList;
	}
	
	/** 커뮤니티 ID */
	private String ctId;
	
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	
	/** 회사ID */
	public String getCompId() {
		return compId;
	}
	
	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
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
		if(ctFncId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncId(커뮤니티 기능 ID):").append(ctFncId).append('\n'); }
		if(ctFncSubjRescId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncSubjRescId(커뮤니티 기능 제목 리소스 ID):").append(ctFncSubjRescId).append('\n'); }
		if(ctFncTyp!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncTyp(커뮤니티 기능 구분):").append(ctFncTyp).append('\n'); }
		if(url!=null) { if(tab!=null) builder.append(tab); builder.append("url(URL):").append(url).append('\n'); }
		if(ptUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ptUrl(포틀릿 URL):").append(ptUrl).append('\n'); }
		if(relTblSubj!=null) { if(tab!=null) builder.append(tab); builder.append("relTblSubj(관계 테이블 제목):").append(relTblSubj).append('\n'); }
		if(dftYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftYn(기본 여부):").append(dftYn).append('\n'); }
		if(mulChoiYn!=null) { if(tab!=null) builder.append(tab); builder.append("mulChoiYn(멀티 선택 여부):").append(mulChoiYn).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용 여부):").append(useYn).append('\n'); }
		if(ctMngYn!=null) { if(tab!=null) builder.append(tab); builder.append("ctMngYn(커뮤니티 메뉴 여부):").append(ctMngYn).append('\n'); }
		if(ctFncNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncNm(커뮤니티 기능 명):").append(ctFncNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(커뮤니티 기능 언어):").append(langTyp).append('\n'); }
		if(fncIdHideList!=null) { if(tab!=null) builder.append(tab); builder.append("fncIdHideList(기능에서 옮겨진 List 숨기기):"); appendStringListTo(builder, fncIdHideList,tab); builder.append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if (compId != null) { if (tab != null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		super.toString(builder, tab);
	}

}