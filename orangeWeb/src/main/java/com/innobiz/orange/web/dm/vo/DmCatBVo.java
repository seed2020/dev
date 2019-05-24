package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/01 17:41 ******/
/**
* 유형기본(DM_CAT_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmCatBVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;
	
	/** 유형ID */ 
	private String catId;

 	/** 유형명 */ 
	private String catNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 설명 */ 
	private String catDesc;

 	/** 심의여부 */ 
	private String discYn;

 	/** 심의자UID */ 
	private String discrUid;

 	/** 작성자표시여부 */ 
	private String regrDispYn;

 	/** 소유자표시여부 */ 
	private String ownrDispYn;

 	/** 항목사용여부 */ 
	private String itemUseYn;
	
	/** 기본여부 */ 
	private String dftYn;

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
	
	/** 심의자명 */
	private String discrNm;
	
	/** 테이블 suffix */
	private String tblSuffix;
	
	/** 테이블 suffix */
	public String getTblSuffix() {
		return tblSuffix;
	}
	public void setTblSuffix(String tblSuffix) {
		this.tblSuffix = tblSuffix;
	}
	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	/** 심의자명 */
 	public String getDiscrNm() {
		return discrNm;
	}
	public void setDiscrNm(String discrNm) {
		this.discrNm = discrNm;
	}
	public void setCatId(String catId) { 
		this.catId = catId;
	}
	/** 유형ID */ 
	public String getCatId() { 
		return catId;
	}

	public void setStorId(String storId) { 
		this.storId = storId;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setCatNm(String catNm) { 
		this.catNm = catNm;
	}
	/** 유형명 */ 
	public String getCatNm() { 
		return catNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setCatDesc(String catDesc) { 
		this.catDesc = catDesc;
	}
	/** 설명 */ 
	public String getCatDesc() { 
		return catDesc;
	}

	public void setDiscYn(String discYn) { 
		this.discYn = discYn;
	}
	/** 심의여부 */ 
	public String getDiscYn() { 
		return discYn;
	}

	public void setDiscrUid(String discrUid) { 
		this.discrUid = discrUid;
	}
	/** 심의자UID */ 
	public String getDiscrUid() { 
		return discrUid;
	}

	public void setRegrDispYn(String regrDispYn) { 
		this.regrDispYn = regrDispYn;
	}
	/** 작성자표시여부 */ 
	public String getRegrDispYn() { 
		return regrDispYn;
	}

	public void setOwnrDispYn(String ownrDispYn) { 
		this.ownrDispYn = ownrDispYn;
	}
	/** 소유자표시여부 */ 
	public String getOwnrDispYn() { 
		return ownrDispYn;
	}

	public void setItemUseYn(String itemUseYn) { 
		this.itemUseYn = itemUseYn;
	}
	/** 항목사용여부 */ 
	public String getItemUseYn() { 
		return itemUseYn;
	}

	/** 기본여부 */ 
	public String getDftYn() {
		return dftYn;
	}
	public void setDftYn(String dftYn) {
		this.dftYn = dftYn;
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
			return "com.innobiz.orange.web.dm.dao.DmCatBDao.selectDmCatB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatBDao.insertDmCatB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatBDao.updateDmCatB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatBDao.deleteDmCatB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmCatBDao.countDmCatB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":유형기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(catId!=null) { if(tab!=null) builder.append(tab); builder.append("catId(유형ID):").append(catId).append('\n'); }
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(catNm!=null) { if(tab!=null) builder.append(tab); builder.append("catNm(유형명):").append(catNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(catDesc!=null) { if(tab!=null) builder.append(tab); builder.append("catDesc(설명):").append(catDesc).append('\n'); }
		if(discYn!=null) { if(tab!=null) builder.append(tab); builder.append("discYn(심의여부):").append(discYn).append('\n'); }
		if(discrUid!=null) { if(tab!=null) builder.append(tab); builder.append("discrUid(심의자UID):").append(discrUid).append('\n'); }
		if(regrDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("regrDispYn(작성자표시여부):").append(regrDispYn).append('\n'); }
		if(ownrDispYn!=null) { if(tab!=null) builder.append(tab); builder.append("ownrDispYn(소유자표시여부):").append(ownrDispYn).append('\n'); }
		if(itemUseYn!=null) { if(tab!=null) builder.append(tab); builder.append("itemUseYn(항목사용여부):").append(itemUseYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(discrNm!=null) { if(tab!=null) builder.append(tab); builder.append("discrNm(심의자명):").append(discrNm).append('\n'); }
		super.toString(builder, tab);
	}

}
