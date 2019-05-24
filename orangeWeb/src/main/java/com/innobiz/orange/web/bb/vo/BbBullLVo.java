package com.innobiz.orange.web.bb.vo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 게시물(BB_X000X_L) 테이블 VO
 */
public class BbBullLVo extends CommonVoImpl implements Comparable<BbBullLVo> {

	/** serialVersionUID */
	private static final long serialVersionUID = -8093051895817019650L;

	/** 게시물ID */
	private Integer bullId;

	/** 회사ID */
	private String compId;

	/** 게시판ID */
	private String brdId;

	/** 부모게시물ID */
	private Integer bullPid;

	/** 답변그룹ID */
	private Integer replyGrpId;

	/** 답변순서 */
	private Integer replyOrdr;

	/** 답변단계 */
	private Integer replyDpth;

	/** 게시물상태코드-T(임시저장),R(예약저장),S(상신),J(반려),B(게시) */
	private String bullStatCd;

	/** 게시물예약여부 */
	private String bullRezvYn;

	/** 대상부서지정여부 */
	private String tgtDeptYn;

	/** 대상사용자지정여부 */
	private String tgtUserYn;

	/** 제목 */
	private String subj;

	/** 카테고리ID */
	private String catId;

	/** 게시예약일시 */
	private String bullRezvDt;

	/** 게시완료일시 */
	private String bullExprDt;

	/** 본문 */
	private String cont;

	/** 조회수 */
	private Integer readCnt;

	/** 찬성수 */
	private Integer prosCnt;

	/** 반대수 */
	private Integer consCnt;

	/** 추천수 */
	private Integer recmdCnt;

	/** 점수 */
	private Integer scre;

	/** 등록자UID */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	/** 확장컬럼01 */
	private String c01;

	/** 확장컬럼02 */
	private String c02;

	/** 확장컬럼03 */
	private String c03;

	/** 확장컬럼04 */
	private String c04;

	/** 확장컬럼05 */
	private String c05;

	/** 확장컬럼06 */
	private String c06;

	/** 확장컬럼07 */
	private String c07;

	/** 확장컬럼08 */
	private String c08;

	/** 확장컬럼09 */
	private String c09;

	/** 확장컬럼10 */
	private String c10;

	/** 확장컬럼11 */
	private String c11;

	/** 확장컬럼12 */
	private String c12;

	/** 확장컬럼13 */
	private String c13;

	/** 확장컬럼14 */
	private String c14;

	/** 확장컬럼15 */
	private String c15;

	/** 확장컬럼16 */
	private String c16;

	/** 확장컬럼17 */
	private String c17;

	/** 확장컬럼18 */
	private String c18;

	/** 확장컬럼19 */
	private String c19;

	/** 확장컬럼20 */
	private String c20;

	/** 익명등록자명 */
	private String anonRegrNm;

	/** 부서ID */
	private String deptId;
	
	/** 부서ID목록 */
	private List<String> deptIdList;
	
	/** 부서명 */
	private String deptNm;

	// 추가 컬럼

	/** 게시물 테이블명 */
	private String tableName;

	/** 게시판명 */
	private String brdNm;

	/** 게시판타입코드 - N(일반), A(익명) */
	private String brdTypCd;

	/** 카테고리명 */
	private String catNm;

	/** 등록자명 */
	private String regrNm;

	/** 수정자명 */
	private String modrNm;

	/** 보안여부 */
	private String secuYn;

	/** 긴급여부 */
	private String ugntYn;

	/** 공지여부 */
	private String notcYn;

	/** 새글여부 */
	private String newYn;

	/** 조회여부 */
	private String readYn;

	/** 게시판종류코드 */
	private String brdKndCd;

	/** 게시판ID들(콤마구분) */
	private String brdIds;

	/** 한줄답변수 */
	private Integer cmtCnt;

	/** 첨부파일수 */
	private Integer fileCnt;

	/** 게시물사진 */
	private BaBullPhotoDVo photoVo;

	/** 확장컬럼명목록 */
	private List<String> exColmNmList;

	/** 검색조건(시작년월일) */
	private String strtYmd;

	/** 검색조건(종료년월일) */
	private String endYmd;

	/** 검색조건(유효기간만료) */
	private String schExpr;

	/** 검색조건(등록자UID) */
	private String schRegrUid;

