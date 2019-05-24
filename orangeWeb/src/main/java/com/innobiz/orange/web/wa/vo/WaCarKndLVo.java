package com.innobiz.orange.web.wa.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2016/12/05 10:27 ******/
/**
* 차종목록(WA_CAR_KND_L) 테이블 VO 
*/
public class WaCarKndLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -8203882241057724014L;

	/** 차종번호 */ 
	private String carKndNo;

 	/** 회사ID */ 
	private String compId;

 	/** 법인번호 */ 
	private String corpNo;

 	/** 차종명 */ 
	private String carKndNm;

 	/** 자동차등록번호 */ 
	private String carRegNo;

 	/** 내용 */ 
	private String cont;
	
	/** 정렬순서 */ 
	private Integer sortOrdr;
	
	/** 이미지ID */ 
	private String tmpImgId;
	
	/** 이미지 */
	private WaCarImgDVo waCarImgDVo;
	
 	public void setCarKndNo(String carKndNo) { 
		this.carKndNo = carKndNo;
	}
	/** 차종번호 */ 
	public String getCarKndNo() { 
		return carKndNo;
	}

	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setCorpNo(String corpNo) { 
		this.corpNo = corpNo;
	}
	/** 법인번호 */ 
	public String getCorpNo() { 
		return corpNo;
	}

	public void setCarKndNm(String carKndNm) { 
		this.carKndNm = carKndNm;
	}
	/** 차종명 */ 
	public String getCarKndNm() { 
		return carKndNm;
	}

	public void setCarRegNo(String carRegNo) { 
		this.carRegNo = carRegNo;
	}
	/** 자동차등록번호 */ 
	public String getCarRegNo() { 
		return carRegNo;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}
	
	public void setSortOrdr(Integer sortOrdr) { 
		this.sortOrdr = sortOrdr;
	}
	/** 정렬순서 */ 
	public Integer getSortOrdr() { 
		return sortOrdr;
	}
	
	/** 이미지ID */ 
	public String getTmpImgId() {
		return tmpImgId;
	}
	public void setTmpImgId(String tmpImgId) {
		this.tmpImgId = tmpImgId;
	}
	
	/** 이미지 */
	public WaCarImgDVo getWaCarImgDVo() {
		return waCarImgDVo;
	}
	public void setWaCarImgDVo(WaCarImgDVo waCarImgDVo) {
		this.waCarImgDVo = waCarImgDVo;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCarKndLDao.selectWaCarKndL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCarKndLDao.insertWaCarKndL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCarKndLDao.updateWaCarKndL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCarKndLDao.deleteWaCarKndL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.wa.dao.WaCarKndLDao.countWaCarKndL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":차종목록]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(carKndNo!=null) { if(tab!=null) builder.append(tab); builder.append("carKndNo(차종번호):").append(carKndNo).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(corpNo!=null) { if(tab!=null) builder.append(tab); builder.append("corpNo(법인번호):").append(corpNo).append('\n'); }
		if(carKndNm!=null) { if(tab!=null) builder.append(tab); builder.append("carKndNm(차종명):").append(carKndNm).append('\n'); }
		if(carRegNo!=null) { if(tab!=null) builder.append(tab); builder.append("carRegNo(자동차등록번호):").append(carRegNo).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(sortOrdr!=null) { if(tab!=null) builder.append(tab); builder.append("sortOrdr(정렬순서):").append(sortOrdr).append('\n'); }
		super.toString(builder, tab);
	}

}
