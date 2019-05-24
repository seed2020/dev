package com.innobiz.orange.web.wh.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/28 16:45 ******/
/**
* 요청기본(WH_REQ_B) 테이블 VO 
*/
public class WhReqBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1063487514355565982L;

	/** 요청번호 */ 
	private String reqNo;
	
 	/** 회사ID */ 
	private String compId;
	
	/** 부서ID */ 
	private String deptId;
	
	/** 문서번호 */ 
	private String docNo;
	
 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

 	/** 요청일 */ 
	private String reqDt;

 	/** 완료희망일 */ 
	private String cmplPdt;

 	/** 프로그램ID */ 
	private String progrmId;

 	/** 프로그램명 */ 
	private String progrmNm;

 	/** 의뢰서여부 */ 
	private String writtenReqYn;

 	/** 의뢰서번호 */ 
	private String writtenReqNo;
	
	/** 업로드번호 */ 
	private String uploadNo;
	
 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
/** 추가*/
	
	/** 부서명 */ 
	private String deptNm;
	
	/** 옵션 검색구분 */ 
	private String schOptCat;
	
	/** 옵션 검색어 */ 
	private String schOptWord;
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob = true;
	
	/** 상세 조회 여부 */ 
	private boolean withDtl=false;
	
	/** 완료 정보 조회 여부 */ 
	private boolean withCmpl=false;
	
	/** 관련요청 조회 여부 */ 
	private boolean withRel=false;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 등록부서명 */ 
	private String regrDeptNm;
	
	/** 모듈ID */ 
	private String mdId;
	
	/** 모듈ID 목록 */ 
	private List<String> mdIdList;
	
	/** 모듈명 */ 
	private String mdNm;
	
	/** 상태코드 */ 
	private String statCd;
	
	/** 상태코드 */ 
	private String paramStatCd;
	
	/** 상태코드 배열 */ 
	private String[] statCdList;
	
	/** 담당자UID */ 
	private String pichUid;
	
	/** 담당자명 */ 
	private String pichNm;
	
	/** 접수자UID */ 
	private String recvUid;
	
	/** 접수자명 */ 
	private String recvNm;
	
	/** 접수일시 */ 
	private String recvDt;
	
	/** 처리자UID */ 
	private String hdlrUid;
	
	/** 처리자명 */ 
	private String hdlrNm;
	
	/** 유형번호 */ 
	private String catNo;
	
	/** 유형명 */ 
	private String catNm;
	
	/** 완료예정일 */ 
	private String cmplDueDt;
	
	/** 완료일시 */ 
	private String cmplDt;
	
	/** 평가여부 */ 
	private String evalYn;
	
	/** 평가명 */ 
	private String evalNm;
	
	/** 파일건수 */ 
	private Integer fileCnt;
	
	/** 전체건수 */ 
	private Integer totalCnt;
	
	/** 공수 */ 
	private String devHourVa;
	
	/** 완료사항 */ 
	private String hdlCont;
	
	/** 관련요청번호 */ 
	private String relNo;
	
	/** 회사ID목록 */
	private List<String> compIdList;
	
 	public void setReqNo(String reqNo) { 
		this.reqNo = reqNo;
	}
	/** 요청번호 */ 
	public String getReqNo() { 
		return reqNo;
	}
	
	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}
	
	public void setDeptId(String deptId) { 
		this.deptId = deptId;
	}
	/** 부서ID */ 
	public String getDeptId() { 
		return deptId;
	}
	
	public void setDocNo(String docNo) { 
		this.docNo = docNo;
	}
	/** 문서번호 */ 
	public String getDocNo() { 
		return docNo;
	}
	
	public void setSubj(String subj) { 
		this.subj = subj;
	}
	/** 제목 */ 
	public String getSubj() { 
		return subj;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}

	public void setReqDt(String reqDt) { 
		this.reqDt = reqDt;
	}
	/** 요청일 */ 
	public String getReqDt() { 
		return reqDt;
	}

	public void setCmplPdt(String cmplPdt) { 
		this.cmplPdt = cmplPdt;
	}
	/** 완료희망일 */ 
	public String getCmplPdt() { 
		return cmplPdt;
	}

	public void setProgrmId(String progrmId) { 
		this.progrmId = progrmId;
	}
	/** 프로그램ID */ 
	public String getProgrmId() { 
		return progrmId;
	}

	public void setProgrmNm(String progrmNm) { 
		this.progrmNm = progrmNm;
	}
	/** 프로그램명 */ 
	public String getProgrmNm() { 
		return progrmNm;
	}

	public void setWrittenReqYn(String writtenReqYn) { 
		this.writtenReqYn = writtenReqYn;
	}
	/** 의뢰서여부 */ 
	public String getWrittenReqYn() { 
		return writtenReqYn;
	}

	public void setWrittenReqNo(String writtenReqNo) { 
		this.writtenReqNo = writtenReqNo;
	}
	/** 의뢰서번호 */ 
	public String getWrittenReqNo() { 
		return writtenReqNo;
	}
	
	public void setUploadNo(String uploadNo) { 
		this.uploadNo = uploadNo;
	}
	/** 업로드번호 */ 
	public String getUploadNo() { 
		return uploadNo;
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
	
	/** 부서명 */ 
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	
	/** 옵션 검색구분 */ 
	public String getSchOptCat() {
		return schOptCat;
	}
	public void setSchOptCat(String schOptCat) {
		this.schOptCat = schOptCat;
	}
	
	/** 옵션 검색어 */ 
	public String getSchOptWord() {
		return schOptWord;
	}
	public void setSchOptWord(String schOptWord) {
		this.schOptWord = schOptWord;
	}
	
	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}
	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	
	/** 상세 조회 여부 */ 
	public boolean isWithDtl() {
		return withDtl;
	}
	public void setWithDtl(boolean withDtl) {
		this.withDtl = withDtl;
	}
	
	/** 완료 정보 조회 여부 */ 
	public boolean isWithCmpl() {
		return withCmpl;
	}
	public void setWithCmpl(boolean withCmpl) {
		this.withCmpl = withCmpl;
	}
	
	/** 관련요청 조회 여부 */ 
	public boolean isWithRel() {
		return withRel;
	}
	public void setWithRel(boolean withRel) {
		this.withRel = withRel;
	}
	
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 등록부서명 */ 
	public String getRegrDeptNm() {
		return regrDeptNm;
	}
	public void setRegrDeptNm(String regrDeptNm) {
		this.regrDeptNm = regrDeptNm;
	}
	
	/** 모듈ID */ 
	public String getMdId() {
		return mdId;
	}
	public void setMdId(String mdId) {
		this.mdId = mdId;
	}
	
	/** 모듈ID 목록 */ 
	public List<String> getMdIdList() {
		return mdIdList;
	}
	public void setMdIdList(List<String> mdIdList) {
		this.mdIdList = mdIdList;
	}
	
	/** 모듈명 */ 
	public String getMdNm() {
		return mdNm;
	}
	public void setMdNm(String mdNm) {
		this.mdNm = mdNm;
	}
	
	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}
	
	/** 상태코드 */ 
	public String getParamStatCd() {
		return paramStatCd;
	}
	public void setParamStatCd(String paramStatCd) {
		this.paramStatCd = paramStatCd;
	}
	
	/** 상태코드 배열 */ 
	public String[] getStatCdList() {
		return statCdList;
	}
	public void setStatCdList(String[] statCdList) {
		this.statCdList = statCdList;
	}
	
	/** 담당자UID */ 
	public String getPichUid() {
		return pichUid;
	}
	public void setPichUid(String pichUid) {
		this.pichUid = pichUid;
	}
	
	/** 담당자명 */
	public String getPichNm() {
		return pichNm;
	}
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
	}
	
	/** 접수자UID */ 
	public String getRecvUid() {
		return recvUid;
	}
	public void setRecvUid(String recvUid) {
		this.recvUid = recvUid;
	}
	
	/** 접수자명 */ 
	public String getRecvNm() {
		return recvNm;
	}
	public void setRecvNm(String recvNm) {
		this.recvNm = recvNm;
	}
	
	/** 접수일시 */ 
	public String getRecvDt() {
		return recvDt;
	}
	public void setRecvDt(String recvDt) {
		this.recvDt = recvDt;
	}
	/** 처리자UID */
	public String getHdlrUid() {
		return hdlrUid;
	}
	public void setHdlrUid(String hdlrUid) {
		this.hdlrUid = hdlrUid;
	}
	
	/** 처리자명 */
	public String getHdlrNm() {
		return hdlrNm;
	}
	public void setHdlrNm(String hdlrNm) {
		this.hdlrNm = hdlrNm;
	}
	
	/** 유형번호 */ 
	public String getCatNo() {
		return catNo;
	}
	
	public void setCatNo(String catNo) {
		this.catNo = catNo;
	}
	
	/** 유형명 */ 
	public String getCatNm() {
		return catNm;
	}
	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}
	
	public void setCmplDueDt(String cmplDueDt) { 
		this.cmplDueDt = cmplDueDt;
	}
	/** 완료예정일 */ 
	public String getCmplDueDt() { 
		return cmplDueDt;
	}
	
	public void setCmplDt(String cmplDt) { 
		this.cmplDt = cmplDt;
	}
	/** 완료일시 */ 
	public String getCmplDt() { 
		return cmplDt;
	}
	
	public void setEvalYn(String evalYn) { 
		this.evalYn = evalYn;
	}
	/** 평가여부 */ 
	public String getEvalYn() { 
		return evalYn;
	}
	
	/** 평가명 */ 
	public String getEvalNm() {
		return evalNm;
	}
	public void setEvalNm(String evalNm) {
		this.evalNm = evalNm;
	}
	
	/** 파일건수 */ 
	public Integer getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(Integer fileCnt) {
		this.fileCnt = fileCnt;
	}
	
	/** 전체건수 */ 
	public Integer getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(Integer totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	/** 공수 */ 
	public String getDevHourVa() {
		return devHourVa;
	}
	public void setDevHourVa(String devHourVa) {
		this.devHourVa = devHourVa;
	}
	
	/** 완료사항 */ 
	public String getHdlCont() {
		return hdlCont;
	}
	public void setHdlCont(String hdlCont) {
		this.hdlCont = hdlCont;
	}
	
	/** 관련요청번호 */ 
	public String getRelNo() {
		return relNo;
	}
	public void setRelNo(String relNo) {
		this.relNo = relNo;
	}
	
	/** 회사ID목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqBDao.selectWhReqB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqBDao.insertWhReqB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqBDao.updateWhReqB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqBDao.deleteWhReqB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqBDao.countWhReqB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(docNo!=null) { if(tab!=null) builder.append(tab); builder.append("docNo(문서번호):").append(docNo).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(reqDt!=null) { if(tab!=null) builder.append(tab); builder.append("reqDt(요청일):").append(reqDt).append('\n'); }
		if(cmplPdt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplPdt(완료희망일):").append(cmplPdt).append('\n'); }
		if(progrmId!=null) { if(tab!=null) builder.append(tab); builder.append("progrmId(프로그램ID):").append(progrmId).append('\n'); }
		if(progrmNm!=null) { if(tab!=null) builder.append(tab); builder.append("progrmNm(프로그램명):").append(progrmNm).append('\n'); }
		if(writtenReqYn!=null) { if(tab!=null) builder.append(tab); builder.append("writtenReqYn(의뢰서여부):").append(writtenReqYn).append('\n'); }
		if(writtenReqNo!=null) { if(tab!=null) builder.append(tab); builder.append("writtenReqNo(의뢰서번호):").append(writtenReqNo).append('\n'); }
		if(uploadNo!=null) { if(tab!=null) builder.append(tab); builder.append("uploadNo(업로드번호):").append(uploadNo).append('\n'); }		
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
