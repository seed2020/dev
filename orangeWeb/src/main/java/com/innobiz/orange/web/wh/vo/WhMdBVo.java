package com.innobiz.orange.web.wh.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/04/14 14:55 ******/
/**
* 모듈기본(WH_MD_B) 테이블 VO 
*/
public class WhMdBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8771503072397737208L;

	/** 모듈ID */ 
	private String mdId;
	
	/** 회사ID */ 
	private String compId;

 	/** 모듈명 */ 
	private String mdNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 상위모듈ID */ 
	private String mdPid;
	
	/** 모듈구분코드 */ 
	private String mdTypCd;

 	/** 유형그룹ID */ 
	private String catGrpId;

 	/** 사용여부 */ 
	private String useYn;

 	/** 정렬순서 */ 
	private Integer sortOrdr;

 	/** 등록자 */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 추가 */
	/** 모듈ID 목록 */ 
	private List<String> mdIdList;
	
	/** 회사ID목록 */
	private List<String> compIdList;
	
	/** 변경할 회사ID */ 
	private String paramCompId;

 	public void setMdId(String mdId) { 
		this.mdId = mdId;
	}
	/** 모듈ID */ 
	public String getMdId() { 
		return mdId;
	}
	
	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}
	
	public void setMdNm(String mdNm) { 
		this.mdNm = mdNm;
	}
	/** 모듈명 */ 
	public String getMdNm() { 
		return mdNm;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setMdPid(String mdPid) { 
		this.mdPid = mdPid;
	}
	/** 상위모듈ID */ 
	public String getMdPid() { 
		return mdPid;
	}
	
	public void setMdTypCd(String mdTypCd) { 
		this.mdTypCd = mdTypCd;
	}
	/** 모듈구분코드 */ 
	public String getMdTypCd() { 
		return mdTypCd;
	}
	
	public void setCatGrpId(String catGrpId) { 
		this.catGrpId = catGrpId;
	}
	/** 유형그룹ID */ 
	public String getCatGrpId() { 
		return catGrpId;
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
	
	/** 모듈ID 목록 */ 
	public List<String> getMdIdList() {
		return mdIdList;
	}
	public void setMdIdList(List<String> mdIdList) {
		this.mdIdList = mdIdList;
	}
	
	/** 회사ID목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}
	
	/** 변경할 회사ID */ 
	public String getParamCompId() {
		return paramCompId;
	}
	public void setParamCompId(String paramCompId) {
		this.paramCompId = paramCompId;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdBDao.selectWhMdB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdBDao.insertWhMdB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdBDao.updateWhMdB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdBDao.deleteWhMdB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wh.dao.WhMdBDao.countWhMdB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":모듈기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }		
		if(mdNm!=null) { if(tab!=null) builder.append(tab); builder.append("mdNm(모듈명):").append(mdNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(mdPid!=null) { if(tab!=null) builder.append(tab); builder.append("mdPid(상위모듈ID):").append(mdPid).append('\n'); }
		if(mdTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("mdTypCd(모듈구분코드):").append(mdTypCd).append('\n'); }		
		if(catGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("catGrpId(유형그룹ID):").append(catGrpId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
