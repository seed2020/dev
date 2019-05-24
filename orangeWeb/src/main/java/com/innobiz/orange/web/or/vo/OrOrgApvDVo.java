package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 조직결재상세(OR_ORG_APV_D) 테이블 VO
 */
public class OrOrgApvDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8600727724159733458L;

	/** 조직ID - KEY */
	private String orgId;

	/** 문서과조직ID */
	private String crdOrgId;

	/** 문서과리소스ID */
	private String crdRescId;

	/** 대리문서과조직ID */
	private String agnCrdOrgId;

	/** 대리문서과리소스ID */
	private String agnCrdRescId;

	/** 실제문서과조직ID */
	private String realCrdOrgId;

	/** 부서장직위리소스ID */
	private String hodpPositRescId;

	/** 발신명의리소스ID */
	private String sendrNmRescId;

	/** 수신처ID */
	private String recvDeptId;

	/** 감사과여부 */
	private String inspYn;

	/** 관인ID */
	private String ofseId;


	// 추가컬럼
	/** 조직ID 목록 */
	private List<String> orgIdList;

	/** 회사ID */
	private String compId;

	/** 문서과리소스명 */
	private String crdRescNm;

	/** 대리문서과리소스명 */
	private String agnCrdRescNm;

	/** 부서장직위리소스명 */
	private String hodpPositRescNm;

	/** 발신명의리소스명 */
	private String sendrNmRescNm;

	/** 조직ID - KEY */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID - KEY */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 문서과조직ID */
	public String getCrdOrgId() {
		return crdOrgId;
	}

	/** 문서과조직ID */
	public void setCrdOrgId(String crdOrgId) {
		this.crdOrgId = crdOrgId;
	}

	/** 문서과리소스ID */
	public String getCrdRescId() {
		return crdRescId;
	}

	/** 문서과리소스ID */
	public void setCrdRescId(String crdRescId) {
		this.crdRescId = crdRescId;
	}

	/** 대리문서과조직ID */
	public String getAgnCrdOrgId() {
		return agnCrdOrgId;
	}

	/** 대리문서과조직ID */
	public void setAgnCrdOrgId(String agnCrdOrgId) {
		this.agnCrdOrgId = agnCrdOrgId;
	}

	/** 대리문서과리소스ID */
	public String getAgnCrdRescId() {
		return agnCrdRescId;
	}

	/** 대리문서과리소스ID */
	public void setAgnCrdRescId(String agnCrdRescId) {
		this.agnCrdRescId = agnCrdRescId;
	}

	/** 실제문서과조직ID */
	public String getRealCrdOrgId() {
		return realCrdOrgId;
	}

	/** 실제문서과조직ID */
	public void setRealCrdOrgId(String realCrdOrgId) {
		this.realCrdOrgId = realCrdOrgId;
	}

	/** 부서장직위리소스ID */
	public String getHodpPositRescId() {
		return hodpPositRescId;
	}

	/** 부서장직위리소스ID */
	public void setHodpPositRescId(String hodpPositRescId) {
		this.hodpPositRescId = hodpPositRescId;
	}

	/** 발신명의리소스ID */
	public String getSendrNmRescId() {
		return sendrNmRescId;
	}

	/** 발신명의리소스ID */
	public void setSendrNmRescId(String sendrNmRescId) {
		this.sendrNmRescId = sendrNmRescId;
	}

	/** 수신처ID */
	public String getRecvDeptId() {
		return recvDeptId;
	}

	/** 수신처ID */
	public void setRecvDeptId(String recvDeptId) {
		this.recvDeptId = recvDeptId;
	}

	/** 감사과여부 */
	public String getInspYn() {
		return inspYn;
	}

	/** 감사과여부 */
	public void setInspYn(String inspYn) {
		this.inspYn = inspYn;
	}

	/** 관인ID */
	public String getOfseId() {
		return ofseId;
	}

	/** 관인ID */
	public void setOfseId(String ofseId) {
		this.ofseId = ofseId;
	}

	/** 조직ID 목록 */
	public List<String> getOrgIdList() {
		return orgIdList;
	}

	/** 조직ID 목록 */
	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 문서과리소스명 */
	public String getCrdRescNm() {
		return crdRescNm;
	}

	/** 문서과리소스명 */
	public void setCrdRescNm(String crdRescNm) {
		this.crdRescNm = crdRescNm;
	}

	/** 대리문서과리소스명 */
	public String getAgnCrdRescNm() {
		return agnCrdRescNm;
	}

	/** 대리문서과리소스명 */
	public void setAgnCrdRescNm(String agnCrdRescNm) {
		this.agnCrdRescNm = agnCrdRescNm;
	}

	/** 부서장직위리소스명 */
	public String getHodpPositRescNm() {
		return hodpPositRescNm;
	}

	/** 부서장직위리소스명 */
	public void setHodpPositRescNm(String hodpPositRescNm) {
		this.hodpPositRescNm = hodpPositRescNm;
	}

	/** 발신명의리소스명 */
	public String getSendrNmRescNm() {
		return sendrNmRescNm;
	}

	/** 발신명의리소스명 */
	public void setSendrNmRescNm(String sendrNmRescNm) {
		this.sendrNmRescNm = sendrNmRescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgApvDDao.selectOrOrgApvD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgApvDDao.insertOrOrgApvD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgApvDDao.updateOrOrgApvD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgApvDDao.deleteOrOrgApvD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOrgApvDDao.countOrOrgApvD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조직결재상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID-PK):").append(orgId).append('\n'); }
		if(crdOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("crdOrgId(문서과조직ID):").append(crdOrgId).append('\n'); }
		if(crdRescId!=null) { if(tab!=null) builder.append(tab); builder.append("crdRescId(문서과리소스ID):").append(crdRescId).append('\n'); }
		if(agnCrdOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("agnCrdOrgId(대리문서과조직ID):").append(agnCrdOrgId).append('\n'); }
		if(agnCrdRescId!=null) { if(tab!=null) builder.append(tab); builder.append("agnCrdRescId(대리문서과리소스ID):").append(agnCrdRescId).append('\n'); }
		if(realCrdOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("realCrdOrgId(실제문서과조직ID):").append(realCrdOrgId).append('\n'); }
		if(hodpPositRescId!=null) { if(tab!=null) builder.append(tab); builder.append("hodpPositRescId(부서장직위리소스ID):").append(hodpPositRescId).append('\n'); }
		if(sendrNmRescId!=null) { if(tab!=null) builder.append(tab); builder.append("sendrNmRescId(발신명의리소스ID):").append(sendrNmRescId).append('\n'); }
		if(recvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptId(수신처ID):").append(recvDeptId).append('\n'); }
		if(inspYn!=null) { if(tab!=null) builder.append(tab); builder.append("inspYn(감사과여부):").append(inspYn).append('\n'); }
		if(ofseId!=null) { if(tab!=null) builder.append(tab); builder.append("ofseId(관인ID):").append(ofseId).append('\n'); }
		if(orgIdList!=null) { if(tab!=null) builder.append(tab); builder.append("orgIdList(조직ID 목록):"); appendStringListTo(builder, orgIdList, tab); builder.append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(crdRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("crdRescNm(문서과리소스명):").append(crdRescNm).append('\n'); }
		if(agnCrdRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("agnCrdRescNm(대리문서과리소스명):").append(agnCrdRescNm).append('\n'); }
		if(hodpPositRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("hodpPositRescNm(부서장직위리소스명):").append(hodpPositRescNm).append('\n'); }
		if(sendrNmRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("sendrNmRescNm(발신명의리소스명):").append(sendrNmRescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
