package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 포틀릿설정상세(CT_PLT_FNC_LIST_D) 테이블 VO
 */
public class CtPltFncListDVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = 9134006969440193245L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 함ID - KEY */
	private String bxId;
	
	/** 포틀릿ID - KEY */
	private String pltId;

	/** 정렬순서 */
	private String sortOrdr;


	// 추가컬럼
	/** 함이름 */
	private String bxNm;

	/** 사용여부 */
	private String useYn;

	/** 메뉴ID */
	private String menuId;
	
	/** 포틀릿ID - KEY */
	public String getPltId() {
		return pltId;
	}

	/** 포틀릿ID - KEY */
	public void setPltId(String pltId) {
		this.pltId = pltId;
	}

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 함ID - KEY */
	public String getBxId() {
		return bxId;
	}

	/** 함ID - KEY */
	public void setBxId(String bxId) {
		this.bxId = bxId;
	}

	/** 정렬순서 */
	public String getSortOrdr() {
		return sortOrdr;
	}

	/** 정렬순서 */
	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}

	/** 함이름 */
	public String getBxNm() {
		return bxNm;
	}

	/** 함이름 */
	public void setBxNm(String bxNm) {
		this.bxNm = bxNm;
	}

	/** 사용여부 */
	public String getUseYn() {
		return useYn;
	}

	/** 사용여부 */
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	/** 메뉴ID */
	public String getMenuId() {
		return menuId;
	}

	/** 메뉴ID */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ct.dao.CtPltFncListDDao.selectCtPltFncListD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ct.dao.CtPltFncListDDao.insertCtPltFncListD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ct.dao.CtPltFncListDDao.updateCtPltFncListD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ct.dao.CtPltFncListDDao.deleteCtPltFncListD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ct.dao.CtPltFncListDDao.countCtPltFncListD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":포틀릿설정상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(bxId!=null) { if(tab!=null) builder.append(tab); builder.append("bxId(함ID):").append(bxId).append('\n'); }
		if(pltId!=null) { if(tab!=null) builder.append(tab); builder.append("pltId(포틀릿ID):").append(pltId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(bxNm!=null) { if(tab!=null) builder.append(tab); builder.append("bxNm(함이름):").append(bxNm).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(menuId!=null) { if(tab!=null) builder.append(tab); builder.append("menuId(메뉴ID):").append(menuId).append('\n'); }
		super.toString(builder, tab);
	}
}