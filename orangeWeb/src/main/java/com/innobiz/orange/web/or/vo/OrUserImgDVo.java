package com.innobiz.orange.web.or.vo;

import com.innobiz.orange.web.cm.vo.CommonVoImpl;
import com.innobiz.orange.web.cm.vo.QueryType;

/**
 * 사용자이미지상세(OR_USER_IMG_D) 테이블 VO
 */
public class OrUserImgDVo extends CommonVoImpl {

	/** serialVersionUID. */
	private static final long serialVersionUID = 3863132167335263071L;

	/** 사용자UID - KEY */
	private String userUid;

	/** 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진 */
	private String userImgTypCd;

	/** 이미지경로 */
	private String imgPath;

	/** 이미지넓이 */
	private String imgWdth;

	/** 이미지높이 */
	private String imgHght;

	/** 수정자UID */
	private String modrUid;

	/** 수정일시 */
	private String modDt;


	// 추가컬럼
	/** 회사ID */
	private String compId;

	/** 사용자이미지구분명 */
	private String userImgTypNm;

	/** 수정자명 */
	private String modrNm;

	/** 사용자UID - KEY */
	public String getUserUid() {
		return userUid;
	}

	/** 사용자UID - KEY */
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	/** 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진 */
	public String getUserImgTypCd() {
		return userImgTypCd;
	}

	/** 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진 */
	public void setUserImgTypCd(String userImgTypCd) {
		this.userImgTypCd = userImgTypCd;
	}

	/** 이미지경로 */
	public String getImgPath() {
		return imgPath;
	}

	/** 이미지경로 */
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	/** 이미지넓이 */
	public String getImgWdth() {
		return imgWdth;
	}

	/** 이미지넓이 */
	public void setImgWdth(String imgWdth) {
		this.imgWdth = imgWdth;
	}

	/** 이미지높이 */
	public String getImgHght() {
		return imgHght;
	}

	/** 이미지높이 */
	public void setImgHght(String imgHght) {
		this.imgHght = imgHght;
	}

	/** 수정자UID */
	public String getModrUid() {
		return modrUid;
	}

	/** 수정자UID */
	public void setModrUid(String modrUid) {
		this.modrUid = modrUid;
	}

	/** 수정일시 */
	public String getModDt() {
		return modDt;
	}

	/** 수정일시 */
	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	/** 회사ID */
	public String getCompId() {
		return compId;
	}

	/** 회사ID */
	public void setCompId(String compId) {
		this.compId = compId;
	}

	/** 사용자이미지구분명 */
	public String getUserImgTypNm() {
		return userImgTypNm;
	}

	/** 사용자이미지구분명 */
	public void setUserImgTypNm(String userImgTypNm) {
		this.userImgTypNm = userImgTypNm;
	}

	/** 수정자명 */
	public String getModrNm() {
		return modrNm;
	}

	/** 수정자명 */
	public void setModrNm(String modrNm) {
		this.modrNm = modrNm;
	}

	/** SQL ID 리턴 */
	public String getQueryId(QueryType queryType) {
		if(getInstanceQueryId()!=null) return getInstanceQueryId();
		if(QueryType.SELECT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserImgDDao.selectOrUserImgD";
		} else if(QueryType.INSERT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserImgDDao.insertOrUserImgD";
		} else if(QueryType.UPDATE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserImgDDao.updateOrUserImgD";
		} else if(QueryType.DELETE==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserImgDDao.deleteOrUserImgD";
		} else if(QueryType.COUNT==queryType){
			return "com.innobiz.orange.web.or.dao.OrUserImgDDao.countOrUserImgD";
		}
		return null;
	}

	/** String으로 변환 */
	public String toString(){
		StringBuilder builder = new StringBuilder(512);
		builder.append('[').append(this.getClass().getName()).append(":사용자이미지상세]\n");
		toString(builder, null);
		return builder.toString();
	}

	/** String으로 변환, builder에 append 함 */
	public void toString(StringBuilder builder, String tab){
		if(userUid!=null) { if(tab!=null) builder.append(tab); builder.append("userUid(사용자UID-PK):").append(userUid).append('\n'); }
		if(userImgTypCd!=null) { if(tab!=null) builder.append(tab); builder.append("userImgTypCd(사용자이미지구분코드-PK):").append(userImgTypCd).append('\n'); }
		if(imgPath!=null) { if(tab!=null) builder.append(tab); builder.append("imgPath(이미지경로):").append(imgPath).append('\n'); }
		if(imgWdth!=null) { if(tab!=null) builder.append(tab); builder.append("imgWdth(이미지넓이):").append(imgWdth).append('\n'); }
		if(imgHght!=null) { if(tab!=null) builder.append(tab); builder.append("imgHght(이미지높이):").append(imgHght).append('\n'); }
		if(modrUid!=null) { if(tab!=null) builder.append(tab); builder.append("modrUid(수정자UID):").append(modrUid).append('\n'); }
		if(modDt!=null) { if(tab!=null) builder.append(tab); builder.append("modDt(수정일시):").append(modDt).append('\n'); }
		if(compId!=null) { if(tab!=null) builder.append(tab); builder.append("compId(회사ID):").append(compId).append('\n'); }
		if(userImgTypNm!=null) { if(tab!=null) builder.append(tab); builder.append("userImgTypNm(사용자이미지구분명):").append(userImgTypNm).append('\n'); }
		if(modrNm!=null) { if(tab!=null) builder.append(tab); builder.append("modrNm(수정자명):").append(modrNm).append('\n'); }
		super.toString(builder, tab);
	}
}
