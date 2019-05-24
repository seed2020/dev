package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 로그인이미지상세(PT_LGIN_IMG_D) 테이블 VO
 */
public class PtLginImgDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2640785431620865742L;

	/** 로그인이미지ID - KEY */
	private String lginImgId;

	/** 리소스ID */
	private String rescId;

	/** 로그인이미지명 */
	private String lginImgNm;

	/** 시작년월일 */
	private String strtYmd;

	/** 종료년월일 */
	private String endYmd;

	/** 기본이미지여부 */
	private String dftImgYn;

	/** TOP픽셀 */
	private String topPx;

	/** LEFT픽셀 */
	private String leftPx;

	/** 사용여부 */
	private String useYn;

	/** 로고이미지경로 */
	private String logoImgPath;

	/** 배경이미지경로 */
	private String bgImgPath;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 리소스명 */
	private String rescNm;

	/** 수정자명 */
	private String modrNm;

	/** 로그인이미지ID - KEY */
	public String getLginImgId() {
		return lginImgId;
	}

	/** 로그인이미지ID - KEY */
	public void setLginImgId(String lginImgId) {
		this.lginImgId = lginImgId;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 로그인이미지명 */
	public String getLginImgNm() {
		return lginImgNm;
	}

	/** 로그인이미지명 */
	public void setLginImgNm(String lginImgNm) {
		this.lginImgNm = lginImgNm;
	}

	/** 시작년월일 */
	public String getStrtYmd() {
		return strtYmd;
	}

	/** 시작년월일 */
	public void setStrtYmd(String strtYmd) {
		this.strtYmd = strtYmd;
	}

	/** 종료년월일 */
	public String getEndYmd() {
		return endYmd;
	}

	/** 종료년월일 */
	public void setEndYmd(String endYmd) {
		this.endYmd = endYmd;
	}

	/** 기본이미지여부 */
	public String getDftImgYn() {
		return dftImgYn;
	}

	/** 기본이미지여부 */
	public void setDftImgYn(String dftImgYn) {
		this.dftImgYn = dftImgYn;
	}

	/** TOP픽셀 */
	public String getTopPx() {
		return topPx;
	}

	/** TOP픽셀 */
	public void setTopPx(String topPx) {
		this.topPx = topPx;
	}

	/** LEFT픽셀 */
	public String getLeftPx() {
		return leftPx;
	}

	/** LEFT픽셀 */
	public void setLeftPx(String leftPx) {
		this.leftPx = leftPx;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 로고이미지경로 */
	public String getLogoImgPath() {
		return logoImgPath;
	}

	/** 로고이미지경로 */
	public void setLogoImgPath(String logoImgPath) {
		this.logoImgPath = logoImgPath;
	}

	/** 배경이미지경로 */
	public String getBgImgPath() {
		return bgImgPath;
	}

	/** 배경이미지경로 */
	public void setBgImgPath(String bgImgPath) {
		this.bgImgPath = bgImgPath;
	}

	/** 수정자UID */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자UID */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLginImgDDao.selectPtLginImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLginImgDDao.insertPtLginImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLginImgDDao.updatePtLginImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLginImgDDao.deletePtLginImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtLginImgDDao.countPtLginImgD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":로그인이미지상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(lginImgId!=null) { if(tab!=null) builder.append(tab); builder.append("lginImgId(로그인이미지ID-PK):").append(lginImgId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(lginImgNm!=null) { if(tab!=null) builder.append(tab); builder.append("lginImgNm(로그인이미지명):").append(lginImgNm).append('\n'); }
		if(strtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if(endYmd!=null) { if(tab!=null) builder.append(tab); builder.append("endYmd(종료년월일):").append(endYmd).append('\n'); }
		if(dftImgYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftImgYn(기본이미지여부):").append(dftImgYn).append('\n'); }
		if(topPx!=null) { if(tab!=null) builder.append(tab); builder.append("topPx(TOP픽셀):").append(topPx).append('\n'); }
		if(leftPx!=null) { if(tab!=null) builder.append(tab); builder.append("leftPx(LEFT픽셀):").append(leftPx).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(logoImgPath!=null) { if(tab!=null) builder.append(tab); builder.append("logoImgPath(로고이미지경로):").append(logoImgPath).append('\n'); }
		if(bgImgPath!=null) { if(tab!=null) builder.append(tab); builder.append("bgImgPath(배경이미지경로):").append(bgImgPath).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
