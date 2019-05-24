package com.innobiz.orange.web.pt.secu;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 장금 정보 */
public class FailLockVo {
	/** 최대 실패 회수 */
	private static int MAX_FAIL_COUNT = 20;
	/** 마지막 실패 시간 */
	private long lastFailTime;
	/** 아이디 */
	private String id;
	/** 아이피 */
	private String ip;
	/** 실패 회수 */
	private int failCount = 1;
	/** 생성자 */
	public FailLockVo(String id, String ip) {
		this.id = id;
		this.ip = ip;
		this.lastFailTime = System.currentTimeMillis();
	}
	/** 실패회수 추가 */
	public boolean addCount(){
		this.failCount++;
		this.lastFailTime = System.currentTimeMillis();
		if(failCount >= MAX_FAIL_COUNT){
			return true;
		}
		return false;
	}
	/** 릴리즈 가능여부 점검 */
	public boolean canRelease(long compare){
		return lastFailTime < compare;
	}
	/** 포멧 - 로그용 */
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** toString */
	public String toString(String prefix) {
		return (prefix==null ? "FailLock" : prefix)
				+" [id=" + id + ", ip=" + ip + ", lastFailTime=" + format.format(new Date(lastFailTime)) + ", failCount=" + failCount + "]";
	}
}
