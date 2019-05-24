package com.innobiz.orange.web.wc.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018-12-18 12:46:46 ******/
/**
* 근태일정(WC_WORK_SCHDL_B) 테이블 VO 
*/
public class WcWorkSchdlBVo extends CommonVoImpl { 
	
	/** serialVersionUID */
	private static final long serialVersionUID = -2761702974491342364L;

	/** 일정ID */ 
	private String schdlId;

 	/** 회사ID */ 
	private String compId;

 	/** 사용자UID */ 
	private String userUid;

 	/** 일정구분코드 */ 
	private String schdlTypCd;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

 	/** 시작일시 */ 
	private String strtDt;

 	/** 종료일시 */ 
	private String endDt;

 	/** 종일여부 */ 
	private String alldayYn;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

/** 추가 */
	
	/** 일정구분명 */ 
	private String schdlTypNm;
	
	/** 사용자명 */ 
	private String userNm;
	
	/** 등록자명 */ 
	private String regrNm;	
	
	/** 검색조건(사용자UID) */
	private String schUserUid;
	
	/** 회사ID목록 */
	private List<String> compIdList;
	
 	public void setSchdlId(String schdlId) { 
		this.schdlId = schdlId;
	}
	/** 일정ID */ 
	public String getSchdlId() { 
		return schdlId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setSchdlTypCd(String schdlTypCd) { 
		this.schdlTypCd = schdlTypCd;
	}
	/** 일정구분코드 */ 
	public String getSchdlTypCd() { 
		return schdlTypCd;
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

	public void setStrtDt(String strtDt) { 
		this.strtDt = strtDt;
	}
	/** 시작일시 */ 
	public String getStrtDt() { 
		return strtDt;
	}

	public void setEndDt(String endDt) { 
		this.endDt = endDt;
	}
	/** 종료일시 */ 
	public String getEndDt() { 
		return endDt;
	}

	public void setAlldayYn(String alldayYn) { 
		this.alldayYn = alldayYn;
	}
	/** 종일여부 */ 
	public String getAlldayYn() { 
		return alldayYn;
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

	/** 일정구분명 */ 
	public String getSchdlTypNm() {
		return schdlTypNm;
	}
	public void setSchdlTypNm(String schdlTypNm) {
		this.schdlTypNm = schdlTypNm;
	}
	
	/** 사용자명 */ 
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 검색조건(사용자UID) */
	public String getSchUserUid() {
		return schUserUid;
	}

	/** 검색조건(사용자UID) */
	public void setSchUserUid(String schUserUid) {
		this.schUserUid = schUserUid;
	}
	
	/** 회사ID목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcWorkSchdlBDao.selectWcWorkSchdlB";
		}else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcWorkSchdlBDao.insertWcWorkSchdlB";
		}else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcWorkSchdlBDao.updateWcWorkSchdlB";
		}else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wc.dao.WcWorkSchdlBDao.deleteWcWorkSchdlB";
		}else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wc.dao.WcWorkSchdlBDao.countWcWorkSchdlB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":근태일정]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(schdlId!=null) { if(tab!=null) builder.append(tab); builder.append("schdlId(일정ID):").append(schdlId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(schdlTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("schdlTypCd(일정구분코드):").append(schdlTypCd).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일시):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일시):").append(endDt).append('\n'); }
		if(alldayYn!=null) { if(tab!=null) builder.append(tab); builder.append("alldayYn(종일여부):").append(alldayYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
