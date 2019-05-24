package com.innobiz.orange.web.pt.secu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.pt.utils.PtConstant;

/** URL과 파라미터를 기반으로 어떤 메뉴가 호출되었는지를 찾는 객체 */
public class AuthUrlMapper {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(AuthUrlMapper.class);
	
	/** URL 별  AuthUrlData 배열 맵 - 동일 URL 경로가 여러개 메뉴레이아웃에 등록 된 경우 */
	private HashMap<String, AuthUrlMetaData[]> authUrlMetaDataArrByUrlMap = new HashMap<String, AuthUrlMetaData[]>();
	
	/** menuId(파라미터명:menuId) 별  AuthUrlData 맵 */
	private HashMap<Integer, AuthUrlMetaData> authUrlMetaDataByCombIdMap = new HashMap<Integer, AuthUrlMetaData>();

	/** mnuId(메뉴ID-메뉴테이블 키) 별  AuthUrlData 맵 */
	private HashMap<Integer, AuthUrlMetaData> authUrlMetaDataByMnuIdMap = new HashMap<Integer, AuthUrlMetaData>();
	
	/** 파라미터 인코딩 캐렉터셋 */
	private String encCharset = "UTF-8";
	
	/** 생성자 */
	public AuthUrlMapper(){}
	
	/** 생성자 */
	public AuthUrlMapper(String encCharset){ this.encCharset = encCharset; }
	
