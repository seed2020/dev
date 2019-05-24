package com.innobiz.orange.web.wp.ctrl;

import java.util.ArrayList;
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

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.em.svc.EmAttachViewSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wp.svc.WpCmSvc;
import com.innobiz.orange.web.wp.util.Crc32Map;
import com.innobiz.orange.web.wp.vo.WpMpBVo;
import com.innobiz.orange.web.wp.vo.WpPrjAuthDVo;
import com.innobiz.orange.web.wp.vo.WpPrjBVo;
import com.innobiz.orange.web.wp.vo.WpPrjGrpBVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpPlanDVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpPlanLVo;
import com.innobiz.orange.web.wp.vo.WpPrjMpRsltDVo;
import com.innobiz.orange.web.wp.vo.WpPrjPichBVo;

/** 프로잭트 컨트롤러 */
@Controller
public class WpPrjCtrl {

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 프로잭트 관리 공통 서비스 */
	@Autowired
	private WpCmSvc wpCmSvc;
	
//	@Autowired
//	private PtSecuSvc ptSecuSvc;

	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;

//	/** 배포 매니저 */
//	@Resource(name = "distManager")
//	private DistManager distManager;
	
	/** 문서뷰어 서비스 */
	@Resource(name = "emAttachViewSvc")
	private EmAttachViewSvc emAttachViewSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/*
승인대기함 : apv
프로잭트 관리(전체) : allPrj
프로잭트별 집계 : prjMd

프로잭트 공수입력 : regMd
프로잭트 관리 : prj
임시저장함 : temp
승인요청함 : askApv
	 * */
	/** [프로잭트 목록] - [승인대기함, 프로잭트 관리(전체), 프로잭트별 집계, 프로잭트 공수입력, 프로잭트 관리, 임시저장함, 승인요청함] */
	@RequestMapping(value = "/wp/listPrj")
	public String listPrj(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "prjNm", required = false) String prjNm,
			@RequestParam(value = "grpId", required = false) String grpId,
			@RequestParam(value = "strtYmd", required = false) String strtYmd,
			@RequestParam(value = "endYmd", required = false) String endYmd,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		
		WpPrjBVo wpPrjBVo = new WpPrjBVo();
		
		// 프로잭트 관리(전체) : allPrj,   프로잭트별 집계 : prjMd
		if("allPrj".equals(cat) || "prjMd".equals(cat)){
			
		// 프로잭트 관리 : prj
		} else if("prj".equals(cat)){
			wpPrjBVo.setPmId(userVo.getUserUid());
			
		// 프로잭트 공수입력 : regMd
		} else if("regMd".equals(cat)){
			wpPrjBVo.setMpId(userVo.getUserUid());
			
		// 임시저장함 : temp
		} else if("temp".equals(cat)){
			wpPrjBVo.setRegUid(userVo.getUserUid());
			wpPrjBVo.setModStatCd("temp");
			wpPrjBVo.setHistory();
			
		// 승인대기함 : apv
		} else if("apv".equals(cat)){
			wpPrjBVo.setModStatCd("askApv");
			wpPrjBVo.setHistory();
			
		// 승인요청함 : askApv
		} else if("askApv".equals(cat)){
			// 승인요청, 반려, 요청취소
			wpPrjBVo.setModStatCdList(ArrayUtil.toList(new String[]{"askApv", "rejt","cancel"}, true));
			wpPrjBVo.setPmId(userVo.getUserUid());
			wpPrjBVo.setHistory();
			
		}
		if(prjNm!=null && !prjNm.isEmpty()){
			wpPrjBVo.setPrjNm(prjNm);
		}
		if(grpId!=null && !grpId.isEmpty()){
			wpPrjBVo.setGrpId(grpId);
		}
		if(strtYmd!=null && !strtYmd.isEmpty()){
			wpPrjBVo.setStrtYmd(strtYmd);
		}
		if(endYmd!=null && !endYmd.isEmpty()){
			wpPrjBVo.setEndYmd(endYmd);
		}
		
		
		Integer recodeCount = commonSvc.count(wpPrjBVo);
		PersonalUtil.setPaging(request, wpPrjBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		
		wpPrjBVo.setOrderBy("STRT_YMD DESC");
		@SuppressWarnings("unchecked")
		List<WpPrjBVo> wpPrjBVoList = (List<WpPrjBVo>)commonSvc.queryList(wpPrjBVo);
		if(wpPrjBVoList != null){
			model.put("wpPrjBVoList", wpPrjBVoList);
		}
		
		// 그룹기본(WP_GRP_B) 테이블
		WpPrjGrpBVo wpPrjGrpBVo = new WpPrjGrpBVo();
		wpPrjGrpBVo.setOrderBy("GRP_NM");
		@SuppressWarnings("unchecked")
		List<WpPrjGrpBVo> wpPrjGrpBVoList = (List<WpPrjGrpBVo>)commonSvc.queryList(wpPrjGrpBVo);
		if(wpPrjGrpBVoList!=null){
			model.put("wpPrjGrpBVoList", wpPrjGrpBVoList);
			Map<String, String> prjGrpMap = new Crc32Map<String, String>();
			for(WpPrjGrpBVo vo : wpPrjGrpBVoList){
				prjGrpMap.put(vo.getGrpId(), vo.getGrpNm());
			}
			model.put("prjGrpMap", prjGrpMap);
		}
		
		// 프로잭트별 집계 : prjMd
		if("prjMd".equals(cat) && wpPrjBVoList != null && !wpPrjBVoList.isEmpty()){
			List<String> prjNoList = new ArrayList<String>();
			for(WpPrjBVo vo : wpPrjBVoList){
				prjNoList.add(vo.getPrjNo());
			}
			
			// 계획 SUM 조회
			WpPrjMpPlanLVo wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
			wpPrjMpPlanLVo.setPrjNoList(prjNoList);
			wpPrjMpPlanLVo.setInstanceQueryId("com.innobiz.orange.web.wp.dao.WpPrjMpPlanLDao.selectPlanMmSumByPrjNo");
			@SuppressWarnings("unchecked")
			List<WpPrjMpPlanLVo> planMmList = (List<WpPrjMpPlanLVo>)commonSvc.queryList(wpPrjMpPlanLVo);
			if(planMmList != null && !planMmList.isEmpty()){
				Map<String, Float> planMmMap = new Crc32Map<String, Float>();
				for(WpPrjMpPlanLVo vo : planMmList){
					if(vo.getPlanMm()!=null && !vo.getPlanMm().isEmpty()){
						planMmMap.put(vo.getPrjNo(), Float.valueOf(vo.getPlanMm()));
					}
				}
				model.put("planMmMap", planMmMap);
			}
			
			// 실적 SUM 조회
			WpPrjMpRsltDVo wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
			wpPrjMpRsltDVo.setPrjNoList(prjNoList);
			wpPrjMpRsltDVo.setInstanceQueryId("com.innobiz.orange.web.wp.dao.WpPrjMpRsltDDao.selectRsltMmSumByPrjNo");
			@SuppressWarnings("unchecked")
			List<WpPrjMpRsltDVo> rsltMmList = (List<WpPrjMpRsltDVo>)commonSvc.queryList(wpPrjMpRsltDVo);
			if(rsltMmList != null && !rsltMmList.isEmpty()){
				Map<String, Float> rsltMmMap = new Crc32Map<String, Float>();
				for(WpPrjMpRsltDVo vo : rsltMmList){
					if(vo.getRsltMm()!=null && !vo.getRsltMm().isEmpty()){
						rsltMmMap.put(vo.getPrjNo(), Float.valueOf(vo.getRsltMm()));
					}
				}
				model.put("rsltMmMap", rsltMmMap);
			}
		}
		
		return LayoutUtil.getJspPath("/wp/listPrj");
	}
	
