package com.innobiz.orange.web.bb.vo;

import java.util.Map;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;

/**
 * 게시판관리(BA_BRD_B) 테이블 VO
 */
public class BaBrdBVo extends CommonVoImpl {

	/** serialVersionUID */
	private static final long serialVersionUID = -9195064846590736835L;

	/** 회사ID */
	private String compId;

	/** 게시판ID */
	private String brdId;

	/** 게시판명 */
	private String brdNm;

	/** 리소스ID */
	private String rescId;

	/** 설명 */
	private String brdDesc;

	/** 부서게시판여부 */
	private String deptBrdYn;

	/** 전사게시판여부 */
	private String allCompYn;

	/** 포토게시판여부 */
	private String photoYn;

	/** 테이블ID */
	private String tblId;

	/** 카테고리사용여부 */
	private String catYn;

	/** 카테고리그룹ID */
	private String catGrpId;

	/** 게시판타입코드 - N(일반), A(익명) */
	private String brdTypCd;

	/** 답변형여부 */
	private String replyYn;

	/** 심의여부 */
	private String discYn;

	/** 심의자UID */
	private String discrUid;

	/** 게시판종류코드 - P(영구), R(유효기간) */
	private String kndCd;

	/** 게시예약기간 */
	private Integer rezvPrd;

	/** 본문사이즈제한 */
	private Integer bodySizeLim;

	/** 첨부사이즈제한 */
	private Integer attSizeLim;

	/** 첨부용량제한 */
	private Integer attCapaLim;

	/** 신규게시물표시여부 */
	private String newDispYn;

	/** 신규게시물표시기간 */
	private Integer newDispPrd;

	/** 이전다음표시여부 */
	private String prevNextYn;

	/** 한줄답변사용여부 */
	private String cmtYn;

	/** 찬반투표사용여부 */
	private String favotYn;

	/** 담당자표시여부 */
	private String pichDispYn;

	/** 담당자UID */
	private String pichUid;

	/** 조회이력사용여부 */
	private String readHstUseYn;

	/** 공지표시기간 */
	private Integer notcDispPrd;

	/** 추천사용여부 */
	private String recmdUseYn;

	/** 점수사용여부 */
	private String screUseYn;

	/** 최근게시물여부 */
	private String lastBullYn;
	
	/** 포틀렛게시판여부 */
	private String pltYn;
	
	/** 옵션값 */
	private String optVa;

	/** 등록자 */
	private String regrUid;

	/** 등록일시 */
	private String regDt;

	/** 수정자 */
	private String modrUid;

	/** 수정일시 */
	private String modDt;

	// 추가 컬럼

	/** 리소스명 */
	private String rescNm;

	/** 테이블명 */
	private String tblNm;

	/** 테이블표시명 */
	private String tblDispNm;

	/** 확장여부 */
	private String exYn;

	/** 게시판타입참조코드 */
	private String brdTypRcd;

	/** 게시판타입명 */
	private String brdTypNm;

	/** 게시판종류참조코드 */
	private String kndRcd;

	/** 게시판종류명 */
	private String kndNm;

	/** 카테고리그룹명 */
	private String catGrpNm;

	/** 심의자명 */
	private String discrNm;

	/** 담당자VO */
	private OrUserBVo pichVo;

	/** 담당자 개인정보상세 */
	private OrUserPinfoDVo pichPinfoVo;
	
	/** 옵션MAP */
	private Map<String,Object> optMap;
	
	/** 검색조건(테이블ID) */
	private String schTblId;
	
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

	/** 게시판명 */
	public String getBrdNm() {
		return brdNm;
	}

	/** 게시판명 */
	public void setBrdNm(String brdNm) {
		this.brdNm = brdNm;
	}

	/** 리소스ID */
	public String getRescId() {
		return rescId;
	}

	/** 리소스ID */
	public void setRescId(String rescId) {
		this.rescId = rescId;
	}

	/** 설명 */
	public String getBrdDesc() {
		return brdDesc;
	}

	/** 설명 */
	public void setBrdDesc(String brdDesc) {
		this.brdDesc = brdDesc;
	}

	/** 부서게시판여부 */
	public String getDeptBrdYn() {
		return deptBrdYn;
	}

	/** 부서게시판여부 */
	public void setDeptBrdYn(String deptBrdYn) {
		this.deptBrdYn = deptBrdYn;
	}

	/** 전사게시판여부 */
	public String getAllCompYn() {
		return allCompYn;
	}

