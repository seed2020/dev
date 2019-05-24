package com.innobiz.orange.web.cm.utils;

import java.util.Locale;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/** 사용자정의 ReloadableResourceBundleMessageSource */
public class CustomMessageSource extends
		ReloadableResourceBundleMessageSource {
	
	/** 로케일에 해당하는 메시지 Properties 조회 */
	public Properties getAllProperties(Locale locale) {
		clearCacheIncludingAncestors();
		PropertiesHolder propertiesHolder = getMergedProperties(locale);
		Properties properties = propertiesHolder.getProperties();
		return properties;
	}
}
