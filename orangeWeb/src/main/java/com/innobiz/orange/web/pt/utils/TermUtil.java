package com.innobiz.orange.web.pt.utils;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 용어 사전용 */
@Component
public class TermUtil {

	/** 자기자신의 객체 - static 함수에서 용하기 위한것 */
	private static TermUtil ins = null;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	public TermUtil(){
		ins = this;
	}
	
	public static String getTerm(String termId, Locale locale) throws SQLException{
		int p = termId.lastIndexOf('.');
		String termVa = null;
		
		if(p>0){
			if(termId.indexOf(".term")>0){
				String setupClsId = termId.substring(0, p);
				String setupId = termId.substring(p+1);
				Map<String, String> termMap = ins.ptSysSvc.getTermMap(setupClsId, locale.getLanguage());
				termVa = termMap==null ? null : termMap.get(setupId);
			}
		}
		
		if(termVa==null){
			termVa = ins.messageProperties.getMessage(termId, locale);
		}
		
		return termVa;
	}
	
	public static String getTerm(String termId, String langTypCd) throws SQLException{
		return getTerm(termId, SessionUtil.toLocale(langTypCd));
	}
}
