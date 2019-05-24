package com.innobiz.orange.web.wb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/****** Object:  Vo - Date: 2016/03/17 17:40 ******/
/**
* 공용폴더(WB_PUB_FLD_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class WbPubFldBVo extends CommonVoImpl {
	/** 폴더ID */ 
	private String fldId;

 	/** 리소스ID */ 
	private String rescId;

 	/** 상위폴더ID */ 
	private String fldPid;

 	/** 사용여부 */ 
	private String useYn;
	
	/** 상위폴더명 */
	private String fldPnm;

 	public void setFldId(String fldId) { 
		this.fldId = fldId;
	}
	/** 폴더ID */ 
	public String getFldId() { 
		return fldId;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setFldPid(String fldPid) { 
		this.fldPid = fldPid;
	}
	/** 상위폴더ID */ 
	public String getFldPid() { 
		return fldPid;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}
	
	/** 상위폴더명 */
	public String getFldPnm() {
		return fldPnm;
	}
	public void setFldPnm(String fldPnm) {
		this.fldPnm = fldPnm;
	}
	
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":공유폴더]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(fldId!=null) { if(tab!=null) builder.append(tab); builder.append("fldId(폴더ID):").append(fldId).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(fldPid!=null) { if(tab!=null) builder.append(tab); builder.append("fldPid(상위폴더ID):").append(fldPid).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		super.toString(builder, tab);
	}

}
