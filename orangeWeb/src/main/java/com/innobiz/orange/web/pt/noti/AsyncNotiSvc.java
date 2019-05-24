package com.innobiz.orange.web.pt.noti;

import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.ServerConfig;

/** 비동기 알림 서비스 - 결재-ERP 연계시 결재가 진행되면서 알림 사항을 http url 호출 방식으로 전달하기 위한것 */
@Service
public class AsyncNotiSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(AsyncNotiSvc.class);

	/** 알림큐 1 */
	private LinkedList<AsyncNotiData> queue1 = new LinkedList<AsyncNotiData>();
	/** 알림큐 2 */
	private LinkedList<AsyncNotiData> queue2 = new LinkedList<AsyncNotiData>();
	/** 알림큐 3 */
	private LinkedList<AsyncNotiData> queue3 = new LinkedList<AsyncNotiData>();
	
	/** 알림큐 1 실행 시간 */
	private long execTime1 = 0;
	/** 알림큐 2 실행 시간 */
	private long execTime2 = 0;
	/** 진행 중인지 여부 */
	private boolean inProcess = false;
	
	/** 비동기 알림큐 데이터를 더함 */
	public void add(AsyncNotiData asyncNotiData){
		queue1.add(asyncNotiData);
	}
	
	/** 알림 실행 */
	@Scheduled(fixedDelay=3000)
	public void send(){
		
		if(inProcess) return;
		inProcess = true;
		
		long currentTime = System.currentTimeMillis(), compareTime;
		if(currentTime - execTime2 > (1000 * 60 * 3)){
			execTime2 = currentTime;
			
			if(!queue3.isEmpty()){
				compareTime = currentTime - (1000 * 60 * 30);
				sendByQueue(queue3, null, currentTime, compareTime);
			}
		}
		
		
		if(currentTime - execTime1 > (1000 * 60)){
			execTime1 = currentTime;
			
			if(!queue2.isEmpty()){
				compareTime = currentTime - (1000 * 60 * 10);
				sendByQueue(queue2, queue3, currentTime, compareTime);
			}
		}
		
		if(!queue1.isEmpty()){
			compareTime = 0;
			sendByQueue(queue1, queue2, currentTime, compareTime);
		}
		
		inProcess = false;
	}
	
	/** 큐별 알림 실행 */
	private void sendByQueue(LinkedList<AsyncNotiData> sendQueue, LinkedList<AsyncNotiData> failQueue, 
			long currentTime, long compareTime){
		
		String result;
		AsyncNotiData data;
		int i, size = sendQueue.size();
		
		for(i=0;i<size;i++){
			data = sendQueue.removeFirst();
			if(data.getLastExecTime()==0 || data.getLastExecTime() < compareTime){
				
				try{
					result = data.send();
					if(data.getAsyncNotiHandler()!=null){
						if(!data.getAsyncNotiHandler().handle(result)){
							if(failQueue != null && data.isRetry()){
								data.setLastExecTime(currentTime);
								failQueue.add(data);
							}
						}
					}
					
				} catch(Exception e){
					Throwable root = toRootException(e);
					LOGGER.error("ERP Noti fail - "+root.getClass().getCanonicalName()+": "+root.getMessage()+"\n"+data.toString());
					if(failQueue != null && data.isRetry() && !ServerConfig.IS_LOC){
						data.setLastExecTime(currentTime);
						failQueue.add(data);
					}
				}
				
			} else {
				sendQueue.addLast(data);
			}
		}
		
	}

	/** 루트 Exception으로  */
	private Throwable toRootException(Throwable throwable){
		Throwable parent = null;
		while((parent = throwable.getCause()) != null){
			throwable = parent;
		}
		return throwable;
	}
}
