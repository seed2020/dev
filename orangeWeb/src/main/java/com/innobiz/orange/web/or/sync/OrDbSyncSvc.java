package com.innobiz.orange.web.or.sync;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.config.ServerConfig;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrDbIntgDVo;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOdurSecuDVo;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.or.vo.OrUserPwHVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.secu.CRC32;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;

/** 조직도 DB 동기화 서비스 */
@Service
public class OrDbSyncSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(OrDbSyncSvc.class);
	
	private static final String DATASOURCE_NAME = "orgSync";
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 동기화 실행 */
	public boolean sync(String compId) throws SQLException, IOException, CmException{
		// 확장 전화번호 사용 여부
		Fnc.exPhoneEnable = "Y".equals(ptSysSvc.getSysPlocMap().get("exPhoneEnable"));
		
		OrDbIntgDVo orDbIntgDVo = new OrDbIntgDVo();
		orDbIntgDVo.setCompId(compId);
		orDbIntgDVo = (OrDbIntgDVo)commonSvc.queryVo(orDbIntgDVo);
		if(orDbIntgDVo == null || !"Y".equals(orDbIntgDVo.getUseYn())) return false;
		
		// 동기화된 코드의 해쉬값 - 코드의 유효성 검증용 (clsCd+cd 값의 해쉬값을 담고 있음)
		List<Integer> codeHashList = new ArrayList<Integer>();
		
		// 조직도
		Map<Integer, String[]> allOrgByOrgIdMap	= new HashMap<Integer, String[]>();
		Map<Integer, String[]> orgByOrgIdMap	= new HashMap<Integer, String[]>();
		Map<Integer, String[]> orgByRidMap		= new HashMap<Integer, String[]>();
		
		// 회사별 지원언어
		List<String> langList = getLangs(orDbIntgDVo.getCompId());
		
		SimpleQuery query = null;
		
		try {
			
			query = SimpleQuery.create(DATASOURCE_NAME, Fnc.createConfigMap(orDbIntgDVo), false);

			if(ServerConfig.IS_LOC) System.out.println("syncCd - start");
			// 코드 동기화
			syncCd(orDbIntgDVo, query, langList, codeHashList);
			
			if(ServerConfig.IS_LOC) System.out.println("syncOrg - start");
			// 조직도 동기화
			syncOrg(orDbIntgDVo, query, langList, orgByOrgIdMap, orgByRidMap, allOrgByOrgIdMap);
			
			if(ServerConfig.IS_LOC) System.out.println("syncUser - start");
			// 사용자 동기화
			syncUser(orDbIntgDVo, query, langList, orgByOrgIdMap, orgByRidMap, allOrgByOrgIdMap, codeHashList);
			
			// 캐쉬 삭제
			QueryQueue queryQueue = new QueryQueue();
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.ORG, CacheConfig.USER);
			commonSvc.execute(queryQueue);
			
		} catch (SQLException e){
			e.printStackTrace();
			throw e;
		} catch (IOException e){
			e.printStackTrace();
			throw e;
		} catch (CmException e){
			e.printStackTrace();
			throw e;
		} finally {
			query.close();
		}
		
		return true;
	}
	
	/** 코드 동기화 
	 *   - 코드는 정렬순서대로 소트되어 조회된다고 가정함
	 * */
	private void syncCd(OrDbIntgDVo orDbIntgDVo, SimpleQuery query,
			List<String> langList, List<Integer> codeHashList) throws SQLException, IOException{
		
		String cdSql = orDbIntgDVo.getCdSql();
		if(cdSql==null || cdSql.isEmpty()) return;
		
		SimpleIterator iterator = null;
		
		QueryQueue queryQueue = new QueryQueue();
		
		// GRADE_CD:직급코드, TITLE_CD:직책코드, POSIT_CD:직위코드, DUTY_CD:직무코드, SECUL_CD:보안등급코드
		String[] clsCds = {"GRADE_CD", "TITLE_CD", "POSIT_CD", "DUTY_CD", "SECUL_CD"};
		
		Integer hash, sortOrdr;
		String compId = orDbIntgDVo.getCompId();
		String rescId, langTypCd, cdVa;
		int i, langSize = langList==null ? 0 : langList.size();
		// 속성 추출용 언어 코드 - 앞글자만 대문자
		List<String> langAttrList = Fnc.toAttrLangList(langList);
		String firstLangTypCd = langList==null || langList.isEmpty() ? "ko" : langList.get(0);
		
		// 기존 코드 맵
		Map<Integer, PtCdBVo> oldCdMap = new HashMap<Integer, PtCdBVo>();
		List<Integer> oldCdList = new ArrayList<Integer>();
		
		List<PtCdBVo> cdList;
		PtCdBVo oldPtCdBVo;
		PtRescBVo ptRescBVo;
		
		// 기존 코드 조회
		for(String clsCd: clsCds){
			cdList = ptCmSvc.getAllCdList(clsCd, firstLangTypCd);
			if(cdList!=null && !cdList.isEmpty()){
				for(PtCdBVo ptCdBVo : cdList){
					hash = CRC32.hash((ptCdBVo.getClsCd()+ptCdBVo.getCd()).getBytes("UTF8"));
					oldCdList.add(hash);
					oldCdMap.put(hash, ptCdBVo);
				}
			}
		}
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean codeByCompEnable = "Y".equals(sysPlocMap.get("codeByCompEnable"));
		boolean seculByCompEnable = "Y".equals(sysPlocMap.get("codeByCompEnable"));
		
		// 동기화할 코드인지 확인용 - clsCd 가 목록에 없으면 전체 삭제하지 않음
		ArrayList<String> syncClsCdList = new ArrayList<String>();
		
		// 1. 조직도 데이터 조회
		cdSql = cdSql.trim();
		if(cdSql.endsWith(";")) cdSql = cdSql.substring(0, cdSql.length()-1);
		iterator = query.queryIterator(cdSql, null);
		Map<String, String> newCodeData;
		String clsCd, cd, useYn, refVa1, refVa2, sysdate = "sysdate";
		boolean isNewCode, isSeculCd, isCdByComp;
		
		// 정렬순서 맵
		Map<Integer, Integer> sortOrdrMap = new HashMap<Integer, Integer>();
		PtCdBVo ptCdBVo;
		
		while((newCodeData = iterator.next()) != null){
			
			clsCd = newCodeData.get("clsCd");
			cd = newCodeData.get("cd");
			useYn = "N".equals(newCodeData.get("useYn")) ? "N" : "Y";
			refVa1 = newCodeData.get("refVa1");
			refVa2 = newCodeData.get("refVa2");
			
			// 직위, 직급 .. 등 대상 코드가 아니면
			if(!ArrayUtil.isInArray(clsCds, clsCd)) continue;
			
			if(!syncClsCdList.contains(clsCd)){
				syncClsCdList.add(clsCd);
			}
			
			isSeculCd  = "SECUL_CD".equals(clsCd);
			isCdByComp = (seculByCompEnable &&  isSeculCd)
					||	(codeByCompEnable  && !isSeculCd);
			
			hash = CRC32.hash(clsCd.getBytes("UTF8"));
			sortOrdr = sortOrdrMap.get(hash);
			if(sortOrdr==null) sortOrdr = 1;
			else sortOrdr++;
			sortOrdrMap.put(hash, sortOrdr);
			
			// 코드 데이터 생성
			ptCdBVo = new PtCdBVo();
			ptCdBVo.setClsCd(clsCd);
			if(isCdByComp){
				ptCdBVo.setCd(compId+"_"+cd);
				ptCdBVo.setCompIds(compId);
			} else {
				ptCdBVo.setCd(cd);
				ptCdBVo.setCompIds("");
			}
			ptCdBVo.setSortOrdr(sortOrdr.toString());
			ptCdBVo.setUseYn(useYn);
			ptCdBVo.setFldYn("N");
			ptCdBVo.setModDt(sysdate);
			
			if(refVa1!=null){
				if("_".equals(refVa1)){
					ptCdBVo.setRefVa1("");
				} else {
					ptCdBVo.setRefVa1(refVa1);
				}
			}
			if(refVa2!=null){
				if("_".equals(refVa2)){
					ptCdBVo.setRefVa2("");
				} else {
					ptCdBVo.setRefVa2(refVa2);
				}
			}
			
			hash = CRC32.hash((clsCd+cd).getBytes("UTF8"));
			oldPtCdBVo = oldCdMap.get(hash);
			
			codeHashList.add(hash);
			
			// rescId 확인
			if(oldPtCdBVo==null){
				isNewCode = true;
				rescId = ptCmSvc.createId("PT_RESC_B");
			} else {
				isNewCode = false;
				rescId = oldPtCdBVo.getRescId();
				oldCdList.remove((Object)hash);
			}
			ptCdBVo.setRescId(rescId);
			
			
			// 리소스 처리
			for(i=0;i<langSize;i++){
				
				langTypCd = langList.get(i);
				cdVa = newCodeData.get("cd"+langAttrList.get(i)+"Va");
				if(cdVa==null && "ko".equals(langTypCd)){
					cdVa = newCodeData.get("cdVa");
				}
				
				if(cdVa!=null && !cdVa.isEmpty()){
					
					if(ptCdBVo.getCdVa()==null || "ko".equals(langTypCd)){
						ptCdBVo.setCdVa(cdVa);
					}
					
					ptRescBVo = new PtRescBVo();
					ptRescBVo.setRescId(rescId);
					ptRescBVo.setLangTypCd(langTypCd);
					ptRescBVo.setRescVa(cdVa);
					
					if(isNewCode){
						queryQueue.insert(ptRescBVo);
					} else {
						queryQueue.store(ptRescBVo);
					}
				}
			}
			
			if(isNewCode){
				queryQueue.insert(ptCdBVo);
			} else {
				queryQueue.store(ptCdBVo);
			}
			
		}
		
		// 삭제된 코드 처리
		int delSize = oldCdList.size();
		boolean deleteCd = "del".equals(orDbIntgDVo.getCdDelCd());
		String compPrefix = compId+"_";
		for(i=0;i<delSize;i++){
			hash = oldCdList.get(i);
			
			oldPtCdBVo = oldCdMap.get(hash);
			if(oldPtCdBVo != null){
				
				// 동기화 코드가 하나도 없으면 - 동기화 안함 (직위,직급은 동기화 하지만 보안등급은 동기화 안하면 - 보안등급 삭제 되지 않게함)
				if(!syncClsCdList.contains(oldPtCdBVo.getClsCd())){
					continue;
				}
				
				clsCd = oldPtCdBVo.getClsCd();
				cd = oldPtCdBVo.getCd();
				
				isSeculCd  = "SECUL_CD".equals(clsCd);
				isCdByComp = (seculByCompEnable &&  isSeculCd) || (codeByCompEnable  && !isSeculCd);
				
				if(isCdByComp && !cd.startsWith(compPrefix)) continue;
				
				// 매핑되지 않은 코드 삭제처리 - 옵션
				if(deleteCd){
					
					// 코드 리소스 지움
					ptRescBVo = new PtRescBVo();
					ptRescBVo.setRescId(oldPtCdBVo.getRescId());
					queryQueue.delete(ptRescBVo);
					
					// 코드 지움
					ptCdBVo = new PtCdBVo();
					ptCdBVo.setClsCd(oldPtCdBVo.getClsCd());
					ptCdBVo.setCd(oldPtCdBVo.getCd());
					queryQueue.delete(ptCdBVo);
					
				// 매핑되지 않은 코드 사용안함 처리 - 옵션
				} else {
					
					hash = CRC32.hash(oldPtCdBVo.getClsCd().getBytes("UTF8"));
					sortOrdr = sortOrdrMap.get(hash);
					if(sortOrdr==null) sortOrdr = 1;
					else sortOrdr++;
					sortOrdrMap.put(hash, sortOrdr);
					
					ptCdBVo = new PtCdBVo();
					ptCdBVo.setClsCd(oldPtCdBVo.getClsCd());
					ptCdBVo.setCd(oldPtCdBVo.getCd());
					ptCdBVo.setSortOrdr(sortOrdr.toString());
					ptCdBVo.setUseYn("N");
					ptCdBVo.setModDt(sysdate);
					queryQueue.update(ptCdBVo);
				}
			}
		}
		
		// 캐쉬 삭제
		String dbTime = ptCacheExpireSvc.getDbTime();
		ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
		commonSvc.execute(queryQueue);
		ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
	}
	

	
	/** 조직도 동기화
	 *  - 최상위 부모코드 : ROOT
	 *  - 데이터는 정렬순서데로 소트 되었다고 가정함
	 *  */
	private void syncOrg(OrDbIntgDVo orDbIntgDVo, SimpleQuery query, List<String> langList, 
			Map<Integer, String[]> orgByOrgIdMap, Map<Integer, String[]> orgByRidMap,
			Map<Integer, String[]> allOrgByOrgIdMap) throws SQLException, IOException {
		
		String orgSql = orDbIntgDVo.getOrgSql();
		if(orgSql==null || orgSql.isEmpty()) return;
		
		SimpleIterator iterator = null;
		
//		Map<Integer, String[]> allOrgByOrgIdMap	= new HashMap<Integer, String[]>();
		Map<Integer, String[]> allOrgByRidMap	= new HashMap<Integer, String[]>();
		
		String compId = orDbIntgDVo.getCompId();
		String[] orgArr, pOrgArr;
		OrOrgBVo orOrgBVo;
		
		// 속성 추출용 언어 코드 - 앞글자만 대문자
		List<String> langAttrList = Fnc.toAttrLangList(langList);
		String firstLangTypCd = langList==null || langList.isEmpty() ? "ko" : langList.get(0);
		
		// 부서없음조직ID, 삭제조직이동조직ID, 삭제사용자이동조직ID
		String noDeptOrgId = orDbIntgDVo.getNoDeptOrgId();
		String delOrgMoveOrgId = orDbIntgDVo.getDelOrgMoveOrgId();
		String delUserMoveOrgId = orDbIntgDVo.getDelUserMoveOrgId();
		
		if(noDeptOrgId!=null && noDeptOrgId.isEmpty()) noDeptOrgId = null;
		if(delOrgMoveOrgId!=null && delOrgMoveOrgId.isEmpty()) delOrgMoveOrgId = null;
		if(delUserMoveOrgId!=null && delUserMoveOrgId.isEmpty()) delUserMoveOrgId = null;
		
		// 입력된 ID(부서없음조직ID, 삭제조직이동조직ID, 삭제사용자이동조직ID)가 있는지 여부
		boolean noDeptOrgIdMatched = false;
		boolean delOrgMoveOrgIdMatched = false;
		boolean delUserMoveOrgIdMatched = false;
		
		// 삭제 또는 정렬순서 조정용
		ArrayList<Integer> delOrgIdList = new ArrayList<Integer>();
		
		// 1. 기존에 저장된 조직도 조회
		orOrgBVo = new OrOrgBVo();
		orOrgBVo.setCompId(compId);
		orOrgBVo.setQueryLang(firstLangTypCd);
		orOrgBVo.setOrderBy("ORG_PID, SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonSvc.queryList(orOrgBVo);
		
		// 2. 조회된 조직도 정보를 맵에 담음
		if(orOrgBVoList != null){
			Integer hash;
			String rid, orgId;
			for(OrOrgBVo storedOrOrgBVo : orOrgBVoList){
				
				rid = storedOrOrgBVo.getRid();
				orgId = storedOrOrgBVo.getOrgId();
				if(rid!=null && rid.isEmpty()) rid = null;
				
				// 조직도 Index
				// orgId=0, orgPid=1, rescId=2, orgTypCd=3, deptId=4, sysOrgYn=5, rid=6, prid=7;
				// orgPid - 삭제 상태 부서의 상위 부서 찾을때 사용
				orgArr = new String[]{
						orgId, storedOrOrgBVo.getOrgPid(), storedOrOrgBVo.getRescId(), 
						storedOrOrgBVo.getOrgTypCd(), null, storedOrOrgBVo.getSysOrgYn(), rid, null
				};
				
				// rid 조회 맵
				if(rid!=null && !rid.isEmpty()){
					hash = CRC32.hash(rid.getBytes("UTF8"));
					allOrgByRidMap.put(hash, orgArr);
				}
				
				// orgId 조회 맵
				hash = CRC32.hash(orgId.getBytes("UTF8"));
				allOrgByOrgIdMap.put(hash, orgArr);
				
				// 삭제목록
				delOrgIdList.add(hash);//hash = CRC32.hash(orgId.getBytes("UTF8"));
				
				// 부서없음조직ID - 조직이 실제로 있는지 확인
				if(!noDeptOrgIdMatched && noDeptOrgId!=null){
					if(noDeptOrgId.equals(orgId)){
						noDeptOrgIdMatched = true;
					}
				}
				// 삭제조직이동조직ID - 조직이 실제로 있는지 확인
				if(!delOrgMoveOrgIdMatched && delOrgMoveOrgId!=null){
					if(delOrgMoveOrgId.equals(orgId)){
						delOrgMoveOrgIdMatched = true;
					}
				}
				// 삭제사용자이동조직ID - 조직이 실제로 있는지 확인
				if(!delUserMoveOrgIdMatched && delUserMoveOrgId!=null){
					if(delUserMoveOrgId.equals(orgId)){
						delUserMoveOrgIdMatched = true;
					}
				}
			}
			orOrgBVoList = null;
		}
		if(!noDeptOrgIdMatched) orDbIntgDVo.setNoDeptOrgId(null);
		if(!delOrgMoveOrgIdMatched) orDbIntgDVo.setDelOrgMoveOrgId(null);
		if(!delUserMoveOrgIdMatched) orDbIntgDVo.setDelUserMoveOrgId(null);
		
		
		QueryQueue queryQueue = new QueryQueue();
		// 조직도 담을 - orgPid 세팅 목적
		orOrgBVoList = new ArrayList<OrOrgBVo>();
		
		// 정렬 순서 맵
		Map<Integer, Integer> sortOrdrByOrgIdMap = new HashMap<Integer, Integer>();
		
		boolean isNewOrg, isSysOrg;
		Integer hash, sortOrdr;
		String rid, prid, orgId, orgPid, rescId, sysdate = "sysdate", useYn;
		String orgTypCd;//C:회사, G:기관, D:부서, P:파트
		Map<String, String> orgNewData;
		
		int i, langSize = langList.size();
		String langTypCd, rescVa, firstOrgId = null;
		OrRescBVo orRescBVo;
		
		// 데이터 조회 후 조직도 테이블에 저장용 데이터 만들기
		try {
			
			// 1. 조직도 데이터 조회
			
			orgSql = orgSql.trim();
			if(orgSql.endsWith(";")) orgSql = orgSql.substring(0, orgSql.length()-1);
			iterator = query.queryIterator(orgSql, null);
			
			// 2. 조직도 기본 정보 생성 / 정렬순서, 부보id, 파트의경우 부서(id, rescId) 는 제외
			//    - rid 기반으로 부모를 찾아야 해서 조회된 데이터가 모두 1차 가공 된 뒤 처리함
			while((orgNewData = iterator.next()) != null){

				rid  = orgNewData.get("rid");
				prid = orgNewData.get("prid");
				if(rid!=null) rid = rid.trim();
				if(prid!=null) prid = prid.trim();
				
				if(rid==null || rid.isEmpty()){
					LOGGER.error("[SYNC ORG ERROR] NO rid : "+orgNewData);
					continue;
				}
				if(prid==null || prid.isEmpty()){
					LOGGER.error("[SYNC ORG ERROR] NO prid : "+orgNewData);
					continue;
				}
				
				
				hash = CRC32.hash(rid.getBytes("UTF8"));
				orgArr = allOrgByRidMap.get(hash);
				
				if(orgArr == null){
					isNewOrg = true;
					isSysOrg = false;
					
					orgId = orCmSvc.createId("OR_ORG_B");
					rescId = orCmSvc.createId("OR_RESC_B");
					
				} else {
					isNewOrg = false;
					isSysOrg = "Y".equals(orgArr[Oidx.sysOrgYn]);
					
					orgId  = orgArr[Oidx.orgId];
					rescId = orgArr[Oidx.rescId];
					
//					orgArr[Oidx.rid ] =  rid;
					orgArr[Oidx.prid] = prid;
					
					// call by reference setting
					hash = CRC32.hash(rid.getBytes("UTF8"));
					orgByRidMap.put(hash, orgArr);
					
					hash = CRC32.hash(orgId.getBytes("UTF8"));
					orgByOrgIdMap.put(hash, orgArr);
					
					// 삭제 목록에서 제거
					hash = CRC32.hash(orgId.getBytes("UTF8"));
					delOrgIdList.remove((Object)hash);
				}
				
				if(firstOrgId==null){
					firstOrgId = orgId;
					if(!noDeptOrgIdMatched){
						orDbIntgDVo.setNoDeptOrgId(firstOrgId);
					}
				}
				
				orOrgBVo = new OrOrgBVo();
				orOrgBVo.setOrgId(orgId);
				orOrgBVo.setRescId(rescId);
				orOrgBVo.setCompId(compId);
				orOrgBVo.setRid(rid);
				
				
				// 조직구분코드 - C:회사, G:기관, D:부서, P:파트
				if("ROOT".equals(prid)){
					orgTypCd = "C";
				} else {
					orgTypCd = orgNewData.get("orgTypCd");
					if(!"G".equals(orgTypCd) && !"P".equals(orgTypCd)){
						orgTypCd = "D";
					}
				}
				orOrgBVo.setOrgTypCd(orgTypCd);
				
				// 파트가 아닌경우 - 부서정보 세팅 - 파트의 경우 orgPid 를 세팅할 때 처리 - 아래
				if(!"P".equals(orgTypCd)){
					orOrgBVo.setDeptId(orgId);
					orOrgBVo.setDeptRescId(rescId);
				}
				
				if(!isSysOrg){
					useYn = orgNewData.get("useYn");
					if(!"N".equals(useYn)) useYn = "Y";
					orOrgBVo.setUseYn(useYn);
					
					orOrgBVo.setSysOrgYn("N");
				}
				orOrgBVo.setModDt(sysdate);
				
				// 시스템 조직이 아닌 경우 - 동기화 제외함
				if(!isSysOrg){
					
					// 리소스 처리
					for(i=0;i<langSize;i++){
						langTypCd = langList.get(i);
						rescVa = orgNewData.get("org"+langAttrList.get(i)+"Nm");
						if(rescVa==null && "ko".equals(langTypCd)){
							rescVa = orgNewData.get("orgNm");
						}
						
						if(rescVa!=null && !rescVa.isEmpty()){
							
							if(orOrgBVo.getOrgNm()==null || "ko".equals(langTypCd)){
								orOrgBVo.setOrgNm(rescVa);
							}
							
							orRescBVo = new OrRescBVo();
							orRescBVo.setRescId(rescId);
							orRescBVo.setLangTypCd(langTypCd);
							orRescBVo.setRescVa(rescVa);
							
							if(isNewOrg){
								queryQueue.insert(orRescBVo);
							} else {
								queryQueue.store(orRescBVo);
							}
						}
					}
				}
				
				if(isNewOrg){
					queryQueue.insert(orOrgBVo);
					
					// 신규 처리
					// orgId=0, orgPid=1, rescId=2, orgTypCd=3, deptId=4, sysOrgYn=5, rid=6, prid=7;
					orgArr = new String[]{
							orgId, null, rescId, 
							orOrgBVo.getOrgTypCd(), null, orOrgBVo.getSysOrgYn(), rid, prid
					};
					
					// rid 조회 맵
					hash = CRC32.hash(rid.getBytes("UTF8"));
					allOrgByRidMap.put(hash, orgArr);
					orgByRidMap.put(hash, orgArr);
					
					
					// orgId 조회 맵
					hash = CRC32.hash(orgId.getBytes("UTF8"));
					allOrgByOrgIdMap.put(hash, orgArr);
					orgByOrgIdMap.put(hash, orgArr);
					
				} else {
					queryQueue.update(orOrgBVo);
				}
				// 부모 조직ID 정리용
				orOrgBVoList.add(orOrgBVo);
			}
			
		} finally {
			iterator.close();
			iterator = null;
		}
		
		
		//////////////////////////////////
		//
		//  1. 조직 정보 의 prid 로 orgPid 를 세팅함
		//  2. orgPid 로 정렬 순서 세팅
		
		int orgSize = orOrgBVoList.size();
		for(i=0;i<orgSize;i++){
			
			orOrgBVo = orOrgBVoList.get(i);
			
			// rid 로 해당 데이터 조회
			rid = orOrgBVo.getRid();
			hash = CRC32.hash(rid.getBytes("UTF8"));
			orgArr = orgByRidMap.get(hash);
			if(orgArr==null) continue;
			
			// prid 로 해당 조직 찾음
			prid = orgArr[Oidx.prid];
			hash = CRC32.hash(prid.getBytes("UTF8"));
			pOrgArr = orgByRidMap.get(hash);
			
			if(pOrgArr != null){
				// orgPid 찾음
				orgPid = pOrgArr[Oidx.orgId];
			} else if("ROOT".equals(prid)){
				orgPid = "ROOT";
			} else {
				// 못찾은 경우
				LOGGER.error("[SYNC ORG WARN] no parent - prid:"+prid);
				if(noDeptOrgIdMatched){
					orgPid = noDeptOrgId;
				} else {
					orgPid = firstOrgId;
				}
			}
			
			orgArr[Oidx.orgPid] = orgPid;
			orOrgBVo.setOrgPid(orgPid);
			
			// 정렬순서 변환 후 세팅
			hash = CRC32.hash(orgPid.getBytes("UTF8"));
			sortOrdr = sortOrdrByOrgIdMap.get(hash);
			if(sortOrdr==null) sortOrdr = 1;
			else sortOrdr++;
			orOrgBVo.setSortOrdr(sortOrdr.toString());
			
			sortOrdrByOrgIdMap.put(hash, sortOrdr);
		}


		//////////////////////////////////
		//
		//  3. 파트의 경우 부서정보 세팅
		
		for(i=0;i<orgSize;i++){
			
			orOrgBVo = orOrgBVoList.get(i);
			
			// 파트가 아니면 생략
			if(!"P".equals(orOrgBVo.getOrgTypCd())) continue;
			
			// rid 로 해당 데이터 조회
			rid = orOrgBVo.getRid();
			hash = CRC32.hash(rid.getBytes("UTF8"));
			orgArr = orgByRidMap.get(hash);
			if(orgArr==null){
				// 이경우 발생 안하지만 - safe code
				orOrgBVo.setOrgTypCd("D");
				orOrgBVo.setDeptId(orOrgBVo.getOrgId());
				orOrgBVo.setDeptRescId(orOrgBVo.getRescId());
				continue;
			}
			
			prid = orgArr[Oidx.prid];
			String oldPrid = null;
			while(true){
				hash = CRC32.hash(prid.getBytes("UTF8"));
				pOrgArr = orgByRidMap.get(hash);
				
				if(pOrgArr == null || prid.equals(oldPrid)){
					// 부모가 없음 - 상위 부서가 없는 파트의 경우
					// 임시코드 - 자신을 부서로 변경하고 자신의 정보를 넣음
					orOrgBVo.setOrgTypCd("D");
					orOrgBVo.setDeptId(orOrgBVo.getOrgId());
					orOrgBVo.setDeptRescId(orOrgBVo.getRescId());
					break;
				} else if("P".equals(pOrgArr[Oidx.orgTypCd])){
					oldPrid = prid;
					// 부모도 파트면 다시 상위를 찾음
					prid = pOrgArr[Oidx.prid];
				} else {
					// 파트가 아닌 부모를 찾음
					orOrgBVo.setDeptId(pOrgArr[Oidx.orgId]);
					orOrgBVo.setDeptRescId(pOrgArr[Oidx.rescId]);
					
					orgArr[Oidx.deptId] = pOrgArr[Oidx.orgId];
					break;
				}
			}
		}
		
		//////////////////////////////////
		//
		//  4. 삭제 정보 처리
		
		boolean shouldDel = "del".equals(orDbIntgDVo.getOrgDelCd());
		boolean shouldMove = delOrgMoveOrgIdMatched && "move".equals(orDbIntgDVo.getOrgDelCd());
		boolean isRoot = false;
		
		OrOrgBVo modOrOrgBVo;
		OrOrgCntcDVo orOrgCntcDVo;
		OrOfseDVo orOfseDVo;
		OrOrgApvDVo orOrgApvDVo;
		
		int delSize = delOrgIdList.size();
		
		boolean noDelete;
		for(i=0; i<delSize; i++){
			
			hash = delOrgIdList.get(i);
			orgArr = allOrgByOrgIdMap.get(hash);
			if(orgArr==null) continue;
			
			orgId = orgArr[Oidx.orgId];
			
			// 삭제 예외 확인
			noDelete = false;
			if(!noDelete && noDeptOrgIdMatched && noDeptOrgId.equals(orgId)){
				noDelete = true;
			}
			if(!noDelete && delOrgMoveOrgIdMatched && delOrgMoveOrgId.equals(orgId)){
				noDelete = true;
			}
			if(!noDelete && delUserMoveOrgIdMatched && delUserMoveOrgId.equals(orgId)){
				noDelete = true;
			}
			if(!noDelete && "Y".equals(orgArr[Oidx.sysOrgYn])){
				noDelete = true;
			}
			// 루트 경로 여부
			isRoot = "ROOT".equals(orgArr[Oidx.orgId]);
			
			// 데이터 삭제의 경우
			if(!noDelete && !isRoot && shouldDel){
				
				orRescBVo = new OrRescBVo();
				orRescBVo.setRescId(orgArr[Oidx.rescId]);
				queryQueue.delete(orRescBVo);
				
				orOrgCntcDVo = new OrOrgCntcDVo();
				orOrgCntcDVo.setOrgId(orgId);
				queryQueue.delete(orOrgCntcDVo);
				
				orOfseDVo = new OrOfseDVo();
				orOfseDVo.setOrgId(orgId);
				queryQueue.delete(orOfseDVo);
				
				orOrgApvDVo = new OrOrgApvDVo();
				orOrgApvDVo.setOrgId(orgId);
				queryQueue.delete(orOrgApvDVo);
				
				modOrOrgBVo = new OrOrgBVo();
				modOrOrgBVo.setOrgId(orgId);
				queryQueue.delete(modOrOrgBVo);
				
			// 데이터 삭제가 아닌 경우
			} else {
				
				// 삭제 상태로 변경할 부서의 [부모 조직 ID] 찾기
				//  - 현재 부서에 있는 경우 - 현재 부서가 유효하지 않으면 옮길 부서를 지정함
				//  - 부서를 옮기는 경우 - 해당 옮겨갈 부서 지정
				
				// 현재 부서에 그대로 있는 경우
				if(isRoot || !shouldMove){
					// 부모 조직ID
					orgPid = orgArr[Oidx.orgPid];
					
				// 이동의 경우 - 삭제 하지 않을 부서
				} else if(noDelete){
					// 부모 조직ID
					orgPid = orgArr[Oidx.orgPid];
					
					// 부모 조직 정보 찾기
					hash = CRC32.hash(orgPid.getBytes("UTF8"));
					pOrgArr = orgByOrgIdMap.get(hash);
					
					if(pOrgArr==null){
						// 동기화된 데이터가 아니거나(prid 가 없으면 동기화된 데이터 아님) 시스템 조직(동기화 제외)이 아닌 경우
						if(noDeptOrgIdMatched){
							orgPid = noDeptOrgId;// "부서 없음" 부서 밑으로
						} else {
							orgPid = firstOrgId;// "부서 없음" 부서가 없으면 - 첫번째 부서 밑으로
						}
					}
					
				// 이동의 경우
				} else {
					orgPid = delOrgMoveOrgId;
				}
				
				hash = CRC32.hash(orgPid.getBytes("UTF8"));
				sortOrdr = sortOrdrByOrgIdMap.get(hash);
				if(sortOrdr==null) sortOrdr = 0;
				else sortOrdr++;
				sortOrdrByOrgIdMap.put(hash, sortOrdr);
				
				modOrOrgBVo = new OrOrgBVo();
				modOrOrgBVo.setOrgId(orgId);
				modOrOrgBVo.setOrgPid(orgPid);
				modOrOrgBVo.setSortOrdr(sortOrdr.toString());
				if(!noDelete) modOrOrgBVo.setUseYn("N");
				queryQueue.update(modOrOrgBVo);
				
				// 사용자 동기화에서 참조하도록 맵에 담음
				if(noDelete){
					hash = CRC32.hash(orgId.getBytes("UTF8"));
					orgByOrgIdMap.put(hash, orgArr);
					
					rid = orgArr[Oidx.rid];
					if(rid!=null && !rid.isEmpty()){
						hash = CRC32.hash(rid.getBytes("UTF8"));
						orgByRidMap.put(hash, orgArr);
						allOrgByOrgIdMap.put(hash, orgArr);
					}
				}
			}
		}
		
		commonSvc.execute(queryQueue);
	}

	
	/** 사용자 동기화
	 *  - 데이터는 정렬순서데로 소트 되었다고 가정함
	 *  */
	private void syncUser(OrDbIntgDVo orDbIntgDVo, SimpleQuery query,
			List<String> langList,
			Map<Integer, String[]> orgByOrgIdMap,
			Map<Integer, String[]> orgByRidMap,
			Map<Integer, String[]> allOrgByOrgIdMap,
			List<Integer> codeHashList
			) throws SQLException, IOException, CmException {
		
		boolean noCdSync = false;
		String cdSql = orDbIntgDVo.getCdSql();
		if(cdSql==null || cdSql.isEmpty()){
			noCdSync = true;
		}
		String orgSql = orDbIntgDVo.getOrgSql();
		if(orgSql==null || orgSql.isEmpty()) return;
		
		String userSql = orDbIntgDVo.getUserSql();
		if(userSql==null || userSql.isEmpty()) return;
		
		// 원직자용
		Map<Integer, String[]> odurByOdurUidMap = new HashMap<Integer, String[]>();
		Map<Integer, String[]> odurByRidMap = new HashMap<Integer, String[]>();
		Map<Integer, String[]> odurByEinMap = new HashMap<Integer, String[]>();
		Map<Integer, String[]> odurByLginIdMap = new HashMap<Integer, String[]>();
		// 원직자 삭제 검토용
		List<Integer> odurDelList = new ArrayList<Integer>();
		
		// 겸직자용
		Map<Integer, String[]> adurByUserUidMap = new HashMap<Integer, String[]>();
		Map<Integer, String[]> adurByRidMap = new HashMap<Integer, String[]>();
		Map<Integer, String[]> adurByEinOrgIdMap = new HashMap<Integer, String[]>();
		Map<Integer, String[]> adurByLginIdOrgIdMap = new HashMap<Integer, String[]>();
		// 겸직자 삭제 검토용
		List<Integer> adurDelList = new ArrayList<Integer>();
		
		boolean keepUserSortOrdr = OrDbSyncUtil.shouldKeepSortOrdr();
		// 정렬순서 - 사용자 추가모드일 일때
		Map<Integer, Integer> keepUserSortOrdrMap = keepUserSortOrdr ? null : new HashMap<Integer, Integer>();
		
		////////////////////////////////////////
		// 사용자 정보 DB 조회
		loadUser(orDbIntgDVo, langList, 
				odurByOdurUidMap, odurByRidMap, odurByEinMap, odurByLginIdMap, odurDelList,
				adurByUserUidMap, adurByRidMap, adurByEinOrgIdMap, adurByLginIdOrgIdMap, adurDelList, keepUserSortOrdrMap);
		
		int i, langSize = langList==null ? 0 : langList.size();
		// 속성 추출용 언어 코드 - 앞글자만 대문자
		List<String> langAttrList = Fnc.toAttrLangList(langList);
		String langTypCd, rescVa;
		
		
		QueryQueue queryQueue = new QueryQueue();
		SimpleIterator iterator = null;
		Map<String, String> userDataMap;
		
		int userCount = 0;
		boolean isNewUser;
		String[] dbUserArr, orgArr, pOrgArr;
		String lginId, aduTypCd;
		
		Integer hash;
		String odurUid, userUid, rescId, genCd, value, orgId, orgRid, cd;
		
		String noDeptOrgId = orDbIntgDVo.getNoDeptOrgId();
		String delUserMoveOrgId = orDbIntgDVo.getDelUserMoveOrgId();
		
		Map<Integer, Integer> sortOrdrMap = new HashMap<Integer, Integer>();
		Integer sortOrdr;
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo;
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo;
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블
		OrUserPwDVo orUserPwDVo;
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo;
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
		OrUserPinfoDVo orUserPinfoDVo;
		// 리소스기본(OR_RESC_B) 테이블
		OrRescBVo orRescBVo;
		// 원직자보안상세(OR_ODUR_SECU_D) 테이블
		OrOdurSecuDVo orOdurSecuDVo;
		// 사용자역할관계(OR_USER_ROLE_R) 테이블
		OrUserRoleRVo orUserRoleRVo;
		// 사용자비밀번호이력(OR_USER_PW_H) 테이블
		OrUserPwHVo orUserPwHVo;
		
		// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
		String[] userStatCds = {"01","02","03","04","05","11","99"};
		// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
		String[] aduTypCds = {"01","02","03"};
		// 동기화 코드
		String[][] syncCds = {
				{"seculCd","SECUL_CD"},
				{"gradeCd","GRADE_CD"},
				{"titleCd","TITLE_CD"},
				{"positCd","POSIT_CD"},
				{"dutyCd","DUTY_CD"}
		};
		// 비밀번호 - SYS:시스템 비밀번호, APV:결재 비밀번호
		String[][] pwCds = {
				{"loginPw","SYS"},
				{"apvPw","APV"}
		};
		// 개인정보 - 전화번호 류
		String[] psnInfoPhons = {
				"mbno","homePhon","homeFno","compPhon","compFno"
		};
		// 개인정보 - 기타(주소등)
		String[] psnInfoEtcs = {
				"email","extnEmail","homeZipNo","homeAdr","homeDetlAdr","compZipNo","compAdr","compDetlAdr","hpageUrl"
		};
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean codeByCompEnable = "Y".equals(sysPlocMap.get("codeByCompEnable"));
		boolean seculByCompEnable = "Y".equals(sysPlocMap.get("codeByCompEnable"));
		boolean isSeculCd, isCdByComp, isSysUser;
		String compId = orDbIntgDVo.getCompId(), refVa1, refVa2;
		
		boolean hasPsnInfo, isOriginUser;
		
		try {
			
			////////////////////////////////////////
			// 원직자 정보 조회 - 연계 정보 조회
			userSql = userSql.trim();
			if(userSql.endsWith(";")) userSql = userSql.substring(0, userSql.length()-1);
			iterator = query.queryIterator(userSql, null);
			iterator.setNullToEmpty(true);
			
			while((userDataMap = iterator.next()) != null){
				
				lginId = userDataMap.get("lginId");
				if(lginId==null || lginId.isEmpty() || lginId.trim().isEmpty()){
					LOGGER.error("[SYNC USER ERROR] NO lginId : "+userDataMap);
					continue;
				}
				orgRid = userDataMap.get("orgRid");
				if(orgRid==null || orgRid.isEmpty()){
					LOGGER.error("[SYNC USER ERROR] NO orgRid : "+userDataMap);
					continue;
				}
				
				// M:남성, F:여성
				genCd = userDataMap.get("genCd");
				if(!"F".equals(genCd)) genCd = "M";
//				if(genCd==null || genCd.isEmpty()){
//					LOGGER.error("[SYNC USER ERROR] NO genCd : "+userDataMap);
//					continue;
//				}
				
				// 원직, 겸직 구분
				aduTypCd = userDataMap.get("aduTypCd");
				if(!ArrayUtil.isInArray(aduTypCds, aduTypCd)) aduTypCd = "01";
				isOriginUser = "01".equals(aduTypCd);
				
				// 원직의 경우
				if(isOriginUser){

					orOdurBVo = new OrOdurBVo();
					orUserBVo = new OrUserBVo();
					
					// DB상의 기존 사용자 정보에 해당하는 사용자 있는지 확인
					dbUserArr = findOdur(userDataMap, odurByRidMap, odurByEinMap, odurByLginIdMap);
					
					// 신규 사용자의 경우
					if(dbUserArr == null){
						isNewUser = true;
						isSysUser = false;
						
						odurUid = orCmSvc.createId("OR_USER_B");
						userUid = odurUid;
						rescId  = orCmSvc.createId("OR_RESC_B");
						
						// 원직자
						orOdurBVo.setOdurUid(odurUid);
						orOdurBVo.setRescId(rescId);
						orOdurBVo.setApvPwTypCd("SYS");//결재비밀번호구분코드 - SYS:시스템 비밀번호, APV:결재 비밀번호
						orOdurBVo.setSysUserYn("N");
						orOdurBVo.setRegDt("sysdate");
						orOdurBVo.setModDt("sysdate");
						
						// 사용자
						orUserBVo.setUserUid(userUid);
						orUserBVo.setOdurUid(odurUid);
						orUserBVo.setRescId(rescId);
						orUserBVo.setAduTypCd("01");// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
						orUserBVo.setRegDt("sysdate");
						orUserBVo.setModDt("sysdate");
						
					// 구 사용자의 경우
					} else {
						
						isNewUser = false;
						isSysUser = "Y".equals(dbUserArr[Uidx.sysUserYn]);
						
						odurUid = dbUserArr[Uidx.odurUid];
						userUid = odurUid;
						rescId  = dbUserArr[Uidx.rescId];
						
						// 원직자
						orOdurBVo.setOdurUid(odurUid);
						orOdurBVo.setModDt("sysdate");
						
						// 사용자
						orUserBVo.setUserUid(userUid);
						orUserBVo.setAduTypCd("01");// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
						orUserBVo.setModDt("sysdate");
						
						// 삭제 정보에서 제거
						hash = CRC32.hash(odurUid.getBytes("UTF8"));
						odurDelList.remove((Object)hash);
						
					}
					
				// 겸직의 경우
				} else {
					
					orOdurBVo = null;
					orUserBVo = new OrUserBVo();
					
					// DB상의 기존 사용자 정보에 해당하는 사용자 있는지 확인 - 겸직자 정보
					dbUserArr = findAdur(userDataMap, adurByRidMap, adurByEinOrgIdMap, adurByLginIdOrgIdMap, orgByRidMap);
					
					// 기존의 겸직자를 못찾으면 원직자를 찾음
					if(dbUserArr == null){
						
						// DB상의 기존 사용자 정보에 해당하는 사용자 있는지 확인 - 원직자 정보
						dbUserArr = findOdur(userDataMap, odurByRidMap, odurByEinMap, odurByLginIdMap);
						
						// 원직자를 찾지 못하면 - 입력 안함
						if(dbUserArr == null){
							LOGGER.error("[SYNC USER ERROR] NO orgin user : "+userDataMap);
							continue;
						}
						
						isNewUser = true;
						isSysUser = false;
						
						userUid = orCmSvc.createId("OR_USER_B");
						odurUid = dbUserArr[Uidx.odurUid];
						rescId  = dbUserArr[Uidx.rescId];
						
						// 사용자(겸직자)
						orUserBVo.setUserUid(userUid);
						orUserBVo.setOdurUid(odurUid);
						orUserBVo.setRescId(rescId);
						orUserBVo.setAduTypCd(aduTypCd);// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
						orUserBVo.setRegDt("sysdate");
						orUserBVo.setModDt("sysdate");
						
					} else {// 겸직정보에 있으면
						
						isNewUser = false;
						isSysUser = "Y".equals(dbUserArr[Uidx.sysUserYn]);
						
						userUid = dbUserArr[Uidx.userUid];
						odurUid = dbUserArr[Uidx.odurUid];
						rescId  = dbUserArr[Uidx.rescId];
						
						// 사용자(겸직자)
						orUserBVo.setUserUid(userUid);
						orUserBVo.setAduTypCd(aduTypCd);// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
						orUserBVo.setModDt("sysdate");
						
						// 삭제 정보에서 제거
						hash = CRC32.hash(userUid.getBytes("UTF8"));
						adurDelList.remove((Object)hash);
					}
				}
				
				if(!isSysUser){
					
					if(isOriginUser){
						orOdurBVo.setLginId(lginId);//로그인ID 
						orOdurBVo.setGenCd(genCd);//성별코드 - M:남성, F:여성 
						orOdurBVo.setEin(userDataMap.get("ein"));//사원번호
					}
					
					// 사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
					value = userDataMap.get("userStatCd");
					if(!ArrayUtil.isInArray(userStatCds, value)) value = "02";
					// 원직 - 상태
					if(isOriginUser) orOdurBVo.setUserStatCd(value);
					// 사용자 - 상태
					orUserBVo.setUserStatCd(value);
					
					refVa1 = userDataMap.get("refVa1");
					refVa2 = userDataMap.get("refVa2");
					
					// 원직자면
					if(isOriginUser){
						
						// 입사일, 퇴사일
						orOdurBVo.setEntraYmd(Fnc.toYmdFormat(userDataMap.get("entraYmd")));//입사년월일
						orOdurBVo.setResigYmd(Fnc.toYmdFormat(userDataMap.get("resigYmd")));//퇴사년월일
						
						// 리소스 처리
						for(i=0;i<langSize;i++){
							langTypCd = langList.get(i);
							rescVa = userDataMap.get("user"+langAttrList.get(i)+"Nm");
							if(rescVa==null && "ko".equals(langTypCd)){
								rescVa = userDataMap.get("userNm");
							}
							
							if(rescVa!=null && !rescVa.isEmpty()){
								
								if(orOdurBVo.getUserNm()==null || "ko".equals(langTypCd)){
									orOdurBVo.setUserNm(rescVa);
									orUserBVo.setUserNm(rescVa);
								}
								
								orRescBVo = new OrRescBVo();
								orRescBVo.setRescId(rescId);
								orRescBVo.setLangTypCd(langTypCd);
								orRescBVo.setRescVa(rescVa);
								
								if(isNewUser){
									queryQueue.insert(orRescBVo);
								} else {
									queryQueue.store(orRescBVo);
								}
							}
						}
						orOdurBVo.setRefVa1(refVa1==null ? "" : refVa1);
						orOdurBVo.setRefVa2(refVa2);
						
						orUserBVo.setRefVa1(refVa1==null ? "" : refVa1);
						orUserBVo.setRefVa2(refVa2);
					} else {
						orUserBVo.setRefVa1(refVa1==null ? "" : refVa1);
						orUserBVo.setRefVa2(refVa2);
					}
				}
				
				
				// 회사
				orUserBVo.setCompId(orDbIntgDVo.getCompId());
				
				// 조직
				hash = CRC32.hash(orgRid.getBytes("UTF8"));
				orgArr = orgByRidMap.get(hash);
				if(orgArr == null){
					orgId = noDeptOrgId;
					hash = CRC32.hash(orgId.getBytes("UTF8"));
					orgArr = orgByOrgIdMap.get(hash);
				}
				
				if(orgArr==null){
					LOGGER.error("[SYNC USER ERROR] NO Org by orgRid : "+orgRid+"  "+userDataMap);
					continue;
				}
				
				orgId = orgArr[Oidx.orgId];
				// 조직 / 부서
				orUserBVo.setOrgId(orgArr[Oidx.orgId]);
				orUserBVo.setOrgRescId(orgArr[Oidx.rescId]);
				if(!"P".equals(orgArr[Oidx.orgTypCd])){
					orUserBVo.setDeptId(orgArr[Oidx.orgId]);
					orUserBVo.setDeptRescId(orgArr[Oidx.rescId]);
				} else {
					hash = CRC32.hash(orgArr[Oidx.deptId].getBytes("UTF8"));
					pOrgArr = orgByOrgIdMap.get(hash);
					orUserBVo.setDeptId(pOrgArr[Oidx.orgId]);
					orUserBVo.setDeptRescId(pOrgArr[Oidx.rescId]);
				}
				
				// 정렬순서 - 정렬순서 유지가 아니면
				if(!keepUserSortOrdr){
					hash = CRC32.hash(orgId.getBytes("UTF8"));
					sortOrdr = sortOrdrMap.get(hash);
					if(sortOrdr==null) sortOrdr = 1;
					else sortOrdr++;
					sortOrdrMap.put(hash, sortOrdr);
					orUserBVo.setSortOrdr(sortOrdr.toString());
				}
				
				if(!isSysUser){
					// 코드
					for(String[] syncCd : syncCds){
						cd = userDataMap.get(syncCd[0]);
						if(cd==null) continue;
						
						if(!cd.isEmpty()){
							
							if(noCdSync || codeHashList.contains(CRC32.hash((syncCd[1]+cd).getBytes("UTF8")))){
								
								isSeculCd  = "SECUL_CD".equals(syncCd[1]);
								isCdByComp = (seculByCompEnable &&  isSeculCd) || (codeByCompEnable  && !isSeculCd);
								
								if(isCdByComp) cd = compId+"_"+cd;
							} else {
								cd = "";
								LOGGER.error("[SYNC USER WARN] "+syncCd[1]+" code removed : "+userDataMap);
							}
						}
						VoUtil.setValue(orUserBVo, syncCd[0], cd);
					}
					
					// 담당업무
					value = userDataMap.get("tichCont");
					orUserBVo.setTichCont(value);
				}
				
				if(isNewUser){
					if(keepUserSortOrdr){
						hash = CRC32.hash(orgId.getBytes("UTF8"));
						sortOrdr = sortOrdrMap.get(hash);
						if(sortOrdr==null) sortOrdr = 1;
						else sortOrdr++;
						sortOrdrMap.put(hash, sortOrdr);
						orUserBVo.setSortOrdr(sortOrdr.toString());
					}
					
					if(isOriginUser) queryQueue.insert(orOdurBVo);
					queryQueue.insert(orUserBVo);
				} else {
					if(isOriginUser) queryQueue.store(orOdurBVo);
					queryQueue.store(orUserBVo);
				}
				
				if(isNewUser && isOriginUser){
					
					// 로그인 / 결재 - 비밀번호
					for(String[] pwCd : pwCds){
						value = userDataMap.get(pwCd[0]);
						if(value!=null && !value.isEmpty()){
							orUserPwDVo = new OrUserPwDVo();
							orUserPwDVo.setOdurUid(odurUid);
							orUserPwDVo.setPwTypCd(pwCd[1]);//비밀번호구분코드 - KEY - SYS:시스템 비밀번호, APV:결재 비밀번호
							orUserPwDVo.setPwEnc(cryptoSvc.encryptPw(value.trim(), odurUid));
							orUserPwDVo.setModDt("sysdate");
							queryQueue.insert(orUserPwDVo);
						}
					}
				}
				
				if(isOriginUser && !isSysUser){
					
					// 개인정보
					hasPsnInfo = false;
					orUserPinfoDVo = new OrUserPinfoDVo();
					orUserPinfoDVo.setOdurUid(odurUid);
					// 개인정보 - 전화번호 류
					for(String phoneNm : psnInfoPhons){
						value = userDataMap.get(phoneNm);
						if(value!=null){
							value = Fnc.toPhone(value);
							if(!value.isEmpty()) value = cryptoSvc.encryptPersanal(value);
							VoUtil.setValue(orUserPinfoDVo, phoneNm+"Enc", value);
							hasPsnInfo = true;
						}
					}
					// 개인정보 - 기타(주소등)
					for(String infoNm : psnInfoEtcs){
						value = userDataMap.get(infoNm);
						if(value!=null){
							if(!value.isEmpty()) value = cryptoSvc.encryptPersanal(value);
							VoUtil.setValue(orUserPinfoDVo, infoNm+"Enc", value);
							hasPsnInfo = true;
						}
					}
					if(hasPsnInfo){
						if(isNewUser){
							queryQueue.insert(orUserPinfoDVo);
						} else {
							queryQueue.store(orUserPinfoDVo);
						}
					}
					
					// 사진경로
					value = userDataMap.get("photoPath");
					if(value!=null && !value.isEmpty()){
						orUserImgDVo = new OrUserImgDVo();
						orUserImgDVo.setUserUid(odurUid);
						orUserImgDVo.setImgPath(value);
						orUserImgDVo.setUserImgTypCd("03");
						orUserImgDVo.setModDt("sysdate");
						orUserImgDVo.setImgWdth("");
						orUserImgDVo.setImgHght("");
						if(isNewUser){
							queryQueue.insert(orUserImgDVo);
						} else {
							queryQueue.store(orUserImgDVo);
						}
					}
					
					// 메일사용여부
					value = userDataMap.get("useMailYn");
					if(value!=null){
						orOdurSecuDVo = new OrOdurSecuDVo();
						if("N".equals(value)){
							orOdurSecuDVo.setOdurUid(odurUid);
							orOdurSecuDVo.setSecuId("useMailYn");
							orOdurSecuDVo.setSecuVa("N");
							queryQueue.store(orOdurSecuDVo);
						} else {
							orOdurSecuDVo.setOdurUid(odurUid);
							orOdurSecuDVo.setSecuId("useMailYn");
							queryQueue.delete(orOdurSecuDVo);
						}
					}
					// 메신저사용여부
					value = userDataMap.get("useMsgrYn");
					if(value!=null){
						orOdurSecuDVo = new OrOdurSecuDVo();
						if("N".equals(value)){
							orOdurSecuDVo.setOdurUid(odurUid);
							orOdurSecuDVo.setSecuId("useMsgrYn");
							orOdurSecuDVo.setSecuVa("N");
							queryQueue.store(orOdurSecuDVo);
						} else {
							orOdurSecuDVo.setOdurUid(odurUid);
							orOdurSecuDVo.setSecuId("useMsgrYn");
							queryQueue.delete(orOdurSecuDVo);
						}
					}
				}
				
				if(++userCount % 100 == 0){
					commonSvc.execute(queryQueue);
					queryQueue = new QueryQueue();
				}
			}
			
			// 사용자 나머지 실행
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				queryQueue = new QueryQueue();
			}
			
			///////////////////////////////////
			// 삭제 처리
			
			boolean noDelete;
			boolean shouldDel = "del".equals(orDbIntgDVo.getUserDelCd());
			boolean shouldMov = delUserMoveOrgId!=null && "move".equals(orDbIntgDVo.getUserDelCd());
			String deptId;
			
			List<Integer> delList;
			Map<Integer, String[]> userMap;
			
			for(i=0;i<2;i++){
				
				if(i==0){
					delList = odurDelList;
					userMap = odurByOdurUidMap;
				} else {
					delList = adurDelList;
					userMap = adurByUserUidMap;
				}
				
				for(Integer odurHash : delList){
					
					dbUserArr = userMap.get(odurHash);
					if(dbUserArr==null) continue;
					
					noDelete = "Y".equals(dbUserArr[Uidx.sysUserYn]);
					
					if(!noDelete && shouldDel){// 데이터 지움
						
						// 원직자면
						if("01".equals(dbUserArr[Uidx.aduTypCd])){
							
							odurUid = dbUserArr[Uidx.odurUid];
							userUid = odurUid;
							
							if(userUid!=null && !userUid.isEmpty()){
								
								// 사용자기본(OR_USER_B) 테이블
								orUserBVo = new OrUserBVo();
								orUserBVo.setUserUid(userUid);
								queryQueue.delete(orUserBVo);
								
								// 사용자역할관계(OR_USER_ROLE_R) 테이블
								orUserRoleRVo = new OrUserRoleRVo();
								orUserRoleRVo.setUserUid(userUid);
								queryQueue.delete(orUserBVo);
								
								// 사용자이미지상세(OR_USER_IMG_D) 테이블
								orUserImgDVo = new OrUserImgDVo();
								orUserImgDVo.setUserUid(userUid);
								queryQueue.delete(orUserBVo);
								
								
								// 원직자기본(OR_ODUR_B) 테이블
								orOdurBVo = new OrOdurBVo();
								orOdurBVo.setOdurUid(odurUid);
								queryQueue.delete(orOdurBVo);
								
								// 사용자비밀번호상세(OR_USER_PW_D) 테이블
								orUserPwDVo = new OrUserPwDVo();
								orUserPwDVo.setOdurUid(odurUid);
								queryQueue.delete(orUserPwDVo);
								
								// 사용자비밀번호이력(OR_USER_PW_H) 테이블
								orUserPwHVo = new OrUserPwHVo();
								orUserPwHVo.setOdurUid(odurUid);
								queryQueue.delete(orUserPwHVo);
								
								// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
								orUserPinfoDVo = new OrUserPinfoDVo();
								orUserPinfoDVo.setOdurUid(odurUid);
								queryQueue.delete(orUserPinfoDVo);
								
								rescId = dbUserArr[Uidx.rescId];
								if(rescId!=null && !rescId.isEmpty()){
									// 리소스기본(OR_RESC_B) 테이블
									orRescBVo = new OrRescBVo();
									orRescBVo.setRescId(odurUid);
									queryQueue.delete(orRescBVo);
								}
								
								// 원직자보안상세(OR_ODUR_SECU_D) 테이블
								orOdurSecuDVo = new OrOdurSecuDVo();
								orOdurSecuDVo.setOdurUid(odurUid);
								queryQueue.delete(orOdurSecuDVo);
							}
							
						// 겸직자면
						} else {
							
							userUid = dbUserArr[Uidx.userUid];
							if(userUid!=null && !userUid.isEmpty()){
								
								// 사용자기본(OR_USER_B) 테이블
								orUserBVo = new OrUserBVo();
								orUserBVo.setUserUid(userUid);
								queryQueue.delete(orUserBVo);
								
								// 사용자역할관계(OR_USER_ROLE_R) 테이블
								orUserRoleRVo = new OrUserRoleRVo();
								orUserRoleRVo.setUserUid(userUid);
								queryQueue.delete(orUserBVo);
								
								// 사용자이미지상세(OR_USER_IMG_D) 테이블
								orUserImgDVo = new OrUserImgDVo();
								orUserImgDVo.setUserUid(userUid);
								queryQueue.delete(orUserBVo);
								
							}
						}
						
					// 데이터 안지움
					} else {
						
						// 원직 - 삭제상태
						if("01".equals(dbUserArr[Uidx.aduTypCd]) && !noDelete){
							orOdurBVo = new OrOdurBVo();
							orOdurBVo.setOdurUid(dbUserArr[Uidx.odurUid]);
							orOdurBVo.setUserStatCd("99");//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
							queryQueue.update(orOdurBVo);
						}
						
						// 사용자 - 삭제상태
						orUserBVo = new OrUserBVo();
						orUserBVo.setUserUid(dbUserArr[Uidx.userUid]);
						if(!noDelete){
							orUserBVo.setUserStatCd("99");//사용자상태코드 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 11:해제, 99:삭제
						}
						
						// 데이터 삭제상태 삭제 부서로 이동
						if(shouldMov){
							// 삭제 처리 후 이동 부서가 없으면 - 부서 없음 으로
							if(delUserMoveOrgId != null){
								orgId = delUserMoveOrgId;
							} else {
								orgId = noDeptOrgId;
							}
//							if(keepUserSortOrdr){
//								// 부서가 바뀌었으면
//								if(!orgId.equals(dbUserArr[Uidx.orgId])){
//									// 정렬순서
//									hash = CRC32.hash(orgId.getBytes("UTF8"));
//									sortOrdr = keepUserSortOrdrMap.get(hash);
//									if(sortOrdr==null) sortOrdr = 1;
//									else sortOrdr++;
//									keepUserSortOrdrMap.put(hash, sortOrdr);
//									orUserBVo.setSortOrdr(sortOrdr.toString());
//								}
//							}
							
						// 현재 부서에서 삭제 상태만
						} else {
							// DB의 orgId
							orgId = dbUserArr[Uidx.orgId];
						}
						
						hash = CRC32.hash(orgId.getBytes("UTF8"));
						orgArr = orgByOrgIdMap.get(hash);
						
						// 조직이 없으면 - 부서없음 으로
						if(orgArr == null){
							orgId = noDeptOrgId;
							hash = CRC32.hash(orgId.getBytes("UTF8"));
							orgArr = orgByOrgIdMap.get(hash);
						}
						
						// 조직 정보
						orUserBVo.setOrgId(orgArr[Oidx.orgId]);
						orUserBVo.setOrgRescId(orgArr[Oidx.rescId]);
						if(shouldMov){
							if(keepUserSortOrdr){
								if(!orgArr[Oidx.orgId].equals(dbUserArr[Uidx.orgId])){
									// 정렬순서
									hash = CRC32.hash(orgId.getBytes("UTF8"));
									sortOrdr = keepUserSortOrdrMap.get(hash);
									if(sortOrdr==null) sortOrdr = 1;
									else sortOrdr++;
									keepUserSortOrdrMap.put(hash, sortOrdr);
									orUserBVo.setSortOrdr(sortOrdr.toString());
								}
							}
						}
						
						// 파트가 아니면 부서정보
						if(!"P".equals(orgArr[Oidx.orgTypCd])){
							orUserBVo.setDeptId(orgArr[Oidx.orgId]);
							orUserBVo.setDeptRescId(orgArr[Oidx.rescId]);
						} else {
							// 파트일 경우 부서정보
							deptId = orgArr[Oidx.deptId];
							if(deptId != null && !deptId.isEmpty()){
								hash = CRC32.hash(deptId.getBytes("UTF8"));
								pOrgArr = allOrgByOrgIdMap.get(hash);
							} else {
								pOrgArr = null;
							}
							if(pOrgArr != null){
								orUserBVo.setDeptId(pOrgArr[Oidx.orgId]);
								orUserBVo.setDeptRescId(pOrgArr[Oidx.rescId]);
							} else {
								// 삭제된 사용자의 부서정보 - 중요치 않음
								orUserBVo.setDeptId(orgArr[Oidx.orgId]);
								orUserBVo.setDeptRescId(orgArr[Oidx.rescId]);
							}
						}
						
						if(!keepUserSortOrdr){
							// 정렬순서
							hash = CRC32.hash(orgId.getBytes("UTF8"));
							sortOrdr = sortOrdrMap.get(hash);
							if(sortOrdr==null) sortOrdr = 1;
							else sortOrdr++;
							sortOrdrMap.put(hash, sortOrdr);
							orUserBVo.setSortOrdr(sortOrdr.toString());
						}
						
						queryQueue.update(orUserBVo);
					}
				}
				
			}
			
			
			if(!queryQueue.isEmpty()){
				commonSvc.execute(queryQueue);
				queryQueue = new QueryQueue();
			}
			
		} finally {
			if(iterator != null){
				iterator.close();
				iterator = null;
			}
		}
		
	}
	
	/** 겸직자 정보 찾기 */
	private String[] findAdur(Map<String, String> userDataMap,
			Map<Integer, String[]> adurByRidMap,
			Map<Integer, String[]> adurByEinOrgIdMap,
			Map<Integer, String[]> adurByLginIdOrgIdMap,
			Map<Integer, String[]> orgByRidMap) throws IOException{
		
		String[] dbUserArr = null;
		
		// 참조ID : rid 매핑
		String rid = userDataMap.get("rid");
		if(rid!=null && !rid.isEmpty()){
			dbUserArr = adurByRidMap.get(CRC32.hash(rid.getBytes("UTF8")));
			if(dbUserArr != null) return dbUserArr;
		}
		
		// 조직ID 찾기 - orgId
		String orgRid = userDataMap.get("orgRid");
		if(orgRid==null) return null;
		String[] orgArr = orgByRidMap.get(CRC32.hash(orgRid.getBytes("UTF8")));
		if(orgArr==null) return null;
		String orgId = orgArr[Oidx.orgId];
		
		// 사번+조직ID 찾기
		String ein = userDataMap.get("ein");
		if(ein!=null && !ein.isEmpty()){
			dbUserArr = adurByEinOrgIdMap.get(CRC32.hash((ein+orgId).getBytes("UTF8")));
			if(dbUserArr != null) return dbUserArr;
		}
		
		// 로그인ID+조직ID 찾기
		String lginId = userDataMap.get("lginId");
		if(lginId!=null && !lginId.isEmpty()){
			dbUserArr = adurByLginIdOrgIdMap.get(CRC32.hash((lginId+orgId).getBytes("UTF8")));
			if(dbUserArr != null) return dbUserArr;
		}
		
		return null;
	}
	
	/** 원직자 정보 찾기 */
	private String[] findOdur(Map<String, String> userDataMap,
			Map<Integer, String[]> odurByRidMap,
			Map<Integer, String[]> odurByEinMap,
			Map<Integer, String[]> odurByLginIdMap) throws IOException{
		
		String[] dbUserArr = null;
		
		String rid = userDataMap.get("rid");
		if(rid!=null && !rid.isEmpty()){
			dbUserArr = odurByRidMap.get(CRC32.hash(rid.getBytes("UTF8")));
			if(dbUserArr != null) return dbUserArr;
		}
		
		String ein = userDataMap.get("ein");
		if(ein!=null && !ein.isEmpty()){
			dbUserArr = odurByEinMap.get(CRC32.hash(ein.getBytes("UTF8")));
			if(dbUserArr != null) return dbUserArr;
		}
		
		String lginId = userDataMap.get("lginId");
		if(lginId!=null && !lginId.isEmpty()){
			dbUserArr = odurByLginIdMap.get(CRC32.hash(lginId.getBytes("UTF8")));
			if(dbUserArr != null) return dbUserArr;
		}
		
		return null;
	}

	/** 사용자 정보 로드 */
	private void loadUser(OrDbIntgDVo orDbIntgDVo, List<String> langList,
			Map<Integer, String[]> odurByOdurUidMap,
			Map<Integer, String[]> odurByRidMap,
			Map<Integer, String[]> odurByEinMap,
			Map<Integer, String[]> odurByLginIdMap, List<Integer> odurDelList,
			Map<Integer, String[]> adurByUserUidMap,
			Map<Integer, String[]> adurByRidMap,
			Map<Integer, String[]> adurByEinOrgIdMap,
			Map<Integer, String[]> adurByLginIdOrgIdMap, List<Integer> adurDelList,
			Map<Integer, Integer> keepUserSortOrdrMap
			) throws SQLException, IOException{
		
		String firstLangTypCd = langList==null || langList.isEmpty() ? "ko" : langList.get(0);
		String[] odurArr, adurArr;
		Integer hash;
		
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setQueryLang(firstLangTypCd);
		@SuppressWarnings("unchecked")
		List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonSvc.queryList(orOdurBVo);
		if(orOdurBVoList != null){
			for(OrOdurBVo storedOrOdurBVo : orOdurBVoList){
				// 배열 Index
				// odurUid=0, userUid=1,    rid=2, ein=3, lginId=4, orgId=5,    rescId=6, sysUserYn=7;
				odurArr = new String[]{
						storedOrOdurBVo.getOdurUid(), storedOrOdurBVo.getOdurUid(),
						storedOrOdurBVo.getRid(), storedOrOdurBVo.getEin(), storedOrOdurBVo.getLginId(), null,
						storedOrOdurBVo.getRescId(), null, storedOrOdurBVo.getSysUserYn()
				};
				
				// 원직자 구분용
				odurByOdurUidMap.put(CRC32.hash(odurArr[Uidx.odurUid].getBytes("UTF8")), odurArr);
				
				if(odurArr[Uidx.rid]!=null && !odurArr[Uidx.rid].isEmpty()){
					odurByRidMap.put(CRC32.hash(odurArr[Uidx.rid].getBytes("UTF8")), odurArr);
				}
				if(odurArr[Uidx.ein]!=null && !odurArr[Uidx.ein].isEmpty()){
					odurByEinMap.put(CRC32.hash(odurArr[Uidx.ein].getBytes("UTF8")), odurArr);
				}
				if(odurArr[Uidx.lginId]!=null && !odurArr[Uidx.lginId].isEmpty()){
					odurByLginIdMap.put(CRC32.hash(odurArr[Uidx.lginId].getBytes("UTF8")), odurArr);
				}
			}
		}
		// 메모리 모지랄 것 대비 - 클린
		orOdurBVoList = null;
		
		String compId = orDbIntgDVo.getCompId();
		
		Integer orgIdHash = null;
		String orgId=null, oldOrgId=null;
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setQueryLang(firstLangTypCd);
		orUserBVo.setOrderBy("ORG_ID, SORT_ORDR");
//		orUserBVo.setCompId(orDbIntgDVo.getCompId());
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
		if(orUserBVoList != null){
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				
				hash = CRC32.hash(storedOrUserBVo.getOdurUid().getBytes("UTF8"));
				odurArr = odurByOdurUidMap.get(hash);
				if(odurArr==null) continue;
				
				// 겸직구분코드 - 01:원직, 02:겸직, 03:파견직
				if("01".equals(storedOrUserBVo.getAduTypCd())){
					adurArr = odurArr;
					adurArr[Uidx.orgId] = storedOrUserBVo.getOrgId();
					adurArr[Uidx.aduTypCd] = storedOrUserBVo.getAduTypCd();
					
					// 삭제 처리용
					if(compId.equals(storedOrUserBVo.getCompId())){
						odurDelList.add(CRC32.hash(adurArr[Uidx.userUid].getBytes("UTF8")));
					}
					
				} else {

					// 배열 Index
					// odurUid=0, userUid=1,
					// rid=2, ein=3, lginId=4, orgId=5,
					// rescId=6, sysUserYn=7, sysUserYn=8;
					adurArr = new String[]{
							odurArr[Uidx.odurUid], storedOrUserBVo.getUserUid(),
							storedOrUserBVo.getRid(), odurArr[Uidx.ein], odurArr[Uidx.lginId], storedOrUserBVo.getOrgId(), 
							storedOrUserBVo.getRescId(), storedOrUserBVo.getAduTypCd(), storedOrUserBVo.getSysUserYn()
					};
					
					// 겸직자 구분용
					adurByUserUidMap.put(CRC32.hash(adurArr[Uidx.userUid].getBytes("UTF8")), adurArr);
					
					if(adurArr[Uidx.rid]!=null && !adurArr[Uidx.rid].isEmpty()){
						adurByRidMap.put(CRC32.hash(adurArr[Uidx.rid].getBytes("UTF8")), adurArr);
					}
					if(adurArr[Uidx.ein]!=null && !adurArr[Uidx.ein].isEmpty()){
						adurByEinOrgIdMap.put(CRC32.hash((adurArr[Uidx.ein]+adurArr[Uidx.orgId]).getBytes("UTF8")), adurArr);
					}
					if(adurArr[Uidx.lginId]!=null && !adurArr[Uidx.lginId].isEmpty()){
						adurByLginIdOrgIdMap.put(CRC32.hash((adurArr[Uidx.lginId]+adurArr[Uidx.orgId]).getBytes("UTF8")), adurArr);
					}
					
					// 삭제 처리용
					if(compId.equals(storedOrUserBVo.getCompId())){
						adurDelList.add(CRC32.hash(adurArr[Uidx.userUid].getBytes("UTF8")));
					}
				}
				
				if(keepUserSortOrdrMap != null){
					orgId = storedOrUserBVo.getOrgId();
					if(oldOrgId==null || !orgId.equals(oldOrgId)){
						orgIdHash = CRC32.hash(orgId.getBytes("UTF8"));
					}
					keepUserSortOrdrMap.put(orgIdHash, Integer.valueOf(storedOrUserBVo.getSortOrdr()));
				}
			}
		}
		orUserBVoList = null;
	}
	
	/** 회사별 언어목록 조회 */
	private List<String> getLangs(String compId) throws SQLException {
		List<PtCdBVo> cdList = ptCmSvc.getLangTypCdListByCompId(compId, "ko");
		List<String> returnList = new ArrayList<String>();
		
		if(cdList != null){
			for(PtCdBVo storedPtCdBVo : cdList){
				returnList.add(storedPtCdBVo.getCd());
			}
		} else {
			returnList.add("ko");
		}
		return returnList;
	}
	
	/** DB 연결 설정 정보 테스트 */
	public void testConnection(OrDbIntgDVo orDbIntgDVo) throws SQLException, IOException  {
		SimpleQuery query = null;
		try {
			query = SimpleQuery.create(DATASOURCE_NAME, Fnc.createConfigMap(orDbIntgDVo), false);
			query.queryMap(orDbIntgDVo.getDbTestSql(), null);
		} finally {
			if(query!=null) query.close();
		}
	}

	// 조직 정보 배열의 인덱스
	private static class Oidx {
		private final static int
			orgId=0, orgPid=1, rescId=2, orgTypCd=3, deptId=4, sysOrgYn=5, rid=6, prid=7;
	}
	// 사용자 정보 배열의 인덱스
	private static class Uidx {
		private final static int 
			odurUid=0, userUid=1, rid=2, ein=3, lginId=4, orgId=5, rescId=6, aduTypCd=7, sysUserYn=8;
	}
	
	private static class Fnc {

		protected static boolean exPhoneEnable = false;
		
		/** 전화번호 포멧(010-123-4567)으로 */
		private static String toPhone(String text){
			if(text==null || text.isEmpty()) return "";
			if(exPhoneEnable) return text.trim();
			
			text = text.replace("-", "").trim();
			int len = text.length();
			if(len<6){
				return "";
			} else if(len<9){
				return "-"+text.substring(0, len-4)+"-"+text.substring(len-4);
			} else if(text.startsWith("02")){
				if(len<11){
					return text.substring(0, 2)+"-"+text.substring(2, len-4)+"-"+text.substring(len-4);
				} else {
					return "";
				}
			} else if(text.startsWith("0")){
				if(len<12){
					return text.substring(0, 3)+"-"+text.substring(3, len-4)+"-"+text.substring(len-4);
				} else {
					return "";
				}
			} else {
				return "";
			}
		}
		/** 날짜 포멧(YYYY-MM-DD)으로 */
		private static String toYmdFormat(String dateString){
	    	if(dateString==null) return null;
	    	int index = 0;
	    	StringBuilder builder = new StringBuilder();
	    	for(char c : dateString.toCharArray()){
	    		if(c>='0' && c<='9'){
	    			if(index==4||index==6) builder.append('-');
	    			builder.append(c);
	    			index++;
	    			if(index==8) break;
	    		}
	    	}
	    	if(index==8) return builder.toString();
	    	return "";
	    }
		
		/** 언어목록을 Attribute Id 변환용 으로 전환 */
		private static List<String> toAttrLangList(List<String> langList) {
			List<String> returnList = new ArrayList<String>();
			for(String lang : langList){
				returnList.add(lang.substring(0, 1).toUpperCase()+lang.substring(1));
			}
			return returnList;
		}
		
		/** DB 연결 설정 정보 Map 으로 변환 */
		private static Map<String, String> createConfigMap(OrDbIntgDVo orDbIntgDVo){
			Map<String, String> configMap = new HashMap<String, String>();
			configMap.put("drv", orDbIntgDVo.getDbDriver());
			configMap.put("url", orDbIntgDVo.getDbUrl());
			configMap.put("usr", orDbIntgDVo.getDbUserId());
			configMap.put("pwd", orDbIntgDVo.getDbPw());
			configMap.put("validationSql", orDbIntgDVo.getDbTestSql());
			return configMap;
		}
	}
}
