package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/19 16:01 ******/
/**
*  결과평가기본(WH_RES_EVAL_B) 테이블 VO 
*/
public class WhResEvalBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 6016149554565588612L;

	/** 평가번호 */ 
	private String evalNo;

 	/** 회사ID */ 
	private String compId;

 	/** 평가명 */ 
	private String evalNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	public void setEvalNo(String evalNo) { 
		this.evalNo = evalNo;
	}
	/** 평가번호 */ 
	public String getEvalNo() { 
		return evalNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setEvalNm(String evalNm) { 
		this.evalNm = evalNm;
	}
	/** 평가명 */ 
	public String getEvalNm() { 
		return evalNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
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

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhResEvalBDao.selectWhResEvalB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhResEvalBDao.insertWhResEvalB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhResEvalBDao.updateWhResEvalB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhResEvalBDao.deleteWhResEvalB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhResEvalBDao.countWhResEvalB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(": 결과평가기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(evalNo!=null) { if(tab!=null) builder.append(tab); builder.append("evalNo(평가번호):").append(evalNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(evalNm!=null) { if(tab!=null) builder.append(tab); builder.append("evalNm(평가명):").append(evalNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
