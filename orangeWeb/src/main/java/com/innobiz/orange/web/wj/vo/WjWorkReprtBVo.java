package com.innobiz.orange.web.wj.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2019-02-20 14:54:21 ******/
/**
* 업무보고 (WJ_WORK_REPRT_B) 테이블 VO 
*/
public class WjWorkReprtBVo extends CommonVoImpl { 
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1208879427223165940L;

	/** 보고번호 */ 
	private String reprtNo;

 	/** 회사ID */ 
	private String compId;

 	/** 조직ID */ 
	private String orgId;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

/** 추가 */
	/** LOB 데이터 조회 여부 */
	private boolean withLob = false;
	
	/** 조직리소스명 */ 
	private String orgRescNm;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 첨부파일 건수 */
	private Integer fileCnt;
	
	/** [검색조건] - 조직ID목록*/
	private List<String> orgIdList;
	
 	public void setReprtNo(String reprtNo) { 
		this.reprtNo = reprtNo;
	}
	/** 보고번호 */ 
	public String getReprtNo() { 
		return reprtNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setOrgId(String orgId) { 
		this.orgId = orgId;
	}
	/** 조직ID */ 
	public String getOrgId() { 
		return orgId;
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
	
	public String getOrgRescNm() {
		return orgRescNm;
	}
	public void setOrgRescNm(String orgRescNm) {
		this.orgRescNm = orgRescNm;
	}
	
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	/** 첨부파일 건수 */
	public Integer getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(Integer fileCnt) {
		this.fileCnt = fileCnt;
	}
	
	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}
	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	
	/** [검색조건] - 조직ID목록*/
	public List<String> getOrgIdList() {
		return orgIdList;
	}
	public void setOrgIdList(List<String> orgIdList) {
		this.orgIdList = orgIdList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.WjWorkReprtBDao.selectWjWorkReprtB";
		}else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.WjWorkReprtBDao.insertWjWorkReprtB";
		}else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.WjWorkReprtBDao.updateWjWorkReprtB";
		}else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.WjWorkReprtBDao.deleteWjWorkReprtB";
		}else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.WjWorkReprtBDao.countWjWorkReprtB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무보고 ]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(reprtNo!=null) { if(tab!=null) builder.append(tab); builder.append("reprtNo(보고번호):").append(reprtNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(orgId!=null) { if(tab!=null) builder.append(tab); builder.append("orgId(조직ID):").append(orgId).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
