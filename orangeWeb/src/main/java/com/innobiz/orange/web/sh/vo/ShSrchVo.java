package com.innobiz.orange.web.sh.vo;

import java.util.ArrayList;
import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;

/**
 * 검색 VO
 */
public class ShSrchVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 8768152435126629585L;

	/** 인덱스번호 - KEY */
	private String idxNo;

	/** 회사ID */
	private String compId;

	/** 모듈참조ID */
	private String mdRid;

	/** 모듈ID */
	private String mdId;

	/** 모듈함ID */
	private String mdBxId;

	/** 모듈함ID 목록 */
	private List<String> mdBxIdList;

	/** 모듈함명 */
	private String mdBxNm;

	/** 제목 */
	private String subj;

	/** 등록자UID */
	private String regrNm;

	/** 등록자UID */
	private String regrUid;

	/** 작성일 */
	private String regDt;

	/** URL */
	private String url;

	/** 제목여부 */
	private Boolean subjYn;

	/** 제목여부 */
	private Boolean bodyYn;

	/** 첨부여부 */
	private Boolean attchYn;

	/** 보안등급코드 목록 */
	private List<String> seculCdList;

	/** 보안등급 사용안함 여부 */
	private Boolean noSeculYn;

	/** 보안등급코드 적용 모듈함ID 목록 */
	private List<String> seculAplyMdBxIdList;

	/** 키워드 목록 */
	private List<ShKwdVo> kwdList;

	/** 카테고리 목록 */
	private List<ShCatVo> catList;