	/** 전사게시판여부 */
	public void setAllCompYn(String allCompYn) {
		this.allCompYn = allCompYn;
	}

	/** 포토게시판여부 */
	public String getPhotoYn() {
		return photoYn;
	}

	/** 포토게시판여부 */
	public void setPhotoYn(String photoYn) {
		this.photoYn = photoYn;
	}

	/** 테이블ID */
	public String getTblId() {
		return tblId;
	}

	/** 테이블ID */
	public void setTblId(String tblId) {
		this.tblId = tblId;
	}

	/** 카테고리사용여부 */
	public String getCatYn() {
		return catYn;
	}

	/** 카테고리사용여부 */
	public void setCatYn(String catYn) {
		this.catYn = catYn;
	}

	/** 카테고리그룹ID */
	public String getCatGrpId() {
		return catGrpId;
	}

	/** 카테고리그룹ID */
	public void setCatGrpId(String catGrpId) {
		this.catGrpId = catGrpId;
	}

	/** 게시판타입코드 - N(일반), A(익명) */
	public String getBrdTypCd() {
		return brdTypCd;
	}

	/** 게시판타입코드 - N(일반), A(익명) */
	public void setBrdTypCd(String brdTypCd) {
		this.brdTypCd = brdTypCd;
	}

	/** 답변형여부 */
	public String getReplyYn() {
		return replyYn;
	}

	/** 답변형여부 */
	public void setReplyYn(String replyYn) {
		this.replyYn = replyYn;
	}

	/** 심의여부 */
	public String getDiscYn() {
		return discYn;
	}

	/** 심의여부 */
	public void setDiscYn(String discYn) {
		this.discYn = discYn;
	}

	/** 심의자UID */
	public String getDiscrUid() {
		return discrUid;
	}

	/** 심의자UID */
	public void setDiscrUid(String discrUid) {
		this.discrUid = discrUid;
	}

	/** 게시판종류코드 - P(영구), R(유효기간) */
	public String getKndCd() {
		return kndCd;
	}

	/** 게시판종류코드 - P(영구), R(유효기간) */
	public void setKndCd(String kndCd) {
		this.kndCd = kndCd;
	}

	/** 게시예약기간 */
	public Integer getRezvPrd() {
		return rezvPrd;
	}

	/** 게시예약기간 */
	public void setRezvPrd(Integer rezvPrd) {
		this.rezvPrd = rezvPrd;
	}

	/** 본문사이즈제한 */
	public Integer getBodySizeLim() {
		return bodySizeLim;
	}

	/** 본문사이즈제한 */
	public void setBodySizeLim(Integer bodySizeLim) {
		this.bodySizeLim = bodySizeLim;
	}

	/** 첨부사이즈제한 */
	public Integer getAttSizeLim() {
		return attSizeLim;
	}

	/** 첨부사이즈제한 */
	public void setAttSizeLim(Integer attSizeLim) {
		this.attSizeLim = attSizeLim;
	}

	/** 첨부용량제한 */
	public Integer getAttCapaLim() {
		return attCapaLim;
	}

	/** 첨부용량제한 */
	public void setAttCapaLim(Integer attCapaLim) {
		this.attCapaLim = attCapaLim;
	}

	/** 신규게시물표시여부 */
	public String getNewDispYn() {
		return newDispYn;
	}

	/** 신규게시물표시여부 */
	public void setNewDispYn(String newDispYn) {
		this.newDispYn = newDispYn;
	}

	/** 신규게시물표시기간 */
	public Integer getNewDispPrd() {
		return newDispPrd;
	}

	/** 신규게시물표시기간 */
	public void setNewDispPrd(Integer newDispPrd) {
		this.newDispPrd = newDispPrd;
	}

	/** 이전다음표시여부 */
	public String getPrevNextYn() {
		return prevNextYn;
	}

	/** 이전다음표시여부 */
	public void setPrevNextYn(String prevNextYn) {
		this.prevNextYn = prevNextYn;
	}

	/** 한줄답변사용여부 */
	public String getCmtYn() {
		return cmtYn;
	}

	/** 한줄답변사용여부 */
	public void setCmtYn(String cmtYn) {
		this.cmtYn = cmtYn;
	}

	/** 찬반투표사용여부 */
	public String getFavotYn() {
		return favotYn;
	}

	/** 찬반투표사용여부 */
	public void setFavotYn(String favotYn) {
		this.favotYn = favotYn;
	}

	/** 담당자표시여부 */
	public String getPichDispYn() {
		return pichDispYn;
	}

