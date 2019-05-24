package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 캐쉬임시(PT_CACHE_T) 테이블 VO
 */
public class PtCacheTVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 5746279187272436737L;

	/** 캐쉬유형ID - KEY */
	private String cacheCatId;

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	private String langTypCd;

	/** 캐쉬ID - KEY */
	private String cacheId;

	/** 수정일시 */
	private String modDt;

	/** 캐쉬유형ID - KEY */
	public String getCacheCatId() {
		return cacheCatId;
	}

	/** 캐쉬유형ID - KEY */
	public void setCacheCatId(String cacheCatId) {
		this.cacheCatId = cacheCatId;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public String getLangTypCd() {
		return langTypCd;
	}

	/** 언어구분코드 - KEY - ko:한글, en:영문, ja:일문, zh:중문 */
	public void setLangTypCd(String langTypCd) {
		this.langTypCd = langTypCd;
	}

	/** 캐쉬ID - KEY */
	public String getCacheId() {
		return cacheId;
	}

	/** 캐쉬ID - KEY */
	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCacheTDao.selectPtCacheT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCacheTDao.insertPtCacheT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCacheTDao.updatePtCacheT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCacheTDao.deletePtCacheT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtCacheTDao.countPtCacheT";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":캐쉬임시]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(cacheCatId!=null) { if(tab!=null) builder.append(tab); builder.append("cacheCatId(캐쉬유형ID-PK):").append(cacheCatId).append('\n'); }
		if(langTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("langTypCd(언어구분코드-PK):").append(langTypCd).append('\n'); }
		if(cacheId!=null) { if(tab!=null) builder.append(tab); builder.append("cacheId(캐쉬ID-PK):").append(cacheId).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}
}
