package com.innobiz.orange.web.wf.vo;

import java.util.List;


/**
 * 이관 VO
 */
public class WfxTranVo extends WfCmWorksVo {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6411383965272794612L;

	/** 생성ID */
	private String genId;

	/** 회사ID */
	private String compId;

	/** 갯수 */
	private String cnt;

	/** ROWNUM SQL */
	private String rnumSql;

	/** 대상 테이블 */
	private String tgtTbl;

	/** 원본 테이블 */
	private String srcTbl;
	
	/** 서브 테이블 */
	private String subTbl;
	
	/** 인덱스명 */
	private String idxNm;

	/** 키 컬럼 */
	private String keyCol;
	
	/** 서브 키 컬럼 */
	private String subKeyCol;

	/** SQL 일련번호 */
	private Integer sqlSeq;
	
	/** 컬럼목록 */
	private List<WfFormColmLVo> colmVoList;
	
	/** 컬럼조합목록[insert select] */
	private List<WfFormColmLVo> mergeColmVoList;
	
	public WfxTranVo(String formNo) {
		super(formNo);
	}
	
	/** 이관ID */
	public String getGenId() {
		return genId;
	}

	/** 이관ID */
	public void setGenId(String genId) {
		this.genId = genId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
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
	
	/** 서브 테이블 */
	public String getSubTbl() {
		return subTbl;
	}

	public void setSubTbl(String subTbl) {
		this.subTbl = subTbl;
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
	
	/** 서브 키 컬럼 */
	public String getSubKeyCol() {
		return subKeyCol;
	}

	public void setSubKeyCol(String subKeyCol) {
		this.subKeyCol = subKeyCol;
	}

	/** SQL 일련번호 */
	public Integer getSqlSeq() {
		return sqlSeq;
	}

	/** SQL 일련번호 */
	public void setSqlSeq(Integer sqlSeq) {
		this.sqlSeq = sqlSeq;
	}
	
	/** 컬럼목록 */
	public List<WfFormColmLVo> getColmVoList() {
		return colmVoList;
	}
	public void setColmVoList(List<WfFormColmLVo> colmVoList) {
		this.colmVoList = colmVoList;
	}
	
	/** 컬럼조합목록[insert select] */
	public List<WfFormColmLVo> getMergeColmVoList() {
		return mergeColmVoList;
	}
	public void setMergeColmVoList(List<WfFormColmLVo> mergeColmVoList) {
		this.mergeColmVoList = mergeColmVoList;
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
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(cnt!=null) { if(tab!=null) builder.append(tab); builder.append("cnt(갯수):").append(cnt).append('\n'); }
		if(rnumSql!=null) { if(tab!=null) builder.append(tab); builder.append("rnumSql(ROWNUM SQL):").append(rnumSql).append('\n'); }
		if(tgtTbl!=null) { if(tab!=null) builder.append(tab); builder.append("tgtTbl(대상 테이블):").append(tgtTbl).append('\n'); }
		if(srcTbl!=null) { if(tab!=null) builder.append(tab); builder.append("srcTbl(원본 테이블):").append(srcTbl).append('\n'); }
		if(subTbl!=null) { if(tab!=null) builder.append(tab); builder.append("subTbl(서브 테이블):").append(subTbl).append('\n'); }
		if(idxNm!=null) { if(tab!=null) builder.append(tab); builder.append("idxNm(인덱스명):").append(idxNm).append('\n'); }
		if(keyCol!=null) { if(tab!=null) builder.append(tab); builder.append("keyCol(키 컬럼):").append(keyCol).append('\n'); }
		if(subKeyCol!=null) { if(tab!=null) builder.append(tab); builder.append("subKeyCol(서브 키 컬럼):").append(subKeyCol).append('\n'); }
		if(sqlSeq!=null) { if(tab!=null) builder.append(tab); builder.append("sqlSeq(SQL 일련번호):").append(sqlSeq).append('\n'); }
		super.toString(builder, tab);
	}
}
