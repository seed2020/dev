package com.innobiz.orange.web.wc.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.vo.DmCommFileDVo;
import com.innobiz.orange.web.em.vo.CmEmailBVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.calender.WcScdCalDay;
import com.innobiz.orange.web.wc.calender.WcScdCalMonth;
import com.innobiz.orange.web.wc.calender.WcScdCalWeek;
import com.innobiz.orange.web.wc.util.LunalCalenderUtil;
import com.innobiz.orange.web.wc.vo.WcAgntRVo;
import com.innobiz.orange.web.wc.vo.WcAnnvDVo;
import com.innobiz.orange.web.wc.vo.WcApntrRVo;
import com.innobiz.orange.web.wc.vo.WcApntrSchdlDVo;
import com.innobiz.orange.web.wc.vo.WcBumkDVo;
import com.innobiz.orange.web.wc.vo.WcErpSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcMailSchdlRVo;
import com.innobiz.orange.web.wc.vo.WcNatBVo;
import com.innobiz.orange.web.wc.vo.WcPromGuestDVo;
import com.innobiz.orange.web.wc.vo.WcRepetSetupDVo;
import com.innobiz.orange.web.wc.vo.WcSchdlBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlDeptRVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpBVo;
import com.innobiz.orange.web.wc.vo.WcSchdlGrpMbshDVo;

/** 일정을 조회 저장 수정 하는 서비스 */
@Service
public class WcScdManagerSvc {
	
	private final String dftNatCd = "KR";
	
	/** 파일업로드 태그ID */
	public static final String FILES_ID = "bcfiles";

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** schdlID생성 서비스*/
	@Autowired
	private WcCmSvc wcCmSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 게시파일 서비스 */
	@Autowired
	private WcFileSvc wcFileSvc;
	
	@Autowired
	private LunalCalenderUtil lunalCalenderUtil;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
//	/** 시스템설정 서비스 */
//	@Autowired
//	private PtSysSvc ptSysSvc;
	
//	/** 암호화 서비스 */
//	@Autowired
//	private CryptoSvc cryptoSvc;
	
	/** 공통 기념일 조회 */
	public Map<String,Object> getCommAnnv(HttpServletRequest request, WcSchdlBVo wcSchdlBVo, ModelMap model) throws SQLException{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
//		Integer recodeCount = this.getWcSchdlBListCnt(wcSchdlBVo);
		
		WcSchdlBVo conditionScd = new WcSchdlBVo();
		conditionScd.setInstanceQueryId("countCommonAnnv");
		conditionScd.setSchdlTypCd("5");
		if(wcSchdlBVo.getSchCompId() != null && !wcSchdlBVo.getSchCompId().isEmpty()) conditionScd.setCompId(wcSchdlBVo.getSchCompId());
		Integer recodeCount = this.getWcSchdlBListCnt(conditionScd);
	
		PersonalUtil.setPaging(request, wcSchdlBVo, recodeCount);
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> commAnnvList = (List<WcSchdlBVo>) commonDao.queryList(wcSchdlBVo);
		// 기념일 상세
		WcAnnvDVo wcAnnvDVo = null;
		// 국가기본
		WcNatBVo wcNatBVo = null;
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		for(WcSchdlBVo storedWcSchdlBVo : commAnnvList){
			wcAnnvDVo = new WcAnnvDVo();
			wcAnnvDVo.setQueryLang(langTypCd);
			wcAnnvDVo.setCompId(storedWcSchdlBVo.getCompId());
			wcAnnvDVo.setSchdlId(storedWcSchdlBVo.getSchdlId());
			wcAnnvDVo = (WcAnnvDVo)commonSvc.queryVo(wcAnnvDVo);
			if(wcAnnvDVo != null) {
				model.put("wcAnnvDVo"+storedWcSchdlBVo.getSchdlId(), wcAnnvDVo);
				// 국가 목록
				wcNatBVo = new WcNatBVo();
				wcNatBVo.setQueryLang(langTypCd);
				wcNatBVo.setCompId(storedWcSchdlBVo.getCompId());
				wcNatBVo.setCd(wcAnnvDVo.getNatCd());
				model.put("wcNatBVo"+storedWcSchdlBVo.getSchdlId(), commonSvc.queryVo(wcNatBVo));
				
			}
		}
		
		rsltMap.put("commAnnvList", commAnnvList);
		rsltMap.put("recodeCount", recodeCount);
		return rsltMap;
	}
	
	/** 공통 기념일수 조회 */
	public Integer getWcSchdlBListCnt(WcSchdlBVo wcSchdlBVo) throws SQLException{
		return commonDao.count(wcSchdlBVo);
	}
	

	/** 그룹 등록 */
	public void setWcSchdlGrpReg(HttpServletRequest request, WcSchdlGrpBVo wcSchdlGrpBVo) throws SQLException{
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date currentTime = new Date ( );
		
		QueryQueue queryQueue = new QueryQueue();
		
		String mTime = mSimpleDateFormat.format ( currentTime );
		String[] userUidArr = request.getParameterValues("userUid");
		String[] rescNmArr = request.getParameterValues("rescNm"); 
		
		wcSchdlGrpBVo.setSchdlGrpId(wcCmSvc.createId("WC_SCHDL_GRP_B"));
		wcSchdlGrpBVo.setGrpNm(request.getParameter("grpNm"));
		wcSchdlGrpBVo.setRegDt(mTime);
		queryQueue.insert(wcSchdlGrpBVo);
		
//		wcSchdlGrpMbshDVo.setGrpMbshList(userUidArr);
		if(userUidArr != null){
			for(int i =0; i<userUidArr.length; i++){
				WcSchdlGrpMbshDVo grpMbshDVo = new WcSchdlGrpMbshDVo();
				grpMbshDVo.setCompId(wcSchdlGrpBVo.getCompId());
				grpMbshDVo.setMbshTypCd("M");
				grpMbshDVo.setSchdlGrpId(wcSchdlGrpBVo.getSchdlGrpId());
				grpMbshDVo.setUserNm(rescNmArr[i]);
				grpMbshDVo.setUserUid(userUidArr[i]);
				if(wcSchdlGrpBVo.getUserUid().equals(userUidArr[i])){
					continue;
				}
				grpMbshDVo.setAuthGradCd(request.getParameter("defAuth"));
				grpMbshDVo.setEmailSendYn(request.getParameter("emailSendYn"));
				queryQueue.insert(grpMbshDVo);
			}
		}

		
		
		WcSchdlGrpMbshDVo wcThis = new WcSchdlGrpMbshDVo();
		wcThis.setSchdlGrpId(wcSchdlGrpBVo.getSchdlGrpId());
		wcThis.setCompId(wcSchdlGrpBVo.getCompId());
		wcThis.setUserUid(wcSchdlGrpBVo.getUserUid());
		wcThis.setAuthGradCd("A");
		wcThis.setMbshTypCd("O");
		wcThis.setEmailSendYn("Y");
		wcThis.setUserNm(wcSchdlGrpBVo.getRegrNm());
		queryQueue.insert(wcThis);

		commonDao.execute(queryQueue);
	}
	
	/** 그룹 수정 */
	public void setWcSchdlGrpMod(HttpServletRequest request, WcSchdlGrpBVo wcSchdlGrpBVo) throws SQLException{
		
		QueryQueue queryQueue = new QueryQueue();

		String[] userUidArr = request.getParameterValues("userUid");
		String[] rescNmArr = request.getParameterValues("rescNm"); 
		
		wcSchdlGrpBVo.setSchdlGrpId(request.getParameter("grpId"));
		wcSchdlGrpBVo.setGrpNm(request.getParameter("grpNm"));
		queryQueue.update(wcSchdlGrpBVo);
		
		/*WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = new WcSchdlGrpMbshDVo();
		wcSchdlGrpMbshDVo.setSchdlGrpId(wcSchdlGrpBVo.getSchdlGrpId());
		wcSchdlGrpMbshDVo.setGrpMbshList(userUidArr);
		queryQueue.delete(wcSchdlGrpMbshDVo);*/
		
		UserVo userVo = LoginSession.getUser(request);
		
		if(userUidArr != null)
		{
			for(int i=0; i<userUidArr.length; i++){
				WcSchdlGrpMbshDVo wscVo = new WcSchdlGrpMbshDVo();
				wscVo.setCompId(userVo.getCompId());
				wscVo.setMbshTypCd("M");
				wscVo.setSchdlGrpId(wcSchdlGrpBVo.getSchdlGrpId());				
				wscVo.setUserUid(userUidArr[i]);
				if(commonDao.count(wscVo)>0) continue;				
				wscVo.setUserNm(rescNmArr[i]);
				wscVo.setAuthGradCd(request.getParameter("defaultAuth"));
				wscVo.setEmailSendYn(request.getParameter("emailSendYn"));
				queryQueue.insert(wscVo);
			}
		}
		
		commonDao.execute(queryQueue);
	}
	
	/** 마스터 Uid 조회*/
	public WcSchdlGrpMbshDVo findMastUser(HttpServletRequest request, WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo) throws SQLException{
	
		wcSchdlGrpMbshDVo.setUserUid(request.getParameter("mastUid"));
		wcSchdlGrpMbshDVo.setSchdlGrpId(request.getParameter("schdlGrpId"));
		WcSchdlGrpMbshDVo schdlGrpMast = (WcSchdlGrpMbshDVo) commonDao.queryVo(wcSchdlGrpMbshDVo);
		
		return schdlGrpMast;
	}
	
	/** 마스터 수정*/
	public void updateGrpMast(HttpServletRequest request, WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo, WcSchdlGrpBVo wcSchdlGrpBVo) throws SQLException{
		
		UserVo userVo = LoginSession.getUser(request);
		wcSchdlGrpMbshDVo.setCompId(userVo.getCompId());
		wcSchdlGrpBVo.setCompId(userVo.getCompId());
		
		wcSchdlGrpMbshDVo.setUserUid(request.getParameter("userUid"));
		wcSchdlGrpMbshDVo.setUserNm(request.getParameter("rescNm"));
		wcSchdlGrpMbshDVo.setSchdlGrpId(request.getParameter("schdlGrpId"));
		wcSchdlGrpMbshDVo.setMbno(request.getParameter("mbno"));
		commonDao.delete(wcSchdlGrpMbshDVo);
		
		wcSchdlGrpMbshDVo.setMbshTypCd("O");
		wcSchdlGrpMbshDVo.setAuthGradCd("A");
		//wcSchdlGrpMbshDVo.setEmailSendYn("Y"); 이메일 발송여부는 아직 모르겠다.
		
		commonDao.insert(wcSchdlGrpMbshDVo);
		
		wcSchdlGrpBVo.setSchdlGrpId(request.getParameter("schdlGrpId"));
		wcSchdlGrpBVo.setMastrUid(request.getParameter("userUid"));
		wcSchdlGrpBVo.setRegrNm(request.getParameter("rescNm"));
		commonDao.update(wcSchdlGrpBVo);
		
		WcSchdlGrpMbshDVo prevMastVo = new WcSchdlGrpMbshDVo();
		prevMastVo.setAuthGradCd(request.getParameter("userSecuCd"));
		prevMastVo.setSchdlGrpId(request.getParameter("schdlGrpId"));
		prevMastVo.setUserUid(request.getParameter("prevMastUid"));
		prevMastVo.setMbshTypCd("M");
		commonDao.update(prevMastVo);
	}
	
	/** 그룹 목록 조회*/
	@SuppressWarnings("unchecked")
	public List<WcSchdlGrpBVo> getWcSchdlGroupList(WcSchdlGrpBVo wcSchdlGrpBVo) throws SQLException {
		return (List<WcSchdlGrpBVo>)commonDao.queryList(wcSchdlGrpBVo);
	}

	/** 그룹 목록수 조회 */
	public Integer getWcSchdlGroupListCnt(WcSchdlGrpBVo wcSchdlGrpBVo) throws SQLException {
		return commonDao.count(wcSchdlGrpBVo);
	}
	
