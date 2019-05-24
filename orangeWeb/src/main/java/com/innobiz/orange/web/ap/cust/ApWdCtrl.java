package com.innobiz.orange.web.ap.cust;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdCnclDVo;
import com.innobiz.orange.web.ap.vo.ApOngdErpFormDVo;
import com.innobiz.orange.web.ap.vo.ApOngdExDVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wd.svc.WdCmSvc;
import com.innobiz.orange.web.wd.vo.WdAnbUseLVo;

/** 결재 - 연차관리 컨트롤 */
@Controller
public class ApWdCtrl {
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 연차 공통 서비스 */
	@Autowired
	private WdCmSvc wdCmSvc;

	
	/** [AJAX] 연차 사용가능 조회
<pre>
anb : 연차남은 갯수
anbMinus : 연차 당겨쓰기 갯수
anbOngo : 연차 결재중 갯수
nanb : 개정연차남은 갯수
nanbMinus : 개정연차 당겨쓰기 갯수
nanbUse : 개정연차 사용 갯수
nanbOngo : 개정연차 결재중 갯수
repb : 대체휴무 갯수
repbOngo : 대체휴무 결재중 갯수
-- mixedUseAllow : 연차 혼합사용(Y/N)
message : 오류 메세지 - 입사일이 설정되지 않았습니다.

newYearMonth:년도 기준일 - 월
newYearDay  :년도 기준일 - 일
</pre>
	 *  */
	@RequestMapping(value = {"/ap/box/getAnbAjx", "/ap/env/getAnbAjx","/ap/adm/form/getAnbAjx"})
	public String getAnbAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {
		String message = null;
		try {
			
			UserVo userVo = LoginSession.getUser(request);
			
			JSONObject jsonObject = data==null || data.isEmpty() || "\"\"".equals(data) ? null : (JSONObject)JSONValue.parse(data);
			String odurUid = jsonObject==null ? null : (String)jsonObject.get("odurUid");
			if(odurUid == null || odurUid.isEmpty()) odurUid = userVo.getOdurUid();
			
			wdCmSvc.getAnbAjx(userVo.getCompId(), odurUid, model, locale);
			
		} catch(Exception e){
			e.printStackTrace();
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		return LayoutUtil.returnJson(model, message);
	}
	
	/** 사용일 확인 */
	@RequestMapping(value = {"/ap/box/checkUseYmdAjx", "/ap/env/checkUseYmdAjx","/ap/adm/form/checkUseYmdAjx"})
	public String checkUseYmdAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {

		String message = null;
		try {
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			
//			String anbTypCd = (String)jsonObject.get("anbTypCd");
			String odurUid = (String)jsonObject.get("odurUid");
			String useYmds = (String)jsonObject.get("useYmds");
			
			OrUserBVo orUserBVo = new OrUserBVo();
			orUserBVo.setUserUid(odurUid);
			orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
			if(orUserBVo==null){
				// wd.msg.noUser=해당하는 사용자가 없습니다.
				String msg = messageProperties.getMessage("wd.msg.noUser", locale) + " ("+odurUid+")";
				model.put("message", msg);
			}
			
			Map<String, String> sysPlocMap = wdCmSvc.getConfig(orUserBVo.getCompId());
			
			Calendar calendar = new GregorianCalendar();
			int year  = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			// 입사일 기준 연차 생성
			boolean enterBaseAnbMak = "Y".equals(sysPlocMap.get("enterBaseAnbMak"));
			
			int newYearMonth = toInt(sysPlocMap.get("newYearMonth"));//년도 기준일 - 월
			int newYearDay   = toInt(sysPlocMap.get("newYearDay"));//년도 기준일 - 일
			if(newYearMonth==0) newYearMonth = 1;
			if(newYearDay==0) newYearDay = 1;
			
			if(enterBaseAnbMak) {
				OrOdurBVo orOdurBVo = new OrOdurBVo();
				orOdurBVo.setOdurUid(odurUid);
				orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
				if(orOdurBVo!=null && orOdurBVo.getEntraYmd()!=null && !orOdurBVo.getEntraYmd().isEmpty()) {
					newYearMonth = Integer.parseInt(orOdurBVo.getEntraYmd().substring(5, 7));
					newYearDay = 1;
				} else {
					// wd.msg.noEntraYmd=입사일이 설정되지 않았습니다.
					throw new CmException(messageProperties.getMessage("wd.msg.noEntraYmd", locale));
				}
			}
			
			if(month < newYearMonth || (month==newYearMonth && day<newYearDay)){
				year--;
			}
			
			String newYearMonthDay = (newYearMonth<10 ? "0" : "")+newYearMonth +"-"+ (newYearDay<10 ? "0" : "")+newYearDay;
			String minUseYmd = year +"-"+ newYearMonthDay;
			String maxUseYmd = (year+1) +"-"+ newYearMonthDay;
			
			
			boolean hasDate = false;
			StringBuilder builder = new StringBuilder();
			
			if(useYmds != null && !useYmds.isEmpty()){
				
				String[] useYmdArray = useYmds.split("\\,");
			
				String durationMsg = null;
				for(int i=0; i<useYmdArray.length; i++){
					if(minUseYmd.compareTo(useYmdArray[i]) > 0){
						// wd.msg.NotMinUseYmd={0} 이전 날짜는 신청 할 수 없습니다.
						durationMsg = messageProperties.getMessage("wd.msg.NotMinUseYmd", new String[]{minUseYmd}, locale) + " ("+useYmdArray[i]+")";
						break;
					}
					if(maxUseYmd.compareTo(useYmdArray[i]) <= 0){
						// wd.msg.NotMaxUseYmd={0} 또는 이후 날짜는 신청 할 수 없습니다.
						durationMsg = messageProperties.getMessage("wd.msg.NotMaxUseYmd", new String[]{maxUseYmd}, locale) + " ("+useYmdArray[i]+")";
						break;
					}
				}
				if(durationMsg != null){
					model.put("message", durationMsg);
					return LayoutUtil.returnJson(model, message);
				}
				
				for(int i=0; i<useYmdArray.length; i++){
					WdAnbUseLVo wdAnbUseLVo = new WdAnbUseLVo();
//					wdAnbUseLVo.setYear(Integer.toString(year));
//					wdAnbUseLVo.setAnbTypCd(anbTypCd);
					wdAnbUseLVo.setOdurUid(odurUid);
					wdAnbUseLVo.setUseYmd(useYmdArray[i]);
					if(commonSvc.count(wdAnbUseLVo) > 0){
						if(hasDate) builder.append("\r\n");
						// wd.msg.dupUseYmd=해당 일자의 사용내역이 이미 등록되어 있습니다.
						builder.append(messageProperties.getMessage("wd.msg.dupUseYmd", locale));
						builder.append(" (").append(useYmdArray[i]).append(")");
						hasDate = true;
					}
				}
			}
			
			if(!hasDate){
				model.put("result", "ok");
			} else {
				model.put("message", builder.toString());
			}
			
		} catch(Exception e){
			e.printStackTrace();
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		return LayoutUtil.returnJson(model, message);
	}
	
	/** [팝업] 완료문서 조회 */
	@RequestMapping(value = "/ap/box/listCmplAnbPop")
	public String listCmplAnbPop(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/listCmplAnbPop");
	}
	/** [팝업] 완료문서 조회 -  프레임 */
	@RequestMapping(value = "/ap/box/listCmplAnbFrm")
	public String listCmplAnbFrm(HttpServletRequest request,
			@Parameter(name="xmlTypId", required=false) String xmlTypId,
			@Parameter(name="startAttr", required=false) String startAttr,
			@Parameter(name="endAttr", required=false) String endAttr,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		List<String> apvNoNotInList = null;
		
		// 진행문서취소상세(AP_ONGD_CNCL_D) - 취소된 결재번호 조회
		ApOngdCnclDVo apOngdCnclDVo = new ApOngdCnclDVo();
		apOngdCnclDVo.setMakrUid(userVo.getUserUid());
		apOngdCnclDVo.setXmlTypId(xmlTypId);
		@SuppressWarnings("unchecked")
		List<ApOngdCnclDVo> apOngdCnclDVoList = (List<ApOngdCnclDVo>)commonSvc.queryList(apOngdCnclDVo);
		if(apOngdCnclDVoList!=null && !apOngdCnclDVoList.isEmpty()){
			apvNoNotInList = new ArrayList<String>();
			for(ApOngdCnclDVo vo : apOngdCnclDVoList){
				apvNoNotInList.add(vo.getApvNo());
			}
		}
		
		// 해당 xmlTypId 으로 완결된 문서 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setMakrUid(userVo.getUserUid());
		apOngdBVo.setXmlTypId(xmlTypId);
		apOngdBVo.setDocProsStatCd("apvd");
		apOngdBVo.setApvNoNotInList(apvNoNotInList);
		Integer recodeCount = commonSvc.count(apOngdBVo);
		
		apOngdBVo.setOrderBy("CMPL_DT DESC");
		PersonalUtil.setFixedPaging(request, apOngdBVo, 10, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(apOngdBVo);
		
		if(apOngdBVoList!=null){
			
			List<String> apvNoList = new ArrayList<String>();
			
			// 맵 목록으로 전환
			Map<String, Object> apOngdBVoMap;
			List<Map<String, Object>> apOngdBVoMapList = new ArrayList<Map<String, Object>>();
			for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
				
				apOngdBVoMap = VoUtil.toMap(storedApOngdBVo, null);
				apOngdBVoMapList.add(apOngdBVoMap);
				
				apvNoList.add(storedApOngdBVo.getApvNo());
			}
			
			// XML 조회 후 기간 추출
			if(		!apvNoList.isEmpty()
					&& ((startAttr != null && !startAttr.isEmpty()) || (endAttr != null && !endAttr.isEmpty()))
					){
				
				ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
				apOngdErpFormDVo.setApvNoList(apvNoList);
				apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
				@SuppressWarnings("unchecked")
				List<ApOngdErpFormDVo> voList = (List<ApOngdErpFormDVo>)commonSvc.queryList(apOngdErpFormDVo);
				
				if(voList != null){
					
					XMLElement xmlElement;
					String startDt, endDt;
					for(ApOngdErpFormDVo vo : voList){
						for(Map<String, Object> map : apOngdBVoMapList){
							if(vo.getApvNo().equals(map.get("apvNo"))){
								
								xmlElement = SAXHandler.parse(vo.getErpVa());
								
								startDt = (startAttr != null && !startAttr.isEmpty()) ? 
										xmlElement.getAttr(startAttr) : null;
								endDt   = (endAttr != null && !endAttr.isEmpty()) ?
										xmlElement.getAttr(endAttr) : null;
								
								if(startDt!=null && startDt.length()==8) startDt = startDt.substring(0,4)+"-"+startDt.substring(4, 6)+"-"+startDt.substring(6);
								if(endDt  !=null && endDt  .length()==8) endDt   = endDt  .substring(0,4)+"-"+endDt  .substring(4, 6)+"-"+endDt  .substring(6);
								
								if(startDt!=null && !startDt.isEmpty()){
									if(endDt!=null && !endDt.isEmpty()){
										map.put("period", startDt+" ~ "+endDt);
									} else {
										map.put("period", startDt);
									}
								} else {
									if(endDt!=null && !endDt.isEmpty()){
										map.put("period", endDt);
									}
								}
								
								break;
							}
						}
					}
				}
			}
			
			model.put("apOngdBVoMapList", apOngdBVoMapList);
		}
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/ap/box/listCmplAnbFrm");
	}

	
	/** 연차 - 완료된 데이터 조회 */
	@RequestMapping(value = {"/ap/box/getAnbCmplDataAjx", "/ap/env/getAnbCmplDataAjx","/ap/adm/form/getAnbCmplDataAjx"})
	public String getAnbCmplDataAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			Locale locale,
			ModelMap model) throws Exception {

		String message = null;
		try {
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String apvNo = (String)jsonObject.get("apvNo");
			
			ApOngdErpFormDVo apOngdErpFormDVo = new ApOngdErpFormDVo();
			apOngdErpFormDVo.setApvNo(apvNo);
			apOngdErpFormDVo.setErpVaTypCd("formBodyXML");
			apOngdErpFormDVo = (ApOngdErpFormDVo)commonSvc.queryVo(apOngdErpFormDVo);
			
			String xml = apOngdErpFormDVo==null ? null : apOngdErpFormDVo.getErpVa();
			
			if(xml != null && !xml.isEmpty()){
				// 속성 목록
				String[] xmlAttrs = new String[]{"erpOdurUid", "erpOptions", "erpStart", "erpEnd", "erpTotalDay", "erpUserNm", "erpHalfStart", "erpHalfStartAmPm", "erpHalfEnd", "erpHalfEndAmPm", "erpReason"};
				XMLElement xmlElement = SAXHandler.parse(xml);
				
				// 리턴 맵
				Map<String, String> returnMap=new HashMap<String, String>();
				
				String value;
				for(String attr : xmlAttrs){
					value=xmlElement.getAttr("body/leave."+attr);
					if(value!=null){
						returnMap.put(attr, value);
					}
				}
				
				if(returnMap.containsKey("erpStart") || returnMap.containsKey("erpEnd")){
					GregorianCalendar cal = new GregorianCalendar();
					String erpDate;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 (E)");
					for(String dateKey : new String[]{"erpStart", "erpEnd"}){
						erpDate=returnMap.get(dateKey);
						erpDate=erpDate.replaceAll("[-: ]", "");
						cal.set(Integer.parseInt(erpDate.substring(0,4)) , Integer.parseInt(erpDate.substring(4,6))-1 , Integer.parseInt(erpDate.substring(6,8)));
						returnMap.put(dateKey+"String", sdf.format(cal.getTime()));
					}
				}
				
				// 수동입력한 날짜 목록
				List<XMLElement> dates = (List<XMLElement>)xmlElement.getChildList("body/dates");
				if(dates!=null){
					StringBuilder sb = new StringBuilder();
					int index=0;
					for(XMLElement element : dates){
						if(index>0) sb.append(",");
						sb.append(element.getAttr("erpDate"));
						index++;
					}
					if(!sb.toString().isEmpty())
						returnMap.put("dates", sb.toString());
				}
				
				returnMap.put("erpCanApvNo", apvNo);
				
				model.put("returnMap", returnMap);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
		}
		return LayoutUtil.returnJson(model, message);
	}
	
	/** 연결 문서 조회 - 대진(안전사고 보고서) */
	/** [팝업] 완료문서 조회 */
	@RequestMapping(value = "/ap/box/listLinkedDocPop")
	public String listLinkedDocPop(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/ap/box/listLinkedDocPop");
	}
	/** [팝업] 완료문서 조회 -  프레임 */
	@RequestMapping(value = "/ap/box/listLinkedDocFrm")
	public String listLinkedDocFrm(HttpServletRequest request,
			@Parameter(name="xmlTypId", required=false) String xmlTypId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setCompId(userVo.getCompId());
		apOngdBVo.setXmlTypId(xmlTypId);
		apOngdBVo.setDocProsStatCdList(ArrayUtil.toList(new String[]{
				"ongo","apvd"
		}, true));
		Integer recodeCount = commonSvc.count(apOngdBVo);
		
		apOngdBVo.setOrderBy("MAK_DT DESC");
		PersonalUtil.setFixedPaging(request, apOngdBVo, 10, recodeCount);
		
		@SuppressWarnings("unchecked")
		List<ApOngdBVo> apOngdBVoList = (List<ApOngdBVo>)commonSvc.queryList(apOngdBVo);
		if(apOngdBVoList!=null){
			
			List<String> apvNoList = new ArrayList<String>();
			
			// 맵 목록으로 전환
			Map<String, Object> apOngdBVoMap;
			List<Map<String, Object>> apOngdBVoMapList = new ArrayList<Map<String, Object>>();
			for(ApOngdBVo storedApOngdBVo : apOngdBVoList){
				
				apOngdBVoMap = VoUtil.toMap(storedApOngdBVo, null);
				apOngdBVoMapList.add(apOngdBVoMap);
				
				apvNoList.add(storedApOngdBVo.getApvNo());
			}
			
			// 연결 문서 조회
			if(!apvNoList.isEmpty()){
				ApOngdExDVo apOngdExDVo = new ApOngdExDVo();
				apOngdExDVo.setApvNoList(apvNoList);
				apOngdExDVo.setExId("erpLinkedApvNo");
				@SuppressWarnings("unchecked")
				List<ApOngdExDVo> apOngdExDVoList = (List<ApOngdExDVo>)commonSvc.queryList(apOngdExDVo);
				if(apOngdExDVoList != null && !apOngdExDVoList.isEmpty()){
					for(ApOngdExDVo vo : apOngdExDVoList){
						for(Map<String, Object> map : apOngdBVoMapList){
							if(vo.getApvNo().equals(map.get("apvNo"))){
								map.put("erpLinkedApvNo", vo.getExVa());
								break;
							}
						}
					}
				}
			}
			
			model.put("apOngdBVoMapList", apOngdBVoMapList);
		}
		model.put("recodeCount", recodeCount);
		
		return LayoutUtil.getJspPath("/ap/box/listLinkedDocFrm");
	}
	
	
//	private float toFloat(String no){
//		if(no==null || no.isEmpty()) return 0;
//		return Float.parseFloat(no);
//	}
	private int toInt(String no){
		if(no==null || no.isEmpty()) return 0;
		return Integer.parseInt(no);
	}

}
