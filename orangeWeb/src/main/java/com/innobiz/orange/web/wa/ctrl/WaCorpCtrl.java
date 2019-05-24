package com.innobiz.orange.web.wa.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.wa.vo.WaCarImgDVo;
import com.innobiz.orange.web.wa.vo.WaCarKndLVo;
import com.innobiz.orange.web.wa.vo.WaCorpBVo;


/** 법인 관리 Controller */
@Controller
public class WaCorpCtrl {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WaCorpCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
//	/** 공통 서비스 */
//	@Autowired
//	private WaCmSvc waCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 법인 조회 */
	@RequestMapping(value = {"/wa/adm/car/listCorpFrm", "/wa/adm/car/setCorpPop", "/wa/corp/listCorpPop"})
	public String listCorpFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		WaCorpBVo waCorpBVo = new WaCorpBVo();
		waCorpBVo.setQueryLang(langTypCd);
		waCorpBVo.setCompId(userVo.getCompId());
		
		// 법인조회
		@SuppressWarnings("unchecked")
		List<WaCorpBVo> waCorpBVoList = (List<WaCorpBVo>)commonSvc.queryList(waCorpBVo);
		model.put("waCorpBVoList", waCorpBVoList);
		if(!request.getRequestURI().startsWith("/wa/adm/car/listCorpFrm")){
			if(request.getRequestURI().indexOf("setCorpPop")>-1){
				// 화면 구성용 2개의 빈 vo 넣음
				waCorpBVoList.add(new WaCorpBVo());
				waCorpBVoList.add(new WaCorpBVo());
				model.put("isSelect", Boolean.FALSE);
			}else{
				model.put("isSelect", Boolean.TRUE);
			}
			return LayoutUtil.getJspPath("/wa/corp/setCorpPop");
		}
		
		return LayoutUtil.getJspPath("/wa/corp/listCorpFrm");
	}
	
	/** [히든프레임] 법인 일괄 저장(왼쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/wa/adm/car/transCorpList")
	public String transCorpList(HttpServletRequest request,			
			@Parameter(name="delList", required=false) String delList,
			@Parameter(name="dialog", required=false) String dialog,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 법인기본(WA_CORP_B) 테이블 VO
			WaCorpBVo waCorpBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int i, size;
			String corpNo;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			// 차종
			WaCarKndLVo waCarKndLVo = null;
			// 이미지
			WaCarImgDVo waCarImgDVo = null;
			List<WaCarKndLVo> deleteCarList=null;
			for(i=0;i<delCds.length;i++){
				corpNo = delCds[i];
				
				// 차종 목록 조회 
				waCarKndLVo = new WaCarKndLVo();
				waCarKndLVo.setCorpNo(corpNo);
				waCarKndLVo.setCompId(userVo.getCompId());
				deleteCarList = (List<WaCarKndLVo>)commonSvc.queryList(waCarKndLVo);
				
				if(deleteCarList!=null && deleteCarList.size()>0){
					for(WaCarKndLVo vo : deleteCarList){
						// 이미지 삭제
						waCarImgDVo = new WaCarImgDVo();
						waCarImgDVo.setCarKndNo(vo.getCarKndNo());
						queryQueue.delete(waCarImgDVo);
					}
					// 차종 삭제
					queryQueue.delete(waCarKndLVo);
				}
				
				// 기본 테이블 - 삭제
				waCorpBVo = new WaCorpBVo();
				waCorpBVo.setCorpNo(corpNo);
				waCorpBVo.setCompId(userVo.getCompId());
				queryQueue.delete(waCorpBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 법인기본(WA_CORP_B) 테이블
			List<WaCorpBVo> waCorpBVoList = (List<WaCorpBVo>)VoUtil.bindList(request, WaCorpBVo.class, new String[]{"valid"});
			size = waCorpBVoList==null ? 0 : waCorpBVoList.size();
			
			for(i=0;i<size;i++){
				waCorpBVo = waCorpBVoList.get(i);
				waCorpBVo.setCompId(userVo.getCompId());
				
				if(waCorpBVo.getCorpNo() == null || waCorpBVo.getCorpNo().isEmpty()){
					waCorpBVo.setCorpNo(commonSvc.nextVal("WA_CORP_B").toString());
				}
				queryQueue.store(waCorpBVo);
			}
			
			commonSvc.execute(queryQueue);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			String menuId = ParamUtil.getRequestParam(request, "menuId", true);
			model.put("todo", "parent.reloadFrm('leftPage','./listCorpFrm.do?menuId="+menuId+"','"+(dialog!=null && !dialog.isEmpty() ? dialog : "")+"');");
		}catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
}
