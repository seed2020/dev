package com.innobiz.orange.web.wj.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.em.svc.EmCmSvc;
import com.innobiz.orange.web.pt.secu.SecuUtil;

@Service
public class WjCmSvc {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "emCmSvc")
	private EmCmSvc emCmSvc;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		return null;
	}
	
	/** NO 통합 생성 */
	public Long createNo(String tableName) throws SQLException {
		if(tableName != null ) return commonSvc.nextVal(tableName);
		return null;
	}
	
	/** String 을 int 로 변환 후 1을 빼서 String 변환하여 리턴 */
	public String addNo(String no, int add){
		if(no==null || no.isEmpty()) return "1";
		int intNo = Integer.parseInt(no);
		return Integer.toString(intNo + add);
	}
	
	/** 첨부파일 ID 생성 */
	public Integer createFileId() throws SQLException {
		return commonSvc.nextVal("WJ_WORK_REPRT_FILE_D").intValue();
	}
	
	/** 요청 경로에 따른 list view set page model에 세팅 */
	public void setPageName(ModelMap model , String path , String prefix , String suffix , String bizNm ){
		String pageName = prefix;
		pageName+= bizNm == null ? "" : bizNm;
		if(suffix != null){
			pageName+=suffix;
		}
		model.put(prefix+(suffix != null ? suffix : "")+"Page", pageName);
	}
	
	/** 요청 경로 */
	public String getRequestPath( HttpServletRequest request , ModelMap model , String bizNm){
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));		
		String[] pages = {"list","view","set","trans"};
		bizNm=path.replaceAll("list|set|view|trans|Del|PltFrm", "");
		model.put("path", bizNm.toLowerCase());
		for(String prefix : pages){
			setPageName(model, path, prefix , null , bizNm );
		}
		setPageName(model, path, "trans" , "Del" , bizNm);
		return bizNm;
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
	/** 선택 목록에 해당하는 ID 정보를 model에 담음 */
	public void setSelMapByIds(String storId, CommonVo commonVo, List<String> selIdList, String langTypCd,
			ModelMap model, boolean first) throws SQLException {
		List<Map<String,Object>> selList = new ArrayList<Map<String,Object>>();
		
		if(selIdList != null && selIdList.size() > 0) {
			if(storId != null) VoUtil.setValue(commonVo, "storId", storId);// 저장소 ID
			//if(storId == null) VoUtil.setValue(commonVo, "regrUid", userVo.getUserUid());// 등록자 ID
			VoUtil.setValue(commonVo, "selIdList", selIdList);// 선택 ID 목록
			VoUtil.setValue(commonVo, "queryLang", langTypCd);// 언어타입
			@SuppressWarnings("unchecked")
			List<? extends CommonVo> list = (List<? extends CommonVo>)commonSvc.queryList(commonVo);
			Map<String,Object> voMap;
			for(CommonVo storedCommonVo : list){
				voMap = VoUtil.toMap(storedCommonVo, null);
				selList.add(voMap);
			}
		}
		// 화면 구성용 1개의 빈 Map 넣음
		if(first) selList.add(new HashMap<String,Object>());
		if(model != null){
			model.put("selList", selList);
		}
		
	}
	
}
