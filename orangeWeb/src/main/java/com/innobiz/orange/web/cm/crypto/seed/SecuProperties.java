package com.innobiz.orange.web.cm.crypto.seed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.innobiz.orange.web.cm.exception.CmException;

/** 암호화된 프라퍼티 */
public class SecuProperties extends Properties {

	/** serialVersionUID */
	private static final long serialVersionUID = 1051648146352632610L;
	
	/** 암호화된 파일의 경로를 설정하여 암호화된 파일을 읽음 */
	public void setPropertyFile(String propertyFile) throws IOException, CmException {
		loadContextFile(propertyFile);
	}
	
	/** 로컬, 개발, 운영 환경별 프로퍼티 파일을 리턴함 */
	private String toContextFile(String contextFile){
		if(contextFile!=null){
			String srcPath, custCode = System.getProperty("CustomerCode");
			if(custCode==null){
				srcPath = contextFile.startsWith("classpath:") ? "/"+contextFile.substring(10) : contextFile;
			} else {
				if(contextFile.startsWith("classpath:")){
					srcPath = "/settings/"+custCode+"/"+contextFile.substring(10);
				} else {
					srcPath = contextFile;
				}
			}
			URL url = SecuProperties.class.getResource(srcPath);
			String path = (url!=null) ? url.getPath() : null;
			return path;
		}
		return null;
	}
	
	/** 파일을 읽고 복호화하여 프로퍼티를 로드함  */
	private void loadContextFile(String propertyFile) throws IOException, CmException {
		String propertyText = toContextFile(propertyFile);
		String txt = SecuPropertyUtil.read(propertyText);
		this.load(new ByteArrayInputStream(txt.replace(":", "\\:").getBytes("UTF-8")));
		if(this.getProperty("cm.login.rsa.bit")==null){
			this.setProperty("cm.login.rsa.bit", "1024");
		}
		
		if("oracle".equals(this.getProperty("dbms"))){
			if(this.getProperty("validationQueryForOracle") != null){
				this.setProperty("validationQuery", this.getProperty("validationQueryForOracle"));
			}
			if(this.getProperty("gwDriverForOracle") != null){
				this.setProperty("gwDriver", this.getProperty("gwDriverForOracle"));
			}
			if(this.getProperty("gwUrlForOracle") != null){
				this.setProperty("gwUrl", this.getProperty("gwUrlForOracle"));
			}
		}
		System.setProperty("GW_DBMS", this.getProperty("dbms"));
	}
}
