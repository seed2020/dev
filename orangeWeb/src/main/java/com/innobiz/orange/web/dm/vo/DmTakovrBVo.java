package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/09/08 15:23 ******/
/**
* 인수인계기본(DM_TAKOVR_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmTakovrBVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;

 	/** 문서그룹ID */ 
	private String docGrpId;

 	/** 조직ID */ 
	private String takOrgId;

 	/** 대상조직ID */ 
	private String tgtOrgId;
	
	/** 회사여부 */ 
	private String compYn;

 	/** 상태코드 */ 
	private String takStatCd;

 	/** 등록자UID */ 
	private String takRegrUid;

 	/** 등록일시 */ 
	private String takRegDt;
	
	/** 인계조직명 */ 
	private String takOrgNm;

 	/** 대상조직명 */ 
	private String tgtOrgNm;
	
 	public void setStorId(String storId) { 
		this.storId = storId;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setDocGrpId(String docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public String getDocGrpId() { 
		return docGrpId;
	}

	public void setTakOrgId(String takOrgId) { 
		this.takOrgId = takOrgId;
	}
	/** 조직ID */ 
	public String getTakOrgId() { 
		return takOrgId;
	}

	public void setTgtOrgId(String tgtOrgId) { 
		this.tgtOrgId = tgtOrgId;
	}
	/** 대상조직ID */ 
	public String getTgtOrgId() { 
		return tgtOrgId;
	}
	
	/** 회사여부 */
	public String getCompYn() {
		return compYn;
	}
	public void setCompYn(String compYn) {
		this.compYn = compYn;
	}
	public void setTakStatCd(String takStatCd) { 
		this.takStatCd = takStatCd;
	}
	/** 상태코드 */ 
	public String getTakStatCd() { 
		return takStatCd;
	}

	public void setTakRegrUid(String takRegrUid) { 
		this.takRegrUid = takRegrUid;
	}
	/** 등록자UID */ 
	public String getTakRegrUid() { 
		return takRegrUid;
	}

	public void setTakRegDt(String takRegDt) { 
		this.takRegDt = takRegDt;
	}
	/** 등록일시 */ 
	public String getTakRegDt() { 
		return takRegDt;
	}
	
	/** 조직명 */ 
	public String getTakOrgNm() {
		return takOrgNm;
	}
	public void setTakOrgNm(String takOrgNm) {
		this.takOrgNm = takOrgNm;
	}
	
	/** 대상조직명 */ 
	public String getTgtOrgNm() {
		return tgtOrgNm;
	}
	public void setTgtOrgNm(String tgtOrgNm) {
		this.tgtOrgNm = tgtOrgNm;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTakovrBDao.selectDmTakovrB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTakovrBDao.insertDmTakovrB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTakovrBDao.updateDmTakovrB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTakovrBDao.deleteDmTakovrB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTakovrBDao.countDmTakovrB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":인수인계기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		if(takOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("takOrgId(조직ID):").append(takOrgId).append('\n'); }
		if(tgtOrgId!=null) { if(tab!=null) builder.append(tab); builder.append("tgtOrgId(대상조직ID):").append(tgtOrgId).append('\n'); }
		if(compYn!=null) { if(tab!=null) builder.append(tab); builder.append("compYn(회사여부):").append(compYn).append('\n'); }
		if(takStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("takStatCd(상태코드):").append(takStatCd).append('\n'); }
		if(takRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("takRegrUid(등록자UID):").append(takRegrUid).append('\n'); }
		if(takRegDt!=null) { if(tab!=null) builder.append(tab); builder.append("takRegDt(등록일시):").append(takRegDt).append('\n'); }
		super.toString(builder, tab);
	}

}