	/** 검색조건(등록자명) */
	private String schRegrNm;

	/** 검색조건(사용자UID) */
	private String schUserUid;

	/** 검색조건(조직ID목록) */
	private String[] schOrgPids;

	/** 검색조건(부서ID) */
	private String schDeptId;
	
	/** 검색조건(조직ID) */
	private String schOrgId;
	
	/** 회사ID목록 */
	private List<String> compIdList;
	

/** 추가 */
	/** 표시순서 */
	private String dispOrdr;
	
	/** 조회이력사용여부 */
	private String readHstUseYn;
	
	/** 답변원문글작성자ID */
	private String replyGrpRegrUid;
	
	/** LOB 데이터 조회 여부 */
	private boolean withLob = true;

	/** 게시대상 조회여부  */
	private String tgtSchYn="N";
	
	/** 확장컬럼 맵 */
	private Map<String,Object> exColMap;
	
	/** 목록조건 적용여부 */
	private boolean isListCondApply=false;
	
	/** 목록조건 paramMap */
	private Map<String,List<String[]>> paramMap;
	
	/** 대표 제목  */
	private String viewTitle;
	
	public boolean isWithLob() {
		return withLob;
	}

	public void setWithLob(boolean withLob) {
		this.withLob = withLob;
	}

	/** 기본 생성자 */
	public BbBullLVo() {
		super();
	}

	/** 테이블명과 게시판종류코드를 받는 생성자 */
	public BbBullLVo(String tableName, String kndCd) {
		this(tableName, kndCd, "B");
	}

	/** 테이블명과 게시판종류코드와 게시판상태코드를 받는 생성자 */
	public BbBullLVo(String tableName, String kndCd, String statCd) {
		super();
		this.tableName = tableName;
		this.brdKndCd = kndCd;
		this.bullStatCd = statCd;
	}

	/** 게시물ID */
	public Integer getBullId() {
		return bullId;
	}

	/** 게시물ID */
	public void setBullId(Integer bullId) {
		this.bullId = bullId;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 게시판ID */
	public String getBrdId() {
		return brdId;
	}

	/** 게시판ID */
	public void setBrdId(String brdId) {
		this.brdId = brdId;
	}

	/** 부모게시물ID */
	public Integer getBullPid() {
		return bullPid;
	}

	/** 부모게시물ID */
	public void setBullPid(Integer bullPid) {
		this.bullPid = bullPid;
	}

	/** 답변그룹ID */
	public Integer getReplyGrpId() {
		return replyGrpId;
	}

	/** 답변그룹ID */
	public void setReplyGrpId(Integer replyGrpId) {
		this.replyGrpId = replyGrpId;
	}

	/** 답변순서 */
	public Integer getReplyOrdr() {
		return replyOrdr;
	}

	/** 답변순서 */
	public void setReplyOrdr(Integer replyOrdr) {
		this.replyOrdr = replyOrdr;
	}

	/** 답변단계 */
	public Integer getReplyDpth() {
		return replyDpth;
	}

	/** 답변단계 */
	public void setReplyDpth(Integer replyDpth) {
		this.replyDpth = replyDpth;
	}

	/** 게시물상태코드 */
	public String getBullStatCd() {
		return bullStatCd;
	}

	/** 게시물상태코드 */
	public void setBullStatCd(String bullStatCd) {
		this.bullStatCd = bullStatCd;
	}

	/** 게시물예약여부 */
	public String getBullRezvYn() {
		return bullRezvYn;
	}

	/** 게시물예약여부 */
	public void setBullRezvYn(String bullRezvYn) {
		this.bullRezvYn = bullRezvYn;
	}

	/** 대상부서지정여부 */
	public String getTgtDeptYn() {
		return tgtDeptYn;
	}

	/** 대상부서지정여부 */
	public void setTgtDeptYn(String tgtDeptYn) {
		this.tgtDeptYn = tgtDeptYn;
	}

	/** 대상사용자지정여부 */
	public String getTgtUserYn() {
		return tgtUserYn;
	}

	/** 대상사용자지정여부 */
	public void setTgtUserYn(String tgtUserYn) {
		this.tgtUserYn = tgtUserYn;
	}

	/** 제목 */
	public String getSubj() {
		return subj;
	}

	/** 제목 */
	public void setSubj(String subj) {
		this.subj = subj;
	}

	/** 카테고리ID */
	public String getCatId() {
		return catId;
	}

	/** 카테고리ID */
	public void setCatId(String catId) {
		this.catId = catId;
	}

	/** 게시예약일시 */
	public String getBullRezvDt() {
		return bullRezvDt;
	}

	/** 게시예약일시 */
	public void setBullRezvDt(String bullRezvDt) {
		this.bullRezvDt = bullRezvDt;
	}

	/** 게시완료일시 */
	public String getBullExprDt() {
		return bullExprDt;
	}

	/** 게시완료일시 */
	public void setBullExprDt(String bullExprDt) {
		this.bullExprDt = bullExprDt;
	}

	/** 본문 */
	public String getCont() {
		return cont;
	}

	/** 본문 */
	public void setCont(String cont) {
		this.cont = cont;
	}

	/** 조회수 */
	public Integer getReadCnt() {
		return readCnt;
	}

	/** 조회수 */
	public void setReadCnt(Integer readCnt) {
		this.readCnt = readCnt;
	}

	/** 찬성수 */
	public Integer getProsCnt() {
		return prosCnt;
	}

	/** 찬성수 */
	public void setProsCnt(Integer prosCnt) {
		this.prosCnt = prosCnt;
	}

	/** 반대수 */
	public Integer getConsCnt() {
		return consCnt;
	}

	/** 반대수 */
	public void setConsCnt(Integer consCnt) {
		this.consCnt = consCnt;
	}

	/** 추천수 */
	public Integer getRecmdCnt() {
		return recmdCnt;
	}

	/** 추천수 */
	public void setRecmdCnt(Integer recmdCnt) {
		this.recmdCnt = recmdCnt;
	}

	/** 점수 */
	public Integer getScre() {
		return scre;
	}

	/** 점수 */
	public void setScre(Integer scre) {
		this.scre = scre;
	}

	/** 등록자UID */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자UID */
	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}

