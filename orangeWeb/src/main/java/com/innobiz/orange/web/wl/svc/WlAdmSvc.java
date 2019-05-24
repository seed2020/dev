package com.innobiz.orange.web.wl.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtSysSetupDVo;
import com.innobiz.orange.web.wl.utils.WlConstant;

@Service
public class WlAdmSvc {
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wlRescSvc")
	private WlRescSvc wlRescSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 환경 설정 로드 */
	public Map<String, String> getEnvConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(WlConstant.SYS_CONFIG+compId, true);
		if(envConfigMap == null || envConfigMap.isEmpty()){
			
			envConfigMap = new HashMap<String, String>();
			// 취합기준일
			envConfigMap.put("consolDay", "today"); // 일일
			envConfigMap.put("consolWeek", "fri"); // 주간
			envConfigMap.put("consolMonth", "28"); // 월간
			envConfigMap.put("consolYear", "1228"); // 연간
			
			// 항목관리
			envConfigMap.put("resultRescId", "");// 실적 리소스ID
			envConfigMap.put("planRescId", "");// 계획 리소스ID
			envConfigMap.put("etcRescId", "");// 기타 리소스ID
			
			envConfigMap.put("resultUseYn", "Y");// 실적 사용여부
			envConfigMap.put("planUseYn", "Y");// 계획 사용여부
			envConfigMap.put("etcUseYn", "");// 기타 사용여부
			
			envConfigMap.put("typCds", "day/week/month/year");// 일일(day),주간(week),월간(month),연간(year)
			
			// 기타
			envConfigMap.put("fileYn", "Y");// 첨부파일사용
			envConfigMap.put("consolOpenYn", "Y");// 취합일지 공개여부
			envConfigMap.put("deptSrchOpt", "log");// 부서일지 조회범위 : 개별(log), 취합(consol), 개별+취합(all)
			
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", WlConstant.SYS_CONFIG+compId, envConfigMap);
		}
		
		String[] keyList = WlConstant.COL_RESC_IDS;
		for(String key : keyList){
			if(envConfigMap.containsKey(key)) wlRescSvc.queryRescBVo(envConfigMap.get(key), envConfigMap, null);
		}
			
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
	/** 시스템 프로퍼티 저장 내용을 QueryQueue에 저장 - Map */
	public void setSysSetupMap(String setupClsId, QueryQueue queryQueue, boolean withoutPrefix, Map<String, String> paramMap, boolean isNull) throws SQLException{
		if(paramMap==null || paramMap.size()==0) return;
				
		String key;//, currDt = commonDao.querySysdate(new CommonVoImpl("YYYY-MM-DD"));
		int prefixLen = setupClsId==null ? 0 : setupClsId.length()+1;
		
		boolean deletePrevious = false;
		PtSysSetupDVo ptSysSetupDVo;
		String setupId;
		
		// set header
		Entry<String, String> entry;
		Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();
				
		while(iterator.hasNext()){
			entry = iterator.next();
			key = entry.getKey();
			if(key!=null && (withoutPrefix || key.startsWith(setupClsId)) && !key.endsWith("SortOrdr")){
				
				if(!deletePrevious){
					// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - 기존 데이터 삭제
					ptSysSetupDVo = new PtSysSetupDVo();
					ptSysSetupDVo.setSetupClsId(setupClsId);
					queryQueue.delete(ptSysSetupDVo);
					deletePrevious = true;
				}
				
				setupId = withoutPrefix ? key : key.substring(prefixLen);
				// 언더바(_)로 시작하면 저장 안함
				if(setupId.isEmpty() || setupId.charAt(0)=='_') continue;
				if(withoutPrefix && "menuId".equals(setupId)) continue;
				if(!isNull && (entry.getValue()==null || entry.getValue().isEmpty())) continue;
				// 시스템설정상세(PT_SYS_SETUP_D) 테이블 - INSERT
				ptSysSetupDVo = new PtSysSetupDVo();
				ptSysSetupDVo.setSetupClsId(setupClsId);
				ptSysSetupDVo.setSetupId(setupId);
				ptSysSetupDVo.setSetupVa(entry.getValue());
				if(paramMap.containsKey(key+"SortOrdr"))
					ptSysSetupDVo.setSortOrdr(paramMap.get(key+"SortOrdr"));
				queryQueue.insert(ptSysSetupDVo);
			}
		}
	}
	
	/** request =>  Map 변환 */
	public Map<String, String> getParameterMap(HttpServletRequest request, String notRegex, String[] notParams){

		Map<String, String> parameterMap = new HashMap<String, String>();
		Enumeration<String> enums = request.getParameterNames();
		String tmpVa;
		while(enums.hasMoreElements()){
			String paramName = (String)enums.nextElement();
			if(notRegex!=null && paramName.matches(".*"+notRegex+".*")) continue;
			if(notParams!=null && ArrayUtil.isInArray(notParams, paramName)) continue;
			String[] parameters = request.getParameterValues(paramName);
	
			// Parameter가 배열일 경우
			if(parameters.length > 1){
				tmpVa=Arrays.toString(parameters);
				parameterMap.put(paramName, tmpVa);
			// Parameter가 배열이 아닌 경우
			}else{
				parameterMap.put(paramName, parameters[0]);
			}
		}

		return parameterMap;
	}
	
	
	/** 조회조건 추가 [회사 및 계열사]*/
	public void setCompAffiliateIdList(String compId, String langTypCd, CommonVo commonVo, boolean isAdmin) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		if(sysPlocMap.get("taskLogAllCompEnable")==null || !"Y".equals(sysPlocMap.get("taskLogAllCompEnable"))){ // 전사여부가 'N' 이면 내 회사만 조회
			VoUtil.setValue(commonVo, "compId", compId);
			return;
		}
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 이면서 계열사여부가 'N' 이면 전체 조회
			VoUtil.setValue(commonVo, "compId", null);
			return;
		}
		// 전사여부 'Y' and 계열사 여부 'Y' 일 경우 계열사 데이터 조회(계열사 없으면 내 회사)
		PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
		if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
			List<String> affiliateIds=ptCompBVo.getAffiliateIds();
			affiliateIds.add(compId);
			// HashSet 으로 중복ID 제거
			Set<String> hs = new HashSet<String>(affiliateIds);
			affiliateIds = new ArrayList<String>(hs);
			VoUtil.setValue(commonVo, "compId", null);
			VoUtil.setValue(commonVo, "compIdList", affiliateIds);
		}else{
			VoUtil.setValue(commonVo, "compId", compId);
		}
	}
	
	
}
