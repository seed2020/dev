package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_REC_MAST_B [커뮤니티 자료실 마스터 기본] */
public class CtRecMastBVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1133956529872859585L;

	/** 게시물ID */
	private Integer bullId;

	/** 회사ID */
	private String compId;

	/** 커뮤니티 ID */
	private String ctId;
	
	/** 커뮤니티 기능ID */
	private String ctFncId;

	/** 부모게시물ID */
	private Integer bullPid;
	
	/** 커뮤니티 기능 순서 */
	private Integer ctFncOrdr;

	/** 답변그룹ID */
	private Integer replyGrpId;

	/** 답변순서 */
	private Integer replyOrdr;

	/** 답변단계 */
	private Integer replyDpth;

	/** 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시) */
	private String bullStatCd;

	/** 게시물예약여부 */
	private String bullRezvYn;

	/** 대상부서지정여부 */
	private String tgtDeptYn;

	/** 대상사용자지정여부 */
	private String tgtUserYn;

	/** 제목 */
	private String subj;

	/** 게시예약일시 */
	private String bullRezvDt;

	/** 게시완료일시 */
	private String bullExprDt;

	/** 본문 */
	private String cont;

	/** 조회수 */
	private Integer readCnt;

	/** 찬성수 */
	private Integer prosCnt;

	/** 반대수 */
	private Integer consCnt;

	/** 추천수 */
	private Integer recmdCnt;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;
	
	/** 첨부여부*/
	private String attYn;
	
	/** 기능 위치 단계 */
	private String ctFncLocStep;
	 
	/** 커뮤니티 기능 UID */
	private String ctFncUid;
	 
	/** 커뮤니티 기능 부모ID */
	private String ctFncPid;
	
	// 추가 컬럼
	
	/** 등록자명  */
	private String regrNm;
	
	/** 수정자명  */
	private String modrNm;
	
	/** 커뮤니티명 */
	private String ctNm;
	
	/** 사용자 언어타입 */
	private String langTyp;
	
	/** 로그인 사용자 UID */
	private String logUserUid;
	
	/** 새글여부 */
	private String newYn;
	
	/** 커뮤니티홈 포틀릿 여부 */
	private String ctHomePtYn; 
	
	/** 한줄답변수 */
	private Integer cmtCnt;
	
	/** 파일검색 */
	private String fileSearch;
	
	public String getFileSearch() {
		return fileSearch;
	}

	public void setFileSearch(String fileSearch) {
		this.fileSearch = fileSearch;
	}
	
	/** 기능명 */
	private String fncNm;
	
	public String getFncNm() {
		return fncNm;
	}

	public void setFncNm(String fncNm) {
		this.fncNm = fncNm;
	}
	
	/** 시작일자 */
	private String strtDt;
	
	public String getStrtDt() {
		return strtDt;
	}

	public void setStrtDt(String strtDt) {
		if(strtDt != null && strtDt.endsWith(".0") && strtDt.length()>2)
			 strtDt=strtDt.substring(0, strtDt.length()-2);
		this.strtDt = strtDt;
	}


	/** 종료일자 */
	private String endDt;
	
	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		if(endDt != null && endDt.endsWith(".0") && endDt.length()>2)
			 endDt=endDt.substring(0, endDt.length()-2);
		this.endDt = endDt;
	}
	
	public Integer getCmtCnt() {
		return cmtCnt;
	}

	public void setCmtCnt(Integer cmtCnt) {
		this.cmtCnt = cmtCnt;
	}
	
	public String getCtHomePtYn() {
		return ctHomePtYn;
	}

	public void setCtHomePtYn(String ctHomePtYn) {
		this.ctHomePtYn = ctHomePtYn;
	}

	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	public String getModrNm() {
		return modrNm;
	}

	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}

	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}

	public String getLogUserUid() {
		return logUserUid;
	}

	public void setLogUserUid(String logUserUid) {
		this.logUserUid = logUserUid;
	}

	public String getNewYn() {
		return newYn;
	}

	public void setNewYn(String newYn) {
		this.newYn = newYn;
	}

	public String getCtFncLocStep() {
		return ctFncLocStep;
	}

	public void setCtFncLocStep(String ctFncLocStep) {
		this.ctFncLocStep = ctFncLocStep;
	}

	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}

	public String getCtFncPid() {
		return ctFncPid;
	}

	public void setCtFncPid(String ctFncPid) {
		this.ctFncPid = ctFncPid;
	}

	public Integer getBullId() {
		return bullId;
	}

	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}

	public String getCtFncId() {
		return ctFncId;
	}

	public void setCtFncId(String ctFncId) {
		this.ctFncId = ctFncId;
	}

	public Integer getBullPid() {
		return bullPid;
	}

	public void setBullPid(Integer bullPid) {
		this.bullPid = bullPid;
	}

	public Integer getCtFncOrdr() {
		return ctFncOrdr;
	}

	public void setCtFncOrdr(Integer ctFncOrdr) {
		this.ctFncOrdr = ctFncOrdr;
	}

	public Integer getReplyGrpId() {
		return replyGrpId;
	}

	public void setReplyGrpId(Integer replyGrpId) {
		this.replyGrpId = replyGrpId;
	}

	public Integer getReplyOrdr() {
		return replyOrdr;
	}

	public void setReplyOrdr(Integer replyOrdr) {
		this.replyOrdr = replyOrdr;
	}

	public Integer getReplyDpth() {
		return replyDpth;
	}

	public void setReplyDpth(Integer replyDpth) {
		this.replyDpth = replyDpth;
	}

	public String getBullStatCd() {
		return bullStatCd;
	}

	public void setBullStatCd(String bullStatCd) {
		this.bullStatCd = bullStatCd;
	}

	public String getBullRezvYn() {
		return bullRezvYn;
	}

	public void setBullRezvYn(String bullRezvYn) {
		this.bullRezvYn = bullRezvYn;
	}

	public String getTgtDeptYn() {
		return tgtDeptYn;
	}

	public void setTgtDeptYn(String tgtDeptYn) {
		this.tgtDeptYn = tgtDeptYn;
	}

	public String getTgtUserYn() {
		return tgtUserYn;
	}

	public void setTgtUserYn(String tgtUserYn) {
		this.tgtUserYn = tgtUserYn;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getBullRezvDt() {
		return bullRezvDt;
	}

	public void setBullRezvDt(String bullRezvDt) {
		if(bullRezvDt != null && bullRezvDt.endsWith(".0") && bullRezvDt.length()>2)
			 bullRezvDt=bullRezvDt.substring(0, bullRezvDt.length()-2);
		this.bullRezvDt = bullRezvDt;
	}

	public String getBullExprDt() {
		return bullExprDt;
	}

	public void setBullExprDt(String bullExprDt) {
		if(bullExprDt != null && bullExprDt.endsWith(".0") && bullExprDt.length()>2)
			 bullExprDt=bullExprDt.substring(0, bullExprDt.length()-2);
		this.bullExprDt = bullExprDt;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public Integer getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}

	public Integer getProsCnt() {
		return prosCnt;
	}

	public void setProsCnt(Integer prosCnt) {
		this.prosCnt = prosCnt;
	}

	public Integer getConsCnt() {
		return consCnt;
	}

	public void setConsCnt(Integer consCnt) {
		this.consCnt = consCnt;
	}

	public Integer getRecmdCnt() {
		return recmdCnt;
	}

	public void setRecmdCnt(Integer recmdCnt) {
		this.recmdCnt = recmdCnt;
	}

	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}

	public String getAttYn() {
		return attYn;
	}

	public void setAttYn(String attYn) {
		this.attYn = attYn;
	}

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(bullId!=null) { if(tab!=null) builder.append(tab); builder.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(ctFncId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncId(커뮤니티 기능ID):").append(ctFncId).append('\n'); }
		if(bullPid!=null) { if(tab!=null) builder.append(tab); builder.append("bullPid(부모게시물ID):").append(bullPid).append('\n'); }
		if(ctFncOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncOrdr(커뮤니티 기능 순서):").append(ctFncOrdr).append('\n'); }
		if(replyGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("replyGrpId(답변그룹ID):").append(replyGrpId).append('\n'); }
		if(replyOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("replyOrdr(답변순서):").append(replyOrdr).append('\n'); }
		if(replyDpth!=null) { if(tab!=null) builder.append(tab); builder.append("replyDpth(답변단계):").append(replyDpth).append('\n'); }
		if(bullStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("bullStatCd(게시물상태코드):").append(bullStatCd).append('\n'); }
		if(bullRezvYn!=null) { if(tab!=null) builder.append(tab); builder.append("bullRezvYn(게시물예약여부):").append(bullRezvYn).append('\n'); }
		if(tgtDeptYn!=null) { if(tab!=null) builder.append(tab); builder.append("tgtDeptYn(대상부서지정여부):").append(tgtDeptYn).append('\n'); }
		if(tgtUserYn!=null) { if(tab!=null) builder.append(tab); builder.append("tgtUserYn(대상사용자지정여부):").append(tgtUserYn).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(bullRezvDt!=null) { if(tab!=null) builder.append(tab); builder.append("bullRezvDt(게시예약일시):").append(bullRezvDt).append('\n'); }
		if(bullExprDt!=null) { if(tab!=null) builder.append(tab); builder.append("bullExprDt(게시완료일시):").append(bullExprDt).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(본문):").append(cont).append('\n'); }
		if(readCnt!=null) { if(tab!=null) builder.append(tab); builder.append("readCnt(조회수):").append(readCnt).append('\n'); }
		if(prosCnt!=null) { if(tab!=null) builder.append(tab); builder.append("prosCnt(찬성수):").append(prosCnt).append('\n'); }
		if(consCnt!=null) { if(tab!=null) builder.append(tab); builder.append("consCnt(반대수):").append(consCnt).append('\n'); }
		if(recmdCnt!=null) { if(tab!=null) builder.append(tab); builder.append("recmdCnt(추천수):").append(recmdCnt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(attYn!=null) { if(tab!=null) builder.append(tab); builder.append("attYn(첨부여부):").append(attYn).append('\n'); }
		if(ctFncLocStep!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncLocStep(기능 위치 단계):").append(ctFncLocStep).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		if(ctFncPid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncPid(커뮤니티 기능 부모ID):").append(ctFncPid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		if(ctNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctNm(커뮤니티명):").append(ctNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(사용자 언어타입):").append(langTyp).append('\n'); }
		if(logUserUid!=null) { if(tab!=null) builder.append(tab); builder.append("logUserUid(로그인 사용자 UID):").append(logUserUid).append('\n'); }
		if(newYn!=null) { if(tab!=null) builder.append(tab); builder.append("newYn(새글여부):").append(newYn).append('\n'); }
		if(ctHomePtYn!=null) { if(tab!=null) builder.append(tab); builder.append("ctHomePtYn(커뮤니티홈 포틀릿 여부):").append(ctHomePtYn).append('\n'); }
		if(cmtCnt!=null) { if(tab!=null) builder.append(tab); builder.append("cmtCnt(한줄답변수):").append(cmtCnt).append('\n'); }
		if(fileSearch!=null) { if(tab!=null) builder.append(tab); builder.append("fileSearch(파일검색):").append(fileSearch).append('\n'); }
		if(fncNm!=null) { if(tab!=null) builder.append(tab); builder.append("fncNm(기능명):").append(fncNm).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일자):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일자):").append(endDt).append('\n'); }
		super.toString(builder, tab);
	}


}