package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.wv.vo.WvSurvUseAuthDVo;

/** CT_SURV_B [커뮤니티 설문 기본] */
public class CtSurvBVo extends CommonVoImpl{

	/** serialVersionUID */
	private static final long serialVersionUID = -3491235207017150719L;

	/** 회사ID */
	private String compId;
	
	/** 설문ID */
	private String survId;
	
	/** 커뮤니티 ID */
	private String ctId;
	
	/** 커뮤니티 기능ID */
	private String ctFncId;
	
	/** 커뮤니티 기능 순서 */
	private Integer ctFncOrdr;
	
	/** 설문 제목*/
	private String subj;

	/** 질문 개수 */
	private int quesCnt;

	/** 설문 시작일시 */
	private String survStartDt;
	
	/** 설문 종료일시 */
	private String survEndDt;
	
	/** 포틀릿ID */
	private String pltId;
	
	/** 등록일시 */
	private String regDt;

	/** 수정일시 */
	private String modDt;
	
	/** 등록자 UID */
	private String regrUid;
	
	/** 등록자명 */
	private String regrNm;
	
	/** 수정자 UID */
	private String modrUid;
	
	/** 설문진행상태코드 */
	private String survPrgStatCd;
	
	/** 공개여부 */
	private String openYn;
	
	/** 재설문여부 */
	private String repetSurvYn;
	
	/** 설문취지 */
	private String survItnt;
	
	/** 설문바닥글 */
	private String survFtr;
	
	/** 첨부여부 */
	private String attYn;
	
	/** 관리자반려의견내용 */
	private String admRjtOpinCont;
	
	/** 설문파일ID */
	private String survFileId;

	/** 기능 위치 단계 */
	private String ctFncLocStep;
	 
	/** 커뮤니티 기능 UID */
	private String ctFncUid;
	 
	/** 커뮤니티 기능 부모ID */
	private String ctFncPid;
	
	/** 설문 대상 코드*/
	private String survTgtCd;
	
	public String getSurvTgtCd() {
		return survTgtCd;
	}

	public void setSurvTgtCd(String survTgtCd) {
		this.survTgtCd = survTgtCd;
	}
	
	// 추가 컬럼
	
	/** 로그인 User Uid*/
	private String logUserUid;
	
	public String getLogUserUid() {
		return logUserUid;
	}

	public void setLogUserUid(String logUserUid) {
		this.logUserUid = logUserUid;
	}
	
	/** 로그인 User Detp Id*/
	private String logUserDeptId;
	

	public String getLogUserDeptId() {
		return logUserDeptId;
	}

	public void setLogUserDeptId(String logUserDeptId) {
		this.logUserDeptId = logUserDeptId;
	}

	/** 참여 인원수 */
	private Integer joinUserCnt;


	public Integer getJoinUserCnt() {
		return joinUserCnt;
	}

	public void setJoinUserCnt(Integer joinUserCnt) {
		this.joinUserCnt = joinUserCnt;
	}

	/** 응답 여부 */
	private String replyYn;
	
	public String getReplyYn() {
		return replyYn;
	}

	public void setReplyYn(String replyYn) {
		this.replyYn = replyYn;
	}
	
	/** 설문검색 List*/
	private List<String> survSearchList;
	
	public List<String> getSurvSearchList() {
		return survSearchList;
	}

	public void setSurvSearchList(List<String> survSearchList) {
		this.survSearchList = survSearchList;
	}
	
	/** 설문 권한 List*/
	private List<WvSurvUseAuthDVo> survAuthList;

	public List<WvSurvUseAuthDVo> getSurvAuthList() {
		return survAuthList;
	}

	public void setSurvAuthList(List<WvSurvUseAuthDVo> survAuthList) {
		this.survAuthList = survAuthList;
	}
	
	/** 회사명 */
	private String compNm;
	

