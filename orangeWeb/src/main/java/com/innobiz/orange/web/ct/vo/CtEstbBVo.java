package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

public class CtEstbBVo extends CommonVoImpl{

	/** serialVersionUID */
	private static final long serialVersionUID = -1707263069186793889L;

	/** 회사 ID */
	
	private String compId;
	
	/** 커뮤니티 ID */
	
	private String ctId;
	
	
	/** 마스터 UID */
	
	private String mastUid;
	
	
	/** 커뮤니티 제목 리소스 ID */
	
	private String ctSubjResc;


	/** 커뮤니티 소개 */
	
	private String ctItro;
	
	/** 커뮤니티 키워드 */
	
	private String ctKwd;
	
	/** 관리대상 여부 */
	
	private String mngTgtYn;

	/** 가입방법 */
	
	private String joinMet;

	/** 기본권한 */
	
	private String dftAuth;
	

	/** 외부 공개 여부 */
	
	private String extnOpenYn;
	
	/** 첨부 제한 용량 */
	
	private Integer attLimSize;
	
	/** 첨부 여부 */
	
	private String attYn;
	
	/** 회원수 */
	
	private Integer mbshCnt;
	
	/** 커뮤니티 상태 */
	
	private String ctStat;
	
	/** 커뮤니티 승인일시 */
	
	private String ctApvdDt;
	

	/** 반려 의견 내용 */
	
	private String rjtOpinCont;
	
	/** 커뮤니티 활동 상태 */
	
	private String ctActStat;

	
	/** 커뮤니티 종료 내용 */
	
	private String ctEndCont;
	
	/** 추천여부 */
	
	private String recmdYn;
	
	
	/** 추천일자 */
	
	private String recmdDt;

	/** 커뮤니티 평가코드 */
	
	private String ctEvalCd;
	
	/** 등록일시 */
	
	private String regDt;
	
	/** 수정일시 */
	
	private String modDt;

	/** 등록자 UID */
	
	private String regrUid;
	
	/** 수정자 UID */
	
	private String modrUid;
	
	/** 수정여부 */
	private String modifyYn;
	
	

	// 추가 컬럼
	/** 커뮤니티 기능 UID (메뉴ID) */
	private String ctFncUid;
	
	/** 로그인 사용자 UID */
	private String logUserUid;
	
	/** 마스터 명 */
	private String mastNm;
	
	/** 구분 */
	private String signal;
	
	/** 로그인 사용자 가입상태 */
	private String logUserJoinStat;
	
	/** 카테고리 아이디 */
	private String catId;
	
	/** 커뮤니티 명 */
	private String ctNm;
	
	/** 다국어처리 언어 */
	private String langTyp;
	
	/** 커뮤니티 상태에 따른 리스트 */
	private List<String> ctSearchList;
	
	/** 카테고리 분류명*/
	private String catNm;
	
	/** 카테고리  폴더명*/
	private String catPnm;
	
	/** 사용자 보안 등급 코드 */
	private String logUserSeculCd;
	
	/** 커뮤니티 활동 상태에 따른 리스트*/
	private List<String> ctActStatList;
	
	/** 커뮤니티 가입 상태에 따른 리스트*/
	private List<String> joinStatList;
	
	/** 총 방문자 수 */
	private Integer ctTotalVisitCnt;
	
	/** 총 게시물 수 */
	private Integer ctTotalBullCnt;
	
	/** 기간 시작일시 */
	private String termStartDt;
	
	/** 기간 종료일시 */
	private String termEndDt;
	
	/** 기간 내 방문자 수 */
	private Integer ctTermVisitCnt;
	
	/** 기간 내 게시물 수 */
	private Integer ctTermBullCnt;
	
	/** 기간 내 회원가입자 수 */
	private Integer ctTermMbshCnt;
	
	/** 게시판 실제 활동인원수 */
	private Integer bullRealActUserCnt;
	
	/** 회원수에 따른 점수 */
	private Integer mbshCntScore;
	
	/** 게시물수에 따른 점수 */
	private Integer bullCntScore;
	
	/** 게시판 활동 인원수에 따른 점수*/
	private Integer bullActUserCntScore;
	
	/** 평가 점수 */
	private Integer evalScore;
	
	/** 비공개 폴더 리스트 */
	private List<String> closedFldList;
	
