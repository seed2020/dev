package com.innobiz.orange.web.ct.svc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.ct.vo.CtFileDVo;
import com.innobiz.orange.web.ct.vo.CtFncDVo;
import com.innobiz.orange.web.ct.vo.CtQuesExamDVo;
import com.innobiz.orange.web.ct.vo.CtSurvBVo;
import com.innobiz.orange.web.ct.vo.CtSurvQuesDVo;
import com.innobiz.orange.web.ct.vo.CtSurvReplyDVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wc.svc.WcScdManagerSvc;
//import com.innobiz.orange.web.ct.vo.CtSurvUseAuthDVo;
@Service
public class CtSurvSvc {
	
	/** 테스트 중_rakoos */
	//private static final Logger LOGGER = Logger.getLogger(CtSurvSvc.class);
	
	/** 설문 공통 서비스 */
	@Autowired
	private CtCmSvc ctCmSvc;
	
	/** 설문 공통 서비스 */
	@Autowired
	private CtCmntSvc ctCmntSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
//	/** 첨부파일 서비스 */
//	@Autowired
//	private CtFileSvc ctFileSvc;
	
	@SuppressWarnings("unchecked")
	public List<CtSurvBVo> getSurvSurvey() {
		List<CtSurvBVo> survey = new ArrayList<CtSurvBVo>();
		CtSurvBVo surv = new CtSurvBVo();
		try{
			survey = (List<CtSurvBVo>) commonDao.queryList(surv);
		}catch(Exception e){
			e.printStackTrace();
		}
		return survey;
	}
	
	public CtSurvBVo oneSelectList(CtSurvBVo ctSurvBVo) throws Exception {
		return (CtSurvBVo) commonDao.queryVo(ctSurvBVo);
	}
	
	public int setCtSurvList(CtSurvBVo ctSurvBVo) throws Exception {
		return commonDao.insert(ctSurvBVo);
	}
	
	@SuppressWarnings("unchecked")
	/** 설문 목록 */
	public List<CtSurvBVo> getCtSurvList(CtSurvBVo ctSurvBVo ) throws Exception {
		return (List<CtSurvBVo>)commonDao.queryList(ctSurvBVo);
	}
	
	/** 설문 목록수 */
	public Integer getCtSurvListCnt(CtSurvBVo ctSurvBVo) throws Exception {
		return commonDao.count(ctSurvBVo);
	}
	
	/** 답변 목록 */
	@SuppressWarnings("unchecked")
	public List<CtSurvReplyDVo> getCtSurvReplyList(CtSurvReplyDVo ctsReVo) throws Exception {
		return (List<CtSurvReplyDVo>)commonDao.queryList(ctsReVo);
	}
	
	/** 답변 목록수 */
	public Integer getCtSurvReplyListCnt(CtSurvReplyDVo ctsReVo) throws Exception {
		return commonDao.count(ctsReVo);
	}
	
	/** 질문 목록수 */
	public Integer getsetCtSurvQuesCnt(CtSurvQuesDVo ctSurvQuesDVo) throws Exception {
		return commonDao.count(ctSurvQuesDVo);
	}
	
//	/** 권한 목록 */
//	public List<CtSurvUseAuthDVo> getCtSurvAuthList(CtSurvUseAuthDVo ctsAuthVo) throws Exception {
//		return (List<CtSurvUseAuthDVo>)commonDao.queryList(ctsAuthVo);
//	}
	
	
	
