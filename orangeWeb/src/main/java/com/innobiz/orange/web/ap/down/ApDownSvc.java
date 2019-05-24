package com.innobiz.orange.web.ap.down;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 문서 다운로드 서비스 */
@Service
public class ApDownSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDownSvc.class);
	
	/** contextProperties */
	@Resource(name="contextProperties")
	private Properties contextProperties;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	protected CommonDao commonDao;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;

	/** 다운로드 진행 Runnable */
	private ApDownMainRunnable apDownMainRunnable;
	
	/** 진행중인 프로퍼티 조회 */
	public Map<String, String> getRunningProp(){
		return apDownMainRunnable==null ? null : apDownMainRunnable.getProperty();
	}
	
	/** ROOT 경로 */
	public String getRootDir(){
		return contextProperties.getProperty("upload.base.dir");
	}
	
	/** 중지 여부 */
	public boolean isStop(){
		return apDownMainRunnable==null ? false : apDownMainRunnable.isStop();
	}
	
	/** 다운로드 검색용 ApOngdBVo 생성 */
	public ApOngdBVo createDownVo(String compId, String durStrtDt, String durEndDt){
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		if((durStrtDt!=null && !durStrtDt.isEmpty())
				|| (durEndDt !=null && !durEndDt .isEmpty())){
			apOngdBVo.setDurCat("makDt");
			if(durStrtDt!=null && !durStrtDt.isEmpty()) apOngdBVo.setDurStrtDt(durStrtDt);
			if(durEndDt !=null && !durEndDt .isEmpty()){
				durEndDt = StringUtil.addDate(durEndDt, 1);
				apOngdBVo. setDurEndDt(durEndDt);
			}
		}
		
		apOngdBVo.setCompId(compId);
		apOngdBVo.setDocStatCd("apvd");
		apOngdBVo.setRecLstTypCd("regRecLst");
		apOngdBVo.setWhereSqllet("AND DOC_TYP_CD != 'paper'");
		
		return apOngdBVo;
	}
	
	/** 다운로드 진행 */
	public boolean startDown(Map<String, String> map) throws SQLException {
		
		if(apDownMainRunnable == null || apDownMainRunnable.isProcessDone()){
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			String domain = svrEnvMap.get("webDomain");
			map.put("domain", domain);
			
			if(ServerConfig.IS_LOC) domain = "127.0.0.1:8080";
			String port = "Y".equals(sysPlocMap.get("portForwarding")) ? ":60080" : "";
			String fileType = map.get("fileType");
			String withFile = fileType.endsWith("File") ? "&withFile=Y" : "";
			map.put("url", "http://"+domain+port+"/ap/self/downloadDoc.do?bxId=admOngoBx"+withFile+"&apvNo=");
			
			apDownMainRunnable = new ApDownMainRunnable(this, map);
			new Thread(apDownMainRunnable).start();
			return true;
		}
		return false;
	}
	
	/** 다운로드 중지 */
	public void stopDown(){
		if(apDownMainRunnable!=null){
			apDownMainRunnable.doStop();
		}
	}
	
	/** 다운로드 드랍 */
	public void stopDrop(){
		if(apDownMainRunnable!=null){
			apDownMainRunnable.doStop();
			apDownMainRunnable = null;
		}
	}
	
	/** 다운로드 진행 수 */
	public int getCmplCnt(){
		if(apDownMainRunnable!=null){
			return apDownMainRunnable.getCmplCnt();
		}
		return -1;
	}
	/** 다운로드 오류 수 */
	public int getErrCnt(){
		if(apDownMainRunnable!=null){
			return apDownMainRunnable.getErrCnt();
		}
		return -1;
	}
	/** 다운로드 전체 수 */
	public int getTgtCnt(){
		if(apDownMainRunnable!=null){
			return apDownMainRunnable.getTgtCnt();
		}
		return -1;
	}
	/** 로거 리턴 */
	public static Logger getLoger(){
		return LOGGER;
	}
}
