package com.innobiz.orange.web.wr.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.wr.utils.WrConstant;

@Service
public class WrAdmSvc {
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 환경 설정 로드 */
	public Map<String, String> getEnvConfigMap(ModelMap model, String compId) throws SQLException {
		Map<String, String> envConfigMap = ptSysSvc.getSysSetupMap(WrConstant.SYS_CONFIG+compId, true);
		if(envConfigMap == null || envConfigMap.isEmpty()){
			
			envConfigMap = new HashMap<String, String>();
			envConfigMap.put("tgtUseYn", "N");// 일정대상 사용여부
			envConfigMap.put("schdlKndCd", "1");// 일정대상 '개인'
			ptCacheSvc.setCache(CacheConfig.SYS_SETUP, "ko", WrConstant.SYS_CONFIG+compId, envConfigMap);
		}
		if(model!=null) model.put("envConfigMap", envConfigMap);
		
		return envConfigMap;
	}
	
	/** 일정대상 조회 */
	public String[][] getSchdlTgtList(HttpServletRequest request){
		return new String[][]{{"1",messageProperties.getMessage("wc.option.psnSchdl", request),"Y"}, 
				{"3",messageProperties.getMessage("wc.option.deptSchdl", request),"N"},{"4",messageProperties.getMessage("wc.option.compSchdl", request),"N"}};
	}
	
	/** 회사 및 계열사 조회 */
	public List<String> getCompAffiliateIdList(String compId, String langTypCd, boolean isAdmin) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		List<String> compIdList = null;
		
		List<PtCompBVo> allCompList=ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 전사여부가 'Y' 이면서 계열사여부가 'N' 이면 전체 조회
			compIdList=new ArrayList<String>();
			for(PtCompBVo storedPtCompBVo : allCompList){
				if(storedPtCompBVo.getUseYn()==null || !"Y".equals(storedPtCompBVo.getUseYn())) continue;
				compIdList.add(storedPtCompBVo.getCompId());
			}
		}else{
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
				compIdList=new ArrayList<String>();
				List<String> affiliateIds=ptCompBVo.getAffiliateIds();
				affiliateIds.add(compId);
				// HashSet 으로 중복ID 제거
				Set<String> hs = new HashSet<String>(affiliateIds);
				affiliateIds = new ArrayList<String>(hs);
				for(PtCompBVo storedPtCompBVo : allCompList){
					if(!affiliateIds.contains(storedPtCompBVo.getCompId())) continue;
					compIdList.add(storedPtCompBVo.getCompId());
				}
			}
		}
		return compIdList;
	}
	
	/** 회사 목록 추가 [회사 및 계열사]*/
	public void setCompAffiliateVoList(ModelMap model, String compId, String langTypCd) throws SQLException{
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		List<PtCompBVo> allCompList=ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		
		if(sysPlocMap.get("affiliatesEnable")==null || !"Y".equals(sysPlocMap.get("affiliatesEnable"))){ // 계열사여부가 'N' 이면 전사 조회
			model.put("ptCompBVoList", allCompList);
		}else{ // 계열사 여부 'Y' 일 경우 계열사 데이터 조회
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			if(ptCompBVo!=null && ptCompBVo.getAffiliateIds()!=null){
				List<String> affiliateIds=ptCompBVo.getAffiliateIds();
				affiliateIds.add(compId);
				// HashSet 으로 중복ID 제거
				Set<String> hs = new HashSet<String>(affiliateIds);
				affiliateIds = new ArrayList<String>(hs);
				
				List<PtCompBVo> ptCompBVoList=new ArrayList<PtCompBVo>();
				
				for(PtCompBVo storedPtCompBVo : allCompList){
					if(!affiliateIds.contains(storedPtCompBVo.getCompId())) continue;
					ptCompBVoList.add(storedPtCompBVo);
				}
				
				model.put("ptCompBVoList", ptCompBVoList);
			}
		}
	}
	
}
