package com.innobiz.orange.web.cm.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;

/** 공통VO 구현체 */
public class CommonVoImpl implements CommonVo {

	/** 디폴트 페이지 번호 : 1 */
	private static final Integer DEFAULT_PAGE_NO = Integer.valueOf(1);
	
	/** 디폴트 페이지 줄 수 : 10 */
	private static final Integer DEFAULT_PAGE_ROW_COUNT = Integer.valueOf(10);
	
	/** serialVersionUID */
	private static final long serialVersionUID = -9063892398926858913L;
	
	/** 임시쿼리ID */
	private String instanceQueryId = null;
	
	/** 쿼리타입 */
	private QueryType queryType = null;

	/** 정렬순서 */
	private String orderBy = null;
	
	/** 쿼리언어(코드조회용) */
	private String queryLang = null;
	
	/** 현재페이지 */
	private Integer pageNo = null;
	
	/** 페이지줄수 */
	private Integer pageRowCnt = null;
	
	/** 페이지줄시작 */
	private Integer pageStrt = null;
	
	/** 레코드번호 */
	private Integer rnum = null;
	
	/** 검색구분 */
	private String schCat = null;
	
	/** 검색어 */
	private String schWord = null;
	
	/** 기간검색구분 */
	private String durCat = null;
	
	/** 기간검색 시작일시 */
	private String durStrtDt = null;
	
	/** 기간검색 종료일시 */
	private String durEndDt = null;

	/** WHERE절 추가문 */
	protected String whereSqllet;
	
	/** 생성자 */
	public CommonVoImpl(){
	}
	
	/** 생성자 */
	public CommonVoImpl(String queryId){
		this.instanceQueryId = queryId;
	}
	
	/** 임시 쿼리ID */
	public String getInstanceQueryId(){
		return this.instanceQueryId;
	}
	
	/** 임시 쿼리ID */
	public void setInstanceQueryId(String instanceQueryId){
		this.instanceQueryId = instanceQueryId;
	}
	
	/** 쿼리ID 리턴 */
	public String getQueryId(QueryType queryType){
		return this.instanceQueryId;
	}
	
	/** 쿼리타입 */
	public QueryType getQueryType(){
		return this.queryType;
	}

	/** 쿼리타입 */
	public void setQueryType(QueryType queryType){
		this.queryType = queryType;
	}
	
	/** 정렬순서 */
	public String getOrderBy(){
		return this.orderBy;
	}

	/** 정렬순서 설정 */
	public void setOrderBy(String orderBy){
		if(orderBy!=null && !orderBy.isEmpty()){
			// SQL-INJECTION 방지용으로 '-' 문자는 제거 합니다.
			this.orderBy = StringUtil.removeChar(orderBy, '-');
		} else {
			this.orderBy = orderBy;
		}
	}
	
	/** 레코드번호 */
	public Integer getRnum(){
		return rnum;
	}
	
	/** 레코드번호 설정 */
	public void setRnum(Integer rnum){
		this.rnum = rnum;
	}
	
	/** 쿼리언어 */
	public String getQueryLang(){
		return this.queryLang;
	}
	
	/** 쿼리언어 */
	public void setQueryLang(String langTypCd){
		this.queryLang = langTypCd;
	}
	
