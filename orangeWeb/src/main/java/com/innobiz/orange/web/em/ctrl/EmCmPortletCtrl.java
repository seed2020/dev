package com.innobiz.orange.web.em.ctrl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.InputSource;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.em.svc.EmailSvc;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.sync.SyncConstant;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;

/** 메일 */
@Controller
public class EmCmPortletCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** SSO 처리용 서비스 */
//	@Autowired
//	private PtSsoSvc ptSsoSvc;
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
//	/** 메세지 처리용 프라퍼티 - 다국어 */
//	@Autowired
//	private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 이메일 서비스 */
	@Resource(name = "emEmailSvc")
	private EmailSvc emailSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmCmPortletCtrl.class);
	
	/** [TAB]이메일  */
	@RequestMapping(value = "/em/plt/listEmailTabPlt")
	public String listEmailTabPlt(HttpServletRequest request,
			@RequestParam(value = "tId", required = false) String tId,
			@RequestParam(value = "tabId", required = false) String tabId,
			ModelMap model) throws Exception {
		
		tId = "notread,recent";
		if(tId == null || tId.isEmpty()) tId = "notread";//개인명함
		String[] tIds = tId.split(",");
		
		List<String[]> tabList = new ArrayList<String[]>();
		String[] tabs = null;
		
		for(int i=0;i<tIds.length;i++){
			if("notread".equals(tIds[i])) { tabs = new String[2]; tabs[0] = tIds[i]; tabs[1] = tIds[i]; tabList.add(tabs);}
			else if("recent".equals(tIds[i])) { tabs = new String[2]; tabs[0] = tIds[i]; tabs[1] = tIds[i]; tabList.add(tabs);}
		}
		
		if(tabList.size() == 0 ){
			// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
			throw new CmException("pt.msg.nodata.passed", request);
		}
		if(tabId == null || tabId.isEmpty()) tabId = tabList.get(0)[0];
		model.put("tId", tabId);
		model.put("tabList", tabList);
		model.put("paramsForList", ParamUtil.getQueryString(request, "tId", "hghtPx"));
		
		UserVo userVo = LoginSession.getUser(request);
		PtMnuLoutDVo ptMnuLoutDVo = ptLoutSvc.getMnuLoutByMdRid(PtConstant.MNU_GRP_REF_MAIL, userVo.getLoutCatId(), userVo.getCompId(), userVo.getLangTypCd());
		if(ptMnuLoutDVo != null){
			model.put("recentUrl", ptMnuLoutDVo.getMnuUrl());
		}
		
		return LayoutUtil.getJspPath("/em/plt/listEmailTabPlt");
	}
	
	/** [FRAME]이메일 목록 조회 */
	@RequestMapping(value = "/em/plt/listEmailTabFrm")
	public String listEmailTabFrm(HttpServletRequest request,
			@RequestParam(value = "hghtPx", required = false) String hghtPx,
			@RequestParam(value = "pageCnt", required = false) String pageCnt,
			@RequestParam(value = "titleYn", required = false) String titleYn,
			@RequestParam(value = "pagingYn", required = false) String pagingYn,
			@RequestParam(value = "colYn", required = false) String colYn,
			@RequestParam(value = "mode", required = false) String mode,
			ModelMap model) throws Exception {
		
		/**
		 *  포틀릿의 height를 기준으로 rowcount를 계산한다.
		 */
		int ptlHght = hghtPx==null || hghtPx.isEmpty() ? 0 : Integer.parseInt(hghtPx);
		int tabHght = ptlHght - 35 - (!"N".equals(colYn) ? 22 : 0 );
		int rowCnt = Math.max(1, (int)Math.floor(tabHght / 22));
		request.setAttribute("pageRowCnt", rowCnt);//RowCnt 삽입

		if(mode == null || mode.isEmpty()) mode = "notread";
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		//이메일 서비스가 없을경우
		if(!emailSvc.isInService() || !(sysPlocMap!=null && "Y".equals(sysPlocMap.get("mailEnable"))))
			return LayoutUtil.getJspPath("/em/plt/listEmailTabFrm");
		String url=null;		
		try{
			HttpClient http = new HttpClient();
			Map<String, String> header = new HashMap<String, String>();
			header.put("User-Agent", SyncConstant.USER_AGENT+"/"+SyncConstant.VERSION);
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("userUid",userVo.getOdurUid());
			param.put("listCnt",rowCnt+"");
			param.put("mode",mode);
			
			// 서버 설정 목록 조회
			Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
			
			// 요청 URL
			String domain=svrEnvMap.get("mailCall");
			
			int p = domain.indexOf(':');
			if(p>0) {
				domain = domain.substring(0, p)+":4040";
			}else{
				domain+=":4040";
			}
			url = "http://"+domain+"/zmail/api/user_mailbox_list.nvd";
			
			//메일 요청 url
			//String url = "http://"+svrEnvMap.get("mailCall")+"/zmail/api/user_mailbox_list.nvd";
			String rsltStr = http.sendPost(url, param, header, "UTF-8");
			
			if(rsltStr == null || "".equals(rsltStr) || rsltStr.indexOf("ERR") > -1) return LayoutUtil.getJspPath("/em/plt/listEmailTabFrm");
			SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new InputSource(new StringReader(rsltStr)));
            Element root = doc.getRootElement();
            
            List<Element> list = root.getChildren("navidmail");
            if(list.size() == 0 ) return LayoutUtil.getJspPath("/em/plt/listEmailTabFrm");
            
        	Iterator<Element> iter = list.iterator();
	        //목록 컬럼명
            String[] cols = {"mail_sn","mailbox_code","from_name","subject","receive_time"};
            
            List<Map<String,String>> rsltMapList = new ArrayList<Map<String,String>>();
            Map<String,String> rsltMap = null;
            Integer recodeCount = 0;
            while(iter.hasNext()){
            	Element el = iter.next();
            	rsltMap = new HashMap<String,String>();
            	for(int i=0;i<cols.length;i++){
            		rsltMap.put(StringUtil.toCamelNotation(cols[i],false), el.getChild(cols[i]).getText());
            	}
            	recodeCount++;
            	rsltMapList.add(rsltMap);
            }
            model.put("rsltMapList", rsltMapList);
            
			//목록 조회 건수
            //CommonVoImpl commonVoImpl = new CommonVoImpl();
			//PersonalUtil.setPaging(request, commonVoImpl, recodeCount);
			//model.put("recodeCount", recodeCount);
			
		}catch (Exception e) {
			LOGGER.error("MAIL PORTLET ERROR - url:"+url);
			LOGGER.error("MAIL PORTLET ERROR - userUid:"+userVo.getUserUid()+" - "
					+ e.getClass().getCanonicalName() +": "+ e.getMessage());
			//e.printStackTrace();
		}
		return LayoutUtil.getJspPath("/em/plt/listEmailTabFrm");
	}
	
}
