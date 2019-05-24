package com.innobiz.orange.web.wl.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/02/20 14:46 ******/
/**
* 업무보고기본(WT_TASK_REPRT_B) 테이블 VO 
*/
public class WlTaskLogBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -4579094357299129910L;

	/** 업무보고번호 */ 
	private String logNo;

 	/** 회사ID */ 
	private String compId;
	
	/** 부서ID */ 
	private String deptId;

 	/** 구분코드 */ 
	private String typCd;

 	/** 보고일자 */ 
	private String reprtDt;
	
	/** 그룹번호 */ 
	private String grpNo;

 	/** 제목 */ 
	private String subj;

 	/** 실적 */ 
	private String resultCont;

 	/** 계획 */ 
	private String planCont;

 	/** 기타내용 */ 
	private String etcCont;

 	/** 상태코드 */ 
	private String statCd;

 	/** 취합여부 */ 
	private String consolYn;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 추가*/
	/** 사용자UID */ 
	private String userUid;
	
	/** 취합대상UID */ 
	private String consolUid;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 첨부파일 건수 */
	private Integer fileCnt;
	
	/** 다중 조회 조건 - 사용자UID */
	private List<String> userUidList; 
	
	/** 다중 조회 조건 - 일지번호 */
	private List<String> logNoList; 
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob = true;
	
	/** 회사ID목록 */
	private List<String> compIdList;

 	public void setLogNo(String logNo) { 
		this.logNo = logNo;
	}
	/** 업무보고번호 */ 
	public String getLogNo() { 
		return logNo;
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
	
	public void setTypCd(String typCd) { 
		this.typCd = typCd;
	}
	/** 구분코드 */ 
	public String getTypCd() { 
		return typCd;
	}

	public void setReprtDt(String reprtDt) { 
		this.reprtDt = reprtDt;
	}
	/** 보고일자 */ 
	public String getReprtDt() { 
		return reprtDt;
	}
	
	public void setGrpNo(String grpNo) { 
		this.grpNo = grpNo;
	}
	/** 그룹번호 */ 
	public String getGrpNo() { 
		return grpNo;
	}
	
	public void setSubj(String subj) { 
		this.subj = subj;
	}
	/** 제목 */ 
	public String getSubj() { 
		return subj;
	}

	public void setResultCont(String resultCont) { 
		this.resultCont = resultCont;
	}
	/** 실적 */ 
	public String getResultCont() { 
		return resultCont;
	}

	public void setPlanCont(String planCont) { 
		this.planCont = planCont;
	}
	/** 계획 */ 
	public String getPlanCont() { 
		return planCont;
	}

	public void setEtcCont(String etcCont) { 
		this.etcCont = etcCont;
	}
	/** 기타내용 */ 
	public String getEtcCont() { 
		return etcCont;
	}

	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}

	public void setConsolYn(String consolYn) { 
		this.consolYn = consolYn;
	}
	/** 취합여부 */ 
	public String getConsolYn() { 
		return consolYn;
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
	
	/** 사용자UID */ 
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	/** 취합대상UID */ 
	public String getConsolUid() {
		return consolUid;
	}
	public void setConsolUid(String consolUid) {
		this.consolUid = consolUid;
	}
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	/** 첨부파일 건수 */
	public Integer getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(Integer fileCnt) {
		this.fileCnt = fileCnt;
	}
	
	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}
	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	
	/** 다중 조회 조건 */
	public List<String> getUserUidList() {
		return userUidList;
	}
	public void setUserUidList(List<String> userUidList) {
		this.userUidList = userUidList;
	}
	
	/** 다중 조회 조건 - 일지번호 */
	public List<String> getLogNoList() {
		return logNoList;
	}
	public void setLogNoList(List<String> logNoList) {
		this.logNoList = logNoList;
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
			return "com.innobiz.orange.web.wl.dao.WlTaskLogBDao.selectWlTaskLogB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogBDao.insertWlTaskLogB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogBDao.updateWlTaskLogB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogBDao.deleteWlTaskLogB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wl.dao.WlTaskLogBDao.countWlTaskLogB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무보고기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(logNo!=null) { if(tab!=null) builder.append(tab); builder.append("logNo(일지번호):").append(logNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(typCd!=null) { if(tab!=null) builder.append(tab); builder.append("typCd(구분코드):").append(typCd).append('\n'); }
		if(reprtDt!=null) { if(tab!=null) builder.append(tab); builder.append("reprtDt(보고일자):").append(reprtDt).append('\n'); }
		if(grpNo!=null) { if(tab!=null) builder.append(tab); builder.append("grpNo(그룹번호):").append(grpNo).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(resultCont!=null) { if(tab!=null) builder.append(tab); builder.append("resultCont(실적):").append(resultCont).append('\n'); }
		if(planCont!=null) { if(tab!=null) builder.append(tab); builder.append("planCont(계획):").append(planCont).append('\n'); }
		if(etcCont!=null) { if(tab!=null) builder.append(tab); builder.append("etcCont(기타내용):").append(etcCont).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(consolYn!=null) { if(tab!=null) builder.append(tab); builder.append("consolYn(취합여부):").append(consolYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
