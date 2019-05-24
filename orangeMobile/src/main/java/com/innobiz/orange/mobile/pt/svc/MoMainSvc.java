package com.innobiz.orange.mobile.pt.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.pt.secu.AuthCdDecider;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.utils.PtConstant;
import com.innobiz.orange.web.pt.vo.PtMnuLoutCombDVo;
import com.innobiz.orange.web.pt.vo.PtMnuLoutDVo;

/** 모바일 메인 서비스 */
@Service
public class MoMainSvc {
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 모바일 보안 서비스 */
	@Autowired
	private MoSecuSvc moSecuSvc;
	
	/** 포털 보안 서비스(레이아웃 포함) */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 메뉴 세팅 */
	public void setMobileMenu(UserVo userVo, ModelMap model) throws SQLException{
		
		// 레이아웃위치코드 별 레이아웃트리 맵 조회(캐쉬) - 레이아웃위치코드 - icon:아이콘, top:상단, main:메인, sub:서브, adm:관리자
		Map<String, List<PtMnuLoutDVo>> loutTreeByLoutLocCdMap = ptLoutSvc.getLoutTreeByLoutLocCdMap(userVo.getCompId(), userVo.getLangTypCd());
		
		String[] userAuthGrpIds = userVo.getUserAuthGrpIds();
		String[] adminAuthGrpIds = userVo.getAdminAuthGrpIds();
		
		// 관리자 여부
		boolean isAdmin = ArrayUtil.isInArray(adminAuthGrpIds, PtConstant.AUTH_SYS_ADMIN)
				|| ArrayUtil.isInArray(adminAuthGrpIds, PtConstant.AUTH_ADMIN);
		
		List<PtMnuLoutDVo> mobileList, footerList;
		mobileList = loutTreeByLoutLocCdMap.get("mobile");
		footerList = loutTreeByLoutLocCdMap.get("footer");
		
		AuthCdDecider authCdDecider = isAdmin ? null : moSecuSvc.getAuthCdDecider(userVo.getCompId());
		
		// 대 메뉴 : 아이콘 영역
		if(!isAdmin){
			List<PtMnuLoutDVo> returnList;
			// 모바일 좌측 아이콘
			returnList = new ArrayList<PtMnuLoutDVo>();
			ptSecuSvc.setAuthedLoutList(mobileList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
			mobileList = returnList;
			
			// 모바일 하단
			returnList = new ArrayList<PtMnuLoutDVo>();
			ptSecuSvc.setAuthedLoutList(footerList, returnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
			footerList = returnList;
		}
		model.put("mobileList", mobileList);
		model.put("footerList", footerList);
		
		// 대메뉴 빈공간 체우기
		int size = mobileList==null ? 0 : mobileList.size();
		if(size<9){
			int emptySize = 8 - size;
			if(emptySize>0){
				model.put("emptySize", Integer.valueOf(emptySize));
			}
		} else {
			int emptySize = 4 - (size % 4);
			if(emptySize == 4) emptySize = 0;
			if(emptySize>0){
				model.put("emptySize", Integer.valueOf(emptySize));
			}
		}
		
		// 대 메뉴 : 개별 메뉴 영역
		
		// 메뉴레이아웃ID 별 서브레이아웃트리 맵 조회(캐쉬) - 왼쪽메뉴의 전체 레이아웃
		Map<Integer, List<PtMnuLoutCombDVo>> loutCombTreeByLoutIdMap = ptLoutSvc.getLoutCombTreeByLoutIdMap(userVo.getCompId(), userVo.getLangTypCd());
		
		List<PtMnuLoutCombDVo> sideList, sideReturnList;
		for(PtMnuLoutDVo ptMnuLoutDVo : mobileList){
			sideList = loutCombTreeByLoutIdMap.get(Hash.hashId(ptMnuLoutDVo.getMnuLoutId()));
			if(!isAdmin){
				sideReturnList = new ArrayList<PtMnuLoutCombDVo>();
				ptSecuSvc.setAuthedCombList(sideList, sideReturnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
				sideList = sideReturnList;
			}
			if(sideList != null && !sideList.isEmpty()){
				model.put(ptMnuLoutDVo.getMnuLoutId(), sideList);
			}
		}
		for(PtMnuLoutDVo ptMnuLoutDVo : footerList){
			sideList = loutCombTreeByLoutIdMap.get(Hash.hashId(ptMnuLoutDVo.getMnuLoutId()));
			if(!isAdmin){
				sideReturnList = new ArrayList<PtMnuLoutCombDVo>();
				ptSecuSvc.setAuthedCombList(sideList, sideReturnList, userAuthGrpIds, adminAuthGrpIds, authCdDecider);
				sideList = sideReturnList;
			}
			if(sideList != null && !sideList.isEmpty()){
				model.put(ptMnuLoutDVo.getMnuLoutId(), sideList);
			}
		}
	}
	
}
