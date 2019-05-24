package com.innobiz.orange.web.bb.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 테이블컬럼(BA_TBL_COLM_D) 테이블 VO
 */
public class BaTblColmDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 6128130997168371751L;

	/** 컬럼ID */
	private String colmId;

	/** 테이블ID */
	private String tblId;

	/** 컬럼명 */
	private String colmNm;

	/** 컬럼표시명 */
	private String colmDispNm;

	/** 리소스ID */
	private String rescId;

	/** 데이터타입 */
	private String dataTyp;

	/** 길이 */
	private Integer colmLen;

	/** 확장컬럼여부 */
	private String exColmYn;

	/** 컬럼타입 */
	private String colmTyp;

	/** 컬럼타입값 */
	private String colmTypVal;

	/** 등록표시여부 */
	private String regDispYn;

	/** 수정표시여부 */
	private String modDispYn;

	/** 조회표시여부 */
	private String readDispYn;

	/** 목록표시여부 */
	private String listDispYn;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	// 추가 컬럼

	/** 리소스명 */
	private String rescNm;

	/** 유효성 체크용 속성 */
	private String valid;

	/** 삭제여부 확인용 속성 */
	private String deleted;

	/** 추가여부 확인용 속성 */
	private String added;
	
	/** 컬럼 ALTER - ADD, MODIFY, DROP */
	private String alter;

	/** 컬럼ID */
	public String getColmId() {
		return colmId;
	}

	/** 컬럼ID */
	public void setColmId(String colmId) {
		this.colmId = colmId;
	}

	/** 테이블ID */
	public String getTblId() {
		return tblId;
	}

	/** 테이블ID */
	public void setTblId(String tblId) {
		this.tblId = tblId;
	}

	/** 컬럼명 */
	public String getColmNm() {
		return colmNm;
	}

	/** 컬럼명 */
	public void setColmNm(String colmNm) {
		this.colmNm = colmNm;
	}

	/** 컬럼표시명 */
	public String getColmDispNm() {
		return colmDispNm;
	}

	/** 컬럼표시명 */
	public void setColmDispNm(String colmDispNm) {
		this.colmDispNm = colmDispNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 데이터타입 */
	public String getDataTyp() {
		return dataTyp;
	}

	/** 데이터타입 */
	public void setDataTyp(String dataTyp) {
		this.dataTyp = dataTyp;
	}

	/** 길이 */
	public Integer getColmLen() {
		return colmLen;
	}

	/** 길이 */
	public void setColmLen(Integer colmLen) {
		this.colmLen = colmLen;
	}

	/** 확장컬럼여부 */
	public String getExColmYn() {
		return exColmYn;
	}

	/** 확장컬럼여부 */
	public void setExColmYn(String exColmYn) {
		this.exColmYn = exColmYn;
	}

	/** 컬럼타입 */
	public String getColmTyp() {
		return colmTyp;
	}

	/** 컬럼타입 */
	public void setColmTyp(String colmTyp) {
		this.colmTyp = colmTyp;
	}

	/** 컬럼타입값 */
	public String getColmTypVal() {
		return colmTypVal;
	}

	/** 컬럼타입값 */
	public void setColmTypVal(String colmTypVal) {
		this.colmTypVal = colmTypVal;
	}

	/** 등록표시여부 */
	public String getRegDispYn() {
		return regDispYn;
	}

	/** 등록표시여부 */
	public void setRegDispYn(String regDispYn) {
		this.regDispYn = regDispYn;
	}

	/** 수정표시여부 */
	public String getModDispYn() {
		return modDispYn;
	}

	/** 수정표시여부 */
	public void setModDispYn(String modDispYn) {
		this.modDispYn = modDispYn;
	}

	/** 조회표시여부 */
	public String getReadDispYn() {
		return readDispYn;
	}

	/** 조회표시여부 */
	public void setReadDispYn(String readDispYn) {
		this.readDispYn = readDispYn;
	}

	/** 목록표시여부 */
	public String getListDispYn() {
		return listDispYn;
	}

	/** 목록표시여부 */
	public void setListDispYn(String listDispYn) {
		this.listDispYn = listDispYn;
	}

	/** 등록자 */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자 */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 수정자 */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자 */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 유효성 체크용 속성 */
	public String getValid() {
		return valid;
	}

	/** 유효성 체크용 속성 */
	public void setValid(String valid) {
		this.valid = valid;
	}

	/** 삭제여부 확인용 속성 */
	public String getDeleted() {
		return deleted;
	}

	/** 삭제여부 확인용 속성 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	/** 추가여부 확인용 속성 */
	public String getAdded() {
		return added;
	}

	/** 추가여부 확인용 속성 */
	public void setAdded(String added) {
		this.added = added;
	}

	/** 컬럼 ALTER */
	public String getAlter() {
		return alter;
	}

	/** 컬럼 ALTER */
	public void setAlter(String alter) {
		this.alter = alter;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblColmDDao.selectBaTblColmD";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblColmDDao.insertBaTblColmD";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblColmDDao.updateBaTblColmD";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblColmDDao.deleteBaTblColmD";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaTblColmDDao.countBaTblColmD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":테이블컬럼]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (colmId != null) { if (tab != null) sb.append(tab); sb.append("colmId(컬럼ID):").append(colmId).append('\n'); }
		if (tblId != null) { if (tab != null) sb.append(tab); sb.append("tblId(테이블ID):").append(tblId).append('\n'); }
		if (colmNm != null) { if (tab != null) sb.append(tab); sb.append("colmNm(컬럼명):").append(colmNm).append('\n'); }
		if (colmDispNm != null) { if (tab != null) sb.append(tab); sb.append("colmDispNm(컬럼표시명):").append(colmDispNm).append('\n'); }
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if (dataTyp != null) { if (tab != null) sb.append(tab); sb.append("dataTyp(데이터타입):").append(dataTyp).append('\n'); }
		if (colmLen != null) { if (tab != null) sb.append(tab); sb.append("colmLen(길이):").append(colmLen).append('\n'); }
		if (exColmYn != null) { if (tab != null) sb.append(tab); sb.append("exColmYn(확장컬럼여부):").append(exColmYn).append('\n'); }
		if (colmTyp != null) { if (tab != null) sb.append(tab); sb.append("colmTyp(컬럼타입):").append(colmTyp).append('\n'); }
		if (colmTypVal != null) { if (tab != null) sb.append(tab); sb.append("colmTypVal(컬럼타입값):").append(colmTypVal).append('\n'); }
		if (regDispYn != null) { if (tab != null) sb.append(tab); sb.append("regDispYn(등록표시여부):").append(regDispYn).append('\n'); }
		if (modDispYn != null) { if (tab != null) sb.append(tab); sb.append("modDispYn(수정표시여부):").append(modDispYn).append('\n'); }
		if (readDispYn != null) { if (tab != null) sb.append(tab); sb.append("readDispYn(조회표시여부):").append(readDispYn).append('\n'); }
		if (listDispYn != null) { if (tab != null) sb.append(tab); sb.append("listDispYn(목록표시여부):").append(listDispYn).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		if (rescNm != null) { if (tab != null) sb.append(tab); sb.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if (valid != null) { if (tab != null) sb.append(tab); sb.append("valid(유효성체크):").append(valid).append('\n'); }
		if (deleted != null) { if (tab != null) sb.append(tab); sb.append("deleted(삭제여부):").append(deleted).append('\n'); }
		if (added != null) { if (tab != null) sb.append(tab); sb.append("added(추가여부):").append(added).append('\n'); }
		if (alter != null) { if (tab != null) sb.append(tab); sb.append("alter(컬럼 ALTER):").append(alter).append('\n'); }
		super.toString(sb, tab);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BaTblColmDVo other = (BaTblColmDVo) obj;

		if (colmId == null) {
			if (other.colmId != null) return false;
		} else if (!colmId.equals(other.colmId)) return false;

		if (colmLen == null) {
			if (other.colmLen != null) return false;
		} else if (!colmLen.equals(other.colmLen)) return false;

		if (colmNm == null) {
			if (other.colmNm != null) return false;
		} else if (!colmNm.equals(other.colmNm)) return false;

		if (dataTyp == null) {
			if (other.dataTyp != null) return false;
		} else if (!dataTyp.equals(other.dataTyp)) return false;

		if (exColmYn == null) {
			if (other.exColmYn != null) return false;
		} else if (!exColmYn.equals(other.exColmYn)) return false;

		if (colmTyp == null) {
			if (other.colmTyp != null) return false;
		} else if (!colmTyp.equals(other.colmTyp)) return false;

		if (colmTypVal == null) {
			if (other.colmTypVal != null) return false;
		} else if (!colmTypVal.equals(other.colmTypVal)) return false;

		if (tblId == null) {
			if (other.tblId != null) return false;
		} else if (!tblId.equals(other.tblId)) return false;

		return true;
	}

}