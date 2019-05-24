package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_PLT_SETUP_D [포틀릿 설정 상세] */
public class CtPltSetupDVo extends CommonVoImpl{

	private static final long serialVersionUID = -4316554775622534276L;
	
	/** 회사ID */
	String compId;
	/** 커뮤니티 기능 ID */
	String ctFncId;
	/** 사용자UID */
	String userUid;
	/** 기간 */
	String perd;
	/** 기능명 리소스 ID */
	String ctFuncSubjRescId;
	/** 커뮤니티 ID */
	String ctId;
	
	public synchronized String getCtFncId() {
		return ctFncId;
	}

	public synchronized void setCtFncId(String ctFncId) {
		this.ctFncId = ctFncId;
	}

	
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}



	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getPerd() {
		return perd;
	}

	public void setPerd(String perd) {
		this.perd = perd;
	}

	public String getCtFuncSubjRescId() {
		return ctFuncSubjRescId;
	}

	public void setCtFuncSubjRescId(String ctFuncSubjRescId) {
		this.ctFuncSubjRescId = ctFuncSubjRescId;
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
		super.toString(builder, tab);
	}

}