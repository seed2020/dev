package com.innobiz.orange.web.pt.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.sync.OrDbSyncUtil;
import com.innobiz.orange.web.pt.vo.PtCacheTVo;

/** 캐쉬 만료 처리용 서비스 */
@Service
public class PtCacheExpireSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtCacheExpireSvc.class);
	
	/** 전체 데이터 */
	private static final String ALL_DATA = "[ALL]";
	
	/** 맥스 체크대기 횟수 - 인터벌은 스프링 설정에 의해 0.5초 간격임 */
	private static int maxWaitingCount = 20;
	
	/** 체크대기 횟수 */
	private int waitingCount = 0;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 마지막 점검 시간 */
	private static String lastCheckTime;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 하위의 모든 캐쉬를 만료 시킴 */
	public void expireAll(QueryQueue queryQueue, String dbTime, String ... cacheCatIds){
		int i, size = cacheCatIds==null ? 0 : cacheCatIds.length;
		for(i=0;i<size;i++){
			expire(queryQueue, dbTime, cacheCatIds[i], ALL_DATA, "ko");
		}
	}
	
//	/** 캐쉬를 만료 시킴 */
//	public void expire(QueryQueue queryQueue, String cacheCatId, String cacheId, List<PtCdBVo> langCdList){
//		int i, size = langCdList==null ? 0 : langCdList.size();
//		if(size==0) expireAll(queryQueue, cacheCatId);
//		else {
//			for(i=0;i<size;i++){
//				expire(cacheCatId, cacheId, langCdList.get(i).getCd(), queryQueue);
//			}
//		}
//	}
	
	/** 캐쉬를 만료 시킴 */
	public void expire(QueryQueue queryQueue, String dbTime, String cacheCatId, String cacheId, String langTypCd){
		PtCacheTVo ptCacheTVo = new PtCacheTVo();
		ptCacheTVo.setCacheCatId(cacheCatId);
		ptCacheTVo.setLangTypCd(langTypCd);
		ptCacheTVo.setCacheId(cacheId);
		ptCacheTVo.setModDt(dbTime);
		if(queryQueue!=null){
			queryQueue.store(ptCacheTVo);
		} else {
			try {
				if(commonDao.update(ptCacheTVo)==0){
					commonDao.insert(ptCacheTVo);
				}
			} catch (SQLException e) {
				LOGGER.error(e);
			}
		}
	}
	
	/** 캐쉬변경여부 바로 체크하도록 설정 */
	public void checkNow(String ... cacheCatIds){
		int i, size = cacheCatIds==null ? 0 : cacheCatIds.length;
		for(i=0;i<size;i++){
			ptCacheSvc.removeAll(cacheCatIds[i]);
		}
		waitingCount = maxWaitingCount;
	}
	
	/** 캐쉬의 만료 여부를 검사함 - 0.5초 간격으로 체크하며, 대기 회수를 체크하여 10초에 1번씩 체크하도록 함 */
	@Scheduled(fixedDelay=500)
	public synchronized void checkExpired(){
		
		// 마지막 체크 시간
		if(lastCheckTime==null){
			try {
				// DB 시간을 가져옴
				lastCheckTime = commonDao.querySysdate(null);
				orCmSvc.setUsers();
				ptSysSvc.setIpChecker();
			} catch (SQLException e) { LOGGER.error(e); }
		
		// 대기초
		} else if(waitingCount < maxWaitingCount){
			waitingCount++;
			
		// DB 조회해서 만료된 데이터가 있는지 점검함
		} else {
			
			waitingCount = 0;
			// 권한유효기간 경과 여부
			boolean authExpired = false, hasExpire = false, userExpired = false, agntExpired = false, sysExpired = false;
			boolean orDbSyncExpired = false;
			
			try {
				PtCacheTVo ptCacheTVo = new PtCacheTVo();
				ptCacheTVo.setModDt(lastCheckTime);
				ptCacheTVo.setOrderBy("MOD_DT, CACHE_CAT_ID, CACHE_ID");
				
				@SuppressWarnings("unchecked")
				List<PtCacheTVo> list = (List<PtCacheTVo>)commonDao.queryList(ptCacheTVo);
				
				String cacheCatId, langTypCd, cacheId;
				int i, size = list==null ? 0 : list.size();
				for(i=0;i<size;i++){
					ptCacheTVo = list.get(i);
					cacheCatId	= ptCacheTVo.getCacheCatId();
					langTypCd	= ptCacheTVo.getLangTypCd();
					cacheId		= ptCacheTVo.getCacheId();
					if(ALL_DATA.equals(cacheId)){
						ptCacheSvc.removeAll(cacheCatId);
					} else {
						ptCacheSvc.removeCache(cacheCatId, langTypCd, cacheId);
					}
					
					if(!authExpired){
						if(cacheCatId.equals(CacheConfig.AUTH) || cacheCatId.equals(CacheConfig.ORG) || cacheCatId.equals(CacheConfig.USER)){
							authExpired = true;
						}
					}
					
					if(!userExpired){
						if(cacheCatId.equals(CacheConfig.USER)){
							userExpired = true;
						}
					}
					
					if(!agntExpired){
						if(cacheCatId.equals(CacheConfig.AP_AGNT)){
							agntExpired = true;
						}
					}
					
					if(!sysExpired){
						if(cacheCatId.equals(CacheConfig.SYS_SETUP)){
							sysExpired = true;
						}
					}
					
					if(!orDbSyncExpired){
						if(cacheCatId.equals(CacheConfig.OR_DB_SYNC)){
							orDbSyncExpired = true;
						}
					}
					
					hasExpire = true;
				}
				
				if(authExpired){
					SessionUtil.expireSessionAuth();
				}
				
				if(agntExpired){
					SessionUtil.expireApAgnt();
				}
				
				if(userExpired){
					try{
						orCmSvc.setUsers();
					} catch(Exception ignore){}
				}
				
				if(sysExpired){
					try{
						ptSysSvc.setIpChecker();
					} catch(Exception ignore){}
				}
				
				if(hasExpire){
					lastCheckTime = commonDao.querySysdate(null);
					waitingCount = 19;
				}
				
				if(orDbSyncExpired){
					OrDbSyncUtil.setExpiredTime();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/** DB의 시간을 조회함 */
	public String getDbTime() throws SQLException{
		return commonDao.querySysdate(null);
	}
	
	/** 결재 대리인설정 만료 시킴 (0시1분, 12시1분) - 참조 : cron = "분 시 월 요일 년도" */
	@Scheduled(cron = "1 0,12 * * * *")
	public void refreshApAgent(){
		SessionUtil.expireApAgnt();
	}
}