	/** [상세조회, 등록/수정] */
	@RequestMapping(value = {"/wp/setPrj", "/wp/viewPrj"})
	public String setPrj(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "cat", required = false) String cat,
			@RequestParam(value = "prjNo", required = false) String prjNo,
			@RequestParam(value = "ver", required = false) String ver,
			ModelMap model, Locale locale) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		boolean isView = request.getRequestURI().indexOf("/viewPrj") > 0;
		if(isView) model.put("viewMode", Boolean.TRUE);
		
		if(ver==null || ver.isEmpty()){
			if("temp".equals(cat)){
				ver = "1.0";
			}
		}
		if(!isView){
			if("allPrj".equals(cat)){
				String auth = (String)request.getAttribute("_AUTH");
				if(!"A".equals(auth) && !"SYS".equals(auth)){
					// 권한 없음 - 포워드
					request.getRequestDispatcher(LayoutUtil.getErrorJsp(403)).forward(request,response);
					return null;
				}
			}
		}
		
		if(prjNo!=null && !prjNo.isEmpty()){
			
			// 버전 조회 - apv:승인대기함, askApv:승인요청함, prj:프로잭트 관리, allPrj:프로잭트 관리(전체)
			if("apv".equals(cat) || "askApv".equals(cat) || "prj".equals(cat) || "allPrj".equals(cat)){
				WpPrjBVo wpPrjBVo = new WpPrjBVo();
				wpPrjBVo.setPrjNo(prjNo);
				wpPrjBVo.setHistory();
				wpPrjBVo.setOrderBy("VER DESC");
				@SuppressWarnings("unchecked")
				List<WpPrjBVo> verWpPrjBVoList = (List<WpPrjBVo>)commonSvc.queryList(wpPrjBVo);
				if(verWpPrjBVoList!=null && !verWpPrjBVoList.isEmpty()){
					
					// 승인대기함, 승인요청함     - 버전 조회
					if("apv".equals(cat) || "askApv".equals(cat)){
						model.put("verWpPrjBVoList", verWpPrjBVoList);
						// 버전이 없으면 마지막 버전
						if(ver==null || ver.isEmpty()){
							ver = verWpPrjBVoList.get(0).getVer();
						}
						
					// 프로잭트 관리, 프로잭트관리(전체)  - 버전 조회
					} else if("prj".equals(cat) || "allPrj".equals(cat)){
						// 승인된 버전만 추림
						List<WpPrjBVo> verList = new ArrayList<WpPrjBVo>();
						for(WpPrjBVo vo : verWpPrjBVoList){
							if("apvd".equals(vo.getModStatCd())){
								verList.add(vo);
							}
						}
						verWpPrjBVoList = verList;
						model.put("verWpPrjBVoList", verWpPrjBVoList);
						
						// 최종 버전이면 - 히스토리 테이블 아닌 본 테이블에서 조회 하도록
						if(!verWpPrjBVoList.isEmpty() && verWpPrjBVoList.get(0).getVer().equals(ver)){
							ver = null;
						}
					}
				}
			}
			
			// 프로잭트기본(WP_PRJ_B) 테이블
			WpPrjBVo wpPrjBVo = new WpPrjBVo();
			wpPrjBVo.setPrjNo(prjNo);
			if(ver!=null && !ver.isEmpty()){
				wpPrjBVo.setVer(ver);
				wpPrjBVo.setHistory();
			}
			wpPrjBVo = (WpPrjBVo)commonSvc.queryVo(wpPrjBVo);
			model.put("wpPrjBVo", wpPrjBVo);
			if(wpPrjBVo == null){
				if(ver!=null && !ver.isEmpty()){
					// wp.msg.noVer=해당하는 버전이 없습니다.
					model.put("message", messageProperties.getMessage("wp.msg.noVer", request));
				} else {
					// wp.msg.noPrj=해당하는 프로잭트가 없습니다.
					model.put("message", messageProperties.getMessage("wp.msg.noPrj", request));
				}
				model.put("togo", "/wp/listPrj.do?cat="+cat+"&menuId="+request.getAttribute("menuId"));
				return LayoutUtil.getResultJsp();
			}
			
			// 프로잭트담당자상세(WP_PRJ_PICH_B) 테이블
			WpPrjPichBVo wpPrjPichBVo = new WpPrjPichBVo();
			wpPrjPichBVo.setPrjNo(prjNo);
			wpPrjPichBVo.setOrderBy("SEQ");
			@SuppressWarnings("unchecked")
			List<WpPrjPichBVo> wpPrjPichBVoList = (List<WpPrjPichBVo>)commonSvc.queryList(wpPrjPichBVo);
			if(wpPrjPichBVoList == null) wpPrjPichBVoList = new ArrayList<WpPrjPichBVo>();
			if(!isView){
				for(int i = 3-wpPrjPichBVoList.size(); i>0; i--){
					wpPrjPichBVoList.add(new WpPrjPichBVo());//화면 구성용
				}
			}
			model.put("wpPrjPichBVoList", wpPrjPichBVoList);
			
			// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블
			WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
			wpPrjMpPlanDVo.setPrjNo(prjNo);
			if(ver!=null && !ver.isEmpty()){
				wpPrjMpPlanDVo.setVer(ver);
				wpPrjMpPlanDVo.setHistory();
			}
			wpPrjMpPlanDVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
			
			boolean hasOuts = false;
			String myRoleCd = null;
			List<String> conMpIdList = new ArrayList<String>();
			List<WpPrjMpPlanDVo> conWpPrjMpPlanDVoList = new ArrayList<WpPrjMpPlanDVo>();
			List<WpPrjMpPlanDVo> devWpPrjMpPlanDVoList = new ArrayList<WpPrjMpPlanDVo>();
			if(wpPrjMpPlanDVoList != null && !wpPrjMpPlanDVoList.isEmpty()){
				for(WpPrjMpPlanDVo vo : wpPrjMpPlanDVoList){
					if("con".equals(vo.getPrjRole1Cd())){
						conWpPrjMpPlanDVoList.add(vo);
						conMpIdList.add(vo.getMpId());
					} else {
						devWpPrjMpPlanDVoList.add(vo);
					}
					if(userVo.getUserUid().equals(vo.getMpId())){
						myRoleCd = vo.getPrjRole2Cd();
					}
					if(vo.getMpTypCd().equals("out")){
						hasOuts = true;
					}
				}
			}
			// 화면 구성용
			if(!isView){
				conWpPrjMpPlanDVoList.add(new WpPrjMpPlanDVo());
				devWpPrjMpPlanDVoList.add(new WpPrjMpPlanDVo());
			}
			model.put("conWpPrjMpPlanDVoList", conWpPrjMpPlanDVoList);
			model.put("devWpPrjMpPlanDVoList", devWpPrjMpPlanDVoList);
			
			// 권한 체크 필요 여부 - apv:승인대기함, allPrj:프로잭트 관리(전체), prjMd:프로잭트별 집계
			boolean needAuthCheck = !"apv".equals(cat) && !"allPrj".equals(cat) && !"prjMd".equals(cat);
			if(		(needAuthCheck && myRoleCd==null)
					// temp:임시저장함 & 등록자
					|| ("temp".equals(cat) && !userVo.getUserUid().equals(wpPrjBVo.getRegUid()))
					){
				// 권한 없음 - 포워드
				request.getRequestDispatcher(LayoutUtil.getErrorJsp(403)).forward(request,response);
				return null;
			}
			if(myRoleCd != null) model.put("myRoleCd", myRoleCd);
			
			if(hasOuts) model.put("hasOuts", Boolean.TRUE);
			
			// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블
			WpPrjMpPlanLVo wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
			wpPrjMpPlanLVo.setPrjNo(prjNo);
			if(ver!=null && !ver.isEmpty()){
				wpPrjMpPlanLVo.setVer(ver);
				wpPrjMpPlanLVo.setHistory();
			}
			wpPrjMpPlanLVo.setOrderBy("MP_ID, M_NO");
			@SuppressWarnings("unchecked")
			List<WpPrjMpPlanLVo> wpPrjMpPlanLVoList = (List<WpPrjMpPlanLVo>)commonSvc.queryList(wpPrjMpPlanLVo);
			
			int maxMCount = 0;
			if(wpPrjMpPlanLVoList!=null && !wpPrjMpPlanLVoList.isEmpty()){
				
				String mpId, oldMpId = null;
				List<WpPrjMpPlanLVo> voList = null;
				Map<String, List<WpPrjMpPlanLVo>> wpPrjMpPlanLVoListMap = new Crc32Map<String, List<WpPrjMpPlanLVo>>();
				for(WpPrjMpPlanLVo vo : wpPrjMpPlanLVoList){
					mpId = vo.getMpId();
					if(oldMpId == null || !oldMpId.equals(mpId)){
						if(voList!=null && maxMCount<voList.size()) maxMCount = voList.size();
						voList = new ArrayList<WpPrjMpPlanLVo>();
						wpPrjMpPlanLVoListMap.put(mpId, voList);
						oldMpId = mpId;
					}
					voList.add(vo);
				}
				if(voList!=null && maxMCount<voList.size()) maxMCount = voList.size();
				model.put("wpPrjMpPlanLVoListMap", wpPrjMpPlanLVoListMap);
				
				double[] conSum = new double[maxMCount+1];
				double[] devSum = new double[maxMCount+1];
				double[] allSum = new double[maxMCount+1];
				int month;
				for(WpPrjMpPlanLVo vo : wpPrjMpPlanLVoList){
					if(vo.getPlanMm()==null || vo.getPlanMm().isEmpty()) continue;
					month = Integer.parseInt(vo.getMNo());
					if(conMpIdList.contains(vo.getMpId())){
						conSum[month] += (Math.round(Double.parseDouble(vo.getPlanMm()) * 1000) / 1000.0);
					} else {
						devSum[month] += (Math.round(Double.parseDouble(vo.getPlanMm()) * 1000) / 1000.0);
					}
					allSum[month] += (Math.round(Double.parseDouble(vo.getPlanMm()) * 1000) / 1000.0);
				}
				
				for(int i=1;i<conSum.length;i++){
					conSum[0] += conSum[i];
					devSum[0] += devSum[i];
					allSum[0] += allSum[i];
				}
				
				conSum[0] = (Math.round(conSum[0] * 1000) / 1000.0);
				devSum[0] = (Math.round(devSum[0] * 1000) / 1000.0);
				allSum[0] = (Math.round(allSum[0] * 1000) / 1000.0);
				
				model.put("conSum", conSum);
				model.put("devSum", devSum);
				model.put("allSum", allSum);
			}
			model.put("maxMCount", (maxMCount==0 ? 12 : maxMCount));
			
			// 첨부파일 세팅
			wpCmSvc.putFileListToModel(prjNo, model, userVo.getCompId());
			
			if(isView){
				
				
				
			}
			
		} else {
			
			// 화면 구성용, 2칸 표시, 1칸 행 추가용 히든
			List<WpPrjPichBVo> wpPrjPichBVoList = new ArrayList<WpPrjPichBVo>();
			for(int i=0; i<3; i++){
				wpPrjPichBVoList.add(new WpPrjPichBVo());
			}
			model.put("wpPrjPichBVoList", wpPrjPichBVoList);
			
			
			List<WpPrjMpPlanDVo> conWpPrjMpPlanDVoList = new ArrayList<WpPrjMpPlanDVo>();
			List<WpPrjMpPlanDVo> devWpPrjMpPlanDVoList = new ArrayList<WpPrjMpPlanDVo>();
			
			// 자기 자신 "관리 PM" 으로
			WpPrjMpPlanDVo vo = new WpPrjMpPlanDVo();
			vo.setMpId(userVo.getUserUid());
			vo.setMpNm(userVo.getUserNm());
			vo.setMpTypCd("emp");
			vo.setPrjRole1Cd("con");
			vo.setPrjRole2Cd("gpm");
			conWpPrjMpPlanDVoList.add(vo);
			
			// 화면 구성용 - (임직원, 외주) 추가용 히든
			conWpPrjMpPlanDVoList.add(new WpPrjMpPlanDVo());
			devWpPrjMpPlanDVoList.add(new WpPrjMpPlanDVo());
			model.put("conWpPrjMpPlanDVoList", conWpPrjMpPlanDVoList);
			model.put("devWpPrjMpPlanDVoList", devWpPrjMpPlanDVoList);
			
			model.put("maxMCount", 12);
		}
		
		// 그룹기본(WP_GRP_B) 테이블
		WpPrjGrpBVo wpPrjGrpBVo = new WpPrjGrpBVo();
		wpPrjGrpBVo.setOrderBy("GRP_NM");
		@SuppressWarnings("unchecked")
		List<WpPrjGrpBVo> wpPrjGrpBVoList = (List<WpPrjGrpBVo>)commonSvc.queryList(wpPrjGrpBVo);
		if(wpPrjGrpBVoList!=null){
			model.put("wpPrjGrpBVoList", wpPrjGrpBVoList);
		}
		return LayoutUtil.getJspPath("/wp/setPrj");
	}
	
	/** 프로잭트 저장 */
	@RequestMapping(value = "/wp/transPrj")
	public String transPrj(HttpServletRequest request,
			Locale locale, ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		QueryQueue queryQueue = new QueryQueue();
		
		UploadHandler uploadHandler = null;
		String cat=null, prjNo=null, prjCd=null, modStatCd=null;
		
		boolean isTemp = false, isAskApv=false, adminEdit=false;
		boolean isHis = false;
		Float ver = (float)1;
		try {
			
			uploadHandler = uploadManager.createHandler(request, "temp", "wp");
			uploadHandler.upload(); // 업로드 파일 정보
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			cat = request.getParameter("cat");
			prjNo = request.getParameter("prjNo");
			prjCd = request.getParameter("prjCd");
			modStatCd = request.getParameter("modStatCd");
			
			isTemp = "temp".equals(modStatCd);
			isAskApv = "askApv".equals(modStatCd);
			isHis = isTemp || isAskApv;
			adminEdit = "allPrj".equals(cat) && userVo.getUserUid().equals("U0000001");
			
			// 프로잭트기본(WP_PRJ_B) 테이블
			WpPrjBVo wpPrjBVo = new WpPrjBVo();
			VoUtil.bind(request, wpPrjBVo);
			if(wpPrjBVo.getPrjAmt()!=null){
				wpPrjBVo.setPrjAmt(wpPrjBVo.getPrjAmt().replace(",",""));
			}
			
			boolean isNew = prjNo==null || prjNo.isEmpty();
			
			if(isNew){
				WpPrjBVo chkVo = new WpPrjBVo();
				chkVo.setPrjCd(prjCd);
				if(commonSvc.count(chkVo) > 0){
					// wp.msg.dupPrjCd=이미 등록된 프로잭트코드 입니다.
					model.put("message", messageProperties.getMessage("wp.msg.dupPrjCd", request));
					return LayoutUtil.getResultJsp();
				}
				
				prjNo = commonSvc.nextVal("WP_PRJ_B").toString();
				wpPrjBVo.setPrjNo(prjNo);
				wpPrjBVo.setVer(ver.toString());
				if(isHis) wpPrjBVo.setHistory();
				wpPrjBVo.setRegDt("sysdate");
				wpPrjBVo.setRegUid(userVo.getUserUid());
				queryQueue.insert(wpPrjBVo);
				
			} else {
				
				WpPrjBVo chkVo = new WpPrjBVo();
				chkVo.setPrjCd(prjCd);
				@SuppressWarnings("unchecked")
				List<WpPrjBVo> chkList = (List<WpPrjBVo>)commonSvc.queryList(chkVo);
				if(chkList==null || !chkList.isEmpty()){
					if((chkList.size()==1 && !prjNo.equals(chkList.get(0).getPrjNo())) || chkList.size()>1){
						// wp.msg.dupPrjCd=이미 등록된 프로잭트코드 입니다.
						model.put("message", messageProperties.getMessage("wp.msg.dupPrjCd", request));
						return LayoutUtil.getResultJsp();
					}
				}
				
				chkVo = new WpPrjBVo();
				chkVo.setPrjNo(prjNo);
				chkVo.setHistory();
				chkVo.setOrderBy("VER DESC");
				@SuppressWarnings("unchecked")
				List<WpPrjBVo> verList = (List<WpPrjBVo>)commonSvc.queryList(chkVo);
				if(verList==null || verList.isEmpty()){
					//wp.msg.noPrj=해당하는 프로잭트가 없습니다.
					model.put("message", messageProperties.getMessage("wp.msg.noPrj", request));
					return LayoutUtil.getResultJsp();
				} else {
					chkVo = verList.get(0);
				}
				
				ver = Double.valueOf(chkVo.getVer()).floatValue();
				if(isAskApv && "apvd".equals(chkVo.getModStatCd())){
					ver = (float)(ver.intValue() + 1);
				}
				
				if(isHis){
					
					WpPrjBVo delVo = new WpPrjBVo();
					delVo.setPrjNo(prjNo);
					delVo.setVer(ver.toString());
					delVo.setHistory();
					queryQueue.delete(delVo);
					
					wpPrjBVo.setPrjNo(prjNo);
					wpPrjBVo.setVer(ver.toString());
					wpPrjBVo.setHistory();
					wpPrjBVo.setRegDt("sysdate");
					wpPrjBVo.setRegUid(userVo.getUserUid());
					queryQueue.insert(wpPrjBVo);
					
				} else {
					wpPrjBVo.setPrjNo(prjNo);
					queryQueue.update(wpPrjBVo);
				}
			}
			
			// 프로잭트담당자상세(WP_PRJ_PICH_B) 테이블
			WpPrjPichBVo wpPrjPichBVo;
			String[] pichNms = request.getParameterValues("pichNm");
			String[] pichGrades = request.getParameterValues("pichGrade");
			String[] pichPhons = request.getParameterValues("pichPhon");
			String[] pichEmails = request.getParameterValues("pichEmail");
			String[] notes = request.getParameterValues("note");
			
			if(!isNew){
				wpPrjPichBVo = new WpPrjPichBVo();
				wpPrjPichBVo.setPrjNo(prjNo);
				queryQueue.delete(wpPrjPichBVo);
			}
			
			for(int i=0; i<(pichNms==null ? 0 : pichNms.length); i++){
				if(pichNms[i]==null || pichNms[i].isEmpty()) continue;
				
				wpPrjPichBVo = new WpPrjPichBVo();
				wpPrjPichBVo.setPrjNo(prjNo);
				wpPrjPichBVo.setSeq(Integer.toString(i+1));
				wpPrjPichBVo.setPichNm(pichNms[i]);
				wpPrjPichBVo.setPichGrade(pichGrades[i]);
				wpPrjPichBVo.setPichPhon(pichPhons[i]);
				wpPrjPichBVo.setPichEmail(pichEmails[i]);
				wpPrjPichBVo.setNote(notes[i]);
				queryQueue.insert(wpPrjPichBVo);
			}
			
			
			// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블
			WpPrjMpPlanDVo wpPrjMpPlanDVo;
			// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블
			WpPrjMpPlanLVo wpPrjMpPlanLVo;
			
			String[] mpIds = request.getParameterValues("mpId");
			String[] mpTypCds = request.getParameterValues("mpTypCd");
			String[] prjRole1Cds = request.getParameterValues("prjRole1Cd");
			String[] prjRole2Cds = request.getParameterValues("prjRole2Cd");
			String[] mpMmSums = request.getParameterValues("mpMmSum");
			
			
			// 제거된 인력 - 멘먼스 입력 되었는지 검사함
			if(!isNew){

				// 프로잭트인력결과상세(WP_PRJ_MP_RSLT_D) 테이블 - 조회  >> 결과가 입력된 사용자 제거 못함
				WpPrjMpRsltDVo wpPrjMpRsltDVo = new WpPrjMpRsltDVo();
				wpPrjMpRsltDVo.setPrjNo(prjNo);
				@SuppressWarnings("unchecked")
				List<WpPrjMpRsltDVo> wpPrjMpRsltDVoList = (List<WpPrjMpRsltDVo>)commonSvc.queryList(wpPrjMpRsltDVo);
				if(wpPrjMpRsltDVoList!=null && !wpPrjMpRsltDVoList.isEmpty()){
					int i, size = wpPrjMpRsltDVoList.size();
					boolean matched;
					// 파라미터 넘겨온 프로잭트 인원이 - 멘데이 입력한 인원에 있으면 제거함
					for(i=0;i<size;i++){
						wpPrjMpRsltDVo = wpPrjMpRsltDVoList.get(i);
						matched = false;
						for(String mpId : mpIds){
							if(mpId!=null && mpId.equals(wpPrjMpRsltDVo.getMpId())){
								matched = true;
								break;
							}
						}
						if(matched){
							wpPrjMpRsltDVoList.remove(i);
							i--;
							size--;
						}
					}
					
					// 제거안된 인원이 있으면 == 멘데이 입력했는데, 프로잭트에서 제거 되면
					if(!wpPrjMpRsltDVoList.isEmpty()){
						String mpId = wpPrjMpRsltDVoList.get(0).getMpId();
						String mpNm = null;
						if(mpId.startsWith("M")){
							WpMpBVo wpMpBVo = new WpMpBVo();
							wpMpBVo.setMpId(mpId);
							wpMpBVo = (WpMpBVo)commonSvc.queryVo(wpMpBVo);
							if(wpMpBVo != null) mpNm = wpMpBVo.getMpNm();
						} else {
							// 사용자기본(OR_USER_B) 테이블
							OrUserBVo orUserBVo = new OrUserBVo();
							if(mpId!=null) orUserBVo.setUserUid(mpId);
							orUserBVo.setQueryLang(userVo.getLangTypCd());
							orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
							if(orUserBVo != null) mpNm = orUserBVo.getRescNm();
						}
						
						//wp.msg.cannotDelWithData=투입 공수가 입력된 투입 인력은 삭제 할 수 없습니다.
						String msg = messageProperties.getMessage("wp.msg.cannotDelWithData", request);
						
						model.put("message", msg + (mpNm==null ? "" : " : "+mpNm));
						return LayoutUtil.getResultJsp();
					}
				}
			}
			

			if(adminEdit){
				// 기존 등록된 인원 - 전부 삭제
				wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
				wpPrjMpPlanDVo.setPrjNo(prjNo);
				queryQueue.delete(wpPrjMpPlanDVo);
				
				// 기존 등록된 인원 - 전부 삭제
				wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
				wpPrjMpPlanLVo.setPrjNo(prjNo);
				queryQueue.delete(wpPrjMpPlanLVo);
				
			} else if(!isNew && isHis){
				// 기존 등록된 인원 - 전부 삭제
				wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
				wpPrjMpPlanDVo.setPrjNo(prjNo);
				wpPrjMpPlanDVo.setVer(ver.toString());
				wpPrjMpPlanDVo.setHistory();
				queryQueue.delete(wpPrjMpPlanDVo);
				
				// 기존 등록된 인원 - 전부 삭제
				wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
				wpPrjMpPlanLVo.setPrjNo(prjNo);
				wpPrjMpPlanLVo.setVer(ver.toString());
				wpPrjMpPlanLVo.setHistory();
				queryQueue.delete(wpPrjMpPlanLVo);
			}
			
			
			Integer sortOrdr = 1, mNo;
			int maxNextMonth = 0;
			String planMm;
			String[] planMms, planYms=null;
			List<String[]> planMnArrList = null;
			
			// 인력 - 임시저장 또는 승인요청만 저장됨
			if(isHis || adminEdit){
				
				// 멘먼스 합계
				double planMmSum = 0;
				
				// 정렬순서 조절용 for
				for(String role1Cd : new String[]{"gpm", "pm", "con", "dev"}){
					for(int i=0; i<mpIds.length; i++){
						if("gpm".equals(role1Cd)){
							if(!"gpm".equals(prjRole2Cds[i])) continue;
						} else if("pm".equals(role1Cd)){
							if(!"pm".equals(prjRole2Cds[i])) continue;
						} else {
							if("gpm".equals(prjRole2Cds[i]) || "pm".equals(prjRole2Cds[i])) continue;
							if(!role1Cd.equals(prjRole1Cds[i])){
								continue;
							}
						}
						if(mpIds[i]==null || mpIds[i].isEmpty()) continue;
						
						wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
						wpPrjMpPlanDVo.setPrjNo(prjNo);
						wpPrjMpPlanDVo.setVer(ver.toString());
						if(!adminEdit) wpPrjMpPlanDVo.setHistory();
						wpPrjMpPlanDVo.setMpId(mpIds[i]);
						wpPrjMpPlanDVo.setMpTypCd(mpTypCds[i]);
						wpPrjMpPlanDVo.setPrjRole1Cd(prjRole1Cds[i]);
						wpPrjMpPlanDVo.setPrjRole2Cd(prjRole2Cds[i]);
						wpPrjMpPlanDVo.setSortOrdr(sortOrdr.toString());
						sortOrdr++;
						wpPrjMpPlanDVo.setMpMmSum(mpMmSums[i]);
						queryQueue.insert(wpPrjMpPlanDVo);
						
						// 프로잭트기본(WP_PRJ_B) 테이블에 - PM 세팅
						if("gpm".equals(prjRole2Cds[i]) && wpPrjBVo.getPmId()==null){
							wpPrjBVo.setPmId(mpIds[i]);
							wpPrjBVo.setPmMpTypCd(mpTypCds[i]);
						} else if("pm".equals(prjRole2Cds[i]) && wpPrjBVo.getPmId()==null){
							wpPrjBVo.setPmId(mpIds[i]);
							wpPrjBVo.setPmMpTypCd(mpTypCds[i]);
						}
						
						if(planMnArrList == null){
							// 계획멘먼스 - 리스트에 담음
							planMnArrList = new ArrayList<String[]>();
							for(maxNextMonth = 1;;maxNextMonth++){
								planMms = request.getParameterValues("M"+maxNextMonth);
								if(planMms==null || planMms.length==0){
									break;
								}
								planMnArrList.add(planMms);
							}
							// 계획 년월 계산
							String strtYmd = request.getParameter("strtYmd");
							if(strtYmd!=null && !strtYmd.isEmpty()){
								planYms = new String[maxNextMonth];
								int year = Integer.parseInt(strtYmd.substring(0, 4));
								int month = Integer.parseInt(strtYmd.substring(5, 7));
								for(int j=0;j<planYms.length;j++){
									planYms[j] = year + "-" + (month<10 ? "0" : "")+month;
									if(month==12){
										year++;
										month=1;
									} else {
										month++;
									}
								}
							}
						}
						
						// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블 - 입력
						for(mNo=1; mNo<maxNextMonth; mNo++){
							wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
							wpPrjMpPlanLVo.setPrjNo(prjNo);
							wpPrjMpPlanLVo.setVer(ver.toString());
							if(!adminEdit) wpPrjMpPlanLVo.setHistory();
							wpPrjMpPlanLVo.setMpId(mpIds[i]);
							wpPrjMpPlanLVo.setMNo(mNo.toString());
							
							planMms = planMnArrList.get(mNo-1);
							planMm = planMms[i];
							if(planMm!=null && !planMm.isEmpty()){
								wpPrjMpPlanLVo.setPlanMm(planMm);
								planMmSum += Double.parseDouble(planMm);
							}
							if(planYms != null){
								wpPrjMpPlanLVo.setPlanYm(planYms[mNo-1]+"-01 00:00:00");
							}
							queryQueue.insert(wpPrjMpPlanLVo);
						}
					}
				}
				
				// 프로잭트 멘먼스 세팅
				wpPrjBVo.setPrjMm(Double.toString(planMmSum));
			}
			
			// 첨부파일
			wpCmSvc.saveAttachFiles(request, queryQueue, prjNo);
			
			// 일괄 SQL 실행
			commonSvc.execute(queryQueue);
			
			
			if("apvd".equals(modStatCd)){
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			} else {
				// ap.trans.submitOk={0} 하였습니다.
				model.put("message", messageProperties.getMessage("ap.trans.submitOk", new String[]{ "#wp.modStatCd."+modStatCd }, request));
			}
			if("temp".equals(modStatCd)){
				cat = "temp";
			} else if("askApv".equals(modStatCd)){
				cat = "askApv";
			}
			
			model.put("todo", "parent.location.replace('/wp/listPrj.do?cat="+cat+"&menuId="+request.getParameter("menuId")+"');");
			
		} catch(Exception e){
			String message = e.getMessage();
			e.printStackTrace();
			model.put("message", (message==null || message.isEmpty() ? e.getClass().getCanonicalName() : message));
		}
		return LayoutUtil.getResultJsp();
	}
	
	
	/** 외주직원 선택 */
	@RequestMapping(value = {"/wp/listOutsPop", "/wp/listOutsFrm"})
	public String listOutsPop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "mpNm", required = false) String mpNm,
			ModelMap model, Locale locale) throws Exception {
		
		// listOutsPop:프레임 싸고 있는 빈 페이지(페이징 처리를 위해), listOutsFrm:실제 페이지
		String uri = request.getRequestURI();
		if(uri.indexOf("/listOutsPop")>0){
			return LayoutUtil.getJspPath("/wp/listOutsPop");
		}
		
		WpMpBVo wpMpBVo = new WpMpBVo();
		if(mpNm!=null && !mpNm.isEmpty()) wpMpBVo.setMpNm(mpNm);
		
		wpMpBVo.setOrderBy("MP_NM");
		Integer recodeCount = commonSvc.count(wpMpBVo);
		model.put("recodeCount", recodeCount);
		
		PersonalUtil.setFixedPaging(request, wpMpBVo, 10, recodeCount);
		@SuppressWarnings("unchecked")
		List<WpMpBVo> wpMpBVoList = (List<WpMpBVo>)commonSvc.queryList(wpMpBVo);
		model.put("wpMpBVoList", wpMpBVoList);
		
		return LayoutUtil.getJspPath("/wp/listOutsFrm");
	}
	
	/** 외주직원 등록 */
	@RequestMapping(value = {"/wp/setOutsPop", "/wp/viewOutsPop"})
	public String setOutsPop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "mpId", required = false) String mpId,
			ModelMap model, Locale locale) throws Exception {
		
		if(request.getRequestURI().indexOf("viewOutsPop")>0){
			model.put("viewMode", Boolean.TRUE);
		}
		
		if(mpId!=null && !mpId.isEmpty()){
			
			WpMpBVo wpMpBVo = new WpMpBVo();
			wpMpBVo.setMpId(mpId);
			wpMpBVo = (WpMpBVo)commonSvc.queryVo(wpMpBVo);
			model.put("wpMpBVo", wpMpBVo);
		}
		
		return LayoutUtil.getJspPath("/wp/setOutsPop");
	}
	
	/** 외주직원 저장 */
	@RequestMapping(value = "/wp/transMp")
	public String transMp(HttpServletRequest request,
			@RequestParam(value = "role1Cd", required = false) String role1Cd,
			ModelMap model) throws Exception {
		try{
			
			WpMpBVo wpMpBVo = new WpMpBVo();
			VoUtil.bind(request, wpMpBVo);
			
			UserVo userVo = LoginSession.getUser(request);
			
			if(wpMpBVo.getMpId()==null || wpMpBVo.getMpId().isEmpty()){
				wpMpBVo.setMpId(wpCmSvc.createId("WP_MP_B"));
				wpMpBVo.setRegDt("sysdate");
				wpMpBVo.setRegUid(userVo.getUserUid());
				commonSvc.insert(wpMpBVo);
			} else {
				commonSvc.update(wpMpBVo);
			}
			
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('setOutsDialog'); parent.setManPower('"+role1Cd+"', 'out');");
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 외주직원 삭제 */
	@RequestMapping(value = "/wp/transMpDelAjx")
	public String transMpDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {

		try{
			QueryQueue queryQueue = new QueryQueue();
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String mpIds = (String)jsonObject.get("mpIds");
			
			WpPrjMpPlanDVo wpPrjMpPlanDVo;
			WpMpBVo wpMpBVo;
			
			int notDelCnt = 0;
			int delCnt = 0;
			for(String mpId : mpIds.split("\\,")){
				if(mpId.isEmpty()) continue;
				
				wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
				wpPrjMpPlanDVo.setMpId(mpId);
				if(commonSvc.count(wpPrjMpPlanDVo) > 0){
					notDelCnt ++;
				} else {
					wpMpBVo = new WpMpBVo();
					wpMpBVo.setMpId(mpId);
					queryQueue.delete(wpMpBVo);
					delCnt++;
				}
			}
			
			if(delCnt > 0){
				commonSvc.execute(queryQueue);
			}
			
			if(delCnt > 0){
				// cm.msg.del.successCnt={0} 건의  데이터가 삭제 되었습니다.
				String msg = messageProperties.getMessage("cm.msg.del.successCnt", new String[]{Integer.toString(delCnt)}, request);
				model.put("message", msg);
				model.put("result", "ok");
			} else if(notDelCnt > 0){
				// wp.msg.cannotDelMpInUse=프로잭트에 투입된 외주 직원은 삭제 할 수 없습니다.
				String msg = messageProperties.getMessage("wp.msg.cannotDelMpInUse", request);
				model.put("message", msg);
			} else {
				// cm.msg.del.noData=삭제할 데이터가 없습니다.
				String msg = messageProperties.getMessage("cm.msg.del.noData", request);
				model.put("message", msg);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 프로잭트 그룹 - 조회 */
	@RequestMapping(value = "/wp/setPrjGrpPop")
	public String setPrjGrpPop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "grpId", required = false) String grpId,
			ModelMap model, Locale locale) throws Exception {
		
		if(grpId!=null && !grpId.isEmpty()){
			WpPrjGrpBVo wpPrjGrpBVo = new WpPrjGrpBVo();
			wpPrjGrpBVo.setGrpId(grpId);
			wpPrjGrpBVo = (WpPrjGrpBVo)commonSvc.queryVo(wpPrjGrpBVo);
			model.put("wpPrjGrpBVo", wpPrjGrpBVo);
		}
		return LayoutUtil.getJspPath("/wp/setPrjGrpPop");
	}
	
	/** 프로잭트 그룹 - 저장 */
	@RequestMapping(value = "/wp/transPrjGrp")
	public String transPrjGrp(HttpServletRequest request,
			@RequestParam(value = "grpId", required = false) String grpId,
			@RequestParam(value = "grpNm", required = false) String grpNm,
			ModelMap model) throws Exception {
		try{
			
			WpPrjGrpBVo wpPrjGrpBVo = new WpPrjGrpBVo();
			wpPrjGrpBVo.setGrpNm(grpNm);
			
			if(grpId==null || grpId.isEmpty()){
				grpId = wpCmSvc.createId("WP_PRJ_GRP_B");
				wpPrjGrpBVo.setGrpId(grpId);
				commonSvc.insert(wpPrjGrpBVo);
			} else {
				wpPrjGrpBVo.setGrpId(grpId);
				commonSvc.update(wpPrjGrpBVo);
			}
			
			// 그룹기본(WP_GRP_B) 테이블
			wpPrjGrpBVo = new WpPrjGrpBVo();
			wpPrjGrpBVo.setOrderBy("GRP_NM");
			@SuppressWarnings("unchecked")
			List<WpPrjGrpBVo> wpPrjGrpBVoList = (List<WpPrjGrpBVo>)commonSvc.queryList(wpPrjGrpBVo);
			model.put("todo", "parent.dialog.close('setPrjGrpDialog'); parent.reloadPrjGrp('"+grpId+"', "+JsonUtil.toJson(wpPrjGrpBVoList)+");");
			
			// cm.msg.save.success=저장 되었습니다.
			//model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 외주직원 삭제 */
	@RequestMapping(value = "/wp/transPrjGrpDelAjx")
	public String transPrjGrpDelAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {

		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String grpId = (String)jsonObject.get("grpId");
			
			WpPrjBVo wpPrjBVo = new WpPrjBVo();
			wpPrjBVo.setGrpId(grpId);
			if(commonSvc.count(wpPrjBVo)==0){
				WpPrjGrpBVo wpPrjGrpBVo = new WpPrjGrpBVo();
				wpPrjGrpBVo.setGrpId(grpId);
				commonSvc.delete(wpPrjGrpBVo);
				
				model.put("result", "ok");
			} else {
				//wp.msg.cannotDelGrpInUse=사용중인 프로잭트 그룹은 삭제 할 수 없습니다.
				model.put("message", messageProperties.getMessage("wp.msg.cannotDelGrpInUse", request));
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}
	
	/** [Ajax 프로세스] - apvd:승인, rejt:반려, cancel:승인취소, delVer:버전삭제, del:삭제  */
	@RequestMapping(value = "/wp/transProcessActAjx")
	public String transProcessActAjx(HttpServletRequest request,
			@Parameter(name="data", required=false)String data,
			ModelMap model) throws Exception {

		try{
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String prjNo = (String)jsonObject.get("prjNo");
			String actCd = (String)jsonObject.get("actCd");
			String rejtRson = (String)jsonObject.get("rejtRson");
			
			WpPrjBVo storedVo = new WpPrjBVo();
			storedVo.setPrjNo(prjNo);
			storedVo.setHistory();
			storedVo.setOrderBy("VER DESC");
			@SuppressWarnings("unchecked")
			List<WpPrjBVo> hisList = (List<WpPrjBVo>)commonSvc.queryList(storedVo);
			if(hisList==null || hisList.isEmpty()){
				storedVo = null;
			} else {
				storedVo = hisList.get(0);
			}
			
			if(storedVo == null){
				if(!"coverOuts".equals(actCd)){
					// wp.msg.noPrj=해당하는 프로잭트가 없습니다.
					model.put("message", messageProperties.getMessage("wp.msg.noPrj", request));
					return JsonUtil.returnJson(model);
				}
			// 승인, 반려, 요청취소
			} else if("apvd".equals(actCd) || "rejt".equals(actCd) || "cancel".equals(actCd)){
				// 승인요청
				if(!"askApv".equals(storedVo.getModStatCd())){
					// wp.msg.notTheStep={0} 처리 단계가 아닙니다.
					model.put("message", messageProperties.getMessage("wp.msg.notTheStep", new String[]{ "#wp.modStatCd."+actCd }, request));
					return JsonUtil.returnJson(model);
				}
			} else if("delVer".equals(actCd)){
				// 요청취소, 반려
				if(!"cancel".equals(storedVo.getModStatCd()) && !"rejt".equals(storedVo.getModStatCd())){
					// wp.msg.notTheStep={0} 처리 단계가 아닙니다.
					model.put("message", messageProperties.getMessage("wp.msg.notTheStep", new String[]{ "#dm.cols.auth.verDel" }, request));
					return JsonUtil.returnJson(model);
				}
			} else if("del".equals(actCd)){
				if(!"temp".equals(storedVo.getModStatCd()) && !"cancel".equals(storedVo.getModStatCd()) && !"rejt".equals(storedVo.getModStatCd())){
					// wp.msg.cannotDel=삭제 할 수 없는 상태입니다.
					model.put("message", messageProperties.getMessage("wp.msg.cannotDel", request));
					return JsonUtil.returnJson(model);
				}
			}
			
			
			String ver = storedVo==null ? null : storedVo.getVer();
			
			// 반려
			if("rejt".equals(actCd)){
				
				WpPrjBVo wpPrjBVo = new WpPrjBVo();
				wpPrjBVo.setPrjNo(prjNo);
				wpPrjBVo.setVer(ver);
				wpPrjBVo.setHistory();
				wpPrjBVo.setModStatCd("rejt");//반려
				wpPrjBVo.setRejtRson(rejtRson);
				commonSvc.update(wpPrjBVo);
				
			// 승인요청 취소
			} else if("cancel".equals(actCd)){
				
				WpPrjBVo wpPrjBVo = new WpPrjBVo();
				wpPrjBVo.setPrjNo(prjNo);
				wpPrjBVo.setVer(ver);
				wpPrjBVo.setHistory();
				wpPrjBVo.setModStatCd("cancel");//요청취소
				commonSvc.update(wpPrjBVo);
				
			// 버전 삭제, 삭제(임시저장의 경우)
			} else if("delVer".equals(actCd) || "del".equals(actCd)){
				
				QueryQueue queryQueue = new QueryQueue();
				
				WpPrjBVo wpPrjBVo = new WpPrjBVo();
				wpPrjBVo.setPrjNo(prjNo);
				wpPrjBVo.setVer(ver);
				wpPrjBVo.setHistory();
				queryQueue.delete(wpPrjBVo);
				
				WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
				wpPrjMpPlanDVo.setPrjNo(prjNo);
				wpPrjMpPlanDVo.setVer(ver);
				wpPrjMpPlanDVo.setHistory();
				queryQueue.delete(wpPrjMpPlanDVo);
				
				WpPrjMpPlanLVo wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
				wpPrjMpPlanLVo.setPrjNo(prjNo);
				wpPrjMpPlanLVo.setVer(ver);
				wpPrjMpPlanLVo.setHistory();
				queryQueue.delete(wpPrjMpPlanLVo);
				
				commonSvc.execute(queryQueue);
				
				if("del".equals(actCd)){
					// ap.trans.submitOk={0} 하였습니다.
					model.put("message", messageProperties.getMessage("cm.msg.del.success", request));
				} else {
					// ap.trans.submitOk={0} 하였습니다.
					model.put("message", messageProperties.getMessage("ap.trans.submitOk", new String[]{ "#dm.cols.auth.verDel" }, request));
				}
				model.put("result", "ok");
				return JsonUtil.returnJson(model);
				
			// 승인
			} else if("apvd".equals(actCd)){
				
				QueryQueue queryQueue = new QueryQueue();
				
				// 프로잭트기본(WP_PRJ_B) 테이블 - 히스토리 - 승인
				WpPrjBVo wpPrjBVo = new WpPrjBVo();
				wpPrjBVo.setPrjNo(prjNo);
				wpPrjBVo.setVer(ver);
				wpPrjBVo.setHistory();
				wpPrjBVo.setModStatCd("apvd");//승인
				wpPrjBVo.setRejtRson(rejtRson);
				queryQueue.update(wpPrjBVo);
				
				// 프로잭트기본(WP_PRJ_B) 테이블 - 기존 - 삭제 
				wpPrjBVo = new WpPrjBVo();
				wpPrjBVo.setPrjNo(prjNo);
				queryQueue.delete(wpPrjBVo);
				
				// 프로잭트기본(WP_PRJ_B) 테이블 - 새 버전 입력
				wpPrjBVo = new WpPrjBVo();
				VoUtil.fromMap(wpPrjBVo, VoUtil.toMap(storedVo, null));
				wpPrjBVo.setModStatCd("apvd");//승인
				wpPrjBVo.setRejtRson(rejtRson);
				wpPrjBVo.setRegDt("sysdate");
				queryQueue.insert(wpPrjBVo);
				
				// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블 - 기존 - 삭제
				WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
				wpPrjMpPlanDVo.setPrjNo(prjNo);
				queryQueue.delete(wpPrjMpPlanDVo);
				
				// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블 - 기존 조회
				wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
				wpPrjMpPlanDVo.setPrjNo(prjNo);
				wpPrjMpPlanDVo.setVer(ver);
				wpPrjMpPlanDVo.setHistory();
				wpPrjMpPlanDVo.setOrderBy("SORT_ORDR");
				@SuppressWarnings("unchecked")
				List<WpPrjMpPlanDVo> planDList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
				if(planDList != null && !planDList.isEmpty()){
					for(WpPrjMpPlanDVo vo : planDList){
						// 새 버전 입력
						queryQueue.insert(vo);
					}
				}
				
				// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블 - 기존 - 삭제
				WpPrjMpPlanLVo wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
				wpPrjMpPlanLVo.setPrjNo(prjNo);
				queryQueue.delete(wpPrjMpPlanLVo);
				
				// 프로잭트인력계획내역(WP_PRJ_MP_PLAN_L) 테이블 - 기존 조회
				wpPrjMpPlanLVo = new WpPrjMpPlanLVo();
				wpPrjMpPlanLVo.setPrjNo(prjNo);
				wpPrjMpPlanLVo.setVer(ver);
				wpPrjMpPlanLVo.setHistory();
				wpPrjMpPlanLVo.setOrderBy("MP_ID");
				@SuppressWarnings("unchecked")
				List<WpPrjMpPlanLVo> planLList = (List<WpPrjMpPlanLVo>)commonSvc.queryList(wpPrjMpPlanLVo);
				if(planDList != null && !planDList.isEmpty()){
					for(WpPrjMpPlanLVo vo : planLList){
						// 새 버전 입력
						queryQueue.insert(vo);
					}
				}
				
				commonSvc.execute(queryQueue);
				
			// 외주 공수 대리 입력
			} else if("coverOuts".equals(actCd)){
				
				QueryQueue queryQueue = new QueryQueue();
				
				String mpIds = (String)jsonObject.get("mpIds");
				
				WpPrjAuthDVo wpPrjAuthDVo = new WpPrjAuthDVo();
				wpPrjAuthDVo.setPrjNo(prjNo);
				wpPrjAuthDVo.setAuthCd("coverOuts");
				queryQueue.delete(wpPrjAuthDVo);
				
				if(mpIds!=null && !mpIds.isEmpty()){
					for(String mpId : mpIds.split(",")){
						wpPrjAuthDVo = new WpPrjAuthDVo();
						wpPrjAuthDVo.setPrjNo(prjNo);
						wpPrjAuthDVo.setAuthCd("coverOuts");
						wpPrjAuthDVo.setMpId(mpId);
						queryQueue.insert(wpPrjAuthDVo);
					}
				}
				
				commonSvc.execute(queryQueue);
				
				// cm.msg.save.success=저장 되었습니다.
				model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
				model.put("result", "ok");
				return JsonUtil.returnJson(model);
			}
			
			// ap.trans.processOk={0} 처리 하였습니다.
			model.put("message", messageProperties.getMessage("ap.trans.processOk", new String[]{ "#wp.modStatCd."+actCd }, request));
			model.put("result", "ok");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		return JsonUtil.returnJson(model);
	}
	
	/** 승인,반려,승인요청 팝업 */
	@RequestMapping(value = "/wp/setProcPop")
	public String setProcPop(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {
		return LayoutUtil.getJspPath("/wp/setProcPop");
	}
	
	/** 첨부파일 다운로드 (사용자) */
	@RequestMapping(value = "/wp/downFile")
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
			ModelAndView mv = wpCmSvc.getFileList(request , fileIds , actionParam);
			return mv;
			
		} catch (Exception e) {
			ModelAndView mv = new ModelAndView("cm/result/commonResult");
			mv.addObject("message", e.getMessage());
		}
		return null;
	}
	
	/** 외주 공수 대리 입력 */
	@RequestMapping(value = "/wp/setCoverOutsPop")
	public String setCoverOutsPop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "prjNo", required = false) String prjNo,
			ModelMap model, Locale locale) throws Exception {
		
		// 프로잭트인력계획상세(WP_PRJ_MP_PLAN_D) 테이블
		WpPrjMpPlanDVo wpPrjMpPlanDVo = new WpPrjMpPlanDVo();
		wpPrjMpPlanDVo.setPrjNo(prjNo);
		wpPrjMpPlanDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<WpPrjMpPlanDVo> wpPrjMpPlanDVoList = (List<WpPrjMpPlanDVo>)commonSvc.queryList(wpPrjMpPlanDVo);
		if(wpPrjMpPlanDVoList != null && !wpPrjMpPlanDVoList.isEmpty()){
			model.put("wpPrjMpPlanDVoList", wpPrjMpPlanDVoList);
			
			// 프로잭트권한상세(WP_PRJ_AUTH_D) 테이블
			WpPrjAuthDVo wpPrjAuthDVo = new WpPrjAuthDVo();
			wpPrjAuthDVo.setPrjNo(prjNo);
			wpPrjAuthDVo.setOrderBy("AUTH_CD, MP_ID");
			@SuppressWarnings("unchecked")
			List<WpPrjAuthDVo> wpPrjAuthDVoList = (List<WpPrjAuthDVo>)commonSvc.queryList(wpPrjAuthDVo);
			if(wpPrjAuthDVoList != null && !wpPrjAuthDVoList.isEmpty()){
				String oldAuthCd=null, authCd;
				List<String> mpIdList = null;
				for(WpPrjAuthDVo vo : wpPrjAuthDVoList){
					authCd = vo.getAuthCd();
					if(oldAuthCd==null || !oldAuthCd.equals(authCd)){
						mpIdList = new ArrayList<String>();
						model.put(authCd+"List", mpIdList);
						oldAuthCd = authCd;
					}
					mpIdList.add(vo.getMpId());
				}
			}
		}
		
		return LayoutUtil.getJspPath("/wp/setCoverOutsPop");
	}
}
