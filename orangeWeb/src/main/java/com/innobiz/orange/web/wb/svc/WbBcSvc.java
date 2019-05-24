package com.innobiz.orange.web.wb.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.wb.vo.WbBcApntrRVo;
import com.innobiz.orange.web.wb.vo.WbBcBVo;
import com.innobiz.orange.web.wb.vo.WbBcCntcDVo;
import com.innobiz.orange.web.wb.vo.WbBcFldBVo;
import com.innobiz.orange.web.wb.vo.WbBcImgDVo;
import com.innobiz.orange.web.wb.vo.WbBcLstSetupBVo;
import com.innobiz.orange.web.wb.vo.WbBcMetngDVo;
import com.innobiz.orange.web.wb.vo.WbBcUserLstSetupRVo;
import com.innobiz.orange.web.wb.vo.WbBcUserScrnSetupRVo;

@Service
public class WbBcSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WbBcSvc.class);
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 첨부파일 서비스 */
	@Autowired
	private WbBcFileSvc wbBcFileSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 폴더 서비스 */
	@Autowired
	private WbBcFldSvc wbBcFldSvc;
	
	/** 명함 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<WbBcBVo> getWbBcList(WbBcBVo wbBcBVo ) throws SQLException {
		return (List<WbBcBVo>)commonDao.queryList(wbBcBVo);
	}
	
	/** 명함 목록 조회 */
	public Integer getWbBcListCnt(WbBcBVo wbBcBVo ) throws SQLException {
		return commonDao.count(wbBcBVo);
	}
	
	/** 명함 목록설정 임의 정의 */
	public List<WbBcLstSetupBVo> setWbBcLstSetupInit(List<WbBcLstSetupBVo> wbBcLstSetupBVoList , String dftSetupYn) {
		String[][] dftSetupList = new String[][]{
				{"bcNm","cols.nm","Y","Y","120"},
				{"compNm","cols.compNm","Y","Y","120"},
				{"compPhon","cols.compPhon","Y","Y","120"},
				{"homePhon","cols.homePhon","Y","Y","120"},
				{"mbno","cols.mbno","Y","Y","120"},
				{"email","cols.email","Y","Y","120"},
				{"birth","cols.birth","N","N","10"},
				{"clnsNm","cols.clns","N","N","120"},
				{"deptNm","cols.dept","N","N","120"},
				{"fno","cols.fno","N","N","20"},
				{"gradeNm","cols.grade","N","N","120"},
				{"weddAnnv","cols.weddAnnv","N","N","10"}};
		wbBcLstSetupBVoList = new ArrayList<WbBcLstSetupBVo>();
		WbBcLstSetupBVo storedWbBcLstSetupBVo = null;
		for(String[] dftSetupString : dftSetupList){
			if(dftSetupYn != null && "Y".equals(dftSetupYn) && !dftSetupYn.equals(dftSetupString[2])  ){
				continue;
			}
			storedWbBcLstSetupBVo = new WbBcLstSetupBVo();
			storedWbBcLstSetupBVo.setAtrbId(dftSetupString[0]);
			storedWbBcLstSetupBVo.setMsgId(dftSetupString[1]);
			storedWbBcLstSetupBVo.setDftSetupYn(dftSetupString[2]);
			storedWbBcLstSetupBVo.setDispYn(dftSetupString[3]);
			storedWbBcLstSetupBVo.setAlnVa("center");
			storedWbBcLstSetupBVo.setVaLen(Integer.valueOf(dftSetupString[4]));
			wbBcLstSetupBVoList.add(storedWbBcLstSetupBVo);
		}
		return wbBcLstSetupBVoList;
	}
	
	/** 명함 목록설정 조회 및 세팅 */
	public void setWbBcSetupInit(HttpServletRequest request , UserVo userVo , ModelMap model) throws SQLException {
		//화면설정 조회[입력창 위치 , 목록구분코드]
		WbBcUserScrnSetupRVo wbBcUserScrnSetupRVo = new WbBcUserScrnSetupRVo();
		wbBcUserScrnSetupRVo.setCompId(userVo.getCompId());//회사ID
		wbBcUserScrnSetupRVo.setRegrUid(userVo.getUserUid());
		wbBcUserScrnSetupRVo = (WbBcUserScrnSetupRVo)commonDao.queryVo(wbBcUserScrnSetupRVo);
		model.put("wbBcUserScrnSetupRVo", wbBcUserScrnSetupRVo);
		
		//사용자 목록설정항목 조회
		WbBcUserLstSetupRVo wbBcUserLstSetupRVo = new WbBcUserLstSetupRVo();
		wbBcUserLstSetupRVo.setCompId(userVo.getCompId());//회사ID
		wbBcUserLstSetupRVo.setRegrUid(userVo.getUserUid());
		@SuppressWarnings("unchecked")
		List<WbBcUserLstSetupRVo> wbBcUserLstSetupRVoList = (List<WbBcUserLstSetupRVo>)commonDao.queryList(wbBcUserLstSetupRVo);
		if(wbBcUserLstSetupRVoList.size() > 0 ){// 사용자목록설정이 있을경우
			model.put("wbBcUserLstSetupRVoList", wbBcUserLstSetupRVoList);
		}else{// 사용자목록설정이 없을경우 관리자 기본설정을 조회한다.
			WbBcLstSetupBVo wbBcLstSetupBVo = new WbBcLstSetupBVo();
			wbBcLstSetupBVo.setCompId(userVo.getCompId());//회사ID
			//기본 목록설정항목 조회
			@SuppressWarnings("unchecked")
			List<WbBcLstSetupBVo> wbBcLstSetupBVoList = (List<WbBcLstSetupBVo>)commonDao.queryList(wbBcLstSetupBVo);
			//관리자 목록설정이 없을경우
			if(wbBcLstSetupBVoList.size() == 0 ){
				wbBcLstSetupBVoList = this.setWbBcLstSetupInit(wbBcLstSetupBVoList, "Y");
			}
			model.put("wbBcUserLstSetupRVoList", wbBcLstSetupBVoList);
		}
	}
	
	/** 명함 목록 조회 */
	public Map<String,Object> getWbBcMapList(HttpServletRequest request , WbBcBVo wbBcBVo ) throws SQLException {
		Map<String,Object> rsltMap = new HashMap<String,Object>();
		if(wbBcBVo.getSchFldId() != null && !"".equals(wbBcBVo.getSchFldId())){//폴더 조회
			if("A".equals(wbBcBVo.getSchFldTypYn())){
				List<WbBcFldBVo> wbBcFldBList = wbBcFldSvc.getDownTreeList(wbBcBVo.getCompId(), wbBcBVo.getSchFldId(), "ko", true, wbBcBVo.isPub(), "Y");
				
				if(wbBcFldBList.size() > 0 ){
					wbBcBVo.setWbBcFldBVo(wbBcFldBList);
				}
			}else{
				wbBcBVo.setSchFldTypYn("F");
				wbBcBVo.setSchFldId(wbBcBVo.getSchFldId());
			}
		}
		String schWord = wbBcBVo.getSchWord();//검색어
		if(schWord!=null && !schWord.isEmpty()){
			wbBcBVo.setSchCat(wbBcBVo.getSchCat());
			wbBcBVo.setSchWord(schWord);
		}
		
		Integer recodeCount = this.getWbBcListCnt(wbBcBVo);
		PersonalUtil.setPaging(request, wbBcBVo, recodeCount);
		
		//목록 조회
		List<WbBcBVo> wbBcBVoList = this.getWbBcList(wbBcBVo);
		
		Map<String, Object> wbBcBInfoMap;
		List<Map<String, Object>> wbBcBMapList = new ArrayList<Map<String, Object>>();
		for(WbBcBVo storedWbBcBVo : wbBcBVoList){
			wbBcBInfoMap = VoUtil.toMap(storedWbBcBVo, null);
			wbBcBMapList.add(wbBcBInfoMap);
		}
		rsltMap.put("wbBcBMapList", wbBcBMapList);
		//rsltMap.put("wbBcBVoList", wbBcBVoList);
		rsltMap.put("recodeCount", recodeCount);
		/*if(wbBcBVoList.size() > 0 ){//목록이 1건 이상일경우 이미지 정보를 로드한다.
			WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
			for(WbBcBVo wbBcBVoImg : wbBcBVoList){
				wbBcImgDVo = new WbBcImgDVo();
				wbBcImgDVo.setBcId(wbBcBVoImg.getBcId());
				wbBcImgDVo = (WbBcImgDVo)commonDao.queryVo(wbBcImgDVo);
				if(wbBcImgDVo != null){
					String fileDir = distManager.getContextProperty("distribute.web.local.root")+wbBcImgDVo.getImgPath();
					wbBcImgDVo.setImgPath(fileDir);
					wbBcBVoImg.setWbBcImgDVo(wbBcImgDVo);
				}
			}
		}*/
		//model.put("recodeCount", recodeCount);
		//model.put("wbBcBVoList", wbBcBVoList);
		
		return rsltMap;
	}
	
	/** 명함 상세보기 */
	public WbBcBVo getWbBcInfo(HttpServletRequest request, WbBcBVo wbBcBVo ) throws SQLException, CmException {
		wbBcBVo = (WbBcBVo)commonDao.queryVo(wbBcBVo);
		if(wbBcBVo==null){
			//cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		// 연락처 목록 조회
		WbBcCntcDVo wbBcCntcDVo = new WbBcCntcDVo();
		wbBcCntcDVo.setBcId(wbBcBVo.getBcId());
		wbBcCntcDVo.setCntcClsCd("CNTC");//연락처분류코드
		@SuppressWarnings("unchecked")
		List<WbBcCntcDVo> wbBcCntcDVoList = (List<WbBcCntcDVo>)commonDao.queryList(wbBcCntcDVo);
		if(wbBcCntcDVoList.size() > 0 ) wbBcBVo.setWbBcCntcDVo(this.checkCntcList(wbBcCntcDVoList)); else wbBcBVo.setWbBcCntcDVo(this.setCntcList(0 , "CNTC"));	// 연락처목록 초기화
		
		wbBcCntcDVo.setCntcClsCd("EMAIL");//연락처분류코드
		@SuppressWarnings("unchecked")
		List<WbBcCntcDVo> wbBcEmailDVoList = (List<WbBcCntcDVo>)commonDao.queryList(wbBcCntcDVo);
		if(wbBcEmailDVoList.size() > 0 ) wbBcBVo.setWbBcEmailDVo(wbBcEmailDVoList); else wbBcBVo.setWbBcEmailDVo(this.setCntcList(1 , "EMAIL"));	// 이메일목록 초기화
		
		/** 지정인 목록 조회 */
		WbBcApntrRVo wbBcApntrRVo = new WbBcApntrRVo();
		wbBcApntrRVo.setBcId(wbBcBVo.getBcId());
		//wbBcApntrRVo.setBcApntrUid(userVo.getUserUid());
		@SuppressWarnings("unchecked")
		List<WbBcApntrRVo> wbBcApntrRVoList = (List<WbBcApntrRVo>)commonDao.queryList(wbBcApntrRVo);
		wbBcBVo.setWbBcApntrRVoList(wbBcApntrRVoList);
		//model.put("wbBcApntrRVoList", wbBcApntrRVoList);
		
		/** 명함이미지상세 */
		WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
		wbBcImgDVo.setBcId(wbBcBVo.getBcId());
		wbBcImgDVo = (WbBcImgDVo)commonDao.queryVo(wbBcImgDVo);
		if(wbBcImgDVo != null){
			//String fileDir = distManager.getContextProperty("distribute.web.local.root")+wbBcImgDVo.getImgPath();
			//wbBcImgDVo.setImgPath(fileDir);
			wbBcBVo.setWbBcImgDVo(wbBcImgDVo);
		}
		//model.put("wbBcImgDVo", wbBcImgDVo);
		
		return wbBcBVo;
	}
	
	// 관련미팅 조회
	public List<WbBcMetngDVo> getWbBcMetngList(WbBcMetngDVo wbBcMetngDVo ) throws SQLException{
		@SuppressWarnings("unchecked")
		List<WbBcMetngDVo> wbBcMetngDVoList = (List<WbBcMetngDVo>)commonDao.queryList(wbBcMetngDVo);
		return wbBcMetngDVoList;
	}
	
	/** 연락처 목록 초기화 */
	public List<WbBcCntcDVo> setCntcList(int idx , String cntcClsCd ) {
		String[][] cntcTypCds = new String[][]{{"compPhon","homePhon","mbno"},{"email"}};
		String[] tempCds = cntcTypCds[idx];
		List<WbBcCntcDVo> wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
		WbBcCntcDVo wbBcCntcDVo = null;
		for(int i=0;i<tempCds.length;i++){
			wbBcCntcDVo = new WbBcCntcDVo();
			wbBcCntcDVo.setCntcTypCd(tempCds[i]);
			wbBcCntcDVo.setCntcTypNo("1");
			wbBcCntcDVo.setCntcClsCd(cntcClsCd);
			wbBcCntcDVoList.add(wbBcCntcDVo);
		}
		return wbBcCntcDVoList;
	}
	
	/** 비어있는 연락처 목록 초기화 */
	public List<WbBcCntcDVo> checkCntcList(List<WbBcCntcDVo> wbBcCntcDVoList ) {
		String[] cntcTypCds = new String[]{"compPhon","homePhon","mbno"};
		for(int i=0;i<cntcTypCds.length;i++){
			for(WbBcCntcDVo wbBcCntcDVo : wbBcCntcDVoList){
				if(wbBcCntcDVo.getCntcTypCd().equals(cntcTypCds[i])){
					cntcTypCds[i] = null;
					break;
				}
			}
		}
		
		WbBcCntcDVo wbBcCntcDVo = null;
		for(int i=0;i<cntcTypCds.length;i++){
			if(cntcTypCds[i] == null ){
				continue;
			}
			wbBcCntcDVo = new WbBcCntcDVo();
			wbBcCntcDVo.setCntcTypCd(cntcTypCds[i]);
			wbBcCntcDVo.setCntcTypNo("1");
			wbBcCntcDVo.setCntcClsCd("CNTC");
			wbBcCntcDVoList.add(wbBcCntcDVo);
		}
		return wbBcCntcDVoList;
	}
	
	public String getFileName(char prefix , String orginalDir){
		String fileName;
		int p = orginalDir.replace('\\', '/').lastIndexOf('/');
		if (p >= 0) fileName = orginalDir.substring(p + 1);
		else fileName = orginalDir;
		p = fileName.lastIndexOf('.');
		String ext = p <= 0 ? "" : fileName.substring(p);
		String newfileName = prefix + StringUtil.getNextHexa()+ext;
		
		return newfileName;
	}
	
	/** 명함 저장 [부가 정보 포함] */
	public void saveBc(HttpServletRequest request, QueryQueue queryQueue , WbBcBVo wbBcBVo , UserVo userVo) throws SQLException, IOException, CmException{
		if(wbBcBVo.getPublTypCd() == null){/** 빠른추가일 경우 공개여부를 비공개로 설정한다. */
			wbBcBVo.setPublTypCd("priv");
		}
		
		// 전체공개면 메인설정을 'O'[신규]로 세팅한다.
		if("allPubl".equals(wbBcBVo.getPublTypCd())){
			wbBcBVo.setMainSetupYn("O");
		}
		
		// 명함 제목의 초성을 추출하여 해당 컬럼에 저장
		if(wbBcBVo.getBcNm() != null && !wbBcBVo.getBcNm().isEmpty()){
			wbBcBVo.setInitialNm(direct(wbBcBVo.getBcNm().charAt(0)));
		}
		
		String bcId = wbBcBVo.getBcId();
		String regrUid = wbBcBVo.getCopyRegrUid() !=null && !"".equals(wbBcBVo.getCopyRegrUid()) ? wbBcBVo.getCopyRegrUid() : userVo.getUserUid();
		if (bcId == null || bcId.isEmpty()) {
			
			// ID 생성
			bcId = wbCmSvc.createId(wbBcBVo.isPub() ? "WB_PUB_BC_B" : "WB_BC_B");
			wbBcBVo.setBcId(bcId);
			// 등록자, 등록일시
			wbBcBVo.setRegrUid(regrUid);
			wbBcBVo.setRegDt("sysdate");
			// 등록구분 신규 처리
			wbBcBVo.setBcRegTypCd("O");
			wbBcBVo.setMainSetupYn("O");
			wbBcBVo.setBumkYn("N");//즐겨찾기여부
			if(wbBcBVo.getFldId() == null || wbBcBVo.getFldId().isEmpty() || (wbBcBVo.getFldId()!=null && !wbBcBVo.getFldId().isEmpty() && "ROOT".equals(wbBcBVo.getFldId()))){
				wbBcBVo.setFldId("NONE");
			}
			// 빠른추가일 경우 연락처 유형에 따라 우선연락처를 선택한다.
			if(wbBcBVo.getDftCntcTypCd() == null || "".equals(wbBcBVo.getDftCntcTypCd())){
				wbBcBVo.setDftCntcTypCd("compPhon");
			}
			// 명함기본(WB_BC_B) 테이블 - INSERT
			queryQueue.insert(wbBcBVo);
			
		} else {
			// 수정자, 수정일시
			wbBcBVo.setModrUid(regrUid);
			wbBcBVo.setModDt("sysdate");
			// 명함관리(WB_BC_B) 테이블 - UPDATE
			queryQueue.update(wbBcBVo);
			
			// 연락처 삭제 목록(연락처 id가 sequence로 변경됨에 따라 수정시에 연락처 목록을 전체삭제한다.)
			WbBcCntcDVo delWbBcCntcDVo = new WbBcCntcDVo();
			delWbBcCntcDVo.setBcId(bcId);
			queryQueue.delete(delWbBcCntcDVo);
			
		}
		//commonDao.execute(queryQueue);
		// 연락처 목록
		@SuppressWarnings("unchecked")
		List<WbBcCntcDVo> wbBcCntcDVoList = (List<WbBcCntcDVo>)VoUtil.bindList(request, WbBcCntcDVo.class, new String[]{"cntcCont"});
		
		if(wbBcCntcDVoList==null || wbBcCntcDVoList.size()==0) wbBcCntcDVoList = wbBcBVo.getWbBcCntcDVo();
		if(wbBcCntcDVoList!=null && wbBcCntcDVoList.size()>0 ){
			Map<String,Integer> seqMap = new HashMap<String,Integer>();
			for(WbBcCntcDVo wbBcCntcDVo : wbBcCntcDVoList){
				if(wbBcCntcDVo.getCntcCont() != null && !wbBcCntcDVo.getCntcCont().isEmpty()){
					//연락처 유형별로 시퀀스를 정의한다.
					if(!seqMap.containsKey(wbBcCntcDVo.getCntcTypCd())) seqMap.put(wbBcCntcDVo.getCntcTypCd(), 1); 
					else seqMap.put(wbBcCntcDVo.getCntcTypCd(), seqMap.get(wbBcCntcDVo.getCntcTypCd()).intValue()+1);
					wbBcCntcDVo.setBcId(bcId);
					wbBcCntcDVo.setBcCntcSeq(seqMap.get(wbBcCntcDVo.getCntcTypCd()));
					queryQueue.insert(wbBcCntcDVo);
				}
			}
		}
		
		/*if(wbBcCntcDVoList.size() > 0 ){
			List<WbBcCntcDVo> cntcInsertList = new ArrayList<WbBcCntcDVo>();//신규항목
			List<WbBcCntcDVo> cntcUpdateList = new ArrayList<WbBcCntcDVo>();//수정항목
			for(WbBcCntcDVo wbBcCntcDVo : wbBcCntcDVoList){
				wbBcCntcDVo.setBcId(bcId);
				if(wbBcCntcDVo.getBcCntcSeq() == null ){
					cntcInsertList.add(wbBcCntcDVo);
					//wbBcCntcDVo.setBcCntcSeq(wbCmSvc.createId("WB_BC_CNTC_D"));
					//queryQueue.insert(wbBcCntcDVo);
				}else{
					cntcUpdateList.add(wbBcCntcDVo);
					//queryQueue.update(wbBcCntcDVo);
				}
			}
			
			// 수정할 목록이 있을경우 수정처리한다.
			if(cntcUpdateList.size() > 0 ){
				for(WbBcCntcDVo updateCntcVo : cntcUpdateList){
					queryQueue.update(updateCntcVo);
				}
			}
			
			// 등록할 목록이 있을경우 등록처리한다.
			if(cntcInsertList.size() > 0 ){
				for(WbBcCntcDVo insertCntcVo : cntcInsertList){
					insertCntcVo.setBcCntcSeq(wbCmSvc.createId("WB_BC_CNTC_D"));
					queryQueue.insert(insertCntcVo);
				}
			}
		}*/
		
		// 지정인 목록
		@SuppressWarnings("unchecked")
		List<WbBcApntrRVo> wbBcApntrRVoList = (List<WbBcApntrRVo>)VoUtil.bindList(request, WbBcApntrRVo.class, new String[]{"bcApntrUid"});
		
		if(wbBcApntrRVoList.size() > 0 ){
			List<WbBcApntrRVo> apntrInsertList = new ArrayList<WbBcApntrRVo>();//신규항목
			List<WbBcApntrRVo> apntrUpdateList = new ArrayList<WbBcApntrRVo>();//수정항목(delete하기 위해서)
			for(WbBcApntrRVo wbBcApntrRVo : wbBcApntrRVoList){
				wbBcApntrRVo.setBcId(bcId);
				
				if("N".equals(wbBcApntrRVo.getUpdateYn()) && (wbBcApntrRVo.getBcApntrUid() != null && !wbBcApntrRVo.getBcApntrUid().isEmpty())){
					apntrInsertList.add(wbBcApntrRVo);
					//queryQueue.insert(wbBcApntrRVo);
				}else{
					apntrUpdateList.add(wbBcApntrRVo);
				}
			}
			
			
			//수정할 목록이 있을경우 부분삭제 또는 전체삭제 한다.
			if(apntrUpdateList.size() > 0 || apntrUpdateList.size() == 0){
				WbBcApntrRVo deleteApntrVo = new WbBcApntrRVo();
				deleteApntrVo.setBcId(bcId);
				String[] deleteUids = new String[apntrUpdateList.size()];
				if(apntrUpdateList.size() > 0){//수정항목이 있으면 해당 UID를 배열에 담아 삭제 처리한다.
					for(int i=0;i<apntrUpdateList.size();i++){
						deleteUids[i] = apntrUpdateList.get(i).getBcApntrUid();
					}
					deleteApntrVo.setDeleteUids(deleteUids);
				}
				queryQueue.delete(deleteApntrVo);
			}
			
			
			// 등록할 목록이 있을경우 등록처리한다.
			if(apntrInsertList.size() > 0 ){
				for(WbBcApntrRVo insertApntrVo : apntrInsertList){
					queryQueue.insert(insertApntrVo);
				}
			}
		}else{
			WbBcApntrRVo deleteApntrVo = new WbBcApntrRVo();
			deleteApntrVo.setBcId(bcId);
			queryQueue.delete(deleteApntrVo);
		}
		
		// 이미지 처리
		// 업로드 경로
		if(wbBcBVo.getToBcId() != null && !"".equals(wbBcBVo.getToBcId())){
			wbBcFileSvc.copyBcImgFile(request, wbBcBVo.getToBcId(), bcId, regrUid, queryQueue);
		}else{
			wbBcFileSvc.copyBcImgTempFile(request, bcId, regrUid, queryQueue, wbBcBVo.getAddUserUid());
		}
	}
	
	/** 명함 전체 삭제 */
	public void deleteBc(QueryQueue queryQueue , WbBcBVo searchVO) throws SQLException{
		// 명함 기본
		WbBcBVo wbBcBVo = null;
		//명함 연락처
		WbBcCntcDVo wbBcCntcDVo = null;
		//명함 지정인
		WbBcApntrRVo wbBcApntrRVo = null;
		
		String[] delIds = searchVO.getDelList();
		// 첨부파일
		List<CommonFileVo> deletedFileList = new ArrayList<CommonFileVo>();
		for(int i=0;i<delIds.length;i++){
			if(delIds[i] != null){
				wbBcCntcDVo = new WbBcCntcDVo();
				wbBcCntcDVo.setBcId(delIds[i]);
				// 명함 연락처 데이터 삭제
				queryQueue.delete(wbBcCntcDVo);
				
				//명함 이미지 삭제
				wbBcFileSvc.deletePhoto(delIds[i], deletedFileList, queryQueue);
				
				//첨부파일 삭제
				deletedFileList.addAll(wbBcFileSvc.deleteBcFile(delIds[i], queryQueue));
				
				// 명함
				wbBcBVo = new WbBcBVo();
				if(searchVO.getCompId() != null && !searchVO.getCompId().isEmpty()){
					wbBcBVo.setCompId(searchVO.getCompId());
				}
				if(searchVO.isPub()) wbBcBVo.setPub(searchVO.isPub()); // 공유명함여부
				else{
					wbBcApntrRVo = new WbBcApntrRVo();
					wbBcApntrRVo.setBcId(delIds[i]);
					// 명함 지정인 데이터 삭제
					queryQueue.delete(wbBcApntrRVo);
				}
				
				// 명함ID
				wbBcBVo.setBcId(delIds[i]);
				// 명함 데이터 삭제
				queryQueue.delete(wbBcBVo);
			}
		}
		
		if(deletedFileList.size() > 0 ){
			// 파일 삭제
			wbBcFileSvc.deleteDiskFiles(deletedFileList);
		}
	}
	
	/** 명함 Csv 저장 [부가 정보 포함]*/
	public void saveCsvBc(QueryQueue queryQueue , WbBcBVo wbBcBVo , UserVo userVo) throws SQLException{
		if(wbBcBVo.getPublTypCd() == null){/** 빠른추가일 경우 공개여부를 비공개로 설정한다. */
			wbBcBVo.setPublTypCd("priv");
		}
		
		// 전체공개면 메인설정을 'O'[신규]로 세팅한다.
		if("allPubl".equals(wbBcBVo.getPublTypCd())){
			wbBcBVo.setMainSetupYn("O");
		}
		
		// 명함 제목의 초성을 추출하여 해당 컬럼에 저장
		if(wbBcBVo.getBcNm() != null && !wbBcBVo.getBcNm().isEmpty()){
			wbBcBVo.setInitialNm(direct(wbBcBVo.getBcNm().charAt(0)));
		}
				
		String bcId = wbBcBVo.getBcId();
		String regrUid = wbBcBVo.getCopyRegrUid() !=null && !"".equals(wbBcBVo.getCopyRegrUid()) ? wbBcBVo.getCopyRegrUid() : userVo.getUserUid();
		if (bcId == null || bcId.isEmpty()) {
			
			// ID 생성
			bcId = wbCmSvc.createId(wbBcBVo.isPub() ? "WB_PUB_BC_B" : "WB_BC_B");
			wbBcBVo.setBcId(bcId);
			// 등록자, 등록일시
			wbBcBVo.setRegrUid(regrUid);
			wbBcBVo.setRegDt("sysdate");
			// 등록구분 신규 처리
			wbBcBVo.setBcRegTypCd("O");
			wbBcBVo.setMainSetupYn("O");
			wbBcBVo.setBumkYn("N");//즐겨찾기여부
			if(wbBcBVo.getFldId() == null || wbBcBVo.getFldId().isEmpty()){
				wbBcBVo.setFldId("NONE");
			}
			// 빠른추가일 경우 연락처 유형에 따라 우선연락처를 선택한다.
			if(wbBcBVo.getDftCntcTypCd() == null || "".equals(wbBcBVo.getDftCntcTypCd())){
				wbBcBVo.setDftCntcTypCd("compPhon");
			}
			// 명함기본(WB_BC_B) 테이블 - INSERT
			queryQueue.insert(wbBcBVo);
			
		} 
		// 연락처 목록
		List<WbBcCntcDVo> wbBcCntcDVoList = wbBcBVo.getWbBcCntcDVo();
		
		if(wbBcCntcDVoList.size() > 0 ){
			WbBcCntcDVo maxWbBcCntcDVo = new WbBcCntcDVo();
			maxWbBcCntcDVo.setBcId(bcId);
			// 기존 연락처의 최대 시퀀스를 구한다.
			maxWbBcCntcDVo.setInstanceQueryId("com.innobiz.orange.web.wb.dao.WbBcCntcDDao.selectMaxCntcSeq");
			Integer maxBcCntcSeq = null;
			Map<String,Integer> seqMap = new HashMap<String,Integer>();
			for(WbBcCntcDVo wbBcCntcDVo : wbBcCntcDVoList){
				//연락처 유형별로 시퀀스를 정의한다.
				if(!seqMap.containsKey(wbBcCntcDVo.getCntcTypCd())) {
					maxWbBcCntcDVo.setCntcTypCd(wbBcCntcDVo.getCntcTypCd());
					maxBcCntcSeq = (Integer)commonDao.queryInt(maxWbBcCntcDVo);
					if(maxBcCntcSeq == null) maxBcCntcSeq = 0;
					seqMap.put(wbBcCntcDVo.getCntcTypCd(), maxBcCntcSeq+1); 
				}else {
					seqMap.put(wbBcCntcDVo.getCntcTypCd(), seqMap.get(wbBcCntcDVo.getCntcTypCd()).intValue()+1);
				}
				wbBcCntcDVo.setBcId(bcId);
				wbBcCntcDVo.setBcCntcSeq(seqMap.get(wbBcCntcDVo.getCntcTypCd()));
				queryQueue.insert(wbBcCntcDVo);
			}
		}
		
		// 이메일 목록
		/*List<WbBcCntcDVo> wbBcEmailDVoList = wbBcBVo.getWbBcEmailDVo();
		
		if(wbBcEmailDVoList.size() > 0 ){
			for(WbBcCntcDVo wbBcCntcDVo : wbBcEmailDVoList){
				wbBcCntcDVo.setBcId(bcId);
				wbBcCntcDVo.setBcCntcSeq(wbCmSvc.createId("WB_BC_CNTC_D"));
				queryQueue.insert(wbBcCntcDVo);
			}
		}*/
	}
	
	
	/** 명함 복사,이동 */
	public void copyBc(HttpServletRequest request , QueryQueue queryQueue , WbBcBVo searchVO , String mode , String bcFldId) throws SQLException, IOException, CmException{
		 
		if(bcFldId==null || "ROOT".equals(bcFldId)) bcFldId = "NONE";
		
		if("copyBc".equals(mode)){// 복사 [명함 아이디를 생성하고 폴더아이디를 저장] 
			WbBcBVo wbBcBVo = (WbBcBVo) this.getWbBcInfo(request, searchVO);
			//복사하는 대상 명함이 전체공개 이면서 원본 이면 신규등록구분을 'C'(복사명함 원본)상태로 변경한다.
			if("allPubl".equals(wbBcBVo.getPublTypCd()) && "O".equals(wbBcBVo.getBcRegTypCd())){
				WbBcBVo updateWbBcBVo = new WbBcBVo();
				updateWbBcBVo.setBcRegTypCd("C");
				updateWbBcBVo.setMainSetupYn("C");
				updateWbBcBVo.setBcId(wbBcBVo.getBcId());
				queryQueue.update(updateWbBcBVo);
				wbBcBVo.setOriginalBcId(wbBcBVo.getBcId());//원본명함ID를 세팅한다. 
			}
			boolean isPub = searchVO.isPub();
			wbBcBVo.setPub(isPub); // 공유명함여부
			// ID 생성
			String bcId = wbCmSvc.createId(wbBcBVo.isPub() ? "WB_PUB_BC_B" : "WB_BC_B");
			
			// 지정인도 복사 대상에 포함되는지 여부 확인후 처리
			
			Map<String,Integer> seqMap = new HashMap<String,Integer>();
			//연락처 복사[전화번호]
			List<WbBcCntcDVo> wbBcCntcDVoList = wbBcBVo.getWbBcCntcDVo();
			if(wbBcCntcDVoList.size() > 0 ){
				for(WbBcCntcDVo wbBcCntcDVo : wbBcCntcDVoList){
					if(wbBcCntcDVo.getCntcCont() != null){
						//연락처 유형별로 시퀀스를 정의한다.
						if(!seqMap.containsKey(wbBcCntcDVo.getCntcTypCd())) seqMap.put(wbBcCntcDVo.getCntcTypCd(), 1); 
						else seqMap.put(wbBcCntcDVo.getCntcTypCd(), seqMap.get(wbBcCntcDVo.getCntcTypCd()).intValue()+1);
						wbBcCntcDVo.setBcId(bcId);
						wbBcCntcDVo.setBcCntcSeq(seqMap.get(wbBcCntcDVo.getCntcTypCd()));
						queryQueue.insert(wbBcCntcDVo);
					}
				}
			}
			
			//연락처 복사[이메일]
			List<WbBcCntcDVo> wbBcEmailDVoList = wbBcBVo.getWbBcEmailDVo();
			if(wbBcEmailDVoList.size() > 0 ){
				for(WbBcCntcDVo wbBcCntcDVo : wbBcEmailDVoList){
					if(wbBcCntcDVo.getCntcCont() != null){
						//연락처 유형별로 시퀀스를 정의한다.
						if(!seqMap.containsKey(wbBcCntcDVo.getCntcTypCd())) seqMap.put(wbBcCntcDVo.getCntcTypCd(), 1); 
						else seqMap.put(wbBcCntcDVo.getCntcTypCd(), seqMap.get(wbBcCntcDVo.getCntcTypCd()).intValue()+1);
						wbBcCntcDVo.setBcId(bcId);
						wbBcCntcDVo.setBcCntcSeq(seqMap.get(wbBcCntcDVo.getCntcTypCd()));
						queryQueue.insert(wbBcCntcDVo);
					}
				}
			}
			
			wbBcBVo.setBcId(bcId); // 명함아이디 새로 부여
			wbBcBVo.setFldId(bcFldId);// 폴더아이디 새로 부여
			// 등록구분 신규 처리
			wbBcBVo.setBcRegTypCd("D");
			wbBcBVo.setMainSetupYn("D");
			wbBcBVo.setRegDt("sysdate");// 등록일 저장
			
			//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
			if(searchVO.getCopyRegrUid() != null && !searchVO.getCopyRegrUid().isEmpty()){
				// 사용자기본(OR_USER_B) 테이블
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setUserUid(searchVO.getCopyRegrUid());
				orUserBVo = (OrUserBVo)commonDao.queryVo(orUserBVo);
				wbBcBVo.setCompId(orUserBVo.getCompId());
				wbBcBVo.setRegrUid(searchVO.getCopyRegrUid());
			}else{
				wbBcBVo.setCompId(searchVO.getCompId());
			}
			
			//공개여부를 비공개로 설정[원본의 공개여부와 상관없이]
			wbBcBVo.setPublTypCd("priv");
			
			//명함 복사시 즐겨찾기 정보 초기화
			wbBcBVo.setBumkYn("N");
			
			// 명함 기본 테이블 저장
			queryQueue.insert(wbBcBVo);
			
			// 이미지 복사
			wbBcFileSvc.copyBcImgFile(request, searchVO.getBcId(), bcId, wbBcBVo.getRegrUid() , queryQueue);
			/*WbBcImgDVo wbBcImgDVo = wbBcBVo.getWbBcImgDVo();
			if(wbBcImgDVo != null){
				wbBcImgDVo.setBcId(bcId);
				queryQueue.insert(wbBcImgDVo);
			}*/
			
			// 파일 복사
			wbBcFileSvc.copyBcFile(request, searchVO.getBcId(), bcId, queryQueue);
			
			
		}else if("moveBc".equals(mode)){// 이동 [명함 폴더 아이디를 변경]
			searchVO.setFldId(bcFldId);// 폴더아이디 새로 부여
			searchVO.setModDt("sysdate");// 등록일 저장
			queryQueue.update(searchVO);
		}
	}
	
	/** 명함 CSV 연락처 형식 (google,outlook,vcard) */
	public List<WbBcLstSetupBVo> getWbBcCntcInit(List<WbBcBVo> wbBcBVo , String csvTypCd) {
		List<WbBcLstSetupBVo> wbBcLstSetupBVoList = new ArrayList<WbBcLstSetupBVo>();
		
		String[][] dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
				{"email","wb.cols.email","Y","Y","120"},{"birth","wb.cols.birth","N","N","10"},{"deptNm","wb.cols.dept","N","N","120"},{"fno","wb.cols.fno","N","N","20"},{"gradeNm","wb.cols.grade","N","N","120"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"}};
		wbBcLstSetupBVoList = new ArrayList<WbBcLstSetupBVo>();
		WbBcLstSetupBVo storedWbBcLstSetupBVo = null;
		for(String[] dftSetupString : dftSetupList){
			storedWbBcLstSetupBVo = new WbBcLstSetupBVo();
			storedWbBcLstSetupBVo.setAtrbId(dftSetupString[0]);
			storedWbBcLstSetupBVo.setMsgId(dftSetupString[1]+("".equals(csvTypCd) ? "" : "."+csvTypCd));
			storedWbBcLstSetupBVo.setDftSetupYn(dftSetupString[2]);
			storedWbBcLstSetupBVo.setDispYn(dftSetupString[3]);
			storedWbBcLstSetupBVo.setAlnVa("center");
			storedWbBcLstSetupBVo.setVaLen(Integer.valueOf(dftSetupString[4]));
			wbBcLstSetupBVoList.add(storedWbBcLstSetupBVo);
		}
		return wbBcLstSetupBVoList;
	}
	
	/** 명함 CSV 형식 가져오기 (google,outlook,express,default) */
	public String[][] getWbBcCsvHeader(String csvTypCd) {
		String[][] dftSetupList = null;
		
		if("google".equals(csvTypCd)){
			dftSetupList = new String[][]{{"bcNm","wb.cols.inOut.bcNm","Y","Y","120"},{"mbno","wb.cols.inOut.mbno","Y","Y","120"},{"compPhon","wb.cols.inOut.compPhon","Y","Y","120"},{"homePhon","wb.cols.inOut.homePhon","Y","Y","120"},
					{"email","wb.cols.inOut.email","Y","Y","120"},{"gradeNm","wb.cols.inOut.grade","N","N","120"},{"compNm","wb.cols.inOut.compNm","Y","Y","120"},{"deptNm","wb.cols.inOut.dept","N","N","120"},
					{"fno","wb.cols.inOut.fno","N","N","20"},{"noteCont","wb.cols.inOut.noteCont","Y","Y","120"},{"compHpageUrl","wb.cols.inOut.compHpageUrl","Y","Y","120"},
					{"birth","wb.cols.inOut.birth","N","N","10"},{"weddAnnv","wb.cols.inOut.weddAnnv","N","N","10"}};
		}else if("outlook".equals(csvTypCd)){
			dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},{"deptNm","wb.cols.dept","N","N","120"},{"gradeNm","wb.cols.grade","N","N","120"},
					{"fno","wb.cols.fno","N","N","20"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
					{"noteCont","wb.cols.noteCont","Y","Y","120"},{"compHpageUrl","wb.cols.compHpageUrl","Y","Y","120"},{"email","wb.cols.email","Y","Y","120"},
					{"birth","wb.cols.birth","N","N","10"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"},
					{"bcNm","wb.cols.inOut.bcNm","Y","Y","120"},{"mbno","wb.cols.inOut.mbno","Y","Y","120"},{"compPhon","wb.cols.inOut.compPhon","Y","Y","120"},{"homePhon","wb.cols.inOut.homePhon","Y","Y","120"},
					{"email","wb.cols.inOut.email","Y","Y","120"},{"gradeNm","wb.cols.inOut.grade","N","N","120"},{"compNm","wb.cols.inOut.compNm","Y","Y","120"},{"deptNm","wb.cols.inOut.dept","N","N","120"},
					{"fno","wb.cols.inOut.fno","N","N","20"},{"noteCont","wb.cols.inOut.noteCont","Y","Y","120"},{"compHpageUrl","wb.cols.inOut.compHpageUrl","Y","Y","120"},
					{"birth","wb.cols.inOut.birth","N","N","10"},{"weddAnnv","wb.cols.inOut.weddAnnv","N","N","10"}
					};
		}else if("outlook2003".equals(csvTypCd)){
			dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},{"deptNm","wb.cols.dept","N","N","120"},{"gradeNm","wb.cols.grade","N","N","120"},
					{"fno","wb.cols.fno","N","N","20"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
					{"noteCont","wb.cols.noteCont","Y","Y","120"},{"compHpageUrl","wb.cols.compHpageUrl","Y","Y","120"},{"email","wb.cols.email","Y","Y","120"},
					{"birth","wb.cols.birth","N","N","10"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"}};
		}else if("outlook2007".equals(csvTypCd)){
			dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},{"deptNm","wb.cols.dept","N","N","120"},{"gradeNm","wb.cols.grade","N","N","120"},
					{"fno","wb.cols.fno","N","N","20"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
					{"noteCont","wb.cols.noteCont","Y","Y","120"},{"compHpageUrl","wb.cols.compHpageUrl","Y","Y","120"},{"email","wb.cols.email","Y","Y","120"},
					{"birth","wb.cols.birth","N","N","10"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"}};
		}else if("express".equals(csvTypCd)){
			dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"email","wb.cols.email","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
					{"psnHpageUrl","wb.cols.psnHpageUrl","Y","Y","120"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"fno","wb.cols.fno","N","N","20"},
					{"compNm","wb.cols.compNm","Y","Y","120"},{"gradeNm","wb.cols.grade","N","N","120"},{"deptNm","wb.cols.dept","N","N","120"},
					{"noteCont","wb.cols.noteCont","Y","Y","120"},	{"birth","wb.cols.birth","N","N","10"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"}};
		}else if("contact".equals(csvTypCd)){
			dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
					{"email","wb.cols.email","Y","Y","120"},{"birth","wb.cols.birth","N","N","10"},{"deptNm","wb.cols.dept","N","N","120"},{"fno","wb.cols.fno","N","N","20"},{"gradeNm","wb.cols.grade","N","N","120"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"}};
		}else{
			dftSetupList = new String[][]{{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},{"compPhon","wb.cols.compPhon","Y","Y","120"},{"homePhon","wb.cols.homePhon","Y","Y","120"},{"mbno","wb.cols.mbno","Y","Y","120"},
					{"email","wb.cols.email","Y","Y","120"},{"birth","wb.cols.birth","N","N","10"},{"deptNm","wb.cols.dept","N","N","120"},{"fno","wb.cols.fno","N","N","20"},{"gradeNm","wb.cols.grade","N","N","120"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"}};
		}
		return dftSetupList;
	}
	
	/** 명함 CSV 형식 (google,outlook,vcard) */
	public List<WbBcLstSetupBVo> getWbBcLstSetupInit(String csvTypCd) {
		List<WbBcLstSetupBVo> wbBcLstSetupBVoList = new ArrayList<WbBcLstSetupBVo>();
		
		String[][] dftSetupList = this.getWbBcCsvHeader(csvTypCd);
		
		WbBcLstSetupBVo storedWbBcLstSetupBVo = null;
		for(String[] dftSetupString : dftSetupList){
			storedWbBcLstSetupBVo = new WbBcLstSetupBVo();
			storedWbBcLstSetupBVo.setAtrbId(dftSetupString[0]);
			storedWbBcLstSetupBVo.setMsgId(dftSetupString[1]+("".equals(csvTypCd) ? "" : "."+csvTypCd));
			storedWbBcLstSetupBVo.setDftSetupYn(dftSetupString[2]);
			storedWbBcLstSetupBVo.setDispYn(dftSetupString[3]);
			storedWbBcLstSetupBVo.setAlnVa("center");
			storedWbBcLstSetupBVo.setVaLen(Integer.valueOf(dftSetupString[4]));
			wbBcLstSetupBVoList.add(storedWbBcLstSetupBVo);
		}
		return wbBcLstSetupBVoList;
	}
	
	/** 명함 CSV Google 형식 (google연락처 가져오기용) */
	public String[][]  getWbBcCsvHeaderGoogle() {
		String[][] dftSetupList = {{"bcNm","wb.cols.bcNm","Y","Y","120"},{"compNm","wb.cols.compNm","Y","Y","120"},
				{"cntcType","wb.cols.phon.type","Y","Y","10"},{"phonValue","wb.cols.phon.value","Y","Y","10"},
				{"cntcType","wb.cols.email.type","Y","Y","10"},{"email","wb.cols.email.value","Y","Y","120"},
				{"birth","wb.cols.birth","N","N","10"},{"deptNm","wb.cols.dept","N","N","120"},
				{"gradeNm","wb.cols.grade","N","N","120"},{"weddAnnv","wb.cols.weddAnnv","N","N","10"},
				{"bcNm","wb.cols.inOut.bcNm","Y","Y","120"},{"mbno","wb.cols.inOut.mbno","Y","Y","120"},{"compPhon","wb.cols.inOut.compPhon","Y","Y","120"},{"homePhon","wb.cols.inOut.homePhon","Y","Y","120"},
				{"email","wb.cols.inOut.email","Y","Y","120"},{"gradeNm","wb.cols.inOut.grade","N","N","120"},{"compNm","wb.cols.inOut.compNm","Y","Y","120"},{"deptNm","wb.cols.inOut.dept","N","N","120"},
				{"fno","wb.cols.inOut.fno","N","N","20"},{"noteCont","wb.cols.inOut.noteCont","Y","Y","120"},{"compHpageUrl","wb.cols.inOut.compHpageUrl","Y","Y","120"},
				{"birth","wb.cols.inOut.birth","N","N","10"},{"weddAnnv","wb.cols.inOut.weddAnnv","N","N","10"}
				};
		
		return dftSetupList;
	}
	
	
	/** 명함 CSV 헤더 체크*/
	public String[] getWbBcLstAtrdInfo(HttpServletRequest request, String columnNm , String[] atrbArrs , String csvTypCd ) {
		String[][] dftSetupList = "google".equals(csvTypCd) ? this.getWbBcCsvHeaderGoogle() : this.getWbBcCsvHeader(csvTypCd);
		
		String msgNm = "";
		//Pattern p = Pattern.compile("[0-9]");
		for(String[] dftSetupString : dftSetupList){
			try{
				msgNm = messageProperties.getMessage(dftSetupString[1]+("".equals(csvTypCd) ? "" : "."+csvTypCd),new String[]{columnNm.replaceAll("[^0-9]", "")} ,request);
			}catch(org.springframework.context.NoSuchMessageException me){
				return null;
			}
			
			if(columnNm.equals(msgNm)){
				if(atrbArrs == null ) atrbArrs = new String[3];
				atrbArrs[0] = dftSetupString[0];//vo와 맵핑 하기 위한 기본이름
				atrbArrs[1] = msgNm;//파일에서 get 하기 위한 실제 헤더명
				atrbArrs[2] = dftSetupString[4];//컬럼의 길이 제한값
				
				return atrbArrs;
			}
			
		}
		return null;
	}
	
	/** 명함 CSV 전화번호 형식 비교 (google,outlook,vcard) */
	public String getWbBcCsvCntcType(String param) {
		if("Home".equals(param)){
			return "homePhon";
		}else if("Mobile".equals(param)){
			return "mbno";
		}else if("Work".equals(param)){
			return "compPhon";
		}else if("Work Fax".equals(param)){
			return "fno";
		}else{
			return "email";
		}
	}
	
	/** 전화번호 형식 split 으로 잘라 String[] 로가져오기*/
	 public String[] getSplitTellNo(String noStr) {
	    Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
        if(noStr == null) return new String[]{ "", "", ""};
        Matcher matcher = tellPattern.matcher( noStr);

        if(matcher.matches()) {
            return new String[]{ matcher.group( 1), matcher.group( 2), matcher.group( 3)};
        } else {
            return new String[]{ "", "", ""};
        }
    }
	 
	/** 전화번호 형식 확인 */
    public boolean isCheckTellNo(String noStr) {
    	Pattern tellPattern = Pattern.compile( "^(01\\d{1}|02|0505|0502|0506|0\\d{1,2})-?(\\d{3,4})-?(\\d{4})");
    	if(noStr == null) return false;
    	Matcher matcher = tellPattern.matcher( noStr);

		if(matcher.matches()) {
	        return true;
	    } else {
	        return false;
	    }
	}
    
	/** bxId별 함 URL 리턴 - menuId 없는 URL 리턴 */
	public String getBxUrlByBxId(String bxId){
		if(bxId.equals("psn"))
			return "/wb/listBc.do";
		else if(bxId.equals("open"))
			return "/wb/listOpenBc.do";
		else if(bxId.equals("pub"))
			return "/wb/pub/listPubBc.do";
		else if(bxId.equals("agnt"))
			return "/wb/listAgntBc.do";
		else if(bxId.equals("bumk"))
			return "/wb/setBcBumk.do";
		else if(bxId.equals("psnMetng"))
			return "/wb/listMetng.do";
		else if(bxId.equals("agntMetng"))
			return "/wb/listAgntMetng.do";		
		else
			return "";
	}
	
	/** 한글 초성 찾기 */
	public String direct(char b){
	   if(Pattern.matches("[0-9]", String.valueOf(b))) return "123";
	   String chosung = null;
	   int first = (b - 44032 ) / ( 21 * 28 );
		switch(first){
			case 0:chosung="ㄱ";break;
			case 1:chosung="ㄱ";break;
			case 2:chosung="ㄴ";break;
			case 3:chosung="ㄷ";break;
			case 4:chosung="ㄷ";break;
			case 5:chosung="ㄹ";break;
			case 6:chosung="ㅁ";break;
			case 7:chosung="ㅂ";break;
			case 8:chosung="ㅂ";break;
			case 9:chosung="ㅅ";break;
			case 10:chosung="ㅅ";break;
			case 11:chosung="ㅇ";break;
			case 12:chosung="ㅈ";break;
			case 13:chosung="ㅈ";break;
			case 14:chosung="ㅊ";break;
			case 15:chosung="ㅋ";break;
			case 16:chosung="ㅌ";break;
			case 17:chosung="ㅍ";break;
			case 18:chosung="ㅎ";break;
			default: chosung=String.valueOf(b);
		}
		return chosung;
    }
	
	/**
	  * 정규식 패턴 검증
	  * @param pattern
	  * @param str
	  * @return
	  */
	 public String getEtcChar(String str){
		 if(Pattern.matches("[0-9]", str)) return "123";
		 if(Pattern.matches("[A-Za-z]", str)) return "A~Z";
		 return str;		 
	 }
	 
	 /** 사용자 명함 등록 */
	 public void saveBcUserList(HttpServletRequest request, QueryQueue queryQueue, List<String> userUidList, String fldId) throws Exception{
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		
		UserVo userVo = LoginSession.getUser(request);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				
		// 사용자 조회
		OrUserBVo searchOrUserBVo = new OrUserBVo();
		searchOrUserBVo.setUserStatCd("02");//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		searchOrUserBVo.setOrderBy("ORG_ID,SORT_ORDR");
		// 타사 조직도 조회
		if(!sysPlocMap.containsKey("globalOrgChartEnable") || "N".equals(sysPlocMap.get("globalOrgChartEnable")))
			searchOrUserBVo.setCompId(userVo.getCompId());
		searchOrUserBVo.setUserUidList(userUidList);
		
		List<Map<String, Object>> returnList = orCmSvc.getUserMapListForSync(searchOrUserBVo, langTypCd);
		if(returnList==null || returnList.size()==0){
			//cm.msg.noData=해당하는 데이터가 없습니다.
			throw new CmException("cm.msg.noData", request);
		}
		
		/** 공유명함여부 */
		boolean isPub = request.getRequestURI().startsWith("/wb/pub/");
		WbBcBVo wbBcBVo = null;
		/*
		String schBcRegrUid = ParamUtil.getRequestParam(request, "schBcRegrUid", false);
		*//** 사용자의 명함 대리 관리자 정보 *//*
		Map<String,Object> agntInfoMap = wbCmSvc.getAgntInfoMap(request , schBcRegrUid , userVo.getUserUid() , "RW" );
		//대리관리자일 경우 대리권한 부여자의 회사ID를 조회한다.
		wbBcBVo.setCopyRegrUid(schBcRegrUid);
		wbBcBVo.setCompId((String)agntInfoMap.get("compId"));*/
		
		List<WbBcCntcDVo> wbBcCntcDVoList = null;
		for(Map<String, Object> userVoMap : returnList){
			wbBcBVo=new WbBcBVo();
			wbBcBVo.setCompId(userVo.getCompId());
			wbBcBVo.setPub(isPub); // 공유명함여부			
			wbBcBVo.setFldId(fldId);
			wbBcBVo.setAddUserUid((String)userVoMap.get("userUid"));
			setUserMapToBcVo(wbBcBVo, userVoMap, langTypCd);
			
			// 연락처
			wbBcCntcDVoList = getBcCntcList(userVoMap, new String[]{"compPhon","homePhon","mbno", "email"});
			if(wbBcCntcDVoList!=null) wbBcBVo.setWbBcCntcDVo(wbBcCntcDVoList);
			// 저장
			saveBc(request, queryQueue , wbBcBVo , userVo);
		}
	 }
	 
	 /** 사용자맵 ==> 명함VO 세팅*/
	public void setUserMapToBcVo(WbBcBVo wbBcBVo, Map<String, Object> userVoMap, String langTypCd) throws SQLException{
		wbBcBVo.setBcNm((String)userVoMap.get("rescNm")); // 명함명
		wbBcBVo.setCompNm(ptCmSvc.getPtCompBVo((String)userVoMap.get("compId"), langTypCd).getCompNm()); // 회사명
		wbBcBVo.setDeptNm((String)userVoMap.get("deptRescNm")); // 부서명
		wbBcBVo.setGradeNm((String)userVoMap.get("gradeNm")); // 직급
		wbBcBVo.setTichCont((String)userVoMap.get("tichCont")); // 담당업무
		wbBcBVo.setFno((String)userVoMap.get("compFno")); // 팩스
		// 주소
		wbBcBVo.setHomeZipNo((String)userVoMap.get("homeZipNo"));
		wbBcBVo.setHomeAdr((String)userVoMap.get("homeAdr"));
		wbBcBVo.setHomeDetlAdr((String)userVoMap.get("homeDetlAdr"));
		wbBcBVo.setCompZipNo((String)userVoMap.get("compZipNo"));
		wbBcBVo.setCompAdr((String)userVoMap.get("compAdr"));
		wbBcBVo.setCompDetlAdr((String)userVoMap.get("compDetlAdr"));
		wbBcBVo.setPsnHpageUrl((String)userVoMap.get("hpageUrl")); // 홈페이지
		
	 }
	
    /** 연락처 세팅 */	 
	public List<WbBcCntcDVo> getBcCntcList(Map<String,Object> userVoMap, String[] attrs){
		List<WbBcCntcDVo> wbBcCntcDVoList = new ArrayList<WbBcCntcDVo>();
		WbBcCntcDVo wbBcCntcDVo = null;
		for(String cntcTypCd : attrs){
			wbBcCntcDVo = new WbBcCntcDVo();
			wbBcCntcDVo.setCntcTypCd(cntcTypCd);
		    wbBcCntcDVo.setCntcClsCd("email".equals(cntcTypCd) ? "EMAIL" : "CNTC");
		    wbBcCntcDVo.setCntcCont((String)userVoMap.get(cntcTypCd));
			wbBcCntcDVoList.add(wbBcCntcDVo);
		}
		if(wbBcCntcDVoList.size()==0) return null;
		
		return wbBcCntcDVoList;
	}
	
	/** 조직도 사용자 ==> 명함에 매핑 */
	public void setUserToBc(WbBcBVo wbBcBVo, String userUid, String langTypCd){
		try{
			Map<String, Object> userVoMap = orCmSvc.getUserMap(userUid, langTypCd);
			if(userVoMap==null) return;
			setUserMapToBcVo(wbBcBVo, userVoMap, langTypCd);
			
			List<WbBcCntcDVo> wbBcCntcDVoList = null;
			// 연락처
			wbBcCntcDVoList = getBcCntcList(userVoMap, new String[]{"compPhon","homePhon","mbno"});
			if(wbBcCntcDVoList!=null) wbBcCntcDVoList = checkCntcList(wbBcCntcDVoList);
			else wbBcCntcDVoList = setCntcList(0 , "CNTC");
			wbBcBVo.setWbBcCntcDVo(wbBcCntcDVoList);
			
			// 메일
			wbBcCntcDVoList = getBcCntcList(userVoMap, new String[]{"email"});
			if(wbBcCntcDVoList==null) wbBcCntcDVoList = setCntcList(1 , "EMAIL");
			wbBcBVo.setWbBcEmailDVo(wbBcCntcDVoList);
			
			// 사용자이미지상세(OR_USER_IMG_D) 테이블
			OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
			// 겸직자 이미지 조회
			orUserImgDVo.setUserUid(userUid);
			orUserImgDVo.setUserImgTypCd("03"); // 사진
			orUserImgDVo = (OrUserImgDVo)commonDao.queryVo(orUserImgDVo);
			if(orUserImgDVo!=null){
				/** 명함이미지상세 */
				WbBcImgDVo wbBcImgDVo = new WbBcImgDVo();
				wbBcImgDVo.setImgPath(orUserImgDVo.getImgPath());
				wbBcImgDVo.setImgWdth(orUserImgDVo.getImgWdth());
				wbBcImgDVo.setImgHght(orUserImgDVo.getImgHght());
				wbBcBVo.setWbBcImgDVo(wbBcImgDVo);
			}
			
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
		}
		
	}
}
