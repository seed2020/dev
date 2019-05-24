package com.innobiz.orange.web.wo.ctrl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.ap.vo.ApErpIntgBVo;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wo.svc.WoCmSvc;
import com.innobiz.orange.web.wo.vo.WoOnecApvLnDVo;
import com.innobiz.orange.web.wo.vo.WoOnecBVo;
import com.innobiz.orange.web.wo.vo.WoOnecCdDVo;
import com.innobiz.orange.web.wo.vo.WoOnecHisLVo;
import com.innobiz.orange.web.wo.vo.WoOnecPichDVo;

/*
승인요청된 내용을 공유된 사람이 보는지 여부 > 승인된 것만
반려시 처리방안 - 이력으로 남기는지 ? > 버림
Drop 처리 - 상신하는 건지  >> 관리자가 Frequence 를 Drop 으로, 상신안함
Category - item 내의 항목 Item/Project 인지 ?
*/

@Controller
public class WoOnecCtrl {

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	@Autowired
	private WoCmSvc woCmSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** [원카드 목록] - [list:원카드 목록, temp:임시저장함, askApv:승인대기함, admList:원카드관리] */
	@RequestMapping(value = "/wo/listOnec")
	public String listOnec(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		boolean valid = true;
		WoOnecBVo woOnecBVo = new WoOnecBVo();
		VoUtil.bind(request, woOnecBVo);
		
		if("list".equals(cat)){
			woOnecBVo.setUserUid(userVo.getUserUid());
		} else if("modify".equals(cat)){
			woOnecBVo.setHoldrUid(userVo.getUserUid());
			woOnecBVo.setStatCdList(ArrayUtil.toList(new String[]{ "temp", "modify", "askApv", "cncl", "rejt" }, true));
			woOnecBVo.setHistory();
		} else if("all".equals(cat)){
			
		} else {
			valid = false;
		}
		
		if(valid){
			
			Integer recodeCount = commonSvc.count(woOnecBVo);
			PersonalUtil.setPaging(request, woOnecBVo, recodeCount);
			model.put("recodeCount", recodeCount);
			
			@SuppressWarnings("unchecked")
			List<WoOnecBVo> woOnecBVoList = (List<WoOnecBVo>)commonSvc.queryList(woOnecBVo);
			if(woOnecBVoList != null && !woOnecBVoList.isEmpty()){
				model.put("woOnecBVoList", woOnecBVoList);
			}
			
			if("list".equals(cat)){
				woOnecBVo = new WoOnecBVo();
				woOnecBVo.setHoldrUid(userVo.getUserUid());
				setFreqDt(woOnecBVo);
				@SuppressWarnings("unchecked")
				List<WoOnecBVo> freqList = (List<WoOnecBVo>)commonSvc.queryList(woOnecBVo);
				if(freqList != null){
					model.put("freqList", freqList);
				}
			}
		}
		
		// 코드 조회
		woCmSvc.setCdList(model);
		
		// 이번달 세팅
		if("list".equals(cat)){
			model.put("thisMonth", StringUtil.getCurrYmd().substring(0, 7));
		}
		
		return LayoutUtil.getJspPath("/wo/listOnec");
	}
	
	/** 당월보고 세팅용 */
	private void setFreqDt(WoOnecBVo woOnecBVo){
		GregorianCalendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		woOnecBVo.setQuarterDt(toYmd(year, month-2));
		woOnecBVo.setHalfDt(toYmd(year, month-5));
		woOnecBVo.setYearDt(toYmd(year, month-11));
	}
	
	private String toYmd(int year, int month){
		while(month < 1){
			month += 12;
			year--;
		}
		while(month > 12){
			month -= 12;
			year++;
		}
		return year+"-"+(month<10 ? "0" : "")+month+"-01";
	}
	
	/** 원카드 - 등록/수정/조회 */
	@RequestMapping(value = {"/wo/setOnec", "/wo/viewOnec", "/wo/self/viewOnec"})
	public String setOnec(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "onecNo", required = false) String onecNo,
			@RequestParam(value = "ver", required = false) String ver,
			@RequestParam(value = "compId", required = false) String compId,		// html 생성용 - 결재
			@RequestParam(value = "langTypCd", required = false) String langTypCd,	// html 생성용 - 결재
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		String uri = request.getRequestURI();
		boolean isView = uri.indexOf("/viewOnec")>-1;
		boolean isHtml = uri.indexOf("/self/viewOnec")>-1;
		boolean isHolder = false;
		
