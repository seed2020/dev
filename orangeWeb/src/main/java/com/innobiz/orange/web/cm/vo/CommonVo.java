package com.innobiz.orange.web.cm.vo;

import java.io.Serializable;
import java.util.Map;

/** 공통VO 인터페이스 */
public interface CommonVo extends Serializable {

	/** 임시 쿼리ID 리턴 */
	public String getInstanceQueryId();
	
	/** 임시 쿼리ID 설정 */
	public void setInstanceQueryId(String instanceQueryId);
	
	/** 쿼리ID 리턴 */
	public String getQueryId(QueryType queryType);
	
	/** 쿼리타입 리턴 */
	public QueryType getQueryType();
	
	/** 쿼리타입 설정 */
	public void setQueryType(QueryType queryType);
	
	/** 정렬순서 리턴 */
	public String getOrderBy();
	
	/** 정렬순서 설정 */
	public void setOrderBy(String orderBy);
	
	/** 쿼리언어 리턴 */
	public String getQueryLang();
	
	/** 쿼리언어 설정 */
	public void setQueryLang(String langTypCd);
	
	/** 객체를 Map으로 전환 */
	public Map<String, Object> toMap();
	
	/** Map에서 정보를 읽어 객체에 설정함 */
	public CommonVo fromMap(Map<String, ? extends Object> map);
	
	/** 현제페이지 */
	public Integer getPageNo();
	
	/** 현제페이지 */
	public void setPageNo(Integer pageNo);
	
	/** 페이지줄수 */
	public Integer getPageRowCnt();
	
	/** 페이지줄수 */
	public void setPageRowCnt(Integer pageRowCnt);
	
	/** 페이지시작 - mysql 호환성 */
	public Integer getPageStrt();
	
	/** 페이지시작 - mysql 호환성  */
	public void setPageStrt(Integer pageStrt);
	
	/** 페이징 처리 정보가 없으면 세팅함 */
	public void setDefaultPage();
	
	/** 검색 구분 */
	public String getSchCat();

	/** 검색 구분 */
	public void setSchCat(String schCat);

	/** 검색어 */
	public String getSchWord();

	/** 검색어 */
	public void setSchWord(String schWord);

	/** 기간검색 구분 */
	public String getDurCat();

	/** 기간검색 구분 */
	public void setDurCat(String durCat);

	/** 기간검색 시작일시 */
	public String getDurStrtDt();

	/** 기간검색 시작일시 */
	public void setDurStrtDt(String durStrtDt);

	/** 기간검색 종료일시 */
	public String getDurEndDt();

	/** 기간검색 종료일시 */
	public void setDurEndDt(String durEndDt);

	/** WHERE절 추가문 */
	public String getWhereSqllet();

	/** WHERE절 추가문 */
	public void setWhereSqllet(String whereSqllet);
	
	/** String으로 변환 */
	public void toString(StringBuilder builder, String tab);
}
