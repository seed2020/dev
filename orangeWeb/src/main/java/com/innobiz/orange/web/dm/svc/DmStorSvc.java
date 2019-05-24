package com.innobiz.orange.web.dm.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmItemBVo;
import com.innobiz.orange.web.dm.vo.DmRescBVo;
import com.innobiz.orange.web.dm.vo.DmStorBVo;
import com.innobiz.orange.web.dm.vo.DmStorDocRVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;

@Service
public class DmStorSvc {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(DmStorSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 공통 서비스 */
	@Autowired
	private DmCmSvc dmCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
//	/** 문서 서비스 */
//	@Autowired
//	private DmDocSvc dmDocSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "dmRescSvc")
	private DmRescSvc dmRescSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 저장소관리(DM_STOR_B) 테이블 - SELECT */
	public DmStorBVo getDmStorBVo(HttpServletRequest request, ModelMap model, String compId, String storId, String useYn, String msgCode) throws SQLException, CmException {
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		
		//임시
		//if(sysPlocMap == null) sysPlocMap = new HashMap<String, String>();
		//sysPlocMap.put("dmEnable", "Y");
		
		// 문서관리 - 사용여부
		if(sysPlocMap == null || !sysPlocMap.containsKey("dmEnable") || "N".equals(sysPlocMap.get("dmEnable"))){
		    //dm.msg.not.service=문서관리 서비스를 제공하지 않습니다. 
		    String message = messageProperties.getMessage("dm.msg.not.service", request);
			LOGGER.error(message+" : getDmStorBVo()");
			//throw new CmException(message);
			model.put("message", message);
			model.put("togo", "/");
			return null;
		}
		
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		// 저장소ID
		String paramStorId = (String)request.getParameter("paramStorId");
		
		// 이관문서조회
		if((storId == null || storId.isEmpty()) && (request.getRequestURI().endsWith("TransferDoc.do") || request.getRequestURI().endsWith("TransferDocFrm.do") 
				|| request.getRequestURI().endsWith("OpenReqDoc.do") || request.getRequestURI().endsWith("OpenReqDocFrm.do") || request.getRequestURI().endsWith("OpenApvDoc.do"))){
			boolean isDftStor = request.getRequestURI().endsWith("TransferDoc.do") || request.getRequestURI().endsWith("TransferDocFrm.do");
			// 저장소 목록 조회[기본저장소제외]
			List<DmStorBVo> storList = getStorList(userVo.getLangTypCd(), userVo.getCompId(), isDftStor, "Y");
			if(storList == null || storList.size()==0){
				// dm.msg.nodata.transferDoc=이관된 문서가 없습니다.
				msgCode = msgCode != null && !msgCode.isEmpty() ? msgCode : "dm.msg.nodata.transferDoc";
				String message = messageProperties.getMessage(msgCode, request);
				LOGGER.info(message);
				model.put("message", message);
				if(request.getRequestURI().startsWith("/dm/adm")) model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/dm/adm/doc/listDoc.do"));
				else model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/dm/doc/listDoc.do"));
				return null;
			}
			if(paramStorId == null || paramStorId.isEmpty()) {
				if(!isDftStor){
					for(DmStorBVo storVo : storList){
						if(storVo.getDftYn() != null && "Y".equals(storVo.getDftYn())){
							paramStorId = storVo.getStorId();
							break;
						}
					}
				}
				if(paramStorId == null || paramStorId.isEmpty()) paramStorId = storList.get(0).getStorId();
			}
			if(model != null) {
				model.put("storList", storList);
			}
		}
		
		if(model != null && paramStorId != null && !paramStorId.isEmpty()) model.put("paramStorId", paramStorId);
		
		if(storId == null && paramStorId != null && !paramStorId.isEmpty()) storId = paramStorId;
		
		// 회사ID
		/*String paramCompId = (String)request.getParameter("paramCompId");
		
		if(paramCompId != null && !paramCompId.isEmpty()) {
			compId = paramCompId;
			storId = null;
			if(model != null) model.put("paramCompId", paramCompId);
		}*/
		
		if(compId == null ) compId = userVo.getCompId();
		
		// 저장소 조회
		DmStorBVo dmStorBVo = getDmStorBVo(compId, langTypCd, storId);
		if(dmStorBVo == null){//저장소가 Null인 경우
			if(model != null){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				if(paramStorId != null && !paramStorId.isEmpty()){
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
				if(request.getRequestURI().startsWith("/dm/adm/")){//관리자 메뉴
					// dm.msg.nodata.stor=기본저장소가 설정되지 않았습니다.\n저장소를 설정해주십시요.
					msgCode = msgCode != null && !msgCode.isEmpty() ? msgCode : "dm.msg.nodata.stor";
					String url=ptSecuSvc.toAuthMenuUrl(userVo, "/dm/adm/stor/listStor.do");
					url+="&paramCompId="+userVo.getCompId();
					model.put("togo", url);
				}else if(request.getRequestURI().startsWith("/dm/doc/listTransferDoc") || request.getRequestURI().startsWith("/dm/adm/doc/listTransferDoc")){
					// dm.msg.nodata.transferDoc=이관된 문서가 없습니다.
					msgCode = msgCode != null && !msgCode.isEmpty() ? msgCode : "dm.msg.nodata.transferDoc";
					model.put("togo", ptSecuSvc.toAuthMenuUrl(userVo, "/dm/doc/listDoc.do"));
				}else{//사용자 메뉴
					model.put("togo", "/");
					// dm.msg.nodata.stor.user=저장소가 설정되지 않았습니다.\n관리자에게 문의하세요.
					msgCode = msgCode != null && !msgCode.isEmpty() ? msgCode : "dm.msg.nodata.stor.user";
				}
				String message = messageProperties.getMessage(msgCode, request);
				LOGGER.error(message);
				model.put("message", message);
			}
			return null;
		}
		return dmStorBVo;
	}
	
	/** 저장소 정보 조회 */
	public DmStorBVo getDmStorBVo(String compId, String langTypCd, String storId) throws SQLException{
		Map<Integer, DmStorBVo> storMap = getStorMap(langTypCd);
		// 저장소ID가 없으면 기본 저장소를 조회한다.
		if(storId == null){
			List<DmStorBVo> storList = getStorList(langTypCd);
			for(DmStorBVo storedDmStorBVo : storList){
				if(compId.equals(storedDmStorBVo.getCompId()) && "Y".equals(storedDmStorBVo.getDftYn())) return storedDmStorBVo;
			}
			return null;
		}
		int hashId = Hash.hashId(storId);
		return storMap.get(hashId);
	}
	
	/** 저장소 목록 조회 */
	public List<DmStorBVo> getFilteredStorList(String langTypCd, String compId, String tblDispNm) throws SQLException{
		List<DmStorBVo> returnList = new ArrayList<DmStorBVo>();
		List<DmStorBVo> list = getStorList(langTypCd);
		if(list != null){
			for(DmStorBVo dmStorBVo : list){
				if(tblDispNm!=null && dmStorBVo.getStorNm()!=null && dmStorBVo.getStorNm().indexOf(tblDispNm) < 0) continue;
				if(compId != null && !compId.equals(dmStorBVo.getCompId())) continue;
				returnList.add(dmStorBVo);
			}
		}
		return returnList;
	}
	

	/** 저장소 맵 리턴(캐쉬) */
	private Map<Integer, DmStorBVo> getStorMap(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		Map<Integer, DmStorBVo> storMap = (Map<Integer, DmStorBVo>)
				ptCacheSvc.getCache(DmConstant.STOR, langTypCd, "MAP", 10);
		if(storMap!=null) return storMap;
		
		storMap = new HashMap<Integer, DmStorBVo>();
		List<DmStorBVo> storList = loadStorMap(langTypCd, storMap);
		ptCacheSvc.setCache(DmConstant.STOR, langTypCd, "MAP", storMap);
		ptCacheSvc.setCache(DmConstant.STOR, langTypCd, "LIST", storList);
		return storMap;
	}
	
	/** 저장소 목록 리턴(캐쉬) */
	public List<DmStorBVo> getStorList(String langTypCd) throws SQLException{
		@SuppressWarnings("unchecked")
		List<DmStorBVo> storList = (List<DmStorBVo>)
				ptCacheSvc.getCache(DmConstant.STOR, langTypCd, "LIST", 10);
		if(storList!=null) return storList;
		Map<Integer, DmStorBVo> storMap = new HashMap<Integer, DmStorBVo>();
		storList = loadStorMap(langTypCd, storMap);
		ptCacheSvc.setCache(DmConstant.STOR, langTypCd, "MAP", storMap);
		ptCacheSvc.setCache(DmConstant.STOR, langTypCd, "LIST", storList);
		return storList;
	}


	/** [회사] 저장소 목록 조회맵 */
	@SuppressWarnings("unchecked")
	private List<DmStorBVo> loadStorMap(String langTypCd, Map<Integer, DmStorBVo> storMap) throws SQLException{
		// 저장소 조회
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setQueryLang(langTypCd);
		//dmStorBVo.setCompId(compId);
		//dmStorBVo.setUseYn("Y");
		List<DmStorBVo> storList = (List<DmStorBVo>)commonDao.queryList(dmStorBVo);
		DmStorBVo storedDmStorBVo;
		int i, size = storList==null ? 0 : storList.size();
		for(i=0;i<size;i++){
			storedDmStorBVo = storList.get(i);
			storMap.put(Hash.hashId(storedDmStorBVo.getStorId()), storedDmStorBVo);
		}
		return storList;
	}
	
	/** 저장소 목록 조회 */
	public List<DmStorBVo> getStorList(String langTypCd, String compId, boolean isNotStor, String useYn) throws SQLException{
		// 저장소 목록을 담을 배열
		List<DmStorBVo> returnList = new ArrayList<DmStorBVo>();
		// 저장소 목록 조회
		List<DmStorBVo> storList = getStorList(langTypCd);
		for(DmStorBVo storedDmStorBVo : storList){
			// 회사ID 비교
			if(compId != null && !compId.equals(storedDmStorBVo.getCompId())) continue;
			// 기본항목제외
			if(isNotStor && "Y".equals(storedDmStorBVo.getDftYn())) continue;
			if(useYn != null && !useYn.equals(storedDmStorBVo.getUseYn())) {
				continue;
			}
			returnList.add(storedDmStorBVo);
		}
		return returnList;
	}
	
	/** 저장소ID 조회 - 문서그룹ID*/
	public String getStorId(String docGrpId) throws SQLException {
		if(docGrpId == null || docGrpId.isEmpty()) return null;
		DmStorDocRVo dmStorDocRVo = new DmStorDocRVo();
		dmStorDocRVo.setDocGrpId(docGrpId);
		dmStorDocRVo = (DmStorDocRVo)commonDao.queryVo(dmStorDocRVo);
		if(dmStorDocRVo == null) return null;
		return dmStorDocRVo.getStorId();
	}
	
	/** 저장소ID 저장, 삭제 - 문서그룹ID*/
	public void saveStorId(QueryQueue queryQueue, String storId, String docGrpId, boolean isDel) throws SQLException {
		if(docGrpId == null || docGrpId.isEmpty()) return;
		DmStorDocRVo dmStorDocRVo = new DmStorDocRVo();
		dmStorDocRVo.setStorId(storId);
		if(docGrpId != null && !docGrpId.isEmpty()) dmStorDocRVo.setDocGrpId(docGrpId);
		if(isDel){
			queryQueue.delete(dmStorDocRVo);
			return;
		}
		if(commonDao.count(dmStorDocRVo)>0) return;
		queryQueue.store(dmStorDocRVo);
	}
	
	/** 테이블명 생성 */
	@SuppressWarnings("unchecked")
	public String createTblNm(String compId) throws SQLException {
		DmStorBVo dmStorBVo = new DmStorBVo();
		//dmStorBVo.setCompId(compId);
		List<DmStorBVo> dmStorBVoList = (List<DmStorBVo>) commonDao.queryList(dmStorBVo);
		int maxIndex = 0;
		for (DmStorBVo storedDmStorBVo : dmStorBVoList) {
			String tblNm = storedDmStorBVo.getTblNm();
			if(tblNm==null) continue;
			Integer index = IdUtil.toInt(tblNm.substring(1));
			if (index > maxIndex) {
				maxIndex = index;
			}
		}
		return 'R' + IdUtil.createId(maxIndex+1, 4);
	}
	
	/** 초기 테이블 리소스 맵 */
	public Map<String,String> getDftRescMap(HttpServletRequest request, String compId, String langTypCd, String prefix) throws SQLException{
		Map<String,String> returnMap = new HashMap<String,String>();
		// 리소스 prefix 설정
		String rescPrefix = prefix == null || prefix.isEmpty() ? "resc" : prefix + "Resc";
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(compId, langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			returnMap.put(rescPrefix+"Va_" + ptCdBVo.getCd(), (ptCmSvc.getPtCompBVo(compId, langTypCd)).getCompNm());
		}
		return returnMap;
		
	}
	
	/** 테이블명 중복 체크 */
	public boolean isTblNmExist(String tblNm) throws SQLException {
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setSchCat("tblNm");
		dmStorBVo.setSchWord(tblNm);
		return (commonDao.count(dmStorBVo) > 0);
	}
	
	/** 저장된 항목 비교 */
	public DmItemBVo getChkDmItemBVo(DmItemBVo dmItemBVo, List<DmItemBVo> dmItemBVoList){
		for(DmItemBVo storedDmItemBVo : dmItemBVoList){
			if(dmItemBVo.getItemNm().equals(storedDmItemBVo.getItemNm())){
				return storedDmItemBVo;
			}
		}
		return null;
	}
	
	/** 추가 항목 항목 바인드 */
	public List<DmItemBVo> getAddItemList(ModelMap model, List<DmItemBVo> dmItemBVoList) throws SQLException {
		//if(dmItemBVoList == null) dmItemBVoList = new ArrayList<DmItemBVo>();
		String[] items = new String[]{"TEXT","TEXTAREA","PHONE","CALENDAR","CODE"};
		int cnt=2,i;
		DmItemBVo dmItemBVo = null,storedDmItemBVo = null;
		int index = 1;
		List<DmItemBVo> returnList = new ArrayList<DmItemBVo>();
		for(String item:items){
			for(i=0;i<cnt;i++){
				dmItemBVo = new DmItemBVo();
				dmItemBVo.setItemNm(IdUtil.createId('C', (long)index, 2));
				//db에서 조회한 항목이 있을경우
				if(dmItemBVoList != null && dmItemBVoList.size() > 0){
					storedDmItemBVo = getChkDmItemBVo(dmItemBVo, dmItemBVoList);
					if(storedDmItemBVo != null) BeanUtils.copyProperties(storedDmItemBVo, dmItemBVo);
				}
				if(dmItemBVo.getItemTyp() == null || dmItemBVo.getItemTyp().isEmpty() ){
					dmItemBVo.setItemTyp(item);
					dmItemBVo.setDataTyp("VARCHAR");
					toDbmsDataType(dmItemBVo);
					if("TEXT".equals(item)) dmItemBVo.setColmLen(120);
					else if("TEXTAREA".equals(item)) dmItemBVo.setColmLen(2000);
					else if("PHONE".equals(item)) dmItemBVo.setColmLen(14);
					else if("CALENDAR".equals(item)) dmItemBVo.setColmLen(10);
					else if("CODE".equals(item)) dmItemBVo.setColmLen(30);
					else dmItemBVo.setColmLen(30);
				}
				dmItemBVo.setAddItemYn("Y");
				returnList.add(dmItemBVo);
				index++;
			}
		}
		if(model!=null) model.put("dmItemBVoList", returnList);
		return returnList;
	}
	
	/** 기본 항목 Map<String,항목Vo> 변환*/
	public Map<String,DmItemBVo> getDftColmVoMap(HttpServletRequest request, String storId) throws SQLException {
		Map<String,DmItemBVo> returnMap = new HashMap<String,DmItemBVo>();
		List<DmItemBVo> colmVoList = getDftColmList(request, new ArrayList<DmItemBVo>(), null, null);
		for(DmItemBVo colmVo : colmVoList){
			// key : 속성ID
			setColmNmChn(colmVo);
			returnMap.put(StringUtil.toCamelNotation(colmVo.getItemNm(), false),colmVo);
		}
		return returnMap;
	}
	
	/** 특정 메뉴(인수인계,이관,열람요청) 추가 항목 바인드*/
	public List<DmItemBVo> getAddColmList(HttpServletRequest request, List<DmItemBVo> colmVoList, QueryQueue queryQueue, String storId) throws SQLException {
		// 추가 항목 생성, 저장
		colmVoList.add(makeColmVo(request, null, null, "TAK_ORG_ID","dm.cols.takOrgNm","VARCHAR",30,"Y","Y","N","N","N","Y","ID",DmConstant.ORG_ID));		// 조직ID
		colmVoList.add(makeColmVo(request, null, null, "TGT_ORG_ID","dm.cols.tgtOrgNm","VARCHAR",30,"Y","Y","N","N","N","Y","ID",DmConstant.ORG_ID));		// 대상조직ID
		colmVoList.add(makeColmVo(request, null, null, "TAK_STAT_CD","dm.cols.takStatNm","VARCHAR",30,"Y","Y","N","N","N","Y","CD",DmConstant.TAK_STAT_CD));		// 인수인계상태코드
		colmVoList.add(makeColmVo(request, null, null, "TAK_REG_DT","dm.cols.takRegDt","DATETIME",null,"Y","N","Y","Y","Y","Y","CALENDAR",null));		// 인수인계상태코드
		colmVoList.add(makeColmVo(request, null, null, "ERR_CONT","dm.cols.errCont","TEXT",null,"Y","N","Y","Y","Y","Y","TEXTAREA",null));		// 오류내용
		colmVoList.add(makeColmVo(request, null, null, "TGT_ID","dm.cols.dtlView.request.tgt","VARCHAR",30,"Y","Y","N","N","N","Y","CID","user,org"));		// 대상ID
		colmVoList.add(makeColmVo(request, null, null, "TGT_TYP_CD","dm.cols.dtlView.request.tgtTyp","VARCHAR",30,"Y","Y","N","N","N","Y","CD",DmConstant.VIEW_TGT_CD));		// 대상구분
		colmVoList.add(makeColmVo(request, null, null, "VIEW_REQ_STAT_CD","dm.cols.dtlView.reqStatCd","VARCHAR",30,"Y","Y","N","N","N","Y","CD",DmConstant.VIEW_STAT_CD));		// 열람요청상태코드
		colmVoList.add(makeColmVo(request, null, null, "READ_DT","dm.cols.dtlView.prd","DATETIME",null,"Y","Y","N","N","N","Y","CALENDAR","MERGE"));		// 열람시작일시
		/*colmVoList.add(makeColmVo(request, null, null, "READ_STRT_DT","dm.cols.dtlView.strtDt","DATETIME",null,"Y","Y","N","N","N","Y","CALENDAR",null));		// 열람시작일시
		colmVoList.add(makeColmVo(request, null, null, "READ_END_DT","dm.cols.dtlView.endDt","DATETIME",null,"Y","Y","N","N","N","Y","CALENDAR",null));		// 열람종료일시
*/		colmVoList.add(makeColmVo(request, null, null, "REQ_USER_UID","dm.cols.dtlView.request.userNm","VARCHAR",30,"Y","N","Y","Y","Y","Y","UID",null));			// 요청사용자UID
		colmVoList.add(makeColmVo(request, null, null, "REQ_DT","dm.cols.dtlView.reqDt","DATETIME",null,"Y","Y","N","N","N","Y","CALENDAR",null));			// 요청일시
		colmVoList.add(makeColmVo(request, null, null, "RJT_OPIN","cols.rjtOpin","VARCHAR",400,"Y","Y","N","N","N","Y","TEXTAREA",null));			// 반려의견
		colmVoList.add(makeColmVo(request, null, null, "DISC_STAT_CD","cols.docStat","VARCHAR",30,"Y","Y","N","N","N","Y","CD",DmConstant.VIEW_STAT_CD));		// 문서상태코드
		
		return colmVoList;
	}
	
	/** 기본 항목 바인드*/
	public List<DmItemBVo> getDftColmList(HttpServletRequest request, List<DmItemBVo> colmVoList, QueryQueue queryQueue, String storId) throws SQLException {
		// 기본 항목 생성, 저장		
		colmVoList.add(makeColmVo(request, queryQueue, storId, "VER_VA","dm.cols.verNo","VARCHAR",5,"Y","N","Y","Y","Y","N","TEXT",null));		// 버전
		colmVoList.add(makeColmVo(request, queryQueue, storId, "FLD_ID","dm.cols.fld","VARCHAR",30,"Y","N","Y","Y","Y","N","FLD",null));		// 폴더ID
		colmVoList.add(makeColmVo(request, queryQueue, storId, "CLS_ID","dm.cols.cls","VARCHAR",30,"Y","N","Y","Y","Y","N","CLS",null));		// 분류체계ID
		colmVoList.add(makeColmVo(request, queryQueue, storId, "DOC_NO","dm.cols.docNo","VARCHAR",30,"Y","N","Y","Y","Y","Y","TEXT",null));		// 문서번호
		colmVoList.add(makeColmVo(request, queryQueue, storId, "SUBJ","cols.subj","NVARCHAR",240,"Y","N","Y","Y","Y","Y","TEXT",null));		// 제목
		colmVoList.add(makeColmVo(request, queryQueue, storId, "CONT","cols.cont","TEXT",null,"Y","N","Y","Y","Y","N","EDITOR",null));		// 내용
		colmVoList.add(makeColmVo(request, queryQueue, storId, "FILE_CNT","cols.att","VARCHAR",30,"Y","N","Y","Y","Y","N","FILE",null));		// 첨부파일건수
		colmVoList.add(makeColmVo(request, queryQueue, storId, "OWNR_UID","dm.cols.ownr","VARCHAR",30,"Y","N","Y","Y","Y","Y","UID",null));		// 소유자
		colmVoList.add(makeColmVo(request, queryQueue, storId, "DOC_KEEP_PRD_CD","dm.cols.docKeepPrd","VARCHAR",30,"Y","N","Y","Y","Y","Y","CD",DmConstant.DOC_KEEP_PRD_CD));		// 문서보존기간
		colmVoList.add(makeColmVo(request, queryQueue, storId, "SECUL_CD","dm.cols.secul","VARCHAR",30,"Y","N","Y","Y","Y","N","CD",DmConstant.SECUL_CD));		// 보안등급코드
		colmVoList.add(makeColmVo(request, queryQueue, storId, "CMPL_DT","dm.cols.cmplDt","DATETIME",null,"Y","N","Y","Y","Y","Y","CALENDAR",null));		// dm.cols.cmplDt=완료일시
		colmVoList.add(makeColmVo(request, queryQueue, storId, "READ_CNT","cols.readCnt","BIGINT",null,"Y","N","Y","Y","Y","N","TEXT",null));		// cols.readCnt=조회수
		colmVoList.add(makeColmVo(request, queryQueue, storId, "REGR_UID","cols.regr","VARCHAR",30,"Y","N","Y","Y","Y","Y","UID",null));		// 등록자
		colmVoList.add(makeColmVo(request, queryQueue, storId, "REG_DT","cols.regDt","DATETIME",null,"Y","N","Y","Y","Y","Y","CALENDAR",null));		// 등록일시
		colmVoList.add(makeColmVo(request, queryQueue, storId, "MODR_UID","cols.modr","VARCHAR",30,"Y","N","Y","Y","Y","N","UID",null));		// 수정자
		colmVoList.add(makeColmVo(request, queryQueue, storId, "MOD_DT","cols.modDt","DATETIME",null,"Y","N","Y","Y","Y","N","CALENDAR",null));		// 수정일시
		return colmVoList;
	}
	
	/** 테이블항목 바인드 */
	public List<DmItemBVo> makeColmVoList(HttpServletRequest request, QueryQueue queryQueue, DmStorBVo dmStorBVo) throws SQLException {
		List<DmItemBVo> dftVoList = new ArrayList<DmItemBVo>();
		String storId = dmStorBVo.getStorId();
		List<DmItemBVo> colmVoList = getDftColmList(request, dftVoList, queryQueue, storId);
		
		for (DmItemBVo dmItemBVo : colmVoList) {
			dmItemBVo.setStorId(storId);
			// 테이블항목(DM_ITEM_B) 테이블
			dmItemBVo.setItemId(dmCmSvc.createId("DM_ITEM_B"));
			
			// 테이블항목(DM_ITEM_B) 테이블 - UPDATE or INSERT		
			queryQueue.insert(dmItemBVo);
		}
		
		//colmVoList.addAll(getDftColmList(request, colmVoList, queryQueue, dmStorBVo.getStorId()));
		
		//추가 항목
		//colmVoList.addAll(getAddItemList(null, null));
		return colmVoList;
	}

	/** 항목VO(DmItemBVo) 생성, 바인드, 저장 */
	private DmItemBVo makeColmVo(HttpServletRequest request, QueryQueue queryQueue, String storId, String colmNm, String titleId,
			String dataTyp, Integer colmLen, String useYn, String addItemYn, String regDispYn, 
			String modDispYn, String readDispYn, String listDispYn, String itemTyp, String itemTypVa) throws SQLException {
		
		DmItemBVo dmItemBVo = new DmItemBVo();
		dmItemBVo.setItemNm(colmNm);
		if(request != null) dmItemBVo.setItemDispNm(messageProperties.getMessage(titleId, request));
		dmItemBVo.setDataTyp(dataTyp);
		dmItemBVo.setColmLen(colmLen);
		dmItemBVo.setUseYn(useYn);
		dmItemBVo.setAddItemYn(addItemYn);
		// 항목구분 [TEXT,EDITOR,CALENDAR,PHONE,UID,CD]
		if(itemTyp != null) dmItemBVo.setItemTyp(itemTyp);
		// 항목구분값 
		if(itemTypVa != null) dmItemBVo.setItemTypVa(itemTypVa);
		
		//목록등 표시여부
		if(regDispYn != null) dmItemBVo.setRegDispYn(regDispYn);
		if(modDispYn != null) dmItemBVo.setModDispYn(modDispYn);
		if(readDispYn != null) dmItemBVo.setReadDispYn(readDispYn);
		if(listDispYn != null) dmItemBVo.setListDispYn(listDispYn);
		
		if(storId != null){//저장소 ID가 Null 이 아닐 경우
			toDbmsDataType(dmItemBVo);// dbms 별 데이터 타입 세팅
			
			DmRescBVo dmRescBVo = dmRescSvc.setDmRescBVo(request, titleId, queryQueue, storId);//리소스 저장
			// 리소스 조회 후 리소스ID와 리소스명 세팅
			dmItemBVo.setRescId(dmRescBVo.getRescId());
			dmItemBVo.setItemDispNm(dmRescBVo.getRescVa());
		}
		
		return dmItemBVo;
	}
	
	/** 저장소 테이블 인덱스 - CREATE */
	public void createIdx(QueryQueue queryQueue, String tblNm) throws SQLException, CmException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("createIdx called - idxNm = IDX_" + tblNm);
		}
		// 테이블 구분자
		String tblPrefix = DmConstant.TBl_NM_PREFIX;
		// 문서기본목록 생성
		DmStorBVo dmStorBVo = null;
		String fullTblNm = null;
		// 테이블 생성
		for(String tbl : DmConstant.CREATE_IDXS){
			fullTblNm = tblPrefix+tblNm+StringUtil.fromCamelNotation(tbl.substring(2));
			// 인덱스 생성
			dmStorBVo = new DmStorBVo();
			dmStorBVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmStorBDao.create"+tbl+"Idx");
			dmStorBVo.setTblNm(fullTblNm);
			queryQueue.update(dmStorBVo);
		}
	}
	
