package com.innobiz.orange.web.ct.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_SURV_QUES_D [커뮤니티 설문 질문 상세] */
public class CtSurvQuesDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 5274555815914065107L;

	/**질문ID*/
	private String quesId;
	
	/**회사ID*/
	private String compId;
	
	/**설문ID*/
	private String survId;

	/**질문정렬순서*/
	private Integer quesSortOrdr;

	/**질문내용*/
	private String quesCont;
	
	/**질문이미지첨부여부*/
	private String quesImgAttYn;

	/**이미지파일ID*/
	private String imgSurvFileId;

	/**보기선택항목코드*/
	private String examChoiItemCd;

	/**복수선택여부*/
	private String mulChoiYn;
	
	//추가컬럼
	/** 주관식개수 */
	private String oendCount;
	
	public String getOendCount() {
		return oendCount;
	}

	public void setOendCount(String oendCount) {
		this.oendCount = oendCount;
	}
	
	/** 이미지저장경로 */
	private String imgSavePath;
	
	public String getImgSavePath() {
		return imgSavePath;
	}

	public void setImgSavePath(String imgSavePath) {
		this.imgSavePath = imgSavePath;
	}
	
	/** 필수답변여부 */
	private String mandaReplyYn;
	
	public String getMandaReplyYn() {
		return mandaReplyYn;
	}

	public void setMandaReplyYn(String mandaReplyYn) {
		this.mandaReplyYn = mandaReplyYn;
	}
	
	/** 답변 리스트 */
	private List<CtSurvReplyDVo> ctSurvReplyDVo;
	
	public List<CtSurvReplyDVo> getCtSurvReplyDVo() {
		return ctSurvReplyDVo;
	}

	public void setCtSurvReplyDVo(List<CtSurvReplyDVo> ctSurvReplyDVo) {
		this.ctSurvReplyDVo = ctSurvReplyDVo;
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

	public Integer getQuesSortOrdr() {
		return quesSortOrdr;
	}

	public void setQuesSortOrdr(Integer quesSortOrdr) {
		this.quesSortOrdr = quesSortOrdr;
	}

	public String getQuesCont() {
		return quesCont;
	}

	public void setQuesCont(String quesCont) {
		this.quesCont = quesCont;
	}

	public String getQuesImgAttYn() {
		return quesImgAttYn;
	}

	public void setQuesImgAttYn(String quesImgAttYn) {
		this.quesImgAttYn = quesImgAttYn;
	}

	public String getImgSurvFileId() {
		return imgSurvFileId;
	}

	public void setImgSurvFileId(String imgSurvFileId) {
		this.imgSurvFileId = imgSurvFileId;
	}

	public String getExamChoiItemCd() {
		return examChoiItemCd;
	}

	public void setExamChoiItemCd(String examChoiItemCd) {
		this.examChoiItemCd = examChoiItemCd;
	}

	public String getMulChoiYn() {
		return mulChoiYn;
	}

	public void setMulChoiYn(String mulChoiYn) {
		this.mulChoiYn = mulChoiYn;
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
		if(quesSortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("quesSortOrdr(질문정렬순서):").append(quesSortOrdr).append('\n'); }
		if(quesCont!=null) { if(tab!=null) builder.append(tab); builder.append("quesCont(질문내용):").append(quesCont).append('\n'); }
		if(quesImgAttYn!=null) { if(tab!=null) builder.append(tab); builder.append("quesImgAttYn(질문이미지첨부여부):").append(quesImgAttYn).append('\n'); }
		if(imgSurvFileId!=null) { if(tab!=null) builder.append(tab); builder.append("imgSurvFileId(이미지파일ID):").append(imgSurvFileId).append('\n'); }
		if(examChoiItemCd!=null) { if(tab!=null) builder.append(tab); builder.append("examChoiItemCd(보기선택항목코드):").append(examChoiItemCd).append('\n'); }
		if(mulChoiYn!=null) { if(tab!=null) builder.append(tab); builder.append("mulChoiYn(복수선택여부):").append(mulChoiYn).append('\n'); }
		if(oendCount!=null) { if(tab!=null) builder.append(tab); builder.append("oendCount(주관식개수):").append(oendCount).append('\n'); }
		if(imgSavePath!=null) { if(tab!=null) builder.append(tab); builder.append("imgSavePath(이미지저장경로):").append(imgSavePath).append('\n'); }
		if(mandaReplyYn!=null) { if(tab!=null) builder.append(tab); builder.append("mandaReplyYn(필수답변여부):").append(mandaReplyYn).append('\n'); }
		if(ctSurvReplyDVo!=null) { if(tab!=null) builder.append(tab); builder.append("ctSurvReplyDVo(답변 리스트):"); appendVoListTo(builder, ctSurvReplyDVo,tab); builder.append('\n'); }
		super.toString(builder, tab);
	}

}