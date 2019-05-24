package com.innobiz.orange.web.pt.svc;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.utils.LimitedSizeMap;
import com.innobiz.orange.web.pt.utils.PtConstant;

/**
 * 캐쉬 서비스
 * <pre>
 * 사용법 : PtCmSvc.getCdList 함수 참조
 *   
 *   // 캐쉬에서 조회 후 데이터 있으면 해당 데이터 리턴
 *   List<PtCdBVo> list = (List<PtCdBVo>)ptCacheSvc.getCache(CacheConfig.CODE, langTypCd, clsCd, 200);
 *   if(list!=null) return list;
 *   >>>>>
 *       - CacheConfig.CODE : 캐쉬 카테고리로 CacheConfig에 static 변수로 정의 하고 사용함
 *       - langTypCd : 언어코드
 *       - clsCd : 실제 캐쉬의 구분 카테고리
 *       - 200 : CacheConfig.CODE 로 지정된 캐쉬는 언어별로 200 개 까지 저장됨, 넘치면 먼저 저장된것 삭제됨
 *         >> 갯수는 초기화 캐쉬를 담을 그릇이 초기화 안되었을 경우 해당 갯수 만큼을 담을 수 있도록 초기화 하기 위한 것
 * 
 *   // .... 해당 데이터를 조회함
 *   list = (List<PtCdBVo>)commonDao.queryList(ptCdBVo);
 * 
 *   // 해당 데이터를 케쉬에 저장함
 *   ptCacheSvc.setCache(CacheConfig.CODE, langTypCd, clsCd, list);
 * </pre>
 * */
@Service
public class PtCacheSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtCacheSvc.class);
	
	/** LimitedSizeMap 을 보관할 Map */
	private HashMap<String, HashMap<String, LimitedSizeMap<String, Object>>> cacheMap = new HashMap<String, HashMap<String, LimitedSizeMap<String, Object>>>();
	
	/** 캐쉬를 저장함 */
	public void setCache(String cacheCatId, String langTypCd, String cacheId, Object value){
		HashMap<String, LimitedSizeMap<String, Object>> langMap = getLangMap(cacheCatId);
		LimitedSizeMap<String, Object> limitedSizeMap = langMap.get(langTypCd==null ? "ko" : langTypCd);
		if(limitedSizeMap==null){
			if(cacheId.equals(PtConstant.PT_SYS_HALT)){
				limitedSizeMap = new LimitedSizeMap<String, Object>(50);
				langMap.put(langTypCd, limitedSizeMap);
				limitedSizeMap.put(cacheId, value);
			} else {
				LOGGER.error("PtCacheSvc.getCache(cacheCatId, cacheId, langTypCd, maxCacheSize) is required !");
			}
		} else {
			limitedSizeMap.put(cacheId, value);
		}
	}
	
	/** 캐쉬를 조회함, 캐쉬를 담을 LimitedSizeMap 이 없으면 최대 캐쉬수를 설정하여 생성함  */
	public Object getCache(String cacheCatId, String langTypCd, String cacheId, int maxCacheSize){
		HashMap<String, LimitedSizeMap<String, Object>> langMap = getLangMap(cacheCatId);
		String langCd = langTypCd==null ? "ko" : langTypCd;
		LimitedSizeMap<String, Object> limitedSizeMap = langMap.get(langCd);
		if(limitedSizeMap==null){
			limitedSizeMap = new LimitedSizeMap<String, Object>(maxCacheSize);
			langMap.put(langCd, limitedSizeMap);
			return null;
		}
		return limitedSizeMap.get(cacheId);
	}
	
	/** 해당 카테고리의 캐쉬아이디에 해당하는 캐쉬를 지움  */
	public void removeCache(String cacheCatId, String langTypCd, String cacheId){
		HashMap<String, LimitedSizeMap<String, Object>> langMap = getLangMap(cacheCatId);
		LimitedSizeMap<String, Object> limitedSizeMap = langMap.get(langTypCd==null ? "ko" : langTypCd);
		if(limitedSizeMap!=null){
			limitedSizeMap.remove(cacheId);
		}
	}
	
	/** 해당 카테고리의 캐쉬를 모두 지움  */
	public void removeAll(String cacheCatId){
		HashMap<String, LimitedSizeMap<String, Object>> langMap = cacheMap.get(cacheCatId);
		if(langMap!=null){
			langMap = new HashMap<String, LimitedSizeMap<String, Object>>();
			cacheMap.put(cacheCatId, langMap);
		}
	}
	
	/** 언어별 캐쉬맵 리턴 */
	private HashMap<String, LimitedSizeMap<String, Object>> getLangMap(String cacheCatId){
		HashMap<String, LimitedSizeMap<String, Object>> langMap = cacheMap.get(cacheCatId);
		if(langMap==null){
			langMap = new HashMap<String, LimitedSizeMap<String, Object>>();
			cacheMap.put(cacheCatId, langMap);
		}
		return langMap;
	}
}
