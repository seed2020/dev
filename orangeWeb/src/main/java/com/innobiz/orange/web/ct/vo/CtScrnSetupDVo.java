package com.innobiz.orange.web.ct.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/** CT_SCRN_SETUP_D [커뮤니티 화면 설정 상세] */
public class CtScrnSetupDVo extends CommonVoImpl implements Cloneable{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 2096543943131307666L;
	
	/** 회사 ID */
	
	private String compId;
	
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 커뮤니티 ID */
	
	private String ctId;
	
	public String getCtId() {
		return ctId;
	}

	public void setCtId(String ctId) {
		this.ctId = ctId;
	}
	
	/** 템플릿 파일 제목 */
	
	private String tplFileSubj;
	
	public String getTplFileSubj() {
		return tplFileSubj;
	}

	public void setTplFileSubj(String tplFileSubj) {
		this.tplFileSubj = tplFileSubj;
	}
	
	/** 커뮤니티 소개 */
	
	private String ctItro;
	
	public String getCtItro() {
		return ctItro;
	}

	public void setCtItro(String ctItro) {
		this.ctItro = ctItro;
	}
	
	/** 위치수 */
	
	private Integer locCnt;
	
	public Integer getLocCnt() {
		return locCnt;
	}

	public void setLocCnt(Integer locCnt) {
		this.locCnt = locCnt;
	}
	
	/** 위치1 ID */
	
	private String loc1CtFncId;
	
	public String getLoc1CtFncId() {
		return loc1CtFncId;
	}

	public void setLoc1CtFncId(String loc1CtFncId) {
		this.loc1CtFncId = loc1CtFncId;
	}
	
	/** 위치2 ID */
	
	private String loc2CtFncId;
	
	public String getLoc2CtFncId() {
		return loc2CtFncId;
	}

	public void setLoc2CtFncId(String loc2CtFncId) {
		this.loc2CtFncId = loc2CtFncId;
	}
	
	/** 위치3 ID */
	
	private String loc3CtFncId;
	
	public String getLoc3CtFncId() {
		return loc3CtFncId;
	}

	public void setLoc3CtFncId(String loc3CtFncId) {
		this.loc3CtFncId = loc3CtFncId;
	}
	
	/** 위치4 ID */
	
	private String loc4CtFncId;
	
	public String getLoc4CtFncId() {
		return loc4CtFncId;
	}

	public void setLoc4CtFncId(String loc4CtFncId) {
		this.loc4CtFncId = loc4CtFncId;
	}

	/** 위치5 ID */
	
	private String loc5CtFncId;
	
	public String getLoc5CtFncId() {
		return loc5CtFncId;
	}

	public void setLoc5CtFncId(String loc5CtFncId) {
		this.loc5CtFncId = loc5CtFncId;
	}

	/** 위치6 ID */
	
	private String loc6CtFncId;
	
	public String getLoc6CtFncId() {
		return loc6CtFncId;
	}

	public void setLoc6CtFncId(String loc6CtFncId) {
		this.loc6CtFncId = loc6CtFncId;
	}

	/** 위치7 ID */
	
	private String loc7CtFncId;
	
	public String getLoc7CtFncId() {
		return loc7CtFncId;
	}

	public void setLoc7CtFncId(String loc7CtFncId) {
		this.loc7CtFncId = loc7CtFncId;
	}

	/** 위치8 ID */
	
	private String loc8CtFncId;
	
	public String getLoc8CtFncId() {
		return loc8CtFncId;
	}

	public void setLoc8CtFncId(String loc8CtFncId) {
		this.loc8CtFncId = loc8CtFncId;
	}

	/** 위치9 ID */
	
	private String loc9CtFncId;
	
	public String getLoc9CtFncId() {
		return loc9CtFncId;
	}

	public void setLoc9CtFncId(String loc9CtFncId) {
		this.loc9CtFncId = loc9CtFncId;
	}

	/** 위치10 ID */
	
	private String loc10CtFncId;
	
	public String getLoc10CtFncId() {
		return loc10CtFncId;
	}

	public void setLoc10CtFncId(String loc10CtFncId) {
		this.loc10CtFncId = loc10CtFncId;
	}

	/** 위치11 ID */
	
	private String loc11CtFncId;
	
	public String getLoc11CtFncId() {
		return loc11CtFncId;
	}

	public void setLoc11CtFncId(String loc11CtFncId) {
		this.loc11CtFncId = loc11CtFncId;
	}

	/** 위치12 ID */
	
	private String loc12CtFncId;
	
	
	
	public String getLoc12CtFncId() {
		return loc12CtFncId;
	}

	public void setLoc12CtFncId(String loc12CtFncId) {
		this.loc12CtFncId = loc12CtFncId;
	}

	/** 이미지 파일 아이디 */
	
	private String imgFileId;
	
	
	public String getImgFileId() {
		return imgFileId;
	}

	public void setImgFileId(String imgFileId) {
		this.imgFileId = imgFileId;
	}

	/** 등록일시 */
	
