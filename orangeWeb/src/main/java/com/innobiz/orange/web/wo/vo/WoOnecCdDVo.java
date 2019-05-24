package com.innobiz.orange.web.wo.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 원카드코드상세(WO_ONEC_CD_D) 테이블 VO
 */
public class WoOnecCdDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5148059149495008447L;

	/** 분류코드 - KEY */
	private String clsCd;

	/** 코드 - KEY */
	private String cd;

	/** 코드값 */
	private String cdVa;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용여부 */
	private String useYn;

	/** 비고 */
	private String note;

	/** 분류코드 - KEY */
	public String getClsCd() {
		return clsCd;
	}

	/** 분류코드 - KEY */
	public void setClsCd(String clsCd) {
		this.clsCd = clsCd;
	}

	/** 코드 - KEY */
	public String getCd() {
		return cd;
	}

	/** 코드 - KEY */
	public void setCd(String cd) {
		this.cd = cd;
	}

	/** 코드값 */
	public String getCdVa() {
		return cdVa;
	}

	/** 코드값 */
	public void setCdVa(String cdVa) {
		this.cdVa = cdVa;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 비고 */
	public String getNote() {
		return note;
	}

	/** 비고 */
	public void setNote(String note) {
		this.note = note;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecCdDDao.selectWoOnecCdD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecCdDDao.insertWoOnecCdD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecCdDDao.updateWoOnecCdD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecCdDDao.deleteWoOnecCdD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecCdDDao.countWoOnecCdD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원카드코드상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(clsCd!=null) { if(tab!=null) builder.append(tab); builder.append("clsCd(분류코드-PK):").append(clsCd).append('\n'); }
		if(cd!=null) { if(tab!=null) builder.append(tab); builder.append("cd(코드-PK):").append(cd).append('\n'); }
		if(cdVa!=null) { if(tab!=null) builder.append(tab); builder.append("cdVa(코드값):").append(cdVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(note!=null) { if(tab!=null) builder.append(tab); builder.append("note(비고):").append(note).append('\n'); }
		super.toString(builder, tab);
	}
}
