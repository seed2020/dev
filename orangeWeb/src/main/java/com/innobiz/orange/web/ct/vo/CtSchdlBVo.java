package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 일정 기본(WC_SCHDL_B)테이블 VO
 */
public class CtSchdlBVo  extends CommonVoImpl implements Cloneable{	

	/** serialVersionUID. */
	private static final long serialVersionUID = 6995665232792597433L;
	
	/** 일정ID */
	private String schdlId;

	/** 사용자UID */
	private String userUid;

	/** 회사ID */
	private String compId;

	/** 부서ID */
	private String deptId;

	/** 그룹ID */
	private String grpId;

	/** 일정구분코드 */
	private String schdlTypCd;

	/** 일정종류코드 */
	private String schdlKndCd;

	/** 공개등급코드 */
	private String openGradCd;

	/** 일정상태코드 */
	private String schdlStatCd;

	/** 에디터구분코드 */
	private String editorTypCd;

	/** 본인입력여부 */
	private String selfRegYn;

	/** 일정시작일시 */
	private String schdlStartDt;

	/** 일정종료일시 */
	private String schdlEndDt;
	
	/** 일정음력시작일시 */
	private String schdlLunaStartDt;

	/** 일정음력종료일시 */
	private String schdlLunaEndDt;

	/** 반복여부 */
	private String repetYn;

	/** 휴일여부 */
	private String holiYn;

	/** 양음력여부 */
	private String solaLunaYn;

	/** 제목 */
	private String subj;

	/** 장소명 */
	private String locNm;

	/** 장소위도경도값 */
	private String locLatdLotdVa;

	/** 내용 */
	private String cont;

	/** 할일우선순서 */
	private String workPrioOrdr;

	/** 할일완료년월일 */
	private String workCmltYmd;

	/** 첨부여부 */
	private String attYn;

	/** 등록자UID */
	private String regrUid;
	
	/** 등록자명 */
	private String regrNm;

	/** 등록일시 */
	private String regDt;

	/** 수정일시 */
	private String modDt;

	/** 수정자UID */
	private String modrUid;

	/** 일정파일ID */
	private String schdlFileId;

	/** 일정 부모ID */
	private String schdlPid;
	
	/** 해당 월별 년월 */
	private String schdlYm;
	
	/** 이전 월별 년월 */
	private String schdlPrevYm;
	
	/** 이후 월별 년월 */
	private String schdlNextYm;
	
	/** 반복일정상태표시 */
	private String schdlRepetState;
	
	/** 일정 순서 */
	private int schdIndex=-1;
	
	/** 검색약속체크 */
	private String searchPromChk; 
	
	/** 검색할일체크 */
	private String searchWorkChk;
	
	/** 검색행사체크 */
	private String searchEvntChk;
	
	/** 검색기념일체크 */
	private String searchAnnvChk;
	
	/** 검색개인체크 */
	private String searchPsnChk;
	
	/** 검색그룹체크 */
	private String searchGrpChk;
	
	/** 검색회사체크 */
	private String searchCompChk;
	
	/** 검색부서체크 */
	private String searchDeptChk;
	
	/** 참석자List */
	private List<CtSchdlPromGuestDVo> promGuestLst ;
	
	/** 일정구분코드 List*/
	private List<String> schdlTypCdLst;
	
	/** 일정종류코드 List*/
	private List<String> schdlKndCdLst;
	
	/** 선택된 그룹 List*/
	private String[] choiGrpIds;
	
	/** start 부터 end 며칠간 */
	private String afterDay;
	
	/** 해당 날짜의 시작 시간 */
	private int startHour;
	
	/** 해당 날짜의 종료 시간 */
	private int endHour;
	
	// 추가 컬럼
	
	/** 반복 시작 일시*/
	private String repetStartDt;
	
	/** 반복 종료 일시*/
	private String repetEndDt;
	
	/** 사용자 언어타입 */
	private String langTyp;
	
	/** 커뮤니티  리스트 */
	private List<String> ctIdList;
	
	/** 커뮤니티 명 */
	private String ctNm;
	
	
/** 일정 수정사항 반영을 위한 추가 컬럼 */
	/** 시작일시(YMD)*/
	private String schdlStrtYmd;
	
	/** 종료일시(YMD)*/
	private String schdlEndYmd;
	
