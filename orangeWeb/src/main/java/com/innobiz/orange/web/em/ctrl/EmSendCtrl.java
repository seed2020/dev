package com.innobiz.orange.web.em.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBodyLVo;
import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmFileSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.em.svc.EmSendSvc;
import com.innobiz.orange.web.em.vo.EmSendBVo;
import com.innobiz.orange.web.em.vo.EmSendFileDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.wb.svc.WbBcSvc;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcCntcDVo;

/** 보내기 컨트롤러 */
@Controller
public class EmSendCtrl {		
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmSendCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 파일 서비스 */
	@Resource(name = "dmFileSvc")
	private DmFileSvc dmFileSvc;
	
	/** 보내기 서비스 */
	@Autowired
	private EmSendSvc emSendSvc;
	
	/** 포털 보안 서비스 */
	@Resource(name = "ptSecuSvc")
	private PtSecuSvc ptSecuSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WbBcSvc wbBcSvc;
	
	/** [AJAX] 보내기 정보 저장 */
	@RequestMapping(value = {"/ap/box/saveSendAjx", "/bb/saveSendAjx", "/dm/doc/saveSendAjx", "/dm/adm/doc/saveSendAjx"})
	public String srchUserAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 보내기 기본
			EmSendBVo emSendBVo = null;
			// 파일 목록
			List<EmSendFileDVo> emSendFileDVoList=new ArrayList<EmSendFileDVo>();
			// 파일
			EmSendFileDVo emSendFileDVo=null;
			// 보내기 번호
			Long no=commonSvc.nextVal("EM_SEND_B");
			String sendNo=no.toString();
			
			// 시스템 설정
			Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
			
			// 첫 줄 삽입여부
			boolean isFirstRow=sysPlocMap != null && sysPlocMap.containsKey("sendRowAddEnable") && "Y".equals(sysPlocMap.get("sendRowAddEnable"));
			
			// 첫 줄 
			String cont=isFirstRow ? "<p><br/></p>" : "";
			
