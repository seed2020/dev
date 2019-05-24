package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/24 11:45 ******/
/**
* 항목표시상세(DM_ITEM_DISP_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmItemDispDVo extends CommonVoImpl {
	/** 항목ID */ 
	private String itemId;

 	/** 항목구분코드 */ 
	private String itemTypCd;

 	/** 회사ID */ 
	private String compId;

 	/** 사용여부 */ 
	private String useYn;

 	/** 속성ID */ 
	private String atrbId;

 	/** 넓이퍼센트 */ 
	private String wdthPerc;

 	/** 줄맞춤값 */ 
	private String alnVa;

 	/** 정렬옵션값 */ 
	private String sortOptVa;

 	/** 데이터정렬값 */ 
	private String dataSortVa;
	
	/** 등록표시여부 */ 
	private String regDispYn;

 	/** 수정표시여부 */ 
	private String modDispYn;
	
 	/** 조회표시여부 */ 
	private String readDispYn;

 	/** 목록표시여부 */ 
	private String listDispYn;

 	/** 목록표시순서 */ 
	private Integer listDispOrdr;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자 */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 항목 VO */
	private DmItemBVo colmVo;
	
	/** 항목 VO */
 	public DmItemBVo getColmVo() {
		return colmVo;
	}
	public void setColmVo(DmItemBVo colmVo) {
		this.colmVo = colmVo;
	}
	
 	public void setItemId(String itemId) { 
		this.itemId = itemId;
	}
	/** 항목ID */ 
	public String getItemId() { 
		return itemId;
	}

	public void setItemTypCd(String itemTypCd) { 
		this.itemTypCd = itemTypCd;
	}
	/** 항목구분코드 */ 
	public String getItemTypCd() { 
		return itemTypCd;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	public void setAtrbId(String atrbId) { 
		this.atrbId = atrbId;
	}
	/** 속성ID */ 
	public String getAtrbId() { 
		return atrbId;
	}

	public void setWdthPerc(String wdthPerc) { 
		this.wdthPerc = wdthPerc;
	}
	/** 넓이퍼센트 */ 
	public String getWdthPerc() { 
		return wdthPerc;
	}

	public void setAlnVa(String alnVa) { 
		this.alnVa = alnVa;
	}
	/** 줄맞춤값 */ 
	public String getAlnVa() { 
		return alnVa;
	}

	public void setSortOptVa(String sortOptVa) { 
		this.sortOptVa = sortOptVa;
	}
	/** 정렬옵션값 */ 
	public String getSortOptVa() { 
		return sortOptVa;
	}

	public void setDataSortVa(String dataSortVa) { 
		this.dataSortVa = dataSortVa;
	}
	/** 데이터정렬값 */ 
	public String getDataSortVa() { 
		return dataSortVa;
	}
	
	public void setRegDispYn(String regDispYn) { 
		this.regDispYn = regDispYn;
	}
	/** 등록표시여부 */ 
	public String getRegDispYn() { 
		return regDispYn;
	}

	public void setModDispYn(String modDispYn) { 
		this.modDispYn = modDispYn;
	}
	/** 수정표시여부 */ 
	public String getModDispYn() { 
		return modDispYn;
	}
	
	public void setReadDispYn(String readDispYn) { 
		this.readDispYn = readDispYn;
	}
	/** 조회표시여부 */ 
	public String getReadDispYn() { 
		return readDispYn;
	}

	public void setListDispYn(String listDispYn) { 
		this.listDispYn = listDispYn;
	}
	/** 목록표시여부 */ 
	public String getListDispYn() { 
		return listDispYn;
	}

	public void setListDispOrdr(Integer listDispOrdr) { 
		this.listDispOrdr = listDispOrdr;
	}
	/** 목록표시순서 */ 
	public Integer getListDispOrdr() { 
		return listDispOrdr;
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
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemDispDDao.selectDmItemDispD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemDispDDao.insertDmItemDispD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemDispDDao.updateDmItemDispD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemDispDDao.deleteDmItemDispD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmItemDispDDao.countDmItemDispD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":항목표시상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(itemId!=null) { if(tab!=null) builder.append(tab); builder.append("itemId(항목ID):").append(itemId).append('\n'); }
		if(itemTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("itemTypCd(항목구분코드):").append(itemTypCd).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(atrbId!=null) { if(tab!=null) builder.append(tab); builder.append("atrbId(속성ID):").append(atrbId).append('\n'); }
		if(wdthPerc!=null) { if(tab!=null) builder.append(tab); builder.append("wdthPerc(넓이퍼센트):").append(wdthPerc).append('\n'); }
		if(alnVa!=null) { if(tab!=null) builder.append(tab); builder.append("alnVa(줄맞춤값):").append(alnVa).append('\n'); }
		if(sortOptVa!=null) { if(tab!=null) builder.append(tab); builder.append("sortOptVa(정렬옵션값):").append(sortOptVa).append('\n'); }
		if(dataSortVa!=null) { if(tab!=null) builder.append(tab); builder.append("dataSortVa(데이터정렬값):").append(dataSortVa).append('\n'); }
		if(readDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("readDispYn(조회표시여부):").append(readDispYn).append('\n'); }
		if(listDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("listDispYn(목록표시여부):").append(listDispYn).append('\n'); }
		if(listDispOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("listDispOrdr(목록표시순서):").append(listDispOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
