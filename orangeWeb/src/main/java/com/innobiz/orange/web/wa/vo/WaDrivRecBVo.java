package com.innobiz.orange.web.wa.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/01/02 11:50 ******/
/**
* 운행기록기본(WA_DRIV_REC_B) 테이블 VO 
*/
public class WaDrivRecBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6118138320069099104L;

	/** 기록번호 */ 
	private Integer recNo;

 	/** 회사ID */ 
	private String compId;

 	/** 법인번호 */ 
	private String corpNo;

 	/** 차종번호 */ 
	private String carKndNo;

 	/** 시작일 */ 
	private String strtDt;

 	/** 종료일 */ 
	private String endDt;

 	/** 본문XML */ 
	private String bodyXml;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 추가 */
	/** 법인명 */ 
	private String corpNm;
	
	/** 사업자번호 */ 
	private String corpRegNo;
	
	/** 차종명 */ 
	private String carKndNm;
	
	/** 자동차등록번호 */ 
	private String carRegNo;
	
 	public void setRecNo(Integer recNo) { 
		this.recNo = recNo;
	}
	/** 기록번호 */ 
	public Integer getRecNo() { 
		return recNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCorpNo(String corpNo) { 
		this.corpNo = corpNo;
	}
	/** 법인번호 */ 
	public String getCorpNo() { 
		return corpNo;
	}

	public void setCarKndNo(String carKndNo) { 
		this.carKndNo = carKndNo;
	}
	/** 차종번호 */ 
	public String getCarKndNo() { 
		return carKndNo;
	}

	public void setStrtDt(String strtDt) { 
		this.strtDt = strtDt;
	}
	/** 시작일 */ 
	public String getStrtDt() { 
		return strtDt;
	}

	public void setEndDt(String endDt) { 
		this.endDt = endDt;
	}
	/** 종료일 */ 
	public String getEndDt() { 
		return endDt;
	}

	public void setBodyXml(String bodyXml) { 
		this.bodyXml = bodyXml;
	}
	/** 본문XML */ 
	public String getBodyXml() { 
		return bodyXml;
	}

	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}

	public void setModrUid(String modrUid) { 
		this.modrUid = modrUid;
	}
	/** 수정자UID */ 
	public String getModrUid() { 
		return modrUid;
	}

	public void setModDt(String modDt) { 
		this.modDt = modDt;
	}
	/** 수정일시 */ 
	public String getModDt() { 
		return modDt;
	}
	
	/** 법인명 */ 
	public String getCorpNm() {
		return corpNm;
	}
	public void setCorpNm(String corpNm) {
		this.corpNm = corpNm;
	}
	
	/** 사업자번호 */ 
	public String getCorpRegNo() {
		return corpRegNo;
	}
	public void setCorpRegNo(String corpRegNo) {
		this.corpRegNo = corpRegNo;
	}
	/** 차종명 */ 
	public String getCarKndNm() {
		return carKndNm;
	}
	public void setCarKndNm(String carKndNm) {
		this.carKndNm = carKndNm;
	}
	
	/** 자동차등록번호 */ 
	public String getCarRegNo() {
		return carRegNo;
	}
	public void setCarRegNo(String carRegNo) {
		this.carRegNo = carRegNo;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaDrivRecBDao.selectWaDrivRecB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaDrivRecBDao.insertWaDrivRecB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wa.dao.WaDrivRecBDao.updateWaDrivRecB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wa.dao.WaDrivRecBDao.deleteWaDrivRecB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaDrivRecBDao.countWaDrivRecB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":운행기록기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(recNo!=null) { if(tab!=null) builder.append(tab); builder.append("recNo(기록번호):").append(recNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(corpNo!=null) { if(tab!=null) builder.append(tab); builder.append("corpNo(법인번호):").append(corpNo).append('\n'); }
		if(carKndNo!=null) { if(tab!=null) builder.append(tab); builder.append("carKndNo(차종번호):").append(carKndNo).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일):").append(endDt).append('\n'); }
		if(bodyXml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyXml(본문XML):").append(bodyXml).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
