package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 리소스기본(OR_RESC_B) 테이블 VO
 */
public class OrRescBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4383251432296437405L;

	/** 리소스ID - KEY */
	private String rescId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 리소스값 */
	private String rescVa;


	// 추가컬럼
	/** 조직부모ID - 조직목록 리소스 일괄 조회용 */
	private String orgPid;

	/** 조직ID - 사용자 목록 리소스 일괄 조회용 */
	private String orgId;

	/** 리소스ID 목록 */
	private List<String> rescIdList;

	/** 회사ID */
	private String compId;

	/** 리소스ID - KEY */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID - KEY */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 리소스값 */
	public String getRescVa() {
		return rescVa;
	}

	/** 리소스값 */
	public void setRescVa(String rescVa) {
		this.rescVa = rescVa;
	}

	/** 조직부모ID - 조직목록 리소스 일괄 조회용 */
	public String getOrgPid() {
		return orgPid;
	}

	/** 조직부모ID - 조직목록 리소스 일괄 조회용 */
	public void setOrgPid(String orgPid) {
		this.orgPid = orgPid;
	}

	/** 조직ID - 사용자 목록 리소스 일괄 조회용 */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID - 사용자 목록 리소스 일괄 조회용 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 리소스ID 목록 */
	public List<String> getRescIdList() {
		return rescIdList;
	}

	/** 리소스ID 목록 */
	public void setRescIdList(List<String> rescIdList) {
		this.rescIdList = rescIdList;
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
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrRescBDao.selectOrRescB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrRescBDao.insertOrRescB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrRescBDao.updateOrRescB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrRescBDao.deleteOrRescB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrRescBDao.countOrRescB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":리소스기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID-PK):").append(rescId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드-PK):").append(langTypCd).append('\n'); }
		if(rescVa!=null) { if(tab!=null) builder.append(tab); builder.append("rescVa(리소스값):").append(rescVa).append('\n'); }
		if(orgPid!=null) { if(tab!=null) builder.append(tab); builder.append("orgPid(조직부모ID - 조직목록 리소스 일괄 조회용):").append(orgPid).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID - 사용자 목록 리소스 일괄 조회용):").append(orgId).append('\n'); }
		if(rescIdList!=null) { if(tab!=null) builder.append(tab); builder.append("rescIdList(리소스ID 목록):"); appendStringListTo(builder, rescIdList, tab); builder.append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		super.toString(builder, tab);
	}
}
