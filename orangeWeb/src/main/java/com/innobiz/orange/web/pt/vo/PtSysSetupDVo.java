package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 시스템설정상세(PT_SYS_SETUP_D) 테이블 VO
 */
public class PtSysSetupDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5474991340606918581L;

	/** 설정분류ID - KEY */
	private String setupClsId;

	/** 설정ID - KEY */
	private String setupId;

	/** 설정값 */
	private String setupVa;

	/** 정렬순서 */
	private String sortOrdr;

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

	/** 설정값 */
	public String getSetupVa() {
		return setupVa;
	}

	/** 설정값 */
	public void setSetupVa(String setupVa) {
		this.setupVa = setupVa;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSysSetupDDao.selectPtSysSetupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSysSetupDDao.insertPtSysSetupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSysSetupDDao.updatePtSysSetupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSysSetupDDao.deletePtSysSetupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtSysSetupDDao.countPtSysSetupD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":시스템설정상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(setupClsId!=null) { if(tab!=null) builder.append(tab); builder.append("setupClsId(설정분류ID-PK):").append(setupClsId).append('\n'); }
		if(setupId!=null) { if(tab!=null) builder.append(tab); builder.append("setupId(설정ID-PK):").append(setupId).append('\n'); }
		if(setupVa!=null) { if(tab!=null) builder.append(tab); builder.append("setupVa(설정값):").append(setupVa).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}
}
