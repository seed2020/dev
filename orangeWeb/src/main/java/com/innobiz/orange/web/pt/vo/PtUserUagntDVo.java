package com.innobiz.orange.web.pt.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자유저에이전트상세(PT_USER_UAGNT_D) 테이블 VO
 */
public class PtUserUagntDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7259733426522131608L;

	/** 원직자UID - KEY */
	private String odurUid;

	/** 유저에이전트해쉬값 - KEY */
	private String uagntHashVa;

	/** 유저에이전트토큰값 */
	private String uagntTknVa;

	/** 원직자UID - KEY */
	public String getOdurUid() {
		return odurUid;
	}

	/** 원직자UID - KEY */
	public void setOdurUid(String odurUid) {
		this.odurUid = odurUid;
	}

	/** 유저에이전트해쉬값 - KEY */
	public String getUagntHashVa() {
		return uagntHashVa;
	}

	/** 유저에이전트해쉬값 - KEY */
	public void setUagntHashVa(String uagntHashVa) {
		this.uagntHashVa = uagntHashVa;
	}

	/** 유저에이전트토큰값 */
	public String getUagntTknVa() {
		return uagntTknVa;
	}

	/** 유저에이전트토큰값 */
	public void setUagntTknVa(String uagntTknVa) {
		this.uagntTknVa = uagntTknVa;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserUagntDDao.selectPtUserUagntD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserUagntDDao.insertPtUserUagntD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserUagntDDao.updatePtUserUagntD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserUagntDDao.deletePtUserUagntD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.pt.dao.PtUserUagntDDao.countPtUserUagntD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자유저에이전트상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(odurUid!=null) { if(tab!=null) builder.append(tab); builder.append("odurUid(원직자UID-PK):").append(odurUid).append('\n'); }
		if(uagntHashVa!=null) { if(tab!=null) builder.append(tab); builder.append("uagntHashVa(유저에이전트해쉬값-PK):").append(uagntHashVa).append('\n'); }
		if(uagntTknVa!=null) { if(tab!=null) builder.append(tab); builder.append("uagntTknVa(유저에이전트토큰값):").append(uagntTknVa).append('\n'); }
		super.toString(builder, tab);
	}
}
