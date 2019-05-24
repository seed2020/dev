package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 진행문서기본(AP_ONGD_B) 테이블 VO
 */
public class ApOngdBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7771588956566526302L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 문서번호 */
	private String docNo;

	/** 회사ID */
	private String compId;

	/** 문서제목 */
	private String docSubj;

	/** 첨부파일여부 */
	private String attFileYn;

	/** 양식ID */
	private String formId;

	/** 양식일련번호 */
	private String formSeq;

	/** 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재, apvLnList:결재자 리스트, apvLnOneTopList:최종결재 리스트, apvLnMultiTopList:도장방 리스트 */
	private String formApvLnTypCd;

	/** 양식명 */
	private String formNm;

	/** 본문높이픽셀 */
	private String bodyHghtPx;

	/** 양식넓이구분코드 - printMin:도장 5개, printAp6:도장 6개, printAp7:도장 7개, printAp8:도장 8개 */
	private String formWdthTypCd;

	/** 문서언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	private String docLangTypCd;

	/** 문서상태코드 - temp:임시저장, retrvMak:기안회수, byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, pred:전결, apv:결재, apvd:승인, rejt:반려, hold:보류, reRevw:재검토, dist:배부, recv:접수, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람, cmplVw:공람완료 */
	private String docStatCd;

	/** 문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람 */
	private String docProsStatCd;

	/** 문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	private String docKeepPrdCd;

	/** 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서 */
	private String docTypCd;

	/** 보안등급코드 */
	private String seculCd;

	/** 긴급문서여부 */
	private String ugntDocYn;

	/** 전체열람여부 */
	private String allReadYn;

	/** 등록대장등록여부 */
	private String regRecLstRegYn;

	/** 등록대장등록예정년월일 */
	private String regRecLstRegSkedYmd;

	/** 보안문서여부 */
	private String secuDocYn;

	/** 문서비밀번호암호값 */
	private String docPwEnc;

	/** 발신명의리소스ID */
	private String sendrNmRescId;

	/** 발신명의리소스명 */
	private String sendrNmRescNm;

	/** 관인경로 */
	private String ofsePath;

	/** 관인높이픽셀 */
	private String ofseHghtPx;

	/** 분류정보ID */
	private String clsInfoId;

	/** 개인분류정보ID */
	private String psnClsInfoId;

	/** 결재라인구분코드 - byOne:1인결재, nomlApv:일반결재, dblApv:이중결재, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 */
	private String apvLnTypCd;

	/** 본문이력번호 */
	private String bodyHstNo;

	/** 첨부이력번호 */
	private String attHstNo;

	/** 수신처이력번호 */
	private String recvDeptHstNo;

	/** 참조문서이력번호 */
	private String refDocHstNo;

	/** 기안일시 */
	private String makDt;

	/** 기안자UID */
	private String makrUid;

	/** 기안자명 */
	private String makrNm;

	/** 기안부서ID */
	private String makDeptId;

	/** 기안부서명 */
	private String makDeptNm;

	/** 현재결재자ID */
	private String curApvrId;

	/** 현재결재자명 */
	private String curApvrNm;

	/** 현재결재자부서여부 */
	private String curApvrDeptYn;

	/** 완결일시 */
	private String cmplDt;

	/** 완결자UID */
	private String cmplrUid;

	/** 완결자명 */
	private String cmplrNm;

	/** 시행범위코드 - dom:대내, for:대외, both:대내외 */
	private String enfcScopCd;

	/** 시행일시 */
	private String enfcDt;

	/** 접수문서번호 */
	private String recvDocNo;

	/** 접수일시 */
	private String recvDt;

	/** 시행문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	private String enfcDocKeepPrdCd;

	/** 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소 */
	private String enfcStatCd;

	/** 발송기관ID */
	private String sendInstId;

	/** 발송기관명 */
	private String sendInstNm;

	/** 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장 */
	private String recLstTypCd;

	/** 대장부서ID */
	private String recLstDeptId;

	/** 원본결재번호 */
	private String orgnApvNo;

	/** 변환결재번호 */
	private String trxApvNo;

	/** 연계구분코드 - ERP:ERP */
	private String intgTypCd;

	/** 연계번호 */
	private String intgNo;

	/** 발신명의조직ID */
	private String sendrNmOrgId;

	/** 참조문서포함발송여부 */
	private String sendWithRefDocYn;

	/** 바닥글값 */
	private String footerVa;

	/** XML구분ID */
	private String xmlTypId;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 결재번호 목록 */
	private List<String> apvNoList;

	/** 결재번호 불포함 목록 */
	private List<String> apvNoNotInList;

	/** 결재자UID */
	private String apvrUid;

	/** 결재자UID 목록 */
	private List<String> apvrUidList;

	/** 결재부서ID */
	private String apvDeptId;

	/** 결재부서ID 목록 */
	private List<String> apvDeptIdList;

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	private String apvrRoleCd;

	/** 결재자역할코드 목록 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	private List<String> apvrRoleCdList;

	/** 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	private String apvStatCd;

	/** 결재상태코드 목록 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	private List<String> apvStatCdList;

	/** 문서승인상태코드 목록 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람 */
	private List<String> docProsStatCdList;

	/** 결재라인부모번호 */
	private String apvLnPno;

	/** 결재라인번호 */
	private String apvLnNo;

	/** 이전결재자결재일시 */
	private String prevApvrApvDt;

	/** 조회일시 */
	private String vwDt;

	/** 정렬순서 */
	private String sortOrdr;

	/** 함ID */
	private String bxId;

	/** 분류정보명 */
	private String clsInfoNm;

	/** 담당자지정여부 */
	private String pichApntYn;

	/** 담당자구분코드 - reqCensr:심사요청, censr:심사, send:발송 */
	private String pichTypCd;

	/** 담당자UID */
	private String pichUid;

	/** 담당자UID 목록 */
	private List<String> pichUidList;

	/** 담당자부서ID */
	private String pichDeptId;

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	private String hdlStatCd;

	/** 수신처ID */
	private String recvDeptId;

	/** 발송일련번호 */
	private String sendSeq;

	/** 참조결재번호 */
	private String refApvNo;

	/** 공람게시부서ID */
	private String pubBxDeptId;

	/** 공람게시일시 */
	private String pubBxDt;

	/** 공람게시종료년월일 */
	private String pubBxEndYmd;

	/** 등록자UID */
	private String regrUid;

	/** 등록자명 */
	private String regrNm;

	/** 등록부서명 */
	private String regDeptNm;

	/** 검색문서번호 */
	private String srchDocNo;

	/** 검색접수문서번호 */
	private String srchRecvDocNo;

	/** 종이결재번호 */
	private String paperApvNo;

	/** 보안등급코드 목록 */
	private List<String> seculCdList;

	/** 본문HTML */
	private String bodyHtml;

	/** 대장부서ID 목록 */
	private List<String> recLstDeptIdList;

	/** 참조열람자UID */
	private String refVwrUid;

	/** 참조열람자UID 목록 */
	private List<String> refVwrUidList;

	/** 참조열람상태코드 - befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함 */
	private String refVwStatCd;

	/** 참조열람상태명 */
	private String refVwStatNm;

	/** 결재자명 */
	private String apvrNm;

	/** 결재자직위명 */
	private String apvrPositNm;

	/** 결재자직책명 */
	private String apvrTitleNm;

	/** 의견보유여부 */
	private String hasOpinYn;

	/** 결재의견내용 */
	private String apvOpinCont;

	/** 기안부서ID 목록 */
	private List<String> makDeptIdList;

	/** 양식결재라인구분명 */
	private String formApvLnTypNm;

	/** 양식넓이구분명 */
	private String formWdthTypNm;

	/** 문서언어구분명 */
	private String docLangTypNm;

	/** 문서상태명 */
	private String docStatNm;

	/** 문서승인상태명 */
	private String docProsStatNm;

	/** 문서보존기간명 */
	private String docKeepPrdNm;

	/** 문서구분명 */
	private String docTypNm;

	/** 보안등급명 */
	private String seculNm;

	/** 문서비밀번호 */
	private String docPw;

	/** 결재라인구분명 */
	private String apvLnTypNm;

	/** 시행범위명 */
	private String enfcScopNm;

	/** 시행문서보존기간명 */
	private String enfcDocKeepPrdNm;

	/** 시행상태명 */
	private String enfcStatNm;

	/** 대장구분명 */
	private String recLstTypNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 문서번호 */
	public String getDocNo() {
		return docNo;
	}

	/** 문서번호 */
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 문서제목 */
	public String getDocSubj() {
		return docSubj;
	}

	/** 문서제목 */
	public void setDocSubj(String docSubj) {
		this.docSubj = docSubj;
	}

	/** 첨부파일여부 */
	public String getAttFileYn() {
		return attFileYn;
	}

	/** 첨부파일여부 */
	public void setAttFileYn(String attFileYn) {
		this.attFileYn = attFileYn;
	}

	/** 양식ID */
	public String getFormId() {
		return formId;
	}

	/** 양식ID */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/** 양식일련번호 */
	public String getFormSeq() {
		return formSeq;
	}

	/** 양식일련번호 */
	public void setFormSeq(String formSeq) {
		this.formSeq = formSeq;
	}

	/** 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재, apvLnList:결재자 리스트, apvLnOneTopList:최종결재 리스트, apvLnMultiTopList:도장방 리스트 */
	public String getFormApvLnTypCd() {
		return formApvLnTypCd;
	}

	/** 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(결재합의혼합), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재, apvLnList:결재자 리스트, apvLnOneTopList:최종결재 리스트, apvLnMultiTopList:도장방 리스트 */
	public void setFormApvLnTypCd(String formApvLnTypCd) {
		this.formApvLnTypCd = formApvLnTypCd;
	}

	/** 양식명 */
	public String getFormNm() {
		return formNm;
	}

	/** 양식명 */
	public void setFormNm(String formNm) {
		this.formNm = formNm;
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

	/** 문서언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getDocLangTypCd() {
		return docLangTypCd;
	}

	/** 문서언어구분코드 - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setDocLangTypCd(String docLangTypCd) {
		this.docLangTypCd = docLangTypCd;
	}

	/** 문서상태코드 - temp:임시저장, retrvMak:기안회수, byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, pred:전결, apv:결재, apvd:승인, rejt:반려, hold:보류, reRevw:재검토, dist:배부, recv:접수, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람, cmplVw:공람완료 */
	public String getDocStatCd() {
		return docStatCd;
	}

	/** 문서상태코드 - temp:임시저장, retrvMak:기안회수, byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, pred:전결, apv:결재, apvd:승인, rejt:반려, hold:보류, reRevw:재검토, dist:배부, recv:접수, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람, cmplVw:공람완료 */
	public void setDocStatCd(String docStatCd) {
		this.docStatCd = docStatCd;
	}

	/** 문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람 */
	public String getDocProsStatCd() {
		return docProsStatCd;
	}

	/** 문서승인상태코드 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람 */
	public void setDocProsStatCd(String docProsStatCd) {
		this.docProsStatCd = docProsStatCd;
	}

	/** 문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	public String getDocKeepPrdCd() {
		return docKeepPrdCd;
	}

	/** 문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	public void setDocKeepPrdCd(String docKeepPrdCd) {
		this.docKeepPrdCd = docKeepPrdCd;
	}

	/** 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서 */
	public String getDocTypCd() {
		return docTypCd;
	}

	/** 문서구분코드 - intro:내부문서, extro:시행문서, paper:종이문서 */
	public void setDocTypCd(String docTypCd) {
		this.docTypCd = docTypCd;
	}

	/** 보안등급코드 */
	public String getSeculCd() {
		return seculCd;
	}

	/** 보안등급코드 */
	public void setSeculCd(String seculCd) {
		this.seculCd = seculCd;
	}

	/** 긴급문서여부 */
	public String getUgntDocYn() {
		return ugntDocYn;
	}

	/** 긴급문서여부 */
	public void setUgntDocYn(String ugntDocYn) {
		this.ugntDocYn = ugntDocYn;
	}

	/** 전체열람여부 */
	public String getAllReadYn() {
		return allReadYn;
	}

	/** 전체열람여부 */
	public void setAllReadYn(String allReadYn) {
		this.allReadYn = allReadYn;
	}

	/** 등록대장등록여부 */
	public String getRegRecLstRegYn() {
		return regRecLstRegYn;
	}

	/** 등록대장등록여부 */
	public void setRegRecLstRegYn(String regRecLstRegYn) {
		this.regRecLstRegYn = regRecLstRegYn;
	}

	/** 등록대장등록예정년월일 */
	public String getRegRecLstRegSkedYmd() {
		return regRecLstRegSkedYmd;
	}

	/** 등록대장등록예정년월일 */
	public void setRegRecLstRegSkedYmd(String regRecLstRegSkedYmd) {
		this.regRecLstRegSkedYmd = regRecLstRegSkedYmd;
	}

	/** 보안문서여부 */
	public String getSecuDocYn() {
		return secuDocYn;
	}

	/** 보안문서여부 */
	public void setSecuDocYn(String secuDocYn) {
		this.secuDocYn = secuDocYn;
	}

	/** 문서비밀번호암호값 */
	public String getDocPwEnc() {
		return docPwEnc;
	}

	/** 문서비밀번호암호값 */
	public void setDocPwEnc(String docPwEnc) {
		this.docPwEnc = docPwEnc;
	}

	/** 발신명의리소스ID */
	public String getSendrNmRescId() {
		return sendrNmRescId;
	}

	/** 발신명의리소스ID */
	public void setSendrNmRescId(String sendrNmRescId) {
		this.sendrNmRescId = sendrNmRescId;
	}

	/** 발신명의리소스명 */
	public String getSendrNmRescNm() {
		return sendrNmRescNm;
	}

	/** 발신명의리소스명 */
	public void setSendrNmRescNm(String sendrNmRescNm) {
		this.sendrNmRescNm = sendrNmRescNm;
	}

	/** 관인경로 */
	public String getOfsePath() {
		return ofsePath;
	}

	/** 관인경로 */
	public void setOfsePath(String ofsePath) {
		this.ofsePath = ofsePath;
	}

	/** 관인높이픽셀 */
	public String getOfseHghtPx() {
		return ofseHghtPx;
	}

	/** 관인높이픽셀 */
	public void setOfseHghtPx(String ofseHghtPx) {
		this.ofseHghtPx = ofseHghtPx;
	}

	/** 분류정보ID */
	public String getClsInfoId() {
		return clsInfoId;
	}

	/** 분류정보ID */
	public void setClsInfoId(String clsInfoId) {
		this.clsInfoId = clsInfoId;
	}

	/** 개인분류정보ID */
	public String getPsnClsInfoId() {
		return psnClsInfoId;
	}

	/** 개인분류정보ID */
	public void setPsnClsInfoId(String psnClsInfoId) {
		this.psnClsInfoId = psnClsInfoId;
	}

	/** 결재라인구분코드 - byOne:1인결재, nomlApv:일반결재, dblApv:이중결재, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 */
	public String getApvLnTypCd() {
		return apvLnTypCd;
	}

	/** 결재라인구분코드 - byOne:1인결재, nomlApv:일반결재, dblApv:이중결재, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 */
	public void setApvLnTypCd(String apvLnTypCd) {
		this.apvLnTypCd = apvLnTypCd;
	}

	/** 본문이력번호 */
	public String getBodyHstNo() {
		return bodyHstNo;
	}

	/** 본문이력번호 */
	public void setBodyHstNo(String bodyHstNo) {
		this.bodyHstNo = bodyHstNo;
	}

	/** 첨부이력번호 */
	public String getAttHstNo() {
		return attHstNo;
	}

	/** 첨부이력번호 */
	public void setAttHstNo(String attHstNo) {
		this.attHstNo = attHstNo;
	}

	/** 수신처이력번호 */
	public String getRecvDeptHstNo() {
		return recvDeptHstNo;
	}

	/** 수신처이력번호 */
	public void setRecvDeptHstNo(String recvDeptHstNo) {
		this.recvDeptHstNo = recvDeptHstNo;
	}

	/** 참조문서이력번호 */
	public String getRefDocHstNo() {
		return refDocHstNo;
	}

	/** 참조문서이력번호 */
	public void setRefDocHstNo(String refDocHstNo) {
		this.refDocHstNo = refDocHstNo;
	}

	/** 기안일시 */
	public String getMakDt() {
		return makDt;
	}

	/** 기안일시 */
	public void setMakDt(String makDt) {
		this.makDt = makDt;
	}

	/** 기안자UID */
	public String getMakrUid() {
		return makrUid;
	}

	/** 기안자UID */
	public void setMakrUid(String makrUid) {
		this.makrUid = makrUid;
	}

	/** 기안자명 */
	public String getMakrNm() {
		return makrNm;
	}

	/** 기안자명 */
	public void setMakrNm(String makrNm) {
		this.makrNm = makrNm;
	}

	/** 기안부서ID */
	public String getMakDeptId() {
		return makDeptId;
	}

	/** 기안부서ID */
	public void setMakDeptId(String makDeptId) {
		this.makDeptId = makDeptId;
	}

	/** 기안부서명 */
	public String getMakDeptNm() {
		return makDeptNm;
	}

	/** 기안부서명 */
	public void setMakDeptNm(String makDeptNm) {
		this.makDeptNm = makDeptNm;
	}

	/** 현재결재자ID */
	public String getCurApvrId() {
		return curApvrId;
	}

	/** 현재결재자ID */
	public void setCurApvrId(String curApvrId) {
		this.curApvrId = curApvrId;
	}

	/** 현재결재자명 */
	public String getCurApvrNm() {
		return curApvrNm;
	}

	/** 현재결재자명 */
	public void setCurApvrNm(String curApvrNm) {
		this.curApvrNm = curApvrNm;
	}

	/** 현재결재자부서여부 */
	public String getCurApvrDeptYn() {
		return curApvrDeptYn;
	}

	/** 현재결재자부서여부 */
	public void setCurApvrDeptYn(String curApvrDeptYn) {
		this.curApvrDeptYn = curApvrDeptYn;
	}

	/** 완결일시 */
	public String getCmplDt() {
		return cmplDt;
	}

	/** 완결일시 */
	public void setCmplDt(String cmplDt) {
		this.cmplDt = cmplDt;
	}

	/** 완결자UID */
	public String getCmplrUid() {
		return cmplrUid;
	}

	/** 완결자UID */
	public void setCmplrUid(String cmplrUid) {
		this.cmplrUid = cmplrUid;
	}

	/** 완결자명 */
	public String getCmplrNm() {
		return cmplrNm;
	}

	/** 완결자명 */
	public void setCmplrNm(String cmplrNm) {
		this.cmplrNm = cmplrNm;
	}

	/** 시행범위코드 - dom:대내, for:대외, both:대내외 */
	public String getEnfcScopCd() {
		return enfcScopCd;
	}

	/** 시행범위코드 - dom:대내, for:대외, both:대내외 */
	public void setEnfcScopCd(String enfcScopCd) {
		this.enfcScopCd = enfcScopCd;
	}

	/** 시행일시 */
	public String getEnfcDt() {
		return enfcDt;
	}

	/** 시행일시 */
	public void setEnfcDt(String enfcDt) {
		this.enfcDt = enfcDt;
	}

	/** 접수문서번호 */
	public String getRecvDocNo() {
		return recvDocNo;
	}

	/** 접수문서번호 */
	public void setRecvDocNo(String recvDocNo) {
		this.recvDocNo = recvDocNo;
	}

	/** 접수일시 */
	public String getRecvDt() {
		return recvDt;
	}

	/** 접수일시 */
	public void setRecvDt(String recvDt) {
		this.recvDt = recvDt;
	}

	/** 시행문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	public String getEnfcDocKeepPrdCd() {
		return enfcDocKeepPrdCd;
	}

	/** 시행문서보존기간코드 - 1Y:1년, 3Y:3년, 5Y:5년, 10Y:10년, 30Y:30년, endless:영구 */
	public void setEnfcDocKeepPrdCd(String enfcDocKeepPrdCd) {
		this.enfcDocKeepPrdCd = enfcDocKeepPrdCd;
	}

	/** 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소 */
	public String getEnfcStatCd() {
		return enfcStatCd;
	}

	/** 시행상태코드 - apvd:승인, rejt:반려, befoEnfc:시행대기, inCensr:심사, censrRejt:심사반려, befoSend:발송대기, sent:발송, cnclEnfc:시행취소 */
	public void setEnfcStatCd(String enfcStatCd) {
		this.enfcStatCd = enfcStatCd;
	}

	/** 발송기관ID */
	public String getSendInstId() {
		return sendInstId;
	}

	/** 발송기관ID */
	public void setSendInstId(String sendInstId) {
		this.sendInstId = sendInstId;
	}

	/** 발송기관명 */
	public String getSendInstNm() {
		return sendInstNm;
	}

	/** 발송기관명 */
	public void setSendInstNm(String sendInstNm) {
		this.sendInstNm = sendInstNm;
	}

	/** 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장 */
	public String getRecLstTypCd() {
		return recLstTypCd;
	}

	/** 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장 */
	public void setRecLstTypCd(String recLstTypCd) {
		this.recLstTypCd = recLstTypCd;
	}

	/** 대장부서ID */
	public String getRecLstDeptId() {
		return recLstDeptId;
	}

	/** 대장부서ID */
	public void setRecLstDeptId(String recLstDeptId) {
		this.recLstDeptId = recLstDeptId;
	}

	/** 원본결재번호 */
	public String getOrgnApvNo() {
		return orgnApvNo;
	}

	/** 원본결재번호 */
	public void setOrgnApvNo(String orgnApvNo) {
		this.orgnApvNo = orgnApvNo;
	}

	/** 변환결재번호 */
	public String getTrxApvNo() {
		return trxApvNo;
	}

	/** 변환결재번호 */
	public void setTrxApvNo(String trxApvNo) {
		this.trxApvNo = trxApvNo;
	}

	/** 연계구분코드 - ERP:ERP */
	public String getIntgTypCd() {
		return intgTypCd;
	}

	/** 연계구분코드 - ERP:ERP */
	public void setIntgTypCd(String intgTypCd) {
		this.intgTypCd = intgTypCd;
	}

	/** 연계번호 */
	public String getIntgNo() {
		return intgNo;
	}

	/** 연계번호 */
	public void setIntgNo(String intgNo) {
		this.intgNo = intgNo;
	}

	/** 발신명의조직ID */
	public String getSendrNmOrgId() {
		return sendrNmOrgId;
	}

	/** 발신명의조직ID */
	public void setSendrNmOrgId(String sendrNmOrgId) {
		this.sendrNmOrgId = sendrNmOrgId;
	}

	/** 참조문서포함발송여부 */
	public String getSendWithRefDocYn() {
		return sendWithRefDocYn;
	}

	/** 참조문서포함발송여부 */
	public void setSendWithRefDocYn(String sendWithRefDocYn) {
		this.sendWithRefDocYn = sendWithRefDocYn;
	}

	/** 바닥글값 */
	public String getFooterVa() {
		return footerVa;
	}

	/** 바닥글값 */
	public void setFooterVa(String footerVa) {
		this.footerVa = footerVa;
	}

	/** XML구분ID */
	public String getXmlTypId() {
		return xmlTypId;
	}

	/** XML구분ID */
	public void setXmlTypId(String xmlTypId) {
		this.xmlTypId = xmlTypId;
	}

	/** 스토리지 리턴 */
	public String getStorage(){
		return storage == null ? "ONGD" : storage;
	}

	/** 스토리지 세팅 */
	public void setStorage(String storage) {
		if(storage==null || storage.isEmpty()) this.storage = null;
		else if(storage.equals("ONGD") || storage.matches("[0-9A-Z]{4}")) this.storage = storage;
	}

	/** 결재번호 목록 */
	public List<String> getApvNoList() {
		return apvNoList;
	}

	/** 결재번호 목록 */
	public void setApvNoList(List<String> apvNoList) {
		this.apvNoList = apvNoList;
	}

	/** 결재번호 불포함 목록 */
	public List<String> getApvNoNotInList() {
		return apvNoNotInList;
	}

	/** 결재번호 불포함 목록 */
	public void setApvNoNotInList(List<String> apvNoNotInList) {
		this.apvNoNotInList = apvNoNotInList;
	}

	/** 결재자UID */
	public String getApvrUid() {
		return apvrUid;
	}

	/** 결재자UID */
	public void setApvrUid(String apvrUid) {
		this.apvrUid = apvrUid;
	}

	/** 결재자UID 목록 */
	public List<String> getApvrUidList() {
		return apvrUidList;
	}

	/** 결재자UID 목록 */
	public void setApvrUidList(List<String> apvrUidList) {
		this.apvrUidList = apvrUidList;
	}

	/** 결재부서ID */
	public String getApvDeptId() {
		return apvDeptId;
	}

	/** 결재부서ID */
	public void setApvDeptId(String apvDeptId) {
		this.apvDeptId = apvDeptId;
	}

	/** 결재부서ID 목록 */
	public List<String> getApvDeptIdList() {
		return apvDeptIdList;
	}

	/** 결재부서ID 목록 */
	public void setApvDeptIdList(List<String> apvDeptIdList) {
		this.apvDeptIdList = apvDeptIdList;
	}

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public String getApvrRoleCd() {
		return apvrRoleCd;
	}

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public void setApvrRoleCd(String apvrRoleCd) {
		this.apvrRoleCd = apvrRoleCd;
	}

	/** 결재자역할코드 목록 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public List<String> getApvrRoleCdList() {
		return apvrRoleCdList;
	}

	/** 결재자역할코드 목록 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public void setApvrRoleCdList(List<String> apvrRoleCdList) {
		this.apvrRoleCdList = apvrRoleCdList;
	}

	/** 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public String getApvStatCd() {
		return apvStatCd;
	}

	/** 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public void setApvStatCd(String apvStatCd) {
		this.apvStatCd = apvStatCd;
	}

	/** 결재상태코드 목록 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public List<String> getApvStatCdList() {
		return apvStatCdList;
	}

	/** 결재상태코드 목록 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public void setApvStatCdList(List<String> apvStatCdList) {
		this.apvStatCdList = apvStatCdList;
	}

	/** 문서승인상태코드 목록 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람 */
	public List<String> getDocProsStatCdList() {
		return docProsStatCdList;
	}

	/** 문서승인상태코드 목록 - temp:임시저장, mak:기안, ongo:결재중, apvd:승인, rejt:반려, pubVw:공람 */
	public void setDocProsStatCdList(List<String> docProsStatCdList) {
		this.docProsStatCdList = docProsStatCdList;
	}

	/** 결재라인부모번호 */
	public String getApvLnPno() {
		return apvLnPno;
	}

	/** 결재라인부모번호 */
	public void setApvLnPno(String apvLnPno) {
		this.apvLnPno = apvLnPno;
	}

	/** 결재라인번호 */
	public String getApvLnNo() {
		return apvLnNo;
	}

	/** 결재라인번호 */
	public void setApvLnNo(String apvLnNo) {
		this.apvLnNo = apvLnNo;
	}

	/** 이전결재자결재일시 */
	public String getPrevApvrApvDt() {
		return prevApvrApvDt;
	}

	/** 이전결재자결재일시 */
	public void setPrevApvrApvDt(String prevApvrApvDt) {
		this.prevApvrApvDt = prevApvrApvDt;
	}

	/** 조회일시 */
	public String getVwDt() {
		return vwDt;
	}

	/** 조회일시 */
	public void setVwDt(String vwDt) {
		this.vwDt = vwDt;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 함ID */
	public String getBxId() {
		return bxId;
	}

	/** 함ID */
	public void setBxId(String bxId) {
		this.bxId = bxId;
	}

	/** 분류정보명 */
	public String getClsInfoNm() {
		return clsInfoNm;
	}

	/** 분류정보명 */
	public void setClsInfoNm(String clsInfoNm) {
		this.clsInfoNm = clsInfoNm;
	}

	/** 담당자지정여부 */
	public String getPichApntYn() {
		return pichApntYn;
	}

	/** 담당자지정여부 */
	public void setPichApntYn(String pichApntYn) {
		this.pichApntYn = pichApntYn;
	}

	/** 담당자구분코드 - reqCensr:심사요청, censr:심사, send:발송 */
	public String getPichTypCd() {
		return pichTypCd;
	}

	/** 담당자구분코드 - reqCensr:심사요청, censr:심사, send:발송 */
	public void setPichTypCd(String pichTypCd) {
		this.pichTypCd = pichTypCd;
	}

	/** 담당자UID */
	public String getPichUid() {
		return pichUid;
	}

	/** 담당자UID */
	public void setPichUid(String pichUid) {
		this.pichUid = pichUid;
	}

	/** 담당자UID 목록 */
	public List<String> getPichUidList() {
		return pichUidList;
	}

	/** 담당자UID 목록 */
	public void setPichUidList(List<String> pichUidList) {
		this.pichUidList = pichUidList;
	}

	/** 담당자부서ID */
	public String getPichDeptId() {
		return pichDeptId;
	}

	/** 담당자부서ID */
	public void setPichDeptId(String pichDeptId) {
		this.pichDeptId = pichDeptId;
	}

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	public String getHdlStatCd() {
		return hdlStatCd;
	}

	/** 처리상태코드 - reqCensr:심사요청, befoCensr:심사전, censrApvd:심사승인, censrRejt:심사반려, befoSend:발송대기, sendCmpl:발송완료, befoRecv:접수대기, recvCmpl:접수완료, recvRetn:접수반송, recvRetrv:접수회수, befoDist:배부대기, distCmpl:배부완료, distRetn:배부반송, distRetrv:배부회수, manl:수동발송, manlCmpl:수동완료, manlRetrv:수동회수, dupSend:중복발송 */
	public void setHdlStatCd(String hdlStatCd) {
		this.hdlStatCd = hdlStatCd;
	}

	/** 수신처ID */
	public String getRecvDeptId() {
		return recvDeptId;
	}

	/** 수신처ID */
	public void setRecvDeptId(String recvDeptId) {
		this.recvDeptId = recvDeptId;
	}

	/** 발송일련번호 */
	public String getSendSeq() {
		return sendSeq;
	}

	/** 발송일련번호 */
	public void setSendSeq(String sendSeq) {
		this.sendSeq = sendSeq;
	}

	/** 참조결재번호 */
	public String getRefApvNo() {
		return refApvNo;
	}

	/** 참조결재번호 */
	public void setRefApvNo(String refApvNo) {
		this.refApvNo = refApvNo;
	}

	/** 공람게시부서ID */
	public String getPubBxDeptId() {
		return pubBxDeptId;
	}

	/** 공람게시부서ID */
	public void setPubBxDeptId(String pubBxDeptId) {
		this.pubBxDeptId = pubBxDeptId;
	}

	/** 공람게시일시 */
	public String getPubBxDt() {
		return pubBxDt;
	}

	/** 공람게시일시 */
	public void setPubBxDt(String pubBxDt) {
		this.pubBxDt = pubBxDt;
	}

	/** 공람게시종료년월일 */
	public String getPubBxEndYmd() {
		return pubBxEndYmd;
	}

	/** 공람게시종료년월일 */
	public void setPubBxEndYmd(String pubBxEndYmd) {
		this.pubBxEndYmd = pubBxEndYmd;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 등록부서명 */
	public String getRegDeptNm() {
		return regDeptNm;
	}

	/** 등록부서명 */
	public void setRegDeptNm(String regDeptNm) {
		this.regDeptNm = regDeptNm;
	}

	/** 검색문서번호 */
	public String getSrchDocNo() {
		return srchDocNo;
	}

	/** 검색문서번호 */
	public void setSrchDocNo(String srchDocNo) {
		this.srchDocNo = srchDocNo;
	}

	/** 검색접수문서번호 */
	public String getSrchRecvDocNo() {
		return srchRecvDocNo;
	}

	/** 검색접수문서번호 */
	public void setSrchRecvDocNo(String srchRecvDocNo) {
		this.srchRecvDocNo = srchRecvDocNo;
	}

	/** 종이결재번호 */
	public String getPaperApvNo() {
		return paperApvNo;
	}

	/** 종이결재번호 */
	public void setPaperApvNo(String paperApvNo) {
		this.paperApvNo = paperApvNo;
	}

	/** 보안등급코드 목록 */
	public List<String> getSeculCdList() {
		return seculCdList;
	}

	/** 보안등급코드 목록 */
	public void setSeculCdList(List<String> seculCdList) {
		this.seculCdList = seculCdList;
	}

	/** 본문HTML */
	public String getBodyHtml() {
		return bodyHtml;
	}

	/** 본문HTML */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	/** 대장부서ID 목록 */
	public List<String> getRecLstDeptIdList() {
		return recLstDeptIdList;
	}

	/** 대장부서ID 목록 */
	public void setRecLstDeptIdList(List<String> recLstDeptIdList) {
		this.recLstDeptIdList = recLstDeptIdList;
	}

	/** 참조열람자UID */
	public String getRefVwrUid() {
		return refVwrUid;
	}

	/** 참조열람자UID */
	public void setRefVwrUid(String refVwrUid) {
		this.refVwrUid = refVwrUid;
	}

	/** 참조열람자UID 목록 */
	public List<String> getRefVwrUidList() {
		return refVwrUidList;
	}

	/** 참조열람자UID 목록 */
	public void setRefVwrUidList(List<String> refVwrUidList) {
		this.refVwrUidList = refVwrUidList;
	}

	/** 참조열람상태코드 - befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함 */
	public String getRefVwStatCd() {
		return refVwStatCd;
	}

	/** 참조열람상태코드 - befoRefVw:참조열람전, inRefVw:참조열람중, cmplRefVw:참조열람완료, noRefVw:참조열람안함 */
	public void setRefVwStatCd(String refVwStatCd) {
		this.refVwStatCd = refVwStatCd;
	}

	/** 참조열람상태명 */
	public String getRefVwStatNm() {
		return refVwStatNm;
	}

	/** 참조열람상태명 */
	public void setRefVwStatNm(String refVwStatNm) {
		this.refVwStatNm = refVwStatNm;
	}

	/** 결재자명 */
	public String getApvrNm() {
		return apvrNm;
	}

	/** 결재자명 */
	public void setApvrNm(String apvrNm) {
		this.apvrNm = apvrNm;
	}

	/** 결재자직위명 */
	public String getApvrPositNm() {
		return apvrPositNm;
	}

	/** 결재자직위명 */
	public void setApvrPositNm(String apvrPositNm) {
		this.apvrPositNm = apvrPositNm;
	}

	/** 결재자직책명 */
	public String getApvrTitleNm() {
		return apvrTitleNm;
	}

	/** 결재자직책명 */
	public void setApvrTitleNm(String apvrTitleNm) {
		this.apvrTitleNm = apvrTitleNm;
	}

	/** 의견보유여부 */
	public String getHasOpinYn() {
		return hasOpinYn;
	}

	/** 의견보유여부 */
	public void setHasOpinYn(String hasOpinYn) {
		this.hasOpinYn = hasOpinYn;
	}

	/** 결재의견내용 */
	public String getApvOpinCont() {
		return apvOpinCont;
	}

	/** 결재의견내용 */
	public void setApvOpinCont(String apvOpinCont) {
		this.apvOpinCont = apvOpinCont;
	}

	/** 기안부서ID 목록 */
	public List<String> getMakDeptIdList() {
		return makDeptIdList;
	}

	/** 기안부서ID 목록 */
	public void setMakDeptIdList(List<String> makDeptIdList) {
		this.makDeptIdList = makDeptIdList;
	}

	/** 양식결재라인구분명 */
	public String getFormApvLnTypNm() {
		return formApvLnTypNm;
	}

	/** 양식결재라인구분명 */
	public void setFormApvLnTypNm(String formApvLnTypNm) {
		this.formApvLnTypNm = formApvLnTypNm;
	}

	/** 양식넓이구분명 */
	public String getFormWdthTypNm() {
		return formWdthTypNm;
	}

	/** 양식넓이구분명 */
	public void setFormWdthTypNm(String formWdthTypNm) {
		this.formWdthTypNm = formWdthTypNm;
	}

	/** 문서언어구분명 */
	public String getDocLangTypNm() {
		return docLangTypNm;
	}

	/** 문서언어구분명 */
	public void setDocLangTypNm(String docLangTypNm) {
		this.docLangTypNm = docLangTypNm;
	}

	/** 문서상태명 */
	public String getDocStatNm() {
		return docStatNm;
	}

	/** 문서상태명 */
	public void setDocStatNm(String docStatNm) {
		this.docStatNm = docStatNm;
	}

	/** 문서승인상태명 */
	public String getDocProsStatNm() {
		return docProsStatNm;
	}

	/** 문서승인상태명 */
	public void setDocProsStatNm(String docProsStatNm) {
		this.docProsStatNm = docProsStatNm;
	}

	/** 문서보존기간명 */
	public String getDocKeepPrdNm() {
		return docKeepPrdNm;
	}

	/** 문서보존기간명 */
	public void setDocKeepPrdNm(String docKeepPrdNm) {
		this.docKeepPrdNm = docKeepPrdNm;
	}

	/** 문서구분명 */
	public String getDocTypNm() {
		return docTypNm;
	}

	/** 문서구분명 */
	public void setDocTypNm(String docTypNm) {
		this.docTypNm = docTypNm;
	}

	/** 보안등급명 */
	public String getSeculNm() {
		return seculNm;
	}

	/** 보안등급명 */
	public void setSeculNm(String seculNm) {
		this.seculNm = seculNm;
	}

	/** 문서비밀번호 */
	public String getDocPw() {
		return docPw;
	}

	/** 문서비밀번호 */
	public void setDocPw(String docPw) {
		this.docPw = docPw;
	}

	/** 결재라인구분명 */
	public String getApvLnTypNm() {
		return apvLnTypNm;
	}

	/** 결재라인구분명 */
	public void setApvLnTypNm(String apvLnTypNm) {
		this.apvLnTypNm = apvLnTypNm;
	}

	/** 시행범위명 */
	public String getEnfcScopNm() {
		return enfcScopNm;
	}

	/** 시행범위명 */
	public void setEnfcScopNm(String enfcScopNm) {
		this.enfcScopNm = enfcScopNm;
	}

	/** 시행문서보존기간명 */
	public String getEnfcDocKeepPrdNm() {
		return enfcDocKeepPrdNm;
	}

	/** 시행문서보존기간명 */
	public void setEnfcDocKeepPrdNm(String enfcDocKeepPrdNm) {
		this.enfcDocKeepPrdNm = enfcDocKeepPrdNm;
	}

	/** 시행상태명 */
	public String getEnfcStatNm() {
		return enfcStatNm;
	}

	/** 시행상태명 */
	public void setEnfcStatNm(String enfcStatNm) {
		this.enfcStatNm = enfcStatNm;
	}

	/** 대장구분명 */
	public String getRecLstTypNm() {
		return recLstTypNm;
	}

	/** 대장구분명 */
	public void setRecLstTypNm(String recLstTypNm) {
		this.recLstTypNm = recLstTypNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBDao.selectApOngdB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBDao.insertApOngdB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBDao.updateApOngdB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBDao.deleteApOngdB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdBDao.countApOngdB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_B").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(docNo!=null) { if(tab!=null) builder.append(tab); builder.append("docNo(문서번호):").append(docNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(docSubj!=null) { if(tab!=null) builder.append(tab); builder.append("docSubj(문서제목):").append(docSubj).append('\n'); }
		if(attFileYn!=null) { if(tab!=null) builder.append(tab); builder.append("attFileYn(첨부파일여부):").append(attFileYn).append('\n'); }
		if(formId!=null) { if(tab!=null) builder.append(tab); builder.append("formId(양식ID):").append(formId).append('\n'); }
		if(formSeq!=null) { if(tab!=null) builder.append(tab); builder.append("formSeq(양식일련번호):").append(formSeq).append('\n'); }
		if(formApvLnTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formApvLnTypCd(양식결재라인구분코드):").append(formApvLnTypCd).append('\n'); }
		if(formNm!=null) { if(tab!=null) builder.append(tab); builder.append("formNm(양식명):").append(formNm).append('\n'); }
		if(bodyHghtPx!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHghtPx(본문높이픽셀):").append(bodyHghtPx).append('\n'); }
		if(formWdthTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("formWdthTypCd(양식넓이구분코드):").append(formWdthTypCd).append('\n'); }
		if(docLangTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("docLangTypCd(문서언어구분코드):").append(docLangTypCd).append('\n'); }
		if(docStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("docStatCd(문서상태코드):").append(docStatCd).append('\n'); }
		if(docProsStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("docProsStatCd(문서승인상태코드):").append(docProsStatCd).append('\n'); }
		if(docKeepPrdCd!=null) { if(tab!=null) builder.append(tab); builder.append("docKeepPrdCd(문서보존기간코드):").append(docKeepPrdCd).append('\n'); }
		if(docTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("docTypCd(문서구분코드):").append(docTypCd).append('\n'); }
		if(seculCd!=null) { if(tab!=null) builder.append(tab); builder.append("seculCd(보안등급코드):").append(seculCd).append('\n'); }
		if(ugntDocYn!=null) { if(tab!=null) builder.append(tab); builder.append("ugntDocYn(긴급문서여부):").append(ugntDocYn).append('\n'); }
		if(allReadYn!=null) { if(tab!=null) builder.append(tab); builder.append("allReadYn(전체열람여부):").append(allReadYn).append('\n'); }
		if(regRecLstRegYn!=null) { if(tab!=null) builder.append(tab); builder.append("regRecLstRegYn(등록대장등록여부):").append(regRecLstRegYn).append('\n'); }
		if(regRecLstRegSkedYmd!=null) { if(tab!=null) builder.append(tab); builder.append("regRecLstRegSkedYmd(등록대장등록예정년월일):").append(regRecLstRegSkedYmd).append('\n'); }
		if(secuDocYn!=null) { if(tab!=null) builder.append(tab); builder.append("secuDocYn(보안문서여부):").append(secuDocYn).append('\n'); }
		if(docPwEnc!=null) { if(tab!=null) builder.append(tab); builder.append("docPwEnc(문서비밀번호암호값):").append(docPwEnc).append('\n'); }
		if(sendrNmRescId!=null) { if(tab!=null) builder.append(tab); builder.append("sendrNmRescId(발신명의리소스ID):").append(sendrNmRescId).append('\n'); }
		if(sendrNmRescNm!=null) { if(tab!=null) builder.append(tab); builder.append("sendrNmRescNm(발신명의리소스명):").append(sendrNmRescNm).append('\n'); }
		if(ofsePath!=null) { if(tab!=null) builder.append(tab); builder.append("ofsePath(관인경로):").append(ofsePath).append('\n'); }
		if(ofseHghtPx!=null) { if(tab!=null) builder.append(tab); builder.append("ofseHghtPx(관인높이픽셀):").append(ofseHghtPx).append('\n'); }
		if(clsInfoId!=null) { if(tab!=null) builder.append(tab); builder.append("clsInfoId(분류정보ID):").append(clsInfoId).append('\n'); }
		if(psnClsInfoId!=null) { if(tab!=null) builder.append(tab); builder.append("psnClsInfoId(개인분류정보ID):").append(psnClsInfoId).append('\n'); }
		if(apvLnTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnTypCd(결재라인구분코드):").append(apvLnTypCd).append('\n'); }
		if(bodyHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHstNo(본문이력번호):").append(bodyHstNo).append('\n'); }
		if(attHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("attHstNo(첨부이력번호):").append(attHstNo).append('\n'); }
		if(recvDeptHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptHstNo(수신처이력번호):").append(recvDeptHstNo).append('\n'); }
		if(refDocHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("refDocHstNo(참조문서이력번호):").append(refDocHstNo).append('\n'); }
		if(makDt!=null) { if(tab!=null) builder.append(tab); builder.append("makDt(기안일시):").append(makDt).append('\n'); }
		if(makrUid!=null) { if(tab!=null) builder.append(tab); builder.append("makrUid(기안자UID):").append(makrUid).append('\n'); }
		if(makrNm!=null) { if(tab!=null) builder.append(tab); builder.append("makrNm(기안자명):").append(makrNm).append('\n'); }
		if(makDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("makDeptId(기안부서ID):").append(makDeptId).append('\n'); }
		if(makDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("makDeptNm(기안부서명):").append(makDeptNm).append('\n'); }
		if(curApvrId!=null) { if(tab!=null) builder.append(tab); builder.append("curApvrId(현재결재자ID):").append(curApvrId).append('\n'); }
		if(curApvrNm!=null) { if(tab!=null) builder.append(tab); builder.append("curApvrNm(현재결재자명):").append(curApvrNm).append('\n'); }
		if(curApvrDeptYn!=null) { if(tab!=null) builder.append(tab); builder.append("curApvrDeptYn(현재결재자부서여부):").append(curApvrDeptYn).append('\n'); }
		if(cmplDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDt(완결일시):").append(cmplDt).append('\n'); }
		if(cmplrUid!=null) { if(tab!=null) builder.append(tab); builder.append("cmplrUid(완결자UID):").append(cmplrUid).append('\n'); }
		if(cmplrNm!=null) { if(tab!=null) builder.append(tab); builder.append("cmplrNm(완결자명):").append(cmplrNm).append('\n'); }
		if(enfcScopCd!=null) { if(tab!=null) builder.append(tab); builder.append("enfcScopCd(시행범위코드):").append(enfcScopCd).append('\n'); }
		if(enfcDt!=null) { if(tab!=null) builder.append(tab); builder.append("enfcDt(시행일시):").append(enfcDt).append('\n'); }
		if(recvDocNo!=null) { if(tab!=null) builder.append(tab); builder.append("recvDocNo(접수문서번호):").append(recvDocNo).append('\n'); }
		if(recvDt!=null) { if(tab!=null) builder.append(tab); builder.append("recvDt(접수일시):").append(recvDt).append('\n'); }
		if(enfcDocKeepPrdCd!=null) { if(tab!=null) builder.append(tab); builder.append("enfcDocKeepPrdCd(시행문서보존기간코드):").append(enfcDocKeepPrdCd).append('\n'); }
		if(enfcStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("enfcStatCd(시행상태코드):").append(enfcStatCd).append('\n'); }
		if(sendInstId!=null) { if(tab!=null) builder.append(tab); builder.append("sendInstId(발송기관ID):").append(sendInstId).append('\n'); }
		if(sendInstNm!=null) { if(tab!=null) builder.append(tab); builder.append("sendInstNm(발송기관명):").append(sendInstNm).append('\n'); }
		if(recLstTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("recLstTypCd(대장구분코드):").append(recLstTypCd).append('\n'); }
		if(recLstDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("recLstDeptId(대장부서ID):").append(recLstDeptId).append('\n'); }
		if(orgnApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("orgnApvNo(원본결재번호):").append(orgnApvNo).append('\n'); }
		if(trxApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("trxApvNo(변환결재번호):").append(trxApvNo).append('\n'); }
		if(intgTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("intgTypCd(연계구분코드):").append(intgTypCd).append('\n'); }
		if(intgNo!=null) { if(tab!=null) builder.append(tab); builder.append("intgNo(연계번호):").append(intgNo).append('\n'); }
		if(sendrNmOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("sendrNmOrgId(발신명의조직ID):").append(sendrNmOrgId).append('\n'); }
		if(sendWithRefDocYn!=null) { if(tab!=null) builder.append(tab); builder.append("sendWithRefDocYn(참조문서포함발송여부):").append(sendWithRefDocYn).append('\n'); }
		if(footerVa!=null) { if(tab!=null) builder.append(tab); builder.append("footerVa(바닥글값):").append(footerVa).append('\n'); }
		if(xmlTypId!=null) { if(tab!=null) builder.append(tab); builder.append("xmlTypId(XML구분ID):").append(xmlTypId).append('\n'); }
		if(apvNoList!=null) { if(tab!=null) builder.append(tab); builder.append("apvNoList(결재번호 목록):"); appendStringListTo(builder, apvNoList, tab); builder.append('\n'); }
		if(apvNoNotInList!=null) { if(tab!=null) builder.append(tab); builder.append("apvNoNotInList(결재번호 불포함 목록):"); appendStringListTo(builder, apvNoNotInList, tab); builder.append('\n'); }
		if(apvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("apvrUid(결재자UID):").append(apvrUid).append('\n'); }
		if(apvrUidList!=null) { if(tab!=null) builder.append(tab); builder.append("apvrUidList(결재자UID 목록):"); appendStringListTo(builder, apvrUidList, tab); builder.append('\n'); }
		if(apvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("apvDeptId(결재부서ID):").append(apvDeptId).append('\n'); }
		if(apvDeptIdList!=null) { if(tab!=null) builder.append(tab); builder.append("apvDeptIdList(결재부서ID 목록):"); appendStringListTo(builder, apvDeptIdList, tab); builder.append('\n'); }
		if(apvrRoleCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleCd(결재자역할코드):").append(apvrRoleCd).append('\n'); }
		if(apvrRoleCdList!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleCdList(결재자역할코드 목록):"); appendStringListTo(builder, apvrRoleCdList, tab); builder.append('\n'); }
		if(apvStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvStatCd(결재상태코드):").append(apvStatCd).append('\n'); }
		if(apvStatCdList!=null) { if(tab!=null) builder.append(tab); builder.append("apvStatCdList(결재상태코드 목록):"); appendStringListTo(builder, apvStatCdList, tab); builder.append('\n'); }
		if(docProsStatCdList!=null) { if(tab!=null) builder.append(tab); builder.append("docProsStatCdList(문서승인상태코드 목록):"); appendStringListTo(builder, docProsStatCdList, tab); builder.append('\n'); }
		if(apvLnPno!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnPno(결재라인부모번호):").append(apvLnPno).append('\n'); }
		if(apvLnNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnNo(결재라인번호):").append(apvLnNo).append('\n'); }
		if(prevApvrApvDt!=null) { if(tab!=null) builder.append(tab); builder.append("prevApvrApvDt(이전결재자결재일시):").append(prevApvrApvDt).append('\n'); }
		if(vwDt!=null) { if(tab!=null) builder.append(tab); builder.append("vwDt(조회일시):").append(vwDt).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(bxId!=null) { if(tab!=null) builder.append(tab); builder.append("bxId(함ID):").append(bxId).append('\n'); }
		if(clsInfoNm!=null) { if(tab!=null) builder.append(tab); builder.append("clsInfoNm(분류정보명):").append(clsInfoNm).append('\n'); }
		if(pichApntYn!=null) { if(tab!=null) builder.append(tab); builder.append("pichApntYn(담당자지정여부):").append(pichApntYn).append('\n'); }
		if(pichTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("pichTypCd(담당자구분코드):").append(pichTypCd).append('\n'); }
		if(pichUid!=null) { if(tab!=null) builder.append(tab); builder.append("pichUid(담당자UID):").append(pichUid).append('\n'); }
		if(pichUidList!=null) { if(tab!=null) builder.append(tab); builder.append("pichUidList(담당자UID 목록):"); appendStringListTo(builder, pichUidList, tab); builder.append('\n'); }
		if(pichDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("pichDeptId(담당자부서ID):").append(pichDeptId).append('\n'); }
		if(hdlStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("hdlStatCd(처리상태코드):").append(hdlStatCd).append('\n'); }
		if(recvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("recvDeptId(수신처ID):").append(recvDeptId).append('\n'); }
		if(sendSeq!=null) { if(tab!=null) builder.append(tab); builder.append("sendSeq(발송일련번호):").append(sendSeq).append('\n'); }
		if(refApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("refApvNo(참조결재번호):").append(refApvNo).append('\n'); }
		if(pubBxDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxDeptId(공람게시부서ID):").append(pubBxDeptId).append('\n'); }
		if(pubBxDt!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxDt(공람게시일시):").append(pubBxDt).append('\n'); }
		if(pubBxEndYmd!=null) { if(tab!=null) builder.append(tab); builder.append("pubBxEndYmd(공람게시종료년월일):").append(pubBxEndYmd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(regDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("regDeptNm(등록부서명):").append(regDeptNm).append('\n'); }
		if(srchDocNo!=null) { if(tab!=null) builder.append(tab); builder.append("srchDocNo(검색문서번호):").append(srchDocNo).append('\n'); }
		if(srchRecvDocNo!=null) { if(tab!=null) builder.append(tab); builder.append("srchRecvDocNo(검색접수문서번호):").append(srchRecvDocNo).append('\n'); }
		if(paperApvNo!=null) { if(tab!=null) builder.append(tab); builder.append("paperApvNo(종이결재번호):").append(paperApvNo).append('\n'); }
		if(seculCdList!=null) { if(tab!=null) builder.append(tab); builder.append("seculCdList(보안등급코드 목록):"); appendStringListTo(builder, seculCdList, tab); builder.append('\n'); }
		if(bodyHtml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyHtml(본문HTML):").append(bodyHtml).append('\n'); }
		if(recLstDeptIdList!=null) { if(tab!=null) builder.append(tab); builder.append("recLstDeptIdList(대장부서ID 목록):"); appendStringListTo(builder, recLstDeptIdList, tab); builder.append('\n'); }
		if(refVwrUid!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrUid(참조열람자UID):").append(refVwrUid).append('\n'); }
		if(refVwrUidList!=null) { if(tab!=null) builder.append(tab); builder.append("refVwrUidList(참조열람자UID 목록):"); appendStringListTo(builder, refVwrUidList, tab); builder.append('\n'); }
		if(refVwStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("refVwStatCd(참조열람상태코드):").append(refVwStatCd).append('\n'); }
		if(refVwStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("refVwStatNm(참조열람상태명):").append(refVwStatNm).append('\n'); }
		if(apvrNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrNm(결재자명):").append(apvrNm).append('\n'); }
		if(apvrPositNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrPositNm(결재자직위명):").append(apvrPositNm).append('\n'); }
		if(apvrTitleNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrTitleNm(결재자직책명):").append(apvrTitleNm).append('\n'); }
		if(hasOpinYn!=null) { if(tab!=null) builder.append(tab); builder.append("hasOpinYn(의견보유여부):").append(hasOpinYn).append('\n'); }
		if(apvOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("apvOpinCont(결재의견내용):").append(apvOpinCont).append('\n'); }
		if(makDeptIdList!=null) { if(tab!=null) builder.append(tab); builder.append("makDeptIdList(기안부서ID 목록):"); appendStringListTo(builder, makDeptIdList, tab); builder.append('\n'); }
		if(formApvLnTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formApvLnTypNm(양식결재라인구분명):").append(formApvLnTypNm).append('\n'); }
		if(formWdthTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("formWdthTypNm(양식넓이구분명):").append(formWdthTypNm).append('\n'); }
		if(docLangTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("docLangTypNm(문서언어구분명):").append(docLangTypNm).append('\n'); }
		if(docStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("docStatNm(문서상태명):").append(docStatNm).append('\n'); }
		if(docProsStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("docProsStatNm(문서승인상태명):").append(docProsStatNm).append('\n'); }
		if(docKeepPrdNm!=null) { if(tab!=null) builder.append(tab); builder.append("docKeepPrdNm(문서보존기간명):").append(docKeepPrdNm).append('\n'); }
		if(docTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("docTypNm(문서구분명):").append(docTypNm).append('\n'); }
		if(seculNm!=null) { if(tab!=null) builder.append(tab); builder.append("seculNm(보안등급명):").append(seculNm).append('\n'); }
		if(docPw!=null) { if(tab!=null) builder.append(tab); builder.append("docPw(문서비밀번호):").append(docPw).append('\n'); }
		if(apvLnTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnTypNm(결재라인구분명):").append(apvLnTypNm).append('\n'); }
		if(enfcScopNm!=null) { if(tab!=null) builder.append(tab); builder.append("enfcScopNm(시행범위명):").append(enfcScopNm).append('\n'); }
		if(enfcDocKeepPrdNm!=null) { if(tab!=null) builder.append(tab); builder.append("enfcDocKeepPrdNm(시행문서보존기간명):").append(enfcDocKeepPrdNm).append('\n'); }
		if(enfcStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("enfcStatNm(시행상태명):").append(enfcStatNm).append('\n'); }
		if(recLstTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("recLstTypNm(대장구분명):").append(recLstTypNm).append('\n'); }
		super.toString(builder, tab);
	}
}
