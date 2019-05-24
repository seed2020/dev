package com.innobiz.orange.web.cu.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/09/26 16:27 ******/
/**
* 인사정보상세(BA_PSN_INFO_D) 테이블 VO 
*/
public class CuPsnInfoDVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6818179106184065389L;

	/** 카드번호 */ 
	private Integer cardNo;

 	/** 주민등록번호암호값 */ 
	private String ssnEnc;

 	/** 생년월일암호값 */ 
	private String birthEnc;

 	/** 자택우편번호암호값 */ 
	private String homeZipNoEnc;

 	/** 자택주소암호값 */ 
	private String homeAdrEnc;

 	/** 자택상세주소암호값 */ 
	private String homeDetlAdrEnc;

 	/** 휴대전화번호암호값 */ 
	private String mbnoEnc;

 	public void setCardNo(Integer cardNo) { 
		this.cardNo = cardNo;
	}
	/** 카드번호 */ 
	public Integer getCardNo() { 
		return cardNo;
	}

	public void setSsnEnc(String ssnEnc) { 
		this.ssnEnc = ssnEnc;
	}
	/** 주민등록번호암호값 */ 
	public String getSsnEnc() { 
		return ssnEnc;
	}

	public void setBirthEnc(String birthEnc) { 
		this.birthEnc = birthEnc;
	}
	/** 생년월일암호값 */ 
	public String getBirthEnc() { 
		return birthEnc;
	}

	public void setHomeZipNoEnc(String homeZipNoEnc) { 
		this.homeZipNoEnc = homeZipNoEnc;
	}
	/** 자택우편번호암호값 */ 
	public String getHomeZipNoEnc() { 
		return homeZipNoEnc;
	}

	public void setHomeAdrEnc(String homeAdrEnc) { 
		this.homeAdrEnc = homeAdrEnc;
	}
	/** 자택주소암호값 */ 
	public String getHomeAdrEnc() { 
		return homeAdrEnc;
	}

	public void setHomeDetlAdrEnc(String homeDetlAdrEnc) { 
		this.homeDetlAdrEnc = homeDetlAdrEnc;
	}
	/** 자택상세주소암호값 */ 
	public String getHomeDetlAdrEnc() { 
		return homeDetlAdrEnc;
	}

	public void setMbnoEnc(String mbnoEnc) { 
		this.mbnoEnc = mbnoEnc;
	}
	/** 휴대전화번호암호값 */ 
	public String getMbnoEnc() { 
		return mbnoEnc;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.selectCuPsnInfoD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.insertCuPsnInfoD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.updateCuPsnInfoD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.deleteCuPsnInfoD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuPsnCardBDao.countCuPsnInfoD";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":인사정보상세]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(cardNo!=null) { if(tab!=null) builder.append(tab); builder.append("cardNo(카드번호):").append(cardNo).append('\n'); }
		if(ssnEnc!=null) { if(tab!=null) builder.append(tab); builder.append("ssnEnc(주민등록번호암호값):").append(ssnEnc).append('\n'); }
		if(birthEnc!=null) { if(tab!=null) builder.append(tab); builder.append("birthEnc(생년월일암호값):").append(birthEnc).append('\n'); }
		if(homeZipNoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeZipNoEnc(자택우편번호암호값):").append(homeZipNoEnc).append('\n'); }
		if(homeAdrEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeAdrEnc(자택주소암호값):").append(homeAdrEnc).append('\n'); }
		if(homeDetlAdrEnc!=null) { if(tab!=null) builder.append(tab); builder.append("homeDetlAdrEnc(자택상세주소암호값):").append(homeDetlAdrEnc).append('\n'); }
		if(mbnoEnc!=null) { if(tab!=null) builder.append(tab); builder.append("mbnoEnc(휴대전화번호암호값):").append(mbnoEnc).append('\n'); }
		super.toString(builder, tab);
	}

}
