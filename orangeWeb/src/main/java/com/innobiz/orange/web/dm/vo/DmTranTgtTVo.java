package com.innobiz.orange.web.dm.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2015/12/03 11:37 ******/
/**
* 이관대상임시(DM_TRAN_TGT_T) 테이블 VO 
*/
@SuppressWarnings("serial")
public class DmTranTgtTVo extends CommonVoImpl {
	/** 이관일련번호 */ 
	private Integer tranSeq;

 	/** 이관ID */ 
	private String tranId;

 	/** 문서그룹ID */ 
	private Integer docGrpId;

 	public void setTranSeq(Integer tranSeq) { 
		this.tranSeq = tranSeq;
	}
	/** 이관일련번호 */ 
	public Integer getTranSeq() { 
		return tranSeq;
	}

	public void setTranId(String tranId) { 
		this.tranId = tranId;
	}
	/** 이관ID */ 
	public String getTranId() { 
		return tranId;
	}

	public void setDocGrpId(Integer docGrpId) { 
		this.docGrpId = docGrpId;
	}
	/** 문서그룹ID */ 
	public Integer getDocGrpId() { 
		return docGrpId;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTranTgtTDao.selectDmTranTgtT";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTranTgtTDao.insertDmTranTgtT";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTranTgtTDao.updateDmTranTgtT";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTranTgtTDao.deleteDmTranTgtT";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.dm.dao.DmTranTgtTDao.countDmTranTgtT";
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
		if(tranSeq!=null) { if(tab!=null) builder.append(tab); builder.append("tranSeq(이관일련번호):").append(tranSeq).append('\n'); }
		if(tranId!=null) { if(tab!=null) builder.append(tab); builder.append("tranId(이관ID):").append(tranId).append('\n'); }
		if(docGrpId!=null) { if(tab!=null) builder.append(tab); builder.append("docGrpId(문서그룹ID):").append(docGrpId).append('\n'); }
		super.toString(builder, tab);
	}

}
