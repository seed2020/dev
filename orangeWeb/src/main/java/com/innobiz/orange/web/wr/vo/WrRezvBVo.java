package com.innobiz.orange.web.wr.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 예약기본(WR_REZV_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class WrRezvBVo extends WrRescMngBVo {
	/** 예약ID */ 
	private String rezvId;

 	/** 예약시작일시 */ 
	private String rezvStrtDt;

 	/** 예약종료일시 */ 
	private String rezvEndDt;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

 	/** 심의상태코드 */ 
	private String discStatCd;

 	/** 심의자UID */ 
	private String discrUid;

 	/** 심의일시 */ 
	private String discDt;

 	/** 심의내용 */ 
	private String discCont;
	
	/** 일정ID */ 
	private String schdlId;
	
	/** 일정대상코드 */ 
	private String schdlKndCd;
	
	/** 배경색코드 */ 
	private String bgcolCd;
	
	/** 신청메일여부 */ 
	private String resqEmailYn;
	
	/** 결과메일여부 */ 
	private String resEmailYn;
	

/** 추가 */
	/** 예약시작일자(yyyy-MM-dd) */ 
	private String rezvStrtYmd;

	/** 예약시작시간(HH24:MI) */ 
	private String rezvStrtTime;
	
 	/** 예약종료일자(yyyy-MM-dd) */ 
	private String rezvEndYmd;
	
	/** 예약종료시간(HH24:MI) */ 
	private String rezvEndTime;
	
	/** 심의자명 */ 
	private String discrNm;
	
	/** 정렬순서*/
	private Integer rowIndex = 1;
	
	/** 중복수*/
	private Integer rowCnt;
	
	/** 목록구분 */
	private String listType;
	
	/** 하루 시작시간 */
	private String strtTime;
	
	/** 하루 종료시간 */
	private String endTime;
	
	/** 시간차이 (30분) */
	private String timeCnt;
	
	/** 예약일 */
	private String rezvDt;
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob;
	
	/** 이전 일정 여부 */
	private String prevRezvYn = "N";
	
	/** 이후 일정 여부 */
	private String nextRezvYn = "N";
	
/** 조회 조건 */
	/** 예약자ID*/
	private String schRegrUid;
	
 	public String getPrevRezvYn() {
		return prevRezvYn;
	}
	public void setPrevRezvYn(String prevRezvYn) {
		this.prevRezvYn = prevRezvYn;
	}
	public String getNextRezvYn() {
		return nextRezvYn;
	}
	public void setNextRezvYn(String nextRezvYn) {
		this.nextRezvYn = nextRezvYn;
	}
	public Integer getRowCnt() {
		return rowCnt;
	}
	public void setRowCnt(Integer rowCnt) {
		this.rowCnt = rowCnt;
	}
	public Integer getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
	public void setRezvId(String rezvId) { 
		this.rezvId = rezvId;
	}
	/** 예약ID */ 
	public String getRezvId() { 
		return rezvId;
	}

	public void setRezvStrtDt(String rezvStrtDt) { 
		this.rezvStrtDt = rezvStrtDt;
	}
	/** 예약시작일시 */ 
	public String getRezvStrtDt() { 
		return rezvStrtDt;
	}

	public void setRezvEndDt(String rezvEndDt) { 
		this.rezvEndDt = rezvEndDt;
	}
	/** 예약종료일시 */ 
	public String getRezvEndDt() { 
		return rezvEndDt;
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

	public void setDiscStatCd(String discStatCd) { 
		this.discStatCd = discStatCd;
	}
	/** 심의상태코드 */ 
	public String getDiscStatCd() { 
		return discStatCd;
	}

	public void setDiscrUid(String discrUid) { 
		this.discrUid = discrUid;
	}
	/** 심의자UID */ 
	public String getDiscrUid() { 
		return discrUid;
	}

	public void setDiscDt(String discDt) { 
		this.discDt = discDt;
	}
	/** 심의일시 */ 
	public String getDiscDt() { 
		return discDt;
	}

	public void setDiscCont(String discCont) { 
		this.discCont = discCont;
	}
	/** 심의내용 */ 
	public String getDiscCont() { 
		return discCont;
	}
	
	public String getRezvStrtYmd() {
		return rezvStrtYmd;
	}
	public void setRezvStrtYmd(String rezvStrtYmd) {
		this.rezvStrtYmd = rezvStrtYmd;
	}
	public String getRezvStrtTime() {
		return rezvStrtTime;
	}
	public void setRezvStrtTime(String rezvStrtTime) {
		this.rezvStrtTime = rezvStrtTime;
	}
	public String getRezvEndYmd() {
		return rezvEndYmd;
	}
	public void setRezvEndYmd(String rezvEndYmd) {
		this.rezvEndYmd = rezvEndYmd;
	}
	public String getRezvEndTime() {
		return rezvEndTime;
	}
	public void setRezvEndTime(String rezvEndTime) {
		this.rezvEndTime = rezvEndTime;
	}
	
	public String getDiscrNm() {
		return discrNm;
	}
	public void setDiscrNm(String discrNm) {
		this.discrNm = discrNm;
	}
	
	public String getSchdlId() {
		return schdlId;
	}
	public void setSchdlId(String schdlId) {
		this.schdlId = schdlId;
	}
	
	/** 일정대상코드 */ 
	public String getSchdlKndCd() {
		return schdlKndCd;
	}
	public void setSchdlKndCd(String schdlKndCd) {
		this.schdlKndCd = schdlKndCd;
	}
	public String getBgcolCd() {
		return bgcolCd;
	}
	public void setBgcolCd(String bgcolCd) {
		this.bgcolCd = bgcolCd;
	}
	
	public String getSchRegrUid() {
		return schRegrUid;
	}
	public void setSchRegrUid(String schRegrUid) {
		this.schRegrUid = schRegrUid;
	}
	
	public String getResqEmailYn() {
		return resqEmailYn;
	}
	public void setResqEmailYn(String resqEmailYn) {
		this.resqEmailYn = resqEmailYn;
	}
	public String getResEmailYn() {
		return resEmailYn;
	}
	public void setResEmailYn(String resEmailYn) {
		this.resEmailYn = resEmailYn;
	}
	
	public String getStrtTime() {
		return strtTime;
	}
	public void setStrtTime(String strtTime) {
		this.strtTime = strtTime;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	public String getTimeCnt() {
		return timeCnt;
	}
	public void setTimeCnt(String timeCnt) {
		this.timeCnt = timeCnt;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/** 예약일 */
	public String getRezvDt() {
		return rezvDt;
	}
	/** 예약일 */
	public void setRezvDt(String rezvDt) {
		this.rezvDt = rezvDt;
	}
	
	public boolean isWithLob() {
		return withLob;
	}
	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRezvBDao.selectWrRezvB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRezvBDao.insertWrRezvB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRezvBDao.updateWrRezvB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRezvBDao.deleteWrRezvB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wr.dao.WrRezvBDao.countWrRezvB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":예약기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(rezvId!=null) { if(tab!=null) builder.append(tab); builder.append("rezvId(예약ID):").append(rezvId).append('\n'); }
		if(rezvStrtDt!=null) { if(tab!=null) builder.append(tab); builder.append("rezvStrtDt(예약시작일시):").append(rezvStrtDt).append('\n'); }
		if(rezvEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("rezvEndDt(예약종료일시):").append(rezvEndDt).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(discStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("discStatCd(심의상태코드):").append(discStatCd).append('\n'); }
		if(discrUid!=null) { if(tab!=null) builder.append(tab); builder.append("discrUid(심의자UID):").append(discrUid).append('\n'); }
		if(discDt!=null) { if(tab!=null) builder.append(tab); builder.append("discDt(심의일시):").append(discDt).append('\n'); }
		if(discCont!=null) { if(tab!=null) builder.append(tab); builder.append("discCont(심의내용):").append(discCont).append('\n'); }
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(bgcolCd!=null) { if(tab!=null) builder.append(tab); builder.append("bgcolCd(배경색코드):").append(bgcolCd).append('\n'); }
		if(resqEmailYn!=null) { if(tab!=null) builder.append(tab); builder.append("resqEmailYn(신청메일여부):").append(resqEmailYn).append('\n'); }
		if(resEmailYn!=null) { if(tab!=null) builder.append(tab); builder.append("resEmailYn(결과메일여부):").append(resEmailYn).append('\n'); }
		if(rezvStrtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("rezvStrtYmd(예약시작일자(yyyy):").append(rezvStrtYmd).append('\n'); }
		if(rezvStrtTime!=null) { if(tab!=null) builder.append(tab); builder.append("rezvStrtTime(예약시작시간(HH24:MI)):").append(rezvStrtTime).append('\n'); }
		if(rezvEndYmd!=null) { if(tab!=null) builder.append(tab); builder.append("rezvEndYmd(예약종료일자(yyyy):").append(rezvEndYmd).append('\n'); }
		if(rezvEndTime!=null) { if(tab!=null) builder.append(tab); builder.append("rezvEndTime(예약종료시간(HH24:MI)):").append(rezvEndTime).append('\n'); }
		if(discrNm!=null) { if(tab!=null) builder.append(tab); builder.append("discrNm(심의자명):").append(discrNm).append('\n'); }
		if(rowIndex!=null) { if(tab!=null) builder.append(tab); builder.append("rowIndex(정렬순서):").append(rowIndex).append('\n'); }
		if(rowCnt!=null) { if(tab!=null) builder.append(tab); builder.append("rowCnt(중복수):").append(rowCnt).append('\n'); }
		if(listType!=null) { if(tab!=null) builder.append(tab); builder.append("listType(목록구분):").append(listType).append('\n'); }
		if(strtTime!=null) { if(tab!=null) builder.append(tab); builder.append("strtTime(하루 시작시간):").append(strtTime).append('\n'); }
		if(endTime!=null) { if(tab!=null) builder.append(tab); builder.append("endTime(하루 종료시간):").append(endTime).append('\n'); }
		if(timeCnt!=null) { if(tab!=null) builder.append(tab); builder.append("timeCnt(시간차이 (30분)):").append(timeCnt).append('\n'); }
		if(rezvDt!=null) { if(tab!=null) builder.append(tab); builder.append("rezvDt(예약일):").append(rezvDt).append('\n'); }
		if(schRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("schRegrUid(예약자ID):").append(schRegrUid).append('\n'); }
		if(withLob) { if(tab!=null) builder.append(tab); builder.append("withLob(LOB 데이터 조회 여부):").append(withLob).append('\n'); }
		super.toString(builder, tab);
	}
}