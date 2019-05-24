package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmClsBVo;
import com.innobiz.orange.web.dm.vo.DmClsRVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;

@Controller
public class DmAdmClsCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmAdmClsCtrl.class);
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 저장소 서비스 */
	@Resource(name = "dmStorSvc")
	private DmStorSvc dmStorSvc;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
//	/** 포털 보안 서비스 */
//	@Autowired
//	private PtSecuSvc ptSecuSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 분류체계관리 */
	@RequestMapping(value = "/dm/adm/cls/setCls")
	public String setCls(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/dm/adm/cls/setCls");
	}
	
	/** [FRAME] 분류체계 트리 조회 */
	@RequestMapping(value = {"/dm/adm/cls/treeClsFrm","/dm/doc/treeDocClsFrm","/dm/adm/doc/treeDocClsFrm","/cm/doc/treeDocClsFrm"})
	public String treeClsFrm(HttpServletRequest request,
			@Parameter(name="clsId", required=false) String clsId,
			@Parameter(name="initSelect", required=false) String initSelect,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		List<DmClsBVo> dmClsBVoList = dmAdmSvc.getDmClsBVoList(storId, langTypCd);
		model.put("dmClsBVoList", dmClsBVoList);
		
		if((clsId == null || clsId.isEmpty()) && (dmClsBVoList.size() > 0 && initSelect != null && "Y".equals(initSelect))){
			model.put("clsId", dmClsBVoList.get(0).getClsId());
		}
		
		// 분류체계 목록 조회
		/*DmClsBVo dmClsBVo = new DmClsBVo();
		dmClsBVo.setStorId(storId);
		@SuppressWarnings("unchecked")
		List<DmClsBVo> dmClsBVoList = (List<DmClsBVo>)commonSvc.queryList(dmClsBVo); 
		model.put("dmClsBVoList", dmClsBVoList);
		// 분류체계ID 가 없을경우 초기 선택값을 세팅해준다.
		if((clsId == null || clsId.isEmpty()) && (dmClsBVoList.size() > 0 && initSelect != null && "Y".equals(initSelect))){
			model.put("clsId", dmClsBVoList.get(0).getClsId());
		}*/
		
		return LayoutUtil.getJspPath("/dm/adm/cls/treeClsFrm");
	}
	
	/** [FRAME] 분류체계 목록 조회(오른쪽 프레임) - 일괄 저장용 조회 */
	@RequestMapping(value = "/dm/adm/cls/listClsFrm")
	public String listClsFrm(HttpServletRequest request,
			@Parameter(name="clsPid", required=false) String clsPid,
			ModelMap model) throws Exception {
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		if(clsPid==null || clsPid.isEmpty()){
			List<DmClsBVo> dmClsBVoList = new ArrayList<DmClsBVo>();
			// 화면 구성용 2개의 빈 vo 넣음
			dmClsBVoList.add(new DmClsBVo());
			dmClsBVoList.add(new DmClsBVo());
			model.put("dmClsBVoList", dmClsBVoList);
		}else{
			DmClsBVo dmClsBVo = new DmClsBVo();
			dmClsBVo.setQueryLang(langTypCd);
			dmClsBVo.setStorId(storId);
			dmClsBVo.setClsPid(clsPid);
			dmClsBVo.setOrderBy("SORT_ORDR");
			@SuppressWarnings("unchecked")
			List<DmClsBVo> dmClsBVoList = (List<DmClsBVo>)commonSvc.queryList(dmClsBVo);
			// 화면 구성용 2개의 빈 vo 넣음
			dmClsBVoList.add(new DmClsBVo());
			dmClsBVoList.add(new DmClsBVo());
			for (DmClsBVo storedDmClsBVo : dmClsBVoList) {
				if (storedDmClsBVo.getRescId() != null) {
					// 리소스기본(DM_RESC_B) 테이블 - 조회
					dmRescSvc.queryRescBVo(storId, storedDmClsBVo.getRescId(), model);
				}
			}
			model.put("dmClsBVoList", dmClsBVoList);
		}
				
		return LayoutUtil.getJspPath("/dm/adm/cls/listClsFrm");
	}
	
	
	/** [히든프레임] 분류목록 일괄 저장(오른쪽 프레임) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cls/transClsList")
	public String transClsList(HttpServletRequest request,			
			@Parameter(name="clsPid", required=false)String clsPid,
			@Parameter(name="delList", required=false)String delList,
			ModelMap model) throws Exception {
		
		try{
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			String tableName = dmStorBVo.getTblNm();
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = new QueryQueue();
			
			// 분류체계기본(DM_CLS_B) 테이블 VO
			DmClsBVo dmClsBVo;
			
			///////////////////////////////////////////////////////////////////
			//
			//  삭제 목록 처리 : Start
			
			int count, i, size;
			String clsId;
			String[] delCds = delList==null || delList.isEmpty() ? new String[]{} : delList.split(",");
			
			DmClsRVo dmClsRVo = new DmClsRVo();
			
			for(i=0;i<delCds.length;i++){
				
				clsId = delCds[i];
				// 해당 분류체계에 문서가 속해 있는지 조회
				dmClsRVo.setStorId(storId);
				dmClsRVo.setTableName(tableName);
				dmClsRVo.setClsId(clsId);
				count = commonSvc.count(dmClsRVo);
				if(count>0){
					// dm.msg.not.del.useCls=사용중인 분류체계는 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("dm.msg.not.del.useCls", request);
					LOGGER.error("fail to del cls - doc count : "+count+"\n"+msg);
					throw new CmException(msg);
				}
				
				//하위 분류체계가 있는지 조회
				dmClsBVo = new DmClsBVo();
				dmClsBVo.setStorId(storId);
				dmClsBVo.setClsPid(clsId);
				count = commonSvc.count(dmClsBVo);
				if(count>0){
					// dm.msg.not.del.childCls=하위분류체계가 있어서 삭제 할 수 없습니다.
					String msg = messageProperties.getMessage("dm.msg.not.del.childCls", request);
					LOGGER.error("fail to del cls - child cls count : "+count+"\n"+msg);
					throw new CmException(msg);
				}
				
				// 분류체계기본(DM_CLS_B) 테이블 - 삭제
				dmClsBVo = new DmClsBVo();
				dmClsBVo.setStorId(storId);
				dmClsBVo.setClsId(clsId);
				queryQueue.delete(dmClsBVo);
			}
			
			//  삭제 목록 처리 : End
			//
			///////////////////////////////////////////////////////////////////
			
			// 분류체계기본(DM_CLS_B) 테이블
			List<DmClsBVo> dmClsBVoList = (List<DmClsBVo>)VoUtil.bindList(request, DmClsBVo.class, new String[]{"valid"});
			size = dmClsBVoList==null ? 0 : dmClsBVoList.size();
			
			if(size>0){
				// 리소스 정보 queryQueue에 담음
				dmRescSvc.collectDmRescBVoList(request, queryQueue, dmClsBVoList, "valid", "rescId", "clsNm", storId);
			}
			
			for(i=0;i<size;i++){
				dmClsBVo = dmClsBVoList.get(i);
				dmClsBVo.setStorId(storId);
				dmClsBVo.setClsPid(clsPid);
				// 수정자, 수정일시
				dmClsBVo.setModrUid(userVo.getUserUid());
				dmClsBVo.setModDt("sysdate");
				
				if(dmClsBVo.getClsId() == null || dmClsBVo.getClsId().isEmpty()){
					dmClsBVo.setClsId(dmCmSvc.createId("DM_CLS_B"));
				}
				queryQueue.store(dmClsBVo);
			}
			
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.CLS);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(DmConstant.CLS);
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.afterTrans('"+clsPid+"');");
		} catch(CmException e){
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	
	
	
}
