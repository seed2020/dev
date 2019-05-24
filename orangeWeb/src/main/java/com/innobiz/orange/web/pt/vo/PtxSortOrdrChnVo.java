package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 정렬순서변경 VO
 */
public class PtxSortOrdrChnVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6541187942165581087L;

	/** 테이블ID */
	private String tabId;

	/** 회사ID */
	private String compId;

	/** PK컬럼 */
	private String pkCol;

	/** PK */
	private String pk;

	/** 두번째 PK컬럼 */
	private String pkCol2;

	/** 두번째 PK */
	private String pk2;

	/** 변경값 */
	private Integer chnVa;

	/** 이하 */
	private Integer lessThen;

	/** 이상 */
	private Integer moreThen;

	/** 테이블ID */
	public String getTabId() {
		return tabId;
	}

	/** 테이블ID */
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** PK컬럼 */
	public String getPkCol() {
		return pkCol;
	}

	/** PK컬럼 */
	public void setPkCol(String pkCol) {
		this.pkCol = pkCol;
	}

	/** PK */
	public String getPk() {
		return pk;
	}

	/** PK */
	public void setPk(String pk) {
		this.pk = pk;
	}

	/** 두번째 PK컬럼 */
	public String getPkCol2() {
		return pkCol2;
	}

	/** 두번째 PK컬럼 */
	public void setPkCol2(String pkCol2) {
		this.pkCol2 = pkCol2;
	}

	/** 두번째 PK */
	public String getPk2() {
		return pk2;
	}

	/** 두번째 PK */
	public void setPk2(String pk2) {
		this.pk2 = pk2;
	}

	/** 변경값 */
	public Integer getChnVa() {
		return chnVa;
	}

	/** 변경값 */
	public void setChnVa(Integer chnVa) {
		this.chnVa = chnVa;
	}

	/** 이하 */
	public Integer getLessThen() {
		return lessThen;
	}

	/** 이하 */
	public void setLessThen(Integer lessThen) {
		this.lessThen = lessThen;
	}

	/** 이상 */
	public Integer getMoreThen() {
		return moreThen;
	}

	/** 이상 */
	public void setMoreThen(Integer moreThen) {
		this.moreThen = moreThen;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":정렬순서변경 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(tabId!=null) { if(tab!=null) builder.append(tab); builder.append("tabId(테이블ID):").append(tabId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(pkCol!=null) { if(tab!=null) builder.append(tab); builder.append("pkCol(PK컬럼):").append(pkCol).append('\n'); }
		if(pk!=null) { if(tab!=null) builder.append(tab); builder.append("pk(PK):").append(pk).append('\n'); }
		if(pkCol2!=null) { if(tab!=null) builder.append(tab); builder.append("pkCol2(두번째 PK컬럼):").append(pkCol2).append('\n'); }
		if(pk2!=null) { if(tab!=null) builder.append(tab); builder.append("pk2(두번째 PK):").append(pk2).append('\n'); }
		if(chnVa!=null) { if(tab!=null) builder.append(tab); builder.append("chnVa(변경값):").append(chnVa).append('\n'); }
		if(lessThen!=null) { if(tab!=null) builder.append(tab); builder.append("lessThen(이하):").append(lessThen).append('\n'); }
		if(moreThen!=null) { if(tab!=null) builder.append(tab); builder.append("moreThen(이상):").append(moreThen).append('\n'); }
		super.toString(builder, tab);
	}
}
