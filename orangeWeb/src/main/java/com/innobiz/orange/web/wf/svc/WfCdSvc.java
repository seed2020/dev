package com.innobiz.orange.web.wf.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.wf.utils.WfConstant;
import com.innobiz.orange.web.wf.vo.WfCdDVo;
import com.innobiz.orange.web.wf.vo.WfCdGrpBVo;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfWorksCodeLVo;

@Service
public class WfCdSvc {
	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(WfCdSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;
	
	/** 코드그룹 목록 세팅 - 등록 */
	public void setFormCdListMap(ModelMap model, List<WfFormColmLVo> wfFormColmLVoList, WfFormRegDVo wfFormRegDVo, String langTypCd) throws SQLException{
		if(wfFormColmLVoList==null || wfFormRegDVo==null || wfFormRegDVo.getAttrVa()==null || wfFormRegDVo.getAttrVa().isEmpty()) return;
		// 양식의 코드를 사용하는 컬럼 목록
		List<String> codeGrpToColmNmList = new ArrayList<String>();
		for(WfFormColmLVo storedWfFormColmLVo : wfFormColmLVoList){
			if(ArrayUtil.isInArray(WfConstant.CODE_GRP_LIST, storedWfFormColmLVo.getColmTypCd())){
				codeGrpToColmNmList.add(storedWfFormColmLVo.getColmNm());
			}
		}
		if(codeGrpToColmNmList.size()==0) return;
		
		// 양식 속성값을 JSON 객체로 변환
		JSONObject attrVa = (JSONObject) JSONValue.parse(wfFormRegDVo.getAttrVa());
		
		// 양식 속성의 상세 데이터
		JSONObject attrDtl=null;
		// 양식 코드 목록 맵
		Map<String, List<WfCdDVo>> formCdListMap = new HashMap<String, List<WfCdDVo>>();
		
		// 코드그룹ID, 코드목록 맵 키
		String cdGrpId=null, cdListKey=null;
		
		for(String colmNm : codeGrpToColmNmList){
			if(!attrVa.containsKey(colmNm)) continue; // 속성값이 없으면 continue
			attrDtl=(JSONObject)attrVa.get(colmNm);
			if(!attrDtl.containsKey("chkTypCd") || "".equals(attrDtl.get("chkTypCd"))) continue; // 구분코드가 없거나 직접입력(공백)이면 continue
			cdGrpId=(String)attrDtl.get("chkTypCd"); // 코드그룹ID
			cdListKey=WfConstant.CODE_LIST_PREFIX+cdGrpId; // 맵에 담을 코드목록 키
			if(formCdListMap.containsKey(cdListKey) && formCdListMap.get(cdListKey)!=null) continue; // 맵에 이미 코드가 담겨 있으면 continue
			formCdListMap.put(cdListKey, getWfCdDVoList(langTypCd, cdGrpId, null, "Y")); // 코드 목록을 맵에 담음
		}
		
		model.put("formCdListMap", formCdListMap);
	}
	
	/** 코드그룹 전체 목록 세팅 - 등록 */
	public void setAllFormCdListMap(ModelMap model, String langTypCd, String compId, String useYn, boolean withCd) throws SQLException{
		
		List<WfCdGrpBVo> wfCdGrpBVoList = getWfCdGrpBVoList(null, langTypCd, compId, useYn, withCd);
		
		// 양식 코드 목록 맵
		Map<String, List<WfCdDVo>> formCdListMap = new HashMap<String, List<WfCdDVo>>();
		
		// 코드그룹ID, 코드목록 맵 키
		String cdListKey=null;
				
		for(WfCdGrpBVo storedWfCdGrpBVo : wfCdGrpBVoList){
			cdListKey=WfConstant.CODE_LIST_PREFIX+storedWfCdGrpBVo.getCdGrpId(); // 맵에 담을 코드목록 키
			formCdListMap.put(cdListKey, storedWfCdGrpBVo.getWfCdDVoList()); // 코드 목록을 맵에 담음
		}
		model.put("formCdListMap", formCdListMap);
	}
	
	/** db 컬럼 목록 중 코드를 사용하는 컬럼 제외 */
	public List<String> getExcludeCdColmList(List<String> jsonColmList, WfFormRegDVo wfFormRegDVo){
		
		if(jsonColmList==null || wfFormRegDVo==null || wfFormRegDVo.getAttrVa()==null || wfFormRegDVo.getAttrVa().isEmpty()) return null;
		// 양식의 코드를 사용하는 컬럼 목록
		List<String> codeGrpToColmNmList = new ArrayList<String>();
		for(String colmNm : jsonColmList){
			if(ArrayUtil.isStartsWithArray(WfConstant.CD_TO_TBL_COLM_LIST, colmNm)){
				codeGrpToColmNmList.add(colmNm);
			}
		}
		if(codeGrpToColmNmList.size()==0) return null;
		
		// 양식 속성값을 JSON 객체로 변환
		JSONObject attrVa = (JSONObject) JSONValue.parse(wfFormRegDVo.getAttrVa());
		
		// 양식 속성의 상세 데이터
		JSONObject attrDtl=null;
		
		List<String> returnList=new ArrayList<String>();
		for(String colmNm : codeGrpToColmNmList){
			if(!attrVa.containsKey(colmNm)) continue; // 속성값이 없으면 continue
			attrDtl=(JSONObject)attrVa.get(colmNm);
			if(!attrDtl.containsKey("chkTypCd") || "".equals(attrDtl.get("chkTypCd"))) continue; // 구분코드가 없거나 직접입력(공백)이면 continue
			returnList.add(colmNm);
		}
		
		if(returnList.size()>0) return returnList;
		
		return null;
	}
	
	/** 코드그룹 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WfCdGrpBVo> getWfCdGrpBVoList(WfCdGrpBVo wfCdGrpBVo, String langTypCd, String compId, String useYn, boolean withCd) throws SQLException{
		
		if(wfCdGrpBVo==null){
			// 코드그룹 목록 조회
			wfCdGrpBVo = new WfCdGrpBVo();
			if(langTypCd!=null) wfCdGrpBVo.setQueryLang(langTypCd);
			if(compId!=null) wfCdGrpBVo.setCompId(compId);
			if(useYn!=null) wfCdGrpBVo.setCdUseYn(useYn);
		}
		
		// 코드그룹(WF_CD_GRP_B) 테이블 - SELECT
		List<WfCdGrpBVo> wfCdGrpBVoList = (List<WfCdGrpBVo>) commonDao.queryList(wfCdGrpBVo);
		
		if(withCd){
			WfCdDVo wfCdDVo = null;
			List<WfCdDVo> wfCdDVoList;
			for (WfCdGrpBVo storedWfCdGrpBVo : wfCdGrpBVoList) {
				// 코드(WF_CD_D) 테이블 - SELECT
				wfCdDVo = new WfCdDVo();
				wfCdDVo.setQueryLang(langTypCd);
				wfCdDVo.setCdGrpId(storedWfCdGrpBVo.getCdGrpId());
				if(useYn!=null) wfCdDVo.setUseYn(useYn);
				wfCdDVoList = (List<WfCdDVo>) commonDao.queryList(wfCdDVo);
				if(wfCdDVoList!=null && wfCdDVoList.size()>0)
					storedWfCdGrpBVo.setWfCdDVoList(wfCdDVoList);
			}
		}
		
		return wfCdGrpBVoList;
	}
	
	/** 코드그룹 JSON 으로 변환 */
	public String getCdGrpListToJson(WfCdGrpBVo wfCdGrpBVo, String langTypCd, String compId, String useYn, boolean withCd) throws SQLException{
		
		List<WfCdGrpBVo> wfCdGrpBVoList=getWfCdGrpBVoList(wfCdGrpBVo, langTypCd, compId, useYn, withCd);
		
		String jsonString = null; 
		
		if(wfCdGrpBVoList!=null && wfCdGrpBVoList.size()>0)
			jsonString = JsonUtil.toJson(wfCdGrpBVoList);
		
		return jsonString;
	}
	
	/** 코드 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WfCdDVo> getWfCdDVoList(String langTypCd, String cdGrpId, List<String> cdGrpIdList, String useYn) throws SQLException{
		if(cdGrpId==null) return null;
		if(langTypCd==null) langTypCd="ko";
		WfCdDVo wfCdDVo = new WfCdDVo();
		wfCdDVo.setQueryLang(langTypCd);
		if(cdGrpId!=null) wfCdDVo.setCdGrpId(cdGrpId);
		if(cdGrpIdList!=null) wfCdDVo.setCdGrpIdList(cdGrpIdList);
		if(useYn!=null) wfCdDVo.setUseYn(useYn);
		return (List<WfCdDVo>) commonDao.queryList(wfCdDVo);
	}
	
	/** 코드그룹 사용여부 조회 */
	public boolean isCdUseList(String cdGrpId, List<String> cdGrpIdList, String cdId, List<String> cdIdList) throws SQLException{
		if(cdGrpId!=null || cdGrpIdList!=null){
			List<WfCdDVo> wfCdDVoList = getWfCdDVoList(null, cdGrpId, cdGrpIdList, null);
			if(wfCdDVoList==null || wfCdDVoList.size()==0) return false;
			
			cdIdList=new ArrayList<String>();
			for(WfCdDVo stroedWfCdDVo : wfCdDVoList){
				cdIdList.add(stroedWfCdDVo.getCdId());
			}
		}
		
		if((cdId==null || cdId.isEmpty()) && (cdIdList==null || cdIdList.size()==0)) return false;
		
		// 테이블관리 기본(WL_TASK_LOG_B) 테이블 - BIND
		WfFormBVo wfFormBVo = new WfFormBVo();
		wfFormBVo.setQueryLang(null);
		
		@SuppressWarnings("unchecked")
		List<WfFormBVo> wfFormBVoList = (List<WfFormBVo>) commonDao.queryList(wfFormBVo);
		
		if(wfFormBVoList==null || wfFormBVoList.isEmpty()) return false;
		
		// 양식번호 목록
		List<String> formNoList=new ArrayList<String>();
		
		// 배포된 양식만 추출
		for(WfFormBVo storedWfFormBVo : wfFormBVoList){
			if(storedWfFormBVo.getGenId()==null) continue;
			formNoList.add(storedWfFormBVo.getFormNo());
		}
		
		if(formNoList.size()==0){
			return false;
		}
		
		WfWorksCodeLVo wfWorksCodeLVo;
		boolean isUse=false;
		for(String formNo : formNoList){
			wfWorksCodeLVo=new WfWorksCodeLVo(formNo);
			//wfWorksCodeLVo.setTypCd("CODE");
			if(cdIdList!=null) wfWorksCodeLVo.setCdVaList(cdIdList);
			else wfWorksCodeLVo.setCdVa(cdId);
			
			if(commonDao.count(wfWorksCodeLVo)>0){
				isUse=true;
				break;
			}
		}
		
		return isUse;
	}
	
	
	/** 코드 목록 세팅 */
	public void setCdList(){
		// 기본으로 설정된 목록 컬럼 조회
		//List<WfFormColmLVo> wfFormColmLVoList = wfAdmSvc.getCurrColmVoList(genId, formNo, null);
	}
}
