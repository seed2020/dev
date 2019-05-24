package com.innobiz.orange.web.wb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 명함미팅상세(WB_BC_METNG_D) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcMetngDVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;
	
	/** 명함ID */ 
	private String bcId;

 	/** 명함미팅상세ID */ 
	private String bcMetngDetlId;

 	/** 대리여부 */ 
	private String agnYn;

 	/** 미팅년월일 */ 
	private String metngYmd;

 	/** 공개여부 */ 
	private String openYn;

 	/** 미팅분류코드 */ 
	private String metngClsCd;

 	/** 미팅제목 */ 
	private String metngSubj;

 	/** 미팅내용 */ 
	private String metngCont;

 	/** 첨부파일ID */ 
	private String attFileId;

 	/** 조회수 */ 
	private String readCnt;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
/** 추가 */
	/** 명함성명 */ 
	private String bcNm;
	
	/** 회사명 */ 
	private String compNm;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 대상UID(복사,이동) */ 
	private String copyRegrUid;
	
	/** 삭제ID 배열 */
	private String[] delList;
	
	/** 미팅분류명 */ 
	private String metngClsNm;
	
	/** 참석자 */ 
	private List<WbBcMetngAtndRVo> wbBcMetngAtndRVo;
	
	/** 첨부파일갯수 */ 
	private int fileCnt;
	
