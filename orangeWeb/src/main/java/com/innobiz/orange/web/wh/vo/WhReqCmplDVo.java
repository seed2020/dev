package com.innobiz.orange.web.wh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/05/04 13:46 ******/
/**
* 요청완료상세(WH_REQ_CMPL_D) 테이블 VO 
*/
public class WhReqCmplDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -2000002543955856187L;
	
	/** 이력번호 */ 
	private String hstNo;
	
	/** 요청번호 */ 
	private String reqNo;
	
	/** 완료일시 */ 
	private String cmplDt;

 	/** 처리내용 */ 
	private String hdlCont;
	
	/** 공수값 */ 
	private String devHourVa;

 	/** 처리업체명 */ 
	private String hdlCompNm;

 	/** 처리비용 */ 
	private String hdlCost;

 	/** 시작일 */ 
	private String strtDt;

 	/** 종료일 */ 
	private String endDt;

	/** 테스트담당자UID */ 
	private String testPichUid;

 	/** 테스트진행내용 */ 
	private String testOngoCont;

 	/** 테스트결과내용 */ 
	private String testResCont;
	
	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	/** 담당자명 */ 
	/** LOB 데이터 조회 여부 */
	private boolean withLob = true;
	
	/** 테스트 담당자명 */
	private String testPichNm;
	
	/** 이력여부 */ 
	private boolean isHst=false;
	
	/** 파일건수 */ 
	private Integer fileCnt;
	
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
	
	public void setCmplDt(String cmplDt) { 
		this.cmplDt = cmplDt;
	}
	/** 완료일시 */ 
	public String getCmplDt() { 
		return cmplDt;
	}
	
	public void setHdlCont(String hdlCont) { 
		this.hdlCont = hdlCont;
	}
	/** 처리내용 */ 
	public String getHdlCont() { 
		return hdlCont;
	}
	
	public void setDevHourVa(String devHourVa) { 
		this.devHourVa = devHourVa;
	}
	/** 공수값 */ 
	public String getDevHourVa() { 
		return devHourVa;
	}
	
	public void setHdlCompNm(String hdlCompNm) { 
		this.hdlCompNm = hdlCompNm;
	}
	/** 처리업체명 */ 
	public String getHdlCompNm() { 
		return hdlCompNm;
	}

	public void setHdlCost(String hdlCost) { 
		this.hdlCost = hdlCost;
	}
	/** 처리비용 */ 
	public String getHdlCost() { 
		return hdlCost;
	}

	public void setStrtDt(String strtDt) { 
		this.strtDt = strtDt;
	}
	/** 시작일 */ 
	public String getStrtDt() { 
		return strtDt;
	}

	public void setEndDt(String endDt) { 
		this.endDt = endDt;
	}
	/** 종료일 */ 
	public String getEndDt() { 
		return endDt;
	}

	public void setTestPichUid(String testPichUid) { 
		this.testPichUid = testPichUid;
	}
	/** 테스트담당자UID */ 
	public String getTestPichUid() { 
		return testPichUid;
	}

	public void setTestOngoCont(String testOngoCont) { 
		this.testOngoCont = testOngoCont;
	}
	/** 테스트진행내용 */ 
	public String getTestOngoCont() { 
		return testOngoCont;
	}

	public void setTestResCont(String testResCont) { 
		this.testResCont = testResCont;
	}
	/** 테스트결과내용 */ 
	public String getTestResCont() { 
		return testResCont;
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
	
	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}
	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	
	/** 담당자명 */ 
	public String getTestPichNm() {
		return testPichNm;
	}
	public void setTestPichNm(String testPichNm) {
		this.testPichNm = testPichNm;
	}
	
	/** 파일건수 */ 
	public Integer getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(Integer fileCnt) {
		this.fileCnt = fileCnt;
	}
	
	/** 이력여부 */ 
	public boolean isHst() {
		return isHst;
	}
	public void setHst(boolean isHst) {
		this.isHst = isHst;
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
			return "com.innobiz.orange.web.wh.dao.WhReqCmplDDao.selectWhReqCmplD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqCmplDDao.insertWhReqCmplD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqCmplDDao.updateWhReqCmplD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqCmplDDao.deleteWhReqCmplD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhReqCmplDDao.countWhReqCmplD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":요청완료상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(hstNo!=null) { if(tab!=null) builder.append(tab); builder.append("hstNo(이력번호):").append(hstNo).append('\n'); }		
		if(reqNo!=null) { if(tab!=null) builder.append(tab); builder.append("reqNo(요청번호):").append(reqNo).append('\n'); }
		if(cmplDt!=null) { if(tab!=null) builder.append(tab); builder.append("cmplDt(완료일시):").append(cmplDt).append('\n'); }
		if(hdlCont!=null) { if(tab!=null) builder.append(tab); builder.append("hdlCont(처리내용):").append(hdlCont).append('\n'); }
		if(devHourVa!=null) { if(tab!=null) builder.append(tab); builder.append("devHourVa(공수값):").append(devHourVa).append('\n'); }		
		if(hdlCompNm!=null) { if(tab!=null) builder.append(tab); builder.append("hdlCompNm(처리업체명):").append(hdlCompNm).append('\n'); }
		if(hdlCost!=null) { if(tab!=null) builder.append(tab); builder.append("hdlCost(처리비용):").append(hdlCost).append('\n'); }
		if(strtDt!=null) { if(tab!=null) builder.append(tab); builder.append("strtDt(시작일):").append(strtDt).append('\n'); }
		if(endDt!=null) { if(tab!=null) builder.append(tab); builder.append("endDt(종료일):").append(endDt).append('\n'); }
		if(testPichUid!=null) { if(tab!=null) builder.append(tab); builder.append("testPichUid(테스트담당자UID):").append(testPichUid).append('\n'); }
		if(testOngoCont!=null) { if(tab!=null) builder.append(tab); builder.append("testOngoCont(테스트진행내용):").append(testOngoCont).append('\n'); }
		if(testResCont!=null) { if(tab!=null) builder.append(tab); builder.append("testResCont(테스트결과내용):").append(testResCont).append('\n'); }
		super.toString(builder, tab);
	}

}
