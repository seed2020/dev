package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/** CT_VISTR_HST_D [커뮤니티 방문자 이력 상세] */
public class CtVistrHstDVo extends CommonVoImpl{

	/** serialVersionUID */
	private static final long serialVersionUID = -1466666223555414544L;
	
	/** 회사 ID */
	private String compId;

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 커뮤니티 ID */
	
	private String ctId;
	
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	
	/** 사용자 UID */
	
	private String userUid;
	
	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	/** 접속 일시 */
	
	private String accsDt;
	
	public String getAccsDt() {
		return accsDt;
	}

	public void setAccsDt(String accsDt) {
		if(accsDt != null && accsDt.endsWith(".0") && accsDt.length()>2)
			 accsDt=accsDt.substring(0, accsDt.length()-2);
		this.accsDt = accsDt;
	}
	
	/** 접속 구분 코드 */
	
	private String accsTypCd;
	
	public String getAccsTypCd() {
		return accsTypCd;
	}

	public void setAccsTypCd(String accsTypCd) {
		this.accsTypCd = accsTypCd;
	}
	
	//추가컬럼
	
	/** 사용자명 */
	
	private String userNm;
	
	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
	/** 부서명 */
	
	private String deptNm;
	
	public String getDeptNm() {
		return deptNm;
	}

	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	
	/** 사용자 보안등급 코드 */
	private String userSeculCd;
	
	public String getUserSeculCd() {
		return userSeculCd;
	}

	public void setUserSeculCd(String userSeculCd) {
		this.userSeculCd = userSeculCd;
	}
	
	/** 가입일자 */
	private String joinDt;
	
	public String getJoinDt() {
		return joinDt;
	}

	public void setJoinDt(String joinDt) {
		if(joinDt != null && joinDt.endsWith(".0") && joinDt.length()>2)
			 joinDt=joinDt.substring(0, joinDt.length()-2);
		this.joinDt = joinDt;
	}
	
	/** 다국어처리 언어 */
	private String langTyp;

	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 오늘방문 수*/
	private String ctTodayCount;
	
	public String getCtTodayCount() {
		return ctTodayCount;
	}

	public void setCtTodayCount(String ctTodayCount) {
		this.ctTodayCount = ctTodayCount;
	}
	
	/** 전체방문 수*/
	private String ctAllCount;
	
	public String getCtAllCount() {
		return ctAllCount;
	}

	public void setCtAllCount(String ctAllCount) {
		this.ctAllCount = ctAllCount;
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
	
	/** 가입상태 */
	private String joinStat;
	
	public String getJoinStat() {
		return joinStat;
	}

	public void setJoinStat(String joinStat) {
		this.joinStat = joinStat;
	}
	
	/** 사용자기본(OR_USER_B) 테이블 VO */
	private OrUserBVo orUserBVo;
	
	/** 사용자기본(OR_USER_B) 테이블 VO */
	public OrUserBVo getOrUserBVo() {
		return orUserBVo;
	}
	
	/** 사용자기본(OR_USER_B) 테이블 VO */
	public void setOrUserBVo(OrUserBVo orUserBVo) {
		this.orUserBVo = orUserBVo;
	}
	
	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	private OrOdurBVo orOdurBVo;
	
	
	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	public OrOdurBVo getOrOdurBVo() {
		return orOdurBVo;
	}

	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	public void setOrOdurBVo(OrOdurBVo orOdurBVo) {
		this.orOdurBVo = orOdurBVo;
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
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자 UID):").append(userUid).append('\n'); }
		if(accsDt!=null) { if(tab!=null) builder.append(tab); builder.append("accsDt(접속 일시):").append(accsDt).append('\n'); }
		if(accsTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("accsTypCd(접속 구분 코드):").append(accsTypCd).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(deptNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptNm(부서명):").append(deptNm).append('\n'); }
		if(userSeculCd!=null) { if(tab!=null) builder.append(tab); builder.append("userSeculCd(사용자 보안등급 코드):").append(userSeculCd).append('\n'); }
		if(joinDt!=null) { if(tab!=null) builder.append(tab); builder.append("joinDt(가입일자):").append(joinDt).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(다국어처리 언어):").append(langTyp).append('\n'); }
		if(ctTodayCount!=null) { if(tab!=null) builder.append(tab); builder.append("ctTodayCount(오늘방문 수):").append(ctTodayCount).append('\n'); }
		if(ctAllCount!=null) { if(tab!=null) builder.append(tab); builder.append("ctAllCount(전체방문 수):").append(ctAllCount).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일자):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일자):").append(endDt).append('\n'); }
		if(joinStat!=null) { if(tab!=null) builder.append(tab); builder.append("joinStat(가입상태):").append(joinStat).append('\n'); }
		if(orUserBVo!=null) { if(tab!=null) builder.append(tab); builder.append("orUserBVo(사용자기본(OR_USER_B) 테이블 VO):\n"); orUserBVo.toString(builder, tab==null?"\t":tab+"\t"); builder.append('\n'); }
		if(orOdurBVo!=null) { if(tab!=null) builder.append(tab); builder.append("orOdurBVo(원직자기본(OR_ODUR_B) 테이블 VO):\n"); orOdurBVo.toString(builder, tab==null?"\t":tab+"\t"); builder.append('\n'); }
		super.toString(builder, tab);
	}

}