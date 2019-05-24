package com.innobiz.orange.web.wf.vo;

import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/02/26 16:00 ******/
/**
* 양식기본(WF_FORM_B) 테이블 VO 
*/
public class WfWorksLVo extends WfCmWorksVo {

	/** serialVersionUID */
	private static final long serialVersionUID = -1065323525704307323L;

 	/** 회사ID */ 
	private String compId;

 	/** 생성ID */ 
	private String genId;

 	/** JSON값 */ 
	private String jsonVa;
	
	/** 레이아웃값  */ 
	private String loutVa;

 	/** 속성값 */ 
	private String attrVa;
	
	/** 모바일레이아웃값 */ 
	private String mobLoutVa;

 	/** 모바일탭값 */ 
	private String mobTabVa;
	
	/** 생성컬럼 맵 */ 
	private Map<String,Object> voMap;
	
	/** 상태코드 */ 
	private String statCd;
	
 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	
	/** 추가 */
	/** 조회수 */ 
	private String readCnt;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 표시순서맵 */
	private Map<String, String> dispOrdrMap;
	
	/** 상세 테이블 여부 */
	private boolean isDetail=false;
	
	/** 가변 컬럼 목록 */ 
	private List<String[]> colmList;
	
	/** 코드 컬럼 목록맵 */
	private Map<String, String[]> cdListMap;
	
	/** 검색 목록 컬럼 맵 */
	private Map<String, List<Map<String, Object>>> schListMap;
	
 	public WfWorksLVo(String formNo) {
		super(formNo);
	}
 	
 	public WfWorksLVo(String formNo, String formId) {
		super(formNo, formId);
	}
 	
 	public WfWorksLVo() {
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}

	public void setJsonVa(String jsonVa) { 
		this.jsonVa = jsonVa;
	}
	/** JSON값 */ 
	public String getJsonVa() { 
		return jsonVa;
	}
	
	public void setLoutVa(String loutVa) { 
		this.loutVa = loutVa;
	}
	/** 레이아웃값  */ 
	public String getLoutVa() { 
		return loutVa;
	}

	public void setAttrVa(String attrVa) { 
		this.attrVa = attrVa;
	}
	/** 속성값 */ 
	public String getAttrVa() { 
		return attrVa;
	}
	
	public void setMobLoutVa(String mobLoutVa) { 
		this.mobLoutVa = mobLoutVa;
	}
	/** 모바일레이아웃값 */ 
	public String getMobLoutVa() { 
		return mobLoutVa;
	}

	public void setMobTabVa(String mobTabVa) { 
		this.mobTabVa = mobTabVa;
	}
	/** 모바일탭값 */ 
	public String getMobTabVa() { 
		return mobTabVa;
	}
	
	/** 생성컬럼 맵 */ 
	public Map<String, Object> getVoMap() {
		return voMap;
	}
	public void setVoMap(Map<String, Object> voMap) {
		this.voMap = voMap;
	}
	
	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}
	
	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자 */ 
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
	
	/** 조회수 */ 
	public String getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(String readCnt) {
		this.readCnt = readCnt;
	}

	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 표시순서맵 */
	public Map<String, String> getDispOrdrMap() {
		return dispOrdrMap;
	}

	public void setDispOrdrMap(Map<String, String> dispOrdrMap) {
		this.dispOrdrMap = dispOrdrMap;
	}

	/** 상세 테이블 여부 */
	public boolean isDetail() {
		return isDetail;
	}
	public void setDetail(boolean isDetail) {
		this.isDetail = isDetail;
	}
	
	/** 가변 컬럼 목록 */ 
	public List<String[]> getColmList() {
		return colmList;
	}
	public void setColmList(List<String[]> colmList) {
		this.colmList = colmList;
	}
	
	/** 코드 컬럼 목록맵 */
	public Map<String, String[]> getCdListMap() {
		return cdListMap;
	}

	public void setCdListMap(Map<String, String[]> cdListMap) {
		this.cdListMap = cdListMap;
	}
	
	/** 검색 목록 컬럼 맵 */
	public Map<String, List<Map<String, Object>>> getSchListMap() {
		return schListMap;
	}

	public void setSchListMap(Map<String, List<Map<String, Object>>> schListMap) {
		this.schListMap = schListMap;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksLDao.selectWfWorksL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksLDao.insertWfWorksL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksLDao.updateWfWorksL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksLDao.deleteWfWorksL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksLDao.countWfWorksL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":생성테이블목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(jsonVa!=null) { if(tab!=null) builder.append(tab); builder.append("jsonVa(JSON값):").append(jsonVa).append('\n'); }
		if(loutVa!=null) { if(tab!=null) builder.append(tab); builder.append("loutVa(레이아웃값):").append(loutVa).append('\n'); }
		if(attrVa!=null) { if(tab!=null) builder.append(tab); builder.append("attrVa(속성값):").append(attrVa).append('\n'); }
		if(mobLoutVa!=null) { if(tab!=null) builder.append(tab); builder.append("mobLoutVa(모바일레이아웃값):").append(mobLoutVa).append('\n'); }
		if(mobTabVa!=null) { if(tab!=null) builder.append(tab); builder.append("mobTabVa(모바일탭값):").append(mobTabVa).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
