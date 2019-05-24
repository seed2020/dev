package com.innobiz.orange.web.ap.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.utils.ApConstant;
import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.pt.noti.AsyncNotiData;
import com.innobiz.orange.web.pt.noti.AsyncNotiSvc;
import com.innobiz.orange.web.pt.secu.UserVo;

/** ERP 연계 알림 서비스 */
@Service
public class ApErpNotiSvc {

	/** 한화 ERP 서비스*/
	@Autowired
	private ApCustHanwhaSvc apCustHanwhaSvc;
	
	/** 나우스 ERP 연계 서비스 */
	@Autowired
	private ApCustNausSvc apCustNausSvc;
	
	/** 이노비즈 연계 서비스 */
	@Autowired
	private ApCustEnobizSvc apCustEnobizSvc;
	
	/** 다인소재  */
	@Autowired
	private ApCustDyneSvc apCustDyneSvc;
	
	
	/** 비동기 알림 서비스 - ERP 연계 알림 */
	@Autowired
    AsyncNotiSvc asyncNotiSvc;
	
	/** ERP에 통보 - ERP 에서 시작된 결제의 통보 */
	public void processErpNoti(AsyncNotiData asyncNotiData) throws SQLException, IOException {
		if(asyncNotiData==null) return;
		
		// 호출 URL 이 있으면 호출 - 표준 방식
		if(asyncNotiData.hasUrl()){
			asyncNotiSvc.add(asyncNotiData);
			return;
		}
		
		// 확장 맵을 꺼냄
		Map<String, String> exMap = asyncNotiData.getExMap();
		if(exMap!=null && !exMap.isEmpty()){
			
			// 연계타입이 한화ERP 이면
			String intgTypCd = exMap.get("intgTypCd");
			if("ERP_HANWHA".equals(intgTypCd)){
				apCustHanwhaSvc.updateHanwhaErp(exMap);
			} else if("ERP_ONECARD".equals(intgTypCd)){
				apCustDyneSvc.updateHanwhaErp(exMap);
			}
			
		}
	}
	
	/** 결재시작 ERP 통보 */
	public void sendErpNotiFromAp(UserVo userVo, String apvNo, String xmlString){
		try {
			// 한화제약 - E78CEB
			if(CustConfig.CUST_HANWHA /*|| CustConfig.DEV_SVR || CustConfig.DEV_PC*/){
				XMLElement xmlElement = SAXHandler.parse(xmlString);
				apCustHanwhaSvc.sendErpNotiFromAp(userVo, apvNo, xmlElement);
				return;
			}
			
			XMLElement xmlElement = SAXHandler.parse(xmlString);
			// 나우스 - NAUS17
			if(CustConfig.CUST_NAUS){
				apCustNausSvc.sendErpNotiFromAp(userVo, apvNo, xmlElement);
			}
			
			String typId = xmlElement.getAttr("head.typId");
			if(typId!=null && typId.startsWith("enobiz")) {
				apCustEnobizSvc.sendErpNotiFromAp(userVo, apvNo, xmlElement);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 연계된 문서인지 검사 - 완결 취소 가능한지 검사용 */
	public boolean isErpNotiFromAp(ApOngdBVo apOngdBVo, String xmlString) {
		
		String intgTypCd = apOngdBVo.getIntgTypCd();
		
		// 연계구분코드
		if(intgTypCd!=null && !intgTypCd.isEmpty() &&
				ArrayUtil.isInArray(ApConstant.INTG_TYP_CDS, intgTypCd)) {
			return true;
		}
		
		try {
			
			if(xmlString!=null && !xmlString.isEmpty()) {
				XMLElement xmlElement = SAXHandler.parse(xmlString);
				String typId = xmlElement.getAttr("head.typId");
				
				// 한화제약 - E78CEB
				if(CustConfig.CUST_HANWHA) {
					if(ArrayUtil.isInArray(new String[]{"training", "leave", "bizTrip"}, typId) ) return true;
					
				// 나우스 - NAUS17
				} else if(CustConfig.CUST_NAUS){
					if("leave".equals(typId)) return true;
				}
				
				// 이노비즈용 - 근태일정
				if(typId!=null && typId.startsWith("enobiz")) {
					// 근태일정 연계 여부
					String erpWorkSchdlYn = xmlElement.getAttr("body/leave.erpWorkSchdlYn");
					if("Y".equals(erpWorkSchdlYn)) {
						return true;
					}
				}
				
				// 연차관리 - 연계
				if(ArrayUtil.isInArray(ApConstant.WD_XML_TYPE_IDS, typId)) {
					return true;
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}
