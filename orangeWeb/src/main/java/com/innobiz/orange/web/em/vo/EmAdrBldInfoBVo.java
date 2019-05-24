package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 대표지번전체(PT_ZIPCD_B_TEMP) 테이블 VO
 */
@SuppressWarnings("serial")
public class EmAdrBldInfoBVo extends CommonVoImpl {
	/** 법정동코드 */ 
	private String lowTownCd;

 	/** 시도명 */ 
	private String sido;

 	/** 시군구명 */ 
	private String gugun;

 	/** 법정읍면동명 */ 
	private String dong;

 	/** 법정리명 */ 
	private String ri;

 	/** 산여부 */ 
	private String mtYn;

 	/** 지번본번(번지) */ 
	private String mainLotNo;

 	/** 지번부번(호) */ 
	private String subLotNo;

 	/** 도로명코드 */ 
	private String roadNmCd;

 	/** 도로명 */ 
	private String roadNm;

 	/** 지하여부 */ 
	private String basementYn;

 	/** 건물본번 */ 
	private String mainBldNo;

 	/** 건물부번 */ 
	private String subBldNo;

 	/** 건축물대장 건물명 */ 
	private String bldNm;

 	/** 상세건물명 */ 
	private String bldDetlNm;

 	/** 건물관리번호 */ 
	private String bldMngNo;

 	/** 읍면동일련번호 */ 
	private String dongSeq;

 	/** 행정동코드 */ 
	private String govTownCd;

 	/** 행정동명 */ 
	private String govTownNm;

 	/** 우편번호 */ 
	private String zipNo;

 	/** 우편일련번호 */ 
	private String zipSeq;

 	/** 다량배달처명 */ 
	private String bulkDeliveryNm;

 	/** 이동사유코드 */ 
	private String moveReasonCd;

 	/** 고시일자 */ 
	private String noticeYmd;

 	/** 변경전도로명주소 */ 
	private String beforeRoadAdr;

 	/** 시군구용 건물명 */ 
	private String cityPublicBldNm;

 	/** 공동주택여부 */ 
	private String aptHourseYn;

 	/** 기초구역번호 */ 
	private String dftZoneNo;

 	/** 상세주소부여여부 */ 
	private String detlAdrYn;

 	/** 비고1 */ 
	private String note1;

 	/** 비고2 */ 
	private String note2;
	
	/** 검색 조건 추가 */
	
	/** 시도 */	
	private String schSido;
	
	/** 시군구 */	
	private String schGugun;
	
	public String getSchSido() {
		return schSido;
	}
	public void setSchSido(String schSido) {
		this.schSido = schSido;
	}
	public String getSchGugun() {
		return schGugun;
	}
	public void setSchGugun(String schGugun) {
		this.schGugun = schGugun;
	}
	public void setLowTownCd(String lowTownCd) { 
		this.lowTownCd = lowTownCd;
	}
	/** 법정동코드 */ 
	public String getLowTownCd() { 
		return lowTownCd;
	}

	public void setSido(String sido) { 
		this.sido = sido;
	}
	/** 시도명 */ 
	public String getSido() { 
		return sido;
	}

	public void setGugun(String gugun) { 
		this.gugun = gugun;
	}
	/** 시군구명 */ 
	public String getGugun() { 
		return gugun;
	}

	public void setDong(String dong) { 
		this.dong = dong;
	}
	/** 법정읍면동명 */ 
	public String getDong() { 
		return dong;
	}

	public void setRi(String ri) { 
		this.ri = ri;
	}
	/** 법정리명 */ 
	public String getRi() { 
		return ri;
	}

	public void setMtYn(String mtYn) { 
		this.mtYn = mtYn;
	}
	/** 산여부 */ 
	public String getMtYn() { 
		return mtYn;
	}

	public void setMainLotNo(String mainLotNo) { 
		this.mainLotNo = mainLotNo;
	}
	/** 지번본번(번지) */ 
	public String getMainLotNo() { 
		return mainLotNo;
	}

	public void setSubLotNo(String subLotNo) { 
		this.subLotNo = subLotNo;
	}
	/** 지번부번(호) */ 
	public String getSubLotNo() { 
		return subLotNo;
	}

	public void setRoadNmCd(String roadNmCd) { 
		this.roadNmCd = roadNmCd;
	}
	/** 도로명코드 */ 
	public String getRoadNmCd() { 
		return roadNmCd;
	}

	public void setRoadNm(String roadNm) { 
		this.roadNm = roadNm;
	}
	/** 도로명 */ 
	public String getRoadNm() { 
		return roadNm;
	}

