package com.innobiz.orange.web.ct.svc;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.exception.SecuException;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtBlonCatDVo;
import com.innobiz.orange.web.ct.vo.CtBullMastBVo;
import com.innobiz.orange.web.ct.vo.CtCatBVo;
import com.innobiz.orange.web.ct.vo.CtDebrBVo;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtFncMbshAuthDVo;
import com.innobiz.orange.web.ct.vo.CtMbshDVo;
import com.innobiz.orange.web.ct.vo.CtSchdlBVo;
import com.innobiz.orange.web.ct.vo.CtScrnSetupDVo;
import com.innobiz.orange.web.ct.vo.CtSiteBVo;
import com.innobiz.orange.web.ct.vo.CtSurvBVo;
import com.innobiz.orange.web.ct.vo.CtVistrHstDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
@Service
public class CtCmntSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 커뮤니티 게시판 서비스 */
	@Autowired
	private CtBullMastSvc ctBullMastSvc;
	
	/** 점수이력 서비스 */
	@Autowired
	private CtScreHstSvc ctScreHstSvc;
	
	/** 추천이력 서비스 */
	@Autowired
	private CtRecmdHstSvc ctRecmdHstSvc;
	
	/** 게시물 첨부파일  */
	@Autowired
	private CtFileSvc ctFileSvc;
	
	/** 커뮤니티 토론실 서비스 */
	@Autowired
	private CtDebrSvc ctDebrSvc;
	
	/** 커뮤니티 쿨사이트 서비스 */
	@Autowired
	private CtSiteSvc ctSiteSvc;
	
	/** 커뮤니티 설문 서비스 */
	@Autowired
	private CtSurvSvc ctSurvSvc;
	
	/** 커뮤니티 일정 서비스 */
	@Autowired
	private CtScdManagerSvc ctScdManagerSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 마스터 수정 */
	public void updateCtMast(HttpServletRequest request, CtMbshDVo ctMbshDVo, QueryQueue queryQueue) throws Exception{
		//이전마스터 UID 
		String prevMastUid = request.getParameter("prevMastUid");
		ctMbshDVo.setUserUid(prevMastUid);
		ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
		
		//수정된 이전마스터 사용자 보안 등급 코드
		String userSecuCd = request.getParameter("userSecuCd");
		ctMbshDVo.setUserSeculCd(userSecuCd);
		//이전마스터 UPDATE
		queryQueue.update(ctMbshDVo);
		
		//새로운 마스터 UID
		String newMastUid = request.getParameter("userUid");
		
		//커뮤니티 회원인지 확인
		CtMbshDVo mbshDVo = new CtMbshDVo();
		mbshDVo.setCtId(ctMbshDVo.getCtId());
		mbshDVo.setUserUid(newMastUid);
		mbshDVo = (CtMbshDVo) commonDao.queryVo(mbshDVo);
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctMbshDVo.getCtId());
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		//mbshDVo == null 이면 신규(INSERT), != null 이면 기존 가입자(UPDATE)
		if(mbshDVo == null){
			CtMbshDVo mastMbshVo = new CtMbshDVo();
			mastMbshVo.setCompId(ctEstbBVo.getCompId());
			mastMbshVo.setCtId(ctMbshDVo.getCtId());
			mastMbshVo.setUserUid(newMastUid);
			mastMbshVo.setUserSeculCd("M");
			mastMbshVo.setJoinDt(currentDay());
			mastMbshVo.setJoinStat("3");
			queryQueue.insert(mastMbshVo);
		}else{
			mbshDVo.setUserSeculCd("M");
			mbshDVo.setJoinStat("3");
			queryQueue.update(mbshDVo);
		}
		
		//커뮤니티 기본 테이블(CT_ESTB_B)에 마스터 변경
		//CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(ctMbshDVo.getCompId());
		//ctEstbBVo.setCtId(ctMbshDVo.getCtId());
		//ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		ctEstbBVo.setMastUid(newMastUid);
		queryQueue.update(ctEstbBVo);
		
	}
	
	/** 오늘 날짜 */
	public String currentDay(){
		return StringUtil.getCurrDateTime();
	}
	
	/** 오늘 날짜 */
	public String currentYmdDay(){
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date currentTime = new Date ( );
		return mSimpleDateFormat.format ( currentTime );
	}
	
	/** 원하는 일- Day */
	public String currentYmdSet(int day){
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
//		Date currentTime = new Date ( );
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, day);
	
		return  mSimpleDateFormat.format(cal.getTime());
	}
	
	/** 커뮤니티 목록 개수 */
	public Integer getCtCmntListCnt(CtEstbBVo ctEstbBVo) throws Exception{
		return commonDao.count(ctEstbBVo);
	}
	
	/** 커뮤니티 목록 */
	@SuppressWarnings("unchecked")
	public List<CtEstbBVo> getCtCmntList(CtEstbBVo ctEstbBVo) throws Exception{
		return (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
	}
	
	/** 커뮤니티 회원 목록 */
	@SuppressWarnings("unchecked")
	public List<CtMbshDVo> getCtMbshList(CtMbshDVo ctMbshDVo) throws Exception{
		return (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
	}
	
	/** 개설승인 목록페이지 */
	public Map<String, Object> getEstbResqList(HttpServletRequest request, CtEstbBVo ctEstbBVo)throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctEstbBVo.getSchWord();//검색어
		Integer recodeCount = this.getCtCmntListCnt(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		//목록 조회
		List<CtEstbBVo> reqsCtVoList = this.getCtCmntList(ctEstbBVo);
		
		rsltMap.put("reqsCtMapList", reqsCtVoList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	/** 관리대상 커뮤니티 신청목록 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMngTgtApvdList(HttpServletRequest request, CtEstbBVo ctEstbBVo)throws Exception{
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request); 
		//String schWord = ctEstbBVo.getSchWord();//검색어
		
		Integer recodeCount = commonDao.count(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		List<CtEstbBVo> ctMngTgtVoList = new ArrayList<CtEstbBVo>();
		ctMngTgtVoList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		rsltMap.put("ctMngTgtMapList", ctMngTgtVoList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	/** 관리대상 커뮤니티 가입 */
	public CtMbshDVo setCtMbshJoin(HttpServletRequest request, CtMbshDVo ctMbshDVo)throws Exception{
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 해당 커뮤니티 정보 가져오기
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		//ctEstbBVo.setCompId(ctMbshDVo.getCompId());
		ctEstbBVo.setCtId(ctMbshDVo.getCtId());
		ctEstbBVo = (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo.getJoinMet().equals("1")){
			ctMbshDVo.setJoinStat("3");
		}else{
			ctMbshDVo.setJoinStat("1");
		}
		ctMbshDVo.setUserSeculCd(ctEstbBVo.getDftAuth());
		ctMbshDVo.setJoinDt(this.currentDay());
		
		queryQueue.insert(ctMbshDVo);
		commonDao.execute(queryQueue);
		
		return null;
		
	}
	
	/** 관리대상 목록 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCmMngTgtList(HttpServletRequest request, CtEstbBVo ctEstbBVo) throws Exception{
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctEstbBVo.getSchWord();//검색어
		Integer recodeCount;
		Integer ctEstbCount = commonDao.count(ctEstbBVo);
		if(ctEstbBVo.getRanking() != null){
			
			if(ctEstbBVo.getRanking() < ctEstbCount){
				recodeCount = ctEstbBVo.getRanking();
			}else{
				recodeCount = ctEstbCount;
			}
			
		}else{
			recodeCount = ctEstbCount;
		}
		
		 
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		List<CtEstbBVo> ctMngTgtVoList = new ArrayList<CtEstbBVo>();
		ctMngTgtVoList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		
		Map<String, Object> ctMngTgtMap;
		List<Map<String, Object>> ctMngTgtMapList = new ArrayList<Map<String,Object>>();
		for(CtEstbBVo storeCtMngTgt : ctMngTgtVoList){
			
			CtMbshDVo ctMbshDVo = new CtMbshDVo();
			ctMbshDVo.setCompId(storeCtMngTgt.getCompId());
			ctMbshDVo.setCtId(storeCtMngTgt.getCtId());
			ctMbshDVo.setUserUid(userVo.getUserUid());
			ctMbshDVo = (CtMbshDVo) commonDao.queryVo(ctMbshDVo);
			if(ctMbshDVo != null){
				storeCtMngTgt.setLogUserJoinStat(ctMbshDVo.getJoinStat());
			}
			ctMngTgtMap = VoUtil.toMap(storeCtMngTgt, null);
			ctMngTgtMapList.add(ctMngTgtMap);
			
		}
		
		rsltMap.put("recodeCount", recodeCount);
		rsltMap.put("ctMngTgtMapList", ctMngTgtMapList);
		return rsltMap;
		
		
	}
	
	/** 해당 카테고리에 포함된 커뮤니티 목록 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCatCmntList(HttpServletRequest request, CtCatBVo ctCatBVo) throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		String langTypCd = LoginSession.getLangTypCd(request);
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// [CtBlonCatDVo] 커뮤니티 개설 기본 & 커뮤니티 카테고리 기본 Join Table 
		CtBlonCatDVo ctBlonCatDVo = new CtBlonCatDVo();
		ctBlonCatDVo.setCatId(request.getParameter("catId"));
		// 해당 카테고리에 속한 커뮤니티 리스트
		//List<CtBlonCatDVo> ctBlonCatList = (List<CtBlonCatDVo>) commonDao.queryList(ctBlonCatDVo);
		
		CtCatBVo closedFld = new CtCatBVo();
		closedFld.setExtnOpenYn("N");
		closedFld.setCompId(userVo.getCompId());
		// 비공개 폴더 목록
		List<CtCatBVo> closedFldList = (List<CtCatBVo>) commonDao.queryList(closedFld);
		// 비공개 폴더 카테고리 ID 목록
		List<String> closedFldIdList = new ArrayList<String>();
		for(CtCatBVo closedFldVo : closedFldList){
			closedFldIdList.add(closedFldVo.getCatId());
		}
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		VoUtil.bind(request, ctEstbBVo);
		ctEstbBVo.setLangTyp(langTypCd);
		ctEstbBVo.setCtActStat("A");
		ctEstbBVo.setLogUserUid(userVo.getUserUid());
		ctEstbBVo.setCompId(userVo.getCompId());
		if(closedFldIdList.size() != 0){
			ctEstbBVo.setClosedFldList(closedFldIdList);
		}
		ctEstbBVo.setInstanceQueryId("countCtAllEstbB");
			
		Integer recodeCount = commonDao.count(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		ctEstbBVo.setInstanceQueryId("selectCtAllEstbB");
		List<CtEstbBVo> ctCatCtList = (List<CtEstbBVo>) commonDao.queryList(ctEstbBVo);
		
		rsltMap.put("catCmntList", ctCatCtList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	
	/** 나의 커뮤니티 목록 조회 */
	public Map<String, Object> getMyCtMapList(HttpServletRequest request, CtEstbBVo ctEstbBVo) throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctEstbBVo.getSchWord();//검색어
		
		Integer recodeCount = this.getCtCmntListCnt(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		//목록 조회
		List<CtEstbBVo> myCtVoList = this.getCtCmntList(ctEstbBVo);
		
		Map<String, Object> ctMyBInfoMap;
		List<Map<String, Object>> ctMyMapList = new ArrayList<Map<String, Object>>();
		for(CtEstbBVo storedCtBcBVo : myCtVoList){
			ctMyBInfoMap = VoUtil.toMap(storedCtBcBVo, null);
			ctMyMapList.add(ctMyBInfoMap);
		}
		
		rsltMap.put("ctFunId", request.getParameter("ctFuncId"));
		rsltMap.put("myCtMapList", ctMyMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	/** 신청중인 커뮤니티 목록 조회 */
	public Map<String, Object> getReqsCtMapList(HttpServletRequest request, CtEstbBVo ctEstbBVo) throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctEstbBVo.getSchWord();//검색어
		Integer recodeCount = this.getCtCmntListCnt(ctEstbBVo);
		PersonalUtil.setPaging(request, ctEstbBVo, recodeCount);
		
		//목록 조회
		List<CtEstbBVo> reqsCtVoList = this.getCtCmntList(ctEstbBVo);
		
		Map<String, Object> reqsCtMap;
		List<Map<String, Object>> reqsCtMapList = new ArrayList<Map<String,Object>>();
		for(CtEstbBVo storeCtEstbBVo : reqsCtVoList){
			reqsCtMap = VoUtil.toMap(storeCtEstbBVo, null);
			reqsCtMapList.add(reqsCtMap);
		}
		rsltMap.put("reqsCtMapList", reqsCtMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	/** 신청중인 커뮤니티 삭제 */
	public void transReqsCtDel(HttpServletRequest request, CtEstbBVo ctEstbBVo, QueryQueue queryQueue) throws SQLException{
		String[] delReqsCtList = request.getParameterValues("delReqsCtId");
		for(String delResqCtId: delReqsCtList){
			CtEstbBVo delResqCtVo = new CtEstbBVo();
			delResqCtVo.setCtId(delResqCtId);
			queryQueue.delete(delResqCtVo);
		}
		commonDao.execute(queryQueue);
	}
	
	/** 전체 커뮤니티 목록 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAllCtCatList(HttpServletRequest request, CtCatBVo ctCatBVo) throws Exception{
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		ctCatBVo.setFldTypCd("F");
		ctCatBVo.setLangTyp(langTypCd);
		ctCatBVo.setCompId(userVo.getCompId());
		List<CtCatBVo> ctCatFldList = new ArrayList<CtCatBVo>();
		//카테고리 폴더 리스트
		ctCatFldList = (List<CtCatBVo>) commonDao.queryList(ctCatBVo);
		
		ctCatBVo.setFldTypCd("C");
		List<CtCatBVo> ctCatClsList = new ArrayList<CtCatBVo>();
		//카테고리 분류 리스트
		ctCatClsList = (List<CtCatBVo>) commonDao.queryList(ctCatBVo);
		
		// 커뮤니티 개수를 포함하고 있는 카테고리 목록 
		List<CtCatBVo> catClsList = new ArrayList<CtCatBVo>();
		
		for(CtCatBVo catBVo : ctCatClsList){
			CtCatBVo catClsBVo = new CtCatBVo();
			catClsBVo.setCatId(catBVo.getCatId());
			catClsBVo.setLangTyp(langTypCd);
			catClsBVo.setFldTypCd(catBVo.getFldTypCd());
			catClsBVo = (CtCatBVo) commonDao.queryVo(catClsBVo);
			catClsList.add(catClsBVo);
		}
		
		//카테고리 개수만큼 div만들기 위한 계산
		int catLength = ctCatFldList.size() / 2;
		int val =  ctCatFldList.size() % 2;
		if(val == 1){
			catLength = catLength++;
		}
		
		rsltMap.put("catLength", catLength);
		rsltMap.put("ctCatFldList", ctCatFldList);
		rsltMap.put("catClsList", catClsList);
		 
		return rsltMap;
	}

	//왼쪽 메뉴 권한별 검색
	public void putLeftMenu(HttpServletRequest request,String ctId, 
			ModelMap model)throws SQLException {
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		String menuId = request.getParameter("menuId");
		
		//setCm(커뮤니티개설)이 커뮤니티 밖에서도 만들어 지기 때문에 menuId CT만 왼쪽 메뉴 LIST를 던져준다.
		if(menuId.substring(0,2).equalsIgnoreCase(PtConstant.MNU_GRP_REF_CT)){
		
			CtMbshDVo ctMbshD = new CtMbshDVo();
			//ctMbshD.setCompId(userVo.getCompId());
			ctMbshD.setCtId(ctId);
			ctMbshD.setUserUid(userVo.getUserUid());
			ctMbshD = (CtMbshDVo) commonDao.queryVo(ctMbshD);
			
			
			
			//================================================================================================
			//커뮤니티 leftCt 왼쪽 메뉴에 관한 List
			//================================================================================================
			CtFncDVo ctFncDVo = new CtFncDVo();
			//ctFncDVo.setCompId(userVo.getCompId());
			ctFncDVo.setCtId(ctId);
			if(ctMbshD != null){
//				if(userVo.isAdmin() && userVo.isSysAdmin()){
//					ctFncDVo.setSeculCd("M");
//				}else{
					ctFncDVo.setSeculCd(ctMbshD.getUserSeculCd());
//				}
				
				ctFncDVo.setAuthCd("R");
			}
			
			
			ctFncDVo.setLangTyp(langTypCd);
			ctFncDVo.setOrderBy("CT_FNC_ORDR, CT_FNC_LOC_STEP, CT_FNC_UID");
			@SuppressWarnings("unchecked")
			List<CtFncDVo> ctFncList = (List<CtFncDVo>) commonDao.queryList(ctFncDVo);
			
			model.put("ctFncList", ctFncList);
			model.put("ctFncChild", ctFncList);
			
			//================================================================================================
			//커뮤니티 leftCt 왼쪽 상단 커뮤니티 및 마스터 정보
			//================================================================================================
			putCtEstbInfo(request, model, ctId);
			//회원현황.
			putJoinPeopleStat(request, model, ctId);
			
			//================================================================================================
			//커뮤니티 파일 사용 합계
			//================================================================================================
			
			model.put("fileSum", getFileAttCalcSum(ctId));
			
		}
		
		model.put("ctId", ctId);
		
		//================================================================================================
		
	}
	
	//관리자 기능별권한관리 체크박스 관련 modelPut
	public void putCtAuth(HttpServletRequest request, ModelMap model,
			String fncUid) throws SQLException, CmException {
		
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtFncMbshAuthDVo ctFncMbshAuthDVo = new CtFncMbshAuthDVo();
		ctFncMbshAuthDVo.setCompId(ctEstbBVo.getCompId());
		ctFncMbshAuthDVo.setCtFncUid(fncUid);
		
		if(fncUid != null){
			CtFncMbshAuthDVo ctAuthS = new CtFncMbshAuthDVo();
			CtFncMbshAuthDVo ctAuthR = new CtFncMbshAuthDVo();
			CtFncMbshAuthDVo ctAuthA = new CtFncMbshAuthDVo();
			
			@SuppressWarnings("unchecked")
			List<CtFncMbshAuthDVo> ctFncMbshAuthList = (List<CtFncMbshAuthDVo>) commonDao.queryList(ctFncMbshAuthDVo);
			
			for(CtFncMbshAuthDVo ctFncMbsh : ctFncMbshAuthList){
				if(ctFncMbsh.getSeculCd().equalsIgnoreCase("S")){
					String [] ctFncAuth = ctFncMbsh.getAuthCd().split("\\/", -1);
					ctAuthS.setAuthSR(ctFncAuth[0]);
					ctAuthS.setAuthSW(ctFncAuth[1]);
					ctAuthS.setAuthSD(ctFncAuth[2]);
					
				}else if(ctFncMbsh.getSeculCd().equalsIgnoreCase("R")){
					String [] ctFncAuth = ctFncMbsh.getAuthCd().split("\\/", -1);
					ctAuthR.setAuthRR(ctFncAuth[0]);
					ctAuthR.setAuthRW(ctFncAuth[1]);
					ctAuthR.setAuthRD(ctFncAuth[2]);
					
				}else if(ctFncMbsh.getSeculCd().equalsIgnoreCase("A")){
					String [] ctFncAuth = ctFncMbsh.getAuthCd().split("\\/", -1);
					ctAuthA.setAuthAR(ctFncAuth[0]);
					ctAuthA.setAuthAW(ctFncAuth[1]);
					ctAuthA.setAuthAD(ctFncAuth[2]);
				}
			}
			
			model.put("ctAuthS", ctAuthS);
			model.put("ctAuthR", ctAuthR);
			model.put("ctAuthA", ctAuthA);
		
			
			model.put("fncUid", fncUid);
		}
		
	}
	
	//권한 체크 auth "W": 쓰기 "D" : 삭제  jsp에서 사용할때 authChk + auth 
	//jsp 사용할때 authChk 넘긴 auth로 뒤에 이름 붙여준다. 저장과 삭제가 동시에 있을 경우를 대비하여,
	//ex) ${authChkW} 또는  ${authChkD} 
	public void putAuthChk(HttpServletRequest request, ModelMap model, String auth ,String ctId, String fncUid) throws SQLException{
		String authChk = null;
		
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
			
		String seculCd = getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), ctId);
		
		if(seculCd == null){
			authChk = "";
		}else{
			CtFncDVo ctFncDVo = new CtFncDVo();
			//ctFncDVo.setCompId(userVo.getCompId());
			ctFncDVo.setCtFncUid(fncUid);
			ctFncDVo.setSeculCd(seculCd);
			ctFncDVo.setAuthCd(auth);
			ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
			authChk = ctFncDVo != null?auth:"";
		}
		
		model.put("authChk" + auth, authChk);
	}
	
	//사용자 등급 코드 가져오기
	public String getUserSeculCd(String compId, String userUid, String ctId) throws SQLException{
		
		CtMbshDVo ctMbsh = new CtMbshDVo();
		//ctMbsh.setCompId(compId);
		ctMbsh.setUserUid(userUid);
		ctMbsh.setCtId(ctId);
		
		ctMbsh = (CtMbshDVo) commonDao.queryVo(ctMbsh);
		
		if(ctMbsh != null){
			return ctMbsh.getUserSeculCd();
		}else{
			return null;
		}
		
	}
	
	//커뮤니티 MODELPUT 개설기본 정보
	public void putCtEstbInfo(HttpServletRequest request, ModelMap model, String ctId) throws SQLException{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
			
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		//ctEstbBVo.setCompId(userVo.getCompId());
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		CtScrnSetupDVo ctScrnSetupDVo = new CtScrnSetupDVo();
		//ctScrnSetupDVo.setCompId(userVo.getCompId());
		ctScrnSetupDVo.setCtId(ctId);
		@SuppressWarnings("unchecked")
		List<CtScrnSetupDVo> ctScrnSetupList = (List<CtScrnSetupDVo>) commonDao.queryList(ctScrnSetupDVo);
		if(ctScrnSetupList.size()>0){
			ctEstbBVo.setCtItro(ctScrnSetupList.get(0).getCtItro());
		}
		model.put("ctEstbBVo", ctEstbBVo);
		
	}
	
	//커뮤니티 개설기본 정보 RETURN 커뮤니티정보
	public CtEstbBVo getCtEstbInfo(HttpServletRequest request, String ctId) throws SQLException{
		
		//세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
			
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		ctEstbBVo.setLangTyp(langTypCd);
		//ctEstbBVo.setCompId(userVo.getCompId());
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		
		return ctEstbBVo;
	}
	
	//회원 현황
	public void putJoinPeopleStat(HttpServletRequest request, ModelMap model, String ctId) throws SQLException{
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);

		//전체회원
		CtMbshDVo ctMbshAllDVo = new CtMbshDVo();
		ctMbshAllDVo.setCtId(ctId);
		//ctMbshAllDVo.setCompId(userVo.getCompId());
		ctMbshAllDVo.setJoinStat("3");
		ctMbshAllDVo.setInstanceQueryId("countCtMbshDAll");
		
		int allPeople = commonDao.count(ctMbshAllDVo);

		model.put("allPeople", allPeople);
		
		//오늘가입회원
		CtMbshDVo ctMbshTodayDVo = new CtMbshDVo();
		ctMbshTodayDVo.setCtId(ctId);
		//ctMbshTodayDVo.setCompId(userVo.getCompId());
		ctMbshTodayDVo.setJoinStat("3");
		ctMbshTodayDVo.setInstanceQueryId("countCtMbshDToday");
		
		int todayPeople = commonDao.count(ctMbshTodayDVo);
		
		model.put("todayPeople", todayPeople);
		
	}
	
	//파일첨부 MB 계산된 합계
	public String getFileAttCalcSum(String ctId) throws SQLException{
		
		CtFileDVo ctFileDVo = new CtFileDVo();
		ctFileDVo.setCtId(ctId);
		ctFileDVo.setInstanceQueryId("countCtFileDSum");
		
		int ctFileSum = commonDao.queryInt(ctFileDVo) != null ? commonDao.queryInt(ctFileDVo):0;
		String fileSum = "0.00";
		if(ctFileSum != 0){
			fileSum = String.format("%.2f", (float)getFileByteConvertMB(ctFileSum));
		}
		
		
		return fileSum;
	}
	
	//파일첨부 그냥 합계
	public int getFileAttSum(String ctId) throws SQLException{
		
		CtFileDVo ctFileDVo = new CtFileDVo();
		ctFileDVo.setCtId(ctId);
		ctFileDVo.setInstanceQueryId("countCtFileDSum");
		
		int ctFileSum = commonDao.queryInt(ctFileDVo)!=null?commonDao.queryInt(ctFileDVo):0;
		
		return ctFileSum;
	}
	
	//바이트를 메가바이트로변환
	public float getFileByteConvertMB(int attSize){
		float convertSize = ((float)attSize / 1024) / 1024;
		return convertSize;
	}
	
	
	//메가바이트를 바이트로변환
	public int getFileMBConvertByte(int attSize){
		int convertSize = (attSize * 1024) * 1024;
		return convertSize;
	}
	
	
	//커뮤니티 회원정보 수 가져오기
	public int getMbshCount(HttpServletRequest request, String ctId) throws SQLException{
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtMbshDVo ctMbshD = new CtMbshDVo();
		//ctMbshD.setCompId(userVo.getCompId());
		ctMbshD.setCtId(ctId);
		return commonDao.count(ctMbshD)!=null?commonDao.count(ctMbshD):0;
		
	}
	
	//커뮤니티 회원정보VO 가져오기
	
	public CtMbshDVo getMbshVo(HttpServletRequest request, String ctId) throws SQLException{
		//세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		CtMbshDVo ctMbshD = new CtMbshDVo();
		//ctMbshD.setCompId(userVo.getCompId());
		ctMbshD.setCtId(ctId);
		ctMbshD.setUserUid(userVo.getUserUid());
		
		return (CtMbshDVo) commonDao.queryVo(ctMbshD);
	}
	
	/** 관리대상 커뮤니티 신청목록 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMngJoinReqsList(HttpServletRequest request, CtMbshDVo ctMbshDVo)throws Exception{
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request); 
		//String schWord = ctMbshDVo.getSchWord();//검색어
		
		Integer recodeCount = commonDao.count(ctMbshDVo);
		PersonalUtil.setPaging(request, ctMbshDVo, recodeCount);
		
		List<CtMbshDVo> ctMngJoinReqsList = new ArrayList<CtMbshDVo>();
		ctMngJoinReqsList = (List<CtMbshDVo>) commonDao.queryList(ctMbshDVo);
		
		rsltMap.put("ctMngJoinReqsList", ctMngJoinReqsList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	//커뮤니티 별 사용자 접속 기록
	public void setVistHst(UserVo userVo, String ctId, String gubunCd, QueryQueue queryQueue) throws SQLException{
		
		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null) return;
		
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setCtId(ctId);
		ctVistrHstDVo.setUserUid(userVo.getUserUid());
		ctVistrHstDVo.setCompId(ctEstbBVo.getCompId());
		ctVistrHstDVo.setAccsTypCd(gubunCd);
		ctVistrHstDVo.setAccsDt(currentDay());
		
		if(queryQueue == null){
			commonDao.insert(ctVistrHstDVo);
		}
		
	}
	
	/** 관리대상 커뮤니티 신청목록 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMngVistrStatList(HttpServletRequest request, CtVistrHstDVo ctVistrHstDVo, String ctId)throws Exception{
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		
		request.setCharacterEncoding("utf-8");
		//String langTypCd = LoginSession.getLangTypCd(request); 
		//String schWord = ctVistrHstDVo.getSchWord();//검색어
		
		//세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtVistrHstDVo ctVistrHstCount = new CtVistrHstDVo();
		ctVistrHstCount.setCtId(ctId);
		ctVistrHstCount.setCompId(ctVistrHstDVo.getCompId());
		ctVistrHstCount.setInstanceQueryId("countCtVistrHstCountSumD");
		
		
		Integer recodeCount = commonDao.count(ctVistrHstCount);
		PersonalUtil.setPaging(request, ctVistrHstDVo, recodeCount);
		
		List<CtVistrHstDVo> ctMngVistHstList = new ArrayList<CtVistrHstDVo>();
		ctMngVistHstList = (List<CtVistrHstDVo>) commonDao.queryList(ctVistrHstDVo);
		
		rsltMap.put("ctMngVistHstList", ctMngVistHstList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
		
	}
	
	/** 게시글 방문 이력 삭제 */
	public void deleteVistrHst(String bullId, QueryQueue queryQueue) throws Exception{
		CtVistrHstDVo ctVistrHstDVo = new CtVistrHstDVo();
		ctVistrHstDVo.setCtId(bullId);
		queryQueue.delete(ctVistrHstDVo);
	}
	
	/** 커뮤니티 홈 메뉴 찾기 */
	public String getCtHomeMenuId(String ctId) throws SQLException{
		
		CtFncDVo ctFncDVo = new CtFncDVo();
		ctFncDVo.setCtFncId("CTHOME");
		ctFncDVo.setCtId(ctId);
		
		ctFncDVo = (CtFncDVo) commonDao.queryVo(ctFncDVo);
		
		
		return ctFncDVo!=null?ctFncDVo.getCtFncUid():"";
	}
	
	
	/** 커뮤니티 완전 삭제 > 커뮤니티 내 자료까지 삭제*/
	public boolean deleteAllCt(HttpServletRequest request, CtEstbBVo ctEstbBVo, QueryQueue queryQueue , List<List<CommonFileVo>> deleteFileListInList) throws SQLException{
		try{
			//커뮤니티 ID
			String ctId = ctEstbBVo.getCtId();
			//첨부파일
			List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
			
			/** 게시판 */
			CtBullMastBVo ctBullMastVo = new CtBullMastBVo();
			ctBullMastVo.setCtId(ctId);
			ctBullMastVo.setCompId(ctEstbBVo.getCompId());
			
			@SuppressWarnings("unchecked")
			List<CtBullMastBVo> ctBullMastList = (List<CtBullMastBVo>) commonDao.queryList(ctBullMastVo);
			for(CtBullMastBVo ctBullMast: ctBullMastList){
				//한줄답변 삭제
				ctBullMastSvc.deleteCmdBoard(ctBullMast.getBullId(), queryQueue);
				//게시물 삭제 
				ctBullMastSvc.deleteBoard(ctBullMast.getBullId(), queryQueue);
				//점수전체 삭제
				ctScreHstSvc.deleteScreHst(ctBullMast.getBullId(), queryQueue);
				//추천전체 삭제
				ctRecmdHstSvc.deleteRecmdHst(ctBullMast.getBullId(), queryQueue);
				//방문이력 삭제
				deleteVistrHst(String.valueOf(ctBullMast.getBullId()), queryQueue);
				//자료(파일) DB 삭제
				deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctBullMast.getBullId()), queryQueue);
				deleteFileListInList.add(deletedFileList);
			}
			
			/** 토론실 */
			CtDebrBVo ctDebrBVo = new CtDebrBVo();
			ctDebrBVo.setCtId(ctId);
			
			@SuppressWarnings("unchecked")
			List<CtDebrBVo> ctDebrList = (List<CtDebrBVo>) commonDao.queryList(ctDebrBVo);
				
			for(CtDebrBVo ctDebr: ctDebrList){
				//토론 게시물 내용 삭제
				ctDebrSvc.deleteDebrOpin(ctDebr.getDebrId(), queryQueue);
				//토론 게시물 삭제
				ctDebrSvc.deleteDebr(ctDebr.getDebrId(), queryQueue);
			}
			
			/** SITE */
			CtSiteBVo ctSiteBVo = new CtSiteBVo();
			ctSiteBVo.setCtId(ctId);
			
			@SuppressWarnings("unchecked")
			List<CtSiteBVo> ctSiteList = (List<CtSiteBVo>) commonDao.queryList(ctSiteBVo);
			for(CtSiteBVo ctSite: ctSiteList){
				ctSiteSvc.deleteSite(ctSite.getSiteId(), ctId ,queryQueue);
			}
			
			/** 설문 */
			CtSurvBVo ctSurvBVo = new CtSurvBVo();
			ctSurvBVo.setCtId(ctId);
			
			@SuppressWarnings("unchecked")
			List<CtSurvBVo> ctSurvBList = (List<CtSurvBVo>) commonDao.queryList(ctSurvBVo);
			
			for(CtSurvBVo ctSurv: ctSurvBList){
				ctSurvSvc.deleteSurv(ctSurv.getSurvId(), ctId ,queryQueue, request);
			}
			
			
			CtSchdlBVo ctSchdlBVo = new CtSchdlBVo();
			ctSchdlBVo.setCtId(ctId);
			
			@SuppressWarnings("unchecked")
			List<CtSchdlBVo> ctSchdlList = (List<CtSchdlBVo>) commonDao.queryList(ctSchdlBVo);
	
			for(CtSchdlBVo ctSchdl: ctSchdlList){
				
				//일정 참석자 포함 모두 삭제
				ctScdManagerSvc.deleteSchdl(ctSchdl.getSchdlId(), ctId, queryQueue, request);
					
				//자료(파일) DB 삭제
				deletedFileList = ctFileSvc.deleteCtFile(String.valueOf(ctSchdl.getSchdlId()), queryQueue);
				deleteFileListInList.add(deletedFileList);
			}
			
			//커뮤니티
			queryQueue.delete(ctEstbBVo);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

		return true;
		
	}
	
	/** 커뮤니티 가입 체크*/
	public boolean chkJoinCt2(UserVo userVo, ModelMap model, HttpServletRequest request, String ctId) throws Exception{
		CtMbshDVo ctMbshD = new CtMbshDVo();
		//ctMbshD.setCompId(userVo.getCompId());
		ctMbshD.setCtId(ctId);
		ctMbshD.setUserUid(userVo.getUserUid());
		
		ctMbshD = (CtMbshDVo) commonDao.queryVo(ctMbshD);
		
		if((ctMbshD == null?true:!ctMbshD.getJoinStat().equalsIgnoreCase("3")) ){
				
			if(ctMbshD != null){
				if(ctMbshD.getJoinStat().equalsIgnoreCase("1")){
					//ct.msg.access.joinWait = 가입 승인 중입니다. 가입 승인 후 사용하시기 바랍니다.
					model.put("message", messageProperties.getMessage("ct.msg.access.joinWait", request));
				}else{
					//ct.msg.access.close = 커뮤니티 회원이 아닙니다.\n 가입후 사용해 주십시요.
					model.put("message", messageProperties.getMessage("ct.msg.access.close", request));
				}
			}else{
				//ct.msg.access.close = 커뮤니티 회원이 아닙니다.\n 가입후 사용해 주십시요.
				model.put("message", messageProperties.getMessage("ct.msg.access.close", request));
			}
			
//			model.put("todo", "parent.location.replace('/ct/listMyCm.do?menuId="+request.getParameter("prevMenuId")
//					+"');");
			model.addAttribute("todo", "history.go(-1);"); 
			//공통 처리 페이지
			return false;
		}
		return true;
	}
	
	/** 커뮤니티 가입 체크
	 * @throws SecuException */
	public void chkJoinCt(UserVo userVo, HttpServletRequest request, String ctId) throws SecuException{
		if(ctId == null || ctId.isEmpty()) return;
		CtMbshDVo ctMbshD = new CtMbshDVo();
		//ctMbshD.setCompId(userVo.getCompId());
		ctMbshD.setCtId(ctId);
		ctMbshD.setUserUid(userVo.getUserUid());
		try{
			ctMbshD = (CtMbshDVo) commonDao.queryVo(ctMbshD);
		}catch(SQLException se){
			se.printStackTrace();
		}
		
		if(request.getRequestURI().startsWith("/ct/viewCm")) return;
		
		if((ctMbshD == null?true:!ctMbshD.getJoinStat().equalsIgnoreCase("3")) ){
			
			if(ctMbshD == null || !"1".equals(ctMbshD.getJoinStat())){
				//ct.msg.access.close = 커뮤니티 회원이 아닙니다.\n 가입후 사용해 주십시요.
				throw new SecuException("ct.msg.access.close", request);
			}
			
			if(ctMbshD != null && "1".equals(ctMbshD.getJoinStat())){
				//ct.msg.access.joinWait = 가입 승인 중입니다. 가입 승인 후 사용하시기 바랍니다.
				throw new SecuException("ct.msg.access.joinWait", request);
			}
		}
	}
	
}
