package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/05/11 11:02 ******/
/**
* 요청평가상세(WH_REQ_EVAL_D) 테이블 VO 
*/
public class WhReqEvalDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -3475908867121535162L;

	/** 요청번호 */ 
	private String reqNo;

 	/** 평가번호 */ 
	private String evalNo;

 	/** 평가사유 */ 
	private String evalRson;

 	/** 추가요청사항 */ 
	private String addReqr;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	/** 평가명  */ 
	private String evalNm;
	
	/** 등록자명  */ 
	private String regrNm;

 	public void setReqNo(String reqNo) { 
		this.reqNo = reqNo;
	}
	/** 요청번호 */ 
	public String getReqNo() { 
		return reqNo;
	}

	public void setEvalNo(String evalNo) { 
		this.evalNo = evalNo;
	}
	/** 평가번호 */ 
	public String getEvalNo() { 
		return evalNo;
	}

	public void setEvalRson(String evalRson) { 
		this.evalRson = evalRson;
	}
	/** 평가사유 */ 
	public String getEvalRson() { 
		return evalRson;
	}

	public void setAddReqr(String addReqr) { 
		this.addReqr = addReqr;
	}
	/** 추가요청사항 */ 
	public String getAddReqr() { 
		return addReqr;
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
	
	/** 평가명  */ 
	public String getEvalNm() {
		return evalNm;
	}
	public void setEvalNm(String evalNm) {
		this.evalNm = evalNm;
	}
	
	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqEvalDDao.selectWhReqEvalD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqEvalDDao.insertWhReqEvalD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqEvalDDao.updateWhReqEvalD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqEvalDDao.deleteWhReqEvalD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqEvalDDao.countWhReqEvalD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청평가상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(evalNo!=null) { if(tab!=null) builder.append(tab); builder.append("evalNo(평가번호):").append(evalNo).append('\n'); }
		if(evalRson!=null) { if(tab!=null) builder.append(tab); builder.append("evalRson(평가사유):").append(evalRson).append('\n'); }
		if(addReqr!=null) { if(tab!=null) builder.append(tab); builder.append("addReqr(추가요청사항):").append(addReqr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