	/** 등록일시 */
	public String getRegDt() {
		return regDt;
	}

	/** 등록일시 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	/** 수정자 */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자 */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 답변원문글작성자ID */
	public String getReplyGrpRegrUid() {
		return replyGrpRegrUid;
	}

	/** 답변원문글작성자ID */
	public void setReplyGrpRegrUid(String replyGrpRegrUid) {
		this.replyGrpRegrUid = replyGrpRegrUid;
	}

	
	/** 확장컬럼 */
	public String getExColm(String colmNm) {
		if (colmNm == null || "".equals(colmNm)) return null;
		switch (Integer.parseInt(colmNm.substring(1))) {
			case 1: return c01;
			case 2: return c02;
			case 3: return c03;
			case 4: return c04;
			case 5: return c05;
			case 6: return c06;
			case 7: return c07;
			case 8: return c08;
			case 9: return c09;
			case 10: return c10;
			case 11: return c11;
			case 12: return c12;
			case 13: return c13;
			case 14: return c14;
			case 15: return c15;
			case 16: return c16;
			case 17: return c17;
			case 18: return c18;
			case 19: return c19;
			case 20: return c20;
		}
		return null;
	}

	/** 확장컬럼01 */
	public String getC01() {
		return c01;
	}

	/** 확장컬럼01 */
	public void setC01(String c01) {
		this.c01 = c01;
	}

	/** 확장컬럼02 */
	public String getC02() {
		return c02;
	}

	/** 확장컬럼02 */
	public void setC02(String c02) {
		this.c02 = c02;
	}

	/** 확장컬럼03 */
	public String getC03() {
		return c03;
	}

	/** 확장컬럼03 */
	public void setC03(String c03) {
		this.c03 = c03;
	}

	/** 확장컬럼04 */
	public String getC04() {
		return c04;
	}

	/** 확장컬럼04 */
	public void setC04(String c04) {
		this.c04 = c04;
	}

	/** 확장컬럼05 */
	public String getC05() {
		return c05;
	}

	/** 확장컬럼05 */
	public void setC05(String c05) {
		this.c05 = c05;
	}

	/** 확장컬럼06 */
	public String getC06() {
		return c06;
	}

	/** 확장컬럼06 */
	public void setC06(String c06) {
		this.c06 = c06;
	}

