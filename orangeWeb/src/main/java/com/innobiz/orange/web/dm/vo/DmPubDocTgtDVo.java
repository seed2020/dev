package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/10/02 15:25 ******/
/**
* 공용문서대상상세(DM_PUB_TGT_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmPubDocTgtDVo extends CommonVoImpl {
	/** 문서그룹ID */ 
	private String docGrpId;

 	/** 대상ID */ 
	private String tgtId;

 	/** 대상구분코드 */ 
	private String tgtTypCd;

 	/** 상태코드 */ 
	private String statCd;

	/** 열람시작일시 */ 
	private String readStrtDt;

 	/** 열람종료일시 */ 
	private String readEndDt;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 반려의견 */ 
	private String rjtOpin;
	
	/** 조회 - 사용자UID */ 
	private String srchUserUid;
	
	/** 조회 - 조직ID */
	private String srchOrgId;
	
 	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	public void setTgtId(String tgtId) { 
		this.tgtId = tgtId;
	}
	/** 대상ID */ 
	public String getTgtId() { 
		return tgtId;
	}

	public void setTgtTypCd(String tgtTypCd) { 
		this.tgtTypCd = tgtTypCd;
	}
	/** 대상구분코드 */ 
	public String getTgtTypCd() { 
		return tgtTypCd;
	}

	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}

	public void setReadStrtDt(String readStrtDt) { 
		this.readStrtDt = readStrtDt;
	}
	/** 열람시작일시 */ 
	public String getReadStrtDt() { 
		return readStrtDt;
	}

	public void setReadEndDt(String readEndDt) { 
		this.readEndDt = readEndDt;
	}
	/** 열람종료일시 */ 
	public String getReadEndDt() { 
		return readEndDt;
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
	
	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 반려의견 */ 
	public String getRjtOpin() {
		return rjtOpin;
	}
	public void setRjtOpin(String rjtOpin) {
		this.rjtOpin = rjtOpin;
	}
	/** 조회옵션 - 조회 사용자UID */ 
	public String getSrchUserUid() {
		return srchUserUid;
	}
	public void setSrchUserUid(String srchUserUid) {
		this.srchUserUid = srchUserUid;
	}
	
	/** 조회 - 조회 조직ID */
	public String getSrchOrgId() {
		return srchOrgId;
	}
	public void setSrchOrgId(String srchOrgId) {
		this.srchOrgId = srchOrgId;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPubDocTgtDDao.selectDmPubDocTgtD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPubDocTgtDDao.insertDmPubDocTgtD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPubDocTgtDDao.updateDmPubDocTgtD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPubDocTgtDDao.deleteDmPubDocTgtD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPubDocTgtDDao.countDmPubDocTgtD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":공용문서대상상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(tgtId!=null) { if(tab!=null) builder.append(tab); builder.append("tgtId(대상ID):").append(tgtId).append('\n'); }
		if(tgtTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("tgtTypCd(대상구분코드):").append(tgtTypCd).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(readStrtDt!=null) { if(tab!=null) builder.append(tab); builder.append("readStrtDt(열람시작일시):").append(readStrtDt).append('\n'); }
		if(readEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("readEndDt(열람종료일시):").append(readEndDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
