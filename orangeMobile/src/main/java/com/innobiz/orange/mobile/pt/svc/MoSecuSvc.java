package com.innobiz.orange.mobile.pt.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.mobile.ct.svc.MoCtSecuSvc;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.exception.SecuException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.AuthUrlMapper;
import com.innobiz.orange.web.pt.secu.AuthUrlMetaData;
import com.innobiz.orange.web.pt.secu.CombAuthGrp;
import com.innobiz.orange.web.pt.secu.CombAuthGrpSubData;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserAuthGrp;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.utils.LoutUtil;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtAuthGrpAuthDVo;
import com.innobiz.orange.web.pt.vo.PtAuthGrpMnuPltRVo;
import com.innobiz.orange.web.pt.vo.PtAuthGrpUserRVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;

/** 모바일 보안 서비스 */
@Service
public class MoSecuSvc {
	
	public static MoSecuSvc ins = null;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;

//	/** 시스템설정 서비스 */
//	@Autowired
//	private PtSysSvc ptSysSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
//	
//	/** 메인 관련 서비스 */
//	@Autowired
//	private PtMainSvc ptMainSvc;
//	
//	/** 나의메뉴 서비스 */
//	@Autowired
//	private PtMyMnuSvc ptMyMnuSvc;
	
	/** 커뮤니티 서비스 */
	@Autowired
	private MoCtSecuSvc moCtSecuSvc;
	
//	/** IP 체크용 객체 - IP 조회 및 정책 적용 */
//	@Autowired
//	private IpChecker ipChecker;
	
	public MoSecuSvc(){ ins = this; }
	
