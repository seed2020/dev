package com.innobiz.orange.web.wf.vo;

import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/****** Object:  Vo - Date: 2018/05/28 15:58 ******/
/**
* 조회이력(WF_WORKS_READ_H) 테이블 VO 
*/
public class WfWorksReadHVo extends WfCmWorksVo {

 	/** serialVersionUID */
	private static final long serialVersionUID = -7544364380591074716L;

	/** 사용자UID */ 
	private String userUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	/** 사용자명 */ 
	private String userNm;
	
	/** 사용자 VO */
	private OrUserBVo orUserBVo;
	
	/** 원직자 VO */
	private OrOdurBVo orOdurBVo;
	
	/** 생성자 */
 	public WfWorksReadHVo() {
		super();
	}
 	
	public WfWorksReadHVo(String formNo) {
		super(formNo);
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}

	/** 등록자명 */ 
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
	/** 사용자 VO */
	public OrUserBVo getOrUserBVo() {
		return orUserBVo;
	}

	public void setOrUserBVo(OrUserBVo orUserBVo) {
		this.orUserBVo = orUserBVo;
	}

	/** 원직자 VO */
	public OrOdurBVo getOrOdurBVo() {
		return orOdurBVo;
	}

	public void setOrOdurBVo(OrOdurBVo orOdurBVo) {
		this.orOdurBVo = orOdurBVo;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksReadHDao.selectWfWorksReadH";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksReadHDao.insertWfWorksReadH";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksReadHDao.updateWfWorksReadH";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksReadHDao.deleteWfWorksReadH";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wf.dao.WfWorksReadHDao.countWfWorksReadH";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":조회이력]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
