package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자설정상세(PT_USER_SETUP_D) 테이블 VO
 */
public class PtUserSetupDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8643785007764160552L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 설정분류ID - KEY */
	private String setupClsId;

	/** 설정ID - KEY */
	private String setupId;

	/** 설정값 */
	private String setupVa;

	/** 캐쉬여부 */
	private String cacheYn;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 설정분류ID - KEY */
	public String getSetupClsId() {
		return setupClsId;
	}

	/** 설정분류ID - KEY */
	public void setSetupClsId(String setupClsId) {
		this.setupClsId = setupClsId;
	}

	/** 설정ID - KEY */
	public String getSetupId() {
		return setupId;
	}

	/** 설정ID - KEY */
	public void setSetupId(String setupId) {
		this.setupId = setupId;
	}

	/** 설정값 */
	public String getSetupVa() {
		return setupVa;
	}

	/** 설정값 */
	public void setSetupVa(String setupVa) {
		this.setupVa = setupVa;
	}

	/** 캐쉬여부 */
	public String getCacheYn() {
		return cacheYn;
	}

	/** 캐쉬여부 */
	public void setCacheYn(String cacheYn) {
		this.cacheYn = cacheYn;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserSetupDDao.selectPtUserSetupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserSetupDDao.insertPtUserSetupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserSetupDDao.updatePtUserSetupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserSetupDDao.deletePtUserSetupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserSetupDDao.countPtUserSetupD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자설정상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(setupClsId!=null) { if(tab!=null) builder.append(tab); builder.append("setupClsId(설정분류ID-PK):").append(setupClsId).append('\n'); }
		if(setupId!=null) { if(tab!=null) builder.append(tab); builder.append("setupId(설정ID-PK):").append(setupId).append('\n'); }
		if(setupVa!=null) { if(tab!=null) builder.append(tab); builder.append("setupVa(설정값):").append(setupVa).append('\n'); }
		if(cacheYn!=null) { if(tab!=null) builder.append(tab); builder.append("cacheYn(캐쉬여부):").append(cacheYn).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