	/** 권한 체크 & 레이아웃 설정  */
	public void processSecurity(HttpServletRequest request, String uri, UserVo userVo, boolean shouldSkip) throws IOException, SQLException, CmException {
		
		// 사용자 세션 정보
		if(userVo==null) userVo = LoginSession.getUser(request);
		
		// 관리자권한그룹ID목록
		String[] userAdminGrpIds = userVo.getAdminAuthGrpIds();
		
		// 시스템 관리자 여부
		boolean isSysAdm = ArrayUtil.isInArray(userAdminGrpIds, PtConstant.AUTH_SYS_ADMIN);
		// 관리자 여부
		boolean isAdmin = isSysAdm || ArrayUtil.isInArray(userAdminGrpIds, PtConstant.AUTH_ADMIN);
		// 권한 할당
		if(isSysAdm){ request.setAttribute("_AUTH", "SYS"); }
		else if(isAdmin){ request.setAttribute("_AUTH", "S"); }
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 회사코드
		String compId = userVo.getCompId();
		
		// 시스템설정 - 레이아웃
		//Map<String, String> layout = ptSysSvc.getLayoutSetup();
		
		// 레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
		//String loutCatId = getLoutCatId(userVo, layout);
		String loutCatId = "M";
		request.setAttribute("_loutCatId", "M");
		
		// 페이지 타입 - 일반페이지인지, 프레임, 팝업, 포틀릿, 등 구분
		int pageType = LoutUtil.getPageType(uri);
		request.setAttribute("_pageType", Integer.valueOf(pageType));
		
		// 세션 IP 체크
//		if(LoutUtil.TYPE_PAGE == pageType){
//			ipChecker.checkSessionIp(request);
//		}
		
		////////////////////////////////////////////////////////////
		// 권한 체크용 객체들 생성
		
		// URL과 파라미터를 기반으로 어떤 메뉴가 호출되었는지를 찾는 객체
		AuthUrlMapper authUrlMapper = ptLoutSvc.getAuthUrlMapper(compId, langTypCd);
		
		// 메뉴에 어떤 권한이 있는지 결정하는 객체
		AuthCdDecider authCdDecider = isAdmin ? null : getAuthCdDecider(compId);
		
		// 사용자가 속한 [사용자권한그룹ID] 목록
		String[] userAuthGrpIds = userVo.getUserAuthGrpIds();
		// 사용자가 속한 [관리자권한그룹ID] 목록
		String[] adminAuthGrpIds = userVo.getAdminAuthGrpIds();
		
		// 메뉴ID
		String menuId = request.getParameter("menuId");
		
		// 커뮤니티 메뉴 여부
		boolean isCT = menuId!=null && menuId.startsWith(PtConstant.MNU_GRP_REF_CT) && uri.startsWith("/ct/");
		
//		// 나의 메뉴의 경로 호출
//		boolean isMyMnu = menuId!=null && !menuId.isEmpty() && menuId.charAt(0)=='Y';
//		
//		boolean isMyMnuLout = false;
		
		// 권한 메타 데이터
		AuthUrlMetaData authUrlMetaData = null;
		if(!shouldSkip && (menuId==null || menuId.isEmpty())){
			// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
			throw new SecuException("cm.msg.notValidPage", request);
		}
		
//		if(isMyMnu){
//			
//			// 페이지가 아닌데 나의메뉴의 메뉴ID로 호출되는 경우가 없으면, 있을 경우(URL을 직접 변경할 경우) 오류방지로 리턴
//			if(LoutUtil.TYPE_PAGE != pageType){
//				if(menuId==null || menuId.isEmpty()){
//					// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
//					throw new SecuException("cm.msg.notValidPage", request);
//				}
//				
//				String authCd = null;
//				PtMyMnuDVo ptMyMnuDVo = ptMyMnuSvc.getPtMyMnuDVo(userVo, menuId, langTypCd);
//				if(ptMyMnuDVo != null){
//					authCd = authCdDecider.getUsrAuthCd(ptMyMnuDVo.getMnuId(), userVo.getUserAuthGrpIds());
//					if(authCd !=null){
//						request.setAttribute("_AUTH", authCd);
//						request.setAttribute("menuId", menuId);
//					}
//				}
//				if(authCd == null){
//					// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//					throw new SecuException("cm.msg.noMenuAuth", request);
//				}
//				return;
//			}
//		} 
		
		if(isCT){
			request.setAttribute("menuId", menuId);
			moCtSecuSvc.getCtMenuList(request);
		} else {
			// 권한 메타 데이터 - 호출 경로에 해당하는 AuthUrlData 조회
			authUrlMetaData = authUrlMapper.getAuthUrlMetaData(request, uri, menuId,
					loutCatId.charAt(0), authCdDecider, userAuthGrpIds, adminAuthGrpIds);
			
			// 상단, 좌측 메뉴 표시할 uri 경로가 아니면
			if(LoutUtil.TYPE_PAGE != pageType){
				if(isAdmin){
					if(authUrlMetaData!=null){
						request.setAttribute("menuId", authUrlMetaData.getCombId());
					}
				} else {
					if(authUrlMetaData!=null){
						request.setAttribute("_AUTH", authUrlMetaData.getAuthCd());
						request.setAttribute("menuId", authUrlMetaData.getCombId());
					} else if(!shouldSkip){
						// cm.msg.notValidPage=파라미터가 잘못되었거나 보안상의 이유로 해당 페이지를 표시할 수 없습니다.
						throw new SecuException("cm.msg.notValidPage", request);
					}
				}
				return;
			} else if(authUrlMetaData!=null){
				request.setAttribute("menuId", authUrlMetaData.getCombId());
			}
		}
		
		PtMnuLoutDVo selectedPtMnuLoutDVo = null;
		
//		if(isMyMnu){
//			// 캐쉬가 없으면 생성용 - : compId+"_RID_MAP"
//			selectedPtMnuLoutDVo = ptLoutSvc.getMnuLoutByMdRid(PtConstant.MNU_GRP_REF_MY, loutCatId, compId, langTypCd);
//			if(selectedPtMnuLoutDVo!=null){
//				request.setAttribute("_ptMnuLoutDVo", selectedPtMnuLoutDVo);
//				// 메타테그 타이틀 설정
//				ptLoutSvc.setMetaTitle(request, selectedPtMnuLoutDVo.getRescNm(), langTypCd);
//			}
//		} else 
		if(isCT){
			// 캐쉬가 없으면 생성용 - : compId+"_RID_MAP"
			selectedPtMnuLoutDVo = ptLoutSvc.getMnuLoutByMdRid(PtConstant.MNU_GRP_REF_CT, loutCatId, compId, langTypCd);
			if(selectedPtMnuLoutDVo!=null){
				request.setAttribute("_ptMnuLoutDVo", selectedPtMnuLoutDVo);
				// 메타테그 타이틀 설정
//				ptLoutSvc.setMetaTitle(request, selectedPtMnuLoutDVo.getRescNm(), langTypCd);
			}
		} else if(authUrlMetaData!=null && authUrlMetaData.getLoutId() != null){
			// 상단의 선택된 메뉴 표시용 - 레이아웃ID 별 레이아웃조회(캐쉬)
			Map<Integer, PtMnuLoutDVo> loutByLoutIdMap = ptLoutSvc.getLoutByLoutIdMap(compId, langTypCd);
			selectedPtMnuLoutDVo = loutByLoutIdMap.get(Hash.hashId(authUrlMetaData.getLoutId()));//권한 메타 데이터 - 의 레이아웃ID
			if(selectedPtMnuLoutDVo!=null){
				request.setAttribute("_ptMnuLoutDVo", selectedPtMnuLoutDVo);
				// 메타테그 타이틀 설정
//				ptLoutSvc.setMetaTitle(request, selectedPtMnuLoutDVo.getRescNm(), langTypCd);
//				if(selectedPtMnuLoutDVo.isMyMnu()){
//					isMyMnuLout = true;
//				}
			}
		}
		
		
		////////////////////////////////////////////////////////////
		// 상단 메뉴
		
		// 상단 메뉴 표시용
		// 레이아웃위치코드 별 레이아웃트리 맵 조회(캐쉬) - 레이아웃위치코드 - icon:아이콘, top:상단, main:메인, sub:서브, adm:관리자
//		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = ptLoutSvc.getLoutTreeByLoutLocCdMap(compId, langTypCd);
//		
//		// 기본 레이아웃의 경우
//		if("B".equals(loutCatId)){
//			
//			List<PtMnuLoutDVo> topList  = loutTreeByLoutLocCdMap.get("top");
//			List<PtMnuLoutDVo> mainList = loutTreeByLoutLocCdMap.get("main");
//			List<PtMnuLoutDVo> subList  = loutTreeByLoutLocCdMap.get("sub");
//			
//			// 관리자의 경우 - 해당 메뉴를 그대로 JSP에 전달
//			if(isAdmin){
//				if(topList!=null)  request.setAttribute("_topList", topList);
//				if(subList!=null)  request.setAttribute("_subList", subList);
//				setBascLoutMainMnu(mainList, selectedPtMnuLoutDVo, request, layout, langTypCd);
//				
//				// 관리자임 세팅
//				request.setAttribute("_byAdmin", Boolean.TRUE);
//				
//			// 관리자가 아닌 경우 - 권한 있는 것만 추려서 JSP에 전달
//			} else {
//				
//				// 권한별 메뉴레이아웃상세 목록 설정
//				List<PtMnuLoutDVo> returnList;
//				
//				// 상단메뉴
//				returnList = new ArrayList<PtMnuLoutDVo>();
//				setAuthedLoutList(topList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//				request.setAttribute("_topList", returnList);
//				
//				// 서브메뉴
//				returnList = new ArrayList<PtMnuLoutDVo>();
//				setAuthedLoutList(subList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//				request.setAttribute("_subList", returnList);
//				
//				// 메인메뉴
//				returnList = new ArrayList<PtMnuLoutDVo>();
//				setAuthedLoutList(mainList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//				setBascLoutMainMnu(returnList, selectedPtMnuLoutDVo, request, layout, langTypCd);
//			}
//			
//		// 아이콘 레이아웃의 경우
//		} else if("I".equals(loutCatId)){
//			
//			List<PtMnuLoutDVo> iconList  = loutTreeByLoutLocCdMap.get("icon");
//			List<PtMnuLoutDVo> leftList  = loutTreeByLoutLocCdMap.get("left");
//			List<PtMnuLoutDVo> rightList  = loutTreeByLoutLocCdMap.get("right");
//			
//			// 관리자의 경우 - 그대로 JSP에 전달
//			if(isAdmin){
//				request.setAttribute("_iconList", iconList);
//				request.setAttribute("_leftList", leftList);
//				request.setAttribute("_rightList", rightList);
//
//				// 관리자임 세팅
//				request.setAttribute("_byAdmin", Boolean.TRUE);
//			// 관리자가 아닌 경우 - 권한 있는 것만 추려서 JSP에 전달
//			} else {
//				// 권한별 메뉴레이아웃상세 목록 설정
//				List<PtMnuLoutDVo> returnList;
//				
//				// 아이콘 목록
//				returnList = new ArrayList<PtMnuLoutDVo>();
//				setAuthedLoutList(iconList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//				request.setAttribute("_iconList", returnList);
//				
//				// 좌측 메뉴 목록
//				returnList = new ArrayList<PtMnuLoutDVo>();
//				setAuthedLoutList(leftList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//				request.setAttribute("_leftList", returnList);
//				
//				// 우측 메뉴 목록
//				returnList = new ArrayList<PtMnuLoutDVo>();
//				setAuthedLoutList(rightList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//				request.setAttribute("_rightList", returnList);
//			}
//		}
//		
//		// 권한 정보 체크
//		if(authUrlMetaData!=null){
//			if(!isAdmin){
//				request.setAttribute("_AUTH", authUrlMetaData.getAuthCd());
//			}
//			request.setAttribute("menuId", authUrlMetaData.getCombId());
//			Map<Integer, PtMnuDVo> mnuMap = ptLoutSvc.getMnuByMnuIdMap(langTypCd);
//			PtMnuDVo ptMnuDVo = mnuMap.get(Hash.hashId(authUrlMetaData.getMnuPltId()));
//			if(ptMnuDVo!=null){
//				request.setAttribute("UI_TITLE", ptMnuDVo.getRescNm());
//				request.setAttribute("META_TITLE", ptMnuDVo.getRescNm()+" | "+request.getAttribute("META_RESC"));
//			}
//		} else if(!isAdmin && !isMyMnu){
//			if(!shouldSkip && !isCT){
//				if(LoutUtil.TYPE_PAGE == pageType || LoutUtil.TYPE_IFRM == pageType){
//					// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//					throw new SecuException("cm.msg.noMenuAuth", request);
//				} else if(LoutUtil.TYPE_PLT == pageType || LoutUtil.TYPE_PLT_IFRM == pageType){
//					// cm.msg.noPltAuth=해당 포틀릿에 권한이 없습니다.
//					throw new SecuException("cm.msg.noPltAuth", request);
//				} else {
//					// cm.msg.noAuth=권한이 없습니다.
//					throw new SecuException("cm.msg.noAuth", request);
//				}
//			} else {
//				return;
//			}
//		}
		
		////////////////////////////////////////////////////////////
		// 좌측 메뉴
		
//		// 나의 메뉴 - 나의메뉴에 등록된 메뉴를 클릭 했을 때 - 나의 메뉴의 menuId 가 넘겨옴
//		if(isMyMnu){
//			if(menuId==null || menuId.isEmpty()){
//				// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//				throw new SecuException("cm.msg.noMenuAuth", request);
//			}
//			
//			List<PtMnuLoutCombDVo> sideList = new ArrayList<PtMnuLoutCombDVo>();
//			PtMnuLoutCombDVo ptMnuLoutCombDVo = ptMyMnuSvc.getMyPtMnuLoutCombDVoList(userVo, langTypCd, authCdDecider, menuId, sideList);
//			request.setAttribute("_sideList", sideList);
//			
//			if(ptMnuLoutCombDVo==null){
//				// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//				throw new SecuException("cm.msg.noMenuAuth", request);
//			} else {
//				
//				authUrlMetaData = authUrlMapper.getAuthUrlMetaDataByMnuId(ptMnuLoutCombDVo.getMnuId());
//				if(authUrlMetaData == null || !authUrlMetaData.isPathMapthed(uri) || !authUrlMetaData.isParamMatched(request)){
//					// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//					throw new SecuException("cm.msg.noMenuAuth", request);
//				}
//				
//				String authCd = authCdDecider.getUsrAuthCd(ptMnuLoutCombDVo.getMnuId(), userVo.getUserAuthGrpIds());
//				if(authCd==null){
//					// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//					throw new SecuException("cm.msg.noMenuAuth", request);
//				}
//				request.setAttribute("_ptMnuLoutCombDVo", ptMnuLoutCombDVo);
//				request.setAttribute("_AUTH", authCd);
//				request.setAttribute("menuId", menuId);
//			}
//			
//		// 나의 메뉴 - 나의메뉴의 메뉴 그룹이 클릭 되었을때 - 메뉴그룹의 menuId 가 넘겨옴
//		} else if(isMyMnuLout){
//			if(menuId==null || menuId.isEmpty()){
//				// cm.msg.noMenuAuth=해당 메뉴에 권한이 없습니다.
//				throw new SecuException("cm.msg.noMenuAuth", request);
//			}
//			Map<String, String> myMnuMap = ptMyMnuSvc.getMyMnuSetup(request, userVo.getUserUid());
//			if(myMnuMap != null && "Y".equals(myMnuMap.get("useMnu"))){
//				List<PtMnuLoutCombDVo> sideList = new ArrayList<PtMnuLoutCombDVo>();
//				ptMyMnuSvc.getMyPtMnuLoutCombDVoList(userVo, langTypCd, authCdDecider, menuId, sideList);
//				request.setAttribute("_sideList", sideList);
//			}
//			
//		} else 
		if(authUrlMetaData!=null){
			request.setAttribute("_AUTH", authUrlMetaData.getAuthCd());
			request.setAttribute("menuId", authUrlMetaData.getCombId());
			
			// 좌측메뉴의 선택된 메뉴 표시용 - 레이아웃콤보(조합)ID별 레이아웃콤보(조합) 맵 조회(캐쉬)
			Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(compId, langTypCd);
			PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(authUrlMetaData.getCombId()));//권한 메타 데이터 - 의 레이아웃콤보ID
			request.setAttribute("_ptMnuLoutCombDVo", ptMnuLoutCombDVo);
			
			// 좌측 메뉴 표시용
			// 메뉴레이아웃ID 별 서브레이아웃트리 맵 조회(캐쉬) - 왼쪽메뉴의 전체 레이아웃
			Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = ptLoutSvc.getLoutCombTreeByLoutIdMap(compId, langTypCd);
			
			// 좌측 메뉴 트리
			List<PtMnuLoutCombDVo> sideList = loutCombTreeByLoutIdMap.get(Hash.hashId(authUrlMetaData.getLoutId()));

			// 관리자의 경우 - 그대로 JSP에 전달
			if(isAdmin){
				request.setAttribute("_sideList", sideList);
				
			// 관리자가 아닌 경우 - 권한 있는 것만 추려서 JSP에 전달
			} else {
				// 권한별 메뉴레이아웃상세 목록 설정
				List<PtMnuLoutCombDVo> returnList = new ArrayList<PtMnuLoutCombDVo>();
				setAuthedCombList(sideList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
				request.setAttribute("_sideList", returnList);
			}
			
			
			// Footer 영역 표시용
			// 레이아웃위치코드 별 레이아웃트리 맵 조회(캐쉬) - 레이아웃위치코드 - icon:아이콘, top:상단, main:메인, sub:서브, adm:관리자
			Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = ptLoutSvc.getLoutTreeByLoutLocCdMap(userVo.getCompId(), userVo.getLangTypCd());
			List<PtMnuLoutDVo> footerList = loutTreeByLoutLocCdMap.get("footer");
			
			// 대 메뉴 : 아이콘 영역
			if(!isAdmin){
				// 모바일 하단
				List<PtMnuLoutDVo> returnList = new ArrayList<PtMnuLoutDVo>();
				setAuthedLoutList(footerList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
				footerList = returnList;
			}
			request.setAttribute("footerList", footerList);
		}
		
//		////////////////////////////////////////////////////////////
//		// 관리자 메뉴
//		
//		List<PtMnuLoutDVo> admList = loutTreeByLoutLocCdMap.get("adm");
//
//		// 관리자의 경우 - 그대로 JSP에 전달
//		if(isAdmin){
//			request.setAttribute("admList", admList);
//			
//		// 관리자가 아닌 경우 - 권한 있는 것만 추려서 JSP에 전달
//		} else {
//			// 권한별 메뉴레이아웃상세 목록 설정
//			List<PtMnuLoutDVo> returnList = new ArrayList<PtMnuLoutDVo>();
//			setAuthedLoutList(admList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
//			if(returnList.size()>0){
//				request.setAttribute("admList", returnList);
//			}
//		}
	}
	
	/** authCheckUrl에 해당하는 menuId 리턴 */
	public String getSecuMenuId(UserVo userVo, String authCheckUrl) throws SQLException{
		
		if(userVo==null) return null;
		
		// 사용자가 속한 [관리자권한그룹ID] 목록
		String[] adminAuthGrpIds = userVo.getAdminAuthGrpIds();
		
		// 관리자 여부
		boolean isAdmin = ArrayUtil.isInArray(adminAuthGrpIds, PtConstant.AUTH_SYS_ADMIN)
				|| ArrayUtil.isInArray(adminAuthGrpIds, PtConstant.AUTH_ADMIN);
		
		// URL과 파라미터를 기반으로 어떤 메뉴가 호출되었는지를 찾는 객체
		AuthUrlMapper authUrlMapper = ptLoutSvc.getAuthUrlMapper(userVo.getCompId(), userVo.getLangTypCd());
		
		// 메뉴에 어떤 권한이 있는지 결정하는 객체
		AuthCdDecider authCdDecider = isAdmin ? null : getAuthCdDecider(userVo.getCompId());
		
		return authUrlMapper.getMenuIdByUrl(authCheckUrl, userVo, authCdDecider);
	}
	
	/** 권한체크용 menuId 파라미터를 붙인 URL로 전환 */
	public String toAuthMenuUrl(UserVo userVo, String url) throws SQLException{
		return toAuthMenuUrl(userVo, url, null);
	}
	/** 권한체크용 menuId 파라미터를 붙인 URL로 전환 */
	public String toAuthMenuUrl(UserVo userVo, String url, String authCheckUrl) throws SQLException{
		String menuId = getSecuMenuId(userVo, authCheckUrl==null ? url : authCheckUrl);
		if(menuId==null) return url;
		return url+(url.indexOf('?')>0 ? '&' : '?')+"menuId="+menuId;
	}
	
	/** URL로 해당 권한있는 메뉴 찾기 */
	public PtMnuDVo getMenuByUrl(String url, UserVo userVo, String langTypCd) throws SQLException{
		String menuId = getSecuMenuId(userVo, url);
		if(menuId == null) return null;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(userVo.getCompId(), langTypCd);
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
	/** URL로 해당 권한있는 메뉴 찾기 */
	public PtMnuDVo getMenuByMenuId(String compId, String menuId, String langTypCd) throws SQLException{
		if(menuId==null) return null;
		Map<Integer, PtMnuLoutCombDVo> loutCombByCombIdMap = ptLoutSvc.getLoutCombByCombIdMap(compId, langTypCd);
		PtMnuLoutCombDVo ptMnuLoutCombDVo = loutCombByCombIdMap.get(Hash.hashId(menuId));
		return ptMnuLoutCombDVo==null ? null : ptMnuLoutCombDVo.getPtMnuDVo();
	}
	
//	/** 레이아웃유형ID 리턴 */
//	private String getLoutCatId(UserVo userVo, Map<String, String> layout) {
//		// 레이아웃유형ID
//		String loutCatId = userVo.getLoutCatId();
//		if("I".equals(loutCatId)){
//			if(!"Y".equals(layout.get("icoLoutUseYn"))){
//				loutCatId = "B";
//				userVo.setLoutCatId(loutCatId);
//			}
//		} else if("B".equals(loutCatId)){
//			if(!"Y".equals(layout.get("bascLoutUseYn"))){
//				loutCatId = "I";
//				userVo.setLoutCatId(loutCatId);
//			}
//		}
//		return loutCatId;
//	}
	
//	/** 기본레이아웃의 메인메뉴 설정 - 상단에 위치할 것과 팝업으로 위치할 것을 분리하여 세팅함 */
//	private void setBascLoutMainMnu(List<PtMnuLoutDVo> ptMnuLoutDVoTreeList, PtMnuLoutDVo selectedPtMnuLoutDVo, HttpServletRequest request, Map<String, String> layout, String langTypCd){
//
//		// 언어별 메인메뉴 최대 갯수
//		String temp = layout.get(langTypCd+"MainMnuMaxCnt");
//		// 메인메뉴 표시 최대 갯수 - 넘으면 팝업으로 표시
//		int mainMaxCnt = (temp==null) ? PtConstant.MAIN_MNU_MAX_CNT : Integer.parseInt(temp);
//		
//		// 서브 메뉴 옵션 - M:독립된 메뉴로 사용, S:메인 메뉴의 하위 메뉴로 사용, N:서브 메뉴 사용 안함
//		request.setAttribute("_subMnuOption", layout.get("subMnuOption"));
//		
//		// 선택된 메뉴 정보
//		PtMnuLoutDVo ptMnuLoutDVo;
//		// 최상위메뉴레이아웃ID
//		String topMnuLoutId = selectedPtMnuLoutDVo==null ? null : selectedPtMnuLoutDVo.getTopMnuLoutId();
//		// 메뉴레이아웃ID
//		String mnuLoutId = selectedPtMnuLoutDVo==null ? null : selectedPtMnuLoutDVo.getMnuLoutId();
//		
//		int i, mainSize = ptMnuLoutDVoTreeList.size(), mainMaxIndex = mainMaxCnt-1;
//		
//		// 상단에 표시할 메인 메뉴
//		ArrayList<PtMnuLoutDVo> mainList = new ArrayList<PtMnuLoutDVo>();
//		// 팝업으로 표시할 메인 메뉴
//		ArrayList<PtMnuLoutDVo> mainPopList = new ArrayList<PtMnuLoutDVo>();
//		
//		// 상단메뉴 css 클래스 - 일반(bmenu bmenu_on), 팝업있는(bmenuarrow bmenuarrow_on)
//		ArrayList<String> mainClassList = new ArrayList<String>();
//		
//		// 상단영역, 팝업영역 분리해서 list에 담음
//		boolean matched, lastAdded = false;
//		for(i=0;i<mainSize;i++){
//			ptMnuLoutDVo = ptMnuLoutDVoTreeList.get(i);
//			
//			// 호출된 메뉴인지 확인
//			if(		(mnuLoutId   !=null && mnuLoutId.equals(ptMnuLoutDVo.getMnuLoutId()))
//				||	(topMnuLoutId!=null && topMnuLoutId.equals(ptMnuLoutDVo.getMnuLoutId()))){
//				matched = true;
//			} else {
//				matched = false;
//			}
//			
//			// 메인 디스플레이 수 보다 작으면 - 상단 영역에 더함
//			if(i<mainMaxIndex){
//				mainList.add(ptMnuLoutDVo);
//				mainClassList.add(matched ? "bmenu_on" : "bmenu");// 펼침(▽) 없는 css 클래스 - on/off
//			} else {
//				// 현제 페이지의 top 메뉴와 loop 도는 것이 같은 메뉴면 - 상단에 더함, 아니면 팝업에 더함
//				if(matched){
//					mainList.add(ptMnuLoutDVo);
//					lastAdded = true;
//				} else {
//					mainPopList.add(ptMnuLoutDVo);
//				}
//			}
//		}
//		
//		// 마지막 것이 더해지지 않은 경우 - 팝업 목록에서 하나 빼서 메인메뉴 목록에 더해줌
//		if(!lastAdded){
//			int popSize = mainPopList.size();
//			
//			// 팝업이 필요 없는 경우 - 마지막 것이 선택되지 않은 형태로 팝업목록에서 제거해서 메인메뉴 목륵에 더해주면 됨
//			if(popSize == 1){
//				mainList.add(mainPopList.remove(0));
//				mainClassList.add("bmenu");// 펼침(▽) 없는 css 클래스 - off
//			// 팝업이 필요한 경우
//			} else if(popSize > 1){
//				mainList.add(mainPopList.remove(0));
//				mainClassList.add("bmenuarrow");// 펼침(▽) [없는] css 클래스 - off
//			}
//			
//		// 맨 마지막이 선택되어서 더해 진 경우 - 메인메뉴 목륵은 쪽에 갯수 다 체워 졌음 - 클래스 목록에만 더해 주면 됨
//		} else {
//			// 팝업이 필요 없는 경우 - 갯수가 상단에 표시되 것만큼 있음
//			if(mainPopList.isEmpty()){ 
//				mainClassList.add("bmenu_on");// 펼침(▽) [없는] css 클래스 - on
//			// 팝업이 필요한 경우
//			} else {
//				mainClassList.add("bmenuarrow_on");// 펼침(▽) [있는] css 클래스 - on
//			}
//		}
//		
//		// attribute 에 세팅함
//		request.setAttribute("_mainList", mainList);//메인 메뉴
//		request.setAttribute("_mainClassList", mainClassList);//style-className : 펼침(있음/없음), 선택(on/off)
//		if(!mainPopList.isEmpty()){
//			request.setAttribute("_mainPopList", mainPopList);// 펼침 있을때 - 팝업으로 뜰 메인 메뉴
//		}
//	}
	
	////////////////////////////////////////
	// 레이아웃의 경우 권한 설정 - 필요함
	
	/** 권한에 따른 상단 레이아웃 메뉴 설정 */
	public boolean setAuthedLoutList(List<PtMnuLoutDVo> ptMnuLoutDVoList, List<PtMnuLoutDVo> returnList,
			String[] usrAuthGrpIds, String[] admAuthGrpIds, AuthCdDecider authCdDecider) throws SQLException{
		
		boolean hasMnu = false;
		List<PtMnuLoutDVo> childList;
		String mnuLoutKndCd;//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
		PtMnuLoutDVo ptMnuLoutDVo, fldPtMnuLoutDVo;
		int i, size = ptMnuLoutDVoList==null ? 0 : ptMnuLoutDVoList.size();
		for(i=0;i<size;i++){
			ptMnuLoutDVo = ptMnuLoutDVoList.get(i);
			mnuLoutKndCd = ptMnuLoutDVo.getMnuLoutKndCd();
			
			if("F".equals(mnuLoutKndCd)){//레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
				childList = new ArrayList<PtMnuLoutDVo>();
				if(setAuthedLoutList(ptMnuLoutDVo.getChildList(), childList, usrAuthGrpIds, admAuthGrpIds, authCdDecider)){
					hasMnu = true;
					fldPtMnuLoutDVo = new PtMnuLoutDVo();
					fldPtMnuLoutDVo.fromMap(ptMnuLoutDVo.toMap());
					fldPtMnuLoutDVo.setChildList(childList);
					returnList.add(fldPtMnuLoutDVo);
				}
			} else {
				
				if("A".equals(ptMnuLoutDVo.getLoutCatId())){//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
					if(authCdDecider.hasAdmAuth(ptMnuLoutDVo.getMnuGrpId(), admAuthGrpIds)){
						hasMnu = true;
						returnList.add(ptMnuLoutDVo);
					}
				} else {
					if(authCdDecider.hasUsrAuth(ptMnuLoutDVo.getMnuGrpId(), usrAuthGrpIds)){
						hasMnu = true;
						returnList.add(ptMnuLoutDVo);
					}
				}
			}
		}
		
		return hasMnu;
	}
	
	/** 권한에 따른 좌측 레이아웃 메뉴 설정 */
	public boolean setAuthedCombList(List<PtMnuLoutCombDVo> ptMnuLoutCombDVoList, List<PtMnuLoutCombDVo> returnList,
			String[] usrAuthGrpIds, String[] admAuthGrpIds, AuthCdDecider authCdDecider) throws SQLException{
		
		boolean hasMnu = false, isFld=false;
		List<PtMnuLoutCombDVo> childList;
		PtMnuDVo ptMnuDVo;
		PtMnuLoutCombDVo ptMnuLoutCombDVo, fldPtMnuLoutCombDVo;
		int i, size = ptMnuLoutCombDVoList==null ? 0 : ptMnuLoutCombDVoList.size();
		for(i=0;i<size;i++){
			ptMnuLoutCombDVo = ptMnuLoutCombDVoList.get(i);
			isFld = "Y".equals(ptMnuLoutCombDVo.getFldYn());//폴더여부
			
			if(isFld){//폴더면
				childList = new ArrayList<PtMnuLoutCombDVo>();
				if(setAuthedCombList(ptMnuLoutCombDVo.getChildList(), childList, usrAuthGrpIds, admAuthGrpIds, authCdDecider)){
					hasMnu = true;
					fldPtMnuLoutCombDVo = new PtMnuLoutCombDVo();
					fldPtMnuLoutCombDVo.fromMap(ptMnuLoutCombDVo.toMap());
					fldPtMnuLoutCombDVo.setChildList(childList);
					returnList.add(fldPtMnuLoutCombDVo);
				}
			} else {
				ptMnuDVo = ptMnuLoutCombDVo.getPtMnuDVo();
				if("A".equals(ptMnuLoutCombDVo.getLoutCatId())){//레이아웃유형ID - B:기본레이아웃, I:아이콘레이아웃, A:관리자레이아웃
					if(ptMnuDVo!=null && authCdDecider.hasAdmAuth(ptMnuDVo.getMnuId(), admAuthGrpIds)){
						hasMnu = true;
						returnList.add(ptMnuLoutCombDVo);
					}
				} else {
					if(ptMnuDVo!=null && authCdDecider.hasUsrAuth(ptMnuDVo.getMnuId(), usrAuthGrpIds)){
						hasMnu = true;
						returnList.add(ptMnuLoutCombDVo);
					}
				}
			}
		}
		
		return hasMnu;
	}
	
	/** 메뉴에 어떤 권한이 있는지 결정하는 객체 - (메뉴 또는 포틀릿, 메뉴그룹, 레이아웃)별 권한그룹ID 와 해당 그룹의 권한(S:슈퍼, A:관리, M:수정, W:쓰기, R:읽기)을 가지고 있음 */
	public AuthCdDecider getAuthCdDecider(String compId) throws SQLException {
		
		AuthCdDecider authDecider = (AuthCdDecider)ptCacheSvc.getCache(CacheConfig.AUTH, "ko", compId, 200);
		if(authDecider!=null) return authDecider;
		
		// 권한그룹메뉴포틀릿관계(PT_AUTH_GRP_MNU_PLT_R) 테이블 - 조회
		PtAuthGrpMnuPltRVo ptAuthGrpMnuPltRVo = new PtAuthGrpMnuPltRVo();
		ptAuthGrpMnuPltRVo.setCompId(compId);
		if(ServerConfig.IS_MOBILE){
			ptAuthGrpMnuPltRVo.setWhereSqllet("AND AUTH_GRP_TYP_CD IN ('M','A')");
		} else {
			ptAuthGrpMnuPltRVo.setWhereSqllet("AND AUTH_GRP_TYP_CD IN ('U','A')");
		}
		ptAuthGrpMnuPltRVo.setOrderBy("AUTH_GRP_TYP_CD, MNU_PLT_ID, (CASE WHEN AUTH_CD = 'A' THEN 1 WHEN AUTH_CD = 'M' THEN 2 WHEN AUTH_CD = 'W' THEN 3 WHEN AUTH_CD = 'R' THEN 4 ELSE 5 END), AUTH_GRP_ID");
		@SuppressWarnings("unchecked")
		List<PtAuthGrpMnuPltRVo> ptAuthGrpMnuPltRVoList = (List<PtAuthGrpMnuPltRVo>)commonDao.queryList(ptAuthGrpMnuPltRVo);
		
		// 메뉴(또는 포틀릿, 메뉴그룹, 레이아웃)별 권한그룹ID와 권한그룹의 권한(S:슈퍼, A:관리, M:수정, W:쓰기, R:읽기)을 담은 메니저
		authDecider = new AuthCdDecider();
		authDecider.setList(ptAuthGrpMnuPltRVoList);
		
		ptCacheSvc.setCache(CacheConfig.AUTH, "ko", compId, authDecider);
		return authDecider;
	}
	
	
	/** UserVo에 사용자 권한그룹ID, 관리자 권한그룹ID 세팅 - 권한조합 이용하여 속한 권한그룹을 세팅함 */
	public void setAuthGrpIds(UserAuthGrp userAuthGrp, UserVo userVo, OrUserBVo orUserBVo, String[] roleCds) throws SQLException{
		CombAuthGrp combAuthGrp = getCombAuthGrp(orUserBVo.getCompId());
		userVo.setUserAuthGrpIds (combAuthGrp.getUserAuthGrpIds(userAuthGrp, orUserBVo, roleCds, userVo.getOrgPids()));
		userVo.setAdminAuthGrpIds(combAuthGrp.getAdminAuthGrpIds(userAuthGrp, orUserBVo, roleCds, userVo.getOrgPids()));
	}
	
	/** 사용자가 속한 사용자권한그룹, 관리자권한그룹 조회, 사용자그룹 */
	public UserAuthGrp getUserAuthGrp(String userUid, String compId, String langTypCd) throws SQLException{
		
		List<String> compIdList = new ArrayList<String>();
		compIdList.add(PtConstant.SYS_COMP_ID);
		compIdList.add(compId);
		
		// 권한그룹사용자관계(PT_AUTH_GRP_USER_R) 테이블 - 조회
		PtAuthGrpUserRVo ptAuthGrpUserRVo = new PtAuthGrpUserRVo();
		ptAuthGrpUserRVo.setUserUid(userUid);
		//ptAuthGrpUserRVo.setCompId(compId);
		ptAuthGrpUserRVo.setCompIdList(compIdList);
		if(ServerConfig.IS_MOBILE){
			ptAuthGrpUserRVo.setWhereSqllet("AND AUTH_GRP_TYP_CD IN ('M', 'A', 'G')");
		} else {
			ptAuthGrpUserRVo.setWhereSqllet("AND AUTH_GRP_TYP_CD IN ('U', 'A', 'G')");
		}
		ptAuthGrpUserRVo.setQueryLang(langTypCd);
		ptAuthGrpUserRVo.setOrderBy("COMP_ID, AUTH_GRP_TYP_CD, EXCLI_YN, AUTH_GRP_ID");
		@SuppressWarnings("unchecked")
		List<PtAuthGrpUserRVo> ptAuthGrpUserRVoList = (List<PtAuthGrpUserRVo>)commonDao.queryList(ptAuthGrpUserRVo);
		
		// 사용자가 속한 그룹(사용자권한그룹, 관리자권한그룹, 사용자그룹) - 디폴트로 USERS 추가
		UserAuthGrp userAuthGrp = new UserAuthGrp(compId);
		if(ptAuthGrpUserRVoList!=null){
			for(PtAuthGrpUserRVo storedPtAuthGrpUserRVo : ptAuthGrpUserRVoList){
				//USERS 는 모든 사용자가 속해있는 그룹으로 이 그룹에 속해 있어도 추가 하지 않음 - 이미 추가됨
				if(!PtConstant.AUTH_USERS.equals(ptAuthGrpUserRVo.getAuthGrpId())){
					userAuthGrp.add(storedPtAuthGrpUserRVo);
				}
			}
		}
		userAuthGrp.prepare();
		
		return userAuthGrp;
	}
	
	/** 회사 전체의 권한그룹 조회 */
	public CombAuthGrp getCombAuthGrp(String compId) throws SQLException {
		
		CombAuthGrp combAuthGrp = (CombAuthGrp)ptCacheSvc.getCache(CacheConfig.AUTH_GRP, "ko", compId, 20);
		if(combAuthGrp!=null) return combAuthGrp;
		
		// 권한그룹권한상세(PT_AUTH_GRP_AUTH_D) 테이블 - 회사별 데이터 조회
		PtAuthGrpAuthDVo ptAuthGrpAuthDVo = new PtAuthGrpAuthDVo();
		ptAuthGrpAuthDVo.setCompId(compId);
		if(ServerConfig.IS_MOBILE){
			ptAuthGrpAuthDVo.setWhereSqllet("AND AUTH_GRP_TYP_CD IN ('M','A','G')");
		} else {
			ptAuthGrpAuthDVo.setWhereSqllet("AND AUTH_GRP_TYP_CD IN ('U','A','G')");
		}
		ptAuthGrpAuthDVo.setOrderBy("COMP_ID, AUTH_GRP_TYP_CD, AUTH_GRP_ID, EXCLI_YN, SEQ, GRP_KND_CD, GRP_ID");
		@SuppressWarnings("unchecked")
		List<PtAuthGrpAuthDVo> ptAuthGrpAuthDVoList = (List<PtAuthGrpAuthDVo>)commonDao.queryList(ptAuthGrpAuthDVo);
		
		combAuthGrp = new CombAuthGrp();
		String authGrpTypCd=null, authGrpId=null, excliYn=null, seq=null, grpKndCd=null;
		
		List<CombAuthGrpSubData> subList = null;
		int i, size = ptAuthGrpAuthDVoList==null ? 0 : ptAuthGrpAuthDVoList.size();
		for(i=0;i<size;i++){
			ptAuthGrpAuthDVo = ptAuthGrpAuthDVoList.get(i);
			
			if(authGrpId==null
					|| authGrpTypCd.equals(ptAuthGrpAuthDVo.getAuthGrpTypCd())
					|| authGrpId.equals(ptAuthGrpAuthDVo.getAuthGrpId())
					|| excliYn.equals(ptAuthGrpAuthDVo.getExcliYn())
					|| seq.equals(ptAuthGrpAuthDVo.getSeq())
					|| grpKndCd.equals(ptAuthGrpAuthDVo.getGrpKndCd())  ){
				
				if(subList!=null){
					combAuthGrp.add(authGrpTypCd, authGrpId, excliYn, seq, grpKndCd, subList);
				}
				authGrpTypCd = ptAuthGrpAuthDVo.getAuthGrpTypCd();
				authGrpId = ptAuthGrpAuthDVo.getAuthGrpId();
				excliYn = ptAuthGrpAuthDVo.getExcliYn();
				seq = ptAuthGrpAuthDVo.getSeq();
				grpKndCd = ptAuthGrpAuthDVo.getGrpKndCd();
				
				subList = new ArrayList<CombAuthGrpSubData>();
			}
			subList.add(new CombAuthGrpSubData(ptAuthGrpAuthDVo.getGrpId(), "Y".equals(ptAuthGrpAuthDVo.getSubInclYn())));
		}
		if(subList!=null){
			combAuthGrp.add(authGrpTypCd, authGrpId, excliYn, seq, grpKndCd, subList);
		}
		
		ptCacheSvc.setCache(CacheConfig.AUTH_GRP, "ko", compId, combAuthGrp);
		return combAuthGrp;
	}
	
	/** 메뉴레이아웃ID 에 해당하는 권한 있는 메뉴레이아웃VO 리턴 */
	public PtMnuLoutDVo getAuthedPtMnuLoutDVo(UserVo userVo, String mnuLoutId, boolean defaultHome) throws SQLException{
		
		String compId = userVo.getCompId();
		String loutCatId = userVo.getLoutCatId();
		String langTypCd = userVo.getLangTypCd();
		
		PtMnuLoutDVo ptMnuLoutDVo = null;
		// 로그인인 할때 초기페이지를 설정한 경우 설정된 ID
		if(mnuLoutId!=null){
			ptMnuLoutDVo = ptLoutSvc.getLoutByLoutId(userVo.getCompId(), mnuLoutId, langTypCd);
		}
		
		// 초기 페이지 설정 안했을때 - 관리자가 [홈]으로 설정한 레이아웃 조회
		if(defaultHome && ptMnuLoutDVo==null){
			ptMnuLoutDVo = ptLoutSvc.getHomePtMnuLoutDVo(compId, loutCatId, langTypCd);
		}
		if(ptMnuLoutDVo==null) return null;
		
		if("F".equals(ptMnuLoutDVo.getMnuLoutKndCd())){
			if(ptMnuLoutDVo.getChildList()!=null && !ptMnuLoutDVo.getChildList().isEmpty()){
				ptMnuLoutDVo = ptMnuLoutDVo.getChildList().get(0);
			}
		}
		return ptMnuLoutDVo;
	}
	
	/** PtMnuLoutDVo 에 따른 디폴트 페이지 리턴 */
	public String getUrlByPtMnuLoutDVo(UserVo userVo, PtMnuLoutDVo ptMnuLoutDVo, boolean whenLogin) throws SQLException{
		//메뉴그룹구분코드 - 01:포털구성(포틀릿,메뉴), 02:포털구성(포틀릿), 03:포털구성(메뉴), 04:포털구성(URL), 11:외부팝업, 12:외부프레임
		String mnuGrpTypCd = ptMnuLoutDVo.getMnuGrpTypCd();
		// 레이아웃종류코드 - F:폴더, G:메뉴그룹, C:메뉴조합
		String mnuLoutKndCd = ptMnuLoutDVo.getMnuLoutKndCd();
		
		// 메뉴조합 이거나 메뉴그룹중 메뉴만 구성한 경우
		if("C".equals(mnuLoutKndCd) || 
				("G".equals(mnuLoutKndCd) && "03".equals(mnuGrpTypCd))){
			
			// 메뉴레이아웃ID 별 서브레이아웃트리 맵 조회(캐쉬) - 왼쪽메뉴의 전체 레이아웃
			Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = ptLoutSvc.getLoutCombTreeByLoutIdMap(userVo.getCompId(), userVo.getLangTypCd());
			// 좌측 메뉴 트리
			List<PtMnuLoutCombDVo> sideList = loutCombTreeByLoutIdMap.get(Hash.hashId(ptMnuLoutDVo.getMnuLoutId()));
			
			String url = null;
			if(userVo.isAdmin()){
				url = ptLoutSvc.getFirstValidUrl(sideList);
			} else {
				AuthCdDecider authCdDecider = getAuthCdDecider(userVo.getCompId());
				List<PtMnuLoutCombDVo> authedSideList = new ArrayList<PtMnuLoutCombDVo>();
				setAuthedCombList(sideList, authedSideList, userVo.getUserAuthGrpIds(), userVo.getAdminAuthGrpIds(), authCdDecider);
				url = ptLoutSvc.getFirstValidUrl(authedSideList);
			}
			if(url==null){
				return LoutUtil.getMnuGrpUrl(ptMnuLoutDVo.getMnuGrpId(), ptMnuLoutDVo.getMnuLoutId());
			} else {
				return url;
			}
			
		// 11:외부팝업 을 설정한 경우 - 초기페이지 설정 페이지로
		} else if("11".equals(mnuGrpTypCd)){
			if(whenLogin){
				return toAuthMenuUrl(userVo, PtConstant.URL_SET_MO_INIT, null);
			} else {
				return null;
			}
		} else {
			return ptMnuLoutDVo.getMnuUrl();
		}
	}
	
	/** 관리자 권한을 가지는 사용자 객체 리턴 */
	public UserVo createEmptyAdmin(String compId, String loutCatId, String langTypCd){
		UserVo userVo = new UserVo();
		userVo.setCompId(compId);
		userVo.setLoutCatId(loutCatId);
		userVo.setLangTypCd(langTypCd);
		userVo.setAdminAuthGrpIds(new String[]{PtConstant.AUTH_ADMIN});
		return userVo;
	}

}
