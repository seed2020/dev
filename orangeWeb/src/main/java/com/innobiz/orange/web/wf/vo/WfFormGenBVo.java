package com.innobiz.orange.web.wf.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/03/16 16:02 ******/
/**
* 폼생성기본(WF_FORM_GEN_B) 테이블 VO 
*/
public class WfFormGenBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 7586808793220760330L;

	/** 생성ID */ 
	private String genId;
	
	/** 회사ID */ 
	private String compId;

 	/** 시작일시 */ 
	private String strtDt;

 	/** 종료일시 */ 
	private String endDt;

 	/** 진행상태코드 */ 
	private String procStatCd;

 	/** 조각모음여부 */ 
	private String defragYn;

	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	/** 등록자명 */ 
	private String regrNm;
	
	/** 삭제여부 */ 
	private String delYn;
	
	/** 양식생성목록(WF_FORM_GEN_L) */ 
	private List<WfFormGenLVo> wfFormGenLVoList;

 	public void setGenId(String genId) { 
		this.genId = genId;
	}
	/** 생성ID */ 
	public String getGenId() { 
		return genId;
	}
	
	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
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

	public void setProcStatCd(String procStatCd) { 
		this.procStatCd = procStatCd;
	}
	/** 진행상태코드 */ 
	public String getProcStatCd() { 
		return procStatCd;
	}

	public void setDefragYn(String defragYn) { 
		this.defragYn = defragYn;
	}
	/** 조각모음여부 */ 
	public String getDefragYn() { 
		return defragYn;
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
	
	/** 등록자명 */ 
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 삭제여부 */ 
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	
	/** 양식생성목록(WF_FORM_GEN_L) */ 
	public List<WfFormGenLVo> getWfFormGenLVoList() {
		return wfFormGenLVoList;
	}
	public void setWfFormGenLVoList(List<WfFormGenLVo> wfFormGenLVoList) {
		this.wfFormGenLVoList = wfFormGenLVoList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenBDao.selectWfFormGenB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenBDao.insertWfFormGenB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenBDao.updateWfFormGenB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenBDao.deleteWfFormGenB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfFormGenBDao.countWfFormGenB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":폼생성기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(genId!=null) { if(tab!=null) builder.append(tab); builder.append("genId(생성ID):").append(genId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일시):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일시):").append(endDt).append('\n'); }
		if(procStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("procStatCd(진행상태코드):").append(procStatCd).append('\n'); }
		if(defragYn!=null) { if(tab!=null) builder.append(tab); builder.append("defragYn(조각모음여부):").append(defragYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
