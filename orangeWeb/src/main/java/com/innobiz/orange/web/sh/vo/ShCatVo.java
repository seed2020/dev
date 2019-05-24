package com.innobiz.orange.web.sh.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 카테고리 VO
 */
public class ShCatVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5309592900806646501L;

	/** 카테고리구분코드 */
	private String catTypCd;

	/** 카테고리코드 */
	private String catCd;

	/** 카테고리코드 목록 */
	private List<String> catCdList;

	/** 카테고리구분코드 */
	public String getCatTypCd() {
		return catTypCd;
	}

	/** 카테고리구분코드 */
	public void setCatTypCd(String catTypCd) {
		this.catTypCd = catTypCd;
	}

	/** 카테고리코드 */
	public String getCatCd() {
		return catCd;
	}

	/** 카테고리코드 */
	public void setCatCd(String catCd) {
		this.catCd = catCd;
	}

	/** 카테고리코드 목록 */
	public List<String> getCatCdList() {
		return catCdList;
	}

	/** 카테고리코드 목록 */
	public void setCatCdList(List<String> catCdList) {
		this.catCdList = catCdList;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":카테고리 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(catTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("catTypCd(카테고리구분코드):").append(catTypCd).append('\n'); }
		if(catCd!=null) { if(tab!=null) builder.append(tab); builder.append("catCd(카테고리코드):").append(catCd).append('\n'); }
		if(catCdList!=null) { if(tab!=null) builder.append(tab); builder.append("catCdList(카테고리코드 목록):"); appendStringListTo(builder, catCdList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
}
