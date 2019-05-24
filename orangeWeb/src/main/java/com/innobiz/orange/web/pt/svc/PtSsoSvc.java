package com.innobiz.orange.web.pt.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LimitedSizeMap;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.sso.SampleConstant;
import com.innobiz.orange.web.pt.sso.SsoConstant;
import com.innobiz.orange.web.pt.vo.PtSsoTVo;
import com.innobiz.orange.web.pt.vo.PtSsoVo;

/** SSO 처리용 서비스 */
@Service
public class PtSsoSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtSsoSvc.class);

//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 원타임ID 타임아웃 초 */
	private static int TIMEOUT_SEC = 60 * 2;
	
	/** 원타임ID 맵 */
	private LimitedSizeMap<Integer, PtSsoVo> onetimeMap = new LimitedSizeMap<Integer, PtSsoVo>(200);
	
	private Map<String, String> header;
	
	/** 재사용 가능하게 - timeout 시에만 해당 정보를 제거함 */
	private boolean onlyTimeout = false;
	
	private String[] wasDomains = null;
	
	public PtSsoSvc(){
		header = new HashMap<String, String>();
		header.put("User-Agent", SsoConstant.USER_AGENT+"/"+SsoConstant.VERSION);
		
//		// B5B877 : (주)덴티움
//		if("B5B877".equals(CustConfig.CUST_CODE)){
//			onlyTimeout = true;
//		}
		
		if(onlyTimeout){
			TIMEOUT_SEC = 60 * 2;
		} else {
			TIMEOUT_SEC = 60 * 3;
		}
		
	}
	
	/** 원타임ID 발급 */
	public String createOnetime(UserVo userVo) throws CmException, IOException{
		Integer onetime = syncWas(userVo);
		// 발급 : License.ins.encryptIntegration
		// 확인 : License.ins.decryptCookie
		// SSO, 동기화 등에서 정보를 암호화 하여 전송할때는 - encryptIntegration 로 암호화 함
		// 해당 모듈에서 응답한 데이터는 - decryptCookie 로 복호화 함
		String localDomain = System.getProperty("localDomain");
		String onetimeData = localDomain==null || localDomain.isEmpty() ? onetime.toString() : onetime.toString()+":"+localDomain;
		return License.ins.encryptIntegration(onetimeData);
	}
	
	/** SSO 정보 세팅 */
	private Integer syncWas(UserVo userVo) throws IOException{
		
		if(wasDomains == null){
			String domainVa = contextProperties.getProperty("was.domain");
			String[] domains = domainVa==null ? null : domainVa.split(",");
			if(domains!=null && domains.length > 1){
				wasDomains = domains;
			} else {
				wasDomains = new String[0];
			}
		}
		
		// 도메인이 설정 안되어 있거나, 1개면 - onetimeMap 에 세팅만 함
		if(wasDomains.length < 2){
			PtSsoVo ptSsoVo = new PtSsoVo(userVo);
			synchronized (onetimeMap) {
				onetimeMap.put(ptSsoVo.getOnetime(), ptSsoVo);
			}
			return ptSsoVo.getOnetime();
		// 도메인을 URL로 호출하여 각 WAS 에 세팅함
		} else {
			HttpClient http = new HttpClient();
			Integer onetime = Math.abs(StringUtil.getNextInt());
			//String uri = "/cm/sso/syncOnetime.do?userUid="+userVo.getUserUid()
			String uri = "/cm/sso/syncOnetime.do?odurUid="+userVo.getOdurUid()
					+"&skin="+userVo.getSkin()+"&lang="+userVo.getLangTypCd()
					+"&onetime="+onetime;
			for(String domain : wasDomains){
				http.sendGet("http://"+domain+uri, header, "UTF-8");
			}
			return onetime;
		}
	}
	
	/** WAS SSO Key 동기화 */
	public void syncOnetime(String odurUid, String skin, String lang, String onetime) {
		PtSsoVo ptSsoVo = new PtSsoVo(odurUid, skin, lang, Integer.valueOf(onetime));
		synchronized (onetimeMap) {
			onetimeMap.put(ptSsoVo.getOnetime(), ptSsoVo);
		}
	}
	
	/** 원타임ID 로 사용자 정보 조회 */
	public String getOnetimeData(String onetime) throws CmException, IOException, SQLException  {
		Integer onetimeKey = null;
		String decripted = null;
		try{
			decripted = License.ins.decryptCookie(onetime);
			onetimeKey = Integer.valueOf(decripted);
		} catch(Exception e){
			LOGGER.error("[SSO Onetime] Invalid SSO : "+(onetime!=null ? onetime+"  " : "")+(decripted!=null ? decripted : ""));
			return null;
		}
		PtSsoVo ptSsoVo = onetimeMap.get(onetimeKey);
		if(ptSsoVo!=null && !ptSsoVo.isOver(TIMEOUT_SEC)){
			if(onlyTimeout){// timeout 만 사용
				if(ptSsoVo.isUsed()){
					LOGGER.warn("[SSO Onetime] already used - onetime:"+ptSsoVo.getOnetime()+" odurUid:"+ptSsoVo.getOdurUid()+" created:"+StringUtil.getDateTime(ptSsoVo.getCreateTime()));
				} else {
					ptSsoVo.setUsed();//사용했음 표시
				}
			} else {
				synchronized (onetimeMap) {
					onetimeMap.remove(ptSsoVo.getOnetime());
				}
			}
			
			String odurUid = ptSsoVo.getOdurUid();
			Map<String, Object> userMap = orCmSvc.getUserMapForSSO(odurUid, ptSsoVo.getLang());
			if(userMap!=null){
				userMap.put("lang", ptSsoVo.getLang());
				userMap.put("skin", ptSsoVo.getSkin());
				String result = JsonUtil.toJson(userMap);
				if(SampleConstant.PRINT){
					System.out.println(
							"---- SSO\n"+
							"String result = \""+result.replace("\"", "\\\"")+"\";\n");
				}
				return License.ins.encryptIntegration(result);
			}
		} else {
			if(ptSsoVo==null){
				LOGGER.error("[SSO Onetime] No data - onetime:"+onetimeKey);
			} else if(ptSsoVo.isOver(TIMEOUT_SEC)){
				synchronized (onetimeMap) {
					onetimeMap.remove(ptSsoVo.getOnetime());
				}
				LOGGER.error("[SSO Onetime] Timeout - onetime:"+ptSsoVo.getOnetime()+" odurUid:"+ptSsoVo.getOdurUid()+" created:"+StringUtil.getDateTime(ptSsoVo.getCreateTime()));
			}
		}
		return null;
	}
	
	/** SSO 원타임Map timeover 된것 제거함 */
	@Scheduled(fixedDelay=30000)
	public void clearOntimeMap(){
		long systime = System.currentTimeMillis();
		List<Integer> keyList = new ArrayList<Integer>();
		PtSsoVo ptSsoVo;
		synchronized (onetimeMap) {
			Iterator<Entry<Integer, PtSsoVo>> iterator = onetimeMap.entrySet().iterator();
			while(iterator.hasNext()){
				ptSsoVo = iterator.next().getValue();
				if(ptSsoVo.isOver(systime, TIMEOUT_SEC)) keyList.add(ptSsoVo.getOnetime());
			}
			if(!keyList.isEmpty()){
				for(Integer key : keyList){
					ptSsoVo = onetimeMap.remove(key);
					// 사용 안된 것만 로그 남김
					if(ptSsoVo!=null && (!onlyTimeout || !ptSsoVo.isUsed())){
						if(wasDomains.length < 2){
							LOGGER.warn("[SSO Onetime] Destroyed - onetime:"+ptSsoVo.getOnetime()+" odurUid:"+ptSsoVo.getOdurUid()+" created:"+StringUtil.getDateTime(ptSsoVo.getCreateTime()));
						}
					}
				}
			}
		}
	}
	/** mobile > web sso 처리용 ssoMobile 값 만들기 */
	public String createSsoMobile(UserVo userVo, String gotoUrl, String authUrl) throws SQLException{
		
		String ssoMobile = StringUtil.getNextHexa();
		PtSsoTVo ptSsoTVo = new PtSsoTVo();
		ptSsoTVo.setSsoId(ssoMobile);
		ptSsoTVo.setUserUid(userVo.getUserUid());
		ptSsoTVo.setLangTypCd(userVo.getLangTypCd());
		ptSsoTVo.setTgtUrl(gotoUrl);
		ptSsoTVo.setAuthUrl(authUrl);
		ptSsoTVo.setRegDt("sysdate");
		commonDao.insert(ptSsoTVo);
		
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		String webDomain = svrEnvMap.get("webDomain");
		
		return "http://"+webDomain+"/index.do?ssoMobile="+ssoMobile;
	}
}
