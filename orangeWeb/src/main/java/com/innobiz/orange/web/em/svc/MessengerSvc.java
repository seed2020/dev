package com.innobiz.orange.web.em.svc;

public interface MessengerSvc {
	
	/** msgKey Naming Rule
	 *  포탈에서 해당 메시지를 읽었을때 메신저 알림에서도 동일하게 처리하기 위한 키
	 *  엔터티 상세그룹약어_업무별PK조합
	 * */
	
	/** 알림발송[멀티] 
	 * param => recvIds : 수신자 userUid(String 배열) , 이하 동일  
	 * */
	public String sendNotice(String[] recvUids, String sendUid, String catNm, String subj, String contents, String url, String msgKey) throws Exception;
	
	/** 알림발송[한명]
	 *  param => recvId : 수신자 userUid , sendId : 발신자 userUid , catNm : 업무명 , subj : 제목 , contents : 내용 , url : 링크 url , msgKey : 업무별 유니크키  
	 * */
	public String sendNotice(String recvUid, String sendUid, String catNm, String subj, String contents, String url, String msgKey) throws Exception;
	
	/** 서비스 중인지 */
	public boolean isInService();
	
	/** 제품명 or 회사명 리턴 */
	public String getProductName();
}