		boolean fromHisTable = !isView || (ver!=null && !ver.isEmpty()) || "modify".equals(cat);
		
		if(onecNo!=null && !onecNo.isEmpty()){
			
			// 버전 조회
			WoOnecBVo verVo = new WoOnecBVo();
			verVo.setOnecNo(onecNo);
			verVo.setInstanceQueryId("com.innobiz.orange.web.wo.dao.WoOnecBDao.selectWoOnecBVer");
			@SuppressWarnings("unchecked")
			List<WoOnecBVo> verVoList = (List<WoOnecBVo>)commonSvc.queryList(verVo);
			if(verVoList!=null && !verVoList.isEmpty()){
				model.put("verVoList", verVoList);
				if(ver==null || ver.isEmpty()){
					ver = verVoList.get(0).getVer();
				}
			} else {
				//wo.msg.noCard=해당하는 원카드가 없습니다.
				model.put("message", messageProperties.getMessage("wo.msg.noCard", request));
				model.put("togo", "/wo/listOnec.do?cat="+cat+"&menuId="+menuId);
				return LayoutUtil.getResultJsp();
			}
			
			// 원카드기본(WO_ONEC_B) 테이블 - 조회
			WoOnecBVo woOnecBVo = new WoOnecBVo();
			woOnecBVo.setOnecNo(onecNo);
			if(fromHisTable){
				woOnecBVo.setVer(ver);
				woOnecBVo.setHistory();
			}
			woOnecBVo = (WoOnecBVo)commonSvc.queryVo(woOnecBVo);
			if(woOnecBVo != null){
				
				isHolder = isHtml ? false : userVo.getUserUid().equals(woOnecBVo.getHoldrUid());
				// 홀더가 아닐때
				if(!isHolder){
					// 수정모드 거나, 이력테이블의 결재끝난것이 아니면
					if(!isHtml && 
							(!isView || (fromHisTable && !"apvd".equals(woOnecBVo.getStatCd())))
							){
						// 권한 없음
						request.getRequestDispatcher(LayoutUtil.getErrorJsp(403)).forward(request,response);
						return null;
					}
					// 홀더가 아닐때, 처음 버전이 결재난것이 아니면 - 제거
					if(!"apvd".equals(verVoList.get(0).getStatCd())){
						verVoList.remove(0);
					}
				} else {
					model.put("lastVer", verVoList.get(0).getVer());
					if(!isHtml && "all".equals(cat) && !"apvd".equals(verVoList.get(0).getStatCd())){
						verVoList.remove(0);
					}
				}
				model.put("woOnecBVo", woOnecBVo);
				
				
				if(woOnecBVo.getDeptId()!=null && !woOnecBVo.getDeptId().isEmpty()){
					OrOrgTreeVo treeVo = orCmSvc.getOrgTreeVo(woOnecBVo.getDeptId(), isHtml ? langTypCd : userVo.getLangTypCd());
					model.put("deptVo", treeVo);
				}
				
				// safe code - 버전을 동일하게
				if(!fromHisTable){
					ver = woOnecBVo.getVer();
				}
				
				if(isView && !"1".equals(ver)){
					WoOnecBVo prevVo = new WoOnecBVo();
					prevVo.setOnecNo(onecNo);
					prevVo.setVer(Integer.toString(Integer.parseInt(ver)-1));
					prevVo.setHistory();
					prevVo = (WoOnecBVo)commonSvc.queryVo(prevVo);
					if(prevVo != null){
						model.put("prevVo", prevVo);
					}
				}
				
				
			} else {
				//wo.msg.noCard=해당하는 원카드가 없습니다.
				model.put("message", messageProperties.getMessage("wo.msg.noCard", request));
				model.put("togo", "/wo/listOnec.do?cat="+cat+"&menuId="+menuId);
				return LayoutUtil.getResultJsp();
			}
			
			// 히스토리
			WoOnecHisLVo woOnecHisLVo = new WoOnecHisLVo();
			woOnecHisLVo.setOnecNo(onecNo);
			woOnecHisLVo.setOrderBy("HIS_REG_DT DESC, HIS_NO DESC");
			@SuppressWarnings("unchecked")
			List<WoOnecHisLVo> woOnecHisLVoList = (List<WoOnecHisLVo>)commonSvc.queryList(woOnecHisLVo);
			if(woOnecHisLVoList!=null && !woOnecHisLVoList.isEmpty()){
				model.put("woOnecHisLVoList", woOnecHisLVoList);
			}
			
			// 담당자 조회
			WoOnecPichDVo woOnecPichDVo = new WoOnecPichDVo();
			woOnecPichDVo.setOnecNo(onecNo);
			woOnecHisLVo.setOrderBy("PICH_TYP_CD, SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<WoOnecPichDVo> woOnecPichDVoList = (List<WoOnecPichDVo>)commonSvc.queryList(woOnecPichDVo);
			if(woOnecPichDVoList != null && !woOnecPichDVoList.isEmpty()){
				String oldPichTypCd = null, pichTypCd;
				List<WoOnecPichDVo> list = null;
				for(WoOnecPichDVo vo : woOnecPichDVoList){
					pichTypCd = vo.getPichTypCd();
					if(oldPichTypCd==null || !oldPichTypCd.equals(pichTypCd)){
						list = new ArrayList<WoOnecPichDVo>();
						model.put("pichTypCd"+pichTypCd+"List", list);
						oldPichTypCd = pichTypCd;
					}
					list.add(vo);
				}
			}
			
			// 결재선 정보
			if(isView && !isHtml && onecNo!=null && !onecNo.isEmpty() && ver!=null && !ver.isEmpty()){
				WoOnecApvLnDVo woOnecApvLnDVo = new WoOnecApvLnDVo();
				woOnecApvLnDVo.setOnecNo(onecNo);
				woOnecApvLnDVo.setApvLnPno("0");
				woOnecApvLnDVo.setVer(ver);
				@SuppressWarnings("unchecked")
				List<WoOnecApvLnDVo> woOnecApvLnDVoList = (List<WoOnecApvLnDVo>)commonSvc.queryList(woOnecApvLnDVo);
				if(woOnecApvLnDVoList!=null && !woOnecApvLnDVoList.isEmpty()){
					model.put("woOnecApvLnDVoList", woOnecApvLnDVoList);
				}
			}
			
			// 첨부파일 세팅
			woCmSvc.putFileListToModel(onecNo, model, isHtml ? compId : userVo.getCompId());
			
		}
		
		// 코드 조회
		woCmSvc.setCdList(model);
		
		if(isHtml){
			model.put("menuId", menuId);
			model.put("viewMode", Boolean.TRUE);
			model.put("htmlMode", Boolean.TRUE);
			return "/wo/viewOnecInc";
		} else if(isView){
			model.put("viewMode", Boolean.TRUE);
			return LayoutUtil.getJspPath("/wo/viewOnec");
		} else {
			return LayoutUtil.getJspPath("/wo/setOnec");
		}
	}
	
