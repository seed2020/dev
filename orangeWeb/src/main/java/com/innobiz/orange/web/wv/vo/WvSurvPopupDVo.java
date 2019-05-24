package com.innobiz.orange.web.wv.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/11/07 15:11 ******/
/**
* 팝업설정상세(WV_SURV_POPUP_D) 테이블 VO 
*/
public class WvSurvPopupDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1773484837630750057L;

	/** 설문ID */ 
	private String survId;

 	/** 회사ID */ 
	private String compId;

 	/** 넓이값 */ 
	private String wdthVa;

 	/** 사용여부 */ 
	private String useYn;

 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	public void setSurvId(String survId) { 
		this.survId = survId;
	}
	/** 설문ID */ 
	public String getSurvId() { 
		return survId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setWdthVa(String wdthVa) { 
		this.wdthVa = wdthVa;
	}
	/** 넓이값 */ 
	public String getWdthVa() { 
		return wdthVa;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	public void setModrUid(String modrUid) { 
		this.modrUid = modrUid;
	}
	/** 수정자 */ 
	public String getModrUid() { 
		return modrUid;
	}

	public void setModDt(String modDt) { 
		this.modDt = modDt;
	}
	/** 수정일시 */ 
	public String getModDt() { 
		return modDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wv.dao.WvSurvPopupDDao.selectWvSurvPopupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wv.dao.WvSurvPopupDDao.insertWvSurvPopupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wv.dao.WvSurvPopupDDao.updateWvSurvPopupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wv.dao.WvSurvPopupDDao.deleteWvSurvPopupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wv.dao.WvSurvPopupDDao.countWvSurvPopupD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":팝업설정상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(survId!=null) { if(tab!=null) builder.append(tab); builder.append("survId(설문ID):").append(survId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(wdthVa!=null) { if(tab!=null) builder.append(tab); builder.append("wdthVa(넓이값):").append(wdthVa).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
