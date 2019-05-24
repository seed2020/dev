package com.innobiz.orange.web.wv.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** WV_SURV_QUES_D [설문 질문 상세] */
public class WvSurvQuesDVo extends CommonVoImpl{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 5181708937648479992L;
	
	/**질문ID*/
	private String quesId;


	public String getQuesId() {
		return quesId;
	}

	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}
	
	/**회사ID*/
	private String compId;


	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/**설문ID*/
	private String survId;


	public String getSurvId() {
		return survId;
	}

	public void setSurvId(String survId) {
		this.survId = survId;
	}
	
	/**질문정렬순서*/
	private int quesSortOrdr;


	public int getQuesSortOrdr() {
		return quesSortOrdr;
	}

	public void setQuesSortOrdr(int quesSortOrdr) {
		this.quesSortOrdr = quesSortOrdr;
	}
	
	/**질문내용*/
	private String quesCont;


	public String getQuesCont() {
		return quesCont;
	}

	public void setQuesCont(String quesCont) {
		this.quesCont = quesCont;
	}
	
	/**질문이미지첨부여부*/
	private String quesImgAttYn;


	public String getQuesImgAttYn() {
		return quesImgAttYn;
	}

	public void setQuesImgAttYn(String quesImgAttYn) {
		this.quesImgAttYn = quesImgAttYn;
	}
	
	/**이미지파일ID*/
	private String imgSurvFileId;


	public String getImgSurvFileId() {
		return imgSurvFileId;
	}

	public void setImgSurvFileId(String imgSurvFileId) {
		this.imgSurvFileId = imgSurvFileId;
	}
	
	/**보기선택항목코드*/
	private String examChoiItemCd;


	public String getExamChoiItemCd() {
		return examChoiItemCd;
	}

	public void setExamChoiItemCd(String examChoiItemCd) {
		this.examChoiItemCd = examChoiItemCd;
	}
	
	/**복수선택여부*/
	private String mulChoiYn;


	public String getMulChoiYn() {
		return mulChoiYn;
	}

	public void setMulChoiYn(String mulChoiYn) {
		this.mulChoiYn = mulChoiYn;
	}
	
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
	private List<WvSurvReplyDVo> wvSurvReplyDVo;
	
	public List<WvSurvReplyDVo> getWvSurvReplyDVo() {
		return wvSurvReplyDVo;
	}

	public void setWvSurvReplyDVo(List<WvSurvReplyDVo> wvSurvReplyDVo) {
		this.wvSurvReplyDVo = wvSurvReplyDVo;
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
		builder.append('[').append(this.getClass().getName()).append(":설문 질문 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(quesId!=null) { if(tab!=null) builder.append(tab); builder.append("quesId(질문ID):").append(quesId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(quesSortOrdr!=0) { if(tab!=null) builder.append(tab); builder.append("quesSortOrdr(질문정렬순서):").append(quesSortOrdr).append('\n'); }
		if(quesCont!=null) { if(tab!=null) builder.append(tab); builder.append("quesCont(질문내용):").append(quesCont).append('\n'); }
		if(quesImgAttYn!=null) { if(tab!=null) builder.append(tab); builder.append("quesImgAttYn(질문이미지첨부여부):").append(quesImgAttYn).append('\n'); }
		if(imgSurvFileId!=null) { if(tab!=null) builder.append(tab); builder.append("imgSurvFileId(이미지파일ID):").append(imgSurvFileId).append('\n'); }
		if(examChoiItemCd!=null) { if(tab!=null) builder.append(tab); builder.append("examChoiItemCd(보기선택항목코드):").append(examChoiItemCd).append('\n'); }
		if(mulChoiYn!=null) { if(tab!=null) builder.append(tab); builder.append("mulChoiYn(복수선택여부):").append(mulChoiYn).append('\n'); }
		if(oendCount!=null) { if(tab!=null) builder.append(tab); builder.append("oendCount(주관식개수):").append(oendCount).append('\n'); }
		if(imgSavePath!=null) { if(tab!=null) builder.append(tab); builder.append("imgSavePath(이미지저장경로):").append(imgSavePath).append('\n'); }
		if(mandaReplyYn!=null) { if(tab!=null) builder.append(tab); builder.append("mandaReplyYn(필수답변여부):").append(mandaReplyYn).append('\n'); }
		if(wvSurvReplyDVo!=null) { if(tab!=null) builder.append(tab); builder.append("wvSurvReplyDVo(답변 리스트):"); appendVoListTo(builder, wvSurvReplyDVo,tab); builder.append('\n'); }
		super.toString(builder, tab);
	}

}