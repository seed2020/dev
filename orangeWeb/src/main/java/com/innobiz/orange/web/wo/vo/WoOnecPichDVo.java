package com.innobiz.orange.web.wo.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 원카드담당자상세(WO_ONEC_PICH_D) 테이블 VO
 */
public class WoOnecPichDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4092099422090340659L;

	/** 원카드번호 - KEY */
	private String onecNo;

	/** 담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송 */
	private String pichTypCd;

	/** 사용자UID - KEY */
	private String userUid;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 사용자명 */
	private String userNm;

	/** 원카드번호 - KEY */
	public String getOnecNo() {
		return onecNo;
	}

	/** 원카드번호 - KEY */
	public void setOnecNo(String onecNo) {
		this.onecNo = onecNo;
	}

	/** 담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송 */
	public String getPichTypCd() {
		return pichTypCd;
	}

	/** 담당자구분코드 - KEY - reqCensr:심사요청, censr:심사, send:발송 */
	public void setPichTypCd(String pichTypCd) {
		this.pichTypCd = pichTypCd;
	}

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
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
			return "com.innobiz.orange.web.wo.dao.WoOnecPichDDao.selectWoOnecPichD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecPichDDao.insertWoOnecPichD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecPichDDao.updateWoOnecPichD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecPichDDao.deleteWoOnecPichD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecPichDDao.countWoOnecPichD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원카드담당자상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(onecNo!=null) { if(tab!=null) builder.append(tab); builder.append("onecNo(원카드번호-PK):").append(onecNo).append('\n'); }
		if(pichTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pichTypCd(담당자구분코드-PK):").append(pichTypCd).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