	/** 시작시간(HH24:MI)*/
	private String schdlStrtTime;
	
	/** 종료시간(HH24:MI)*/
	private String schdlEndTime;
	
	/** 일정구분명 */
	private String schdlTypNm;
	
	/** 종일여부 */
	private String alldayYn;
	
	/** 그룹명 */
	private String grpNm;
	
	/** 이메일발송여부 */
	private String emailSendYn;
	
	
	/** 커뮤니티 기능ID */
	private String ctFncId;
	
	/** 기능 위치 단계 */
	private String ctFncLocStep;
	 
	/** 커뮤니티 기능 UID */
	private String ctFncUid;
	 
	/** 커뮤니티 기능 부모ID */
	private String ctFncPid;
	
	/** 커뮤니티 ID */
	private String ctId;
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob;
	
/**	자료검색을 위한 추가 컬럼	*/
	
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

	public List<String> getCtIdList() {
		return ctIdList;
	}

	public void setCtIdList(List<String> ctIdList) {
		this.ctIdList = ctIdList;
	}

	/**
	 * @return the emailSendYn
	 */
	public String getEmailSendYn() {
		return emailSendYn;
	}

	/**
	 * @param emailSendYn the emailSendYn to set
	 */
	public void setEmailSendYn(String emailSendYn) {
		this.emailSendYn = emailSendYn;
	}

	public String getGrpNm() {
		return grpNm;
	}

	public void setGrpNm(String grpNm) {
		this.grpNm = grpNm;
	}

	public String getSchdlId() {
		return schdlId;
	}

	public void setSchdlId(String schdlId) {
		this.schdlId = schdlId;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getGrpId() {
		return grpId;
	}

	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}

	public String getSchdlTypCd() {
		return schdlTypCd;
	}

	public void setSchdlTypCd(String schdlTypCd) {
		this.schdlTypCd = schdlTypCd;
	}

	public String getSchdlKndCd() {
		return schdlKndCd;
	}

	public void setSchdlKndCd(String schdlKndCd) {
		this.schdlKndCd = schdlKndCd;
	}

	public String getOpenGradCd() {
		return openGradCd;
	}

	public void setOpenGradCd(String openGradCd) {
		this.openGradCd = openGradCd;
	}

	public String getSchdlStatCd() {
		return schdlStatCd;
	}

	public void setSchdlStatCd(String schdlStatCd) {
		this.schdlStatCd = schdlStatCd;
	}

	public String getEditorTypCd() {
		return editorTypCd;
	}

	public void setEditorTypCd(String editorTypCd) {
		this.editorTypCd = editorTypCd;
	}

	public String getSelfRegYn() {
		return selfRegYn;
	}

	public String getCtFncId() {
		return ctFncId;
	}

	public void setCtFncId(String ctFncId) {
		this.ctFncId = ctFncId;
	}

	public String getCtFncLocStep() {
		return ctFncLocStep;
	}

	public void setCtFncLocStep(String ctFncLocStep) {
		this.ctFncLocStep = ctFncLocStep;
	}

	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
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

	public void setSelfRegYn(String selfRegYn) {
		this.selfRegYn = selfRegYn;
	}

	public String getSchdlStartDt() {
		return schdlStartDt;
	}

	public void setSchdlStartDt(String schdlStartDt) {
		if(schdlStartDt != null && schdlStartDt.endsWith(".0") && schdlStartDt.length()>2)
			 schdlStartDt=schdlStartDt.substring(0, schdlStartDt.length()-2);
		this.schdlStartDt = schdlStartDt;
	}

	public String getSchdlEndDt() {
		return schdlEndDt;
	}

	public void setSchdlEndDt(String schdlEndDt) {
		if(schdlEndDt != null && schdlEndDt.endsWith(".0") && schdlEndDt.length()>2)
			 schdlEndDt=schdlEndDt.substring(0, schdlEndDt.length()-2);
		this.schdlEndDt = schdlEndDt;
	}

	public String getRepetYn() {
		return repetYn;
	}

	public void setRepetYn(String repetYn) {
		this.repetYn = repetYn;
	}

	public List<String> getSchdlKndCdLst() {
		return schdlKndCdLst;
	}

	public void setSchdlKndCdLst(List<String> schdlKndCdLst) {
		this.schdlKndCdLst = schdlKndCdLst;
	}

