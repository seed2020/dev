package com.innobiz.orange.web.dm.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/04 11:42 ******/
/**
* 항목기본(DM_ITEM_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmItemBVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;
	
	/** 항목ID */ 
	private String itemId;

 	/** 항목명 */ 
	private String itemNm;

 	/** 항목표시명 */ 
	private String itemDispNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 데이터타입 */ 
	private String dataTyp;

 	/** 길이 */ 
	private Integer colmLen;

 	/** 항목구분 */ 
	private String itemTyp;

 	/** 항목구분값 */ 
	private String itemTypVa;

 	/** 사용여부 */ 
	private String useYn;
	
	/** 추가항목여부 */ 
	private String addItemYn;
	
 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 등록자명 */
	private String regrNm;
	
	/** 등록표시여부 */ 
	private String regDispYn;

 	/** 수정표시여부 */ 
	private String modDispYn;

 	/** 조회표시여부 */ 
	private String readDispYn;

 	/** 목록표시여부 */ 
	private String listDispYn;
	
	/** 코드 목록 */ 
	private List<DmCdDVo> cdList;
	
	/** 등록표시여부 */ 
	public String getRegDispYn() {
		return regDispYn;
	}
	public void setRegDispYn(String regDispYn) {
		this.regDispYn = regDispYn;
	}
	/** 수정표시여부 */ 
	public String getModDispYn() {
		return modDispYn;
	}
	public void setModDispYn(String modDispYn) {
		this.modDispYn = modDispYn;
	}
	/** 조회표시여부 */ 
	public String getReadDispYn() {
		return readDispYn;
	}
	public void setReadDispYn(String readDispYn) {
		this.readDispYn = readDispYn;
	}
	/** 목록표시여부 */ 
	public String getListDispYn() {
		return listDispYn;
	}
	public void setListDispYn(String listDispYn) {
		this.listDispYn = listDispYn;
	}
	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	/** 저장소ID */ 
 	public String getStorId() {
		return storId;
	}
	public void setStorId(String storId) {
		this.storId = storId;
	}
	public void setItemId(String itemId) { 
		this.itemId = itemId;
	}
	/** 항목ID */ 
	public String getItemId() { 
		return itemId;
	}

	public void setItemNm(String itemNm) { 
		this.itemNm = itemNm;
	}
	/** 항목명 */ 
	public String getItemNm() { 
		return itemNm;
	}

	public void setItemDispNm(String itemDispNm) { 
		this.itemDispNm = itemDispNm;
	}
	/** 항목표시명 */ 
	public String getItemDispNm() { 
		return itemDispNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setDataTyp(String dataTyp) { 
		this.dataTyp = dataTyp;
	}
	/** 데이터타입 */ 
	public String getDataTyp() { 
		return dataTyp;
	}

	public void setColmLen(Integer colmLen) { 
		this.colmLen = colmLen;
	}
	/** 길이 */ 
	public Integer getColmLen() { 
		return colmLen;
	}

	public void setItemTyp(String itemTyp) { 
		this.itemTyp = itemTyp;
	}
	/** 항목구분 */ 
	public String getItemTyp() { 
		return itemTyp;
	}

	public void setItemTypVa(String itemTypVa) { 
		this.itemTypVa = itemTypVa;
	}
	/** 항목구분값 */ 
	public String getItemTypVa() { 
		return itemTypVa;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}
	
	public void setAddItemYn(String addItemYn) { 
		this.addItemYn = addItemYn;
	}
	/** 추가항목여부 */ 
	public String getAddItemYn() { 
		return addItemYn;
	}
	
	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}
	/** 등록자 */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
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
	
	/** 코드 목록 */ 
	public List<DmCdDVo> getCdList() {
		return cdList;
	}
	public void setCdList(List<DmCdDVo> cdList) {
		this.cdList = cdList;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemBDao.selectDmItemB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemBDao.insertDmItemB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemBDao.updateDmItemB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemBDao.deleteDmItemB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemBDao.countDmItemB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":항목기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(itemId!=null) { if(tab!=null) builder.append(tab); builder.append("itemId(항목ID):").append(itemId).append('\n'); }
		if(itemNm!=null) { if(tab!=null) builder.append(tab); builder.append("itemNm(항목명):").append(itemNm).append('\n'); }
		if(itemDispNm!=null) { if(tab!=null) builder.append(tab); builder.append("itemDispNm(항목표시명):").append(itemDispNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(dataTyp!=null) { if(tab!=null) builder.append(tab); builder.append("dataTyp(데이터타입):").append(dataTyp).append('\n'); }
		if(colmLen!=null) { if(tab!=null) builder.append(tab); builder.append("colmLen(길이):").append(colmLen).append('\n'); }
		if(itemTyp!=null) { if(tab!=null) builder.append(tab); builder.append("itemTyp(항목구분):").append(itemTyp).append('\n'); }
		if(itemTypVa!=null) { if(tab!=null) builder.append(tab); builder.append("itemTypVa(항목구분값):").append(itemTypVa).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(addItemYn!=null) { if(tab!=null) builder.append(tab); builder.append("addItemYn(추가항목여부):").append(addItemYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(regDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("regDispYn(등록표시여부):").append(regDispYn).append('\n'); }
		if(modDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("modDispYn(수정표시여부):").append(modDispYn).append('\n'); }
		if(readDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("readDispYn(조회표시여부):").append(readDispYn).append('\n'); }
		if(listDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("listDispYn(목록표시여부):").append(listDispYn).append('\n'); }
		
		super.toString(builder, tab);
	}

}
