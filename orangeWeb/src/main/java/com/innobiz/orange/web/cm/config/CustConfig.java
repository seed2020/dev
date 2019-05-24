package com.innobiz.orange.web.cm.config;

import com.innobiz.orange.web.cm.crypto.License;

public class CustConfig {

	/** 고객코드 */
	public final static String CUST_CODE = License.ins.getCustomerCode();
	
	/** 개발서버 - AD8227 */
	public final static boolean DEV_SVR = "AD8227".equals(CUST_CODE);
	
	/** 개발PC - ABC123 */
	public final static boolean DEV_PC = "ABC123".equals(CUST_CODE);
	
	/** 리눅스 개발서버 - LINUX */
	public final static boolean DEV_LINUX = "LINUX".equals(CUST_CODE);
	
	
	/** 프라코 - FB7F63 */
	public final static boolean CUST_PLAKOR = "FB7F63".equals(CUST_CODE);

	/** 한화제약 - E78CEB */
	public final static boolean CUST_HANWHA = "E78CEB".equals(CUST_CODE);
	
	/** 두원 - DOOWON */
	public final static boolean CUST_DOOWON = "DOOWON".equals(CUST_CODE);
	
	/** 센텍 - SENTEC */
	public final static boolean CUST_SENTEC = "SENTEC".equals(CUST_CODE);
	
	/** 나우스 - NAUS17 */
	public final static boolean CUST_NAUS = "NAUS17".equals(CUST_CODE);
	
	/** 나라셀라 - CD4B5E */
	public final static boolean CUST_NARACELLAR = "CD4B5E".equals(CUST_CODE);
	
	/** 덴티움 - B5B877 */
	public final static boolean CUST_DENTIUM = "B5B877".equals(CUST_CODE);
	
	/** 이노비즈 - BBFB64 */
	public final static boolean CUST_ENOBIZ = "BBFB64".equals(CUST_CODE);
	
	/** 대진글라스 */
	public final static boolean CUST_DAEJIN_G = "DAEJIN".equals(CUST_CODE);
}