			// 문서관리
			if(request.getRequestURI().startsWith("/dm")){
				// 공용, 개인 문서 
				String docTyp = (String) object.get("docTyp");
				if(docTyp == null) docTyp = "psn";
				
				// 문서ID
				String docId = (String) object.get("docId");
				
				// 기본저장소 조회
				DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
				String storId = dmStorBVo.getStorId();
				String tableName = dmStorBVo.getTblNm();
				// 공통문서여부[복사원본문서가 공통문서인지 여부]
				boolean isCmDoc = docTyp == null || docTyp.isEmpty() || !"psn".equals(docTyp);
				
				// 문서 상세 조회
				DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo(langTypCd, isCmDoc ? dmStorBVo : null, docId, null, true);
				if(dmDocLVo == null){
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				// 공통문서일 경우 권한을 체크한다.
				if(isCmDoc){
					// 요청경로 세팅
					String path = dmCmSvc.getRequestPath(request, model , "Doc");
							
					/** [권한] Start */
					boolean admUriChk = request.getRequestURI().startsWith("/dm/adm/");
					Map<String,String> authMap = dmDocSvc.getAuthMap(model, admUriChk, dmDocLVo, userVo, storId, langTypCd, "/viewSendOpt", false);
					// 권한체크 제외 URL
					boolean isUrlChk = dmDocSvc.chkNotAuth(path);
					if(!admUriChk && !isUrlChk && !dmDocSvc.chkDocSeculCd(userVo, dmDocLVo, dmDocSvc.chkDocAuth(authMap, "send", path))){
						LOGGER.error("[ERROR] User Auth Check Fail - request path : "+path+"\tuserUid : "+userVo.getUserUid()+"\tauth : send");
						//cm.msg.noAuth=권한이 없습니다.
						throw new CmException("cm.msg.noAuth", request);
					}
					/** [권한] End */
				}
				// 보내기 기본VO
				emSendBVo = new EmSendBVo();
				// 제목
				emSendBVo.setSubj(dmDocLVo.getSubj());
				// 내용
				if(dmDocLVo.getCont()!=null)
					cont+=dmDocLVo.getCont();
				if(!cont.isEmpty()) emSendBVo.setCont(cont);
				
				// 문서 파일 목록
				List<CommonFileVo> fileVoList = dmFileSvc.getDmFileVoList(docId, tableName);
				if(fileVoList!=null){
					int fileSeq=0;
					for(CommonFileVo fileVo : fileVoList){
						emSendFileDVo=new EmSendFileDVo();
						// 파일VO 복사
						emSendSvc.setFileVo((CommonVo)fileVo, (CommonVo)emSendFileDVo, "dispNm","fileExt","fileSize","savePath");
						emSendFileDVo.setFileSeq(++fileSeq);
						emSendFileDVoList.add(emSendFileDVo);
					}
				}
				
				// 결재
			}else if(request.getRequestURI().startsWith("/ap")){
				
				String apvNo = (String) object.get("apvNo");
				
				// 진행문서기본(AP_ONGD_B) - 조회
				ApOngdBVo apOngdBVo = new ApOngdBVo();
				apOngdBVo.setApvNo(apvNo);
				apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
				//cm.msg.noData=해당하는 데이터가 없습니다.
				if(apOngdBVo==null) throw new CmException("cm.msg.noData", request);
				
				// 진행문서본문내역(AP_ONGD_BODY_L) - 조회
				ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
				apOngdBodyLVo.setApvNo(apvNo);
				apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
				apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
				
				// 진행문서첨부파일내역(AP_ONGD_ATT_FILE_L) - 조회
				ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
				apOngdAttFileLVo.setApvNo(apvNo);
				apOngdAttFileLVo.setAttHstNo(apOngdBVo.getAttHstNo());
				@SuppressWarnings("unchecked")
				List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
				
				// 보내기(EM_SEND_B)
				emSendBVo = new EmSendBVo();
				emSendBVo.setSubj(apOngdBVo.getDocSubj());// 제목
				
				if(apOngdBodyLVo!=null && apOngdBodyLVo.getBodyHtml()!=null)
					cont+=apOngdBodyLVo.getBodyHtml();// 내용
				if(!cont.isEmpty()) emSendBVo.setCont(cont);
				
				if(apOngdAttFileLVoList!=null){
					int fileSeq=0;
					for(ApOngdAttFileLVo storedApOngdAttFileLVo : apOngdAttFileLVoList){
						
						emSendFileDVo=new EmSendFileDVo();
						emSendFileDVo.setFileSeq(fileSeq++);
						emSendFileDVo.setDispNm(storedApOngdAttFileLVo.getAttDispNm());
						emSendFileDVo.setFileExt(storedApOngdAttFileLVo.getFileExt());
						if(storedApOngdAttFileLVo.getFileKb() != null && !storedApOngdAttFileLVo.getFileKb().isEmpty()){
							long fileSize = Long.parseLong(storedApOngdAttFileLVo.getFileKb()) * 1000;
							emSendFileDVo.setFileSize(fileSize);
						}
						emSendFileDVo.setSavePath(storedApOngdAttFileLVo.getFilePath());
						emSendFileDVoList.add(emSendFileDVo);
					}
				}
			}
			
			if(emSendBVo!=null){
				QueryQueue queryQueue = new QueryQueue();
				// 기본 저장
				emSendBVo.setSendNo(sendNo);
				queryQueue.insert(emSendBVo);
				
				// 파일 저장
				if(emSendFileDVoList.size()>0){
					for(EmSendFileDVo storedEmSendFileDVo : emSendFileDVoList){
						storedEmSendFileDVo.setSendNo(sendNo);
						queryQueue.insert(storedEmSendFileDVo);
					}
				}
				if(!queryQueue.isEmpty() && queryQueue.size()>0) commonSvc.execute(queryQueue);
			}
			
			// 보내기번호 리턴
			model.put("sendNo", sendNo);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 보내기 작성 */
	@RequestMapping(value = {"/ap/box/setSendPop", "/bb/setSendPop", "/dm/doc/setSendPop", "/dm/adm/doc/setSendPop", "/cm/bb/setSendPop"})
	public String setSendWritePop(HttpServletRequest request,
			//@RequestParam(value = "module", required = true) String module,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			// pt.logout.timeout=로그인 세션이 종료 되었습니다.
			throw new CmException("pt.logout.timeout", request);
		}
		
		// 팝업여부
		boolean isWinPop=false;
		String frmUrl=null, params=null;
		// 게시판으로 보내기
		if(request.getRequestURI().startsWith("/bb") || request.getRequestURI().startsWith("/cm/bb")){
			if(request.getRequestURI().startsWith("/cm/")){ // cm 일 경우 메뉴ID param에 추가
				String brdId = ParamUtil.getRequestParam(request, "brdId", true);
				params="menuId="+ptSecuSvc.getSecuMenuId(userVo, "/bb/listBull.do?brdId="+brdId);
				params+="&returnFunc=sendPopClose";
				isWinPop=true;
			}
			frmUrl="/bb/setBull.do";
			model.put("saveFunc", "saveBull"); // 저장 버튼명
		}
		
		model.put("frmUrl", frmUrl); // 등록 URL
		model.put("params", params); // 파라미터
		model.put("paramsForList", ParamUtil.getQueryString(request, "data"));
		model.put("isWinPop", isWinPop);
		if(isWinPop) return LayoutUtil.getJspPath("/em/send/setSendPop", "Frm");
		return LayoutUtil.getJspPath("/em/send/setSendPop");
	}
	
