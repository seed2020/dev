package com.innobiz.orange.web.sh.ctrl;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.HttpClient;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.sh.svc.ShSrchSvc;
import com.innobiz.orange.web.sh.vo.ShSrchVo;

@Controller
public class ShSrchCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 검색 서비스 */
	@Autowired
	private ShSrchSvc shSrchSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 검색 페이지 */
	@RequestMapping(value = "/sh/index")
	public String index(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "kwd", required = false) String kwd,
			@RequestParam(value = "mdRid", required = false) String mdRid,
			@RequestParam(value = "mdBxId", required = false) String mdBxId,
			@RequestParam(value = "subjYn", required = false) String subjYn,
			@RequestParam(value = "bodyYn", required = false) String bodyYn,
			@RequestParam(value = "attchYn", required = false) String attchYn,
			@RequestParam(value = "pageNo", required = false) String pageNo,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			response.sendRedirect(PtConstant.URL_LOGIN);
			return null;
		}
		
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean withDM = "Y".equals(sysPlocMap.get("dmEnable"));
		boolean withMail = "Y".equals(sysPlocMap.get("mailSrchEnable"));
		if(withMail) model.put("mailSrchEnable", Boolean.TRUE);
		
		// 검색 모듈 설정 - 권한 여부 체크
		List<String> mdRidList = new ArrayList<String>();
		if(userVo.hasMnuGrpMdRidOf("AP")) mdRidList.add("AP");
		if(userVo.hasMnuGrpMdRidOf("BB")) mdRidList.add("BB");
		if(withDM && userVo.hasMnuGrpMdRidOf("DM")) mdRidList.add("DM");
		if(withMail && userVo.hasMnuGrpMdRidOf("MAIL")) mdRidList.add("MAIL");
		String[] mdRids = ArrayUtil.toArray(mdRidList);
		
		int[] mdSums = new int[mdRids.length];
		for(int i=0; i<mdSums.length; i++) mdSums[i] = 0;
		
		boolean allSearch = mdRid==null || mdRid.isEmpty() || !ArrayUtil.isInArray(mdRids, mdRid);
		Integer pageRowCnt = allSearch ? 10 : 20;
		
		model.put("mdRids", mdRids);
		if(allSearch){
			mdRid = null;
			mdBxId = null;
		} else {
			if(mdRid!=null) model.put("mdRid", mdRid);
			if(mdBxId!=null) model.put("mdBxId", mdBxId);
		}
		
		boolean noAreaParam = subjYn==null && bodyYn==null && attchYn==null;
		
		// 파라미터 세팅
		if(		noAreaParam
				|| ("Y".equals(subjYn) && "Y".equals(bodyYn) && "Y".equals(attchYn))){
			model.put("subjYn", Boolean.TRUE);
			model.put("bodyYn", Boolean.TRUE);
			model.put("attchYn", Boolean.TRUE);
		}
		
		String currMdRid, mdBxNm=null;
		if(kwd!=null && !kwd.isEmpty()){
			
			
			ShSrchVo shSrchVo;
			Integer countSum, currCount=0;
			
			for(int i=0; i<mdRids.length;i++){
				currMdRid = mdRids[i];
				
				// 메뉴의 권한이 없으면
				if(!userVo.hasMnuGrpMdRidOf(currMdRid)){
					model.put(currMdRid+"CountSum", Integer.valueOf(0));
					continue;
				}
				
				// 메일 검색의 경우
				if("MAIL".equals(currMdRid)){
					
					// 서버 설정 목록 조회
					Map<String, String> svrEnvMap = ptSysSvc.getSvrEnvMap();
					String mailDomain = svrEnvMap.get("mailCall");
					
					// 메일 카운트 조회 - url
					String url = "http://"+mailDomain
							+ "/zmail/api/search_mail_count.nvd"
							+ "?user_account="+userVo.getOdurUid()
							+ (noAreaParam || "Y".equals(subjYn) ? "&subject=Y" : "")
							+ (noAreaParam || "Y".equals(attchYn) ? "&attach=Y" : "")
							+ "&keyword="+URLEncoder.encode(kwd, "UTF-8");
					
					String strXml = null;
					XMLElement xmlElement = null;
					HttpClient client = new HttpClient();
					
					try{
						// 메일 카운트 조회 - url 호출로 xml 조회
						strXml = client.sendGet(url, "UTF-8");
						if(strXml!=null){
							strXml = strXml.trim();
							if(strXml.startsWith("<")){
								// xml 파싱 및 결과 추출
								xmlElement = SAXHandler.parse(strXml);
								String cnt = xmlElement.getValue("totalcount/count");
								if(cnt!=null && !cnt.isEmpty()) currCount = Integer.valueOf(cnt);
							}
						}
					} catch(Exception e){}
					
					if(currCount==null || currCount.intValue()==0){
						model.put(currMdRid+"CountSum", Integer.valueOf(0));
						continue;
					} else {
						if(allSearch){
							// 더보기 세팅
							if(currCount > pageRowCnt){
								model.put("more"+currMdRid, currCount);
								model.put("kwdEnc", URLEncoder.encode(kwd, "UTF-8"));
							}
						} else {
							// 페이징
							model.put("pageRowCnt", pageRowCnt);
							model.put("recodeCount"+currMdRid, currCount);
						}
					}
					
					mdSums[i] = currCount.intValue();
					model.put(currMdRid+"CountSum", currCount);
					
					if(allSearch || currMdRid.equals(mdRid)){
						
						PtMnuLoutDVo ptMnuLoutDVo = ptLoutSvc.getMnuLoutByMdRid(PtConstant.MNU_GRP_REF_MAIL, userVo.getLoutCatId(), userVo.getCompId(), userVo.getLangTypCd());
						
						model.put("mailDomain", mailDomain);
						model.put("mailUrl", ptMnuLoutDVo.getMnuUrl());
						
						Integer currentPage = 1;
						if(pageNo!=null && !pageNo.isEmpty()){
							try{ currentPage = Integer.valueOf(pageNo); }
							catch(Exception ignore){}
						}
						
						// 메일 목록 조회 - url
						url = "http://"+mailDomain
								+ "/zmail/api/search_mail.nvd"
								+ "?user_account="+userVo.getOdurUid()
								+ (noAreaParam || "Y".equals(subjYn) ? "&subject=Y" : "")
								+ (noAreaParam || "Y".equals(attchYn) ? "&attach=Y" : "")
								+ "&keyword="+URLEncoder.encode(kwd, "UTF-8")
								+ "&currentPage="+currentPage
								+ "&pageSize="+pageRowCnt
								;
						
						try{
							// 메일 목록 조회 - url 호출로 xml 조회
							strXml = client.sendGet(url, "UTF-8");
							if(strXml!=null){
								strXml = strXml.trim();
								if(strXml.startsWith("<")){
									// xml 파싱 및 결과 추출
									xmlElement = SAXHandler.parse(strXml);
									List<XMLElement> childList = xmlElement.getChildList(".");
									Integer index = null;
									String value;
									String[] subCutList = {"subject_link", "mailbox_name_link", "from_name_link"};
									
									XMLElement elFromMail;
									
									for(XMLElement el : childList){
										if(index == null){
											index = el.getValue(subCutList[0]).indexOf("/zmail/");
										}
										if(index>0){
											for(String elName : subCutList){
												value =  el.getValue(elName);
												if(value!=null && value.length()>index){
													el.getElem(elName).setValue(value.substring(index));
												}
												if(elName.equals("from_name_link")){
													elFromMail = new XMLElement("from_mail");
													elFromMail.setValue(value.substring(value.lastIndexOf("&to=")+4));
													el.addChild(elFromMail);
												}
											}
										}
									}
									
									model.put("navidXmlList"+currMdRid, childList);
/*
<navidmail>
	<subject>[결재]_001_대결을_위한_기_안_문</subject>
	<mailbox_name>받은메일함</mailbox_name>
	<from_name>김셋</from_name>
	<receive_time>201706130857</receive_time>
	<subject_link>http://mail.innogw.com:4040/zmail/login.nvd?cmd=mail_detail&mail_sn=1926&mailbox_code=1100</subject_link>
	<mailbox_name_link>http://mail.innogw.com:4040/zmail/login.nvd?cmd=receive_mailbox</mailbox_name_link>
	<from_name_link>http://mail.innogw.com:4040/zmail/login.nvd?cmd=write_mail_to&to=k03@innogw.com</from_name_link>
</navidmail>
 * */
								}
							}
						} catch(Exception e){}
						
					}
					
				// 메일 검색이 아닌 경우
				} else {

					/////////////////////////////////
					// 카운트 - (결재)대장별, (게시)게시판별
					
					// 검색VO 만들기
					shSrchVo = shSrchSvc.createSrchVo(currMdRid, kwd, userVo, null);
					if(shSrchVo==null){
						model.put(currMdRid+"CountSum", Integer.valueOf(0));
						continue;
					}
					shSrchVo.setInstanceQueryId("com.innobiz.orange.web.sh.dao.ShSrshDao.countShSrchByMdBx");
					// 검색 범위(제목,본문,첨부) 세팅
					if(		!noAreaParam
							&& !("Y".equals(subjYn) && "Y".equals(bodyYn) && "Y".equals(attchYn))){
						if("Y".equals(subjYn)) shSrchVo.setSubjYn(Boolean.TRUE);
						if("Y".equals(bodyYn)) shSrchVo.setBodyYn(Boolean.TRUE);
						if("Y".equals(attchYn)) shSrchVo.setAttchYn(Boolean.TRUE);
					}
					
					// 레코드 수 조회
					@SuppressWarnings("unchecked")
					List<ShSrchVo> countList = (List<ShSrchVo>)commonSvc.queryList(shSrchVo);
					
					// 레코드 수 조회 결과가 있으면
					if(countList!=null){
						
						countSum = 0;
						currCount = 0;
						for(ShSrchVo storedShSrchVo : countList){
							countSum += storedShSrchVo.getCnt();
							
							if(mdBxId!=null && mdBxId.equals(storedShSrchVo.getMdBxId())){
								currCount = storedShSrchVo.getCnt();
							}
						}
						model.put(currMdRid+"CountSum", Integer.valueOf(countSum));
						model.put(currMdRid+"CountList", countList);
						
						mdSums[i] = countSum.intValue();
						
						// 목록 뿌릴 데이터 조회
						if(allSearch || currMdRid.equals(mdRid)){
							
							shSrchVo.setInstanceQueryId("com.innobiz.orange.web.sh.dao.ShSrshDao.selectShSrch");
							if(mdBxId!=null){
								shSrchVo.setMdBxId(mdBxId);
								if(currCount>0){
									setPaging(shSrchVo, pageNo, pageRowCnt, currCount);
									
									@SuppressWarnings("unchecked")
									List<ShSrchVo> shSrchVoList = (List<ShSrchVo>)commonSvc.queryList(shSrchVo);
									if(shSrchVoList!=null){
										model.put("shSrchVoList"+currMdRid, shSrchVoList);
									}
									
									if(currCount > pageRowCnt){
										// 페이징
										model.put("pageRowCnt", pageRowCnt);
										model.put("recodeCount"+currMdRid, currCount);
									}
								}
							} else {
								if(countSum > 0){
									setPaging(shSrchVo, pageNo, pageRowCnt, countSum);
									
									@SuppressWarnings("unchecked")
									List<ShSrchVo> shSrchVoList = (List<ShSrchVo>)commonSvc.queryList(shSrchVo);
									if(shSrchVoList!=null){
										model.put("shSrchVoList"+currMdRid, shSrchVoList);
									}
									
									if(allSearch){
										// 더보기 세팅
										if(countSum > pageRowCnt){
											model.put("more"+currMdRid, countSum);
											model.put("kwdEnc", URLEncoder.encode(kwd, "UTF-8"));
										}
									} else {
										// 페이징
										model.put("pageRowCnt", pageRowCnt);
										model.put("recodeCount"+currMdRid, countSum);
									}
								}
							}
						}
						
					} else {
						model.put(currMdRid+"CountSum", Integer.valueOf(0));
					}
					
				}
				
			}
		}
		
		// 검색 타이틀 설정
		String title1 = messageProperties.getMessage("pt.title.integratedSearch", request);
		if(!allSearch){
			
			if(mdBxNm==null && mdBxId!=null){
				UserVo adminVo = ptSecuSvc.createEmptyAdmin(userVo.getCompId(), userVo.getLoutCatId(), userVo.getLangTypCd());
				ArrayList<PtMnuDVo> mnuList = ptSecuSvc.getAuthedMnuVoListByMdRid(adminVo, mdRid);
				if(mnuList != null){
					for(PtMnuDVo ptMnuDVo : mnuList){
						if(mdBxId.equals(ptMnuDVo.getMdId())){
							mdBxNm = ptMnuDVo.getRescNm();
						}
					}
				}
			}
			
			try{
				if(mdBxId==null || mdBxNm==null){
					model.put("titleValue", title1+" - "
							+messageProperties.getMessage("pt.sh.menu."+mdRid, request));
				} else {
					model.put("titleValue", title1+" - "
							+messageProperties.getMessage("pt.sh.menu."+mdRid, request) + " / "
							+mdBxNm);
				}
			} catch(Exception ignore){
				model.put("titleValue", title1);
			}
		} else {
			model.put("titleValue", title1);
		}
		if(allSearch) model.put("allSearch", Boolean.TRUE);
		
		return LayoutUtil.getJspPath("/sh/index");
	}
	
	/** 페이징 처리 */
	private void setPaging(CommonVo commonVo, String pageNo, Integer pageRowCnt, Integer recodeCount) throws SQLException{
		commonVo.setPageNo(1);
		commonVo.setPageRowCnt(pageRowCnt);
		if(pageNo!=null && !pageNo.isEmpty()){
			try{
				commonVo.setPageNo(Math.max(Integer.parseInt(pageNo), 1));
			} catch(Exception ignore){}
		}
		if(recodeCount!=null){
			int currPage = (recodeCount / pageRowCnt)+1;
			if(commonVo.getPageNo() > currPage) commonVo.setPageNo(currPage);
		}
	}
}
