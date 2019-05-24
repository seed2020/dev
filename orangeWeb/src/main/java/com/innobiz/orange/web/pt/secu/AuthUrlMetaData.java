package com.innobiz.orange.web.pt.secu;

import javax.servlet.http.HttpServletRequest;

/** 권한 메타 데이터 - 해당 URL의 권한 체크 목적으로 레이아웃ID, 레이아웃조합ID, 메뉴ID, URL, 파라미터 정보를 가지고 있음  */
public class AuthUrlMetaData {

	/** 레이아웃조합ID - 파라미터(menuId)에 해당하는 레이아웃조합ID, 메뉴의 메뉴ID에 매핑됨 */
	private String combId;
	
	/** 레이아웃ID - 메뉴그룹에 매핑되는 메뉴레이아웃ID */
	private String loutId;
	
	/** 메뉴ID or 포틀릿ID - 레이아웃조합의 조합ID에 매핑된 메뉴의 메뉴ID */
	private String mnuPltId;
	
	/** URI */
	private String uri;
	
	/** 파라미터 */
	private String[][] params;
	
	/** 권한코드 */
	private String authCd = null;
	
	/** 관리자 메뉴 여부 */
	private boolean admMnu = false;
	
	/** 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, P:포틀릿 용 */
	private char loutCatId;

	/** 생성자 */
	private AuthUrlMetaData() {}
	
	/** 생성자 */
	public AuthUrlMetaData(String loutId, String combId, String mnuPltId, char loutCatId, String uri, String[][] params, boolean admMnu) {
		this.loutId = loutId;
		this.combId = combId;
		this.mnuPltId = mnuPltId;
		this.loutCatId = loutCatId;
		this.uri = uri;
		this.params = params;
		this.admMnu = admMnu;
	}
	
	/** 메타데이타중 loutId, combId, mnuPltId 만 복사한 신규 인스탄스 리턴 */
	public AuthUrlMetaData copyWithAuthCd(String authCd){
		AuthUrlMetaData ins = new AuthUrlMetaData();
		ins.loutId = loutId;
		ins.combId = combId;
		ins.mnuPltId = mnuPltId;
		ins.authCd = authCd;
		return ins;
	}

	/** 레이아웃조합ID - 파라미터(menuId)에 해당하는 레이아웃조합ID, 메뉴의 메뉴ID에 매핑됨 */
	public String getCombId() {
		return combId;
	}

	/** 레이아웃ID - 메뉴그룹에 매핑되는 메뉴레이아웃ID */
	public String getLoutId() {
		return loutId;
	}

	/** 메뉴ID - 레이아웃조합의 조합ID에 매핑된 메뉴의 메뉴ID */
	public String getMnuPltId() {
		return mnuPltId;
	}

	/** URI */
	public String getUri() {
		return uri;
	}

	/** 파라미터 */
	public String[][] getParams() {
		return params;
	}

	/** 권한코드 */
	public String getAuthCd() {
		return authCd;
	}

	/** 권한코드 */
	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	/** 관리자 메뉴 여부 */
	public boolean isAdmMnu() {
		return admMnu;
	}

	/** 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃, P:포틀릿 용 */
	public char getLoutCatId() {
		return loutCatId;
	}
	
	/** 파라미터가 같은지 검사 */
	public boolean isParamMatched(HttpServletRequest request){
		if(params==null || params.length==0) return true;
		String value;
		for(int i=0; i<params.length; i++){
			if(params[i][0]!=null){
				value = request.getParameter(params[i][0]);
				if(value==null || !value.equals(params[i][1])) return false;
			}
		}
		return true;
	}
	
	/** 경로가 같은지 체크 */
	public boolean isPathMapthed(String url){
		int p = this.uri.lastIndexOf('/');
		String checkPath = p<0 ? url : url.substring(0, p+1);
		return url != null && url.startsWith(checkPath);
	}
}
