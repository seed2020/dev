package com.innobiz.orange.web.cu.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/10/10 17:18 ******/
/**
* 업무현황(CU_TASK_STAT_B) 테이블 VO 
*/
public class CuTaskStatBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7531292784410383238L;

	/** 현황번호 */ 
	private String statNo;
	
	/** 보고번호 */ 
	private String reprtNo;

 	/** 회사ID */ 
	private String compId;

 	/** 보고일시 */ 
	private String reprtDt;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

 	/** 본문XML */ 
	private String bodyXml;

 	/** 비공개여부 */ 
	private String privYn;

 	/** 코드1 */ 
	private String cd1;

 	/** 코드2 */ 
	private String cd2;

 	/** 코드3 */ 
	private String cd3;

 	/** 코드4 */ 
	private String cd4;

 	/** 코드5 */ 
	private String cd5;

 	/** 코드6 */ 
	private String cd6;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 추가 */
	
	/** 등록자명 */
	private String regrNm;
	
	/** 수정자명 */
	private String modrNm;
	
	/** 사용자UID */ 
	private String userUid;
	
	/** 구분코드[ 업무현황 : null , 주간보고 : REPRT] */ 
	private String typCd;
	
	/** 비고 */ 
	private String note;
	
 	public void setStatNo(String statNo) { 
		this.statNo = statNo;
	}
	/** 현황번호 */ 
	public String getStatNo() { 
		return statNo;
	}
	
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

	public void setReprtDt(String reprtDt) { 
		this.reprtDt = reprtDt;
	}
	/** 보고일시 */ 
	public String getReprtDt() { 
		return reprtDt;
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

	public void setBodyXml(String bodyXml) { 
		this.bodyXml = bodyXml;
	}
	/** 본문XML */ 
	public String getBodyXml() { 
		return bodyXml;
	}

	public void setPrivYn(String privYn) { 
		this.privYn = privYn;
	}
	/** 비공개여부 */ 
	public String getPrivYn() { 
		return privYn;
	}

	public void setCd1(String cd1) { 
		this.cd1 = cd1;
	}
	/** 코드1 */ 
	public String getCd1() { 
		return cd1;
	}

	public void setCd2(String cd2) { 
		this.cd2 = cd2;
	}
	/** 코드2 */ 
	public String getCd2() { 
		return cd2;
	}

	public void setCd3(String cd3) { 
		this.cd3 = cd3;
	}
	/** 코드3 */ 
	public String getCd3() { 
		return cd3;
	}

	public void setCd4(String cd4) { 
		this.cd4 = cd4;
	}
	/** 코드4 */ 
	public String getCd4() { 
		return cd4;
	}

	public void setCd5(String cd5) { 
		this.cd5 = cd5;
	}
	/** 코드5 */ 
	public String getCd5() { 
		return cd5;
	}

	public void setCd6(String cd6) { 
		this.cd6 = cd6;
	}
	/** 코드6 */ 
	public String getCd6() { 
		return cd6;
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

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}
	
	/** 사용자UID */ 
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	/** 구분코드[ 업무현황 : null , 주간보고 : REPRT] */ 
	public String getTypCd() {
		return typCd;
	}
	public void setTypCd(String typCd) {
		this.typCd = typCd;
	}
	
	/** 비고 */ 
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.selectCuTaskStatB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.insertCuTaskStatB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.updateCuTaskStatB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.deleteCuTaskStatB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuTaskStatBDao.countCuTaskStatB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":업무현황]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(statNo!=null) { if(tab!=null) builder.append(tab); builder.append("statNo(현황번호):").append(statNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(reprtDt!=null) { if(tab!=null) builder.append(tab); builder.append("reprtDt(보고일시):").append(reprtDt).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(bodyXml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyXml(본문XML):").append(bodyXml).append('\n'); }
		if(privYn!=null) { if(tab!=null) builder.append(tab); builder.append("privYn(비공개여부):").append(privYn).append('\n'); }
		if(cd1!=null) { if(tab!=null) builder.append(tab); builder.append("cd1(코드1):").append(cd1).append('\n'); }
		if(cd2!=null) { if(tab!=null) builder.append(tab); builder.append("cd2(코드2):").append(cd2).append('\n'); }
		if(cd3!=null) { if(tab!=null) builder.append(tab); builder.append("cd3(코드3):").append(cd3).append('\n'); }
		if(cd4!=null) { if(tab!=null) builder.append(tab); builder.append("cd4(코드4):").append(cd4).append('\n'); }
		if(cd5!=null) { if(tab!=null) builder.append(tab); builder.append("cd5(코드5):").append(cd5).append('\n'); }
		if(cd6!=null) { if(tab!=null) builder.append(tab); builder.append("cd6(코드6):").append(cd6).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