	/** 원카드 - 히스토리 팝업 */
	@RequestMapping(value = "/wo/setOnecHisPop")
	public String setOnecHisPop(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {
		return LayoutUtil.getJspPath("/wo/setOnecHisPop");
	}
	
	/** 원카드 - 저장 */
	@RequestMapping(value = "/wo/transOnec")
	public String transOnec(HttpServletRequest request,
			@RequestParam(value = "menuId", required = false) String menuId,
			Locale locale, ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		UploadHandler uploadHandler = null;
		String cat=null, onecNo=null, statCd=null;
		
		boolean isTemp = false, isAskApv=false, isModify=false;
		boolean isHis = false;
		Integer ver = 1;
		try {
			
			uploadHandler = uploadManager.createHandler(request, "temp", "wo");
			uploadHandler.upload(); // 업로드 파일 정보
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			cat = request.getParameter("cat");
			onecNo = request.getParameter("onecNo");
			statCd = request.getParameter("statCd");
			
			isTemp = "temp".equals(statCd);
			isAskApv = "askApv".equals(statCd);
			isModify = "modify".equals(statCd);
			isHis = isTemp || isAskApv || isModify;
			
			// 원카드기본(WO_ONEC_B) 테이블
			WoOnecBVo woOnecBVo = new WoOnecBVo();
			WoOnecBVo oldVo = null;
			VoUtil.bind(request, woOnecBVo);
			
			boolean isNew = onecNo==null || onecNo.isEmpty();
			
			if(isNew){
				
				onecNo = commonSvc.nextVal("WO_ONEC_B").toString();
				woOnecBVo.setOnecNo(onecNo);
				woOnecBVo.setVer(ver.toString());
				if(isHis) woOnecBVo.setHistory();
				woOnecBVo.setRegDt("sysdate");
				woOnecBVo.setHoldrUid(userVo.getUserUid());
				queryQueue.insert(woOnecBVo);
				
			} else {
				
				WoOnecBVo chkVo = new WoOnecBVo();
				chkVo = new WoOnecBVo();
				chkVo.setOnecNo(onecNo);
				chkVo.setHistory();
				chkVo.setOrderBy("VER DESC");
				@SuppressWarnings("unchecked")
				List<WoOnecBVo> verList = (List<WoOnecBVo>)commonSvc.queryList(chkVo);
				if(verList==null || verList.isEmpty()){
					//wo.msg.noCard=해당하는 원카드가 없습니다.
					model.put("message", messageProperties.getMessage("wo.msg.noCard", request));
					return LayoutUtil.getResultJsp();
				} else {
					chkVo = verList.get(0);
				}
				
				ver = Integer.valueOf(chkVo.getVer()).intValue();
				if(isHis && "apvd".equals(chkVo.getStatCd())){
					ver = ver.intValue() + 1;
					
					// 완료 버전의 현 버전과 상태 업데이트
					WoOnecBVo apvdVo = new WoOnecBVo();
					apvdVo.setOnecNo(onecNo);
					apvdVo.setCurVer(ver.toString());
					apvdVo.setStatCd("modify");
					queryQueue.update(apvdVo);
				}
				
				// 이전 저장 조회 - 히스토리 작업용
				oldVo = new WoOnecBVo();
				oldVo.setOnecNo(onecNo);
				oldVo = (WoOnecBVo)commonSvc.queryVo(oldVo);
				
				if(isHis){
					
					WoOnecBVo delVo = new WoOnecBVo();
					delVo.setOnecNo(onecNo);
					delVo.setVer(ver.toString());
					delVo.setHistory();
					queryQueue.delete(delVo);
					
					woOnecBVo.setOnecNo(onecNo);
					woOnecBVo.setVer(ver.toString());
					woOnecBVo.setHistory();
					woOnecBVo.setRegDt("sysdate");
					woOnecBVo.setHoldrUid(userVo.getUserUid());
					if(isAskApv){
						woOnecBVo.setStatCd(oldVo==null ? "temp" : "modify");
					}
					if(oldVo!=null){
						woOnecBVo.setApvdHisNo(oldVo.getApvdHisNo());
						//woOnecBVo.setApvCmplDt(woOnecBVo.getApvCmplDt());
					}
					queryQueue.insert(woOnecBVo);
					
					if(oldVo != null){
						WoOnecBVo modVo = new WoOnecBVo();
						modVo.setOnecNo(onecNo);
						modVo.setStatCd("modify");
						queryQueue.update(modVo);
					}
					
				} else {
					woOnecBVo.setOnecNo(onecNo);
					queryQueue.update(woOnecBVo);
				}
			}
			
			Integer currHisNo = 0;
			WoOnecHisLVo woOnecHisLVo;
			if(!isNew){
				// 컨펌 전의 히스토리 삭제
				woOnecHisLVo = new WoOnecHisLVo();
				if(oldVo!=null && oldVo.getApvdHisNo()!=null && !oldVo.getApvdHisNo().isEmpty()){
					currHisNo = Integer.valueOf(oldVo.getApvdHisNo());
					if(currHisNo.intValue() > 0){
						woOnecHisLVo.setMinHisNo(oldVo.getApvdHisNo());
					}
				}
				queryQueue.delete(woOnecHisLVo);
			}
			
			// 파라미터 히스토리 insert
			String[] hisRegDts = request.getParameterValues("hisRegDt");
			String[] hisConts = request.getParameterValues("hisCont");
			if(hisRegDts != null){
				
				List<WoOnecHisLVo> hisList = new ArrayList<WoOnecHisLVo>();
				for(int i=0; i<hisRegDts.length && i<hisConts.length; i++){
					if(hisRegDts[i]!=null && !hisRegDts[i].isEmpty()
							&& hisConts[i]!=null && !hisConts[i].isEmpty()){
						
						woOnecHisLVo = new WoOnecHisLVo();
						woOnecHisLVo.setOnecNo(onecNo);
						woOnecHisLVo.setHisRegDt(hisRegDts[i]);
						woOnecHisLVo.setHisCont(hisConts[i]);
						hisList.add(woOnecHisLVo);
					}
				}
				
				if(!hisList.isEmpty()){
					// sort
					Collections.sort(hisList, new Comparator<WoOnecHisLVo>() {
			            @Override
			            public int compare(WoOnecHisLVo h1, WoOnecHisLVo h2) {
			            	return h1.getHisRegDt().compareTo(h2.getHisRegDt());
			            }
			        });
					
					for(WoOnecHisLVo vo : hisList){
						currHisNo++;
						vo.setHisNo(currHisNo.toString());
						queryQueue.insert(vo);
					}
				}
			}
			
			// 담당자
			String uids;
			WoOnecPichDVo woOnecPichDVo;
			for(String cd : new String[]{ "R","A","C","I","S","QC" }){
				uids = request.getParameter("pichTypCd"+cd);
				if(uids!=null){
					woOnecPichDVo = new WoOnecPichDVo();
					woOnecPichDVo.setOnecNo(onecNo);
					woOnecPichDVo.setPichTypCd(cd);
					queryQueue.delete(woOnecPichDVo);
					
					if(!uids.isEmpty()){
						for(String uid : uids.split("\\,")){
							woOnecPichDVo = new WoOnecPichDVo();
							woOnecPichDVo.setOnecNo(onecNo);
							woOnecPichDVo.setPichTypCd(cd);
							woOnecPichDVo.setUserUid(uid);
							queryQueue.insert(woOnecPichDVo);
						}
					}
				}
			}
			
			
			// 첨부파일
			woCmSvc.saveAttachFiles(request, queryQueue, onecNo);
			
			// 일괄 SQL 실행
			commonSvc.execute(queryQueue);
			
			if(isAskApv){
				
				// 서버 설정 목록 조회
				Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				
				String domain = ServerConfig.IS_LOC ? "127.0.0.1:8080" : svrEnvMap.get("webDomain");
				String port = "Y".equals(sysPlocMap.get("portForwarding")) ? ":60080" : "";
				String url = "http://"+domain+port+"/wo/self/viewOnec.do?cat=modify&onecNo="+onecNo+"&compId="+userVo.getCompId()+"&langTypCd="+userVo.getLangTypCd()+"&menuId="+menuId;
				
				HttpClient client = new HttpClient();
				String html = client.sendGet(url, "UTF-8");
				
				String formId = request.getParameter("formId");
				
				Map<String, String> refMap = new HashMap<String, String>();
				refMap.put("onecNo", woOnecBVo.getOnecNo());
				refMap.put("ver", woOnecBVo.getVer());
				String refVa = JsonUtil.toJson(refMap);
				
				// ERP연계기본(AP_ERP_INTG_B) 테이블
				ApErpIntgBVo apErpIntgBVo = new ApErpIntgBVo();
				apErpIntgBVo.setFormId(formId);
				apErpIntgBVo.setIntgStatCd("req");
				apErpIntgBVo.setDocSubj("[One Card] "+woOnecBVo.getItemNm());
				apErpIntgBVo.setRegDt("sysdate");
				apErpIntgBVo.setIntgTypCd("ERP_ONECARD");
//				apErpIntgBVo.setFormNm(fnm);
				apErpIntgBVo.setRefVa(refVa);
				apErpIntgBVo.setBodyHtml(html);
				
				// 연계번호 최대값 조회
				ApErpIntgBVo maxApErpIntgBVo = new ApErpIntgBVo();
				maxApErpIntgBVo.setInstanceQueryId("com.innobiz.orange.web.ap.dao.ApErpIntgBDao.selectMaxApErpIntgB");
				
				Long intgNo = commonSvc.queryLong(maxApErpIntgBVo);
				apErpIntgBVo.setIntgNo(intgNo.toString());
				
				commonSvc.insert(apErpIntgBVo);
				
				String bxId = "myBx";
				String redirectUrl = "/ap/box/setDoc.do?bxId="+bxId + "&intgNo="+intgNo;
				redirectUrl = ptSecuSvc.toAuthMenuUrl(userVo, redirectUrl, "/ap/box/listApvBx.do?bxId="+bxId);
				
				model.put("todo", "parent.location.href=\""+redirectUrl+"\";");
				return LayoutUtil.getResultJsp();
			}
			
			if("temp".equals(statCd)){
				// ap.trans.submitOk={0} 하였습니다.
				model.put("message", messageProperties.getMessage("ap.trans.submitOk", new String[]{ "#wo.statCd."+statCd }, request));
				cat = "modify";
			} else if("modify".equals(statCd)){
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			}
			
			model.put("todo", "parent.location.replace('/wo/listOnec.do?cat="+cat+"&menuId="+request.getParameter("menuId")+"');");
			
		} catch(Exception e){
			String message = e.getMessage();
			e.printStackTrace();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
		}
		return LayoutUtil.getResultJsp();
	}
	
	/** [Ajax 프로세스] - del:삭제, changeHoldr:홀더변경  */
	@RequestMapping(value = "/wo/transProcessActAjx")
	public String transProcessActAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {

		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String onecNo = (String)jsonObject.get("onecNo");
			String ver = (String)jsonObject.get("ver");
			String actCd = (String)jsonObject.get("actCd");
			String message = null, messageId=null;
			
			WoOnecBVo storedVo = null;
			
			if(!"changeHoldr".equals(actCd)){
				
				storedVo = new WoOnecBVo();
				storedVo.setOnecNo(onecNo);
				if(ver!=null && !ver.isEmpty()){
					storedVo.setVer(ver);
					storedVo.setHistory();
				}
				storedVo = (WoOnecBVo)commonSvc.queryVo(storedVo);
				
				if(storedVo == null){
					//wo.msg.noCard=해당하는 원카드가 없습니다.
					model.put("message", messageProperties.getMessage("wo.msg.noCard", request));
					return JsonUtil.returnJson(model);
				}
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 삭제 - 버전 삭제
			if("del".equals(actCd)){
				
				if("apvd".equals(storedVo.getStatCd()) || "askApv".equals(storedVo.getStatCd())){
					// wp.msg.cannotDel=삭제 할 수 없는 상태입니다.
					model.put("message", messageProperties.getMessage("wp.msg.cannotDel", request));
					return JsonUtil.returnJson(model);
					
//					// wp.msg.notTheStep={0} 처리 단계가 아닙니다.
//					model.put("message", messageProperties.getMessage("wp.msg.notTheStep", new String[]{ "#cm.btn.del" }, request));
//					return JsonUtil.returnJson(model);
				}
				
				// 이력 삭제
				WoOnecBVo woOnecBVo = new WoOnecBVo();
				woOnecBVo.setOnecNo(onecNo);
				woOnecBVo.setVer(ver);
				woOnecBVo.setHistory();
				queryQueue.delete(woOnecBVo);
				
				// 현재 상태 - 반려 ..등 > 결재 완료
				woOnecBVo = new WoOnecBVo();
				woOnecBVo.setOnecNo(onecNo);
				woOnecBVo.setStatCd("apvd");
				queryQueue.update(woOnecBVo);
				
				// 추가된 히스토리 - 삭제
				WoOnecHisLVo woOnecHisLVo = new WoOnecHisLVo();
				woOnecHisLVo.setOnecNo(onecNo);
				if(storedVo.getApvdHisNo()!=null && !storedVo.getApvdHisNo().isEmpty()){
					woOnecHisLVo.setMinHisNo(storedVo.getApvdHisNo());
				}
				queryQueue.delete(woOnecHisLVo);
				
				// cm.msg.del.success=삭제 되었습니다.
				messageId = "cm.msg.del.success";
				message = messageProperties.getMessage("cm.msg.del.success", request);
				
			// 홀더 변경
			} else if("changeHoldr".equals(actCd)){
				
				String oldHoldrUid = (String)jsonObject.get("oldHoldrUid");
				String holdrUid = (String)jsonObject.get("holdrUid");
				
				// 이력 테이블 변경
				WoOnecBVo woOnecBVo = new WoOnecBVo();
				if(onecNo!=null && !onecNo.isEmpty()){
					woOnecBVo.setOnecNo(onecNo);
				}
				woOnecBVo.setHoldrUid(holdrUid);
				woOnecBVo.setOldHoldrUid(oldHoldrUid);
				woOnecBVo.setHistory();
				queryQueue.update(woOnecBVo);
				
				// 본 테이블 변경
				woOnecBVo = new WoOnecBVo();
				if(onecNo!=null && !onecNo.isEmpty()){
					woOnecBVo.setOnecNo(onecNo);
				}
				woOnecBVo.setHoldrUid(holdrUid);
				woOnecBVo.setOldHoldrUid(oldHoldrUid);
				queryQueue.update(woOnecBVo);
				
				// cm.msg.save.success=저장 되었습니다.
				messageId = "cm.msg.save.success";
			}
			
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
			}
			
			if(message != null){
				model.put("message", message);
			} else if(messageId != null){
				model.put("message", messageProperties.getMessage(messageId, request));
			}
			// ap.trans.processOk={0} 처리 하였습니다.
			//model.put("message", messageProperties.getMessage("ap.trans.processOk", new String[]{ "#wp.modStatCd."+actCd }, request));
			model.put("result", "ok");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 환경설정 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wo/settings")
	public String settings(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			ModelMap model, Locale locale) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		WoOnecCdDVo woOnecCdDVo = new WoOnecCdDVo();
		woOnecCdDVo.setOrderBy("CLS_CD, SORT_ORDR");
		List<WoOnecCdDVo> woOnecCdDVoList = (List<WoOnecCdDVo>)commonSvc.queryList(woOnecCdDVo);
		
		List<WoOnecCdDVo> cdList = null;
		if(woOnecCdDVoList !=null && !woOnecCdDVoList.isEmpty()){
			
			Map<String, WoOnecCdDVo> sysCdMap = new HashMap<String, WoOnecCdDVo>();
			model.put("sysCdMap", sysCdMap);
			
			String clsCd, oldClsCd = null;
			for(WoOnecCdDVo vo : woOnecCdDVoList){
				clsCd = vo.getClsCd();
				if(oldClsCd==null || !oldClsCd.equals(clsCd)){
					cdList = new ArrayList<WoOnecCdDVo>();
					model.put(clsCd+"List", cdList);
					oldClsCd = clsCd;
				}
				cdList.add(vo);
				if("SYS".equals(clsCd)){
					sysCdMap.put(vo.getCd(), vo);
				}
			}
		}
		
		String[] clsCds = new String[]{"ONEC_TYP_CD", "CAT_CD", "ORGN_CD", "STORG_CD"};
		model.put("clsCds", clsCds);
		for(String clsCd : clsCds){
			
			cdList = (List<WoOnecCdDVo>)model.get(clsCd+"List");
			if(cdList==null){
				cdList = new ArrayList<WoOnecCdDVo>();
				model.put(clsCd+"List", cdList);
			}
			// 화면 구성용
			cdList.add(new WoOnecCdDVo());
			cdList.add(new WoOnecCdDVo());
		}
		
		// ONEC_ADM:원카드 관리자, ONEC_VWR:원카드 전체 조회자
		List<OrUserBVo> admList = woCmSvc.getUserListByRoleCd(userVo.getCompId(), "ONEC_ADM", userVo.getLangTypCd());
		if(admList != null) model.put("onecAdmList", admList);
		List<OrUserBVo> vwrList = woCmSvc.getUserListByRoleCd(userVo.getCompId(), "ONEC_VWR", userVo.getLangTypCd());
		if(vwrList != null) model.put("onecVwrList", vwrList);
		
		return LayoutUtil.getJspPath("/wo/settings");
	}
	
	/** 원카드 설정 - 저장(권한) */
	@RequestMapping(value = "/wo/transAuths")
	public String transAuths(HttpServletRequest request,
			@RequestParam(value = "cat", required = false) String cat,
			Locale locale, ModelMap model) throws Exception {
		
		String admUids = request.getParameter("admUids");
		String vwrUids = request.getParameter("vwrUids");
		
		QueryQueue queryQueue = new QueryQueue();
		
		// ONEC_ADM:원카드 관리자, ONEC_VWR:원카드 전체 조회자
		OrUserRoleRVo orUserRoleRVo = new OrUserRoleRVo();
		orUserRoleRVo.setRoleCd("ONEC_ADM");
		queryQueue.delete(orUserRoleRVo);
		
		orUserRoleRVo = new OrUserRoleRVo();
		orUserRoleRVo.setRoleCd("ONEC_VWR");
		queryQueue.delete(orUserRoleRVo);
		
		if(admUids!=null && !admUids.isEmpty()){
			for(String uid : admUids.split("\\,")){
				orUserRoleRVo = new OrUserRoleRVo();
				orUserRoleRVo.setUserUid(uid);
				orUserRoleRVo.setRoleCd("ONEC_ADM");
				queryQueue.insert(orUserRoleRVo);
			}
		}
		
		if(vwrUids!=null && !vwrUids.isEmpty()){
			for(String uid : vwrUids.split("\\,")){
				orUserRoleRVo = new OrUserRoleRVo();
				orUserRoleRVo.setUserUid(uid);
				orUserRoleRVo.setRoleCd("ONEC_VWR");
				queryQueue.insert(orUserRoleRVo);
			}
		}
		
		commonSvc.execute(queryQueue);
		
		// cm.msg.save.success=저장 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		
		return LayoutUtil.getResultJsp();
		
	}
	
	
	/** 원카드 설정 - 저장(코드) */
	@RequestMapping(value = "/wo/transSettings")
	public String transSettings(HttpServletRequest request,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "clsCd", required = false) String clsCd,
			Locale locale, ModelMap model) throws Exception {
		
		WoOnecCdDVo woOnecCdDVo = new WoOnecCdDVo();
		woOnecCdDVo.setClsCd(clsCd);
		@SuppressWarnings("unchecked")
		List<WoOnecCdDVo> oldList = (List<WoOnecCdDVo>)commonSvc.queryList(woOnecCdDVo);
		
		List<String> delCdList = new ArrayList<String>();
		if(oldList != null){
			for(WoOnecCdDVo vo : oldList){
				delCdList.add(vo.getCd());
			}
		}
		
		QueryQueue queryQueue = new QueryQueue();
		
		woOnecCdDVo = new WoOnecCdDVo();
		woOnecCdDVo.setClsCd(clsCd);
		queryQueue.delete(woOnecCdDVo);
		
		String[] cds = request.getParameterValues("cd");
		String[] cdVas = request.getParameterValues("cdVa");
		String[] useYns = request.getParameterValues("useYn");
		String[] notes = request.getParameterValues("note");
		
		Integer sortOrdr = 1;
		for(int i=0; i<cds.length; i++){
			
			if(cds[i]!=null && !(cds[i] = cds[i].trim()).isEmpty()
					&& cdVas[i]!=null && !(cdVas[i] = cdVas[i].trim()).isEmpty()
					){
				
				delCdList.remove(cds[i]);
				woOnecCdDVo = new WoOnecCdDVo();
				woOnecCdDVo.setClsCd(clsCd);
				woOnecCdDVo.setCd(cds[i]);
				woOnecCdDVo.setCdVa(cdVas[i]);
				woOnecCdDVo.setUseYn(useYns[i]);
				woOnecCdDVo.setNote(notes[i]);
				woOnecCdDVo.setSortOrdr(sortOrdr.toString());
				sortOrdr++;
				
				queryQueue.insert(woOnecCdDVo);
			}
			
		}
		
		if(!delCdList.isEmpty()){
			WoOnecBVo ckVo;
			boolean valid;
			for(String delCd : delCdList){
				
				valid = true;
				ckVo = new WoOnecBVo();
				
				if("ONEC_TYP_CD".equals(clsCd)){
					ckVo.setOnecTypCd(delCd);
				} else if("CAT_CD".equals(clsCd)){
					ckVo.setCatCd(delCd);
				} else if("ORGN_CD".equals(clsCd)){
					ckVo.setOrgnCd(delCd);
				} else if("STORG_CD".equals(clsCd)){
					ckVo.setStorgCd(delCd);
				} else {
					valid = false;
				}
				if(valid){
					ckVo.setHistory();
					if(commonSvc.count(ckVo) > 0){
						//pt.msg.not.del.cd.inUse=사용중인 코드는 삭제 할 수 없습니다.
						String message = messageProperties.getMessage("pt.msg.not.del.cd.inUse", request)+ " : "+delCd;
						model.put("message", message);
						return LayoutUtil.getResultJsp();
					}
				}
			}
		}
		
		commonSvc.execute(queryQueue);
		
		// cm.msg.save.success=저장 되었습니다.
		model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		
		return LayoutUtil.getResultJsp();
	}

	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = "/wo/downFile")
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
			ModelAndView mv = woCmSvc.getFileList(request , fileIds , actionParam);
			return mv;
			
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
}
