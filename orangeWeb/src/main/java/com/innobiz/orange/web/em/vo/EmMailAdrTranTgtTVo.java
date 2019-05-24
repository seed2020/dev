package com.innobiz.orange.web.em.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2017/03/16 15:41 ******/
/**
* 이관대상임시(EM_MAIL_ADR_TRAN_TGT_T) 테이블 VO 
*/
public class EmMailAdrTranTgtTVo extends CommonVoImpl {
	/** serialVersionUID */
	private static final long serialVersionUID = -3575831252255821295L;

	/** 이관ID */ 
	private String tranId;

 	/** 순번 */ 
	private Integer seq;

 	/** 사용자UID */ 
	private String userUid;

 	/** 사용자번호 */ 
	private String userSn;
	
	/** 폴더건수 */ 
	private Integer fldCnt;

 	/** 주소록건수 */ 
	private Integer adrCnt;
	
 	/** 오류여부 */ 
	private String errYn;

 	public void setTranId(String tranId) { 
		this.tranId = tranId;
	}
	/** 이관ID */ 
	public String getTranId() { 
		return tranId;
	}

	public void setSeq(Integer seq) { 
		this.seq = seq;
	}
	/** 순번 */ 
	public Integer getSeq() { 
		return seq;
	}

	public void setUserUid(String userUid) { 
		this.userUid = userUid;
	}
	/** 사용자UID */ 
	public String getUserUid() { 
		return userUid;
	}

	public void setUserSn(String userSn) { 
		this.userSn = userSn;
	}
	/** 사용자번호 */ 
	public String getUserSn() { 
		return userSn;
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
	
	public void setErrYn(String errYn) { 
		this.errYn = errYn;
	}
	/** 오류여부 */ 
	public String getErrYn() { 
		return errYn;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranTgtTDao.selectEmMailAdrTranTgtT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranTgtTDao.insertEmMailAdrTranTgtT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranTgtTDao.updateEmMailAdrTranTgtT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranTgtTDao.deleteEmMailAdrTranTgtT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.em.dao.EmMailAdrTranTgtTDao.countEmMailAdrTranTgtT";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":이관대상임시]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID):").append(tranId).append('\n'); }
		if(seq!=null) { if(tab!=null) builder.append(tab); builder.append("seq(순번):").append(seq).append('\n'); }
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID):").append(userUid).append('\n'); }
		if(userSn!=null) { if(tab!=null) builder.append(tab); builder.append("userSn(사용자번호):").append(userSn).append('\n'); }
		if(fldCnt!=null) { if(tab!=null) builder.append(tab); builder.append("fldCnt(폴더건수):").append(fldCnt).append('\n'); }
		if(adrCnt!=null) { if(tab!=null) builder.append(tab); builder.append("adrCnt(주소록건수):").append(adrCnt).append('\n'); }		
		if(errYn!=null) { if(tab!=null) builder.append(tab); builder.append("errYn(오류여부):").append(errYn).append('\n'); }
		super.toString(builder, tab);
	}

}
