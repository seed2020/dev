package com.innobiz.orange.web.cu.vo;

import java.util.List;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/****** Object:  Vo - Date: 2018/01/31 14:20 ******/
/**
* 받은쪽지(CU_NOTE_RECV_B) 테이블 VO 
*/
public class CuNoteRecvBVo extends CommonVoImpl {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -108330920471449172L;

	/** 수신번호 */ 
	private String recvNo;

 	/** 발송번호 */ 
	private String sendNo;

	/** 수신자UID */ 
	private String recvrUid;

 	/** 읽음여부 */ 
	private String readYn;

 	/** 제목 */ 
	private String subj;

 	/** 내용 */ 
	private String cont;

	/** 삭제여부 */ 
	private String delYn;
	
 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 추가 */
	/** 받은쪽지여부 */ 
	private boolean isRecvNote = true;
	
	/** 등록자명 */
	private String regrNm;
	
	/** 수신자명 */
	private String recvNm;
	
	/** 번호ID 목록 */
	private List<String> seqIdList;
	
	/** 보낸사람UID*/
	private String schRegrUid;
	
	/** 받는사람UID*/
	private String schRecvUid;
	
	/** 파일건수 */
	private int fileCnt;
	
	/** 받는사람목록*/
	private List<CuNoteRecvLVo> cuNoteRecvLVoList;

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

	public void setReadYn(String readYn) { 
		this.readYn = readYn;
	}
	/** 읽음여부 */ 
	public String getReadYn() { 
		return readYn;
	}

	public void setSubj(String subj) { 
		this.subj = subj;
	}
	/** 제목 */ 
	public String getSubj() { 
		return subj;
	}

	public void setCont(String cont) { 
		this.cont = cont;
	}
	/** 내용 */ 
	public String getCont() { 
		return cont;
	}
	
	public void setDelYn(String delYn) { 
		this.delYn = delYn;
	}
	/** 삭제여부 */ 
	public String getDelYn() { 
		return delYn;
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
	
	/** 받은쪽지여부 */ 
	public boolean isRecvNote() {
		return isRecvNote;
	}
	public void setRecvNote(boolean isRecvNote) {
		this.isRecvNote = isRecvNote;
	}

	/** 등록자명 */
	public String getRegrNm() {
		return regrNm;
	}
	public void setRegrNm(String regrNm) {
		this.regrNm = regrNm;
	}
	
	/** 수신자명 */
	public String getRecvNm() {
		return recvNm;
	}
	public void setRecvNm(String recvNm) {
		this.recvNm = recvNm;
	}
	
	/** 번호ID 목록 */
	public List<String> getSeqIdList() {
		return seqIdList;
	}
	public void setSeqIdList(List<String> seqIdList) {
		this.seqIdList = seqIdList;
	}
	
	/** 보낸사람UID*/
	public String getSchRegrUid() {
		return schRegrUid;
	}
	public void setSchRegrUid(String schRegrUid) {
		this.schRegrUid = schRegrUid;
	}
	
	/** 받는사람UID*/
	public String getSchRecvUid() {
		return schRecvUid;
	}
	public void setSchRecvUid(String schRecvUid) {
		this.schRecvUid = schRecvUid;
	}
	
	/** 파일건수 */
	public int getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(int fileCnt) {
		this.fileCnt = fileCnt;
	}
	
	/** 받는사람목록*/
	public List<CuNoteRecvLVo> getCuNoteRecvLVoList() {
		return cuNoteRecvLVoList;
	}
	public void setCuNoteRecvLVoList(List<CuNoteRecvLVo> cuNoteRecvLVoList) {
		this.cuNoteRecvLVoList = cuNoteRecvLVoList;
	}
	
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.selectCuNoteRecvB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.insertCuNoteRecvB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.updateCuNoteRecvB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.deleteCuNoteRecvB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.cu.dao.CuNoteRecvBDao.countCuNoteRecvB";
		}
		return null;
	}

	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":받은쪽지]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(recvNo!=null) { if(tab!=null) builder.append(tab); builder.append("recvNo(수신번호):").append(recvNo).append('\n'); }
		if(sendNo!=null) { if(tab!=null) builder.append(tab); builder.append("sendNo(발송번호):").append(sendNo).append('\n'); }
		if(recvrUid!=null) { if(tab!=null) builder.append(tab); builder.append("recvrUid(수신자UID):").append(recvrUid).append('\n'); }
		if(readYn!=null) { if(tab!=null) builder.append(tab); builder.append("readYn(읽음여부):").append(readYn).append('\n'); }
		if(subj!=null) { if(tab!=null) builder.append(tab); builder.append("subj(제목):").append(subj).append('\n'); }
		if(cont!=null) { if(tab!=null) builder.append(tab); builder.append("cont(내용):").append(cont).append('\n'); }
		if(delYn!=null) { if(tab!=null) builder.append(tab); builder.append("delYn(삭제여부):").append(delYn).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
