package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 진행양식기본(AP_ONGO_FORM_B) 테이블 VO
 */
public class ApOngoFormBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5556826494317575183L;

	/** 양식ID - KEY */
	private String formId;

	/** 양식일련번호 - KEY */
	private String formSeq;

	/** 회사ID */
	private String compId;

	/** 리소스ID */
	private String rescId;

	/** 양식명 */
	private String formNm;

	/** 양식함ID */
	private String formBxId;

	/** 양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용 */
	private String formTypCd;

	/** 연계ID */
	private String intgId;

	/** 정렬순서 */
	private String sortOrdr;

	/** 본문높이픽셀 */
	private String bodyHghtPx;

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	private String formWdthTypCd;

	/** 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재 */
	private String formApvLnTypCd;

	/** 템플릿여부 */
	private String tplYn;

	/** 사용여부 */
	private String useYn;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** ERP양식ID */
	private String erpFormId;

	/** ERP양식구분코드 - xmlFromAp:XML 결재에서 생성,xmlFromErp:XML ERP에서 생성 */
	private String erpFormTypCd;

	/** 문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	private String docKeepPrdCd;

	/** 양식여백값 - 상 우 하 좌(css margin 표현값) */
	private String formMagnVa;

	/** 연계양식여부 */
	private String intgFormYn;

	/** 본문외곽선여부 */
	private String bodyOlineYn;

	/** 결재라인그룹ID */
	private String apvLnGrpId;

	/** 고정결재자여부 */
	private String fixdApvrYn;

	/** 자동결재선코드 */
	private String autoApvLnCd;

	/** 문서제목코드 */
	private String docSubjCd;

	/** 참조열람그룹ID */
	private String refVwGrpId;

	/** 참조열람고정결재자여부 */
	private String refVwFixdApvrYn;

	/** 본문HTML */
	private String bodyHtml;


	// 추가컬럼
	/** 결재번호 */
	private String apvNo;

	/** 리소스명 */
	private String rescNm;

	/** 양식구분명 */
	private String formTypNm;

	/** 양식넓이구분명 */
	private String formWdthTypNm;

	/** 양식결재라인구분명 */
	private String formApvLnTypNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 문서보존기간명 */
	private String docKeepPrdNm;

	/** 자동결재선명 */
	private String autoApvLnNm;

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

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 양식명 */
	public String getFormNm() {
		return formNm;
	}

	/** 양식명 */
	public void setFormNm(String formNm) {
		this.formNm = formNm;
	}

	/** 양식함ID */
	public String getFormBxId() {
		return formBxId;
	}

	/** 양식함ID */
	public void setFormBxId(String formBxId) {
		this.formBxId = formBxId;
	}

	/** 양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용 */
	public String getFormTypCd() {
		return formTypCd;
	}

	/** 양식구분코드 - intro:기안(내부문서), extro:기안(시행겸용), trans:시행변환용 */
	public void setFormTypCd(String formTypCd) {
		this.formTypCd = formTypCd;
	}

	/** 연계ID */
	public String getIntgId() {
		return intgId;
	}

	/** 연계ID */
	public void setIntgId(String intgId) {
		this.intgId = intgId;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 본문높이픽셀 */
	public String getBodyHghtPx() {
		return bodyHghtPx;
	}

	/** 본문높이픽셀 */
	public void setBodyHghtPx(String bodyHghtPx) {
		this.bodyHghtPx = bodyHghtPx;
	}

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	public String getFormWdthTypCd() {
		return formWdthTypCd;
	}

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	public void setFormWdthTypCd(String formWdthTypCd) {
		this.formWdthTypCd = formWdthTypCd;
	}

	/** 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재 */
	public String getFormApvLnTypCd() {
		return formApvLnTypCd;
	}

	/** 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재 */
	public void setFormApvLnTypCd(String formApvLnTypCd) {
		this.formApvLnTypCd = formApvLnTypCd;
	}

	/** 템플릿여부 */
	public String getTplYn() {
		return tplYn;
	}

	/** 템플릿여부 */
	public void setTplYn(String tplYn) {
		this.tplYn = tplYn;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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

	/** ERP양식ID */
	public String getErpFormId() {
		return erpFormId;
	}

	/** ERP양식ID */
	public void setErpFormId(String erpFormId) {
		this.erpFormId = erpFormId;
	}

	/** ERP양식구분코드 - xmlFromAp:XML 결재에서 생성,xmlFromErp:XML ERP에서 생성 */
	public String getErpFormTypCd() {
		return erpFormTypCd;
	}

	/** ERP양식구분코드 - xmlFromAp:XML 결재에서 생성,xmlFromErp:XML ERP에서 생성 */
	public void setErpFormTypCd(String erpFormTypCd) {
		this.erpFormTypCd = erpFormTypCd;
	}

	/** 문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	public String getDocKeepPrdCd() {
		return docKeepPrdCd;
	}

	/** 문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	public void setDocKeepPrdCd(String docKeepPrdCd) {
		this.docKeepPrdCd = docKeepPrdCd;
	}

	/** 양식여백값 - 상 우 하 좌(css margin 표현값) */
	public String getFormMagnVa() {
		return formMagnVa;
	}

	/** 양식여백값 - 상 우 하 좌(css margin 표현값) */
	public void setFormMagnVa(String formMagnVa) {
		this.formMagnVa = formMagnVa;
	}

	/** 연계양식여부 */
	public String getIntgFormYn() {
		return intgFormYn;
	}

	/** 연계양식여부 */
	public void setIntgFormYn(String intgFormYn) {
		this.intgFormYn = intgFormYn;
	}

	/** 본문외곽선여부 */
	public String getBodyOlineYn() {
		return bodyOlineYn;
	}

	/** 본문외곽선여부 */
	public void setBodyOlineYn(String bodyOlineYn) {
		this.bodyOlineYn = bodyOlineYn;
	}

	/** 결재라인그룹ID */
	public String getApvLnGrpId() {
		return apvLnGrpId;
	}

	/** 결재라인그룹ID */
	public void setApvLnGrpId(String apvLnGrpId) {
		this.apvLnGrpId = apvLnGrpId;
	}

	/** 고정결재자여부 */
	public String getFixdApvrYn() {
		return fixdApvrYn;
	}

	/** 고정결재자여부 */
	public void setFixdApvrYn(String fixdApvrYn) {
		this.fixdApvrYn = fixdApvrYn;
	}

	/** 자동결재선코드 */
	public String getAutoApvLnCd() {
		return autoApvLnCd;
	}

	/** 자동결재선코드 */
	public void setAutoApvLnCd(String autoApvLnCd) {
		this.autoApvLnCd = autoApvLnCd;
	}

	/** 문서제목코드 */
	public String getDocSubjCd() {
		return docSubjCd;
	}

	/** 문서제목코드 */
	public void setDocSubjCd(String docSubjCd) {
		this.docSubjCd = docSubjCd;
	}

	/** 참조열람그룹ID */
	public String getRefVwGrpId() {
		return refVwGrpId;
	}

	/** 참조열람그룹ID */
	public void setRefVwGrpId(String refVwGrpId) {
		this.refVwGrpId = refVwGrpId;
	}

	/** 참조열람고정결재자여부 */
	public String getRefVwFixdApvrYn() {
		return refVwFixdApvrYn;
	}

	/** 참조열람고정결재자여부 */
	public void setRefVwFixdApvrYn(String refVwFixdApvrYn) {
		this.refVwFixdApvrYn = refVwFixdApvrYn;
	}

	/** 본문HTML */
	public String getBodyHtml() {
		return bodyHtml;
	}

	/** 본문HTML */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	/** 결재번호 */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 양식구분명 */
	public String getFormTypNm() {
		return formTypNm;
	}

	/** 양식구분명 */
	public void setFormTypNm(String formTypNm) {
		this.formTypNm = formTypNm;
	}

	/** 양식넓이구분명 */
	public String getFormWdthTypNm() {
		return formWdthTypNm;
	}

	/** 양식넓이구분명 */
	public void setFormWdthTypNm(String formWdthTypNm) {
		this.formWdthTypNm = formWdthTypNm;
	}

	/** 양식결재라인구분명 */
	public String getFormApvLnTypNm() {
		return formApvLnTypNm;
	}

	/** 양식결재라인구분명 */
	public void setFormApvLnTypNm(String formApvLnTypNm) {
		this.formApvLnTypNm = formApvLnTypNm;
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** 문서보존기간명 */
	public String getDocKeepPrdNm() {
		return docKeepPrdNm;
	}

	/** 문서보존기간명 */
	public void setDocKeepPrdNm(String docKeepPrdNm) {
		this.docKeepPrdNm = docKeepPrdNm;
	}

	/** 자동결재선명 */
	public String getAutoApvLnNm() {
		return autoApvLnNm;
	}

	/** 자동결재선명 */
	public void setAutoApvLnNm(String autoApvLnNm) {
		this.autoApvLnNm = autoApvLnNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormBDao.selectApOngoFormB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormBDao.insertApOngoFormB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormBDao.updateApOngoFormB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormBDao.deleteApOngoFormB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngoFormBDao.countApOngoFormB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행양식기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID-PK):").append(formId).append('\n'); }
		if(formSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formSeq(양식일련번호-PK):").append(formSeq).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(formNm!=null) { if(tab!=null) builder.append(tab); builder.append("formNm(양식명):").append(formNm).append('\n'); }
		if(formBxId!=null) { if(tab!=null) builder.append(tab); builder.append("formBxId(양식함ID):").append(formBxId).append('\n'); }
		if(formTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formTypCd(양식구분코드):").append(formTypCd).append('\n'); }
		if(intgId!=null) { if(tab!=null) builder.append(tab); builder.append("intgId(연계ID):").append(intgId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(bodyHghtPx!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHghtPx(본문높이픽셀):").append(bodyHghtPx).append('\n'); }
		if(formWdthTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formWdthTypCd(양식넓이구분코드):").append(formWdthTypCd).append('\n'); }
		if(formApvLnTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formApvLnTypCd(양식결재라인구분코드):").append(formApvLnTypCd).append('\n'); }
		if(tplYn!=null) { if(tab!=null) builder.append(tab); builder.append("tplYn(템플릿여부):").append(tplYn).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(erpFormId!=null) { if(tab!=null) builder.append(tab); builder.append("erpFormId(ERP양식ID):").append(erpFormId).append('\n'); }
		if(erpFormTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("erpFormTypCd(ERP양식구분코드):").append(erpFormTypCd).append('\n'); }
		if(docKeepPrdCd!=null) { if(tab!=null) builder.append(tab); builder.append("docKeepPrdCd(문서보존기간코드):").append(docKeepPrdCd).append('\n'); }
		if(formMagnVa!=null) { if(tab!=null) builder.append(tab); builder.append("formMagnVa(양식여백값):").append(formMagnVa).append('\n'); }
		if(intgFormYn!=null) { if(tab!=null) builder.append(tab); builder.append("intgFormYn(연계양식여부):").append(intgFormYn).append('\n'); }
		if(bodyOlineYn!=null) { if(tab!=null) builder.append(tab); builder.append("bodyOlineYn(본문외곽선여부):").append(bodyOlineYn).append('\n'); }
		if(apvLnGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnGrpId(결재라인그룹ID):").append(apvLnGrpId).append('\n'); }
		if(fixdApvrYn!=null) { if(tab!=null) builder.append(tab); builder.append("fixdApvrYn(고정결재자여부):").append(fixdApvrYn).append('\n'); }
		if(autoApvLnCd!=null) { if(tab!=null) builder.append(tab); builder.append("autoApvLnCd(자동결재선코드):").append(autoApvLnCd).append('\n'); }
		if(docSubjCd!=null) { if(tab!=null) builder.append(tab); builder.append("docSubjCd(문서제목코드):").append(docSubjCd).append('\n'); }
		if(refVwGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("refVwGrpId(참조열람그룹ID):").append(refVwGrpId).append('\n'); }
		if(refVwFixdApvrYn!=null) { if(tab!=null) builder.append(tab); builder.append("refVwFixdApvrYn(참조열람고정결재자여부):").append(refVwFixdApvrYn).append('\n'); }
		if(bodyHtml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHtml(본문HTML):").append(bodyHtml).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호):").append(apvNo).append('\n'); }
		if(rescNm!=null) { if(tab!=null) builder.append(tab); builder.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if(formTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formTypNm(양식구분명):").append(formTypNm).append('\n'); }
		if(formWdthTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formWdthTypNm(양식넓이구분명):").append(formWdthTypNm).append('\n'); }
		if(formApvLnTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formApvLnTypNm(양식결재라인구분명):").append(formApvLnTypNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		if(docKeepPrdNm!=null) { if(tab!=null) builder.append(tab); builder.append("docKeepPrdNm(문서보존기간명):").append(docKeepPrdNm).append('\n'); }
		if(autoApvLnNm!=null) { if(tab!=null) builder.append(tab); builder.append("autoApvLnNm(자동결재선명):").append(autoApvLnNm).append('\n'); }
		super.toString(builder, tab);
	}
}