	/** [팝업] 게시판 목록조회 */
	@RequestMapping(value = {"/ap/box/listBrdPop"})
	public String findDocPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		if(request.getRequestURI().startsWith("/ap/box/listBrd")){
			model.put("frmSrc", "listBrdFrm");
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "data","menuId"));
		// get 파라미터를 post 파라미터로 전달하기 위해
		model.put("paramEntryList", ParamUtil.getEntryMapList(request, "data","menuId"));
					
		return LayoutUtil.getJspPath("/em/send/listSelectPop");
	}
	
	/** [FRAME] 게시판 */
	@RequestMapping(value = {"/ap/box/listBrdFrm", "/dm/doc/listBrdFrm","/dm/adm/doc/listBrdFrm"})
	public String listBrdFrm(HttpServletRequest request,
			@RequestParam(value = "mnuGrpMdCd", required = false) String mnuGrpMdCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
				
		// 게시판관리(BA_BRD_B) 테이블 - BIND
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
				
		// 모듈 별 권한 있는 모듈참조ID 목록
		List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "W");
		
		List<BaBrdBVo> hasAuthBaBrdBVoList = new ArrayList<BaBrdBVo>();
		if(mdIds != null && baBrdBVoList != null){
			for(String mdId : mdIds){
				for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
					if(mdId.equals(storedBaBrdBVo.getBrdId())){
						hasAuthBaBrdBVoList.add(storedBaBrdBVo);
						break;
					}
				}
			}
		}
		model.put("baBrdBVoList", hasAuthBaBrdBVoList);
				
		return LayoutUtil.getJspPath("/bb/listBrdFrm");
	}
	
	/** [XML] - 게시판 목록 조회 */
	@RequestMapping(value = "/cm/send/getBrdList")
	public String getBrdList( HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		String result = null;
		
		try{
			Document doc = null;
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			if(userVo==null){
				// pt.logout.timeout=로그인 세션이 종료 되었습니다.
				throw new CmException("pt.logout.timeout", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
					
			// 게시판관리(BA_BRD_B) 테이블 - BIND
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setQueryLang(langTypCd);
			bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
			baBrdBVo.setOrderBy("T.BRD_ID ASC");
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			@SuppressWarnings("unchecked")
			List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
			
			// 모듈 별 권한 있는 모듈참조ID 목록
			List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "W");
			
			List<BaBrdBVo> hasAuthBaBrdBVoList = new ArrayList<BaBrdBVo>();
			if(mdIds != null && baBrdBVoList != null){
				for(String mdId : mdIds){
					for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
						if(mdId.equals(storedBaBrdBVo.getBrdId())){
							hasAuthBaBrdBVoList.add(storedBaBrdBVo);
							break;
						}
					}
				}
			}
			if(hasAuthBaBrdBVoList.size()>0){
				Element boardList = new Element("boardList");
				for(BaBrdBVo storedBaBrdBVo : hasAuthBaBrdBVoList){
					boardList.addContent(new Element("board").setAttribute("value", storedBaBrdBVo.getBrdId()).setText(storedBaBrdBVo.getRescNm()));
				}
				doc=new Document().setRootElement(boardList);
			}
			XMLOutputter xo = new XMLOutputter();
			xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
			result = xo.outputString(doc);
			
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			result=getMessage("error", e.getMessage());
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			result=getMessage("error", e.getMessage());
		}
		
		if(result!=null && !result.isEmpty()){
			PrintWriter write = response.getWriter();
			write.print(result);
			//response.getOutputStream().write(result.getBytes());
			write.flush();
			write.close();
			return null;
		}
		
		response.sendError(403);
		return null;
	}
	
	/** [JSON] - 게시판 목록 조회 */
	@RequestMapping(value = "/cm/send/getBrdListJson")
	public String getBrdListJson( HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		String result = null;
		String callback = request.getParameter("callback");
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			if(userVo==null){
				// pt.logout.timeout=로그인 세션이 종료 되었습니다.
				throw new CmException("pt.logout.timeout", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
					
			// 게시판관리(BA_BRD_B) 테이블 - BIND
			BaBrdBVo baBrdBVo = new BaBrdBVo();
			baBrdBVo.setQueryLang(langTypCd);
			bbBrdSvc.setCompId(request, baBrdBVo);  // 회사ID
			baBrdBVo.setOrderBy("T.BRD_ID ASC");
			
			// 게시판관리(BA_BRD_B) 테이블 - SELECT
			@SuppressWarnings("unchecked")
			List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
			
			// 모듈 별 권한 있는 모듈참조ID 목록
			List<String> mdIds = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, "BB", "W");
			
			List<BaBrdBVo> hasAuthBaBrdBVoList = new ArrayList<BaBrdBVo>();
			if(mdIds != null && baBrdBVoList != null){
				for(String mdId : mdIds){
					for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
						if(mdId.equals(storedBaBrdBVo.getBrdId())){
							hasAuthBaBrdBVoList.add(storedBaBrdBVo);
							break;
						}
					}
				}
			}
			result=JsonUtil.toJson(hasAuthBaBrdBVoList);
		} catch(CmException e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			result=getMessageJSON("error", e.getMessage());
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			result=getMessageJSON("error", e.getMessage());
		}
		
		if(result!=null && !result.isEmpty()){
			PrintWriter write = response.getWriter();
			write.write(callback+"(" + result + ")");
			//response.getOutputStream().write(result.getBytes());
			write.flush();
			write.close();
			return null;
		}
		
		response.sendError(403);
		return null;
	}
	
	/** [JSON] - 명함 보내기 */
	@RequestMapping(value = {"/cm/send/wb/sendBc", "/cm/send/wb/pub/sendBc"})
	public String sendBc( HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model) throws IOException {
		response.setContentType("text/xml;charset=utf-8");
		String result = null;
		String callback = request.getParameter("callback");
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			if(userVo==null){
				// pt.logout.timeout=로그인 세션이 종료 되었습니다.
				throw new CmException("pt.logout.timeout", request);
			}
			
			String bcNm = (String)request.getParameter("bcNm");
			if(bcNm==null || bcNm.isEmpty()){
				LOGGER.error("[ERROR] sendBc : bcNm is null!!");
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			if(StringUtil.chkMaxByte("30", StringUtil.getBytes(bcNm))){
				String subTitle = "(actual: "+StringUtil.getBytes(bcNm)+", maximum: 30)";
				String title = messageProperties.getMessage("cols.nm", request);
				//wb.msg.bcIn.NotDataLength.msg1={0} 이(가) 데이터 길이에 맞지 않습니다. (maximum:{1})
				String msg = messageProperties.getMessage("wb.msg.bcIn.NotDataLength.msg1", new String[]{title, subTitle}, request);				
				throw new CmException(msg);				
			}
			
			QueryQueue queryQueue = new QueryQueue();
			
			WbBcBVo wbBcBVo = new WbBcBVo();
			wbBcBVo.setCompId(userVo.getCompId());
			/** 공유명함여부 */
			boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
			wbBcBVo.setPub(isPub); // 공유명함여부
			wbBcBVo.setBcNm(bcNm); // 명함명
			String fldId = (String)request.getParameter("fldId"); // 폴더ID
			if(fldId!=null && !fldId.isEmpty()) wbBcBVo.setFldId(IdUtil.createId('F', Long.valueOf(fldId), 8));
			
			// 연락처 목록
			List<WbBcCntcDVo> wbBcCntcDVoList = null;
			if(request.getParameter("mbno")!=null || request.getParameter("email")!=null){
				wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
				
				WbBcCntcDVo wbBcCntcDVo = null;
				if(request.getParameter("mbno")!=null){
					wbBcBVo.setDftCntcTypCd("mbno"); // 기본연락처는 모바일로 설정
					wbBcCntcDVo = new WbBcCntcDVo();
					wbBcCntcDVo.setCntcTypCd("mbno");
					wbBcCntcDVo.setCntcClsCd("CNTC");					
					wbBcCntcDVo.setCntcCont((String)request.getParameter("mbno"));
					wbBcCntcDVoList.add(wbBcCntcDVo);
				}
				if(request.getParameter("email")!=null){
					wbBcCntcDVo = new WbBcCntcDVo();
					wbBcCntcDVo.setCntcTypCd("email");
					wbBcCntcDVo.setCntcClsCd("EMAIL");					
					wbBcCntcDVo.setCntcCont((String)request.getParameter("email"));
					wbBcCntcDVoList.add(wbBcCntcDVo);
				}
				wbBcBVo.setWbBcCntcDVo(wbBcCntcDVoList);
			}
			
			wbBcSvc.saveBc(request, queryQueue , wbBcBVo , userVo);
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			result=getMessageJSON("message", messageProperties.getMessage("cm.msg.save.success", request));
		} catch(CmException e){
			model.put("message", e.getMessage());
			result=getMessageJSON("error", e.getMessage());
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
			result=getMessageJSON("error", e.getMessage());
		}
		
		if(result!=null && !result.isEmpty()){
			PrintWriter write = response.getWriter();
			write.write(callback+"(" + result + ")");
			//response.getOutputStream().write(result.getBytes());
			write.flush();
			write.close();
			return null;
		}
		
		response.sendError(403);
		return null;
	}
	
	/** xml 메세지 */
	private String getMessage(String element, String msg){
		//최상위 element 생성
		Element root = new Element(element);
		root.addContent(msg);
		Document doc = new Document().setRootElement(root);
		XMLOutputter xo = new XMLOutputter();
		xo.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
		return xo.outputString(doc);
	}
	
	/** json 메세지 */
	@SuppressWarnings("unchecked")
	private String getMessageJSON(String key, String msg){
		JSONObject obj = new JSONObject();
		obj.put(key, msg);
		return obj.toString();
	}

}
