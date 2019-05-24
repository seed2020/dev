package com.innobiz.orange.web.em.ctrl;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.ap.svc.ApDocTransSvc;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdAttFileLVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.ap.vo.ApOngdBodyLVo;
import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullFileDVo;
import com.innobiz.orange.web.bb.vo.BaBullTgtDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmDocSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmFileDVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.em.vo.CmSrchBVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;

/*
<list>
	<!--
	mdRid		모듈참조ID - BB:게시, AP:결재, DM:문서관리
	mdBxId		함ID - 게시:게시판ID, 결재:대장ID
	mdId		모듈ID - 게시물ID, 결재번호 - 테이블의 키
	subj		제목
	url			호출URL
	actId		액션구분 - I:INSERT, U:UPDATE, D:DELETE, C:(카테고리,권한) 변경
	seculCd		보안등급 (문서에 걸지 않으면 : none)
	exprYmd		만료년월일 - 옵션(게시의 경우만 있으며 만료일 이후 문서 출력 안함)
	regrUid		등록자UID
	-->
	<item mdRid="" mdBxId="" mdId="" subj="" regDt="" url="" actId="" seculCd="" exprYmd="" regrUid="">
		<resource support="ko,en">
			<!--
				mdBx:(게시판,함)명
				owner:등록자명
				ownerDept:등록부서(발송부서)
			-->
			<mdBx ko="" en="" />
			<owner ko="" en="" />
			<ownerDept ko="" en="" />
		</resource>
		
		<category>
			<!-- and 검색조건 / key:value 구조 -->
			<!-- 구동방식:후륜구동, 엔진방식:하이브리드 -->
			<someCategory>categoryValue</someCategory>
			<someCategory2>categoryValue2</someCategory2>
		</category>
		
		<authority>
			<!-- 권한  검색조건 / key:value 구조 -->
			<!--
			[ 목록 출력 조건 ]
			1. user 에 해당 사용자가 있으면 출력 (보안등급 관련 없음)
			2. 문서 보안등급이 사용자보안등급 이하 이고, (dept에 사용자 부서가 있거나, deptSub에 사용자 (자기 부서포함)상위부서가 있을 때)  
			
			파라미터로 넘겨주는 값은 하나 이상 일 수 있으며, 그중에  하나라도 있으면 OK
			noAuth : 권한없음 - 게시판의 경우(사용자, 부서 선택 안함)
			user : 사용자UID
			dept : 부서ID - 콤마(,)구분 / 전사에서 볼 수 있는 경우(NO_DEPT) 넣어줌
			deptSub : 하위부서 - 콤마(,)구분
			-->
			<noAuth>Y</noAuth>
			<user>value,value</user>
			<dept>value,value</dept>
			<deptSub>value,value</deptSub>
		</authority>
		<body>HTML</body>
		<attaches>
			<!-- dispNm:표시명, path:경로 -->
			<attach dispNm="" path="" />
		</attaches>
	</item>
</list>
*/
/** 검색엔진 색인용 메타 XML 생성 컨트롤러 */
@Controller
public class EmSrchIndexCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmSrchIndexCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 게시판관리 서비스 */
	@Autowired
	private BbBrdSvc bbBrdSvc;
	
	/** 게시물 서비스 */
	@Autowired
	private BbBullSvc bbBullSvc;
	
	/** 문서 저장소 서비스 */
	@Autowired
	private DmStorSvc dmStorSvc;
	
	/** 문서 서비스 */
	@Resource(name = "dmDocSvc")
	private DmDocSvc dmDocSvc;
	
	/** 문서 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
//	/** 포털 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 문서 저장 서비스 */
	@Autowired
	private ApDocTransSvc apDocTransSvc;

	/** 전 부서 공개 여부 */
	private static final String NO_DEPT = "NO_DEPT";
	
	@RequestMapping(value = "/cm/search/queryIndex")
	public String queryIndex(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="pageNo", required=false) String pageNo,
			@Parameter(name="pageRowCnt", required=false) String pageRowCnt,
			@Parameter(name="skipFirst", required=false) String skipFirst,
			ModelMap model) throws Exception {
		
		try {
		
			boolean pagingDone = false;
			CmSrchBVo cmSrchBVo = new CmSrchBVo();
			if(pageNo != null && pageRowCnt!=null){
				try{
					cmSrchBVo.setPageRowCnt(Integer.parseInt(pageRowCnt));
					cmSrchBVo.setPageNo(Integer.parseInt(pageNo));
					pagingDone = true;
				} catch(Exception ignore){}
			}
			if(!pagingDone){
				cmSrchBVo.setPageRowCnt(5);
				cmSrchBVo.setPageNo(1);
			}
			cmSrchBVo.setOrderBy("SRCH_NO");
			
			@SuppressWarnings("unchecked")
			List<CmSrchBVo> cmSrchBVoList = (List<CmSrchBVo>)commonSvc.queryList(cmSrchBVo);
			
			if(cmSrchBVoList==null){
				response.setContentType("application/xhtml+xml");
				PrintWriter writer = response.getWriter();
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><list></list>");
				writer.close();
				return null;
			}
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("list");
			doc.appendChild(rootElement);
			
			Element item;
			Attr attr;
			
			String attrVa;
			Map<String, Object> cmSrchMap;
			String[] itemAttrs = { "srchNo", "mdRid", "mdBxId", "mdId", "url", "actId", "compId" };
			
			int skipCount=0, skipRequest=0;
			if(skipFirst!=null && !skipFirst.isEmpty()){
				try{ skipRequest = Integer.parseInt(skipFirst); }catch(Exception ignore){}
			}
			
			boolean itemAppended = false;
			
			for(CmSrchBVo storedCmSrchBVo : cmSrchBVoList){
				
				if(skipRequest>0){
					if(skipCount++<skipRequest) continue;
				}
				
				// item 생성
				item = doc.createElement("item");
				
				// item 어트리뷰트 세팅
				cmSrchMap = VoUtil.toMap(storedCmSrchBVo, null);
				for(String attrNm : itemAttrs){
					attr = doc.createAttribute(attrNm);
					attrVa = (String)cmSrchMap.get(attrNm);
					attr.setValue(attrVa==null ? "" : attrVa);
					item.setAttributeNode(attr);
				}
				
				if("AP".equals(storedCmSrchBVo.getMdRid())){
					if(processAp(doc, item, storedCmSrchBVo)){
						rootElement.appendChild(item);
						itemAppended = true;
					} else {
						deleteSrchData(storedCmSrchBVo.getSrchNo());
					}
				} else if("BB".equals(storedCmSrchBVo.getMdRid())){
					if("D".equals(storedCmSrchBVo.getActId()) || processBB(doc, item, storedCmSrchBVo)){
						rootElement.appendChild(item);
						itemAppended = true;
					} else {
						deleteSrchData(storedCmSrchBVo.getSrchNo());
					}
				} else if("DM".equals(storedCmSrchBVo.getMdRid())){
					if("D".equals(storedCmSrchBVo.getActId()) || processDM(doc, item, storedCmSrchBVo)){
						rootElement.appendChild(item);
						itemAppended = true;
					} else {
						deleteSrchData(storedCmSrchBVo.getSrchNo());
					}
				}
				
			}
			
			if(itemAppended){
	//			response.setContentType("application/xml");
				response.setContentType("application/xhtml+xml");
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				
				DOMSource source = new DOMSource(doc);
				ServletOutputStream outputStream = response.getOutputStream();
				StreamResult result = new StreamResult(outputStream);
				transformer.transform(source, result);
				outputStream.close();
				return null;
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		response.setContentType("application/xhtml+xml");
		PrintWriter writer = response.getWriter();
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><list></list>");
		writer.close();
		return null;
	}
	
	/** 인덱싱 메타 데이터 삭제 */
	@RequestMapping(value = "/cm/search/responseIndex")
	public String responseIndex(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="srchNos", required=false) String srchNos,
			ModelMap model) throws Exception {
		
		if(srchNos!=null && !srchNos.isEmpty()){
			
			CmSrchBVo cmSrchBVo;
			QueryQueue queryQueue = new QueryQueue();
			for(String srchNo : srchNos.split(",")){
				cmSrchBVo = new CmSrchBVo();
				cmSrchBVo.setSrchNo(srchNo);
				queryQueue.delete(cmSrchBVo);
			}
			
			commonSvc.execute(queryQueue);
		}
		
		response.setContentType("application/xml");
		PrintWriter writer = response.getWriter();
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><result>ok</result>");
		writer.close();
		return null;
	}
	
	
	/** 결재 검색 정보 생성 */
	private boolean processAp(Document doc, Element item, CmSrchBVo cmSrchBVo) throws SQLException{
		
		String apvNo = cmSrchBVo.getMdId();
		
		// 저장소 조회
		String storage = apCmSvc.queryStorage(apvNo);
		
		// 진행문서기본(AP_ONGD_B) 테이블 - 조회
		ApOngdBVo apOngdBVo = new ApOngdBVo();
		apOngdBVo.setApvNo(apvNo);
		apOngdBVo.setStorage(storage);
		apOngdBVo = (ApOngdBVo)commonSvc.queryVo(apOngdBVo);
		if(apOngdBVo==null) return false;
		
		// 대장구분코드 - regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		String recLstTypCd = apOngdBVo.getRecLstTypCd();
		
		boolean recvBx = "recvRecLst".equals(recLstTypCd);// 접수대장
		boolean distBx = "distRecLst".equals(recLstTypCd);// 배부대장
		boolean regBx  = "regRecLst".equals(recLstTypCd);// 등록 대장
		// 유통문서
		boolean secondaryDoc = recvBx || distBx;
		// 문서상태코드 - temp:임시저장, retrvMak:기안회수, byOne:1인결재, mak:기안, revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, pred:전결, apv:결재, apvd:승인, rejt:반려, hold:보류, reRevw:재검토, dist:배부, recv:접수, makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람, cmplVw:공람완료
		String docStatCd = apOngdBVo.getDocStatCd();
		// 반려여부
		boolean rejected = "rejt".equals(docStatCd);
		
		// 대장문서가 아니면 리턴
		if(!secondaryDoc && !regBx) return false;
		
		Element sub;
		Attr attr;
		
		//////////////////////////////////////
		//
		//    item
		
		// 제목: subj
		attr = doc.createAttribute("subj");
		attr.setValue(apOngdBVo.getDocSubj());
		item.setAttributeNode(attr);
		
		// 등록일: regDt
		String regDt = secondaryDoc ? apOngdBVo.getRecvDt() : apOngdBVo.getMakDt();
		attr = doc.createAttribute("regDt");
		attr.setValue(regDt);
		item.setAttributeNode(attr);
		
		// 보안등급: seculCd - 설정 없으면 : none
		String seculCd = apOngdBVo.getSeculCd();
		if(seculCd==null || seculCd.isEmpty()) seculCd = "none";
		attr = doc.createAttribute("seculCd");
		attr.setValue(seculCd);
		item.setAttributeNode(attr);
		
		// 등록자UID
		attr = doc.createAttribute("regrUid");
		attr.setValue(apOngdBVo.getMakrUid());
		item.setAttributeNode(attr);
		
		// 만료일: exprYmd - 게시만료일
		
		//////////////////////////////////////
		//
		//    resource - 어권별 정보 - 대장명, 등록자명, 부서명
		Element resource = doc.createElement("resource");
		item.appendChild(resource);
		
		// 지원 언어 목록
		ArrayList<String> langList = new ArrayList<String>();
		
		// 발송기관명  - <ownerDept ko="" en="" />
		if(secondaryDoc && apOngdBVo.getSendInstId()!=null){//recvRecLst:접수 대장, distRecLst:배부 대장
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(apOngdBVo.getSendInstId());
			orOrgBVo = (OrOrgBVo)commonSvc.queryVo(orOrgBVo);
			
			String rescId = orOrgBVo==null ? null : orOrgBVo.getRescId();
			
			if(rescId!=null && !rescId.isEmpty()){
				sub = createOrRescElement(doc, "ownerDept", rescId, langList);
				if(sub != null){
					resource.appendChild(sub);
				}
			}
			
		// 등록자  - <owner ko="" en="" />
		} else {
			
			if(apOngdBVo.getMakrUid() != null && !apOngdBVo.getMakrUid().isEmpty()){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(apOngdBVo.getMakrUid());
				orUserBVo.setQueryLang(apOngdBVo.getDocLangTypCd());
				orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
				
				String rescId = orUserBVo==null ? null : orUserBVo.getRescId();
				if(rescId!=null && !rescId.isEmpty()){
					sub = createOrRescElement(doc, "owner", rescId, langList);
					if(sub != null){
						resource.appendChild(sub);
					}
				}
			} else {
				if(LOGGER.isEnabledFor(Level.WARN)){
					LOGGER.warn("AP maker is null - apvNo:"+apOngdBVo.getApvNo());
				}
			}
		}
		
		// 대장명(게시판명) - <mdBx ko="" en="" />
		if(cmSrchBVo.getMdBxRescId()!=null && !cmSrchBVo.getMdBxRescId().isEmpty()){
			sub = createPtRescElement(doc, "mdBx", cmSrchBVo.getMdBxRescId(), langList);
			if(sub != null){
				resource.appendChild(sub);
			}
		}
		
		// support 세팅 - 지원 언어 목록
		if(!langList.isEmpty()){
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for(String lang : langList){
				if(first) first = false;
				else builder.append(',');
				builder.append(lang);
			}
			
			attr = doc.createAttribute("support");
			attr.setValue(builder.toString());
			resource.setAttributeNode(attr);
		}
		

		//////////////////////////////////////
		//
		//    category
		
		// 카테고리 : item/category
		Element category = doc.createElement("category");
		item.appendChild(category);
		
		// 분류정보(clsInfoId)
		String clsInfoId = apOngdBVo.getClsInfoId();
		if(clsInfoId!=null && !clsInfoId.isEmpty()){
			sub = doc.createElement("clsInfoId");
			sub.appendChild(doc.createTextNode(clsInfoId));
			category.appendChild(sub);
		}
		
		//////////////////////////////////////
		//
		//    authority - 권한
		
		// 권한 : item/authority
		Element authority = doc.createElement("authority");
		item.appendChild(authority);
		
//		boolean allReadDoc = false;
		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<String> deptList = new ArrayList<String>();
		
		// 권한없음: 게시에서만 사용
		// - <noAuth>Y</noAuth>
		
		// 등록대장 문서이고, 전체공개여부:Y 인 경우
		if("Y".equals(apOngdBVo.getAllReadYn()) && regBx){
//			allReadDoc = true;
			deptList.add(NO_DEPT);// 전부서 공개
//			sub = doc.createElement("noAuth");
//			sub.appendChild(doc.createTextNode("Y"));
//			authority.appendChild(sub);
		}
		
		// 대장부서ID
		// - <dept>O0000004</dept>
		String recLstDeptId = apOngdBVo.getRecLstDeptId();
		if(recLstDeptId!=null && !recLstDeptId.isEmpty()){
			deptList.add(recLstDeptId);
		}
		
		// 결재선
		if(!secondaryDoc){// 접수, 배부 문서가 아니면
			
			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setStorage(storage);
			apOngdApvLnDVo.setQueryLang(apOngdBVo.getDocLangTypCd());
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apOngdApvLnDVoList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
			
			if(apOngdApvLnDVoList != null){
				
				// 결재자역할코드 - byOne:1인결재, mak:기안, revw:검토, 
				//		psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, 
				//		prcDept:처리부서, byOneAgr:합의1인결재, makAgr:합의기안, 
				//		abs:공석, apv:결재, pred:전결, entu:결재안함(위임), 
				//		postApvd:사후보고(후열), psnInfm:개인통보, deptInfm:부서통보, 
				//		makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람
				String apvrRoleCd;
				
				// 결재상태코드 - befoApv:결재전, inApv:결재중, apvd:승인, rejt:반려, 
				//		befoAgr:합의전, inAgr:합의중, cons:반대, pros:찬성, 
				//		hold:보류, cncl:취소, reRevw:재검토, 
				//		inInfm:통보중, befoVw:공람전, inVw:공람중, cmplVw:공람완료
				String apvStatCd;
				
				String apvrUid;
				String apvDeptId;
				
				for(ApOngdApvLnDVo storedApOngdApvLnDVo : apOngdApvLnDVoList){
					
					apvrRoleCd = storedApOngdApvLnDVo.getApvrRoleCd();
					apvStatCd = storedApOngdApvLnDVo.getApvStatCd();
					// 부서합의 제외, 처리부서 제외
					if(ArrayUtil.isInArray(new String[]{"deptOrdrdAgr", "deptParalAgr", "prcDept"}, apvrRoleCd)){
						continue;
					}
					
					// 결재선이 부서이면
					if("Y".equals(storedApOngdApvLnDVo.getApvrDeptYn())){
						apvDeptId = storedApOngdApvLnDVo.getApvDeptId();
						
						// 부서통보 - 부서권한 더하기
						if("deptInfm".equals(apvrRoleCd) && apvDeptId!=null && !apvDeptId.isEmpty() && !rejected){
							if(!deptList.contains(apvDeptId)){
								deptList.add(apvDeptId);
							}
						}
					} else {
						// befoApv:결재전, befoAgr:합의전
						if(ArrayUtil.isInArray(new String[]{"befoApv", "befoAgr"}, apvStatCd)){
							continue;
						}
						
						// 문서상태코드 - mak:기안
						if(rejected && !"mak".equals(docStatCd)){
							continue;
						}
						
						apvrUid = storedApOngdApvLnDVo.getApvrUid();
						if(apvrUid!=null && !apvrUid.isEmpty()){
							if(!userList.contains(apvrUid)){
								userList.add(apvrUid);
							}
						}
						
					}
				}
			}
			
		}
		// 부서
		if(!deptList.isEmpty()){
			sub = doc.createElement("dept");
			sub.appendChild(doc.createTextNode(getStringFromList(deptList)));
			authority.appendChild(sub);
		}
		// 사용자
		if(!userList.isEmpty()){
			sub = doc.createElement("user");
			sub.appendChild(doc.createTextNode(getStringFromList(userList)));
			authority.appendChild(sub);
		}
		
		
		//////////////////////////////////////
		//
		//    body - 본문 HTML
		
		// <body>HTML</body>
		Element body = doc.createElement("body");
		item.appendChild(body);
		
		ApOngdBodyLVo apOngdBodyLVo = new ApOngdBodyLVo();
		apOngdBodyLVo.setApvNo(apvNo);
		apOngdBodyLVo.setBodyHstNo(apOngdBVo.getBodyHstNo());
		apOngdBodyLVo.setStorage(storage);
		apOngdBodyLVo = (ApOngdBodyLVo)commonSvc.queryVo(apOngdBodyLVo);
		if(apOngdBodyLVo != null){
			//이관 데이터 오류 방지
			String bodyHtml = apOngdBodyLVo.getBodyHtml();
			if(bodyHtml!=null) bodyHtml = bodyHtml.replace((char)0, ' ');
			body.appendChild(doc.createTextNode(bodyHtml));
		}

		//////////////////////////////////////
		//
		//    attaches - 첨부
		
		// <attaches>
		//   <!-- dispNm:표시명, path:경로 -->
		//   <attach dispNm="" path="" />
		// </attaches>
		Element attaches = doc.createElement("attaches");
		item.appendChild(attaches);
		
		ApOngdAttFileLVo apOngdAttFileLVo = new ApOngdAttFileLVo();
		apOngdAttFileLVo.setApvNo(apvNo);
		apOngdAttFileLVo.setAttHstNo(apOngdBVo.getAttHstNo());
		apOngdAttFileLVo.setStorage(storage);
		@SuppressWarnings("unchecked")
		List<ApOngdAttFileLVo> apOngdAttFileLVoList = (List<ApOngdAttFileLVo>)commonSvc.queryList(apOngdAttFileLVo);
		if(apOngdAttFileLVoList != null){
			
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			for(ApOngdAttFileLVo storedApOngdAttFileLVo : apOngdAttFileLVoList){
				
				sub = doc.createElement("attach");
				attaches.appendChild(sub);
				// dispNm
				attr = doc.createAttribute("dispNm");
				attr.setValue(storedApOngdAttFileLVo.getAttDispNm());
				sub.setAttributeNode(attr);
				// path
				attr = doc.createAttribute("path");
				attr.setValue(wasCopyBaseDir+storedApOngdAttFileLVo.getFilePath());
				sub.setAttributeNode(attr);
			}
		}
		return true;
	}
	
	/** 게시 검색 정보 생성 */
	private boolean processBB(Document doc, Element item, CmSrchBVo cmSrchBVo) throws Exception{
		
		String brdId = cmSrchBVo.getMdBxId();
		
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo("ko", brdId);
		
		if(baBrdBVo==null) return false;
		
		String bullId = cmSrchBVo.getMdId();
		
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), "ko");
		if(bbBullLVo==null) return false;
		
		Element sub;
		Attr attr;
		
		//////////////////////////////////////
		//
		//    item
		
		// 제목: subj
		attr = doc.createAttribute("subj");
		attr.setValue(bbBullLVo.getSubj());
		item.setAttributeNode(attr);
		
		// 등록일: regDt
		String regDt = bbBullLVo.getRegDt();
		attr = doc.createAttribute("regDt");
		attr.setValue(regDt);
		item.setAttributeNode(attr);
		
		// 보안등급: seculCd - 설정 없으면 : none
		attr = doc.createAttribute("seculCd");
		attr.setValue("none");
		item.setAttributeNode(attr);
		
		// 등록자UID
		attr = doc.createAttribute("regrUid");
		attr.setValue(bbBullLVo.getRegrUid());
		item.setAttributeNode(attr);
		
		// 만료일: exprYmd - 게시만료일
		String exprYmd = bbBullLVo.getBullExprDt() != null && !bbBullLVo.getBullExprDt().isEmpty() ? bbBullLVo.getBullExprDt() : "" ;
		attr = doc.createAttribute("exprYmd");
		attr.setValue(exprYmd);
		item.setAttributeNode(attr);
		
		/*if("D".equals(cmSrchBVo.getActId())){
			return true;
		}*/
		//////////////////////////////////////
		//
		//    resource - 어권별 정보 - 대장명, 등록자명, 부서명
		Element resource = doc.createElement("resource");
		item.appendChild(resource);
		
		// 지원 언어 목록
		ArrayList<String> langList = new ArrayList<String>();
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(bbBullLVo.getRegrUid());
		orUserBVo.setQueryLang("ko");
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		
		String rescId = orUserBVo==null ? null : orUserBVo.getRescId();
		if(rescId!=null && !rescId.isEmpty()){
			sub = createOrRescElement(doc, "owner", rescId, langList);
			if(sub != null){
				resource.appendChild(sub);
			}
		}
		
		// 대장명(게시판명) - <mdBx ko="" en="" />
		if(cmSrchBVo.getMdBxRescId()!=null && !cmSrchBVo.getMdBxRescId().isEmpty()){
			sub = createPtRescElement(doc, "mdBx", cmSrchBVo.getMdBxRescId(), langList);
			if(sub != null){
				resource.appendChild(sub);
			}
		}
		
		// support 세팅 - 지원 언어 목록
		if(!langList.isEmpty()){
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for(String lang : langList){
				if(first) first = false;
				else builder.append(',');
				builder.append(lang);
			}
			
			attr = doc.createAttribute("support");
			attr.setValue(builder.toString());
			resource.setAttributeNode(attr);
		}
		

		//////////////////////////////////////
		//
		//    category
		
		// 카테고리 : item/category
		Element category = doc.createElement("category");
		item.appendChild(category);
		
		// 분류정보(clsInfoId)
		String catId = bbBullLVo.getCatId();
		if(catId!=null && !catId.isEmpty()){
			sub = doc.createElement("catId");
			sub.appendChild(doc.createTextNode(catId));
			category.appendChild(sub);
		}
		
		//////////////////////////////////////
		//
		//    authority - 권한
		
		// 권한 : item/authority
		Element authority = doc.createElement("authority");
		item.appendChild(authority);
		
		// 권한없음: 게시대상(사용자, 부서)이 지정되지 않은 경우
		// - <noAuth>Y</noAuth>
		boolean noAuth = false;
		if((bbBullLVo.getTgtDeptYn() == null || "N".equals(bbBullLVo.getTgtDeptYn())) &&
				(bbBullLVo.getTgtUserYn() == null || "N".equals(bbBullLVo.getTgtUserYn()))){
			sub = doc.createElement("noAuth");
			sub.appendChild(doc.createTextNode("Y"));
			authority.appendChild(sub);
			noAuth = true;
		}
		
		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<String> deptList = new ArrayList<String>();
		ArrayList<String> deptWithList = new ArrayList<String>();
		
		//대상 부서 설정 'Y'
		if ("Y".equals(bbBullLVo.getTgtDeptYn())) {
			// 게시대상(BA_BULL_TGT_D) 테이블 - SELECT
			BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
			baBullTgtDVo.setBullId(bbBullLVo.getBullId());
			baBullTgtDVo.setTgtTyp("D");
			@SuppressWarnings("unchecked")
			List<BaBullTgtDVo> baBullTgtDVoList = (List<BaBullTgtDVo>) commonSvc.queryList(baBullTgtDVo);
			if(baBullTgtDVoList.size() > 0){
				for(BaBullTgtDVo storedBaBullTgtDVo : baBullTgtDVoList){
					if (storedBaBullTgtDVo.getWithSubYn() != null && "Y".equals(storedBaBullTgtDVo.getWithSubYn())) {
						if(!deptWithList.contains(storedBaBullTgtDVo.getTgtId())){
							deptWithList.add(storedBaBullTgtDVo.getTgtId());
						}
					}else{
						if(!deptList.contains(storedBaBullTgtDVo.getTgtId())){
							deptList.add(storedBaBullTgtDVo.getTgtId());
						}
					}
					
				}
			}
		}
		
		//대상 사용자 설정 'Y'
		if ("Y".equals(bbBullLVo.getTgtUserYn())) {
			// 게시대상(BA_BULL_TGT_D) 테이블 - SELECT
			BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
			baBullTgtDVo.setBullId(bbBullLVo.getBullId());
			baBullTgtDVo.setTgtTyp("U");
			@SuppressWarnings("unchecked")
			List<BaBullTgtDVo> baBullTgtDVoList = (List<BaBullTgtDVo>) commonSvc.queryList(baBullTgtDVo);
			if(baBullTgtDVoList.size() > 0){
				for(BaBullTgtDVo storedBaBullTgtDVo : baBullTgtDVoList){
					if(!userList.contains(storedBaBullTgtDVo.getTgtId())){
						userList.add(storedBaBullTgtDVo.getTgtId());
					}
				}
			}
		}
		
		if(!noAuth){
			// 부서
			if(!deptList.isEmpty()){
				sub = doc.createElement("dept");
				sub.appendChild(doc.createTextNode(getStringFromList(deptList)));
				authority.appendChild(sub);
			}
			// 하위부서
			if(!deptWithList.isEmpty()){
				sub = doc.createElement("deptSub");
				sub.appendChild(doc.createTextNode(getStringFromList(deptWithList)));
				authority.appendChild(sub);
			}
			// 사용자
			if(userList.contains(bbBullLVo.getRegrUid())){
				userList.add(bbBullLVo.getRegrUid());
			}
			sub = doc.createElement("user");
			sub.appendChild(doc.createTextNode(getStringFromList(userList)));
			authority.appendChild(sub);
		}
		
		//////////////////////////////////////
		//
		//    body - 본문 HTML
		
		// <body>HTML</body>
		Element body = doc.createElement("body");
		if(bbBullLVo.getCont() != null){
			body.appendChild(doc.createTextNode(bbBullLVo.getCont()));
		}
		item.appendChild(body);
		//////////////////////////////////////
		//
		//    attaches - 첨부
		
		// <attaches>
		//   <!-- dispNm:표시명, path:경로 -->
		//   <attach dispNm="" path="" />
		// </attaches>
		Element attaches = doc.createElement("attaches");
		item.appendChild(attaches);
		
		// 게시물첨부파일(BA_BULL_FILE_D) 테이블 - SELECT
		BaBullFileDVo baBullFileDVo = new BaBullFileDVo();
		baBullFileDVo.setRefId(String.valueOf(bbBullLVo.getBullId()));
		@SuppressWarnings("unchecked")
		List<CommonFileVo> fileList = (ArrayList<CommonFileVo>)commonSvc.queryList(baBullFileDVo);
		if(fileList != null){
			
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			for(CommonFileVo storedCommonFileVo : fileList){
				
				sub = doc.createElement("attach");
				attaches.appendChild(sub);
				// dispNm
				attr = doc.createAttribute("dispNm");
				attr.setValue(storedCommonFileVo.getDispNm());
				sub.setAttributeNode(attr);
				// path
				attr = doc.createAttribute("path");
				attr.setValue(wasCopyBaseDir+storedCommonFileVo.getSavePath());
				sub.setAttributeNode(attr);
			}
		}
		return true;
	}
	
	/** 문서 검색 정보 생성 */
	private boolean processDM(Document doc, Element item, CmSrchBVo cmSrchBVo) throws Exception{
		
		// 문서그룹ID
		String docGrpId = cmSrchBVo.getMdId();
				
		// 문서에 매핑되어 있는 저장소ID 조회
		String paramStorId = dmStorSvc.getStorId(docGrpId);
		if(paramStorId == null) return false;
		
		// 저장소(DM_STOR_B) - SELECT [현재 저장소]
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(null, "ko", paramStorId);
		if(dmStorBVo==null){
			LOGGER.error("[ERROR] dmStorBVo is null !");
			return false;
		}
		
		// 문서정보 조회
		DmDocLVo dmDocLVo = dmDocSvc.getDmDocLVo("ko", dmStorBVo, null, docGrpId, true);
		if(dmDocLVo==null){
			LOGGER.error("[ERROR] dmDocLVo is null !");
			return false;
		}
		
		Element sub;
		Attr attr;
		
		//////////////////////////////////////
		//
		//    item
		
		// 제목: subj
		attr = doc.createAttribute("subj");
		attr.setValue(dmDocLVo.getSubj());
		item.setAttributeNode(attr);
		
		// 등록일: regDt
		String regDt = dmDocLVo.getRegDt();
		attr = doc.createAttribute("regDt");
		attr.setValue(regDt);
		item.setAttributeNode(attr);
		
		// 보안등급: seculCd - 설정 없으면 : none
		attr = doc.createAttribute("seculCd");
		attr.setValue(dmDocLVo.getSeculCd());
		item.setAttributeNode(attr);
		
		// 등록자UID
		attr = doc.createAttribute("regrUid");
		attr.setValue(dmDocLVo.getOwnrUid()); // 소유자UID
		item.setAttributeNode(attr);
		
		// 소유자UID
		/*attr = doc.createAttribute("ownrUid");
		attr.setValue(dmDocLVo.getOwnrUid());
		item.setAttributeNode(attr);*/
				
		// 만료일: exprYmd - 보존기한
		String exprYmd = dmDocLVo.getKeepPrdDt() != null && !dmDocLVo.getKeepPrdDt().isEmpty() ? dmDocLVo.getKeepPrdDt() : "" ;
		attr = doc.createAttribute("exprYmd");
		attr.setValue(exprYmd);
		item.setAttributeNode(attr);
		
		/*if("D".equals(cmSrchBVo.getActId())){
			return true;
		}*/
		//////////////////////////////////////
		//
		//    resource - 어권별 정보 - 대장명, 등록자명, 부서명
		Element resource = doc.createElement("resource");
		item.appendChild(resource);
		
		// 지원 언어 목록
		ArrayList<String> langList = new ArrayList<String>();
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(dmDocLVo.getOwnrUid());
		orUserBVo.setQueryLang("ko");
		orUserBVo = (OrUserBVo)commonSvc.queryVo(orUserBVo);
		
		String rescId = orUserBVo==null ? null : orUserBVo.getRescId();
		if(rescId!=null && !rescId.isEmpty()){
			sub = createOrRescElement(doc, "owner", rescId, langList);
			if(sub != null){
				resource.appendChild(sub);
			}
		}
		
		// 대장명(게시판명) - <mdBx ko="" en="" />
		if(cmSrchBVo.getMdBxRescId()!=null && !cmSrchBVo.getMdBxRescId().isEmpty()){
			sub = createPtRescElement(doc, "mdBx", cmSrchBVo.getMdBxRescId(), langList);
			if(sub != null){
				resource.appendChild(sub);
			}
		}
		
		// support 세팅 - 지원 언어 목록
		if(!langList.isEmpty()){
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for(String lang : langList){
				if(first) first = false;
				else builder.append(',');
				builder.append(lang);
			}
			
			attr = doc.createAttribute("support");
			attr.setValue(builder.toString());
			resource.setAttributeNode(attr);
		}
		

		//////////////////////////////////////
		//
		//    category
		
		// 카테고리 : item/category
		Element category = doc.createElement("category");
		item.appendChild(category);
		
		//////////////////////////////////////
		//
		//    authority - 권한
		
		// 권한 : item/authority
		Element authority = doc.createElement("authority");
		item.appendChild(authority);
		
		ArrayList<String> userList = new ArrayList<String>();
		ArrayList<String> deptList = new ArrayList<String>();
		
		// 문서 폴더유형 조회
		DmFldBVo topFldVo = dmAdmSvc.getTopTreeVo(dmStorBVo.getStorId(), dmDocLVo.getFldId(), "ko");
		if(topFldVo == null) return false;
		// 회사폴더 여부
		boolean isComp = DmConstant.FLD_COMP.equals(topFldVo.getFldGrpId());
		
		// 회사폴더이면 전체공개
		if(isComp) deptList.add(NO_DEPT);// 전부서 공개
		else deptList.add(orUserBVo.getOrgId());
		
		// 등록자와 소유자가 같으면 UID는 하나만 세팅
		if(dmDocLVo.getRegrUid().equals(dmDocLVo.getOwnrUid())){
			userList.add(dmDocLVo.getOwnrUid()); //소유자UID
		}else{
			userList.add(dmDocLVo.getRegrUid()); // 등록자UID
			userList.add(dmDocLVo.getOwnrUid()); //소유자UID
		}
		
		// 부서
		if(!deptList.isEmpty()){
			sub = doc.createElement("dept");
			sub.appendChild(doc.createTextNode(getStringFromList(deptList)));
			authority.appendChild(sub);
		}
		// 사용자
		if(!userList.isEmpty()){
			sub = doc.createElement("user");
			sub.appendChild(doc.createTextNode(getStringFromList(userList)));
			authority.appendChild(sub);
		}
		
		//////////////////////////////////////
		//
		//    body - 본문 HTML
		
		// <body>HTML</body>
		Element body = doc.createElement("body");
		if(dmDocLVo.getCont() != null){
			body.appendChild(doc.createTextNode(dmDocLVo.getCont()));
		}
		item.appendChild(body);
		//////////////////////////////////////
		//
		//    attaches - 첨부
		
		// <attaches>
		//   <!-- dispNm:표시명, path:경로 -->
		//   <attach dispNm="" path="" />
		// </attaches>
		Element attaches = doc.createElement("attaches");
		item.appendChild(attaches);
		
		// 첨부파일(DM_테이블명_FILE_D) 테이블 - SELECT
		DmFileDVo dmFileDVo = new DmFileDVo();
		dmFileDVo.setTableName(dmStorBVo.getTblNm());
		dmFileDVo.setRefId(dmDocLVo.getDocId());
		@SuppressWarnings("unchecked")
		List<CommonFileVo> fileList = (ArrayList<CommonFileVo>)commonSvc.queryList(dmFileDVo);
		if(fileList != null){
			
			String wasCopyBaseDir = distManager.getWasCopyBaseDir();
			if (wasCopyBaseDir == null) {
				distManager.init();
				wasCopyBaseDir = distManager.getWasCopyBaseDir();
			}
			
			for(CommonFileVo storedCommonFileVo : fileList){
				
				sub = doc.createElement("attach");
				attaches.appendChild(sub);
				// dispNm
				attr = doc.createAttribute("dispNm");
				attr.setValue(storedCommonFileVo.getDispNm());
				sub.setAttributeNode(attr);
				// path
				attr = doc.createAttribute("path");
				attr.setValue(wasCopyBaseDir+storedCommonFileVo.getSavePath());
				sub.setAttributeNode(attr);
			}
		}
		return true;
	}
	
	/** 조직 리소스 세팅 */
	private Element createOrRescElement(Document doc, String elementName, String rescId, ArrayList<String> langList) throws SQLException{

		OrRescBVo orRescBVo = new OrRescBVo();
		orRescBVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<OrRescBVo> orRescBVoList = (List<OrRescBVo>)commonSvc.queryList(orRescBVo);
		
		if(orRescBVoList != null){
			
			Element element = doc.createElement(elementName);
			Attr attr;
			
			String langTypCd;
			for(OrRescBVo storedOrRescBVo : orRescBVoList){
				
				langTypCd = storedOrRescBVo.getLangTypCd();
				if(!langList.contains(langTypCd)){
					langList.add(langTypCd);
				}
				
				attr = doc.createAttribute(langTypCd);
				attr.setValue(storedOrRescBVo.getRescVa());
				element.setAttributeNode(attr);
			}
			
			return element;
		}
		
		return null;
	}
	
	/** 포털 리소스 세팅 */
	private Element createPtRescElement(Document doc, String elementName, String rescId, ArrayList<String> langList) throws SQLException{

		PtRescBVo ptRescBVo = new PtRescBVo();
		ptRescBVo.setRescId(rescId);
		@SuppressWarnings("unchecked")
		List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptRescBVo);
		
		if(ptRescBVoList != null){
			
			Element element = doc.createElement(elementName);
			Attr attr;
			
			String langTypCd;
			for(PtRescBVo storedPtRescBVo : ptRescBVoList){
				
				langTypCd = storedPtRescBVo.getLangTypCd();
				if(!langList.contains(langTypCd)){
					langList.add(langTypCd);
				}
				
				attr = doc.createAttribute(langTypCd);
				attr.setValue(storedPtRescBVo.getRescVa());
				element.setAttributeNode(attr);
			}
			
			return element;
		}
		
		return null;
	}
	
	/** 데이터 삭제 */
	private void deleteSrchData(String srchNo) throws SQLException{
		CmSrchBVo cmSrchBVo = new CmSrchBVo();
		cmSrchBVo.setSrchNo(srchNo);
		commonSvc.delete(cmSrchBVo);
	}
	
	/** List<String> 를 콤마 구별 스트링으로 변환 */
	private String getStringFromList(List<String> list){
		int i, size = list.size();
		if(size==0) return "";
		if(size==1) return list.get(0);
		
		StringBuilder builder = new StringBuilder(64);
		builder.append(list.get(0));
		for(i=1;i<size;i++){
			builder.append(',').append(list.get(i));
		}
		return builder.toString();
	}
	
	/** 인덱싱 메타 데이터 재 인덱싱 */
	@RequestMapping(value = "/cm/search/reindex")
	public String reindex(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="mdRids", required=false) String mdRids,
			@Parameter(name="compIds", required=false) String compIds,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(!userVo.isSysAdmin()){
			response.sendRedirect("/");
			return null;
		}
		
		if(mdRids==null || mdRids.isEmpty()){
			response.sendRedirect("/");
			return null;
		}
		
		boolean hasMd = false;
		for(String mdRid : mdRids.split(",")){
			if("AP".equals(mdRid)){
				if(compIds==null || compIds.isEmpty()){
					if(apDocTransSvc.reindexAp(userVo, userVo.getCompId())){
						hasMd = true;
					}
				} else {
					for(String compId : compIds.split(",")){
						if(apDocTransSvc.reindexAp(userVo, compId)){
							hasMd = true;
						}
					}
				}
			} else if("BB".equals(mdRid)){
				if(compIds==null || compIds.isEmpty()){
					if(bbBullSvc.reindexBb(userVo, userVo.getCompId())){
						hasMd = true;
					}
				} else {
					for(String compId : compIds.split(",")){
						if(bbBullSvc.reindexBb(userVo, compId)){
							hasMd = true;
						}
					}
				}
			} else if("DM".equals(mdRid)){
				if(compIds==null || compIds.isEmpty()){
					if(dmDocSvc.reindexDm(userVo, userVo.getCompId())){
						hasMd = true;
					}
				} else {
					for(String compId : compIds.split(",")){
						if(dmDocSvc.reindexDm(userVo, compId)){
							hasMd = true;
						}
					}
				}
			}
		}
		
		if(hasMd){
			model.put("message", "Indexing started !");
		} else {
			model.put("message", "No data !");
		}
		model.put("todo", "history.go('-1');");
		return LayoutUtil.getResultJsp();
	}
}
