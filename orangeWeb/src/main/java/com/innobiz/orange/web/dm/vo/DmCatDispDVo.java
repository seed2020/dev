package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/08 15:51 ******/
/**
* 유형표시상세(DM_CAT_DISP_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmCatDispDVo extends DmItemDispDVo {
	/** 저장소ID */ 
	private String storId;

 	/** 유형ID */ 
	private String catId;

 	/** 추가항목여부 */ 
	private String addItemYn;
	
	public void setStorId(String storId) { 
		this.storId = storId;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setCatId(String catId) { 
		this.catId = catId;
	}
	/** 유형ID */ 
	public String getCatId() { 
		return catId;
	}

	public void setAddItemYn(String addItemYn) { 
		this.addItemYn = addItemYn;
	}
	/** 추가항목여부 */ 
	public String getAddItemYn() { 
		return addItemYn;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatDispDDao.selectDmCatDispD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatDispDDao.insertDmCatDispD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatDispDDao.updateDmCatDispD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatDispDDao.deleteDmCatDispD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatDispDDao.countDmCatDispD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":유형표시상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(유형ID):").append(catId).append('\n'); }
		super.toString(builder, tab);
	}

}
