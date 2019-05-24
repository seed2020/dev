package com.innobiz.orange.web.dm.vo;

import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/06/05 18:07 ******/
/**
* 문서기본(DM_회사약어_ID_L) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmDocLVo extends DmDocDVo {
	/** 문서ID */ 
	private String docId;

 	/** 기본여부 */ 
	private String dftYn;

 	/** 분류체계그룹ID */ 
	private String clsGrpId;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

 	/** 파일 건수 */ 
	private Integer fileCnt;
	
 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 분류명 */ 
	private String clsNm;
	
	/** 분류체계 관계 Vo List*/
	private List<DmClsRVo> dmClsRVoList;
	
	/** 대상 테이블명[이관] */
	private String tgtTableName;
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob = false;
	
	/** 등록자명 */
	private String regrNm;
	
	/** 수정자명 */
	private String modrNm;
	
	/** 추가항목명목록 */
	private List<String> addItemNmList;
	
	/** 확장컬럼01 */
	private String c1;

	/** 확장컬럼02 */
	private String c2;

	/** 확장컬럼03 */
	private String c3;

	/** 확장컬럼04 */
	private String c4;

	/** 확장컬럼05 */
	private String c5;

	/** 확장컬럼06 */
	private String c6;

	/** 확장컬럼07 */
	private String c7;

	/** 확장컬럼08 */
	private String c8;

	/** 확장컬럼09 */
	private String c9;

	/** 확장컬럼10 */
	private String cA;
	
	/** 키워드명 */ 
	private String kwdNm;
	
	/** 최신여부 */ 
	private String newYn;
	
	/** 최신기한일시 */ 
	private String newDdlnDt;
	
	/** 보존기한여부 */ 
	private String keepDdlnYn;
	
	/** 보존기한일시 */ 
	private String keepDdlnDt;
	
	/** 심의여부 */ 
	private String discYn;
	
	/** 심의자UID */ 
	private String discrUid;
	
	/** 상신자UID */ 
	private String submrUid;
	
	/** 하위문서 목록맵 */
	private List<Map<String, Object>> subDocMapList;
	
	/** 이전문서ID */ 
	private String prevDocId;
	
	/** 다음문서ID */ 
	private String nextDocId;
	
	/** 즐겨찾기Vo */
	private DmBumkBVo dmBumkBVo;
	
	/** 즐겨찾기ID */ 
	private String bumkId;
	
	/** 하위포함여부 */ 
	private String withSubYn = "Y";
	
	/** 하위정렬여부 */ 
	private String orderSubYn = "Y";
	
	/** 표시순서 */
	private String dispOrdr;
	
	/** 회사ID */ 
	private String compId;
	
	/** 관련문서 건수 */ 
	private Integer subDocCnt;
	
	/** 인계조직ID */ 
	private String takOrgId;

 	/** 대상조직ID */ 
	private String tgtOrgId;
	
	/** 인계조직명 */ 
	private String takOrgNm;

 	/** 대상조직명 */ 
	private String tgtOrgNm;
	
	/** 인수인계상태코드 */ 
	private String takStatCd;
	
	/** 인수인계상태 */ 
	private String takStatNm;
	
	/** 인계등록일자 */ 
	private String takRegDt;
	
	/** 인수인계기본 */
	private DmTakovrBVo dmTakovrBVo;
	
	/** 오류내용 */ 
	private String errCont;
	
	/** 대상저장소ID */ 
	private String tgtStorId;
	
	/** LOB 데이터 조회 여부 */
	public boolean isWithLob() {
		return withLob;
	}

	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}
	
	/** 분류체계 관계 Vo List*/
 	public List<DmClsRVo> getDmClsRVoList() {
		return dmClsRVoList;
	}

	public void setDmClsRVoList(List<DmClsRVo> dmClsRVoList) {
		this.dmClsRVoList = dmClsRVoList;
	}

	public List<String> getAddItemNmList() {
		return addItemNmList;
	}
	public void setAddItemNmList(List<String> addItemNmList) {
		this.addItemNmList = addItemNmList;
	}
	
	/** 대상 테이블명[이관] */
	public String getTgtTableName() {
		return tgtTableName;
	}

	public void setTgtTableName(String tgtTableName) {
		this.tgtTableName = tgtTableName;
	}

	public void setDocId(String docId) { 
		this.docId = docId;
	}
	/** 문서ID */ 
	public String getDocId() { 
		return docId;
	}

	public void setDftYn(String dftYn) { 
		this.dftYn = dftYn;
	}
	/** 기본여부 */ 
	public String getDftYn() { 
		return dftYn;
	}

	public void setClsGrpId(String clsGrpId) { 
		this.clsGrpId = clsGrpId;
	}
	/** 분류체계그룹ID */ 
	public String getClsGrpId() { 
		return clsGrpId;
	}

	public void setSubj(String subj) { 
		this.subj = subj;
	}
	/** 제목 */ 
	public String getSubj() { 
		return subj;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}

	public void setFileCnt(Integer fileCnt) { 
		this.fileCnt = fileCnt;
	}
	/** 첨부파일건수 */ 
	public Integer getFileCnt() { 
		return fileCnt;
	}

	public void setRegrUid(String regrUid) { 
		this.regrUid = regrUid;
	}

	/** 등록자UID */ 
	public String getRegrUid() { 
		return regrUid;
	}

	public void setRegDt(String regDt) { 
		this.regDt = regDt;
	}
	/** 등록일시 */ 
	public String getRegDt() { 
		return regDt;
	}

	public void setModrUid(String modrUid) { 
		this.modrUid = modrUid;
	}
	/** 수정자UID */ 
	public String getModrUid() { 
		return modrUid;
	}

	public void setModDt(String modDt) { 
		this.modDt = modDt;
	}
	/** 수정일시 */ 
	public String getModDt() { 
		return modDt;
	}
	
	/** 분류명 */ 
	public String getClsNm() {
		return clsNm;
	}

	public void setClsNm(String clsNm) {
		this.clsNm = clsNm;
	}

	/** 확장컬럼01 */
	public String getC1() {
		return c1;
	}

	/** 확장컬럼01 */
	public void setC1(String c1) {
		this.c1 = c1;
	}

	/** 확장컬럼02 */
	public String getC2() {
		return c2;
	}

	/** 확장컬럼02 */
	public void setC2(String c2) {
		this.c2 = c2;
	}

	/** 확장컬럼03 */
	public String getC3() {
		return c3;
	}

	/** 확장컬럼03 */
	public void setC3(String c3) {
		this.c3 = c3;
	}

	/** 확장컬럼04 */
	public String getC4() {
		return c4;
	}

	/** 확장컬럼04 */
	public void setC4(String c4) {
		this.c4 = c4;
	}

	/** 확장컬럼05 */
	public String getC5() {
		return c5;
	}

	/** 확장컬럼05 */
	public void setC5(String c5) {
		this.c5 = c5;
	}

	/** 확장컬럼06 */
	public String getC6() {
		return c6;
	}

	/** 확장컬럼06 */
	public void setC6(String c6) {
		this.c6 = c6;
	}

	/** 확장컬럼07 */
	public String getC7() {
		return c7;
	}

	/** 확장컬럼07 */
	public void setC7(String c7) {
		this.c7 = c7;
	}

	/** 확장컬럼08 */
	public String getC8() {
		return c8;
	}

	/** 확장컬럼08 */
	public void setC8(String c8) {
		this.c8 = c8;
	}

	/** 확장컬럼09 */
	public String getC9() {
		return c9;
	}

	/** 확장컬럼09 */
	public void setC9(String c9) {
		this.c9 = c9;
	}

	/** 확장컬럼10 */
	public String getCA() {
		return cA;
	}

	/** 확장컬럼10 */
	public void setCA(String cA) {
		this.cA = cA;
	}
	
	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}

	/** 등록자명 */
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}
	
	/** 키워드명 */ 
	public String getKwdNm() {
		return kwdNm;
	}

	public void setKwdNm(String kwdNm) {
		this.kwdNm = kwdNm;
	}
	
	/** 최신여부 */ 
	public String getNewYn() {
		return newYn;
	}

	public void setNewYn(String newYn) {
		this.newYn = newYn;
	}
	
	/** 최신기한일시 */ 
	public String getNewDdlnDt() {
		return newDdlnDt;
	}

	public void setNewDdlnDt(String newDdlnDt) {
		this.newDdlnDt = newDdlnDt;
	}
	
	/** 보존기한여부 */ 
	public String getKeepDdlnYn() {
		return keepDdlnYn;
	}
	
	public void setKeepDdlnYn(String keepDdlnYn) {
		this.keepDdlnYn = keepDdlnYn;
	}
	
	/** 보존기한일시 */ 
	public String getKeepDdlnDt() {
		return keepDdlnDt;
	}

	public void setKeepDdlnDt(String keepDdlnDt) {
		this.keepDdlnDt = keepDdlnDt;
	}
	
	/** 심의여부 */ 
	public String getDiscYn() {
		return discYn;
	}

	public void setDiscYn(String discYn) {
		this.discYn = discYn;
	}
	
	/** 심의자UID */ 
	public String getDiscrUid() {
		return discrUid;
	}

	public void setDiscrUid(String discrUid) {
		this.discrUid = discrUid;
	}
	
	/** 상신자UID */ 
	public String getSubmrUid() {
		return submrUid;
	}

	public void setSubmrUid(String submrUid) {
		this.submrUid = submrUid;
	}
	
	/** 하위문서 목록맵 */
	public List<Map<String, Object>> getSubDocMapList() {
		return subDocMapList;
	}

	public void setSubDocMapList(List<Map<String, Object>> subDocMapList) {
		this.subDocMapList = subDocMapList;
	}
	
	public String getPrevDocId() {
		return prevDocId;
	}

	public void setPrevDocId(String prevDocId) {
		this.prevDocId = prevDocId;
	}

	public String getNextDocId() {
		return nextDocId;
	}

	public void setNextDocId(String nextDocId) {
		this.nextDocId = nextDocId;
	}
	
	/** 즐겨찾기ID */
	public String getBumkId() {
		return bumkId;
	}

	public void setBumkId(String bumkId) {
		this.bumkId = bumkId;
	}

	/** 즐겨찾기Vo */
	public DmBumkBVo getDmBumkBVo() {
		return dmBumkBVo;
	}

	public void setDmBumkBVo(DmBumkBVo dmBumkBVo) {
		this.dmBumkBVo = dmBumkBVo;
	}
	
	/** 하위포함여부 */ 
	public String getWithSubYn() {
		return withSubYn;
	}

	public void setWithSubYn(String withSubYn) {
		this.withSubYn = withSubYn;
	}
	
	/** 표시순서 */
	public String getDispOrdr() {
		return dispOrdr;
	}

	public void setDispOrdr(String dispOrdr) {
		this.dispOrdr = dispOrdr;
	}
	
	/** 회사ID */ 
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	/** 관련문서 건수 */ 
	public Integer getSubDocCnt() {
		return subDocCnt;
	}

	public void setSubDocCnt(Integer subDocCnt) {
		this.subDocCnt = subDocCnt;
	}
	
	/** 하위정렬여부 */ 
	public String getOrderSubYn() {
		return orderSubYn;
	}

	public void setOrderSubYn(String orderSubYn) {
		this.orderSubYn = orderSubYn;
	}
	
	/** 인수인계기본 */
	public DmTakovrBVo getDmTakovrBVo() {
		return dmTakovrBVo;
	}

	public void setDmTakovrBVo(DmTakovrBVo dmTakovrBVo) {
		this.dmTakovrBVo = dmTakovrBVo;
	}
	
	/** 조직명 */ 
	public String getTakOrgNm() {
		return takOrgNm;
	}
	public void setTakOrgNm(String takOrgNm) {
		this.takOrgNm = takOrgNm;
	}
	
	/** 대상조직명 */ 
	public String getTgtOrgNm() {
		return tgtOrgNm;
	}
	public void setTgtOrgNm(String tgtOrgNm) {
		this.tgtOrgNm = tgtOrgNm;
	}
	
	/** 인수인계상태코드 */ 
	public String getTakStatCd() {
		return takStatCd;
	}

	public void setTakStatCd(String takStatCd) {
		this.takStatCd = takStatCd;
	}
	
	/** 조직ID */ 
	public String getTakOrgId() {
		return takOrgId;
	}

	public void setTakOrgId(String takOrgId) {
		this.takOrgId = takOrgId;
	}
	
	/** 대상조직ID */ 
	public String getTgtOrgId() {
		return tgtOrgId;
	}

	public void setTgtOrgId(String tgtOrgId) {
		this.tgtOrgId = tgtOrgId;
	}
	
	/** 인수인계상태 */ 
	public String getTakStatNm() {
		return takStatNm;
	}

	public void setTakStatNm(String takStatNm) {
		this.takStatNm = takStatNm;
	}
	
	/** 인계등록일자 */ 
	public String getTakRegDt() {
		return takRegDt;
	}

	public void setTakRegDt(String takRegDt) {
		this.takRegDt = takRegDt;
	}
	
	/** 오류내용 */ 
	public String getErrCont() {
		return errCont;
	}

	public void setErrCont(String errCont) {
		this.errCont = errCont;
	}

	/** 대상저장소ID */ 
	public String getTgtStorId() {
		return tgtStorId;
	}

	public void setTgtStorId(String tgtStorId) {
		this.tgtStorId = tgtStorId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocLDao.selectDmDocL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocLDao.insertDmDocL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocLDao.updateDmDocL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocLDao.deleteDmDocL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmDocLDao.countDmDocL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":문서기본]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(docId!=null) { if(tab!=null) builder.append(tab); builder.append("docId(문서ID-PK):").append(docId).append('\n'); }
		if(dftYn!=null) { if(tab!=null) builder.append(tab); builder.append("dftYn(기본여부):").append(dftYn).append('\n'); }
		if(clsGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("clsGrpId(분류체계그룹ID):").append(clsGrpId).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(fileCnt!=null) { if(tab!=null) builder.append(tab); builder.append("fileCnt(첨부파일건수):").append(fileCnt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
