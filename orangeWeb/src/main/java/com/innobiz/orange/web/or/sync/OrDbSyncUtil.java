package com.innobiz.orange.web.or.sync;

import com.innobiz.orange.web.cm.config.CustConfig;

/**  */
public class OrDbSyncUtil {
	
	private static long expiredTime = 0;
	
	public static void setExpiredTime(){
		expiredTime = System.currentTimeMillis();
	}
	
	public static boolean isConfigExpired(long LastConfigTime){
		return expiredTime!=0 && expiredTime > LastConfigTime;
	}
	
	public static boolean shouldKeepSortOrdr(){
		// 한화제약 - E78CEB
		if(CustConfig.CUST_HANWHA){
			return true;
		}
		return false;
	}
}