	/** 그룹 목록 */
	public Map<String,Object> getWcGroupMapList(HttpServletRequest request , WcSchdlGrpBVo wcSchdlGrpBVo) throws SQLException, CmException, IOException {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//String schWord = wcSchdlGrpBVo.getSchWord();//검색어
		//String fncMy = wcSchdlGrpBVo.getFncMy();//그룹설정
		
		UserVo userVo = LoginSession.getUser(request);
		
	
		Integer recodeCount = this.getWcSchdlGroupListCnt(wcSchdlGrpBVo);
		PersonalUtil.setPaging(request, wcSchdlGrpBVo, recodeCount);
		
		//목록 조회
		List<WcSchdlGrpBVo> wcSchdlGroupBVoList = this.getWcSchdlGroupList(wcSchdlGrpBVo);
		
		Map<String, Object> wcSchdlGroupMap;
		List<Map<String, Object>> wcSchdlGroupBMapList = new ArrayList<Map<String, Object>>();
		for(WcSchdlGrpBVo storedWcSchdlGroupBVo : wcSchdlGroupBVoList){
			Map<String, Object> schdlGrpMbshMap = orCmSvc.getUserMap(storedWcSchdlGroupBVo.getMastrUid(), langTypCd);
			if(schdlGrpMbshMap==null) continue;
			storedWcSchdlGroupBVo.setRegrNm((String) schdlGrpMbshMap.get("rescNm"));
			
			if(storedWcSchdlGroupBVo.getMastrUid().equals(userVo.getUserUid())){
				storedWcSchdlGroupBVo.setAuth("MNG");
			}
			if(userVo.getAdminAuthGrpIds()!=null&&userVo.getAdminAuthGrpIds().length>0){
				storedWcSchdlGroupBVo.setAuth("MNG");
			}
			if(storedWcSchdlGroupBVo.getAuthGradCd()!=null&&storedWcSchdlGroupBVo.getAuthGradCd().equals("A")){
				storedWcSchdlGroupBVo.setAuth("MNG");
			}
			
			wcSchdlGroupMap = VoUtil.toMap(storedWcSchdlGroupBVo, null);
			wcSchdlGroupBMapList.add(wcSchdlGroupMap);
		}
		rsltMap.put("wcSchdlGroupBMapList", wcSchdlGroupBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	/** 그릅 회원 목록 조회*/
	@SuppressWarnings("unchecked")
	public List<WcSchdlGrpMbshDVo> getWcSchdlGroupMbshList(WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo) throws SQLException{
		return (List<WcSchdlGrpMbshDVo>) commonDao.queryList(wcSchdlGrpMbshDVo);
	}
	
	/** 그룹 회원수 조회 */
	public Integer getWcSchdlGroupMbshListCnt(WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo) throws SQLException{
		return commonDao.count(wcSchdlGrpMbshDVo);
		
	}
	
	/** 그룹 회원 목록 조회 */
	public Map<String,Object> getWcSchdlGrpMbshList(HttpServletRequest request, WcSchdlGrpMbshDVo wcSchdlGrpMbshDvo) throws SQLException, CmException, IOException{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		String behaveValue = request.getParameter("behave");
		//String schWord = wcSchdlGrpMbshDvo.getSchWord();//검색어
		//String mastUid = request.getParameter("mastUid");
		String schdlId;
		
		if(behaveValue.equals("N") || behaveValue.equals("search")){
			schdlId = request.getParameter("schdlGrpId");
		}else{
			schdlId = wcSchdlGrpMbshDvo.getSchdlGrpId();
			wcSchdlGrpMbshDvo.setUserUid(null);
		}
		wcSchdlGrpMbshDvo.setSchdlGrpId(schdlId);
		Integer recodeCount = this.getWcSchdlGroupMbshListCnt(wcSchdlGrpMbshDvo);
		PersonalUtil.setPaging(request, wcSchdlGrpMbshDvo, recodeCount);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//목록 조회
		List<WcSchdlGrpMbshDVo> wcSchdlGroupMbshDVoList = this.getWcSchdlGroupMbshList(wcSchdlGrpMbshDvo);
		
		Map<String, Object> wcSchdlGroupMbshMap;
		List<Map<String, Object>> wcSchdlGroupMbshDMapList = new ArrayList<Map<String, Object>>();
		for(WcSchdlGrpMbshDVo storedWcSchdlGroupBVo : wcSchdlGroupMbshDVoList){
			Map<String, Object> schdlGrpMbshMap = orCmSvc.getUserMap(storedWcSchdlGroupBVo.getUserUid(), langTypCd);
			if(schdlGrpMbshMap==null) continue;
			storedWcSchdlGroupBVo.setDeptRescNm((String) schdlGrpMbshMap.get("deptRescNm"));
			storedWcSchdlGroupBVo.setGradeNm((String) schdlGrpMbshMap.get("gradeNm"));
			storedWcSchdlGroupBVo.setMbno((String) schdlGrpMbshMap.get("mbno"));
			storedWcSchdlGroupBVo.setUserNm((String) schdlGrpMbshMap.get("rescNm"));
			wcSchdlGroupMbshMap = VoUtil.toMap(storedWcSchdlGroupBVo, null);
			wcSchdlGroupMbshDMapList.add(wcSchdlGroupMbshMap);
		}
		rsltMap.put("wcSchdlGroupMbshDMapList", wcSchdlGroupMbshDMapList);
		rsltMap.put("mastUid", request.getParameter("mastUid"));
		//rsltMap.put("schdlGrpId", request.getParameter("schdlGroupId"));
		rsltMap.put("schdlGrpId", wcSchdlGrpMbshDvo.getSchdlGrpId());
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	/** 그룹 멤버 권한 설정 */
	public void updateGrpMbshAuthGrad(HttpServletRequest request, WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo, QueryQueue queryQueue) throws SQLException{
		String[] userUidArr = request.getParameterValues("authSetUserUid");
		
		wcSchdlGrpMbshDVo.setAuthGradCd(request.getParameter("authGrad"));
		wcSchdlGrpMbshDVo.setSchdlGrpId(request.getParameter("authSetUserSchdlGrpId"));
		wcSchdlGrpMbshDVo.setGrpMbshList(userUidArr);
		
		queryQueue.update(wcSchdlGrpMbshDVo);
		commonDao.execute(queryQueue);
	}
	
	/** 그룹 멤버 delete */
	public void delGrpMbsh(HttpServletRequest request, WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo, QueryQueue queryQueue) throws SQLException{
		String[] userUidArr = request.getParameterValues("delUserUid");
		
		wcSchdlGrpMbshDVo.setSchdlGrpId(request.getParameter("delUserSchdlGrpId"));
		wcSchdlGrpMbshDVo.setGrpMbshList(userUidArr);
		
		queryQueue.delete(wcSchdlGrpMbshDVo);
		commonDao.execute(queryQueue);
	}
	
	/** 그룹 멤버 add */
	public WcSchdlGrpMbshDVo setWcSchldGrpMbshList(HttpServletRequest request, WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo, QueryQueue queryQueue) throws SQLException{

		//wcSchdlGrpMbshDVo.setUserUid(request.getParameter("rescId"));
		//wcSchdlGrpMbshDVo.setUserNm(request.getParameter("rescNm"));
		
		String[] userUidArr = request.getParameterValues("userUid");
		String[] rescNmArr = request.getParameterValues("rescNm");
		
		wcSchdlGrpMbshDVo.setGrpMbshList(userUidArr);
		//wcSchdlGrpMbshDVo.setPositNm(request.getParameter("positNm"));
		//wcSchdlGrpMbshDVo.setDeptRescNm(request.getParameter("deptRescNm"));
		//wcSchdlGrpMbshDVo.setMbno(request.getParameter("mbno"));
		queryQueue.delete(wcSchdlGrpMbshDVo);
		
		UserVo userVo = LoginSession.getUser(request);
		
		for(int i=0; i<userUidArr.length; i++){
			WcSchdlGrpMbshDVo wscVo = new WcSchdlGrpMbshDVo();
			wscVo.setCompId(userVo.getCompId());
			wscVo.setMbshTypCd("M");
			wscVo.setSchdlGrpId(request.getParameter("schdlGrpId"));
			wscVo.setUserNm(rescNmArr[i]);
			wscVo.setUserUid(userUidArr[i]);
			queryQueue.insert(wscVo);
		}
		commonDao.execute(queryQueue);
		return wcSchdlGrpMbshDVo;
		
	}
	
	/** 그룹 삭제*/
	public void deleteSchldGrp(WcSchdlGrpBVo wcSchdlGrpBVo, String schdlGrpId, QueryQueue queryQueue) {
		
		WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = new WcSchdlGrpMbshDVo();
		wcSchdlGrpBVo.setSchdlGrpId(schdlGrpId);
		wcSchdlGrpMbshDVo.setSchdlGrpId(schdlGrpId);
		
		queryQueue.delete(wcSchdlGrpMbshDVo);
		queryQueue.delete(wcSchdlGrpBVo);
		
//		commonDao.delete(wcSchdlGrpBVo);
		
//		queryQueue.delete(wcSchdlGrpBVo);
//		return;
	}
	
	/** 그룹 탈퇴*/
	public void withdrawSchldGrp(WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo, String schdlGrpId, QueryQueue queryQueue) {
		
		wcSchdlGrpMbshDVo.setSchdlGrpId(schdlGrpId);
		
		queryQueue.delete(wcSchdlGrpMbshDVo);
		
//		commonDao.delete(wcSchdlGrpBVo);
		
//		queryQueue.delete(wcSchdlGrpBVo);
//		return;
	}

	/** 한자리수 월 , 일에 0 조합 */
    public String getMixDt(int d){
    	return d < 10 ? "0"+ d : ""+d;
    }
	
	public WcScdCalMonth getScdMonth(int year
			,int month
			,int schdlTyp
			,String timeZone
			,String bumk
			,String agnt
			,String choiGrpIds[]
			,String viewUserUid
			,String viewOrgId
			,String userUid
			,String userDeptId
			,String userCompId
			,String catId
			,boolean withSchdl) {
		Calendar cal = Calendar.getInstance();		
		int start_day, end_day, beforeLastDay = 0;
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.YEAR, year);
		// 월은 0부터 11로 리턴되기 때문에 항상 1을 증가해야 한다. 7을 입력하면 8월이 된다.
		// 알고자 하는 달을 입력했을 경우에는 -1
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, 1);
		
		// 1~7까지 일~토
		start_day = cal.get(Calendar.DAY_OF_WEEK);
		end_day = cal.getActualMaximum(Calendar.DATE);
		
		List<WcScdCalWeek> weekObj = new ArrayList<WcScdCalWeek>();
		List<WcScdCalDay> dayObj = new ArrayList<WcScdCalDay>();
		
		//이전달 구하는 달력 생성
		//beforeLastDay : 전달 28,29,30,31 계산.
		Calendar beforeCal = Calendar.getInstance();
		int processMonth =month;
		int processYear = year;
		//month가 1월로 오면 이전 달은 12월로 셋팅 하면서  year -1 
		if(processMonth == 1){
			processYear -= 1;
			processMonth = 12;
		}else{
			processMonth -= 1;
		}
		beforeCal.set(Calendar.YEAR, processYear);
		beforeCal.set(Calendar.MONTH, processMonth-1); 
		beforeCal.set(Calendar.DATE, 1);
		beforeLastDay = beforeCal.getActualMaximum(Calendar.DATE);
		beforeLastDay = (beforeLastDay + 2) - start_day;
		
		//Month객체 생성
		WcScdCalMonth monthObj=new WcScdCalMonth();
		monthObj.setMonth(month);
		monthObj.setYear(year);
		//음,양력 
		monthObj.setSolaYN("Y");
		//스케쥴 입력
		
		
		int processWeekDay =1;
//		int sixDay = end_day + start_day ;
		int startweek=1;
		int endweek=6;
		/*int endweek=end_day/7;
		//시작날짜가 1번째로 시작되지 않거나, 마지막날의 7로 나눈 몫이 0과 같지 않다면 주차를 1주차 추가한다.
		if(end_day%7!=0||start_day>1){
			//시작일과 마지막일의 합이 36이상 넘어 가면 6주이다.
			if(sixDay > 36){
				endweek=endweek+2;
			}else{
				endweek=endweek+1;
			}
		}*/
		
		//temp값 까지 계산
		int tempDay;
		//1~마지막일까지.. day
		int day=0;
		
		//1부터 정해진 주차 까지 계산
		for(int i=startweek; i<=endweek; i++){
			for(int j=1; j<=7; j++){
				tempDay=j+((i-1)*7);
				WcScdCalDay days = new WcScdCalDay();
				days.setMonth(month);
				days.setYear(year);
				days.setSolaYN("Y");
				
				if(start_day>tempDay){
					//시작일 그 이전 날짜 채우는 구간
					//이전월 연한 빨간색 set
					if(j == 1) days.setHoliFlag("scddate_red_prev");
					else days.setHoliFlag("scddate_prev");
					days.setDay(beforeLastDay);
					days.setToDayFlag("scdtd");
					
					
					//전달 년 월 set
					days.setYear(processYear);
					days.setMonth(processMonth);
					
					days.setDays(processYear+""+getMixDt(processMonth)+""+getMixDt(beforeLastDay));
					
					//전달 ~ 전달마지막일 add
					dayObj.add(days);
					beforeLastDay++;
				}else{
					day++;
					//1일부터 end_day 구하는 구간
					if(day<=end_day){
						if(j == 1) days.setHoliFlag("scddate_red");
						else days.setHoliFlag("scddate");
						
						//today 설정
						Calendar defaultCal = Calendar.getInstance();

						int defYear = defaultCal.get(Calendar.YEAR);
						int defMonth = defaultCal.get(Calendar.MONTH) +1;
						int defDay = defaultCal.get(Calendar.DAY_OF_MONTH);
						
						//today flag 표시
						if(defYear == year && defMonth ==month &&defDay == day ) days.setToDayFlag("scd_today");
						else days.setToDayFlag("scdtd");
						
						//이번달 년 월 set
						days.setYear(year);
						days.setMonth(month);
						days.setDays(year+""+getMixDt(month)+""+getMixDt(day));
						//1~마지막일자 입력
						days.setDay(day);
						dayObj.add(days);
						
					//종료일 그 이후 채우는 구간
					}else{ 
						if(j == 1) days.setHoliFlag("scddate_red_prev");
						else days.setHoliFlag("scddate_prev");
						days.setDay(processWeekDay);
						days.setToDayFlag("scdtd");
						
						
						//다음달 년 월 set
						int nextYear =year;
						int nextMonth =month;
						if(12 >= (nextMonth + 1)) nextMonth+= 1;
						else{
							nextMonth = 1;
							nextYear += 1;
						}
						days.setYear(nextYear);
						days.setMonth(nextMonth);
						days.setDays(nextYear+""+getMixDt(nextMonth)+""+getMixDt(processWeekDay));
						//마지막일 ~ 다음달 첫주 add
						dayObj.add(days);
						processWeekDay++;
					}
				}	
				
			}
			WcScdCalWeek weeks = new WcScdCalWeek();
			weeks.setMonth(month);
			weeks.setYear(year);
			weeks.setWeek(i);
			weeks.setSolaYN("Y");
			
			weeks.setDays(dayObj);
			weekObj.add(weeks);
			dayObj = new ArrayList<WcScdCalDay>();			
		}
		
		monthObj.setWeeks(weekObj);
		
		try {
			if(withSchdl)
				getMonthSchdlList(year, month,schdlTyp,timeZone, bumk,  agnt, choiGrpIds, viewUserUid, viewOrgId, userUid,userDeptId, userCompId, monthObj,catId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return monthObj;
	}

	
	public WcScdCalWeek getScdWeek(int year, int month, int week,int schdlTyp,String timeZone, String bumk, String agnt,String choiGrpIds[], String viewUserUid,String viewOrgId,String userUid,String userDeptId,String userCompId,String catId) {
		WcScdCalMonth calMonth=getScdMonth(year,month,schdlTyp,timeZone,bumk,agnt,choiGrpIds,viewUserUid,viewOrgId,userUid,userDeptId,userCompId,catId,true);
		WcScdCalWeek calWeek=calMonth.getWeeks().get(week-1);
		
		return calWeek;
	}	
	
	public WcScdCalWeek getScdWeek(int year, int month, int week,int schdlTyp, WcScdCalMonth calMonth) {		
		WcScdCalWeek calWeek=calMonth.getWeeks().get(week-1);		
		return calWeek;
	}

	public WcScdCalDay getScdDay(int year, int month, int day,int schdlTyp,String timeZone,String bumk, String agnt,String choiGrpIds[], String viewUserUid,String viewOrgId,String userUid,String userDeptId,String userCompId,String catId) {
		WcScdCalMonth calMonth=getScdMonth(year,month,schdlTyp,timeZone,bumk,agnt,choiGrpIds,viewUserUid,viewOrgId,userUid,userDeptId,userCompId,catId,true);
		for(WcScdCalWeek calWeek:calMonth.getWeeks()){
			for(WcScdCalDay calDay:calWeek.getDays()){
				if(calDay.getMonth()==month && calDay.getDay()==day){
					return calDay;
				}
			}
		}		
		return null;
	}
	
	public WcScdCalDay getScdDay(int year, int month, int day,int schdlTyp, WcScdCalMonth calMonth) {		
		for(WcScdCalWeek calWeek:calMonth.getWeeks()){
			for(WcScdCalDay calDay:calWeek.getDays()){
				if(calDay.getMonth()==month && calDay.getDay()==day){
					return calDay;
				}
			}
		}		
		return null;
	}
	
	
	/**
	 * 해당 월/주/일 의 스케쥴을 리턴 함.
	 */
	public WcScdCalMonth getMonthSchdlList(
			int year
			,int month
			,int schdlTyp
			,String timeZone
			,String bumk
			,String agnt
			,String choiGrpIds[]
			,String viewUserUid
			,String viewOrgId
			,String userUid
			,String userDeptId
			,String userCompId			
			,WcScdCalMonth monthObj
			,String catId
			) throws SQLException, CmException, IOException
	{
		//1일 이전 30또는 31일 까지 스케쥴 생성 및 삽입
		WcSchdlBVo conditionScd = new WcSchdlBVo();
		conditionScd.setInstanceQueryId("selectStartToEndDate");
		conditionScd.setOrderBy("SCHDL_START_DT");
		String monthStr=(month<10)? ("0" + month):month+"";
		conditionScd.setSchdlYm(year+ "-" +monthStr);		
		conditionScd.setSchdlKndCd(schdlTyp!=-1?String.valueOf(schdlTyp):null);
		conditionScd.setSchdlTypCd(catId);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		boolean otherUserSchdlFlag=false;
		boolean otherDeptSchdlFlag=false;

		//조회 대상
		conditionScd.setUserUid(userUid);
		
		//대리인
		if(agnt!=null&&!agnt.equals("")&&!agnt.equals("-1")){
			conditionScd.setUserUid(agnt);
		}
		//즐겨찾기
		//bumk -> 부서, 사람일수도 있음
		if(bumk!=null&&!bumk.equals("")&&!bumk.equals("-1")){
			String[] tempValue = bumk.split(":");
			if(tempValue.length > 1 ) bumk = tempValue[0];
			if(schdlTyp==1){
				conditionScd.setUserUid(bumk);
				//conditionScd.setSchdlTypCd(String.valueOf(schdlTyp));
				otherUserSchdlFlag=true;
				//conditionScd.setOpenGradCd("1");
			}else if(schdlTyp==3){
				conditionScd.setDeptId(bumk);
				//conditionScd.setSchdlTypCd(String.valueOf(schdlTyp));
				otherDeptSchdlFlag=true;
				//conditionScd.setOpenGradCd("1");
			}
		}else{
			if(schdlTyp==3){
				conditionScd.setUserUid(userUid);
				conditionScd.setDeptId(userDeptId);
				
				//대리인
				if(agnt!=null&&!agnt.equals("")&&!agnt.equals("-1")){
					conditionScd.setUserUid(agnt);
					conditionScd.setDeptId(null);
				}
				
			}
			
			if(schdlTyp==4){
				conditionScd.setUserUid(null);
				conditionScd.setCompId(userCompId);
			}
		}
		
		conditionScd.setCompId(userCompId);
		if(schdlTyp==-1){
			conditionScd.setChoiGrpIds(choiGrpIds);
			conditionScd.setSchdlKndCd("-1");
			conditionScd.setDeptId(userDeptId);
			
			if(agnt!=null&&!agnt.equals("")&&!agnt.equals("-1")){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(agnt);
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				conditionScd.setDeptId(orUserBVo.getDeptId());
			}
			
			//그룹일정 이면서 그룹정보가 있을경우 
		}else if(schdlTyp==2 && choiGrpIds != null && choiGrpIds.length > 0){
			conditionScd.setUserUid(null);
			conditionScd.setDeptId(null);
			conditionScd.setChoiGrpIds(choiGrpIds);
		}
		
		//다른사람일정 조회
		if(viewUserUid!=null&&!viewUserUid.equals("")){
			conditionScd.setUserUid(viewUserUid);
			//conditionScd.setSchdlTypCd(String.valueOf(schdlTyp));
			otherUserSchdlFlag=true;
			//conditionScd.setOpenGradCd("1");
		}
		//다른부서일정 조회
		if(viewOrgId!=null&&!viewOrgId.equals("")){
			conditionScd.setDeptId(viewOrgId);
			//conditionScd.setSchdlTypCd(String.valueOf(schdlTyp));
			otherDeptSchdlFlag=true;
		}
		
		
		//이전달 년 월 set
		int prevYear =year;
		int prevMonth = month;
		if(1 <= (prevMonth - 1)) prevMonth -= 1;
		else{
			prevMonth = 12;
			prevYear -= 1;
		}
		
		String convPrevMonth = prevMonth+"";
		if(prevMonth<10)
			convPrevMonth = "0" + prevMonth;
		if(convPrevMonth.length()==1) convPrevMonth = "0" + convPrevMonth;
		conditionScd.setSchdlPrevYm(prevYear + "-" + convPrevMonth);
		//다음달 년 월 set
		int nextYear =year;
		int nextMonth = month;
		if(12 >= (nextMonth + 1)) nextMonth+= 1;
		else{
			nextMonth = 1;
			nextYear += 1;
		}
		
		String convNextMonth = "0" + nextMonth;
		if(convNextMonth.length()==1) convNextMonth = "0" + convNextMonth;
		conditionScd.setSchdlNextYm(nextYear + "-" + convNextMonth);
		
		//모든 스케쥴을 가져온다.
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> schdl = (List<WcSchdlBVo>) commonSvc.queryList(conditionScd);
		//타인 조회시 비공개,지정인
		if(otherUserSchdlFlag){
			boolean apntFlag=false;
			for(WcApntrRVo apntVo:getApntList(null,conditionScd.getUserUid())){
				if(apntVo.getApntrUid().equals(userUid)){
					apntFlag=true;
					break;
				}
			}
			
			List<WcSchdlBVo> removeScdl=new ArrayList<WcSchdlBVo>();
			for(WcSchdlBVo wcs : schdl){
				//지정인공개
				if(wcs.getOpenGradCd()!=null&&wcs.getOpenGradCd().equals("2")&&!apntFlag){
					wcs.setSubj("비공개 일정");
					wcs.setCont("비공개 일정");
					removeScdl.add(wcs);
				}else if(wcs.getOpenGradCd()!=null&&wcs.getOpenGradCd().equals("3")){//비공개
					wcs.setSubj("비공개 일정");
					wcs.setCont("비공개 일정");
					removeScdl.add(wcs);
				}
			}
			for(WcSchdlBVo wcs : removeScdl){
				schdl.remove(wcs);
			}
		}
		
		if(otherDeptSchdlFlag){
			List<WcSchdlBVo> removeScdl=new ArrayList<WcSchdlBVo>();
			for(WcSchdlBVo wcs : schdl){
				if(wcs.getOpenGradCd()!=null&&wcs.getOpenGradCd().equals("3")){//비공개
					wcs.setSubj("비공개 일정");
					wcs.setCont("비공개 일정");
					removeScdl.add(wcs);
				}
			}
			for(WcSchdlBVo wcs : removeScdl){
				schdl.remove(wcs);
			}
		}
		
		//공통기념일 체크
		List<WcSchdlBVo> delList = new ArrayList<WcSchdlBVo>();
		// 국가코드
		String natCd = ParamUtil.getRequestParam(request, "natCd", false);
		if(natCd==null || natCd.isEmpty()){
			natCd = getNatCd(userVo);
		}
		WcAnnvDVo wcAnnvDVo = null;
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		for(WcSchdlBVo wcs : schdl){
			if(wcs.getSchdlTypCd().equals("5")){
				if(wcs.getCompId() != null && !wcs.getCompId().isEmpty() && 
						!userVo.getCompId().equals(wcs.getCompId())){
					delList.add(wcs);
					continue;
				}
				// 기념일상세 조회
				wcAnnvDVo = getWcAnnvDVo(langTypCd, userVo.getCompId(), wcs.getSchdlPid());
				if(!isSchdlNatChk(wcAnnvDVo, natCd)) {
					delList.add(wcs);
					continue;
				}
				if(wcAnnvDVo != null) wcs.setSubj(wcAnnvDVo.getRescNm());
			}
		}
		if(delList.size() > 0){
			for(WcSchdlBVo removeVo : delList){
				schdl.remove(removeVo);
			}
		}
		
		boolean sortCheck=false;
		for(WcSchdlBVo wcs : schdl){
			if(timeZone!=null&&!timeZone.equals("")&&!timeZone.equals(TimeZone.getDefault().getID())){
				DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );
				dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
				Calendar startCal=stringConvertCal(wcs.getSchdlStartDt(), false);
				wcs.setSchdlStartDt(dateFormat.format(startCal.getTime()));
				Calendar endCal=stringConvertCal(wcs.getSchdlEndDt(), false);				
				wcs.setSchdlEndDt(dateFormat.format(endCal.getTime()));
				sortCheck=true;
			}
			
			if(wcs.getSolaLunaYn()!=null&&wcs.getSolaLunaYn().equals("N")){
				String startTime=wcs.getSchdlStartDt().substring(11,wcs.getSchdlStartDt().length() );
				//String startDate=lunalCalenderUtil.fromLunar(wcs.getSchdlStartDt());
				String startDate=lunalCalenderUtil.toSolar(wcs.getSchdlStartDt());
				wcs.setSchdlLunaStartDt(wcs.getSchdlStartDt());
				wcs.setSchdlLunaEndDt(wcs.getSchdlEndDt());
				
				wcs.setSchdlStartDt(startDate+" "+startTime);				
				String endTime=wcs.getSchdlEndDt().substring(11,wcs.getSchdlEndDt().length() );
				//String endDate=lunalCalenderUtil.fromLunar(wcs.getSchdlEndDt());
				String endDate=lunalCalenderUtil.toSolar(wcs.getSchdlEndDt());
				
				wcs.setSchdlEndDt(endDate+" "+endTime);
				sortCheck=true;
			}
		}
		if(sortCheck)
			Collections.sort(schdl, new IndexSchdlStartDateAscCompare());
				
		for(int i=0; i < monthObj.getWeeks().size(); i++){
			for(int j=0; j < 7; j++){
				
				WcScdCalDay culentDay=monthObj.getWeeks().get(i).getDays().get(j);
				culentDay.setDayOfTheWeek(String.valueOf(j));
				int calenderYear=culentDay.getYear();
				int calenderMonth= culentDay.getMonth();
				int calenderDay=culentDay.getDay();
				
				Calendar currentCal = Calendar.getInstance ( );
				currentCal.set ( calenderYear, calenderMonth - 1, calenderDay );
				currentCal.set( Calendar.HOUR_OF_DAY, 0 );
				currentCal.set( Calendar.MINUTE, 0 );
				currentCal.set( Calendar.SECOND, 0 );
				currentCal.set( Calendar.MILLISECOND, 0 );
				
				for(WcSchdlBVo wcs : schdl){
					
					if(!wcs.getSchdlStartDt().substring(0,10).equalsIgnoreCase(wcs.getSchdlEndDt().substring(0,10))){
						wcs.setSchdlRepetState("scd_repeat");
					}
					
					int sYear = Integer.parseInt(wcs.getSchdlStartDt().substring(0,4));
					int sMonth =Integer.parseInt(wcs.getSchdlStartDt().substring(5,7));
					int sDate = Integer.parseInt(wcs.getSchdlStartDt().substring(8,10));
					
					
					
					int eYear = Integer.parseInt(wcs.getSchdlEndDt().substring(0,4));
					int eMonth = Integer.parseInt(wcs.getSchdlEndDt().substring(5,7));
					int eDate = Integer.parseInt(wcs.getSchdlEndDt().substring(8,10));
					
//					if(calenderMonth==5&&calenderDay==31){
//						System.out.println();
//					}
//					
//					if(wcs.getSchdlId().equals("XO100001")){
//						System.out.println();
//					}
					Calendar startCal = Calendar.getInstance ( );
					startCal.set ( sYear, sMonth - 1, sDate );
					startCal.set( Calendar.HOUR_OF_DAY, 0 );
					startCal.set( Calendar.MINUTE, 0 );
					startCal.set( Calendar.SECOND, 0 );
					startCal.set( Calendar.MILLISECOND, 0 );
									
					Calendar endCal = Calendar.getInstance ( );
					endCal.set ( eYear, eMonth - 1, eDate );
					endCal.set( Calendar.HOUR_OF_DAY, 0 );
					endCal.set( Calendar.MINUTE, 0 );
					endCal.set( Calendar.SECOND, 0 );
					endCal.set( Calendar.MILLISECOND, 0 );
			
					//시작날짜
					if(startCal.equals(currentCal)){
						if(wcs.getSchdlTypCd().equals("5")){
							//if(culentDay.getScds().size()>0&&culentDay.getScds().get(0).getSchdlTypCd().equals("5"))continue;
							wcs.setSchdIndex(0);
							culentDay.getScds().add(wcs);
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							continue;
						}else if(culentDay.getScds().size()>0){
							//Collections.sort(culentDay.getScds(), new IndexAscCompare());
							int maxIndex=-1;
							 
							for(WcSchdlBVo vo:culentDay.getScds()){
								if(vo.getSchdIndex()>maxIndex){
									maxIndex=vo.getSchdIndex();
								}
							}
							
							
							boolean isEmpty=false;
							for(int k=1; k<maxIndex; k++){
								boolean isExist=false;
								for(WcSchdlBVo vo:culentDay.getScds()){										
									if(vo.getSchdIndex()==k){
										isExist=true;
										break;
									}
								}
								if(isExist==false){
									wcs.setSchdIndex(k);
									culentDay.getScds().add(wcs);
									isEmpty=true;
									break;
								}
							}
							if(isEmpty==false){
								wcs.setSchdIndex(maxIndex+1);
								culentDay.getScds().add(wcs);
								Collections.sort(culentDay.getScds(), new IndexAscCompare());
								if(culentDay.getScds().size()>0)
									culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							}
						}else{
							wcs.setSchdIndex(1);
							culentDay.getScds().add(wcs);
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
						}
						continue;
					}						
					//중간날짜
					if(startCal.before(currentCal)&&endCal.after(currentCal)){
						if(wcs.getSchdlTypCd().equals("5")){
							//if(culentDay.getScds().size()>0&&culentDay.getScds().get(0).getSchdlTypCd().equals("5"))continue;
							wcs.setSchdIndex(0);
							culentDay.getScds().add(wcs);
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							continue;
						}else{
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							
							culentDay.getScds().add(wcs);
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0){
								int maxIndex=-1;
								 
								for(WcSchdlBVo vo:culentDay.getScds()){
									if(vo.getSchdIndex()>maxIndex){
										maxIndex=vo.getSchdIndex();
									}
								}
								if(maxIndex==-1){
								
									maxIndex=1;
									wcs.setSchdIndex(1);
								}
								
								culentDay.setScdMaxIndex(maxIndex);
							}
							continue;
						}
					}
					
					//종료날짜
					if(endCal.equals(currentCal)){
						if(wcs.getSchdlTypCd().equals("5")){
							//if(culentDay.getScds().size()>0&&culentDay.getScds().get(0).getSchdlTypCd().equals("5"))continue;
							wcs.setSchdIndex(0);
							culentDay.getScds().add(wcs);
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							Collections.sort(culentDay.getScds(), new IndexAscCompare());
							if(culentDay.getScds().size()>0)
								culentDay.setScdMaxIndex(culentDay.getScds().get(culentDay.getScds().size()-1).getSchdIndex());
							continue;
						}else{
							if("Y".equals(wcs.getHoliYn())){//공통 기념일이면서 휴일로 설정된 경우 일자도 red 로 표현
								if(culentDay.getHoliFlag().indexOf("prev") > -1) culentDay.setHoliFlag("scddate_red_prev");								
								else culentDay.setHoliFlag("scddate_red");
							}
							
							if(!startCal.equals(endCal)){
								//종일일정이 아니면서 종료시간이 12시 이상일경우에만 일정을 매핑한다.
								if((wcs.getAlldayYn() == null || "N".equals(wcs.getAlldayYn())) && "00".equals(wcs.getSchdlEndDt().substring(11,13)) ){
									continue;
								}
								
								culentDay.getScds().add(wcs);	
								Collections.sort(culentDay.getScds(), new IndexAscCompare());
								if(culentDay.getScds().size()>0){
									int maxIndex=-1;
									 
									for(WcSchdlBVo vo:culentDay.getScds()){
										if(vo.getSchdIndex()>maxIndex){
											maxIndex=vo.getSchdIndex();
										}
									}
									culentDay.setScdMaxIndex(maxIndex);
								}
									
							}								
							continue;
						}
					}
					
					
//						if(calenderYear == sYear &&calenderMonth  == sMonth && calenderDay == sDate){
//							
//						}

				}
			}
			
		}
		
		for(int i=0; i < monthObj.getWeeks().size(); i++){
			for(int j=0; j < 7; j++){
				
				WcScdCalDay culentDay=monthObj.getWeeks().get(i).getDays().get(j);
				
				int calenderYear=culentDay.getYear();
				int calenderMonth= culentDay.getMonth();
				int calenderDay=culentDay.getDay();
				
				Calendar currentCal = Calendar.getInstance ( );
				currentCal.set ( calenderYear, calenderMonth - 1, calenderDay );
				currentCal.set( Calendar.HOUR_OF_DAY, 0 );
				currentCal.set( Calendar.MINUTE, 0 );
				currentCal.set( Calendar.SECOND, 0 );
				currentCal.set( Calendar.MILLISECOND, 0 );
				
				int maxScdIndex=culentDay.getScdMaxIndex();
				for(int idx=1; idx<maxScdIndex; idx++){
					boolean emptyflag=false;
					for(WcSchdlBVo scd:culentDay.getScds()){
						if(idx==scd.getSchdIndex()){
							emptyflag=true;
							break;
						}
					}
					if(!emptyflag){
						WcSchdlBVo emptyScd=new WcSchdlBVo();
						emptyScd.setSchdIndex(idx);
						String scdTypeCd="";
						if(j==0){
							for(WcSchdlBVo beforScd:monthObj.getWeeks().get(i-1).getDays().get(6).getScds()){
								if(beforScd.getSchdIndex()==idx){
									scdTypeCd=beforScd.getSchdlTypCd();
									break;
								}
							}
						}else{
							for(WcSchdlBVo beforScd:monthObj.getWeeks().get(i).getDays().get(j-1).getScds()){
								if(beforScd.getSchdIndex()==idx){
									scdTypeCd=beforScd.getSchdlTypCd();
									break;
								}
							}
						}						
						emptyScd.setSchdlTypCd(scdTypeCd);
						culentDay.getScds().add(emptyScd);
						Collections.sort(culentDay.getScds(), new IndexAscCompare());
					}
				}
				
			}
		}
		
		return monthObj;	
		  
	}	
	

	public static class IndexAscCompare implements Comparator<WcSchdlBVo> {
		 
		/**
		 * 오름차순(ASC)
		 */	
		@Override
		public int compare(WcSchdlBVo fisrtSchd, WcSchdlBVo secondSchd) {			
			return fisrtSchd.getSchdIndex()-secondSchd.getSchdIndex();
		}

	}
	
	public static class IndexSchdlStartDateAscCompare implements Comparator<WcSchdlBVo> {
		 
		/**
		 * 오름차순(ASC)
		 */	
		@Override
		public int compare(WcSchdlBVo fisrtSchd, WcSchdlBVo secondSchd) {	
			Calendar firstStartCal = stringConvertCal(fisrtSchd.getSchdlStartDt(),false);
			Calendar secondStartCal = stringConvertCal(secondSchd.getSchdlStartDt(),false);
			return ((firstStartCal.getTimeInMillis()-secondStartCal.getTimeInMillis())>0?1:-1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void repetSetup(WcRepetSetupDVo wcRepetVo, WcSchdlBVo wcsVo,
			List<WcPromGuestDVo> wcGuestVo, QueryQueue queryQueue, HttpServletRequest request) {
		
		List<WcSchdlDeptRVo> wcSchdlDeptRVoList = null;
		// 부서일정 관계 저장 - 부서일정 선택부서 목록
		if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
			wcSchdlDeptRVoList = (List<WcSchdlDeptRVo>)VoUtil.bindList(request, WcSchdlDeptRVo.class, new String[]{"orgId"});
					
		if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY")){
			repetDay(wcRepetVo, wcsVo, wcGuestVo, queryQueue, request, null, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK")){
			repetWeek(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_MY")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_MT")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, null, wcSchdlDeptRVoList);
		}
	}
	
	private void repetYely(WcRepetSetupDVo wcRepetVo, WcSchdlBVo wcsVo,
			List<WcPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList, List<WcSchdlDeptRVo> wcSchdlDeptRVoList) {
		
		try{
			
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			
			int firYelySelect =  Integer.parseInt(wcRepetVo.getRepetMm());
		
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);

			
			//일정 시작일정 과 끝일의 차이를 구한다.
			long difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
			int scdGapDay = (int) (difference/(24*60*60));
	
		
			String schdlPid = wcsVo.getSchdlId();
			
			if(repetStartCal.before(scdEndCal)){
				repetStartCal=(Calendar) scdEndCal.clone();
			}
			
			if(scdEndCal.after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}
			
			
			int strtYear = repetStartCal.get(Calendar.YEAR); 
			int strtMonth = repetStartCal.get(Calendar.MONTH); 
			int endYear = repetEndCal.get(Calendar.YEAR); 
			int endMonth = repetEndCal.get(Calendar.MONTH); 
			int monthGap = (endYear - strtYear)* 12 + (endMonth - strtMonth);
			
			int startYear=0;
			int startMonth=0;
			//int startWeek=0;
			int startWeekDay=0;
			Calendar startCal = (Calendar) scdStartCal.clone();
			
			
			
			if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_YR")){
				int firYelyDaySelect=Integer.parseInt(wcRepetVo.getApntDd());
				for(int i=0; i<=(monthGap/12)+1; i++){
					
					startYear=repetStartCal.get(Calendar.YEAR)+i;
					startMonth=firYelySelect;
					startWeekDay=firYelyDaySelect;
					startCal.set(startYear,startMonth-1,startWeekDay);
					
					if(startCal.get(Calendar.DATE)!=startWeekDay)
						continue;
					
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){						
						
						
						WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
						scdEndCal.set(startYear,startMonth-1, startWeekDay);
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
						
						scdEndCal.add(Calendar.DATE, scdGapDay);
						
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));

						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
						
							
							cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							
							//나의일정cd가 -1 이면 공통기념일.
							if(!cloneWcsVo.getSchdlKndCd().equalsIgnoreCase("-1")){
								// 게시파일 저장
								//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
								wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
							}
							
							
							queryQueue.insert(cloneWcsVo);
							
							if(wcSchdlDeptRVoList!=null) // 부서일정 - 부서선택 목록 저장
								saveWcSchdlDeptRVo(request, queryQueue, wcSchdlDeptRVoList, cloneWcsVo.getSchdlId(), false);
							
							if(wcGuestVo.size() != 0){					
								for(WcPromGuestDVo wcguest:  wcGuestVo){	
									WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						}
					
					}
				}
			
			}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_YR")){
				int secYelyWeekSelect=Integer.parseInt(wcRepetVo.getApntWk());
				int secYelyWeekOfDaySelect=Integer.parseInt(wcRepetVo.getApntDy());
				for(int i=0; i<=(monthGap/12)+1; i++){
					
					startYear=repetStartCal.get(Calendar.YEAR)+i;
					startMonth=firYelySelect;
					startWeekDay=secYelyWeekOfDaySelect;
					WcScdCalMonth calMonth=getScdMonth(startYear,startMonth,-1,null,null,null,null,null,null,null,null,null,null,false);
					WcScdCalWeek wcWeek=calMonth.getWeeks().get(secYelyWeekSelect-1);
					WcScdCalDay wcDay=wcWeek.getDays().get(secYelyWeekOfDaySelect-1);
					
					if(wcDay.getMonth()!=startMonth)
						continue;

					startCal.set(wcDay.getYear(),wcDay.getMonth()-1,wcDay.getDay());
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){
						
						WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
						//scdEndCal.set(startYear,startMonth-1, startWeekDay);
						scdEndCal.set(wcDay.getYear(),wcDay.getMonth()-1,wcDay.getDay());
						
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
																	
						scdEndCal.add(Calendar.DATE, scdGapDay);
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));

						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
							
							cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							
							// 게시파일 저장
							//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue , "indiv");
							wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
							
							queryQueue.insert(cloneWcsVo);
							if(wcGuestVo.size() != 0){					
								for(WcPromGuestDVo wcguest:  wcGuestVo){	
									WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						}
						
						
						
					}
				
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	private void repetMoly(WcRepetSetupDVo wcRepetVo, WcSchdlBVo wcsVo,
			List<WcPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList, List<WcSchdlDeptRVo> wcSchdlDeptRVoList) {
		try{
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			
			int firMolySelect =  Integer.parseInt(wcRepetVo.getRepetMm());
		
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			
			//일정 시작일정 과 끝일의 차이를 구한다.
			long difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
			int scdGapDay = (int) (difference/(24*60*60));
	
		
			String schdlPid = wcsVo.getSchdlId();
			
			if(repetStartCal.before(scdEndCal)){
				repetStartCal=(Calendar) scdEndCal.clone();
			}
			
			if(scdEndCal.after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}
			
			
			int strtYear = repetStartCal.get(Calendar.YEAR); 
			int strtMonth = repetStartCal.get(Calendar.MONTH); 
			int endYear = repetEndCal.get(Calendar.YEAR); 
			int endMonth = repetEndCal.get(Calendar.MONTH); 
			int monthGap = (endYear - strtYear)* 12 + (endMonth - strtMonth);
			
			int startYear=0;
			int startMonth=0;
			//int startWeek=0;
			int startWeekDay=0;
			Calendar startCal = (Calendar) scdStartCal.clone();
			
			if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_MY")){
				for(int i=0; i<=(monthGap/firMolySelect)+1; i++){
					int firMolyDaySelect =  Integer.parseInt(wcRepetVo.getApntDd());
					
					startYear=repetStartCal.get(Calendar.YEAR);
					startMonth=(repetStartCal.get(Calendar.MONTH)+1)+(i*firMolySelect);
					startWeekDay=firMolyDaySelect;
					startCal.set(startYear,startMonth-1,startWeekDay);
					
					if(startCal.get(Calendar.DATE)!=startWeekDay)
						continue;
					
					
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){
						WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
						scdEndCal.set(startYear,startMonth-1, startWeekDay);
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
						
						scdEndCal.add(Calendar.DATE, scdGapDay);
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
						
						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
							
							cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
							cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
							// 게시파일 저장
							//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
							wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
						
						
							queryQueue.insert(cloneWcsVo);
							
							if(wcSchdlDeptRVoList!=null) // 부서일정 - 부서선택 목록 저장
								saveWcSchdlDeptRVo(request, queryQueue, wcSchdlDeptRVoList, cloneWcsVo.getSchdlId(), false);
							
							if(wcGuestVo.size() != 0){					
								for(WcPromGuestDVo wcguest:  wcGuestVo){	
									WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						}
					}
					
				}
				
			}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_MT")){
				for(int i=0; i<=(monthGap/firMolySelect)+1; i++){								
					startYear=repetStartCal.get(Calendar.YEAR);
					startMonth=(repetStartCal.get(Calendar.MONTH)+1)+(i*firMolySelect);
					WcScdCalMonth calMonth=getScdMonth(startYear,startMonth,-1,null,null,null,null,null,null,null,null,null,null,false);
					int secMolyWeekSelect = Integer.parseInt(wcRepetVo.getApntWk());		
					int secMolyWeekOfDaySelect =  Integer.parseInt(wcRepetVo.getApntDy());
					
//					WcScdCalWeek wcWeek=calMonth.getWeeks().get(secMolyWeekSelect-1);
//					WcScdCalDay wcDay=wcWeek.getDays().get(secMolyWeekOfDaySelect-1);
					//WcScdCalWeek wcWeek=null;
					WcScdCalDay wcDay=null;
					
					int choiceWeekSelect=0;
					for(int t=0; t<calMonth.getWeeks().size(); t++){
						WcScdCalWeek wcWeekTmp=calMonth.getWeeks().get(t);
						for(int p=0; p<wcWeekTmp.getDays().size(); p++){
							WcScdCalDay wcDayTmp=wcWeekTmp.getDays().get(p);
							if(wcDayTmp.getMonth()!=startMonth)
								continue;
							
							if((secMolyWeekOfDaySelect-1)==p){
								choiceWeekSelect++;
								if(choiceWeekSelect==secMolyWeekSelect){
									if(wcDayTmp.getMonth()!=startMonth)
										continue;
									//wcWeek=wcWeekTmp;
									wcDay=wcDayTmp;
									break;
								}
							}
						}
					}
					
					if(wcDay==null)
						continue;
					
					startCal.set(wcDay.getYear(),wcDay.getMonth()-1,wcDay.getDay());
	
					if(startCal.after(repetStartCal)&&startCal.before(repetEndCal)){
						WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
						scdEndCal.set(wcDay.getYear(),wcDay.getMonth()-1, wcDay.getDay());
						scdStartCal=(Calendar) scdEndCal.clone();
						
						int hour = Integer.parseInt(scdStartDay.substring(11,13));
						int minuit = Integer.parseInt(scdStartDay.substring(14,16));
						scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
						scdStartCal.set( Calendar.MINUTE, minuit );
						
						scdEndCal.add(Calendar.DATE, scdGapDay);
						cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
						cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
						
						//행사 일 경우
						if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
							int afterDay = Integer.parseInt(request.getParameter("afterDay"));
							String endTm = request.getParameter("endTm");
							String strtTm = request.getParameter("strtTm");
							
							for(int j=0; j<afterDay; j++){
								WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
								try {
									cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
								} catch (SQLException e1) {
									
									e1.printStackTrace();
								}
								
								// 게시파일 저장
								wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
								int fileSaveSize=queryQueue.size();
								cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
								
								Calendar schdlStartCal=Calendar.getInstance();
								schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlStartCal.add(Calendar.DATE, j);
								Calendar schdlEndCal=Calendar.getInstance();
								schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
								schdlEndCal.add(Calendar.DATE, j);
								DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
								
								//시작 일 +시 간
								cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
								//종료 일 +시간
								cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
								queryQueue.insert(cloneAvntWcsVo);
								// 게시파일 저장
								
							}
						}else{
							cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
							cloneWcsVo.setSchdlPid(schdlPid);
							
							// 게시파일 저장
							//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
							wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
							
							queryQueue.insert(cloneWcsVo);
							if(wcGuestVo.size() != 0){					
								for(WcPromGuestDVo wcguest:  wcGuestVo){	
									WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
									cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
									queryQueue.insert(cloneWcGuest);							
								}
							}	
						
						}
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	private void repetWeek(WcRepetSetupDVo wcRepetVo, WcSchdlBVo wcsVo,
			List<WcPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList, List<WcSchdlDeptRVo> wcSchdlDeptRVoList) {
		try{
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			int repetWele = Integer.parseInt(wcRepetVo.getRepetWk());
			String dow =  wcRepetVo.getApntDy();
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			
			//일정 시작일정 과 끝일의 차이를 구한다.
			long difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
			int scdGapDay = (int) (difference/(24*60*60));
	
		
			String schdlPid = wcsVo.getSchdlId();
			
			if(repetStartCal.before(scdEndCal)){
				repetStartCal=(Calendar) scdEndCal.clone();
			}
			
			
			if(scdEndCal.after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}else{
				
				int strtYear = repetStartCal.get(Calendar.YEAR); 
				int strtMonth = repetStartCal.get(Calendar.MONTH); 
				int endYear = repetEndCal.get(Calendar.YEAR); 
				int endMonth = repetEndCal.get(Calendar.MONTH); 
				int monthGap = (endYear - strtYear)* 12 + (endMonth - strtMonth);
				//Map<String, WcScdCalMonth> calenderMap=new HashMap<String, WcScdCalMonth>();
				
				
				int startYear=0;
				int startMonth=0;
				int startWeek=0;
				int startWeekDay=0;
				Calendar startCal = Calendar.getInstance();
				
				//boolean choiceWeek=false;
				for(int i=0; i<=monthGap; i++){
					if(i>0)
						repetStartCal.add(Calendar.MONTH, 1);
					WcScdCalMonth calMonth=getScdMonth(repetStartCal.get(Calendar.YEAR),repetStartCal.get(Calendar.MONTH)+1,-1,null,null,null,null,null,null,null,null,null,null,false);
					
					for(int weekIdx=0; weekIdx < calMonth.getWeeks().size(); weekIdx++){
						WcScdCalWeek  week=calMonth.getWeeks().get(weekIdx);
						for(int j=0; j<7; j++){
							WcScdCalDay day=week.getDays().get(j);
							Calendar currentCal = Calendar.getInstance();
							currentCal.set(day.getYear(),day.getMonth()-1,day.getDay());
							currentCal.set( Calendar.HOUR_OF_DAY, 0 );
							currentCal.set( Calendar.MINUTE, 0 );
							currentCal.set( Calendar.SECOND, 0 );
							currentCal.set( Calendar.MILLISECOND, 0 );
							
							if(i==0&&repetStartCal.after(currentCal)){
								//choiceWeek=false;
								continue;
							}else{
								if(startMonth==0 && startWeekDay==0){
									//choiceWeek=true;
									startYear=day.getYear();
									startMonth=week.getDays().get(0).getMonth();
									startWeek=weekIdx;//현재것을 셋팅
									startWeekDay=week.getDays().get(0).getDay();
									startCal = Calendar.getInstance();
									startCal.set(startYear,startMonth-1,startWeekDay);
									startCal.set( Calendar.HOUR_OF_DAY, 0 );
									startCal.set( Calendar.MINUTE, 0 );
									startCal.set( Calendar.SECOND, 0 );
									startCal.set( Calendar.MILLISECOND, 0 );
									//월화수목금토일 검사후 일정 입력
									boolean insertFlag=false;
									if(dow!=null){
										String dows[]=dow.split("/");
										for(String dowTemp:dows){
											if(dowTemp.equals("SUN")&&j==0){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("MON")&&j==1){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("TUE")&&j==2){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("WED")&&j==3){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("THU")&&j==4){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("FRI")&&j==5){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("SAT")&&j==6){
												insertFlag=true;
												break;
											}
										}
									}
									if(insertFlag){
										WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
										scdEndCal.set(day.getYear(),day.getMonth()-1,day.getDay());
										scdStartCal=(Calendar) scdEndCal.clone();
										
										int hour = Integer.parseInt(scdStartDay.substring(11,13));
										int minuit = Integer.parseInt(scdStartDay.substring(14,16));
										scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
										scdStartCal.set( Calendar.MINUTE, minuit );
										
										scdEndCal.add(Calendar.DATE, scdGapDay);
										cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
										cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
										
										//if(scdStartCal.after(repetEndCal)||scdStartCal.equals(repetEndCal)){
										if(stringConvertCal(repetDateFormat.format(scdStartCal.getTime()),true).after(repetEndCal)){
											break;
										}
										
										//행사 일 경우
										if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
											int afterDay = Integer.parseInt(request.getParameter("afterDay"));
											String endTm = request.getParameter("endTm");
											String strtTm = request.getParameter("strtTm");
											
											for(int k=0; k<afterDay; k++){
												WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
												try {
													cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
												} catch (SQLException e1) {
													
													e1.printStackTrace();
												}
												
												// 게시파일 저장
												wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
												int fileSaveSize=queryQueue.size();
												cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
												
												Calendar schdlStartCal=Calendar.getInstance();
												schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlStartCal.add(Calendar.DATE, k);
												Calendar schdlEndCal=Calendar.getInstance();
												schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlEndCal.add(Calendar.DATE, k);
												DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
												
												//시작 일 +시 간
												cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
												//종료 일 +시간
												cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
												queryQueue.insert(cloneAvntWcsVo);
												// 게시파일 저장
												
											}
										}else{
											cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
											cloneWcsVo.setSchdlPid(schdlPid);
											
											// 게시파일 저장
											//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
											wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
											
											queryQueue.insert(cloneWcsVo);
											
											if(wcSchdlDeptRVoList!=null) // 부서일정 - 부서선택 목록 저장
												saveWcSchdlDeptRVo(request, queryQueue, wcSchdlDeptRVoList, cloneWcsVo.getSchdlId(), false);
											
											if(wcGuestVo.size() != 0){					
												for(WcPromGuestDVo wcguest:  wcGuestVo){	
													WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
													cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
													queryQueue.insert(cloneWcGuest);							
												}
											}	
										}
										
									}
									if(j==6){
										//다음 주차 첫 날 입력..
										startCal.add(Calendar.DATE, repetWele*7);
										startYear=startCal.get(Calendar.YEAR);
										startMonth=startCal.get(Calendar.MONTH)+1;
										startWeek=-1;//현재것을 셋팅
										startWeekDay=startCal.get(Calendar.DATE);
									}
									
									
								}else if(startWeek==weekIdx){
									//월화수목금토일 검사후 일정 입력
									//월화수목금토일 검사후 일정 입력
									boolean insertFlag=false;
									if(dow!=null){
										String dows[]=dow.split("/");
										for(String dowTemp:dows){
											if(dowTemp.equals("SUN")&&j==0){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("MON")&&j==1){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("TUE")&&j==2){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("WED")&&j==3){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("THU")&&j==4){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("FRI")&&j==5){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("SAT")&&j==6){
												insertFlag=true;
												break;
											}
										}
									}
									if(insertFlag){
										WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
										scdEndCal.set(day.getYear(),day.getMonth()-1,day.getDay());
										scdStartCal=(Calendar) scdEndCal.clone();
										
										int hour = Integer.parseInt(scdStartDay.substring(11,13));
										int minuit = Integer.parseInt(scdStartDay.substring(14,16));
										scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
										scdStartCal.set( Calendar.MINUTE, minuit );
										
										scdEndCal.add(Calendar.DATE, scdGapDay);
										cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
										cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
										
										//if(scdStartCal.after(repetEndCal)||scdStartCal.equals(repetEndCal)){
										if(stringConvertCal(repetDateFormat.format(scdStartCal.getTime()),true).after(repetEndCal)){
											break;
										}
										
										//행사 일 경우
										if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
											int afterDay = Integer.parseInt(request.getParameter("afterDay"));
											String endTm = request.getParameter("endTm");
											String strtTm = request.getParameter("strtTm");
											
											for(int k=0; k<afterDay; k++){
												WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
												try {
													cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
												} catch (SQLException e1) {
													
													e1.printStackTrace();
												}
												
												// 게시파일 저장
												wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
												int fileSaveSize=queryQueue.size();
												cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
												
												Calendar schdlStartCal=Calendar.getInstance();
												schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlStartCal.add(Calendar.DATE, k);
												Calendar schdlEndCal=Calendar.getInstance();
												schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlEndCal.add(Calendar.DATE, k);
												DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
												
												//시작 일 +시 간
												cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
												//종료 일 +시간
												cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
												queryQueue.insert(cloneAvntWcsVo);
												// 게시파일 저장
												
											}
										}else{
										
											cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
											cloneWcsVo.setSchdlPid(schdlPid);
											
											// 게시파일 저장
											//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
											wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
											
											
											queryQueue.insert(cloneWcsVo);
											if(wcGuestVo.size() != 0){					
												for(WcPromGuestDVo wcguest:  wcGuestVo){	
													WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
													cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
													queryQueue.insert(cloneWcGuest);							
												}
											}	
										}
									
									}
									
									
									
									if(j==6){
										//다음 주차 첫 날 입력..
										startCal.add(Calendar.DATE, repetWele*7);
										startYear=startCal.get(Calendar.YEAR);
										startMonth=startCal.get(Calendar.MONTH)+1;
										startWeek=-1;//현재것을 셋팅
										startWeekDay=startCal.get(Calendar.DATE);
									}
									
									
								}else if(startYear==day.getYear()
										&&startMonth==day.getMonth()
										&&startWeekDay==day.getDay()){									
									startWeek=weekIdx;
									
									//월화수목금토일 검사후 일정 입력
									//월화수목금토일 검사후 일정 입력
									boolean insertFlag=false;
									if(dow!=null){
										String dows[]=dow.split("/");
										for(String dowTemp:dows){
											if(dowTemp.equals("SUN")&&j==0){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("MON")&&j==1){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("TUE")&&j==2){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("WED")&&j==3){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("THU")&&j==4){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("FRI")&&j==5){
												insertFlag=true;
												break;
											}
											
											if(dowTemp.equals("SAT")&&j==6){
												insertFlag=true;
												break;
											}
										}
									}
									if(insertFlag){
										WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();				
										scdEndCal.set(day.getYear(),day.getMonth()-1,day.getDay());
										scdStartCal=(Calendar) scdEndCal.clone();
										
										int hour = Integer.parseInt(scdStartDay.substring(11,13));
										int minuit = Integer.parseInt(scdStartDay.substring(14,16));
										scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
										scdStartCal.set( Calendar.MINUTE, minuit );
										
										scdEndCal.add(Calendar.DATE, scdGapDay);
										cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
										cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
										
										if(scdStartCal.after(repetEndCal)||scdStartCal.equals(repetEndCal)){
											break;
										}
										
										//행사 일 경우
										if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
											int afterDay = Integer.parseInt(request.getParameter("afterDay"));
											String endTm = request.getParameter("endTm");
											String strtTm = request.getParameter("strtTm");
											
											for(int k=0; k<afterDay; k++){
												WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
												try {
													cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
												} catch (SQLException e1) {
													
													e1.printStackTrace();
												}
												
												// 게시파일 저장
												wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
												int fileSaveSize=queryQueue.size();
												cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
												
												Calendar schdlStartCal=Calendar.getInstance();
												schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlStartCal.add(Calendar.DATE, k);
												Calendar schdlEndCal=Calendar.getInstance();
												schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
												schdlEndCal.add(Calendar.DATE, k);
												DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
												
												//시작 일 +시 간
												cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
												//종료 일 +시간
												cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
												queryQueue.insert(cloneAvntWcsVo);
												// 게시파일 저장
												
											}
										}else{
										
											cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
											cloneWcsVo.setSchdlPid(schdlPid);
											
											// 게시파일 저장
											//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
											wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
											
											queryQueue.insert(cloneWcsVo);
											if(wcGuestVo.size() != 0){					
												for(WcPromGuestDVo wcguest:  wcGuestVo){	
													WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
													cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
													queryQueue.insert(cloneWcGuest);							
												}
											}	
										}
									
									}
									
									
								}
							}
								
						}
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}

	public void repetDay(WcRepetSetupDVo wcRepetVo, WcSchdlBVo wcsVo,
		List<WcPromGuestDVo> wcGuestVo,QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList, List<WcSchdlDeptRVo> wcSchdlDeptRVoList){
		try{
			String repetStartDate = wcRepetVo.getRepetStartDt();
			String repetEndDate = wcRepetVo.getRepetEndDt();
			String repetDay =  wcRepetVo.getRepetDd();
			String scdStartDay = wcsVo.getSchdlStartDt();
			String scdEndDay = wcsVo.getSchdlEndDt();
			DateFormat repetDateFormat = new SimpleDateFormat ( "yyyy-MM-dd HH:mm" );
	
			Calendar repetStartCal = stringConvertCal(repetStartDate,true);
			Calendar repetEndCal = stringConvertCal(repetEndDate,true);
			Calendar scdStartCal = stringConvertCal(scdStartDay,false);
			Calendar scdEndCal = stringConvertCal(scdEndDay,false);
			long difference;
			int gapday = 0;
			int scdGapDay = 0;
			int repetCount =0;
			String schdlPid = wcsVo.getSchdlId();
			if(repetStartCal.before(getConvertYmd(scdEndCal))){
				repetStartCal=scdEndCal;
			}
			if(getConvertYmd(scdEndCal).after(repetEndCal)){
				System.out.println("잘못된 계산 : 일정 종료일이 반복일정종료일 보다 큽니다.");
				return;
			}else{
	
				//반복 시작 끝날짜 차이계산
				difference = (repetEndCal.getTimeInMillis() - repetStartCal.getTimeInMillis())/1000;
				gapday = (int) (difference/(24*60*60));
				
				//일정 시작일정 과 끝일의 차이를 구한다.
				difference = (scdEndCal.getTimeInMillis() - scdStartCal.getTimeInMillis())/1000;
				scdGapDay = (int) (difference/(24*60*60));
	
				repetCount  = gapday/Integer.parseInt(repetDay);			
				
				int hour = Integer.parseInt(scdEndDay.substring(11,13));
				int minuit = Integer.parseInt(scdEndDay.substring(14,16));
				repetStartCal.set( Calendar.HOUR_OF_DAY, hour );
				repetStartCal.set( Calendar.MINUTE, minuit );
				scdEndCal = (Calendar)repetStartCal.clone();
				
				for(int i=0; i <= repetCount; i++){
					
					WcSchdlBVo cloneWcsVo = (WcSchdlBVo) wcsVo.clone();	
					if(getConvertYmd(scdStartCal).equals(getConvertYmd(repetStartCal)) || i > 0 )
						scdEndCal.add(Calendar.DATE, Integer.parseInt(repetDay));
					scdStartCal=(Calendar) scdEndCal.clone();
					
					hour = Integer.parseInt(scdStartDay.substring(11,13));
					minuit = Integer.parseInt(scdStartDay.substring(14,16));
					scdStartCal.set( Calendar.HOUR_OF_DAY, hour );
					scdStartCal.set( Calendar.MINUTE, minuit );					
					scdEndCal.add(Calendar.DATE, scdGapDay);
					
					cloneWcsVo.setSchdlStartDt(repetDateFormat.format(scdStartCal.getTime()));
					cloneWcsVo.setSchdlEndDt(repetDateFormat.format(scdEndCal.getTime()));
					
					if(getConvertYmd(scdStartCal).after(repetEndCal) ){//||getConvertYmd(scdStartCal).equals(repetEndCal)
						break;
					}
					
					//행사 일 경우
					if(wcsVo.getSchdlTypCd()!=null&&wcsVo.getSchdlTypCd().equals("3")){
						int afterDay = Integer.parseInt(request.getParameter("afterDay"));
						String endTm = request.getParameter("endTm");
						String strtTm = request.getParameter("strtTm");
						
						for(int j=0; j<afterDay; j++){
							WcSchdlBVo cloneAvntWcsVo=(WcSchdlBVo) cloneWcsVo.clone();
							try {
								cloneAvntWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));								
							} catch (SQLException e1) {
								
								e1.printStackTrace();
							}
							
							// 게시파일 저장
							wcFileSvc.saveScdlFile(request, cloneAvntWcsVo.getSchdlId(), queryQueue, "indiv");
							int fileSaveSize=queryQueue.size();
							cloneAvntWcsVo.setAttYn(fileSaveSize>0?"Y":"N");
							
							Calendar schdlStartCal=Calendar.getInstance();
							schdlStartCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
							schdlStartCal.add(Calendar.DATE, j);
							Calendar schdlEndCal=Calendar.getInstance();
							schdlEndCal.set(Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(0,4)), Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(5,7))-1, Integer.parseInt(cloneWcsVo.getSchdlStartDt().substring(8,10)));
							schdlEndCal.add(Calendar.DATE, j);
							DateFormat dateFormat = new SimpleDateFormat ( "yyyy-MM-dd" );
							
							//시작 일 +시 간
							cloneAvntWcsVo.setSchdlStartDt(dateFormat.format(schdlStartCal.getTime()) + " "+strtTm);
							//종료 일 +시간
							cloneAvntWcsVo.setSchdlEndDt(dateFormat.format(schdlEndCal.getTime()) + " "+endTm);							
							queryQueue.insert(cloneAvntWcsVo);
							// 게시파일 저장
						}
					}else{	
						cloneWcsVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));					
						cloneWcsVo.setSchdlPid(schdlPid);
						// 게시파일 저장
						//wcFileSvc.saveScdlFile(request, cloneWcsVo.getSchdlId(), queryQueue, "indiv");
						wcFileSvc.saveFile(request, cloneWcsVo.getSchdlId(), queryQueue, copyFileList);
						
						queryQueue.insert(cloneWcsVo);
						
						if(wcSchdlDeptRVoList!=null) // 부서일정 - 부서선택 목록 저장
							saveWcSchdlDeptRVo(request, queryQueue, wcSchdlDeptRVoList, cloneWcsVo.getSchdlId(), false);
						
						if(wcGuestVo.size() != 0){					
							for(WcPromGuestDVo wcguest:  wcGuestVo){	
								WcPromGuestDVo cloneWcGuest=(WcPromGuestDVo) wcguest.clone();
								cloneWcGuest.setSchdlId(cloneWcsVo.getSchdlId());
								queryQueue.insert(cloneWcGuest);							
							}
						}	
					}
						
					
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	//시간 초기화
	public static Calendar getConvertYmd(Calendar dt){
		Calendar convertCal = Calendar.getInstance();
		convertCal.set(dt.get(Calendar.YEAR),dt.get(Calendar.MONTH),dt.get(Calendar.DATE));
		convertCal.set( Calendar.HOUR_OF_DAY, 0 );
		convertCal.set( Calendar.MINUTE, 0 );
		convertCal.set( Calendar.SECOND, 0 );
		convertCal.set( Calendar.MILLISECOND, 0 );
		return convertCal;
	}

//	public static void main(String[] arg){
//		
//	}
	public static Calendar stringConvertCal(String date,boolean resetHHmm){
		//date ex)2014-05-30
		int Year = Integer.parseInt(date.substring(0,4));
		int Month =Integer.parseInt(date.substring(5,7));
		int Date = Integer.parseInt(date.substring(8,10));
		
		Calendar convertCal = Calendar.getInstance();
		convertCal.set(Year,Month-1,Date);
		if(resetHHmm){
			convertCal.set( Calendar.HOUR_OF_DAY, 0 );
			convertCal.set( Calendar.MINUTE, 0 );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}else{
			int hour = Integer.parseInt(date.substring(11,13));
			int minuit = Integer.parseInt(date.substring(14,16));
			convertCal.set( Calendar.HOUR_OF_DAY, hour );
			convertCal.set( Calendar.MINUTE, minuit );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}
		
		return convertCal;
	}
	
	public static Calendar stringConvertCal(String date,boolean resetHHmm,String timeZone){
		//date ex)2014-05-30
		int Year = Integer.parseInt(date.substring(0,4));
		int Month =Integer.parseInt(date.substring(5,7));
		int Date = Integer.parseInt(date.substring(8,10));
		
		Calendar convertCal = Calendar.getInstance();
		convertCal.setTimeZone(TimeZone.getTimeZone(timeZone));
		convertCal.set(Year,Month-1,Date);
		if(resetHHmm){
			convertCal.set( Calendar.HOUR_OF_DAY, 0 );
			convertCal.set( Calendar.MINUTE, 0 );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}else{
			int hour = Integer.parseInt(date.substring(11,13));
			int minuit = Integer.parseInt(date.substring(14,16));
			convertCal.set( Calendar.HOUR_OF_DAY, hour );
			convertCal.set( Calendar.MINUTE, minuit );
			convertCal.set( Calendar.SECOND, 0 );
			convertCal.set( Calendar.MILLISECOND, 0 );
		}
		
		return convertCal;
	}
	
	public void setAgntSave(List<WcAgntRVo> wcAgntVos,QueryQueue queryQueue, String userUid) throws SQLException{
		
		if(!userUid.equalsIgnoreCase("")){
			//전체삭제하려면 새로운 객체팔요
			WcAgntRVo removeAllAgntVo = new WcAgntRVo();
			removeAllAgntVo.setUserUid(userUid);
			queryQueue.delete(removeAllAgntVo);
			//전체 삭제 후 다시 insert
			if(wcAgntVos.size() != 0){
				for(WcAgntRVo wcagnt:  wcAgntVos){
					queryQueue.insert(wcagnt);
				}

			}
		}
		
	}
	public void setApntSave(List<WcApntrRVo> wcApntVos,QueryQueue queryQueue ,String userUid) throws SQLException{
		
		if(!userUid.equalsIgnoreCase("")){
			//전체삭제하려면 새로운 객체팔요
			WcApntrRVo removeAllApntVo = new WcApntrRVo();
			removeAllApntVo.setUserUid(userUid);
			//전체 삭제 후 다시 insert
			queryQueue.delete(removeAllApntVo);
			if(wcApntVos.size() != 0){
				for(WcApntrRVo wcapnt:  wcApntVos){
					queryQueue.insert(wcapnt);
				}			
			}
		}
		

	}
	@SuppressWarnings("unchecked")
	public List<WcApntrSchdlDVo> getApntSchdlList(String langTypCd, String userUid) throws CmException, IOException{
		
		List<WcApntrSchdlDVo> wcApntrSchdlVos = new ArrayList<WcApntrSchdlDVo>();
		
		WcApntrSchdlDVo wcApntVo = new WcApntrSchdlDVo();
		wcApntVo.setUserUid(userUid);
		wcApntVo.setOrderBy("ORDR");
		// 삭제할 목록
		List<WcApntrSchdlDVo> removeList = new ArrayList<WcApntrSchdlDVo>();
		try {
			wcApntrSchdlVos =  (List<WcApntrSchdlDVo>) commonDao.queryList(wcApntVo);
			for(WcApntrSchdlDVo apnt : wcApntrSchdlVos){
				Map<String, Object> agntMap =  orCmSvc.getUserMap(apnt.getApntrUid(), langTypCd);
				if(agntMap==null){
					removeList.add(apnt);
					continue;
				}
				//리소스명
				apnt.setRescNm((String) agntMap.get("userNm"));
				//부서리소스명
				apnt.setDeptRescNm((String) agntMap.get("deptRescNm"));
				//직위명
				apnt.setPositNm((String) agntMap.get("positNm"));
				//직무명 
				apnt.setDutyNm((String) agntMap.get("dutyNm"));
				//직책명
				apnt.setTitleNm((String) agntMap.get("titleNm"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(removeList.size()>0 && wcApntrSchdlVos!=null && wcApntrSchdlVos.size()>0){
			for(WcApntrSchdlDVo removeVo : removeList){
				wcApntrSchdlVos.remove(removeVo);
			}
		}
		
		return wcApntrSchdlVos;
	}
	
	@SuppressWarnings("unchecked")
	public List<WcApntrRVo> getApntTgtList(String langTypCd, String userUid) throws CmException, IOException{
		
		List<WcApntrRVo> wcApntrVos = new ArrayList<WcApntrRVo>();
		
		WcApntrRVo wcApntVo = new WcApntrRVo();
		wcApntVo.setApntrUid(userUid);
		// 삭제할 목록
		List<WcApntrRVo> removeList = new ArrayList<WcApntrRVo>();
		try {
			wcApntrVos =  (List<WcApntrRVo>) commonDao.queryList(wcApntVo);
			for(WcApntrRVo apnt : wcApntrVos){
				Map<String, Object> agntMap =  orCmSvc.getUserMap(apnt.getUserUid(), langTypCd);
				if(agntMap==null){
					removeList.add(apnt);
					continue;
				}
				//리소스명
				apnt.setRescNm((String) agntMap.get("userNm"));
				//부서리소스명
				apnt.setDeptRescNm((String) agntMap.get("deptRescNm"));
				//직위명
				apnt.setPositNm((String) agntMap.get("positNm"));
				//직무명 
				apnt.setDutyNm((String) agntMap.get("dutyNm"));
				//직책명
				apnt.setTitleNm((String) agntMap.get("titleNm"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		if(removeList.size()>0 && wcApntrVos!=null && wcApntrVos.size()>0){
			for(WcApntrRVo removeVo : removeList){
				wcApntrVos.remove(removeVo);
			}
		}
		
		return wcApntrVos;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<WcAgntRVo> getAgntTgtList(String langTypCd, String agntUid) throws CmException, IOException{

		List<WcAgntRVo> wcAgntVos = new ArrayList<WcAgntRVo>();

		WcAgntRVo wcAgntVo = new WcAgntRVo();
		wcAgntVo.setAgntUid(agntUid);
		// 삭제할 목록
		List<WcAgntRVo> removeList = new ArrayList<WcAgntRVo>();
		try {
			wcAgntVos =  (List<WcAgntRVo>) commonDao.queryList(wcAgntVo);
			
			for(WcAgntRVo agnt : wcAgntVos){
				Map<String, Object> agntMap =  orCmSvc.getUserMap(agnt.getUserUid(), langTypCd);
				if(agntMap==null){
					removeList.add(agnt);
					continue;
				}
				//if(!agntMap.containsKey("rescNm") || agntMap.get("rescNm")==null || "".equals(agntMap.get("rescNm"))) continue;
				//리소스명
				agnt.setRescNm((String) agntMap.get("rescNm"));
				//부서리소스명
				agnt.setDeptRescNm((String) agntMap.get("deptRescNm"));
				//직위명
				agnt.setPositNm((String) agntMap.get("positNm"));
				//직무명 
				agnt.setDutyNm((String) agntMap.get("dutyNm"));
				//직책명
				agnt.setTitleNm((String) agntMap.get("titleNm"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		if(removeList.size()>0 && wcAgntVos.size()>0){
			for(WcAgntRVo removeVo : removeList){
				wcAgntVos.remove(removeVo);
			}
		}
		return wcAgntVos;
	}
	
	@SuppressWarnings("unchecked")
	public String getAgntTgtAuth(String langTypCd, String userVoUid, String userUid) throws SQLException{
		String userAuth =null;
		List<WcAgntRVo> wcAgntVos = new ArrayList<WcAgntRVo>();

		WcAgntRVo wcAgntVo = new WcAgntRVo();
		wcAgntVo.setAgntUid(userVoUid);
		
		wcAgntVos = (List<WcAgntRVo>) commonDao.queryList(wcAgntVo);
		if(wcAgntVos.size() != 0){
			for(WcAgntRVo wcAgnt : wcAgntVos){
				if(userUid.equalsIgnoreCase(wcAgnt.getUserUid())){
					userAuth = "pass";
					break;
				}else{
					userAuth = "fail";
				}
			}
		}else{
			userAuth = "fail";
		}

		return userAuth;
	}
	
	@SuppressWarnings("unchecked")
	public List<WcAgntRVo> getAgntList(String langTypCd, String userUid) throws CmException, IOException{
		
		List<WcAgntRVo> wcAgntVos = new ArrayList<WcAgntRVo>();
		
		WcAgntRVo wcAgntVo = new WcAgntRVo();
		wcAgntVo.setUserUid(userUid);
		// 삭제할 목록
		List<WcAgntRVo> removeList = new ArrayList<WcAgntRVo>();
		try {
			wcAgntVos =  (List<WcAgntRVo>) commonDao.queryList(wcAgntVo);
			for(WcAgntRVo agnt : wcAgntVos){
				Map<String, Object> agntMap =  orCmSvc.getUserMap(agnt.getAgntUid(), langTypCd);
				if(agntMap==null){
					removeList.add(agnt);
					continue;
				}
				//리소스명
				agnt.setRescNm((String) agntMap.get("rescNm"));
				//부서리소스명
				agnt.setDeptRescNm((String) agntMap.get("deptRescNm"));
				//직위명
				agnt.setPositNm((String) agntMap.get("positNm"));
				//직무명 
				agnt.setDutyNm((String) agntMap.get("dutyNm"));
				//직책명
				agnt.setTitleNm((String) agntMap.get("titleNm"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(removeList.size()>0 && wcAgntVos!=null && wcAgntVos.size()>0){
			for(WcAgntRVo removeVo : removeList){
				wcAgntVos.remove(removeVo);
			}
		}
		return wcAgntVos;
	}
	
	@SuppressWarnings("unchecked")
	public List<WcApntrRVo> getApntList(String langTypCd, String userUid) throws CmException, IOException{
		
		List<WcApntrRVo> wcApntVos = new ArrayList<WcApntrRVo>();
		
		WcApntrRVo wcApntVo = new WcApntrRVo();
		wcApntVo.setUserUid(userUid);
		// 삭제할 목록
		List<WcApntrRVo> removeList = new ArrayList<WcApntrRVo>();
		try {			
			wcApntVos =  (List<WcApntrRVo>) commonDao.queryList(wcApntVo);
			for(WcApntrRVo apnt : wcApntVos){
				Map<String, Object> apntMap =  orCmSvc.getUserMap(apnt.getApntrUid(), langTypCd);
				if(apntMap==null){
					removeList.add(apnt);
					continue;
				}
				//리소스명
				apnt.setRescNm((String) apntMap.get("rescNm"));
				//부서리소스명
				apnt.setDeptRescNm((String) apntMap.get("deptRescNm"));
				//직위명
				apnt.setPositNm((String) apntMap.get("positNm"));
				//직무명 
				apnt.setDutyNm((String) apntMap.get("dutyNm"));
				//직책명
				apnt.setTitleNm((String) apntMap.get("titleNm"));
				
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(removeList.size()>0 && wcApntVos!=null && wcApntVos.size()>0){
			for(WcApntrRVo removeVo : removeList){
				wcApntVos.remove(removeVo);
			}
		}
		return wcApntVos;
	}
	
	/** 환경설정조회 */
	public ModelMap getAgntApntModel(HttpServletRequest request, ModelMap model, String userUid) throws CmException, IOException {
		 
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			List<WcAgntRVo> agntLst = getAgntList(langTypCd, userUid);		   
			List<WcApntrRVo> apntLst = getApntList(langTypCd, userUid);
			model.put("returnAgntLst", agntLst);
			model.put("returnApntLst", apntLst);
			
			return model;
	}
	
	/** 나의일정검색 목록 조회*/
	@SuppressWarnings("unchecked")
	public List<WcSchdlBVo> getSchdlGroupList(WcSchdlBVo wcsVo) throws SQLException {
		return (List<WcSchdlBVo>)commonDao.queryList(wcsVo);
	}
	
	
	/** 나의일정검색  목록수 조회 */
	public Integer getSchdlGroupListCnt(WcSchdlBVo wcsVo) throws SQLException {
		return commonDao.count(wcsVo);
	}
	
	
	
	//나의일정검색  Map return
	public Map<String,Object> getSchdlMapList(HttpServletRequest request , WcSchdlBVo wcsVo) throws SQLException, IOException {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");

		//String schWord = wcsVo.getSchWord();//검색어
		
		Integer recodeCount = this.getSchdlGroupListCnt(wcsVo);
		
		
		//참석자 조회용 compId 
		//UserVo userVo = LoginSession.getUser(request);
		//목록 조회
		List<WcSchdlBVo> schdlGroupBVoList = this.getSchdlGroupList(wcsVo);
		
		Map<String, Object> schdlGroupMap;
		List<Map<String, Object>> schdlGroupBMapList = new ArrayList<Map<String, Object>>();
		for(WcSchdlBVo storedWcSchdlGroupBVo : schdlGroupBVoList){
			
			storedWcSchdlGroupBVo.setAfterDay(betweenDay(storedWcSchdlGroupBVo.getSchdlStartDt(),storedWcSchdlGroupBVo.getSchdlEndDt()));
			
			schdlGroupMap = VoUtil.toMap(storedWcSchdlGroupBVo, null);
			schdlGroupBMapList.add(schdlGroupMap);
		}
		rsltMap.put("schdlGroupBMapList", schdlGroupBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}


	//날짜 시작일과 종료일 사이 계산
	public String betweenDay(String schdlStartDt, String schdlEndDt) {
		long startDay,endDay; 
		
		// Cal객체와 MilliSecond 로 변환 
		startDay = stringConvertCal(schdlStartDt, true).getTime().getTime(); 
		endDay = stringConvertCal(schdlEndDt, true).getTime().getTime();  
		
		// 계산 
	    int days =(int)((endDay-startDay)/(1000*60*60*24)); 
	    //ex) (2014-05-20) - (2014-05-15) = 5일이지만 당일 포함 6일간 이니 +1 해줌
		return String.valueOf(days+1);
	}
	
	@SuppressWarnings("unchecked")
	public List<WcPromGuestDVo> getPromGuestLst(WcPromGuestDVo wcPromGuest) throws SQLException {
		return (List<WcPromGuestDVo>)commonDao.queryList(wcPromGuest);
	}
	
	//개인 즐겨찾기 목록
	@SuppressWarnings("unchecked")
	public List<WcBumkDVo> getBumkDept(String userUid) throws SQLException{
		WcBumkDVo condition=new WcBumkDVo();		
		condition.setUserUid(userUid);
		condition.setUserDeptTypCd("2");
		return (List<WcBumkDVo>) commonDao.queryList(condition);
	}
	
	//개인 즐겨찾기 목록
	@SuppressWarnings("unchecked")
	public List<WcBumkDVo> getBumkPsn(String userUid) throws SQLException{
		WcBumkDVo condition=new WcBumkDVo();		
		condition.setUserUid(userUid);
		condition.setUserDeptTypCd("1");
		return (List<WcBumkDVo>) commonDao.queryList(condition);
	}
	
	//일정보내기 스케쥴 조회
	public List<WcSchdlBVo> getSchdlSendMail(String startDay, String option, HttpServletRequest request, CmEmailBVo cmEmailBVo ){
		//참석자 조회용 compId 
		UserVo userVo = LoginSession.getUser(request);
		
		List<WcSchdlBVo> wcsList = new ArrayList<WcSchdlBVo>();
		
		int year = Integer.parseInt(startDay.substring(0,4));
		int month =Integer.parseInt(startDay.substring(5,7));
		int day = Integer.parseInt(startDay.substring(8,10));
		int startWeek =0;
		
		WcScdCalMonth wcsMonth = getScdMonth(year, month, -1, null, null,null, null, null, null,
				userVo.getUserUid(), userVo.getDeptId(), userVo.getCompId(), null, true);
		//옵션 : M:월간/W:주간/D:일일/
		if(option.equalsIgnoreCase("M")){
			for(int i=0; i < wcsMonth.getWeeks().size(); i++){
				for(int j=0; j < wcsMonth.getWeeks().get(i).getDays().size(); j++){
					for(int x=0; x < wcsMonth.getWeeks().get(i).getDays().get(j).getScds().size(); x++){
						if(wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x) != null
						&& wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x).getSchdlId() != null){
							wcsList.add( wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x));
						}
					}
				}
			}
			cmEmailBVo.setSubj("[" + userVo.getUserNm() + "] " + wcsMonth.getMonth()+ " 월 일정");
			
		}else if(option.equalsIgnoreCase("W")){
	//해당 일의 주차 를 찾는다.
	findWeek :for(int i=0; i < wcsMonth.getWeeks().size(); i++){
				for(int j=0; j < wcsMonth.getWeeks().get(i).getDays().size(); j++){
					if(wcsMonth.getWeeks().get(i).getDays().get(j).getDay() == day){
						startWeek = wcsMonth.getWeeks().get(i).getWeek() -1;
						break findWeek;
					}
				}
			}
			 //해당 주차 의 schld를 구한다.
			 for(int i=0; i < 7; i++){
				for(int j=0; j < wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().size(); j++){
					if(wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().get(j) != null
					&& wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().get(j).getSchdlId() != null){
						wcsList.add( wcsMonth.getWeeks().get(startWeek).getDays().get(i).getScds().get(j));
					}
				}
			}
			 
			 cmEmailBVo.setSubj("[" + userVo.getUserNm() + "] " + wcsMonth.getMonth()+ " 월" + wcsMonth.getWeeks().get(startWeek).getWeek() +" 째주 일정");
			
		}else if(option.equalsIgnoreCase("D")){
			findDay : for(int i=0; i < wcsMonth.getWeeks().size(); i++){
				for(int j=0; j < wcsMonth.getWeeks().get(i).getDays().size(); j++){
					//day 비교 후 같은 day schdl만 구해온다.
					if(wcsMonth.getWeeks().get(i).getDays().get(j).getDay() == day){
						for(int x=0; x < wcsMonth.getWeeks().get(i).getDays().get(j).getScds().size(); x++){
							if(wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x) != null 
									&& wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x).getSchdlId() != null){
								wcsList.add( wcsMonth.getWeeks().get(i).getDays().get(j).getScds().get(x));
							}
						}
						break findDay;
					}
					
				}
			}
			
			cmEmailBVo.setSubj("[" + userVo.getUserNm() + "] " + wcsMonth.getMonth()+ " 월" + day +" 일 일정");
		}
		
		return wcsList;
		
	}
	
	/** 참석자 정보 저장 */
	public List<WcPromGuestDVo> saveGuest(HttpServletRequest request,QueryQueue queryQueue , WcSchdlBVo wcsVo) {
		WcPromGuestDVo deleteGuestVo = new WcPromGuestDVo();
		deleteGuestVo.setSchdlId(wcsVo.getSchdlId());
		queryQueue.delete(deleteGuestVo);
		
		@SuppressWarnings("unchecked")
		List<WcPromGuestDVo> wcPromGuestDVoList = (List<WcPromGuestDVo>)VoUtil.bindList(request, WcPromGuestDVo.class, new String[]{"guestNm"});
		
		// 등록할 목록이 있을경우 등록처리한다.
		if(wcPromGuestDVoList.size() > 0 ){
			for(WcPromGuestDVo insertGuestVo : wcPromGuestDVoList){
				insertGuestVo.setStatCd("T");
				insertGuestVo.setSchdlId(wcsVo.getSchdlId());
				if(insertGuestVo.getEmailYn()!=null) insertGuestVo.setEmailSendYn(insertGuestVo.getEmailYn());
				else insertGuestVo.setEmailSendYn(wcsVo.getEmailSendYn());
				
				queryQueue.insert(insertGuestVo);
			}
		}
		
		return wcPromGuestDVoList;
	}
	
	
	/** 나의 일정 건수 조회 */
	public String getSchdlCnt( UserVo userVo ) throws SQLException{
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		wcSchdlBVo.setUserUid(userVo.getUserUid());
		wcSchdlBVo.setDeptId(userVo.getDeptId());
		wcSchdlBVo.setCompId(userVo.getCompId());
		wcSchdlBVo.setSchdlStartDt(StringUtil.getCurrYmd());
		//wcSchdlBVo.setSchdlEndDt(StringUtil.getCurrYmd());
		
		//목록 조회 건수
		Integer recodeCount = commonSvc.count(wcSchdlBVo);
		if(recodeCount == null) return "0";
		return String.valueOf(recodeCount.intValue());
	}
	
	/** 설정된 국가코드 조회 */
	public String getNatCd(UserVo userVo) throws CmException, SQLException{
		String natCd = null;
		// 사용자 설정 조회
		WcNatBVo wcNatBVo = new WcNatBVo();
		wcNatBVo.setCompId(userVo.getCompId());			
		wcNatBVo.setUserUid(userVo.getUserUid());
		wcNatBVo = (WcNatBVo)commonSvc.queryVo(wcNatBVo);
		if(wcNatBVo != null) natCd = wcNatBVo.getCd();
		
		// 목록 조회 초기화
		wcNatBVo = new WcNatBVo();
		wcNatBVo.setCompId(userVo.getCompId());
		// 해당회사에 설정된 국가코드 목록 조회
		@SuppressWarnings("unchecked")
		List<WcNatBVo> wcNatBVoList = (List<WcNatBVo>)commonDao.queryList(wcNatBVo);
		
		// 사용자 설정이 있을경우 국가코드 유무 체크(관리자가 해당 코드를 삭제하였을경우)
		if(natCd!=null && !natCd.isEmpty()){
			// 사용여부
			boolean isUse = false;
			for(WcNatBVo storedWcNatBVo : wcNatBVoList){
				if(natCd.equals(storedWcNatBVo.getCd())){
					isUse = true; break;
				}
			}
			if(isUse) return natCd;
			natCd = null;
		}
		
		// 사용자 설정이 없을경우 관리자 설정을 조회한다.
		if(natCd==null || natCd.isEmpty()){
			for(WcNatBVo storedWcNatBVo : wcNatBVoList){
				if(storedWcNatBVo.getDftYn()!=null && "Y".equals(storedWcNatBVo.getDftYn())){
					natCd = storedWcNatBVo.getCd(); break;
				}
			}
			if(natCd != null && !natCd.isEmpty()) return natCd;
			// 관리자 기본설정이 없을경우 첫번째 국가코드를 리턴한다.
			if(wcNatBVoList.size()>0) return wcNatBVoList.get(0).getCd();
			// 관리자가 등록한 국가목록이 없을경우 기본값을 리턴한다.
			if(natCd==null || natCd.isEmpty()) return dftNatCd;
		}
		return null;
	}
	
	/** 기념일 상세 조회 */
	public WcAnnvDVo getWcAnnvDVo(String langTypCd, String compId, String schdlId) throws CmException, SQLException{
		// 기념일 상세
		WcAnnvDVo wcAnnvDVo = new WcAnnvDVo();
		wcAnnvDVo.setCompId(compId);
		wcAnnvDVo.setSchdlId(schdlId);
		wcAnnvDVo.setQueryLang(langTypCd);
		return (WcAnnvDVo)commonSvc.queryVo(wcAnnvDVo);
	}
	
	/** 기념일의 국가코드와 비교 */
	public boolean isSchdlNatChk(WcAnnvDVo wcAnnvDVo, String natCd) throws CmException, SQLException{
		if (wcAnnvDVo != null && !natCd.equals(wcAnnvDVo.getNatCd())) {
			return false;
		}
		return true;
	}
	
	/** 지정인 목록 체크 */
	@SuppressWarnings("unchecked")
	public boolean chkSchdlUser(String userUid, String chkUserUid, String actKey) throws SQLException{
		boolean returnChk = false;
		if("agnt".equals(actKey)){ // 대리인
			WcAgntRVo wcAgntrVo = new WcAgntRVo();
			wcAgntrVo.setUserUid(userUid);
			List<WcAgntRVo> list =  (List<WcAgntRVo>) commonDao.queryList(wcAgntrVo);
			for(WcAgntRVo storedWcAgntrRVo : list){
				if(storedWcAgntrRVo.getAgntUid().equals(chkUserUid)){
					returnChk=true;
					break;
				}
			}
		}else if("apnt".equals(actKey)){ // 지정인
			WcApntrRVo wcApntVo = new WcApntrRVo();
			wcApntVo.setUserUid(userUid);
			List<WcApntrRVo> list =  (List<WcApntrRVo>) commonDao.queryList(wcApntVo);
			for(WcApntrRVo storedWcApntrRVo : list){
				if(storedWcApntrRVo.getApntrUid().equals(chkUserUid)){
					returnChk=true;
					break;
				}
			}
		}
		
		return returnChk;
	}
	
	/** 사용자 그룹 목록 조회 */
	public String[] getGrpList(String userUid) throws SQLException{
		WcSchdlGrpBVo wcSchdlGrpBVo = new WcSchdlGrpBVo(); 
		wcSchdlGrpBVo.setUserUid(userUid);
		wcSchdlGrpBVo.setFncMy("Y");
		
		List<WcSchdlGrpBVo> grpList = getWcSchdlGroupList(wcSchdlGrpBVo);
		List<String> returnList = new ArrayList<String>();
		for(WcSchdlGrpBVo storedWcSchdlGrpBVo : grpList){
			returnList.add(storedWcSchdlGrpBVo.getSchdlGrpId());
		}
		if(returnList.size()>0){
			return ArrayUtil.toArray(returnList);
		}
		
		return null;
	}
	
	/** 언어코드 변환 */
	public String getCalLangTypCd(String langTypCd){
		if(langTypCd.startsWith("zh")) return "zh-cn";
		return langTypCd;
	}
	
	/** 음력 -> 양력 변환*/
	public List<WcSchdlBVo> getLunarSchdlList(List<WcSchdlBVo> list){
		if(list==null || list.size()==0) return null;
		String strtDate, strtTime, endDate, endTime;
		for(WcSchdlBVo vo : list){
			if(vo.getSolaLunaYn()==null || "Y".equals(vo.getSolaLunaYn())) continue;
			strtDate = lunalCalenderUtil.toSolar(vo.getSchdlStartDt());
			strtTime = vo.getSchdlStartDt().substring(11);
			vo.setSchdlLunaStartDt(vo.getSchdlStartDt());			
			vo.setSchdlStartDt(strtDate+" "+strtTime);
			endDate = lunalCalenderUtil.toSolar(vo.getSchdlEndDt());
			endTime = vo.getSchdlEndDt().substring(11);
			vo.setSchdlEndDt(endDate+" "+endTime);
			vo.setSchdlLunaEndDt(vo.getSchdlEndDt());
		}
		return list;
	}
	
	//이전 다음 일정 세팅
    public String getDateOfDay(  String startDay , String tabType , String schedulePmValue , String clicknps , int stepValue){
 		if(startDay != null && ( schedulePmValue == null || "".equals(schedulePmValue) )){
 			return startDay;
 		}
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		GregorianCalendar cal = new GregorianCalendar ( );
 		String daysValue = "";
 		if(startDay != null && !"".equals(startDay)){
 			cal.set(Integer.parseInt(startDay.split("-")[0]) , Integer.parseInt(startDay.split("-")[1])-1 , Integer.parseInt(startDay.split("-")[2]));
 	 		
 	 		if("month".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.MONTH, 1);
 	 			else
 	 				cal.add(Calendar.MONTH, -1);
 	 		}else if("week".equals(tabType) || "weeks".equals(tabType) || "agendaWeek".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.DATE, 7);
 	 			else
 	 				cal.add(Calendar.DATE, -7);
 	 		}else if("day".equals(tabType) || "agendaDay".equals(tabType)){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.DATE, 1);
 	 			else
 	 				cal.add(Calendar.DATE, -1);
 	 		}else if( "year".equals(tabType) ){
 	 			if("p".equals(schedulePmValue))
 	 				cal.add(Calendar.YEAR, 1);
 	 			else
 	 				cal.add(Calendar.YEAR, -1);
 	 		}else if("allSchedule".equals(tabType)){
 	 			if("".equals(clicknps)){
 	 				if("p".equals(schedulePmValue))
 	 	  				cal.add(Calendar.DATE, 1);
 	 	  			else
 	 	  				cal.add(Calendar.DATE, -1);
 	 			}else{
 	 				if("p".equals(schedulePmValue))
 	 	  				cal.add(Calendar.DATE, stepValue);
 	 	  			else
 	 	  				cal.add(Calendar.DATE, -stepValue);
 	 			}
 	 		}
 	 		Date days = cal.getTime();
 	 		daysValue = sdf.format(days);
 		}else{
 			daysValue = sdf.format((Date)(cal.getTime()));
 		}
 		return daysValue;
 	}
    
    /** 반복일정 수정 저장[파일]*/
    @SuppressWarnings("unchecked")
    public void repetSetup(WcRepetSetupDVo wcRepetVo, WcSchdlBVo wcsVo,
			List<WcPromGuestDVo> wcGuestVo, QueryQueue queryQueue, HttpServletRequest request, List<DmCommFileDVo> copyFileList) {
		
    	List<WcSchdlDeptRVo> wcSchdlDeptRVoList = null;
		// 부서일정 관계 저장 - 부서일정 선택부서 목록
		if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
			wcSchdlDeptRVoList = (List<WcSchdlDeptRVo>)VoUtil.bindList(request, WcSchdlDeptRVo.class, new String[]{"orgId"});
		
		if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY")){
			repetDay(wcRepetVo, wcsVo, wcGuestVo, queryQueue, request, copyFileList, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK")){
			repetWeek(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_MY")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_MT")){
			repetMoly(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_DY_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList, wcSchdlDeptRVoList);
		}else if(wcRepetVo.getRepetPerdCd().equalsIgnoreCase("EV_WK_YR")){
			repetYely(wcRepetVo, wcsVo, wcGuestVo,queryQueue, request, copyFileList, wcSchdlDeptRVoList);
		}
	}
    
    /** 그룹권한 정보 */
    public WcSchdlGrpMbshDVo getWcSchdlGrpMbshDVo(UserVo userVo, String grpId) throws SQLException{
    	WcSchdlGrpMbshDVo wcSchdlGrpMbshDVo = new WcSchdlGrpMbshDVo();
		wcSchdlGrpMbshDVo.setCompId(userVo.getCompId());
		wcSchdlGrpMbshDVo.setUserUid(userVo.getUserUid());
		wcSchdlGrpMbshDVo.setSchdlGrpId(grpId);
		wcSchdlGrpMbshDVo=(WcSchdlGrpMbshDVo)commonSvc.queryVo(wcSchdlGrpMbshDVo);
		return wcSchdlGrpMbshDVo;
    }
    
    /** 일정권한 체크 */
    public void setSchdlAuth(ModelMap model, WcSchdlBVo wcSchdlBVo, String userUid, String schdlId) throws SQLException{
    	boolean isChk = false;
    	if(wcSchdlBVo.getUserUid().equals(userUid)) isChk=true;
    	
    	if(!isChk){
    		// 대리인 여부 확인
    		boolean isSrch = chkSchdlUser(wcSchdlBVo.getUserUid(), userUid, "agnt");
    		if(isSrch) isChk=true;
    	}
    	
    	/*if(!isChk){
    		if((wcSchdlBVo.getSchdlKndCd()!=null && !"2".equals(wcSchdlBVo.getSchdlKndCd())) || wcSchdlBVo.getGrpId()==null || wcSchdlBVo.getGrpId().isEmpty())
    		wcSchdlGrpMbshDVo = wcScdManagerSvc.getWcSchdlGrpMbshDVo(userVo, storedWcSchdlBVo.getGrpId());
    	}*/
		
		if(isChk) model.put("editYn", "Y");
		
    }
    
    /** 공휴일 조회 */
    public List<String> getSelectSchdlList(String compId, String langTypCd, String natCd, String start, String end, String format) throws SQLException{
    	List<String> excludeList = new ArrayList<String>();
    	// 공휴일 조회
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		wcSchdlBVo.setInstanceQueryId("com.innobiz.orange.web.wc.dao.SchdlBDao.selectSchdlB");
		wcSchdlBVo.setQueryLang(langTypCd);
		wcSchdlBVo.setCompId(compId);
		wcSchdlBVo.setNatCd(natCd); // 국가코드
		// 기념일 조회(음력일정을 구하기 위해 한달전 일정까지 조회한다.)
		wcSchdlBVo.setSchdlStartDt(start);
		wcSchdlBVo.setSchdlEndDt(end);
		wcSchdlBVo.setSchdlTypCd("5");//기념일
		wcSchdlBVo.setHoliYn("Y"); // 공휴일
		
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> annvList = (List<WcSchdlBVo>)commonSvc.queryList(wcSchdlBVo);
		
		if(annvList!=null && annvList.size()>0){
			annvList=getLunarSchdlList(annvList); //음력=>양력 변환
			String strtDt=null,endDt=null;
			for(WcSchdlBVo storedWcSchdlBVo : annvList){
				strtDt=storedWcSchdlBVo.getSchdlStartDt().substring(0,10);
				endDt=storedWcSchdlBVo.getSchdlEndDt().substring(0,10);
				excludeList.addAll(getFromToDate(strtDt.replaceAll("[-: ]", ""), endDt.replaceAll("[-: ]", ""), null, true, format, false));
			}
			// 중복제거
			Set<String> hs = new HashSet<String>(excludeList);
			excludeList = new ArrayList<String>(hs);
		}
		
		return excludeList;
    }
    
   /* *//** 기간별 날짜 조회 *//*
    public List<String> getFromToDateList(String compId, String langTypCd, String natCd, 
    		String start, String end, String holiYn) throws SQLException{
    	if(start==null || start.isEmpty()) // 시작일자가 없으면 현재 날짜 삽입
			start=commonDao.querySysdate(null).substring(0,10);
		
		if(end==null || end.isEmpty()) // 종료일자가 없으면 현재 날짜 삽입
			end=commonDao.querySysdate(null).substring(0,10);
		
		String realStrtDt = getDateOfDay(start, "month", "s", null, 1);
		
		// 제외할 날짜 목록
		List<String> excludeList = null;
		
		if(holiYn!=null && !holiYn.isEmpty() && "N".equals(holiYn)){
			excludeList = getSelectSchdlList(compId, langTypCd, natCd, realStrtDt, end);
		}
		
		realStrtDt=start.replaceAll("[-: ]", "");
		
		end=end.replaceAll("[-: ]", "");
		System.out.println("end : "+end);
		List<String> fromToList = getFromToDate(realStrtDt, end, excludeList, excludeList==null ? true : false);
    }*/
    
    /** 기간별 날짜 조회 - List<String>*/
    public List<String> getFromToDate(String start, String end, List<String> excludeList, boolean isInclude, String format, boolean isOnlyHoli){
    	if(start==null || start.isEmpty()) return null;
    	GregorianCalendar cal = new GregorianCalendar();
    	cal.set(Integer.parseInt(start.substring(0,4)) , Integer.parseInt(start.substring(4,6))-1 , Integer.parseInt(start.substring(6,8)));
    	if(format==null) format="yyyyMMdd";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    	
    	List<String> list = new ArrayList<String>();
    	
    	if(isInclude || (!isInclude && ((!isOnlyHoli && dayOfWeek != Calendar.SATURDAY  && dayOfWeek != Calendar.SUNDAY && !excludeList.contains(start)) 
    			|| (isOnlyHoli && (dayOfWeek == Calendar.SATURDAY  || dayOfWeek == Calendar.SUNDAY || excludeList.contains(start)) ) )  )){
    		list.add(sdf.format(cal.getTime())); // 시작일 등록
    	}
    		
    	
    	String date=null;
    	String tmpDate;
    	while(true){
    		cal.add(Calendar.DATE, 1);
    		date=sdf.format(cal.getTime());
    		tmpDate=date.replaceAll("[-: ]", "");
    		if(Integer.parseInt(tmpDate)>Integer.parseInt(end)) break; // 마지막 날짜 체크
    		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    		
    		if(!isInclude && ((!isOnlyHoli && (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || (excludeList!=null && excludeList.contains(tmpDate)))) 
    	    		|| (isOnlyHoli && dayOfWeek != Calendar.SATURDAY  && dayOfWeek != Calendar.SUNDAY && !excludeList.contains(tmpDate))) // 주말포함여부
    				) // 제외날짜 체크
        		continue;
    		list.add(date);
    	}
    	
    	return list;
    }
    
    /** 기간별 날짜 조회 - List<String[]>*/
    public List<String[]> getFromToDates(String start, String end, List<String> excludeList, boolean isInclude, String format){
    	if(start==null || start.isEmpty()) return null;
    	GregorianCalendar cal = new GregorianCalendar();
    	cal.set(Integer.parseInt(start.substring(0,4)) , Integer.parseInt(start.substring(4,6))-1 , Integer.parseInt(start.substring(6,8)));
    	if(format==null) format="yyyyMMdd";
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    	
    	List<String[]> list = new ArrayList<String[]>();
    	if(isInclude || (!isInclude && ((dayOfWeek != Calendar.SATURDAY  && dayOfWeek != Calendar.SUNDAY ) && 
    			(excludeList==null || (excludeList!=null && !excludeList.contains(start))))))
    		list.add(new String[]{sdf.format(cal.getTime()),String.valueOf(dayOfWeek)}); // 시작일 등록
    	
    	String date=null;
    	String tmpDate;
    	while(true){
    		cal.add(Calendar.DATE, 1);
    		date=sdf.format(cal.getTime());
    		tmpDate=date.replaceAll("[-: ]", "");
    		if(Integer.parseInt(tmpDate)>Integer.parseInt(end)) break; // 마지막 날짜 체크
    		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    		if(!isInclude && ((dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) || // 주말포함여부
    				(excludeList!=null && excludeList.contains(tmpDate)))) // 제외날짜 체크
        		continue;
    		list.add(new String[]{date,String.valueOf(dayOfWeek)});
    	}
    	
    	return list;
    }
   
   /** ERP 연계 일정 - 저장을 위한 VO정보 수집 */
   public void addErpSchdlVoList(List<WcErpSchdlBVo> updateList, String start, String end, String schdlTypCd, String compId, String userUid, String userNm){
	   // if(start.length()!=8) return;
	   if(!(start.length()==8 || start.length()==10)) return;
	   if(start.length()==8) start=getFmtDateString(start);
	   if(end==null || end.isEmpty()) end=start;
	   // 일정 저장
	   WcErpSchdlBVo wcErpSchdlBVo = new WcErpSchdlBVo();
	   wcErpSchdlBVo.setCompId(compId);
	   wcErpSchdlBVo.setUserUid(userUid);
	   wcErpSchdlBVo.setSchdlTypCd(schdlTypCd);
	   wcErpSchdlBVo.setSubj(userNm);
	   wcErpSchdlBVo.setStrtDt(start);
	   wcErpSchdlBVo.setEndDt(end);
	   wcErpSchdlBVo.setAlldayYn("Y");
	   updateList.add(wcErpSchdlBVo);
   }
   
   /** ERP 연계 일정 - 저장 */
   public void updateErpSchdl(QueryQueue queryQueue, WcErpSchdlBVo wcErpSchdlBVo, String cont, String regrUid) throws SQLException{
	   wcErpSchdlBVo.setCont(cont);
	   wcErpSchdlBVo.setRegrUid(regrUid);
	   wcErpSchdlBVo.setRegDt("sysdate");
	   wcErpSchdlBVo.setSchdlId(wcCmSvc.createId("WC_ERP_SCHDL_B"));
	   queryQueue.insert(wcErpSchdlBVo);
   }
   
   /** ERP 연계 일정 - 목록 저장 */
   public void updateErpSchdlVoList(XMLElement element, List<WcErpSchdlBVo> updateList, 
		   String typId, String compId, String regrUid, String erpStart, String erpEnd) throws SQLException{
	   QueryQueue queryQueue = new QueryQueue();
	   for(WcErpSchdlBVo storedWcErpSchdlBVo : updateList){
		   updateErpSchdl(queryQueue, storedWcErpSchdlBVo, getErpSchdlCont(typId, element, erpStart, erpEnd), regrUid);
	   }
	   if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
	  
   }
   
   /** ERP 연계 일정 - 문서별 일정 내용 생성 */
   public String getErpSchdlCont(String typId, XMLElement element, String strtDt, String endDt){
	   StringBuilder sb = new StringBuilder();
	   sb.append("기간").append(" : ").append(strtDt).append(" ~ ").append(endDt);
	   
	   if("bizTrip".equals(typId)){
	   }else if("leave".equals(typId)){
	   }else if("training".equals(typId)){
		   String strtSi = element.getAttr("body/detail.erpStartSi");
		   String endSi = element.getAttr("body/detail.erpEndSi");
		   if(strtSi==null || endSi==null) return sb.toString();
		   sb.append("\n").append("시간").append(" : ").append(strtSi).append("시 부터 ").append(endSi).append("시 까지");
		   sb.append("(").append(element.getAttr("body/detail.erpTotalDay")).append("일 ").append("/ 총 ").append(element.getAttr("body/detail.erpTotalTime")).append("시간)");
	   }
	   
	   return sb.toString();
   }
   
   /** 날짜 포맷 변경 */
   public String getFmtDateString(String date){
	   return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
   }
   
   /** 메일일정 관계 저장*/
   public void setMailSchdlVoList(QueryQueue queryQueue, String mailId, String schdlId, String copySchdlId, String userUid, String regrUid, String acptYn) throws SQLException{
	   WcMailSchdlRVo wcMailSchdlRVo = new WcMailSchdlRVo();
	   wcMailSchdlRVo.setSchdlId(schdlId);
	   wcMailSchdlRVo.setUserUid(userUid);
	   
	   boolean isEmptyQuery=queryQueue==null;
	   if(acptYn!=null && !acptYn.isEmpty()){
		   wcMailSchdlRVo.setAcptYn(acptYn);
		   wcMailSchdlRVo.setCmplDt("sysdate");
		   if(copySchdlId!=null) wcMailSchdlRVo.setCopySchdlId(copySchdlId);
		   if(isEmptyQuery) commonDao.update(wcMailSchdlRVo);
		   else queryQueue.update(wcMailSchdlRVo);
	   }else{
		   // 수정여부
		   boolean isUpdate=commonDao.count(wcMailSchdlRVo)>0;
		   wcMailSchdlRVo.setRegDt("sysdate");
		   wcMailSchdlRVo.setAcptYn("N");
		   wcMailSchdlRVo.setCmplDt("");
		   if(regrUid!=null) wcMailSchdlRVo.setRegrUid(regrUid);
		   if(mailId!=null) wcMailSchdlRVo.setMailId(mailId);
		   if(isEmptyQuery){
			   if(isUpdate) commonDao.update(wcMailSchdlRVo);
			   else commonDao.insert(wcMailSchdlRVo);
		   }else{
			   if(isUpdate) queryQueue.update(wcMailSchdlRVo);
			   else queryQueue.insert(wcMailSchdlRVo);
		   }
	   }
   }
   
   /** 일정 보내기 - 저장 */
   @SuppressWarnings("unchecked")
   public String copySchdl(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String originId, 
		   List<DmCommFileDVo> copyFileList) throws SQLException, CmException, IOException{
	    // 전체일정 조회
	    WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
	    wcSchdlBVo.setCompId(userVo.getCompId());
	    wcSchdlBVo.setSchdlPid(originId);
	    List<WcSchdlBVo> list = (List<WcSchdlBVo>)commonDao.queryList(wcSchdlBVo);
	    if(list==null || list.size()==0){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
	    }
	    
	    //참석자 목록 조회
	    WcPromGuestDVo guestVo = null;
	    List<WcPromGuestDVo> guestVoList = null;
	    // 신규일정VO
	    WcSchdlBVo newVo = null;
	    // 파일목록
	    List<CommonFileVo> fileList = null;
	    String schdlId=null, newSchdlPid=null; // 일정ID, 일정부모ID
	    // 일정 복사
	    for(WcSchdlBVo originVo : list){
	    	newVo=new WcSchdlBVo();
	    	BeanUtils.copyProperties(originVo, newVo, new String[]{"schdlId", "schdlPid", "compId", "userUid", "deptId", "regrUid", "regrNm"});
	    	schdlId=wcCmSvc.createId("WC_SCHDL_B");
	    	if(newSchdlPid==null) newSchdlPid=schdlId;
	    	newVo.setSchdlId(schdlId);
		    newVo.setSchdlPid(newSchdlPid);
		    newVo.setCompId(userVo.getCompId());
		    newVo.setUserUid(userVo.getUserUid());
		    newVo.setDeptId(userVo.getDeptId());
		    newVo.setRegrUid(userVo.getUserUid());
		    newVo.setRegrNm(userVo.getUserNm());
		    newVo.setOpenGradCd("3"); // 비공개로 설정
		    newVo.setSchdlKndCd("1"); // 개인일정
		    queryQueue.insert(newVo);
		    
		    // 참석자 조회
		    guestVo = new WcPromGuestDVo();
		    guestVo.setSchdlId(originVo.getSchdlId());
		    guestVoList = (List<WcPromGuestDVo>)commonDao.queryList(guestVo);
		    
		    // 참석자 복사
		    if(guestVoList!=null && guestVoList.size()>0){
		    	for(WcPromGuestDVo storedGuestDVo : guestVoList){
		    		guestVo = new WcPromGuestDVo();
			    	BeanUtils.copyProperties(storedGuestDVo, guestVo, new String[]{"schdlId"});
			    	guestVo.setSchdlId(newVo.getSchdlId());
			    	queryQueue.insert(guestVo);
		    	}
		    }
		    
		    // 반복일정에 첨부파일이 있으면 해당 파일VO 를 복사한다. 
		    if(originVo.getAttYn()!=null && "Y".equals(originVo.getAttYn())){
		    	// 파일 목록 조회
		    	fileList = wcFileSvc.getFileVoList(originVo.getSchdlId());
		    	if(fileList!=null && fileList.size()>0){
		    		// 파일복사(DB)
			    	wcFileSvc.copyFile(request, newVo.getSchdlId(), queryQueue, fileList, copyFileList, userVo.getUserUid());
		    	}
		    }
	    }
	    
	    // 반복설정 저장
	    WcRepetSetupDVo wcRepetVo = new WcRepetSetupDVo();
	    wcRepetVo.setSchdlId(originId);
	    wcRepetVo=(WcRepetSetupDVo) commonDao.queryVo(wcRepetVo);
	    if(newSchdlPid!=null && wcRepetVo!=null){
	    	wcRepetVo.setSchdlId(newSchdlPid);
		    // 반복설정 저장
		    queryQueue.insert(wcRepetVo);
	    }
	    
	    return newSchdlPid;
   }
   
   /** 일정 보내기 - 저장*/
   public void copySchdlTmp(HttpServletRequest request, QueryQueue queryQueue, UserVo userVo, String originId, 
		   List<DmCommFileDVo> copyFileList) throws SQLException, CmException, IOException{
	   
	    // 일정조회
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		wcSchdlBVo.setSchdlId(originId);
		wcSchdlBVo.setCompId(userVo.getCompId());
		wcSchdlBVo = (WcSchdlBVo)commonSvc.queryVo(wcSchdlBVo);
		if(wcSchdlBVo==null){
			// cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);	
		}
		// 원본일정 등록자UID - 참석자에 원본일정 등록자도 포함한다
    	//String originRegrUid = wcSchdlBVo.getRegrUid();
    	
	    // 일정ID 생성
		String schdlId=wcCmSvc.createId("WC_SCHDL_B");
		// 일정 저장
	    wcSchdlBVo.setSchdlId(schdlId);
	    wcSchdlBVo.setSchdlPid(wcSchdlBVo.getSchdlId());
	    wcSchdlBVo.setCompId(userVo.getCompId());
	    wcSchdlBVo.setUserUid(userVo.getUserUid());
	    wcSchdlBVo.setDeptId(userVo.getDeptId());
	    wcSchdlBVo.setRegrUid(userVo.getUserUid());
	    wcSchdlBVo.setRegrNm(userVo.getUserNm());
	    wcSchdlBVo.setOpenGradCd("3"); // 비공개로 설정
	    wcSchdlBVo.setSchdlKndCd("1"); // 개인일정
	    queryQueue.insert(wcSchdlBVo);
	    
	    //참석자 목록 조회
	    WcPromGuestDVo guestVo = new WcPromGuestDVo();
	    guestVo.setSchdlId(originId);
	    @SuppressWarnings("unchecked")
    	List<WcPromGuestDVo> guestVoList = (List<WcPromGuestDVo>)commonDao.queryList(guestVo);
	    if(guestVoList!=null && guestVoList.size()>0){
	    	for(WcPromGuestDVo storedGuestDVo : guestVoList){
	    		// 본인이면 저장항목에서 제외 - 여기 체크
	    		/*if(storedGuestDVo.getGuestEmplYn() !=null && "Y".equals(storedGuestDVo.getGuestEmplYn()) && 
	    				storedGuestDVo.getGuestUid().equals(userVo.getUserUid())) continue;*/
	    		guestVo = new WcPromGuestDVo();
		    	BeanUtils.copyProperties(storedGuestDVo, guestVo, new String[]{"schdlId"});
		    	guestVo.setSchdlId(schdlId);
		    	queryQueue.insert(guestVo);
	    	}
	    	
	    	// 등록자 상세정보 조회
	    	/*Map<String, Object> userMap = orCmSvc.getUserMap(originRegrUid, "ko");
	    	if(userMap!=null){
	    		guestVo = new WcPromGuestDVo();
		    	guestVo.setSchdlId(schdlId);
		    	guestVo.setGuestUid((String)userMap.get("userUid"));
		    	guestVo.setGuestNm((String)userMap.get("rescNm"));
		    	guestVo.setGuestPositNm((String)userMap.get("positNm"));
		    	guestVo.setGuestDeptNm((String)userMap.get("deptRescNm"));
		    	guestVo.setGuestCompNm((String)userMap.get("orgRescNm"));
		    	guestVo.setStatCd("T");
		    	guestVo.setGuestEmplYn("Y");
		    	guestVo.setEmail((String)userMap.get("email"));
		    	queryQueue.insert(guestVo);
	    	}*/
	    }
	    
	    List<CommonFileVo> fileList = null;
		// 원본일정에 첨부파일이 있으면 해당 파일VO 를 복사한다. 
	    if(wcSchdlBVo.getAttYn()!=null && "Y".equals(wcSchdlBVo.getAttYn())){
	    	// 파일 목록 조회
	    	fileList = wcFileSvc.getFileVoList(originId);
	    	if(fileList!=null && fileList.size()>0){
	    		// 파일복사(DB)
		    	wcFileSvc.copyFile(request, schdlId, queryQueue, fileList, copyFileList, userVo.getUserUid());
	    	}
	    }
	    
	    // 반복일정 조회
	    wcSchdlBVo = new WcSchdlBVo();
	    wcSchdlBVo.setCompId(userVo.getCompId());
	    wcSchdlBVo.setSchdlPid(originId);
	    //wcSchdlBVo.setRepetYn("Y");
	    
   	    // 원본일정이 반복일정이면 동일하게 저장한다. 
	    if(commonDao.count(wcSchdlBVo)>0){
		    WcRepetSetupDVo wcRepetVo = new WcRepetSetupDVo();
		    wcRepetVo.setSchdlId(originId);
		    wcRepetVo=(WcRepetSetupDVo) commonDao.queryVo(wcRepetVo);
		    if(wcRepetVo==null) return;
		    wcRepetVo.setSchdlId(schdlId);
		    // 반복설정 저장
		    queryQueue.insert(wcRepetVo);
		    
		    @SuppressWarnings("unchecked")
		    List<WcSchdlBVo> list = (List<WcSchdlBVo>)commonDao.queryList(wcSchdlBVo);
		    WcSchdlBVo newSchdlVo = null;
		    // 반복일정 복사
		    for(WcSchdlBVo storedWcSchdlBVo : list){
		    	if(storedWcSchdlBVo.getSchdlId().equals(storedWcSchdlBVo.getSchdlPid())) continue;
		    	newSchdlVo=new WcSchdlBVo();
		    	BeanUtils.copyProperties(storedWcSchdlBVo, newSchdlVo, new String[]{"schdlId", "schdlPid", "compId", "userUid", "deptId", "regrUid", "regrNm"});
		    	newSchdlVo.setSchdlId(wcCmSvc.createId("WC_SCHDL_B"));
			    newSchdlVo.setSchdlPid(schdlId);
			    newSchdlVo.setCompId(userVo.getCompId());
			    newSchdlVo.setUserUid(userVo.getUserUid());
			    newSchdlVo.setDeptId(userVo.getDeptId());
			    newSchdlVo.setRegrUid(userVo.getUserUid());
			    newSchdlVo.setRegrNm(userVo.getUserNm());
			    newSchdlVo.setOpenGradCd("3"); // 비공개로 설정
			    newSchdlVo.setSchdlKndCd("1"); // 개인일정
			    queryQueue.insert(newSchdlVo);
			    
			    // 참석자 복사
			    if(guestVoList!=null && guestVoList.size()>0){
			    	for(WcPromGuestDVo storedGuestDVo : guestVoList){
			    		guestVo = new WcPromGuestDVo();
				    	BeanUtils.copyProperties(storedGuestDVo, guestVo, new String[]{"schdlId"});
				    	guestVo.setSchdlId(newSchdlVo.getSchdlId());
				    	queryQueue.insert(guestVo);
			    	}
			    }
			    
			    // 반복일정에 첨부파일이 있으면 해당 파일VO 를 복사한다. 
			    if(storedWcSchdlBVo.getAttYn()!=null && "Y".equals(storedWcSchdlBVo.getAttYn())){
			    	// 파일 목록 조회
			    	fileList = wcFileSvc.getFileVoList(storedWcSchdlBVo.getSchdlId());
			    	if(fileList!=null && fileList.size()>0){
			    		// 파일복사(DB)
				    	wcFileSvc.copyFile(request, newSchdlVo.getSchdlId(), queryQueue, fileList, copyFileList, userVo.getUserUid());
			    	}
			    }
			    
		    }
		}
	   
   }
   
   
   /** 부서일정관계 저장 - 부서일정 선택부서 목록 */
   @SuppressWarnings("unchecked")
   public List<WcSchdlDeptRVo> saveWcSchdlDeptRVo(HttpServletRequest request, QueryQueue queryQueue, 
		   List<WcSchdlDeptRVo> wcSchdlDeptRVoList, String schdlId, boolean isUpdate){
	   if(isUpdate) // 수정이면 기존 목록 삭제
		   deleteWcSchdlDeptRVo(queryQueue, schdlId);
	   
	   // 부서일정관계(WC_SCHDL_DEPT_R) 테이블
	   if(wcSchdlDeptRVoList==null)
		   wcSchdlDeptRVoList = (List<WcSchdlDeptRVo>)VoUtil.bindList(request, WcSchdlDeptRVo.class, new String[]{"orgId"});
	   if(wcSchdlDeptRVoList!=null && wcSchdlDeptRVoList.size()>0){
		   for(WcSchdlDeptRVo storedWcSchdlDeptRVo : wcSchdlDeptRVoList){
			   storedWcSchdlDeptRVo.setSchdlId(schdlId);
			   queryQueue.insert(storedWcSchdlDeptRVo);
		   }
	   }
	   return wcSchdlDeptRVoList;
   }
   
   /** 부서일정관계 삭제 - 부서일정 선택부서 목록 */
   public void deleteWcSchdlDeptRVo(QueryQueue queryQueue, String schdlId){
	   WcSchdlDeptRVo wcSchdlDeptRVo = new WcSchdlDeptRVo();
	   wcSchdlDeptRVo.setSchdlId(schdlId);
	   queryQueue.delete(wcSchdlDeptRVo);
   }
   
   /** 부서일정 - 선택부서 목록 세팅 */
   public void setDeptList(ModelMap model, String schdlId) throws SQLException{
	   WcSchdlDeptRVo wcSchdlDeptRVo = new WcSchdlDeptRVo();
	   wcSchdlDeptRVo.setSchdlId(schdlId);
	   @SuppressWarnings("unchecked")
	   List<WcSchdlDeptRVo> wcSchdlDeptRVoList = (List<WcSchdlDeptRVo>)commonSvc.queryList(wcSchdlDeptRVo);
	   model.put("wcSchdlDeptRVoList", wcSchdlDeptRVoList);
   }
   
   /** 일정 삭제 */
   public void deleteSchdl(QueryQueue queryQueue, String schdlPid, List<CommonFileVo> deletedFileList) throws SQLException{
		if(schdlPid==null || schdlPid.isEmpty()) return;
		
		WcSchdlBVo wcSchdlBVo = new WcSchdlBVo();
		wcSchdlBVo.setSchdlPid(schdlPid);
		@SuppressWarnings("unchecked")
		List<WcSchdlBVo> wcSchdlBVoList = (List<WcSchdlBVo>) commonSvc.queryList(wcSchdlBVo);
		
		String storedSchdlId=null;
		List<CommonFileVo> fileList=null;
		for(WcSchdlBVo wcsVo : wcSchdlBVoList){
			storedSchdlId=wcsVo.getSchdlId();
			
			// 참석자
			WcPromGuestDVo promGuestVo = new WcPromGuestDVo();
			promGuestVo.setSchdlId(storedSchdlId);
			queryQueue.delete(promGuestVo);
			
			/** 부서일정 - 선택부서 목록 삭제 */
			if(wcsVo.getOpenGradCd()!=null && "4".equals(wcsVo.getOpenGradCd()))
				deleteWcSchdlDeptRVo(queryQueue, storedSchdlId);
			
			// 일정
			WcSchdlBVo wcsOneVo = new WcSchdlBVo();
			wcsOneVo.setSchdlId(storedSchdlId);
			queryQueue.delete(wcsOneVo);
			
			// 파일 삭제
			fileList=wcFileSvc.deleteWcFile(storedSchdlId, queryQueue);
			if(fileList!=null && fileList.size()>0){
				deletedFileList.addAll(fileList);
			}
		}
		
		//반복설정 삭제
		WcRepetSetupDVo repetVo = new WcRepetSetupDVo();
		repetVo.setSchdlId(schdlPid);
		queryQueue.delete(repetVo);
   }
   
   /** 참석자 목록 조회 */
	public List<WcPromGuestDVo> getSchdlGuestList(String schdlId) throws SQLException {
	    WcPromGuestDVo wcPromGuestDVo = new WcPromGuestDVo();
		wcPromGuestDVo.setSchdlId(schdlId);
		@SuppressWarnings("unchecked")
		List<WcPromGuestDVo> wcPromGuestDVoList = (List<WcPromGuestDVo>)commonDao.queryList(wcPromGuestDVo);
		if(wcPromGuestDVoList!=null && wcPromGuestDVoList.size()>0){
			for(WcPromGuestDVo storedWcPromGuestDVo : wcPromGuestDVoList){
				storedWcPromGuestDVo.setEmailYn(storedWcPromGuestDVo.getEmailSendYn());
			}
		}
		return wcPromGuestDVoList;
	}
   
   
}


