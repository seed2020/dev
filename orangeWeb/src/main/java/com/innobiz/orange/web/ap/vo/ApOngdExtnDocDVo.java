package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행문서외부문서상세(AP_ONGD_EXTN_DOC_D) 테이블 VO
 */
public class ApOngdExtnDocDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4792526470561401554L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 외부문서구분코드 - extnRecv:외부문서접수, paperApv:서면결재 */
	private String extnDocTypCd;

	/** 외부문서컨텐츠구분코드 - doc:문서, photo:사진, blueprint:도면 */
	private String extnDocContTypCd;

	/** 담당자UID */
	private String pichUid;

	/** 담당자명 */
	private String pichNm;

	/** 생산년도 */
	private String makYy;

	/** 발신자명 */
	private String sendrNm;

	/** 페이지수 */
	private String pageCnt;

	/** 생산지역명 */
	private String makZoneNm;

	/** 원본결재번호 */
	private String orgnApvNo;

	/** 등록일련번호 */
	private String regSeq;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 외부문서구분명 */
	private String extnDocTypNm;

	/** 외부문서컨텐츠구분명 */
	private String extnDocContTypNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 외부문서구분코드 - extnRecv:외부문서접수, paperApv:서면결재 */
	public String getExtnDocTypCd() {
		return extnDocTypCd;
	}

	/** 외부문서구분코드 - extnRecv:외부문서접수, paperApv:서면결재 */
	public void setExtnDocTypCd(String extnDocTypCd) {
		this.extnDocTypCd = extnDocTypCd;
	}

	/** 외부문서컨텐츠구분코드 - doc:문서, photo:사진, blueprint:도면 */
	public String getExtnDocContTypCd() {
		return extnDocContTypCd;
	}

	/** 외부문서컨텐츠구분코드 - doc:문서, photo:사진, blueprint:도면 */
	public void setExtnDocContTypCd(String extnDocContTypCd) {
		this.extnDocContTypCd = extnDocContTypCd;
	}

	/** 담당자UID */
	public String getPichUid() {
		return pichUid;
	}

	/** 담당자UID */
	public void setPichUid(String pichUid) {
		this.pichUid = pichUid;
	}

	/** 담당자명 */
	public String getPichNm() {
		return pichNm;
	}

	/** 담당자명 */
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
	}

	/** 생산년도 */
	public String getMakYy() {
		return makYy;
	}

	/** 생산년도 */
	public void setMakYy(String makYy) {
		this.makYy = makYy;
	}

	/** 발신자명 */
	public String getSendrNm() {
		return sendrNm;
	}

	/** 발신자명 */
	public void setSendrNm(String sendrNm) {
		this.sendrNm = sendrNm;
	}

	/** 페이지수 */
	public String getPageCnt() {
		return pageCnt;
	}

	/** 페이지수 */
	public void setPageCnt(String pageCnt) {
		this.pageCnt = pageCnt;
	}

	/** 생산지역명 */
	public String getMakZoneNm() {
		return makZoneNm;
	}

	/** 생산지역명 */
	public void setMakZoneNm(String makZoneNm) {
		this.makZoneNm = makZoneNm;
	}

	/** 원본결재번호 */
	public String getOrgnApvNo() {
		return orgnApvNo;
	}

	/** 원본결재번호 */
	public void setOrgnApvNo(String orgnApvNo) {
		this.orgnApvNo = orgnApvNo;
	}

	/** 등록일련번호 */
	public String getRegSeq() {
		return regSeq;
	}

	/** 등록일련번호 */
	public void setRegSeq(String regSeq) {
		this.regSeq = regSeq;
	}

	/** 스토리지 리턴 */
	public String getStorage(){
		return storage == null ? "ONGD" : storage;
	}

	/** 스토리지 세팅 - ONGD, 년도4자리 */
	public void setStorage(String storage) {
		if(storage==null || storage.isEmpty()) this.storage = null;
		else if(storage.equals("ONGD") || storage.matches("[0-9A-Z]{4}")) this.storage = storage;
	}

	/** 외부문서구분명 */
	public String getExtnDocTypNm() {
		return extnDocTypNm;
	}

	/** 외부문서구분명 */
	public void setExtnDocTypNm(String extnDocTypNm) {
		this.extnDocTypNm = extnDocTypNm;
	}

	/** 외부문서컨텐츠구분명 */
	public String getExtnDocContTypNm() {
		return extnDocContTypNm;
	}

	/** 외부문서컨텐츠구분명 */
	public void setExtnDocContTypNm(String extnDocContTypNm) {
		this.extnDocContTypNm = extnDocContTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExtnDocDDao.selectApOngdExtnDocD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExtnDocDDao.insertApOngdExtnDocD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExtnDocDDao.updateApOngdExtnDocD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExtnDocDDao.deleteApOngdExtnDocD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdExtnDocDDao.countApOngdExtnDocD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서외부문서상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_EXTN_DOC_D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(extnDocTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("extnDocTypCd(외부문서구분코드):").append(extnDocTypCd).append('\n'); }
		if(extnDocContTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("extnDocContTypCd(외부문서컨텐츠구분코드):").append(extnDocContTypCd).append('\n'); }
		if(pichUid!=null) { if(tab!=null) builder.append(tab); builder.append("pichUid(담당자UID):").append(pichUid).append('\n'); }
		if(pichNm!=null) { if(tab!=null) builder.append(tab); builder.append("pichNm(담당자명):").append(pichNm).append('\n'); }
		if(makYy!=null) { if(tab!=null) builder.append(tab); builder.append("makYy(생산년도):").append(makYy).append('\n'); }
		if(sendrNm!=null) { if(tab!=null) builder.append(tab); builder.append("sendrNm(발신자명):").append(sendrNm).append('\n'); }
		if(pageCnt!=null) { if(tab!=null) builder.append(tab); builder.append("pageCnt(페이지수):").append(pageCnt).append('\n'); }
		if(makZoneNm!=null) { if(tab!=null) builder.append(tab); builder.append("makZoneNm(생산지역명):").append(makZoneNm).append('\n'); }
		if(orgnApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("orgnApvNo(원본결재번호):").append(orgnApvNo).append('\n'); }
		if(regSeq!=null) { if(tab!=null) builder.append(tab); builder.append("regSeq(등록일련번호):").append(regSeq).append('\n'); }
		if(extnDocTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("extnDocTypNm(외부문서구분명):").append(extnDocTypNm).append('\n'); }
		if(extnDocContTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("extnDocContTypNm(외부문서컨텐츠구분명):").append(extnDocContTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
