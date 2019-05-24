package com.innobiz.orange.web.em.preview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 미리보기 권한 서비스 */
@Service
public class PreviewAuthSvc {

	/** 미리보기 유효 밀리 세컨드 - 5분 */
	private static long ALLOW_TIME_GAP = 1000 * 60 * 5;
	
	/** 권한정보를 저장할 맵 */
	HashMap<PreviewAuth, Long> authMap = new HashMap<PreviewAuth, Long>();
	
	/** 권한 있는지 여부 */
	public boolean hasAuth(HttpServletRequest request){
		
		// 사용자 세션
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null) return false;
		
		// 호출 경로로 부터 fileId를 추출
		String fileId = null;
		String uri = request.getRequestURI();
		int p = uri.indexOf("/F");
		int q = p>0 ? uri.indexOf('.', p+2) : 0;
		if(p>0 && q>0){
			fileId = uri.substring(p+1, q);
		} else {
			// fileId 가 없는 경로면
			return false;
		}
		
		
		PreviewAuth previewAuth = new PreviewAuth(userVo.getUserUid(), fileId);
		synchronized (authMap) {
			Long createTime = authMap.get(previewAuth);
			if(createTime==null) return false;
			
			if(System.currentTimeMillis() - createTime > ALLOW_TIME_GAP){
				authMap.remove(previewAuth);
				return false;
			}
			
			return true;
		}
		
	}
	
	/** 권한을 세팅함 */
	public void setAuth(String userUid, String fileId){
		synchronized (authMap) {
			authMap.put(new PreviewAuth(userUid, fileId), System.currentTimeMillis());
		}
	}
	
	/** 경과시간 지난 것을 제거함 */
	@Scheduled(fixedDelay=60000)
	public void removeExpired(){
		
		if(authMap.isEmpty()) return;
		
		long standardTime = System.currentTimeMillis() - ALLOW_TIME_GAP;
		ArrayList<PreviewAuth> removalList = new ArrayList<PreviewAuth>();
		
		synchronized (authMap) {
			
			// 만기된 것[5분(ALLOW_TIME_GAP)이 지난 것]을 removalList 에 모음 
			Iterator<Entry<PreviewAuth, Long>> iterator = authMap.entrySet().iterator();
			Entry<PreviewAuth, Long> entry;
			while(iterator.hasNext()){
				entry = iterator.next();
				if(entry.getValue() < standardTime){
					removalList.add(entry.getKey());
				}
			}
			
			// 만기된 것을 제거
			if(!removalList.isEmpty()){
				for(PreviewAuth toRemove : removalList){
					authMap.remove(toRemove);
				}
			}
		}
	}
	
}
