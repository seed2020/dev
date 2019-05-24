package com.innobiz.orange.web.dm.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/15 13:55 ******/
/**
* 분류체계기본(DM_CLS_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmClsBVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;

 	/** 분류체계ID */ 
	private String clsId;

 	/** 분류체계명 */ 
	private String clsNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 상위분류체계ID */ 
	private String clsPid;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 사용여부 */ 
	private String useYn;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 선택ID 목록 */ 
	private List<String> selIdList;
	
	/** 선택ID 목록 */ 
	public List<String> getSelIdList() {
		return selIdList;
	}

	public void setSelIdList(List<String> selIdList) {
		this.selIdList = selIdList;
	}
	
 	public void setStorId(String storId) { 
		this.storId = storId;
	}

	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setClsId(String clsId) { 
		this.clsId = clsId;
	}
	/** 분류체계ID */ 
	public String getClsId() { 
		return clsId;
	}

	public void setClsNm(String clsNm) { 
		this.clsNm = clsNm;
	}
	/** 분류체계명 */ 
	public String getClsNm() { 
		return clsNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setClsPid(String clsPid) { 
		this.clsPid = clsPid;
	}
	/** 상위분류체계ID */ 
	public String getClsPid() { 
		return clsPid;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
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
	/** 수정자UID */ 
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
			return "com.innobiz.orange.web.dm.dao.DmClsBDao.selectDmClsB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsBDao.insertDmClsB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsBDao.updateDmClsB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsBDao.deleteDmClsB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmClsBDao.countDmClsB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":분류체계기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(clsId!=null) { if(tab!=null) builder.append(tab); builder.append("clsId(분류체계ID):").append(clsId).append('\n'); }
		if(clsNm!=null) { if(tab!=null) builder.append(tab); builder.append("clsNm(분류체계명):").append(clsNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(clsPid!=null) { if(tab!=null) builder.append(tab); builder.append("clsPid(상위분류체계ID):").append(clsPid).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
