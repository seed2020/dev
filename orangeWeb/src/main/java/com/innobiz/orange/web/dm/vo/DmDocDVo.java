package com.innobiz.orange.web.dm.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/07/06 13:52 ******/
/**
* 문서상세(DM_DOC_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmDocDVo extends DmCmmBVo {
 	/** 문서그룹ID */ 
	private String docGrpId;
	
	/** 하위여부 */ 
	private String subYn;
	
	/** 하위문서그룹ID */ 
	private String subDocGrpId;
	
	/** 부모문서ID */ 
	private String docPid;
	
	/** 정렬순서 */
	private String sortOrdr;
	
	/** 하위정렬단계 */ 
	private Integer sortDpth;
	
 	/** 상태코드 */ 
	private String statCd;

 	/** 버전값 */ 
	private String verVa;

 	/** 폴더ID */ 
	private String fldId;

 	/** 문서번호 */ 
	private String docNo;

 	/** 보존기한일시 */ 
	private String keepPrdDt;

 	/** 소유자UID */ 
	private String ownrUid;

 	/** 문서보존기간코드 */ 
	private String docKeepPrdCd;

 	/** 보안등급코드 */ 
	private String seculCd;

 	/** 완료일시 */ 
	private String cmplDt;
	
	/** 조회수 */
	private Integer readCnt;
	
	/** 참조구분 */ 
	private String refTyp;

 	/** 참조ID */ 
	private String refId;

 	/** 참조URL */ 
	private String refUrl;

 	/** 원본여부 */ 
	private String orgnYn;
	
	/** 소유자명 */ 
	private String ownrNm;
	
	/** 폴더명 */
	private String fldNm;
	
	/** 문서보존기간명 */ 
	private String docKeepPrdNm;

 	/** 보안등급명 */ 
	private String seculNm;
	
	/** 보안등급 목록 */
	private List<String> seculCdList;
	
	/** 최대정렬순서 */
	private String maxSortOrdr;
	
	/** 조회옵션 - 조회 사용자UID */ 
	private String srchUserUid;
	
	/** 조회 - 조회 조직ID */
	private String srchOrgId;
	
	/** 조회옵션 - 권한적용여부 */ 
	private boolean isListSrchAuth;
	
	/** 폴더조회여부 - 사용자가 속한*/ 
	private boolean isFldSrch = true;
	
	/** 폴더 Vo List*/
	private List<DmFldBVo> dmFldBVoList;
	
	/** 하위문서그룹ID 목록*/ 
	private List<String> subDocGrpIdList;
	
	/** 문서열람요청 상태코드 */
	private String viewReqStatCd;
	
	/** 대상ID */
	private String tgtId;
	
	/** 대상명 */
	private String tgtNm;
	
	/** 대상구분코드 */
	private String tgtTypCd;
	
	/** 문서열람요청 상태 */
	private String viewReqStatNm;
	
	/** 대상구분명 */
	private String tgtTypNm;
	
	/** 열람시작일시 */
	private String readStrtDt;
	
	/** 열람종료일시 */
	private String readEndDt;
	
	/** 요청자UID */
	private String reqUserUid;
	
	/** 요청사용자명 */
	private String reqUserNm;
	
	/** 요청일시 */
	private String reqDt;
	
	/** 반려의견 */
	private String rjtOpin;
	
	/** 이관ID */ 
	private String tranId;
	
	/** 심의상태코드[A,S,R] */ 
	private String discStatCd;
	
	/** 심의상태명 */ 
	private String discStatNm;
	
	/** 조회여부 */ 
	private String readYn;
	
	/** 문서열람대상 */ 
	private DmPubDocTgtDVo dmPubDocTgtDVo;
	
	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}
	
	/** 하위여부 */ 
	public String getSubYn() {
		return subYn;
	}

	public void setSubYn(String subYn) {
		this.subYn = subYn;
	}
	
	/** 하위문서그룹ID */ 
	public String getSubDocGrpId() {
		return subDocGrpId;
	}

	public void setSubDocGrpId(String subDocGrpId) {
		this.subDocGrpId = subDocGrpId;
	}
	
	/** 부모문서ID */ 
	public String getDocPid() {
		return docPid;
	}

	public void setDocPid(String docPid) {
		this.docPid = docPid;
	}
	
	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}
	
	/** 정렬단계 */ 
	public Integer getSortDpth() {
		return sortDpth;
	}

	public void setSortDpth(Integer sortDpth) {
		this.sortDpth = sortDpth;
	}
	
	public void setKeepPrdDt(String keepPrdDt) { 
		this.keepPrdDt = keepPrdDt;
	}
	/** 보존기한일시 */ 
	public String getKeepPrdDt() { 
		return keepPrdDt;
	}

	/** 소유자명 */ 
	public String getOwnrNm() {
		return ownrNm;
	}

	public void setOwnrNm(String ownrNm) {
		this.ownrNm = ownrNm;
	}

	/** 폴더명 */
	public String getFldNm() {
		return fldNm;
	}

	public void setFldNm(String fldNm) {
		this.fldNm = fldNm;
	}
	
	/** 폴더 Vo List*/
	public List<DmFldBVo> getDmFldBVoList() {
		return dmFldBVoList;
	}

	public void setDmFldBVoList(List<DmFldBVo> dmFldBVoList) {
		this.dmFldBVoList = dmFldBVoList;
	}
	
	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}
	
	public void setVerVa(String verVa) { 
		this.verVa = verVa;
	}
	/** 버전값 */ 
	public String getVerVa() { 
		return verVa;
	}

	public void setFldId(String fldId) { 
		this.fldId = fldId;
	}
	/** 폴더ID */ 
	public String getFldId() { 
		return fldId;
	}
	
	public void setDocNo(String docNo) { 
		this.docNo = docNo;
	}
	/** 문서번호 */ 
	public String getDocNo() { 
		return docNo;
	}
	
	public void setOwnrUid(String ownrUid) { 
		this.ownrUid = ownrUid;
	}
	/** 소유자ID */ 
	public String getOwnrUid() { 
		return ownrUid;
	}

	public void setDocKeepPrdCd(String docKeepPrdCd) { 
		this.docKeepPrdCd = docKeepPrdCd;
	}
	/** 문서보존기간코드 */ 
	public String getDocKeepPrdCd() { 
		return docKeepPrdCd;
	}

	public void setSeculCd(String seculCd) { 
		this.seculCd = seculCd;
	}
	/** 보안등급코드 */ 
	public String getSeculCd() { 
		return seculCd;
	}
	
	/** 완료일시 */ 
	public String getCmplDt() {
		return cmplDt;
	}

	public void setCmplDt(String cmplDt) {
		this.cmplDt = cmplDt;
	}
	
	/** 조회수 */
	public Integer getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}
	
	public void setRefTyp(String refTyp) { 
		this.refTyp = refTyp;
	}
	/** 참조구분 */ 
	public String getRefTyp() { 
		return refTyp;
	}

	public void setRefId(String refId) { 
		this.refId = refId;
	}
	/** 참조ID */ 
	public String getRefId() { 
		return refId;
	}

	public void setRefUrl(String refUrl) { 
		this.refUrl = refUrl;
	}
	/** 참조URL */ 
	public String getRefUrl() { 
		return refUrl;
	}

	public void setOrgnYn(String orgnYn) { 
		this.orgnYn = orgnYn;
	}
	/** 원본여부 */ 
	public String getOrgnYn() { 
		return orgnYn;
	}
	
	/** 문서보존기간명 */ 
	public String getDocKeepPrdNm() {
		return docKeepPrdNm;
	}

	public void setDocKeepPrdNm(String docKeepPrdNm) {
		this.docKeepPrdNm = docKeepPrdNm;
	}
	
	/** 보안등급명 */ 
	public String getSeculNm() {
		return seculNm;
	}

	public void setSeculNm(String seculNm) {
		this.seculNm = seculNm;
	}
	
	/** 하위문서그룹ID 목록*/ 
	public List<String> getSubDocGrpIdList() {
		return subDocGrpIdList;
	}
	public void setSubDocGrpIdList(List<String> subDocGrpIdList) {
		this.subDocGrpIdList = subDocGrpIdList;
	}
	
	/** 조회옵션 - 조회 사용자UID */ 
	public String getSrchUserUid() {
		return srchUserUid;
	}
	public void setSrchUserUid(String srchUserUid) {
		this.srchUserUid = srchUserUid;
	}
	
	/** 조회 - 조회 조직ID */
	public String getSrchOrgId() {
		return srchOrgId;
	}
	public void setSrchOrgId(String srchOrgId) {
		this.srchOrgId = srchOrgId;
	}
	/** 조회옵션 - 권한적용여부 */
	public boolean isListSrchAuth() {
		return isListSrchAuth;
	}
	public void setListSrchAuth(boolean isListSrchAuth) {
		this.isListSrchAuth = isListSrchAuth;
	}
	/** 보안등급 목록 */
	public List<String> getSeculCdList() {
		return seculCdList;
	}
	public void setSeculCdList(List<String> seculCdList) {
		this.seculCdList = seculCdList;
	}
	
	/** 폴더조회여부 - 사용자가 속한*/ 
	public boolean isFldSrch() {
		return isFldSrch;
	}
	public void setFldSrch(boolean isFldSrch) {
		this.isFldSrch = isFldSrch;
	}
	
	/** 최대정렬순서 */
	public String getMaxSortOrdr() {
		return maxSortOrdr;
	}
	public void setMaxSortOrdr(String maxSortOrdr) {
		this.maxSortOrdr = maxSortOrdr;
	}
	
	/** 문서열람요청 상태코드 */
	public String getViewReqStatCd() {
		return viewReqStatCd;
	}
	public void setViewReqStatCd(String viewReqStatCd) {
		this.viewReqStatCd = viewReqStatCd;
	}
	
	/** 대상ID */
	public String getTgtId() {
		return tgtId;
	}
	public void setTgtId(String tgtId) {
		this.tgtId = tgtId;
	}
	/** 대상명 */
	public String getTgtNm() {
		return tgtNm;
	}
	public void setTgtNm(String tgtNm) {
		this.tgtNm = tgtNm;
	}
	/** 대상구분 */
	public String getTgtTypCd() {
		return tgtTypCd;
	}
	public void setTgtTypCd(String tgtTypCd) {
		this.tgtTypCd = tgtTypCd;
	}
	
	/** 문서열람요청 상태 */
	public String getViewReqStatNm() {
		return viewReqStatNm;
	}
	public void setViewReqStatNm(String viewReqStatNm) {
		this.viewReqStatNm = viewReqStatNm;
	}
	/** 대상구분명 */
	public String getTgtTypNm() {
		return tgtTypNm;
	}
	public void setTgtTypNm(String tgtTypNm) {
		this.tgtTypNm = tgtTypNm;
	}
	/** 열람시작일시 */
	public String getReadStrtDt() {
		return readStrtDt;
	}
	public void setReadStrtDt(String readStrtDt) {
		this.readStrtDt = readStrtDt;
	}
	/** 열람종료일시 */
	public String getReadEndDt() {
		return readEndDt;
	}
	public void setReadEndDt(String readEndDt) {
		this.readEndDt = readEndDt;
	}
	
	/** 요청사용자명 */
	public String getReqUserNm() {
		return reqUserNm;
	}
	public void setReqUserNm(String reqUserNm) {
		this.reqUserNm = reqUserNm;
	}
	
	/** 요청자UID */
	public String getReqUserUid() {
		return reqUserUid;
	}
	public void setReqUserUid(String reqUserUid) {
		this.reqUserUid = reqUserUid;
	}
	/** 요청일시 */
	public String getReqDt() {
		return reqDt;
	}
	public void setReqDt(String reqDt) {
		this.reqDt = reqDt;
	}
	/** 반려의견 */
	public String getRjtOpin() {
		return rjtOpin;
	}
	public void setRjtOpin(String rjtOpin) {
		this.rjtOpin = rjtOpin;
	}
	
	/** 이관ID */ 
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
 	public String getTranId() {
		return tranId;
	}
 	
 	/** 심의상태코드[A,S,R] */ 
	public String getDiscStatCd() {
		return discStatCd;
	}
	public void setDiscStatCd(String discStatCd) {
		this.discStatCd = discStatCd;
	}
	
	/** 심의상태명 */ 
	public String getDiscStatNm() {
		return discStatNm;
	}
	public void setDiscStatNm(String discStatNm) {
		this.discStatNm = discStatNm;
	}
	
	/** 조회여부 */ 
	public String getReadYn() {
		return readYn;
	}
	public void setReadYn(String readYn) {
		this.readYn = readYn;
	}
	
	/** 문서열람대상 */ 
	public DmPubDocTgtDVo getDmPubDocTgtDVo() {
		return dmPubDocTgtDVo;
	}
	public void setDmPubDocTgtDVo(DmPubDocTgtDVo dmPubDocTgtDVo) {
		this.dmPubDocTgtDVo = dmPubDocTgtDVo;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocDDao.selectDmDocD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocDDao.insertDmDocD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocDDao.updateDmDocD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocDDao.deleteDmDocD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocDDao.countDmDocD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":문서상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(verVa!=null) { if(tab!=null) builder.append(tab); builder.append("verVa(버전값):").append(verVa).append('\n'); }
		if(fldId!=null) { if(tab!=null) builder.append(tab); builder.append("fldId(폴더ID):").append(fldId).append('\n'); }
		if(docNo!=null) { if(tab!=null) builder.append(tab); builder.append("docNo(문서번호):").append(docNo).append('\n'); }
		if(keepPrdDt!=null) { if(tab!=null) builder.append(tab); builder.append("keepPrdDt(보존기한일시):").append(keepPrdDt).append('\n'); }
		if(ownrUid!=null) { if(tab!=null) builder.append(tab); builder.append("ownrUid(소유자UID):").append(ownrUid).append('\n'); }
		if(docKeepPrdCd!=null) { if(tab!=null) builder.append(tab); builder.append("docKeepPrdCd(문서보존기간코드):").append(docKeepPrdCd).append('\n'); }
		if(seculCd!=null) { if(tab!=null) builder.append(tab); builder.append("seculCd(보안등급코드):").append(seculCd).append('\n'); }
		if(cmplDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDt(완료일시):").append(cmplDt).append('\n'); }
		if(refTyp!=null) { if(tab!=null) builder.append(tab); builder.append("refTyp(참조구분):").append(refTyp).append('\n'); }
		if(refId!=null) { if(tab!=null) builder.append(tab); builder.append("refId(참조ID):").append(refId).append('\n'); }
		if(refUrl!=null) { if(tab!=null) builder.append(tab); builder.append("refUrl(참조URL):").append(refUrl).append('\n'); }
		if(orgnYn!=null) { if(tab!=null) builder.append(tab); builder.append("orgnYn(원본여부):").append(orgnYn).append('\n'); }
		super.toString(builder, tab);
	}

}