	/** 설문 목록 조회 */
	public Map<String,Object> getCtSurvMapList(HttpServletRequest request , CtSurvBVo ctSurvBVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//String schWord = ctSurvBVo.getSchWord();//검색어
		Integer recodeCount = this.getCtSurvListCnt(ctSurvBVo);
		PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
		
		//목록 조회
		List<CtSurvBVo> ctSurvBVoList = this.getCtSurvList(ctSurvBVo);
		QueryQueue queryQueue = new QueryQueue();

		
		Map<String, Object> ctSurvBInfoMap;
		List<Map<String, Object>> ctSurvBMapList = new ArrayList<Map<String, Object>>();
		for(CtSurvBVo storedCtSurvBVo : ctSurvBVoList){
			//권한 조회
//			CtSurvUseAuthDVo useAuthDVo = new CtSurvUseAuthDVo();
//			useAuthDVo.setSurvId(storedCtSurvBVo.getSurvId());
//			List<CtSurvUseAuthDVo> useAuthVoList = (List<CtSurvUseAuthDVo>) commonDao.queryList(useAuthDVo);
//			storedCtSurvBVo.setSurvAuthList(useAuthVoList);
			
			//오늘 날짜에 따른 상태권한 세팅
			Calendar startCal=WcScdManagerSvc.stringConvertCal(storedCtSurvBVo.getSurvStartDt(),true);
			Calendar endCal=WcScdManagerSvc.stringConvertCal(storedCtSurvBVo.getSurvEndDt(),true);
			Date curDate = new Date();
			Calendar currentCal = Calendar.getInstance();
			currentCal.setTime(curDate);
			CtSurvBVo ctSurvStat = new CtSurvBVo();
			ctSurvStat.setCompId(storedCtSurvBVo.getCompId());
			ctSurvStat.setSurvId(storedCtSurvBVo.getSurvId());
			
			if(storedCtSurvBVo.getSurvPrgStatCd().equals("3")){
				if(startCal.after(currentCal)){
					storedCtSurvBVo.setSurvPrgStatCd("1");
					ctSurvStat.setSurvPrgStatCd(storedCtSurvBVo.getSurvPrgStatCd());
					queryQueue.update(ctSurvStat);
				}else{
					if(endCal.before(currentCal)){
						storedCtSurvBVo.setSurvPrgStatCd("4");
						ctSurvStat.setSurvPrgStatCd(storedCtSurvBVo.getSurvPrgStatCd());
						queryQueue.update(ctSurvStat);
					}else{
						storedCtSurvBVo.setSurvPrgStatCd("3");
					}
				}
			}
			ctSurvBInfoMap = VoUtil.toMap(storedCtSurvBVo, null);
			ctSurvBMapList.add(ctSurvBInfoMap);
		}
		if(queryQueue.size() != 0){
			commonDao.execute(queryQueue);
		}
		rsltMap.put("ctSurvBMapList", ctSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	

	public void setCtSurvList(HttpServletRequest request, CtSurvBVo ctSurvBVo,
			QueryQueue queryQueue, UserVo userVo, ModelMap model, String fnc, String ctId,
			String fncUid)throws Exception{
		//Integer recodeCount = this.getCtSurvListCnt(ctSurvBVo)+1;
		//recodeCount value -> String형으로 변환
		//String setSurvCount = String.valueOf(recodeCount);
		//String fncId = null;
		// 현재 시간(우리나라)
	
		String mTime = StringUtil.getCurrDateTime();
		String survId = request.getParameter("survId");
		ctSurvBVo.setSubj(request.getParameter("subj"));
		ctSurvBVo.setSurvStartDt(request.getParameter("strtDt") + " 23:59:59");
		ctSurvBVo.setSurvEndDt(request.getParameter("finDt") + " 23:59:59");
		ctSurvBVo.setRegDt(mTime);
		ctSurvBVo.setRepetSurvYn(request.getParameter("resurvPosbYn"));
		
		
		if(request.getParameterValues("tgt") != null){
			String[] tgt = request.getParameterValues("tgt");
			String survTgt = "";
			
			for(int i=0; i < tgt.length; i++){
				survTgt += tgt[i]+"/";
			}
			
			ctSurvBVo.setSurvTgtCd(survTgt.substring(0, survTgt.length()-1 ).trim());
		}
		
		//커뮤니티 Id저장
		ctSurvBVo.setCtId(ctId);
		
		ctSurvBVo.setSurvItnt(request.getParameter("editor1"));
		ctSurvBVo.setSurvFtr(request.getParameter("editor2"));
		
		//Fnc기능 저장
		CtFncDVo  ctFncDVo = ctCmSvc.getCtFncDVo(userVo, fncUid, ctId);
		ctSurvBVo.setCtFncId(ctFncDVo.getCtFncId());
		ctSurvBVo.setCtFncOrdr(ctFncDVo.getCtFncOrdr());
		ctSurvBVo.setCtFncLocStep(ctFncDVo.getCtFncLocStep());
		ctSurvBVo.setCtFncUid(ctFncDVo.getCtFncUid());
		ctSurvBVo.setCtFncPid(ctFncDVo.getCtFncPid());
		
		if( fnc.equalsIgnoreCase("reg") 
				|| survId.isEmpty() || survId == null){
			
			ctSurvBVo.setSurvId(ctCmSvc.createId("CT_SURV_B"));
			//미저장
			ctSurvBVo.setSurvPrgStatCd("5");
			//수정자
			ctSurvBVo.setModrUid(userVo.getUserUid());
			//수정시간
			ctSurvBVo.setModDt(mTime);
			
			
			
			queryQueue.insert(ctSurvBVo);
			
		}else if(fnc.equalsIgnoreCase("mod")
				&& survId != null
				&& !survId.equalsIgnoreCase("")){
			ctSurvBVo.setSurvId(survId);
			//수정자
			ctSurvBVo.setModrUid(userVo.getUserUid());
			//수정시간
			ctSurvBVo.setModDt(mTime);
			queryQueue.update(ctSurvBVo);
			
		}
		
		//refId 때문에 마지막에 처리해야하 합니다.
		//fncUid, fncId 넣어야한다.
//		ctFileSvc.saveSurvFile(request, ctSurvBVo.getSurvId(), queryQueue, fncUid , ctFncDVo.getCtFncId());
//		int fileSaveSize=queryQueue.size();
//		ctSurvBVo.setAttYn(fileSaveSize>0?"Y":"N");
		
		model.put("ctSurvBVo", ctSurvBVo);
		 
	}

		
	
	//오늘날짜구하기
	public String currentDay(){
		return StringUtil.getCurrDateTime();
	}
	
	
	/** 주관식 답변 조회 */
	public Map<String,Object> getOendReplyCont(HttpServletRequest request , CtSurvReplyDVo ctsReVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		
		Integer recodeCount = this.getCtSurvReplyListCnt(ctsReVo);
		PersonalUtil.setPaging(request, ctsReVo, recodeCount);
		
		//목록 조회
		List<CtSurvReplyDVo> ctSurvReplyVoList = this.getCtSurvReplyList(ctsReVo);
		
		Map<String, Object> ctSurvBInfoMap;
		List<Map<String, Object>> ctSurvBMapList = new ArrayList<Map<String, Object>>();
		for(CtSurvReplyDVo storedCtSurvBVo : ctSurvReplyVoList){
			ctSurvBInfoMap = VoUtil.toMap(storedCtSurvBVo, null);
			ctSurvBMapList.add(ctSurvBInfoMap);
		}
		rsltMap.put("ctSurvBMapList", ctSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	
	/** 신청중인 설문 목록 조회 */
	public Map<String,Object> getSurvApvdMapList(HttpServletRequest request , CtSurvBVo ctSurvBVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		String langTypCd = LoginSession.getLangTypCd(request);
		//String schWord = ctSurvBVo.getSchWord();//검색어
		
		Integer recodeCount = this.getCtSurvListCnt(ctSurvBVo);
		PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
		
		//목록 조회
		List<CtSurvBVo> ctSurvBVoList = this.getCtSurvList(ctSurvBVo);
		
		Map<String, Object> ctSurvBInfoMap;
		List<Map<String, Object>> ctSurvBMapList = new ArrayList<Map<String, Object>>();
		for(CtSurvBVo storedCtSurvBVo : ctSurvBVoList){
			
//			CtSurvBVo ctsVo =  new CtSurvBVo();
//			ctsVo.setSurvId(survId);
//			VoUtil.bind(request, ctsVo);
//			//설문기본
//			ctsVo = (CtSurvBVo) commonDao.queryVo(ctsVo);
//			if(ctsVo != null){
				Map<String, Object> agntMap =  orCmSvc.getUserMap(storedCtSurvBVo.getRegrUid(), langTypCd);
				if(agntMap==null) continue;
				//등록자명
				storedCtSurvBVo.setRegrNm((String) agntMap.get("userNm"));
//			}
			
			ctSurvBInfoMap = VoUtil.toMap(storedCtSurvBVo, null);
			ctSurvBMapList.add(ctSurvBInfoMap);
		}
		rsltMap.put("ctSurvBMapList", ctSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	
	/** 설문 목록 조회 관리자 */
	public Map<String,Object> getAdmSurvMapList(HttpServletRequest request , CtSurvBVo ctSurvBVo ) throws Exception {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		request.setCharacterEncoding("utf-8");
		//String schWord = ctSurvBVo.getSchWord();//검색어
		ctSurvBVo.setInstanceQueryId("countUserCtSurvB");
		Integer recodeCount = this.getCtSurvListCnt(ctSurvBVo);
		PersonalUtil.setPaging(request, ctSurvBVo, recodeCount);
		UserVo userVo = LoginSession.getUser(request);
		
		ctSurvBVo.setInstanceQueryId("selectUserCtSurvB");
		//목록 조회
		List<CtSurvBVo> ctSurvBVoList = this.getCtSurvList(ctSurvBVo);
		QueryQueue queryQueue = new QueryQueue();
		
		Map<String, Object> ctSurvBInfoMap;
		List<Map<String, Object>> ctSurvBMapList = new ArrayList<Map<String, Object>>();
		for(CtSurvBVo storedCtSurvBVo : ctSurvBVoList){
			
			Calendar startCal=WcScdManagerSvc.stringConvertCal(storedCtSurvBVo.getSurvStartDt(),true);
			Calendar endCal=WcScdManagerSvc.stringConvertCal(storedCtSurvBVo.getSurvEndDt(),false);
			Date curDate = new Date();
			Calendar currentCal = Calendar.getInstance();
			currentCal.setTime(curDate);
			CtSurvBVo ctSurvStat = new CtSurvBVo();
			ctSurvStat.setCompId(storedCtSurvBVo.getCompId());
			ctSurvStat.setSurvId(storedCtSurvBVo.getSurvId());
			
			//설문 진행 상태코드 값
			if(storedCtSurvBVo.getSurvPrgStatCd().equals("3")){
//				
				//오늘이 시작날짜보다 이 후면 진행중
				if(startCal.after(currentCal)){
					storedCtSurvBVo.setSurvPrgStatCd("1");
					ctSurvStat.setSurvPrgStatCd(storedCtSurvBVo.getSurvPrgStatCd());
					queryQueue.update(ctSurvStat);
				}else{
					if(endCal.before(currentCal)){
						storedCtSurvBVo.setSurvPrgStatCd("4");
						ctSurvStat.setSurvPrgStatCd(storedCtSurvBVo.getSurvPrgStatCd());
						queryQueue.update(ctSurvStat);
					}else{
						storedCtSurvBVo.setSurvPrgStatCd("3");
					}
				}
			}
			
			
			if(storedCtSurvBVo.getSurvTgtCd() != null){
				String[] tgtCd = storedCtSurvBVo.getSurvTgtCd().split("\\/");
				
					for(int i=0; i < tgtCd.length; i++){
					
						if(tgtCd[i].equalsIgnoreCase("M")) storedCtSurvBVo.setSurvTgtM(tgtCd[i]);
						if(tgtCd[i].equalsIgnoreCase("S")) storedCtSurvBVo.setSurvTgtS(tgtCd[i]);
						if(tgtCd[i].equalsIgnoreCase("R")) storedCtSurvBVo.setSurvTgtR(tgtCd[i]);
						if(tgtCd[i].equalsIgnoreCase("A")) storedCtSurvBVo.setSurvTgtA(tgtCd[i]);
					}
			}
			
			
			storedCtSurvBVo.setCtMyAuth(ctCmntSvc.getUserSeculCd(userVo.getCompId(), userVo.getUserUid(), storedCtSurvBVo.getCtId()));

			ctSurvBInfoMap = VoUtil.toMap(storedCtSurvBVo, null);
			ctSurvBMapList.add(ctSurvBInfoMap);
			
			
		}
		if(queryQueue.size() != 0){
			commonDao.execute(queryQueue);
		}
		rsltMap.put("ctSurvBMapList", ctSurvBMapList);
		rsltMap.put("recodeCount", recodeCount);
		
		return rsltMap;
	}
	
	/** 커뮤니티 설문 삭제 */
	@SuppressWarnings("unchecked")
	public void deleteSurv(String survId, String ctId ,QueryQueue queryQueue, HttpServletRequest request) throws Exception{
		/** 삭제순서 
		 * 1.설문답변상세 (WV_SURV_REPLY_D)삭제
		 * 2.설문파일상세 (WV_SURV_FILD_D)삭제
		 * 3.질문보기상세 (WV_QUES_EXAM_D)삭제
		 * 4.설문질문상세 (CT_SURV_QUES_D)삭제
		 * 5.설문기본  (WV_SURV_B)삭제
		 * 6.설문사용권한상세 (WV_SURV_USE_AUTH_D)삭제
		 * */

		//설문 유효성 검사
		if(survId == null || survId.isEmpty() || ctId == null || ctId.isEmpty()){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
			
		//해당 설문 내용을 모두 조회
		CtSurvBVo ctsThisVo =  new CtSurvBVo();
		ctsThisVo.setSurvId(survId);
		ctsThisVo.setCtId(ctId);
		ctsThisVo = (CtSurvBVo) commonDao.queryVo(ctsThisVo);
		
		//설문답변상세 삭제
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
		ctsReVo.setSurvId(ctsThisVo.getSurvId());
		List<CtSurvReplyDVo> ctsReList = (List<CtSurvReplyDVo>) commonDao.queryList(ctsReVo);
		if(ctsReList.size() != 0){
			for(CtSurvReplyDVo ctsRe :ctsReList){
				CtSurvReplyDVo ctsReDel = new CtSurvReplyDVo();
				ctsReDel.setSurvId(ctsRe.getSurvId());
				ctsReDel.setCompId(ctsRe.getCompId());
				ctsReDel.setQuesId(ctsRe.getQuesId());
				ctsReDel.setReplyNo(ctsRe.getReplyNo());
				//답변자삭제
				queryQueue.delete(ctsReDel);
			}
		}
		
		//질문보기상세 & 이미지 삭제
		CtQuesExamDVo ctQExamVo = new CtQuesExamDVo();
		ctQExamVo.setSurvId(ctsThisVo.getSurvId());
		
		List<CtQuesExamDVo> ctQExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQExamVo);
		if(ctQExamList.size() != 0){
			for(CtQuesExamDVo ctQE : ctQExamList){
				if(!ctQE.getImgSurvFileId().equalsIgnoreCase("") && ctQE.getImgSurvFileId() != null){
					CtFileDVo ctsFileVo = new CtFileDVo();
					if(ctQE.getImgSurvFileId() != null){
						if(!ctQE.getImgSurvFileId().equalsIgnoreCase("")){
							ctsFileVo.setFileId(Integer.parseInt(ctQE.getImgSurvFileId()));
							//질문보기상세관련 설문파일상세 File 삭제
							queryQueue.delete(ctsFileVo);
						}
					}
				}
				CtQuesExamDVo ctQExamDel = new CtQuesExamDVo();
				ctQExamDel.setSurvId(ctQE.getSurvId());
				ctQExamDel.setQuesId(ctQE.getQuesId());
				ctQExamDel.setCompId(ctQE.getCompId());
				ctQExamDel.setExamNo(ctQE.getExamNo());
				//질문보기상세 삭제
				queryQueue.delete(ctQExamDel);
			}
		}
		
		//설문질문상세 삭제
		CtSurvQuesDVo ctsQuesVo = new CtSurvQuesDVo();
		ctsQuesVo.setSurvId(ctsThisVo.getSurvId());
		
		List<CtSurvQuesDVo> ctsQuesList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQuesVo);
		if(ctsQuesList.size() != 0){
			for(CtSurvQuesDVo ctsQ: ctsQuesList){
				if(ctsQ.getImgSurvFileId() != null){
					if(!ctsQ.getImgSurvFileId().equalsIgnoreCase("")){
						CtFileDVo ctsFileVo = new CtFileDVo();
						ctsFileVo.setFileId(Integer.parseInt(ctsQ.getImgSurvFileId()));
						//설문질문상세관련 설문파일상세 File 삭제
						queryQueue.delete(ctsFileVo);
					}
				}
				CtSurvQuesDVo ctsQDel = new CtSurvQuesDVo();
				ctsQDel.setSurvId(ctsQ.getSurvId());
				ctsQDel.setQuesId(ctsQ.getQuesId());
				ctsQDel.setCompId(ctsQ.getCompId());
				
				//설문질문상세 삭제
				queryQueue.delete(ctsQDel);
			}
		}
		
		CtSurvBVo ctsBVo = new CtSurvBVo();
		ctsBVo.setSurvId(ctsThisVo.getSurvId());
		ctsBVo.setCtId(ctsThisVo.getCtId());
		//설문기본 삭제
		queryQueue.delete(ctsBVo);
			
	}

	/** 설문 관리자 삭제 */
	@SuppressWarnings("unchecked")
	public void deleteSurv(String survId, QueryQueue queryQueue, HttpServletRequest request) throws Exception{
	/** 삭제순서 
	 * 1.설문답변상세 (WV_SURV_REPLY_D)삭제
	 * 2.설문파일상세 (WV_SURV_FILD_D)삭제
	 * 3.질문보기상세 (WV_QUES_EXAM_D)삭제
	 * 4.설문질문상세 (CT_SURV_QUES_D)삭제
	 * 5.설문기본  (WV_SURV_B)삭제
	 * 6.설문사용권한상세 (WV_SURV_USE_AUTH_D)삭제
	 * */

	//설문 유효성 검사
	if(survId == null || survId.isEmpty()){
		// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
		throw new CmException("pt.msg.nodata.passed", request);
	}
		
		//해당 설문 내용을 모두 조회
		CtSurvBVo ctsThisVo =  new CtSurvBVo();
		ctsThisVo.setSurvId(survId);
		ctsThisVo = (CtSurvBVo) commonDao.queryVo(ctsThisVo);
		
		//설문답변상세 삭제
		CtSurvReplyDVo ctsReVo = new CtSurvReplyDVo();
		ctsReVo.setSurvId(ctsThisVo.getSurvId());
		List<CtSurvReplyDVo> ctsReList = (List<CtSurvReplyDVo>) commonDao.queryList(ctsReVo);
		if(ctsReList.size() != 0){
			for(CtSurvReplyDVo ctsRe :ctsReList){
				CtSurvReplyDVo ctsReDel = new CtSurvReplyDVo();
				ctsReDel.setSurvId(ctsRe.getSurvId());
				ctsReDel.setCompId(ctsRe.getCompId());
				ctsReDel.setQuesId(ctsRe.getQuesId());
				ctsReDel.setReplyNo(ctsRe.getReplyNo());
				//답변자삭제
				queryQueue.delete(ctsReDel);
			}
		}
		
		//질문보기상세 & 이미지 삭제
		CtQuesExamDVo ctQExamVo = new CtQuesExamDVo();
		ctQExamVo.setSurvId(ctsThisVo.getSurvId());
		
		List<CtQuesExamDVo> ctQExamList = (List<CtQuesExamDVo>) commonDao.queryList(ctQExamVo);
		if(ctQExamList.size() != 0){
			for(CtQuesExamDVo ctQE : ctQExamList){
				if(!ctQE.getImgSurvFileId().equalsIgnoreCase("") && ctQE.getImgSurvFileId() != null){
					CtFileDVo ctsFileVo = new CtFileDVo();
					if(ctQE.getImgSurvFileId() != null){
						if(!ctQE.getImgSurvFileId().equalsIgnoreCase("")){
							ctsFileVo.setFileId(Integer.parseInt(ctQE.getImgSurvFileId()));
							//질문보기상세관련 설문파일상세 File 삭제
							queryQueue.delete(ctsFileVo);
						}
					}
				}
				CtQuesExamDVo ctQExamDel = new CtQuesExamDVo();
				ctQExamDel.setSurvId(ctQE.getSurvId());
				ctQExamDel.setQuesId(ctQE.getQuesId());
				ctQExamDel.setCompId(ctQE.getCompId());
				ctQExamDel.setExamNo(ctQE.getExamNo());
				//질문보기상세 삭제
				queryQueue.delete(ctQExamDel);
			}
		}
		
		//설문질문상세 삭제
		CtSurvQuesDVo ctsQuesVo = new CtSurvQuesDVo();
		ctsQuesVo.setSurvId(ctsThisVo.getSurvId());
		
		List<CtSurvQuesDVo> ctsQuesList = (List<CtSurvQuesDVo>) commonDao.queryList(ctsQuesVo);
		if(ctsQuesList.size() != 0){
			for(CtSurvQuesDVo ctsQ: ctsQuesList){
				if(ctsQ.getImgSurvFileId() != null){
					if(!ctsQ.getImgSurvFileId().equalsIgnoreCase("")){
						CtFileDVo ctsFileVo = new CtFileDVo();
						ctsFileVo.setFileId(Integer.parseInt(ctsQ.getImgSurvFileId()));
						//설문질문상세관련 설문파일상세 File 삭제
						queryQueue.delete(ctsFileVo);
					}
				}
				CtSurvQuesDVo ctsQDel = new CtSurvQuesDVo();
				ctsQDel.setSurvId(ctsQ.getSurvId());
				ctsQDel.setQuesId(ctsQ.getQuesId());
				ctsQDel.setCompId(ctsQ.getCompId());
				
				//설문질문상세 삭제
				queryQueue.delete(ctsQDel);
			}
		}
		
		CtSurvBVo ctsBVo = new CtSurvBVo();
		ctsBVo.setSurvId(ctsThisVo.getSurvId());
		ctsBVo.setCtId(ctsThisVo.getCtId());
		//설문기본 삭제
		queryQueue.delete(ctsBVo);
			
	}
		
}
	
//	public List<CtSurvUseAuthDVo> getAuthList(UserVo userVo, CtSurvBVo ctsVo, String authTypCd, String authGrdCd ) throws Exception{
//		
//		CtSurvUseAuthDVo ctsVoteDeptAuth =  new CtSurvUseAuthDVo();
//		ctsVoteDeptAuth.setCompId(userVo.getCompId());
//		ctsVoteDeptAuth.setSurvId(ctsVo.getSurvId());
//		ctsVoteDeptAuth.setAuthTgtTypCd(authTypCd);
//		ctsVoteDeptAuth.setAuthGradCd(authGrdCd);
//		
//		return getCtSurvAuthList(ctsVoteDeptAuth);
//	}

