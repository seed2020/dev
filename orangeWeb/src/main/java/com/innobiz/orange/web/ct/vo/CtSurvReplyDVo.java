package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_SURV_REPLY_D[커뮤니티 설문 답변 상세 */
public class CtSurvReplyDVo extends CommonVoImpl  implements Cloneable{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -7918495525464806789L;

	/** 질문ID */
	private String quesId;
	
	/** 회사ID */
	private String compId;
	
	/** 설문ID */
	private String survId;
		
	/** 답변번호 */
	private Integer replyNo;
		
	/** 답변자ID */
	private String replyrUid;
		
	/** 답변자 부서 ID */
	private String replyrDeptId;
	
	/** 답변일시 */
	private String replyDt;
		
	/** 객관식입력항목답변내용 */
	private String mulcInputReplyCont;
		
	/** 주관식답변내용 */
	private String oendReplyCont;
	
	//추가 컬럼
	
	/** 질문보기List */
	public List<CtQuesExamDVo> quesExam;
	
	public List<CtQuesExamDVo> getQuesExam() {
		return quesExam;
	}

	public void setQuesExam(List<CtQuesExamDVo> quesExam) {
		this.quesExam = quesExam;
	}

	/** 부서별총합계 */
	private String deptTotalCount;

	public String getDeptTotalCount() {
		return deptTotalCount;
	}

	public void setDeptTotalCount(String deptTotalCount) {
		this.deptTotalCount = deptTotalCount;
	}
	
	/** 객관식답변 여부 */
	private String mulcInputReplyContYn;
	
	public String getMulcInputReplyContYn() {
		return mulcInputReplyContYn;
	}

	public void setMulcInputReplyContYn(String mulcInputReplyContYn) {
		this.mulcInputReplyContYn = mulcInputReplyContYn;
	}
	
	
	public String getQuesId() {
		return quesId;
	}

	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getSurvId() {
		return survId;
	}

	public void setSurvId(String survId) {
		this.survId = survId;
	}

	public Integer getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(Integer replyNo) {
		this.replyNo = replyNo;
	}

	public String getReplyrUid() {
		return replyrUid;
	}

	public void setReplyrUid(String replyrUid) {
		this.replyrUid = replyrUid;
	}

	public String getReplyrDeptId() {
		return replyrDeptId;
	}

	public void setReplyrDeptId(String replyrDeptId) {
		this.replyrDeptId = replyrDeptId;
	}

	public String getReplyDt() {
		return replyDt;
	}

	public void setReplyDt(String replyDt) {
		if(replyDt != null && replyDt.endsWith(".0") && replyDt.length()>2)
			 replyDt=replyDt.substring(0, replyDt.length()-2);
		this.replyDt = replyDt;
	}

	public String getMulcInputReplyCont() {
		return mulcInputReplyCont;
	}

	public void setMulcInputReplyCont(String mulcInputReplyCont) {
		this.mulcInputReplyCont = mulcInputReplyCont;
	}

	public String getOendReplyCont() {
		return oendReplyCont;
	}

	public void setOendReplyCont(String oendReplyCont) {
		this.oendReplyCont = oendReplyCont;
	}
	
	public Object clone() throws CloneNotSupportedException {
		 CtSurvReplyDVo a = (CtSurvReplyDVo)super.clone();
		  return a;
		
	}

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(quesId!=null) { if(tab!=null) builder.append(tab); builder.append("quesId(질문ID):").append(quesId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(replyNo!=null) { if(tab!=null) builder.append(tab); builder.append("replyNo(답변번호):").append(replyNo).append('\n'); }
		if(replyrUid!=null) { if(tab!=null) builder.append(tab); builder.append("replyrUid(답변자ID):").append(replyrUid).append('\n'); }
		if(replyrDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("replyrDeptId(답변자 부서 ID):").append(replyrDeptId).append('\n'); }
		if(replyDt!=null) { if(tab!=null) builder.append(tab); builder.append("replyDt(답변일시):").append(replyDt).append('\n'); }
		if(mulcInputReplyCont!=null) { if(tab!=null) builder.append(tab); builder.append("mulcInputReplyCont(객관식입력항목답변내용):").append(mulcInputReplyCont).append('\n'); }
		if(oendReplyCont!=null) { if(tab!=null) builder.append(tab); builder.append("oendReplyCont(주관식답변내용):").append(oendReplyCont).append('\n'); }
		if(deptTotalCount!=null) { if(tab!=null) builder.append(tab); builder.append("deptTotalCount(부서별총합계):").append(deptTotalCount).append('\n'); }
		if(mulcInputReplyContYn!=null) { if(tab!=null) builder.append(tab); builder.append("mulcInputReplyContYn(객관식답변 여부):").append(mulcInputReplyContYn).append('\n'); }
		super.toString(builder, tab);
	}
	

}