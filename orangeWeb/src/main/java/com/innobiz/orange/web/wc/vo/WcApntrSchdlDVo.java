package com.innobiz.orange.web.wc.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.wc.calender.WcScdCalDay;

/**
 * 지정인 일정 상세(WC_APNTR_R)테이블 VO
 */
public class WcApntrSchdlDVo  extends CommonVoImpl {	

	/** serialVersionUID. */
	private static final long serialVersionUID = -2732141986428232561L;

	
	/** 회사ID */
	private String compId;
	
	/** 사용자UID */
	private String userUid;

	/** 지정인UID */
	private String apntrUid;	

	/** 회사ID */
	public String getCompId() {
		return compId;
	}
	
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
	
	/** 순서  */
	private int ordr;
	
	/** 타일 조회된 일정 */
	private WcScdCalDay scdCalDay; 

	public WcScdCalDay getScdCalDay() {
		return scdCalDay;
	}

	public void setScdCalDay(WcScdCalDay scdCalDay) {
		this.scdCalDay = scdCalDay;
	}

	public int getOrdr() {
		return ordr;
	}

	public void setOrdr(int ordr) {
		this.ordr = ordr;
	}
	
	

	/** 사용자UID  */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID  */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 지정인UID */
	public String getApntrUid() {
		return apntrUid;
	}

	/** 지정인UID */
	public void setApntrUid(String apntrUid) {
		this.apntrUid = apntrUid;
	}
	
	/** 회사ID  */
	public void setCompId(String compId) {
		this.compId = compId;
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
		builder.append('[').append(this.getClass().getName()).append(":지정인 일정 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(apntrUid!=null) { if(tab!=null) builder.append(tab); builder.append("apntrUid(지정인UID):").append(apntrUid).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(deptRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptRescNm(부서리소스명):").append(deptRescNm).append('\n'); }
		if(positNm!=null) { if(tab!=null) builder.append(tab); builder.append("positNm(직위명):").append(positNm).append('\n'); }
		if(dutyNm!=null) { if(tab!=null) builder.append(tab); builder.append("dutyNm(직무명):").append(dutyNm).append('\n'); }
		if(titleNm!=null) { if(tab!=null) builder.append(tab); builder.append("titleNm(직책명):").append(titleNm).append('\n'); }
		if(ordr!=0) { if(tab!=null) builder.append(tab); builder.append("ordr(순서):").append(ordr).append('\n'); }
		super.toString(builder, tab);
	}
	
	
}