/** 검색조건 */
	/** 공개여부 */
	private String schOpenYn;
	
	/** 미팅분류코드 */ 
	private String schMetngClsCd;
	
	/** 미팅 시작일 */ 
	private String metngStrtDt;
	
	/** 미팅 종료일 */ 
	private String metngEndDt;
	
	/** 명함ID */ 
	private String schBcId;
	
	/** 등록자UID */ 
	private String schRegrUid;
	
 	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	public void setBcId(String bcId) { 
		this.bcId = bcId;
	}
	/** 명함ID */ 
	public String getBcId() { 
		return bcId;
	}

	public void setBcMetngDetlId(String bcMetngDetlId) { 
		this.bcMetngDetlId = bcMetngDetlId;
	}
	/** 명함미팅상세ID */ 
	public String getBcMetngDetlId() { 
		return bcMetngDetlId;
	}

	public void setAgnYn(String agnYn) { 
		this.agnYn = agnYn;
	}
	/** 대리여부 */ 
	public String getAgnYn() { 
		return agnYn;
	}

	public void setMetngYmd(String metngYmd) { 
		this.metngYmd = metngYmd;
	}
	/** 미팅년월일 */ 
	public String getMetngYmd() { 
		return metngYmd;
	}

	public void setOpenYn(String openYn) { 
		this.openYn = openYn;
	}
	/** 공개여부 */ 
	public String getOpenYn() { 
		return openYn;
	}

	public void setMetngClsCd(String metngClsCd) { 
		this.metngClsCd = metngClsCd;
	}
	/** 미팅분류코드 */ 
	public String getMetngClsCd() { 
		return metngClsCd;
	}

	public void setMetngSubj(String metngSubj) { 
		this.metngSubj = metngSubj;
	}
	/** 미팅제목 */ 
	public String getMetngSubj() { 
		return metngSubj;
	}

	public void setMetngCont(String metngCont) { 
		this.metngCont = metngCont;
	}
	/** 미팅내용 */ 
	public String getMetngCont() { 
		return metngCont;
	}

	public void setAttFileId(String attFileId) { 
		this.attFileId = attFileId;
	}
	/** 첨부파일ID */ 
	public String getAttFileId() { 
		return attFileId;
	}

	public void setReadCnt(String readCnt) { 
		this.readCnt = readCnt;
	}
	/** 조회수 */ 
	public String getReadCnt() { 
		return readCnt;
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
	
	public String getCopyRegrUid() {
		return copyRegrUid;
	}
	public void setCopyRegrUid(String copyRegrUid) {
		this.copyRegrUid = copyRegrUid;
	}
	public String[] getDelList() {
		return delList;
	}
	public void setDelList(String[] delList) {
		this.delList = delList;
	}
	
	public String getBcNm() {
		return bcNm;
	}
	public void setBcNm(String bcNm) {
		this.bcNm = bcNm;
	}
	public String getCompNm() {
		return compNm;
	}
	public void setCompNm(String compNm) {
		this.compNm = compNm;
	}
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	public String getSchOpenYn() {
		return schOpenYn;
	}
	public void setSchOpenYn(String schOpenYn) {
		this.schOpenYn = schOpenYn;
	}
	public String getSchMetngClsCd() {
		return schMetngClsCd;
	}
	public void setSchMetngClsCd(String schMetngClsCd) {
		this.schMetngClsCd = schMetngClsCd;
	}
	
	public String getMetngClsNm() {
		return metngClsNm;
	}
	public void setMetngClsNm(String metngClsNm) {
		this.metngClsNm = metngClsNm;
	}
	
	public List<WbBcMetngAtndRVo> getWbBcMetngAtndRVo() {
		return wbBcMetngAtndRVo;
	}
	public void setWbBcMetngAtndRVo(List<WbBcMetngAtndRVo> wbBcMetngAtndRVo) {
		this.wbBcMetngAtndRVo = wbBcMetngAtndRVo;
	}
	
	public int getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(int fileCnt) {
		this.fileCnt = fileCnt;
	}
	
	public String getMetngStrtDt() {
		return metngStrtDt;
	}
	public void setMetngStrtDt(String metngStrtDt) {
		this.metngStrtDt = metngStrtDt;
	}
	public String getMetngEndDt() {
		return metngEndDt;
	}
	public void setMetngEndDt(String metngEndDt) {
		this.metngEndDt = metngEndDt;
	}
	
	public String getSchBcId() {
		return schBcId;
	}
	public void setSchBcId(String schBcId) {
		this.schBcId = schBcId;
	}
	public String getSchRegrUid() {
		return schRegrUid;
	}
	public void setSchRegrUid(String schRegrUid) {
		this.schRegrUid = schRegrUid;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngDDao.selectWbBcMetngD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngDDao.insertWbBcMetngD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngDDao.updateWbBcMetngD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngDDao.deleteWbBcMetngD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcMetngDDao.countWbBcMetngD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함미팅상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(bcId!=null) { if(tab!=null) builder.append(tab); builder.append("bcId(명함ID):").append(bcId).append('\n'); }
		if(bcMetngDetlId!=null) { if(tab!=null) builder.append(tab); builder.append("bcMetngDetlId(명함미팅상세ID):").append(bcMetngDetlId).append('\n'); }
		if(agnYn!=null) { if(tab!=null) builder.append(tab); builder.append("agnYn(대리여부):").append(agnYn).append('\n'); }
		if(metngYmd!=null) { if(tab!=null) builder.append(tab); builder.append("metngYmd(미팅년월일):").append(metngYmd).append('\n'); }
		if(openYn!=null) { if(tab!=null) builder.append(tab); builder.append("openYn(공개여부):").append(openYn).append('\n'); }
		if(metngClsCd!=null) { if(tab!=null) builder.append(tab); builder.append("metngClsCd(미팅분류코드):").append(metngClsCd).append('\n'); }
		if(metngSubj!=null) { if(tab!=null) builder.append(tab); builder.append("metngSubj(미팅제목):").append(metngSubj).append('\n'); }
		if(metngCont!=null) { if(tab!=null) builder.append(tab); builder.append("metngCont(미팅내용):").append(metngCont).append('\n'); }
		if(attFileId!=null) { if(tab!=null) builder.append(tab); builder.append("attFileId(첨부파일ID):").append(attFileId).append('\n'); }
		if(readCnt!=null) { if(tab!=null) builder.append(tab); builder.append("readCnt(조회수):").append(readCnt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(bcNm!=null) { if(tab!=null) builder.append(tab); builder.append("bcNm(명함성명):").append(bcNm).append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(copyRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("copyRegrUid(대상UID(복사,이동)):").append(copyRegrUid).append('\n'); }
		if(delList!=null) { if(tab!=null) builder.append(tab); builder.append("delList(삭제ID 배열):"); appendArrayTo(builder, delList); builder.append('\n'); }
		if(metngClsNm!=null) { if(tab!=null) builder.append(tab); builder.append("metngClsNm(미팅분류명):").append(metngClsNm).append('\n'); }
		if(wbBcMetngAtndRVo!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcMetngAtndRVo(참석자):"); appendVoListTo(builder, wbBcMetngAtndRVo,tab); builder.append('\n'); }
		if(fileCnt!=0) { if(tab!=null) builder.append(tab); builder.append("fileCnt(첨부파일갯수):").append(fileCnt).append('\n'); }
		if(schOpenYn!=null) { if(tab!=null) builder.append(tab); builder.append("schOpenYn(공개여부):").append(schOpenYn).append('\n'); }
		if(schMetngClsCd!=null) { if(tab!=null) builder.append(tab); builder.append("schMetngClsCd(미팅분류코드):").append(schMetngClsCd).append('\n'); }
		if(metngStrtDt!=null) { if(tab!=null) builder.append(tab); builder.append("metngStrtDt(미팅 시작일):").append(metngStrtDt).append('\n'); }
		if(metngEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("metngEndDt(미팅 종료일):").append(metngEndDt).append('\n'); }
		if(schBcId!=null) { if(tab!=null) builder.append(tab); builder.append("schBcId(명함ID):").append(schBcId).append('\n'); }
		if(schRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("schRegrUid(등록자UID):").append(schRegrUid).append('\n'); }
		super.toString(builder, tab);
	}

}