package com.innobiz.orange.web.cu.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/02/01 15:46 ******/
/**
* 수신(CU_NOTE_RECV_L) 테이블 VO 
*/
public class CuNoteRecvLVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6118579430255785956L;

	/** 수신번호 */ 
	private String recvNo;
	
	/** 발송번호 */ 
	private String sendNo;
	
 	/** 수신자UID */ 
	private String recvrUid;

 	/** 수신자명 */ 
	private String recvrNm;

 	/** 수신일시 */ 
	private String recvDt;
	
	/** 추가 */
	/** 수신자리소스명 */
	private String recvrRescNm;

 	public void setRecvNo(String recvNo) { 
		this.recvNo = recvNo;
	}
	/** 수신번호 */ 
	public String getRecvNo() { 
		return recvNo;
	}
	
	public void setSendNo(String sendNo) { 
		this.sendNo = sendNo;
	}
	/** 발송번호 */ 
	public String getSendNo() { 
		return sendNo;
	}

	public void setRecvrUid(String recvrUid) { 
		this.recvrUid = recvrUid;
	}
	/** 수신자UID */ 
	public String getRecvrUid() { 
		return recvrUid;
	}

	public void setRecvrNm(String recvrNm) { 
		this.recvrNm = recvrNm;
	}
	/** 수신자명 */ 
	public String getRecvrNm() { 
		return recvrNm;
	}

	public void setRecvDt(String recvDt) { 
		this.recvDt = recvDt;
	}
	/** 수신일시 */ 
	public String getRecvDt() { 
		return recvDt;
	}
	
	/** 수신자리소스명 */
	public String getRecvrRescNm() {
		return recvrRescNm;
	}
	public void setRecvrRescNm(String recvrRescNm) {
		this.recvrRescNm = recvrRescNm;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.selectCuNoteRecvL";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.insertCuNoteRecvL";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.updateCuNoteRecvL";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.deleteCuNoteRecvL";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.countCuNoteRecvL";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":수신]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(recvNo!=null) { if(tab!=null) builder.append(tab); builder.append("recvNo(수신번호):").append(recvNo).append('\n'); }
		if(sendNo!=null) { if(tab!=null) builder.append(tab); builder.append("sendNo(발송번호):").append(sendNo).append('\n'); }
		if(recvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("recvrUid(수신자UID):").append(recvrUid).append('\n'); }
		if(recvrNm!=null) { if(tab!=null) builder.append(tab); builder.append("recvrNm(수신자명):").append(recvrNm).append('\n'); }
		if(recvDt!=null) { if(tab!=null) builder.append(tab); builder.append("recvDt(수신일시):").append(recvDt).append('\n'); }
		super.toString(builder, tab);
	}

}