	/** 객체를 Map으로 전환 */
	public Map<String, Object> toMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		VoUtil.toMap(this, map);
		return map;
	}
	
	/** Map에서 정보를 읽어 객체에 설정함 */
	public CommonVo fromMap(Map<String, ? extends Object> map){
		VoUtil.fromMap(this, map);
		return this;
	}
	
	/** 현제페이지 */
	public Integer getPageNo() {
		return pageNo;
	}
	
	/** 현제페이지 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	/** 페이지줄수 */
	public Integer getPageRowCnt() {
		return pageRowCnt;
	}
	
	/** 페이지줄수 */
	public void setPageRowCnt(Integer pageRowCnt) {
		this.pageRowCnt = pageRowCnt;
	}
	
	/** 페이지시작 - mysql 호환성 */
	public Integer getPageStrt(){
		return pageStrt;
	}
	
	/** 페이지시작 - mysql 호환성  */
	public void setPageStrt(Integer pageStrt){
		this.pageStrt = pageStrt;
	}

	/** 페이징 처리 정보가 없으면 세팅함 */
	public void setDefaultPage(){
		if(pageNo==null) pageNo = DEFAULT_PAGE_NO;
		if(pageRowCnt==null) pageRowCnt = DEFAULT_PAGE_ROW_COUNT;
	}
	
	/** 검색구분 */
	public String getSchCat() {
		return schCat;
	}

	/** 검색구분 */
	public void setSchCat(String schCat) {
		if(schCat!=null && !schCat.isEmpty()){
			// SQL-INJECTION 방지용으로 '-' 문자는 제거 합니다.
			this.schCat = StringUtil.removeChar(schCat, '-');
		} else {
			this.schCat = schCat;
		}
	}

	/** 검색어 */
	public String getSchWord() {
		return schWord;
	}

	/** 검색어 */
	public void setSchWord(String schWord) {
		this.schWord = schWord;
	}

	/** 기간검색구분 */
	public String getDurCat() {
		return durCat;
	}

	/** 기간검색구분 */
	public void setDurCat(String durCat) {
		if(durCat!=null && !durCat.isEmpty()){
			// SQL-INJECTION 방지용으로 '-' 문자는 제거 합니다.
			this.durCat = StringUtil.removeChar(durCat, '-');
		} else {
			this.durCat = durCat;
		}
	}

	/** 기간검색 시작일시 */
	public String getDurStrtDt() {
		return durStrtDt;
	}

	/** 기간검색 시작일시 */
	public void setDurStrtDt(String durStrtDt) {
		this.durStrtDt = durStrtDt;
	}

	/** 기간검색 종료일시 */
	public String getDurEndDt() {
		return durEndDt;
	}

	/** 기간검색 종료일시 */
	public void setDurEndDt(String durEndDt) {
		this.durEndDt = durEndDt;
	}

	/** WHERE절 추가문 */
	public String getWhereSqllet() {
		return whereSqllet;
	}

	/** WHERE절 추가문 */
	public void setWhereSqllet(String whereSqllet) {
		this.whereSqllet = whereSqllet;
	}

	/** toString */
	public String toString(){
		return VoUtil.toString(this);
	}
	
	/** String으로 변환, builder2에 append 함 */
	public void toString(StringBuilder builder, String tab){
		StringBuilder builder2 = new StringBuilder(128);
		if(instanceQueryId!=null) {
			if(tab!=null) builder2.append(tab);
			builder2.append("instanceQueryId(쿼리ID):").append(instanceQueryId).append('\n');
		} else if(queryType!=null) {
			if(tab!=null) builder2.append(tab);
			builder2.append("queryType(쿼리타입):");
			if(QueryType.INSERT==queryType) builder2.append("INSERT");
			else if(QueryType.UPDATE==queryType) builder2.append("UPDATE");
			else if(QueryType.DELETE==queryType) builder2.append("DELETE");
			else if(QueryType.SELECT==queryType) builder2.append("SELECT");
			else if(QueryType.STORE==queryType) builder2.append("STORE");
			else if(QueryType.COUNT==queryType) builder2.append("COUNT");
			builder2.append('\n');
		}
		
		if(orderBy!=null) { if(tab!=null) builder2.append(tab); builder2.append("orderBy(정렬순서):").append(orderBy).append('\n'); }
		if(queryLang!=null) { if(tab!=null) builder2.append(tab); builder2.append("queryLang(쿼리언어):").append(queryLang).append('\n'); }
		if(pageNo!=null) { if(tab!=null) builder2.append(tab); builder2.append("pageNo(현재페이지):").append(pageNo).append('\n'); }
		if(pageRowCnt!=null) { if(tab!=null) builder2.append(tab); builder2.append("pageRowCnt(페이지줄수):").append(pageRowCnt).append('\n'); }
		if(rnum!=null) { if(tab!=null) builder2.append(tab); builder2.append("rnum(레코드번호):").append(rnum).append('\n'); }
		if(schCat!=null) { if(tab!=null) builder2.append(tab); builder2.append("schCat(검색구분):").append(schCat).append('\n'); }
		if(schWord!=null) { if(tab!=null) builder2.append(tab); builder2.append("schWord(검색어):").append(schWord).append('\n'); }
		if(durCat!=null) { if(tab!=null) builder2.append(tab); builder2.append("durCat(기간검색구분):").append(durCat).append('\n'); }
		if(durStrtDt!=null) { if(tab!=null) builder2.append(tab); builder2.append("durStrtDt(기간검색시작일시):").append(durStrtDt).append('\n'); }
		if(durEndDt!=null) { if(tab!=null) builder2.append(tab); builder2.append("durEndDt(기간검색종료일시):").append(durEndDt).append('\n'); }
		if(whereSqllet!=null) { if(tab!=null) builder2.append(tab); builder.append("whereSqllet(WHERE절 추가문):").append(whereSqllet).append('\n'); }
		if(builder2.length()>0){
			builder.append(builder2);
		}
	}
	/** StringBuilder 더함, toString 변환용 */
	protected void appendArrayTo(StringBuilder builder, String[] arr){
		ArrayUtil.appendArrayTo(builder, arr);
	}
	/** StringBuilder 더함, toString 변환용 */
	protected void appendArrayTo(StringBuilder builder, String[][] arr){
		ArrayUtil.appendArrayTo(builder, arr);
	}
	/** StringBuilder 더함, toString 변환용 */
	protected void appendStringListTo(StringBuilder builder, List<? extends Object> strList, String tab){
		if(strList!=null && !strList.isEmpty()){
			builder.append('[');
			int i, size = strList.size();
			for(i=0;i<size;i++){
				if(i>0) builder.append(',').append(' ');
				builder.append(strList.get(i));
			}
			builder.append(']');
		}
	}
	/** StringBuilder 더함, toString 변환용 */
	protected void appendVoListTo(StringBuilder builder, List<? extends CommonVo> list, String tab){
		if(list!=null && !list.isEmpty()){
			builder.append('\n');
			tab = tab == null ? "\t" : tab+"\t";
			boolean first = true;
			for(CommonVo commonVo : list){
				if(first) first = false;
				else {
					builder.deleteCharAt(builder.length()-1);
					builder.append(" //\n");
				}
				commonVo.toString(builder, tab);
			}
		}
	}
}
