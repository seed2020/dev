package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 용어설정상세(PT_TERM_SETUP_D) 테이블 VO
 */
public class PtTermSetupDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7831836602255220693L;

	/** 설정분류ID - KEY */
	private String setupClsId;

	/** 설정ID - KEY */
	private String setupId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 용어값 */
	private String termVa;

	/** 설정분류ID - KEY */
	public String getSetupClsId() {
		return setupClsId;
	}

	/** 설정분류ID - KEY */
	public void setSetupClsId(String setupClsId) {
		this.setupClsId = setupClsId;
	}

	/** 설정ID - KEY */
	public String getSetupId() {
		return setupId;
	}

	/** 설정ID - KEY */
	public void setSetupId(String setupId) {
		this.setupId = setupId;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 용어값 */
	public String getTermVa() {
		return termVa;
	}

	/** 용어값 */
	public void setTermVa(String termVa) {
		this.termVa = termVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtTermSetupDDao.selectPtTermSetupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtTermSetupDDao.insertPtTermSetupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtTermSetupDDao.updatePtTermSetupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtTermSetupDDao.deletePtTermSetupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtTermSetupDDao.countPtTermSetupD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":용어설정상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(setupClsId!=null) { if(tab!=null) builder.append(tab); builder.append("setupClsId(설정분류ID-PK):").append(setupClsId).append('\n'); }
		if(setupId!=null) { if(tab!=null) builder.append(tab); builder.append("setupId(설정ID-PK):").append(setupId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드-PK):").append(langTypCd).append('\n'); }
		if(termVa!=null) { if(tab!=null) builder.append(tab); builder.append("termVa(용어값):").append(termVa).append('\n'); }
		super.toString(builder, tab);
	}
}
