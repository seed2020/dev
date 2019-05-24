package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 대리인 관계(WC_AGNT_R)테이블 VO
 */
public class WcAgntRVo  extends CommonVoImpl {	

	/** serialVersionUID. */
	private static final long serialVersionUID = -4805750676372639541L;
	
	/** 회사ID */
	private String compId;

	/** 사용자UID */
	private String userUid;

	/** 대리인UID */
	private String agntUid;
	
	/** 리소스명 */
	private String rescNm;
	
	/** 부서리소스명 */
	private String deptRescNm;
	
	/** 직위명 */
	private String positNm;
	
	/** 직무명 */
	private String dutyNm;
	
	/** 직책명 */
	private String titleNm;
	
	
	

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 대리인UID */
	public String getAgntUid() {
		return agntUid;
	}

	/** 대리인UID */
	public void setAgntUid(String agntUid) {
		this.agntUid = agntUid;
	}
	
	public String getRescNm() {
		return rescNm;
	}

	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	public String getDeptRescNm() {
		return deptRescNm;
	}

	public void setDeptRescNm(String deptRescNm) {
		this.deptRescNm = deptRescNm;
	}

	public String getPositNm() {
		return positNm;
	}

	public void setPositNm(String positNm) {
		this.positNm = positNm;
	}

	public String getDutyNm() {
		return dutyNm;
	}

	public void setDutyNm(String dutyNm) {
		this.dutyNm = dutyNm;
	}

	public String getTitleNm() {
		return titleNm;
	}

	public void setTitleNm(String titleNm) {
		this.titleNm = titleNm;
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
		builder.append('[').append(this.getClass().getName()).append(":대리인 관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(agntUid!=null) { if(tab!=null) builder.append(tab); builder.append("agntUid(대리인UID):").append(agntUid).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(deptRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescNm(부서리소스명):").append(deptRescNm).append('\n'); }
		if(positNm!=null) { if(tab!=null) builder.append(tab); builder.append("positNm(직위명):").append(positNm).append('\n'); }
		if(dutyNm!=null) { if(tab!=null) builder.append(tab); builder.append("dutyNm(직무명):").append(dutyNm).append('\n'); }
		if(titleNm!=null) { if(tab!=null) builder.append(tab); builder.append("titleNm(직책명):").append(titleNm).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}