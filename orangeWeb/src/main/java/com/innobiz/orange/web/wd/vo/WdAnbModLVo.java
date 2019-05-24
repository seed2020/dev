package com.innobiz.orange.web.wd.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 연차차감내역(WD_ANB_MOD_L) 테이블 VO
 */
public class WdAnbModLVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5113433257016442229L;

	/** 기준년도 - KEY */
	private String year;

	/** 연차구분코드 - KEY - anb:연차, nanb:개정연차, repb:대휴, offb:공가 */
	private String anbTypCd;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 일련번호 - KEY */
	private String seq;

	/** 차감구분코드 - cre:발생, creMod:발생차감, use:사용, useMod:사용차감, forw:이월, forwMod:이월차감, calc:정산, calcMod:정산차감, ongo:결재중, ongoMod:결재중차감 */
	private String modTypCd;

	/** 차감수 */
	private String modCnt;

	/** 사용년월일 */
	private String useYmd;

	/** 월 */
	private String month;

	/** 결재번호 */
	private String apvNo;

	/** 근거 */
	private String rson;

	/** 차감자UID */
	private String modUid;

	/** 차감일시 */
	private String modDt;

	/** 비고 */
	private String note;


	// 추가컬럼
	/** 차감구분코드 목록 */
	private List<String> modTypCdList;

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

	/** 일련번호 - KEY */
	public String getSeq() {
		return seq;
	}

	/** 일련번호 - KEY */
	public void setSeq(String seq) {
		this.seq = seq;
	}

	/** 차감구분코드 - cre:발생, creMod:발생차감, use:사용, useMod:사용차감, forw:이월, forwMod:이월차감, calc:정산, calcMod:정산차감, ongo:결재중, ongoMod:결재중차감 */
	public String getModTypCd() {
		return modTypCd;
	}

	/** 차감구분코드 - cre:발생, creMod:발생차감, use:사용, useMod:사용차감, forw:이월, forwMod:이월차감, calc:정산, calcMod:정산차감, ongo:결재중, ongoMod:결재중차감 */
	public void setModTypCd(String modTypCd) {
		this.modTypCd = modTypCd;
	}

	/** 차감수 */
	public String getModCnt() {
		return modCnt;
	}

	/** 차감수 */
	public void setModCnt(String modCnt) {
		this.modCnt = modCnt;
	}

	/** 사용년월일 */
	public String getUseYmd() {
		return useYmd;
	}

	/** 사용년월일 */
	public void setUseYmd(String useYmd) {
		this.useYmd = useYmd;
	}

	/** 월 */
	public String getMonth() {
		return month;
	}

	/** 월 */
	public void setMonth(String month) {
		this.month = month;
	}

	/** 결재번호 */
	public String getApvNo() {
		return apvNo;
	}

	/** 결재번호 */
	public void setApvNo(String apvNo) {
		this.apvNo = apvNo;
	}

	/** 근거 */
	public String getRson() {
		return rson;
	}

	/** 근거 */
	public void setRson(String rson) {
		this.rson = rson;
	}

	/** 차감자UID */
	public String getModUid() {
		return modUid;
	}

	/** 차감자UID */
	public void setModUid(String modUid) {
		this.modUid = modUid;
	}

	/** 차감일시 */
	public String getModDt() {
		return modDt;
	}

	/** 차감일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 비고 */
	public String getNote() {
		return note;
	}

	/** 비고 */
	public void setNote(String note) {
		this.note = note;
	}

	/** 차감구분코드 목록 */
	public List<String> getModTypCdList() {
		return modTypCdList;
	}

	/** 차감구분코드 목록 */
	public void setModTypCdList(List<String> modTypCdList) {
		this.modTypCdList = modTypCdList;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbModLDao.selectWdAnbModL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbModLDao.insertWdAnbModL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbModLDao.updateWdAnbModL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbModLDao.deleteWdAnbModL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wd.dao.WdAnbModLDao.countWdAnbModL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":연차차감내역]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(year!=null) { if(tab!=null) builder.append(tab); builder.append("year(기준년도-PK):").append(year).append('\n'); }
		if(anbTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("anbTypCd(연차구분코드-PK):").append(anbTypCd).append('\n'); }
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(일련번호-PK):").append(seq).append('\n'); }
		if(modTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("modTypCd(차감구분코드):").append(modTypCd).append('\n'); }
		if(modCnt!=null) { if(tab!=null) builder.append(tab); builder.append("modCnt(차감수):").append(modCnt).append('\n'); }
		if(useYmd!=null) { if(tab!=null) builder.append(tab); builder.append("useYmd(사용년월일):").append(useYmd).append('\n'); }
		if(month!=null) { if(tab!=null) builder.append(tab); builder.append("month(월):").append(month).append('\n'); }
		if(apvNo!=null) { if(tab!=null) builder.append(tab); builder.append("apvNo(결재번호):").append(apvNo).append('\n'); }
		if(rson!=null) { if(tab!=null) builder.append(tab); builder.append("rson(근거):").append(rson).append('\n'); }
		if(modUid!=null) { if(tab!=null) builder.append(tab); builder.append("modUid(차감자UID):").append(modUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(차감일시):").append(modDt).append('\n'); }
		if(note!=null) { if(tab!=null) builder.append(tab); builder.append("note(비고):").append(note).append('\n'); }
		if(modTypCdList!=null) { if(tab!=null) builder.append(tab); builder.append("modTypCdList(차감구분코드 목록):"); appendStringListTo(builder, modTypCdList, tab); builder.append('\n'); }
		super.toString(builder, tab);
	}
	
	public boolean isModified(){
		return modTypCd!=null && modTypCd.indexOf("Mod")>0;
	}
}
