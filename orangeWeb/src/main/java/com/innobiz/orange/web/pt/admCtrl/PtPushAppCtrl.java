package com.innobiz.orange.web.pt.admCtrl;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.config.CustConfig;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtPushAppDVo;

@Controller
public class PtPushAppCtrl {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PtPushAppCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;

	/** plist/ipa 다운로드 */
	@RequestMapping(value = "/app/{custCd}/{device}", method = RequestMethod.GET)
	public ModelAndView OrangeNoti(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value="custCd") final String custCd,
			@PathVariable(value="device") final String device) throws Exception {
		
		String uri = request.getRequestURI();
		
		boolean isPlist = uri.endsWith(".plist");
		boolean isIpa = uri.endsWith(".ipa");
		boolean isApk = uri.endsWith(".apk");
		boolean isFile = isPlist || isIpa || isApk;
		
		boolean isHtml = uri.endsWith(".html") || uri.endsWith(".do");
		boolean isIOS = "iphone".equals(device) || "ios".equals(device) || "apple".equals(device);
		boolean isAndroid = "android".equals(device);
		
		if(isHtml && !isIOS && !isAndroid){
			response.setStatus(404);
			return null;
		}
		if(!isHtml && !isFile){
			response.setStatus(404);
			return null;
		}
		
		// 이노비즈 - BBFB64
		if(!(CustConfig.DEV_PC || CustConfig.DEV_SVR || CustConfig.CUST_ENOBIZ)){
			response.setStatus(404);
			return null;
		}
		
		PtPushAppDVo ptPushAppDVo = new PtPushAppDVo();
		ptPushAppDVo.setCustCd(custCd);
		ptPushAppDVo.setSeq("0");
		ptPushAppDVo = (PtPushAppDVo)commonSvc.queryVo(ptPushAppDVo);
		if(ptPushAppDVo==null){
			LOGGER.error("No data for Push App : "+custCd);
			response.setStatus(404);
			return null;
		}
		
		if(isPlist || isIpa){
			String endYmd = ptPushAppDVo.getEndYmd();
			String curYmd = StringUtil.getCurrYmd();
			if(endYmd==null || endYmd.isEmpty() || endYmd.compareTo(curYmd)<0){
				LOGGER.error("Expired for Push App : "+custCd+"   curYmd:"+curYmd+"  endYmd:"+endYmd+" >> HTTP 526 : Invalid SSL Certificate");
				response.setStatus(526);
				response.getOutputStream().write("HTTP 526 : Invalid SSL Certificate".getBytes());
				return null;
			}
		}
		
		if(!"Y".equals(ptPushAppDVo.getUseYn())){
			LOGGER.error("Expired for Push App - not use : "+custCd);
			response.setStatus(404);
			return null;
		}
		
		// 설치용 html
		if(isHtml){
			
			if(isIOS){
				
				String wasCopyBaseDir = distManager.getWasCopyBaseDir();
				File file = new File(wasCopyBaseDir+ptPushAppDVo.getIpaPath());
				if(!file.isFile()){
					LOGGER.error("No file for Push App - OrangeNoti.ipa : "+custCd);
					response.setStatus(404);
					return null;
				}
				
				ModelAndView mv = new ModelAndView("/support/app/apple");
				mv.addObject("ptPushAppDVo", ptPushAppDVo);
				return mv;
				
			} else {
				
				String wasCopyBaseDir = distManager.getWasCopyBaseDir();
				File file = new File(wasCopyBaseDir+ptPushAppDVo.getApkPath());
				if(!file.isFile()){
					LOGGER.error("No file for Push App - OrangeNoti.apk : "+custCd);
					response.setStatus(404);
					return null;
				}
				
				ModelAndView mv = new ModelAndView("/support/app/android");
				mv.addObject("ptPushAppDVo", ptPushAppDVo);
				return mv;
			}
			
		// 설치용 plist - 아이폰 설치 xml
		} else if(isPlist){
			
			ModelAndView mv = new ModelAndView("/support/app/plist");
			mv.addObject("ptPushAppDVo", ptPushAppDVo);
			return mv;
			
		// 설치용 ipa - 아이폰 설치 파일
		} else if(isIpa){
			
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			File file = new File(wasCopyBaseDir+ptPushAppDVo.getIpaPath());
			if(!file.isFile()){
				LOGGER.error("No file for OrangeNoti.ipa : "+custCd+"  - "+wasCopyBaseDir+ptPushAppDVo.getIpaPath());
				response.setStatus(404);
				return null;
			}
			
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", "OrangeNoti.ipa");
			return mv;

		// 설치용 apk - 안드로이드 설치 파일
		} else if(isApk){
			
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			File file = new File(wasCopyBaseDir+ptPushAppDVo.getApkPath());
			if(!file.isFile()){
				LOGGER.error("No file for OrangeNoti.apk : "+custCd+"  - "+wasCopyBaseDir+ptPushAppDVo.getApkPath());
				response.setStatus(404);
				return null;
			}
			
			ModelAndView mv = new ModelAndView("fileDownloadView");
			mv.addObject("downloadFile", file);
			mv.addObject("realFileName", "OrangeNoti.apk");
			return mv;
			
		} else {
			response.setStatus(404);
			return null;
		}
	}
	
	/** 푸쉬앱 목록 조회 화면 */
	@RequestMapping(value = "/pt/adm/app/listPushApp", method = RequestMethod.GET)
	public String listPushApp(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "custNm", required = false) String custNm,
			ModelMap model) throws Exception {
		
		PtPushAppDVo ptPushAppDVo = new PtPushAppDVo();
		ptPushAppDVo.setSeq("0");
		if(custNm!=null && !custNm.isEmpty()) ptPushAppDVo.setCustNm(custNm);
		
		// 레코드수 조회 - 패지징
		Integer recodeCount = commonSvc.count(ptPushAppDVo);
		// 페이지 정보 세팅
		PersonalUtil.setPaging(request, ptPushAppDVo, recodeCount);
		
		// 목록조회
		@SuppressWarnings("unchecked")
		List<PtPushAppDVo> ptPushAppDVoList = (List<PtPushAppDVo>)commonSvc.queryList(ptPushAppDVo);
		
		model.put("recodeCount", recodeCount);
		model.put("ptPushAppDVoList", ptPushAppDVoList);
		
		return LayoutUtil.getJspPath("/pt/adm/app/listPushApp");
	}
	
	/** [팝업] 푸쉬앱 상세 조회 화면 */
	@RequestMapping(value = "/pt/adm/app/setPushAppPop", method = RequestMethod.GET)
	public String setPushAppPop(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "custCd", required = false) String custCd,
			@RequestParam(value = "seq", required = false) String seq,
			ModelMap model) throws Exception {
		
		if(custCd!=null && !custCd.isEmpty()){
			
			PtPushAppDVo ptPushAppDVo = new PtPushAppDVo();
			ptPushAppDVo.setCustCd(custCd);
			ptPushAppDVo.setOrderBy("REG_DT DESC");

			@SuppressWarnings("unchecked")
			List<PtPushAppDVo> ptPushAppDVoList = (List<PtPushAppDVo>)commonSvc.queryList(ptPushAppDVo);
			
			if(ptPushAppDVoList != null && !ptPushAppDVoList.isEmpty()){
				
				if(seq==null || seq.isEmpty()) seq = "0";
				model.put("seq", seq);
				
				// 루프 돌면서 시퀀스가 같은것을 조회로 세팅함
				int i, size = ptPushAppDVoList.size();
				for(i=0;i<size;i++){
					
					ptPushAppDVo = ptPushAppDVoList.get(i);
					
					// 동일한 시퀀스 이면
					if(seq.equals(ptPushAppDVo.getSeq())){
						
						model.put("ptPushAppDVo", ptPushAppDVo);
						
						if(ptPushAppDVo.getIpaPath()!=null && !ptPushAppDVo.getIpaPath().isEmpty()){
							model.put("appleLink", "<p><a href=\"https://gw.enobiz.com/support/app/"+
									ptPushAppDVo.getCustCd()+"/ios.html\" target=\"_blank\">아이폰 푸쉬 앱 설치 (만료:"+ptPushAppDVo.getEndYmd()+")</a></p>");
						}
						
						if(ptPushAppDVo.getApkPath()!=null && !ptPushAppDVo.getApkPath().isEmpty()){
							model.put("androidLink", "<p><a href=\"http://gw.enobiz.com/support/app/"+
									ptPushAppDVo.getCustCd()+"/android.html\" target=\"_blank\">안드로이드 푸쉬 앱 설치</a></p>");
						}
						
						model.put("ptPushAppDVoList", ptPushAppDVoList);
						
						break;
					}
				}
			}
		}
		
		return LayoutUtil.getJspPath("/pt/adm/app/setPushAppPop");
	}

	/** 푸쉬앱 설정 - 저장 */
	@RequestMapping(value = "/pt/adm/app/transPushApp")
	public String transPushApp(HttpServletRequest request,
			ModelMap model, Locale locale) throws Exception {
		
		try{

			QueryQueue queryQueue = new QueryQueue();
			
			UploadHandler uploadHandler = uploadManager.createHandler(request, "temp", "pt");
			uploadHandler.upload();//업로드 파일 정보
			
			Map<String, String> paramMap = uploadHandler.getParamMap();//파라미터 정보
			Map<String, File> fileMap = uploadHandler.getFileMap();//파일 리스트 Map
			
			// 파라미터 변수 매핑
			PtPushAppDVo ptPushAppDVo = new PtPushAppDVo();
			VoUtil.fromMap(ptPushAppDVo, paramMap);
			ptPushAppDVo.setSeq("0");
			ptPushAppDVo.setRegDt("sysdate");
			
			DistHandler distHandler = distManager.createHandler("apple/"+ptPushAppDVo.getCustCd(), locale);
			
			// 업로드 파일 정보
			File ipaFile = fileMap.get("ipaFile");
			if(ipaFile != null){
				String distPath = distHandler.addWasList(ipaFile.getAbsolutePath());
				ptPushAppDVo.setIpaPath(distPath);
			}
			
			File apkFile = fileMap.get("apkFile");
			if(apkFile != null){
				String distPath = distHandler.addWasList(apkFile.getAbsolutePath());
				ptPushAppDVo.setApkPath(distPath);
			}
			
			// 이전 데이터 조회
			PtPushAppDVo oldPtPushAppDVo = new PtPushAppDVo();
			oldPtPushAppDVo.setCustCd(ptPushAppDVo.getCustCd());
			oldPtPushAppDVo.setSeq("0");
			oldPtPushAppDVo = (PtPushAppDVo)commonSvc.queryVo(oldPtPushAppDVo);
			
			// 이전 데이터 있고
			// 종료일이 다르거나
			// ipa 파일이 첨부되고 이전 ipa 파일이 있거나
			// apk 파일이 첨부되고 이전 apk 파일이 있으면
			if(oldPtPushAppDVo!=null && (
					!oldPtPushAppDVo.getEndYmd().equals(ptPushAppDVo.getEndYmd()) ||
					(ipaFile!=null && oldPtPushAppDVo.getIpaPath()!=null) ||
					(apkFile!=null && oldPtPushAppDVo.getApkPath()!=null)
					)){
				
				// 시퀀스 증가 시켜 이전 로그 만듬
				oldPtPushAppDVo.setSeq(null);
				queryQueue.insert(oldPtPushAppDVo);
			}
			
			// 이전 데이터 없으면 insert, 있으면 update
			if(oldPtPushAppDVo==null){
				queryQueue.insert(ptPushAppDVo);
			} else {
				queryQueue.update(ptPushAppDVo);
			}
			
			if(ipaFile != null || apkFile != null){
				distHandler.distribute();
			}
			
			commonSvc.execute(queryQueue);
			
			if(ipaFile != null){
				uploadHandler.removeTempDir();
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			
		} catch(Exception e){
			String message = e.getMessage();
			if(message==null || message.isEmpty()) message = e.getClass().getCanonicalName();
			LOGGER.error(message);
			// cm.msg.save.fail=저장에 실패하였습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.fail", request));
			e.printStackTrace();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 푸쉬앱 설정 삭제 */
	@RequestMapping(value = "/pt/adm/app/transPushAppAjx")
	public String transPushAppAjx(HttpServletRequest request,
			@Parameter(name="data", required=false) String data,
			ModelMap model, Locale locale) throws Exception {
		
		try {
//			QueryQueue queryQueue = new QueryQueue();
			
			JSONObject jsonObject = (JSONObject)JSONValue.parse(data);
			String cmd = (String)jsonObject.get("cmd");
			
			
			if("deleteHis".equals(cmd)){
				
				String custCd = (String)jsonObject.get("custCd");
				String seq = (String)jsonObject.get("seq");
				
				if(custCd!=null && !custCd.isEmpty() && seq!=null && !seq.isEmpty()){
					
					PtPushAppDVo ptPushAppDVo = new PtPushAppDVo();
					ptPushAppDVo.setCustCd(custCd);
					ptPushAppDVo.setSeq(seq);
					commonSvc.delete(ptPushAppDVo);
					
					//cm.msg.del.success=삭제 되었습니다.
					String msg = messageProperties.getMessage("cm.msg.del.success", request);
					model.put("message", msg);
					model.put("result", "ok");
				}
				
			} else if("delete".equals(cmd)){
				
				String custCds = (String)jsonObject.get("custCds");
				
				if(custCds!=null && !custCds.isEmpty()){
					
					QueryQueue queryQueue = new QueryQueue();
					PtPushAppDVo ptPushAppDVo;
					for(String paramCd : custCds.split(",")){
						ptPushAppDVo = new PtPushAppDVo();
						ptPushAppDVo.setCustCd(paramCd);
						queryQueue.delete(ptPushAppDVo);
					}
					
					if(!queryQueue.isEmpty()){
						commonSvc.execute(queryQueue);
						
						//cm.msg.del.success=삭제 되었습니다.
						String msg = messageProperties.getMessage("cm.msg.del.success", request);
						model.put("message", msg);
						model.put("result", "ok");
					}
					
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.returnJson(model);
	}
	
	
	
}
