package com.innobiz.orange.web.pt.ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.IpChecker;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtMyMnuSvc;
import com.innobiz.orange.web.pt.svc.PtPltSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;
import com.innobiz.orange.web.pt.vo.PtMyMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMyPltRVo;
import com.innobiz.orange.web.pt.vo.PtPltDVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.pt.vo.PtRescDVo;
import com.innobiz.orange.web.pt.vo.PtUserSetupDVo;

/** 나의메뉴 - 개인화 */
@Controller
public class PtMyMnuCtrl {
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 사용자 개인 설정 서비스 */
	@Autowired
	private PtMyMnuSvc ptMyMnuSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;

	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 포틀릿 관련 서비스 */
	@Autowired
	private PtPltSvc ptPltSvc;

	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
	@Autowired
	private IpChecker ipChecker;
	
	/** 메세지 프로퍼티 */
	@Autowired
	private MessageProperties messageProperties;
	
	
	////////////////////////////////////////////////////////////
	//
	//                나의 메뉴 설정
	
	/** 나의메뉴 설정 */
	@RequestMapping(value = "/pt/psn/my/setMyMnu")
	public String setMyMnu(HttpServletRequest request,
			ModelMap model) throws Exception {
		UserVo userVo = LoginSession.getUser(request);
		Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
		model.put("myMnuMap", myMnuMap);
		return LayoutUtil.getJspPath("/pt/psn/my/setMyMnu");
	}
	
	/** 나의메뉴 설정 - 팝업 */
	@RequestMapping(value = "/pt/psn/my/setMyMnuPop")
	public String setMyMnuPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 상단 메뉴 표시용
		// 레이아웃위치코드 별 레이아웃트리 맵 조회(캐쉬) - 레이아웃위치코드 - icon:아이콘, top:상단, main:메인, sub:서브, adm:관리자
		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = ptLoutSvc.getLoutTreeByLoutLocCdMap(userVo.getCompId(), langTypCd);
		
		List<PtMnuLoutDVo> loutList, ptMnuLoutDVoList = new ArrayList<PtMnuLoutDVo>();
		String[] areas = "B".equals(userVo.getLoutCatId()) ? new String[]{"main", "sub", "top"} : new String[]{"icon", "left", "right"};
		
		boolean isAdmin = userVo.isAdmin();
		
		// 외부망 권한 적용
		boolean isExAuth = ipChecker.isExAuth(userVo);
		
		// 사용자가 속한 [사용자권한그룹ID] 목록
		String[] usrAuthGrpIds = userVo.getUserAuthGrpIds();
		AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
				userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
		
		for(String area : areas){
			loutList = loutTreeByLoutLocCdMap.get(area);
			appendLoutList(loutList, ptMnuLoutDVoList, isAdmin, usrAuthGrpIds, authCdDecider);
		}
		model.put("ptMnuLoutDVoList", ptMnuLoutDVoList);
		
		// 회사별 지원 언어 - 스크립트에서 활용 목적으로 "langs"에 담음
		StringBuilder builder = new StringBuilder(32);
		List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = langTypCdList==null ? 0 : langTypCdList.size();
		boolean first = true;
		for(i=0;i<size;i++){
			if(first) first = false;
			else builder.append(',');
			builder.append(langTypCdList.get(i).getCd());
		}
		model.put("langs", builder.toString());
		
