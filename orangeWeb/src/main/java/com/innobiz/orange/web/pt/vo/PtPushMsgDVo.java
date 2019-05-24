package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 푸쉬메시지상세(PT_PUSH_MSG_D) 테이블 VO
 */
public class PtPushMsgDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5832150060242069051L;

	/** 푸쉬메시지ID - KEY */
	private String pushMsgId;

	/** 모듈ID */
	private String mdId;

	/** 모듈참조ID */
	private String mdRid;

	/** 푸쉬제목 */
	private String pushSubj;

	/** 웹URL */
	private String webUrl;

	/** 웹권한URL */
	private String webAuthUrl;

	/** 모바일URL */
	private String mobUrl;

	/** 모방일권한URL */
	private String mobAuthUrl;

	/** 사용자UID */
	private String userUid;

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 발행일시 */
	private String isuDt;

	/** 유효로그인수 */
	private String valdLginCnt;


	// 추가컬럼
	/** 휴대전화번호 */
	private String mbno;

	/** 유효여부 */
	private Boolean valid;

	/** 최종시도시간 */
	private Long lastTryTime;

	/** 사용자UID 목록 */
	private List<String> userUidList;

	/** 회사ID */
	private String compId;

	/** 최소발행일시 */
	private String minIsuDt;

	/** 최대발행일시 */
	private String maxIsuDt;

	/** 사용자명 */
	private String userNm;

	/** 푸쉬메시지ID - KEY */
	public String getPushMsgId() {
		return pushMsgId;
	}

	/** 푸쉬메시지ID - KEY */
	public void setPushMsgId(String pushMsgId) {
		this.pushMsgId = pushMsgId;
	}

	/** 모듈ID */
	public String getMdId() {
		return mdId;
	}

	/** 모듈ID */
	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	/** 모듈참조ID */
	public String getMdRid() {
		return mdRid;
	}

	/** 모듈참조ID */
	public void setMdRid(String mdRid) {
		this.mdRid = mdRid;
	}

	/** 푸쉬제목 */
	public String getPushSubj() {
		return pushSubj;
	}

	/** 푸쉬제목 */
	public void setPushSubj(String pushSubj) {
		this.pushSubj = pushSubj;
	}

	/** 웹URL */
	public String getWebUrl() {
		return webUrl;
	}

	/** 웹URL */
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	/** 웹권한URL */
	public String getWebAuthUrl() {
		return webAuthUrl;
	}

	/** 웹권한URL */
	public void setWebAuthUrl(String webAuthUrl) {
		this.webAuthUrl = webAuthUrl;
	}

	/** 모바일URL */
	public String getMobUrl() {
		return mobUrl;
	}

	/** 모바일URL */
	public void setMobUrl(String mobUrl) {
		this.mobUrl = mobUrl;
	}

	/** 모방일권한URL */
	public String getMobAuthUrl() {
		return mobAuthUrl;
	}

	/** 모방일권한URL */
	public void setMobAuthUrl(String mobAuthUrl) {
		this.mobAuthUrl = mobAuthUrl;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 발행일시 */
	public String getIsuDt() {
		return isuDt;
	}

	/** 발행일시 */
	public void setIsuDt(String isuDt) {
		this.isuDt = isuDt;
	}

	/** 유효로그인수 */
	public String getValdLginCnt() {
		return valdLginCnt;
	}

	/** 유효로그인수 */
	public void setValdLginCnt(String valdLginCnt) {
		this.valdLginCnt = valdLginCnt;
	}

	/** 휴대전화번호 */
	public String getMbno() {
		return mbno;
	}

	/** 휴대전화번호 */
	public void setMbno(String mbno) {
		this.mbno = mbno;
	}

	/** 유효여부 */
	public Boolean getValid() {
		return valid;
	}

	/** 유효여부 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	/** 최종시도시간 */
	public Long getLastTryTime() {
		return lastTryTime;
	}

	/** 최종시도시간 */
	public void setLastTryTime(Long lastTryTime) {
		this.lastTryTime = lastTryTime;
	}

	/** 사용자UID 목록 */
	public List<String> getUserUidList() {
		return userUidList;
	}

	/** 사용자UID 목록 */
	public void setUserUidList(List<String> userUidList) {
		this.userUidList = userUidList;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 최소발행일시 */
	public String getMinIsuDt() {
		return minIsuDt;
	}

	/** 최소발행일시 */
	public void setMinIsuDt(String minIsuDt) {
		this.minIsuDt = minIsuDt;
	}

	/** 최대발행일시 */
	public String getMaxIsuDt() {
		return maxIsuDt;
	}

	/** 최대발행일시 */
	public void setMaxIsuDt(String maxIsuDt) {
		this.maxIsuDt = maxIsuDt;
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
			return "com.innobiz.orange.web.pt.dao.PtPushMsgDDao.selectPtPushMsgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushMsgDDao.insertPtPushMsgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushMsgDDao.updatePtPushMsgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushMsgDDao.deletePtPushMsgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtPushMsgDDao.countPtPushMsgD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":푸쉬메시지상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(pushMsgId!=null) { if(tab!=null) builder.append(tab); builder.append("pushMsgId(푸쉬메시지ID-PK):").append(pushMsgId).append('\n'); }
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(mdRid!=null) { if(tab!=null) builder.append(tab); builder.append("mdRid(모듈참조ID):").append(mdRid).append('\n'); }
		if(pushSubj!=null) { if(tab!=null) builder.append(tab); builder.append("pushSubj(푸쉬제목):").append(pushSubj).append('\n'); }
		if(webUrl!=null) { if(tab!=null) builder.append(tab); builder.append("webUrl(웹URL):").append(webUrl).append('\n'); }
		if(webAuthUrl!=null) { if(tab!=null) builder.append(tab); builder.append("webAuthUrl(웹권한URL):").append(webAuthUrl).append('\n'); }
		if(mobUrl!=null) { if(tab!=null) builder.append(tab); builder.append("mobUrl(모바일URL):").append(mobUrl).append('\n'); }
		if(mobAuthUrl!=null) { if(tab!=null) builder.append(tab); builder.append("mobAuthUrl(모방일권한URL):").append(mobAuthUrl).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드):").append(langTypCd).append('\n'); }
		if(isuDt!=null) { if(tab!=null) builder.append(tab); builder.append("isuDt(발행일시):").append(isuDt).append('\n'); }
		if(valdLginCnt!=null) { if(tab!=null) builder.append(tab); builder.append("valdLginCnt(유효로그인수):").append(valdLginCnt).append('\n'); }
		if(mbno!=null) { if(tab!=null) builder.append(tab); builder.append("mbno(휴대전화번호):").append(mbno).append('\n'); }
		if(valid!=null) { if(tab!=null) builder.append(tab); builder.append("valid(유효여부):").append(valid).append('\n'); }
		if(lastTryTime!=null) { if(tab!=null) builder.append(tab); builder.append("lastTryTime(최종시도시간):").append(lastTryTime).append('\n'); }
		if(userUidList!=null) { if(tab!=null) builder.append(tab); builder.append("userUidList(사용자UID 목록):"); appendStringListTo(builder, userUidList, tab); builder.append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(minIsuDt!=null) { if(tab!=null) builder.append(tab); builder.append("minIsuDt(최소발행일시):").append(minIsuDt).append('\n'); }
		if(maxIsuDt!=null) { if(tab!=null) builder.append(tab); builder.append("maxIsuDt(최대발행일시):").append(maxIsuDt).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		super.toString(builder, tab);
	}
}
