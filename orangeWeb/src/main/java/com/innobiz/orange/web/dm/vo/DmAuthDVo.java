package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/11 11:48 ******/
/**
* 문서권한상세(DM_AUTH_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmAuthDVo extends CommonVoImpl {
	/** 회사ID */ 
	private String compId;

 	/** 분류코드 */ 
	private String clsCd;

 	/** 코드 */ 
	private String cd;

 	/** 권한값 */ 
	private String authVa;

 	/** 사용여부 */ 
	private String useYn;

 	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setClsCd(String clsCd) { 
		this.clsCd = clsCd;
	}
	/** 분류코드 */ 
	public String getClsCd() { 
		return clsCd;
	}

	public void setCd(String cd) { 
		this.cd = cd;
	}
	/** 코드 */ 
	public String getCd() { 
		return cd;
	}

	public void setAuthVa(String authVa) { 
		this.authVa = authVa;
	}
	/** 권한값 */ 
	public String getAuthVa() { 
		return authVa;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmAuthDDao.selectDmAuthD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmAuthDDao.insertDmAuthD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmAuthDDao.updateDmAuthD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmAuthDDao.deleteDmAuthD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmAuthDDao.countDmAuthD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":문서권한상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(clsCd!=null) { if(tab!=null) builder.append(tab); builder.append("clsCd(분류코드):").append(clsCd).append('\n'); }
		if(cd!=null) { if(tab!=null) builder.append(tab); builder.append("cd(코드):").append(cd).append('\n'); }
		if(authVa!=null) { if(tab!=null) builder.append(tab); builder.append("authVa(권한값):").append(authVa).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		super.toString(builder, tab);
	}

}
