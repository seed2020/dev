package com.innobiz.orange.web.wv.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** WV_QUES_EXAM_D [질문 보기 상세] */
public class WvQuesExamDVo extends CommonVoImpl{

	/** serialVersionUID */
	private static final long serialVersionUID = 638268535558810484L;
	
	/** 질문ID */
	private String quesId;


	public String getQuesId() {
		return quesId;
	}

	public void setQuesId(String quesId) {
		this.quesId = quesId;
	}
	
	/** 회사ID */
	private String compId;


	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 설문ID */
	private String survId;


	public String getSurvId() {
		return survId;
	}

	public void setSurvId(String survId) {
		this.survId = survId;
	}
	
	/** 보기번호 */
	private int examNo;


	public int getExamNo() {
		return examNo;
	}

	public void setExamNo(int examNo) {
		this.examNo = examNo;
	}
	
	/** 보기순서 */
	private int examOrdr;


	public int getExamOrdr() {
		return examOrdr;
	}

	public void setExamOrdr(int examOrdr) {
		this.examOrdr = examOrdr;
	}
	
	/** 보기표시명 */
	private String examDispNm;


	public String getExamDispNm() {
		return examDispNm;
	}

	public void setExamDispNm(String examDispNm) {
		this.examDispNm = examDispNm;
	}
	
	/** 입력 여부 */
	private String inputYn;


	public String getInputYn() {
		return inputYn;
	}

	public void setInputYn(String inputYn) {
		this.inputYn = inputYn;
	}
	
	/** 보기이미지사용여부 */
	private String examImgUseYn;


	public String getExamImgUseYn() {
		return examImgUseYn;
	}

	public void setExamImgUseYn(String examImgUseYn) {
		this.examImgUseYn = examImgUseYn;
	}
	
	/** 이미지파일ID */
	private String imgSurvFileId;


	public String getImgSurvFileId() {
		return imgSurvFileId;
	}

	public void setImgSurvFileId(String imgSurvFileId) {
		this.imgSurvFileId = imgSurvFileId;
	}
	
	/** 선택된개수 */
	private String selectCount;
	
	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}
	
	/** 질문평균 */
	private String quesAverage;
	
	public String getQuesAverage() {
		return quesAverage;
	}

	public void setQuesAverage(String quesAverage) {
		this.quesAverage = quesAverage;
	}
	
	/** 이미지저장경로 */
	private String imgSavePath;
	
	public String getImgSavePath() {
		return imgSavePath;
	}

	public void setImgSavePath(String imgSavePath) {
		this.imgSavePath = imgSavePath;
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
		builder.append('[').append(this.getClass().getName()).append(":질문 보기 상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(quesId!=null) { if(tab!=null) builder.append(tab); builder.append("quesId(질문ID):").append(quesId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(examNo!=0) { if(tab!=null) builder.append(tab); builder.append("examNo(보기번호):").append(examNo).append('\n'); }
		if(examOrdr!=0) { if(tab!=null) builder.append(tab); builder.append("examOrdr(보기순서):").append(examOrdr).append('\n'); }
		if(examDispNm!=null) { if(tab!=null) builder.append(tab); builder.append("examDispNm(보기표시명):").append(examDispNm).append('\n'); }
		if(inputYn!=null) { if(tab!=null) builder.append(tab); builder.append("inputYn(입력 여부):").append(inputYn).append('\n'); }
		if(examImgUseYn!=null) { if(tab!=null) builder.append(tab); builder.append("examImgUseYn(보기이미지사용여부):").append(examImgUseYn).append('\n'); }
		if(imgSurvFileId!=null) { if(tab!=null) builder.append(tab); builder.append("imgSurvFileId(이미지파일ID):").append(imgSurvFileId).append('\n'); }
		if(selectCount!=null) { if(tab!=null) builder.append(tab); builder.append("selectCount(선택된개수):").append(selectCount).append('\n'); }
		if(quesAverage!=null) { if(tab!=null) builder.append(tab); builder.append("quesAverage(질문평균):").append(quesAverage).append('\n'); }
		if(imgSavePath!=null) { if(tab!=null) builder.append(tab); builder.append("imgSavePath(이미지저장경로):").append(imgSavePath).append('\n'); }
		super.toString(builder, tab);
	}

}