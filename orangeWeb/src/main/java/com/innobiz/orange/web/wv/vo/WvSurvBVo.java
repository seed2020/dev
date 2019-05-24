package com.innobiz.orange.web.wv.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** WV_SURV_B[설문 기본] */
public class WvSurvBVo  extends CommonVoImpl {	
	
	/** serialVersionUID. */
	private static final long serialVersionUID = 3254853458596025084L;
	
	private int wCnt;
	public int getWCntCnt() {
		return wCnt;
	}

	public void setWCnt(int wCnt) {
		this.wCnt = wCnt;
	}
	
	private int rCnt;
	public int getRCntCnt() {
		return rCnt;
	}

	public void setRCnt(int rCnt) {
		this.rCnt = rCnt;
	}
	
	private int useAuthCnt;
	public int getUseAuthCnt() {
		return useAuthCnt;
	}

	public void setUseAuthCnt(int useAuthCnt) {
		this.useAuthCnt = useAuthCnt;
	}
	
	
	/** 회사ID */
	private String compId;
	
		
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 설문ID */
	private String survId;
	

	public String getSurvId() {
		return survId;
	}

	public void setSurvId(String survId) {
		this.survId = survId;
	}
	
	/** 설문 제목*/
	private String survSubj;
	
	
	public String getSurvSubj() {
		return survSubj;
	}

	public void setSurvSubj(String survSubj) {
		this.survSubj = survSubj;
	}

	/** 질문 개수 */
	private int quesCnt;
	
	
	public int getQuesCnt() {
		return quesCnt;
	}

	public void setQuesCnt(int quesCnt) {
		this.quesCnt = quesCnt;
	}

	/** 설문 시작일시 */
	private String survStartDt;
	
	
	public String getSurvStartDt() {
		return survStartDt;
	}

	public void setSurvStartDt(String survStartDt) {
		if(survStartDt != null && survStartDt.endsWith(".0") && survStartDt.length()>2)
			 survStartDt=survStartDt.substring(0, survStartDt.length()-2);
		this.survStartDt = survStartDt;
	}
	
	/** 설문 종료일시 */
	private String survEndDt;
	
	
	public String getSurvEndDt() {
		return survEndDt;
	}

	public void setSurvEndDt(String survEndDt) {
		if(survEndDt != null && survEndDt.endsWith(".0") && survEndDt.length()>2)
			 survEndDt=survEndDt.substring(0, survEndDt.length()-2);
		this.survEndDt = survEndDt;
	}
	
	/** 포틀릿ID */
	private String pltId;
	
	
	public String getPltId() {
		return pltId;
	}

	public void setPltId(String pltId) {
		this.pltId = pltId;
	}
	
