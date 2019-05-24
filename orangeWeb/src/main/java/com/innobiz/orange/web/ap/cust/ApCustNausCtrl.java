package com.innobiz.orange.web.ap.cust;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

@Controller
public class ApCustNausCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApCustNausCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;

	/** [AJAX] - ERP에서 사번 유효성 검증 */
	@RequestMapping(value = "/ap/box/chkNausErpEinAjx")
	public String chkValidUserAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String erpUserNm = (String) object.get("erpUserNm");
			String erpEin = (String) object.get("erpEin");
			
			if (erpUserNm==null || erpUserNm.isEmpty() || erpEin==null || erpEin.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			String result = null;
			// 나우스 - NAUS17, 개발PC - ABC123, 개발서버 - AD8227
			if(CustConfig.CUST_NAUS || CustConfig.DEV_PC || CustConfig.DEV_SVR){
				String erpDomain=null;
				// 나우스 - NAUS17
				if(CustConfig.CUST_NAUS){
					// 서버 설정 목록 조회
					Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
					erpDomain = svrEnvMap.get("erpCall");
				}else{
					erpDomain = "210.121.147.199/KO855_Default";
				}
				
				if(erpDomain==null || erpDomain.isEmpty()){
					LOGGER.error("ERP SSO fail - no erp domain");
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
				String url = (erpDomain.startsWith("http://") ? "" : "http://")
						+ erpDomain + "/ui/gw_IF/check_emp.asp";
				
				//String domain= "NAUS17".equals(custCode) ? "192.168.60.21" : "210.121.147.199";
				String param = "emp_no="+erpEin+"&emp_nm="+erpUserNm;
				url+="?"+param;
				HttpClient http = new HttpClient();
				result = http.sendGet(url, "UTF-8");
				if(result==null || result.isEmpty()){
					LOGGER.error("[ERROR] - result is null!! - {erpUserNm : "+erpUserNm+", erpEin : "+erpEin+"}");
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
			}else{
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				// 원직자VO
				OrOdurBVo orOdurBVo = new OrOdurBVo();
				orOdurBVo.setEin(erpEin);				
				orOdurBVo.setQueryLang(langTypCd);
				int cnt=commonSvc.count(orOdurBVo);
				if(cnt==0){
					LOGGER.error("[ERROR] - No data - {erpUserNm : "+erpUserNm+", erpEin : "+erpEin+"}");
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}else if(cnt>1){
					LOGGER.error("[ERROR] - Too many data("+cnt+") - {erpUserNm : "+erpUserNm+", erpEin : "+erpEin+"}");
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
				if(!erpUserNm.equals(orOdurBVo.getRescNm())){
					LOGGER.error("[ERROR] - The name is not the same - {erpUserNm : "+erpUserNm+", erpEin : "+erpEin+"}");
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
				result="Y";
			}
			
			model.put("result", "ok");
			model.put("isValid", result);
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
}
