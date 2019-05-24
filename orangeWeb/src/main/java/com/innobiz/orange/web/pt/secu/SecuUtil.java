package com.innobiz.orange.web.pt.secu;

import javax.servlet.http.HttpServletRequest;

/** JSP에서 tag-lib 에 의해 호출 되며 해당 권한이 있는 지를 검사하는 유틸 */
public class SecuUtil {
	
//	/** 권한 - SYS:시스템, S:슈퍼, A:관리, M:수정, W:쓰기, R:읽기<br/><br/>
//	 *  
//	 *  SYS - 시스템 관리권한 - 회사 범위를 넘어서는 설정이 필요한 사람
//	 *  S:슈퍼 - 권한그룹 [슈퍼관리자]에 속한 사용자
//	 *  A:관리 - 관리 권한을 가지는 사용자<br/>
//	 *  M:수정 - 수정 권한을 가지는 사용자<br/>
//	 *  W:쓰기 - 쓰기 권한을 가지는 사용자<br/>
//	 *  R:읽기 - 읽기 권한을 가지는 사용자<br/>
//	 * */
//	public static boolean hasAuth(HttpServletRequest request, String requiredAuth){
//		return hasAuth(request, requiredAuth, null);
//	}
	/** 권한체크 - SYS:시스템, S:슈퍼, A:관리, M:수정, W:쓰기, R:읽기<br/><br/>
	 *  
	 *  ownerUid 의미 : W:쓰기 이상의 권한이 있고, 작성자가 세션과 같으면 권한 있음<br/><br/>
	 *  
	 *  SYS - 시스템 관리권한 - 회사 범위를 넘어서는 설정이 필요한 사람<br/>
	 *  S:슈퍼 - 권한그룹 [슈퍼관리자]에 속한 사용자<br/>
	 *  A:관리 - 관리 권한을 가지는 사용자<br/>
	 *  M:수정 - 수정 권한을 가지는 사용자<br/>
	 *  W:쓰기 - 쓰기 권한을 가지는 사용자<br/>
	 *  R:읽기 - 읽기 권한을 가지는 사용자<br/>
	 * */
	public static boolean hasAuth(HttpServletRequest request, String requiredAuth, String ownerUid){
		String userAuth = (String)request.getAttribute("_AUTH");
		UserVo userVo = LoginSession.getUser(request);
		return hasAuth(userVo, userAuth, requiredAuth, ownerUid);
	}
	/** 권한체크 */
	public static boolean hasAuth(UserVo userVo, String userAuth, String requiredAuth, String ownerUid){
		
		// [요청권한]이 없으면 권한있음
		if(requiredAuth==null || requiredAuth.isEmpty()) return true;

		// 해당 페이지에 대한 [사용자권한]이 없으면 권한 없음
		if(userAuth==null || userAuth.isEmpty()) return false;
		
		// [시스템 관리자] 권한
		if("SYS".equals(userAuth)) return true;
		// [관리자] 권한
		if("S".equals(userAuth)){
			if("SYS".equals(requiredAuth)) return false;
			return true;
		}
		
		// [요청권한]과 [사용자권한]이 같으면
		if(requiredAuth.equals(userAuth)) {
			return true;
		}
		
		// ownerUid(작성자UID) 가 있을때
		if(ownerUid!=null && !ownerUid.isEmpty()){
			// 사용자 권한이 A:관리, M:수정, W:쓰기 이고, 작성자와 세션의 사용자가 같으면 권한 있음
			if("A".equals(userAuth) || "M".equals(userAuth) || "W".equals(userAuth)){
				if(userVo!=null && ownerUid.equals(userVo.getUserUid())) return true;
			}
		}
		
		// [요청권한]이 M:수정 일때
		if("M".equals(requiredAuth)){
			// [사용자권한]이 A:관리 이면
			if("A".equals(userAuth)) return true;
			return false;
		// [요청권한]이 W:쓰기 일때
		} else if("W".equals(requiredAuth)){
			// [사용자권한]이 A:관리, M:수정 이면
			if("A".equals(userAuth) || "M".equals(userAuth)) return true;
			return false;
		// [요청권한]이 R:읽기 일때
		} else if("R".equals(requiredAuth)){
			return true;
		}
		return false;
	}
}
