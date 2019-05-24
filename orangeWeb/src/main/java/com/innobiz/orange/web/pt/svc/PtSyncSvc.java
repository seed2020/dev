package com.innobiz.orange.web.pt.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.sso.SampleConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtRescDVo;

/** 동기화 처리용 서비스 */
@Service
public class PtSyncSvc {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

//	/** 캐쉬 만료 처리용 서비스 */
//	@Autowired
//	private PtCacheExpireSvc ptCacheExpireSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;

	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	public String processSync(JSONObject jsonObject){
		
		String func = (String)jsonObject.get("func");
		String compId = (String)jsonObject.get("compId");
		String lang = (String)jsonObject.get("lang");
		
		try{
			if("getCompList".equals(func)){
				return getCompList(lang, jsonObject);
			} else if("getOrgList".equals(func)){
				return getOrgList(compId, lang, jsonObject);
			} else if("getCodeListMap".equals(func)){
				return getCodeListMap(compId, lang, jsonObject);
			} else if("getUserList".equals(func)){
				String pageString = (String)jsonObject.get("page");
				if(pageString!=null){
					Integer page = Integer.valueOf(pageString);
					return getUserList(compId, lang, page, jsonObject);
				}
			} else if("getUser".equals(func)){
				String userUid = (String)jsonObject.get("userUid");
				if(userUid!=null) userUid = userUid.toUpperCase();
				return getUser(userUid, lang, jsonObject);
			} else if("getUserListByUserUid".equals(func)){
				String userUids = (String)jsonObject.get("userUids");
				if(userUids!=null) userUids = userUids.toUpperCase();
				return getUserListByUserUid(userUids, lang, jsonObject);
			} else if("getLanguageList".equals(func)){
				return getLanguageList(compId, jsonObject);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/** 회사목록 조회 */
	private String getCompList(String langTypCd, JSONObject jsonObject) throws SQLException, CmException, IOException {
		Map<String, String> map;
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, null, langTypCd);
		if(ptCompBVoList!=null){
			for(PtCompBVo ptCompBVo : ptCompBVoList){
				map = new HashMap<String, String>();
				map.put("compId", ptCompBVo.getCompId());
				map.put("rescNm", ptCompBVo.getRescNm());
				map.put("useYn", ptCompBVo.getUseYn());
				setPtRescD(ptCompBVo.getCompId(), ptCompBVo.getRescId(), map);
				returnList.add(map);
			}
		}
		
		if(returnList!=null && !returnList.isEmpty()){
			return encrypt("getCompList", returnList, jsonObject);
		}
		return null;
	}
	
	/** 조직도 조회 */
	private String getOrgList(String compId, String langTypCd, JSONObject jsonObject) throws CmException, IOException, SQLException  {
		Map<String, String> map;
		boolean groupVer = "GROUP".equals(compId);
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		
		// 그룹버전일 경우 - 최상위 조직 코드 설정
		if(groupVer){
			//or.label.orgRootName=조직도
			String rootName = messageProperties.getMessage("or.label.orgRootName", SessionUtil.toLocale(langTypCd));
			map = new HashMap<String, String>();
			map.put("orgId","GROUP");
			map.put("rescNm", rootName);
			map.put("orgPid", "ROOT");
			map.put("sortOrdr", "1");
			map.put("compId", "GROUP");
			map.put("useYn", "Y");
			returnList.add(map);
		}
		
		List<OrOrgBVo> orOrgBVoList = orCmSvc.getOrgTreeList(groupVer ? null : compId, "Y", langTypCd);
		if(orOrgBVoList!=null){
			for(OrOrgBVo orOrgBVo : orOrgBVoList){
				map = new HashMap<String, String>();
				map.put("orgId", orOrgBVo.getOrgId());
				map.put("rescNm", orOrgBVo.getRescNm());
				if(groupVer && "ROOT".equals(orOrgBVo.getOrgPid())){
					map.put("orgPid", "GROUP");
				} else {
					map.put("orgPid", orOrgBVo.getOrgPid());
				}
				map.put("sortOrdr", orOrgBVo.getSortOrdr());
				map.put("compId", orOrgBVo.getCompId());
				map.put("useYn", orOrgBVo.getUseYn());
				
				setOrRescB(orOrgBVo.getRescId(), map);
				returnList.add(map);
			}
		}
		if(returnList!=null && !returnList.isEmpty()){
			return encrypt("getOrgList", returnList, jsonObject);
		}
		return null;
	}
	
	/** 코드조회 조회 */
	private String getCodeListMap(String compId, String langTypCd, JSONObject jsonObject) throws SQLException, CmException, IOException {
		boolean groupVer = "GROUP".equals(compId);
		Map<String, String> map;
		List<Map<String, String>> list;
		Map<String, List<Map<String, String>>> returnMap = new HashMap<String, List<Map<String, String>>>();
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean codeByCompEnable = "Y".equals(sysPlocMap.get("codeByCompEnable"));
		boolean unifiedCode = compId==null || "GROUP".equals(compId) || !codeByCompEnable;
		
		List<PtCdBVo> ptCdBVoList;
		for(String code : new String[] {"GRADE_CD", "TITLE_CD", "POSIT_CD", "DUTY_CD", "SECUL_CD", "USER_STAT_CD"}){
			
			if(unifiedCode){
				ptCdBVoList = ptCmSvc.getAllCdList(code, langTypCd);
			} else {
				// 회사별로 설정된 코드
				ptCdBVoList = ptCmSvc.getCdListEqCompId(code, langTypCd, compId, null);
						//.getFilteredCdList(code, langTypCd, groupVer ? "A01" : compId, null, null, null);
			}
			
			ptCdBVoList = ptCmSvc.getFilteredCdList(code, langTypCd, groupVer ? "A01" : compId, null, null, null);
			list = new ArrayList<Map<String, String>>();
			if(ptCdBVoList != null){
				for(PtCdBVo ptCdBVo : ptCdBVoList){
					map = new HashMap<String, String>();
					map.put("cd", ptCdBVo.getCd());
					map.put("rescNm", ptCdBVo.getRescNm());
					map.put("sortOrdr", ptCdBVo.getSortOrdr());
					map.put("useYn", ptCdBVo.getUseYn());
					
					setPtRescB(ptCdBVo.getRescId(), map);
					list.add(map);
				}
			}
			
			if(!list.isEmpty()){
				returnMap.put(StringUtil.toCamelNotation(code, false), list);
			}
		}
		
		if(!returnMap.isEmpty()){
			return encrypt("getCodeListMap", returnMap, jsonObject);
		}
		return null;
	}
	
	/** 사용자 목록 조회 - 회사별 또는 전체 */
	private String getUserList(String compId, String langTypCd, Integer page, JSONObject jsonObject) throws CmException, IOException, SQLException  {
		
		/*
		 * 현재 메일에서만 사용하고 있으며 - 메일 사용안함 처리가 반영됨
		 * */
		
		boolean groupVer = "GROUP".equals(compId);
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setCompId(groupVer ? null : compId);
		// 삭제 상태 사용자도 넘김 - 해당 시스템에서 골라서 사용
		//orUserBVo.setUserStatCd("02");//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		orUserBVo.setUserStatCdList(ArrayUtil.toList(new String[]{
				"02","03","04","05"
		}, true));
		orUserBVo.setPageNo(page);
		orUserBVo.setPageRowCnt(50);
		// 메일 사용 안함
		orUserBVo.setWhereSqllet("AND ODUR_UID NOT IN (SELECT ODUR_UID FROM OR_ODUR_SECU_D WHERE SECU_ID='useMailYn' AND SECU_VA='N')");
		orUserBVo.setOrderBy("USER_UID");
		
