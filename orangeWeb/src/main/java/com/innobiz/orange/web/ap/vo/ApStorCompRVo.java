package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 저장소회사관계(AP_STOR_COMP_R) 테이블 VO
 */
public class ApStorCompRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7071805333269337364L;

	/** 저장소ID - KEY */
	private String storId;

	/** 회사ID - KEY */
	private String compId;

	/** 저장소리소스ID */
	private String storRescId;

	/** 회사문서건수 */
	private String compDocCnt;

	/** 사용여부 */
	private String useYn;


	// 추가컬럼
	/** 전체문서건수 */
	private String allDocCnt;

	/** 저장소리소스명 */
	private String storRescNm;

	/** 저장소ID - KEY */
	public String getStorId() {
		return storId;
	}

	/** 저장소ID - KEY */
	public void setStorId(String storId) {
		this.storId = storId;
	}

	/** 회사ID - KEY */
	public String getCompId() {
		return compId;
	}

	/** 회사ID - KEY */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 저장소리소스ID */
	public String getStorRescId() {
		return storRescId;
	}

	/** 저장소리소스ID */
	public void setStorRescId(String storRescId) {
		this.storRescId = storRescId;
	}

	/** 회사문서건수 */
	public String getCompDocCnt() {
		return compDocCnt;
	}

	/** 회사문서건수 */
	public void setCompDocCnt(String compDocCnt) {
		this.compDocCnt = compDocCnt;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 전체문서건수 */
	public String getAllDocCnt() {
		return allDocCnt;
	}

	/** 전체문서건수 */
	public void setAllDocCnt(String allDocCnt) {
		this.allDocCnt = allDocCnt;
	}

	/** 저장소리소스명 */
	public String getStorRescNm() {
		return storRescNm;
	}

	/** 저장소리소스명 */
	public void setStorRescNm(String storRescNm) {
		this.storRescNm = storRescNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorCompRDao.selectApStorCompR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorCompRDao.insertApStorCompR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorCompRDao.updateApStorCompR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorCompRDao.deleteApStorCompR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApStorCompRDao.countApStorCompR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":저장소회사관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID-PK):").append(storId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID-PK):").append(compId).append('\n'); }
		if(storRescId!=null) { if(tab!=null) builder.append(tab); builder.append("storRescId(저장소리소스ID):").append(storRescId).append('\n'); }
		if(compDocCnt!=null) { if(tab!=null) builder.append(tab); builder.append("compDocCnt(회사문서건수):").append(compDocCnt).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(allDocCnt!=null) { if(tab!=null) builder.append(tab); builder.append("allDocCnt(전체문서건수):").append(allDocCnt).append('\n'); }
		if(storRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("storRescNm(저장소리소스명):").append(storRescNm).append('\n'); }
		super.toString(builder, tab);
	}
}
