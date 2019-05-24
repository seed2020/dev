package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/14 11:26 ******/
/**
* 메일주소록이관(EM_MAIL_ADR_TRAN_B) 테이블 VO 
*/
public class EmMailAdrTranBVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = -2201276034627262059L;

	/** 이관ID */ 
	private String tranId;
	
	/** 사용자수 */ 
	private Integer userCnt;

 	/** 폴더건수 */ 
	private Integer fldCnt;

 	/** 주소록건수 */ 
	private Integer adrCnt;
	
 	/** 이관시작일시 */ 
	private String tranStrtDt;

 	/** 이관종료일시 */ 
	private String tranEndDt;

 	/** 이관진행상태코드 */ 
	private String tranProcStatCd; // preparing:준비중, processing:진행중, completed:완료, error:에러 

 	/** 이관내용 */ 
	private String tranCont;

 	/** 오류내용 */ 
	private String errCont;

 	/** 오류여부 */ 
	private String errYn;

 	/** 사용자UID */ 
	private String userUid;

 	public void setTranId(String tranId) { 
		this.tranId = tranId;
	}
	/** 이관ID */ 
	public String getTranId() { 
		return tranId;
	}
	
	public void setUserCnt(Integer userCnt) { 
		this.userCnt = userCnt;
	}
	/** 사용자수 */ 
	public Integer getUserCnt() { 
		return userCnt;
	}

	public void setFldCnt(Integer fldCnt) { 
		this.fldCnt = fldCnt;
	}
	/** 폴더건수 */ 
	public Integer getFldCnt() { 
		return fldCnt;
	}

	public void setAdrCnt(Integer adrCnt) { 
		this.adrCnt = adrCnt;
	}
	/** 주소록건수 */ 
	public Integer getAdrCnt() { 
		return adrCnt;
	}
	
	public void setTranStrtDt(String tranStrtDt) { 
		this.tranStrtDt = tranStrtDt;
	}
	/** 이관시작일시 */ 
	public String getTranStrtDt() { 
		return tranStrtDt;
	}

	public void setTranEndDt(String tranEndDt) { 
		this.tranEndDt = tranEndDt;
	}
	/** 이관종료일시 */ 
	public String getTranEndDt() { 
		return tranEndDt;
	}

	public void setTranProcStatCd(String tranProcStatCd) { 
		this.tranProcStatCd = tranProcStatCd;
	}
	/** 이관진행상태코드 */ 
	public String getTranProcStatCd() { 
		return tranProcStatCd;
	}

	public void setTranCont(String tranCont) { 
		this.tranCont = tranCont;
	}
	/** 이관내용 */ 
	public String getTranCont() { 
		return tranCont;
	}

	public void setErrCont(String errCont) { 
		this.errCont = errCont;
	}
	/** 오류내용 */ 
	public String getErrCont() { 
		return errCont;
	}

	public void setErrYn(String errYn) { 
		this.errYn = errYn;
	}
	/** 오류여부 */ 
	public String getErrYn() { 
		return errYn;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranBDao.selectEmMailAdrTranB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranBDao.insertEmMailAdrTranB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranBDao.updateEmMailAdrTranB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranBDao.deleteEmMailAdrTranB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranBDao.countEmMailAdrTranB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":메일주소록이관]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID):").append(tranId).append('\n'); }
		if(userCnt!=null) { if(tab!=null) builder.append(tab); builder.append("userCnt(사용자수):").append(userCnt).append('\n'); }
		if(fldCnt!=null) { if(tab!=null) builder.append(tab); builder.append("fldCnt(폴더건수):").append(fldCnt).append('\n'); }
		if(adrCnt!=null) { if(tab!=null) builder.append(tab); builder.append("adrCnt(주소록건수):").append(adrCnt).append('\n'); }		
		if(tranStrtDt!=null) { if(tab!=null) builder.append(tab); builder.append("tranStrtDt(이관시작일시):").append(tranStrtDt).append('\n'); }
		if(tranEndDt!=null) { if(tab!=null) builder.append(tab); builder.append("tranEndDt(이관종료일시):").append(tranEndDt).append('\n'); }
		if(tranProcStatCd!=null) { if(tab!=null) builder.append(tab); builder.append("tranProcStatCd(이관진행상태코드):").append(tranProcStatCd).append('\n'); }
		if(tranCont!=null) { if(tab!=null) builder.append(tab); builder.append("tranCont(이관내용):").append(tranCont).append('\n'); }
		if(errCont!=null) { if(tab!=null) builder.append(tab); builder.append("errCont(오류내용):").append(errCont).append('\n'); }
		if(errYn!=null) { if(tab!=null) builder.append(tab); builder.append("errYn(오류여부):").append(errYn).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		super.toString(builder, tab);
	}

}
