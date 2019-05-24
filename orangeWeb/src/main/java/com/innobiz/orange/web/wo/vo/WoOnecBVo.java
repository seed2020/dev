package com.innobiz.orange.web.wo.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 원카드기본(WO_ONEC_B) 테이블 VO
 */
public class WoOnecBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 4207817283702319219L;

	/** 원카드번호 - KEY */
	private String onecNo;

	/** 버전 */
	private String ver;

	/** 원카드구분코드 */
	private String onecTypCd;

	/** 주기코드 */
	private String freqCd;

	/** 품목명 */
	private String itemNm;

	/** 최초작성일 */
	private String issDt;

	/** 개정일 */
	private String revsDt;

	/** 개발완료일 */
	private String dueDt;

	/** 홀더UID */
	private String holdrUid;

	/** 브랜드명 */
	private String brndNm;

	/** 카테고리코드 */
	private String catCd;

	/** 출처코드 */
	private String orgnCd;

	/** 시장상황 */
	private String mketSitu;

	/** 고객분석 */
	private String custAnal;

	/** 경쟁사분석 */
	private String cmptrAnal;

	/** 벤치마크품목 */
	private String bmItem;

	/** 벤치마크소비자가 */
	private String bmRetPrc;

	/** 벤치마크판매가 */
	private String bmSalsPrc;

	/** 벤치마크SP */
	private String bmSp;

	/** 베치마크스펙사이즈 */
	private String bmSpecSize;

	/** 차이점 */
	private String diff;

	/** 상세 */
	private String spec;

	/** 대상고객 */
	private String tgtCust;

	/** 포장 */
	private String pack;

	/** 저장형태코드 */
	private String storgCd;

	/** 포지션자사X */
	private String posMyX;

	/** 포지션자사Y */
	private String posMyY;

	/** 포지션타사X */
	private String posOthrX;

	/** 포지션타사Y */
	private String posOthrY;

	/** 포지션목표가 */
	private String posTgtPrc;

	/** 포지션원가 */
	private String posCost;

	/** 포지션SP */
	private String posSp;

	/** 포지션비고 */
	private String posRemk;

	/** TA승인여부 */
	private String taApvYn;

	/** 판매예측3개월볼륨 */
	private String sf3mVo;

	/** 판매예측6개월볼륨 */
	private String sf6mVo;

	/** 판매예측1년볼륨 */
	private String sf1yVo;

	/** 판매예측2년볼륨 */
	private String sf2yVo;

	/** 판매예측3년볼륨 */
	private String sf3yVo;

	/** 판매예측3개월판매 */
	private String sf3mSail;

	/** 판매예측6개월판매 */
	private String sf6mSail;

	/** 판매예측1년월판매 */
	private String sf1ySail;

	/** 판매예측2년판매 */
	private String sf2ySail;

	/** 판매예측3년판매 */
	private String sf3ySail;

	/** 판매예측비고 */
	private String sfRemk;

	/** 프로모션계획 */
	private String promPlan;

	/** 판매활동 */
	private String sailPerfm;

	/** 비고 */
	private String note;

	/** 부서ID */
	private String deptId;

	/** 승인된이력번호 */
	private String apvdHisNo;

	/** 현재버전 */
	private String curVer;

	/** 상태코드 */
	private String statCd;

	/** 등록일시 */
	private String regDt;

	/** 결재번호 */
	private String apvNo;

	/** 결재요청일시 */
	private String apvAskDt;

	/** 결재완료일시 */
	private String apvCmplDt;


	// 추가컬럼
	/** 테이블타입 */
	private String tableType = null;

	/** 상태코드 목록 */
	private List<String> statCdList;

	/** 사용자UID */
	private String userUid;

	/** 분기일 */
	private String quarterDt;

	/** 반기일 */
	private String halfDt;

	/** 년간일 */
	private String yearDt;

	/** 이전홀더UID */
	private String oldHoldrUid;

	/** 홀더명 */
	private String holdrNm;

	/** 원카드번호 - KEY */
	public String getOnecNo() {
		return onecNo;
	}

	/** 원카드번호 - KEY */
	public void setOnecNo(String onecNo) {
		this.onecNo = onecNo;
	}

	/** 버전 */
	public String getVer() {
		return ver;
	}

	/** 버전 */
	public void setVer(String ver) {
		this.ver = ver;
	}

	/** 원카드구분코드 */
	public String getOnecTypCd() {
		return onecTypCd;
	}

	/** 원카드구분코드 */
	public void setOnecTypCd(String onecTypCd) {
		this.onecTypCd = onecTypCd;
	}

	/** 주기코드 */
	public String getFreqCd() {
		return freqCd;
	}

	/** 주기코드 */
	public void setFreqCd(String freqCd) {
		this.freqCd = freqCd;
	}

	/** 품목명 */
	public String getItemNm() {
		return itemNm;
	}

	/** 품목명 */
	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}

	/** 최초작성일 */
	public String getIssDt() {
		return issDt;
	}

	/** 최초작성일 */
	public void setIssDt(String issDt) {
		this.issDt = issDt;
	}

	/** 개정일 */
	public String getRevsDt() {
		return revsDt;
	}

	/** 개정일 */
	public void setRevsDt(String revsDt) {
		this.revsDt = revsDt;
	}

	/** 개발완료일 */
	public String getDueDt() {
		return dueDt;
	}

	/** 개발완료일 */
	public void setDueDt(String dueDt) {
		this.dueDt = dueDt;
	}

	/** 홀더UID */
	public String getHoldrUid() {
		return holdrUid;
	}

	/** 홀더UID */
	public void setHoldrUid(String holdrUid) {
		this.holdrUid = holdrUid;
	}

	/** 브랜드명 */
	public String getBrndNm() {
		return brndNm;
	}

	/** 브랜드명 */
	public void setBrndNm(String brndNm) {
		this.brndNm = brndNm;
	}

	/** 카테고리코드 */
	public String getCatCd() {
		return catCd;
	}

	/** 카테고리코드 */
	public void setCatCd(String catCd) {
		this.catCd = catCd;
	}

	/** 출처코드 */
	public String getOrgnCd() {
		return orgnCd;
	}

	/** 출처코드 */
	public void setOrgnCd(String orgnCd) {
		this.orgnCd = orgnCd;
	}

	/** 시장상황 */
	public String getMketSitu() {
		return mketSitu;
	}

	/** 시장상황 */
	public void setMketSitu(String mketSitu) {
		this.mketSitu = mketSitu;
	}

	/** 고객분석 */
	public String getCustAnal() {
		return custAnal;
	}

	/** 고객분석 */
	public void setCustAnal(String custAnal) {
		this.custAnal = custAnal;
	}

	/** 경쟁사분석 */
	public String getCmptrAnal() {
		return cmptrAnal;
	}

	/** 경쟁사분석 */
	public void setCmptrAnal(String cmptrAnal) {
		this.cmptrAnal = cmptrAnal;
	}

	/** 벤치마크품목 */
	public String getBmItem() {
		return bmItem;
	}

	/** 벤치마크품목 */
	public void setBmItem(String bmItem) {
		this.bmItem = bmItem;
	}

	/** 벤치마크소비자가 */
	public String getBmRetPrc() {
		return bmRetPrc;
	}

	/** 벤치마크소비자가 */
	public void setBmRetPrc(String bmRetPrc) {
		this.bmRetPrc = bmRetPrc;
	}

	/** 벤치마크판매가 */
	public String getBmSalsPrc() {
		return bmSalsPrc;
	}

	/** 벤치마크판매가 */
	public void setBmSalsPrc(String bmSalsPrc) {
		this.bmSalsPrc = bmSalsPrc;
	}

	/** 벤치마크SP */
	public String getBmSp() {
		return bmSp;
	}

	/** 벤치마크SP */
	public void setBmSp(String bmSp) {
		this.bmSp = bmSp;
	}

	/** 베치마크스펙사이즈 */
	public String getBmSpecSize() {
		return bmSpecSize;
	}

	/** 베치마크스펙사이즈 */
	public void setBmSpecSize(String bmSpecSize) {
		this.bmSpecSize = bmSpecSize;
	}

	/** 차이점 */
	public String getDiff() {
		return diff;
	}

	/** 차이점 */
	public void setDiff(String diff) {
		this.diff = diff;
	}

	/** 상세 */
	public String getSpec() {
		return spec;
	}

	/** 상세 */
	public void setSpec(String spec) {
		this.spec = spec;
	}

	/** 대상고객 */
	public String getTgtCust() {
		return tgtCust;
	}

	/** 대상고객 */
	public void setTgtCust(String tgtCust) {
		this.tgtCust = tgtCust;
	}

	/** 포장 */
	public String getPack() {
		return pack;
	}

	/** 포장 */
	public void setPack(String pack) {
		this.pack = pack;
	}

	/** 저장형태코드 */
	public String getStorgCd() {
		return storgCd;
	}

	/** 저장형태코드 */
	public void setStorgCd(String storgCd) {
		this.storgCd = storgCd;
	}

	/** 포지션자사X */
	public String getPosMyX() {
		return posMyX;
	}

	/** 포지션자사X */
	public void setPosMyX(String posMyX) {
		this.posMyX = posMyX;
	}

	/** 포지션자사Y */
	public String getPosMyY() {
		return posMyY;
	}

	/** 포지션자사Y */
	public void setPosMyY(String posMyY) {
		this.posMyY = posMyY;
	}

	/** 포지션타사X */
	public String getPosOthrX() {
		return posOthrX;
	}

	/** 포지션타사X */
	public void setPosOthrX(String posOthrX) {
		this.posOthrX = posOthrX;
	}

	/** 포지션타사Y */
	public String getPosOthrY() {
		return posOthrY;
	}

	/** 포지션타사Y */
	public void setPosOthrY(String posOthrY) {
		this.posOthrY = posOthrY;
	}

	/** 포지션목표가 */
	public String getPosTgtPrc() {
		return posTgtPrc;
	}

	/** 포지션목표가 */
	public void setPosTgtPrc(String posTgtPrc) {
		this.posTgtPrc = posTgtPrc;
	}

	/** 포지션원가 */
	public String getPosCost() {
		return posCost;
	}

	/** 포지션원가 */
	public void setPosCost(String posCost) {
		this.posCost = posCost;
	}

	/** 포지션SP */
	public String getPosSp() {
		return posSp;
	}

	/** 포지션SP */
	public void setPosSp(String posSp) {
		this.posSp = posSp;
	}

	/** 포지션비고 */
	public String getPosRemk() {
		return posRemk;
	}

	/** 포지션비고 */
	public void setPosRemk(String posRemk) {
		this.posRemk = posRemk;
	}

	/** TA승인여부 */
	public String getTaApvYn() {
		return taApvYn;
	}

	/** TA승인여부 */
	public void setTaApvYn(String taApvYn) {
		this.taApvYn = taApvYn;
	}

	/** 판매예측3개월볼륨 */
	public String getSf3mVo() {
		return sf3mVo;
	}

	/** 판매예측3개월볼륨 */
	public void setSf3mVo(String sf3mVo) {
		this.sf3mVo = sf3mVo;
	}

	/** 판매예측6개월볼륨 */
	public String getSf6mVo() {
		return sf6mVo;
	}

	/** 판매예측6개월볼륨 */
	public void setSf6mVo(String sf6mVo) {
		this.sf6mVo = sf6mVo;
	}

	/** 판매예측1년볼륨 */
	public String getSf1yVo() {
		return sf1yVo;
	}

	/** 판매예측1년볼륨 */
	public void setSf1yVo(String sf1yVo) {
		this.sf1yVo = sf1yVo;
	}

	/** 판매예측2년볼륨 */
	public String getSf2yVo() {
		return sf2yVo;
	}

	/** 판매예측2년볼륨 */
	public void setSf2yVo(String sf2yVo) {
		this.sf2yVo = sf2yVo;
	}

	/** 판매예측3년볼륨 */
	public String getSf3yVo() {
		return sf3yVo;
	}

	/** 판매예측3년볼륨 */
	public void setSf3yVo(String sf3yVo) {
		this.sf3yVo = sf3yVo;
	}

	/** 판매예측3개월판매 */
	public String getSf3mSail() {
		return sf3mSail;
	}

	/** 판매예측3개월판매 */
	public void setSf3mSail(String sf3mSail) {
		this.sf3mSail = sf3mSail;
	}

	/** 판매예측6개월판매 */
	public String getSf6mSail() {
		return sf6mSail;
	}

	/** 판매예측6개월판매 */
	public void setSf6mSail(String sf6mSail) {
		this.sf6mSail = sf6mSail;
	}

	/** 판매예측1년월판매 */
	public String getSf1ySail() {
		return sf1ySail;
	}

	/** 판매예측1년월판매 */
	public void setSf1ySail(String sf1ySail) {
		this.sf1ySail = sf1ySail;
	}

	/** 판매예측2년판매 */
	public String getSf2ySail() {
		return sf2ySail;
	}

	/** 판매예측2년판매 */
	public void setSf2ySail(String sf2ySail) {
		this.sf2ySail = sf2ySail;
	}

	/** 판매예측3년판매 */
	public String getSf3ySail() {
		return sf3ySail;
	}

	/** 판매예측3년판매 */
	public void setSf3ySail(String sf3ySail) {
		this.sf3ySail = sf3ySail;
	}

	/** 판매예측비고 */
	public String getSfRemk() {
		return sfRemk;
	}

	/** 판매예측비고 */
	public void setSfRemk(String sfRemk) {
		this.sfRemk = sfRemk;
	}

	/** 프로모션계획 */
	public String getPromPlan() {
		return promPlan;
	}

	/** 프로모션계획 */
	public void setPromPlan(String promPlan) {
		this.promPlan = promPlan;
	}

	/** 판매활동 */
	public String getSailPerfm() {
		return sailPerfm;
	}

	/** 판매활동 */
	public void setSailPerfm(String sailPerfm) {
		this.sailPerfm = sailPerfm;
	}

	/** 비고 */
	public String getNote() {
		return note;
	}

	/** 비고 */
	public void setNote(String note) {
		this.note = note;
	}

	/** 부서ID */
	public String getDeptId() {
		return deptId;
	}

	/** 부서ID */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/** 승인된이력번호 */
	public String getApvdHisNo() {
		return apvdHisNo;
	}

	/** 승인된이력번호 */
	public void setApvdHisNo(String apvdHisNo) {
		this.apvdHisNo = apvdHisNo;
	}

	/** 현재버전 */
	public String getCurVer() {
		return curVer;
	}

	/** 현재버전 */
	public void setCurVer(String curVer) {
		this.curVer = curVer;
	}

	/** 상태코드 */
	public String getStatCd() {
		return statCd;
	}

	/** 상태코드 */
	public void setStatCd(String statCd) {
		this.statCd = statCd;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 결재번호 */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 결재요청일시 */
	public String getApvAskDt() {
		return apvAskDt;
	}

	/** 결재요청일시 */
	public void setApvAskDt(String apvAskDt) {
		this.apvAskDt = apvAskDt;
	}

	/** 결재완료일시 */
	public String getApvCmplDt() {
		return apvCmplDt;
	}

	/** 결재완료일시 */
	public void setApvCmplDt(String apvCmplDt) {
		this.apvCmplDt = apvCmplDt;
	}

	/** 테이블 타입 리턴 */
	public String getTableType(){
		return tableType==null ? "B" : "B"+tableType;
	}

	/** 테이블 타입 제거 */
	public void removeTableType(){
		tableType = null;
	}

	/** 히스토리 테이블 설정 */
	public void setHistory(){
		tableType = "H";
	}

	/** 상태코드 목록 */
	public List<String> getStatCdList() {
		return statCdList;
	}

	/** 상태코드 목록 */
	public void setStatCdList(List<String> statCdList) {
		this.statCdList = statCdList;
	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 분기일 */
	public String getQuarterDt() {
		return quarterDt;
	}

	/** 분기일 */
	public void setQuarterDt(String quarterDt) {
		this.quarterDt = quarterDt;
	}

	/** 반기일 */
	public String getHalfDt() {
		return halfDt;
	}

	/** 반기일 */
	public void setHalfDt(String halfDt) {
		this.halfDt = halfDt;
	}

	/** 년간일 */
	public String getYearDt() {
		return yearDt;
	}

	/** 년간일 */
	public void setYearDt(String yearDt) {
		this.yearDt = yearDt;
	}

	/** 이전홀더UID */
	public String getOldHoldrUid() {
		return oldHoldrUid;
	}

	/** 이전홀더UID */
	public void setOldHoldrUid(String oldHoldrUid) {
		this.oldHoldrUid = oldHoldrUid;
	}

	/** 홀더명 */
	public String getHoldrNm() {
		return holdrNm;
	}

	/** 홀더명 */
	public void setHoldrNm(String holdrNm) {
		this.holdrNm = holdrNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecBDao.selectWoOnecB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecBDao.insertWoOnecB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecBDao.updateWoOnecB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecBDao.deleteWoOnecB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wo.dao.WoOnecBDao.countWoOnecB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":원카드기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(onecNo!=null) { if(tab!=null) builder.append(tab); builder.append("onecNo(원카드번호-PK):").append(onecNo).append('\n'); }
		if(ver!=null) { if(tab!=null) builder.append(tab); builder.append("ver(버전):").append(ver).append('\n'); }
		if(onecTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("onecTypCd(원카드구분코드):").append(onecTypCd).append('\n'); }
		if(freqCd!=null) { if(tab!=null) builder.append(tab); builder.append("freqCd(주기코드):").append(freqCd).append('\n'); }
		if(itemNm!=null) { if(tab!=null) builder.append(tab); builder.append("itemNm(품목명):").append(itemNm).append('\n'); }
		if(issDt!=null) { if(tab!=null) builder.append(tab); builder.append("issDt(최초작성일):").append(issDt).append('\n'); }
		if(revsDt!=null) { if(tab!=null) builder.append(tab); builder.append("revsDt(개정일):").append(revsDt).append('\n'); }
		if(dueDt!=null) { if(tab!=null) builder.append(tab); builder.append("dueDt(개발완료일):").append(dueDt).append('\n'); }
		if(holdrUid!=null) { if(tab!=null) builder.append(tab); builder.append("holdrUid(홀더UID):").append(holdrUid).append('\n'); }
		if(brndNm!=null) { if(tab!=null) builder.append(tab); builder.append("brndNm(브랜드명):").append(brndNm).append('\n'); }
		if(catCd!=null) { if(tab!=null) builder.append(tab); builder.append("catCd(카테고리코드):").append(catCd).append('\n'); }
		if(orgnCd!=null) { if(tab!=null) builder.append(tab); builder.append("orgnCd(출처코드):").append(orgnCd).append('\n'); }
		if(mketSitu!=null) { if(tab!=null) builder.append(tab); builder.append("mketSitu(시장상황):").append(mketSitu).append('\n'); }
		if(custAnal!=null) { if(tab!=null) builder.append(tab); builder.append("custAnal(고객분석):").append(custAnal).append('\n'); }
		if(cmptrAnal!=null) { if(tab!=null) builder.append(tab); builder.append("cmptrAnal(경쟁사분석):").append(cmptrAnal).append('\n'); }
		if(bmItem!=null) { if(tab!=null) builder.append(tab); builder.append("bmItem(벤치마크품목):").append(bmItem).append('\n'); }
		if(bmRetPrc!=null) { if(tab!=null) builder.append(tab); builder.append("bmRetPrc(벤치마크소비자가):").append(bmRetPrc).append('\n'); }
		if(bmSalsPrc!=null) { if(tab!=null) builder.append(tab); builder.append("bmSalsPrc(벤치마크판매가):").append(bmSalsPrc).append('\n'); }
		if(bmSp!=null) { if(tab!=null) builder.append(tab); builder.append("bmSp(벤치마크SP):").append(bmSp).append('\n'); }
		if(bmSpecSize!=null) { if(tab!=null) builder.append(tab); builder.append("bmSpecSize(베치마크스펙사이즈):").append(bmSpecSize).append('\n'); }
		if(diff!=null) { if(tab!=null) builder.append(tab); builder.append("diff(차이점):").append(diff).append('\n'); }
		if(spec!=null) { if(tab!=null) builder.append(tab); builder.append("spec(상세):").append(spec).append('\n'); }
		if(tgtCust!=null) { if(tab!=null) builder.append(tab); builder.append("tgtCust(대상고객):").append(tgtCust).append('\n'); }
		if(pack!=null) { if(tab!=null) builder.append(tab); builder.append("pack(포장):").append(pack).append('\n'); }
		if(storgCd!=null) { if(tab!=null) builder.append(tab); builder.append("storgCd(저장형태코드):").append(storgCd).append('\n'); }
		if(posMyX!=null) { if(tab!=null) builder.append(tab); builder.append("posMyX(포지션자사X):").append(posMyX).append('\n'); }
		if(posMyY!=null) { if(tab!=null) builder.append(tab); builder.append("posMyY(포지션자사Y):").append(posMyY).append('\n'); }
		if(posOthrX!=null) { if(tab!=null) builder.append(tab); builder.append("posOthrX(포지션타사X):").append(posOthrX).append('\n'); }
		if(posOthrY!=null) { if(tab!=null) builder.append(tab); builder.append("posOthrY(포지션타사Y):").append(posOthrY).append('\n'); }
		if(posTgtPrc!=null) { if(tab!=null) builder.append(tab); builder.append("posTgtPrc(포지션목표가):").append(posTgtPrc).append('\n'); }
		if(posCost!=null) { if(tab!=null) builder.append(tab); builder.append("posCost(포지션원가):").append(posCost).append('\n'); }
		if(posSp!=null) { if(tab!=null) builder.append(tab); builder.append("posSp(포지션SP):").append(posSp).append('\n'); }
		if(posRemk!=null) { if(tab!=null) builder.append(tab); builder.append("posRemk(포지션비고):").append(posRemk).append('\n'); }
		if(taApvYn!=null) { if(tab!=null) builder.append(tab); builder.append("taApvYn(TA승인여부):").append(taApvYn).append('\n'); }
		if(sf3mVo!=null) { if(tab!=null) builder.append(tab); builder.append("sf3mVo(판매예측3개월볼륨):").append(sf3mVo).append('\n'); }
		if(sf6mVo!=null) { if(tab!=null) builder.append(tab); builder.append("sf6mVo(판매예측6개월볼륨):").append(sf6mVo).append('\n'); }
		if(sf1yVo!=null) { if(tab!=null) builder.append(tab); builder.append("sf1yVo(판매예측1년볼륨):").append(sf1yVo).append('\n'); }
		if(sf2yVo!=null) { if(tab!=null) builder.append(tab); builder.append("sf2yVo(판매예측2년볼륨):").append(sf2yVo).append('\n'); }
		if(sf3yVo!=null) { if(tab!=null) builder.append(tab); builder.append("sf3yVo(판매예측3년볼륨):").append(sf3yVo).append('\n'); }
		if(sf3mSail!=null) { if(tab!=null) builder.append(tab); builder.append("sf3mSail(판매예측3개월판매):").append(sf3mSail).append('\n'); }
		if(sf6mSail!=null) { if(tab!=null) builder.append(tab); builder.append("sf6mSail(판매예측6개월판매):").append(sf6mSail).append('\n'); }
		if(sf1ySail!=null) { if(tab!=null) builder.append(tab); builder.append("sf1ySail(판매예측1년월판매):").append(sf1ySail).append('\n'); }
		if(sf2ySail!=null) { if(tab!=null) builder.append(tab); builder.append("sf2ySail(판매예측2년판매):").append(sf2ySail).append('\n'); }
		if(sf3ySail!=null) { if(tab!=null) builder.append(tab); builder.append("sf3ySail(판매예측3년판매):").append(sf3ySail).append('\n'); }
		if(sfRemk!=null) { if(tab!=null) builder.append(tab); builder.append("sfRemk(판매예측비고):").append(sfRemk).append('\n'); }
		if(promPlan!=null) { if(tab!=null) builder.append(tab); builder.append("promPlan(프로모션계획):").append(promPlan).append('\n'); }
		if(sailPerfm!=null) { if(tab!=null) builder.append(tab); builder.append("sailPerfm(판매활동):").append(sailPerfm).append('\n'); }
		if(note!=null) { if(tab!=null) builder.append(tab); builder.append("note(비고):").append(note).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(apvdHisNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvdHisNo(승인된이력번호):").append(apvdHisNo).append('\n'); }
		if(curVer!=null) { if(tab!=null) builder.append(tab); builder.append("curVer(현재버전):").append(curVer).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호):").append(apvNo).append('\n'); }
		if(apvAskDt!=null) { if(tab!=null) builder.append(tab); builder.append("apvAskDt(결재요청일시):").append(apvAskDt).append('\n'); }
		if(apvCmplDt!=null) { if(tab!=null) builder.append(tab); builder.append("apvCmplDt(결재완료일시):").append(apvCmplDt).append('\n'); }
		if(statCdList!=null) { if(tab!=null) builder.append(tab); builder.append("statCdList(상태코드 목록):"); appendStringListTo(builder, statCdList, tab); builder.append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(quarterDt!=null) { if(tab!=null) builder.append(tab); builder.append("quarterDt(분기일):").append(quarterDt).append('\n'); }
		if(halfDt!=null) { if(tab!=null) builder.append(tab); builder.append("halfDt(반기일):").append(halfDt).append('\n'); }
		if(yearDt!=null) { if(tab!=null) builder.append(tab); builder.append("yearDt(년간일):").append(yearDt).append('\n'); }
		if(oldHoldrUid!=null) { if(tab!=null) builder.append(tab); builder.append("oldHoldrUid(이전홀더UID):").append(oldHoldrUid).append('\n'); }
		if(holdrNm!=null) { if(tab!=null) builder.append(tab); builder.append("holdrNm(홀더명):").append(holdrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
