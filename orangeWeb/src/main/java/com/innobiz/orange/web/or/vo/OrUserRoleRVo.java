package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

import java.util.List;

/**
 * 사용자역할관계(OR_USER_ROLE_R) 테이블 VO
 */
public class OrUserRoleRVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 7364770924325661131L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 역할코드 - KEY - 01:부서장, 02:문서담당자, 03:문서과문서담당자, 04:양식관리자 */
	private String roleCd;


	// 추가컬럼
	/** 회사ID */
	private String compId;

	/** 사용자UID 목록 */
	private List<String> userUidList;

	/** 역할명 */
	private String roleNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 역할코드 - KEY - 01:부서장, 02:문서담당자, 03:문서과문서담당자, 04:양식관리자 */
	public String getRoleCd() {
		return roleCd;
	}

	/** 역할코드 - KEY - 01:부서장, 02:문서담당자, 03:문서과문서담당자, 04:양식관리자 */
	public void setRoleCd(String roleCd) {
		this.roleCd = roleCd;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 사용자UID 목록 */
	public List<String> getUserUidList() {
		return userUidList;
	}

	/** 사용자UID 목록 */
	public void setUserUidList(List<String> userUidList) {
		this.userUidList = userUidList;
	}

	/** 역할명 */
	public String getRoleNm() {
		return roleNm;
	}

	/** 역할명 */
	public void setRoleNm(String roleNm) {
		this.roleNm = roleNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserRoleRDao.selectOrUserRoleR";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserRoleRDao.insertOrUserRoleR";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserRoleRDao.updateOrUserRoleR";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserRoleRDao.deleteOrUserRoleR";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserRoleRDao.countOrUserRoleR";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자역할관계]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(roleCd!=null) { if(tab!=null) builder.append(tab); builder.append("roleCd(역할코드-PK):").append(roleCd).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userUidList!=null) { if(tab!=null) builder.append(tab); builder.append("userUidList(사용자UID 목록):"); appendStringListTo(builder, userUidList, tab); builder.append('\n'); }
		if(roleNm!=null) { if(tab!=null) builder.append(tab); builder.append("roleNm(역할명):").append(roleNm).append('\n'); }
		super.toString(builder, tab);
	}
}
