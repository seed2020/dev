package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/10/28 10:32 ******/
/**
* 포틀릿설정상세(DM_PLT_SETUP_D) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmPltSetupDVo extends CommonVoImpl {
	/** 사용자UID */ 
	private String userUid;

 	/** 포틀릿ID */ 
	private String pltId;

 	/** 함ID */ 
	private String bxId;

 	/** 정렬순서 */ 
	private String sortOrdr;
	
	// 추가컬럼
	/** 함이름 */
	private String bxNm;

	/** 사용여부 */
	private String useYn;

	/** 메뉴ID */
	private String menuId;
		
 	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setPltId(String pltId) { 
		this.pltId = pltId;
	}
	/** 포틀릿ID */ 
	public String getPltId() { 
		return pltId;
	}

	public void setBxId(String bxId) { 
		this.bxId = bxId;
	}
	/** 함ID */ 
	public String getBxId() { 
		return bxId;
	}

	public void setSortOrdr(String sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public String getSortOrdr() { 
		return sortOrdr;
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
			return "com.innobiz.orange.web.dm.dao.DmPltSetupDDao.selectDmPltSetupD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPltSetupDDao.insertDmPltSetupD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPltSetupDDao.updateDmPltSetupD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPltSetupDDao.deleteDmPltSetupD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmPltSetupDDao.countDmPltSetupD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":포틀릿설정상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(pltId!=null) { if(tab!=null) builder.append(tab); builder.append("pltId(포틀릿ID):").append(pltId).append('\n'); }
		if(bxId!=null) { if(tab!=null) builder.append(tab); builder.append("bxId(함ID):").append(bxId).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		if(bxNm!=null) { if(tab!=null) builder.append(tab); builder.append("bxNm(함이름):").append(bxNm).append('\n'); }
		if(useYn!=null) { if(tab!=null) builder.append(tab); builder.append("useYn(사용여부):").append(useYn).append('\n'); }
		if(menuId!=null) { if(tab!=null) builder.append(tab); builder.append("menuId(메뉴ID):").append(menuId).append('\n'); }
		super.toString(builder, tab);
	}

}
