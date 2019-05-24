package com.innobiz.orange.web.cu.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018-11-20 16:07:25 ******/
/**
* 코드상세(WE_CD_D) 테이블 VO 
*/
public class WeCdDVo extends CommonVoImpl { 
	
	/** serialVersionUID */
	private static final long serialVersionUID = -2053913724911892381L;

	/** 코드ID */ 
	private String cdId;

 	/** 코드그룹ID */ 
	private String cdGrpId;

 	/** 코드명 */ 
	private String cdNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 사용여부 */ 
	private String useYn;

 	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 추가 */
	/** 코드리소스명 */ 
	private String cdRescNm;
	
	/** 코드그룹ID 목록 */ 
	private List<String> cdGrpIdList;
	
 	public void setCdId(String cdId) { 
		this.cdId = cdId;
	}
	/** 코드ID */ 
	public String getCdId() { 
		return cdId;
	}

	public void setCdGrpId(String cdGrpId) { 
		this.cdGrpId = cdGrpId;
	}
	/** 코드그룹ID */ 
	public String getCdGrpId() { 
		return cdGrpId;
	}

	public void setCdNm(String cdNm) { 
		this.cdNm = cdNm;
	}
	/** 코드명 */ 
	public String getCdNm() { 
		return cdNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setUseYn(String useYn) { 
		this.useYn = useYn;
	}
	/** 사용여부 */ 
	public String getUseYn() { 
		return useYn;
	}

	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** 코드리소스명 */ 
	public String getCdRescNm() {
		return cdRescNm;
	}
	public void setCdRescNm(String cdRescNm) {
		this.cdRescNm = cdRescNm;
	}
	
	/** 코드그룹ID 목록 */ 
	public List<String> getCdGrpIdList() {
		return cdGrpIdList;
	}
	public void setCdGrpIdList(List<String> cdGrpIdList) {
		this.cdGrpIdList = cdGrpIdList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.WeCdDDao.selectWeCdD";
		}else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.WeCdDDao.insertWeCdD";
		}else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.WeCdDDao.updateWeCdD";
		}else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.WeCdDDao.deleteWeCdD";
		}else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.WeCdDDao.countWeCdD";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":코드상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(cdId!=null) { if(tab!=null) builder.append(tab); builder.append("cdId(코드ID):").append(cdId).append('\n'); }
		if(cdGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("cdGrpId(코드그룹ID):").append(cdGrpId).append('\n'); }
		if(cdNm!=null) { if(tab!=null) builder.append(tab); builder.append("cdNm(코드명):").append(cdNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