	/** 확장컬럼07 */
	public String getC07() {
		return c07;
	}

	/** 확장컬럼07 */
	public void setC07(String c07) {
		this.c07 = c07;
	}

	/** 확장컬럼08 */
	public String getC08() {
		return c08;
	}

	/** 확장컬럼08 */
	public void setC08(String c08) {
		this.c08 = c08;
	}

	/** 확장컬럼09 */
	public String getC09() {
		return c09;
	}

	/** 확장컬럼09 */
	public void setC09(String c09) {
		this.c09 = c09;
	}

	/** 확장컬럼10 */
	public String getC10() {
		return c10;
	}

	/** 확장컬럼10 */
	public void setC10(String c10) {
		this.c10 = c10;
	}

	/** 확장컬럼11 */
	public String getC11() {
		return c11;
	}

	/** 확장컬럼11 */
	public void setC11(String c11) {
		this.c11 = c11;
	}

	/** 확장컬럼12 */
	public String getC12() {
		return c12;
	}

	/** 확장컬럼12 */
	public void setC12(String c12) {
		this.c12 = c12;
	}

	/** 확장컬럼13 */
	public String getC13() {
		return c13;
	}

	/** 확장컬럼13 */
	public void setC13(String c13) {
		this.c13 = c13;
	}

	/** 확장컬럼14 */
	public String getC14() {
		return c14;
	}

	/** 확장컬럼14 */
	public void setC14(String c14) {
		this.c14 = c14;
	}

	/** 확장컬럼15 */
	public String getC15() {
		return c15;
	}

	/** 확장컬럼15 */
	public void setC15(String c15) {
		this.c15 = c15;
	}

	/** 확장컬럼16 */
	public String getC16() {
		return c16;
	}

	/** 확장컬럼16 */
	public void setC16(String c16) {
		this.c16 = c16;
	}

	/** 확장컬럼17 */
	public String getC17() {
		return c17;
	}

	/** 확장컬럼17 */
	public void setC17(String c17) {
		this.c17 = c17;
	}

	/** 확장컬럼18 */
	public String getC18() {
		return c18;
	}

	/** 확장컬럼18 */
	public void setC18(String c18) {
		this.c18 = c18;
	}

	/** 확장컬럼19 */
	public String getC19() {
		return c19;
	}

	/** 확장컬럼19 */
	public void setC19(String c19) {
		this.c19 = c19;
	}

	/** 확장컬럼20 */
	public String getC20() {
		return c20;
	}

	/** 확장컬럼20 */
	public void setC20(String c20) {
		this.c20 = c20;
	}

	/** 익명등록자명 */
	public String getAnonRegrNm() {
		return anonRegrNm;
	}

	/** 익명등록자명 */
	public void setAnonRegrNm(String anonRegrNm) {
		this.anonRegrNm = anonRegrNm;
	}

	/** 부서ID */
	public String getDeptId() {
		return deptId;
	}

	/** 부서ID */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	/** 부서ID목록 */
	public List<String> getDeptIdList() {
		return deptIdList;
	}

	public void setDeptIdList(List<String> deptIdList) {
		this.deptIdList = deptIdList;
	}

	/** 부서명 */
	public String getDeptNm() {
		return deptNm;
	}

