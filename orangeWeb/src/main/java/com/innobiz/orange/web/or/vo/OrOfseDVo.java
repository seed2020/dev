package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 관인상세(OR_OFSE_D) 테이블 VO
 */
public class OrOfseDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1624298508409044157L;

	/** 조직ID - KEY */
	private String orgId;

	/** 일련번호 - KEY */
	private String seq;

	/** 관인구분코드 - 01:관인(기관), 02:서명인(부서) */
	private String ofseTypCd;

	/** 관인명 */
	private String ofseNm;

	/** 리소스ID */
	private String rescId;

	/** 기본관인여부 */
	private String dftOfseYn;

	/** 폐기여부 */
	private String disuYn;

	/** 이미지경로 */
	private String imgPath;

	/** 이미지넓이 */
	private String imgWdth;

	/** 이미지높이 */
	private String imgHght;

	/** 변경사유 */
	private String chnRson;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 폐기자UID */
	private String disurUid;

	/** 폐기일시 */
	private String disuDt;


	// 추가컬럼
	/** 회사ID */
	private String compId;

	/** 관인구분명 */
	private String ofseTypNm;

	/** 리소스명 */
	private String rescNm;

	/** 등록자명 */
	private String regrNm;

	/** 폐기자명 */
	private String disurNm;

	/** 조직ID - KEY */
	public String getOrgId() {
		return orgId;
	}

	/** 조직ID - KEY */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 관인구분코드 - 01:관인(기관), 02:서명인(부서) */
	public String getOfseTypCd() {
		return ofseTypCd;
	}

	/** 관인구분코드 - 01:관인(기관), 02:서명인(부서) */
	public void setOfseTypCd(String ofseTypCd) {
		this.ofseTypCd = ofseTypCd;
	}

	/** 관인명 */
	public String getOfseNm() {
		return ofseNm;
	}

	/** 관인명 */
	public void setOfseNm(String ofseNm) {
		this.ofseNm = ofseNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 기본관인여부 */
	public String getDftOfseYn() {
		return dftOfseYn;
	}

	/** 기본관인여부 */
	public void setDftOfseYn(String dftOfseYn) {
		this.dftOfseYn = dftOfseYn;
	}

	/** 폐기여부 */
	public String getDisuYn() {
		return disuYn;
	}

	/** 폐기여부 */
	public void setDisuYn(String disuYn) {
		this.disuYn = disuYn;
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

	/** 변경사유 */
	public String getChnRson() {
		return chnRson;
	}

	/** 변경사유 */
	public void setChnRson(String chnRson) {
		this.chnRson = chnRson;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 폐기자UID */
	public String getDisurUid() {
		return disurUid;
	}

	/** 폐기자UID */
	public void setDisurUid(String disurUid) {
		this.disurUid = disurUid;
	}

	/** 폐기일시 */
	public String getDisuDt() {
		return disuDt;
	}

	/** 폐기일시 */
	public void setDisuDt(String disuDt) {
		this.disuDt = disuDt;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 관인구분명 */
	public String getOfseTypNm() {
		return ofseTypNm;
	}

	/** 관인구분명 */
	public void setOfseTypNm(String ofseTypNm) {
		this.ofseTypNm = ofseTypNm;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 폐기자명 */
	public String getDisurNm() {
		return disurNm;
	}

	/** 폐기자명 */
	public void setDisurNm(String disurNm) {
		this.disurNm = disurNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOfseDDao.selectOrOfseD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOfseDDao.insertOrOfseD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOfseDDao.updateOrOfseD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrOfseDDao.deleteOrOfseD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrOfseDDao.countOrOfseD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":관인상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID-PK):").append(orgId).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(ofseTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("ofseTypCd(관인구분코드):").append(ofseTypCd).append('\n'); }
		if(ofseNm!=null) { if(tab!=null) builder.append(tab); builder.append("ofseNm(관인명):").append(ofseNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(dftOfseYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftOfseYn(기본관인여부):").append(dftOfseYn).append('\n'); }
		if(disuYn!=null) { if(tab!=null) builder.append(tab); builder.append("disuYn(폐기여부):").append(disuYn).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(이미지넓이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(이미지높이):").append(imgHght).append('\n'); }
		if(chnRson!=null) { if(tab!=null) builder.append(tab); builder.append("chnRson(변경사유):").append(chnRson).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(disurUid!=null) { if(tab!=null) builder.append(tab); builder.append("disurUid(폐기자UID):").append(disurUid).append('\n'); }
		if(disuDt!=null) { if(tab!=null) builder.append(tab); builder.append("disuDt(폐기일시):").append(disuDt).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(ofseTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("ofseTypNm(관인구분명):").append(ofseTypNm).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(disurNm!=null) { if(tab!=null) builder.append(tab); builder.append("disurNm(폐기자명):").append(disurNm).append('\n'); }
		super.toString(builder, tab);
	}
}