	/** 폐쇄 신청 유무*/
	private String clsReqsYn;
	
	/** 폐쇄 커뮤니티 포함 검색flag */
	private String schClose;
	
	/** 전사여부 */
	private String allCompYn;
	
	public String getSchClose() {
		return schClose;
	}

	public void setSchClose(String schClose) {
		this.schClose = schClose;
	}

	public String getClsReqsYn() {
		return clsReqsYn;
	}

	public void setClsReqsYn(String clsReqsYn) {
		this.clsReqsYn = clsReqsYn;
	}

	public List<String> getClosedFldList() {
		return closedFldList;
	}

	public void setClosedFldList(List<String> closedFldList) {
		this.closedFldList = closedFldList;
	
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
	
	/** 순위 */
	private Integer ranking;
	
	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	
	public Integer getMbshCntScore() {
		return mbshCntScore;
	}

	public void setMbshCntScore(Integer mbshCntScore) {
		this.mbshCntScore = mbshCntScore;
	}

	public Integer getBullCntScore() {
		return bullCntScore;
	}

	public void setBullCntScore(Integer bullCntScore) {
		this.bullCntScore = bullCntScore;
	}

	public Integer getBullActUserCntScore() {
		return bullActUserCntScore;
	}

	public void setBullActUserCntScore(Integer bullActUserCntScore) {
		this.bullActUserCntScore = bullActUserCntScore;
	}

	public Integer getEvalScore() {
		return evalScore;
	}

	public void setEvalScore(Integer evalScore) {
		this.evalScore = evalScore;
	}

	public Integer getBullRealActUserCnt() {
		return bullRealActUserCnt;
	}

	public void setBullRealActUserCnt(Integer bullRealActUserCnt) {
		this.bullRealActUserCnt = bullRealActUserCnt;
	}

	public Integer getCtTermMbshCnt() {
		return ctTermMbshCnt;
	}

	public void setCtTermMbshCnt(Integer ctTermMbshCnt) {
		this.ctTermMbshCnt = ctTermMbshCnt;
	}

	public String getTermStartDt() {
		return termStartDt;
	}

	public void setTermStartDt(String termStartDt) {
		if(termStartDt != null && termStartDt.endsWith(".0") && termStartDt.length()>2)
			 termStartDt=termStartDt.substring(0, termStartDt.length()-2);
		this.termStartDt = termStartDt;
	}

	public String getTermEndDt() {
		return termEndDt;
	}

	public void setTermEndDt(String termEndDt) {
		if(termEndDt != null && termEndDt.endsWith(".0") && termEndDt.length()>2)
			 termEndDt=termEndDt.substring(0, termEndDt.length()-2);
		this.termEndDt = termEndDt;
	}

	public Integer getCtTermVisitCnt() {
		return ctTermVisitCnt;
	}

	public void setCtTermVisitCnt(Integer ctTermVisitCnt) {
		this.ctTermVisitCnt = ctTermVisitCnt;
	}

	public Integer getCtTermBullCnt() {
		return ctTermBullCnt;
	}

	public void setCtTermBullCnt(Integer ctTermBullCnt) {
		this.ctTermBullCnt = ctTermBullCnt;
	}

	public Integer getCtTotalVisitCnt() {
		return ctTotalVisitCnt;
	}

	public void setCtTotalVisitCnt(Integer ctTotalVisitCnt) {
		this.ctTotalVisitCnt = ctTotalVisitCnt;
	}

	public Integer getCtTotalBullCnt() {
		return ctTotalBullCnt;
	}

	public void setCtTotalBullCnt(Integer ctTotalBullCnt) {
		this.ctTotalBullCnt = ctTotalBullCnt;
	}

	public List<String> getCtActStatList() {
		return ctActStatList;
	}

	public void setCtActStatList(List<String> ctActStatList) {
		this.ctActStatList = ctActStatList;
	}

	public String getCatId() {
		return catId;
	}
	
	public void setCatId(String catId) {
		this.catId = catId;
	}
	
	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}
	
	public String getLogUserSeculCd() {
		return logUserSeculCd;
	}

	public void setLogUserSeculCd(String logUserSeculCd) {
		this.logUserSeculCd = logUserSeculCd;
	}

	public String getCatPnm() {
		return catPnm;
	}

	public void setCatPnm(String catPnm) {
		this.catPnm = catPnm;
	}

