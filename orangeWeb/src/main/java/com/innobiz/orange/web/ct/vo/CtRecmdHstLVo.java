package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/**
 * 추천이력(CT_RECMD_HST_L) 테이블 VO
 */
@SuppressWarnings("serial")
public class CtRecmdHstLVo extends CommonVoImpl{
	
	/** 게시물ID */
	private Integer bullId;

	/** 사용자UID */
	private String userUid;

	/** 등록일시 */
	private String regDt;

	// 추가 컬럼

	/** 사용자명 */
	private String userNm;

	/** 사용자기본(OR_USER_B) 테이블 VO */
	private OrUserBVo orUserBVo;

	/** 원직자기본(OR_ODUR_B) 테이블 VO */
	private OrOdurBVo orOdurBVo;
	
	
	public Integer getBullId() {
		return bullId;
	}

	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public OrUserBVo getOrUserBVo() {
		return orUserBVo;
	}

	public void setOrUserBVo(OrUserBVo orUserBVo) {
		this.orUserBVo = orUserBVo;
	}

	public OrOdurBVo getOrOdurBVo() {
		return orOdurBVo;
	}

	public void setOrOdurBVo(OrOdurBVo orOdurBVo) {
		this.orOdurBVo = orOdurBVo;
	}

	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(bullId!=null) { if(tab!=null) builder.append(tab); builder.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(orUserBVo!=null) { if(tab!=null) builder.append(tab); builder.append("orUserBVo(사용자기본(OR_USER_B) 테이블 VO):\n"); orUserBVo.toString(builder, tab==null?"\t":tab+"\t"); builder.append('\n'); }
		if(orOdurBVo!=null) { if(tab!=null) builder.append(tab); builder.append("orOdurBVo(원직자기본(OR_ODUR_B) 테이블 VO):\n"); orOdurBVo.toString(builder, tab==null?"\t":tab+"\t"); builder.append('\n'); }
		super.toString(builder, tab);
	}

}