package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 수신그룹상세(AP_RECV_GRP_D) 테이블 VO
 */
public class ApRecvGrpDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 26733791243145720L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 수신그룹ID - KEY */
	private String recvGrpId;

	/** 수신그룹일련번호 - KEY */
	private String recvGrpSeq;

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	private String recvDeptTypCd;

	/** 수신처ID */
	private String recvDeptId;

	/** 수신처명 */
	private String recvDeptNm;

	/** 참조처ID */
	private String refDeptId;

	/** 참조처명 */
	private String refDeptNm;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 수신처구분명 */
	private String recvDeptTypNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 수신그룹ID - KEY */
	public String getRecvGrpId() {
		return recvGrpId;
	}

	/** 수신그룹ID - KEY */
	public void setRecvGrpId(String recvGrpId) {
		this.recvGrpId = recvGrpId;
	}

	/** 수신그룹일련번호 - KEY */
	public String getRecvGrpSeq() {
		return recvGrpSeq;
	}

	/** 수신그룹일련번호 - KEY */
	public void setRecvGrpSeq(String recvGrpSeq) {
		this.recvGrpSeq = recvGrpSeq;
	}

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	public String getRecvDeptTypCd() {
		return recvDeptTypCd;
	}

	/** 수신처구분코드 - dom:대내, for:대외, outOrg:외부기관 */
	public void setRecvDeptTypCd(String recvDeptTypCd) {
		this.recvDeptTypCd = recvDeptTypCd;
	}

	/** 수신처ID */
	public String getRecvDeptId() {
		return recvDeptId;
	}

	/** 수신처ID */
	public void setRecvDeptId(String recvDeptId) {
		this.recvDeptId = recvDeptId;
	}

	/** 수신처명 */
	public String getRecvDeptNm() {
		return recvDeptNm;
	}

	/** 수신처명 */
	public void setRecvDeptNm(String recvDeptNm) {
		this.recvDeptNm = recvDeptNm;
	}

	/** 참조처ID */
	public String getRefDeptId() {
		return refDeptId;
	}

	/** 참조처ID */
	public void setRefDeptId(String refDeptId) {
		this.refDeptId = refDeptId;
	}

	/** 참조처명 */
	public String getRefDeptNm() {
		return refDeptNm;
	}

	/** 참조처명 */
	public void setRefDeptNm(String refDeptNm) {
		this.refDeptNm = refDeptNm;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 수신처구분명 */
	public String getRecvDeptTypNm() {
		return recvDeptTypNm;
	}

	/** 수신처구분명 */
	public void setRecvDeptTypNm(String recvDeptTypNm) {
		this.recvDeptTypNm = recvDeptTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRecvGrpDDao.selectApRecvGrpD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRecvGrpDDao.insertApRecvGrpD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRecvGrpDDao.updateApRecvGrpD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRecvGrpDDao.deleteApRecvGrpD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApRecvGrpDDao.countApRecvGrpD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":수신그룹상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(recvGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("recvGrpId(수신그룹ID-PK):").append(recvGrpId).append('\n'); }
		if(recvGrpSeq!=null) { if(tab!=null) builder.append(tab); builder.append("recvGrpSeq(수신그룹일련번호-PK):").append(recvGrpSeq).append('\n'); }
		if(recvDeptTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptTypCd(수신처구분코드):").append(recvDeptTypCd).append('\n'); }
		if(recvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptId(수신처ID):").append(recvDeptId).append('\n'); }
		if(recvDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptNm(수신처명):").append(recvDeptNm).append('\n'); }
		if(refDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("refDeptId(참조처ID):").append(refDeptId).append('\n'); }
		if(refDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("refDeptNm(참조처명):").append(refDeptNm).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(recvDeptTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptTypNm(수신처구분명):").append(recvDeptTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
