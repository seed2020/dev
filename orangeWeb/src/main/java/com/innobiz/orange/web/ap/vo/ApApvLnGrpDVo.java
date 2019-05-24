package com.innobiz.orange.web.ap.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 결재라인그룹상세(AP_APV_LN_GRP_D) 테이블 VO
 */
public class ApApvLnGrpDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 6416492729634386267L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 결재라인그룹ID - KEY */
	private String apvLnGrpId;

	/** 결재라인그룹일련번호 - KEY */
	private String apvLnGrpSeq;

	/** 결재자UID */
	private String apvrUid;

	/** 결재부서ID */
	private String apvDeptId;

	/** 결재자부서여부 */
	private String apvrDeptYn;

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	private String apvrRoleCd;

	/** 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서 */
	private String dblApvTypCd;

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	private String absRsonCd;


	// 추가컬럼
	/** 고정결재자여부 */
	private String fixdApvrYn;

	/** 사용자명 */
	private String userNm;

	/** 결재자명 */
	private String apvrNm;

	/** 결재자역할명 */
	private String apvrRoleNm;

	/** 이중결재구분명 */
	private String dblApvTypNm;

	/** 부재사유명 */
	private String absRsonNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 결재라인그룹ID - KEY */
	public String getApvLnGrpId() {
		return apvLnGrpId;
	}

	/** 결재라인그룹ID - KEY */
	public void setApvLnGrpId(String apvLnGrpId) {
		this.apvLnGrpId = apvLnGrpId;
	}

	/** 결재라인그룹일련번호 - KEY */
	public String getApvLnGrpSeq() {
		return apvLnGrpSeq;
	}

	/** 결재라인그룹일련번호 - KEY */
	public void setApvLnGrpSeq(String apvLnGrpSeq) {
		this.apvLnGrpSeq = apvLnGrpSeq;
	}

	/** 결재자UID */
	public String getApvrUid() {
		return apvrUid;
	}

	/** 결재자UID */
	public void setApvrUid(String apvrUid) {
		this.apvrUid = apvrUid;
	}

	/** 결재부서ID */
	public String getApvDeptId() {
		return apvDeptId;
	}

	/** 결재부서ID */
	public void setApvDeptId(String apvDeptId) {
		this.apvDeptId = apvDeptId;
	}

	/** 결재자부서여부 */
	public String getApvrDeptYn() {
		return apvrDeptYn;
	}

	/** 결재자부서여부 */
	public void setApvrDeptYn(String apvrDeptYn) {
		this.apvrDeptYn = apvrDeptYn;
	}

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public String getApvrRoleCd() {
		return apvrRoleCd;
	}

	/** 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, abs:공석, apv:결재, pred:전결, entu:결재안함(위임), postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public void setApvrRoleCd(String apvrRoleCd) {
		this.apvrRoleCd = apvrRoleCd;
	}

	/** 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서 */
	public String getDblApvTypCd() {
		return dblApvTypCd;
	}

	/** 이중결재구분코드 - reqDept:신청부서, prcDept:처리부서 */
	public void setDblApvTypCd(String dblApvTypCd) {
		this.dblApvTypCd = dblApvTypCd;
	}

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	public String getAbsRsonCd() {
		return absRsonCd;
	}

	/** 부재사유코드 - 01:출장, 02:회의, 03:교육,연수, 04:휴가, 05:병가, 06:외출, 07:건강검진, 08:예비군, 09:퇴직, 99:기타 */
	public void setAbsRsonCd(String absRsonCd) {
		this.absRsonCd = absRsonCd;
	}

	/** 고정결재자여부 */
	public String getFixdApvrYn() {
		return fixdApvrYn;
	}

	/** 고정결재자여부 */
	public void setFixdApvrYn(String fixdApvrYn) {
		this.fixdApvrYn = fixdApvrYn;
	}

	/** 사용자명 */
	public String getUserNm() {
		return userNm;
	}

	/** 사용자명 */
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	/** 결재자명 */
	public String getApvrNm() {
		return apvrNm;
	}

	/** 결재자명 */
	public void setApvrNm(String apvrNm) {
		this.apvrNm = apvrNm;
	}

	/** 결재자역할명 */
	public String getApvrRoleNm() {
		return apvrRoleNm;
	}

	/** 결재자역할명 */
	public void setApvrRoleNm(String apvrRoleNm) {
		this.apvrRoleNm = apvrRoleNm;
	}

	/** 이중결재구분명 */
	public String getDblApvTypNm() {
		return dblApvTypNm;
	}

	/** 이중결재구분명 */
	public void setDblApvTypNm(String dblApvTypNm) {
		this.dblApvTypNm = dblApvTypNm;
	}

	/** 부재사유명 */
	public String getAbsRsonNm() {
		return absRsonNm;
	}

	/** 부재사유명 */
	public void setAbsRsonNm(String absRsonNm) {
		this.absRsonNm = absRsonNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpDDao.selectApApvLnGrpD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpDDao.insertApApvLnGrpD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpDDao.updateApApvLnGrpD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpDDao.deleteApApvLnGrpD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.ap.dao.ApApvLnGrpDDao.countApApvLnGrpD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":결재라인그룹상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(apvLnGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnGrpId(결재라인그룹ID-PK):").append(apvLnGrpId).append('\n'); }
		if(apvLnGrpSeq!=null) { if(tab!=null) builder.append(tab); builder.append("apvLnGrpSeq(결재라인그룹일련번호-PK):").append(apvLnGrpSeq).append('\n'); }
		if(apvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("apvrUid(결재자UID):").append(apvrUid).append('\n'); }
		if(apvDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("apvDeptId(결재부서ID):").append(apvDeptId).append('\n'); }
		if(apvrDeptYn!=null) { if(tab!=null) builder.append(tab); builder.append("apvrDeptYn(결재자부서여부):").append(apvrDeptYn).append('\n'); }
		if(apvrRoleCd!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleCd(결재자역할코드):").append(apvrRoleCd).append('\n'); }
		if(dblApvTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("dblApvTypCd(이중결재구분코드):").append(dblApvTypCd).append('\n'); }
		if(absRsonCd!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonCd(부재사유코드):").append(absRsonCd).append('\n'); }
		if(fixdApvrYn!=null) { if(tab!=null) builder.append(tab); builder.append("fixdApvrYn(고정결재자여부):").append(fixdApvrYn).append('\n'); }
		if(userNm!=null) { if(tab!=null) builder.append(tab); builder.append("userNm(사용자명):").append(userNm).append('\n'); }
		if(apvrNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrNm(결재자명):").append(apvrNm).append('\n'); }
		if(apvrRoleNm!=null) { if(tab!=null) builder.append(tab); builder.append("apvrRoleNm(결재자역할명):").append(apvrRoleNm).append('\n'); }
		if(dblApvTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("dblApvTypNm(이중결재구분명):").append(dblApvTypNm).append('\n'); }
		if(absRsonNm!=null) { if(tab!=null) builder.append(tab); builder.append("absRsonNm(부재사유명):").append(absRsonNm).append('\n'); }
		super.toString(builder, tab);
	}
}
