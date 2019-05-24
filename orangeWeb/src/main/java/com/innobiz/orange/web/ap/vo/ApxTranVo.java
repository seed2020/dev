package com.innobiz.orange.web.ap.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 이관 VO
 */
public class ApxTranVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7008985710248588305L;

	/** 이관ID */
	private String tranId;

	/** 회사ID */
	private String compId;

	/** 저장소ID */
	private String storId;

	/** 등록대장이관기준일 */
	private String regRecLstBaseDt;

	/** 접수대장이관기준일 */
	private String recvRecLstBaseDt;

	/** 대장구분코드 */
	private String recLstTypCd;

	/** 양식ID 목록 */
	private List<String> formIdList;

	/** 이관데이터구분코드 - doc:문서, trx:시행변환, paper:종이문서 */
	private String tranDataTypCd;

	/** 갯수 */
	private String cnt;

	/** ROWNUM SQL */
	private String rnumSql;

	/** 대상 테이블 */
	private String tgtTbl;

	/** 원본 테이블 */
	private String srcTbl;

	/** 인덱스명 */
	private String idxNm;

	/** 키 컬럼 */
	private String keyCol;

	/** SQL 일련번호 */
	private Integer sqlSeq;

	/** 이관ID */
	public String getTranId() {
		return tranId;
	}

	/** 이관ID */
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 저장소ID */
	public String getStorId() {
		return storId;
	}

	/** 저장소ID */
	public void setStorId(String storId) {
		this.storId = storId;
	}

	/** 등록대장이관기준일 */
	public String getRegRecLstBaseDt() {
		return regRecLstBaseDt;
	}

	/** 등록대장이관기준일 */
	public void setRegRecLstBaseDt(String regRecLstBaseDt) {
		this.regRecLstBaseDt = regRecLstBaseDt;
	}

	/** 접수대장이관기준일 */
	public String getRecvRecLstBaseDt() {
		return recvRecLstBaseDt;
	}

	/** 접수대장이관기준일 */
	public void setRecvRecLstBaseDt(String recvRecLstBaseDt) {
		this.recvRecLstBaseDt = recvRecLstBaseDt;
	}

	/** 대장구분코드 */
	public String getRecLstTypCd() {
		return recLstTypCd;
	}

	/** 대장구분코드 */
	public void setRecLstTypCd(String recLstTypCd) {
		this.recLstTypCd = recLstTypCd;
	}

	/** 양식ID 목록 */
	public List<String> getFormIdList() {
		return formIdList;
	}

	/** 양식ID 목록 */
	public void setFormIdList(List<String> formIdList) {
		this.formIdList = formIdList;
	}

	/** 이관데이터구분코드 - doc:문서, trx:시행변환, paper:종이문서 */
	public String getTranDataTypCd() {
		return tranDataTypCd;
	}

	/** 이관데이터구분코드 - doc:문서, trx:시행변환, paper:종이문서 */
	public void setTranDataTypCd(String tranDataTypCd) {
		this.tranDataTypCd = tranDataTypCd;
	}

	/** 갯수 */
	public String getCnt() {
		return cnt;
	}

	/** 갯수 */
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	/** ROWNUM SQL */
	public String getRnumSql() {
		return rnumSql;
	}

	/** ROWNUM SQL */
	public void setRnumSql(String rnumSql) {
		this.rnumSql = rnumSql;
	}

	/** 대상 테이블 */
	public String getTgtTbl() {
		return tgtTbl;
	}

	/** 대상 테이블 */
	public void setTgtTbl(String tgtTbl) {
		this.tgtTbl = tgtTbl;
	}

	/** 원본 테이블 */
	public String getSrcTbl() {
		return srcTbl;
	}

	/** 원본 테이블 */
	public void setSrcTbl(String srcTbl) {
		this.srcTbl = srcTbl;
	}

	/** 인덱스명 */
	public String getIdxNm() {
		return idxNm;
	}

	/** 인덱스명 */
	public void setIdxNm(String idxNm) {
		this.idxNm = idxNm;
	}

	/** 키 컬럼 */
	public String getKeyCol() {
		return keyCol;
	}

	/** 키 컬럼 */
	public void setKeyCol(String keyCol) {
		this.keyCol = keyCol;
	}

	/** SQL 일련번호 */
	public Integer getSqlSeq() {
		return sqlSeq;
	}

	/** SQL 일련번호 */
	public void setSqlSeq(Integer sqlSeq) {
		this.sqlSeq = sqlSeq;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이관 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID):").append(tranId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(regRecLstBaseDt!=null) { if(tab!=null) builder.append(tab); builder.append("regRecLstBaseDt(등록대장이관기준일):").append(regRecLstBaseDt).append('\n'); }
		if(recvRecLstBaseDt!=null) { if(tab!=null) builder.append(tab); builder.append("recvRecLstBaseDt(접수대장이관기준일):").append(recvRecLstBaseDt).append('\n'); }
		if(recLstTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("recLstTypCd(대장구분코드):").append(recLstTypCd).append('\n'); }
		if(formIdList!=null) { if(tab!=null) builder.append(tab); builder.append("formIdList(양식ID 목록):"); appendStringListTo(builder, formIdList, tab); builder.append('\n'); }
		if(tranDataTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("tranDataTypCd(이관데이터구분코드):").append(tranDataTypCd).append('\n'); }
		if(cnt!=null) { if(tab!=null) builder.append(tab); builder.append("cnt(갯수):").append(cnt).append('\n'); }
		if(rnumSql!=null) { if(tab!=null) builder.append(tab); builder.append("rnumSql(ROWNUM SQL):").append(rnumSql).append('\n'); }
		if(tgtTbl!=null) { if(tab!=null) builder.append(tab); builder.append("tgtTbl(대상 테이블):").append(tgtTbl).append('\n'); }
		if(srcTbl!=null) { if(tab!=null) builder.append(tab); builder.append("srcTbl(원본 테이블):").append(srcTbl).append('\n'); }
		if(idxNm!=null) { if(tab!=null) builder.append(tab); builder.append("idxNm(인덱스명):").append(idxNm).append('\n'); }
		if(keyCol!=null) { if(tab!=null) builder.append(tab); builder.append("keyCol(키 컬럼):").append(keyCol).append('\n'); }
		if(sqlSeq!=null) { if(tab!=null) builder.append(tab); builder.append("sqlSeq(SQL 일련번호):").append(sqlSeq).append('\n'); }
		super.toString(builder, tab);
	}
}
