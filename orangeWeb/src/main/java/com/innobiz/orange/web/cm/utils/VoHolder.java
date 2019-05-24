package com.innobiz.orange.web.cm.utils;

import com.innobiz.orange.web.cm.vo.CommonVo;

/** 쓰레드 기반의 오류처리를 위한 오류정보를 들고 있는 객체 */
public class VoHolder {

	/** 오류 VO를 담는 ThreadLocal */
	private static ThreadLocal<CommonVo> voLocal = new ThreadLocal<CommonVo>();
	
	/** 오류 Exception을 담는 ThreadLocal */
	private static ThreadLocal<Exception> exLocal = new ThreadLocal<Exception>();
	
	/** 오류 정보를 지움 */
	public static void clear(){
		voLocal.remove();
		exLocal.remove();
	}
	
	/** 오류 정보를 세팅 */
	public static void set(Exception exception, CommonVo commonVo){
		if(exception!=null) exLocal.set(exception);
		if(commonVo!=null) voLocal.set(commonVo);
	}
	
	/** 가지고 있는 Exception 리턴 */
	public static Exception getException(){
		return exLocal.get();
	}

	/** 가지고 있는 Vo 리턴 */
	public static CommonVo getVo(){
		return voLocal.get();
	}
	
}