	public String getCompNm() {
		return compNm;
	}

	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}
	
	/** 사용자 언어 타입 */
	private String langTyp;
	

	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 커뮤니티  리스트 */
	private List<String> ctIdList;
	
	public List<String> getCtIdList() {
		return ctIdList;
	}

	public void setCtIdList(List<String> ctIdList) {
		this.ctIdList = ctIdList;
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
	
	/** 커뮤니티별 나의권한 */
	private String ctMyAuth;
	
	public String getCtMyAuth() {
		return ctMyAuth;
	}

	public void setCtMyAuth(String ctMyAuth) {
		this.ctMyAuth = ctMyAuth;
	}
	
	/** 커뮤니티명 */
	private String ctNm;
	
	public String getCtNm() {
		return ctNm;
	}

	public void setCtNm(String ctNm) {
		this.ctNm = ctNm;
	}
	
	/** 커뮤니티 통합기능Uid */
	private String ctComFncUid;
	
	public String getCtComFncUid() {
		return ctComFncUid;
	}

	public void setCtComFncUid(String ctComFncUid) {
		this.ctComFncUid = ctComFncUid;
	}
	
	/** 커뮤니티 기능 URL */
	private String ctFncUrl;
	
	public String getCtFncUrl() {
		return ctFncUrl;
	}

	public void setCtFncUrl(String ctFncUrl) {
		this.ctFncUrl = ctFncUrl;
	}
	
	/** 관계테이블명 */
	private String relTblSubj;
	

	public String getRelTblSubj() {
		return relTblSubj;
	}
	
	/** 기능명 */
	private String fncNm;
	
	public String getFncNm() {
		return fncNm;
	}

	public void setFncNm(String fncNm) {
		this.fncNm = fncNm;
	}
	
	/** 마스터UID */
	private String mastUid;
	
	public String getMastUid() {
		return mastUid;
	}

	public void setMastUid(String mastUid) {
		this.mastUid = mastUid;
	}
	
	/** 마스터명 */
	private String mastNm;
	
	public String getMastNm() {
		return mastNm;
	}

	public void setMastNm(String mastNm) {
		this.mastNm = mastNm;
	}
	
	/** 설문대상 마스터 */
	private String survTgtM;
	/** 설문대상 스텝 */
	private String survTgtS;
	/** 설문대상 정회원 */
	private String survTgtR;
	/** 설문대상 준회원 */
	private String survTgtA;
	
	public String getSurvTgtM() {
		return survTgtM;
	}

	public void setSurvTgtM(String survTgtM) {
		this.survTgtM = survTgtM;
	}

	public String getSurvTgtS() {
		return survTgtS;
	}

	public void setSurvTgtS(String survTgtS) {
		this.survTgtS = survTgtS;
	}

	public String getSurvTgtR() {
		return survTgtR;
	}

	public void setSurvTgtR(String survTgtR) {
		this.survTgtR = survTgtR;
	}

	public String getSurvTgtA() {
		return survTgtA;
	}

	public void setSurvTgtA(String survTgtA) {
		this.survTgtA = survTgtA;
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

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getSurvId() {
		return survId;
	}

	public void setSurvId(String survId) {
		this.survId = survId;
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

	public Integer getCtFncOrdr() {
		return ctFncOrdr;
	}

	public void setCtFncOrdr(Integer ctFncOrdr) {
		this.ctFncOrdr = ctFncOrdr;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public int getQuesCnt() {
		return quesCnt;
	}

	public void setQuesCnt(int quesCnt) {
		this.quesCnt = quesCnt;
	}

	public String getSurvStartDt() {
		return survStartDt;
	}

	public void setSurvStartDt(String survStartDt) {
		if(survStartDt != null && survStartDt.endsWith(".0") && survStartDt.length()>2)
			 survStartDt=survStartDt.substring(0, survStartDt.length()-2);
		this.survStartDt = survStartDt;
	}

	public String getSurvEndDt() {
		return survEndDt;
	}

	public void setSurvEndDt(String survEndDt) {
		if(survEndDt != null && survEndDt.endsWith(".0") && survEndDt.length()>2)
			 survEndDt=survEndDt.substring(0, survEndDt.length()-2);
		this.survEndDt = survEndDt;
	}

	public String getPltId() {
		return pltId;
	}

	public void setPltId(String pltId) {
		this.pltId = pltId;
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

	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	public String getSurvPrgStatCd() {
		return survPrgStatCd;
	}

	public void setSurvPrgStatCd(String survPrgStatCd) {
		this.survPrgStatCd = survPrgStatCd;
	}

	public String getOpenYn() {
		return openYn;
	}

	public void setOpenYn(String openYn) {
		this.openYn = openYn;
	}

	public String getRepetSurvYn() {
		return repetSurvYn;
	}

	public void setRepetSurvYn(String repetSurvYn) {
		this.repetSurvYn = repetSurvYn;
	}

	public String getSurvItnt() {
		return survItnt;
	}

	public void setSurvItnt(String survItnt) {
		this.survItnt = survItnt;
	}

	public String getSurvFtr() {
		return survFtr;
	}

	public void setSurvFtr(String survFtr) {
		this.survFtr = survFtr;
	}

	public String getAttYn() {
		return attYn;
	}

	public void setAttYn(String attYn) {
		this.attYn = attYn;
	}

	public String getAdmRjtOpinCont() {
		return admRjtOpinCont;
	}

	public void setAdmRjtOpinCont(String admRjtOpinCont) {
		this.admRjtOpinCont = admRjtOpinCont;
	}

	public String getSurvFileId() {
		return survFileId;
	}

	public void setSurvFileId(String survFileId) {
		this.survFileId = survFileId;
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
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(ctFncId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncId(커뮤니티 기능ID):").append(ctFncId).append('\n'); }
		if(ctFncOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncOrdr(커뮤니티 기능 순서):").append(ctFncOrdr).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(설문 제목):").append(subj).append('\n'); }
		if(quesCnt!=0) { if(tab!=null) builder.append(tab); builder.append("quesCnt(질문 개수):").append(quesCnt).append('\n'); }
		if(survStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("survStartDt(설문 시작일시):").append(survStartDt).append('\n'); }
		if(survEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("survEndDt(설문 종료일시):").append(survEndDt).append('\n'); }
		if(pltId!=null) { if(tab!=null) builder.append(tab); builder.append("pltId(포틀릿ID):").append(pltId).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(survPrgStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("survPrgStatCd(설문진행상태코드):").append(survPrgStatCd).append('\n'); }
		if(openYn!=null) { if(tab!=null) builder.append(tab); builder.append("openYn(공개여부):").append(openYn).append('\n'); }
		if(repetSurvYn!=null) { if(tab!=null) builder.append(tab); builder.append("repetSurvYn(재설문여부):").append(repetSurvYn).append('\n'); }
		if(survItnt!=null) { if(tab!=null) builder.append(tab); builder.append("survItnt(설문취지):").append(survItnt).append('\n'); }
		if(survFtr!=null) { if(tab!=null) builder.append(tab); builder.append("survFtr(설문바닥글):").append(survFtr).append('\n'); }
		if(attYn!=null) { if(tab!=null) builder.append(tab); builder.append("attYn(첨부여부):").append(attYn).append('\n'); }
		if(admRjtOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("admRjtOpinCont(관리자반려의견내용):").append(admRjtOpinCont).append('\n'); }
		if(survFileId!=null) { if(tab!=null) builder.append(tab); builder.append("survFileId(설문파일ID):").append(survFileId).append('\n'); }
		if(ctFncLocStep!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncLocStep(기능 위치 단계):").append(ctFncLocStep).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		if(ctFncPid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncPid(커뮤니티 기능 부모ID):").append(ctFncPid).append('\n'); }
		if(survTgtCd!=null) { if(tab!=null) builder.append(tab); builder.append("survTgtCd(설문 대상 코드):").append(survTgtCd).append('\n'); }
		if(logUserUid!=null) { if(tab!=null) builder.append(tab); builder.append("logUserUid(로그인 User Uid):").append(logUserUid).append('\n'); }
		if(logUserDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("logUserDeptId(로그인 User Detp Id):").append(logUserDeptId).append('\n'); }
		if(joinUserCnt!=null) { if(tab!=null) builder.append(tab); builder.append("joinUserCnt(참여 인원수):").append(joinUserCnt).append('\n'); }
		if(replyYn!=null) { if(tab!=null) builder.append(tab); builder.append("replyYn(응답 여부):").append(replyYn).append('\n'); }
		if(survSearchList!=null) { if(tab!=null) builder.append(tab); builder.append("survSearchList(설문검색 List):"); appendStringListTo(builder, survSearchList,tab); builder.append('\n'); }
		if(survAuthList!=null) { if(tab!=null) builder.append(tab); builder.append("survAuthList(설문 권한 List):"); appendVoListTo(builder, survAuthList,tab); builder.append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(사용자 언어 타입):").append(langTyp).append('\n'); }
		if(ctIdList!=null) { if(tab!=null) builder.append(tab); builder.append("ctIdList(커뮤니티  리스트):"); appendStringListTo(builder, ctIdList,tab); builder.append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일자):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일자):").append(endDt).append('\n'); }
		if(ctMyAuth!=null) { if(tab!=null) builder.append(tab); builder.append("ctMyAuth(커뮤니티별 나의권한):").append(ctMyAuth).append('\n'); }
		if(ctNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctNm(커뮤니티명):").append(ctNm).append('\n'); }
		if(ctComFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctComFncUid(커뮤니티 통합기능Uid):").append(ctComFncUid).append('\n'); }
		if(ctFncUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUrl(커뮤니티 기능 URL):").append(ctFncUrl).append('\n'); }
		if(relTblSubj!=null) { if(tab!=null) builder.append(tab); builder.append("relTblSubj(관계테이블명):").append(relTblSubj).append('\n'); }
		if(fncNm!=null) { if(tab!=null) builder.append(tab); builder.append("fncNm(기능명):").append(fncNm).append('\n'); }
		if(mastUid!=null) { if(tab!=null) builder.append(tab); builder.append("mastUid(마스터UID):").append(mastUid).append('\n'); }
		if(mastNm!=null) { if(tab!=null) builder.append(tab); builder.append("mastNm(마스터명):").append(mastNm).append('\n'); }
		if(survTgtM!=null) { if(tab!=null) builder.append(tab); builder.append("survTgtM(설문대상 마스터):").append(survTgtM).append('\n'); }
		if(survTgtS!=null) { if(tab!=null) builder.append(tab); builder.append("survTgtS(설문대상 스텝):").append(survTgtS).append('\n'); }
		if(survTgtR!=null) { if(tab!=null) builder.append(tab); builder.append("survTgtR(설문대상 정회원):").append(survTgtR).append('\n'); }
		if(survTgtA!=null) { if(tab!=null) builder.append(tab); builder.append("survTgtA(설문대상 준회원):").append(survTgtA).append('\n'); }
		super.toString(builder, tab);
	}

}