	/**
	 * 호출 경로에 해당하는 AuthUrlData 조회
	 * 
	 * */
	public AuthUrlMetaData getAuthUrlMetaData(HttpServletRequest request, String uri, String menuId, char loutCatId,
			AuthCdDecider authCdDecider, String[] usrAuthGrpIds, String[] admAuthGrpIds){
		
		// 메뉴레이아웃조합:PtMnuLoutCombDVo 의 mnuLoutCombId 에 해당
		//String menuId = request.getParameter("menuId");//menuId(파라미터명:menuId)
		
		// URI 이 여러개 존재 (URI : URL에서 파라미터 제거한것)
		//  - 1. 파라미터 일치 중 권한 있는 것
		//		- 1. menuId 가 일치
		//		- 2. 첫번째 꺼
		//  - 2. menuId 가 일치하는 것
		AuthUrlMetaData[] authUrlMetaDataArr = authUrlMetaDataArrByUrlMap.get(uri);
		AuthUrlMetaData authUrlMetaData;
		if(authUrlMetaDataArr!=null){
			int len = authUrlMetaDataArr.length;
			// 첫번째 파라미터가 일치하는 url
			AuthUrlMetaData mathcedAuthUrlMetaData = null;
			// menuId(파라미터명:menuId) 가 일치하는 것이 없을때 - 파라미터 프로세싱 - 1순위
			for(int i=0;i<len;i++){
				if(authUrlMetaDataArr[i] == null) continue;
				
				if(loutCatId != authUrlMetaDataArr[i].getLoutCatId()
						&& authUrlMetaDataArr[i].getLoutCatId()!='P') continue;
				
				if(authUrlMetaDataArr[i].isParamMatched(request)){
					authUrlMetaData = createAuthUrlMetaData(authUrlMetaDataArr[i], authCdDecider, usrAuthGrpIds, admAuthGrpIds);
					
					if(authUrlMetaData!=null){
						// menuId(파라미터명:menuId) 가 일치하는 것 일치하면
						if(menuId!=null && menuId.equals(authUrlMetaDataArr[i].getCombId())){
							return authUrlMetaData;
						} else if(mathcedAuthUrlMetaData==null){
							mathcedAuthUrlMetaData = authUrlMetaData;
						}
					}
				}
			}
			if(mathcedAuthUrlMetaData!=null){
				return mathcedAuthUrlMetaData;
			}
			
			// - 파라미터 맞지 않는 것 제거함
//			// menuId(파라미터명:menuId) 가 일치하는 것 - 2순위
//			if(menuId!=null && !menuId.isEmpty()){
//				for(int i=0;i<len;i++){
//					if(authUrlMetaDataArr[i] == null) continue;
//					if(loutCatId != authUrlMetaDataArr[i].getLoutCatId() && authUrlMetaDataArr[i].getLoutCatId()!='P') continue;
//					
//					if(menuId.equals(authUrlMetaDataArr[i].getCombId())){
//						return createAuthUrlMetaData(authUrlMetaDataArr[i], authCdDecider, usrAuthGrpIds, admAuthGrpIds);
//					}
//				}
//			}
		}
		
		// 이하 : URL 메뉴로 등록 되지 않은 경우
		
		// menuId(파라미터명:menuId) 없으면
		if(menuId==null || menuId.isEmpty()) return null;
		
		// menuId(파라미터명:menuId) 에 해당하는 데이터 중 같은 폴더의 경로만 리턴함 
		authUrlMetaData = authUrlMetaDataByCombIdMap.get(Hash.hashId(menuId));
		if(authUrlMetaData == null) return null;
		
		if(authUrlMetaData.isPathMapthed(uri) && authUrlMetaData.isParamMatched(request)){
			return createAuthUrlMetaData(authUrlMetaData, authCdDecider, usrAuthGrpIds, admAuthGrpIds);
		}
		
		return null;
	}
	/** URL 기반으로 menuId 파라미터 구하기 */
	public String getMenuIdByUrl(String authCheckUrl, UserVo userVo, AuthCdDecider authCdDecider){
		
		// uri 분리
		int p = authCheckUrl.indexOf('?');
		String uri = p<0 ? authCheckUrl : authCheckUrl.substring(0, p);
		
		// 파라미터 맵
		Map<String, String> paramMap = null;
		if(p>0){
			paramMap = new HashMap<String, String>();
			for(String param : authCheckUrl.substring(p+1).split("&")){
				if((p = param.indexOf('='))>0){
					try {
						paramMap.put(param.substring(0,p), URLDecoder.decode(param.substring(p+1), "UTF-8"));
					} catch (UnsupportedEncodingException ignore) {}
				}
			}
		}
		
		String[] admAuthGrpIds = userVo.getAdminAuthGrpIds();
		String[] usrAuthGrpIds = userVo.getUserAuthGrpIds();
		
		char userLoutCatId = userVo.getLoutCatId().isEmpty() ? 0 : userVo.getLoutCatId().charAt(0);
		
		// URI 이 여러개 존재 (URI : URL에서 파라미터 제거한것)
		//  - 1. 파라미터 일치 중 권한 있는 것
		//  - 2. 첫번째 꺼
		AuthUrlMetaData[] authUrlMetaDataArr = authUrlMetaDataArrByUrlMap.get(uri);
		AuthUrlMetaData firstMetaData = null;
		if(authUrlMetaDataArr!=null){
			int len = authUrlMetaDataArr.length;
			char loutCatId;
			
			// 파라미터가 일치하는 것
			for(int i=0;i<len;i++){
				if(authUrlMetaDataArr[i]==null) continue;
				
				loutCatId = authUrlMetaDataArr[i].getLoutCatId();
				if((loutCatId=='B' || loutCatId=='I') && userLoutCatId != loutCatId) continue;
				
				if(isParamMatched(paramMap, authUrlMetaDataArr[i].getParams())){
					if(authCdDecider==null) return authUrlMetaDataArr[i].getCombId();
					if(firstMetaData==null) firstMetaData = authUrlMetaDataArr[i];
					// 권한코드 조회
					String authCd = authCdDecider.getAdmAuthCd(authUrlMetaDataArr[i].getMnuPltId(),
							authUrlMetaDataArr[i].isAdmMnu() ? admAuthGrpIds : usrAuthGrpIds);
					if(authCd!=null) return authUrlMetaDataArr[i].getCombId();
				}
			}
			// 첫번째 것
			if(firstMetaData!=null) return firstMetaData.getCombId();
		}
		
		// 이하 : URL 메뉴로 등록 되지 않은 경우
		return null;
	}
	
	/** mnuId(메뉴ID-메뉴테이블 키) 별  AuthUrlData 리턴 */
	public AuthUrlMetaData getAuthUrlMetaDataByMnuId(String mnuId){
		return authUrlMetaDataByMnuIdMap.get(Hash.hashId(mnuId));
	}
	
	/** 권한있는 권한메타데이터 생성 리턴 - 기존의 데이터를 복사 후 권한정보 세팅하여 리턴함  */
	private AuthUrlMetaData createAuthUrlMetaData(AuthUrlMetaData authUrlMetaData, AuthCdDecider authCdDecider,
			String[] usrAuthGrpIds, String[] admAuthGrpIds){
		
		if(authCdDecider==null){
			if(ArrayUtil.isInArray(admAuthGrpIds, PtConstant.AUTH_SYS_ADMIN)){
				return authUrlMetaData.copyWithAuthCd("SYS");
			} else if(ArrayUtil.isInArray(admAuthGrpIds, PtConstant.AUTH_ADMIN)){
				return authUrlMetaData.copyWithAuthCd("S");
			}
		}
		
		if(authUrlMetaData.isAdmMnu()){
			// 권한코드 조회
			String authCd = authCdDecider.getAdmAuthCd(authUrlMetaData.getMnuPltId(), admAuthGrpIds);
			if(authCd==null) return null;
			return authUrlMetaData.copyWithAuthCd(authCd);
		} else {
			// 권한코드 조회
			String authCd = authCdDecider.getUsrAuthCd(authUrlMetaData.getMnuPltId(), usrAuthGrpIds);
			if(authCd==null) return null;
			return authUrlMetaData.copyWithAuthCd(authCd);
		}
	}
	
