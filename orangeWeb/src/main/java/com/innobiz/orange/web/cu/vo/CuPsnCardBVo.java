package com.innobiz.orange.web.cu.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/09/26 15:16 ******/
/**
* 인사기록카드(BA_PSN_CARD_B) 테이블 VO 
*/
public class CuPsnCardBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1977742443056233480L;

	/** 카드번호 */ 
	private Integer cardNo;

 	/** 회사ID */ 
	private String compId;

 	/** 사번 */ 
	private String ein;

 	/** 소속법인 */ 
	private String compNm;

 	/** 부서명 */ 
	private String deptNm;

 	/** 입사일자 */ 
	private String joinDt;

 	/** 팀명 */ 
	private String teamNm;

 	/** 직위명 */ 
	private String positNm;

 	/** 한글명 */ 
	private String koNm;

 	/** 한문명 */ 
	private String znNm;

 	/** 영문명 */ 
	private String enNm;

 	/** 본문XML */ 
	private String bodyXml;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;

 	/** 수정자UID */ 
	private String modrUid;

 	/** 수정일시 */ 
	private String modDt;
	
	/** 주민등록번호 */ 
	private String ssn;

 	/** 생년월일 */ 
	private String birth;

 	/** 자택우편번호 */ 
	private String homeZipNo;

 	/** 자택주소 */ 
	private String homeAdr;

 	/** 자택상세주소 */ 
	private String homeDetlAdr;

 	/** 휴대전화번호 */ 
	private String mbno;
	
	/** 휴대전화번호 '-' 형태 */ 
	private String mbnos;
	
	/** 사번 목록 */ 
	private List<String> einList=null;
	
 	public void setCardNo(Integer cardNo) { 
		this.cardNo = cardNo;
	}
	/** 카드번호 */ 
	public Integer getCardNo() { 
		return cardNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setEin(String ein) { 
		this.ein = ein;
	}
	/** 사번 */ 
	public String getEin() { 
		return ein;
	}

	public void setCompNm(String compNm) { 
		this.compNm = compNm;
	}
	/** 소속법인 */ 
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

	public void setJoinDt(String joinDt) { 
		this.joinDt = joinDt;
	}
	/** 입사일자 */ 
	public String getJoinDt() { 
		return joinDt;
	}

	public void setTeamNm(String teamNm) { 
		this.teamNm = teamNm;
	}
	/** 팀명 */ 
	public String getTeamNm() { 
		return teamNm;
	}

	public void setPositNm(String positNm) { 
		this.positNm = positNm;
	}
	/** 직위명 */ 
	public String getPositNm() { 
		return positNm;
	}

	public void setKoNm(String koNm) { 
		this.koNm = koNm;
	}
	/** 한글명 */ 
	public String getKoNm() { 
		return koNm;
	}

	public void setZnNm(String znNm) { 
		this.znNm = znNm;
	}
	/** 한문명 */ 
	public String getZnNm() { 
		return znNm;
	}

	public void setEnNm(String enNm) { 
		this.enNm = enNm;
	}
	/** 영문명 */ 
	public String getEnNm() { 
		return enNm;
	}

	public void setBodyXml(String bodyXml) { 
		this.bodyXml = bodyXml;
	}
	/** 본문XML */ 
	public String getBodyXml() { 
		return bodyXml;
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

	public void setSsn(String ssn) { 
		this.ssn = ssn;
	}
	/** 주민등록번호 */ 
	public String getSsn() { 
		return ssn;
	}

	public void setBirth(String birth) { 
		this.birth = birth;
	}
	/** 생년월일 */ 
	public String getBirth() { 
		return birth;
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

	public void setMbno(String mbno) { 
		this.mbno = mbno;
	}
	/** 휴대전화번호 */ 
	public String getMbno() { 
		return mbno;
	}
	
	/** 휴대전화번호 '-' 형태 */ 
	public String getMbnos() {
		return mbnos;
	}
	public void setMbnos(String mbnos) {
		this.mbnos = mbnos;
	}
	/** 사번 목록 */ 
	public List<String> getEinList() {
		return einList;
	}
	public void setEinList(List<String> einList) {
		this.einList = einList;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.selectCuPsnCardB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.insertCuPsnCardB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.updateCuPsnCardB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.deleteCuPsnCardB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.countCuPsnCardB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":인사기록카드]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(cardNo!=null) { if(tab!=null) builder.append(tab); builder.append("cardNo(카드번호):").append(cardNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(ein!=null) { if(tab!=null) builder.append(tab); builder.append("ein(사번):").append(ein).append('\n'); }
		if(compNm!=null) { if(tab!=null) builder.append(tab); builder.append("compNm(소속법인):").append(compNm).append('\n'); }
		if(deptNm!=null) { if(tab!=null) builder.append(tab); builder.append("deptNm(부서명):").append(deptNm).append('\n'); }
		if(joinDt!=null) { if(tab!=null) builder.append(tab); builder.append("joinDt(입사일자):").append(joinDt).append('\n'); }
		if(teamNm!=null) { if(tab!=null) builder.append(tab); builder.append("teamNm(팀명):").append(teamNm).append('\n'); }
		if(positNm!=null) { if(tab!=null) builder.append(tab); builder.append("positNm(직위명):").append(positNm).append('\n'); }
		if(koNm!=null) { if(tab!=null) builder.append(tab); builder.append("koNm(한글명):").append(koNm).append('\n'); }
		if(znNm!=null) { if(tab!=null) builder.append(tab); builder.append("znNm(한문명):").append(znNm).append('\n'); }
		if(enNm!=null) { if(tab!=null) builder.append(tab); builder.append("enNm(영문명):").append(enNm).append('\n'); }
		if(bodyXml!=null) { if(tab!=null) builder.append(tab); builder.append("bodyXml(본문XML):").append(bodyXml).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		super.toString(builder, tab);
	}

}