	/** 부서명 */
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}

	/** 게시물 테이블명 */
	public String getTableName() {
		return tableName;
	}

	/** 게시물 테이블명 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** 게시판명 */
	public String getBrdNm() {
		return brdNm;
	}

	/** 게시판명 */
	public void setBrdNm(String brdNm) {
		this.brdNm = brdNm;
	}

	/** 게시판타입코드 - N(일반), A(익명) */
	public String getBrdTypCd() {
		return brdTypCd;
	}

	/** 게시판타입코드 - N(일반), A(익명) */
	public void setBrdTypCd(String brdTypCd) {
		this.brdTypCd = brdTypCd;
	}

	/** 카테고리명 */
	public String getCatNm() {
		return catNm;
	}

	/** 카테고리명 */
	public void setCatNm(String catNm) {
		this.catNm = catNm;
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

	/** 보안여부 */
	public String getSecuYn() {
		return secuYn;
	}

	/** 보안여부 */
	public void setSecuYn(String secuYn) {
		this.secuYn = secuYn;
	}

	/** 긴급여부 */
	public String getUgntYn() {
		return ugntYn;
	}

	/** 긴급여부 */
	public void setUgntYn(String ugntYn) {
		this.ugntYn = ugntYn;
	}

	/** 공지여부 */
	public String getNotcYn() {
		return notcYn;
	}

	/** 공지여부 */
	public void setNotcYn(String notcYn) {
		this.notcYn = notcYn;
	}

	/** 새글여부 */
	public String getNewYn() {
		return newYn;
	}

	/** 새글여부 */
	public void setNewYn(String newYn) {
		this.newYn = newYn;
	}

	/** 조회여부 */
	public String getReadYn() {
		return readYn;
	}

	/** 조회여부 */
	public void setReadYn(String readYn) {
		this.readYn = readYn;
	}

	/** 게시판종류코드 */
	public String getBrdKndCd() {
		return brdKndCd;
	}

	/** 게시판종류코드 */
	public void setBrdKndCd(String brdKndCd) {
		this.brdKndCd = brdKndCd;
	}

	/** 게시판ID들(콤마구분) */
	public String getBrdIds() {
		return brdIds;
	}

	/** 게시판ID들(콤마구분) */
	public void setBrdIds(String brdIds) {
		this.brdIds = brdIds;
	}

	/** 한줄답변수 */
	public Integer getCmtCnt() {
		return cmtCnt;
	}

	/** 한줄답변수 */
	public void setCmtCnt(Integer cmtCnt) {
		this.cmtCnt = cmtCnt;
	}

	/** 첨부파일수 */
	public Integer getFileCnt() {
		return fileCnt;
	}

	/** 첨부파일수 */
	public void setFileCnt(Integer fileCnt) {
		this.fileCnt = fileCnt;
	}

	/** 게시물사진 */
	public BaBullPhotoDVo getPhotoVo() {
		return photoVo;
	}

	/** 게시물사진 */
	public void setPhotoVo(BaBullPhotoDVo photoVo) {
		this.photoVo = photoVo;
	}

	/** 확장컬럼명목록 */
	public List<String> getExColmNmList() {
		return exColmNmList;
	}

	/** 확장컬럼명목록 */
	public void setExColmNmList(List<String> exColmNmList) {
		this.exColmNmList = exColmNmList;
	}

	/** 검색조건(시작년월일) */
	public String getStrtYmd() {
		return strtYmd;
	}

	/** 검색조건(시작년월일) */
	public void setStrtYmd(String strtYmd) {
		this.strtYmd = strtYmd;
	}

	/** 검색조건(종료년월일) */
	public String getEndYmd() {
		return endYmd;
	}

	/** 검색조건(종료년월일) */
	public void setEndYmd(String endYmd) {
		this.endYmd = endYmd;
	}

	/** 검색조건(유효기간만료) */
	public String getSchExpr() {
		return schExpr;
	}

	/** 검색조건(유효기간만료) */
	public void setSchExpr(String schExpr) {
		this.schExpr = schExpr;
	}

	/** 검색조건(등록자UID) */
	public String getSchRegrUid() {
		return schRegrUid;
	}

	/** 검색조건(등록자UID) */
	public void setSchRegrUid(String schRegrUid) {
		this.schRegrUid = schRegrUid;
	}

	/** 검색조건(등록자명) */
	public String getSchRegrNm() {
		return schRegrNm;
	}

	/** 검색조건(등록자명) */
	public void setSchRegrNm(String schRegrNm) {
		this.schRegrNm = schRegrNm;
	}

	/** 검색조건(사용자UID) */
	public String getSchUserUid() {
		return schUserUid;
	}

	/** 검색조건(사용자UID) */
	public void setSchUserUid(String schUserUid) {
		this.schUserUid = schUserUid;
	}

	/** 검색조건(조직ID목록) */
	public String[] getSchOrgPids() {
		return schOrgPids;
	}

	/** 검색조건(조직ID목록) */
	public void setSchOrgPids(String[] schOrgPids) {
		this.schOrgPids = schOrgPids;
	}

	/** 검색조건(부서ID) */
	public String getSchDeptId() {
		return schDeptId;
	}

	/** 검색조건(부서ID) */
	public void setSchDeptId(String schDeptId) {
		this.schDeptId = schDeptId;
	}
	
	/** 검색조건(조직ID) */
	public String getSchOrgId() {
		return schOrgId;
	}
	
	/** 검색조건(조직ID) */
	public void setSchOrgId(String schOrgId) {
		this.schOrgId = schOrgId;
	}
	
	/** 회사ID목록 */
	public List<String> getCompIdList() {
		return compIdList;
	}

	public void setCompIdList(List<String> compIdList) {
		this.compIdList = compIdList;
	}

	/** 표시순서 */
	public String getDispOrdr() {
		return dispOrdr;
	}

	public void setDispOrdr(String dispOrdr) {
		this.dispOrdr = dispOrdr;
	}
	
	/** 조회이력사용여부 */
	public String getReadHstUseYn() {
		return readHstUseYn;
	}
	
	/** 조회이력사용여부 */
	public void setReadHstUseYn(String readHstUseYn) {
		this.readHstUseYn = readHstUseYn;
	}
	
	/** 게시대상 조회여부  */
	public String getTgtSchYn() {
		return tgtSchYn;
	}

	public void setTgtSchYn(String tgtSchYn) {
		this.tgtSchYn = tgtSchYn;
	}
	
	/** 확장컬럼 맵 */
	public Map<String, Object> getExColMap() {
		return exColMap;
	}

	public void setExColMap(Map<String, Object> exColMap) {
		this.exColMap = exColMap;
	}
	
	/** 목록조건 적용여부 */
	public boolean isListCondApply() {
		return isListCondApply;
	}

	public void setListCondApply(boolean isListCondApply) {
		this.isListCondApply = isListCondApply;
	}

	/** 목록조건 paramMap */
	public Map<String, List<String[]>> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, List<String[]>> paramMap) {
		this.paramMap = paramMap;
	}
	
	/** 대표 제목  */
	public String getViewTitle() {
		return viewTitle;
	}

	public void setViewTitle(String viewTitle) {
		this.viewTitle = viewTitle;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BbBullLDao.selectBbBullL";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BbBullLDao.insertBbBullL";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BbBullLDao.updateBbBullL";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BbBullLDao.deleteBbBullL";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BbBullLDao.countBbBullL";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시물]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append함 */
	public void toString(StringBuilder sb, String tab) {
		if (bullId != null) { if (tab != null) sb.append(tab); sb.append("bullId(게시물ID):").append(bullId).append('\n'); }
		if (compId != null) { if (tab != null) sb.append(tab); sb.append("compId(회사ID):").append(compId).append('\n'); }
		if (brdId != null) { if (tab != null) sb.append(tab); sb.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if (brdNm != null) { if (tab != null) sb.append(tab); sb.append("brdNm(게시판명):").append(brdNm).append('\n'); }
		if (bullPid != null) { if (tab != null) sb.append(tab); sb.append("bullPid(부모게시물ID):").append(bullPid).append('\n'); }
		if (replyGrpId != null) { if (tab != null) sb.append(tab); sb.append("replyGrpId(답변그룹ID):").append(replyGrpId).append('\n'); }
		if (replyOrdr != null) { if (tab != null) sb.append(tab); sb.append("replyOrdr(답변순서):").append(replyOrdr).append('\n'); }
		if (replyDpth != null) { if (tab != null) sb.append(tab); sb.append("replyDpth(답변단계):").append(replyDpth).append('\n'); }
		if (bullStatCd != null) { if (tab != null) sb.append(tab); sb.append("bullStatCd(게시물상태코드):").append(bullStatCd).append('\n'); }
		if (bullRezvYn != null) { if (tab != null) sb.append(tab); sb.append("bullRezvYn(게시물예약여부):").append(bullRezvYn).append('\n'); }
		if (tgtDeptYn != null) { if (tab != null) sb.append(tab); sb.append("tgtDeptYn(대상부서지정여부):").append(tgtDeptYn).append('\n'); }
		if (tgtUserYn != null) { if (tab != null) sb.append(tab); sb.append("tgtUserYn(대상사용자지정여부):").append(tgtUserYn).append('\n'); }
		if (subj != null) { if (tab != null) sb.append(tab); sb.append("subj(제목):").append(subj).append('\n'); }
		if (catId != null) { if (tab != null) sb.append(tab); sb.append("catId(카테고리ID):").append(catId).append('\n'); }
		if (catNm != null) { if (tab != null) sb.append(tab); sb.append("catNm(카테고리명):").append(catNm).append('\n'); }
		if (bullRezvDt != null) { if (tab != null) sb.append(tab); sb.append("bullRezvDt(게시예약일시):").append(bullRezvDt).append('\n'); }
		if (bullExprDt != null) { if (tab != null) sb.append(tab); sb.append("bullExprDt(게시완료일시):").append(bullExprDt).append('\n'); }
		if (cont != null) { if (tab != null) sb.append(tab); sb.append("cont(본문):").append(cont).append('\n'); }
		if (readCnt != null) { if (tab != null) sb.append(tab); sb.append("readCnt(조회수):").append(readCnt).append('\n'); }
		if (prosCnt != null) { if (tab != null) sb.append(tab); sb.append("prosCnt(찬성수):").append(prosCnt).append('\n'); }
		if (consCnt != null) { if (tab != null) sb.append(tab); sb.append("consCnt(반대수):").append(consCnt).append('\n'); }
		if (recmdCnt != null) { if (tab != null) sb.append(tab); sb.append("recmdCnt(추천수):").append(recmdCnt).append('\n'); }
		if (scre != null) { if (tab != null) sb.append(tab); sb.append("scre(점수):").append(scre).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		if (c01 != null) { if (tab != null) sb.append(tab); sb.append("c01(확장컬럼01):").append(c01).append('\n'); }
		if (c02 != null) { if (tab != null) sb.append(tab); sb.append("c02(확장컬럼02):").append(c02).append('\n'); }
		if (c03 != null) { if (tab != null) sb.append(tab); sb.append("c03(확장컬럼03):").append(c03).append('\n'); }
		if (c04 != null) { if (tab != null) sb.append(tab); sb.append("c04(확장컬럼04):").append(c04).append('\n'); }
		if (c05 != null) { if (tab != null) sb.append(tab); sb.append("c05(확장컬럼05):").append(c05).append('\n'); }
		if (c06 != null) { if (tab != null) sb.append(tab); sb.append("c06(확장컬럼06):").append(c06).append('\n'); }
		if (c07 != null) { if (tab != null) sb.append(tab); sb.append("c07(확장컬럼07):").append(c07).append('\n'); }
		if (c08 != null) { if (tab != null) sb.append(tab); sb.append("c08(확장컬럼08):").append(c08).append('\n'); }
		if (c09 != null) { if (tab != null) sb.append(tab); sb.append("c09(확장컬럼09):").append(c09).append('\n'); }
		if (c10 != null) { if (tab != null) sb.append(tab); sb.append("c10(확장컬럼10):").append(c10).append('\n'); }
		if (c11 != null) { if (tab != null) sb.append(tab); sb.append("c11(확장컬럼11):").append(c11).append('\n'); }
		if (c12 != null) { if (tab != null) sb.append(tab); sb.append("c12(확장컬럼12):").append(c12).append('\n'); }
		if (c13 != null) { if (tab != null) sb.append(tab); sb.append("c13(확장컬럼13):").append(c13).append('\n'); }
		if (c14 != null) { if (tab != null) sb.append(tab); sb.append("c14(확장컬럼14):").append(c14).append('\n'); }
		if (c15 != null) { if (tab != null) sb.append(tab); sb.append("c15(확장컬럼15):").append(c15).append('\n'); }
		if (c16 != null) { if (tab != null) sb.append(tab); sb.append("c16(확장컬럼16):").append(c16).append('\n'); }
		if (c17 != null) { if (tab != null) sb.append(tab); sb.append("c17(확장컬럼17):").append(c17).append('\n'); }
		if (c18 != null) { if (tab != null) sb.append(tab); sb.append("c18(확장컬럼18):").append(c18).append('\n'); }
		if (c19 != null) { if (tab != null) sb.append(tab); sb.append("c19(확장컬럼19):").append(c19).append('\n'); }
		if (c20 != null) { if (tab != null) sb.append(tab); sb.append("c20(확장컬럼20):").append(c20).append('\n'); }
		if (anonRegrNm != null) { if (tab != null) sb.append(tab); sb.append("anonRegrNm(익명등록자명):").append(anonRegrNm).append('\n'); }
		if (deptId != null) { if (tab != null) sb.append(tab); sb.append("deptId(부서ID):").append(deptId).append('\n'); }
		if (tableName != null) { if (tab != null) sb.append(tab); sb.append("tableName(게시물 테이블명):").append(tableName).append('\n'); }
		if (brdTypCd != null) { if (tab != null) sb.append(tab); sb.append("brdTypCd(게시판타입코드):").append(brdTypCd).append('\n'); }
		if (regrNm != null) { if (tab != null) sb.append(tab); sb.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if (modrNm != null) { if (tab != null) sb.append(tab); sb.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		if (secuYn != null) { if (tab != null) sb.append(tab); sb.append("secuYn(보안여부):").append(secuYn).append('\n'); }
		if (ugntYn != null) { if (tab != null) sb.append(tab); sb.append("ugntYn(긴급여부):").append(ugntYn).append('\n'); }
		if (notcYn != null) { if (tab != null) sb.append(tab); sb.append("notcYn(공지여부):").append(notcYn).append('\n'); }
		if (newYn != null) { if (tab != null) sb.append(tab); sb.append("newYn(새글여부):").append(newYn).append('\n'); }
		if (readYn != null) { if (tab != null) sb.append(tab); sb.append("readYn(조회여부):").append(readYn).append('\n'); }
		if (brdKndCd != null) { if (tab != null) sb.append(tab); sb.append("brdKndCd(게시판종류코드):").append(brdKndCd).append('\n'); }
		if (brdIds != null) { if (tab != null) sb.append(tab); sb.append("brdIds(게시판ID들(콤마구분)):").append(brdIds).append('\n'); }
		if (cmtCnt != null) { if (tab != null) sb.append(tab); sb.append("cmtCnt(한줄답변수):").append(cmtCnt).append('\n'); }
		if (fileCnt != null) { if (tab != null) sb.append(tab); sb.append("fileCnt(첨부파일수):").append(fileCnt).append('\n'); }
		if (photoVo != null) { if (tab != null) sb.append(tab); sb.append("photoVo(게시물사진):").append(photoVo).append('\n'); }
		if (exColmNmList != null) { if (tab != null) sb.append(tab); sb.append("exColmNmList(확장컬럼명목록):").append(exColmNmList).append('\n'); }
		if (strtYmd != null) { if (tab != null) sb.append(tab); sb.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if (endYmd != null) { if (tab != null) sb.append(tab); sb.append("endYmd(종료년월일):").append(endYmd).append('\n'); }
		if (schExpr != null) { if (tab != null) sb.append(tab); sb.append("schExpr(검색조건(유효기간만료)):").append(schExpr).append('\n'); }
		if (schRegrUid != null) { if (tab != null) sb.append(tab); sb.append("schRegrUid(검색조건(등록자UID)):").append(schRegrUid).append('\n'); }
		if (schRegrNm != null) { if (tab != null) sb.append(tab); sb.append("schRegrNm(검색조건(등록자명)):").append(schRegrNm).append('\n'); }
		if (schUserUid != null) { if (tab != null) sb.append(tab); sb.append("schUserUid(검색조건(사용자UID)):").append(schUserUid).append('\n'); }
		if (schOrgPids != null) { if (tab != null) sb.append(tab); sb.append("schOrgPids(검색조건(조직ID목록)):").append(Arrays.toString(schOrgPids)).append('\n'); }
		if (schDeptId != null) { if (tab != null) sb.append(tab); sb.append("schDeptId(검색조건(부서ID)):").append(schDeptId).append('\n'); }
		if (replyGrpRegrUid != null) { if (tab != null) sb.append(tab); sb.append("replyGrpRegrUid:").append(replyGrpRegrUid).append('\n'); }
		if (dispOrdr != null) { if (tab != null) sb.append(tab); sb.append("dispOrdr(표시순서):").append(dispOrdr).append('\n'); }
		if (withLob) { if(tab!=null) sb.append(tab); sb.append("withLob(LOB 데이터 조회 여부):").append(withLob).append('\n'); }
		super.toString(sb, tab);
	}

	/** 정렬을 위해 추가함 */
	@Override
	public int compareTo(BbBullLVo o) {
		if (bullId == null || o == null || o.getBullId() == null) return 0;
		return (bullId < o.getBullId() ? -1 : (bullId.equals(o.getBullId()) ? 0 : 1));
	}
	
}