package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행양식결재라인상세(AP_ONGO_FORM_APV_LN_D) 테이블 VO
 */
public class ApOngoFormApvLnDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1815702303783818762L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식일련번호 - KEY */
	private String formSeq;

	/** 양식구성일련번호 - KEY */
	private String formCombSeq;

	/** 결재라인타이틀구분코드 - KEY - apv:결재, agr:합의, req:신청, prc:처리 */
	private String apvLnTitlTypCd;

	/** 결재라인표시구분코드 - 3row:3줄, 2row:2줄, 1row:1줄 */
	private String apvLnDispTypCd;

	/** 최대개수 */
	private String maxCnt;

	/** 줄맞춤값 */
	private String alnVa;

	/** 테두리선사용여부 */
	private String bordUseYn;

	/** 타이틀사용여부 */
	private String titlUseYn;

	/** 목록중복표시여부 */
	private String lstDupDispYn;


	// 추가컬럼
	/** 최대값 합계 */
	private Integer maxSum;

	/** 결재라인타이틀구분명 */
	private String apvLnTitlTypNm;

	/** 결재라인표시구분명 */
	private String apvLnDispTypNm;

	/** 양식ID - KEY */
	public String getFormId() {
		return formId;
	}

	/** 양식ID - KEY */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 양식일련번호 - KEY */
	public String getFormSeq() {
		return formSeq;
	}

	/** 양식일련번호 - KEY */
	public void setFormSeq(String formSeq) {
		this.formSeq = formSeq;
	}

	/** 양식구성일련번호 - KEY */
	public String getFormCombSeq() {
		return formCombSeq;
	}

	/** 양식구성일련번호 - KEY */
	public void setFormCombSeq(String formCombSeq) {
		this.formCombSeq = formCombSeq;
	}

	/** 결재라인타이틀구분코드 - KEY - apv:결재, agr:합의, req:신청, prc:처리 */
	public String getApvLnTitlTypCd() {
		return apvLnTitlTypCd;
	}

	/** 결재라인타이틀구분코드 - KEY - apv:결재, agr:합의, req:신청, prc:처리 */
	public void setApvLnTitlTypCd(String apvLnTitlTypCd) {
		this.apvLnTitlTypCd = apvLnTitlTypCd;
	}

	/** 결재라인표시구분코드 - 3row:3줄, 2row:2줄, 1row:1줄 */
	public String getApvLnDispTypCd() {
		return apvLnDispTypCd;
	}

	/** 결재라인표시구분코드 - 3row:3줄, 2row:2줄, 1row:1줄 */
	public void setApvLnDispTypCd(String apvLnDispTypCd) {
		this.apvLnDispTypCd = apvLnDispTypCd;
	}

	/** 최대개수 */
	public String getMaxCnt() {
		return maxCnt;
	}

	/** 최대개수 */
	public void setMaxCnt(String maxCnt) {
		this.maxCnt = maxCnt;
	}

	/** 줄맞춤값 */
	public String getAlnVa() {
		return alnVa;
	}

	/** 줄맞춤값 */
	public void setAlnVa(String alnVa) {
		this.alnVa = alnVa;
	}

	/** 테두리선사용여부 */
	public String getBordUseYn() {
		return bordUseYn;
	}

	/** 테두리선사용여부 */
	public void setBordUseYn(String bordUseYn) {
		this.bordUseYn = bordUseYn;
	}

	/** 타이틀사용여부 */
	public String getTitlUseYn() {
		return titlUseYn;
	}

	/** 타이틀사용여부 */
	public void setTitlUseYn(String titlUseYn) {
		this.titlUseYn = titlUseYn;
	}

	/** 목록중복표시여부 */
	public String getLstDupDispYn() {
		return lstDupDispYn;
	}

	/** 목록중복표시여부 */
	public void setLstDupDispYn(String lstDupDispYn) {
		this.lstDupDispYn = lstDupDispYn;
	}

	/** 최대값 합계 */
	public Integer getMaxSum() {
		return maxSum;
	}

	/** 최대값 합계 */
	public void setMaxSum(Integer maxSum) {
		this.maxSum = maxSum;
	}

	/** 결재라인타이틀구분명 */
	public String getApvLnTitlTypNm() {
		return apvLnTitlTypNm;
	}

	/** 결재라인타이틀구분명 */
	public void setApvLnTitlTypNm(String apvLnTitlTypNm) {
		this.apvLnTitlTypNm = apvLnTitlTypNm;
	}

	/** 결재라인표시구분명 */
	public String getApvLnDispTypNm() {
		return apvLnDispTypNm;
	}

	/** 결재라인표시구분명 */
	public void setApvLnDispTypNm(String apvLnDispTypNm) {
		this.apvLnDispTypNm = apvLnDispTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormApvLnDDao.selectApOngoFormApvLnD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormApvLnDDao.insertApOngoFormApvLnD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormApvLnDDao.updateApOngoFormApvLnD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormApvLnDDao.deleteApOngoFormApvLnD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormApvLnDDao.countApOngoFormApvLnD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행양식결재라인상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formSeq(양식일련번호-PK):").append(formSeq).append('\n'); }
		if(formCombSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formCombSeq(양식구성일련번호-PK):").append(formCombSeq).append('\n'); }
		if(apvLnTitlTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnTitlTypCd(결재라인타이틀구분코드-PK):").append(apvLnTitlTypCd).append('\n'); }
		if(apvLnDispTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnDispTypCd(결재라인표시구분코드):").append(apvLnDispTypCd).append('\n'); }
		if(maxCnt!=null) { if(tab!=null) builder.append(tab); builder.append("maxCnt(최대개수):").append(maxCnt).append('\n'); }
		if(alnVa!=null) { if(tab!=null) builder.append(tab); builder.append("alnVa(줄맞춤값):").append(alnVa).append('\n'); }
		if(bordUseYn!=null) { if(tab!=null) builder.append(tab); builder.append("bordUseYn(테두리선사용여부):").append(bordUseYn).append('\n'); }
		if(titlUseYn!=null) { if(tab!=null) builder.append(tab); builder.append("titlUseYn(타이틀사용여부):").append(titlUseYn).append('\n'); }
		if(lstDupDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("lstDupDispYn(목록중복표시여부):").append(lstDupDispYn).append('\n'); }
		if(maxSum!=null) { if(tab!=null) builder.append(tab); builder.append("maxSum(최대값 합계):").append(maxSum).append('\n'); }
		if(apvLnTitlTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnTitlTypNm(결재라인타이틀구분명):").append(apvLnTitlTypNm).append('\n'); }
		if(apvLnDispTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnDispTypNm(결재라인표시구분명):").append(apvLnDispTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