	/** 등록일시 */
	private String regDt;
	
	
	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	/** 수정일시 */
	private String modDt;
	

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}
	
	/** 등록자 ID */
	private String regrUid;
	

	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}
	
	/** 등록자명 */
	private String regrNm;
	

	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 수정자 ID */
	private String modrUid;
	
	
	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}
	
	/** 설문진행상태코드 */
	private String survPrgStatCd;
	
	
	public String getSurvPrgStatCd() {
		return survPrgStatCd;
	}

	public void setSurvPrgStatCd(String survPrgStatCd) {
		this.survPrgStatCd = survPrgStatCd;
	}
	
	/** 공개여부 */
	private String openYn;
	

	public String getOpenYn() {
		return openYn;
	}

	public void setOpenYn(String openYn) {
		this.openYn = openYn;
	}
	
	
	/** 재설문여부 */
	private String repetSurvYn;


	public String getRepetSurvYn() {
		return repetSurvYn;
	}

	public void setRepetSurvYn(String repetSurvYn) {
		this.repetSurvYn = repetSurvYn;
	}
	
	/** 설문취지 */
	private String survItnt;
	

	public String getSurvItnt() {
		return survItnt;
	}

	public void setSurvItnt(String survItnt) {
		this.survItnt = survItnt;
	}
	
	/** 설문바닥글 */
	private String survFtr;
	

	public String getSurvFtr() {
		return survFtr;
	}

	public void setSurvFtr(String survFtr) {
		this.survFtr = survFtr;
	}
	
	/** 첨부여부 */
	private String attYn;
	

	public String getAttYn() {
		return attYn;
	}

	public void setAttYn(String attYn) {
		this.attYn = attYn;
	}
	
	/** 관리자반려의견내용 */
	private String admRjtOpinCont;


	public String getAdmRjtOpinCont() {
		return admRjtOpinCont;
	}

	public void setAdmRjtOpinCont(String admRjtOpinCont) {
		this.admRjtOpinCont = admRjtOpinCont;
	}
	
	/** 설문파일ID */
	private String survFileId;
	

	public String getSurvFileId() {
		return survFileId;
	}

	public void setSurvFileId(String survFileId) {
		this.survFileId = survFileId;
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
	
	/** 사용자 설문목록 구분값 */
	private String authTgtUid;

	public String getAuthTgtUid() {
		return authTgtUid;
	}

	public void setAuthTgtUid(String authTgtUid) {
		this.authTgtUid = authTgtUid;
	}
	
	/** 비권한여부 */
	private String noAuthYn;

	public String getNoAuthYn() {
		return noAuthYn;
	}

	public void setNoAuthYn(String noAuthYn) {
		this.noAuthYn = noAuthYn;
	}
	
	/** 조직ID목록 */
	private String[] schOrgPids;
	
	public String[] getSchOrgPids() {
		return schOrgPids;
	}

	public void setSchOrgPids(String[] schOrgPids) {
		this.schOrgPids = schOrgPids;
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
		builder.append('[').append(this.getClass().getName()).append(":설문 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(wCnt!=0) { if(tab!=null) builder.append(tab); builder.append("wCnt(serialVersionUID.):").append(wCnt).append('\n'); }
		if(rCnt!=0) { if(tab!=null) builder.append(tab); builder.append("rCnt(serialVersionUID.):").append(rCnt).append('\n'); }
		if(useAuthCnt!=0) { if(tab!=null) builder.append(tab); builder.append("useAuthCnt(serialVersionUID.):").append(useAuthCnt).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(survSubj!=null) { if(tab!=null) builder.append(tab); builder.append("survSubj(설문 제목):").append(survSubj).append('\n'); }
		if(quesCnt!=0) { if(tab!=null) builder.append(tab); builder.append("quesCnt(질문 개수):").append(quesCnt).append('\n'); }
		if(survStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("survStartDt(설문 시작일시):").append(survStartDt).append('\n'); }
		if(survEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("survEndDt(설문 종료일시):").append(survEndDt).append('\n'); }
		if(pltId!=null) { if(tab!=null) builder.append(tab); builder.append("pltId(포틀릿ID):").append(pltId).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 ID):").append(regrUid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 ID):").append(modrUid).append('\n'); }
		if(survPrgStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("survPrgStatCd(설문진행상태코드):").append(survPrgStatCd).append('\n'); }
		if(openYn!=null) { if(tab!=null) builder.append(tab); builder.append("openYn(공개여부):").append(openYn).append('\n'); }
		if(repetSurvYn!=null) { if(tab!=null) builder.append(tab); builder.append("repetSurvYn(재설문여부):").append(repetSurvYn).append('\n'); }
		if(survItnt!=null) { if(tab!=null) builder.append(tab); builder.append("survItnt(설문취지):").append(survItnt).append('\n'); }
		if(survFtr!=null) { if(tab!=null) builder.append(tab); builder.append("survFtr(설문바닥글):").append(survFtr).append('\n'); }
		if(attYn!=null) { if(tab!=null) builder.append(tab); builder.append("attYn(첨부여부):").append(attYn).append('\n'); }
		if(admRjtOpinCont!=null) { if(tab!=null) builder.append(tab); builder.append("admRjtOpinCont(관리자반려의견내용):").append(admRjtOpinCont).append('\n'); }
		if(survFileId!=null) { if(tab!=null) builder.append(tab); builder.append("survFileId(설문파일ID):").append(survFileId).append('\n'); }
		if(logUserUid!=null) { if(tab!=null) builder.append(tab); builder.append("logUserUid(로그인 User Uid):").append(logUserUid).append('\n'); }
		if(logUserDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("logUserDeptId(로그인 User Detp Id):").append(logUserDeptId).append('\n'); }
		if(joinUserCnt!=null) { if(tab!=null) builder.append(tab); builder.append("joinUserCnt(참여 인원수):").append(joinUserCnt).append('\n'); }
		if(replyYn!=null) { if(tab!=null) builder.append(tab); builder.append("replyYn(응답 여부):").append(replyYn).append('\n'); }
		if(survSearchList!=null) { if(tab!=null) builder.append(tab); builder.append("survSearchList(설문검색 List):"); appendStringListTo(builder, survSearchList,tab); builder.append('\n'); }
		if(survAuthList!=null) { if(tab!=null) builder.append(tab); builder.append("survAuthList(설문 권한 List):"); appendVoListTo(builder, survAuthList,tab); builder.append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(사용자 언어 타입):").append(langTyp).append('\n'); }
		if(authTgtUid!=null) { if(tab!=null) builder.append(tab); builder.append("authTgtUid(사용자 설문목록 구분값):").append(authTgtUid).append('\n'); }
		if(noAuthYn!=null) { if(tab!=null) builder.append(tab); builder.append("noAuthYn(비권한여부):").append(noAuthYn).append('\n'); }
		if(schOrgPids!=null) { if(tab!=null) builder.append(tab); builder.append("schOrgPids(조직ID목록):").append(schOrgPids).append('\n'); }
		
		super.toString(builder, tab);
	}





	
	
}