	public String getHoliYn() {
		return holiYn;
	}

	public void setHoliYn(String holiYn) {
		this.holiYn = holiYn;
	}

	public String getSolaLunaYn() {
		return solaLunaYn;
	}

	public void setSolaLunaYn(String solaLunaYn) {
		this.solaLunaYn = solaLunaYn;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getLocNm() {
		return locNm;
	}

	public void setLocNm(String locNm) {
		this.locNm = locNm;
	}

	public String getLocLatdLotdVa() {
		return locLatdLotdVa;
	}

	public void setLocLatdLotdVa(String locLatdLotdVa) {
		this.locLatdLotdVa = locLatdLotdVa;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getWorkPrioOrdr() {
		return workPrioOrdr;
	}

	public void setWorkPrioOrdr(String workPrioOrdr) {
		this.workPrioOrdr = workPrioOrdr;
	}

	public String getWorkCmltYmd() {
		return workCmltYmd;
	}

	public void setWorkCmltYmd(String workCmltYmd) {
		this.workCmltYmd = workCmltYmd;
	}

	public String getAttYn() {
		return attYn;
	}

	public void setAttYn(String attYn) {
		this.attYn = attYn;
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

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	public String getSchdlFileId() {
		return schdlFileId;
	}

	public void setSchdlFileId(String schdlFileId) {
		this.schdlFileId = schdlFileId;
	}

	public String getSchdlPid() {
		return schdlPid;
	}

	public void setSchdlPid(String schdlPid) {
		this.schdlPid = schdlPid;
	}
	public String getSchdlYm() {
		return schdlYm;
	}

	public void setSchdlYm(String schdlYm) {
		this.schdlYm = schdlYm;
	}

	public String getSchdlPrevYm() {
		return schdlPrevYm;
	}

	public void setSchdlPrevYm(String schdlPrevYm) {
		this.schdlPrevYm = schdlPrevYm;
	}

	public String getSchdlNextYm() {
		return schdlNextYm;
	}

	public void setSchdlNextYm(String schdlNextYm) {
		this.schdlNextYm = schdlNextYm;
	}
	
	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	public String getSchdlRepetState() {
		return schdlRepetState;
	}

	public void setSchdlRepetState(String schdlRepetState) {
		this.schdlRepetState = schdlRepetState;
	}
	
	public String getSearchPromChk() {
		return searchPromChk;
	}

	public void setSearchPromChk(String searchPromChk) {
		this.searchPromChk = searchPromChk;
	}

	public String getSearchWorkChk() {
		return searchWorkChk;
	}

	public void setSearchWorkChk(String searchWorkChk) {
		this.searchWorkChk = searchWorkChk;
	}

	public String getSearchEvntChk() {
		return searchEvntChk;
	}

	public void setSearchEvntChk(String searchEvntChk) {
		this.searchEvntChk = searchEvntChk;
	}

	public String getSearchAnnvChk() {
		return searchAnnvChk;
	}

	public void setSearchAnnvChk(String searchAnnvChk) {
		this.searchAnnvChk = searchAnnvChk;
	}

	public String getSearchPsnChk() {
		return searchPsnChk;
	}

	public void setSearchPsnChk(String searchPsnChk) {
		this.searchPsnChk = searchPsnChk;
	}

	public String getSearchGrpChk() {
		return searchGrpChk;
	}

	public void setSearchGrpChk(String searchGrpChk) {
		this.searchGrpChk = searchGrpChk;
	}

	public String getSearchCompChk() {
		return searchCompChk;
	}

	public void setSearchCompChk(String searchCompChk) {
		this.searchCompChk = searchCompChk;
	}
	
	public String getSearchDeptChk() {
		return searchDeptChk;
	}

	public void setSearchDeptChk(String searchDeptChk) {
		this.searchDeptChk = searchDeptChk;
	}
	
	public List<CtSchdlPromGuestDVo> getPromGuestLst() {
		return promGuestLst;
	}

	public void setPromGuestLst(List<CtSchdlPromGuestDVo> promGuestLst) {
		this.promGuestLst = promGuestLst;
	}
	
	public String getAfterDay() {
		return afterDay;
	}

	public void setAfterDay(String afterDay) {
		this.afterDay = afterDay;
	}
	
	public String getRepetStartDt() {
		return repetStartDt;
	}

	public void setRepetStartDt(String repetStartDt) {
		if(repetStartDt != null && repetStartDt.endsWith(".0") && repetStartDt.length()>2)
			 repetStartDt=repetStartDt.substring(0, repetStartDt.length()-2);
		this.repetStartDt = repetStartDt;
	}

	public String getRepetEndDt() {
		return repetEndDt;
	}

	public void setRepetEndDt(String repetEndDt) {
		if(repetEndDt != null && repetEndDt.endsWith(".0") && repetEndDt.length()>2)
			 repetEndDt=repetEndDt.substring(0, repetEndDt.length()-2);
		this.repetEndDt = repetEndDt;
	}

	public String getSchdlStrtYmd() {
		return schdlStrtYmd;
	}

	public void setSchdlStrtYmd(String schdlStrtYmd) {
		this.schdlStrtYmd = schdlStrtYmd;
	}

	public String getSchdlEndYmd() {
		return schdlEndYmd;
	}

	public void setSchdlEndYmd(String schdlEndYmd) {
		this.schdlEndYmd = schdlEndYmd;
	}

	public String getSchdlStrtTime() {
		return schdlStrtTime;
	}

	public void setSchdlStrtTime(String schdlStrtTime) {
		this.schdlStrtTime = schdlStrtTime;
	}

	public String getSchdlEndTime() {
		return schdlEndTime;
	}

	public void setSchdlEndTime(String schdlEndTime) {
		this.schdlEndTime = schdlEndTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSchdlTypNm() {
		return schdlTypNm;
	}

	public void setSchdlTypNm(String schdlTypNm) {
		this.schdlTypNm = schdlTypNm;
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
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(grpId!=null) { if(tab!=null) builder.append(tab); builder.append("grpId(그룹ID):").append(grpId).append('\n'); }
		if(schdlTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlTypCd(일정구분코드):").append(schdlTypCd).append('\n'); }
		if(schdlKndCd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlKndCd(일정종류코드):").append(schdlKndCd).append('\n'); }
		if(openGradCd!=null) { if(tab!=null) builder.append(tab); builder.append("openGradCd(공개등급코드):").append(openGradCd).append('\n'); }
		if(schdlStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlStatCd(일정상태코드):").append(schdlStatCd).append('\n'); }
		if(editorTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("editorTypCd(에디터구분코드):").append(editorTypCd).append('\n'); }
		if(selfRegYn!=null) { if(tab!=null) builder.append(tab); builder.append("selfRegYn(본인입력여부):").append(selfRegYn).append('\n'); }
		if(schdlStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("schdlStartDt(일정시작일시):").append(schdlStartDt).append('\n'); }
		if(schdlEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("schdlEndDt(일정종료일시):").append(schdlEndDt).append('\n'); }
		if(schdlLunaStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("schdlLunaStartDt(일정음력시작일시):").append(schdlLunaStartDt).append('\n'); }
		if(schdlLunaEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("schdlLunaEndDt(일정음력종료일시):").append(schdlLunaEndDt).append('\n'); }
		if(repetYn!=null) { if(tab!=null) builder.append(tab); builder.append("repetYn(반복여부):").append(repetYn).append('\n'); }
		if(holiYn!=null) { if(tab!=null) builder.append(tab); builder.append("holiYn(휴일여부):").append(holiYn).append('\n'); }
		if(solaLunaYn!=null) { if(tab!=null) builder.append(tab); builder.append("solaLunaYn(양음력여부):").append(solaLunaYn).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(locNm!=null) { if(tab!=null) builder.append(tab); builder.append("locNm(장소명):").append(locNm).append('\n'); }
		if(locLatdLotdVa!=null) { if(tab!=null) builder.append(tab); builder.append("locLatdLotdVa(장소위도경도값):").append(locLatdLotdVa).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(workPrioOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("workPrioOrdr(할일우선순서):").append(workPrioOrdr).append('\n'); }
		if(workCmltYmd!=null) { if(tab!=null) builder.append(tab); builder.append("workCmltYmd(할일완료년월일):").append(workCmltYmd).append('\n'); }
		if(attYn!=null) { if(tab!=null) builder.append(tab); builder.append("attYn(첨부여부):").append(attYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(schdlFileId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlFileId(일정파일ID):").append(schdlFileId).append('\n'); }
		if(schdlPid!=null) { if(tab!=null) builder.append(tab); builder.append("schdlPid(일정 부모ID):").append(schdlPid).append('\n'); }
		if(schdlYm!=null) { if(tab!=null) builder.append(tab); builder.append("schdlYm(해당 월별 년월):").append(schdlYm).append('\n'); }
		if(schdlPrevYm!=null) { if(tab!=null) builder.append(tab); builder.append("schdlPrevYm(이전 월별 년월):").append(schdlPrevYm).append('\n'); }
		if(schdlNextYm!=null) { if(tab!=null) builder.append(tab); builder.append("schdlNextYm(이후 월별 년월):").append(schdlNextYm).append('\n'); }
		if(schdlRepetState!=null) { if(tab!=null) builder.append(tab); builder.append("schdlRepetState(반복일정상태표시):").append(schdlRepetState).append('\n'); }
		if(schdIndex!=0) { if(tab!=null) builder.append(tab); builder.append("schdIndex(일정 순서):").append(schdIndex).append('\n'); }
		if(searchPromChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchPromChk(검색약속체크):").append(searchPromChk).append('\n'); }
		if(searchWorkChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchWorkChk(검색할일체크):").append(searchWorkChk).append('\n'); }
		if(searchEvntChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchEvntChk(검색행사체크):").append(searchEvntChk).append('\n'); }
		if(searchAnnvChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchAnnvChk(검색기념일체크):").append(searchAnnvChk).append('\n'); }
		if(searchPsnChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchPsnChk(검색개인체크):").append(searchPsnChk).append('\n'); }
		if(searchGrpChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchGrpChk(검색그룹체크):").append(searchGrpChk).append('\n'); }
		if(searchCompChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchCompChk(검색회사체크):").append(searchCompChk).append('\n'); }
		if(searchDeptChk!=null) { if(tab!=null) builder.append(tab); builder.append("searchDeptChk(검색부서체크):").append(searchDeptChk).append('\n'); }
		if(promGuestLst!=null) { if(tab!=null) builder.append(tab); builder.append("promGuestLst(참석자List):"); appendVoListTo(builder, promGuestLst,tab); builder.append('\n'); }
		if(schdlTypCdLst!=null) { if(tab!=null) builder.append(tab); builder.append("schdlTypCdLst(일정구분코드 List):"); appendStringListTo(builder, schdlTypCdLst,tab); builder.append('\n'); }
		if(schdlKndCdLst!=null) { if(tab!=null) builder.append(tab); builder.append("schdlKndCdLst(일정종류코드 List):"); appendStringListTo(builder, schdlKndCdLst,tab); builder.append('\n'); }
		if(choiGrpIds!=null) { if(tab!=null) builder.append(tab); builder.append("choiGrpIds(선택된 그룹 List):"); appendArrayTo(builder, choiGrpIds); builder.append('\n'); }
		if(afterDay!=null) { if(tab!=null) builder.append(tab); builder.append("afterDay(start 부터 end 며칠간):").append(afterDay).append('\n'); }
		if(startHour!=0) { if(tab!=null) builder.append(tab); builder.append("startHour(해당 날짜의 시작 시간):").append(startHour).append('\n'); }
		if(endHour!=0) { if(tab!=null) builder.append(tab); builder.append("endHour(해당 날짜의 종료 시간):").append(endHour).append('\n'); }
		if(repetStartDt!=null) { if(tab!=null) builder.append(tab); builder.append("repetStartDt(반복 시작 일시):").append(repetStartDt).append('\n'); }
		if(repetEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("repetEndDt(반복 종료 일시):").append(repetEndDt).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(사용자 언어타입):").append(langTyp).append('\n'); }
		if(ctIdList!=null) { if(tab!=null) builder.append(tab); builder.append("ctIdList(커뮤니티  리스트):"); appendStringListTo(builder, ctIdList,tab); builder.append('\n'); }
		if(ctNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctNm(커뮤니티 명):").append(ctNm).append('\n'); }
		if(schdlStrtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlStrtYmd(시작일시(YMD)):").append(schdlStrtYmd).append('\n'); }
		if(schdlEndYmd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlEndYmd(종료일시(YMD)):").append(schdlEndYmd).append('\n'); }
		if(schdlStrtTime!=null) { if(tab!=null) builder.append(tab); builder.append("schdlStrtTime(시작시간(HH24:MI)):").append(schdlStrtTime).append('\n'); }
		if(schdlEndTime!=null) { if(tab!=null) builder.append(tab); builder.append("schdlEndTime(종료시간(HH24:MI)):").append(schdlEndTime).append('\n'); }
		if(schdlTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("schdlTypNm(일정구분명):").append(schdlTypNm).append('\n'); }
		if(alldayYn!=null) { if(tab!=null) builder.append(tab); builder.append("alldayYn(종일여부):").append(alldayYn).append('\n'); }
		if(grpNm!=null) { if(tab!=null) builder.append(tab); builder.append("grpNm(그룹명):").append(grpNm).append('\n'); }
		if(emailSendYn!=null) { if(tab!=null) builder.append(tab); builder.append("emailSendYn(이메일발송여부):").append(emailSendYn).append('\n'); }
		if(ctFncId!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncId(커뮤니티 기능ID):").append(ctFncId).append('\n'); }
		if(ctFncLocStep!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncLocStep(기능 위치 단계):").append(ctFncLocStep).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		if(ctFncPid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncPid(커뮤니티 기능 부모ID):").append(ctFncPid).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(fncNm!=null) { if(tab!=null) builder.append(tab); builder.append("fncNm(기능명):").append(fncNm).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일자):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일자):").append(endDt).append('\n'); }
		if(ctComFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctComFncUid(커뮤니티 통합기능Uid):").append(ctComFncUid).append('\n'); }
		if(ctFncUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUrl(커뮤니티 기능 URL):").append(ctFncUrl).append('\n'); }
		if(relTblSubj!=null) { if(tab!=null) builder.append(tab); builder.append("relTblSubj(관계테이블명):").append(relTblSubj).append('\n'); }
		if(mastUid!=null) { if(tab!=null) builder.append(tab); builder.append("mastUid(마스터UID):").append(mastUid).append('\n'); }
		if(mastNm!=null) { if(tab!=null) builder.append(tab); builder.append("mastNm(마스터명):").append(mastNm).append('\n'); }
		if(withLob) { if(tab!=null) builder.append(tab); builder.append("withLob(LOB 데이터 조회 여부):").append(withLob).append('\n'); }
		super.toString(builder, tab);
	}

	public int getSchdIndex() {
		return schdIndex;
	}

	public void setSchdIndex(int schdIndex) {
		this.schdIndex = schdIndex;
	}
	
	public Object clone() throws CloneNotSupportedException {
		 CtSchdlBVo a = (CtSchdlBVo)super.clone();
		  return a;
		
	}

	public List<String> getSchdlTypCdLst() {
		return schdlTypCdLst;
	}

	public void setSchdlTypCdLst(List<String> schdlTypCdLst) {
		this.schdlTypCdLst = schdlTypCdLst;
	}

	public String[] getChoiGrpIds() {
		return choiGrpIds;
	}

	public void setChoiGrpIds(String[] choiGrpIds) {
		this.choiGrpIds = choiGrpIds;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public String getSchdlLunaStartDt() {
		return schdlLunaStartDt;
	}

	public void setSchdlLunaStartDt(String schdlLunaStartDt) {
		if(schdlLunaStartDt != null && schdlLunaStartDt.endsWith(".0") && schdlLunaStartDt.length()>2)
			 schdlLunaStartDt=schdlLunaStartDt.substring(0, schdlLunaStartDt.length()-2);
		this.schdlLunaStartDt = schdlLunaStartDt;
	}

	public String getSchdlLunaEndDt() {
		return schdlLunaEndDt;
	}

	public void setSchdlLunaEndDt(String schdlLunaEndDt) {
		if(schdlLunaEndDt != null && schdlLunaEndDt.endsWith(".0") && schdlLunaEndDt.length()>2)
			 schdlLunaEndDt=schdlLunaEndDt.substring(0, schdlLunaEndDt.length()-2);
		this.schdlLunaEndDt = schdlLunaEndDt;
	}

	public String getAlldayYn() {
		return alldayYn;
	}

	public void setAlldayYn(String alldayYn) {
		this.alldayYn = alldayYn;
	}
	
	public boolean isWithLob() {
		return withLob;
	}

	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	

	
}