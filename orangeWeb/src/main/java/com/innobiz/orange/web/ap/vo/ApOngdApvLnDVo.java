package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 진행문서결재라인상세(AP_ONGD_APV_LN_D) 테이블 VO
 */
public class ApOngdApvLnDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1885449069968458672L;

	/** 결재번호 - KEY */
	private String apvNo;

	/** 결재라인부모번호 - KEY */
	private String apvLnPno;

	/** 결재라인번호 - KEY */
	private String apvLnNo;

	/** 결재라인이력번호 */
	private String apvLnHstNo;

	/** 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서 */
	private String dblApvTypCd;

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	private String apvrRoleCd;

	/** 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	private String apvStatCd;

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	private String absRsonCd;

	/** 부재사유명 */
	private String absRsonNm;

	/** 결재자부서여부 */
	private String apvrDeptYn;

	/** 담당자지정여부 */
	private String pichApntYn;

	/** 전결여부 */
	private String predYn;

	/** 대결자UID */
	private String agntUid;

	/** 결재자UID */
	private String apvrUid;

	/** 결재자명 */
	private String apvrNm;

	/** 결재자직위코드 */
	private String apvrPositCd;

	/** 결재자직위명 */
	private String apvrPositNm;

	/** 결재자직책코드 */
	private String apvrTitleCd;

	/** 결재자직책명 */
	private String apvrTitleNm;

	/** 결재부서ID */
	private String apvDeptId;

	/** 결재부서명 */
	private String apvDeptNm;

	/** 결재부서약어명 */
	private String apvDeptAbbrNm;

	/** 직위표시값 */
	private String positDispVa;

	/** 서명표시값 */
	private String signDispVa;

	/** 일시표시값 */
	private String dtDispVa;

	/** 서명이미지경로 */
	private String signImgPath;

	/** 조회일시 */
	private String vwDt;

	/** 결재일시 */
	private String apvDt;

	/** 이전결재자결재일시 */
	private String prevApvrApvDt;

	/** 결재의견내용 */
	private String apvOpinCont;

	/** 결재의견표시여부 */
	private String apvOpinDispYn;

	/** 이전본문이력번호 */
	private String prevBodyHstNo;

	/** 보류본문이력번호 */
	private String holdBodyHstNo;

	/** 이전결재라인이력번호 */
	private String prevApvLnHstNo;

	/** 보류결재라인이력번호 */
	private String holdApvLnHstNo;

	/** 이전첨부이력번호 */
	private String prevAttHstNo;

	/** 보류첨부이력번호 */
	private String holdAttHstNo;

	/** 이전수신처이력번호 */
	private String prevRecvDeptHstNo;

	/** 보류수신처이력번호 */
	private String holdRecvDeptHstNo;

	/** 이전참조문서이력번호 */
	private String prevRefDocHstNo;

	/** 보류참조문서이력번호 */
	private String holdRefDocHstNo;

	/** 고정결재자여부 */
	private String fixdApvrYn;


	// 추가컬럼
	/** 스토리지 */
	private String storage;

	/** 테이블타입 */
	private String tableType = null;

	/** 결재자역할코드 목록 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	private List<String> apvrRoleCdList;

	/** 결재상태코드 목록 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	private List<String> apvStatCdList;

	/** 다음결재라인번호 */
	private String nextApvLnNo;

	/** 발송일련번호 */
	private String sendSeq;

	/** 하위의견여부 */
	private String subOpinYn;

	/** WHERE 결재라인부모번호 */
	private String whereApvLnPno;

	/** WHERE 결재라인번호 */
	private String whereApvLnNo;

	/** 결재상태명 */
	private String apvStatNm;

	/** 대결자명 */
	private String agntNm;

	/** 결재번호 - KEY */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 - KEY */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 결재라인부모번호 - KEY */
	public String getApvLnPno() {
		return apvLnPno;
	}

	/** 결재라인부모번호 - KEY */
	public void setApvLnPno(String apvLnPno) {
		this.apvLnPno = apvLnPno;
	}

	/** 결재라인번호 - KEY */
	public String getApvLnNo() {
		return apvLnNo;
	}

	/** 결재라인번호 - KEY */
	public void setApvLnNo(String apvLnNo) {
		this.apvLnNo = apvLnNo;
	}

	/** 결재라인이력번호 */
	public String getApvLnHstNo() {
		return apvLnHstNo;
	}

	/** 결재라인이력번호 */
	public void setApvLnHstNo(String apvLnHstNo) {
		this.apvLnHstNo = apvLnHstNo;
	}

	/** 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서 */
	public String getDblApvTypCd() {
		return dblApvTypCd;
	}

	/** 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서 */
	public void setDblApvTypCd(String dblApvTypCd) {
		this.dblApvTypCd = dblApvTypCd;
	}

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public String getApvrRoleCd() {
		return apvrRoleCd;
	}

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public void setApvrRoleCd(String apvrRoleCd) {
		this.apvrRoleCd = apvrRoleCd;
	}

	/** 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public String getApvStatCd() {
		return apvStatCd;
	}

	/** 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public void setApvStatCd(String apvStatCd) {
		this.apvStatCd = apvStatCd;
	}

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	public String getAbsRsonCd() {
		return absRsonCd;
	}

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	public void setAbsRsonCd(String absRsonCd) {
		this.absRsonCd = absRsonCd;
	}

	/** 부재사유명 */
	public String getAbsRsonNm() {
		return absRsonNm;
	}

	/** 부재사유명 */
	public void setAbsRsonNm(String absRsonNm) {
		this.absRsonNm = absRsonNm;
	}

	/** 결재자부서여부 */
	public String getApvrDeptYn() {
		return apvrDeptYn;
	}

	/** 결재자부서여부 */
	public void setApvrDeptYn(String apvrDeptYn) {
		this.apvrDeptYn = apvrDeptYn;
	}

	/** 담당자지정여부 */
	public String getPichApntYn() {
		return pichApntYn;
	}

	/** 담당자지정여부 */
	public void setPichApntYn(String pichApntYn) {
		this.pichApntYn = pichApntYn;
	}

	/** 전결여부 */
	public String getPredYn() {
		return predYn;
	}

	/** 전결여부 */
	public void setPredYn(String predYn) {
		this.predYn = predYn;
	}

	/** 대결자UID */
	public String getAgntUid() {
		return agntUid;
	}

	/** 대결자UID */
	public void setAgntUid(String agntUid) {
		this.agntUid = agntUid;
	}

	/** 결재자UID */
	public String getApvrUid() {
		return apvrUid;
	}

	/** 결재자UID */
	public void setApvrUid(String apvrUid) {
		this.apvrUid = apvrUid;
	}

	/** 결재자명 */
	public String getApvrNm() {
		return apvrNm;
	}

	/** 결재자명 */
	public void setApvrNm(String apvrNm) {
		this.apvrNm = apvrNm;
	}

	/** 결재자직위코드 */
	public String getApvrPositCd() {
		return apvrPositCd;
	}

	/** 결재자직위코드 */
	public void setApvrPositCd(String apvrPositCd) {
		this.apvrPositCd = apvrPositCd;
	}

	/** 결재자직위명 */
	public String getApvrPositNm() {
		return apvrPositNm;
	}

	/** 결재자직위명 */
	public void setApvrPositNm(String apvrPositNm) {
		this.apvrPositNm = apvrPositNm;
	}

	/** 결재자직책코드 */
	public String getApvrTitleCd() {
		return apvrTitleCd;
	}

	/** 결재자직책코드 */
	public void setApvrTitleCd(String apvrTitleCd) {
		this.apvrTitleCd = apvrTitleCd;
	}

	/** 결재자직책명 */
	public String getApvrTitleNm() {
		return apvrTitleNm;
	}

	/** 결재자직책명 */
	public void setApvrTitleNm(String apvrTitleNm) {
		this.apvrTitleNm = apvrTitleNm;
	}

	/** 결재부서ID */
	public String getApvDeptId() {
		return apvDeptId;
	}

	/** 결재부서ID */
	public void setApvDeptId(String apvDeptId) {
		this.apvDeptId = apvDeptId;
	}

	/** 결재부서명 */
	public String getApvDeptNm() {
		return apvDeptNm;
	}

	/** 결재부서명 */
	public void setApvDeptNm(String apvDeptNm) {
		this.apvDeptNm = apvDeptNm;
	}

	/** 결재부서약어명 */
	public String getApvDeptAbbrNm() {
		return apvDeptAbbrNm;
	}

	/** 결재부서약어명 */
	public void setApvDeptAbbrNm(String apvDeptAbbrNm) {
		this.apvDeptAbbrNm = apvDeptAbbrNm;
	}

	/** 직위표시값 */
	public String getPositDispVa() {
		return positDispVa;
	}

	/** 직위표시값 */
	public void setPositDispVa(String positDispVa) {
		this.positDispVa = positDispVa;
	}

	/** 서명표시값 */
	public String getSignDispVa() {
		return signDispVa;
	}

	/** 서명표시값 */
	public void setSignDispVa(String signDispVa) {
		this.signDispVa = signDispVa;
	}

	/** 일시표시값 */
	public String getDtDispVa() {
		return dtDispVa;
	}

	/** 일시표시값 */
	public void setDtDispVa(String dtDispVa) {
		this.dtDispVa = dtDispVa;
	}

	/** 서명이미지경로 */
	public String getSignImgPath() {
		return signImgPath;
	}

	/** 서명이미지경로 */
	public void setSignImgPath(String signImgPath) {
		this.signImgPath = signImgPath;
	}

	/** 조회일시 */
	public String getVwDt() {
		return vwDt;
	}

	/** 조회일시 */
	public void setVwDt(String vwDt) {
		this.vwDt = vwDt;
	}

	/** 결재일시 */
	public String getApvDt() {
		return apvDt;
	}

	/** 결재일시 */
	public void setApvDt(String apvDt) {
		this.apvDt = apvDt;
	}

	/** 이전결재자결재일시 */
	public String getPrevApvrApvDt() {
		return prevApvrApvDt;
	}

	/** 이전결재자결재일시 */
	public void setPrevApvrApvDt(String prevApvrApvDt) {
		this.prevApvrApvDt = prevApvrApvDt;
	}

	/** 결재의견내용 */
	public String getApvOpinCont() {
		return apvOpinCont;
	}

	/** 결재의견내용 */
	public void setApvOpinCont(String apvOpinCont) {
		this.apvOpinCont = apvOpinCont;
	}

	/** 결재의견표시여부 */
	public String getApvOpinDispYn() {
		return apvOpinDispYn;
	}

	/** 결재의견표시여부 */
	public void setApvOpinDispYn(String apvOpinDispYn) {
		this.apvOpinDispYn = apvOpinDispYn;
	}

	/** 이전본문이력번호 */
	public String getPrevBodyHstNo() {
		return prevBodyHstNo;
	}

	/** 이전본문이력번호 */
	public void setPrevBodyHstNo(String prevBodyHstNo) {
		this.prevBodyHstNo = prevBodyHstNo;
	}

	/** 보류본문이력번호 */
	public String getHoldBodyHstNo() {
		return holdBodyHstNo;
	}

	/** 보류본문이력번호 */
	public void setHoldBodyHstNo(String holdBodyHstNo) {
		this.holdBodyHstNo = holdBodyHstNo;
	}

	/** 이전결재라인이력번호 */
	public String getPrevApvLnHstNo() {
		return prevApvLnHstNo;
	}

	/** 이전결재라인이력번호 */
	public void setPrevApvLnHstNo(String prevApvLnHstNo) {
		this.prevApvLnHstNo = prevApvLnHstNo;
	}

	/** 보류결재라인이력번호 */
	public String getHoldApvLnHstNo() {
		return holdApvLnHstNo;
	}

	/** 보류결재라인이력번호 */
	public void setHoldApvLnHstNo(String holdApvLnHstNo) {
		this.holdApvLnHstNo = holdApvLnHstNo;
	}

	/** 이전첨부이력번호 */
	public String getPrevAttHstNo() {
		return prevAttHstNo;
	}

	/** 이전첨부이력번호 */
	public void setPrevAttHstNo(String prevAttHstNo) {
		this.prevAttHstNo = prevAttHstNo;
	}

	/** 보류첨부이력번호 */
	public String getHoldAttHstNo() {
		return holdAttHstNo;
	}

	/** 보류첨부이력번호 */
	public void setHoldAttHstNo(String holdAttHstNo) {
		this.holdAttHstNo = holdAttHstNo;
	}

	/** 이전수신처이력번호 */
	public String getPrevRecvDeptHstNo() {
		return prevRecvDeptHstNo;
	}

	/** 이전수신처이력번호 */
	public void setPrevRecvDeptHstNo(String prevRecvDeptHstNo) {
		this.prevRecvDeptHstNo = prevRecvDeptHstNo;
	}

	/** 보류수신처이력번호 */
	public String getHoldRecvDeptHstNo() {
		return holdRecvDeptHstNo;
	}

	/** 보류수신처이력번호 */
	public void setHoldRecvDeptHstNo(String holdRecvDeptHstNo) {
		this.holdRecvDeptHstNo = holdRecvDeptHstNo;
	}

	/** 이전참조문서이력번호 */
	public String getPrevRefDocHstNo() {
		return prevRefDocHstNo;
	}

	/** 이전참조문서이력번호 */
	public void setPrevRefDocHstNo(String prevRefDocHstNo) {
		this.prevRefDocHstNo = prevRefDocHstNo;
	}

	/** 보류참조문서이력번호 */
	public String getHoldRefDocHstNo() {
		return holdRefDocHstNo;
	}

	/** 보류참조문서이력번호 */
	public void setHoldRefDocHstNo(String holdRefDocHstNo) {
		this.holdRefDocHstNo = holdRefDocHstNo;
	}

	/** 고정결재자여부 */
	public String getFixdApvrYn() {
		return fixdApvrYn;
	}

	/** 고정결재자여부 */
	public void setFixdApvrYn(String fixdApvrYn) {
		this.fixdApvrYn = fixdApvrYn;
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

	/** 테이블 타입 리턴 */
	public String getTableType(){
		return tableType==null ? "D" : tableType;
	}

	/** 테이블 타입 제거 */
	public void removeTableType(){
		tableType = null;
	}

	/** 히스토리 테이블 설정 */
	public void setHistory(){
		tableType = "H";
	}

	/** 이행 테이블 설정 */
	public void setExecution(){
		tableType = "E";
	}

	/** 결재자역할코드 목록 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public List<String> getApvrRoleCdList() {
		return apvrRoleCdList;
	}

	/** 결재자역할코드 목록 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public void setApvrRoleCdList(List<String> apvrRoleCdList) {
		this.apvrRoleCdList = apvrRoleCdList;
	}

	/** 결재상태코드 목록 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public List<String> getApvStatCdList() {
		return apvStatCdList;
	}

	/** 결재상태코드 목록 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료 */
	public void setApvStatCdList(List<String> apvStatCdList) {
		this.apvStatCdList = apvStatCdList;
	}

	/** 다음결재라인번호 */
	public String getNextApvLnNo() {
		return nextApvLnNo;
	}

	/** 다음결재라인번호 */
	public void setNextApvLnNo(String nextApvLnNo) {
		this.nextApvLnNo = nextApvLnNo;
	}

	/** 발송일련번호 */
	public String getSendSeq() {
		return sendSeq;
	}

	/** 발송일련번호 */
	public void setSendSeq(String sendSeq) {
		this.sendSeq = sendSeq;
	}

	/** 하위의견여부 */
	public String getSubOpinYn() {
		return subOpinYn;
	}

	/** 하위의견여부 */
	public void setSubOpinYn(String subOpinYn) {
		this.subOpinYn = subOpinYn;
	}

	/** WHERE 결재라인부모번호 */
	public String getWhereApvLnPno() {
		return whereApvLnPno;
	}

	/** WHERE 결재라인부모번호 */
	public void setWhereApvLnPno(String whereApvLnPno) {
		this.whereApvLnPno = whereApvLnPno;
	}

	/** WHERE 결재라인번호 */
	public String getWhereApvLnNo() {
		return whereApvLnNo;
	}

	/** WHERE 결재라인번호 */
	public void setWhereApvLnNo(String whereApvLnNo) {
		this.whereApvLnNo = whereApvLnNo;
	}

	/** 결재상태명 */
	public String getApvStatNm() {
		return apvStatNm;
	}

	/** 결재상태명 */
	public void setApvStatNm(String apvStatNm) {
		this.apvStatNm = apvStatNm;
	}

	/** 대결자명 */
	public String getAgntNm() {
		return agntNm;
	}

	/** 대결자명 */
	public void setAgntNm(String agntNm) {
		this.agntNm = agntNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.selectApOngdApvLnD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.insertApOngdApvLnD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.updateApOngdApvLnD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.deleteApOngdApvLnD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApOngdApvLnDDao.countApOngdApvLnD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":진행문서결재라인상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(storage!=null || tableType!=null) { if(tab!=null) builder.append(tab); builder.append("storage(스토리지):AP_").append(storage==null ? "ONGD" : storage).append("_APV_LN_").append(tableType!=null ? tableType : "D").append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호-PK):").append(apvNo).append('\n'); }
		if(apvLnPno!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnPno(결재라인부모번호-PK):").append(apvLnPno).append('\n'); }
		if(apvLnNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnNo(결재라인번호-PK):").append(apvLnNo).append('\n'); }
		if(apvLnHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnHstNo(결재라인이력번호):").append(apvLnHstNo).append('\n'); }
		if(dblApvTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("dblApvTypCd(이중결재구분코드):").append(dblApvTypCd).append('\n'); }
		if(apvrRoleCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleCd(결재자역할코드):").append(apvrRoleCd).append('\n'); }
		if(apvStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvStatCd(결재상태코드):").append(apvStatCd).append('\n'); }
		if(absRsonCd!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonCd(부재사유코드):").append(absRsonCd).append('\n'); }
		if(absRsonNm!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonNm(부재사유명):").append(absRsonNm).append('\n'); }
		if(apvrDeptYn!=null) { if(tab!=null) builder.append(tab); builder.append("apvrDeptYn(결재자부서여부):").append(apvrDeptYn).append('\n'); }
		if(pichApntYn!=null) { if(tab!=null) builder.append(tab); builder.append("pichApntYn(담당자지정여부):").append(pichApntYn).append('\n'); }
		if(predYn!=null) { if(tab!=null) builder.append(tab); builder.append("predYn(전결여부):").append(predYn).append('\n'); }
		if(agntUid!=null) { if(tab!=null) builder.append(tab); builder.append("agntUid(대결자UID):").append(agntUid).append('\n'); }
		if(apvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("apvrUid(결재자UID):").append(apvrUid).append('\n'); }
		if(apvrNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrNm(결재자명):").append(apvrNm).append('\n'); }
		if(apvrPositCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvrPositCd(결재자직위코드):").append(apvrPositCd).append('\n'); }
		if(apvrPositNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrPositNm(결재자직위명):").append(apvrPositNm).append('\n'); }
		if(apvrTitleCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvrTitleCd(결재자직책코드):").append(apvrTitleCd).append('\n'); }
		if(apvrTitleNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrTitleNm(결재자직책명):").append(apvrTitleNm).append('\n'); }
		if(apvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("apvDeptId(결재부서ID):").append(apvDeptId).append('\n'); }
		if(apvDeptNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvDeptNm(결재부서명):").append(apvDeptNm).append('\n'); }
		if(apvDeptAbbrNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvDeptAbbrNm(결재부서약어명):").append(apvDeptAbbrNm).append('\n'); }
		if(positDispVa!=null) { if(tab!=null) builder.append(tab); builder.append("positDispVa(직위표시값):").append(positDispVa).append('\n'); }
		if(signDispVa!=null) { if(tab!=null) builder.append(tab); builder.append("signDispVa(서명표시값):").append(signDispVa).append('\n'); }
		if(dtDispVa!=null) { if(tab!=null) builder.append(tab); builder.append("dtDispVa(일시표시값):").append(dtDispVa).append('\n'); }
		if(signImgPath!=null) { if(tab!=null) builder.append(tab); builder.append("signImgPath(서명이미지경로):").append(signImgPath).append('\n'); }
		if(vwDt!=null) { if(tab!=null) builder.append(tab); builder.append("vwDt(조회일시):").append(vwDt).append('\n'); }
		if(apvDt!=null) { if(tab!=null) builder.append(tab); builder.append("apvDt(결재일시):").append(apvDt).append('\n'); }
		if(prevApvrApvDt!=null) { if(tab!=null) builder.append(tab); builder.append("prevApvrApvDt(이전결재자결재일시):").append(prevApvrApvDt).append('\n'); }
		if(apvOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("apvOpinCont(결재의견내용):").append(apvOpinCont).append('\n'); }
		if(apvOpinDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("apvOpinDispYn(결재의견표시여부):").append(apvOpinDispYn).append('\n'); }
		if(prevBodyHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("prevBodyHstNo(이전본문이력번호):").append(prevBodyHstNo).append('\n'); }
		if(holdBodyHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("holdBodyHstNo(보류본문이력번호):").append(holdBodyHstNo).append('\n'); }
		if(prevApvLnHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("prevApvLnHstNo(이전결재라인이력번호):").append(prevApvLnHstNo).append('\n'); }
		if(holdApvLnHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("holdApvLnHstNo(보류결재라인이력번호):").append(holdApvLnHstNo).append('\n'); }
		if(prevAttHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("prevAttHstNo(이전첨부이력번호):").append(prevAttHstNo).append('\n'); }
		if(holdAttHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("holdAttHstNo(보류첨부이력번호):").append(holdAttHstNo).append('\n'); }
		if(prevRecvDeptHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("prevRecvDeptHstNo(이전수신처이력번호):").append(prevRecvDeptHstNo).append('\n'); }
		if(holdRecvDeptHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("holdRecvDeptHstNo(보류수신처이력번호):").append(holdRecvDeptHstNo).append('\n'); }
		if(prevRefDocHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("prevRefDocHstNo(이전참조문서이력번호):").append(prevRefDocHstNo).append('\n'); }
		if(holdRefDocHstNo!=null) { if(tab!=null) builder.append(tab); builder.append("holdRefDocHstNo(보류참조문서이력번호):").append(holdRefDocHstNo).append('\n'); }
		if(fixdApvrYn!=null) { if(tab!=null) builder.append(tab); builder.append("fixdApvrYn(고정결재자여부):").append(fixdApvrYn).append('\n'); }
		if(apvrRoleCdList!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleCdList(결재자역할코드 목록):"); appendStringListTo(builder, apvrRoleCdList, tab); builder.append('\n'); }
		if(apvStatCdList!=null) { if(tab!=null) builder.append(tab); builder.append("apvStatCdList(결재상태코드 목록):"); appendStringListTo(builder, apvStatCdList, tab); builder.append('\n'); }
		if(nextApvLnNo!=null) { if(tab!=null) builder.append(tab); builder.append("nextApvLnNo(다음결재라인번호):").append(nextApvLnNo).append('\n'); }
		if(sendSeq!=null) { if(tab!=null) builder.append(tab); builder.append("sendSeq(발송일련번호):").append(sendSeq).append('\n'); }
		if(subOpinYn!=null) { if(tab!=null) builder.append(tab); builder.append("subOpinYn(하위의견여부):").append(subOpinYn).append('\n'); }
		if(whereApvLnPno!=null) { if(tab!=null) builder.append(tab); builder.append("whereApvLnPno(WHERE 결재라인부모번호):").append(whereApvLnPno).append('\n'); }
		if(whereApvLnNo!=null) { if(tab!=null) builder.append(tab); builder.append("whereApvLnNo(WHERE 결재라인번호):").append(whereApvLnNo).append('\n'); }
		if(apvStatNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvStatNm(결재상태명):").append(apvStatNm).append('\n'); }
		if(agntNm!=null) { if(tab!=null) builder.append(tab); builder.append("agntNm(대결자명):").append(agntNm).append('\n'); }
		super.toString(builder, tab);
	}
}
