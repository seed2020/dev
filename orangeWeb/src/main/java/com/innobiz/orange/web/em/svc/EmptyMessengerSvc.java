package com.innobiz.orange.web.em.svc;


/** 기능없는 메신저 - 스프링 삽입용 - 오류방지 */
public class EmptyMessengerSvc implements MessengerSvc {
	
	/** 알림발송 - 다건 */
	@Override
	public String sendNotice(String[] recvUids, String sendUid, String catNm,
			String subj, String contents, String url, String msgKey) {
		return null;
	}

	/** 알림발송 - 한건 */
	@Override
	public String sendNotice(String recvUid, String sendUid, String catNm,
			String subj, String contents, String url, String msgKey) {
		return null;
	}
	
	/** 서비스 중인지 */
	public boolean isInService(){
		return false;
	}
	
	/** 제품명 or 회사명 리턴 */
	public String getProductName(){
		return null;
	}
}
