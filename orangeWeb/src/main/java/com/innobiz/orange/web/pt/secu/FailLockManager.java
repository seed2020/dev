package com.innobiz.orange.web.pt.secu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 로그인 실패 장금 장치 */
@Component
public class FailLockManager {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(FailLockManager.class);
	/** 장금 해제 시간 - 30분 */
	private long LOCK_REASE_TIME = 30 * 60 * 1000;
	/** 장금 맵 */
	private HashMap<Integer, FailLockVo> lockedMap = new HashMap<Integer, FailLockVo>();
	/** 실패 맵 */ 
	private HashMap<Integer, FailLockVo> failedMap = new HashMap<Integer, FailLockVo>();
	/** 실패 추가 */
	public void addFail(String id, String ip){
		Integer hash = hashValues(id, ip);
		
		FailLockVo failed = lockedMap.get(hash);
		if(failed != null) return;
		
		failed = failedMap.get(hash);
		if(failed==null){
			failed = new FailLockVo(id, ip);
			failedMap.put(hash, failed);
		} else {
			if(failed.addCount()){
				failedMap.remove(hash);
				lockedMap.put(hash, failed);
				LOGGER.error(failed.toString("Login-Fail Lock:"));
			}
		}
	}
	/** 장금 여부 확인 */
	public boolean isLocked(String id, String ip){
		Integer hash = hashValues(id, ip);
		FailLockVo failed = lockedMap.get(hash);
		if(failed != null){
			LOGGER.error(failed.toString("Locked Attempt:"));
			return true;
		}
		return false;
	}
	/** 장금 해제 */
	@Scheduled(fixedDelay=60000)
	public void release(){
		Integer key;
		FailLockVo vo;
		long compare = System.currentTimeMillis() - LOCK_REASE_TIME;
		ArrayList<Integer> releaseList = new ArrayList<Integer>();
		synchronized (lockedMap) {
			Iterator<Integer> iterator = lockedMap.keySet().iterator();
			while(iterator.hasNext()){
				key = iterator.next();
				vo = lockedMap.get(key);
				if(vo.canRelease(compare)){
					releaseList.add(key);
				}
			}
			if(!releaseList.isEmpty()){
				for(Integer i : releaseList){
					lockedMap.remove(i);
				}
				releaseList.clear();
			}
		}
		synchronized (failedMap) {
			Iterator<Integer> iterator = failedMap.keySet().iterator();
			while(iterator.hasNext()){
				key = iterator.next();
				vo = failedMap.get(key);
				if(vo.canRelease(compare)){
					releaseList.add(key);
				}
			}
			if(!releaseList.isEmpty()){
				for(Integer i : releaseList){
					failedMap.remove(i);
				}
				releaseList.clear();
			}
		}
	}
	/** 해쉬 */
	private Integer hashValues(String id, String ip){
		if(ip==null || ip.isEmpty()) return CRC32.hash(id.getBytes());
		return CRC32.hash((id+ip).getBytes());
	}
	/** 잠김 정보 삭제 */
	public void clear(){
		lockedMap = new HashMap<Integer, FailLockVo>();
		failedMap = new HashMap<Integer, FailLockVo>();
	}
}
