package com.innobiz.orange.web.wd.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 연차사용내역(WD_ANB_USE_L) 테이블 VO
 */
public class WdAnbUseLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 279623002623109269L;

	/** 기준년도 - KEY */
	private String year;

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	private String anbTypCd;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 사용년월일 - KEY */
	private String useYmd;

	/** 완결여부 */
	private String cmplYn;

	/** 사용수 */
	private String useCnt;

	/** 등록일 */
	private String regDt;

	/** 결재번호 */
	private String apvNo;

	/** 근거 */
	private String rson;

	/** 기준년도 - KEY */
	public String getYear() {
		return year;
	}

	/** 기준년도 - KEY */
	public void setYear(String year) {
		this.year = year;
	}

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	public String getAnbTypCd() {
		return anbTypCd;
	}

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	public void setAnbTypCd(String anbTypCd) {
		this.anbTypCd = anbTypCd;
	}

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 사용년월일 - KEY */
	public String getUseYmd() {
		return useYmd;
	}

	/** 사용년월일 - KEY */
	public void setUseYmd(String useYmd) {
		this.useYmd = useYmd;
	}

	/** 완결여부 */
	public String getCmplYn() {
		return cmplYn;
	}

	/** 완결여부 */
	public void setCmplYn(String cmplYn) {
		this.cmplYn = cmplYn;
	}

	/** 사용수 */
	public String getUseCnt() {
		return useCnt;
	}

	/** 사용수 */
	public void setUseCnt(String useCnt) {
		this.useCnt = useCnt;
	}

	/** 등록일 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 결재번호 */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 근거 */
	public String getRson() {
		return rson;
	}

	/** 근거 */
	public void setRson(String rson) {
		this.rson = rson;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbUseLDao.selectWdAnbUseL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbUseLDao.insertWdAnbUseL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbUseLDao.updateWdAnbUseL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbUseLDao.deleteWdAnbUseL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbUseLDao.countWdAnbUseL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":연차사용내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(year!=null) { if(tab!=null) builder.append(tab); builder.append("year(기준년도-PK):").append(year).append('\n'); }
		if(anbTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("anbTypCd(연차구분코드-PK):").append(anbTypCd).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(useYmd!=null) { if(tab!=null) builder.append(tab); builder.append("useYmd(사용년월일-PK):").append(useYmd).append('\n'); }
		if(cmplYn!=null) { if(tab!=null) builder.append(tab); builder.append("cmplYn(완결여부):").append(cmplYn).append('\n'); }
		if(useCnt!=null) { if(tab!=null) builder.append(tab); builder.append("useCnt(사용수):").append(useCnt).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일):").append(regDt).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호):").append(apvNo).append('\n'); }
		if(rson!=null) { if(tab!=null) builder.append(tab); builder.append("rson(근거):").append(rson).append('\n'); }
		super.toString(builder, tab);
	}
}
