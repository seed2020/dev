package com.innobiz.orange.web.wh.svc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.EscapeUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wh.vo.WhCatGrpBVo;
import com.innobiz.orange.web.wh.vo.WhCatGrpLVo;
import com.innobiz.orange.web.wh.vo.WhMdBVo;
import com.innobiz.orange.web.wh.vo.WhMdPichLVo;
import com.innobiz.orange.web.wh.vo.WhPichGrpBVo;
import com.innobiz.orange.web.wh.vo.WhPichGrpLVo;
import com.innobiz.orange.web.wh.vo.WhReqBVo;
import com.innobiz.orange.web.wh.vo.WhReqCmplDVo;
import com.innobiz.orange.web.wh.vo.WhReqEvalDVo;
import com.innobiz.orange.web.wh.vo.WhReqOngdDVo;
import com.innobiz.orange.web.wh.vo.WhReqOngdHVo;
import com.innobiz.orange.web.wh.vo.WhReqRelDVo;
import com.innobiz.orange.web.wh.vo.WhReqStatDVo;
import com.innobiz.orange.web.wh.vo.WhResEvalBVo;

@Service
public class WhHpSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WhHpSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 공통 서비스 */
	@Resource(name = "whCmSvc")
	private WhCmSvc whCmSvc;
	
	/** 관리 서비스 */
	@Resource(name = "whAdmSvc")
	private WhAdmSvc whAdmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 파일 서비스 */
	@Resource(name = "whFileSvc")
	private WhFileSvc whFileSvc;
	
	/** 문서번호 서비스 */
	@Resource(name = "whDocNoSvc")
	private WhDocNoSvc whDocNoSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 시스템 모듈 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhMdBVo> getSysMdList(WhMdBVo whMdBVo, String compId, String langTypCd, String mdPid, String useYn) throws SQLException{
		boolean isNotParam=whMdBVo==null;
		if(isNotParam)
			whMdBVo = new WhMdBVo();
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		if(compId!=null) whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whMdBVo, false);
		whMdBVo.setQueryLang(langTypCd);
		if(isNotParam && mdPid!=null)
			whMdBVo.setMdPid(mdPid);
		if(isNotParam && useYn!=null) whMdBVo.setUseYn(useYn);
		whMdBVo.setOrderBy("SORT_ORDR ASC");
		
		return (List<WhMdBVo>)commonDao.queryList(whMdBVo);
	}
	
	/** 모듈 정보 조회 */
	public WhMdBVo getWhMdBVo(String compId, String mdId, String langTypCd) throws SQLException{
		return whAdmSvc.getWhMdBVo(compId, mdId, langTypCd);
	}
	
	/** 모듈 정보 조회 - DB*/
	public WhMdBVo getWhMdBVo(String mdId, String langTypCd) throws SQLException{
		return whAdmSvc.getWhMdBVo(mdId, langTypCd);
	}
	
	/** 하위 모듈ID 목록 조회 */
	public List<String> getSubIdList(String compId, String mdId, String langTypCd) throws SQLException{
		return whAdmSvc.getSubIdList(compId, mdId, langTypCd);
	}
	
	/** 하위 모듈ID 전체목록 조회 */
	public List<String> getSubIdAllList(List<String> mdIdList, String compId, String mdId, String langTypCd) throws SQLException{
		return whAdmSvc.getSubIdAllList(mdIdList, compId, mdId, langTypCd);
	}
	
	/** 결과평가 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhResEvalBVo> getResEvalList(String compId, String langTypCd) throws SQLException{
		WhResEvalBVo whResEvalBVo = new WhResEvalBVo();
		whResEvalBVo.setQueryLang(langTypCd);
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whResEvalBVo, false);
		return (List<WhResEvalBVo>) commonDao.queryList(whResEvalBVo);
	}
	
	/** 모듈 담당자 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WhMdPichLVo> getMdPichList(String langTypCd, String mdId, String pichTypCd) throws SQLException{
		WhMdPichLVo whMdPichLVo = new WhMdPichLVo();
		whMdPichLVo.setQueryLang(langTypCd);
		whMdPichLVo.setMdId(mdId);
		if(pichTypCd!=null) whMdPichLVo.setPichTypCd(pichTypCd);
		return (List<WhMdPichLVo>) commonDao.queryList(whMdPichLVo);
	}
	
	/** 담당자 목록 조회 - 중복제거 */
	@SuppressWarnings("unchecked")
	public List<WhMdPichLVo> getUsePichList(String compId, String langTypCd, String mdId, String pichTypCd) throws SQLException{
		WhMdPichLVo whMdPichLVo = new WhMdPichLVo();
		whMdPichLVo.setQueryLang(langTypCd);
		whMdPichLVo.setMdId(mdId);
		
		// 병합할 데이터 여부[사용자 이외 그룹이 있을경우]
		boolean isMerge=false;
		
		if(pichTypCd==null || "G".equals(pichTypCd)){
			whMdPichLVo.setPichTypCd("G");
			isMerge=commonDao.count(whMdPichLVo)>0;
			whMdPichLVo.setPichTypCd(null);
		}
		// 담당자그룹상세 목록VO
		List<WhPichGrpLVo> tmpList=null;
		WhMdPichLVo newDtlVo = null;
		if(pichTypCd!=null) whMdPichLVo.setPichTypCd(pichTypCd);
		List<WhMdPichLVo> whMdPichLVoList = (List<WhMdPichLVo>) commonDao.queryList(whMdPichLVo);
		if(whMdPichLVoList==null || whMdPichLVoList.size()==0){
			WhPichGrpBVo whPichGrpBVo = whAdmSvc.getWhPichGrpBVo(compId, langTypCd, "Y", "Y");
			if(whPichGrpBVo!=null){
				whMdPichLVoList=new ArrayList<WhMdPichLVo>();
				tmpList=whAdmSvc.getPichGrpDtlList(langTypCd, whPichGrpBVo.getPichGrpId());
				if(tmpList!=null && tmpList.size()>0){
					for(WhPichGrpLVo dtlVo : tmpList){
						newDtlVo=new WhMdPichLVo();
						newDtlVo.setIdVa(dtlVo.getUserUid());
						newDtlVo.setPichNm(dtlVo.getUserNm());
						whMdPichLVoList.add(newDtlVo);
					}
				}
			}
			return whMdPichLVoList;
		}
		
		if(isMerge){
			// 중복제거를 위해 키를 담아두는 맵
			Map<String,String> chkMap=new HashMap<String,String>();
			// 중복을 제거한 사용자 목록
			List<WhMdPichLVo> returnList=new ArrayList<WhMdPichLVo>();
			
			for(WhMdPichLVo storedWhMdPichLVo : whMdPichLVoList){
				if("U".equals(storedWhMdPichLVo.getPichTypCd()) && !chkMap.containsKey(storedWhMdPichLVo.getIdVa())){
					returnList.add(storedWhMdPichLVo);
					chkMap.put(storedWhMdPichLVo.getIdVa(), "Y");
				}else if("G".equals(storedWhMdPichLVo.getPichTypCd())){
					tmpList=whAdmSvc.getPichGrpDtlList(langTypCd, storedWhMdPichLVo.getIdVa());
					if(tmpList!=null && tmpList.size()>0){
						for(WhPichGrpLVo dtlVo : tmpList){
							if(chkMap.containsKey(dtlVo.getUserUid())) continue;
							newDtlVo=new WhMdPichLVo();
							newDtlVo.setIdVa(dtlVo.getUserUid());
							newDtlVo.setPichNm(dtlVo.getUserNm());
							returnList.add(newDtlVo);
							chkMap.put(dtlVo.getUserUid(), "Y");
						}
					}
				}else continue;
			}
			return returnList;
		}
		
		return whMdPichLVoList;
	}
	
	/** 유형그룹 상세 목록 조회 */
	public List<WhCatGrpLVo> getCatGrpDtlList(String compId, String langTypCd, String catGrpId) throws SQLException{
		List<WhCatGrpLVo> whCatGrpLVoList = whAdmSvc.getCatGrpDtlList(langTypCd, catGrpId);
		if(whCatGrpLVoList==null || whCatGrpLVoList.size()==0){
			WhCatGrpBVo whCatGrpBVo = whAdmSvc.getCatGrpDtl(compId, langTypCd, null, "Y", "Y");
			if(whCatGrpBVo!=null){
				whCatGrpLVoList = whAdmSvc.getCatGrpDtlList(langTypCd, whCatGrpBVo.getCatGrpId());
			}
		}
		return whCatGrpLVoList;
	}
	
	/** 메뉴별 조회조건 세팅 */
	public void setQueryUrlOptions(HttpServletRequest request, ModelMap model, WhReqBVo whReqBVo, String suffix, 
			UserVo userVo, String langTypCd, boolean isAdmin, Map<String, String> configMap) throws SQLException, CmException{
		
		String statCd = ParamUtil.getRequestParam(request, "statCd", false); // 상태코드
		if(!isAdmin){
			suffix=suffix.toLowerCase();
			if(suffix.startsWith("req")){ // 요청
				whReqBVo.setRegrUid(userVo.getUserUid());
			}else if(suffix.startsWith("recv")){ // 접수
				if(statCd==null) {
					statCd="R";
					whReqBVo.setStatCd(statCd);
					if(model!=null) model.put("paramStatCd", statCd);
				}
				if(!(statCd!=null && !statCd.isEmpty() && ArrayUtil.isInArray(new String[]{"R","A","G"}, statCd)))
					whReqBVo.setStatCdList(new String[]{"R","A","G"}); // 요청, 접수, 반려
			}else if(suffix.startsWith("hdl")){ // 처리
				String cmplInYn = ParamUtil.getRequestParam(request, "cmplInYn", false); // 처리완료 포함여부
				
				if(!(statCd!=null && !statCd.isEmpty() && ArrayUtil.isInArray(new String[]{"A","P"}, statCd))){
					if(cmplInYn!=null && "Y".equals(cmplInYn))
						whReqBVo.setStatCdList(new String[]{"A","P","C"}); // 접수, 진행중, 처리완료						
					else
						whReqBVo.setStatCdList(new String[]{"A","P"}); // 접수, 진행중, 처리완료
						
				}else{
					if(cmplInYn!=null && "Y".equals(cmplInYn) && !"C".equals(statCd)){
						whReqBVo.setStatCd(null);
						whReqBVo.setStatCdList(new String[]{statCd,"C"}); // 상태코드(파라미터) + 처리완료
					}
				}
				
				if(configMap==null)
					configMap=getEnvConfigAttr(model, userVo.getCompId(), null);
				
				// 담당자 조회여부
				boolean isPichSrch = configMap.containsKey("hdlLstOpt") && configMap.get("hdlLstOpt")!=null && "pich".equals(configMap.get("hdlLstOpt"));
				if(isPichSrch)
					whReqBVo.setPichUid(userVo.getUserUid()); // 담당자UID
			}else if(suffix.startsWith("dashbrd")){ // 현황
				String compId=userVo.getCompId();
				if(configMap==null)
					configMap=getEnvConfigAttr(model, compId, null);
				
				String durStrtDt = ParamUtil.getRequestParam(request, "durStrtDt", false);
				String durEndDt = ParamUtil.getRequestParam(request, "durEndDt", false);
				if((durStrtDt==null || durStrtDt.isEmpty()) && (durEndDt==null || durEndDt.isEmpty())){
					whReqBVo.setDurCat("reqDt"); // 요청일
					whReqBVo.setDurStrtDt(getDateToAdd(null, Integer.parseInt(configMap.get("dueDt")), true));
					if(model!=null) model.put("durStrtDt", whReqBVo.getDurStrtDt());
				}
			}
		}
		
		if((whReqBVo.getSchCat()!=null && "hdlCont".equals(whReqBVo.getSchCat()) && whReqBVo.getSchWord()!=null && !whReqBVo.getSchWord().isEmpty()) || 
				(whReqBVo.getHdlCont()!=null && !whReqBVo.getHdlCont().isEmpty()) ){
			whReqBVo.setWithCmpl(true);
		}
		
		if(whReqBVo.getStatCd()!=null && !whReqBVo.getStatCd().isEmpty()){
			statCd=whReqBVo.getStatCd();
			if("A".equals(statCd))
				whReqBVo.setOrderBy("RECV_DT ASC");
			else if("G".equals(statCd))
				whReqBVo.setOrderBy("RECV_DT DESC");
			else if("P".equals(statCd))
				whReqBVo.setOrderBy("CMPL_DUE_DT ASC");
			else if("C".equals(statCd))
				whReqBVo.setOrderBy("CMPL_DT DESC");
			else
				whReqBVo.setOrderBy("REQ_DT ASC");
		}
	}
	
	/** 요청 목록 파일 건수 세팅 */
	public void setWhReqBVoFileCnt(List<WhReqBVo> list) throws SQLException{
		for(WhReqBVo storedWhReqBVo : list){
			storedWhReqBVo.setFileCnt(whFileSvc.getFileVoListCnt(storedWhReqBVo.getReqNo(), "R")); // 파일 건수 조회
		}
	}
	
	/** 요청완료상세 목록 파일 건수 세팅 */
	public void setWhReqCmplDVoFileCnt(List<WhReqCmplDVo> list) throws SQLException{
		for(WhReqCmplDVo storedWhReqCmplDVo : list){
			storedWhReqCmplDVo.setFileCnt(whFileSvc.getFileVoListCnt(storedWhReqCmplDVo.getReqNo(), "C")); // 파일 건수 조회
		}
	}
	
	/** 목록에 최상위모듈명 세팅*/
	public void setTopMdNm(String langTypCd, String compId, List<WhReqBVo> list) throws SQLException{
		WhMdBVo whMdBVo=null;
		for(WhReqBVo storedWhReqBVo : list){
			if(storedWhReqBVo.getMdId()==null || storedWhReqBVo.getMdId().isEmpty()) continue;
			whMdBVo=whAdmSvc.getTopTreeVo(compId, storedWhReqBVo.getMdId(), langTypCd);
			if(whMdBVo==null) continue;
			storedWhReqBVo.setMdNm(whMdBVo.getMdNm());
		}
	}
	
	/** 메뉴별 권한체크 */
	public void chkUrlAuth(HttpServletRequest request, String suffix, UserVo userVo, 
			WhReqBVo whReqBVo, WhReqOngdDVo whReqOngdDVo, Map<String, String> configMap, boolean isAdmin) throws CmException{
		if(isAdmin) return;
		suffix=suffix.toLowerCase();
		if(whReqBVo!=null && suffix.startsWith("req")){ // 요청
			if(!userVo.getUserUid().equals(whReqBVo.getRegrUid())){
				// wh.jsp.not.regr=등록자가 아닙니다.
				throw new CmException("wh.jsp.not.regr", request);
			}
		} 
		
		if(whReqOngdDVo!=null && suffix.startsWith("hdl")){ // 처리
			// 담당자 조회여부
			boolean isPichSrch = configMap.containsKey("hdlLstOpt") && configMap.get("hdlLstOpt")!=null && "pich".equals(configMap.get("hdlLstOpt"));
			if(isPichSrch && !userVo.getUserUid().equals(whReqOngdDVo.getPichUid())){
				// wh.jsp.not.pich=담당자가 아닙니다.
				throw new CmException("wh.jsp.not.pich", request);
			}
		}
		
	}
	
	/** 시스템 모듈 세팅 */
	public void setParamSysMdList(HttpServletRequest request, ModelMap model, String compId, String langTypCd, String mdId, String useYn) throws SQLException, CmException{
		List<List<WhMdBVo>> paramMdList=new ArrayList<List<WhMdBVo>>();
		List<WhMdBVo> whMdBVoList=null;
		
		if(mdId==null)
			mdId = ParamUtil.getRequestParam(request, "mdId", false);
		if(mdId!=null && !mdId.isEmpty()){
			WhMdBVo whMdBVo = getWhMdBVo(mdId, langTypCd);
			if(!"ROOT".equals(whMdBVo.getMdPid())){ // 1단계 모듈이 아닐경우 상위 모듈 목록 조회
				List<String> mdPidList=new ArrayList<String>();
				List<String> mdIdList=new ArrayList<String>();
				List<WhMdBVo> topTreeList = whAdmSvc.getTopTreeList(whMdBVo.getCompId(), mdId, langTypCd, true);
				if(topTreeList!=null && topTreeList.size()>0){
					for(WhMdBVo storedWhMdBVo : topTreeList){
						mdIdList.add(storedWhMdBVo.getMdId());
						mdPidList.add(storedWhMdBVo.getMdPid());					
					}
				}
				
				if(mdPidList.size()>0){
					Collections.reverse(mdIdList); // 순서를 변경
					Collections.reverse(mdPidList); // 순서를 변경
					for(String pid : mdPidList){
						whMdBVoList=getSysMdList(null, compId, langTypCd, pid, useYn);
						if(whMdBVoList!=null && whMdBVoList.size()>0)
							paramMdList.add(whMdBVoList);
					}
					
					model.put("paramMdIds", mdIdList); // 콤보박스를 선택하기 위한 모듈ID 배열
				}
				
			}
		}
		// 목록이 없을경우 상위 모듈 목록 조회
		if(paramMdList.size()==0){
			whMdBVoList=getSysMdList(null, compId, langTypCd, "ROOT", useYn);
			if(whMdBVoList!=null)
				paramMdList.add(whMdBVoList);
		}
		// 선택된 모듈ID의 하위 모듈 조회
		if(mdId!=null && !mdId.isEmpty()){
			whMdBVoList=getSysMdList(null, compId, langTypCd, mdId, useYn);
			if(whMdBVoList!=null && whMdBVoList.size()>0)
				paramMdList.add(whMdBVoList);
		}
		
		if(paramMdList.size()>0)
			model.put("paramMdList", paramMdList);
	}
	
	/** 설정 조회 */
	public Map<String, String> getEnvConfigAttr(ModelMap model, String compId, String[] keys) throws SQLException{
		// 환경설정 조회
		Map<String,String> envConfigMap = whAdmSvc.getEnvConfigMap(keys==null ? model : null, compId);		
		if(keys==null) return envConfigMap;
		Map<String,String> configMap=new HashMap<String,String>();
		for(String key : keys){
			if(envConfigMap.containsKey(key)){
				configMap.put(key, envConfigMap.get(key));
			}
		}
		if(model!=null) model.put("envConfigMap", envConfigMap);
		return configMap;
	}
	
	/** 최대 본문 사이즈 model에 추가 */
	public void putBodySizeToModel(HttpServletRequest request, ModelMap model) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		Integer bodySize = ptSysSvc.getBodySizeMap(langTypCd, userVo.getCompId()).get("wh");
		if(bodySize==null) bodySize=0;
		// 시스템 설정 조회 - 본문 사이즈
		bodySize = bodySize * 1024;
		model.put("bodySize", bodySize);
	}
	
	
	/** 저장 - 요청 */
	public String saveHelp(HttpServletRequest request, QueryQueue queryQueue, String reqNo) throws CmException, SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
					
		// 테이블관리 기본(WL_REQ_B) 테이블 - BIND
		WhReqBVo whReqBVo = new WhReqBVo();
		VoUtil.bind(request, whReqBVo);
		whReqBVo.setQueryLang(langTypCd);
		whReqBVo.setCompId(userVo.getCompId()); // 회사ID
		whReqBVo.setDeptId(userVo.getOrgId()); // 조직ID
		if(whReqBVo.getWrittenReqNo()==null)
			whReqBVo.setWrittenReqNo("");
		// 등록자UID
		String regrUid = userVo.getUserUid();
		boolean isNew=reqNo==null || reqNo.isEmpty(); // 신규 여부
		if(isNew){
			reqNo=whCmSvc.createNo("WH_REQ_B").toString();
			whReqBVo.setReqNo(reqNo);
			whReqBVo.setRegrUid(regrUid);
			whReqBVo.setRegDt("sysdate");
			whDocNoSvc.setDocNo(whReqBVo, langTypCd, userVo.getCompId(), null, whReqBVo.getMdId(), regrUid);
			queryQueue.insert(whReqBVo);
			
			saveHtmlYn(request, queryQueue, reqNo, "R", null); // html여부 저장
			saveWhReqRelDVo(request, queryQueue, reqNo); // 관련요청 저장
			
		}else{
			// 상태코드 검증
			chkSaveStatCd(request, reqNo, whReqBVo.getStatCd());
			
			whReqBVo.setModrUid(regrUid);
			whReqBVo.setModDt("sysdate");
			queryQueue.update(whReqBVo);
			
			saveWhReqRelDVo(request, queryQueue, reqNo); // 관련요청 저장
		}
		
		// 진행상세 저장
		saveProgress(request, queryQueue, reqNo, null);
		
		return reqNo;
	}
	
	/** 상태코드 검증 */
	public void chkSaveStatCd(HttpServletRequest request, String reqNo, String statCd) throws SQLException, CmException{
		WhReqOngdDVo whReqOngdDVo = getReqOngdDVo(reqNo);
		if(whReqOngdDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 환경설정
		Map<String, String> configMap=getEnvConfigAttr(null, userVo.getCompId(), null);
		
		String reqStatCd=whReqOngdDVo.getStatCd();
		// if(("A".equals(statCd) || "G".equals(statCd)) && !("R".equals(reqStatCd) || "A".equals(reqStatCd) || "G".equals(reqStatCd))){
		if(("R".equals(statCd) || "T".equals(statCd)) && !("R".equals(reqStatCd) || "T".equals(reqStatCd) || "G".equals(reqStatCd))){
			// wh.jsp.not.save='요청' 상태에서만 변경이 가능합니다.
			throw new CmException("wh.jsp.not.save", request);
		}
		
		if("A".equals(statCd) && ( 
				((configMap.get("recvModYn")==null || "N".equals(configMap.get("recvModYn"))) && !"R".equals(reqStatCd)) 
				|| ("Y".equals(configMap.get("recvModYn")) && !("R".equals(reqStatCd) || "A".equals(reqStatCd))) )
				){
			// wh.jsp.not.recv='요청' 상태에서만 '접수' 또는 '반려'가 가능합니다.
			throw new CmException("wh.jsp.not.recv", request);
		}
		
		if("G".equals(statCd) && !"R".equals(reqStatCd)){
			// wh.jsp.not.recv='요청' 상태에서만 '접수' 또는 '반려'가 가능합니다.
			throw new CmException("wh.jsp.not.recv", request);
		}
		
		if("P".equals(statCd) && ( 
				((configMap.get("hdlModYn")==null || "N".equals(configMap.get("hdlModYn"))) && !"A".equals(reqStatCd)) 
				|| ("Y".equals(configMap.get("hdlModYn")) && !("A".equals(reqStatCd) || "P".equals(reqStatCd))) )
				){
			// wh.jsp.not.progress='접수' 상태에서만 진행으로 변경이 가능합니다.
			throw new CmException("wh.jsp.not.progress", request);
		}
		
		/*if("P".equals(statCd) && !"A".equals(reqStatCd)){
			// wh.jsp.not.progress='접수' 상태에서만 진행으로 변경이 가능합니다.
			throw new CmException("wh.jsp.not.progress", request);
		}*/
		if("C".equals(statCd) && !"P".equals(reqStatCd)){
			if("C".equals(reqStatCd) || "R".equals(reqStatCd) || "A".equals(reqStatCd)){
				if(("C".equals(reqStatCd) && (!configMap.containsKey("cmplModYn") || "N".equals(configMap.get("cmplModYn")))) || 
						("R".equals(reqStatCd) && (!configMap.containsKey("cmplRecvYn") || "N".equals(configMap.get("cmplRecvYn")))) ||
						("A".equals(reqStatCd) && (!configMap.containsKey("cmplHdlYn") || "N".equals(configMap.get("cmplHdlYn")))))
					throw new CmException("wh.jsp.not.cmpl", request); // wh.jsp.not.cmpl='진행' 상태에서만 '완료'로 변경이 가능합니다.				
			}else{
				// wh.jsp.not.cmpl='진행' 상태에서만 '완료'로 변경이 가능합니다.
				throw new CmException("wh.jsp.not.cmpl", request);
			}
		}
	};
	
	/** 저장 - 진행중 [유효성 검증 후 저장]*/
	public void saveProgress(HttpServletRequest request, QueryQueue queryQueue, 
			Map<String,Object> paramMap, String reqNo, String statCd) throws SQLException, CmException{
		// 상태코드 검증
		chkSaveStatCd(request, reqNo, statCd);
		
		WhReqOngdDVo whReqOngdDVo = new WhReqOngdDVo();
		whCmSvc.setParamToVo(paramMap, whReqOngdDVo);
		
		// 진행상세 저장
		saveProgress(request, queryQueue, null, whReqOngdDVo);
		
		// 진행처리 이력 저장
		if(statCd!=null && "P".equals(statCd)){
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			paramMap.put("regrUid", userVo.getUserUid());
			saveProgressHst(request, paramMap, queryQueue, reqNo, null);
		}
		
	}
	
	/** 저장 - 진행중 [VO]*/
	public void saveProgress(HttpServletRequest request, QueryQueue queryQueue, String reqNo, WhReqOngdDVo whReqOngdDVo){
		// 진행 요청 상세
		if(whReqOngdDVo==null){
			whReqOngdDVo = new WhReqOngdDVo();		
			VoUtil.bind(request, whReqOngdDVo);
		}
		if(reqNo!=null && (whReqOngdDVo.getReqNo()==null || whReqOngdDVo.getReqNo().isEmpty()))
			whReqOngdDVo.setReqNo(reqNo);
		if(whReqOngdDVo.getEvalYn()==null || whReqOngdDVo.getEvalYn().isEmpty())
			whReqOngdDVo.setEvalYn("N");
		queryQueue.store(whReqOngdDVo);
	}
	
	/** 저장 - 진행중 이력 [VO]*/
	public void saveProgressHst(HttpServletRequest request, Map<String,Object> paramMap, QueryQueue queryQueue, String reqNo, WhReqOngdHVo whReqOngdHVo) throws SQLException{
		// 진행 요청 상세
		if(whReqOngdHVo==null){
			whReqOngdHVo = new WhReqOngdHVo();
			if(paramMap!=null)
				whCmSvc.setParamToVo(paramMap, whReqOngdHVo);
			else
				VoUtil.bind(request, whReqOngdHVo);
		}
		if(reqNo!=null && (whReqOngdHVo.getReqNo()==null || whReqOngdHVo.getReqNo().isEmpty()))
			whReqOngdHVo.setReqNo(reqNo);
		if(whReqOngdHVo.getRegDt()==null || whReqOngdHVo.getRegDt().isEmpty())
			whReqOngdHVo.setRegDt("sysdate");
		
		if(whReqOngdHVo.getReqNo()==null || whReqOngdHVo.getReqNo().isEmpty())
			return;
		
		if(whReqOngdHVo.getHstNo()==null || whReqOngdHVo.getHstNo().isEmpty())
			whReqOngdHVo.setHstNo(whCmSvc.createNo("WH_REQ_ONGD_H").toString());
		queryQueue.store(whReqOngdHVo);
	}
	
	/** 저장 - 완료 */
	public void saveComplete(HttpServletRequest request, QueryQueue queryQueue, String reqNo, WhReqCmplDVo whReqCmplDVo){
		// 진행 요청 완료
		if(whReqCmplDVo==null){
			whReqCmplDVo = new WhReqCmplDVo();
			VoUtil.bind(request, whReqCmplDVo);
		}
		if(reqNo!=null && (whReqCmplDVo.getReqNo()==null || whReqCmplDVo.getReqNo().isEmpty()))
			whReqCmplDVo.setReqNo(reqNo);
		if(whReqCmplDVo.getHdlCost()!=null && !whReqCmplDVo.getHdlCost().isEmpty()) // 숫자 콤마 제거
			whReqCmplDVo.setHdlCost(whReqCmplDVo.getHdlCost().replaceAll(",", ""));
		queryQueue.store(whReqCmplDVo);
	}
	
	/** 저장 - HTML 여부 */
	public void saveHtmlYn(HttpServletRequest request, QueryQueue queryQueue, String reqNo, String statCd, WhReqStatDVo whReqStatDVo){
		// 요청 상태 상세
		if(whReqStatDVo==null){
			whReqStatDVo = new WhReqStatDVo();
			VoUtil.bind(request, whReqStatDVo);
		}
		if(reqNo!=null && (whReqStatDVo.getReqNo()==null || whReqStatDVo.getReqNo().isEmpty()))
			whReqStatDVo.setReqNo(reqNo);
		if(statCd!=null)
			whReqStatDVo.setStatCd(statCd);
		if(whReqStatDVo.getHtmlYn()==null || whReqStatDVo.getHtmlYn().isEmpty())
			whReqStatDVo.setHtmlYn("N");
		queryQueue.store(whReqStatDVo);
	}
	
	/** 저장 - 요청관련상세 */
	public void saveWhReqRelDVo(HttpServletRequest request, QueryQueue queryQueue, String reqNo){		
		String relReqModified = (String)request.getParameter("relReqModified");
		// 전송된 것이 없으면
		if(relReqModified==null || !"Y".equals(relReqModified)) return;
		
		WhReqRelDVo whReqRelDVo = new WhReqRelDVo();
		whReqRelDVo.setReqNo(reqNo);
		queryQueue.delete(whReqRelDVo); // 기존 관련요청 삭제
		
		String[] relReq = request.getParameterValues("relReq");
		if(relReq==null) return;
		
		Integer sortOrdr=0;
		JSONObject jsonObject;
		for(String json : relReq){
			jsonObject = (JSONObject)JSONValue.parse(json);
			whReqRelDVo = new WhReqRelDVo();
			whReqRelDVo.setReqNo(reqNo);
			whReqRelDVo.setRelNo((String)jsonObject.get("reqNo"));
			whReqRelDVo.setSortOrdr(++sortOrdr);
			queryQueue.insert(whReqRelDVo);
		}
	}
	
	/** 저장 - 요청 결과평가 */
	public void saveReqEval(HttpServletRequest request, QueryQueue queryQueue, String reqNo, WhReqEvalDVo whReqEvalDVo){
		// 요청결과 상세
		if(whReqEvalDVo==null){
			whReqEvalDVo = new WhReqEvalDVo();		
			VoUtil.bind(request, whReqEvalDVo);
		}
		if(reqNo!=null && (whReqEvalDVo.getReqNo()==null || whReqEvalDVo.getReqNo().isEmpty()))
			whReqEvalDVo.setReqNo(reqNo);
		queryQueue.store(whReqEvalDVo);
	}
	
	/** 조회 - 진행중 */
	public WhReqOngdDVo getReqOngdDVo(String reqNo) throws SQLException{
		WhReqOngdDVo whReqOngdDVo = new WhReqOngdDVo();
		whReqOngdDVo.setReqNo(reqNo);
		return (WhReqOngdDVo)commonDao.queryVo(whReqOngdDVo);
	}
	
	/** 조회 - 진행중 이력 */
	public WhReqOngdHVo getWhReqOngdHVo(String langTypCd, String reqNo, String hstNo) throws SQLException{
		WhReqOngdHVo whReqOngdHVo = new WhReqOngdHVo();
		whReqOngdHVo.setQueryLang(langTypCd);
		whReqOngdHVo.setReqNo(reqNo);
		whReqOngdHVo.setHstNo(hstNo);
		return (WhReqOngdHVo)commonDao.queryVo(whReqOngdHVo);
	}
	
	/** 조회 - 완료 */
	public WhReqCmplDVo getReqCmplDVo(String reqNo) throws SQLException{
		WhReqCmplDVo whReqCmplDVo = new WhReqCmplDVo();
		whReqCmplDVo.setReqNo(reqNo);
		return (WhReqCmplDVo)commonDao.queryVo(whReqCmplDVo);
	}
	
	/** 조회 - 완료 */
	public WhReqCmplDVo getReqCmplDVo(String reqNo, boolean withLob) throws SQLException{
		WhReqCmplDVo whReqCmplDVo = new WhReqCmplDVo();
		whReqCmplDVo.setReqNo(reqNo);
		whReqCmplDVo.setWithLob(withLob);
		return (WhReqCmplDVo)commonDao.queryVo(whReqCmplDVo);
	}
	
	/** 조회 - 완료 */
	public WhReqCmplDVo getReqCmplDVo(String langTypCd, String reqNo, String hstNo, boolean withLob) throws SQLException{
		WhReqCmplDVo whReqCmplDVo = new WhReqCmplDVo();
		whReqCmplDVo.setQueryLang(langTypCd);
		whReqCmplDVo.setReqNo(reqNo);
		whReqCmplDVo.setHstNo(hstNo);
		whReqCmplDVo.setWithLob(withLob);
		whReqCmplDVo.setHst(true);
		return (WhReqCmplDVo)commonDao.queryVo(whReqCmplDVo);
	}
	
	/** 조회 - 상태상세(HTML여부) */
	public WhReqStatDVo getWhReqStatDVo(String reqNo, String statCd) throws SQLException{
		WhReqStatDVo whReqStatDVo = new WhReqStatDVo();
		whReqStatDVo.setReqNo(reqNo);
		whReqStatDVo.setStatCd(statCd);
		return (WhReqStatDVo)commonDao.queryVo(whReqStatDVo);
	}
	
	/** 조회 - 상태상세(HTML여부) */
	public boolean isHtml(String reqNo, String statCd) throws SQLException{
		WhReqStatDVo whReqStatDVo = new WhReqStatDVo();
		whReqStatDVo.setReqNo(reqNo);
		whReqStatDVo.setStatCd(statCd);
		whReqStatDVo=(WhReqStatDVo)commonDao.queryVo(whReqStatDVo);
		if(whReqStatDVo==null || whReqStatDVo.getHtmlYn()==null)
			return false;
		return "Y".equals(whReqStatDVo.getHtmlYn());
	}
	
	/** 조회 - 요청관련상세 여부 */
	public boolean isReqRel(String reqNo) throws SQLException{		
		WhReqRelDVo whReqRelDVo = new WhReqRelDVo();
		whReqRelDVo.setReqNo(reqNo);
		
		return commonDao.count(whReqRelDVo)>0;
	}
	
	/** 조회 - 결과평가 */
	public WhReqEvalDVo getReqEvalDVo(String reqNo) throws SQLException{
		WhReqEvalDVo whReqEvalDVo = new WhReqEvalDVo();
		whReqEvalDVo.setReqNo(reqNo);
		return (WhReqEvalDVo)commonDao.queryVo(whReqEvalDVo);
	}
	
	/** 요청 삭제 */
	public void deleteReq(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String reqNo, List<CommonFileVo> deletedFileList, boolean isAdmin) throws SQLException, CmException{
		
		WhReqOngdDVo whReqOngdDVo = getReqOngdDVo(reqNo);
		if(whReqOngdDVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		String statCd=whReqOngdDVo.getStatCd(); // 현재 상태
		if(!isAdmin && !"R".equals(statCd) && !"T".equals(statCd) && !"G".equals(statCd)){
			// wh.msg.not.delete=진행중인 요청은 삭제할 수 없습니다.
			throw new CmException("wh.msg.not.delete", request);
		}
		
		if("C".equals(statCd)){
			// 평가상세 삭제
			if(whReqOngdDVo.getEvalYn()!=null && "Y".equals(whReqOngdDVo.getEvalYn())){
				WhReqEvalDVo whReqEvalDVo = new WhReqEvalDVo();
				whReqEvalDVo.setReqNo(reqNo);
				queryQueue.delete(whReqEvalDVo);
			}
			
			// 완료상세 삭제
			deleteFile(queryQueue, deletedFileList, reqNo, "C");
			WhReqCmplDVo whReqCmplDVo = new WhReqCmplDVo();
			whReqCmplDVo.setReqNo(reqNo);
			queryQueue.delete(whReqCmplDVo);
		}
		
		// 요청상태 상세 삭제
		WhReqStatDVo whReqStatDVo = new WhReqStatDVo();
		whReqStatDVo.setReqNo(reqNo);
		queryQueue.delete(whReqStatDVo);
		
		// 요청관련상세 삭제
		WhReqRelDVo whReqRelDVo = new WhReqRelDVo();
		whReqRelDVo.setReqNo(reqNo);
		queryQueue.delete(whReqRelDVo);
		
		// 진행상세 삭제
		//deleteFile(queryQueue, deletedFileList, reqNo, "P"); 추후 파일 추가시 주석 제거
		whReqOngdDVo = new WhReqOngdDVo();
		whReqOngdDVo.setReqNo(reqNo);
		queryQueue.delete(whReqOngdDVo);		
		
		// 요청기본 삭제
		deleteFile(queryQueue, deletedFileList, reqNo, "R");
		WhReqBVo whReqBVo = new WhReqBVo();
		whReqBVo.setReqNo(reqNo);
		
		queryQueue.delete(whReqBVo);
	}
	
	/** 파일 삭제 */
	public void deleteFile(QueryQueue queryQueue, List<CommonFileVo> deletedFileList, String reqNo, String typCd) throws SQLException{
		List<CommonFileVo> list=null;
		if(deletedFileList!=null){
			list=whFileSvc.deleteReqFile(reqNo, queryQueue, typCd);
			if(list!=null && list.size()>0) deletedFileList.addAll(list);
		}else{
			// 파일 삭제
			whFileSvc.deleteReqFile(reqNo, queryQueue, typCd);
		}
	}
	
	/** 결과평가 삭제 */
	public void deleteEvalList(HttpServletRequest request, QueryQueue queryQueue, String reqNo) throws SQLException, CmException{
		// 결과평가 삭제
		WhReqEvalDVo whReqEvalDVo = new WhReqEvalDVo();
		whReqEvalDVo.setReqNo(reqNo);
		queryQueue.delete(whReqEvalDVo);
		
		// 진행상세 변경
		saveProgress(request, queryQueue, reqNo, null);
	}
	
	/** 날짜 추가 - 예정일 계산 */
	public String getDateToAdd(String paramDay, Integer addDay, boolean isSubtract){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar cal = new GregorianCalendar();
		if(paramDay!=null){
			paramDay=paramDay.replaceAll("[-: ]", "");
			cal.set(Integer.parseInt(paramDay.substring(0, 4)) , Integer.parseInt(paramDay.substring(4, 6))-1 , Integer.parseInt(paramDay.substring(6, 8)));
		}
 		cal.add(Calendar.DATE, isSubtract ? -addDay.intValue() : addDay.intValue());
 		return sdf.format(cal.getTime());
	}
	
	/** 컬럼 목록 조회 */
	public String[][] getColAllList(){
		String[][] colList = new String[][]{
				{"docNo","wh.cols.docNo","1"},
				{"reqDt","wh.cols.reqYmd","2"},
				{"deptNm","wh.cols.req.deptNm","3"},
				{"mdNm","wh.cols.md.nm","4"},
				{"progrmNm","wh.cols.req.progrmNm","5"},
				{"regrNm","wh.cols.req.reqr","6"},
				{"cont","cols.cont","7"},
				{"hdlCont","wh.cols.hdl.hdlCont","8"},
				{"hdlrNm","wh.cols.hdl.pich","9"},
				{"cmplDt","wh.cols.req.cmplDt","10"},
				{"devHourVa","wh.cfg.devHour","11"}
				};
		
		return colList;
	}
	
	/** 엑셀 컬럼 속성ID 목록 조회 */
	public List<String> getLstColAtrbList(){
		List<String> colAtrbList = new ArrayList<String>();
		String[][] allList = getColAllList();
		for(String[] col : allList){
			colAtrbList.add(col[0]);
		}
		
		return colAtrbList;
	}
	
	/** 엑셀 컬럼 속성명 목록 조회 */
	public List<String> getLstColNmList(HttpServletRequest request){
		List<String> colNmList = new ArrayList<String>();
		String[][] allList = getColAllList();
		for(String[] col : allList){
			colNmList.add(messageProperties.getMessage(col[1], request));
		}
		return colNmList;
	}
	
	/** 요청 메일 내용 */
	public String getReqMailHTML(HttpServletRequest request, WhReqBVo whReqBVo, String htmlYn, String msgUrl){
		
		StringBuilder builder = new StringBuilder(1024);
		
		builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">\n");
		builder.append("<table style=\"border:0px\" border=\"0\">\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("cols.subj", request)).append("</strong></td>\n");//제목
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		if(msgUrl==null || msgUrl.isEmpty()){
			builder.append(whReqBVo.getSubj());
		} else {
			builder.append("<a href=\"").append(msgUrl).append("\" target=\"_top\">").append(whReqBVo.getSubj()).append("</a>");
		}
		builder.append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.jsp.sysMd.title", request)).append("</strong></td>\n");// 시스템 모듈
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(whReqBVo.getMdNm()).append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.cols.req.reqYmd", request)).append("</strong></td>\n");// 요청일
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(StringUtil.toShortDate(whReqBVo.getReqDt())).append("</td></tr>\n");
	
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.cols.req.cmplYmd", request)).append("</strong></td>\n");// 완료희망일
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(StringUtil.toShortDate(whReqBVo.getCmplPdt())).append("</td></tr>\n");
		
		if(whReqBVo.getCont()!=null){
			builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			builder.append("<td colspan=\"3\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(htmlYn!=null && "Y".equals(htmlYn) ? EscapeUtil.escapeValue(whReqBVo.getCont()) : EscapeUtil.escapeHTML(whReqBVo.getCont())).append("</td>");
		}
		builder.append("</tr>\n");
		
		builder.append("</table>\n");
		builder.append("</p>\n<br/><br/>\n");
		
		return builder.toString();
	}
	
	/** 완료 메일 내용 */
	public String getCmplMailHTML(HttpServletRequest request, WhReqBVo whReqBVo, String htmlYn, String msgUrl){
		
		StringBuilder builder = new StringBuilder(1024);
		
		builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">\n");
		builder.append("<table style=\"border:0px\" border=\"0\">\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("cols.subj", request)).append("</strong></td>\n");//제목
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		if(msgUrl==null || msgUrl.isEmpty()){
			builder.append(whReqBVo.getSubj());
		} else {
			builder.append("<a href=\"").append(msgUrl).append("\" target=\"_top\">").append(whReqBVo.getSubj()).append("</a>");
		}
		builder.append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.jsp.sysMd.title", request)).append("</strong></td>\n");// 시스템 모듈
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(whReqBVo.getMdNm()).append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.cols.hdl.typ", request)).append("</strong></td>\n");// 처리유형
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(whReqBVo.getCatNm()).append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.cols.hdl.pich", request)).append("</strong></td>\n");// 처리 담당자
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(whReqBVo.getHdlrNm()).append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.cols.req.reqYmd", request)).append("</strong></td>\n");//요청일
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(StringUtil.toShortDate(whReqBVo.getReqDt())).append("</td></tr>\n");
	
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("wh.cols.req.cmplDt", request)).append("</strong></td>\n");//완료일
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(StringUtil.toShortDate(whReqBVo.getCmplDt())).append("</td></tr>\n");
		
		if(whReqBVo.getHdlCont()!=null){
			builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			builder.append("<td colspan=\"3\" style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(htmlYn!=null && "Y".equals(htmlYn) ? EscapeUtil.escapeValue(whReqBVo.getHdlCont()) : EscapeUtil.escapeHTML(whReqBVo.getHdlCont())).append("</td>");
		}
		builder.append("</tr>\n");
		
		builder.append("</table>\n");
		builder.append("</p>\n<br/><br/>\n");
		
		return builder.toString();
	}
	
	
}
