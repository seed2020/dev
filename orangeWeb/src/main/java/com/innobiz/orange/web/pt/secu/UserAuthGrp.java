package com.innobiz.orange.web.pt.secu;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtAuthGrpUserRVo;

/** 사용자가 속한 그룹(사용자권한그룹, 관리자권한그룹, 사용자그룹) 정보를 담는 객체 <br/>
 *   - 로그인 할때 자신이 속한 권한그룹 정보 읽어서 생성함
 *   - CombAuthGrp 과 비교하여 사용자가 속한 권한그룹목록(String-Array)를 생성 - 세션에 저장함
 *   - 권한 조합이 후 UserAuthGrp 은 폐기함
 *  */
public class UserAuthGrp {

	/** 회사ID - KEY */
	private String compId;

	/** 사용자 권한관리 상세 데이터 맵 : U:사용자권한그룹 or M:모바일권한그룹 */
	private UserAuthGrpDetl userAuthDetl = null;
	
	/** 사용자 권한관리 상세 데이터 맵 : A:관리자권한그룹 */
	private UserAuthGrpDetl adminAuthDetl = null;
	
	/** 사용자 권한관리 상세 데이터 맵 : G:사용자그룹 */
	private UserAuthGrpDetl userGrpDetl = null;
	
	/** 생성자 */
	public UserAuthGrp(String compId){
		this.compId = compId;
		add("G", false, PtConstant.AUTH_USERS);// 디폴트로 속해지는 그룹
	}
	
	/** 권한그룹사용자관계 데이터를 더함 */
	public void add(PtAuthGrpUserRVo ptAuthGrpUserRVo){
		add(ptAuthGrpUserRVo.getAuthGrpTypCd(), "Y".equals(ptAuthGrpUserRVo.getExcliYn()), ptAuthGrpUserRVo.getAuthGrpId());
	}
	
	/** 권한그룹사용자관계 데이터를 더함 */
	public void add(String authGrpTypCd, boolean excli, String authGrpId){
		// 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
		// 사용자 권한관리 상세 데이터(UserAuthMngDetlData) 에 사용자 정보 더함
		getUserAuthGrpDetl(authGrpTypCd, true).add(excli, authGrpId);
	}
	
	private UserAuthGrpDetl getUserAuthGrpDetl(String authGrpTypCd, boolean notEmpty){
		// 권한그룹구분코드 - G:사용자그룹, U:사용자권한그룹, A:관리자권한그룹, M:모바일권한그룹
		if("U".equals(authGrpTypCd) || "M".equals(authGrpTypCd)){
			if(userAuthDetl==null && notEmpty) userAuthDetl = new UserAuthGrpDetl();
			return userAuthDetl;
		} else if("A".equals(authGrpTypCd)){
			if(adminAuthDetl==null && notEmpty) adminAuthDetl = new UserAuthGrpDetl();
			return adminAuthDetl;
		} else if("G".equals(authGrpTypCd)){
			if(userGrpDetl==null && notEmpty) userGrpDetl = new UserAuthGrpDetl();
			return userGrpDetl;
		}
		return null;
	}
	
	/** 권한그룹구분코드 별, 제외대상여부 별, 권한그룹 목록 리턴 */
	public String[] getAuthGrps(String authGrpTypCd, boolean excli){
		UserAuthGrpDetl userAuthDetl = getUserAuthGrpDetl(authGrpTypCd, false);
		if(userAuthDetl==null) return null;
		return userAuthDetl.getAuthGrps(excli);
	}
	
	public void prepare(){
		UserAuthGrpDetl[] detls = new UserAuthGrpDetl[]{ userAuthDetl, adminAuthDetl, userGrpDetl };
		for(int i=0;i<detls.length;i++){
			if(detls[i]!=null){
				detls[i].prepare();
			}
		}
	}
	
	/** 스트링 변환 - 디버그용 */
	public String toString(){
		StringBuilder builder = new StringBuilder(256);
		String[] arr = ServerConfig.IS_MOBILE ? new String[]{"M", "A", "G"} : new String[]{"U", "A", "G"};
		UserAuthGrpDetl[] detls = new UserAuthGrpDetl[]{ userAuthDetl, adminAuthDetl, userGrpDetl };
		
		for(int i=0;i<detls.length;i++){
			if(detls[i]!=null){
				detls[i].appendTo(compId, arr[i], true,  builder);
				detls[i].appendTo(compId, arr[i], false, builder);
			}
		}
		String string = builder.toString();
		if(string.isEmpty()){
			return "No-UserAuthData";
		} else {
			return string;
		}
	}
}
