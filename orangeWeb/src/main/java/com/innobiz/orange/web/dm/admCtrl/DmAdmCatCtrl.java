package com.innobiz.orange.web.dm.admCtrl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.dm.svc.DmCmSvc;
import com.innobiz.orange.web.dm.svc.DmRescSvc;
import com.innobiz.orange.web.dm.svc.DmStorSvc;
import com.innobiz.orange.web.dm.vo.DmCatBVo;
import com.innobiz.orange.web.dm.vo.DmCatDispDVo;
import com.innobiz.orange.web.dm.vo.DmCdGrpBVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.dm.vo.DmItemBVo;
import com.innobiz.orange.web.dm.vo.DmItemDispDVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmRescBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.utils.PersonalUtil;

@Controller
public class DmAdmCatCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmAdmCatCtrl.class);
	
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
	
	
	/** 문서 유형 목록 조회 */
	@RequestMapping(value = "/dm/adm/cat/listCat")
	public String listCat(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Cat");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 조회조건 매핑
		DmCatBVo dmCatBVo = new DmCatBVo();
		VoUtil.bind(request, dmCatBVo);
		dmCatBVo.setQueryLang(langTypCd);
		dmCatBVo.setStorId(storId);
		//dmCmSvc.setDmTblSuffix(dmCatBVo);
		//System.out.println("dmCatBVo.getTblSuffix() : "+dmCatBVo.getTblSuffix());
		// 카운트 조회
		Integer recodeCount = commonSvc.count(dmCatBVo);
		
		PersonalUtil.setPaging(request, dmCatBVo, recodeCount);
		model.put("recodeCount", recodeCount);
		// 레코드 조회
		if(recodeCount.intValue()>0){
			@SuppressWarnings("unchecked")
			List<DmCatBVo> dmCatBVoList = (List<DmCatBVo>)commonSvc.queryList(dmCatBVo);
			model.put("dmCatBVoList", dmCatBVoList);
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "catId"));
		model.put("params", ParamUtil.getQueryString(request));
				
		return LayoutUtil.getJspPath("/dm/adm/cat/listCat");
	}
	
	/** 문서 유형 등록 수정 화면 */
	@RequestMapping(value = "/dm/adm/cat/setCat")
	public String setCat(HttpServletRequest request,
			@RequestParam(value = "catId", required = false) String catId,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Cat");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
		
		// 조회조건 매핑
		DmCatBVo dmCatBVo = new DmCatBVo();
		VoUtil.bind(request, dmCatBVo);
		dmCatBVo.setQueryLang(langTypCd);
		//수정
		if(catId != null && !catId.isEmpty()){
			dmCatBVo.setStorId(storId);
			dmCatBVo = (DmCatBVo)commonSvc.queryVo(dmCatBVo);
			if (dmCatBVo.getRescId() != null) {
				// 리소스기본(DM_RESC_B) 테이블 - 조회, 모델에 추가
				dmRescSvc.queryRescBVo(storId, dmCatBVo.getRescId(), model);
			}
		}
		model.put("dmCatBVo", dmCatBVo);
		// 추가항목 정보 조회
		DmItemBVo dmItemBVo = new DmItemBVo();
		dmItemBVo.setQueryLang(langTypCd);
		dmItemBVo.setStorId(storId);
		dmItemBVo.setUseYn("Y");
		dmItemBVo.setAddItemYn("Y");
		//dmItemBVo.setOrderBy("LIST_DISP_ORDR");
		@SuppressWarnings("unchecked")
		List<DmItemBVo> dmItemBVoList = (List<DmItemBVo>)commonSvc.queryList(dmItemBVo);
		//수정일 경우 추가항목 데이터 로드
		if(catId != null && !catId.isEmpty()){
			DmCatDispDVo dispVo = null;
			for(DmItemBVo storedDmItemBVo : dmItemBVoList){
				dispVo = new DmCatDispDVo();
				dispVo.setStorId(storId);
				dispVo.setCatId(catId);
				dispVo.setItemId(storedDmItemBVo.getItemId());
				if(commonSvc.count(dispVo) > 0){
					dispVo = (DmCatDispDVo)commonSvc.queryVo(dispVo);
					storedDmItemBVo.setUseYn(dispVo.getUseYn());
				}
			}
		}
		model.put("dmItemBVoList", dmItemBVoList);
		model.put("paramsForList", ParamUtil.getQueryString(request, "catId"));
		model.put("params", ParamUtil.getQueryString(request));
				
		return LayoutUtil.getJspPath("/dm/adm/cat/setCat");
	}
	
	/** 유형관리 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dm/adm/cat/transCat")
	public String transCat(HttpServletRequest request,
			@RequestParam(value = "catId", required = false) String catId,
			ModelMap model) {

		try {
			String listPage = ParamUtil.getRequestParam(request, "listPage", true);
			String viewPage = ParamUtil.getRequestParam(request, "viewPage", true);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			DmRescBVo dmRescBVo = dmRescSvc.collectDmRescBVo(request, "cat", queryQueue, storId);
			if (dmRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 유형관리(DM_CAT_B) 테이블 - BIND
			DmCatBVo dmCatBVo = new DmCatBVo();
			VoUtil.bind(request, dmCatBVo);

			// 리소스 조회 후 리소스ID와 리소스명 세팅
			dmCatBVo.setRescId(dmRescBVo.getRescId());
			dmCatBVo.setCatNm(dmRescBVo.getRescVa());
			
			// 저장소ID
			dmCatBVo.setStorId(storId);

			// 기본값
			if (dmCatBVo.getDiscYn() == null || dmCatBVo.getDiscrUid() == null || dmCatBVo.getDiscrUid().isEmpty()) dmCatBVo.setDiscYn("N");
			List<DmItemBVo> colmVoList = new ArrayList<DmItemBVo>(); 
			
			// 기본항목 조회
			DmItemBVo dmItemBVo = new DmItemBVo();
			dmItemBVo.setStorId(storId);
			dmItemBVo.setUseYn("Y");
			dmItemBVo.setAddItemYn("N");
			
			if (catId == null || catId.isEmpty()) {
				// 유형ID 생성
				dmCatBVo.setCatId(dmCmSvc.createId("DM_CAT_B"));
				// 수정자, 수정일시
				dmCatBVo.setRegrUid(userVo.getUserUid());
				dmCatBVo.setRegDt("sysdate");
				
				// 신규등록일 경우 설정된 기본유형이 있는지 조회한다.
				DmCatBVo storedDmCatBVo = new DmCatBVo();
				storedDmCatBVo.setStorId(storId);
				storedDmCatBVo.setDftYn("Y");
				// 설정된 기본유형이 없을경우 신규유형을 기본으로 세팅한다.
				if(commonSvc.count(storedDmCatBVo)==0)
					dmCatBVo.setDftYn("Y");
				else
					dmCatBVo.setDftYn("N");
				
				// 유형관리(DM_CAT_B) 테이블 - INSERT
				queryQueue.insert(dmCatBVo);
				colmVoList.addAll((List<DmItemBVo>)commonSvc.queryList(dmItemBVo));

			} else {
				// 수정자, 수정일시
				dmCatBVo.setModrUid(userVo.getUserUid());
				dmCatBVo.setModDt("sysdate");
				// 유형관리(DM_CAT_B) 테이블 - UPDATE
				queryQueue.update(dmCatBVo);
				//sortOrdr = commonSvc.count(dmItemBVo);
			}
			
			//추가 항목 표시여부 삭제
			DmCatDispDVo dispVo = null;
			
			// 항목(DM_ITEM_B) 테이블 - BIND
			List<DmItemBVo> boundColmVoList = (List<DmItemBVo>) VoUtil.bindList(request, DmItemBVo.class, new String[]{"itemId"});
			colmVoList.addAll(boundColmVoList);
			
			Integer sortOrdr = 1;
			// 기본 정렬 항목[LEFT]
			String[] alnVas = {"fldNm","clsGrpNm","docNo","subj","cont"};
			for (DmItemBVo colmVo : colmVoList) {
				// 항목표시여부(DM_CAT_DISP_D) 테이블 - INSERT
				dispVo = new DmCatDispDVo();
				dispVo.setStorId(storId);
				dispVo.setCatId(dmCatBVo.getCatId());
				dispVo.setItemId(colmVo.getItemId());
				
				//if (!"Y".equals(colmVo.getUseYn())) dispVo.setListDispOrdr(99); 
				//else dispVo.setListDispOrdr(99); 
				
				dispVo.setListDispOrdr(sortOrdr);
				if(catId != null && !catId.isEmpty() && commonSvc.count(dispVo) > 0){
					dispVo.setUseYn(colmVo.getUseYn());
					// 등록자, 등록일시
					dispVo.setModrUid(userVo.getUserUid());
					dispVo.setModDt("sysdate");
					queryQueue.update(dispVo);
					continue;
				}
				
				// 기본항목 순서
				if ("N".equals(colmVo.getAddItemYn())) dispVo.setListDispOrdr(sortOrdr);
				else dispVo.setListDispOrdr(99);// 추가항목
				
				dispVo.setUseYn(colmVo.getUseYn());
				dispVo.setAddItemYn(colmVo.getAddItemYn());
				// 등록자, 등록일시
				dispVo.setRegrUid(userVo.getUserUid());
				dispVo.setRegDt("sysdate");
				
				if("N".equals(colmVo.getAddItemYn())){
					dispVo.setRegDispYn(colmVo.getRegDispYn());
					dispVo.setModDispYn(colmVo.getModDispYn());
					dispVo.setReadDispYn(colmVo.getReadDispYn());
					dispVo.setListDispYn(colmVo.getListDispYn());
				}else{
					dispVo.setRegDispYn("Y");
					dispVo.setModDispYn("Y");
					dispVo.setReadDispYn("Y");
					dispVo.setListDispYn("N");
				}
				
				/*if("CONT".equals(colmVo.getItemNm())) dispVo.setListDispYn("N");
				else dispVo.setListDispYn("Y");*/
				
				//속성ID
				dmStorSvc.setColmNmChn(colmVo);
				if ("Y".equals(colmVo.getAddItemYn())) dispVo.setAtrbId(colmVo.getItemNm().substring(0,colmVo.getItemNm().length()-1).toLowerCase()+colmVo.getItemNm().substring(colmVo.getItemNm().length()-1));
				else dispVo.setAtrbId(StringUtil.toCamelNotation(colmVo.getItemNm(), false));
			
				dispVo.setAlnVa(ArrayUtil.isInArray(alnVas, dispVo.getAtrbId()) ? "left" : "center");
				if("subj".equals(dispVo.getAtrbId())) dispVo.setWdthPerc("25");//제목 가로길이 설정
				
				if("CD".equals(colmVo.getItemTyp())) dispVo.setSortOptVa("cd");
				else if("CODE".equals(colmVo.getItemTyp())) dispVo.setSortOptVa("code");
				else dispVo.setSortOptVa("value");
				
				if("regDt".equals(dispVo.getAtrbId())) {// 기본 정렬 등록일시 설정
					dispVo.setDataSortVa("desc");
				}
				
				queryQueue.insert(dispVo);
				sortOrdr++;
			}
			
			/*dispVo.setStorId(storId);
			dispVo.setCatId(dmCatBVo.getCatId());
			dispVo.setAddItemYn("Y");
			commonSvc.delete(dispVo);*/
			
			commonSvc.execute(queryQueue);
			/*// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.BRD);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.BRD);*/

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			
			if (catId == null || catId.isEmpty()) {// 등록일 경우 목록 화면으로 이동
				model.put("todo", "parent.location.replace('" + listPage + "');");
			} else {// 수정일 경우 상세보기 화면으로 이동
				model.put("todo", "parent.location.replace('" + viewPage + "');");
			}
		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}
	
	/** [AJAX] 유형관리 수정 (관리자) */
	@RequestMapping(value = "/dm/adm/cat/transCatAjx")
	public String transCatAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String catId = (String) object.get("catId");
			String dftYn = (String) object.get("dftYn");
			if (catId == null || catId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 유형관리(DM_CAT_B) 테이블 - BIND
			DmCatBVo dmCatBVo = new DmCatBVo(),newDmCatBVo;
			dmCatBVo.setStorId(storId);
			dmCatBVo.setCatId(catId);
			// 테이블 수정
			QueryQueue queryQueue = new QueryQueue();
			
			// 기본여부 변경
			if(dftYn != null && !dftYn.isEmpty()){
				dmCatBVo.setDftYn(dftYn);
				DmCatBVo storedDmCatBVo = (DmCatBVo)commonSvc.queryVo(dmCatBVo);
				// 유형ID가 다를경우 선택한 유형을 기본으로 설정한다. 
				if(storedDmCatBVo == null || !catId.equals(storedDmCatBVo.getCatId())){
					newDmCatBVo = new DmCatBVo();
					BeanUtils.copyProperties(dmCatBVo, newDmCatBVo);
					
					//모든 유형 기본여부 초기화
					dmCatBVo.setCatId(null);
					dmCatBVo.setDftYn("N");
					queryQueue.update(dmCatBVo);
					
					//선택된유형 기본여부로 설정
					newDmCatBVo.setDftYn("Y");
					queryQueue.update(newDmCatBVo);
				}
			}
			
			if(!queryQueue.isEmpty())	commonSvc.execute(queryQueue);
			
			// cm.msg.save.success=저장 되었습니다.
			message = messageProperties.getMessage("cm.msg.save.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [AJAX] 유형관리 삭제 (관리자) */
	@RequestMapping(value = "/dm/adm/cat/transCatDelAjx")
	public String transCatDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String catId = (String) object.get("catId");
			if (catId == null || catId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			//폴더에서 해당 유형을 사용하고 있는지 조회
			DmFldBVo dmFldBVo = new DmFldBVo(storId);
			dmFldBVo.setStorId(storId);
			dmFldBVo.setCatId(catId);
			int count = commonSvc.count(dmFldBVo); 
			if( count > 0){
				// dm.msg.not.del.useCat=사용중인 유형은 삭제 할 수 없습니다.
				String msg = messageProperties.getMessage("dm.msg.not.del.useCat", request);
				LOGGER.error("fail to del cat - fld count : "+count+"\n"+msg);
				throw new CmException(msg);
			}
			
			// 유형관리(DM_CAT_B) 테이블 - BIND
			DmCatBVo dmCatBVo = new DmCatBVo();
			dmCatBVo.setStorId(storId);
			dmCatBVo.setCatId(catId);
			
			// 테이블 삭제
			QueryQueue queryQueue = new QueryQueue();
			dmAdmSvc.deleteCat(request, dmCatBVo, queryQueue);
			commonSvc.execute(queryQueue);
			
			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** [팝업] 항목관리 (관리자) */
	@RequestMapping(value = {"/dm/adm/cat/setItemMngPop","/dm/adm/env/setItemMngPop"})
	public String setItemMngPop(HttpServletRequest request,
			@RequestParam(value = "catId", required = false) String catId,
			@RequestParam(value = "itemTypCd", required = false) String itemTypCd,
			ModelMap model) throws Exception {
		
		//유형관리
		if(request.getRequestURI().startsWith("/dm/adm/cat/")){
			if(catId.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 항목표시여부 리스트
			List<DmCatDispDVo> itemDispList = dmAdmSvc.getDmCatDispDVoList(request, storId, catId, false, null, null, null);
			model.put("itemDispList", itemDispList);
		}else{
			if(itemTypCd.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 항목표시여부 리스트
			model.put("itemDispList", dmAdmSvc.getItemDispList(request, false, null, null, itemTypCd, null));
		}
		return LayoutUtil.getJspPath("/dm/adm/cat/setItemMngPop");
	}

	/** 항목관리/목록순서 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/dm/adm/cat/transDisp","/dm/adm/env/transDisp"})
	public String transDisp(HttpServletRequest request,
			@RequestParam(value = "catId", required = false) String catId,
			@RequestParam(value = "itemTypCd", required = false) String itemTypCd,
			@RequestParam(value = "dialog", required = true) String dialog,
			ModelMap model) {
		
		try {
			QueryQueue queryQueue = null;
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//유형관리
			if(request.getRequestURI().startsWith("/dm/adm/cat/")){
				if (catId.isEmpty()) {
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				// 기본저장소 조회
				DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
				if(dmStorBVo == null){
					return LayoutUtil.getResultJsp();
					//throw new CmException("dm.msg.nodata.stor", request);
				}
				String storId = dmStorBVo.getStorId();
							
				queryQueue = new QueryQueue();
				// 항목표시여부(DM_CAT_DISP_D) 테이블 - BIND
				List<DmCatDispDVo> boundDispVoList = (List<DmCatDispDVo>) VoUtil.bindList(request, DmCatDispDVo.class, new String[]{"itemId"});
				
				Integer sortOrdr = 1;
				for (DmCatDispDVo dispVo : boundDispVoList) {
					// 수정자, 수정일시
					dispVo.setStorId(storId);
					dispVo.setCatId(catId);
					if(dispVo.getAlnVa() != null && !dispVo.getAlnVa().isEmpty()){
						dispVo.setListDispOrdr(sortOrdr);
					}
					dispVo.setModrUid(userVo.getUserUid());
					dispVo.setModDt("sysdate");
					queryQueue.update(dispVo);
					sortOrdr++;
				}
			}else{
				if(itemTypCd.isEmpty()){
					// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
					throw new CmException("pt.msg.nodata.passed", request);
				}
				
				queryQueue = new QueryQueue();
				
				// 항목표시여부(DM_CAT_DISP_D) 테이블 - BIND
				List<DmItemDispDVo> boundDispVoList = (List<DmItemDispDVo>) VoUtil.bindList(request, DmItemDispDVo.class, new String[]{"atrbId"});
				
				Integer sortOrdr = 1;
				for (DmItemDispDVo dispVo : boundDispVoList) {
					dispVo.setCompId(userVo.getCompId());
					dispVo.setItemTypCd(itemTypCd);
					if(dispVo.getItemId() == null || dispVo.getItemId().isEmpty()){// 항목ID 생성
						dispVo.setItemId(dmCmSvc.createId("DM_ITEM_DISP_D"));
					}
					if(dispVo.getAlnVa() != null && !dispVo.getAlnVa().isEmpty()){
						dispVo.setListDispOrdr(sortOrdr);
					}
					// 수정자, 수정일시
					dispVo.setModrUid(userVo.getUserUid());
					dispVo.setModDt("sysdate");
					queryQueue.store(dispVo);
					sortOrdr++;
				}
			}
			if(queryQueue != null && !queryQueue.isEmpty()) commonSvc.execute(queryQueue);

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.dialog.close('" + dialog + "');");

//		} catch (CmException e) {
//			LOGGER.error(e.getMessage());
//			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}

	/** [팝업] 목록순서 (관리자) */
	@RequestMapping(value = {"/dm/adm/cat/setListOrdrPop","/dm/adm/env/setListOrdrPop"})
	public String setListOrdrPop(HttpServletRequest request,
			@RequestParam(value = "catId", required = false) String catId,
			@RequestParam(value = "itemTypCd", required = false) String itemTypCd,
			ModelMap model) throws Exception {
		
		if(request.getRequestURI().startsWith("/dm/adm/cat/")){
			if (catId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
	
			// 기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			// 항목표시여부 리스트
			List<DmCatDispDVo> itemDispList = dmAdmSvc.getDmCatDispDVoList(request, storId, catId, true, "Y", "list", null);
			model.put("itemDispList", itemDispList);
		}else{
			if(itemTypCd.isEmpty()){
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			QueryQueue queryQueue = null;
			// 기본항목 표시여부 목록 조회
			DmItemDispDVo dmItemDispDVo = new DmItemDispDVo();
			dmItemDispDVo.setCompId(userVo.getCompId());
			dmItemDispDVo.setItemTypCd(itemTypCd);
			// 기본항목이 저장되어 있지 않으면 초기화 한다.
			if(commonSvc.count(dmItemDispDVo) == 0){
				queryQueue = new QueryQueue();
				List<DmItemDispDVo> dmItemDispDVoList = new ArrayList<DmItemDispDVo>();
				dmAdmSvc.setDftDispVoList(request, LoginSession.getLangTypCd(request), dmItemDispDVoList, null, null, null);
				if(dmItemDispDVoList != null && dmItemDispDVoList.size()>0){
					Integer sortOrdr = 1;
					for (DmItemDispDVo storedDispVo : dmItemDispDVoList) {
						storedDispVo.setCompId(userVo.getCompId());
						storedDispVo.setItemId(dmCmSvc.createId("DM_ITEM_DISP_D"));
						if("Y".equals(storedDispVo.getListDispYn())) {
							storedDispVo.setListDispOrdr(sortOrdr);
							sortOrdr++;
						}else storedDispVo.setListDispOrdr(99);
						storedDispVo.setItemTypCd(itemTypCd);
						// 수정자, 수정일시
						storedDispVo.setModrUid(userVo.getUserUid());
						storedDispVo.setModDt("sysdate");
						queryQueue.insert(storedDispVo);
						
					}
				}
			}
			if(queryQueue != null) commonSvc.execute(queryQueue);
			
			model.put("itemDispList", dmAdmSvc.getItemDispList(request, true, "Y", "list", itemTypCd, null));
			
		}
		return LayoutUtil.getJspPath("/dm/adm/cat/setListOrdrPop");
	}
	
	/** 추가항목 관리 */
	@RequestMapping(value = "/dm/adm/cat/setItem")
	public String setItem(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Item");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
				
		// 추가항목 정보 조회
		DmItemBVo dmItemBVo = new DmItemBVo();
		dmItemBVo.setQueryLang(langTypCd);
		dmItemBVo.setStorId(storId);
		dmItemBVo.setAddItemYn("Y");
		@SuppressWarnings("unchecked")
		List<DmItemBVo> dmItemBVoList = (List<DmItemBVo>)commonSvc.queryList(dmItemBVo);
		
		// db에 추가항목이 없을 경우 기본항목을 세팅한다.
		if(dmItemBVoList == null || dmItemBVoList.size() ==0){
			dmStorSvc.getAddItemList(model, dmItemBVoList);
		}
		model.put("paramsForList", ParamUtil.getQueryString(request, "itemId"));
		model.put("params", ParamUtil.getQueryString(request));
		
		/*// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);*/
				
		return LayoutUtil.getJspPath("/dm/adm/cat/setItem");
	}
	
	/** [팝업] 항목관리 등록 및 수정 화면 (관리자) */
	@RequestMapping(value = "/dm/adm/cat/setItemPop")
	public String setItemPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		// 요청경로 세팅
		dmCmSvc.getRequestPath(request, model , "Item");
				
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		//UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		
		// 기본저장소 조회
		DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
		
		if(dmStorBVo == null){
			return LayoutUtil.getResultJsp();
			//throw new CmException("dm.msg.nodata.stor", request);
		}
		String storId = dmStorBVo.getStorId();
				
		// 추가항목 정보 조회
		DmItemBVo dmItemBVo = new DmItemBVo();
		dmItemBVo.setQueryLang(langTypCd);
		dmItemBVo.setStorId(storId);
		@SuppressWarnings("unchecked")
		List<DmItemBVo> dmItemBVoList = (List<DmItemBVo>)commonSvc.queryList(dmItemBVo);
		
		// 항목관리 목록 매핑
		dmStorSvc.getAddItemList(model, dmItemBVoList);
		
		for (DmItemBVo colmVo : dmItemBVoList) {
			if (colmVo.getRescId() != null) {
				// 리소스기본(DM_RESC_B) 테이블 - 조회
				dmRescSvc.queryRescBVo(storId, colmVo.getRescId(), model);
			}
		}
		
		// 코드그룹(DM_CD_GRP_B) 테이블 - BIND
		DmCdGrpBVo dmCdGrpBVo = new DmCdGrpBVo();
		dmCdGrpBVo.setQueryLang(langTypCd);
		dmCdGrpBVo.setStorId(storId);
		dmCdGrpBVo.setGrpUseYn("Y");//사용중
		// 코드그룹(DM_CD_GRP_B) 테이블 - SELECT
		@SuppressWarnings("unchecked")
		List<DmCdGrpBVo> cdList = (List<DmCdGrpBVo>) commonSvc.queryList(dmCdGrpBVo);
		model.put("cdList", cdList);
				
		model.put("paramsForList", ParamUtil.getQueryString(request, "itemId"));
		model.put("params", ParamUtil.getQueryString(request));
				
		return LayoutUtil.getJspPath("/dm/adm/cat/setItemPop");
	}
	
	/** [히든프레임] 항목관리 - 저장 */
	@RequestMapping(value = "/dm/adm/cat/transItem")
	public String transItem(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			//String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			
			//기본저장소 조회
			DmStorBVo dmStorBVo = dmStorSvc.getDmStorBVo(request, model, null, null, null, null);
			
			if(dmStorBVo == null){
				return LayoutUtil.getResultJsp();
				//throw new CmException("dm.msg.nodata.stor", request);
			}
			String storId = dmStorBVo.getStorId();
			
			QueryQueue queryQueue = new QueryQueue();
						
			// 항목(DM_ITEM_B) 테이블 - BIND
			@SuppressWarnings("unchecked")
			List<DmItemBVo> boundColmVoList = (List<DmItemBVo>) VoUtil.bindList(request, DmItemBVo.class, new String[]{"useYn"});
			
			// 리소스기본(DM_RESC_B) 테이블 - UPDATE or INSERT, 항목(DM_ITEM_B) 테이블 - BIND
			dmRescSvc.collectDmRescBVoList(request, queryQueue, boundColmVoList, "useYn", "rescId", "itemDispNm", storId);
			
			// 항목(DM_ITEM_B) 테이블 - INSERT, UPDATE
			for (DmItemBVo storedDmItemBVo : boundColmVoList) {
				storedDmItemBVo.setStorId(storId);
				storedDmItemBVo.setAddItemYn("Y");
				if(storedDmItemBVo.getItemId() != null && !storedDmItemBVo.getItemId().isEmpty()){
					// 수정자, 수정일시
					storedDmItemBVo.setModrUid(userVo.getUserUid());
					storedDmItemBVo.setModDt("sysdate");
					queryQueue.update(storedDmItemBVo);
				}else{
					// 항목ID, 테이블ID
					storedDmItemBVo.setItemId(dmCmSvc.createId("DM_ITEM_B"));
					// 수정자, 수정일시
					storedDmItemBVo.setRegrUid(userVo.getUserUid());
					storedDmItemBVo.setRegDt("sysdate");
					queryQueue.insert(storedDmItemBVo);
				}
			}
						
			commonSvc.execute(queryQueue);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");
			
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
}