	/** 담당자표시여부 */
	public void setPichDispYn(String pichDispYn) {
		this.pichDispYn = pichDispYn;
	}

	/** 담당자UID */
	public String getPichUid() {
		return pichUid;
	}

	/** 담당자UID */
	public void setPichUid(String pichUid) {
		this.pichUid = pichUid;
	}

	/** 조회이력사용여부 */
	public String getReadHstUseYn() {
		return readHstUseYn;
	}

	/** 조회이력사용여부 */
	public void setReadHstUseYn(String readHstUseYn) {
		this.readHstUseYn = readHstUseYn;
	}

	/** 공지표시기간 */
	public Integer getNotcDispPrd() {
		return notcDispPrd;
	}

	/** 공지표시기간 */
	public void setNotcDispPrd(Integer notcDispPrd) {
		this.notcDispPrd = notcDispPrd;
	}

	/** 추천사용여부 */
	public String getRecmdUseYn() {
		return recmdUseYn;
	}

	/** 추천사용여부 */
	public void setRecmdUseYn(String recmdUseYn) {
		this.recmdUseYn = recmdUseYn;
	}

	/** 점수사용여부 */
	public String getScreUseYn() {
		return screUseYn;
	}

	/** 점수사용여부 */
	public void setScreUseYn(String screUseYn) {
		this.screUseYn = screUseYn;
	}

	/** 최근게시물여부 */
	public String getLastBullYn() {
		return lastBullYn;
	}

	/** 최근게시물여부 */
	public void setLastBullYn(String lastBullYn) {
		this.lastBullYn = lastBullYn;
	}
	
	/** 포틀렛게시판여부 */
	public String getPltYn() {
		return pltYn;
	}

	/** 포틀렛게시판여부 */
	public void setPltYn(String pltYn) {
		this.pltYn = pltYn;
	}
	
	/** 옵션값 */
	public String getOptVa() {
		return optVa;
	}

	public void setOptVa(String optVa) {
		this.optVa = optVa;
	}

	/** 등록자 */
	public String getRegrUid() {
		return regrUid;
	}

	/** 등록자 */
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

	/** 리소스명 */
	public String getRescNm() {
		return rescNm;
	}

	/** 리소스명 */
	public void setRescNm(String rescNm) {
		this.rescNm = rescNm;
	}

	/** 테이블명 */
	public String getTblNm() {
		return tblNm;
	}

	/** 테이블명 */
	public void setTblNm(String tblNm) {
		this.tblNm = tblNm;
	}

	/** 테이블표시명 */
	public String getTblDispNm() {
		return tblDispNm;
	}

	/** 테이블표시명 */
	public void setTblDispNm(String tblDispNm) {
		this.tblDispNm = tblDispNm;
	}

	/** 확장여부 */
	public String getExYn() {
		return exYn;
	}

	/** 확장여부 */
	public void setExYn(String exYn) {
		this.exYn = exYn;
	}

	/** 게시판타입참조코드 */
	public String getBrdTypRcd() {
		return brdTypRcd;
	}

	/** 게시판타입참조코드 */
	public void setBrdTypRcd(String brdTypRcd) {
		this.brdTypRcd = brdTypRcd;
	}

	/** 게시판타입명 */
	public String getBrdTypNm() {
		return brdTypNm;
	}

	/** 게시판타입명 */
	public void setBrdTypNm(String brdTypNm) {
		this.brdTypNm = brdTypNm;
	}

	/** 게시판종류참조코드 */
	public String getKndRcd() {
		return kndRcd;
	}

	/** 게시판종류참조코드 */
	public void setKndRcd(String kndRcd) {
		this.kndRcd = kndRcd;
	}

	/** 게시판종류명 */
	public String getKndNm() {
		return kndNm;
	}

	/** 게시판종류명 */
	public void setKndNm(String kndNm) {
		this.kndNm = kndNm;
	}

	/** 카테고리그룹명 */
	public String getCatGrpNm() {
		return catGrpNm;
	}

	/** 카테고리그룹명 */
	public void setCatGrpNm(String catGrpNm) {
		this.catGrpNm = catGrpNm;
	}

	/** 심의자명 */
	public String getDiscrNm() {
		return discrNm;
	}

	/** 심의자명 */
	public void setDiscrNm(String discrNm) {
		this.discrNm = discrNm;
	}

	/** 담당자VO */
	public OrUserBVo getPichVo() {
		return pichVo;
	}

