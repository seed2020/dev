package com.innobiz.orange.web.pt.secu;

/** 권한그룹ID, 권한코드 VO <br/>
 *  - 어떤 그룹이 어떤 권한을 가지고 있는지 담는 VO
 *  */
public class AuthCdVo {
	
	/** 권한그룹ID */
	public String authGrpId;
	
	/** 권한코드 */
	public String authCd;
	
	/** 생성자 */
	public AuthCdVo(String authGrpId, String authCd) {
		this.authGrpId = authGrpId;
		this.authCd = authCd;
	}
	
	/** 더 높은 권한 인지 검사 */
	public boolean higherThan(String authCd){
		// 권한 순위 : "S" > "A" > "M" > "W" > "R"
		// "S" 는 이전에 걸러져 실제로 검사 대상이 아님
		if(authCd==null) return true;
		if(authCd.equals("R")){
			return !this.authCd.equals("R");
		}
		if(authCd.equals("W")){
			return this.authCd.equals("A") || this.authCd.equals("M");
		}
		if(authCd.equals("M")){
			return this.authCd.equals("A");
		}
		return false;
	}
	
}
