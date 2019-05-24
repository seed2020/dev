package com.innobiz.orange.web.or.sync;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 조직도 DB 동기화 데이터
 */
public class OrDbSyncData extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8480635559259737971L;

	/** 회사ID */
	private String compId;

	/** 실행시간 */
	private String runTime;

	/** 최종실행년월일 */
	private String lastRunYmd;

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 실행시간 */
	public String getRunTime() {
		return runTime;
	}

	/** 실행시간 */
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	/** 최종실행년월일 */
	public String getLastRunYmd() {
		return lastRunYmd;
	}

	/** 최종실행년월일 */
	public void setLastRunYmd(String lastRunYmd) {
		this.lastRunYmd = lastRunYmd;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조직도 DB 동기화]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(runTime!=null) { if(tab!=null) builder.append(tab); builder.append("runTime(실행시간):").append(runTime).append('\n'); }
		if(lastRunYmd!=null) { if(tab!=null) builder.append(tab); builder.append("lastRunYmd(최종실행년월일):").append(lastRunYmd).append('\n'); }
		super.toString(builder, tab);
	}
}
