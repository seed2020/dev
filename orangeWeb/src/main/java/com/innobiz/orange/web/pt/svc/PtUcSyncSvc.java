package com.innobiz.orange.web.pt.svc;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.RC4;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOdurSecuDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/** 동기화 처리용 서비스 */
@Service
public class PtUcSyncSvc {
	
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
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	// xml 구분자
	private final String valSpRt = ";";
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 회사목록 조회 */
	public List<PtCompBVo> getCompList(String langTypCd) throws SQLException, IOException {
		return ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
	}
	
	/** 조직정보 조회 */
	public OrOrgBVo getOrgInfo(String compId , String langTypCd , String orgId , String orgPid) throws SQLException{
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setCompId(compId);
		orOrgBVo.setUseYn("Y");
		if(orgPid != null && !"".equals(orgPid)) orOrgBVo.setOrgPid(orgPid);
		if(orgId != null && !"".equals(orgId)) orOrgBVo.setOrgId(orgId);
		if(orgPid == null && orgId == null ) return null;
		orOrgBVo.setQueryLang(langTypCd);
		return (OrOrgBVo)commonDao.queryVo(orOrgBVo);
	}
	
	/** 조직도 목록 조회 */
	public List<OrOrgBVo> getOrgList(String compId, String useYn, String langTypCd, String orgPid) throws SQLException{
		//조직기본(OR_ORG_B) 테이블
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setCompId(compId);
		orOrgBVo.setUseYn(useYn);
		orOrgBVo.setQueryLang(langTypCd);
		if(orgPid!=null) orOrgBVo.setOrgPid(orgPid); 
		
		orOrgBVo.setOrderBy("T.ORG_PID, T.SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
		return orOrgBVoList;
	}
	
	/** 사용자 목록 조회 - 회사별 또는 전체 */
	public List<Map<String, Object>> getUserMapList(String langTypCd, String compId) throws SQLException, CmException, IOException {
		OrUserBVo searchOrUserBVo = new OrUserBVo();
		searchOrUserBVo.setUserStatCd("02");//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		searchOrUserBVo.setOrderBy("ORG_ID,SORT_ORDR");
		if(compId!=null && !compId.isEmpty()) searchOrUserBVo.setCompId(compId);
		List<Map<String, Object>> returnList = orCmSvc.getUserMapListForSync(searchOrUserBVo, langTypCd);
		
		return returnList;
	}
	
	/** 사용자 정보 조회 */
	public Map<String, Object> getUserMap(String userUid, String langTypCd) throws SQLException, CmException, IOException{
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		if(userUid!=null) orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
		if(orUserBVo==null) return null;
		
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
		orOdurBVo.setQueryLang(langTypCd);
		orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
		if(orOdurBVo==null) return null;
		
		Map<String, Object> userInfoMap = new HashMap<String, Object>();
		VoUtil.toMap(orUserBVo, userInfoMap);
		
		userInfoMap.put("rid", orOdurBVo.getRid());
		userInfoMap.put("ein", orOdurBVo.getEin());
		userInfoMap.put("lginId", orOdurBVo.getLginId());
		userInfoMap.put("genCd", orOdurBVo.getGenCd());
		userInfoMap.put("genNm", orOdurBVo.getGenNm());
		
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(orUserBVo.getOdurUid());
		orUserPinfoDVo.setQueryLang(langTypCd);
		orUserPinfoDVo = (OrUserPinfoDVo)commonDao.queryVo(orUserPinfoDVo);
		if(orUserPinfoDVo!=null){
			orCmSvc.decryptUserPinfoToMap(orUserPinfoDVo, userInfoMap);
		}
		userInfoMap.remove("ssn");
		return userInfoMap;
	}
	
	/** xml 매핑 속성 목록 */
	public String[][] getXmlAtts(String typ, boolean isLinux){
		if(typ == null ) return null;
		String[][] atts = null;
		//{메신저속성명,시스템속성명,암호화여부}
		if(isLinux){
			if("allUser".equals(typ)){
				atts = new String[][]{{"user_id","lginId","N"},{"user_pass","","N"},{"user_jumin","","N"},{"user_certnum","","N"},{"user_name","rescNm","N"},{"user_gbl_name","","N"},{"user_etc_name","","N"},
						{"user_paycl_name","positNm","N"},{"user_payra_name","titleNm","N"},{"user_class_name","gradeNm","N"},{"user_group_code","orgId","N"},{"user_group_name","orgRescNm","N"},
						{"user_add_group_code1","","N"},{"user_add_group_name1","","N"},{"user_add_group_code2","","N"},{"user_add_group_name2","","N"},{"user_add_group_code3","","N"},
						{"user_add_group_name3","","N"},{"user_address","homeAdr","N"},{"user_birthday","","N"},{"user_birth_gubun","","N"},
						{"user_tel_home","homePhon","N"},{"user_tel_office","compPhon","N"},{"user_tel_company","","N"},{"user_tel_mobile","mbno","N"},{"user_tel_fax","compFno","N"},{"user_tel_roming","compFno","N"},
						{"user_email","email","N"},{"user_homepage","hpageUrl","N"},{"user_field2","userUid","Y"},{"user_picture_pos","imgPath","N"}};
			}else if("orgDept".equals(typ)){
				atts = new String[][]{{"deptcode","orgId","N"},{"deptname","rescNm","Y"},{"parent_deptcode","orgPid","N"}, {"sort_field","sortOrdr","N"}};
			}else if("orgUser".equals(typ)){
				atts = new String[][]{{"user_id","lginId","Y"},{"deptcode","orgId","N"}, {"sort_field","sortOrdr","N"},{"user_end","",""}};
			}else{
				return null;
			}
		}else{
			if("allUser".equals(typ)){
				atts = new String[][]{{"user_id","lginId","Y"},{"user_pass","","N"},{"user_name","rescNm","Y"},{"user_jumin","","Y"},{"user_certnum","","Y"},
						{"user_paycl_name","positNm","N"},{"user_payra_name","titleNm","N"},{"user_class_name","gradeNm","N"},{"user_group_code","orgId","N"},{"user_group_name","orgRescNm","N"},
						{"user_add_group_code1","","N"},{"user_add_group_name1","","N"},{"user_add_group_code2","","N"},{"user_add_group_name2","","N"},{"user_add_group_code3","","N"},
						{"user_add_group_name3","","N"},{"user_zipno","homeZipNo","Y"},{"user_address","homeAdr","Y"},{"user_birth_gubun","","N"},{"user_birthday","","Y"},
						{"user_tel_home","homePhon","Y"},{"user_tel_office","compPhon","Y"},{"user_tel_company","","Y"},{"user_tel_mobile","mbno","Y"},{"user_tel_fax","compFno","Y"},
						{"user_email","email","Y"},{"user_homepage","hpageUrl","N"},{"user_bigo","","N"},{"user_default_rule","","N"},{"user_caption1","","N"},{"user_field1","userUid","Y"},{"user_caption2","","N"},
						{"user_field2","","N"},{"user_caption3","","N"},{"user_field3","","N"},{"user_caption4","","N"},{"user_field4","","N"},{"user_caption5","","N"},{"user_field5","","N"},{"user_picture_pos","","N"},
						{"user_work_status","","N"},{"user_gender","genCd","N"},{"user_empno","ein","N"},{"user_comp_code","compId","N"},{"user_comp_name","compNm","N"}};
			}else if("allUserInfo".equals(typ)){
				atts = new String[][]{{"user_name","rescNm"},{"user_paycl_name","positNm"},{"user_payra_name","titleNm"},{"user_group_name","orgRescNm"},{"user_comp_name","compNm"}};
			}else if("orgUser".equals(typ)){
				atts = new String[][]{{"user_id","lginId","Y"},{"group_code","orgId","N"},{"user_uid","userUid","Y"}};
			}else if("orgUserInfo".equals(typ)){
				atts = new String[][]{{"user_paycl_name","positNm"},{"user_payra_name","titleNm"},{"user_class_name","gradeNm"},{"user_group_name","orgRescNm"},{"user_comp_name","compNm"}};
			}else if("orgDept".equals(typ)){
				atts = new String[][]{{"group_code","orgId","N"},{"group_name","rescNm","Y"},{"parent_code","orgPid","N"},{"sort_ordr","sortOrdr","N"}};
			}else if("department".equals(typ)){
				atts = new String[][]{{"deptcode","orgId","N"},{"deptname","rescNm","Y"},{"parent_deptcode","orgPid","N"}};
			}else if("deptUser".equals(typ)){
				atts = new String[][]{{"user_id","lginId","Y"},{"deptcode","orgId","N"},{"user_end","",""}};
			}else{
				return null;
			}
		}
		
		return atts;
	}
	
	/** 조직도 조회 [메신저 xml 형태] */
	public Document getOrgDeptList(boolean isLinux, String encYn , String langTypCd , String xmlTyp , String compId) throws SQLException, IOException, GeneralSecurityException  {
		// 회사별 언어코드
		langTypCd = langTypCd == null ? "ko" : langTypCd;
		Element root = new Element("groups");
		Element group = null;
		//xml 매핑 속성 목록
		String[][] atts = getXmlAtts("orgDept", isLinux);
		Map<String, Object> voMap;
		String orgInfo = "";
		List<PtCompBVo> ptCompBVoList = getCompList(langTypCd);
		
		//조직도
		String topGroupNm = null;
		
		//회사별 동기화 체크
		boolean isCompSync = false;
		String topCdVal = null;
		if(compId != null && !"".equals(compId)){
			//회사정보 조회
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			//등록되지 않은 회사 처리
			if(ptCompBVo==null){
				return new Document().setRootElement(root);
			}
			
			topGroupNm = ptCompBVo.getRescNm();
			isCompSync = true;
			//회사의 조직정보에서 최상위 ROOT 정보 조회
			OrOrgBVo orOrgBVo = getOrgInfo(compId, langTypCd, null, "ROOT");
			//최상위 ROOT의 조직ID
			topCdVal = orOrgBVo.getOrgId();
		}
		
		if(topCdVal == null) topCdVal = "1";
		
		//최상위 루트 생성
		group = new Element("group");
		group.addContent(new Element("group_code").setAttribute("value", topCdVal));
		

		if(compId == null || "".equals(compId) || topGroupNm == null ) topGroupNm = messageProperties.getMessage("or.label.orgRootName", SessionUtil.toLocale(langTypCd));
		
		group.addContent(new Element("group_name").setAttribute("value", "Y".equals(encYn) ? RC4.getEncrypt(topGroupNm) : topGroupNm));
		group.addContent(new Element("parent_code").setAttribute("value", topCdVal));
		root.addContent(group);
		
		// 조직목록
		List<OrOrgBVo> orOrgBVoList=null, orParentOrgList=null;
		
		for(PtCompBVo ptCompBVo : ptCompBVoList){
			//회사ID가 있을경우 해당 회사ID가 아닌경우 동기화 하지 않는다.
			if(isCompSync && !compId.equals(ptCompBVo.getCompId())) continue;
			//orOrgBVoList = orCmSvc.getOrgTreeList(ptCompBVo.getCompId(), "Y", langTypCd);
			orParentOrgList = getOrgList(ptCompBVo.getCompId(), "Y", langTypCd, "ROOT");
			for(OrOrgBVo parentOrgVo : orParentOrgList){
				orOrgBVoList = orCmSvc.getDownTreeList(parentOrgVo.getOrgId(), langTypCd);
				for(OrOrgBVo orOrgBVo : orOrgBVoList){
					// 사용여부가 'N' 인 조직 동기화 제외
					if(orOrgBVo.getUseYn()!=null && "N".equals(orOrgBVo.getUseYn())) continue;
					// 부모ID가 ROOT 일경우 동기화에서 제외한다.
					if(isCompSync && "ROOT".equals(orOrgBVo.getOrgPid())) continue;
					group = new Element("group");
					voMap = VoUtil.toMap(orOrgBVo, null);
					for(String[] userAttr : atts){
						orgInfo = !"".equals(userAttr[1]) && voMap.containsKey(userAttr[1]) && voMap.get(userAttr[1]) != null ? (String)voMap.get(userAttr[1]) : "";
						//1.전체 조직정보 동기화 [orgPid 가 ROOT 일경우 pid를 topCdVal(조직코드)로 변경해준다.]
						//2.회사별 조직정보 동기화 [ROOT 하위 조직정보의 pid를 topCdVal(조직코드)로 변경해준다.]
						if("orgPid".equals(userAttr[1]) && !"".equals(orgInfo) && ("ROOT".equals(voMap.get(userAttr[1])) || (isCompSync && topCdVal != null && topCdVal.equals(voMap.get(userAttr[1])))) ) orgInfo = topCdVal;
						if(!"".equals(orgInfo) && "Y".equals(encYn) && "Y".equals(userAttr[2])) orgInfo = RC4.getEncrypt(orgInfo);
						group.addContent(new Element(userAttr[0]).setAttribute("value", orgInfo));
					}
					root.addContent(group);
				}
			}
			
		}
		return new Document().setRootElement(root);
	}
	
	/** user_info_xml [다국어 정보 생성] */
	public void setUserInfo(Element person , List<PtCdBVo> langTypCdList , Map<String, Object> userVoMap , Map<String, Object> userInfoMap , String[][] infoAtts , String encYn) throws SQLException, CmException, IOException, GeneralSecurityException{
		Element infoRoot = new Element("persons");
		Element intoPerson = null;
		Document infoDoc = new Document();
		String userInfo = "";
		//전체 언어타입 목록
		for(PtCdBVo storedPtCdBVo : langTypCdList){
			userInfoMap = getUserMap((String)userVoMap.get("userUid"), storedPtCdBVo.getCd());
			boolean insertFlag = true;
			for(String[] infoAtt : infoAtts){
				if(userInfoMap.containsKey(infoAtt[1])){//값이 있는경우에만 element 삽입
					if(insertFlag){
						intoPerson = new Element("person");
						intoPerson.setAttribute("langid",storedPtCdBVo.getCd());
						insertFlag = false;
					}
					if("compNm".equals(infoAtt[1])){//회사명 삽입
						userInfo = ptCmSvc.getPtCompBVo((String)userInfoMap.get("compId"), storedPtCdBVo.getCd()).getCompNm();
					}else{
						userInfo = userInfoMap.get(infoAtt[1]) == null ? "" : (String)userInfoMap.get(infoAtt[1]);
					}
					intoPerson.addContent(new Element(infoAtt[0]).setAttribute("value", userInfo));
				}
			}
			if(!insertFlag) infoRoot.addContent(intoPerson);
		}
		infoDoc.setRootElement(infoRoot);
		XMLOutputter xo = new XMLOutputter();
		xo.setFormat(Format.getCompactFormat().setEncoding("UTF-8"));
		//xml 결과값
		String infoXml = xo.outputString(infoDoc);
		//암호화 여부가 'Y'면 해당 xml 전체를 암호화한다.
		if(!"".equals(infoXml) && "Y".equals(encYn) ) infoXml = RC4.getEncrypt(infoXml);
		person.addContent(new Element("user_xml_info").setContent(new CDATA(infoXml)));
	}
	
	/** 메신저 사용안함 체크(사용자) */
	public boolean isNotUseMsgrYn(String odurUid) throws SQLException{		
		OrOdurSecuDVo orOdurSecuDVo = new OrOdurSecuDVo();
		orOdurSecuDVo.setOdurUid(odurUid);
		orOdurSecuDVo.setSecuId("useMsgrYn"); // 메신저 사용여부		
		return commonDao.count(orOdurSecuDVo)>0;
	}
	
	/** 전체사용자 조회 [메신저 xml 형태] */
	public Document getUserList(boolean isLinux, String encYn , String langTypCd , String xmlTyp, String compId) throws SQLException, CmException, IOException, GeneralSecurityException  {
		if(xmlTyp == null ) xmlTyp = "allUser";
		if(langTypCd == null ) langTypCd = "ko";
		/** 언어코드 조회 */
		List<PtCdBVo> langTypCdList = ptCmSvc.getCdList("LANG_TYP_CD", "ko", "Y");
		
		//최상위 element 생성
		Element root = new Element("persons");
		Element person = null;
		
		//사용자 정보 조회
		List<Map<String, Object>> returnList = getUserMapList(langTypCd, compId);
				
		if(returnList.size() > 0 ){
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			String domain = svrEnvMap.get("webDomain"); // imgDomain			
			if(ServerConfig.IS_LOC) domain = "127.0.0.1:8080";

			// 이미지 URL PREFIX
			String url = "http://"+domain;
			
			//{메신저 속성명,시스템속성명,암호화여부}
			String[][] atts = getXmlAtts(xmlTyp, isLinux);
			if(atts == null ) return null;
			Map<String, Object> userInfoMap = null;
			String userInfo = "";
			
			//사용자 다국어관련 속성 조회
			String[][] infoAtts = getXmlAtts(xmlTyp+"Info", isLinux);
			String lginId=null;
			for(Map<String, Object> userVoMap : returnList){
				lginId=(String)userVoMap.get("lginId");
				if(lginId==null || lginId.trim().isEmpty()) continue;
				if(userVoMap.containsKey("odurUid") && isNotUseMsgrYn((String)userVoMap.get("odurUid"))) continue;
				if("allUser".equals(xmlTyp) && !"01".equals(userVoMap.get("aduTypCd"))) continue;
				person = new Element("person");
				for(String[] userAttr : atts){
					//맵에 담긴 정보가 없을경우 "" 공백삽입
					if("compNm".equals(userAttr[1])){
						userInfo = ptCmSvc.getPtCompBVo((String)userVoMap.get("compId"), langTypCd).getCompNm();
					}else{
						userInfo = !"".equals(userAttr[1]) && userVoMap.containsKey(userAttr[1]) && userVoMap.get(userAttr[1]) != null ? (String)userVoMap.get(userAttr[1]) : "";
					}
					if(!userInfo.isEmpty() && "imgPath".equals(userAttr[1])){
						userInfo=url+""+userInfo;
					}
					
					//암호화여부 'Y', 시스템속성 'Y' 일경우 RC4 모듈 적용 
					if(!"".equals(userInfo) && "Y".equals(encYn) && "Y".equals(userAttr[2])) userInfo = RC4.getEncrypt(userInfo);
					//person element 에 value 속성 삽입
					person.addContent(new Element(userAttr[0]).setAttribute("value", userInfo));
				}
				// 리눅스가 아니면..
				if(!ServerConfig.IS_LINUX){
					//추가 정보 삽입(CDATA xml 형태)
					if( ("allUser".equals(xmlTyp) && langTypCdList.size() > 1 ) || ("orgUser".equals(xmlTyp) && !"01".equals(userVoMap.get("aduTypCd")) )){
						setUserInfo(person, langTypCdList, userVoMap, userInfoMap, infoAtts, encYn);
					}
				}
				
				root.addContent(person);
			}
		}
		
		return new Document().setRootElement(root);
	}
	
	
	/** 조직도 조회 [메신저 xml 형태] - 리눅스 */
	public Document getOrgDeptList(boolean isLinux, String lang, String compId, String encYn) throws Exception  {
		String langTypCd = lang==null || lang.isEmpty() ? "ko" : lang;
		// 회사별 언어코드
		Element root = new Element("departments");
		Element group = null;
		//xml 매핑 속성 목록
		String[][] atts = getXmlAtts("orgDept", isLinux);
		Map<String, Object> voMap;
		String orgInfo = "";
		// 회사 목록 조회
		List<PtCompBVo> ptCompBVoList = getCompList(langTypCd);
		if(ptCompBVoList==null || ptCompBVoList.size()==0) return null;
		//조직도
		String topGroupNm = null;
		
		List<PtCdBVo> langTypCdList = null;
		
		//회사별 동기화 체크
		boolean isCompSync = false;
		String topCdVal = null;
		if(compId != null && !"".equals(compId)){
			//회사정보 조회
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
			//등록되지 않은 회사 처리
			if(ptCompBVo==null){
				return new Document().setRootElement(root);
			}
			topGroupNm = "";
			// 회사별 설정된 리소스의 어권 정보
			langTypCdList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
			for(PtCdBVo storedPtCdBVo : langTypCdList){
				if(lang != null && !lang.isEmpty() && !lang.equals(storedPtCdBVo.getCd())) continue;
				ptCompBVo = ptCmSvc.getPtCompBVo(compId, storedPtCdBVo.getCd());
				topGroupNm+="".equals(topGroupNm) ? ptCompBVo.getRescNm() : valSpRt+ptCompBVo.getRescNm();
			}
			//topGroupNm = ptCompBVo.getRescNm();
			isCompSync = true;
			//회사의 조직정보에서 최상위 ROOT 정보 조회
			OrOrgBVo orOrgBVo = getOrgInfo(compId, langTypCd, null, "ROOT");
			//최상위 ROOT의 조직ID
			topCdVal = orOrgBVo.getOrgId();
		}
		
		if(topCdVal == null) topCdVal = "1";
		
		//최상위 루트 생성
		group = new Element("department");
		group.addContent(new Element("deptcode").setAttribute("value", topCdVal));

		if(compId == null || "".equals(compId) || topGroupNm == null ) topGroupNm = messageProperties.getMessage("or.label.orgRootName", SessionUtil.toLocale(langTypCd));
		
		group.addContent(new Element("deptname").setAttribute("value", topGroupNm));
		group.addContent(new Element("parent_deptcode").setAttribute("value", topCdVal));
		root.addContent(group);
		
		List<OrOrgBVo> orOrgBVoList=null, orParentOrgList=null;
		
		OrOrgTreeVo orOrgTreeVo = null;
		for(PtCompBVo ptCompBVo : ptCompBVoList){
			//회사ID가 있을경우 해당 회사ID가 아닌경우 동기화 하지 않는다.
			if(isCompSync && !compId.equals(ptCompBVo.getCompId())) continue;
			
			// 회사별 설정된 리소스의 어권 정보
			if(langTypCdList==null) langTypCdList = ptCmSvc.getLangTypCdListByCompId(ptCompBVo.getCompId(), ptCompBVo.getQueryLang());
			
			orParentOrgList = getOrgList(ptCompBVo.getCompId(), "Y", langTypCd, "ROOT");
			
			// 회사별 조직목록
			//orOrgBVoList = orCmSvc.getOrgTreeList(ptCompBVo.getCompId(), "Y", "ko");
			for(OrOrgBVo parentOrgVo : orParentOrgList){
				orOrgBVoList = orCmSvc.getDownTreeList(parentOrgVo.getOrgId(), langTypCd);
				for(OrOrgBVo orOrgBVo : orOrgBVoList){
					// 사용여부가 'N' 인 조직 동기화 제외
					if(orOrgBVo.getUseYn()!=null && "N".equals(orOrgBVo.getUseYn())) continue;
					// 부모ID가 ROOT 일경우 동기화에서 제외한다.
					if(isCompSync && "ROOT".equals(orOrgBVo.getOrgPid())) continue;
					group = new Element("department");
					voMap = VoUtil.toMap(orOrgBVo, null);
					for(String[] userAttr : atts){
						orgInfo = "";
						if("deptname".equals(userAttr[0])){
							for(PtCdBVo storedPtCdBVo : langTypCdList){
								if(lang != null && !lang.isEmpty() && !lang.equals(storedPtCdBVo.getCd())) continue;
								orOrgTreeVo = orCmSvc.getOrgTreeVo(orOrgBVo.getOrgId(), storedPtCdBVo.getCd());
								if(orOrgTreeVo==null){
									orgInfo+="".equals(orgInfo) ? "" : valSpRt+"";
									continue;
								}
								orgInfo+= "".equals(orgInfo) ? orOrgTreeVo.getRescNm() : valSpRt+orOrgTreeVo.getRescNm();
							}
						}else{
							orgInfo = !"".equals(userAttr[1]) && voMap.containsKey(userAttr[1]) && voMap.get(userAttr[1]) != null ? (String)voMap.get(userAttr[1]) : "";
						}
						if("orgPid".equals(userAttr[1]) && !"".equals(orgInfo) && ("ROOT".equals(voMap.get(userAttr[1])) || (isCompSync && topCdVal != null && topCdVal.equals(voMap.get(userAttr[1])))) ) orgInfo = topCdVal;
						group.addContent(new Element(userAttr[0]).setAttribute("value", orgInfo));
					}
					root.addContent(group);
				}
			}
		}
		
		return new Document().setRootElement(root);
	}
	
	/** 조직도 조회 [메신저 xml 형태] - 엔터프라이즈 버전 */
	public Document getSortOrgDeptList(boolean isLinux, String lang, String compId, String encYn) throws Exception  {
		String langTypCd = lang==null || lang.isEmpty() ? "ko" : lang;
		// 회사별 언어코드
		Element root = new Element("departments");
		Element group = null;
		//xml 매핑 속성 목록
		String[][] atts = getXmlAtts("orgDept", isLinux);
		// 회사 목록 조회
		List<PtCompBVo> ptCompBVoList = getCompList(langTypCd);
		if(ptCompBVoList==null || ptCompBVoList.size()==0) return null;
		//조직도
		String topGroupNm = null;
		
		List<PtCdBVo> langTypCdList = null;
		
		//회사별 동기화 체크
		boolean isCompSync = compId!=null && !compId.isEmpty();
		String topCdVal = "1";
		
		//최상위 루트 생성
		group = new Element("department");
		group.addContent(new Element("deptcode").setAttribute("value", topCdVal));

		if(compId == null || "".equals(compId) || topGroupNm == null ) topGroupNm = messageProperties.getMessage("or.label.orgRootName", SessionUtil.toLocale(langTypCd));
		
		group.addContent(new Element("deptname").setAttribute("value", topGroupNm));
		group.addContent(new Element("parent_deptcode").setAttribute("value", topCdVal));
		root.addContent(group);
		
		Map<String, List<OrOrgBVo>> orgMap=null;
		for(PtCompBVo ptCompBVo : ptCompBVoList){
			//회사ID가 있을경우 해당 회사ID가 아닌경우 동기화 하지 않는다.
			if(isCompSync && !compId.equals(ptCompBVo.getCompId())) continue;
			// 회사별 설정된 리소스의 어권 정보
			if(langTypCdList==null) langTypCdList = ptCmSvc.getLangTypCdListByCompId(ptCompBVo.getCompId(), ptCompBVo.getQueryLang());
			orgMap=getForeignOrgTreeListMap(ptCompBVo.getCompId(), "Y", langTypCd);
			if(orgMap==null) continue;
			setOrgList(root, group, atts, encYn, langTypCd, langTypCdList, topCdVal, orgMap, "ROOT");
		}
		
		return new Document().setRootElement(root);
	}
	
	/** 조직도 세팅 */
	public void setOrgList(Element root, Element group, String[][] atts, String encYn, String lang, List<PtCdBVo> langTypCdList, String topCdVal, Map<String, List<OrOrgBVo>> orgMap, String orgPid) throws SQLException{
		if(!orgMap.containsKey(orgPid)) return;
		List<OrOrgBVo> orOrgBVoList = orgMap.get(orgPid);
		Map<String, Object> voMap=null;
		String orgInfo;
		OrOrgTreeVo orOrgTreeVo;
		for(OrOrgBVo orOrgBVo : orOrgBVoList){
			group = new Element("department");
			voMap = VoUtil.toMap(orOrgBVo, null);
			for(String[] userAttr : atts){
				orgInfo = "";
				if("deptname".equals(userAttr[0])){
					for(PtCdBVo storedPtCdBVo : langTypCdList){
						if(lang != null && !lang.isEmpty() && !lang.equals(storedPtCdBVo.getCd())) continue;
						orOrgTreeVo = orCmSvc.getOrgTreeVo(orOrgBVo.getOrgId(), storedPtCdBVo.getCd());
						if(orOrgTreeVo==null){
							orgInfo+="".equals(orgInfo) ? "" : valSpRt+"";
							continue;
						}
						orgInfo+= "".equals(orgInfo) ? orOrgTreeVo.getRescNm() : valSpRt+orOrgTreeVo.getRescNm();
					}
				}else{
					orgInfo = !"".equals(userAttr[1]) && voMap.containsKey(userAttr[1]) && voMap.get(userAttr[1]) != null ? (String)voMap.get(userAttr[1]) : "";
					if("sortOrdr".equals(userAttr[1]) && !orgInfo.isEmpty() && Integer.parseInt(orgInfo)<10) orgInfo="0"+orgInfo;  
				}
				if("orgPid".equals(userAttr[1]) && !"".equals(orgInfo) && ("ROOT".equals(voMap.get(userAttr[1])))) orgInfo = topCdVal;
				group.addContent(new Element(userAttr[0]).setAttribute("value", orgInfo));
			}
			root.addContent(group);
			if(orgMap.containsKey(orOrgBVo.getOrgId())) setOrgList(root, group, atts, encYn, lang, langTypCdList, topCdVal, orgMap, orOrgBVo.getOrgId());
		}
	}

	/** 조직목록 맵 */
	public Map<String, List<OrOrgBVo>> getForeignOrgTreeListMap(String compId, String useYn, String langTypCd) throws SQLException{
		List<OrOrgBVo> orgTreeList = orCmSvc.getOrgTreeList(compId, useYn, langTypCd);
		//List<OrOrgBVo> orgTreeList = orCmSvc.getAffiliateOrgTreeList(compId, useYn, langTypCd);
		
		if(orgTreeList==null || orgTreeList.size()==0)
			return null;
		
		Map<String, List<OrOrgBVo>> returnMap = new HashMap<String, List<OrOrgBVo>>();
		List<OrOrgBVo> orgList=null;
		boolean first;
		for(OrOrgBVo storedOrOrgBVo : orgTreeList){
			first=!returnMap.containsKey(storedOrOrgBVo.getOrgPid());
			if(first) orgList=new ArrayList<OrOrgBVo>();
			else orgList=returnMap.get(storedOrOrgBVo.getOrgPid());
			orgList.add(storedOrOrgBVo);
			if(first) returnMap.put(storedOrOrgBVo.getOrgPid(), orgList);
		}
		
		return returnMap;
		
		
	}
	
	
	/** 조직사용자 조회 [메신저 xml 형태] - 리눅스*/
	public Document getOrgUserList(boolean isLinux, String compId, String encYn) throws SQLException, CmException, IOException, GeneralSecurityException  {
		//최상위 element 생성
		Element root = new Element("persons");
		Element person = null;
		
		//사용자 정보 조회
		List<Map<String, Object>> returnList = getUserMapList("ko", compId);
		if(returnList == null || returnList.size()==0) return new Document().setRootElement(root);
		
		//{메신저 속성명,시스템속성명,암호화여부}
		String[][] atts = getXmlAtts("orgUser", isLinux);
		if(atts == null ) return null;
		String userInfo = "";
		String lginId=null;
		for(Map<String, Object> userVoMap : returnList){
			lginId=(String)userVoMap.get("lginId");
			if(lginId==null || lginId.trim().isEmpty()) continue;
			if(userVoMap.containsKey("odurUid") && isNotUseMsgrYn((String)userVoMap.get("odurUid"))) continue;
			person = new Element("person");
			for(String[] userAttr : atts){
				//맵에 담긴 정보가 없을경우 "" 공백삽입
				userInfo = !"".equals(userAttr[1]) && userVoMap.containsKey(userAttr[1]) && userVoMap.get(userAttr[1]) != null ? (String)userVoMap.get(userAttr[1]) : "";
				if("sortOrdr".equals(userAttr[1]) && !userInfo.isEmpty() && Integer.parseInt(userInfo)<10) userInfo="0"+userInfo;
				//person element 에 value 속성 삽입
				if(userInfo.isEmpty()) person.addContent(new Element(userAttr[0]));
				else person.addContent(new Element(userAttr[0]).setAttribute("value", userInfo));
			}
			root.addContent(person);
		}
		
		return new Document().setRootElement(root);
	}
	
	/** 전체사용자 조회 [메신저 xml 형태] - 리눅스*/
	public Document getAllUserList(boolean isLinux, String lang, String compId, String encYn) throws SQLException, CmException, IOException, GeneralSecurityException  {
		/** 언어코드 조회 */
		//List<PtCdBVo> langTypCdList = ptCmSvc.getCdList("LANG_TYP_CD", "ko", "Y");
		
		//최상위 element 생성
		Element root = new Element("persons");
		
		//사용자 정보 조회
		List<Map<String, Object>> returnList = getUserMapList("ko", compId);
		if(returnList == null || returnList.size()==0) return new Document().setRootElement(root);
		
		//{메신저 속성명,시스템속성명,암호화여부}
		String[][] atts = getXmlAtts("allUser", isLinux);
		if(atts == null ) return null;
		String userInfo = "";
		
		// 서버 설정 목록 조회
		Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
		String domain = svrEnvMap.get("webDomain"); // imgDomain			
		if(ServerConfig.IS_LOC) domain = "127.0.0.1:8080";

		// 이미지 URL PREFIX
		String url = "http://"+domain;
					
		// 다국어 '|' 파이프 연결 속성명
		String[] langAtts = new String[]{"user_name","user_paycl_name","user_group_name","user_class_name","user_payra_name"};
		
		List<PtCdBVo> langTypCdList = null;
		
		Map<String, Map<String, Object>> userInfoMap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> voMap = null;
		String langVal = null;
		Element person = null;
		String lginId=null;
		for(Map<String, Object> userVoMap : returnList){
			lginId=(String)userVoMap.get("lginId");
			if(lginId==null || lginId.trim().isEmpty()) continue;
			if(userVoMap.containsKey("odurUid") && isNotUseMsgrYn((String)userVoMap.get("odurUid"))) continue;
			person = new Element("person");
			for(String[] userAttr : atts){
				langVal = "";
				if(ArrayUtil.isInArray(langAtts, userAttr[0])){
					// 회사별 설정된 리소스의 어권 정보
					langTypCdList = ptCmSvc.getLangTypCdListByCompId((String)userVoMap.get("compId"), "ko");
					
					//전체 언어타입 목록
					for(PtCdBVo storedPtCdBVo : langTypCdList){
						if(lang != null && !lang.isEmpty() && !lang.equals(storedPtCdBVo.getCd())) continue;
						voMap = getUserMap(userInfoMap, (String)userVoMap.get("userUid"), storedPtCdBVo.getCd());
						if(voMap.get(userAttr[1])==null || "".equals(voMap.get(userAttr[1]))) langVal+="";
						else langVal+="".equals(langVal) ? voMap.get(userAttr[1]) : valSpRt+voMap.get(userAttr[1]);
						userInfo=langVal;
					}
				}else{
					//맵에 담긴 정보가 없을경우 "" 공백삽입
					userInfo = !"".equals(userAttr[1]) && userVoMap.containsKey(userAttr[1]) && userVoMap.get(userAttr[1]) != null ? (String)userVoMap.get(userAttr[1]) : "";
					if(!userInfo.isEmpty() && "imgPath".equals(userAttr[1])){
						userInfo=url+""+userInfo;
					}
				}
				//person element 에 value 속성 삽입
				/*if(userInfo.isEmpty()) person.addContent(new Element(userAttr[0]));
				else */
				//암호화여부 'Y', 시스템속성 'Y' 일경우 RC4 모듈 적용 
				if(!"".equals(userInfo) && "Y".equals(encYn) && "Y".equals(userAttr[2])) userInfo = RC4.getEncrypt(userInfo);
				person.addContent(new Element(userAttr[0]).setAttribute("value", userInfo));
			}
			// Default 룰코드
			person.addContent(new Element("user_default_rule").setAttribute("value", "5B39C8B553C821E7CDDC6DA64B5BD2EE"));
			person.addContent(new Element("user_end"));
			root.addContent(person);
		}
		
		return new Document().setRootElement(root);
	}
	
	/** 사용자 정보 조회 - 리눅스 */
	public Map<String, Object> getUserMap(Map<String, Map<String, Object>> userInfoMap, String userUid, String langTypCd) throws SQLException, CmException, IOException{
		if(userInfoMap.containsKey(userUid+langTypCd)) return userInfoMap.get(userUid+langTypCd);
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		if(userUid!=null) orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
		if(orUserBVo==null) return null;
		
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUid(orUserBVo.getOdurUid());
		orOdurBVo.setQueryLang(langTypCd);
		orOdurBVo = (OrOdurBVo)commonDao.queryVo(orOdurBVo);
		if(orOdurBVo==null) return null;
		
		Map<String,Object> returnMap = VoUtil.toMap(orUserBVo, new HashMap<String,Object>());
		userInfoMap.put(userUid+langTypCd, returnMap);
	
		return returnMap;
	}
	
}
