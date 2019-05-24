package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;
/**
 * 사용자 로그인 이력 설정(삭제주기)(OR_USER_LGIN_SETUP_B) 테이블 VO
 */
@SuppressWarnings("serial")
public class OrUserLginSetupBVo extends CommonVoImpl {

	/** 회사ID */ 
	private String compId;

 	/** 반복구분코드 */ 
	private String repetTypCd;

 	/** 시작년월일 */ 
	private String strtYmd;

 	/** 등록자UID */ 
	private String regrUid;

 	/** 등록일시 */ 
	private String regDt;
	
	/** 다음반복연월일 */ 
	private String nextRepetYmd;
	
	/** 사용여부 */ 
	private String useYn;
	
	/** 삭제여부 */ 
	private String delYn;

 	public void setCompId(String compId) { 
		this.compId = compId;
	}
	/** 회사ID */ 
	public String getCompId() { 
		return compId;
	}

	public void setRepetTypCd(String repetTypCd) { 
		this.repetTypCd = repetTypCd;
	}
	/** 반복구분코드 */ 
	public String getRepetTypCd() { 
		return repetTypCd;
	}

	public void setStrtYmd(String strtYmd) { 
		this.strtYmd = strtYmd;
	}
	/** 시작년월일 */ 
	public String getStrtYmd() { 
		return strtYmd;
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
	
	public String getNextRepetYmd() {
		return nextRepetYmd;
	}
	public void setNextRepetYmd(String nextRepetYmd) {
		this.nextRepetYmd = nextRepetYmd;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginSetupBDao.selectOrUserLginSetupB";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginSetupBDao.insertOrUserLginSetupB";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginSetupBDao.updateOrUserLginSetupB";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginSetupBDao.deleteOrUserLginSetupB";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserLginSetupBDao.countOrUserLginSetupB";
		}
		return null;
	}
	
	/** String으로 변환 */ 
	public String toString() { 
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자로그인이력설정]");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */ 
	public void toString(StringBuilder builder, String tab) { 
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(repetTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("repetTypCd(반복구분코드):").append(repetTypCd).append('\n'); }
		if(strtYmd!=null) { if(tab!=null) builder.append(tab); builder.append("strtYmd(시작년월일):").append(strtYmd).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자UID):").append(regrUid).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		super.toString(builder, tab);
	}

}
