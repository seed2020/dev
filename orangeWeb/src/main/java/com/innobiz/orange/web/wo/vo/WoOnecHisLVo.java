package com.innobiz.orange.web.wo.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 원카드이력내역(WO_ONEC_HIS_L) 테이블 VO
 */
public class WoOnecHisLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5969320632840071708L;

	/** 원카드번호 - KEY */
	private String onecNo;

	/** 이력번호 - KEY */
	private String hisNo;

	/** 이력등록일시 */
	private String hisRegDt;

	/** 이력내용 */
	private String hisCont;


	// 추가컬럼
	/** 최소이력번호 */
	private String minHisNo;

	/** 원카드번호 - KEY */
	public String getOnecNo() {
		return onecNo;
	}

	/** 원카드번호 - KEY */
	public void setOnecNo(String onecNo) {
		this.onecNo = onecNo;
	}

	/** 이력번호 - KEY */
	public String getHisNo() {
		return hisNo;
	}

	/** 이력번호 - KEY */
	public void setHisNo(String hisNo) {
		this.hisNo = hisNo;
	}

	/** 이력등록일시 */
	public String getHisRegDt() {
		return hisRegDt;
	}

	/** 이력등록일시 */
	public void setHisRegDt(String hisRegDt) {
		this.hisRegDt = hisRegDt;
	}

	/** 이력내용 */
	public String getHisCont() {
		return hisCont;
	}

	/** 이력내용 */
	public void setHisCont(String hisCont) {
		this.hisCont = hisCont;
	}

	/** 최소이력번호 */
	public String getMinHisNo() {
		return minHisNo;
	}

	/** 최소이력번호 */
	public void setMinHisNo(String minHisNo) {
		this.minHisNo = minHisNo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecHisLDao.selectWoOnecHisL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecHisLDao.insertWoOnecHisL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecHisLDao.updateWoOnecHisL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecHisLDao.deleteWoOnecHisL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecHisLDao.countWoOnecHisL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원카드이력내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(onecNo!=null) { if(tab!=null) builder.append(tab); builder.append("onecNo(원카드번호-PK):").append(onecNo).append('\n'); }
		if(hisNo!=null) { if(tab!=null) builder.append(tab); builder.append("hisNo(이력번호-PK):").append(hisNo).append('\n'); }
		if(hisRegDt!=null) { if(tab!=null) builder.append(tab); builder.append("hisRegDt(이력등록일시):").append(hisRegDt).append('\n'); }
		if(hisCont!=null) { if(tab!=null) builder.append(tab); builder.append("hisCont(이력내용):").append(hisCont).append('\n'); }
		if(minHisNo!=null) { if(tab!=null) builder.append(tab); builder.append("minHisNo(최소이력번호):").append(minHisNo).append('\n'); }
		super.toString(builder, tab);
	}
}
