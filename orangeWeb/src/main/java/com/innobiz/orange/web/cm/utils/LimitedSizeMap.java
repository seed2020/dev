package com.innobiz.orange.web.cm.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/** 최대로 담을 수 있는 크기를 제한한 맵, 캐쉬용 */
public class LimitedSizeMap<K, V> extends LinkedHashMap<K, V> {

	/** serialVersionUID */
	private static final long serialVersionUID = 2024408958432954203L;
	
	/** CacheDataMap의 최대 담을 수 있는 사이즈, Memory overflow 방지용 */
	private int maxSize;
	
	/** 생성자, maxSize를 설정함 */
	public LimitedSizeMap(int maxSize){
		this.maxSize = maxSize;
	}
	
	/** maxSize를 설정함 */
	public void setMaxSize(int maxSize){
		this.maxSize = maxSize;
	}
	
	/** maxSize를 리턴 */
	public int getMaxSize(){
		return maxSize;
	}
	
	/** 최대 담을 수 있는 사이지 이상의 경우에 오래된 것을 지움 */
	protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
		return size() > maxSize;
	}
}
