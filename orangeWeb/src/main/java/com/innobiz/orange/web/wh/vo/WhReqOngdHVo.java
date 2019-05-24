package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/09/29 15:19 ******/
/**
* 요청진행이력(WH_REQ_ONGD_H) 테이블 VO 
*/
public class WhReqOngdHVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 9063901318691479814L;

	/** 이력번호 */ 
	private String hstNo;

 	/** 요청번호 */ 
	private String reqNo;

 	/** 담당자UID */ 
	private String pichUid;

 	/** 완료예정일 */ 
	private String cmplDueDt;

 	/** 진행내용 */ 
	private String ongoCont;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	
	/** 담당자명 */ 
	private String pichNm;
	
	/** 등록자명 */ 
	private String regrNm;

 	public void setHstNo(String hstNo) { 
		this.hstNo = hstNo;
	}
	/** 이력번호 */ 
	public String getHstNo() { 
		return hstNo;
	}

	public void setReqNo(String reqNo) { 
		this.reqNo = reqNo;
	}
	/** 요청번호 */ 
	public String getReqNo() { 
		return reqNo;
	}

	public void setPichUid(String pichUid) { 
		this.pichUid = pichUid;
	}
	/** 담당자UID */ 
	public String getPichUid() { 
		return pichUid;
	}

	public void setCmplDueDt(String cmplDueDt) { 
		this.cmplDueDt = cmplDueDt;
	}
	/** 완료예정일 */ 
	public String getCmplDueDt() { 
		return cmplDueDt;
	}

	public void setOngoCont(String ongoCont) { 
		this.ongoCont = ongoCont;
	}
	/** 진행내용 */ 
	public String getOngoCont() { 
		return ongoCont;
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
	
	/** 담당자명 */ 
	public String getPichNm() {
		return pichNm;
	}
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
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
			return "com.innobiz.orange.web.wh.dao.WhReqOngdHDao.selectWhReqOngdH";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdHDao.insertWhReqOngdH";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdHDao.updateWhReqOngdH";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdHDao.deleteWhReqOngdH";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdHDao.countWhReqOngdH";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청진행이력]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(hstNo!=null) { if(tab!=null) builder.append(tab); builder.append("hstNo(이력번호):").append(hstNo).append('\n'); }
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(pichUid!=null) { if(tab!=null) builder.append(tab); builder.append("pichUid(담당자UID):").append(pichUid).append('\n'); }
		if(cmplDueDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDueDt(완료예정일):").append(cmplDueDt).append('\n'); }
		if(ongoCont!=null) { if(tab!=null) builder.append(tab); builder.append("ongoCont(진행내용):").append(ongoCont).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
