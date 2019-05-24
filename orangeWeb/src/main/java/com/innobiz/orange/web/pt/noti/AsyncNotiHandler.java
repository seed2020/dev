package com.innobiz.orange.web.pt.noti;

/** 비동기 알림큐 에서 받은 데이터가 정확한지 확인하는 큐 */
public interface AsyncNotiHandler {
	
	public boolean handle(String result);
	
}
