package com.innobiz.orange.web.wh.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/05/04 13:39 ******/
/**
* 요청진행상세(WH_REQ_ONGD_D) 테이블 VO 
*/
public class WhReqOngdDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -2045579351274639792L;

	/** 요청번호 */ 
	private String reqNo;

 	/** 모듈ID */ 
	private String mdId;

 	/** 상태코드 */ 
	private String statCd;
	
	/** 이전상태코드 */ 
	private String prevStatCd;
	
	/** 담당자UID */ 
	private String pichUid;

 	/** 접수자UID */ 
	private String recvUid;

 	/** 접수일 */ 
	private String recvDt;

 	/** 접수내용  */ 
	private String recvCont;

 	/** 처리자UID */ 
	private String hdlrUid;

 	/** 유형번호 */ 
	private String catNo;

 	/** 완료예정일 */ 
	private String cmplDueDt;

 	/** 진행내용 */ 
	private String ongoCont;

 	/** 완료일시 */ 
	private String cmplDt;

 	/** 평가여부 */ 
	private String evalYn;

 	/** 평가번호 */ 
	private String evalNo;
	
	/** 추가 */
	/** 담당자명 */ 
	private String pichNm;
	
	/** 접수자명 */ 
	private String recvNm;
	
	/** 처리자명 */ 
	private String hdlrNm;
	
	/** 유형명 */ 
	private String catNm;
	
	/** 모듈ID 목록 */ 
	private List<String> mdIdList;
	
	/** 유형번호 목록 */ 
	private List<String> catNoList;
	
 	public void setReqNo(String reqNo) { 
		this.reqNo = reqNo;
	}
	/** 요청번호 */ 
	public String getReqNo() { 
		return reqNo;
	}

	public void setMdId(String mdId) { 
		this.mdId = mdId;
	}
	/** 모듈ID */ 
	public String getMdId() { 
		return mdId;
	}

	public void setStatCd(String statCd) { 
		this.statCd = statCd;
	}
	/** 상태코드 */ 
	public String getStatCd() { 
		return statCd;
	}
	
	public void setPrevStatCd(String prevStatCd) { 
		this.prevStatCd = prevStatCd;
	}
	/** 이전상태코드 */ 
	public String getPrevStatCd() { 
		return prevStatCd;
	}
	
	public void setPichUid(String pichUid) { 
		this.pichUid = pichUid;
	}
	/** 담당자UID */ 
	public String getPichUid() { 
		return pichUid;
	}
	
	public void setRecvUid(String recvUid) { 
		this.recvUid = recvUid;
	}
	/** 접수자UID */ 
	public String getRecvUid() { 
		return recvUid;
	}

	public void setRecvDt(String recvDt) { 
		this.recvDt = recvDt;
	}
	/** 접수일 */ 
	public String getRecvDt() { 
		return recvDt;
	}

	public void setRecvCont(String recvCont) { 
		this.recvCont = recvCont;
	}
	/** 접수내용  */ 
	public String getRecvCont() { 
		return recvCont;
	}

	public void setHdlrUid(String hdlrUid) { 
		this.hdlrUid = hdlrUid;
	}
	/** 처리자UID */ 
	public String getHdlrUid() { 
		return hdlrUid;
	}

	public void setCatNo(String catNo) { 
		this.catNo = catNo;
	}
	/** 유형번호 */ 
	public String getCatNo() { 
		return catNo;
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

	public void setCmplDt(String cmplDt) { 
		this.cmplDt = cmplDt;
	}
	/** 완료일시 */ 
	public String getCmplDt() { 
		return cmplDt;
	}

	public void setEvalYn(String evalYn) { 
		this.evalYn = evalYn;
	}
	/** 평가여부 */ 
	public String getEvalYn() { 
		return evalYn;
	}

	public void setEvalNo(String evalNo) { 
		this.evalNo = evalNo;
	}
	/** 평가번호 */ 
	public String getEvalNo() { 
		return evalNo;
	}
	
	/** 담당자명 */ 
	public String getPichNm() {
		return pichNm;
	}
	public void setPichNm(String pichNm) {
		this.pichNm = pichNm;
	}
	
	/** 접수자명 */ 
	public String getRecvNm() {
		return recvNm;
	}
	public void setRecvNm(String recvNm) {
		this.recvNm = recvNm;
	}
	
	/** 처리자명 */ 
	public String getHdlrNm() {
		return hdlrNm;
	}
	public void setHdlrNm(String hdlrNm) {
		this.hdlrNm = hdlrNm;
	}
	
	/** 유형명 */
	public String getCatNm() {
		return catNm;
	}
	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}
	
	/** 모듈ID 목록 */ 
	public List<String> getMdIdList() {
		return mdIdList;
	}
	public void setMdIdList(List<String> mdIdList) {
		this.mdIdList = mdIdList;
	}
	
	/** 유형번호 목록 */ 
	public List<String> getCatNoList() {
		return catNoList;
	}
	public void setCatNoList(List<String> catNoList) {
		this.catNoList = catNoList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdDDao.selectWhReqOngdD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdDDao.insertWhReqOngdD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdDDao.updateWhReqOngdD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdDDao.deleteWhReqOngdD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqOngdDDao.countWhReqOngdD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청진행상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(statCd!=null) { if(tab!=null) builder.append(tab); builder.append("statCd(상태코드):").append(statCd).append('\n'); }
		if(prevStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("prevStatCd(이전상태코드):").append(prevStatCd).append('\n'); }
		if(recvUid!=null) { if(tab!=null) builder.append(tab); builder.append("recvUid(접수자UID):").append(recvUid).append('\n'); }
		if(recvDt!=null) { if(tab!=null) builder.append(tab); builder.append("recvDt(접수일):").append(recvDt).append('\n'); }
		if(recvCont!=null) { if(tab!=null) builder.append(tab); builder.append("recvCont(접수내용 ):").append(recvCont).append('\n'); }
		if(hdlrUid!=null) { if(tab!=null) builder.append(tab); builder.append("hdlrUid(처리자UID):").append(hdlrUid).append('\n'); }
		if(catNo!=null) { if(tab!=null) builder.append(tab); builder.append("catNo(유형번호):").append(catNo).append('\n'); }
		if(cmplDueDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDueDt(완료예정일):").append(cmplDueDt).append('\n'); }
		if(ongoCont!=null) { if(tab!=null) builder.append(tab); builder.append("ongoCont(진행내용):").append(ongoCont).append('\n'); }
		if(cmplDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDt(완료일시):").append(cmplDt).append('\n'); }
		if(evalYn!=null) { if(tab!=null) builder.append(tab); builder.append("evalYn(평가여부):").append(evalYn).append('\n'); }
		if(evalNo!=null) { if(tab!=null) builder.append(tab); builder.append("evalNo(평가번호):").append(evalNo).append('\n'); }
		super.toString(builder, tab);
	}

}
