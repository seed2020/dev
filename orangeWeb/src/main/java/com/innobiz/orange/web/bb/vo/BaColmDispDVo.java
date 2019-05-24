package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 컬럼표시여부(BA_COLM_DISP_D) 테이블 VO
 */
public class BaColmDispDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -3285840383264112523L;

	/** 게시판ID */
	private String brdId;

	/** 컬럼ID */
	private String colmId;

	/** 사용여부 */
	private String useYn;

	/** 등록표시여부 */
	private String regDispYn;

	/** 수정표시여부 */
	private String modDispYn;

	/** 조회표시여부 */
	private String readDispYn;

	/** 목록표시여부 */
	private String listDispYn;

	/** 목록표시순서 */
	private Integer listDispOrdr;
	
	/** 넓이퍼센트 */ 
	private String wdthPerc;

 	/** 줄맞춤값 */ 
	private String alnVa;

 	/** 정렬옵션값 */ 
	private String sortOptVa;

 	/** 데이터정렬값 */ 
	private String dataSortVa;
	
	/** 조회표시순서 */
	private Integer readDispOrdr;
	
	/** 병합넓이값 */ 
	private String colsWdthVa;
	
	/** 모바일(대표) 컬럼여부 */ 
	private String titlColYn;
	
	/** 필수여부 */ 
	private String mandatoryYn;
	
	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	// 추가 컬럼

	/** 테이블컬럼 VO */
	private BaTblColmDVo colmVo;
	
	/** 속성 ID  */
	private String atrbId;

	/** 게시판ID */
	public String getBrdId() {
		return brdId;
	}

	/** 게시판ID */
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}

	/** 컬럼ID */
	public String getColmId() {
		return colmId;
	}

	/** 컬럼ID */
	public void setColmId(String colmId) {
		this.colmId = colmId;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 등록표시여부 */
	public String getRegDispYn() {
		return regDispYn;
	}

	/** 등록표시여부 */
	public void setRegDispYn(String regDispYn) {
		this.regDispYn = regDispYn;
	}

	/** 수정표시여부 */
	public String getModDispYn() {
		return modDispYn;
	}

	/** 수정표시여부 */
	public void setModDispYn(String modDispYn) {
		this.modDispYn = modDispYn;
	}

	/** 조회표시여부 */
	public String getReadDispYn() {
		return readDispYn;
	}

	/** 조회표시여부 */
	public void setReadDispYn(String readDispYn) {
		this.readDispYn = readDispYn;
	}

	/** 목록표시여부 */
	public String getListDispYn() {
		return listDispYn;
	}

	/** 목록표시여부 */
	public void setListDispYn(String listDispYn) {
		this.listDispYn = listDispYn;
	}

	/** 목록표시순서 */
	public Integer getListDispOrdr() {
		return listDispOrdr;
	}

	/** 목록표시순서 */
	public void setListDispOrdr(Integer listDispOrdr) {
		this.listDispOrdr = listDispOrdr;
	}
	
	public void setWdthPerc(String wdthPerc) { 
		this.wdthPerc = wdthPerc;
	}
	/** 넓이퍼센트 */ 
	public String getWdthPerc() { 
		return wdthPerc;
	}

	public void setAlnVa(String alnVa) { 
		this.alnVa = alnVa;
	}
	/** 줄맞춤값 */ 
	public String getAlnVa() { 
		return alnVa;
	}

	public void setSortOptVa(String sortOptVa) { 
		this.sortOptVa = sortOptVa;
	}
	/** 정렬옵션값 */ 
	public String getSortOptVa() { 
		return sortOptVa;
	}

	public void setDataSortVa(String dataSortVa) { 
		this.dataSortVa = dataSortVa;
	}
	/** 데이터정렬값 */ 
	public String getDataSortVa() { 
		return dataSortVa;
	}
	
	/** 조회표시순서 */
	public Integer getReadDispOrdr() {
		return readDispOrdr;
	}

	public void setReadDispOrdr(Integer readDispOrdr) {
		this.readDispOrdr = readDispOrdr;
	}

	/** 병합넓이값 */ 
	public String getColsWdthVa() {
		return colsWdthVa;
	}

	public void setColsWdthVa(String colsWdthVa) {
		this.colsWdthVa = colsWdthVa;
	}
	
	/** 대표 컬럼여부 */ 
	public String getTitlColYn() {
		return titlColYn;
	}

	public void setTitlColYn(String titlColYn) {
		this.titlColYn = titlColYn;
	}
	
	/** 필수여부 */ 
	public String getMandatoryYn() {
		return mandatoryYn;
	}

	public void setMandatoryYn(String mandatoryYn) {
		this.mandatoryYn = mandatoryYn;
	}

	/** 등록자 */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자 */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 수정자 */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자 */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 테이블컬럼 VO */
	public BaTblColmDVo getColmVo() {
		return colmVo;
	}

	/** 테이블컬럼 VO */
	public void setColmVo(BaTblColmDVo colmVo) {
		this.colmVo = colmVo;
	}
	
	/** 속성 ID  */
	public String getAtrbId() {
		return atrbId;
	}

	public void setAtrbId(String atrbId) {
		this.atrbId = atrbId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaColmDispDDao.selectBaColmDispD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaColmDispDDao.insertBaColmDispD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaColmDispDDao.updateBaColmDispD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaColmDispDDao.deleteBaColmDispD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaColmDispDDao.countBaColmDispD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":컬럼표시여부]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, sb에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (brdId != null) { if (tab != null) sb.append(tab); sb.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if (colmId != null) { if (tab != null) sb.append(tab); sb.append("colmId(컬럼ID):").append(colmId).append('\n'); }
		if (useYn != null) { if (tab != null) sb.append(tab); sb.append("useYn(사용여부):").append(useYn).append('\n'); }
		if (regDispYn != null) { if (tab != null) sb.append(tab); sb.append("regDispYn(등록표시여부):").append(regDispYn).append('\n'); }
		if (modDispYn != null) { if (tab != null) sb.append(tab); sb.append("modDispYn(수정표시여부):").append(modDispYn).append('\n'); }
		if (readDispYn != null) { if (tab != null) sb.append(tab); sb.append("readDispYn(조회표시여부):").append(readDispYn).append('\n'); }
		if (listDispYn != null) { if (tab != null) sb.append(tab); sb.append("listDispYn(목록표시여부):").append(listDispYn).append('\n'); }
		if (listDispOrdr != null) { if (tab != null) sb.append(tab); sb.append("listDispOrdr(목록표시순서):").append(listDispOrdr).append('\n'); }
		if(wdthPerc!=null) { if(tab!=null) sb.append(tab); sb.append("wdthPerc(넓이퍼센트):").append(wdthPerc).append('\n'); }
		if(alnVa!=null) { if(tab!=null) sb.append(tab); sb.append("alnVa(줄맞춤값):").append(alnVa).append('\n'); }
		if(sortOptVa!=null) { if(tab!=null) sb.append(tab); sb.append("sortOptVa(정렬옵션값):").append(sortOptVa).append('\n'); }
		if(dataSortVa!=null) { if(tab!=null) sb.append(tab); sb.append("dataSortVa(데이터정렬값):").append(dataSortVa).append('\n'); }
		if(readDispOrdr!=null) { if(tab!=null) sb.append(tab); sb.append("readDispOrdr(조회표시순서):").append(readDispOrdr).append('\n'); }
		if(colsWdthVa!=null) { if(tab!=null) sb.append(tab); sb.append("colsWdthVa(병합넒이값):").append(colsWdthVa).append('\n'); }
		if(titlColYn!=null) { if(tab!=null) sb.append(tab); sb.append("titlColYn(모바일 대표 컬럼여부):").append(titlColYn).append('\n'); }
		if(mandatoryYn!=null) { if(tab!=null) sb.append(tab); sb.append("mandatoryYn(필수여부):").append(mandatoryYn).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(sb, tab);
	}
}