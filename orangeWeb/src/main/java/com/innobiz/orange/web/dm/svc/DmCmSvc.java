package com.innobiz.orange.web.dm.svc;

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
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.pt.secu.SecuUtil;

@Service
public class DmCmSvc {
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		if ("DM_CAT_B".equals(tableName)) { // 유형
			return commonSvc.createId(tableName, 'C', 8);
		}else if ("DM_STOR_B".equals(tableName)) { // 저장소
			return commonSvc.createId(tableName, 'T', 8);
		}else if ("DM_STOR_RESC_B".equals(tableName)) { // 저장소 리소스
			return commonSvc.createId(tableName, 'R', 8);
		} else if ("DM_CD_GRP_B".equals(tableName)) { // 코드그룹
			return commonSvc.createId(tableName, 'P', 8);
		} else if ("DM_CD_D".equals(tableName)) { // 코드상세
			return commonSvc.createId(tableName, 'D',  8);
		} else if ("DM_ITEM_B".equals(tableName)) { // 항목기본
			return commonSvc.createId(tableName, 'I',  8);
		} else if ("DM_FLD_B".equals(tableName)) { // 폴더기본
			return commonSvc.createId(tableName, 'F',  8);
		} else if ("DM_COMM_FLD_B".equals(tableName)) { // 공통폴더기본
			return commonSvc.createId(tableName, 'F',  8);
		} else if ("DM_CLS_B".equals(tableName)) { // 분류기본
			return commonSvc.createId(tableName, 'L',  8);
		} else if ("DM_BUMK_B".equals(tableName)) { // 즐겨찾기 기본
			return commonSvc.createId(tableName, 'B',  8);
		} else if ("DM_RESC_B".equals(tableName)) { // 리소스기본
			return commonSvc.createId(tableName, 'S',  8);
		} else if ("DM_PSN_FLD_B".equals(tableName)) { // 개인폴더 기본
			return commonSvc.createId(tableName, 'N',  8);
		} else if ("DM_ITEM_DISP_D".equals(tableName)) { // 항목표시상세
			return commonSvc.createId(tableName, 'M',  8);
		} else if ("DM_TRAN_B".equals(tableName)) { // 이관이력
			return commonSvc.createId(tableName, 'H',  8);
		} 
		
		return null;
	}
	
	/** NO 통합 생성 */
	public Long createNo(String tableName) throws SQLException {
		if("DM_DOC_L".equals(tableName)){
			return commonSvc.nextVal(tableName);
		}
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
	public Integer createFileId(String tableName) throws SQLException {
		if(tableName != null ) return commonSvc.nextVal(tableName).intValue();
		return null;
	}
	
	/** 요청 경로에 따른 list view set page model에 세팅 */
	public void setPageName(ModelMap model , String path , String prefix , String suffix , String bizNm ){
		String pageName = prefix;
		pageName+= bizNm == null ? "" : bizNm;
		if(suffix != null){
			pageName+=suffix;
		}
		//System.out.println(prefix+(suffix != null ? suffix : "")+"Page = "+pageName);
		model.put(prefix+(suffix != null ? suffix : "")+"Page", pageName);
	}
	
	/** 요청 경로 */
	public String getRequestPath( HttpServletRequest request , ModelMap model , String bizNm){
		String path = request.getRequestURI();
		path = path.substring(path.lastIndexOf("/")+1);
		path = path.substring(0,path.lastIndexOf("."));
		model.put("path", path);
		String[] pages = {"list","view","set","trans"};
		if(bizNm != null && "Doc".equals(bizNm)){
			bizNm = getPathSuffix(path);
		}
		for(String prefix : pages){
			setPageName(model, path, prefix , null , bizNm );
		}
		setPageName(model, path, "trans" , "Del" , bizNm);
		return path;
	}
	
	public String getPathSuffix(String path){
		String suffix = "";
		String[] pages = {"list","view","set","trans"};
		for(String page : pages){
			if(path.substring(0, page.length()).equals(page)){
				suffix = path.substring(page.length());
				break;
			}
		}
		if("".equals(suffix)) suffix = "Doc";
		return suffix;
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}
	
	/** 선택 목록에 해당하는 분류체계,폴더 정보맵을 model에 담음 */
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