//	/** 권한 목록 */
//	private List<ShAuthVo> authList;

	/** 사용자UID */
	private String userUid;

	/** 부서ID */
	private String deptId;

	/** 하위부서 목록 */
	private List<String> subDeptList;

	/** 수 */
	private Integer cnt;
	
	/** 요약 */
	private String smry;

	/** 인덱스번호 */
	public String getIdxNo() {
		return idxNo;
	}

	/** 인덱스번호 */
	public void setIdxNo(String idxNo) {
		this.idxNo = idxNo;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 모듈참조ID */
	public String getMdRid() {
		return mdRid;
	}

	/** 모듈참조ID */
	public void setMdRid(String mdRid) {
		this.mdRid = mdRid;
	}

	/** 모듈ID */
	public String getMdId() {
		return mdId;
	}

	/** 모듈ID */
	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	/** 모듈함ID */
	public String getMdBxId() {
		return mdBxId;
	}

	/** 모듈함ID */
	public void setMdBxId(String mdBxId) {
		this.mdBxId = mdBxId;
	}

	/** 모듈함ID 목록 */
	public List<String> getMdBxIdList() {
		return mdBxIdList;
	}

	/** 모듈함ID 목록 */
	public void setMdBxIdList(List<String> mdBxIdList) {
		this.mdBxIdList = mdBxIdList;
	}

	/** 모듈함명 */
	public String getMdBxNm() {
		return mdBxNm;
	}

	/** 모듈함명 */
	public void setMdBxNm(String mdBxNm) {
		this.mdBxNm = mdBxNm;
	}

	/** 제목 */
	public String getSubj() {
		return subj;
	}

	/** 제목 */
	public void setSubj(String subj) {
		this.subj = subj;
	}

	/** 등록자UID */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자UID */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 작성일 */
	public String getRegDt() {
		return regDt;
	}

	/** 작성일 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** URL */
	public String getUrl() {
		return url;
	}

	/** URL */
	public void setUrl(String url) {
		this.url = url;
	}

	/** 제목여부 */
	public Boolean isSubjYn() {
		return subjYn;
	}

	/** 제목여부 */
	public void setSubjYn(Boolean subjYn) {
		this.subjYn = subjYn;
	}

	/** 제목여부 */
	public Boolean isBodyYn() {
		return bodyYn;
	}

	/** 제목여부 */
	public void setBodyYn(Boolean bodyYn) {
		this.bodyYn = bodyYn;
	}

	/** 첨부여부 */
	public Boolean isAttchYn() {
		return attchYn;
	}

	/** 첨부여부 */
	public void setAttchYn(Boolean attchYn) {
		this.attchYn = attchYn;
	}

	/** 보안등급코드 목록 */
	public List<String> getSeculCdList() {
		return seculCdList;
	}

	/** 보안등급코드 목록 */
	public void setSeculCdList(List<String> seculCdList) {
		this.seculCdList = seculCdList;
	}

	/** 보안등급 사용안함 여부 */
	public Boolean isNoSeculYn() {
		return noSeculYn;
	}

	/** 보안등급 사용안함 여부 */
	public void setNoSeculYn(Boolean noSeculYn) {
		this.noSeculYn = noSeculYn;
	}

	/** 보안등급코드 적용 모듈함ID 목록 */
	public List<String> getSeculAplyMdBxIdList() {
		return seculAplyMdBxIdList;
	}

	/** 보안등급코드 적용 모듈함ID 목록 */
	public void setSeculAplyMdBxIdList(List<String> seculAplyMdBxIdList) {
		this.seculAplyMdBxIdList = seculAplyMdBxIdList;
	}

	/** 키워드 목록 */
	public List<ShKwdVo> getKwdList() {
		return kwdList;
	}

	/** 키워드 목록 */
	public void setKwdList(List<ShKwdVo> kwdList) {
		this.kwdList = kwdList;
	}

	/** 카테고리 목록 */
	public List<ShCatVo> getCatList() {
		return catList;
	}

	/** 카테고리 목록 */
	public void setCatList(List<ShCatVo> catList) {
		this.catList = catList;
	}
	
	/** 카테고리 추가 */
	public void addCat(String catTypCd, String catCd){
		if(catList==null) catList = new ArrayList<ShCatVo>();
		ShCatVo shCatVo = new ShCatVo();
		shCatVo.setCatTypCd(catTypCd);
		shCatVo.setCatCd(catCd);
		catList.add(shCatVo);
	}
	
	/** 카테고리 목록 추가 */
	public void addCatList(String catTypCd, List<String> catCdList){
		if(catList==null) catList = new ArrayList<ShCatVo>();
		ShCatVo shCatVo = new ShCatVo();
		shCatVo.setCatTypCd(catTypCd);
		shCatVo.setCatCdList(catCdList);
		catList.add(shCatVo);
	}

//	/** 권한 목록 */
//	public List<ShAuthVo> getAuthList() {
//		return authList;
//	}
//
//	/** 권한 목록 */
//	public void setAuthList(List<ShAuthVo> authList) {
//		this.authList = authList;
//	}

	/** 사용자UID */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 부서ID */
	public String getDeptId() {
		return deptId;
	}

	/** 부서ID */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/** 하위부서 목록 */
	public List<String> getSubDeptList() {
		return subDeptList;
	}

	/** 하위부서 목록 */
	public void setSubDeptList(List<String> subDeptList) {
		this.subDeptList = subDeptList;
	}
	
//	/** 권한 추가 */
//	public void addAuth(String authTypCd, String authCd){
//		if(authList==null) authList = new ArrayList<ShAuthVo>();
//		ShAuthVo shAuthVo = new ShAuthVo();
//		shAuthVo.setAuthTypCd(authTypCd);
//		shAuthVo.setAuthCd(authCd);
//		authList.add(shAuthVo);
//	}
//	
//	/** 권한목록 추가 */
//	public void addAuthList(String authTypCd, List<String> authCdList){
//		if(authList==null) authList = new ArrayList<ShAuthVo>();
//		ShAuthVo shAuthVo = new ShAuthVo();
//		shAuthVo.setAuthTypCd(authTypCd);
//		shAuthVo.setAuthCdList(authCdList);
//		authList.add(shAuthVo);
//	}

	/** 수 */
	public Integer getCnt() {
		return cnt;
	}

	/** 수 */
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	
	/** 요약 */
	public String getSmry() {
		return smry;
	}

	/** 요약 */
	public void setSmry(String smry) {
		this.smry = smry;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":검색 VO]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(idxNo!=null) { if(tab!=null) builder.append(tab); builder.append("idxNo(인덱스번호):").append(idxNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(mdRid!=null) { if(tab!=null) builder.append(tab); builder.append("mdRid(모듈참조ID):").append(mdRid).append('\n'); }
		if(mdId!=null) { if(tab!=null) builder.append(tab); builder.append("mdId(모듈ID):").append(mdId).append('\n'); }
		if(mdBxId!=null) { if(tab!=null) builder.append(tab); builder.append("mdBxId(모듈함ID):").append(mdBxId).append('\n'); }
		if(mdBxIdList!=null) { if(tab!=null) builder.append(tab); builder.append("mdBxIdList(모듈함ID 목록):"); appendStringListTo(builder, mdBxIdList, tab); builder.append('\n'); }
		if(mdBxNm!=null) { if(tab!=null) builder.append(tab); builder.append("mdBxNm(모듈함명):").append(mdBxNm).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자UID):").append(regrNm).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(작성일):").append(regDt).append('\n'); }
		if(url!=null) { if(tab!=null) builder.append(tab); builder.append("url(URL):").append(url).append('\n'); }
		if(subjYn!=null) { if(tab!=null) builder.append(tab); builder.append("subjYn(제목여부):").append(subjYn).append('\n'); }
		if(bodyYn!=null) { if(tab!=null) builder.append(tab); builder.append("bodyYn(제목여부):").append(bodyYn).append('\n'); }
		if(attchYn!=null) { if(tab!=null) builder.append(tab); builder.append("attchYn(첨부여부):").append(attchYn).append('\n'); }
		if(seculCdList!=null) { if(tab!=null) builder.append(tab); builder.append("seculCdList(보안등급코드 목록):"); appendStringListTo(builder, seculCdList, tab); builder.append('\n'); }
		if(noSeculYn!=null) { if(tab!=null) builder.append(tab); builder.append("noSeculYn(보안등급 사용안함 여부):").append(noSeculYn).append('\n'); }
		if(seculAplyMdBxIdList!=null) { if(tab!=null) builder.append(tab); builder.append("seculAplyMdBxIdList(보안등급코드 적용 모듈함ID 목록):"); appendStringListTo(builder, seculAplyMdBxIdList, tab); builder.append('\n'); }
		if(kwdList!=null) { if(tab!=null) builder.append(tab); builder.append("kwdList(키워드 목록):"); appendVoListTo(builder, kwdList, tab); builder.append('\n'); }
		if(catList!=null) { if(tab!=null) builder.append(tab); builder.append("catList(카테고리 목록):"); appendVoListTo(builder, catList, tab); builder.append('\n'); }
//		if(authList!=null) { if(tab!=null) builder.append(tab); builder.append("authList(권한 목록):"); appendVoListTo(builder, authList, tab); builder.append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(deptId!=null) { if(tab!=null) builder.append(tab); builder.append("deptId(부서ID):").append(deptId).append('\n'); }
		if(subDeptList!=null) { if(tab!=null) builder.append(tab); builder.append("subDeptList(하위부서 목록):"); appendStringListTo(builder, subDeptList, tab); builder.append('\n'); }
		if(cnt!=null) { if(tab!=null) builder.append(tab); builder.append("cnt(수):").append(cnt).append('\n'); }
		if(smry!=null) { if(tab!=null) builder.append(tab); builder.append("smry(요약):").append(smry).append('\n'); }
		super.toString(builder, tab);
	}
}