	private String regDt;
	
	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		if(regDt != null && regDt.endsWith(".0") && regDt.length()>2)
			 regDt=regDt.substring(0, regDt.length()-2);
		this.regDt = regDt;
	}
	
	/** 수정일시 */
	
	private String modDt;
	
	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		if(modDt != null && modDt.endsWith(".0") && modDt.length()>2)
			 modDt=modDt.substring(0, modDt.length()-2);
		this.modDt = modDt;
	}

	
	/** 등록자 UID */
	
	private String regrUid;
	
	public String getRegrUid() {
		return regrUid;
	}

	public void setRegrUid(String regrUid) {
		this.regrUid = regrUid;
	}
	
	/** 수정자 UID */
	
	private String modrUid;
	
	public String getModrUid() {
		return modrUid;
	}

	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}
	
	/** 에디터구분코드 */
	
	private String editorTypCd;
	
	public String getEditorTypCd() {
		return editorTypCd;
	}

	public void setEditorTypCd(String editorTypCd) {
		this.editorTypCd = editorTypCd;
	}
	
	/** 커뮤니티 기능 명 */
	
	private String ctFncNm;
	
	public String getCtFncNm() {
		return ctFncNm;
	}

	public void setCtFncNm(String ctFncNm) {
		this.ctFncNm = ctFncNm;
	}
	
	/** 커뮤니티 언어 */
	
	private String langTyp;
	
	public String getLangTyp() {
		return langTyp;
	}

	public void setLangTyp(String langTyp) {
		this.langTyp = langTyp;
	}
	
	/** 커뮤니티 기능 URL */
	
	private String ptUrl;
	
	public String getPtUrl() {
		return ptUrl;
	}

	public void setPtUrl(String ptUrl) {
		this.ptUrl = ptUrl;
	}
	
	/** 커뮤니티 기능 UID */
	private String ctFncUid;
	
	public String getCtFncUid() {
		return ctFncUid;
	}

	public void setCtFncUid(String ctFncUid) {
		this.ctFncUid = ctFncUid;
	}
	
	 public Object clone() throws CloneNotSupportedException{
	        return super.clone(); 
	    }
	
	/** SQL ID 리턴 */
	@Override
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		String classNameDomain=getClass().getName().substring(0, getClass().getName().length()-2).replaceAll("\\.vo\\.", ".dao.");
		if(QueryType.SELECT==queryType){
			return classNameDomain+"Dao.select"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.INSERT==queryType){
			return classNameDomain+"Dao.insert"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.UPDATE==queryType){
			return classNameDomain+"Dao.update"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.DELETE==queryType){
			return classNameDomain+"Dao.delete"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		} else if(QueryType.COUNT==queryType){
			return classNameDomain+"Dao.count"+classNameDomain.split("\\.")[classNameDomain.split("\\.").length-1];
		}
		return null;
	}

	/** String으로 변환 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":일정 기본]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	@Override
	public void toString(StringBuilder builder, String tab){
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사 ID):").append(compId).append('\n'); }
		if(ctId!=null) { if(tab!=null) builder.append(tab); builder.append("ctId(커뮤니티 ID):").append(ctId).append('\n'); }
		if(tplFileSubj!=null) { if(tab!=null) builder.append(tab); builder.append("tplFileSubj(템플릿 파일 제목):").append(tplFileSubj).append('\n'); }
		if(ctItro!=null) { if(tab!=null) builder.append(tab); builder.append("ctItro(커뮤니티 소개):").append(ctItro).append('\n'); }
		if(locCnt!=null) { if(tab!=null) builder.append(tab); builder.append("locCnt(위치수):").append(locCnt).append('\n'); }
		if(loc1CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc1CtFncId(위치1 ID):").append(loc1CtFncId).append('\n'); }
		if(loc2CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc2CtFncId(위치2 ID):").append(loc2CtFncId).append('\n'); }
		if(loc3CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc3CtFncId(위치3 ID):").append(loc3CtFncId).append('\n'); }
		if(loc4CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc4CtFncId(위치4 ID):").append(loc4CtFncId).append('\n'); }
		if(loc5CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc5CtFncId(위치5 ID):").append(loc5CtFncId).append('\n'); }
		if(loc6CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc6CtFncId(위치6 ID):").append(loc6CtFncId).append('\n'); }
		if(loc7CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc7CtFncId(위치7 ID):").append(loc7CtFncId).append('\n'); }
		if(loc8CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc8CtFncId(위치8 ID):").append(loc8CtFncId).append('\n'); }
		if(loc9CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc9CtFncId(위치9 ID):").append(loc9CtFncId).append('\n'); }
		if(loc10CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc10CtFncId(위치10 ID):").append(loc10CtFncId).append('\n'); }
		if(loc11CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc11CtFncId(위치11 ID):").append(loc11CtFncId).append('\n'); }
		if(loc12CtFncId!=null) { if(tab!=null) builder.append(tab); builder.append("loc12CtFncId(위치12 ID):").append(loc12CtFncId).append('\n'); }
		if(imgFileId!=null) { if(tab!=null) builder.append(tab); builder.append("imgFileId(이미지 파일 아이디):").append(imgFileId).append('\n'); }
		if(regDt!=null) { if(tab!=null) builder.append(tab); builder.append("regDt(등록일시):").append(regDt).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(regrUid!=null) { if(tab!=null) builder.append(tab); builder.append("regrUid(등록자 UID):").append(regrUid).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자 UID):").append(modrUid).append('\n'); }
		if(editorTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("editorTypCd(에디터구분코드):").append(editorTypCd).append('\n'); }
		if(ctFncNm!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncNm(커뮤니티 기능 명):").append(ctFncNm).append('\n'); }
		if(langTyp!=null) { if(tab!=null) builder.append(tab); builder.append("langTyp(커뮤니티 언어):").append(langTyp).append('\n'); }
		if(ptUrl!=null) { if(tab!=null) builder.append(tab); builder.append("ptUrl(커뮤니티 기능 URL):").append(ptUrl).append('\n'); }
		if(ctFncUid!=null) { if(tab!=null) builder.append(tab); builder.append("ctFncUid(커뮤니티 기능 UID):").append(ctFncUid).append('\n'); }
		super.toString(builder, tab);
	}

}