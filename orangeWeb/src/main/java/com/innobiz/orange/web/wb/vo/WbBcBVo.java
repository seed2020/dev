package com.innobiz.orange.web.wb.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 명함기본(WM_WB_BC_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class WbBcBVo extends CommonVoImpl {
	/** 명함ID */ 
	private String bcId;

 	/** 공개구분코드 */ 
	private String publTypCd;

 	/** 폴더ID */ 
	private String fldId;
	
	/** 폴더명 */ 
	private String fldNm;

 	/** 명함성명 */ 
	private String bcNm;
	
	/** 영문성명 */
	private String bcEnNm;
	
	/** 기본연락처구분코드 */
	private String dftCntcTypCd;
	
 	/** 회사명 */ 
	private String compNm;

 	/** 부서명 */ 
	private String deptNm;

 	/** 직급명 */ 
	private String gradeNm;

 	/** 담당업무내용 */ 
	private String tichCont;

 	/** 주요인사여부 */ 
	private String iptfgYn;

 	/** 팩스번호 */ 
	private String fno;

 	/** 회사_우편번호 */ 
	private String compZipNo;

 	/** 회사_주소 */ 
	private String compAdr;

 	/** 회사_상세주소 */ 
	private String compDetlAdr;

 	/** 자택우편번호 */ 
	private String homeZipNo;

 	/** 자택주소 */ 
	private String homeAdr;

 	/** 자택상세주소 */ 
	private String homeDetlAdr;

 	/** 즐겨찾기여부 */ 
	private String bumkYn;

 	/** 사진파일ID */ 
	private String photoFileId;

 	/** 성별코드 */ 
	private String genCd;

 	/** 국적코드 */ 
	private String natyCd;

 	/** 생년월일 */ 
	private String birth;

 	/** 생년월일월력코드 */ 
	private String birthSclcCd;

 	/** 결혼기념일 */ 
	private String weddAnnv;

 	/** 결혼기념일월력코드 */ 
	private String weddAnnvSclcCd;

 	/** 개인홈페이지URL */ 
	private String psnHpageUrl;

 	/** 회사홈페이지URL */ 
	private String compHpageUrl;

 	/** 취미내용 */ 
	private String hobyCont;

 	/** 특기사항내용 */ 
	private String spectCont;

 	/** 초등학교명 */ 
	private String eschlNm;

 	/** 중학교명 */ 
	private String mschlNm;

 	/** 고등학교명 */ 
	private String hschlNm;

 	/** 대학교내용 */ 
	private String univCont;

 	/** 약력내용 */ 
	private String bhistCont;

 	/** 친밀도ID */ 
	private String clnsId;

 	/** 비고 */ 
	private String noteCont;

 	/** 첨부파일ID */ 
	private String attFileId;

 	/** 명함등록구분코드 */ 
	private String bcRegTypCd;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;

 	/** 삭제여부 */ 
	private String delYn;
	
	/** 회사ID */
	private String compId;
	
	/** 원본명함ID */ 
	private String originalBcId;
	
	/** 메인설정여부 */ 
	private String mainSetupYn;
	
	/** 사본명함ID */ 
	private String duplicateBcId;
	
	/** 초성명 */ 
	private String initialNm;
	
	/** 공개폴더ID */
	private String openFldId;
	
/** 추가 */
	/** 회사전화번호 */ 
	private String compPhon;
	
	/** 이동전화번호 */ 
	private String mbno;
	
	/** 자택전화번호 */ 
	private String homePhon;
	
	/** 이메일 */ 
	private String email;
	
	/** 성별이름 */ 
	private String genNm;
	
	/** 국적명 */ 
	private String natyNm;
	
	/** 친밀도명 */ 
	private String clnsNm;
	
	/** 연락처 목록*/
	private List<WbBcCntcDVo> wbBcCntcDVo;
	
	/** 이메일 목록 */
	private List<WbBcCntcDVo> wbBcEmailDVo;
	
	/** 지정인 목록 */
	private List<WbBcApntrRVo> wbBcApntrRVoList;
	
	/** 명함이미지상세 */
	private WbBcImgDVo wbBcImgDVo;
	
	/** 명함이미지임시파일경로 */
	private String tempDir;
	
	/** 복사 대상 명함ID */
	private String toBcId;
	
	/** 대상UID(복사,이동) */ 
	private String copyRegrUid;
	
	/** 삭제ID 배열 */
	private String[] delList;
	
	/** 관련 미팅*/
	private List<WbBcMetngDVo> wbBcMetngDVo;
	
	/** 등록자명 */ 
	private String regrNm;
	
	/** 사본명함갯수 */ 
	private int duplicateBcCnt;
	
	/** 초기화여부 */ 
	private String resetYn;
	
	/** 기본연락처 */
	private String dftCntc;
	
	/** 추가 사용자UID - 조직도 추가 */
	private String addUserUid;
	
/** 검색조건 추가 */
	
	/** 폴더ID*/
	private String schFldId;
	
	/** 폴더검색 or 하위폴더포함여부*/
	private String schFldTypYn;
	
	/** 주요인사여부 */
	private String schIptfgYn;
	
	/** 초성검색정보 */
	private String schInitial;
	
	/** 전체공개여부 */
	private String allPublYn;
	
	/** 부서공개여부 */
	private String deptPublYn;
	
	/** 지정인공개여부 */
	private String apntPublYn;
	
	/** 공개 사용자 아이디 */
	private String schUserUid;
	
	/** 회사ID */
	private String schCompId;
	
	/** 부서아이디 */
	private String schDeptId;
	
	/** 검색 조건 공개여부 배열 */
	private String[] schOpenTypCds;
	
	/** 폴더 배열 */
	private List<WbBcFldBVo> wbBcFldBVo;
	
	/** 즐겨찾기여부 */ 
	private String schBumkYn;
	
	/** 원본명함ID */ 
	private String schOriginalBcId;
	
	/** 등록자UID */ 
	private String schRegrUid;
	
	/** 공용폴더 목록 */
	private List<WbPubFldBVo> wbPubFldBVoList;
	
	/** 공용명함여부 */ 
	private boolean isPub = false;
	
 	public String getSchOriginalBcId() {
		return schOriginalBcId;
	}
	public void setSchOriginalBcId(String schOriginalBcId) {
		this.schOriginalBcId = schOriginalBcId;
	}
	public int getDuplicateBcCnt() {
		return duplicateBcCnt;
	}
	public void setDuplicateBcCnt(int duplicateBcCnt) {
		this.duplicateBcCnt = duplicateBcCnt;
	}
	public String getDuplicateBcId() {
		return duplicateBcId;
	}
	public void setDuplicateBcId(String duplicateBcId) {
		this.duplicateBcId = duplicateBcId;
	}
	public void setBcId(String bcId) { 
		this.bcId = bcId;
	}
	/** 명함ID */ 
	public String getBcId() { 
		return bcId;
	}

	public void setPublTypCd(String publTypCd) { 
		this.publTypCd = publTypCd;
	}
	/** 공개구분코드 */ 
	public String getPublTypCd() { 
		return publTypCd;
	}

	public void setFldId(String fldId) { 
		this.fldId = fldId;
	}
	/** 폴더ID */ 
	public String getFldId() { 
		return fldId;
	}

	public void setBcNm(String bcNm) { 
		this.bcNm = bcNm;
	}
	/** 명함성명 */ 
	public String getBcNm() { 
		return bcNm;
	}

	public void setCompNm(String compNm) { 
		this.compNm = compNm;
	}
	/** 회사명 */ 
	public String getCompNm() { 
		return compNm;
	}

	public void setDeptNm(String deptNm) { 
		this.deptNm = deptNm;
	}
	/** 부서명 */ 
	public String getDeptNm() { 
		return deptNm;
	}

	public void setGradeNm(String gradeNm) { 
		this.gradeNm = gradeNm;
	}
	/** 직급명 */ 
	public String getGradeNm() { 
		return gradeNm;
	}

	public void setTichCont(String tichCont) { 
		this.tichCont = tichCont;
	}
	/** 담당업무내용 */ 
	public String getTichCont() { 
		return tichCont;
	}

	public void setIptfgYn(String iptfgYn) { 
		this.iptfgYn = iptfgYn;
	}
	/** 주요인사여부 */ 
	public String getIptfgYn() { 
		return iptfgYn;
	}

	public void setFno(String fno) { 
		this.fno = fno;
	}
	/** 팩스번호 */ 
	public String getFno() { 
		return fno;
	}

	public void setCompZipNo(String compZipNo) { 
		this.compZipNo = compZipNo;
	}
	/** 회사_우편번호 */ 
	public String getCompZipNo() { 
		return compZipNo;
	}

	public void setCompAdr(String compAdr) { 
		this.compAdr = compAdr;
	}
	/** 회사_주소 */ 
	public String getCompAdr() { 
		return compAdr;
	}

	public void setCompDetlAdr(String compDetlAdr) { 
		this.compDetlAdr = compDetlAdr;
	}
	/** 회사_상세주소 */ 
	public String getCompDetlAdr() { 
		return compDetlAdr;
	}

	public void setHomeZipNo(String homeZipNo) { 
		this.homeZipNo = homeZipNo;
	}
	/** 자택우편번호 */ 
	public String getHomeZipNo() { 
		return homeZipNo;
	}

	public void setHomeAdr(String homeAdr) { 
		this.homeAdr = homeAdr;
	}
	/** 자택주소 */ 
	public String getHomeAdr() { 
		return homeAdr;
	}

	public void setHomeDetlAdr(String homeDetlAdr) { 
		this.homeDetlAdr = homeDetlAdr;
	}
	/** 자택상세주소 */ 
	public String getHomeDetlAdr() { 
		return homeDetlAdr;
	}

	public void setBumkYn(String bumkYn) { 
		this.bumkYn = bumkYn;
	}
	/** 즐겨찾기여부 */ 
	public String getBumkYn() { 
		return bumkYn;
	}

	public void setPhotoFileId(String photoFileId) { 
		this.photoFileId = photoFileId;
	}
	/** 사진파일ID */ 
	public String getPhotoFileId() { 
		return photoFileId;
	}

	public void setGenCd(String genCd) { 
		this.genCd = genCd;
	}
	/** 성별코드 */ 
	public String getGenCd() { 
		return genCd;
	}

	public void setNatyCd(String natyCd) { 
		this.natyCd = natyCd;
	}
	/** 국적코드 */ 
	public String getNatyCd() { 
		return natyCd;
	}

	public void setBirth(String birth) { 
		this.birth = birth;
	}
	/** 생년월일 */ 
	public String getBirth() { 
		return birth;
	}

	public void setBirthSclcCd(String birthSclcCd) { 
		this.birthSclcCd = birthSclcCd;
	}
	/** 생년월일월력코드 */ 
	public String getBirthSclcCd() { 
		return birthSclcCd;
	}

	public void setWeddAnnv(String weddAnnv) { 
		this.weddAnnv = weddAnnv;
	}
	/** 결혼기념일 */ 
	public String getWeddAnnv() { 
		return weddAnnv;
	}

	public void setWeddAnnvSclcCd(String weddAnnvSclcCd) { 
		this.weddAnnvSclcCd = weddAnnvSclcCd;
	}
	/** 결혼기념일월력코드 */ 
	public String getWeddAnnvSclcCd() { 
		return weddAnnvSclcCd;
	}

	public void setPsnHpageUrl(String psnHpageUrl) { 
		this.psnHpageUrl = psnHpageUrl;
	}
	/** 개인홈페이지URL */ 
	public String getPsnHpageUrl() { 
		return psnHpageUrl;
	}

	public void setCompHpageUrl(String compHpageUrl) { 
		this.compHpageUrl = compHpageUrl;
	}
	/** 회사홈페이지URL */ 
	public String getCompHpageUrl() { 
		return compHpageUrl;
	}

	public void setHobyCont(String hobyCont) { 
		this.hobyCont = hobyCont;
	}
	/** 취미 */ 
	public String getHobyCont() { 
		return hobyCont;
	}

	public void setSpectCont(String spectCont) { 
		this.spectCont = spectCont;
	}
	/** 특기사항 */ 
	public String getSpectCont() { 
		return spectCont;
	}

	public void setEschlNm(String eschlNm) { 
		this.eschlNm = eschlNm;
	}
	/** 초등학교 */ 
	public String getEschlNm() { 
		return eschlNm;
	}

	public void setMschlNm(String mschlNm) { 
		this.mschlNm = mschlNm;
	}
	/** 중학교 */ 
	public String getMschlNm() { 
		return mschlNm;
	}

	public void setHschlNm(String hschlNm) { 
		this.hschlNm = hschlNm;
	}
	/** 고등학교 */ 
	public String getHschlNm() { 
		return hschlNm;
	}

	public void setUnivCont(String univCont) { 
		this.univCont = univCont;
	}
	/** 대학교내용 */ 
	public String getUnivCont() { 
		return univCont;
	}

	public void setBhistCont(String bhistCont) { 
		this.bhistCont = bhistCont;
	}
	/** 약력 */ 
	public String getBhistCont() { 
		return bhistCont;
	}
	/** 친밀도ID */ 
	public String getClnsId() {
		return clnsId;
	}
	/** 친밀도ID */
	public void setClnsId(String clnsId) {
		this.clnsId = clnsId;
	}
	public void setNoteCont(String noteCont) { 
		this.noteCont = noteCont;
	}
	/** 비고 */ 
	public String getNoteCont() { 
		return noteCont;
	}

	public void setAttFileId(String attFileId) { 
		this.attFileId = attFileId;
	}
	/** 첨부파일ID */ 
	public String getAttFileId() { 
		return attFileId;
	}

	public void setBcRegTypCd(String bcRegTypCd) { 
		this.bcRegTypCd = bcRegTypCd;
	}
	/** 명함등록구분코드 */ 
	public String getBcRegTypCd() { 
		return bcRegTypCd;
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

	public void setDelYn(String delYn) { 
		this.delYn = delYn;
	}
	/** 삭제여부 */ 
	public String getDelYn() { 
		return delYn;
	}
	
	public String getSchFldId() {
		return schFldId;
	}
	public void setSchFldId(String schFldId) {
		this.schFldId = schFldId;
	}
	public String getSchFldTypYn() {
		return schFldTypYn;
	}
	public void setSchFldTypYn(String schFldTypYn) {
		this.schFldTypYn = schFldTypYn;
	}
	public String getSchIptfgYn() {
		return schIptfgYn;
	}
	public void setSchIptfgYn(String schIptfgYn) {
		this.schIptfgYn = schIptfgYn;
	}
	public String getSchInitial() {
		return schInitial;
	}
	public void setSchInitial(String schInitial) {
		this.schInitial = schInitial;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getBcEnNm() {
		return bcEnNm;
	}
	public void setBcEnNm(String bcEnNm) {
		this.bcEnNm = bcEnNm;
	}
	
	public String getFldNm() {
		return fldNm;
	}
	public void setFldNm(String fldNm) {
		this.fldNm = fldNm;
	}
	
	public List<WbBcCntcDVo> getWbBcCntcDVo() {
		return wbBcCntcDVo;
	}
	public void setWbBcCntcDVo(List<WbBcCntcDVo> wbBcCntcDVo) {
		this.wbBcCntcDVo = wbBcCntcDVo;
	}
	
	public List<WbBcCntcDVo> getWbBcEmailDVo() {
		return wbBcEmailDVo;
	}
	public void setWbBcEmailDVo(List<WbBcCntcDVo> wbBcEmailDVo) {
		this.wbBcEmailDVo = wbBcEmailDVo;
	}
	
	public String getDftCntcTypCd() {
		return dftCntcTypCd;
	}
	public void setDftCntcTypCd(String dftCntcTypCd) {
		this.dftCntcTypCd = dftCntcTypCd;
	}
	
	public String getCompPhon() {
		return compPhon;
	}
	public void setCompPhon(String compPhon) {
		this.compPhon = compPhon;
	}

	public String getMbno() {
		return mbno;
	}
	public void setMbno(String mbno) {
		this.mbno = mbno;
	}
	
	public String getClnsNm() {
		return clnsNm;
	}
	public void setClnsNm(String clnsNm) {
		this.clnsNm = clnsNm;
	}
	
	public String getGenNm() {
		return genNm;
	}
	public void setGenNm(String genNm) {
		this.genNm = genNm;
	}
	
	public WbBcImgDVo getWbBcImgDVo() {
		return wbBcImgDVo;
	}
	public void setWbBcImgDVo(WbBcImgDVo wbBcImgDVo) {
		this.wbBcImgDVo = wbBcImgDVo;
	}
	
	public String getHomePhon() {
		return homePhon;
	}
	public void setHomePhon(String homePhon) {
		this.homePhon = homePhon;
	}
	
	public String getToBcId() {
		return toBcId;
	}
	public void setToBcId(String toBcId) {
		this.toBcId = toBcId;
	}
	
	public String getAllPublYn() {
		return allPublYn;
	}
	public void setAllPublYn(String allPublYn) {
		this.allPublYn = allPublYn;
	}
	public String getDeptPublYn() {
		return deptPublYn;
	}
	public void setDeptPublYn(String deptPublYn) {
		this.deptPublYn = deptPublYn;
	}
	public String getApntPublYn() {
		return apntPublYn;
	}
	public void setApntPublYn(String apntPublYn) {
		this.apntPublYn = apntPublYn;
	}
	public String[] getSchOpenTypCds() {
		return schOpenTypCds;
	}
	public void setSchOpenTypCds(String[] schOpenTypCds) {
		this.schOpenTypCds = schOpenTypCds;
	}
	
	public String getSchUserUid() {
		return schUserUid;
	}
	public void setSchUserUid(String schUserUid) {
		this.schUserUid = schUserUid;
	}
	public String getSchDeptId() {
		return schDeptId;
	}
	public void setSchDeptId(String schDeptId) {
		this.schDeptId = schDeptId;
	}
	
	public String getSchCompId() {
		return schCompId;
	}
	public void setSchCompId(String schCompId) {
		this.schCompId = schCompId;
	}
	
	public List<WbBcApntrRVo> getWbBcApntrRVoList() {
		return wbBcApntrRVoList;
	}
	public void setWbBcApntrRVoList(List<WbBcApntrRVo> wbBcApntrRVoList) {
		this.wbBcApntrRVoList = wbBcApntrRVoList;
	}
	
	public String[] getDelList() {
		return delList;
	}
	public void setDelList(String[] delList) {
		this.delList = delList;
	}
	
	public String getCopyRegrUid() {
		return copyRegrUid;
	}
	public void setCopyRegrUid(String copyRegrUid) {
		this.copyRegrUid = copyRegrUid;
	}
	public String getTempDir() {
		return tempDir;
	}
	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}
	
	public List<WbBcFldBVo> getWbBcFldBVo() {
		return wbBcFldBVo;
	}
	public void setWbBcFldBVo(List<WbBcFldBVo> wbBcFldBVo) {
		this.wbBcFldBVo = wbBcFldBVo;
	}
	
	/** 공용폴더 목록 */
	public List<WbPubFldBVo> getWbPubFldBVoList() {
		return wbPubFldBVoList;
	}
	public void setWbPubFldBVoList(List<WbPubFldBVo> wbPubFldBVoList) {
		this.wbPubFldBVoList = wbPubFldBVoList;
	}
	
	
	public String getSchBumkYn() {
		return schBumkYn;
	}
	public void setSchBumkYn(String schBumkYn) {
		this.schBumkYn = schBumkYn;
	}
	
	public String getCompId() {
		return compId;
	}
	public void setCompId(String compId) {
		this.compId = compId;
	}
	
	
	public List<WbBcMetngDVo> getWbBcMetngDVo() {
		return wbBcMetngDVo;
	}
	public void setWbBcMetngDVo(List<WbBcMetngDVo> wbBcMetngDVo) {
		this.wbBcMetngDVo = wbBcMetngDVo;
	}
	
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	public String getOriginalBcId() {
		return originalBcId;
	}
	public void setOriginalBcId(String originalBcId) {
		this.originalBcId = originalBcId;
	}
	
	public String getMainSetupYn() {
		return mainSetupYn;
	}
	public void setMainSetupYn(String mainSetupYn) {
		this.mainSetupYn = mainSetupYn;
	}
	
	public String getResetYn() {
		return resetYn;
	}
	public void setResetYn(String resetYn) {
		this.resetYn = resetYn;
	}
	public String getSchRegrUid() {
		return schRegrUid;
	}
	public void setSchRegrUid(String schRegrUid) {
		this.schRegrUid = schRegrUid;
	}
	public String getNatyNm() {
		return natyNm;
	}
	public void setNatyNm(String natyNm) {
		this.natyNm = natyNm;
	}
	
	public String getDftCntc() {
		return dftCntc;
	}
	public void setDftCntc(String dftCntc) {
		this.dftCntc = dftCntc;
	}
	
	/** 추가 사용자UID - 조직도 추가 */
	public String getAddUserUid() {
		return addUserUid;
	}
	public void setAddUserUid(String addUserUid) {
		this.addUserUid = addUserUid;
	}
	public String getInitialNm() {
		return initialNm;
	}
	public void setInitialNm(String initialNm) {
		this.initialNm = initialNm;
	}
	
	/** 공개폴더ID */
	public String getOpenFldId() {
		return openFldId;
	}
	public void setOpenFldId(String openFldId) {
		this.openFldId = openFldId;
	}
	
	/** 공용명함여부 */ 
	public boolean isPub() {
		return isPub;
	}
	public void setPub(boolean isPub) {
		this.isPub = isPub;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBDao.selectWbBcB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBDao.insertWbBcB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBDao.updateWbBcB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBDao.deleteWbBcB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wb.dao.WbBcBDao.countWbBcB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":명함기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab){
		if(bcId!=null) { if(tab!=null) builder.append(tab); builder.append("bcId(명함ID):").append(bcId).append('\n'); }
		if(publTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("publTypCd(공개구분코드):").append(publTypCd).append('\n'); }
		if(fldId!=null) { if(tab!=null) builder.append(tab); builder.append("fldId(폴더ID):").append(fldId).append('\n'); }
		if(fldNm!=null) { if(tab!=null) builder.append(tab); builder.append("fldNm(폴더명):").append(fldNm).append('\n'); }
		if(bcNm!=null) { if(tab!=null) builder.append(tab); builder.append("bcNm(명함성명):").append(bcNm).append('\n'); }
		if(bcEnNm!=null) { if(tab!=null) builder.append(tab); builder.append("bcEnNm(영문성명):").append(bcEnNm).append('\n'); }
		if(dftCntcTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("dftCntcTypCd(기본연락처구분코드):").append(dftCntcTypCd).append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(회사명):").append(compNm).append('\n'); }
		if(deptNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptNm(부서명):").append(deptNm).append('\n'); }
		if(gradeNm!=null) { if(tab!=null) builder.append(tab); builder.append("gradeNm(직급명):").append(gradeNm).append('\n'); }
		if(tichCont!=null) { if(tab!=null) builder.append(tab); builder.append("tichCont(담당업무내용):").append(tichCont).append('\n'); }
		if(iptfgYn!=null) { if(tab!=null) builder.append(tab); builder.append("iptfgYn(주요인사여부):").append(iptfgYn).append('\n'); }
		if(fno!=null) { if(tab!=null) builder.append(tab); builder.append("fno(팩스번호):").append(fno).append('\n'); }
		if(compZipNo!=null) { if(tab!=null) builder.append(tab); builder.append("compZipNo(회사_우편번호):").append(compZipNo).append('\n'); }
		if(compAdr!=null) { if(tab!=null) builder.append(tab); builder.append("compAdr(회사_주소):").append(compAdr).append('\n'); }
		if(compDetlAdr!=null) { if(tab!=null) builder.append(tab); builder.append("compDetlAdr(회사_상세주소):").append(compDetlAdr).append('\n'); }
		if(homeZipNo!=null) { if(tab!=null) builder.append(tab); builder.append("homeZipNo(자택우편번호):").append(homeZipNo).append('\n'); }
		if(homeAdr!=null) { if(tab!=null) builder.append(tab); builder.append("homeAdr(자택주소):").append(homeAdr).append('\n'); }
		if(homeDetlAdr!=null) { if(tab!=null) builder.append(tab); builder.append("homeDetlAdr(자택상세주소):").append(homeDetlAdr).append('\n'); }
		if(bumkYn!=null) { if(tab!=null) builder.append(tab); builder.append("bumkYn(즐겨찾기여부):").append(bumkYn).append('\n'); }
		if(photoFileId!=null) { if(tab!=null) builder.append(tab); builder.append("photoFileId(사진파일ID):").append(photoFileId).append('\n'); }
		if(genCd!=null) { if(tab!=null) builder.append(tab); builder.append("genCd(성별코드):").append(genCd).append('\n'); }
		if(natyCd!=null) { if(tab!=null) builder.append(tab); builder.append("natyCd(국적코드):").append(natyCd).append('\n'); }
		if(birth!=null) { if(tab!=null) builder.append(tab); builder.append("birth(생년월일):").append(birth).append('\n'); }
		if(birthSclcCd!=null) { if(tab!=null) builder.append(tab); builder.append("birthSclcCd(생년월일월력코드):").append(birthSclcCd).append('\n'); }
		if(weddAnnv!=null) { if(tab!=null) builder.append(tab); builder.append("weddAnnv(결혼기념일):").append(weddAnnv).append('\n'); }
		if(weddAnnvSclcCd!=null) { if(tab!=null) builder.append(tab); builder.append("weddAnnvSclcCd(결혼기념일월력코드):").append(weddAnnvSclcCd).append('\n'); }
		if(psnHpageUrl!=null) { if(tab!=null) builder.append(tab); builder.append("psnHpageUrl(개인홈페이지URL):").append(psnHpageUrl).append('\n'); }
		if(compHpageUrl!=null) { if(tab!=null) builder.append(tab); builder.append("compHpageUrl(회사홈페이지URL):").append(compHpageUrl).append('\n'); }
		if(hobyCont!=null) { if(tab!=null) builder.append(tab); builder.append("hobyCont(취미내용):").append(hobyCont).append('\n'); }
		if(spectCont!=null) { if(tab!=null) builder.append(tab); builder.append("spectCont(특기사항내용):").append(spectCont).append('\n'); }
		if(eschlNm!=null) { if(tab!=null) builder.append(tab); builder.append("eschlNm(초등학교명):").append(eschlNm).append('\n'); }
		if(mschlNm!=null) { if(tab!=null) builder.append(tab); builder.append("mschlNm(중학교명):").append(mschlNm).append('\n'); }
		if(hschlNm!=null) { if(tab!=null) builder.append(tab); builder.append("hschlNm(고등학교명):").append(hschlNm).append('\n'); }
		if(univCont!=null) { if(tab!=null) builder.append(tab); builder.append("univCont(대학교내용):").append(univCont).append('\n'); }
		if(bhistCont!=null) { if(tab!=null) builder.append(tab); builder.append("bhistCont(약력내용):").append(bhistCont).append('\n'); }
		if(clnsId!=null) { if(tab!=null) builder.append(tab); builder.append("clnsId(친밀도ID):").append(clnsId).append('\n'); }
		if(noteCont!=null) { if(tab!=null) builder.append(tab); builder.append("noteCont(비고):").append(noteCont).append('\n'); }
		if(attFileId!=null) { if(tab!=null) builder.append(tab); builder.append("attFileId(첨부파일ID):").append(attFileId).append('\n'); }
		if(bcRegTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("bcRegTypCd(명함등록구분코드):").append(bcRegTypCd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(delYn!=null) { if(tab!=null) builder.append(tab); builder.append("delYn(삭제여부):").append(delYn).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(originalBcId!=null) { if(tab!=null) builder.append(tab); builder.append("originalBcId(원본명함ID):").append(originalBcId).append('\n'); }
		if(mainSetupYn!=null) { if(tab!=null) builder.append(tab); builder.append("mainSetupYn(메인설정여부):").append(mainSetupYn).append('\n'); }
		if(duplicateBcId!=null) { if(tab!=null) builder.append(tab); builder.append("duplicateBcId(사본명함ID):").append(duplicateBcId).append('\n'); }
		if(initialNm!=null) { if(tab!=null) builder.append(tab); builder.append("initialNm(초성명):").append(initialNm).append('\n'); }
		if(compPhon!=null) { if(tab!=null) builder.append(tab); builder.append("compPhon(회사전화번호):").append(compPhon).append('\n'); }
		if(mbno!=null) { if(tab!=null) builder.append(tab); builder.append("mbno(이동전화번호):").append(mbno).append('\n'); }
		if(homePhon!=null) { if(tab!=null) builder.append(tab); builder.append("homePhon(자택전화번호):").append(homePhon).append('\n'); }
		if(email!=null) { if(tab!=null) builder.append(tab); builder.append("email(이메일):").append(email).append('\n'); }
		if(genNm!=null) { if(tab!=null) builder.append(tab); builder.append("genNm(성별이름):").append(genNm).append('\n'); }
		if(natyNm!=null) { if(tab!=null) builder.append(tab); builder.append("natyNm(국적명):").append(natyNm).append('\n'); }
		if(clnsNm!=null) { if(tab!=null) builder.append(tab); builder.append("clnsNm(친밀도명):").append(clnsNm).append('\n'); }
		if(wbBcCntcDVo!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcCntcDVo(연락처 목록):"); appendVoListTo(builder, wbBcCntcDVo,tab); builder.append('\n'); }
		if(wbBcEmailDVo!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcEmailDVo(이메일 목록):"); appendVoListTo(builder, wbBcEmailDVo,tab); builder.append('\n'); }
		if(wbBcApntrRVoList!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcApntrRVoList(지정인 목록):"); appendVoListTo(builder, wbBcApntrRVoList,tab); builder.append('\n'); }
		if(wbBcImgDVo!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcImgDVo(명함이미지상세):\n"); wbBcImgDVo.toString(builder, tab==null?"\t":tab+"\t"); builder.append('\n'); }
		if(tempDir!=null) { if(tab!=null) builder.append(tab); builder.append("tempDir(명함이미지임시파일경로):").append(tempDir).append('\n'); }
		if(toBcId!=null) { if(tab!=null) builder.append(tab); builder.append("toBcId(복사 대상 명함ID):").append(toBcId).append('\n'); }
		if(copyRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("copyRegrUid(대상UID(복사,이동)):").append(copyRegrUid).append('\n'); }
		if(delList!=null) { if(tab!=null) builder.append(tab); builder.append("delList(삭제ID 배열):"); appendArrayTo(builder, delList); builder.append('\n'); }
		if(wbBcMetngDVo!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcMetngDVo(관련 미팅):"); appendVoListTo(builder, wbBcMetngDVo,tab); builder.append('\n'); }
		if(regrNm!=null) { if(tab!=null) builder.append(tab); builder.append("regrNm(등록자명):").append(regrNm).append('\n'); }
		if(duplicateBcCnt!=0) { if(tab!=null) builder.append(tab); builder.append("duplicateBcCnt(사본명함갯수):").append(duplicateBcCnt).append('\n'); }
		if(resetYn!=null) { if(tab!=null) builder.append(tab); builder.append("resetYn(초기화여부):").append(resetYn).append('\n'); }
		if(dftCntc!=null) { if(tab!=null) builder.append(tab); builder.append("dftCntc(기본연락처):").append(dftCntc).append('\n'); }
		if(schFldId!=null) { if(tab!=null) builder.append(tab); builder.append("schFldId(폴더ID):").append(schFldId).append('\n'); }
		if(schFldTypYn!=null) { if(tab!=null) builder.append(tab); builder.append("schFldTypYn(폴더검색 or 하위폴더포함여부):").append(schFldTypYn).append('\n'); }
		if(schIptfgYn!=null) { if(tab!=null) builder.append(tab); builder.append("schIptfgYn(주요인사여부):").append(schIptfgYn).append('\n'); }
		if(schInitial!=null) { if(tab!=null) builder.append(tab); builder.append("schInitial(초성검색정보):").append(schInitial).append('\n'); }
		if(allPublYn!=null) { if(tab!=null) builder.append(tab); builder.append("allPublYn(전체공개여부):").append(allPublYn).append('\n'); }
		if(deptPublYn!=null) { if(tab!=null) builder.append(tab); builder.append("deptPublYn(부서공개여부):").append(deptPublYn).append('\n'); }
		if(apntPublYn!=null) { if(tab!=null) builder.append(tab); builder.append("apntPublYn(지정인공개여부):").append(apntPublYn).append('\n'); }
		if(schUserUid!=null) { if(tab!=null) builder.append(tab); builder.append("schUserUid(공개 사용자 아이디):").append(schUserUid).append('\n'); }
		if(schCompId!=null) { if(tab!=null) builder.append(tab); builder.append("schCompId(회사ID):").append(schCompId).append('\n'); }
		if(schDeptId!=null) { if(tab!=null) builder.append(tab); builder.append("schDeptId(부서아이디):").append(schDeptId).append('\n'); }
		if(schOpenTypCds!=null) { if(tab!=null) builder.append(tab); builder.append("schOpenTypCds(검색 조건 공개여부 배열):"); appendArrayTo(builder, schOpenTypCds); builder.append('\n'); }
		if(wbBcFldBVo!=null) { if(tab!=null) builder.append(tab); builder.append("wbBcFldBVo(폴더 배열):"); appendVoListTo(builder, wbBcFldBVo,tab); builder.append('\n'); }
		if(schBumkYn!=null) { if(tab!=null) builder.append(tab); builder.append("schBumkYn(즐겨찾기여부):").append(schBumkYn).append('\n'); }
		if(schOriginalBcId!=null) { if(tab!=null) builder.append(tab); builder.append("schOriginalBcId(원본명함ID):").append(schOriginalBcId).append('\n'); }
		if(schRegrUid!=null) { if(tab!=null) builder.append(tab); builder.append("schRegrUid(등록자UID):").append(schRegrUid).append('\n'); }
		super.toString(builder, tab);
	}
	
}