package com.innobiz.orange.web.wd.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 연차기본(WD_ANB_B) 테이블 VO
 */
public class WdAnbBVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6199360947131766947L;

	/** 기준년도 - KEY */
	private String year;

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	private String anbTypCd;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 회사ID */
	private String compId;

	/** 이월수 */
	private String forwCnt;

	/** 이월차감수 */
	private String forwModCnt;

	/** 발생수 */
	private String creCnt;

	/** 발생차감수 */
	private String creModCnt;

	/** 사용수 */
	private String useCnt;

	/** 사용차감수 */
	private String useModCnt;

	/** 진행수 */
	private String ongoCnt;

	/** 진행차감수 */
	private String ongoModCnt;

	/** 발생년도 */
	private String creYear;


	// 추가컬럼
	/** 원직자UID 목록 */
	private List<String> odurUidList;

	/** 기준년도 - KEY */
	public String getYear() {
		return year;
	}

	/** 기준년도 - KEY */
	public void setYear(String year) {
		this.year = year;
	}

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	public String getAnbTypCd() {
		return anbTypCd;
	}

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	public void setAnbTypCd(String anbTypCd) {
		this.anbTypCd = anbTypCd;
	}

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 이월수 */
	public String getForwCnt() {
		return forwCnt;
	}

	/** 이월수 */
	public void setForwCnt(String forwCnt) {
		this.forwCnt = forwCnt;
	}

	/** 이월차감수 */
	public String getForwModCnt() {
		return forwModCnt;
	}

	/** 이월차감수 */
	public void setForwModCnt(String forwModCnt) {
		this.forwModCnt = forwModCnt;
	}

	/** 발생수 */
	public String getCreCnt() {
		return creCnt;
	}

	/** 발생수 */
	public void setCreCnt(String creCnt) {
		this.creCnt = creCnt;
	}

	/** 발생차감수 */
	public String getCreModCnt() {
		return creModCnt;
	}

	/** 발생차감수 */
	public void setCreModCnt(String creModCnt) {
		this.creModCnt = creModCnt;
	}

	/** 사용수 */
	public String getUseCnt() {
		return useCnt;
	}

	/** 사용수 */
	public void setUseCnt(String useCnt) {
		this.useCnt = useCnt;
	}

	/** 사용차감수 */
	public String getUseModCnt() {
		return useModCnt;
	}

	/** 사용차감수 */
	public void setUseModCnt(String useModCnt) {
		this.useModCnt = useModCnt;
	}

	/** 진행수 */
	public String getOngoCnt() {
		return ongoCnt;
	}

	/** 진행수 */
	public void setOngoCnt(String ongoCnt) {
		this.ongoCnt = ongoCnt;
	}

	/** 진행차감수 */
	public String getOngoModCnt() {
		return ongoModCnt;
	}

	/** 진행차감수 */
	public void setOngoModCnt(String ongoModCnt) {
		this.ongoModCnt = ongoModCnt;
	}

	/** 발생년도 */
	public String getCreYear() {
		return creYear;
	}

	/** 발생년도 */
	public void setCreYear(String creYear) {
		this.creYear = creYear;
	}

	/** 원직자UID 목록 */
	public List<String> getOdurUidList() {
		return odurUidList;
	}

	/** 원직자UID 목록 */
	public void setOdurUidList(List<String> odurUidList) {
		this.odurUidList = odurUidList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbBDao.selectWdAnbB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbBDao.insertWdAnbB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbBDao.updateWdAnbB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbBDao.deleteWdAnbB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbBDao.countWdAnbB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":연차기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(year!=null) { if(tab!=null) builder.append(tab); builder.append("year(기준년도-PK):").append(year).append('\n'); }
		if(anbTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("anbTypCd(연차구분코드-PK):").append(anbTypCd).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(forwCnt!=null) { if(tab!=null) builder.append(tab); builder.append("forwCnt(이월수):").append(forwCnt).append('\n'); }
		if(forwModCnt!=null) { if(tab!=null) builder.append(tab); builder.append("forwModCnt(이월차감수):").append(forwModCnt).append('\n'); }
		if(creCnt!=null) { if(tab!=null) builder.append(tab); builder.append("creCnt(발생수):").append(creCnt).append('\n'); }
		if(creModCnt!=null) { if(tab!=null) builder.append(tab); builder.append("creModCnt(발생차감수):").append(creModCnt).append('\n'); }
		if(useCnt!=null) { if(tab!=null) builder.append(tab); builder.append("useCnt(사용수):").append(useCnt).append('\n'); }
		if(useModCnt!=null) { if(tab!=null) builder.append(tab); builder.append("useModCnt(사용차감수):").append(useModCnt).append('\n'); }
		if(ongoCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ongoCnt(진행수):").append(ongoCnt).append('\n'); }
		if(ongoModCnt!=null) { if(tab!=null) builder.append(tab); builder.append("ongoModCnt(진행차감수):").append(ongoModCnt).append('\n'); }
		if(creYear!=null) { if(tab!=null) builder.append(tab); builder.append("creYear(발생년도):").append(creYear).append('\n'); }
		if(odurUidList!=null) { if(tab!=null) builder.append(tab); builder.append("odurUidList(원직자UID 목록):"); appendStringListTo(builder, odurUidList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
}
