package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행양식이미지상세(AP_ONGO_FORM_IMG_D) 테이블 VO
 */
public class ApOngoFormImgDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7559000409183395779L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식일련번호 - KEY */
	private String formSeq;

	/** 양식이미지구분코드 - KEY - docLogo:로고, docSymbol:심볼 */
	private String formImgTypCd;

	/** 이미지경로 */
	private String imgPath;

	/** 이미지넓이 */
	private String imgWdth;

	/** 이미지높이 */
	private String imgHght;


	// 추가컬럼
	/** 양식이미지구분명 */
	private String formImgTypNm;

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

	/** 양식이미지구분코드 - KEY - docLogo:로고, docSymbol:심볼 */
	public String getFormImgTypCd() {
		return formImgTypCd;
	}

	/** 양식이미지구분코드 - KEY - docLogo:로고, docSymbol:심볼 */
	public void setFormImgTypCd(String formImgTypCd) {
		this.formImgTypCd = formImgTypCd;
	}

	/** 이미지경로 */
	public String getImgPath() {
		return imgPath;
	}

	/** 이미지경로 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	/** 이미지넓이 */
	public String getImgWdth() {
		return imgWdth;
	}

	/** 이미지넓이 */
	public void setImgWdth(String imgWdth) {
		this.imgWdth = imgWdth;
	}

	/** 이미지높이 */
	public String getImgHght() {
		return imgHght;
	}

	/** 이미지높이 */
	public void setImgHght(String imgHght) {
		this.imgHght = imgHght;
	}

	/** 양식이미지구분명 */
	public String getFormImgTypNm() {
		return formImgTypNm;
	}

	/** 양식이미지구분명 */
	public void setFormImgTypNm(String formImgTypNm) {
		this.formImgTypNm = formImgTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormImgDDao.selectApOngoFormImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormImgDDao.insertApOngoFormImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormImgDDao.updateApOngoFormImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormImgDDao.deleteApOngoFormImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormImgDDao.countApOngoFormImgD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행양식이미지상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formSeq(양식일련번호-PK):").append(formSeq).append('\n'); }
		if(formImgTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formImgTypCd(양식이미지구분코드-PK):").append(formImgTypCd).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(이미지넓이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(이미지높이):").append(imgHght).append('\n'); }
		if(formImgTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formImgTypNm(양식이미지구분명):").append(formImgTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
