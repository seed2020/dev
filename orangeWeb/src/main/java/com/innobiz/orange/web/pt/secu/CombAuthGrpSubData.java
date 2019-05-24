package com.innobiz.orange.web.pt.secu;

/** 권한의 조합을 위한 상세의 서브 정보를 담는 객체
 *  - 그룹ID, 하위포함여부 를 가지고 있는 단순 데이터
 * */
public class CombAuthGrpSubData {

	/** 그룹ID */
	public String grpId;
	/** 하위포함여부 */
	public boolean subIncl; 
	
	/** 생성자 */
	public CombAuthGrpSubData(String grpId, boolean subIncl){
		this.grpId = grpId;
		this.subIncl = subIncl;
	}
	/** 스트링 변환 */
	public String toString(){
		if(!subIncl) return grpId;
		return grpId+":Y";
	}
	/** 배열을 스트링으로 변환 */
	public static String toString(CombAuthGrpSubData[] array){
		if(array==null) return "";
		if(array.length==0) return "[]";
		StringBuilder builder = new StringBuilder(254);
		for(CombAuthGrpSubData ins:array){
			builder.append(ins.grpId);
			if(ins.subIncl) builder.append(":Y");
		}
		return builder.toString();
	}
}
