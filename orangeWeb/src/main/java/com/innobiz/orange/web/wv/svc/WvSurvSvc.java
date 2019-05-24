package com.innobiz.orange.web.wv.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
import com.innobiz.orange.web.wv.vo.WvSurvBVo;
import com.innobiz.orange.web.wv.vo.WvSurvPopupDVo;
import com.innobiz.orange.web.wv.vo.WvSurvQuesDVo;
import com.innobiz.orange.web.wv.vo.WvSurvReplyDVo;
import com.innobiz.orange.web.wv.vo.WvSurvUseAuthDVo;
@Service
public class WvSurvSvc {
	
	/** 테스트 중_rakoos */
	//private static final Logger LOGGER = Logger.getLogger(WvSurvSvc.class);
	
	/** 설문 공통 서비스 */
	@Autowired
	private WvCmSvc wvCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
//	/** 조직 공통 서비스 */
//	@Autowired
//	private OrCmSvc orCmSvc;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WvFileSvc wvFileSvc;
	
	/** 설문 목록 얻기 */
	@SuppressWarnings("unchecked")
	public List<WvSurvBVo> getSurvSurvey() {
		List<WvSurvBVo> survey = new ArrayList<WvSurvBVo>();
		
		WvSurvBVo surv = new WvSurvBVo();
		
		try{
			survey = (List<WvSurvBVo>) commonDao.queryList(surv);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
		return survey;
		
	}
	
	public WvSurvBVo oneSelectList(WvSurvBVo wvSurvBVo) throws Exception {
		return (WvSurvBVo) commonDao.queryVo(wvSurvBVo);
		
	}
	
	/** 설문 Insert */
	public int setWvSurvList(WvSurvBVo wvSurvBVo) throws Exception {
		return commonDao.insert(wvSurvBVo);
	}
	
	@SuppressWarnings("unchecked")
	/** 설문 목록 */
	public List<WvSurvBVo> getWvSurvList(WvSurvBVo wvSurvBVo ) throws Exception {
		return (List<WvSurvBVo>)commonDao.queryList(wvSurvBVo);
	}
	
	/** 설문 목록수 */
	public Integer getWvSurvListCnt(WvSurvBVo wvSurvBVo) throws Exception {
		return commonDao.count(wvSurvBVo);
	}
	
	/** 답변 목록 */
	@SuppressWarnings("unchecked")
	public List<WvSurvReplyDVo> getWvSurvReplyList(WvSurvReplyDVo wvsReVo) throws Exception {
		return (List<WvSurvReplyDVo>)commonDao.queryList(wvsReVo);
	}
	
	/** 답변 목록수 */
	public Integer getWvSurvReplyListCnt(WvSurvReplyDVo wvsReVo) throws Exception {
		return commonDao.count(wvsReVo);
	}
	
	/** 질문 목록수 */
	public Integer getsetWvSurvQuesCnt(WvSurvQuesDVo wvSurvQuesDVo) throws Exception {
		return commonDao.count(wvSurvQuesDVo);
	}
	
	/** 권한 목록 */
	@SuppressWarnings("unchecked")
	public List<WvSurvUseAuthDVo> getWvSurvAuthList(WvSurvUseAuthDVo wvsAuthVo) throws Exception {
		return (List<WvSurvUseAuthDVo>)commonDao.queryList(wvsAuthVo);
	}
	
	/** 설문 목록 조회 */
	public Map<String,Object> getWvSurvMapList(HttpServletRequest request , WvSurvBVo wvSurvBVo ) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		
		//String schWord = wvSurvBVo.getSchWord();//검색어
		
		wvSurvBVo.setInstanceQueryId("countUserWvSurvB_201411");
		Integer recodeCount = this.getWvSurvListCnt(wvSurvBVo);
		PersonalUtil.setPaging(request, wvSurvBVo, recodeCount);
		
		wvSurvBVo.setInstanceQueryId("selectUserWvSurvB_201411");
		//목록 조회
		List<WvSurvBVo> wvSurvBVoList = this.getWvSurvList(wvSurvBVo);
		QueryQueue queryQueue = new QueryQueue();

		
		Map<String, Object> wvSurvBInfoMap;
		List<Map<String, Object>> wvSurvBMapList = new ArrayList<Map<String, Object>>();
		for(WvSurvBVo storedWvSurvBVo : wvSurvBVoList){
			//권한 조회
			WvSurvUseAuthDVo useAuthDVo = new WvSurvUseAuthDVo();
			useAuthDVo.setSurvId(storedWvSurvBVo.getSurvId());
			@SuppressWarnings("unchecked")
			List<WvSurvUseAuthDVo> useAuthVoList = (List<WvSurvUseAuthDVo>) commonDao.queryList(useAuthDVo);
			storedWvSurvBVo.setSurvAuthList(useAuthVoList);
						
			//오늘 날짜에 따른 상태권한 세팅
			Calendar startCal=WcScdManagerSvc.stringConvertCal(storedWvSurvBVo.getSurvStartDt(),true);
			//true : 날짜셋팅 defalut:00:00:00 false: timeset
			Calendar endCal=WcScdManagerSvc.stringConvertCal(storedWvSurvBVo.getSurvEndDt(),false);
			Date curDate = new Date();
			Calendar currentCal = Calendar.getInstance();
			currentCal.setTime(curDate);
			WvSurvBVo wvSurvStat = new WvSurvBVo();
			wvSurvStat.setCompId(storedWvSurvBVo.getCompId());
			wvSurvStat.setSurvId(storedWvSurvBVo.getSurvId());
			
			
			if(storedWvSurvBVo.getSurvPrgStatCd().equals("3")){
				if(startCal.after(currentCal)){
					storedWvSurvBVo.setSurvPrgStatCd("1");
					wvSurvStat.setSurvPrgStatCd(storedWvSurvBVo.getSurvPrgStatCd());
					queryQueue.update(wvSurvStat);
				}else{
					if(endCal.before(currentCal)){
						storedWvSurvBVo.setSurvPrgStatCd("4");
						wvSurvStat.setSurvPrgStatCd(storedWvSurvBVo.getSurvPrgStatCd());
						queryQueue.update(wvSurvStat);
					}else{
						storedWvSurvBVo.setSurvPrgStatCd("3");
					}
				}
			}
			wvSurvBInfoMap = VoUtil.toMap(storedWvSurvBVo, null);
			wvSurvBMapList.add(wvSurvBInfoMap);
		}
		if(queryQueue.size() != 0){
			commonDao.execute(queryQueue);
		}
		rsltMap.put("wvSurvBMapList", wvSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	
	/** 설문 저장 */
	public void setWvSurvList(HttpServletRequest request, WvSurvBVo wvSurvBVo, QueryQueue queryQueue, UserVo userVo, ModelMap model, String fnc)throws Exception{
		String mTime = StringUtil.getCurrDateTime();
		String survId = request.getParameter("survId");
		wvSurvBVo.setSurvSubj(request.getParameter("subj"));
		//wvSurvBVo.setSurvStartDt(request.getParameter("strtDt") + " 23:59:59");
		wvSurvBVo.setSurvStartDt(request.getParameter("strtDt") + " 00:00:00");
		wvSurvBVo.setSurvEndDt(request.getParameter("finDt") + " 23:59:59");
		wvSurvBVo.setPltId("");
		wvSurvBVo.setRegDt(mTime);
		
		wvSurvBVo.setOpenYn(request.getParameter("resPublYn"));
		wvSurvBVo.setRepetSurvYn(request.getParameter("resurvPosbYn"));
		wvSurvBVo.setSurvItnt(request.getParameter("editor1"));
		wvSurvBVo.setSurvFtr(request.getParameter("editor2"));
		wvSurvBVo.setAdmRjtOpinCont("");
		
		wvSurvBVo.setNoAuthYn(request.getParameter("noAuthYn"));
		
		
		if( fnc.equalsIgnoreCase("reg") 
				|| survId.isEmpty() || survId == null){
			
			wvSurvBVo.setSurvId(wvCmSvc.createId("WV_SURV_B"));
			//미저장
			wvSurvBVo.setSurvPrgStatCd("5");
			//수정자
			wvSurvBVo.setModrUid(userVo.getUserUid());
			//수정시간
			wvSurvBVo.setModDt(mTime);
			
			queryQueue.insert(wvSurvBVo);
			
		}else if(fnc.equalsIgnoreCase("mod")
				&& survId != null
				&& !survId.equalsIgnoreCase("")){
			wvSurvBVo.setSurvId(survId);
			//수정자
			wvSurvBVo.setModrUid(userVo.getUserUid());
			//수정시간
			wvSurvBVo.setModDt(mTime);
			queryQueue.update(wvSurvBVo);
			
		}
		
		//refId 때문에 마지막에 처리해야하 합니다.
		wvFileSvc.saveSurvFile(request, wvSurvBVo.getSurvId(), queryQueue);
		int fileSaveSize=queryQueue.size();
		wvSurvBVo.setAttYn(fileSaveSize>0?"Y":"N");
		
		model.put("wvSurvBVo", wvSurvBVo);
		 
	}

		
	
	/** 오늘날짜구하기 */
	public String currentDay(){
		return StringUtil.getCurrDateTime();
	}
	
	
	/** 주관식 답변 조회 */
	public Map<String,Object> getOendReplyCont(HttpServletRequest request , WvSurvReplyDVo wvsReVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");

		Integer recodeCount = this.getWvSurvReplyListCnt(wvsReVo);
		PersonalUtil.setPaging(request, wvsReVo, recodeCount);
		
		//목록 조회
		List<WvSurvReplyDVo> wvSurvReplyVoList = this.getWvSurvReplyList(wvsReVo);
		
		Map<String, Object> wvSurvBInfoMap;
		List<Map<String, Object>> wvSurvBMapList = new ArrayList<Map<String, Object>>();
		for(WvSurvReplyDVo storedWvSurvBVo : wvSurvReplyVoList){
			wvSurvBInfoMap = VoUtil.toMap(storedWvSurvBVo, null);
			wvSurvBMapList.add(wvSurvBInfoMap);
		}
		rsltMap.put("wvSurvBMapList", wvSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	
	/** 신청중인 설문 목록 조회 */
	public Map<String,Object> getSurvApvdMapList(HttpServletRequest request , WvSurvBVo wvSurvBVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = wvSurvBVo.getSchWord();//검색어
		
		Integer recodeCount = this.getWvSurvListCnt(wvSurvBVo);
		PersonalUtil.setPaging(request, wvSurvBVo, recodeCount);
		
		//목록 조회
		List<WvSurvBVo> wvSurvBVoList = this.getWvSurvList(wvSurvBVo);
		
		Map<String, Object> wvSurvBInfoMap;
		List<Map<String, Object>> wvSurvBMapList = new ArrayList<Map<String, Object>>();
		for(WvSurvBVo storedWvSurvBVo : wvSurvBVoList){
			
//			WvSurvBVo wvsVo =  new WvSurvBVo();
//			wvsVo.setSurvId(survId);
//			VoUtil.bind(request, wvsVo);
//			//설문기본
//			wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
//			if(wvsVo != null){
				//Map<String, Object> agntMap =  orCmSvc.getUserMap(storedWvSurvBVo.getRegrUid(), langTypCd);
				
				// 사용자기본(OR_USER_B) 테이블
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(storedWvSurvBVo.getRegrUid());
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				
				//등록자명
				storedWvSurvBVo.setRegrNm(orUserBVo.getRescNm());
//			}
			
			wvSurvBInfoMap = VoUtil.toMap(storedWvSurvBVo, null);
			wvSurvBMapList.add(wvSurvBInfoMap);
		}
		rsltMap.put("wvSurvBMapList", wvSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	
	/** 설문 목록 조회 관리자 */
	public Map<String,Object> getAdmSurvMapList(HttpServletRequest request , WvSurvBVo wvSurvBVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//String schWord = wvSurvBVo.getSchWord();//검색어
		Integer recodeCount = this.getWvSurvListCnt(wvSurvBVo);
		PersonalUtil.setPaging(request, wvSurvBVo, recodeCount);
		
		//목록 조회
		List<WvSurvBVo> wvSurvBVoList = this.getWvSurvList(wvSurvBVo);
		QueryQueue queryQueue = new QueryQueue();
		
		Map<String, Object> wvSurvBInfoMap;
		List<Map<String, Object>> wvSurvBMapList = new ArrayList<Map<String, Object>>();
		for(WvSurvBVo storedWvSurvBVo : wvSurvBVoList){
			
			Calendar startCal=WcScdManagerSvc.stringConvertCal(storedWvSurvBVo.getSurvStartDt(),true);
			Calendar endCal=WcScdManagerSvc.stringConvertCal(storedWvSurvBVo.getSurvEndDt(),false);
			Date curDate = new Date();
			Calendar currentCal = Calendar.getInstance();
			currentCal.setTime(curDate);
			WvSurvBVo wvSurvStat = new WvSurvBVo();
			wvSurvStat.setCompId(storedWvSurvBVo.getCompId());
			wvSurvStat.setSurvId(storedWvSurvBVo.getSurvId());
			
			//설문 진행 상태코드 값
			if(storedWvSurvBVo.getSurvPrgStatCd().equals("3")){
//				
				//오늘이 시작날짜보다 이 후면 진행중
				if(startCal.after(currentCal)){
					storedWvSurvBVo.setSurvPrgStatCd("1");
					wvSurvStat.setSurvPrgStatCd(storedWvSurvBVo.getSurvPrgStatCd());
					queryQueue.update(wvSurvStat);
				}else{
					if(endCal.before(currentCal)){
						storedWvSurvBVo.setSurvPrgStatCd("4");
						wvSurvStat.setSurvPrgStatCd(storedWvSurvBVo.getSurvPrgStatCd());
						queryQueue.update(wvSurvStat);
					}else{
						storedWvSurvBVo.setSurvPrgStatCd("3");
					}
				}
			}
			wvSurvBInfoMap = VoUtil.toMap(storedWvSurvBVo, null);
			wvSurvBMapList.add(wvSurvBInfoMap);
		}
		if(queryQueue.size() != 0){
			commonDao.execute(queryQueue);
		}
		rsltMap.put("wvSurvBMapList", wvSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	/** 권한 목록 조회 */
	public List<WvSurvUseAuthDVo> getAuthList(UserVo userVo, WvSurvBVo wvsVo, String authTypCd, String authGrdCd ) throws Exception{
		
		WvSurvUseAuthDVo wvsVoteDeptAuth =  new WvSurvUseAuthDVo();
		wvsVoteDeptAuth.setCompId(userVo.getCompId());
		wvsVoteDeptAuth.setSurvId(wvsVo.getSurvId());
		wvsVoteDeptAuth.setAuthTgtTypCd(authTypCd);
		wvsVoteDeptAuth.setAuthGradCd(authGrdCd);
		
		return getWvSurvAuthList(wvsVoteDeptAuth);
	}
	
	/** Map to Vo */
	public void setParamToVo(Map<String, Object> paramMap, CommonVo commonVo){
		if(paramMap==null || commonVo==null) return;
		// set header
		Entry<String, Object> entry;
		Iterator<Entry<String, Object>> iterator = paramMap.entrySet().iterator();
		String key;
		while(iterator.hasNext()){
			entry = iterator.next();
			key = entry.getKey();
			VoUtil.setValue(commonVo, key, entry.getValue());
		}
	}

	/** 팝업 설문 목록 조회 */
	public List<String> getPopSurvIdList(HttpServletRequest request, UserVo userVo) throws SQLException{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 회사ID		
		String compId=userVo.getCompId();
		// 사용자UID		
		String userUid=userVo.getUserUid();
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();		
		wvSurvBVo.setLogUserUid(userUid);
		wvSurvBVo.setLogUserDeptId(userVo.getDeptId());
		//추가 - 조직ID 목록
		wvSurvBVo.setSchOrgPids(userVo.getOrgPids());
		wvSurvBVo.setAuthTgtUid("authTgtUid");
		wvSurvBVo.setLangTyp(langTypCd);
		
		List<String> survList=new ArrayList<String>();
		survList.add("3");
		wvSurvBVo.setSurvSearchList(survList);
		wvSurvBVo.setQueryLang(langTypCd);
		wvSurvBVo.setCompId(compId);
		wvSurvBVo.setInstanceQueryId("com.innobiz.orange.web.wv.dao.WvSurvBDao.selectPopupWvSurvB");
		
		// 기간 검색
		String toDate = StringUtil.getCurrYmd();
		wvSurvBVo.setDurCat("fromYmd");
		wvSurvBVo.setDurStrtDt(toDate);
		wvSurvBVo.setDurEndDt(toDate);
		
		@SuppressWarnings("unchecked")
		List<String> survIdList = (List<String>)commonDao.queryList(wvSurvBVo);
		
		List<String> returnList = null;
		if(survIdList!=null){
			// 설문 참여여부
			WvSurvReplyDVo wvSurvReplyDVo = null;
			// 팝업 설정
			WvSurvPopupDVo wvSurvPopupDVo =null;
			for(String survId : survIdList){
				wvSurvReplyDVo = new WvSurvReplyDVo();
				wvSurvReplyDVo.setCompId(compId);
				wvSurvReplyDVo.setSurvId(survId);
				wvSurvReplyDVo.setReplyrUid(userUid);
				// 설문 참여자 이면 제외
				if(commonDao.count(wvSurvReplyDVo)>0) continue;
				
				wvSurvPopupDVo = new WvSurvPopupDVo();
				wvSurvPopupDVo.setCompId(compId);
				wvSurvPopupDVo.setSurvId(survId);
				wvSurvPopupDVo.setUseYn("Y"); // 사용여부
				wvSurvPopupDVo = (WvSurvPopupDVo)commonDao.queryVo(wvSurvPopupDVo);
				if(wvSurvPopupDVo==null) continue;
				
				if(returnList==null)
					returnList=new ArrayList<String>();
				returnList.add(survId);
			}
		}
		
		return returnList;
		
	}
	
}
