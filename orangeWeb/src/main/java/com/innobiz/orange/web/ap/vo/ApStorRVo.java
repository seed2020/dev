package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 저장소관계(AP_STOR_R) 테이블 VO
 */
public class ApStorRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7925343340640371332L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 저장소ID - KEY */
	private String storId;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 저장소ID - KEY */
	public String getStorId() {
		return storId;
	}

	/** 저장소ID - KEY */
	public void setStorId(String storId) {
		this.storId = storId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorRDao.selectApStorR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorRDao.insertApStorR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorRDao.updateApStorR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorRDao.deleteApStorR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorRDao.countApStorR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":저장소관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID-PK):").append(storId).append('\n'); }
		super.toString(builder, tab);
	}
}
