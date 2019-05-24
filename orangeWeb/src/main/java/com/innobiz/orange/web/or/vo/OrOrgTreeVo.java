package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 조직도 트리구성 VO - 로그인 처리용 조직도 트리 캐쉬 데이터
 */
public class OrOrgTreeVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8834592431698319140L;

	/** 조직ID */
	private String orgId;

	/** 리소스ID */
	private String rescId;

	/** 리소스명 */
	private String rescNm;

	/** 조직약어리소스명 */
	private String orgAbbrRescNm;

	/** 회사ID */
	private String compId;

	/** 조직부모ID */
	private String orgPid;

	/** 조직구분코드 - C:회사, G:기관, D:부서, P:파트 */
	private String orgTypCd;

	/** 정렬순서 */
	private String sortOrdr;

	/** 사용안함 */
	private Boolean disable;

	/** 조직ID */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 조직약어리소스명 */
	public String getOrgAbbrRescNm() {
		return orgAbbrRescNm;
	}

	/** 조직약어리소스명 */
	public void setOrgAbbrRescNm(String orgAbbrRescNm) {
		this.orgAbbrRescNm = orgAbbrRescNm;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 조직부모ID */
	public String getOrgPid() {
		return orgPid;
	}

	/** 조직부모ID */
	public void setOrgPid(String orgPid) {
		this.orgPid = orgPid;
	}

	/** 조직구분코드 - C:회사, G:기관, D:부서, P:파트 */
	public String getOrgTypCd() {
		return orgTypCd;
	}

	/** 조직구분코드 - C:회사, G:기관, D:부서, P:파트 */
	public void setOrgTypCd(String orgTypCd) {
		this.orgTypCd = orgTypCd;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 사용안함 */
	public Boolean isDisable() {
		return disable;
	}

	/** 사용안함 */
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조직도 트리구성 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(orgAbbrRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("orgAbbrRescNm(조직약어리소스명):").append(orgAbbrRescNm).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(orgPid!=null) { if(tab!=null) builder.append(tab); builder.append("orgPid(조직부모ID):").append(orgPid).append('\n'); }
		if(orgTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("orgTypCd(조직구분코드):").append(orgTypCd).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(disable!=null) { if(tab!=null) builder.append(tab); builder.append("disable(사용안함):").append(disable).append('\n'); }
		super.toString(builder, tab);
	}
}
