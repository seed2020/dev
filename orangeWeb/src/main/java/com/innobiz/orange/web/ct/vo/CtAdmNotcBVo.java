package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_ADM_NOTC_B [커뮤니티 게시판 마스터 기본] */
public class CtAdmNotcBVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6385922368319758728L;

	/** 게시물ID */
	private Integer bullId;
	
	/** 회사ID */
	private String compId;
	
	/** 부모게시물ID */
	private Integer bullPid;
	
	/** 답변그룹ID */
	private Integer replyGrpId;
	
	/** 답변순서 */
	private Integer replyOrdr;
	
	/** 답변단계 */
	private Integer replyDpth;
	
	/** 게시물 상태코드 */
	private String bullStatCd;
	
	/** 게시물 예약 여부*/
	private String bullRezvYn;
	
	/** 대상 부서 지정 여부 */
	private String tgtDeptYn;
	
	/** 대상 사용자 지정 여부 */
	private String tgtUserYn;
	
	/** 제목 */
	private String subj;
	
	/** 게시 예약 일시 */
	private String bullRezvDt;
	
	/** 게시 완료 일시 */
	private String bullExprDt;
	
	/** 본문 */
	private String cont;
	
	/** 조회수 */
	private Integer readCnt;
	
	/** 찬성수 */
	private Integer prosCnt;
	
	/** 반대수 */
	private Integer consCnt;
	
	/** 추천수 */
	private Integer recmdCnt;
	
	/** 등록자UID */
	private String regrUid;
	
	/** 등록일시 */
	private String regDt;
	
	/** 수정자UID */
	private String modrUid;
	
	/** 수정일시 */
	private String modDt;

	/** 첨부여부 */
	private String attYn;
	
	//추가 컬럼
	/** 등록자 명 */
	private String regrNm;
	
	/** 수정자 명 */
	private String modrNm;
	
	/** 게시물 테이블 명 */
	private String tableName;
	
	/** 새글여부 */
	private String newYn;
	
	/** 검색조건(유효기간만료) */
	private String schExpr;
	
	/** 만료여부 */
	private String exprYn;
	

	public String getExprYn() {
		return exprYn;
	}

	public void setExprYn(String exprYn) {
		this.exprYn = exprYn;
	}

	public String getSchExpr() {
		return schExpr;
	}

	public void setSchExpr(String schExpr) {
		this.schExpr = schExpr;
	}

	public String getNewYn() {
		return newYn;
	}

	public void setNewYn(String newYn) {
		this.newYn = newYn;
	}

	public String getModrNm() {
		return modrNm;
	}

	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRegrNm() {
		return regrNm;
	}

	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	public Integer getBullId() {
		return bullId;
	}

	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public Integer getBullPid() {
		return bullPid;
	}

	public void setBullPid(Integer bullPid) {
		this.bullPid = bullPid;
	}

	public Integer getReplyGrpId() {
		return replyGrpId;
	}

	public void setReplyGrpId(Integer replyGrpId) {
		this.replyGrpId = replyGrpId;
	}

	public Integer getReplyOrdr() {
		return replyOrdr;
	}

	public void setReplyOrdr(Integer replyOrdr) {
		this.replyOrdr = replyOrdr;
	}

	public Integer getReplyDpth() {
		return replyDpth;
	}

	public void setReplyDpth(Integer replyDpth) {
		this.replyDpth = replyDpth;
	}

	public String getBullStatCd() {
		return bullStatCd;
	}

	public void setBullStatCd(String bullStatCd) {
		this.bullStatCd = bullStatCd;
	}

	public String getBullRezvYn() {
		return bullRezvYn;
	}

	public void setBullRezvYn(String bullRezvYn) {
		this.bullRezvYn = bullRezvYn;
	}

	public String getTgtDeptYn() {
		return tgtDeptYn;
	}

	public void setTgtDeptYn(String tgtDeptYn) {
		this.tgtDeptYn = tgtDeptYn;
	}

	public String getTgtUserYn() {
		return tgtUserYn;
	}

	public void setTgtUserYn(String tgtUserYn) {
		this.tgtUserYn = tgtUserYn;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getBullRezvDt() {
		return bullRezvDt;
	}

	public void setBullRezvDt(String bullRezvDt) {
		if(bullRezvDt != null && bullRezvDt.endsWith(".0") && bullRezvDt.length()>2)
			 bullRezvDt=bullRezvDt.substring(0, bullRezvDt.length()-2);
		this.bullRezvDt = bullRezvDt;
	}

	public String getBullExprDt() {
		return bullExprDt;
	}

	public void setBullExprDt(String bullExprDt) {
		if(bullExprDt != null && bullExprDt.endsWith(".0") && bullExprDt.length()>2)
			 bullExprDt=bullExprDt.substring(0, bullExprDt.length()-2);
		this.bullExprDt = bullExprDt;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public Integer getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}

	public Integer getProsCnt() {
		return prosCnt;
	}

	public void setProsCnt(Integer prosCnt) {
		this.prosCnt = prosCnt;
	}

	public Integer getConsCnt() {
		return consCnt;
	}

	public void setConsCnt(Integer consCnt) {
		this.consCnt = consCnt;
	}

	public Integer getRecmdCnt() {
		return recmdCnt;
	}

	public void setRecmdCnt(Integer recmdCnt) {
		this.recmdCnt = recmdCnt;
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

	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}

	public String getAttYn() {
		return attYn;
	}

	public void setAttYn(String attYn) {
		this.attYn = attYn;
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
		builder.append('[').append(this.getClass().getName()).append(":커뮤니티 게시판 마스터 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(bullId!=null) { if(tab!=null) builder.append(tab); builder.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(bullPid!=null) { if(tab!=null) builder.append(tab); builder.append("bullPid(부모게시물ID):").append(bullPid).append('\n'); }
		if(replyGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("replyGrpId(답변그룹ID):").append(replyGrpId).append('\n'); }
		if(replyOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("replyOrdr(답변순서):").append(replyOrdr).append('\n'); }
		if(replyDpth!=null) { if(tab!=null) builder.append(tab); builder.append("replyDpth(답변단계):").append(replyDpth).append('\n'); }
		if(bullStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("bullStatCd(게시물 상태코드):").append(bullStatCd).append('\n'); }
		if(bullRezvYn!=null) { if(tab!=null) builder.append(tab); builder.append("bullRezvYn(게시물 예약 여부):").append(bullRezvYn).append('\n'); }
		if(tgtDeptYn!=null) { if(tab!=null) builder.append(tab); builder.append("tgtDeptYn(대상 부서 지정 여부):").append(tgtDeptYn).append('\n'); }
		if(tgtUserYn!=null) { if(tab!=null) builder.append(tab); builder.append("tgtUserYn(대상 사용자 지정 여부):").append(tgtUserYn).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(bullRezvDt!=null) { if(tab!=null) builder.append(tab); builder.append("bullRezvDt(게시 예약 일시):").append(bullRezvDt).append('\n'); }
		if(bullExprDt!=null) { if(tab!=null) builder.append(tab); builder.append("bullExprDt(게시 완료 일시):").append(bullExprDt).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(본문):").append(cont).append('\n'); }
		if(readCnt!=null) { if(tab!=null) builder.append(tab); builder.append("readCnt(조회수):").append(readCnt).append('\n'); }
		if(prosCnt!=null) { if(tab!=null) builder.append(tab); builder.append("prosCnt(찬성수):").append(prosCnt).append('\n'); }
		if(consCnt!=null) { if(tab!=null) builder.append(tab); builder.append("consCnt(반대수):").append(consCnt).append('\n'); }
		if(recmdCnt!=null) { if(tab!=null) builder.append(tab); builder.append("recmdCnt(추천수):").append(recmdCnt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(attYn!=null) { if(tab!=null) builder.append(tab); builder.append("attYn(첨부여부):").append(attYn).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자 명):").append(regrNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자 명):").append(modrNm).append('\n'); }
		if(tableName!=null) { if(tab!=null) builder.append(tab); builder.append("tableName(게시물 테이블 명):").append(tableName).append('\n'); }
		if(newYn!=null) { if(tab!=null) builder.append(tab); builder.append("newYn(새글여부):").append(newYn).append('\n'); }
		if(schExpr!=null) { if(tab!=null) builder.append(tab); builder.append("schExpr(검색조건(유효기간만료)):").append(schExpr).append('\n'); }
		if(exprYn!=null) { if(tab!=null) builder.append(tab); builder.append("exprYn(만료여부):").append(exprYn).append('\n'); }
		super.toString(builder, tab);
	}
	

}