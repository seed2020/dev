package com.innobiz.orange.mobile.ct.ctrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.mobile.cm.utils.MoLayoutUtil;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.svc.CtSurvSvc;
import com.innobiz.orange.web.ct.vo.CtEstbBVo;
import com.innobiz.orange.web.ct.vo.CtQuesExamDVo;
import com.innobiz.orange.web.ct.vo.CtSurvBVo;
import com.innobiz.orange.web.ct.vo.CtSurvQuesDVo;
import com.innobiz.orange.web.ct.vo.CtSurvReplyDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 설문조사 */
@Controller
public class MoCtVoteCtrl {
	
	/** 공통 서비스 */
	@Autowired
	private CtSurvSvc ctSurvSvc;

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;
	
	@RequestMapping(value= "/ct/surv/listSurv")
	public String listSurv(HttpServletRequest request, ModelMap model) throws Exception {
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String ctId = request.getParameter("ctId");
		String fncUid = request.getParameter("menuId");

		// 조회조건 매핑
		CtSurvBVo ctSurvBVo = new CtSurvBVo();
		VoUtil.bind(request, ctSurvBVo);
		ctSurvBVo.setLogUserUid(userVo.getUserUid());
		ctSurvBVo.setLangTyp(langTypCd);
		ctSurvBVo.setCtId(ctId);
		ctSurvBVo.setCtFncUid(fncUid);
		
		List<String> survList=new ArrayList<String>();
		//검색조건 [상태]
		String schCtStat = request.getParameter("schCtStat");
		if(schCtStat != null && schCtStat != ""){
			
			if(schCtStat.equalsIgnoreCase("6")){
				//union을 하기때문에 조회가 되지 않는 flag를 입력합니다.
				survList.add("F");
				ctSurvBVo.setSurvPrgStatCd("6");
			}else{
				survList.add(schCtStat);
				ctSurvBVo.setSurvPrgStatCd(survList.get(0));
			}
			ctSurvBVo.setSurvSearchList(survList);
			
		}else{
			//모바일에서는 임시저장,준비중 항목을 보여주지 않는다.
			ctSurvBVo.setSurvPrgStatCd("60");
			//준비중, 진행중, 마감, 임시저장
			//survList.add("1");
			survList.add("3");
			survList.add("4");
			ctSurvBVo.setSurvSearchList(survList);
		}
		
		ctSurvBVo.setQueryLang(langTypCd);
		Map<String,Object> rsltMap = ctSurvSvc.getAdmSurvMapList(request, ctSurvBVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("logUserUid", ctSurvBVo.getLogUserUid());
		model.put("schCtStat", schCtStat);

		return MoLayoutUtil.getJspPath("/ct/surv/listSurv", "ct");
	}
	
	/** 설문참여 */
	@RequestMapping(value= "/ct/surv/viewSurv")
	public String viewSurv(HttpServletRequest request, ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		//String ctId = request.getParameter("ctId");
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		VoUtil.bind(request, ctsVo);
		
		//파일 추가
		//ctFileSvc.putFileListToModel(survId, model);
		
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
						
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			ctsQueVo.setCompId(ctsVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			@SuppressWarnings("unchecked")
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				ctQueExamVo.setCompId(ctsQVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				@SuppressWarnings("unchecked")
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
				for(CtQuesExamDVo ctQVo : ctQueExamList){
					returnWcQueExamList.add(ctQVo);
				}
				
				//단 라디오버튼일때는 각각 넣어줘야함 나중에 정리 필요함.
				CtSurvReplyDVo ctSurvReplyDVo = new CtSurvReplyDVo();
				ctSurvReplyDVo.setSurvId(survId);
				ctSurvReplyDVo.setReplyrUid(userVo.getUserUid());
				ctSurvReplyDVo.setQuesId(ctsQVo.getQuesId());
				@SuppressWarnings("unchecked")
				List<CtSurvReplyDVo> survReplyListAdd = (List<CtSurvReplyDVo>) commonDao.queryList(ctSurvReplyDVo);
				ctsQVo.setCtSurvReplyDVo(survReplyListAdd);
			}
			
			CtSurvReplyDVo ctSurvReplyDVo = new CtSurvReplyDVo();
			ctSurvReplyDVo.setSurvId(survId);
			ctSurvReplyDVo.setReplyrUid(userVo.getUserUid());
			@SuppressWarnings("unchecked")
			List<CtSurvReplyDVo> survReplyList = (List<CtSurvReplyDVo>) commonDao.queryList(ctSurvReplyDVo);

			model.put("survReplyList", survReplyList);
			model.put("ctQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
		}

		return MoLayoutUtil.getJspPath("/ct/surv/viewSurv", "ct");
	}
	
	/** 통계보기 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/ct/surv/viewSurvRes")
	public String viewSurvRes(HttpServletRequest request, ModelMap model) throws Exception {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		String survId = request.getParameter("survId");
		String repetYn = request.getParameter("repetYn");
		//String ctId = request.getParameter("ctId");
		
		//ctFileSvc.putFileListToModel(survId, model);
		
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
		
		ctsReVo.setSurvId(survId);
		ctsReVo.setReplyrUid(userVo.getUserUid());
		
		String langTypCd = LoginSession.getLangTypCd(request);
		
		CtSurvBVo ctsVo =  new CtSurvBVo();
		ctsVo.setSurvId(survId);
		ctsVo.setQueryLang(langTypCd);
		VoUtil.bind(request, ctsVo);
		//설문기본
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		if(ctsVo != null){
			
			//설문질문상세
			CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
			ctsQueVo.setSurvId(ctsVo.getSurvId());
			ctsQueVo.setCompId(ctsVo.getCompId());
			ctsQueVo.setOrderBy("QUES_SORT_ORDR");
			
			List<CtSurvQuesDVo> ctsQueList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
			
			List<CtQuesExamDVo> returnWcQueExamList = new ArrayList<CtQuesExamDVo>();
			
			for(CtSurvQuesDVo ctsQVo : ctsQueList){
				//질문보기상세
				CtQuesExamDVo ctQueExamVo =  new CtQuesExamDVo();
				ctQueExamVo.setSurvId(ctsQVo.getSurvId());
				ctQueExamVo.setCompId(ctsQVo.getCompId());
				ctQueExamVo.setQuesId(ctsQVo.getQuesId());
				
				List<CtQuesExamDVo> ctQueExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQueExamVo);
					for(CtQuesExamDVo ctQVo : ctQueExamList){
						
						//질문전체 count
						CtSurvReplyDVo ctReAllCount = new CtSurvReplyDVo();
						ctReAllCount.setCompId(ctQVo.getCompId());
						ctReAllCount.setSurvId(ctQVo.getSurvId());
						ctReAllCount.setQuesId(ctQVo.getQuesId());
						float quesAllCount = commonDao.count(ctReAllCount)!=null?commonDao.count(ctReAllCount):0;

						//질문보기 개당 count
						CtSurvReplyDVo ctReOneCount = new CtSurvReplyDVo();
						ctReOneCount.setCompId(ctQVo.getCompId());
						ctReOneCount.setSurvId(ctQVo.getSurvId());
						ctReOneCount.setQuesId(ctQVo.getQuesId());
						ctReOneCount.setReplyNo(ctQVo.getExamNo());
						//질문보기상세 선택된 개수 count 구하기
						float quesOneCount = commonDao.count(ctReOneCount)!=null?commonDao.count(ctReOneCount):0;
						
						//평균 구하기
						float average = (quesOneCount / quesAllCount) * 100;
						
						ctQVo.setSelectCount(String.valueOf((int)quesOneCount));
						ctQVo.setQuesAverage(String.valueOf((int)average));
						returnWcQueExamList.add(ctQVo);
					}
					
					//주관식 질문  count
					CtSurvReplyDVo ctReOendCount = new CtSurvReplyDVo();
					ctReOendCount.setCompId(ctsQVo.getCompId());
					ctReOendCount.setSurvId(ctsQVo.getSurvId());
					ctReOendCount.setQuesId(ctsQVo.getQuesId());
					ctReOendCount.setReplyNo(-1);
					float quesOendCount = commonDao.count(ctReOendCount)!=null?commonDao.count(ctReOendCount):0;
					
					ctsQVo.setOendCount(String.valueOf((int)quesOendCount));
			}

			model.put("returnWcQueExamList", returnWcQueExamList);
			model.put("ctsQueList", ctsQueList);
			model.put("ctsVo", ctsVo);	
			model.put("repetYn", repetYn);
			model.put("logUserUid", userVo.getUserUid());
		}
		
		return MoLayoutUtil.getJspPath("/ct/surv/viewSurvRes", "ct");
	}
	
	/** 설문참여 저장*/
	@RequestMapping(value= "/ct/surv/viewSurvSave")
	public String viewSurvSave(HttpServletRequest request, ModelMap model) throws Exception {
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request); 
		String survId = request.getParameter("survId");
		String ctId = ParamUtil.getRequestParam(request, "ctId", true);

		CtEstbBVo ctEstbBVo = new CtEstbBVo();
		ctEstbBVo.setCtId(ctId);
		
		ctEstbBVo =  (CtEstbBVo) commonDao.queryVo(ctEstbBVo);
		if(ctEstbBVo==null){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		
		CtSurvBVo ctsVo = new CtSurvBVo(); 
		ctsVo.setSurvId(survId);
		ctsVo.setCtId(ctId);
		ctsVo.setCompId(ctEstbBVo.getCompId());
		ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
		
		if(ctsVo != null){
			//재설문일시 사용사 답변상세 삭제 후 재 입력
			if(ctsVo.getRepetSurvYn().equalsIgnoreCase("Y")){
				CtSurvReplyDVo ctsReVo = new  CtSurvReplyDVo();
				ctsReVo.setSurvId(survId);
				ctsReVo.setReplyrUid(userVo.getUserUid());
				ctsReVo.setCompId(ctEstbBVo.getCompId());
				queryQueue.delete(ctsReVo);
			}
		}
		
		CtSurvQuesDVo ctsQueVo =  new CtSurvQuesDVo();
		ctsQueVo.setSurvId(survId);
		@SuppressWarnings("unchecked")
		List<CtSurvQuesDVo> returnQuesVo =  (List<CtSurvQuesDVo>) commonDao.queryList(ctsQueVo);
		
  		for(CtSurvQuesDVo ctsQVo : returnQuesVo){
			CtSurvReplyDVo ctsRVo = new CtSurvReplyDVo();
			
			
			ctsRVo.setCompId(ctsQVo.getCompId());
			ctsRVo.setQuesId(ctsQVo.getQuesId());
			ctsRVo.setSurvId(ctsQVo.getSurvId());
			ctsRVo.setReplyDt(ctSurvSvc.currentDay());
			ctsRVo.setReplyrUid(userVo.getUserUid());
			ctsRVo.setReplyrDeptId(userVo.getDeptId());
			
			//객관식
			if(ctsQVo.getMulChoiYn() != null){
				if(ctsQVo.getMulChoiYn().equalsIgnoreCase("Y")){
					String[] survQues = request.getParameterValues(ctsQVo.getQuesId());
					String[] survQuesInput = request.getParameterValues("inputCheck"+ctsQVo.getQuesId());
					if(survQues != null){
						for(int i=0; i < survQues.length; i++){
							CtSurvReplyDVo cloneCtSurv =(CtSurvReplyDVo) ctsRVo.clone();
							cloneCtSurv.setReplyNo(Integer.parseInt(survQues[i]));
							if(survQuesInput != null){
									//입력여부Y이면 객관식에 text 가능함.
									cloneCtSurv.setMulcInputReplyCont(survQuesInput[Integer.parseInt(survQues[i])-1]);
							}
							queryQueue.insert(cloneCtSurv);
						}
					}
				}else if(ctsQVo.getMulChoiYn().equalsIgnoreCase("N")){
					String survQues = request.getParameter(ctsQVo.getQuesId());
					String survQuesInput = request.getParameter("inputRadio"+ ctsQVo.getQuesId() + survQues);
					
					if(survQues != null){
						ctsRVo.setReplyNo(Integer.parseInt(survQues));
						if(survQuesInput != null){
							//입력여부Y이면 객관식에 text 가능함.
							ctsRVo.setMulcInputReplyCont(survQuesInput);
						}
						queryQueue.insert(ctsRVo);
					}
				}
			//주관식
			}else{
				String survQues = request.getParameter(ctsQVo.getQuesId());
				
				if(!survQues.isEmpty()){
					ctsRVo.setReplyNo(-1);
					ctsRVo.setOendReplyCont(survQues);
					queryQueue.insert(ctsRVo);
				}
			}
			
		}
		
		commonDao.execute(queryQueue);		
		
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		model.put("todo", "$m.nav.prev(event, '/ct/surv/listSurv.do?menuId="+request.getParameter("menuId")
				+ "&ctId=" + ctId
				+"');");
		
		//공통 처리 페이지
		return MoLayoutUtil.getResultJsp();
	}
	
	/** 주관식 답변보기*/
	@RequestMapping(value= "/ct/surv/listOendAnsPop")
	public String listOendAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String quesCount = request.getParameter("quesCount");
		request.setAttribute("pageRowCnt", 10);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
		ctsQueVo.setQueryLang(langTypCd);
		//ctsQueVo.setCompId(userVo.getCompId());
		ctsQueVo.setQuesId(quesId);
		ctsQueVo.setSurvId(survId);

		ctsQueVo = (CtSurvQuesDVo) commonDao.queryVo(ctsQueVo);
		
		// 조회조건 매핑
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
		
		
		ctsReVo.setQueryLang(langTypCd);
		//ctsReVo.setCompId(userVo.getCompId());
		ctsReVo.setSurvId(survId);
		ctsReVo.setQuesId(quesId);
		ctsReVo.setReplyNo(-1);
		ctsReVo.setOrderBy("REPLY_DT DESC");
		VoUtil.bind(request, ctsReVo);
		
		Map<String,Object> rsltMap = ctSurvSvc.getOendReplyCont(request, ctsReVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("ctsQueVo", ctsQueVo);
		model.put("quesCount", quesCount);

		return MoLayoutUtil.getJspPath("/ct/surv/listOendAnsPop");
	}
	
	/** 객관식입력항목 답변보기*/
	@RequestMapping(value= "/ct/surv/listMulcAnsPop")
	public String listMulcAnsPop(HttpServletRequest request, ModelMap model) throws Exception {
		String survId = request.getParameter("survId");
		String quesId = request.getParameter("quesId");
		String replyNo = request.getParameter("replyNo");
		request.setAttribute("pageRowCnt", 10);//RowCnt 삽입
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		CtSurvQuesDVo ctsQueVo = new CtSurvQuesDVo();
		ctsQueVo.setQueryLang(langTypCd);
		//ctsQueVo.setCompId(userVo.getCompId());
		ctsQueVo.setQuesId(quesId);
		ctsQueVo.setSurvId(survId);

		ctsQueVo = (CtSurvQuesDVo) commonDao.queryVo(ctsQueVo);
		
		// 조회조건 매핑
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
//		VoUtil.bind(request, ctSurvBVo);
		
		ctsReVo.setQueryLang(langTypCd);
		//ctsReVo.setCompId(userVo.getCompId());
		ctsReVo.setSurvId(survId);
		ctsReVo.setQuesId(quesId);
		ctsReVo.setReplyNo(Integer.parseInt(replyNo));
		ctsReVo.setMulcInputReplyContYn("Y");
		ctsReVo.setOrderBy("REPLY_DT DESC");
		Map<String,Object> rsltMap = ctSurvSvc.getOendReplyCont(request, ctsReVo);
		model.put("recodeCount", rsltMap.get("recodeCount"));
		model.put("ctSurvBMapList", rsltMap.get("ctSurvBMapList"));
		model.put("ctsQueVo", ctsQueVo);
		
		return MoLayoutUtil.getJspPath("/ct/surv/listMulcAnsPop");
	}
	
}
