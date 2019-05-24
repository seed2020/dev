package com.innobiz.orange.web.dm.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/02 14:46 ******/
/**
* 저장소기본(DM_STOR_B) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmStorBVo extends CommonVoImpl {
	/** 저장소ID */ 
	private String storId;

 	/** 회사ID */ 
	private String compId;
	
 	/** 테이블명 */ 
	private String tblNm;

 	/** 저장소명 */ 
	private String storNm;

 	/** 리소스ID */ 
	private String rescId;

 	/** 사용여부 */ 
	private String useYn;

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
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob = false;
	
	/** 추가 */
	/** 테이블컬럼 VO 리스트 */
	private List<DmItemBVo> colmVoList;
	
	/** SQL일련번호 */
	private Integer sqlSeq;
		
	/** 항목기본목록*/
	public void setStorId(String storId) { 
		this.storId = storId;
	}
	public List<DmItemBVo> getColmVoList() {
		return colmVoList;
	}
	public void setColmVoList(List<DmItemBVo> colmVoList) {
		this.colmVoList = colmVoList;
	}
	/** 저장소ID */ 
	public String getStorId() { 
		return storId;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}
	
	public void setTblNm(String tblNm) { 
		this.tblNm = tblNm;
	}
	/** 테이블명 */ 
	public String getTblNm() { 
		return tblNm;
	}

	public void setStorNm(String storNm) { 
		this.storNm = storNm;
	}
	/** 테이블표시명 */ 
	public String getStorNm() { 
		return storNm;
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

	public void setDftYn(String dftYn) { 
		this.dftYn = dftYn;
	}
	/** 기본여부 */ 
	public String getDftYn() { 
		return dftYn;
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
	
	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}

	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	
	/** SQL일련번호 */
	public Integer getSqlSeq() {
		return sqlSeq;
	}
	public void setSqlSeq(Integer sqlSeq) {
		this.sqlSeq = sqlSeq;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorBDao.selectDmStorB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorBDao.insertDmStorB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorBDao.updateDmStorB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorBDao.deleteDmStorB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmStorBDao.countDmStorB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":저장소기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(storId!=null) { if(tab!=null) builder.append(tab); builder.append("storId(저장소ID):").append(storId).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(tblNm!=null) { if(tab!=null) builder.append(tab); builder.append("tblNm(테이블명):").append(tblNm).append('\n'); }
		if(storNm!=null) { if(tab!=null) builder.append(tab); builder.append("storNm(저장소명):").append(storNm).append('\n'); }
		if(rescId!=null) { if(tab!=null) builder.append(tab); builder.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(dftYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftYn(기본여부):").append(dftYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