	/** 담당자VO */
	public void setPichVo(OrUserBVo pichVo) {
		this.pichVo = pichVo;
	}

	/** 담당자 개인정보상세 */
	public OrUserPinfoDVo getPichPinfoVo() {
		return pichPinfoVo;
	}

	/** 담당자 개인정보상세 */
	public void setPichPinfoVo(OrUserPinfoDVo pichPinfoVo) {
		this.pichPinfoVo = pichPinfoVo;
	}

	/** 검색조건(테이블ID) */
	public String getSchTblId() {
		return schTblId;
	}

	/** 검색조건(테이블ID) */
	public void setSchTblId(String schTblId) {
		this.schTblId = schTblId;
	}
	
	/** 옵션MAP */
	public Map<String, Object> getOptMap() {
		return optMap;
	}

	public void setOptMap(Map<String, Object> optMap) {
		this.optMap = optMap;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if (getInstanceQueryId() != null) return getInstanceQueryId();
		if (QueryType.SELECT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBrdBDao.selectBaBrdB";
		} else if (QueryType.INSERT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBrdBDao.insertBaBrdB";
		} else if (QueryType.UPDATE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBrdBDao.updateBaBrdB";
		} else if (QueryType.DELETE == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBrdBDao.deleteBaBrdB";
		} else if (QueryType.COUNT == queryType) {
			return "com.innobiz.orange.web.bb.dao.BaBrdBDao.countBaBrdB";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString() {
		StringBuilder sb = new StringBuilder(512);
		sb.append('[').append(this.getClass().getName()).append(":게시판관리]\n");
		toString(sb, null);
		return sb.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder sb, String tab) {
		if (compId != null) { if (tab != null) sb.append(tab); sb.append("compId(회사ID):").append(compId).append('\n'); }
		if (brdId != null) { if (tab != null) sb.append(tab); sb.append("brdId(게시판ID):").append(brdId).append('\n'); }
		if (brdNm != null) { if (tab != null) sb.append(tab); sb.append("brdNm(게시판명):").append(brdNm).append('\n'); }
		if (rescId != null) { if (tab != null) sb.append(tab); sb.append("rescId(리소스ID):").append(rescId).append('\n'); }
		if (brdDesc != null) { if (tab != null) sb.append(tab); sb.append("brdDesc(설명):").append(brdDesc).append('\n'); }
		if (deptBrdYn != null) { if (tab != null) sb.append(tab); sb.append("deptBrdYn(부서게시판여부):").append(deptBrdYn).append('\n'); }
		if (allCompYn != null) { if (tab != null) sb.append(tab); sb.append("allCompYn(전사게시판여부):").append(allCompYn).append('\n'); }
		if (photoYn != null) { if (tab != null) sb.append(tab); sb.append("photoYn(포토게시판여부):").append(photoYn).append('\n'); }
		if (tblId != null) { if (tab != null) sb.append(tab); sb.append("tblId(테이블ID):").append(tblId).append('\n'); }
		if (catYn != null) { if (tab != null) sb.append(tab); sb.append("catYn(카테고리사용여부):").append(catYn).append('\n'); }
		if (catGrpId != null) { if (tab != null) sb.append(tab); sb.append("catGrpId(카테고리그룹ID):").append(catGrpId).append('\n'); }
		if (brdTypCd != null) { if (tab != null) sb.append(tab); sb.append("brdTypCd(게시판타입코드):").append(brdTypCd).append('\n'); }
		if (replyYn != null) { if (tab != null) sb.append(tab); sb.append("replyYn(답변형여부):").append(replyYn).append('\n'); }
		if (discYn != null) { if (tab != null) sb.append(tab); sb.append("discYn(심의여부):").append(discYn).append('\n'); }
		if (discrUid != null) { if (tab != null) sb.append(tab); sb.append("discrUid(심의자UID):").append(discrUid).append('\n'); }
		if (kndCd != null) { if (tab != null) sb.append(tab); sb.append("kndCd(게시판종류코드):").append(kndCd).append('\n'); }
		if (rezvPrd != null) { if (tab != null) sb.append(tab); sb.append("rezvPrd(게시예약기간):").append(rezvPrd).append('\n'); }
		if (bodySizeLim != null) { if (tab != null) sb.append(tab); sb.append("bodySizeLim(본문사이즈제한):").append(bodySizeLim).append('\n'); }
		if (attSizeLim != null) { if (tab != null) sb.append(tab); sb.append("attSizeLim(첨부사이즈제한):").append(attSizeLim).append('\n'); }
		if (attCapaLim != null) { if (tab != null) sb.append(tab); sb.append("attCapaLim(첨부용량제한):").append(attCapaLim).append('\n'); }
		if (newDispYn != null) { if (tab != null) sb.append(tab); sb.append("newDispYn(신규게시물표시여부):").append(newDispYn).append('\n'); }
		if (newDispPrd != null) { if (tab != null) sb.append(tab); sb.append("newDispPrd(신규게시물표시기간):").append(newDispPrd).append('\n'); }
		if (prevNextYn != null) { if (tab != null) sb.append(tab); sb.append("prevNextYn(이전다음표시여부):").append(prevNextYn).append('\n'); }
		if (cmtYn != null) { if (tab != null) sb.append(tab); sb.append("cmtYn(한줄답변사용여부):").append(cmtYn).append('\n'); }
		if (favotYn != null) { if (tab != null) sb.append(tab); sb.append("favotYn(찬반투표사용여부):").append(favotYn).append('\n'); }
		if (pichDispYn != null) { if (tab != null) sb.append(tab); sb.append("pichDispYn(담당자표시여부):").append(pichDispYn).append('\n'); }
		if (pichUid != null) { if (tab != null) sb.append(tab); sb.append("pichUid(담당자UID):").append(pichUid).append('\n'); }
		if (readHstUseYn != null) { if (tab != null) sb.append(tab); sb.append("readHstUseYn(조회이력사용여부):").append(readHstUseYn).append('\n'); }
		if (notcDispPrd != null) { if (tab != null) sb.append(tab); sb.append("notcDispPrd(공지표시기간):").append(notcDispPrd).append('\n'); }
		if (recmdUseYn != null) { if (tab != null) sb.append(tab); sb.append("recmdUseYn(추천사용여부):").append(recmdUseYn).append('\n'); }
		if (screUseYn != null) { if (tab != null) sb.append(tab); sb.append("screUseYn(점수사용여부):").append(screUseYn).append('\n'); }
		if (lastBullYn != null) { if (tab != null) sb.append(tab); sb.append("lastBullYn(최근게시물여부):").append(lastBullYn).append('\n'); }
		if (optVa != null) { if (tab != null) sb.append(tab); sb.append("optVa(옵션값):").append(optVa).append('\n'); }
		if (regrUid != null) { if (tab != null) sb.append(tab); sb.append("regrUid(등록자):").append(regrUid).append('\n'); }
		if (regDt != null) { if (tab != null) sb.append(tab); sb.append("regDt(등록일시):").append(regDt).append('\n'); }
		if (modrUid != null) { if (tab != null) sb.append(tab); sb.append("modrUid(수정자):").append(modrUid).append('\n'); }
		if (modDt != null) { if (tab != null) sb.append(tab); sb.append("modDt(수정일시):").append(modDt).append('\n'); }
		if (rescNm != null) { if (tab != null) sb.append(tab); sb.append("rescNm(리소스명):").append(rescNm).append('\n'); }
		if (tblNm != null) { if (tab != null) sb.append(tab); sb.append("tblNm(테이블명):").append(tblNm).append('\n'); }
		if (tblDispNm != null) { if (tab != null) sb.append(tab); sb.append("tblDispNm(테이블표시명):").append(tblDispNm).append('\n'); }
		if (exYn != null) { if (tab != null) sb.append(tab); sb.append("exYn(확장여부):").append(exYn).append('\n'); }
		if (kndNm != null) { if (tab != null) sb.append(tab); sb.append("kndNm(게시판종류명):").append(kndNm).append('\n'); }
		if (catGrpNm != null) { if (tab != null) sb.append(tab); sb.append("catGrpNm(카테고리그룹명):").append(catGrpNm).append('\n'); }
		if (discrNm != null) { if (tab != null) sb.append(tab); sb.append("discrNm(심의자명):").append(discrNm).append('\n'); }
		if (pichVo != null) { if (tab != null) sb.append(tab); sb.append("pichVo(담당자VO):").append(pichVo).append('\n'); }
		if (pichPinfoVo != null) { if (tab != null) sb.append(tab); sb.append("pichPinfoVo(담당자 개인정보상세):").append(pichPinfoVo).append('\n'); }
		if (schTblId != null) { if (tab != null) sb.append(tab); sb.append("schTblId(검색조건(테이블ID)):").append(schTblId).append('\n'); }
		super.toString(sb, tab);
	}
}