	public String getCatNm() {
		return catNm;
	}

	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}

	public String getModifyYn() {
		return modifyYn;
	}

	public void setModifyYn(String modifyYn) {
		this.modifyYn = modifyYn;
	}

	public List<String> getCtSearchList() {
		return ctSearchList;
	}

	public void setCtSearchList(List<String> ctSearchList) {
		this.ctSearchList = ctSearchList;
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

	public String getLogUserJoinStat() {
		return logUserJoinStat;
	}

	public void setLogUserJoinStat(String logUserJoinStat) {
		this.logUserJoinStat = logUserJoinStat;
	}

	public String getSignal() {
		return signal;
	}

	public void setSignal(String signal) {
		this.signal = signal;
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

	public String getMastUid() {
		return mastUid;
	}

	public void setMastUid(String mastUid) {
		this.mastUid = mastUid;
	}

	public String getCtSubjResc() {
		return ctSubjResc;
	}

	public void setCtSubjResc(String ctSubjResc) {
		this.ctSubjResc = ctSubjResc;
	}

	public String getCtItro() {
		return ctItro;
	}

	public void setCtItro(String ctItro) {
		this.ctItro = ctItro;
	}

	public String getCtKwd() {
		return ctKwd;
	}

	public void setCtKwd(String ctKwd) {
		this.ctKwd = ctKwd;
	}

	public String getMngTgtYn() {
		return mngTgtYn;
	}

	public void setMngTgtYn(String mngTgtYn) {
		this.mngTgtYn = mngTgtYn;
	}

	public String getJoinMet() {
		return joinMet;
	}

	public void setJoinMet(String joinMet) {
		this.joinMet = joinMet;
	}

	public String getDftAuth() {
		return dftAuth;
	}

	public void setDftAuth(String dftAuth) {
		this.dftAuth = dftAuth;
	}

	public String getExtnOpenYn() {
		return extnOpenYn;
	}

	public void setExtnOpenYn(String extnOpenYn) {
		this.extnOpenYn = extnOpenYn;
	}

	public Integer getAttLimSize() {
		return attLimSize;
	}

	public void setAttLimSize(Integer attLimSize) {
		this.attLimSize = attLimSize;
	}

	public String getAttYn() {
		return attYn;
	}

	public void setAttYn(String attYn) {
		this.attYn = attYn;
	}

	public Integer getMbshCnt() {
		return mbshCnt;
	}

	public void setMbshCnt(Integer mbshCnt) {
		this.mbshCnt = mbshCnt;
	}

	public String getCtStat() {
		return ctStat;
	}

	public void setCtStat(String ctStat) {
		this.ctStat = ctStat;
	}

	public String getCtApvdDt() {
		return ctApvdDt;
	}

	public void setCtApvdDt(String ctApvdDt) {
		if(ctApvdDt != null && ctApvdDt.endsWith(".0") && ctApvdDt.length()>2)
			 ctApvdDt=ctApvdDt.substring(0, ctApvdDt.length()-2);
		this.ctApvdDt = ctApvdDt;
	}

	public String getRjtOpinCont() {
		return rjtOpinCont;
	}

	public void setRjtOpinCont(String rjtOpinCont) {
		this.rjtOpinCont = rjtOpinCont;
	}

	public String getCtActStat() {
		return ctActStat;
	}

	public void setCtActStat(String ctActStat) {
		this.ctActStat = ctActStat;
	}

	public String getCtEndCont() {
		return ctEndCont;
	}

	public void setCtEndCont(String ctEndCont) {
		this.ctEndCont = ctEndCont;
	}

	public String getRecmdYn() {
		return recmdYn;
	}

	public void setRecmdYn(String recmdYn) {
		this.recmdYn = recmdYn;
	}

	public String getRecmdDt() {
		return recmdDt;
	}

	public void setRecmdDt(String recmdDt) {
		if(recmdDt != null && recmdDt.endsWith(".0") && recmdDt.length()>2)
			 recmdDt=recmdDt.substring(0, recmdDt.length()-2);
		this.recmdDt = recmdDt;
	}

	public String getCtEvalCd() {
		return ctEvalCd;
	}

	public void setCtEvalCd(String ctEvalCd) {
		this.ctEvalCd = ctEvalCd;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}

	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	public String getLogUserUid() {
		return logUserUid;
	}

	public void setLogUserUid(String logUserUid) {
		this.logUserUid = logUserUid;
	}

	public String getMastNm() {
		return mastNm;
	}

	public void setMastNm(String mastNm) {
		this.mastNm = mastNm;
	}

	public synchronized List<String> getJoinStatList() {
		return joinStatList;
	}

	public synchronized void setJoinStatList(List<String> joinStatList) {
		this.joinStatList = joinStatList;
	}
	
	/** 전사여부 */
	public String getAllCompYn() {
		return allCompYn;
	}

	public void setAllCompYn(String allCompYn) {
		this.allCompYn = allCompYn;
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
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사 ID):").append(compId).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(mastUid!=null) { if(tab!=null) builder.append(tab); builder.append("mastUid(마스터 UID):").append(mastUid).append('\n'); }
		if(ctSubjResc!=null) { if(tab!=null) builder.append(tab); builder.append("ctSubjResc(커뮤니티 제목 리소스 ID):").append(ctSubjResc).append('\n'); }
		if(ctItro!=null) { if(tab!=null) builder.append(tab); builder.append("ctItro(커뮤니티 소개):").append(ctItro).append('\n'); }
		if(ctKwd!=null) { if(tab!=null) builder.append(tab); builder.append("ctKwd(커뮤니티 키워드):").append(ctKwd).append('\n'); }
		if(mngTgtYn!=null) { if(tab!=null) builder.append(tab); builder.append("mngTgtYn(관리대상 여부):").append(mngTgtYn).append('\n'); }
		if(joinMet!=null) { if(tab!=null) builder.append(tab); builder.append("joinMet(가입방법):").append(joinMet).append('\n'); }
		if(dftAuth!=null) { if(tab!=null) builder.append(tab); builder.append("dftAuth(기본권한):").append(dftAuth).append('\n'); }
		if(extnOpenYn!=null) { if(tab!=null) builder.append(tab); builder.append("extnOpenYn(외부 공개 여부):").append(extnOpenYn).append('\n'); }
		if(attLimSize!=null) { if(tab!=null) builder.append(tab); builder.append("attLimSize(첨부 제한 용량):").append(attLimSize).append('\n'); }
		if(attYn!=null) { if(tab!=null) builder.append(tab); builder.append("attYn(첨부 여부):").append(attYn).append('\n'); }
		if(mbshCnt!=null) { if(tab!=null) builder.append(tab); builder.append("mbshCnt(회원수):").append(mbshCnt).append('\n'); }
		if(ctStat!=null) { if(tab!=null) builder.append(tab); builder.append("ctStat(커뮤니티 상태):").append(ctStat).append('\n'); }
		if(ctApvdDt!=null) { if(tab!=null) builder.append(tab); builder.append("ctApvdDt(커뮤니티 승인일시):").append(ctApvdDt).append('\n'); }
		if(rjtOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("rjtOpinCont(반려 의견 내용):").append(rjtOpinCont).append('\n'); }
		if(ctActStat!=null) { if(tab!=null) builder.append(tab); builder.append("ctActStat(커뮤니티 활동 상태):").append(ctActStat).append('\n'); }
		if(ctEndCont!=null) { if(tab!=null) builder.append(tab); builder.append("ctEndCont(커뮤니티 종료 내용):").append(ctEndCont).append('\n'); }
		if(recmdYn!=null) { if(tab!=null) builder.append(tab); builder.append("recmdYn(추천여부):").append(recmdYn).append('\n'); }
		if(recmdDt!=null) { if(tab!=null) builder.append(tab); builder.append("recmdDt(추천일자):").append(recmdDt).append('\n'); }
		if(ctEvalCd!=null) { if(tab!=null) builder.append(tab); builder.append("ctEvalCd(커뮤니티 평가코드):").append(ctEvalCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(modifyYn!=null) { if(tab!=null) builder.append(tab); builder.append("modifyYn(수정여부):").append(modifyYn).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID (메뉴ID)):").append(ctFncUid).append('\n'); }
		if(logUserUid!=null) { if(tab!=null) builder.append(tab); builder.append("logUserUid(로그인 사용자 UID):").append(logUserUid).append('\n'); }
		if(mastNm!=null) { if(tab!=null) builder.append(tab); builder.append("mastNm(마스터 명):").append(mastNm).append('\n'); }
		if(signal!=null) { if(tab!=null) builder.append(tab); builder.append("signal(구분):").append(signal).append('\n'); }
		if(logUserJoinStat!=null) { if(tab!=null) builder.append(tab); builder.append("logUserJoinStat(로그인 사용자 가입상태):").append(logUserJoinStat).append('\n'); }
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(카테고리 아이디):").append(catId).append('\n'); }
		if(ctNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctNm(커뮤니티 명):").append(ctNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(다국어처리 언어):").append(langTyp).append('\n'); }
		if(ctSearchList!=null) { if(tab!=null) builder.append(tab); builder.append("ctSearchList(커뮤니티 상태에 따른 리스트):"); appendStringListTo(builder, ctSearchList,tab); builder.append('\n'); }
		if(catNm!=null) { if(tab!=null) builder.append(tab); builder.append("catNm(카테고리 분류명):").append(catNm).append('\n'); }
		if(catPnm!=null) { if(tab!=null) builder.append(tab); builder.append("catPnm(카테고리  폴더명):").append(catPnm).append('\n'); }
		if(logUserSeculCd!=null) { if(tab!=null) builder.append(tab); builder.append("logUserSeculCd(사용자 보안 등급 코드):").append(logUserSeculCd).append('\n'); }
		if(ctActStatList!=null) { if(tab!=null) builder.append(tab); builder.append("ctActStatList(커뮤니티 활동 상태에 따른 리스트):"); appendStringListTo(builder, ctActStatList,tab); builder.append('\n'); }
		if(joinStatList!=null) { if(tab!=null) builder.append(tab); builder.append("joinStatList(커뮤니티 가입 상태에 따른 리스트):"); appendStringListTo(builder, joinStatList,tab); builder.append('\n'); }
		if(ctTotalVisitCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ctTotalVisitCnt(총 방문자 수):").append(ctTotalVisitCnt).append('\n'); }
		if(ctTotalBullCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ctTotalBullCnt(총 게시물 수):").append(ctTotalBullCnt).append('\n'); }
		if(termStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("termStartDt(기간 시작일시):").append(termStartDt).append('\n'); }
		if(termEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("termEndDt(기간 종료일시):").append(termEndDt).append('\n'); }
		if(ctTermVisitCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ctTermVisitCnt(기간 내 방문자 수):").append(ctTermVisitCnt).append('\n'); }
		if(ctTermBullCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ctTermBullCnt(기간 내 게시물 수):").append(ctTermBullCnt).append('\n'); }
		if(ctTermMbshCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ctTermMbshCnt(기간 내 회원가입자 수):").append(ctTermMbshCnt).append('\n'); }
		if(bullRealActUserCnt!=null) { if(tab!=null) builder.append(tab); builder.append("bullRealActUserCnt(게시판 실제 활동인원수):").append(bullRealActUserCnt).append('\n'); }
		if(mbshCntScore!=null) { if(tab!=null) builder.append(tab); builder.append("mbshCntScore(회원수에 따른 점수):").append(mbshCntScore).append('\n'); }
		if(bullCntScore!=null) { if(tab!=null) builder.append(tab); builder.append("bullCntScore(게시물수에 따른 점수):").append(bullCntScore).append('\n'); }
		if(bullActUserCntScore!=null) { if(tab!=null) builder.append(tab); builder.append("bullActUserCntScore(게시판 활동 인원수에 따른 점수):").append(bullActUserCntScore).append('\n'); }
		if(evalScore!=null) { if(tab!=null) builder.append(tab); builder.append("evalScore(평가 점수):").append(evalScore).append('\n'); }
		if(closedFldList!=null) { if(tab!=null) builder.append(tab); builder.append("closedFldList(비공개 폴더 리스트):"); appendStringListTo(builder, closedFldList,tab); builder.append('\n'); }
		if(clsReqsYn!=null) { if(tab!=null) builder.append(tab); builder.append("clsReqsYn(폐쇄 신청 유무):").append(clsReqsYn).append('\n'); }
		if(schClose!=null) { if(tab!=null) builder.append(tab); builder.append("schClose(폐쇄 커뮤니티 포함 검색flag):").append(schClose).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일자):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일자):").append(endDt).append('\n'); }
		if(ranking!=null) { if(tab!=null) builder.append(tab); builder.append("ranking(순위):").append(ranking).append('\n'); }
		super.toString(builder, tab);
	}

}