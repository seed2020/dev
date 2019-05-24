package com.innobiz.orange.web.ap.down;

/** 다운로드용 데이터 */
public class ApDownData {

	/** 결재번호 */
	public String apvNo;
	/** 파일명 */
	public String fileName;
	/** 생성자 */
	public ApDownData(String apvNo, String fileName){
		this.apvNo = apvNo;
		this.fileName = fileName;
	}
}
