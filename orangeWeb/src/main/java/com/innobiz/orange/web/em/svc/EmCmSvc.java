package com.innobiz.orange.web.em.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.utils.EmConstant;
import com.innobiz.orange.web.em.vo.EmAdrCommVo;
import com.innobiz.orange.web.em.vo.EmUserSetupDVo;
import com.innobiz.orange.web.pt.secu.LastSessionChecker;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;

@Service
public class EmCmSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmCmSvc.class);
	
	private final String ADR_CACHE = "ADR_CACHE";
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 시도,시군구 맵 리턴(캐쉬) */
	private Map<Integer,List<EmAdrCommVo>> getSidoMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer,List<EmAdrCommVo>> sidoMap = (Map<Integer,List<EmAdrCommVo>>)
				ptCacheSvc.getCache(ADR_CACHE, langTypCd, "MAP", 10);
		if(sidoMap!=null) return sidoMap;
		
		sidoMap = new HashMap<Integer,List<EmAdrCommVo>>();
		loadSidoMap(sidoMap);
		ptCacheSvc.setCache(ADR_CACHE, langTypCd, "MAP",  sidoMap);
		return sidoMap;
	}
	
	/** 전체 시도,시군구 조회 맵 */
	@SuppressWarnings("unchecked")
	public void loadSidoMap(Map<Integer,List<EmAdrCommVo>> map) throws SQLException{
		List<EmAdrCommVo> list = new ArrayList<EmAdrCommVo>();
		EmAdrCommVo searchVO = new EmAdrCommVo();
		searchVO.setInstanceQueryId("com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.selectEmAdrBldInfoBSido");
		
		int hashId = Hash.hashId("SIDO");
		
		// 시도 조회
		list = (List<EmAdrCommVo>)commonDao.queryList(searchVO);
		if(list != null && list.size()>0) map.put(hashId, list);
		
		// 시군구 조회
		searchVO.setInstanceQueryId("com.innobiz.orange.web.em.dao.EmAdrBldInfoBDao.selectEmAdrBldInfoBGugun");
		list = (List<EmAdrCommVo>)commonDao.queryList(searchVO);
		if(list != null && list.size()>0){
			Map<Integer,List<EmAdrCommVo>> chkMap = new HashMap<Integer,List<EmAdrCommVo> >();
			for(EmAdrCommVo commVo : list){
				if(commVo.getValue() == null || commVo.getValue().isEmpty() || commVo.getValue().length() < 5) continue;
				hashId = Hash.hashId(commVo.getValue().substring(0,2));
				if(!chkMap.containsKey(hashId)) chkMap.put(hashId, new ArrayList<EmAdrCommVo>());
				chkMap.get(hashId).add(commVo);
			}
			Entry<Integer, List<EmAdrCommVo>> entry;
			if(chkMap.size()>0){
				Iterator<Entry<Integer, List<EmAdrCommVo>>> iterator = chkMap.entrySet().iterator();
				while(iterator.hasNext()){
					entry = iterator.next();
					map.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		if(map.size() == 0) LOGGER.error("sido is size 0!!");
	}
	
	/** 시도, 시군구 조회 */
	public List<EmAdrCommVo> getSidoList(String key, String langTypCd) throws SQLException{
		Map<Integer,List<EmAdrCommVo>> sidoMap = getSidoMap(langTypCd);
		return sidoMap.get(Hash.hashId(key));
	}
	
	/** 사용자 설정상세 맵 조회 */
	public Map<String, String> getUserSetupMap(String userUid, String setupClsId, boolean useCache) throws SQLException{
		if(useCache){
			@SuppressWarnings("unchecked")
			Map<String, String> map = (Map<String, String>)ptCacheSvc.getCache(EmConstant.USER_SETUP+"_"+userUid.toUpperCase(), "ko", setupClsId, 600);
			if(map!=null) return map;
		}
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		// 시스템설정상세(PT_SYS_SETUP_D) 테이블
		EmUserSetupDVo emUserSetupDVo = new EmUserSetupDVo();
		emUserSetupDVo.setUserUid(userUid);
		emUserSetupDVo.setSetupClsId(setupClsId);
		
		@SuppressWarnings("unchecked")
		List<EmUserSetupDVo> emUserSetupDVoList = (List<EmUserSetupDVo>)commonDao.queryList(emUserSetupDVo);
		
		emUserSetupDVo = null;
		if(emUserSetupDVoList!=null){
			for(EmUserSetupDVo storedEmUserSetupDVo: emUserSetupDVoList){
				if(emUserSetupDVo==null) emUserSetupDVo = storedEmUserSetupDVo;
				returnMap.put(storedEmUserSetupDVo.getSetupId(), storedEmUserSetupDVo.getSetupVa());
			}
		}
		// 중복로그인 방지 - 방지가 아니면, 클리어함
		if(setupClsId.equals(PtConstant.PT_SYS_PLOC)){
			if(!"Y".equals(returnMap.get("blockDupLogin"))){
				LastSessionChecker.clearLastSession();
			}
		}
		if(useCache){
			ptCacheSvc.setCache(EmConstant.USER_SETUP+"_"+userUid.toUpperCase(), "ko", setupClsId, returnMap);
		}
		
		return returnMap;
	}
	
	/** 사용자 설정 저장 내용을 QueryQueue에 저장 - Map */
	public void setUserSetupMap(String userUid, String setupClsId, QueryQueue queryQueue, boolean withoutPrefix, Map<String, String> paramMap, boolean isNull) throws SQLException{
		if(paramMap==null || paramMap.size()==0) return;
				
		String key;
		int prefixLen = setupClsId==null ? 0 : setupClsId.length()+1;
		
		boolean deletePrevious = false;
		EmUserSetupDVo emUserSetupDVo;
		String setupId;
		
		// set header
		Entry<String, String> entry;
		Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();
				
		while(iterator.hasNext()){
			entry = iterator.next();
			key = entry.getKey();
			if(key!=null && (withoutPrefix || key.startsWith(setupClsId)) && !key.endsWith("SortOrdr")){
				
				if(!deletePrevious){
					// 사용자설정상세(EM_USER_SETUP_D) 테이블 - 기존 데이터 삭제
					emUserSetupDVo = new EmUserSetupDVo();
					emUserSetupDVo.setUserUid(userUid);
					emUserSetupDVo.setSetupClsId(setupClsId);
					queryQueue.delete(emUserSetupDVo);
					deletePrevious = true;
				}
				
				setupId = withoutPrefix ? key : key.substring(prefixLen);
				// 언더바(_)로 시작하면 저장 안함
				if(setupId.isEmpty() || setupId.charAt(0)=='_') continue;
				if(withoutPrefix && "menuId".equals(setupId)) continue;
				if(!isNull && (entry.getValue()==null || entry.getValue().isEmpty())) continue;
				// 사용자설정상세(EM_USER_SETUP_D) 테이블 - INSERT
				emUserSetupDVo = new EmUserSetupDVo();
				emUserSetupDVo.setUserUid(userUid);
				emUserSetupDVo.setSetupClsId(setupClsId);
				emUserSetupDVo.setSetupId(setupId);
				emUserSetupDVo.setSetupVa(entry.getValue());
				queryQueue.insert(emUserSetupDVo);
			}
		}
	}
	
	/** 사용자 설정 저장 내용을 QueryQueue에 저장 */
	public void setUserSetup(String userUid, String setupClsId, QueryQueue queryQueue, boolean withoutPrefix, HttpServletRequest request) throws SQLException{
		
		Enumeration<String> enums = request.getParameterNames();
		String key;//, currDt = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		int prefixLen = setupClsId==null ? 0 : setupClsId.length()+1;
		
		boolean deletePrevious = false;
		EmUserSetupDVo emUserSetupDVo;
		String setupId;
		
		while(enums.hasMoreElements()){
			key = enums.nextElement();
			if(key!=null && (withoutPrefix || key.startsWith(setupClsId)) && !key.endsWith("SortOrdr")){
				
				if(!deletePrevious){
					// 사용자설정상세(EM_USER_SETUP_D) 테이블 - 기존 데이터 삭제
					emUserSetupDVo = new EmUserSetupDVo();
					emUserSetupDVo.setUserUid(userUid);
					emUserSetupDVo.setSetupClsId(setupClsId);
					queryQueue.delete(emUserSetupDVo);
					deletePrevious = true;
				}
				
				setupId = withoutPrefix ? key : key.substring(prefixLen);
				// 언더바(_)로 시작하면 저장 안함
				if(setupId.isEmpty() || setupId.charAt(0)=='_') continue;
				if(withoutPrefix && "menuId".equals(setupId)) continue;
				
				// 사용자설정상세(EM_USER_SETUP_D) 테이블 - INSERT
				emUserSetupDVo = new EmUserSetupDVo();
				emUserSetupDVo.setUserUid(userUid);
				emUserSetupDVo.setSetupClsId(setupClsId);
				emUserSetupDVo.setSetupId(setupId);
				emUserSetupDVo.setSetupVa(request.getParameter(key));
				queryQueue.insert(emUserSetupDVo);
			}
		}
	}
	
}
