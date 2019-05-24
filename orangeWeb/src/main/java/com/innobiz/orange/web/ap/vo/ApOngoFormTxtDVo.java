package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행양식텍스트상세(AP_ONGO_FORM_TXT_D) 테이블 VO
 */
public class ApOngoFormTxtDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 534958587456326539L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식일련번호 - KEY */
	private String formSeq;

	/** 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글 */
	private String formTxtTypCd;

	/** 텍스트내용 */
	private String txtCont;

	/** 텍스트폰트값 */
	private String txtFontVa;

	/** 텍스트스타일값 */
	private String txtStylVa;

	/** 텍스트크기 */
	private String txtSize;

	/** 텍스트색상값 */
	private String txtColrVa;


	// 추가컬럼
	/** 양식텍스트구분명 */
	private String formTxtTypNm;

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

	/** 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글 */
	public String getFormTxtTypCd() {
		return formTxtTypCd;
	}

	/** 양식텍스트구분코드 - KEY - docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글 */
	public void setFormTxtTypCd(String formTxtTypCd) {
		this.formTxtTypCd = formTxtTypCd;
	}

	/** 텍스트내용 */
	public String getTxtCont() {
		return txtCont;
	}

	/** 텍스트내용 */
	public void setTxtCont(String txtCont) {
		this.txtCont = txtCont;
	}

	/** 텍스트폰트값 */
	public String getTxtFontVa() {
		return txtFontVa;
	}

	/** 텍스트폰트값 */
	public void setTxtFontVa(String txtFontVa) {
		this.txtFontVa = txtFontVa;
	}

	/** 텍스트스타일값 */
	public String getTxtStylVa() {
		return txtStylVa;
	}

	/** 텍스트스타일값 */
	public void setTxtStylVa(String txtStylVa) {
		this.txtStylVa = txtStylVa;
	}

	/** 텍스트크기 */
	public String getTxtSize() {
		return txtSize;
	}

	/** 텍스트크기 */
	public void setTxtSize(String txtSize) {
		this.txtSize = txtSize;
	}

	/** 텍스트색상값 */
	public String getTxtColrVa() {
		return txtColrVa;
	}

	/** 텍스트색상값 */
	public void setTxtColrVa(String txtColrVa) {
		this.txtColrVa = txtColrVa;
	}

	/** 양식텍스트구분명 */
	public String getFormTxtTypNm() {
		return formTxtTypNm;
	}

	/** 양식텍스트구분명 */
	public void setFormTxtTypNm(String formTxtTypNm) {
		this.formTxtTypNm = formTxtTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormTxtDDao.selectApOngoFormTxtD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormTxtDDao.insertApOngoFormTxtD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormTxtDDao.updateApOngoFormTxtD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormTxtDDao.deleteApOngoFormTxtD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormTxtDDao.countApOngoFormTxtD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행양식텍스트상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formSeq(양식일련번호-PK):").append(formSeq).append('\n'); }
		if(formTxtTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formTxtTypCd(양식텍스트구분코드-PK):").append(formTxtTypCd).append('\n'); }
		if(txtCont!=null) { if(tab!=null) builder.append(tab); builder.append("txtCont(텍스트내용):").append(txtCont).append('\n'); }
		if(txtFontVa!=null) { if(tab!=null) builder.append(tab); builder.append("txtFontVa(텍스트폰트값):").append(txtFontVa).append('\n'); }
		if(txtStylVa!=null) { if(tab!=null) builder.append(tab); builder.append("txtStylVa(텍스트스타일값):").append(txtStylVa).append('\n'); }
		if(txtSize!=null) { if(tab!=null) builder.append(tab); builder.append("txtSize(텍스트크기):").append(txtSize).append('\n'); }
		if(txtColrVa!=null) { if(tab!=null) builder.append(tab); builder.append("txtColrVa(텍스트색상값):").append(txtColrVa).append('\n'); }
		if(formTxtTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formTxtTypNm(양식텍스트구분명):").append(formTxtTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