		return LayoutUtil.getJspPath("/pt/psn/my/setMyMnuPop");
	}
	
	/** Layout을  returnList 에 담음 */
	private void appendLoutList(List<PtMnuLoutDVo> loutList, List<PtMnuLoutDVo> returnList, boolean isAdmin, String[] usrAuthGrpIds, AuthCdDecider authCdDecider){
		if(loutList==null) return;
		String mnuLoutKndCd;
		for(PtMnuLoutDVo ptMnuLoutDVo : loutList){
			mnuLoutKndCd = ptMnuLoutDVo.getMnuLoutKndCd();
			if("F".equals(mnuLoutKndCd)){
				appendLoutList(ptMnuLoutDVo.getChildList(), returnList, isAdmin, usrAuthGrpIds, authCdDecider);
			} else if(	"C".equals(mnuLoutKndCd) || 
					(	"G".equals(mnuLoutKndCd) && ("01".equals(ptMnuLoutDVo.getMnuGrpTypCd()) || "03".equals(ptMnuLoutDVo.getMnuGrpTypCd())) )){
				if(isAdmin){
					returnList.add(ptMnuLoutDVo);
				} else {
					if(authCdDecider.hasUsrAuth(ptMnuLoutDVo.getMnuGrpId(), usrAuthGrpIds)){
						returnList.add(ptMnuLoutDVo);
					}
				}
			}
		}
	}
	
	/** 나의메뉴 설정 - 설정된 메뉴 트리 조회 */
	@RequestMapping(value = "/pt/psn/my/treeMyMnuFrm")
	public String treeMyMnuFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		boolean isAdmin = userVo.isAdmin();
		
		// 외부망 권한 적용
		boolean isExAuth = ipChecker.isExAuth(userVo);
		
		AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
				userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
		
		List<PtMyMnuDVo> ptMyMnuDVoList = ptMyMnuSvc.getPtMyMnuDVoList(userVo, langTypCd, authCdDecider);
		if(ptMyMnuDVoList != null){
			model.put("ptMyMnuDVoList", ptMyMnuDVoList);
		}
		
		return LayoutUtil.getJspPath("/pt/psn/my/treeMyMnuFrm");
	}
	
	/** 나의메뉴 설정 - 설정된 메뉴 트리 조회 */
	@RequestMapping(value = "/pt/psn/my/treeMnuFrm")
	public String treeMnuFrm(HttpServletRequest request,
			@Parameter(name="mnuLoutId", required=false) String mnuLoutId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 좌측 메뉴 표시용
		// 메뉴레이아웃ID 별 서브레이아웃트리 맵 조회(캐쉬) - 왼쪽메뉴의 전체 레이아웃
		Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = ptLoutSvc.getLoutCombTreeByLoutIdMap(userVo.getCompId(), langTypCd);
		
		if(mnuLoutId != null){
			// 좌측 메뉴 트리
			List<PtMnuLoutCombDVo> sideList = loutCombTreeByLoutIdMap.get(Hash.hashId(mnuLoutId));
			boolean isAdmin = userVo.isAdmin();
			// 사용자가 속한 [사용자권한그룹ID] 목록
			String[] usrAuthGrpIds = userVo.getUserAuthGrpIds();
			
			// 외부망 권한 적용
			boolean isExAuth = ipChecker.isExAuth(userVo);
			
			AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
					userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
			
			// 권한별 메뉴레이아웃상세 목록 설정
			List<PtMnuLoutCombDVo> returnList = new ArrayList<PtMnuLoutCombDVo>();
			appendCombList(sideList, returnList, mnuLoutId, isAdmin, usrAuthGrpIds, authCdDecider);
			request.setAttribute("ptMnuLoutCombDVoList", returnList);
		}
		
		model.put("treeSelectOption", "2");//트리 javascript 에서 여러개 선택 되도록 함.
		
		return LayoutUtil.getJspPath("/pt/psn/my/treeMnuFrm");
	}
	
	/** 레이아웃  returnList 에 더하기 */
	private boolean appendCombList(List<PtMnuLoutCombDVo> combList, List<PtMnuLoutCombDVo> returnList, 
			String mnuLoutCombPid, boolean isAdmin, String[] usrAuthGrpIds, AuthCdDecider authCdDecider){
		if(combList==null) return false;
		
		boolean hasMnu = false, isFld=false;
		List<PtMnuLoutCombDVo> childList;
		PtMnuDVo ptMnuDVo;
		for(PtMnuLoutCombDVo ptMnuLoutCombDVo : combList){
			
			isFld = "Y".equals(ptMnuLoutCombDVo.getFldYn());//폴더여부
			if(isFld){//폴더면
				childList = new ArrayList<PtMnuLoutCombDVo>();
				if(appendCombList(ptMnuLoutCombDVo.getChildList(), childList, ptMnuLoutCombDVo.getMnuLoutCombId(), isAdmin, usrAuthGrpIds, authCdDecider)){
					hasMnu = true;
					//ptMnuLoutCombDVo.setMnuLoutCombPid(mnuLoutCombPid);
					returnList.add(ptMnuLoutCombDVo);
					returnList.addAll(childList);
				}
			} else {
				ptMnuDVo = ptMnuLoutCombDVo.getPtMnuDVo();
				if(isAdmin || (ptMnuDVo!=null && authCdDecider.hasUsrAuth(ptMnuDVo.getMnuId(), usrAuthGrpIds))){
					hasMnu = true;
					//ptMnuLoutCombDVo.setMnuLoutCombPid(mnuLoutCombPid);
					returnList.add(ptMnuLoutCombDVo);
				}
			}
		}
		return hasMnu;
	}
	/** [팝업 내 팝업] 메뉴조합 - 폴더추가/폴더수정 */
	@RequestMapping(value = "/pt/psn/my/setFldPop")
	public String setFldPop(HttpServletRequest request,
			@Parameter(name="rescId", required=false) String rescId,
			@Parameter(name="mnuYn", required=false) String mnuYn,
			@Parameter(name="type", required=false) String type,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		if(rescId!=null && !rescId.isEmpty()){
			if("N".equals(mnuYn)){
				// 리소스상세(PT_RESC_D) 테이블 - 폴더명 조회
				PtRescDVo ptRescDVo = new PtRescDVo();
				ptRescDVo.setCompId(userVo.getCompId());
				ptRescDVo.setRescId(rescId);
				@SuppressWarnings("unchecked")
				List<PtRescDVo> ptRescDVoList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
				if(ptRescDVoList != null){
					for(PtRescDVo storedPtRescDVo : ptRescDVoList){
						model.put(storedPtRescDVo.getRescId()+"_"+storedPtRescDVo.getLangTypCd(), storedPtRescDVo.getRescVa());
					}
				}
			} else if("Y".equals(mnuYn)){
				// 리소스기본(PT_RESC_B) 테이블 - 메뉴명 조회
				PtRescBVo ptRescBVo = new PtRescBVo();
				ptRescBVo.setRescId(rescId);
				@SuppressWarnings("unchecked")
				List<PtRescBVo> ptRescBVoVoList = (List<PtRescBVo>)commonSvc.queryList(ptRescBVo);
				if(ptRescBVoVoList != null){
					for(PtRescBVo storedPtRescBVo : ptRescBVoVoList){
						model.put(storedPtRescBVo.getRescId()+"_"+storedPtRescBVo.getLangTypCd(), storedPtRescBVo.getRescVa());
					}
				}
			}
		}
		// 메뉴조합구성에서 팝업호출 하면 setFldPop
		// 기본레이아웃 설정에서 팝업호출 하면 setFld
		model.put("confirmBtnScript", (type!=null && type.equals("pop")) ? "setFldPop" : "setFld");
		return LayoutUtil.getJspPath("/pt/psn/my/setFldPop");
	}
	
	/** 메뉴조합 - 저장 */
	@RequestMapping(value = "/pt/psn/my/transMyMnuPop")
	public String transMyMnuPop(HttpServletRequest request,
			@RequestParam(value = "dataString", required = false) String dataString,
			ModelMap model) throws Exception {
		
		try {
			QueryQueue queryQueue = new QueryQueue();
			
			UserVo userVo = LoginSession.getUser(request);
			JSONArray jsonArray = (JSONArray)JSONValue.parse(dataString);
			
			// 메뉴 사용으로 - 설정 저장
			PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
			ptUserSetupDVo.setUserUid(userVo.getUserUid());
			ptUserSetupDVo.setSetupClsId(PtConstant.PT_MY_MNU);
			ptUserSetupDVo.setSetupId("useMnu");
			ptUserSetupDVo.setSetupVa("Y");
			queryQueue.store(ptUserSetupDVo);
			
			// 나의메뉴상세(PT_MY_MNU_D) 테이블 - 기존 데이터 삭제
			PtMyMnuDVo ptMyMnuDVo = new PtMyMnuDVo();
			ptMyMnuDVo.setUserUid(userVo.getUserUid());
			queryQueue.delete(ptMyMnuDVo);
			
			// 메뉴조합 Json 정보 파싱하여 QueryQueue에 저장
			parseMyMnuJson(queryQueue, jsonArray, userVo.getUserUid(), userVo, 0);

			commonSvc.execute(queryQueue);
			
			Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
			myMnuMap.put("useMnu", "Y");
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
			
//		} catch(CmException e){
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	/** 메뉴조합 Json 정보 파싱하여 QueryQueue에 저장 */
	private int parseMyMnuJson(QueryQueue queryQueue, JSONArray jsonArray,
			String mnuLoutCombPid, UserVo userVo, int sortOrdr) throws SQLException{
		
		Map<?,?> jsonMap, rescMap;
		boolean newResc;
		String mnuLoutCombId, rescId, refRescId, langTypCd, mnuNm=null;
		PtRescDVo ptRescDVo;
		Iterator<?> iterator;
		JSONArray jsonSubArray;
		PtMyMnuDVo ptMyMnuDVo;
		int i, size = jsonArray==null ? 0 : jsonArray.size();
		
		String compId = userVo.getCompId();
		
		// 기존 마이메뉴 조회 - 맵으로 전환
		ptMyMnuDVo = new PtMyMnuDVo(); 
		ptMyMnuDVo.setUserUid(userVo.getUserUid());
		@SuppressWarnings("unchecked")
		List<PtMyMnuDVo> ptMyMnuDVoList = (List<PtMyMnuDVo>)commonSvc.queryList(ptMyMnuDVo);
		Map<Integer, String> mnuId2CombIdMap = new HashMap<Integer, String>();
		if(ptMyMnuDVoList != null){
			for(PtMyMnuDVo storedPtMyMnuDVo : ptMyMnuDVoList){
				mnuId2CombIdMap.put(Hash.hashId(storedPtMyMnuDVo.getMnuId()), storedPtMyMnuDVo.getMnuLoutCombId());
			}
		}
		
		for(i=0;i<size;i++){
			jsonMap = (Map<?, ?>)jsonArray.get(i);
			
			rescId = null;
			mnuNm = null;
			// 리소스상세(PT_RESC_D) 테이블 VO - 폴더 생성 또는 폴더명 변경
			rescMap = (Map<?,?>)jsonMap.get("rescs");
			if(rescMap!=null && !rescMap.isEmpty()){
				
				newResc = false;
				rescId = (String)jsonMap.get("rescId");
				if(rescId==null || rescId.isEmpty()){
					rescId = ptCmSvc.createId("PT_RESC_D");
					newResc = true;
				}
				
				iterator = rescMap.keySet().iterator();
				while(iterator.hasNext()){
					langTypCd = (String)iterator.next();
					ptRescDVo = new PtRescDVo();
					ptRescDVo.setCompId(compId);
					ptRescDVo.setLangTypCd(langTypCd);
					ptRescDVo.setRescId(rescId);
					ptRescDVo.setRescVa((String)rescMap.get(langTypCd));
					if(newResc) queryQueue.insert(ptRescDVo);
					else queryQueue.store(ptRescDVo);
					
					if(mnuNm==null || "ko".equals(langTypCd)){
						mnuNm = ptRescDVo.getRescVa();
					}
				}
			} else {
				rescId = (String)jsonMap.get("rescId");
				if(rescId!=null && rescId.isEmpty()) rescId = null;
				
				if(rescId==null){
					// refRescId 가 넘겨오는 경우는 - 메뉴 조합의 폴더의 경우로,
					// 이경우에는 리소스를 조회하고, 신규 생성한 리소스ID로 해당 데이터를 INSERT 함
					// >> 메뉴 조합의 리소스를 카피하여 개인용으로 만듬
					refRescId = (String)jsonMap.get("refRescId");
					if(refRescId != null && !refRescId.isEmpty()){
						
						rescId = ptCmSvc.createId("PT_RESC_D");
						
						ptRescDVo = new PtRescDVo();
						ptRescDVo.setCompId(compId);
						ptRescDVo.setRescId(refRescId);
						
						@SuppressWarnings("unchecked")
						List<PtRescDVo> rescList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
						if(rescList != null){
							for(PtRescDVo storedPtRescDVo : rescList){
								storedPtRescDVo.setRescId(rescId);
								queryQueue.insert(storedPtRescDVo);
							}
						}
					}
				}
			}
			
			mnuLoutCombId = (String)jsonMap.get("mnuLoutCombId");
			if(mnuLoutCombId!=null) {
				// 기존의 것 수정 할 때 combId 가 아닌 mnuId 로 전달되어서
				// mnuId를 기존에 사용하던 combId로 전환함
				mnuLoutCombId = mnuId2CombIdMap.get(Hash.hashId(mnuLoutCombId));
			}
			if(mnuLoutCombId==null) {
				mnuLoutCombId = ptCmSvc.createId("PT_MY_MNU_D");
			}
			
			ptMyMnuDVo = new PtMyMnuDVo();
			ptMyMnuDVo.setUserUid(userVo.getUserUid());
			ptMyMnuDVo.setMnuLoutCombId(mnuLoutCombId);
			ptMyMnuDVo.setMnuLoutCombPid(mnuLoutCombPid);
			ptMyMnuDVo.setCompId(compId);
			
			if(mnuNm==null || mnuNm.isEmpty()){
				mnuNm = (String)jsonMap.get("mnuRescNm");
			}
			ptMyMnuDVo.setMnuNm(mnuNm);
			ptMyMnuDVo.setFldYn((String)jsonMap.get("fldYn"));
			ptMyMnuDVo.setRescId(rescId);
			ptMyMnuDVo.setMnuId((String)jsonMap.get("mnuId"));
			if(rescId==null){
				ptMyMnuDVo.setMnuRescId((String)jsonMap.get("mnuRescId"));
			}
			ptMyMnuDVo.setSortOrdr(Integer.toString(++sortOrdr));
			queryQueue.insert(ptMyMnuDVo);
			
			jsonSubArray = (JSONArray)jsonMap.get("children");
			if(jsonSubArray!=null && !jsonSubArray.isEmpty()){
				sortOrdr = parseMyMnuJson(queryQueue, jsonSubArray, mnuLoutCombId, userVo, sortOrdr);
			}
/*
// 변경
[
	{"mnuLoutCombId":"M0000043","fldYn":"Y","mnuRescNm":"전자결재2","rescId":"R0000ATV","mnuId":"M0000043","mnuRescId":"","sortOrdr":"1",
		"children":[
			{"mnuLoutCombId":"M00000BM","fldYn":"N","mnuRescNm":"대기함","rescId":"","mnuId":"M00000BM","mnuRescId":"R00000PO","sortOrdr":"1"},
			{"mnuLoutCombId":"M00000C0","fldYn":"N","mnuRescNm":"진행함","rescId":"","mnuId":"M00000C0","mnuRescId":"R000011O","sortOrdr":"2"}]
		},
	{"mnuLoutCombId":"M00000DI","fldYn":"N","mnuRescNm":"공람게시","rescId":"","mnuId":"M00000DI","mnuRescId":"R000019H","sortOrdr":"2"}
]
// 신규 저장
[
	{"mnuId":"M0000043","fldYn":"Y","mnuRescId":"R00000BQ","mnuRescNm":"전자결재","sortOrdr":"1",
		"rescs":{"ko":"전자결재2","en":"Approval"},
		"children":[
			{"mnuId":"M00000BM","fldYn":"N","mnuRescId":"R00000PO","mnuRescNm":"대기함","sortOrdr":"1"},
			{"mnuId":"M00000C0","fldYn":"N","mnuRescId":"R000011O","mnuRescNm":"진행함","sortOrdr":"2"}]
	},
	{"mnuId":"M00000DI","fldYn":"N","mnuRescId":"R000019H","mnuRescNm":"공람게시","sortOrdr":"2"}
]
 */
		}
		return sortOrdr;
	}
	
	////////////////////////////////////////////////////////////
	//
	//                나의 메뉴 - 포틀릿 - 설정
	
	/** 포틀릿 설정 1단계 */
	@RequestMapping(value = "/pt/psn/my/setPltStep1")
	public String setPltStep1(HttpServletRequest request,
			@Parameter(name="mnuGrpId", required=false) String mnuGrpId,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
		if(myMnuMap!=null && !myMnuMap.isEmpty()){
			model.put("pltLoutCd", myMnuMap.get("pltLoutCd"));
		}
		
		return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep1");
	}
	
	/** 포틀릿 설정 2단계 */
	@RequestMapping(value = "/pt/psn/my/setPltStep2")
	public String setPltStep2(HttpServletRequest request,
			@Parameter(name="pltLoutCd", required=false) String pltLoutCd,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		if(pltLoutCd!=null){
			List<PtMyPltRVo> ptMyPltRVoList = ptMyMnuSvc.getMyPltList(userVo.getUserUid(), pltLoutCd, langTypCd);
			
			// 관리자 여부
			boolean isAdmin = userVo.isAdmin();

			// 외부망 권한 적용
			boolean isExAuth = ipChecker.isExAuth(userVo);
			
			// 메뉴에 어떤 권한이 있는지 결정하는 객체
			AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
					userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
			ptPltSvc.setMyPltZoneList(ptMyPltRVoList, pltLoutCd, langTypCd, model, authCdDecider, userVo.getUserAuthGrpIds());
		}
		
		// 포틀릿 정책 읽기
		model.put("pltPolc", ptSysSvc.getPltPolc());
		
		// FREE:1단자유구성
		if("FREE".equals(pltLoutCd)){
			// pt.pltSetup.js 추가 include
			model.put("JS_OPTS", new String[]{"pt.pltSetup"});
			return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep2Free");
		} else if(pltLoutCd!=null && pltLoutCd.startsWith("D2")){
			// pt.jsp.setPltStep2.left=좌 측
			// pt.jsp.setPltStep2.right=우 측
			model.put("subTitle1", messageProperties.getMessage("pt.jsp.setPltStep2.left", request));
			model.put("subTitle2", messageProperties.getMessage("pt.jsp.setPltStep2.right", request));
			model.put("divCount", Integer.valueOf(2));
			return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep2Div");
		} else if(pltLoutCd!=null && pltLoutCd.startsWith("D3")){
			// pt.jsp.setPltStep2.left=좌 측
			// pt.jsp.setPltStep2.middle=중 앙
			// pt.jsp.setPltStep2.right=우 측
			model.put("subTitle1", messageProperties.getMessage("pt.jsp.setPltStep2.left", request));
			model.put("subTitle2", messageProperties.getMessage("pt.jsp.setPltStep2.middle", request));
			model.put("subTitle3", messageProperties.getMessage("pt.jsp.setPltStep2.right", request));
			model.put("divCount", Integer.valueOf(3));
			return LayoutUtil.getJspPath("/pt/adm/plt/setPltStep2Div");
		}
		
		//cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
		throw new CmException("cm.msg.notValidPage", request);
	}
	
	/** 포틀릿 설정 저장(3단계) */
	@RequestMapping(value = "/pt/psn/my/transPltSetup")
	public String transPltSetup(HttpServletRequest request,
			@Parameter(name="useMnu", required=false) String useMnu,
			@Parameter(name="usePlt", required=false) String usePlt,
			@Parameter(name="pltLoutCd", required=false) String pltLoutCd,
			@Parameter(name="zoneData1", required=false) String zoneData1,
			@Parameter(name="zoneData2", required=false) String zoneData2,
			@Parameter(name="zoneData3", required=false) String zoneData3,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String userUid = userVo.getUserUid();
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 개인설정 - 삭제
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userUid);
		ptUserSetupDVo.setSetupClsId(PtConstant.PT_MY_MNU);
		queryQueue.delete(ptUserSetupDVo);
		
		// 개인설정에 - 메뉴사용영부, 포틀릿사용여부, 포틀릿레이아웃코드 저장
		setMyMnuSetup(queryQueue, userUid, "useMnu", "Y".equals(useMnu) ? "Y" : "N", "Y");
		setMyMnuSetup(queryQueue, userUid, "usePlt", "Y".equals(usePlt) ? "Y" : "N", "Y");
		setMyMnuSetup(queryQueue, userUid, "pltLoutCd", pltLoutCd, "Y");
		
		// 나의포틀릿관계(PT_MY_PLT_R) 테이블 - 기존데이터 삭제
		PtMyPltRVo ptMyPltRVo = new PtMyPltRVo();
		ptMyPltRVo.setUserUid(userUid);
		queryQueue.delete(ptMyPltRVo);
		
		//FREE
		if("FREE".equals(pltLoutCd)){
			// 포틀릿지역코드 설정된 포틀릿 데이터 모으기
			collectPtMyPltRVo(userUid, "1", zoneData1, queryQueue, true);
		} else {
			// 포틀릿지역코드 설정된 포틀릿 데이터 모으기
			collectPtMyPltRVo(userUid, "1", zoneData1, queryQueue, false);
			collectPtMyPltRVo(userUid, "2", zoneData2, queryQueue, false);
			collectPtMyPltRVo(userUid, "3", zoneData3, queryQueue, false);
		}
		
		try {

			commonSvc.execute(queryQueue);
			
			Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
			myMnuMap.put("useMnu", useMnu);
			myMnuMap.put("usePlt", usePlt);
			myMnuMap.put("pltLoutCd", pltLoutCd);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.goBack();");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** 포틀릿 설정 데이터 모으기 */
	@SuppressWarnings("unchecked")
	private void collectPtMyPltRVo(String userUid, String pltZoneCd, String data, QueryQueue queryQueue, boolean withPosition){
		if(data==null || data.isEmpty()) return;
		
		JSONArray jsonArray = (JSONArray)JSONValue.parse(data);
		
		Object obj;
		Map<String, String> jsonMap;
		PtMyPltRVo ptMyPltRVo;
		int i, size = jsonArray==null ? 0 : jsonArray.size();
		for(i=0;i<size;i++){
			jsonMap = (Map<String, String>)jsonArray.get(i);
			
			// 나의포틀릿관계(PT_MY_PLT_R) 테이블
			ptMyPltRVo = new PtMyPltRVo();
			ptMyPltRVo.setUserUid(userUid);
			ptMyPltRVo.setPltId((String)jsonMap.get("pltId"));
			ptMyPltRVo.setRescId((String)jsonMap.get("rescId"));
			ptMyPltRVo.setPltZoneCd(pltZoneCd);
			ptMyPltRVo.setSortOrdr(Integer.toString(i+1));
			if(withPosition){// 포틀릿레이아웃 이 FREE 인 경우
				obj = jsonMap.get("topPx");
				ptMyPltRVo.setTopPx(obj==null ? null : obj.toString());//TOP픽셀
				obj = jsonMap.get("leftPx");
				ptMyPltRVo.setLeftPx(obj==null ? null : obj.toString());//LEFT픽셀
				obj = jsonMap.get("wdthPx");
				ptMyPltRVo.setWdthPx(obj==null ? null : obj.toString());//넓이픽셀
				obj = jsonMap.get("hghtPx");
				ptMyPltRVo.setHghtPx(obj==null ? null : obj.toString());//높이픽셀
				obj = jsonMap.get("zidx");
				ptMyPltRVo.setZidx(obj==null ? null : obj.toString());//Z-INDEX
			}
			// 포틀릿설정관계(PT_PLT_SETUP_R) 테이블 - INSERT
			queryQueue.insert(ptMyPltRVo);
		}
	}
	/** [팝업] 가로/세로선 추가 */
	@RequestMapping(value = "/pt/psn/my/setLinePop")
	public String setLinePop(HttpServletRequest request,
			ModelMap model) throws Exception {
		// Ctrl 로직 없음 - 단순 팝업
		return LayoutUtil.getJspPath("/pt/adm/plt/setLinePop");
	}
	
	/** 개인설정 테이블에 저장 */
	private void setMyMnuSetup(QueryQueue queryQueue, String userUid, String setupId, String setupVa, String cacheYn){
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userUid);
		ptUserSetupDVo.setSetupClsId(PtConstant.PT_MY_MNU);
		ptUserSetupDVo.setSetupId(setupId);
		ptUserSetupDVo.setSetupVa(setupVa);
		ptUserSetupDVo.setCacheYn(cacheYn);
		queryQueue.insert(ptUserSetupDVo);
	}
	
	/** 포틀릿 추가 - 팝업 */
	@RequestMapping(value = "/pt/psn/my/listPltPop")
	public String listPltPop(HttpServletRequest request,
			@Parameter(name="pageNo", required=false) String pageNo,
			ModelMap model) throws Exception {
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 코드조회 : PLT_CAT_CD (포틀릿카테고리코드)
		List<PtCdBVo> pltCatCdList = ptCmSvc.getCdList("PLT_CAT_CD", langTypCd, "Y");
		model.put("pltCatCdList", pltCatCdList);
		
		// 포틀릿상세(PT_PLT_D) 테이블 - 변수 바인딩
		PtPltDVo ptPltDVo = new PtPltDVo();
		VoUtil.bind(request, ptPltDVo);
		ptPltDVo.setQueryLang(langTypCd);
		ptPltDVo.setPageNo(null);
		
		ptPltDVo.setUseYn("Y");
		List<String> openCompIdList = new ArrayList<String>();
		openCompIdList.add(PtConstant.SYS_COMP_ID);
		openCompIdList.add(userVo.getCompId());
		ptPltDVo.setOpenCompIdList(openCompIdList);
		// 카테고리 명 순, 포틀릿 명 순
		ptPltDVo.setOrderBy("PLT_CAT_NM, RESC_NM");
		
		// 목록조회
		@SuppressWarnings("unchecked")
		List<PtPltDVo> storedPtPltDVoList = (List<PtPltDVo>)commonSvc.queryList(ptPltDVo);
		List<PtPltDVo> ptPltDVoList = new ArrayList<PtPltDVo>();
		
		// 공개범위 - 회사명 세팅
		PtCompBVo ptCompBVo;
		Map<String, PtCompBVo> ptCompMap = ptCmSvc.getPtCompBVoMap(langTypCd);
		String openCompId, allCompNm = messageProperties.getMessage("cm.option.allComp", request);//전체회사
		
		int pageNum = 1;
		if(pageNo!=null && !pageNo.isEmpty()){
			try{ pageNum = Integer.parseInt(pageNo); }
			catch(Exception ignore){}
		}
		int skipCnt = (pageNum - 1) * 10;
		
		// 권한 체크가 DB 쿼리로 조회 가능하지 않으며
		// 포틀릿의 갯수가 적어서 - 전체 데이터 조회 후 권한 있는 것만 다시 추리는 방법을 선택함
		
		boolean isAdmin = userVo.isAdmin();
		
		// 외부망 권한 적용
		boolean isExAuth = ipChecker.isExAuth(userVo);
		
		AuthCdDecider authCdDecider = !isExAuth && isAdmin ? null : ptSecuSvc.getAuthCdDecider(userVo.getCompId(),
				userVo.isInternalIp() ? PtConstant.AUTH_IP_IN : PtConstant.AUTH_IP_EX);
		String[] usrAuthGrpIds = userVo.getUserAuthGrpIds();
		
		int recodeCount = 0, addedCount = 0;
		if(storedPtPltDVoList!=null){
			for(PtPltDVo storedPtPltDVo : storedPtPltDVoList){
				
				// 권한 없는 것 제외함
				if(!isAdmin && !authCdDecider.hasUsrAuth(storedPtPltDVo.getPltId(), usrAuthGrpIds)){
					continue;
				}
				
				// 총 레코드 수 더함
				recodeCount++;
				
				// 페이지 이동 했을 경우 만큼 앞에서 제거함
				if(skipCnt>0){
					skipCnt--;
					continue;
				}
				
				// 페이지 별 10개의 레코드만 표시함으로 10개 이상의 데이터는 더하지 않음
				if(addedCount<10){
					
					// 공개범위 - 회사명 세팅
					openCompId = storedPtPltDVo.getOpenCompId();
					if(openCompId==null || openCompId.equals(PtConstant.SYS_COMP_ID) || openCompId.isEmpty()){
						storedPtPltDVo.setOpenCompNm(allCompNm);
					} else {
						ptCompBVo = ptCompMap.get(openCompId);
						storedPtPltDVo.setOpenCompNm(ptCompBVo==null ? null : ptCompBVo.getRescNm());
					}
					
					// 세팅할 리스트에 더함
					ptPltDVoList.add(storedPtPltDVo);
					addedCount++;
				}
				
			}
		}
		
		model.put("recodeCount", Integer.valueOf(recodeCount));
		model.put("ptPltDVoList", ptPltDVoList);
		
		// 저장 버튼 보이기
		model.put("saveAuth", "W");
		
		return LayoutUtil.getJspPath("/pt/adm/plt/listPltPop");
	}
	
	/** 나의메뉴 설정 - 저장 - 메뉴사용여부, 포틀릿사용여부 만 저장함 */
	@RequestMapping(value = "/pt/psn/my/transMyMnu")
	public String transMyMnu(HttpServletRequest request,
			@Parameter(name="useMnu", required=false) String useMnu,
			@Parameter(name="usePlt", required=false) String usePlt,
			ModelMap model) throws Exception {
		
		UserVo userVo = LoginSession.getUser(request);
		String userUid = userVo.getUserUid();
		
		Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
		String pltLoutCd = (myMnuMap==null) ? null : myMnuMap.get("pltLoutCd");
		
		QueryQueue queryQueue = new QueryQueue();
		
		// 개인설정 - 삭제
		PtUserSetupDVo ptUserSetupDVo = new PtUserSetupDVo();
		ptUserSetupDVo.setUserUid(userUid);
		ptUserSetupDVo.setSetupClsId(PtConstant.PT_MY_MNU);
		queryQueue.delete(ptUserSetupDVo);
		
		useMnu = "Y".equals(useMnu) ? "Y" : "N";
		usePlt = "Y".equals(usePlt) ? "Y" : "N";
		
		// 개인설정에 - 메뉴사용영부, 포틀릿사용여부, 포틀릿레이아웃코드 저장
		setMyMnuSetup(queryQueue, userUid, "useMnu", useMnu, "Y");
		setMyMnuSetup(queryQueue, userUid, "usePlt", usePlt, "Y");
		if(pltLoutCd != null){
			setMyMnuSetup(queryQueue, userUid, "pltLoutCd", pltLoutCd, "Y");
		}
		
		try {

			commonSvc.execute(queryQueue);
			
			if(myMnuMap!=null){
				myMnuMap.put("useMnu", useMnu);
				myMnuMap.put("usePlt", usePlt);
			}
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.reload();");
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 마이메뉴 URL 구함 */
	@RequestMapping(value = "/cm/getMyMnuUrlAjx")
	public String getLoutUrlAjx(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		UserVo userVo = LoginSession.getUser(request);
		if(userVo==null){
			model.put("url", PtConstant.URL_LOGIN);
			return LayoutUtil.returnJson(model);
		} else {
			model.put("url", ptMyMnuSvc.getFirstMyPage(request, userVo));
			return LayoutUtil.returnJson(model);
		}
	}
}