		List<Map<String, Object>> returnList = orCmSvc.getUserMapListForSync(orUserBVo, langTypCd);
		if(returnList!=null && !returnList.isEmpty()){
			return encrypt("getUserList", returnList, jsonObject);
		}
		return null;
	}

	/** 사용자 조회 - 1명 사용자 */
	private String getUser(String userUid, String langTypCd, JSONObject jsonObject) throws CmException, SQLException, IOException {
		Map<String, Object> userMap = orCmSvc.getUserMapForSSO(userUid, langTypCd);
		if(userMap!=null){
			return encrypt("getUser", userMap, jsonObject);
		}
		return null;
	}
	
	/** 사용자 조회 - userUids 로 넘겨진 사용자 */
	private String getUserListByUserUid(String userUids, String langTypCd, JSONObject jsonObject) throws CmException, IOException, SQLException {
		if(userUids==null || userUids.isEmpty()) return null;
		
		List<String> userUidList = new ArrayList<String>();
		for(String userUid : userUids.split(",")){
			userUidList.add(userUid);
		}
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUidList(userUidList);
		orUserBVo.setOrderBy("USER_UID");
		
		List<Map<String, Object>> returnList = orCmSvc.getUserMapListForSync(orUserBVo, langTypCd);
		if(returnList!=null && !returnList.isEmpty()){
			return encrypt("getUserListByUserUid", returnList, jsonObject);
		}
		return null;
	}
	
	/** 회사별 언어셋 조회 */
	private String getLanguageList(String compId, JSONObject jsonObject) throws SQLException, CmException, IOException{
		
		String langTypCd = contextProperties.getProperty("login.default.lang", "ko");
		List<PtCdBVo> cdList;
		if(compId==null || compId.isEmpty() || "GROUP".equals(compId)){
			// 전체 언어 조회
			cdList = ptCmSvc.getAllCdList("LANG_TYP_CD", langTypCd);
		} else {
			// 회사별 설정된 언어 조회
			cdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
		}
		if(cdList==null){
			return null;
		}
		
		// 코드만 담음
		List<String> returnList = new ArrayList<String>();
		for(PtCdBVo ptCdBVo : cdList){
			returnList.add(ptCdBVo.getCd());
		}
		return encrypt("getLanguageList", returnList, jsonObject);
	}
	
	/** 포털 언어별 리소스 세팅 - 회사명 세팅용 */
	private void setPtRescD(String compId, String rescId, Map<String, String> map) throws SQLException{
		PtRescDVo ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId(compId);
		ptRescDVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<PtRescDVo> list = (List<PtRescDVo>)commonDao.queryList(ptRescDVo);
		if(list!=null){
			for(PtRescDVo vo : list){
				map.put("rescNm-"+vo.getLangTypCd(), vo.getRescVa());
			}
		}
	}
	
	/** 포털 언어별 리소스 세팅 - 코드명 세팅용 */
	private void setPtRescB(String rescId, Map<String, String> map) throws SQLException{
		PtRescBVo ptRescBVo = new PtRescBVo();
		ptRescBVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<PtRescBVo> list = (List<PtRescBVo>)commonDao.queryList(ptRescBVo);
		if(list!=null){
			for(PtRescBVo vo : list){
				map.put("rescNm-"+vo.getLangTypCd(), vo.getRescVa());
			}
		}
	}
	
	/** 조직 언어별 리소스 세팅 - 조직명, 사용자명 세팅용 */
	private void setOrRescB(String rescId, Map<String, String> map) throws SQLException{
		OrRescBVo orRescBVo = new OrRescBVo();
		orRescBVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<OrRescBVo> list = (List<OrRescBVo>)commonDao.queryList(orRescBVo);
		if(list!=null){
			for(OrRescBVo vo : list){
				map.put("rescNm-"+vo.getLangTypCd(), vo.getRescVa());
			}
		}
	}
	
	/** 암호화 */
	@SuppressWarnings("rawtypes")
	private String encrypt(String funcNm, Object object, JSONObject jsonObject) throws CmException, IOException {
		String result = JsonUtil.toJson(object);
		
		if(SampleConstant.PRINT){
			System.out.println(
					"---- "+funcNm+"\n"+
					"String result = \""+result.replace("\"", "\\\"")+"\";\n");
		}
		
		boolean serverDebug = isDebug();
		boolean clientDebug = serverDebug && "Y".equals(jsonObject.get("debug")); 
		
		if(serverDebug){
			StringBuilder builder = new StringBuilder(1024 * 8);
			String va;
			
			builder.append("MAIL ORG SYNC -");
			for(String key : new String[]{ "func","compId","lang","page","userUid","userUids" }){
				va = (String)jsonObject.get(key);
				if(va!=null && !va.isEmpty()){
					builder.append("  ").append(key).append(':').append(va);
				}
			}
			builder.append('\n');
			
			if(object==null){
				builder.append("NULL");
				builder.append('\n');
			} else if(object instanceof List){
				for(Object obj : (List)object){
					if(obj==null) builder.append("NULL");
					else builder.append(obj.toString());
					builder.append('\n');
				}
			} else if("getCodeListMap".equals(jsonObject.get("func"))){
				for(String clsCd : new String[]{ "gradeCd","titleCd","positCd","dutyCd","seculCd","userStatCd" }){
					List cdList = (List)((Map)object).get(clsCd);
					if(cdList!=null){
						builder.append('[').append(clsCd).append(']').append('\n');
						for(Object obj : cdList){
							if(obj==null) builder.append("NULL");
							else builder.append(obj.toString());
							builder.append('\n');
						}
					}
				}
			} else {
				builder.append(object.toString());
				builder.append('\n');
			}
			System.out.println(builder.toString());
		}
		return License.ins.encryptIntegration(clientDebug ? "debug:"+result : result);
	}
	
	/** [시스템설정] 조직 메일 동기화 로그 */
	private boolean isDebug(){
		try {
			return "Y".equals(ptSysSvc.getSysPlocMap().get("orgMailSyncLogEnable"));
		} catch (SQLException e) {
			return false;
		}
	}
}
