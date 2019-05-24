package com.innobiz.orange.web.ap.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 나우스 ERP 연계 서비스 */
@Service
public class ApCustNausSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 나우스 ERP 통보 */
	public void sendErpNotiFromAp(UserVo userVo, String apvNo, XMLElement xmlElement) throws SQLException, IOException, CmException{
		//TODO
		String typId = xmlElement.getAttr("head.typId");
		
		if(typId==null || !"leave".equals(typId))
			return;
		
		try{
			// 휴가 종류
			String erpOptions = xmlElement.getAttr("body/leave.erpOptions");
			if(erpOptions==null || erpOptions.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpOptions");
			}
			// 기타(외출,지각,조퇴,결근)는 근태리더기로 I/F되고 있으므로 넘겨주는 데이타에서 제외
			if("00".equals(erpOptions))
				return;
			
			// 시작일(월,일) - 공통 파라미터
			String erpStart = xmlElement.getAttr("body/leave.erpStart");
			if(erpStart==null || erpStart.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpStart");
			}
			
			// 종료일(월,일) - 공통 파라미터
			String erpEnd = xmlElement.getAttr("body/leave.erpEnd");
			if(erpEnd==null || erpEnd.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpEnd");
			}
			
			// 휴가 사유
			String erpReason = xmlElement.getAttr("body/leave.erpReason");
			if(erpReason==null || erpReason.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpReason");
			}
			
			// 신청자 사번
			String erpEin = xmlElement.getAttr("body/leave.erpEin");
			if(erpEin==null || erpEin.isEmpty()){
				throw new CmException("[ERROR] - 필수값이 없습니다.\tname : erpEin");
			}
			
			if(ServerConfig.IS_LOC) // 개발
				return;
			
//			Map<String, String> header = new HashMap<String, String>();
//			header.put("User-Agent", SyncConstant.USER_AGENT+"/"+SyncConstant.VERSION);
			
			Map<String, String> param = new HashMap<String, String>();
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String erpDomain = svrEnvMap.get("erpCall");
			
			//String url = "http://210.121.147.199/ko855_default/ui/gw_IF/push_dilig_info.asp";
			String url = "http://"+erpDomain+"/ui/gw_IF/push_dilig_info.asp";
			param.put("emp_no", erpEin);
			param.put("dilig_cd", erpOptions);
			param.put("dilig_strt_dt", erpStart);
			param.put("dilig_end_dt", erpEnd);
			param.put("remark", erpReason);
			
			HttpClient http = new HttpClient();
//			String result = http.sendPost(url, param, header, "EUC-KR");
			String result = http.sendPost(url, param, null, "EUC-KR");
			if(result!=null){
				
			}
						
		}catch(Exception e){
			throw new CmException(e.getMessage());
		}
	}
}
