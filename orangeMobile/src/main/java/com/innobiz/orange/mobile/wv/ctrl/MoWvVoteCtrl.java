package com.innobiz.orange.mobile.wv.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wv.svc.WvCmSvc;
import com.innobiz.orange.web.wv.svc.WvFileSvc;
import com.innobiz.orange.web.wv.svc.WvSurvSvc;
import com.innobiz.orange.web.wv.vo.WvQuesExamDVo;
import com.innobiz.orange.web.wv.vo.WvSurvBVo;
import com.innobiz.orange.web.wv.vo.WvSurvQuesDVo;
import com.innobiz.orange.web.wv.vo.WvSurvReplyDVo;

/** 설문조사 */
@Controller
public class MoWvVoteCtrl {

	/** 공통 서비스 */
	@Autowired
	private WvSurvSvc wvSurvSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WvFileSvc wvFileSvc;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 설문 공통 서비스 */
	@Autowired
	private WvCmSvc wvCmSvc;
	
	/** 첨부설정 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 설문 목록 */
	@RequestMapping(value = "/wv/listSurv")
	public String listSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		wvSurvBVo.setLogUserUid(userVo.getUserUid());
		wvSurvBVo.setLogUserDeptId(userVo.getDeptId());
		//추가 - 조직ID 목록
		wvSurvBVo.setSchOrgPids(userVo.getOrgPids());
		wvSurvBVo.setAuthTgtUid("authTgtUid");
		wvSurvBVo.setLangTyp(langTypCd);
		List<String> survList=new ArrayList<String>();
		
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && !schCtStat.isEmpty()){
			if(schCtStat.equals("ALL"))
			{
				survList.add("3");
				survList.add("4");
			}
			else
				survList.add(schCtStat);
		}else{
			/**
			 * 설문목록 검색조건 디폴트는 '진행중'
			 * */
			survList.add("3");
		}
		
		wvSurvBVo.setSurvSearchList(survList);
		wvSurvBVo.setQueryLang(langTypCd);
		wvSurvBVo.setCompId(userVo.getCompId());
		/**
		 * 설문목록에서 임시저장 상태는 뺌.
		 * */
		//wvSurvBVo.setSurvPrgStatCd("6"); 
		Map<String,Object> rsltMap = wvSurvSvc.getWvSurvMapList(request, wvSurvBVo);
		
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));
		model.put("logUserUid", wvSurvBVo.getLogUserUid());
		model.put("logUserDeptId", wvSurvBVo.getLogUserDeptId());
		if(userVo.getOrgPids()!=null)
			model.put("orgPidsToString", Arrays.toString(userVo.getOrgPids()));
			
		return MoLayoutUtil.getJspPath("/wv/listSurv");
	}
	
	/** 설문참여 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/wv/viewSurv")
	public String viewSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		VoUtil.bind(request, wvsVo);
		
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");

			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
				for(WvQuesExamDVo wvQVo : wcQueExamList){
					returnWcQueExamList.add(wvQVo);
				}
			
				//단 라디오버튼일때는 각각 넣어줘야함 나중에 정리 필요함.
				WvSurvReplyDVo wvSurvReplyDVo = new WvSurvReplyDVo();
				wvSurvReplyDVo.setSurvId(survId);
				wvSurvReplyDVo.setReplyrUid(userVo.getUserUid());
				wvSurvReplyDVo.setQuesId(wvsQVo.getQuesId());
			
				List<WvSurvReplyDVo> survReplyListAdd = (List<WvSurvReplyDVo>) commonDao.queryList(wvSurvReplyDVo);
				
				wvsQVo.setWvSurvReplyDVo(survReplyListAdd);
			}

			//멀티체크와 주관식일때 문제 없이 이용가능함.
			WvSurvReplyDVo wvSurvReplyDVo = new WvSurvReplyDVo();
			wvSurvReplyDVo.setSurvId(survId);
			wvSurvReplyDVo.setReplyrUid(userVo.getUserUid());
			
			List<WvSurvReplyDVo> survReplyList = (List<WvSurvReplyDVo>) commonDao.queryList(wvSurvReplyDVo);
			
			model.put("survReplyList", survReplyList);
			model.put("wcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);	
		}

		return MoLayoutUtil.getJspPath("/wv/viewSurv");
	}
	
	/** 주관식 답변보기*/
	@RequestMapping(value= "/wv/listOendAnsPop")
	public String listOendAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
		wvsQueVo.setQueryLang(langTypCd);
		wvsQueVo.setCompId(userVo.getCompId());
		wvsQueVo.setQuesId(quesId);
		wvsQueVo.setSurvId(survId);

		wvsQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
		
		// 조회조건 매핑
		WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
		
		wvsReVo.setQueryLang(langTypCd);
		wvsReVo.setCompId(userVo.getCompId());
		wvsReVo.setSurvId(survId);
		wvsReVo.setQuesId(quesId);
		wvsReVo.setReplyNo(-1);
		wvsReVo.setOrderBy("REPLY_DT DESC");
				
		Integer recodeCount = wvSurvSvc.getWvSurvReplyListCnt(wvsReVo);
		PersonalUtil.setPaging(request, wvsReVo, recodeCount);
		wvsReVo.setPageRowCnt(recodeCount);
		
		//목록 조회
		List<WvSurvReplyDVo> wvSurvReplyVoList = wvSurvSvc.getWvSurvReplyList(wvsReVo);
		
		Map<String, Object> wvSurvBInfoMap;
		List<Map<String, Object>> wvSurvBMapList = new ArrayList<Map<String, Object>>();
		for(WvSurvReplyDVo storedWvSurvBVo : wvSurvReplyVoList){
			wvSurvBInfoMap = VoUtil.toMap(storedWvSurvBVo, null);
			wvSurvBMapList.add(wvSurvBInfoMap);
		}
		
		model.put("wvSurvBMapList", wvSurvBMapList);
		model.put("recodeCount", recodeCount);
		
		model.put("wvsQueVo", wvsQueVo);
		model.put("quesCount", quesCount);
		
		return MoLayoutUtil.getJspPath("/wv/listOendAnsPop");
	}
	
	/** 객관식입력항목 답변보기*/
	@RequestMapping(value= "/wv/listMulcAnsPop")
	public String listMulcAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String replyNo = request.getParameter("replyNo");

		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
		wvsQueVo.setQueryLang(langTypCd);
		wvsQueVo.setCompId(userVo.getCompId());
		wvsQueVo.setQuesId(quesId);
		wvsQueVo.setSurvId(survId);

		wvsQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
		
		// 조회조건 매핑
		WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
		
		wvsReVo.setQueryLang(langTypCd);
		wvsReVo.setCompId(userVo.getCompId());
		wvsReVo.setSurvId(survId);
		wvsReVo.setQuesId(quesId);
		wvsReVo.setReplyNo(Integer.parseInt(replyNo));
		wvsReVo.setOrderBy("REPLY_DT DESC");
		wvsReVo.setMulcInputReplyContYn("Y");
		
		Integer recodeCount = wvSurvSvc.getWvSurvReplyListCnt(wvsReVo);
		PersonalUtil.setPaging(request, wvsReVo, recodeCount);
		wvsReVo.setPageRowCnt(recodeCount);
		
		//목록 조회
		List<WvSurvReplyDVo> wvSurvReplyVoList = wvSurvSvc.getWvSurvReplyList(wvsReVo);
		
		Map<String, Object> wvSurvBInfoMap;
		List<Map<String, Object>> wvSurvBMapList = new ArrayList<Map<String, Object>>();
		for(WvSurvReplyDVo storedWvSurvBVo : wvSurvReplyVoList){
			wvSurvBInfoMap = VoUtil.toMap(storedWvSurvBVo, null);
			wvSurvBMapList.add(wvSurvBInfoMap);
		}
		model.put("wvSurvBMapList", wvSurvBMapList);
		model.put("recodeCount", recodeCount);

		model.put("wvsQueVo", wvsQueVo);
		
		return MoLayoutUtil.getJspPath("/wv/listMulcAnsPop");
	}
	
	/** 통계보기 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/wv/viewSurvRes")
	public String viewSurvRes(HttpServletRequest request, ModelMap model) throws Exception {

		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		String openYn = request.getParameter("openYn");
		String repetYn = request.getParameter("repetYn");
		
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		WvSurvReplyDVo wvsReVo = new WvSurvReplyDVo();
		
		wvsReVo.setSurvId(survId);
		wvsReVo.setReplyrUid(userVo.getUserUid());
		
		int survReply = commonDao.count(wvsReVo)!=null?commonDao.count(wvsReVo):0; 
		
		if(survReply != 0){ 
			if(openYn.equalsIgnoreCase("N")){
				model.put("message", messageProperties.getMessage("wv.msg.survApvd.openFail", request));
				model.put("todo", "parent.location.replace('/wv/listSurv.do?menuId="+request.getParameter("menuId")
						+"');");
				
				//공통 처리 페이지
				return MoLayoutUtil.getResultJsp();
			}
		}
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		wvsVo.setQueryLang(langTypCd);
		VoUtil.bind(request, wvsVo);
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");

			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						
						//질문전체 count
						WvSurvReplyDVo wvReAllCount = new WvSurvReplyDVo();
						wvReAllCount.setCompId(wvQVo.getCompId());
						wvReAllCount.setSurvId(wvQVo.getSurvId());
						wvReAllCount.setQuesId(wvQVo.getQuesId());
						float quesAllCount = commonDao.count(wvReAllCount)!=null?commonDao.count(wvReAllCount):0;

						//질문보기 개당 count
						WvSurvReplyDVo wvReOneCount = new WvSurvReplyDVo();
						wvReOneCount.setCompId(wvQVo.getCompId());
						wvReOneCount.setSurvId(wvQVo.getSurvId());
						wvReOneCount.setQuesId(wvQVo.getQuesId());
						wvReOneCount.setReplyNo(wvQVo.getExamNo());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(wvReOneCount)!=null?commonDao.count(wvReOneCount):0;
						
						//평균 구하기
						float average = (quesOneCount / quesAllCount) * 100;
						
						wvQVo.setSelectCount(String.valueOf((int)quesOneCount));
						wvQVo.setQuesAverage(String.valueOf((int)average));
						returnWcQueExamList.add(wvQVo);
					}
					
					//주관식 질문  count
					WvSurvReplyDVo wvReOendCount = new WvSurvReplyDVo();
					wvReOendCount.setCompId(wvsQVo.getCompId());
					wvReOendCount.setSurvId(wvsQVo.getSurvId());
					wvReOendCount.setQuesId(wvsQVo.getQuesId());
					wvReOendCount.setReplyNo(-1);
					float quesOendCount = commonDao.count(wvReOendCount)!=null?commonDao.count(wvReOendCount):0;
					
					wvsQVo.setOendCount(String.valueOf((int)quesOendCount));
			}

			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);
			model.put("repetYn", repetYn);
		}
		
		return MoLayoutUtil.getJspPath("/wv/viewSurvRes");
	}
	
	/** 설문참여 저장*/
	@RequestMapping(value= "/wv/viewSurvSave")
	public String viewSurvSave(HttpServletRequest request, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo = new WvSurvBVo(); 
		wvsVo.setSurvId(survId);
		wvsVo.setCompId(userVo.getCompId());
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		
		if(wvsVo != null){
			//재설문일시 사용사 답변상세 삭제 후 재 입력
			if(wvsVo.getRepetSurvYn().equalsIgnoreCase("Y")){
				WvSurvReplyDVo wvsReVo = new  WvSurvReplyDVo();
				wvsReVo.setSurvId(survId);
				wvsReVo.setReplyrUid(userVo.getUserUid());
				wvsReVo.setCompId(userVo.getCompId());
				queryQueue.delete(wvsReVo);
			}
		}
		
		WvSurvQuesDVo wvsQueVo =  new WvSurvQuesDVo();
		wvsQueVo.setSurvId(survId);
		@SuppressWarnings("unchecked")
		List<WvSurvQuesDVo> returnQuesVo =  (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
		
  		for(WvSurvQuesDVo wcsQVo : returnQuesVo){
			WvSurvReplyDVo wcsRVo = new WvSurvReplyDVo();
			
			
			wcsRVo.setCompId(wcsQVo.getCompId());
			wcsRVo.setQuesId(wcsQVo.getQuesId());
			wcsRVo.setSurvId(wcsQVo.getSurvId());
			wcsRVo.setReplyDt(wvSurvSvc.currentDay());
			wcsRVo.setReplyrUid(userVo.getUserUid());
			wcsRVo.setReplyrDeptId(userVo.getDeptId());
			
			//객관식
			if(wcsQVo.getMulChoiYn() != null){
				if(wcsQVo.getMulChoiYn().equalsIgnoreCase("Y")){
					String[] survQues = request.getParameterValues(wcsQVo.getQuesId());
					String[] survQuesInput = request.getParameterValues("inputCheck"+wcsQVo.getQuesId());
					if(survQues != null){
						for(int i=0; i < survQues.length; i++){
							WvSurvReplyDVo cloneWvSurv =(WvSurvReplyDVo) wcsRVo.clone();
							cloneWvSurv.setReplyNo(Integer.parseInt(survQues[i]));
							if(survQuesInput != null){
								//입력여부Y이면 객관식에 text 가능함.
								cloneWvSurv.setMulcInputReplyCont(survQuesInput[Integer.parseInt(survQues[i])-1]);
							}
							queryQueue.insert(cloneWvSurv);
						}
					}
				}else if(wcsQVo.getMulChoiYn().equalsIgnoreCase("N")){
					String survQues = request.getParameter(wcsQVo.getQuesId());
					String survQuesInput = request.getParameter("inputRadio"+ wcsQVo.getQuesId() + survQues);
					if(survQues != null){
						wcsRVo.setReplyNo(Integer.parseInt(survQues));
						if(survQuesInput != null){
							//입력여부Y이면 객관식에 text 가능함.
							wcsRVo.setMulcInputReplyCont(survQuesInput);
							
						}
						queryQueue.insert(wcsRVo);
					}
				}
			//주관식
			}else{
				String survQues = request.getParameter(wcsQVo.getQuesId());
				
				if(!survQues.isEmpty()){
					wcsRVo.setReplyNo(-1);
					wcsRVo.setOendReplyCont(survQues);
					queryQueue.insert(wcsRVo);
				}
			}
			
		}
		
		commonDao.execute(queryQueue);		
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "$m.nav.prev(event, '/wv/listSurv.do?menuId="+request.getParameter("menuId")
				+"');");
		
		//공통 처리 페이지
		return MoLayoutUtil.getResultJsp();
	}
	
	/** 승인대기 설문 */
	@RequestMapping(value= "/wv/listSurvApvd")
	public String listSurvApvd(HttpServletRequest request, ModelMap model) throws Exception {
		
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 조회조건 매핑
		WvSurvBVo wvSurvBVo = new WvSurvBVo();
		VoUtil.bind(request, wvSurvBVo);
		//SecuUtil.hasAuth 사용자의 관리권한 여부 확인
		wvSurvBVo.setCompId(userVo.getCompId());
		
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		if(!userVo.isAdmin() && !userVo.isSysAdmin() && !SecuUtil.hasAuth(request, "A", null)){
			rsltMap.put("wvSurvBMapList", null);
			rsltMap.put("recodeCount", 0);
		}else{
			List<String> survList=new ArrayList<String>();
			survList.add("2");
			wvSurvBVo.setSurvSearchList(survList);

			wvSurvBVo.setQueryLang(langTypCd);
			rsltMap = wvSurvSvc.getSurvApvdMapList(request, wvSurvBVo);
		}

		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("wvSurvBMapList", rsltMap.get("wvSurvBMapList"));

		return MoLayoutUtil.getJspPath("/wv/listSurvApvd");
	}
	
	/** 설문승인 view */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/wv/viewSurvApvd")
	public String viewSurvApvd(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		
		WvSurvBVo wvsVo =  new WvSurvBVo();
		wvsVo.setSurvId(survId);
		VoUtil.bind(request, wvsVo);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		wvFileSvc.putFileListToModel(survId, model, userVo.getCompId());
		
		//설문기본
		wvsVo = (WvSurvBVo) commonDao.queryVo(wvsVo);
		if(wvsVo != null){
			
			//설문질문상세
			WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
			wvsQueVo.setSurvId(wvsVo.getSurvId());
			wvsQueVo.setCompId(wvsVo.getCompId());
			wvsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<WvSurvQuesDVo> wvsQueList = (List<WvSurvQuesDVo>) commonDao.queryList(wvsQueVo);
			
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			
			for(WvSurvQuesDVo wvsQVo : wvsQueList){
				//질문보기상세
				WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
				wvQueExamVo.setSurvId(wvsQVo.getSurvId());
				wvQueExamVo.setCompId(wvsQVo.getCompId());
				wvQueExamVo.setQuesId(wvsQVo.getQuesId());
				
				List<WvQuesExamDVo> wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
					for(WvQuesExamDVo wvQVo : wcQueExamList){
						returnWcQueExamList.add(wvQVo);
					}
			}
		
			model.put("wcQueExamList", returnWcQueExamList);
			model.put("wvsQueList", wvsQueList);
			model.put("wvsVo", wvsVo);	
		}
		return MoLayoutUtil.getJspPath("/wv/viewSurvApvd");
	}
	
	/** 설문대기 승인 반려 */
	@RequestMapping(value= {"/wv/setSurvApvdSave"})
	public String setSurvApvdPolcSave(HttpServletRequest request,
			@RequestParam(value = "data", required = true) String data,
			ModelMap model) {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String survId = (String) object.get("survId");
			String apvdYn = (String) object.get("apvdYn");
			String returnSurvCont = (String) object.get("returnSurvCont");
			if (survId == null || apvdYn == null ) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			WvSurvBVo wvsVo = new WvSurvBVo();
			wvsVo.setSurvId(survId);
			wvsVo.setCompId(userVo.getCompId());
			
			if(apvdYn.equalsIgnoreCase("Y")){
				wvsVo.setSurvPrgStatCd("3");
			}else if(apvdYn.equalsIgnoreCase("N")){
				wvsVo.setSurvPrgStatCd("9");
				wvsVo.setAdmRjtOpinCont(returnSurvCont);
			}
			
			QueryQueue queryQueue = new QueryQueue();
			queryQueue.update(wvsVo);
			commonDao.execute(queryQueue);
			
			if ("N".equals(apvdYn)) {
				// bb.msg.rjt.success=반려 처리되었습니다.
				model.put("message", messageProperties.getMessage("bb.msg.rjt.success", request));
			} else {
				// bb.msg.apvd.success=승인 처리되었습니다.
				model.put("message", messageProperties.getMessage("bb.msg.apvd.success", request));
			}

			model.put("result", "ok");
			
		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			model.put("message", e.getMessage());
		}
		
		return JsonUtil.returnJson(model);
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = {"/wv/downFile","/wv/preview/downFile"})
	public ModelAndView downFile(HttpServletRequest request,
			@RequestParam(value = "fileIds", required = true) String fileIds,
			@RequestParam(value = "actionParam", required = false) String actionParam
			) throws Exception {
		
		try {
			if (fileIds.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
						
			// 다운로드 체크
			emAttachViewSvc.chkAttachDown(request, userVo.getCompId());
			// 파일 목록조회
			ModelAndView mv = wvCmSvc.getFileList(request , fileIds , actionParam);
			
			return mv;
			
		} catch (CmException e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
			return mv;
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** [팝업] 반려의견 */
	@RequestMapping(value = "/wv/setSurvApvdPop")
	public String setSurvApvdPop(HttpServletRequest request,
			@Parameter(name="survId", required=false) String survId,
			ModelMap model) throws Exception {
		
		return MoLayoutUtil.getJspPath("/wv/setSurvApvdPop");
	}
	
	/** 부서별 통계 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= {"/wv/listDeptStacsSub","/wv/listDeptStacsPop"})
	public String listDeptStacsPop(HttpServletRequest request, Locale locale , ModelMap model) throws Exception {
		
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
	
		//설문질문상세 (질문순서때문에 사용함)
		WvSurvQuesDVo wvsQueVo = new WvSurvQuesDVo();
		wvsQueVo.setQuesId(quesId);
		wvsQueVo.setSurvId(survId);
		wvsQueVo.setCompId(userVo.getCompId());
		
		wvsQueVo = (WvSurvQuesDVo) commonDao.queryVo(wvsQueVo);
		
		List<WvQuesExamDVo> wcQueExamList = new ArrayList<WvQuesExamDVo>();
		
		//질문보기상세
		WvQuesExamDVo wvQueExamVo =  new WvQuesExamDVo();
		wvQueExamVo.setSurvId(survId);
		wvQueExamVo.setCompId(userVo.getCompId());
		wvQueExamVo.setQuesId(quesId);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		WvSurvReplyDVo wvSurvReplyDistinct = new WvSurvReplyDVo();
		wvSurvReplyDistinct.setInstanceQueryId("selectWvSurvReplyDssss");
		wvSurvReplyDistinct.setSurvId(survId);
		wvSurvReplyDistinct.setCompId(userVo.getCompId());
		wvSurvReplyDistinct.setQuesId(quesId);
		wvSurvReplyDistinct.setQueryLang(langTypCd);
		/*String replyrDeptId = request.getParameter("replyrDeptId");
		if(replyrDeptId != null && !replyrDeptId.isEmpty() ){
			wvSurvReplyDistinct.setReplyrDeptId(replyrDeptId);
		}*/
		
		List<WvSurvReplyDVo> wvSurvReplyDeptList = (List<WvSurvReplyDVo>) commonDao.queryList(wvSurvReplyDistinct);
		
		for(WvSurvReplyDVo wvRe : wvSurvReplyDeptList){
			List<WvQuesExamDVo> returnWcQueExamList = new ArrayList<WvQuesExamDVo>();
			wcQueExamList = (List<WvQuesExamDVo>) commonDao.queryList(wvQueExamVo);
			for(WvQuesExamDVo wvQVo : wcQueExamList){
				//질문전체 count
				WvSurvReplyDVo wvReAllCount = new WvSurvReplyDVo();
				wvReAllCount.setCompId(wvQVo.getCompId());
				wvReAllCount.setSurvId(wvQVo.getSurvId());
				wvReAllCount.setQuesId(wvQVo.getQuesId());
				wvReAllCount.setReplyrDeptId(wvRe.getReplyrDeptId());
				float quesAllCount = commonDao.count(wvReAllCount)!=null?commonDao.count(wvReAllCount):0;

				//질문보기 개당 count
				WvSurvReplyDVo wvReOneCount = new WvSurvReplyDVo();
				wvReOneCount.setCompId(wvQVo.getCompId());
				wvReOneCount.setSurvId(wvQVo.getSurvId());
				wvReOneCount.setQuesId(wvQVo.getQuesId());
				wvReOneCount.setReplyNo(wvQVo.getExamNo());
				wvReOneCount.setReplyrDeptId(wvRe.getReplyrDeptId());
				//질문보기상세 선택된 개수 count 구하기
				float quesOneCount = commonDao.count(wvReOneCount)!=null?commonDao.count(wvReOneCount):0;
				
				wvQVo.setSelectCount(String.valueOf((int)quesOneCount));
				wvRe.setDeptTotalCount(String.valueOf((int)quesAllCount));
				returnWcQueExamList.add(wvQVo);
			}
			wvRe.setQuesExam(returnWcQueExamList);
		}
		
		model.put("wvSurvReplyDeptList", wvSurvReplyDeptList);
		model.put("wcQueExamList", wcQueExamList);
		model.put("wvsQueVo", wvsQueVo);
		model.put("quesCount",quesCount);
	
		if("listDeptStacsSub".equals(path)){
			model.put("UI_TITLE", messageProperties.getMessage("wv.cols.set.deptStat", locale)); //부서별 통계
			return MoLayoutUtil.getJspPath("/wv/listDeptStacsSub");
		}
			
		return MoLayoutUtil.getJspPath("/wv/listDeptStacsPop");
	}
}
