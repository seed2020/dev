package com.innobiz.orange.web.bb.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaColmDispDVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtLoutSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;
import com.innobiz.orange.web.pt.vo.PtMnuDVo;

/** 게시판관리 서비스 */
@Service
public class BbBrdSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 조직 공통 서비스 */
	@Resource(name = "orCmSvc")
	private OrCmSvc orCmSvc;

	/** 캐쉬 서비스 */
	@Autowired
	private PtCacheSvc ptCacheSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 메뉴 레이아웃 서비스 */
	@Autowired
	private PtLoutSvc ptLoutSvc;
	
	/** 게시판관리VO 얻기 */
	public BaBrdBVo getBaBrdBVo(String langTypCd, String brdId) throws SQLException {
		// 캐쉬 조회
		BaBrdBVo baBrdBVo = (BaBrdBVo) ptCacheSvc.getCache(CacheConfig.BRD, langTypCd, brdId, 30);
		if (baBrdBVo != null) return baBrdBVo;
		
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		baBrdBVo = new BaBrdBVo();
		baBrdBVo.setBrdId(brdId);
		baBrdBVo.setQueryLang(langTypCd);
		baBrdBVo = (BaBrdBVo) commonDao.queryVo(baBrdBVo);
		if(baBrdBVo!=null && baBrdBVo.getOptVa()!=null)
			baBrdBVo.setOptMap(getOptToMap(baBrdBVo.getOptVa()));
		
		// 캐쉬 저장
		ptCacheSvc.setCache(CacheConfig.BRD, langTypCd, brdId, baBrdBVo);
		return baBrdBVo;
	}

	/** 게시판관리VO 목록 얻기 */
	@SuppressWarnings("unchecked")
	public List<BaBrdBVo> getBaBrdBVoList(HttpServletRequest request) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		setCompId(request, baBrdBVo);           // 회사ID
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		return (List<BaBrdBVo>) commonDao.queryList(baBrdBVo);
	}

	/**
	 * 컬럼표시여부 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	public List<BaColmDispDVo> getBaColmDispDVoList(HttpServletRequest request, String brdId, boolean listOrdered, 
			String useYn, String listDispYn, String readDispYn, boolean readOrdered) throws SQLException {
		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - BIND
		BaColmDispDVo baColmDispDVo = new BaColmDispDVo();
		baColmDispDVo.setQueryLang(langTypCd);
		baColmDispDVo.setBrdId(brdId);
		//if (useYn != null) baColmDispDVo.setUseYn(useYn);
		if (listDispYn != null)
			baColmDispDVo.setListDispYn(listDispYn);
		else if (readDispYn != null)
			baColmDispDVo.setReadDispYn(readDispYn);
		
		if (listOrdered) baColmDispDVo.setOrderBy("T.LIST_DISP_ORDR ASC");
		else if (readOrdered) baColmDispDVo.setOrderBy("T.READ_DISP_ORDR ASC");

		// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - SELECT
		List<BaColmDispDVo> baColmDispDVoList = (List<BaColmDispDVo>) commonDao.queryList(baColmDispDVo);

		if (baColmDispDVoList != null) {
			for (BaColmDispDVo dispVo : baColmDispDVoList) {
				// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
				BaTblColmDVo colmVo = new BaTblColmDVo();
				colmVo.setQueryLang(langTypCd);
				colmVo.setColmId(dispVo.getColmId());
				colmVo = (BaTblColmDVo) commonDao.queryVo(colmVo);
				if(colmVo.getColmTyp()!=null && !colmVo.getColmTyp().isEmpty()){
					if("CODE".equals(colmVo.getColmTyp()) || "CODERADIO".equals(colmVo.getColmTyp())) dispVo.setSortOptVa("code");
					else if("USER".equals(colmVo.getColmTyp())) dispVo.setSortOptVa("user");
					else if("DEPT".equals(colmVo.getColmTyp())) dispVo.setSortOptVa("dept");
					else dispVo.setSortOptVa("value");
				}else
					dispVo.setSortOptVa("value");
				if(colmVo.getColmNm()!=null && !colmVo.getColmNm().isEmpty())
					dispVo.setAtrbId(StringUtil.toCamelNotation(colmVo.getColmNm(), false));
				dispVo.setColmVo(colmVo);
			}
		}
		return baColmDispDVoList;
	}

	/** 담당자 정보 세팅 */
	public void setPichVo(BaBrdBVo baBrdBVo, String langTypCd) throws IOException, CmException, SQLException  {
		if (baBrdBVo == null) return;
		if (baBrdBVo.getPichUid() == null || "".equals(baBrdBVo.getPichUid())) return;
		
		// 사용자기본(OR_USER_B) 테이블 - SELECT
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setQueryLang(langTypCd);
		orUserBVo.setUserUid(baBrdBVo.getPichUid());
		orUserBVo = (OrUserBVo) commonDao.queryVo(orUserBVo);
		if(orUserBVo==null) return;
		baBrdBVo.setPichVo(orUserBVo);
		
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블 - SELECT
		OrUserPinfoDVo orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setOdurUid(orUserBVo.getOdurUid());
		orUserPinfoDVo = (OrUserPinfoDVo) commonDao.queryVo(orUserPinfoDVo);
		
		if (orUserPinfoDVo != null) {
			orCmSvc.decryptUserPinfo(orUserPinfoDVo);
			baBrdBVo.setPichPinfoVo(orUserPinfoDVo);
		}
	}

	/** 게시판ID목록으로 게시판명목록을 얻음 */
	public String getBrdNms(String langTypCd, String brdIdList) throws SQLException {
		String[] split = brdIdList.split(",");
		StringBuilder brdNms = new StringBuilder();
		for (String brdId : split) {
			BaBrdBVo vo = getBaBrdBVo(langTypCd, brdId);
			if (brdNms.length() > 0) brdNms.append(',');
			brdNms.append(vo.getRescNm());
		}
		return brdNms.toString();
	}

	/** 회사ID 세팅 */
	public void setCompId(HttpServletRequest request, BaBrdBVo baBrdBVo) {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 회사ID
		baBrdBVo.setCompId(userVo.getCompId());
	}

	/** 컬럼표시 목록표시여부 세팅 */
	public boolean setListDispYn(BaBrdBVo baBrdBVo, BaTblColmDVo colmVo, BaColmDispDVo dispVo) {
		// 카테고리
		if ("CAT_ID".equals(colmVo.getColmNm())) {
			dispVo.setListDispYn(baBrdBVo.getCatYn());
			return true;
		}
		// 찬성수
		if ("PROS_CNT".equals(colmVo.getColmNm())) {
			dispVo.setListDispYn(baBrdBVo.getFavotYn());
			return true;
		}
		// 반대수
		if ("CONS_CNT".equals(colmVo.getColmNm())) {
			dispVo.setListDispYn(baBrdBVo.getFavotYn());
			return true;
		}
		// 추천수
		if ("RECMD_CNT".equals(colmVo.getColmNm())) {
			dispVo.setListDispYn(baBrdBVo.getRecmdUseYn());
			return true;
		}
		// 점수
		if ("SCRE".equals(colmVo.getColmNm())) {
			dispVo.setListDispYn(baBrdBVo.getScreUseYn());
			return true;
		}
		
		// 조회 이력
		if ("READ_CNT".equals(colmVo.getColmNm())) {
			if(baBrdBVo.getReadHstUseYn()==null || "N".equals(baBrdBVo.getReadHstUseYn())){
				dispVo.setListDispYn("N");
				dispVo.setReadDispYn("N");
			}else{
				dispVo.setListDispYn("Y");
				dispVo.setReadDispYn("Y");
			}
			
			return true;
		}
				
		// FAQ 게시판 일 경우
		if(baBrdBVo.getBrdTypCd()!=null && "F".equals(baBrdBVo.getBrdTypCd())){
			if(ArrayUtil.isInArray(new String[]{"BULL_REZV_DT", "BULL_EXPR_DT", "PROS_CNT", "CONS_CNT", "RECMD_CNT", "SCRE"}, colmVo.getColmNm())){
				dispVo.setListDispYn("N");
				dispVo.setReadDispYn("N");
				return true;
			}
		}
		return false;
	}
	
	/** 메뉴 권한이 있는 게시판 목록 검증 */
	public boolean chkMnuVos(List<PtMnuDVo> ptMnuList, String chkBrdId) throws SQLException{
		boolean flag=false;
		for(PtMnuDVo storedPtMnuDVo : ptMnuList){
			if(chkBrdId.equals(storedPtMnuDVo.getMdId())) {flag = true; break;}
		}
		return flag;
	}
	
	/** 메뉴 권한이 있는 게시판 목록 검증 */
	public boolean chkMdIds(List<String> mdIds, String chkBrdId) throws SQLException{
		boolean flag=false;
		if(mdIds==null) return flag;
		for(String mdId : mdIds){
			if(chkBrdId.equals(mdId)) {flag = true; break;}
		}
		return flag;
	}
	
	/** 회사목록 조회 */
	public List<PtCompBVo> getCompList(String langTypCd) throws SQLException, IOException {
		return ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
	}
	
	/** 회사별 게시판 목록 조회 */
	@SuppressWarnings("unchecked")
	public List<BaBrdBVo> getCompBrdList(String langTypCd, String compId, String allCompYn, String deptBrdYn, String discYn, boolean isMenu) throws SQLException {
		// 게시판관리(BA_BRD_B) 테이블 - SELECT
		BaBrdBVo baBrdBVo = new BaBrdBVo();
		baBrdBVo.setQueryLang(langTypCd);
		baBrdBVo.setCompId(compId);
		if(allCompYn!=null) baBrdBVo.setAllCompYn(allCompYn);// 전사게시판 여부
		if(deptBrdYn!=null) baBrdBVo.setDeptBrdYn(deptBrdYn); // 부서게시판 여부
		if(discYn!=null) baBrdBVo.setDiscYn(discYn);// 심의게시판 여부
		baBrdBVo.setOrderBy("T.BRD_ID ASC");
		
		List<BaBrdBVo> returnVoList=null;
		
		// 게시판 목록
		List<BaBrdBVo> baBrdBVoList = (List<BaBrdBVo>) commonDao.queryList(baBrdBVo);
		
		// 메뉴 목록
		List<PtMnuDVo> ptMnuDVoList = ptLoutSvc.getMnuListByMdRid("BB", compId, langTypCd);
		
		if(isMenu && baBrdBVoList!=null){
			returnVoList=new ArrayList<BaBrdBVo>();
			for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
				if(storedBaBrdBVo.getAllCompYn()!=null && "Y".equals(storedBaBrdBVo.getAllCompYn())) continue; // 전사게시판 제외
				for(PtMnuDVo menuVo : ptMnuDVoList){				
					if(menuVo.getMdId().equals(storedBaBrdBVo.getBrdId())){
						returnVoList.add(storedBaBrdBVo);
						break;
					}
				}
			}
		}
		
		return isMenu ? returnVoList : baBrdBVoList;
	}
	
	/**
	 * 컬럼표시여부 리스트 맵으로 변환
	 */
	public Map<String, BaColmDispDVo> getBaColmDispDVoListMap(ModelMap model, 
			List<BaColmDispDVo> baColmDispDVoList, boolean isDftColm, boolean isExColm, String readYn, String listYn) throws SQLException {
		Map<String, BaColmDispDVo> returnMap = null;
		if (baColmDispDVoList != null) {
			returnMap=new HashMap<String, BaColmDispDVo>();
			for (BaColmDispDVo dispVo : baColmDispDVoList) {
				if(!isDftColm && dispVo.getColmVo().getExColmYn()!=null && "N".equals(dispVo.getColmVo().getExColmYn())) continue;
				if(!isExColm && dispVo.getColmVo().getExColmYn()!=null && "Y".equals(dispVo.getColmVo().getExColmYn())) continue;
				if(readYn!=null && !dispVo.getReadDispYn().equals(readYn)) continue;
				if(listYn!=null && !dispVo.getListDispYn().equals(listYn)) continue;
				returnMap.put(StringUtil.toCamelNotation(dispVo.getColmVo().getColmNm(), false), dispVo);
			}
		}
		if(returnMap!=null){
			if( model!=null) model.put("colmMap", returnMap);
			return returnMap;
		}
		return null;
	}
	
	/** 게시판 옵션 Map으로 변환 후 세팅*/
	@SuppressWarnings("unchecked")
	public Map<String,Object> getOptToMap(String value) throws SQLException{
		if(value==null || value.isEmpty()) return null;
		Object obj=JsonUtil.jsonToObj(value);
		if(obj==null) return null;
		return (Map<String,Object>)obj;
	}
	
	/**
	 * 컬럼표시여부 리스트 리턴
	 */
	@SuppressWarnings("unchecked")
	public List<BaColmDispDVo> getColDispList(HttpServletRequest request, 
			List<BaColmDispDVo> baColmDispDVoList, String brdId, boolean ordered, 
			String listDispYn, String readDispYn, boolean isDftColm, boolean isExColm) throws SQLException {
		
		boolean isNew = baColmDispDVoList==null;
		// 세션의 언어코드
		String langTypCd = null;
		
		if(isNew){
			langTypCd = LoginSession.getLangTypCd(request);		
			// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - BIND
			BaColmDispDVo baColmDispDVo = new BaColmDispDVo();
			baColmDispDVo.setQueryLang(langTypCd);
			baColmDispDVo.setBrdId(brdId);
			//if (useYn != null) baColmDispDVo.setUseYn(useYn);
			if (listDispYn != null){
				baColmDispDVo.setListDispYn(listDispYn);
				if (ordered) baColmDispDVo.setOrderBy("T.LIST_DISP_ORDR ASC");
			}else if (readDispYn != null){
				baColmDispDVo.setReadDispYn(readDispYn);
				if (ordered) baColmDispDVo.setOrderBy("T.READ_DISP_ORDR ASC");
			}
		
			// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - SELECT
			baColmDispDVoList = (List<BaColmDispDVo>) commonDao.queryList(baColmDispDVo);
		}
		
		List<BaColmDispDVo> returnList=new ArrayList<BaColmDispDVo>();
		if (baColmDispDVoList != null) {
			BaTblColmDVo colmVo = null;
			for (BaColmDispDVo dispVo : baColmDispDVoList) {
				if(!isNew && ((listDispYn!=null && !listDispYn.equals(dispVo.getListDispYn())) || (readDispYn!=null && !readDispYn.equals(dispVo.getReadDispYn())) )) continue;
				if(isNew){
					// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
					colmVo = new BaTblColmDVo();
					colmVo.setQueryLang(langTypCd);
					colmVo.setColmId(dispVo.getColmId());
					colmVo = (BaTblColmDVo) commonDao.queryVo(colmVo);
				}else
					colmVo=dispVo.getColmVo();
				if(colmVo==null) continue;
				if(!isDftColm && colmVo.getExColmYn()!=null && "N".equals(colmVo.getExColmYn())) continue;
				if(!isExColm && colmVo.getExColmYn()!=null && "Y".equals(colmVo.getExColmYn())) continue;
				
				if(isNew){
					if(colmVo.getColmTyp()!=null && !colmVo.getColmTyp().isEmpty()){
						if("CODE".equals(colmVo.getColmTyp()) || "CODERADIO".equals(colmVo.getColmTyp())) dispVo.setSortOptVa("code");
						else if("USER".equals(colmVo.getColmTyp())) dispVo.setSortOptVa("user");
						else if("DEPT".equals(colmVo.getColmTyp())) dispVo.setSortOptVa("dept");
						else dispVo.setSortOptVa("value");
					}else
						dispVo.setSortOptVa("value");
					
					dispVo.setColmVo(colmVo);
				}
				returnList.add(dispVo);
			}
		}
		return returnList;
	}
	
	/** 컬럼표시여부 리스트 리턴 [기본,확장 컬럼별] */
	public List<BaColmDispDVo> getColDispList(List<BaColmDispDVo> baColmDispDVoList, String listDispYn, String readDispYn, boolean isDftColm, boolean isExColm) throws SQLException {
		return getColDispList(null, baColmDispDVoList, null, true, listDispYn, readDispYn, isDftColm, isExColm);
	}
	
	/** 컬럼표시여부 - 게시판 설정별 컬럼 데이터 삭제 */
	public List<BaColmDispDVo> getColDispList(String langTypCd, String brdId, List<BaColmDispDVo> baColmDispDVoList) throws SQLException {
		
		List<String> removeNmList=new ArrayList<String>();
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = getBaBrdBVo(langTypCd, brdId);
		
		if(baBrdBVo.getOptMap()!=null){
			/*if(baBrdBVo.getOptMap().get("bbTgtDispYn")==null || !"Y".equals(baBrdBVo.getOptMap().get("bbTgtDispYn"))){
				removeNmList.add("BULL_REZV_DT");
			}*/
			/*if(baBrdBVo.getOptMap().get("bbOptYn")==null || !"Y".equals(baBrdBVo.getOptMap().get("bbOptYn"))){
				removeNmList.add("BULL_EXPR_DT");
			}*/
		}
		
		if(baBrdBVo.getCatYn()==null || !"Y".equals(baBrdBVo.getCatYn())){
			removeNmList.add("CAT_ID");
		}
		
		if(baBrdBVo.getRecmdUseYn()==null || !"Y".equals(baBrdBVo.getRecmdUseYn())){
			removeNmList.add("RECMD_CNT");
		}
				
		if(baBrdBVo.getFavotYn()==null || !"Y".equals(baBrdBVo.getFavotYn())){
			removeNmList.add("PROS_CNT");
			removeNmList.add("CONS_CNT");
		}	
		
		if(baBrdBVo.getScreUseYn()==null || !"Y".equals(baBrdBVo.getScreUseYn())){
			removeNmList.add("SCRE");
		}
		
		if(baBrdBVo.getReadHstUseYn()==null || "N".equals(baBrdBVo.getReadHstUseYn())){
			removeNmList.add("READ_CNT");
		}
		
		if(removeNmList.size()==0) return baColmDispDVoList;
		String[] arr = ArrayUtil.toArray(removeNmList);
		
		List<BaColmDispDVo> removeList=new ArrayList<BaColmDispDVo>();
		
		for(BaColmDispDVo storedBaColmDispDVo : baColmDispDVoList){
			if(ArrayUtil.isInArray(arr, storedBaColmDispDVo.getColmVo().getColmNm())){
				removeList.add(storedBaColmDispDVo);
			}
		}
		if(removeList.size()>0){
			for(BaColmDispDVo removeVo : removeList){
				baColmDispDVoList.remove(removeVo);
			}
		}
		
		return baColmDispDVoList;
	}
	
	/** 모바일 목록 정렬 변경 - 대표컬럼이 있을경우에만 */
	public List<BaColmDispDVo> getMobileListSort(BaBrdBVo baBrdBVo, List<BaColmDispDVo> baColmDispDVoList){
		if(!ServerConfig.IS_MOBILE || baBrdBVo.getOptMap()==null || baBrdBVo.getOptMap().get("listCondApplyYn")==null || "N".equals(baBrdBVo.getOptMap().get("listCondApplyYn")) || !"N".equals(baBrdBVo.getBrdTypCd()))
			return baColmDispDVoList;
		
		List<BaColmDispDVo> returnList=new ArrayList<BaColmDispDVo>();
		List<BaColmDispDVo> tmpList=new ArrayList<BaColmDispDVo>();
		int index=0;
		boolean isSort=true;
		for(BaColmDispDVo dispVo : baColmDispDVoList){
			if(index==0 && dispVo.getTitlColYn()!=null && "Y".equals(dispVo.getTitlColYn())){
				isSort=false;
				break;
			}
			if(dispVo.getTitlColYn()!=null && "Y".equals(dispVo.getTitlColYn())) returnList.add(dispVo);
			else tmpList.add(dispVo);
			index++;
		}
		if(isSort){
			returnList.addAll(tmpList);
			return returnList;
		}else{
			return baColmDispDVoList;
		}
		
	}
	
	/** 대표컬럼 조회[모바일 목록, 이전다음] */
	public BaColmDispDVo getFirstBaColmDispDVo(BaBrdBVo baBrdBVo, List<BaColmDispDVo> baColmDispDVoList){
		if(baBrdBVo.getOptMap()==null || baBrdBVo.getOptMap().get("listCondApplyYn")==null || "N".equals(baBrdBVo.getOptMap().get("listCondApplyYn")) || !"N".equals(baBrdBVo.getBrdTypCd()))
			return null;
		BaColmDispDVo baColmDispDVo=null;
		int index=0;
		for(BaColmDispDVo dispVo : baColmDispDVoList){
			if((index==0 && dispVo.getTitlColYn()!=null && "Y".equals(dispVo.getTitlColYn())) || (index>0 && dispVo.getTitlColYn()!=null && "Y".equals(dispVo.getTitlColYn()))){
				baColmDispDVo=dispVo;
				break;
			}
			index++;
		}
		if(baColmDispDVo==null){
			return baColmDispDVoList.get(0);
		}
		
		return baColmDispDVo;
	}
	
	/** 컬럼표시여부 리턴 */
	@SuppressWarnings("unchecked")
	public boolean isColmDisp(String langTypCd, String brdId, 
			String useYn, String chkNm) throws SQLException {
		// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - BIND
		BaColmDispDVo baColmDispDVo = new BaColmDispDVo();
		baColmDispDVo.setQueryLang(langTypCd);
		baColmDispDVo.setBrdId(brdId);
		baColmDispDVo.setListDispYn("Y");
		
		boolean isDisp=false;
		// 컬럼표시여부(BA_COLM_DISP_D) 테이블 - SELECT
		BaTblColmDVo colmVo = null;
		String colmNm=null;
		List<BaColmDispDVo> baColmDispDVoList = (List<BaColmDispDVo>) commonDao.queryList(baColmDispDVo);
		if(baColmDispDVoList!=null && baColmDispDVoList.size()>0){
			for (BaColmDispDVo dispVo : baColmDispDVoList) {
				// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
				colmVo = new BaTblColmDVo();
				colmVo.setQueryLang(langTypCd);
				colmVo.setColmId(dispVo.getColmId());
				colmVo = (BaTblColmDVo) commonDao.queryVo(colmVo);
				if(colmVo!=null){
					colmNm=StringUtil.toCamelNotation(colmVo.getColmNm(), false);
					if(colmNm!=null && colmNm.equals(chkNm)){
						isDisp=true;
						break;
					}
				}
			}
		}
		return isDisp;
		
	}
	
	/** 본문삽입용 양식번호 조회 */
	public String getBrdToFormNo(BaBrdBVo baBrdBVo){
		if(baBrdBVo.getOptMap()!=null && baBrdBVo.getOptMap().containsKey("wfFormNo")){
			return (String)baBrdBVo.getOptMap().get("wfFormNo");
		}
		return null;
	}
}
