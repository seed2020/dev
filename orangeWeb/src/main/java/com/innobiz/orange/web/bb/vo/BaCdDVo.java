package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** 코드상세(BA_CD_D) 테이블 VO */
@SuppressWarnings("serial")
public class BaCdDVo extends CommonVoImpl {
	/** 코드그룹ID */ 
	private String cdGrpId;

 	/** 코드ID */ 
	private String cdId;

 	/** 코드값 */ 
	private String cdVa;

 	/** 리소스ID */ 
	private String rescId;

 	/** 정렬순서 */ 
	private String sortOrdr;

 	/** 사용여부 */ 
	private String useYn;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 리소스명 */
	private String rescNm;

	/** 유효성 체크용 속성 */
	private String valid;

	/** 삭제여부 확인용 속성 */
	private String deleted;
	
	/** 리소스명 */
 	public String getRescNm() {
		return rescNm;
	}
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}
	
	/** 유효성 체크용 속성 */
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	
	/** 삭제여부 확인용 속성 */
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public void setCdGrpId(String cdGrpId) { 
		this.cdGrpId = cdGrpId;
	}
	/** 코드그룹ID */ 
	public String getCdGrpId() { 
		return cdGrpId;
	}

	public void setCdId(String cdId) { 
		this.cdId = cdId;
	}
	/** 코드ID */ 
	public String getCdId() { 
		return cdId;
	}

	public void setCdVa(String cdVa) { 
		this.cdVa = cdVa;
	}
	/** 코드값 */ 
	public String getCdVa() { 
		return cdVa;
	}

	public void setRescId(String rescId) { 
		this.rescId = rescId;
	}
	/** 리소스ID */ 
	public String getRescId() { 
		return rescId;
	}

	public void setSortOrdr(String sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public String getSortOrdr() { 
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
	/** 등록자UID */ 
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
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdDDao.selectBaCdD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdDDao.insertBaCdD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdDDao.updateBaCdD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdDDao.deleteBaCdD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaCdDDao.countBaCdD";
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
		if(cdGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("cdGrpId(코드그룹ID):").append(cdGrpId).append('\n'); }
		if(cdId!=null) { if(tab!=null) builder.append(tab); builder.append("cdId(코드ID):").append(cdId).append('\n'); }
		if(cdVa!=null) { if(tab!=null) builder.append(tab); builder.append("cdVa(코드값):").append(cdVa).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