	public void setBasementYn(String basementYn) { 
		this.basementYn = basementYn;
	}
	/** 지하여부 */ 
	public String getBasementYn() { 
		return basementYn;
	}

	public void setMainBldNo(String mainBldNo) { 
		this.mainBldNo = mainBldNo;
	}
	/** 건물본번 */ 
	public String getMainBldNo() { 
		return mainBldNo;
	}

	public void setSubBldNo(String subBldNo) { 
		this.subBldNo = subBldNo;
	}
	/** 건물부번 */ 
	public String getSubBldNo() { 
		return subBldNo;
	}

	public void setBldNm(String bldNm) { 
		this.bldNm = bldNm;
	}
	/** 건축물대장 건물명 */ 
	public String getBldNm() { 
		return bldNm;
	}

	public void setBldDetlNm(String bldDetlNm) { 
		this.bldDetlNm = bldDetlNm;
	}
	/** 상세건물명 */ 
	public String getBldDetlNm() { 
		return bldDetlNm;
	}

	public void setBldMngNo(String bldMngNo) { 
		this.bldMngNo = bldMngNo;
	}
	/** 건물관리번호 */ 
	public String getBldMngNo() { 
		return bldMngNo;
	}

	public void setDongSeq(String dongSeq) { 
		this.dongSeq = dongSeq;
	}
	/** 읍면동일련번호 */ 
	public String getDongSeq() { 
		return dongSeq;
	}

	public void setGovTownCd(String govTownCd) { 
		this.govTownCd = govTownCd;
	}
	/** 행정동코드 */ 
	public String getGovTownCd() { 
		return govTownCd;
	}

	public void setGovTownNm(String govTownNm) { 
		this.govTownNm = govTownNm;
	}
	/** 행정동명 */ 
	public String getGovTownNm() { 
		return govTownNm;
	}

	public void setZipNo(String zipNo) { 
		this.zipNo = zipNo;
	}
	/** 우편번호 */ 
	public String getZipNo() { 
		return zipNo;
	}

	public void setZipSeq(String zipSeq) { 
		this.zipSeq = zipSeq;
	}
	/** 우편일련번호 */ 
	public String getZipSeq() { 
		return zipSeq;
	}

	public void setBulkDeliveryNm(String bulkDeliveryNm) { 
		this.bulkDeliveryNm = bulkDeliveryNm;
	}
	/** 다량배달처명 */ 
	public String getBulkDeliveryNm() { 
		return bulkDeliveryNm;
	}

	public void setMoveReasonCd(String moveReasonCd) { 
		this.moveReasonCd = moveReasonCd;
	}
	/** 이동사유코드 */ 
	public String getMoveReasonCd() { 
		return moveReasonCd;
	}

	public void setNoticeYmd(String noticeYmd) { 
		this.noticeYmd = noticeYmd;
	}
	/** 고시일자 */ 
	public String getNoticeYmd() { 
		return noticeYmd;
	}

	public void setBeforeRoadAdr(String beforeRoadAdr) { 
		this.beforeRoadAdr = beforeRoadAdr;
	}
	/** 변경전도로명주소 */ 
	public String getBeforeRoadAdr() { 
		return beforeRoadAdr;
	}

	public void setCityPublicBldNm(String cityPublicBldNm) { 
		this.cityPublicBldNm = cityPublicBldNm;
	}
	/** 시군구용 건물명 */ 
	public String getCityPublicBldNm() { 
		return cityPublicBldNm;
	}

	public void setAptHourseYn(String aptHourseYn) { 
		this.aptHourseYn = aptHourseYn;
	}
	/** 공동주택여부 */ 
	public String getAptHourseYn() { 
		return aptHourseYn;
	}

	public void setDftZoneNo(String dftZoneNo) { 
		this.dftZoneNo = dftZoneNo;
	}
	/** 기초구역번호 */ 
	public String getDftZoneNo() { 
		return dftZoneNo;
	}

	public void setDetlAdrYn(String detlAdrYn) { 
		this.detlAdrYn = detlAdrYn;
	}
	/** 상세주소부여여부 */ 
	public String getDetlAdrYn() { 
		return detlAdrYn;
	}

	public void setNote1(String note1) { 
		this.note1 = note1;
	}
	/** 비고1 */ 
	public String getNote1() { 
		return note1;
	}

	public void setNote2(String note2) { 
		this.note2 = note2;
	}
	/** 비고2 */ 
	public String getNote2() { 
		return note2;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.selectEmAdrBldInfoB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.insertEmAdrBldInfoB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.updateEmAdrBldInfoB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.deleteEmAdrBldInfoB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.countEmAdrBldInfoB";
		}
		return null;
	}

}