	/** 파라미터가 같은지 검사 */
	private boolean isParamMatched(Map<String, String> paramMap, String[][] params){
		if(params==null || params.length==0) return true;
		String value;
		for(int i=0; i<params.length; i++){
			if(params[i][0]!=null){
				value = paramMap==null ? null : paramMap.get(params[i][0]);
				if(value==null || !value.equals(params[i][1])) return false;
			}
		}
		return true;
	}
	
	/** 레이아웃ID, 메뉴ID, URL을 더함 */
	public void add(String loutId, String menuId, String mnuPltId, char loutCatId, String url, boolean isAdmMnu) {
		if(url==null) return;
		if(url.startsWith("http:") || url.startsWith("https:")) return;
		
		int p = url.indexOf('?');
		String uri = p<0 ? url : url.substring(0,p);
		String[][] params = p<0 ? null : toParams(url.substring(p+1), menuId);
		
		// "/index.do"로 끝나는 것은 디렉토리경로(index.do 제거)도 같이 권한정보에 세팅함
		if(uri.endsWith("/index.do")){
			add(loutId, menuId, mnuPltId, loutCatId, uri.substring(0, uri.length()-8)+(params==null ? "" : url.substring(p)), isAdmMnu);
		}
		
		AuthUrlMetaData[] oldMetaArr, metaArr;
		AuthUrlMetaData meta = new AuthUrlMetaData(loutId, menuId, mnuPltId, loutCatId, uri, params, isAdmMnu);
		
		// 파라미터:menuId 별  AuthUrlData 맵 에 더함
		authUrlMetaDataByCombIdMap.put(Hash.hashId(menuId), meta);
		
		// 메뉴ID별 AuthUrlData 맵 - 나의메뉴 체크용
		authUrlMetaDataByMnuIdMap.put(Hash.hashId(mnuPltId), meta);
		
		// URL 배열 맵에 없으면
		if((oldMetaArr = authUrlMetaDataArrByUrlMap.get(uri)) == null){
			authUrlMetaDataArrByUrlMap.put(uri, new AuthUrlMetaData[]{ meta, null });
		} else {
			// 여분자리 체크 후 비어 있으면 넣음
			if(oldMetaArr[oldMetaArr.length -1] == null){
				oldMetaArr[oldMetaArr.length -1] = meta;
			} else {
				// 2자리 여분자리를 더 만든 배열 옮긴 후 해당 배열을 맵에 저장
				metaArr = new AuthUrlMetaData[oldMetaArr.length+2];
				System.arraycopy(oldMetaArr, 0, metaArr, 0, oldMetaArr.length);
				metaArr[oldMetaArr.length] = meta;
				authUrlMetaDataArrByUrlMap.put(uri, metaArr);
			}
		}
	}
	
	/** 파라미터 프로세싱 - URL 파라미터를 key, value 나누어서 이차원 String 배열에 담음 */
	private String[][] toParams(String param, String menuId) {
		if(param==null || param.isEmpty()) return null;
		StringTokenizer st = new StringTokenizer(param, "&");
		List<String[]> list = new ArrayList<String[]>();
		int i, p, size;
		String token, paramName;
		while(st.hasMoreElements()){
			token = st.nextToken();
			if((p = token.indexOf('='))>0){
				try {
					paramName = token.substring(0, p);
					if("menuId".equals(paramName)) continue;
					list.add(new String[]{ paramName, URLDecoder.decode(token.substring(p+1), encCharset) });
				} catch (UnsupportedEncodingException e) {
					LOGGER.error("메뉴의 파라미터 세팅은 ["+encCharset+"]로 URL 인코딩 되어야 합니다. - menuId : "+menuId);
					e.printStackTrace();
				}
			}
		}
		size = list.size();
		if(size==0) return null;
		String[][] params = new String[size][];
		for(i=0;i<size;i++) params[i] = list.get(i);
		return params;
	}
	
}
