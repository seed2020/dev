package com.innobiz.orange.web.sh.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 키워드 VO
 */
public class ShKwdVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1149006646737644217L;
	
	public static ShKwdVo create(String kwd, String mdRid){
		if(kwd==null || kwd.isEmpty()) return null;
		
		ShKwdVo ins = new ShKwdVo();
		ins.kwd = kwd;
		
		int no = kwd.charAt(0) % 256;
		ins.partiNo = no<10 ? mdRid+"00"+no : no<100 ? mdRid+"0"+no : mdRid+no;
		return ins;
	}

	/** 키워드 */
	private String kwd;

	/** 파티션번호 */
	private String partiNo;

	/** 키워드 */
	public String getKwd() {
		return kwd;
	}

	/** 키워드 */
	public void setKwd(String kwd) {
		this.kwd = kwd;
	}

	/** 파티션번호 */
	public String getPartiNo() {
		return partiNo;
	}

	/** 파티션번호 */
	public void setPartiNo(String partiNo) {
		this.partiNo = partiNo;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":키워드 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(kwd!=null) { if(tab!=null) builder.append(tab); builder.append("kwd(키워드):").append(kwd).append('\n'); }
		if(partiNo!=null) { if(tab!=null) builder.append(tab); builder.append("partiNo(파티션번호):").append(partiNo).append('\n'); }
		super.toString(builder, tab);
	}
}