	/** 저장소 테이블 - CREATE */
	private void createTbl(String compId, String tblNm, List<DmItemBVo> colmVoList, QueryQueue queryQueue) throws SQLException, CmException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("createTbl called - tblNm = " + tblNm);
		}
		// 테이블 구분자
		String tblPrefix = DmConstant.TBl_NM_PREFIX;
		// 문서기본목록 생성
		DmStorBVo dmStorBVo = null;//new DmStorBVo();
		String fullTblNm = null;
		// 테이블 생성
		for(String tbl : DmConstant.CREATE_TBLS){
			fullTblNm = tblPrefix+tblNm+StringUtil.fromCamelNotation(tbl.substring(2));
			dmStorBVo = new DmStorBVo();
			if("DmDocL".equals(tbl)){// 기본문서
				dmStorBVo.setColmVoList(colmVoList);
			}
			dmStorBVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmStorBDao.create"+tbl);
			dmStorBVo.setTblNm(fullTblNm);
			queryQueue.update(dmStorBVo);
			// 인덱스 생성
			/*if(ArrayUtil.isInArray(DmConstant.CREATE_IDXS, tbl)){
				dmStorBVo = new DmStorBVo();
				dmStorBVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmStorBDao.create"+tbl+"Idx");
				dmStorBVo.setTblNm(fullTblNm);
				queryQueue.update(dmStorBVo);
			}*/
		}
		
		/*try {
			dmStorBVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmStorBDao.createDmStorL");
			commonDao.update(dmStorBVo);
			
		} catch (SQLException e) {
			throw e;
		} catch (CmException e) {
			throw e;
		}*/
	}
	
	/** 저장소 테이블 - CREATE */
	public void createTables(DmStorBVo newDmStorBVo) throws SQLException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("createTbl called - tblNm = " + newDmStorBVo.getTblNm());
		}
		// 중복된 테이블명 체크
		if (isTblNmExist(newDmStorBVo.getTblNm())) {
			LOGGER.error("table name is allready!!");
			return;
		}
				
		String[][] tables = null;
		
		boolean isMssql = "mssql".equals(contextProperties.getProperty("dbms"));
		//boolean isOracle = "oracle".equals(contextProperties.getProperty("dbms"));
		//boolean isMysql = "mysql".equals(contextProperties.getProperty("dbms"));
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"DmDocL", "5"},
					{"DmDocD", "21"},
					{"DmClsR", "4"},
					{"DmDocVerL", "5"},
					{"DmKwdL", "4"},
					{"DmFileD", "12"},
					{"DmTaskH", "7"}
			};
		} else if(isOracle()){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"DmDocL", "13"},
					{"DmDocD", "23"},
					{"DmClsR", "6"},
					{"DmDocVerL", "7"},
					{"DmKwdL", "6"},
					{"DmFileD", "14"},
					{"DmTaskH", "9"}
			};
		} else if(isMysql()){
			// { query-id, 해당 id 내의 sql 갯수 }
			tables = new String[][]{
					{"DmDocL", "3"},
					{"DmDocD", "3"},
					{"DmClsR", "3"},
					{"DmDocVerL", "3"},
					{"DmKwdL", "3"},
					{"DmFileD", "3"},
					{"DmTaskH", "3"}
			};
		} else {
			return;
		}
		
		String sqlIdPrefix = isMssql ?
				"com.innobiz.orange.web.dm.dao.DmxTableMSSQLDao.create" : 
					isMysql() ? "com.innobiz.orange.web.dm.dao.DmxTableMYSQLDao.create" : 
						"com.innobiz.orange.web.dm.dao.DmxTableORACLEDao.create";
		
		DmStorBVo dmStorBVo = new DmStorBVo();
		int i, size;
		for(String[] table : tables){
			dmStorBVo.setInstanceQueryId(sqlIdPrefix+table[0]);
			size = Integer.parseInt(table[1]);
			
			if("DmDocL".equals(table[0])){// 기본문서에 확장컬럼 추가
				dmStorBVo.setColmVoList(getAddItemList(null, null));
			}
			dmStorBVo.setTblNm(newDmStorBVo.getTblNm());
			// 테이블 생성
			for(i=1;i<=size;i++){
				dmStorBVo.setSqlSeq(i);
				commonDao.update(dmStorBVo);
			}
		}
		
		// 인덱스 생성
		String[][] indexes = null;
		
		if(isMssql){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"DmDocL", "1"},
					{"DmDocD", "1"}
			};
		} else if(isOracle()){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"DmDocL", "1"},
					{"DmDocD", "1"}
			};
		} else if(isMysql()){
			// { query-id, 해당 id 내의 sql 갯수 }
			indexes = new String[][]{
					{"DmDocL", "1"},
					{"DmDocD", "1"}
			};
		} else {
			return;
		}
		
		sqlIdPrefix = isMssql ?
				"com.innobiz.orange.web.dm.dao.DmxTableMSSQLDao.index" : 
					isMysql() ? "com.innobiz.orange.web.dm.dao.DmxTableMYSQLDao.index": 
						"com.innobiz.orange.web.dm.dao.DmxTableORACLEDao.index";
		
		for(String[] index : indexes){
			dmStorBVo.setInstanceQueryId(sqlIdPrefix+index[0]);
			size = Integer.parseInt(index[1]);
			// 인덱스 생성
			for(i=1;i<=size;i++){
				dmStorBVo.setSqlSeq(i);
				commonDao.update(dmStorBVo);
			}
		}
		
	}
	
	/** 저장소 테이블 - DROP */
	public void dropTables(String tblNm, String compId, QueryQueue queryQueue) throws SQLException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("dropTbl called - tblNm = " + tblNm);
		}
		
		DmStorBVo dmStorBVo = null;
		// 테이블 구분자
		String tblPrefix = DmConstant.TBl_NM_PREFIX;
		String fullTblNm = null;
		for(String tbl : DmConstant.CREATE_TBLS){
			dmStorBVo = new DmStorBVo();
			fullTblNm = tblPrefix+tblNm+StringUtil.fromCamelNotation(tbl.substring(2));
			// 저장소(DM_X000X_L) 테이블 - DROP
			dmStorBVo.setTblNm(fullTblNm);
			dmStorBVo.setInstanceQueryId("com.innobiz.orange.web.dm.dao.DmStorBDao.dropDmStorL");
			queryQueue.update(dmStorBVo);
		}
	}

	/** 테이블 등록 */
	public void insertTbl(HttpServletRequest request, DmStorBVo dmStorBVo, QueryQueue queryQueue, String compId, boolean isExecute)
			throws CmException, SQLException {
		
		// 중복된 테이블명 체크
		if (isTblNmExist(dmStorBVo.getTblNm())) {
			// bb.msg.dupTblNm=중복된 테이블명입니다.
			throw new CmException("bb.msg.dupTblNm", request);
		}
		
		// 저장소항목 목록조회
		List<DmItemBVo> colmVoList = makeColmVoList(request, queryQueue, dmStorBVo);
		
		// 테이블 생성시 제외할 컬럼을 추출한다.[분류체계ID...]
		List<DmItemBVo> excludeList = new ArrayList<DmItemBVo>();
		for(DmItemBVo storedDmItemBVo : colmVoList){
			for(String exCol : DmConstant.COLM_NM_EXCLUDE){
				if(storedDmItemBVo.getItemNm().equals(exCol)){
					excludeList.add(storedDmItemBVo);
				}
			}
		}
		// 제외할 컬럼이 있을경우
		if(excludeList.size()>0){
			for(DmItemBVo storedDmItemBVo : excludeList){
				colmVoList.remove(storedDmItemBVo);
			}
		}
		//추가항목
		colmVoList.addAll(getAddItemList(null, null));
		// 테이블 - CREATE
		createTbl(compId, dmStorBVo.getTblNm(), colmVoList, queryQueue);
		
		// 저장소관리(DM_STOR_B) 테이블 - INSERT
		queryQueue.insert(dmStorBVo);
		
		if(!isExecute) return;
		try {
			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, DmConstant.STOR);
			commonDao.execute(queryQueue);
			ptCacheExpireSvc.checkNow(DmConstant.STOR);
		} catch (SQLException e) {
			LOGGER.error("[ERROR] createTbl is error - table name : "+dmStorBVo.getTblNm());
			// 저장소 테이블 - DROP
			deleteTbl(request, dmStorBVo.getStorId(), queryQueue);
			//dropTbl(dmStorBVo.getTblNm(), compId);
			throw e;
		}
	}

	/** 테이블 삭제 */
	public void deleteTbl(HttpServletRequest request, String storId, QueryQueue queryQueue)
			throws SQLException {
		
		//저장소관리 BIND
		DmStorBVo dmStorBVo = new DmStorBVo();
		dmStorBVo.setStorId(storId);

		// 저장소관리(DM_STOR_B) 테이블 - SELECT
		DmStorBVo storedDmStorBVo = (DmStorBVo) commonDao.queryVo(dmStorBVo);
		// 기본저장소 삭제 금지
		//if("Y".equals(storedDmStorBVo.getDftYn())) return;
		
		String compId = storedDmStorBVo.getCompId();
		// 리소스기본(DM_RESC_B) 테이블 - DELETE
		if (storedDmStorBVo.getRescId() != null && !storedDmStorBVo.getRescId().isEmpty()) {
			DmRescBVo dmRescBVo = new DmRescBVo(storId);
			dmRescBVo.setRescId(storedDmStorBVo.getRescId());
			queryQueue.delete(dmRescBVo);
		}
		
		// 저장소관리(DM_STOR_B) 테이블 - DELETE
		queryQueue.delete(dmStorBVo);
		
		// 저장소ID를 가지고 있는 테이블 데이터 삭제
		storQue(queryQueue, storId, null, "del");
		
		// 테이블 - DROP
		dropTables(storedDmStorBVo.getTblNm(), compId, queryQueue);
	}
	
	/** 저장소 이관 - storId : 이관되기 전 저장소ID [문서를 제외한 코드성 데이터] */
	@SuppressWarnings("unchecked")
	public void storQue(QueryQueue queryQueue, String storId, String copyStorId, String action){
		//패키지 경로[추후 클래스를 통한 패키지 자동 매핑]
		String pakage = DmConstant.pakage;
		
		//1.select insert를 통한 데이터 복사
		//2.이관할 목록을 조회해서 건별 복사
		
		try {
			Class<? extends CommonVo> clazz = null;
			//이관할 VO 명 배열
			CommonVo commonVo;
			for(String nm : DmConstant.COPY_VOS){
				nm = pakage + "."+nm;
				clazz = (Class<? extends CommonVo>)Class.forName(nm);
				//VoUtil.setValue(commonVo, "tblSuffix", "_H");// 이력테이블에 기존거 복사(현재 구성안됨) - 추후 설계 변경 가능(공통)
				if(ArrayUtil.isInArray(DmConstant.STOR_VOS, nm)){
					commonVo = clazz.getDeclaredConstructor(String.class).newInstance(storId);
				}else{
					commonVo = clazz.newInstance();
					VoUtil.setValue(commonVo, "storId", storId);
				}
				
				if("copy".equals(action) && copyStorId != null && !copyStorId.isEmpty()){
					// 이관할 목록을 조회해서 건별 복사
					if(commonDao.count(commonVo) > 0){
						List<? extends CommonVo> list = (List<? extends CommonVo>)commonDao.queryList(commonVo);
						for(CommonVo storedCommonVo : list){
							VoUtil.setValue(storedCommonVo, "storId", copyStorId);
							queryQueue.insert(storedCommonVo);
						}
					}
				}else if("del".equals(action)){
					queryQueue.delete(commonVo);
				}
			}
 
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	/** 저장소 이관 - 생성 테이블 목록  */
	@SuppressWarnings("unchecked")
	public void transferDocQue(QueryQueue queryQueue, String tableName, String copyTableName, String[] docGrpIds) throws SQLException{
		//패키지 경로[추후 클래스를 통한 패키지 자동 매핑]
		String pakage = DmConstant.pakage;
		
		//1.select insert를 통한 데이터 복사
		//2.이관할 목록을 조회해서 건별 복사
		
		try {
			// 이관할 VO 목록
			List<CommonVo> voList = new ArrayList<CommonVo>();
			Class<? extends CommonVo> clazz = null;
			//이관할 VO 명 배열
			CommonVo commonVo;
			for(String nm : DmConstant.CREATE_TBLS){
				// 기본문서 제외
				if("DmDocL".equals(nm)) continue;
				nm = pakage + "."+nm;
				clazz = (Class<? extends CommonVo>)Class.forName(nm);
				//VoUtil.setValue(commonVo, "tblSuffix", "_H");// 이력테이블에 기존거 복사(현재 구성안됨) - 추후 설계 변경 가능(공통)
				commonVo = clazz.newInstance();
				// 생성테이블에 테이블명 세팅
				VoUtil.setValue(commonVo, "tableName", tableName);
				voList.add(commonVo);
			}
			
			if(voList.size()>0){
				DmDocLVo dmDocLVo = new DmDocLVo();
				dmDocLVo.setTableName(tableName);
				if("A".equals("")){
					//List<DmDocLVo> docList = (List<DmDocLVo>)commonDao.queryList(dmDocLVo);
				}else{
					if(docGrpIds != null && docGrpIds.length>0){
						
					}
				}
			}
			
 
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	/** 항목명이 코드일 경우 속성ID를 변경해준다. */
	public void setColmNmChn(DmItemBVo colmVo){
		for(String nm : DmConstant.COLM_NM_CDS){
			if(colmVo.getItemNm().endsWith(nm)){
				colmVo.setItemNm(colmVo.getItemNm().replaceAll(nm,DmConstant.COLM_NM_SUFFIX).trim());
			}
		}
	}
	
	/** 테이블명을 이력으로 변경 */
	public void setDmTblSuffix(CommonVo... commonVoList){
		for(CommonVo commonVo : commonVoList){
			VoUtil.setValue(commonVo, "tblSuffix", "_H");
		}
	}
	
	private boolean dbmsChecked = false;
	private boolean dbmsOracle = false;
	private boolean dbmsMysql = false;
	
	private void checkDbms(){
		String dbms = contextProperties.getProperty("dbms");
		dbmsOracle = "oracle".equals(dbms);
		dbmsMysql  = "mysql" .equals(dbms);
		dbmsChecked = true;
	}
	
	/** 오라클 체크 */
	public boolean isOracle(){
		if(!dbmsChecked) checkDbms();
		return dbmsOracle;
	}
	public boolean isMysql(){
		if(!dbmsChecked) checkDbms();
		return dbmsMysql;
	}
	/** DBMS 별 컬럼타입, 길이 변환 - 기준 MS-SQL */
	private void toDbmsDataType(DmItemBVo dmItemBVo){
		if(isOracle()){
			String dataTyp = dmItemBVo.getDataTyp();
			if("VARCHAR".equals(dataTyp) || "NVARCHAR".equals(dataTyp)){
				dmItemBVo.setDataTyp("VARCHAR2");
			} else if("INT".equals(dataTyp)){
				dmItemBVo.setDataTyp("NUMBER");
				dmItemBVo.setColmLen(10);
			} else if("BIGINT".equals(dataTyp)){
				dmItemBVo.setDataTyp("NUMBER");
				dmItemBVo.setColmLen(22);
			} else if("DATETIME".equals(dataTyp)){
				dmItemBVo.setDataTyp("DATE");
				dmItemBVo.setColmLen(null);
			} else if("TEXT".equals(dataTyp)){
				dmItemBVo.setDataTyp("CLOB");
				dmItemBVo.setColmLen(null);
			} else if("IMAGE".equals(dataTyp)){
				dmItemBVo.setDataTyp("BLOB");
				dmItemBVo.setColmLen(null);
			}
		} else if(isMysql()){
			String dataTyp = dmItemBVo.getDataTyp();
			if(dataTyp.equals("INT") || dataTyp.equals("BIGINT") || dataTyp.equals("DATETIME")){
				// 그대로
			} else if("NVARCHAR".equals(dataTyp)){
				dmItemBVo.setDataTyp("VARCHAR");
			} else if("TEXT".equals(dataTyp) || "NTEXT".equals(dataTyp)){
				dmItemBVo.setDataTyp("LONGTEXT");
				dmItemBVo.setColmLen(null);
			} else if(dataTyp.equals("IMAGE") || dataTyp.equals("FILE") || dataTyp.equals("VARBINARY")){
				dmItemBVo.setDataTyp("LONGBLOB");
				dmItemBVo.setColmLen(null);
			}
		}
	}
	
	

	
}
