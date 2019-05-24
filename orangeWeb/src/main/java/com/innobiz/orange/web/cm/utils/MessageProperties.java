package com.innobiz.orange.web.cm.utils;

import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** 메세지 처리용 프라퍼티, 다국어 지원 포함 */
public class MessageProperties {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MessageProperties.class);
	
	/** static instance 반환용 */
	private static MessageProperties messageInstance;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** MessageSourceAccessor, Spring 지원 기능 */
	private MessageSourceAccessor messageSourceAccessor = null;
	
	/** MessageSourceAccessor 를 설정함 */
	public void setMessageSourceAccessor(MessageSourceAccessor messageSourceAccessor) {
		this.messageSourceAccessor = messageSourceAccessor;
		messageInstance = this;
	}
	
	/** 어권에 맞는 메세지를 리턴 */
	public String getMessage(String key, HttpServletRequest request) {
		Locale locale = (Locale)request.getSession(true).getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
		return getMessage(key, null, locale);
	}
	
	/** 어권에 맞는 메세지를 리턴 */
	public String getMessage(String key,  String[] args, HttpServletRequest request) {
		Locale locale = (Locale)request.getSession(true).getAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE");
		return getMessage(key, args, locale);
	}
	
	/** 어권에 맞는 메세지를 리턴 */
	public String getMessage(String key, Locale locale) {
		return getMessage(key, null, locale);
	}
	
	/** 어권에 맞는 메세지를 리턴 */
	public String getMessage(String key,  String[] args, Locale locale) {
		
		Locale msgLocale = locale==null ? Locale.KOREA : locale;
		if(args==null) return messageSourceAccessor.getMessage(key, msgLocale);
		int i, size = args.length;
		for(i=0;i<size;i++){
			if(args[i]!=null && args[i].length()>1 && args[i].charAt(0)=='#'){
				try {
					args[i] = messageSourceAccessor.getMessage(args[i].substring(1), msgLocale);
				} catch(org.springframework.context.NoSuchMessageException me){
					LOGGER.error("Message for '"+args[i].substring(1)+"' dose not defined !");
				}
			}
		}
		return messageSourceAccessor.getMessage(key, args, msgLocale);
	}
	
	/** MessageProperties instance 반환 */
	public static MessageProperties getInstance(){
		return messageInstance;
	}
	
	/** ContextProperties instance 반환 */
	public static Properties getContextProperties(){
		return messageInstance.contextProperties;
	}
}
