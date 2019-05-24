package com.innobiz.orange.web.or.svc;

import java.io.File;
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

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ExcelReader;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOfseDVo;
import com.innobiz.orange.web.or.vo.OrOrgApvDVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgCntcDVo;
import com.innobiz.orange.web.or.vo.OrRescBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.or.vo.OrUserPinfoDVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.or.vo.OrUserRoleRVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;

/** 조직 엑셀 업로드 서비스 */
@Service
public class OrExcelUploadSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(OrExcelUploadSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 포털 공통 서비스 */
	@Autowired
	PtCmSvc ptCmSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	OrCmSvc orCmSvc;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 확장 전화번호 사용 */
	private boolean exPhoneEnable = false;
	
	/** 확장 언어
	 * th : 태국어
	 *  */
	private String[] extLangs = { "th" };
	
	/** 엑셀 일괄 저장 */
	public void processExcel(File file, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws IOException, SQLException, CmException{
		
		// 확장 전화번호 사용
		exPhoneEnable = "Y".equals(ptSysSvc.getSysPlocMap().get("exPhoneEnable"));
		
		String langTypCd = userVo.getLangTypCd();
		OrExcelUploadData orExcelUploadData = new OrExcelUploadData(langTypCd);
		
		boolean isKo = "ko".equals(langTypCd);
		String[] tabNames = isKo ? new String[]{"코드","조직도","사용자"} : new String[]{"Code","Organization","User"};
		Map<String, String[][]> map = ExcelReader.readToMap(file, tabNames);
		String[][] sheet;
		
		// 탭 체크
		for(int i=0;i<3;i++){
			sheet = map.get(tabNames[i]);
			if(sheet==null){
				if(isKo){
					LOGGER.error("Excel User Upload - ["+tabNames[i]+"] 텝 없음");
					errorList.add("["+tabNames[i]+"] 텝 없음");
				} else {
					LOGGER.error("Excel User Upload - No "+tabNames[i]+" Tab in Excel File");
					errorList.add("No ["+tabNames[i]+"] Tab in Excel File");
				}
			}
		}
		if(!errorList.isEmpty()) return;
		
		// 엑셀 작업
		for(int i=0;i<3;i++){
			sheet = map.get(tabNames[i]);
			if(i==0){
				processCd(sheet, orExcelUploadData, queryQueue, userVo, errorList);
			} else if(i==1){
				processOrg(sheet, orExcelUploadData, queryQueue, userVo, errorList);
			} else if(i==2){
				processUser(sheet, orExcelUploadData, queryQueue, userVo, errorList);
			}
			if(!errorList.isEmpty()) break;
		}
	}
	
	/** 언어코드 리턴 */
	private ArrayList<String> getKoLangList(){
		ArrayList<String> langList = new ArrayList<String>();
		for(String lang : new String[]{"ko", "en", "ja", "zh"}){
			langList.add(lang);
		}
		for(String lang : extLangs){
			langList.add(lang);
		}
		return langList;
	}
	
	/** 해더 정보를 파싱해서  attrIdMap 에 데이터를 넣고, 해당 언어 목록을 리턴함 */
	private ArrayList<String> parseHeaderLang(String[] headers, String headerPrefix, 
			String attrId, String langTypeCd, Map<String, String> attrIdMap){
		
		ArrayList<String> langList = new ArrayList<String>();
		
		if(langTypeCd!=null){
			attrIdMap.put(headerPrefix, attrId+"-"+langTypeCd);
			langList.add(langTypeCd);
		}
		
		int p;
		String headerLang;
		for(String header : headers){
			if(header!=null && header.startsWith(headerPrefix) && header.endsWith(")")){
				p = header.indexOf("(");
				if(p<0) continue;
				
				headerLang = header.substring(p+1,header.length()-1);
				if(headerLang.length()!=2) continue;
				
				attrIdMap.put(header, attrId+"-"+headerLang);
				if(!langList.contains(headerLang)){
					langList.add(headerLang);
				}
			}
		}
		
		return langList;
	}
	
	/** 코드 엑셀 데이터 처리 */
	private void processCd(String[][] sheet, OrExcelUploadData orExcelUploadData, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws SQLException{
		
		String langTypCd = userVo.getLangTypCd();
		boolean isKo = "ko".equals(langTypCd);
		
		ArrayList<String> langList = null;
		
		// 타이틀 항목 전환 맵
		Map<String, String> attrIdMap = new HashMap<String, String>();
		if(isKo){
			attrIdMap.put("코드구분", "clsNm");
			attrIdMap.put("코드", "cd");
			attrIdMap.put("코드명", "cdVa-ko");
			attrIdMap.put("코드명[국문]", "cdVa-ko");
			attrIdMap.put("코드명[영문]", "cdVa-en");
			attrIdMap.put("코드명[일문]", "cdVa-ja");
			attrIdMap.put("코드명[중문]", "cdVa-zh");
			for(String lang : extLangs){
				attrIdMap.put("코드명["+lang+"]", "cdVa-"+lang);
			}
			attrIdMap.put("회사ID", "compIds");
			langList = getKoLangList();
		} else {
			attrIdMap.put("Code Category", "clsNm");
			attrIdMap.put("Code", "cd");
			attrIdMap.put("Company ID", "compIds");
			// 코드명
			langList = parseHeaderLang(sheet[0], "Code Name", "cdVa", langTypCd, attrIdMap);
		}
		
		// 필수 항목
		String[] mandatorys =  new String[]{"clsNm", "cd", "cdVa-"+langTypCd};
		
		List<String> clsCdList = new ArrayList<String>();
		
		// 엑셀 리스트
		List<Map<String, String>> list = processSheet(sheet, attrIdMap, null, null, mandatorys);
		if(list==null || list.isEmpty()) return;
		
		
		PtRescBVo ptRescBVo;
		PtCdBVo ptCdBVo, storedPtCdBVo;
		
		List<Integer> dupCheckList = null;
		Map<String, List<Integer>> dupCheckMap = new HashMap<String, List<Integer>>();
		
		Integer sortOrdr = 0, hashValue;
		String clsNm, clsCd, cd, rescId, rescVa, userUid = userVo.getUserUid(), oldClsCd = null;
		
		String tabNm = isKo ? "[코드 탭] - " : "[Code Sheet] - ";
		String errClsCd = isKo ? "코드구분" : "Code Category";
		String errDupCd = isKo ? "중복된 [코드]" : "Duplicated [Code]";
		String errNoCompId = isKo ? "'회사ID' 없음" : "No 'Company ID'";
		String errCdByOtherComp = isKo ? "타사 사용중인 코드" : "Code used by other company";
		
		String errMsg, compIds, storedCompIds;
		String[] compIdArr, storedCompIdArr;
		boolean hasError = false, hasCompId;
		
		// 코드 로드
		Map<Integer, PtCdBVo> storedCdMap = new HashMap<Integer, PtCdBVo>();
		Map<String, Integer> sortOrderMap = new HashMap<String, Integer>();
		loadCdMap(sortOrderMap, storedCdMap, langTypCd);
		
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		boolean codeByCompEnable = "Y".equals(sysPlocMap.get("codeByCompEnable"));
		boolean seculByCompEnable = "Y".equals(sysPlocMap.get("seculByCompEnable"));
		String[] orCodes = {"GRADE_CD","TITLE_CD","POSIT_CD","DUTY_CD"};
		boolean codeByComp;
		
		// 오류 체크
		for(Map<String, String> map : list){
			
			// 분류코드 - 코드의 폴더 정보 확인
			clsNm = map.get("clsNm");
			clsCd = orExcelUploadData.getCd("CodeCategory", clsNm, tabNm+errClsCd, errorList);
			if(clsCd==null){
				hasError = true;
				continue;
			}
			
			// 코드 중복 체크
			cd = map.get("cd");
			if(!clsCdList.contains(clsCd)){
				clsCdList.add(clsCd);
				dupCheckList = new ArrayList<Integer>();
				dupCheckMap.put(clsCd, dupCheckList);
				oldClsCd = clsCd;
			} else if(!clsCd.equals(oldClsCd)){
				dupCheckList = dupCheckMap.get(clsCd);
				oldClsCd = clsCd;
			}
			
			hashValue = hash(cd);
			if(dupCheckList.contains(hashValue)){
				errMsg = tabNm+errDupCd+" : "+clsNm+" / "+cd;
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
			} else {
				dupCheckList.add(hashValue);
			}
		}
		if(hasError) return;
		
		oldClsCd = null;
		clsCdList = new ArrayList<String>();
		
		for(Map<String, String> map : list){
			
			// 분류코드 - 코드의 폴더 정보 확인
			clsNm = map.get("clsNm");
			clsCd = orExcelUploadData.getCd("CodeCategory", clsNm, tabNm+errClsCd, errorList);
			
			// 분류코드 기준
			if(!clsCdList.contains(clsCd)){
				clsCdList.add(clsCd);

				sortOrdr = sortOrderMap.get(clsCd);
				if(sortOrdr==null) sortOrdr = 0;
				
				sortOrderMap.put(clsCd, sortOrdr);
				oldClsCd = clsCd;
				
			} else if(!clsCd.equals(oldClsCd)){
				
				sortOrderMap.put(oldClsCd, sortOrdr);
				sortOrdr = sortOrderMap.get(clsCd);
				oldClsCd = clsCd;
			}
			
			
			ptCdBVo = new PtCdBVo();

			cd = map.get("cd");
			storedPtCdBVo = storedCdMap.get(hash(clsCd+"-"+cd));
			
			// 리소스 매핑
			rescId = storedPtCdBVo==null ? null : storedPtCdBVo.getRescId();
			if(!hasError){
				for(String lang : langList){
					rescVa = map.get("cdVa-"+lang);
					if(rescVa!=null && !rescVa.isEmpty()){
						// 리소스ID
						if(rescId==null){
							rescId = ptCmSvc.createId("PT_RESC_B");
						}
						
						if(ptCdBVo.getCdVa()==null || lang.equals("ko")){
							ptCdBVo.setCdVa(rescVa);
						}
						
						// 리소스 저장
						ptRescBVo = new PtRescBVo();
						ptRescBVo.setRescId(rescId);
						ptRescBVo.setLangTypCd(lang);
						ptRescBVo.setRescVa(rescVa);
						if(storedPtCdBVo==null){
							queryQueue.insert(ptRescBVo);
						} else {
							queryQueue.store(ptRescBVo);
						}
					}
				}
				// 코드에 리스소ID, 값 세팅
				ptCdBVo.setRescId(rescId);
				ptCdBVo.setRescNm(map.get("cdVa-"+langTypCd));
			}
			
			// 코드 저장
			ptCdBVo.setClsCd(clsCd);
			ptCdBVo.setCd(cd);
			sortOrdr++;
			ptCdBVo.setSortOrdr(sortOrdr.toString());
			ptCdBVo.setUseYn("Y");
			ptCdBVo.setFldYn("N");
			ptCdBVo.setSysCdYn("N");
			ptCdBVo.setModrUid(userUid);
			ptCdBVo.setModDt("sysdate");
			
			compIds = map.get("compIds");
			storedCompIds = storedPtCdBVo==null ? null : storedPtCdBVo.getCompIds();
			
			// 해당 코드가 회사별 코드인지 체크
			if(ArrayUtil.isInArray(orCodes, clsCd)){
				codeByComp = codeByCompEnable;
			} else if("SECUL_CD".equals(clsCd)){
				codeByComp = seculByCompEnable;
			} else {
				codeByComp = false;
			}
			
			// 회사별 코드 설정(회사별 직위,직급 or 회사별 보안코드) 일 경우
			// - 회사ID 가 없으면 오류 처리
			if(codeByComp && (compIds==null || compIds.isEmpty())){
				// 회사코드 없다는 오류 메세지
				errMsg = tabNm+errNoCompId+" : "+clsNm+" / "+cd;
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
			}
			
			if(storedCompIds==null || storedCompIds.isEmpty()){
				if(compIds!=null && !compIds.isEmpty()){
					ptCdBVo.setCompIds(compIds);	
				}
			} else {
				if(compIds!=null && !compIds.isEmpty()){
					
					// compId 가 중복되지 않으면 합쳐서 넣기
					compIdArr = compIds.split(",");
					storedCompIdArr = storedCompIds.split(",");
					
					StringBuilder builder = new StringBuilder();
					builder.append(storedCompIds);
					
					for(String compIdNew : compIdArr){
						if(compIdNew==null) continue;
						
						hasCompId = false;
						for(String compIdStored : storedCompIdArr){
							if(compIdNew.equals(compIdStored)){
								hasCompId = true;
								break;
							}
						}
						
						if(!hasCompId){
							// 설정이 회사별 코드 이면
							if(codeByComp){
								// 다른회사에서 사용중인 코드 라는 에러 메세지
								errMsg = tabNm+errCdByOtherComp+" : "+clsNm+" / "+cd;
								if(!errorList.contains(errMsg)){
									errorList.add(errMsg);
								}
								hasError = true;
							} else {
								builder.append(',').append(compIdNew);
							}
						}
					}
					ptCdBVo.setCompIds(builder.toString());
					
				} else {
					ptCdBVo.setCompIds(storedCompIds);
				}
			}
			
			if(storedPtCdBVo==null){
				ptCdBVo.setRegrUid(userUid);
				ptCdBVo.setRegDt("sysdate");
				queryQueue.insert(ptCdBVo);
			} else {
				queryQueue.store(ptCdBVo);
			}
			
			orExcelUploadData.setCd(clsNm, ptCdBVo.getRescNm(), ptCdBVo.getCd());
		}
	}
	
	/** 코드데이터 로드 */
	private void loadCdMap(Map<String, Integer> sortOrderMap, Map<Integer, PtCdBVo> storedCdMap, String langTypCd) throws SQLException{
		
		PtCdBVo ptCdBVo = new PtCdBVo();
		ptCdBVo.setWhereSqllet("AND CLS_CD IN ('GRADE_CD', 'TITLE_CD', 'POSIT_CD', 'SECUL_CD', 'DUTY_CD', 'ROLE_CD')");
		ptCdBVo.setQueryLang(langTypCd);
		ptCdBVo.setOrderBy("CLS_CD, SORT_ORDR");
		
		@SuppressWarnings("unchecked")
		List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>)commonDao.queryList(ptCdBVo);
		if(ptCdBVoList != null){
			for(PtCdBVo storedPtCdBVo : ptCdBVoList){
				storedCdMap.put(hash(storedPtCdBVo.getClsCd()+"-"+storedPtCdBVo.getCd()), storedPtCdBVo);
				sortOrderMap.put(storedPtCdBVo.getClsCd(), Integer.valueOf(storedPtCdBVo.getSortOrdr()));
			}
		}
	}
	
	/** 조직도 엑셀 데이터 처리 */
	private void processOrg(String[][] sheet, OrExcelUploadData orExcelUploadData, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws SQLException{
		
		String langTypCd = userVo.getLangTypCd();
		boolean isKo = "ko".equals(langTypCd);
		
		ArrayList<String> langList = null;
		
		// 타이틀 항목 전환 맵
		Map<String, String> attrIdMap = new HashMap<String, String>();
		if(isKo){
			attrIdMap.put("조직명[영문]", "orgNm-en");
			attrIdMap.put("조직명[일문]", "orgNm-ja");
			attrIdMap.put("조직명[중문]", "orgNm-zh");
			for(String lang : extLangs){
				attrIdMap.put("조직명["+lang+"]", "orgNm-"+lang);
			}
			attrIdMap.put("회사ID", "compId");
			attrIdMap.put("조직구분", "orgTypNm");
			attrIdMap.put("참조ID", "rid");
			attrIdMap.put("부서장직위", "hodpPositRescNm-ko");
			attrIdMap.put("부서장직위[영문]", "hodpPositRescNm-en");
			attrIdMap.put("부서장직위[일문]", "hodpPositRescNm-ja");
			attrIdMap.put("부서장직위[중문]", "hodpPositRescNm-zh");
			for(String lang : extLangs){
				attrIdMap.put("부서장직위["+lang+"]", "hodpPositRescNm-"+lang);
			}
			attrIdMap.put("조직약어", "orgAbbrRescNm-ko");
			attrIdMap.put("조직약어[영문]", "orgAbbrRescNm-en");
			attrIdMap.put("조직약어[일문]", "orgAbbrRescNm-ja");
			attrIdMap.put("조직약어[중문]", "orgAbbrRescNm-zh");
			for(String lang : extLangs){
				attrIdMap.put("조직약어["+lang+"]", "orgAbbrRescNm-"+lang);
			}
			attrIdMap.put("발신명의", "sendrNmRescNm-ko");
			attrIdMap.put("발신명의[영문]", "sendrNmRescNm-en");
			attrIdMap.put("발신명의[일문]", "sendrNmRescNm-ja");
			attrIdMap.put("발신명의[중문]", "sendrNmRescNm-zh");
			for(String lang : extLangs){
				attrIdMap.put("발신명의["+lang+"]", "sendrNmRescNm-"+lang);
			}
			
			attrIdMap.put("전화번호", "phon");
			attrIdMap.put("이메일", "repEmail");
			attrIdMap.put("팩스번호", "fno");
			attrIdMap.put("홈페이지URL", "repHpageUrl");
			attrIdMap.put("우편번호", "zipNo");
			attrIdMap.put("주소", "adr");
			attrIdMap.put("상세주소", "detlAdr");
			langList = getKoLangList();
		} else {
			// 조직명
			langList = parseHeaderLang(sheet[0], "Organization Name", "orgNm", langTypCd, attrIdMap);
			
			attrIdMap.put("Company ID", "compId");
			attrIdMap.put("Organization Type", "orgTypNm");
			attrIdMap.put("Reference ID", "rid");
			
			// 부서장직위
			parseHeaderLang(sheet[0], "Position of Chief of Department", "hodpPositRescNm", langTypCd, attrIdMap);
			// 조직약어
			parseHeaderLang(sheet[0], "Abbreviation of Organization", "orgAbbrRescNm", langTypCd, attrIdMap);
			// 발신명의
			parseHeaderLang(sheet[0], "Name of Official Sending", "sendrNmRescNm", langTypCd, attrIdMap);
			
			attrIdMap.put("Organization Phone", "phon");
			attrIdMap.put("Organization Email", "repEmail");
			attrIdMap.put("Organization Fax", "fno");
			attrIdMap.put("Organization Homepage URL", "repHpageUrl");
			attrIdMap.put("Zip No", "zipNo");
			attrIdMap.put("Organization Address", "adr");
			attrIdMap.put("Organization Address Detail", "detlAdr");
		}
		// 조직 RID 맞추는 작업용
		attrIdMap.put("refVa1", "refVa1");
		
		// 필수 항목
		String[] mandatorys = new String[]{"orgNm-"+langTypCd, "compId", "orgTypNm"};
		
		// 엑셀 리스트
		List<Map<String, String>> list = processSheet(sheet, attrIdMap, "orgNm-"+langTypCd,
				isKo ? "조직명" : "Organization Name", mandatorys);
		
		if(list==null || list.isEmpty()) return;
		
		// 조직기본(OR_ORG_B) 테이블
		OrOrgBVo orOrgBVo, parentOrOrgBVo, storedOrOrgBVo;
		// 조직연락처상세(OR_ORG_CNTC_D) 테이블
		OrOrgCntcDVo orOrgCntcDVo;
		// 조직결재상세(OR_ORG_APV_D) 테이블
		OrOrgApvDVo orOrgApvDVo, storedOrOrgApvDVo;
		// 관인상세(OR_OFSE_D) 테이블
		OrOfseDVo orOfseDVo;
		// 리소스기본(OR_RESC_B) 테이블
		OrRescBVo orRescBVo;
		// 원직자기본(OR_ODUR_B)
		OrOdurBVo orOdurBVo;
		
		OrOrgBVo[] treeArray = new OrOrgBVo[20];
		
		// 중복체크 용
		List<Integer> ridDupCheckList = new ArrayList<Integer>();
		Integer hashValue;
		
		List<String> compIdList = new ArrayList<String>();
		
		boolean valid = true, hasError = false;
		int level, oldLevel = 0, sortOrdr=1, i;
		String compId, orgId, rescId, rescVa, value, userUid = userVo.getUserUid(), rid, refVa1;
		
		String tempCd, errMsg;
		String tabNm = isKo ? "[조직도 탭] - " : "[Organization Sheet] - ";
		String errOrgTyp = isKo ? "조직구분" : "Organization Type";
		String errDupRid = isKo ? "중복된 [참조ID]" : "Duplicated [Reference ID]";
		
		// 기존 조직 로드
		Map<Integer, OrOrgBVo> storedOrgRidMap = new HashMap<Integer, OrOrgBVo>();
		Map<Integer, OrOrgApvDVo> storedOrgApvMap = new HashMap<Integer, OrOrgApvDVo>();
		Map<Integer, OrOrgBVo> storedOrgRefMap = new HashMap<Integer, OrOrgBVo>();
		loadOrgMap(storedOrgRidMap, storedOrgApvMap, storedOrgRefMap, langTypCd);
		
		// 에러 체크
		for(Map<String, String> map : list){
			// rid 중복 체크
			rid = map.get("rid");
			if(rid!=null && !rid.isEmpty()){
				hashValue = hash(rid);
				if(ridDupCheckList.contains(hashValue)){
					errMsg = tabNm+errDupRid+" : "+rid;
					if(!errorList.contains(errMsg)){
						errorList.add(errMsg);
					}
					hasError = true;
					continue;
				} else {
					ridDupCheckList.add(hashValue);
				}
			}
			// 조직구분 체크
			tempCd = map.get("orgTypNm");
			if(tempCd==null || tempCd.isEmpty()) tempCd = "D";
			if(orExcelUploadData.getCd("OrganizationType", tempCd, tabNm+errOrgTyp, errorList) == null){
				hasError = true;
			}
		}
		if(hasError) return;
		
		int[] sortOrdrArr = { 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };
		
		for(Map<String, String> map : list){
			
			///////////////////////////////////////////
			//
			//     회사별 조직 삭제
			
			compId = map.get("compId");
			if(!compIdList.contains(compId)){
				compIdList.add(compId);
				
//				orRescBVo = new OrRescBVo();
//				orRescBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrRescBDao.deleteOrRescBByExcelOrg");
//				orRescBVo.setCompId(compId);
//				queryQueue.delete(orRescBVo);
//				
//				orOfseDVo = new OrOfseDVo();
//				orOfseDVo.setCompId(compId);
//				queryQueue.delete(orOfseDVo);
//				
//				orOrgApvDVo = new OrOrgApvDVo();
//				orOrgApvDVo.setCompId(compId);
//				queryQueue.delete(orOrgApvDVo);
//				
//				orOrgCntcDVo = new OrOrgCntcDVo();
//				orOrgCntcDVo.setCompId(compId);
//				queryQueue.delete(orOrgCntcDVo);
//				
//				orOrgBVo = new OrOrgBVo();
//				orOrgBVo.setCompId(compId);
//				queryQueue.delete(orOrgBVo);
				
				// 조직 업로드 - 단계1
				// 동기화단계코드:EX_INIT 으로 설정 - 해당회사 것만
				orOrgBVo = new OrOrgBVo();
				orOrgBVo.setSyncStepCd("EX_INIT");
				orOrgBVo.setWhereSqllet("AND COMP_ID = '"+compId+"'");
				queryQueue.update(orOrgBVo);
				
				// 사용자 업로드 - 단계1
				// 동기화단계코드:EX_INIT 으로 설정 - 해당회사 것만
				orOdurBVo = new OrOdurBVo();
				orOdurBVo.setSyncStepCd("EX_INIT");
				orOdurBVo.setWhereSqllet("AND ODUR_UID IN (SELECT ODUR_UID FROM OR_USER_B WHERE COMP_ID = '"+compId+"')");
				queryQueue.update(orOrgBVo);
			}
			
			// 저장된 데이터 확인 - rid 기준, 차선:refVa1
			rid = map.get("rid");
			refVa1 = map.get("refVa1");
			storedOrOrgBVo = null;
			storedOrOrgApvDVo = null;
			if(rid!=null && !rid.isEmpty()){
				storedOrOrgBVo = storedOrgRidMap.get(hash(rid));
			}
			if(storedOrOrgBVo == null && refVa1!=null && !refVa1.isEmpty()){
				storedOrOrgBVo = storedOrgRefMap.get(hash(refVa1));
			}
			if(storedOrOrgBVo != null){
				storedOrOrgApvDVo = storedOrgApvMap.get(hash(storedOrOrgBVo.getOrgId()));
			}
			
			///////////////////////////////////////////
			//
			//     조직기본(OR_ORG_B) 테이블
			
			orOrgBVo = new OrOrgBVo();
			
			// 조직명
			rescId = storedOrOrgBVo == null ? null : storedOrOrgBVo.getRescId();
			for(String lang : langList){
				rescVa = map.get("orgNm-"+lang);
				if(rescVa!=null && !rescVa.isEmpty()){
					// 리소스ID
					if(rescId==null){
						rescId = orCmSvc.createId("OR_RESC_B");
						orOrgBVo.setRescId(rescId);
					}
					if(lang.equals("ko") || orOrgBVo.getOrgNm()==null){
						orOrgBVo.setOrgNm(rescVa);
					}
					
					// 리소스 저장
					orRescBVo = new OrRescBVo();
					orRescBVo.setRescId(rescId);
					orRescBVo.setLangTypCd(lang);
					orRescBVo.setRescVa(rescVa);
					if(storedOrOrgBVo==null){
						queryQueue.insert(orRescBVo);
					} else {
						queryQueue.store(orRescBVo);
					}
				}
			}
			
			// 조직 코드
			if(storedOrOrgBVo == null){
				orgId = orCmSvc.createId("OR_ORG_B");
			} else {
				orgId = storedOrOrgBVo.getOrgId();
			}
			
			// 트리 레벨
			try { level = Integer.parseInt(map.get("treeLevel")); }
			catch(Exception e){ continue; }
			
			if(level > oldLevel){
				sortOrdrArr[level] = 1;
			} else {
				sortOrdrArr[level]++;
			}
			sortOrdr = sortOrdrArr[level];
			
			parentOrOrgBVo = treeArray[level-1];
			
			orOrgBVo.setOrgId(orgId);
			orOrgBVo.setRescNm(map.get("orgNm-"+langTypCd));
			orOrgBVo.setOrgNm(map.get("orgNm-ko")!=null ? map.get("orgNm-ko") : orOrgBVo.getRescNm());
			orOrgBVo.setCompId(compId);
			
			if(level==1){
				orOrgBVo.setOrgPid("ROOT");
				orOrgBVo.setOrgTypCd("C");
			} else {
				if(parentOrOrgBVo==null) continue;
				orOrgBVo.setOrgPid(parentOrOrgBVo.getOrgId());
				
				tempCd = map.get("orgTypNm");
				if(tempCd==null || tempCd.isEmpty()) tempCd = "D";
				orOrgBVo.setOrgTypCd(orExcelUploadData.getCd("OrganizationType", tempCd, tabNm+errOrgTyp, errorList));
				if(orOrgBVo.getOrgTypCd() == null){
					orOrgBVo.setOrgTypCd("D");
				}
			}
			
			if(!"P".equals(orOrgBVo.getOrgTypCd())){
				orOrgBVo.setDeptId(orgId);
				orOrgBVo.setDeptRescId(rescId);
			} else {
				valid = false;
				for(i=level-1;i>=1;i--){
					if(!"P".equals(treeArray[i].getOrgTypCd())){
						orOrgBVo.setDeptId(treeArray[i].getOrgId());
						orOrgBVo.setDeptRescId(treeArray[i].getRescId());
						valid = true;
						break;
					}
				}
				if(!valid) {
					if(isKo){
						errMsg = tabNm+errOrgTyp+" : ["+orOrgBVo.getRescNm()+"] - 파트만 가능";
					} else {
						errMsg = tabNm+errOrgTyp+" : ["+orOrgBVo.getRescNm()+"] - Only Part";
					}
					if(!errorList.contains(errMsg)){
						errorList.add(errMsg);
					}
					hasError = true;
					continue;
				}
			}
			
			orOrgBVo.setRid(map.get("rid"));
			orOrgBVo.setSortOrdr(Integer.toString(sortOrdr));
			
			orOrgBVo.setUseYn("Y");
			orOrgBVo.setSysOrgYn("N");
			
			// 조직약어
			rescId = storedOrOrgBVo == null ? null : storedOrOrgBVo.getOrgAbbrRescId();
			for(String lang : langList){
				rescVa = map.get("orgAbbrRescNm-"+lang);
				if(rescVa!=null && !rescVa.isEmpty()){
					// 리소스ID
					if(rescId==null && !hasError){
						rescId = orCmSvc.createId("OR_RESC_B");
					}
					
					// 리소스 저장
					orRescBVo = new OrRescBVo();
					orRescBVo.setRescId(rescId);
					orRescBVo.setLangTypCd(lang);
					orRescBVo.setRescVa(rescVa);
					if(storedOrOrgBVo==null){
						queryQueue.insert(orRescBVo);
					} else {
						queryQueue.store(orRescBVo);
					}
				}
			}
			orOrgBVo.setOrgAbbrRescId(rescId);
			
			orOrgBVo.setRegrUid(userUid);
			orOrgBVo.setRegDt("sysdate");
			
			orOrgBVo.setModrUid(userUid);
			orOrgBVo.setModDt("sysdate");
			
			// 조직 업로드 - 단계2
			// 동기화단계코드:EX_UPDATED 으로 변경 - 데이터 있는 것만
			orOrgBVo.setSyncStepCd("EX_UPDATED");
			
			if(storedOrOrgBVo!=null || "O0000001".equals(orgId)){
				queryQueue.store(orOrgBVo);
			} else {
				queryQueue.insert(orOrgBVo);
			}
			
			///////////////////////////////////////////
			//
			//     조직결재상세(OR_ORG_APV_D) 테이블
			
			orOrgApvDVo = new OrOrgApvDVo();
			valid = false;
			
			// 부서장직위
			rescId = storedOrOrgApvDVo == null ? null : storedOrOrgApvDVo.getHodpPositRescId();
			for(String lang : langList){
				rescVa = map.get("hodpPositRescNm-"+lang);
				if(rescVa!=null && !rescVa.isEmpty()){
					// 리소스ID
					if(rescId==null){
						rescId = orCmSvc.createId("OR_RESC_B");
					}
					
					// 리소스 저장
					orRescBVo = new OrRescBVo();
					orRescBVo.setRescId(rescId);
					orRescBVo.setLangTypCd(lang);
					orRescBVo.setRescVa(rescVa);
					if(storedOrOrgApvDVo==null){
						queryQueue.insert(orRescBVo);
					} else {
						queryQueue.store(orRescBVo);
					}
				}
			}
			if(rescId != null){
				valid = true;
				orOrgApvDVo.setHodpPositRescId(rescId);
			}
			
			// 발신명의
			rescId = storedOrOrgApvDVo == null ? null : storedOrOrgApvDVo.getSendrNmRescId();
			for(String lang : langList){
				rescVa = map.get("sendrNmRescNm-"+lang);
				if(rescVa!=null && !rescVa.isEmpty()){
					// 리소스ID
					if(rescId==null){
						rescId = orCmSvc.createId("OR_RESC_B");
					}
					
					// 리소스 저장
					orRescBVo = new OrRescBVo();
					orRescBVo.setRescId(rescId);
					orRescBVo.setLangTypCd(lang);
					orRescBVo.setRescVa(rescVa);
					if(storedOrOrgApvDVo==null){
						queryQueue.insert(orRescBVo);
					} else {
						queryQueue.store(orRescBVo);
					}
				}
			}
			if(rescId != null){
				valid = true;
				orOrgApvDVo.setSendrNmRescId(rescId);
			}
			
			if(valid){
				orOrgApvDVo.setOrgId(orgId);
				orOrgApvDVo.setInspYn("N");
				if(storedOrOrgApvDVo==null){
					queryQueue.insert(orOrgApvDVo);
				} else {
					queryQueue.store(orOrgApvDVo);
				}
			}
			
			///////////////////////////////////////////
			//
			//     조직연락처상세(OR_ORG_CNTC_D) 테이블
			
			orOrgCntcDVo = new OrOrgCntcDVo();
			valid = false;
			
			value = map.get("phon");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setPhon(value);
				valid = true;
			}
			
			value = map.get("repEmail");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setRepEmail(value);
				valid = true;
			}
			
			value = map.get("fno");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setFno(value);
				valid = true;
			}
			
			value = map.get("repHpageUrl");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setRepHpageUrl(value);
				valid = true;
			}
			
			value = map.get("zipNo");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setZipNo(value);
				valid = true;
			}
			
			value = map.get("adr");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setAdr(value);
				valid = true;
			}
			
			value = map.get("detlAdr");
			if(value != null && !value.isEmpty()){
				orOrgCntcDVo.setDetlAdr(value);
				valid = true;
			}
			
			if(valid){
				orOrgCntcDVo.setOrgId(orgId);
				queryQueue.store(orOrgCntcDVo);
			}
			
			treeArray[level] = orOrgBVo;
			oldLevel = level;
			
			orExcelUploadData.setOrg(parentOrOrgBVo, orOrgBVo);
		}
		
		// 조직 업로드 - 단계3
		// 동기화단계코드:EX_INIT 으로 남아 있는 데이터 삭제
		
		// 부서장직위 - 리소스 삭제
		orRescBVo = new OrRescBVo();
		orRescBVo.setWhereSqllet("AND RESC_ID IN("
				+"SELECT HODP_POSIT_RESC_ID FROM OR_ORG_APV_D WHERE ORG_ID IN "
				+"(SELECT ORG_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT')"
				+")");
		queryQueue.delete(orRescBVo);
		
		// 발신명의 - 리소스 삭제
		orRescBVo = new OrRescBVo();
		orRescBVo.setWhereSqllet("AND RESC_ID IN("
				+"SELECT SENDR_NM_RESC_ID FROM OR_ORG_APV_D WHERE ORG_ID IN "
				+"(SELECT ORG_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT')"
				+")");
		queryQueue.delete(orRescBVo);
		
		// 관인 - 리소스 삭제
		orRescBVo = new OrRescBVo();
		orRescBVo.setWhereSqllet("AND RESC_ID IN("
				+"SELECT RESC_ID FROM OR_OFSE_D WHERE ORG_ID IN "
				+"(SELECT ORG_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT')"
				+")");
		queryQueue.delete(orRescBVo);
		
		// 조직약어 - 리소스 삭제
		orRescBVo = new OrRescBVo();
		orRescBVo.setWhereSqllet("AND RESC_ID IN("
				+"SELECT ORG_ABBR_RESC_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT'"
				+")");
		queryQueue.delete(orRescBVo);
		
		// 조직명 - 리소스 삭제
		orRescBVo = new OrRescBVo();
		orRescBVo.setWhereSqllet("AND RESC_ID IN("
				+"SELECT RESC_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT'"
				+")");
		queryQueue.delete(orRescBVo);
		
		// 조직결재상세 - 삭제
		orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setWhereSqllet("AND ORG_ID IN("
				+"SELECT ORG_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT'"
				+")");
		queryQueue.delete(orOrgApvDVo);
		
		// 관인상세 - 삭제
		orOfseDVo = new OrOfseDVo();
		orOfseDVo.setWhereSqllet("AND ORG_ID IN("
				+"SELECT ORG_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT'"
				+")");
		queryQueue.delete(orOfseDVo);
		
		// 조직연락처상세 - 삭제
		orOrgCntcDVo = new OrOrgCntcDVo();
		orOrgCntcDVo.setWhereSqllet("AND ORG_ID IN("
				+"SELECT ORG_ID FROM OR_ORG_B WHERE SYNC_STEP_CD = 'EX_INIT'"
				+")");
		queryQueue.delete(orOrgCntcDVo);
		
		// 조직기본 - 삭제
		orOrgBVo = new OrOrgBVo();
		orOrgBVo.setWhereSqllet("SYNC_STEP_CD = 'EX_INIT'");
		queryQueue.delete(orOrgBVo);
		
		// 조직 업로드 - 단계4
		// 동기화 코드 초기화
		orOrgBVo = new OrOrgBVo();
		orOrgBVo.setSyncStepCd("");
		orOrgBVo.setWhereSqllet("SYNC_STEP_CD = 'EX_INIT'");
		queryQueue.update(orOrgBVo);
		
	}
	
	/** 조직데이터 로드 */
	private void loadOrgMap(Map<Integer, OrOrgBVo> storedOrgRidMap,
			Map<Integer, OrOrgApvDVo> storedOrgApvMap,
			Map<Integer, OrOrgBVo> storedOrgRefMap, String langTypCd) throws SQLException{
		
		// 조직기본(OR_ORG_B)
		OrOrgBVo orOrgBVo = new OrOrgBVo();
		orOrgBVo.setQueryLang(langTypCd);
		orOrgBVo.setOrderBy("ORG_PID, SORT_ORDR");
		
		String rid, refVa1;
		
		@SuppressWarnings("unchecked")
		List<OrOrgBVo> orOrgBVoList = (List<OrOrgBVo>)commonDao.queryList(orOrgBVo);
		if(orOrgBVoList != null){
			for(OrOrgBVo storedOrOrgBVo : orOrgBVoList){
				rid = storedOrOrgBVo.getRid();
				refVa1 = storedOrOrgBVo.getRefVa1();
				if(rid!=null && !rid.isEmpty()){
					storedOrgRidMap.put(hash(rid), storedOrOrgBVo);
				}
				if(refVa1!=null && !refVa1.isEmpty()){
					storedOrgRefMap.put(hash(refVa1), storedOrOrgBVo);
				}
			}
		}
		
		// 조직결재상세(OR_ORG_APV_D)
		OrOrgApvDVo orOrgApvDVo = new OrOrgApvDVo();
		orOrgApvDVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<OrOrgApvDVo> orOrgApvDVoList = (List<OrOrgApvDVo>)commonDao.queryList(orOrgApvDVo);
		if(orOrgApvDVoList != null){
			for(OrOrgApvDVo storedOrOrgApvDVo : orOrgApvDVoList){
				storedOrgApvMap.put(hash(storedOrOrgApvDVo.getOrgId()), storedOrOrgApvDVo);
			}
		}
	}
	
	/** 사용자 엑셀 데이터 처리 */
	public void processUser(String[][] sheet, OrExcelUploadData orExcelUploadData, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws SQLException, CmException, IOException{
		
		String langTypCd = userVo.getLangTypCd();
		boolean isKo = "ko".equals(langTypCd);
		
		ArrayList<String> langList = null;
		
		// 타이틀 항목 전환 맵
		Map<String, String> attrIdMap = new HashMap<String, String>();
		if(isKo){
			attrIdMap.put("조직명", "orgNm");
			attrIdMap.put("로그인ID", "lginId");
			attrIdMap.put("비밀번호", "pw");
			attrIdMap.put("사용자명", "userNm-ko");
			attrIdMap.put("사용자명[영문]", "userNm-en");
			attrIdMap.put("사용자명[일문]", "userNm-ja");
			attrIdMap.put("사용자명[중문]", "userNm-zh");
			for(String lang : extLangs){
				attrIdMap.put("사용자명["+lang+"]", "userNm-"+lang);
			}
			attrIdMap.put("성별", "genNm");
			attrIdMap.put("이메일", "email");
			attrIdMap.put("직급", "gradeNm");
			attrIdMap.put("직책", "titleNm");
			attrIdMap.put("직위", "positNm");
			attrIdMap.put("직무", "dutyNm");
			attrIdMap.put("보안등급", "seculNm");
			attrIdMap.put("역할", "roleNms");
			attrIdMap.put("상태", "userStatNm");
			attrIdMap.put("사원번호", "ein");
			attrIdMap.put("담당업무", "tichCont");
			attrIdMap.put("입사일", "entraYmd");
			attrIdMap.put("참조ID", "rid");
			attrIdMap.put("외부이메일", "extnEmail");
			attrIdMap.put("휴대전화번호", "mbno");
			attrIdMap.put("회사전화번호", "compPhon");
			attrIdMap.put("회사우편번호", "compZipNo");
			attrIdMap.put("회사주소", "compAdr");
			attrIdMap.put("회사상세주소", "compDetlAdr");
			attrIdMap.put("회사팩스번호", "compFno");
			attrIdMap.put("자택전화번호", "homePhon");
			attrIdMap.put("자택우편번호", "homeZipNo");
			attrIdMap.put("자택주소", "homeAdr");
			attrIdMap.put("자택상세주소", "homeDetlAdr");
			attrIdMap.put("자택팩스번호", "homeFno");
			langList = getKoLangList();
		} else {
			attrIdMap.put("Organization Name", "orgNm");
			attrIdMap.put("Login ID", "lginId");
			attrIdMap.put("Password", "pw");
			
			// 사용자명
			langList = parseHeaderLang(sheet[0], "User Name", "userNm", langTypCd, attrIdMap);
			
			attrIdMap.put("Gender", "genNm");
			attrIdMap.put("Email", "email");
			attrIdMap.put("Grade", "gradeNm");
			attrIdMap.put("Title", "titleNm");
			attrIdMap.put("Position", "positNm");
			attrIdMap.put("Duty", "dutyNm");
			attrIdMap.put("Security Level", "seculNm");
			attrIdMap.put("Role", "roleNms");
			attrIdMap.put("User Status", "userStatNm");
			attrIdMap.put("Employee Identification Number", "ein");
			attrIdMap.put("Task In Charge", "tichCont");
			attrIdMap.put("Entry Date", "entraYmd");
			attrIdMap.put("Reference ID", "rid");
			attrIdMap.put("External Email", "extnEmail");
			attrIdMap.put("Mobile Phone", "mbno");
			attrIdMap.put("Office Phone", "compPhon");
			attrIdMap.put("Office Zip No", "compZipNo");
			attrIdMap.put("Office Address", "compAdr");
			attrIdMap.put("Office Address Detail", "compDetlAdr");
			attrIdMap.put("Office Fax", "compFno");
			attrIdMap.put("Home Phone", "homePhon");
			attrIdMap.put("Home Zip No", "homeZipNo");
			attrIdMap.put("Home Address", "homeAdr");
			attrIdMap.put("Home Address Detail", "homeDetlAdr");
			attrIdMap.put("Home Fax", "homeFno");
		}
		
		// 필수 항목
		String[] mandatorys = new String[]{"orgNm", "lginId", "pw", "userNm-"+langTypCd, "genNm", "email"};
		
		// 엑셀 리스트
		List<Map<String, String>> list = processSheet(sheet, attrIdMap, null, null, mandatorys);

		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo;
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo, storedOrOdurBVo;
		// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
		OrUserPinfoDVo orUserPinfoDVo;
		// 사용자비밀번호상세(OR_USER_PW_D) 테이블
		OrUserPwDVo orUserPwDVo;
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo;
		// 사용자역할관계(OR_USER_ROLE_R) 테이블
		OrUserRoleRVo orUserRoleRVo;
		// 리소스기본(OR_RESC_B) 테이블
		OrRescBVo orRescBVo;
		// 조직기본(OR_ORG_B) 테이블
		OrOrgBVo orOrgBVo;
		
		List<String> compIdList = new ArrayList<String>();
		
		Map<Integer, Integer> sortOrdrMap = new HashMap<Integer, Integer>();
		
		Integer hashId, sortOrdr;
		int i;
		String[] roles;
		String compId, orgNm, rescId, rescVa, roleCd, roleNms, value, odurUid, userUid = userVo.getUserUid();
		String codeClsNm, tempCd, errMsg;
		
		String tabNm = isKo ? "[사용자 탭] - " : "[User Sheet] - ";
		
		String errOrgNm = isKo ? "조직명" : "Organization Name";
		String errHomeZip = isKo ? "자택우편번호" : "Home Zip No";
		String errOfficeZip = isKo ? "회사우편번호" : "Office Zip No";
		
		String errMbno = isKo ? "휴대전화번호" : "Mobile Phone";
		String errHomePhon = isKo ? "자택전화번호" : "Home Phone";
		String errHomeFno = isKo ? "자택팩스번호" : "Home Fax";
		String errCompPhon = isKo ? "회사전화번호" : "Office Phone";
		String errCompFno = isKo ? "회사팩스번호" : "Office Fax";
		
		// 중복 체크용
		@SuppressWarnings("unchecked")
		List<Integer>[] dupCheckLists = new List[4];
		for(int j=0;j<dupCheckLists.length;j++){
			dupCheckLists[j] = new ArrayList<Integer>();
		}
		String[] dupCheckAttrIds = { "lginId", "email", "ein", "rid" };
		String[] dupCheckErrs = isKo ?
				new String[]{
						"중복된 [로그인ID]",
						"중복된 [이메일]",
						"중복된 [사원번호]",
						"중복된 [참조ID]"
				} : new String[]{
						"Duplicated [Login ID]",
						"Duplicated [Email]",
						"Duplicated [Employee Identification Number]",
						"Duplicated [Reference ID]"
				};
		
		boolean hasDup, hasError = false;
		String checkValue, attrId, errLineHeader;
		Integer hashValue;
		
		// 코드 변환 에러 체크용
		String[][] cdCvtMapper = new String[][]{
				{"seculNm",			isKo ? "보안등급" : "Security Level",	isKo ? "보안등급" : "Security Level"},
				{"gradeNm",			isKo ? "직급" : "Grade",				isKo ? "직급" : "Grade"},
				{"titleNm",			isKo ? "직책" : "Title",				isKo ? "직책" : "Title"},
				{"positNm",			isKo ? "직위" : "Position",			isKo ? "직위" : "Position"},
				{"dutyNm",			isKo ? "직무" : "Duty",				isKo ? "직무" : "Duty"},
				{"userStatNm",		isKo ? "상태" : "User Status",		"UserStatusCd"},
				{"genNm",			isKo ? "성별" : "Gender",				"Gender"},
				{"roleNms",			isKo ? "역할" : "Role",				isKo ? "역할" : "Role"},
		};
		
		// 에러 체크
		for(Map<String, String> map : list){
			
			// 조직명 - 조직도 탭에 있는지 체크
			orgNm = map.get("orgNm");
			orOrgBVo = orExcelUploadData.getOrg(orgNm);
			if(orOrgBVo == null){
				errMsg = tabNm+errOrgNm+" : "+orgNm;
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
				continue;
			}
			
			// 중복 체크 - 로그인ID, 이메일, 사원번호, 참조ID
			hasDup = false;
			for(int j=0; j<dupCheckAttrIds.length; j++){
				checkValue = map.get(dupCheckAttrIds[j]);
				if(checkValue != null && !checkValue.isEmpty()){
					hashValue = hash(checkValue);
					if(dupCheckLists[j].contains(hashValue)){
						errMsg = tabNm+dupCheckErrs[j]+" : "+checkValue;
						if(!errorList.contains(errMsg)){
							errorList.add(errMsg);
						}
						hasDup = true;
					} else {
						dupCheckLists[j].add(hashValue);
					}
				}
			}
			if(hasDup) hasError = true;
			
			// 코드 변환 체크
			for(i=0; i<cdCvtMapper.length;i++){
				attrId = cdCvtMapper[i][0];
				checkValue = map.get(attrId);
				
				if(checkValue!=null && !checkValue.isEmpty()){
					
					// 역할 - 콤마 구분자로 들어 있음
					if(attrId.equals("roleNms")){
						
						for(String role : checkValue.split(",")){
							role = role.trim();
							if(role.isEmpty()) continue;
							
							codeClsNm = cdCvtMapper[i][2];
							errLineHeader = tabNm+cdCvtMapper[i][1];
							role = orExcelUploadData.getCd(codeClsNm, role, errLineHeader, errorList);
							if(role==null){
								hasError = true;
							}
						}
						
					} else {
						codeClsNm = cdCvtMapper[i][2];
						errLineHeader = tabNm+cdCvtMapper[i][1];
						checkValue = orExcelUploadData.getCd(codeClsNm, checkValue, errLineHeader, errorList);
						if(checkValue==null){
							hasError = true;
						}
					}
				}
			}
			
		}
		if(hasError) return;
		
		Map<Integer, OrOdurBVo> storedOdurMap = new HashMap<Integer, OrOdurBVo>();
		loadUserMap(storedOdurMap, langTypCd);
		//Map<Integer, OrUserBVo> storedUserMap = new HashMap<Integer, OrUserBVo>();
		//loadUserMap(storedOdurMap, storedUserMap, langTypCd);
		
		for(Map<String, String> map : list){
			
			// 조직 정보
			orgNm = map.get("orgNm");
			orOrgBVo = orExcelUploadData.getOrg(orgNm);
			
			
			///////////////////////////////////////////
			//
			//     회사별 사용자 삭제
			
			compId = orOrgBVo.getCompId();
			if(!compIdList.contains(compId)){
				compIdList.add(compId);
				
//				orRescBVo = new OrRescBVo();
//				orRescBVo.setInstanceQueryId("com.innobiz.orange.web.or.dao.OrRescBDao.deleteOrRescBByExcelUser");
//				orRescBVo.setCompId(compId);
//				queryQueue.delete(orRescBVo);
//				
//				// 사용자역할관계(OR_USER_ROLE_R) 테이블
//				orUserRoleRVo = new OrUserRoleRVo();
//				orUserRoleRVo.setCompId(compId);
//				queryQueue.delete(orUserRoleRVo);
//				
//				// 사용자이미지상세(OR_USER_IMG_D) 테이블
//				orUserImgDVo = new OrUserImgDVo();
//				orUserImgDVo.setCompId(compId);
//				queryQueue.delete(orUserImgDVo);
//				
//				// 사용자비밀번호상세(OR_USER_PW_D) 테이블
//				orUserPwDVo = new OrUserPwDVo();
//				orUserPwDVo.setCompId(compId);
//				queryQueue.delete(orUserPwDVo);
//				
//				// 사용자개인정보상세(OR_USER_PINFO_D) 테이블
//				orUserPinfoDVo = new OrUserPinfoDVo();
//				orUserPinfoDVo.setCompId(compId);
//				queryQueue.delete(orUserPinfoDVo);
//				
//				// 원직자기본(OR_ODUR_B) 테이블
//				orOdurBVo = new OrOdurBVo();
//				orOdurBVo.setCompId(compId);
//				queryQueue.delete(orOdurBVo);
//				
//				// 사용자기본(OR_USER_B) 테이블
//				orUserBVo = new OrUserBVo();
//				orUserBVo.setCompId(compId);
//				queryQueue.delete(orUserBVo);
			}
			
			// 저장된 원직자 정보
			storedOrOdurBVo = storedOdurMap.get(hash(map.get("lginId")));
			
			///////////////////////////////////////////
			//
			//     사용자기본(OR_USER_B) 테이블
			
			orUserBVo = new OrUserBVo();
			
			// 리소스 매핑
			rescId = storedOrOdurBVo==null ? null : storedOrOdurBVo.getRescId();
			for(String lang : langList){
				rescVa = map.get("userNm-"+lang);
				if(rescVa!=null && !rescVa.isEmpty()){
					// 리소스ID
					if(rescId==null){
						rescId = orCmSvc.createId("OR_RESC_B");
						orUserBVo.setRescId(rescId);
						orUserBVo.setUserNm(rescVa);
					}
					
					// 리소스 저장
					orRescBVo = new OrRescBVo();
					orRescBVo.setRescId(rescId);
					orRescBVo.setLangTypCd(lang);
					orRescBVo.setRescVa(rescVa);
					if(storedOrOdurBVo==null){
						queryQueue.insert(orRescBVo);
					} else {
						queryQueue.store(orRescBVo);
					}
				}
			}
			
			//if(rescId==null) continue;
			
			if(storedOrOdurBVo==null){
				odurUid = orCmSvc.createId("OR_USER_B");
			} else {
				odurUid = storedOrOdurBVo.getOdurUid();
			}
			
			orUserBVo.setUserUid(odurUid);
			orUserBVo.setOdurUid(odurUid);
			orUserBVo.setCompId(compId);
			orUserBVo.setOrgId(orOrgBVo.getOrgId());
			orUserBVo.setOrgRescId(orOrgBVo.getRescId());
			orUserBVo.setDeptId(orOrgBVo.getDeptId());
			orUserBVo.setDeptRescId(orOrgBVo.getDeptRescId());
			
			// 정렬 순서
			hashId = Hash.hashId(orOrgBVo.getOrgId());
			sortOrdr = sortOrdrMap.get(hashId);
			if(sortOrdr == null) sortOrdr = 1;
			else sortOrdr++;
			sortOrdrMap.put(hashId, sortOrdr);
			orUserBVo.setSortOrdr(sortOrdr.toString());
			
			// 코드 변환
			codeClsNm = isKo ? "보안등급" : "Security Level";
			orUserBVo.setSeculCd(orExcelUploadData.getCd(codeClsNm, map.get("seculNm"), tabNm+codeClsNm, errorList));
			codeClsNm = isKo ? "직급" : "Grade";
			orUserBVo.setGradeCd(orExcelUploadData.getCd(codeClsNm, map.get("gradeNm"), tabNm+codeClsNm, errorList));
			codeClsNm = isKo ? "직책" : "Title";
			orUserBVo.setTitleCd(orExcelUploadData.getCd(codeClsNm, map.get("titleNm"), tabNm+codeClsNm, errorList));
			codeClsNm = isKo ? "직위" : "Position";
			orUserBVo.setPositCd(orExcelUploadData.getCd(codeClsNm, map.get("positNm"), tabNm+codeClsNm, errorList));
			codeClsNm = isKo ? "직무" : "Duty";
			orUserBVo.setDutyCd(orExcelUploadData.getCd(codeClsNm, map.get("dutyNm"), tabNm+codeClsNm, errorList));
			
			codeClsNm = isKo ? "상태" : "User Status";
			orUserBVo.setUserStatCd(orExcelUploadData.getCd("UserStatusCd", map.get("userStatNm"), tabNm+codeClsNm, errorList));
			if(orUserBVo.getUserStatCd()==null) orUserBVo.setUserStatCd("02");// 사용자상태코드 - 02:근무중
			
			orUserBVo.setAduTypCd("01");//겸직구분코드 - 01:원직
			orUserBVo.setTichCont(map.get("tichCont"));//담당업무내용
			
			orUserBVo.setRegrUid(userUid);
			orUserBVo.setRegDt("sysdate");
			orUserBVo.setModrUid(userUid);
			orUserBVo.setModDt("sysdate");
			
			if(storedOrOdurBVo==null){
				queryQueue.insert(orUserBVo);
			} else {
				queryQueue.store(orUserBVo);
			}
			
			
			///////////////////////////////////////////
			//
			//     원직자기본(OR_ODUR_B) 테이블
			
			orOdurBVo = new OrOdurBVo();
			orOdurBVo.setOdurUid(odurUid);
			
			orOdurBVo.setLginId(map.get("lginId"));
			orOdurBVo.setUserNm(orUserBVo.getUserNm());
			orOdurBVo.setRescId(orUserBVo.getRescId());
			orOdurBVo.setRid(map.get("rid"));
			orOdurBVo.setEin(map.get("ein"));
			orOdurBVo.setUserStatCd(orUserBVo.getUserStatCd());
			orOdurBVo.setEntraYmd(toYmdString(map.get("entraYmd")));
			
			tempCd = map.get("genNm")==null ? "M" : map.get("genNm");
			codeClsNm = isKo ? "성별" : "Gender";
			orOdurBVo.setGenCd(orExcelUploadData.getCd("Gender", tempCd, tabNm+codeClsNm, errorList));
			if(orOdurBVo.getGenCd()==null) orOdurBVo.setGenCd("M");
			orOdurBVo.setApvPwTypCd("SYS");

			orOdurBVo.setRegrUid(userUid);
			orOdurBVo.setRegDt("sysdate");
			orOdurBVo.setModrUid(userUid);
			orOdurBVo.setModDt("sysdate");
			
			// 사용자 업로드 - 단계2
			// 동기화단계코드:EX_UPDATED 으로 변경 - 데이터 있는 것만
			orOdurBVo.setSyncStepCd("EX_UPDATED");
			
			if(storedOrOdurBVo==null){
				queryQueue.insert(orOdurBVo);
			} else {
				queryQueue.store(orOdurBVo);
			}
			
			///////////////////////////////////////////
			//
			//     사용자역할관계(OR_USER_ROLE_R) 테이블
			
			// 해당 사용자 역할 관계 삭제
			orUserRoleRVo = new OrUserRoleRVo();
			orUserRoleRVo.setUserUid(odurUid);
			queryQueue.delete(orUserRoleRVo);
			
			// 역할 관계
			roleNms = map.get("roleNms");
			codeClsNm = isKo ? "역할" : "Role";
			if(roleNms != null && !roleNms.isEmpty()){
				roles = roleNms.split(",");
				for(i=0;i<roles.length;i++){
					value = roles[i].trim();
					if(value.isEmpty()) continue;
					
					roleCd = orExcelUploadData.getCd(codeClsNm, value, tabNm+codeClsNm, errorList);
					if(roleCd == null) continue;
					
					// 사용자역할관계(OR_USER_ROLE_R) 테이블
					orUserRoleRVo = new OrUserRoleRVo();
					orUserRoleRVo.setUserUid(odurUid);
					orUserRoleRVo.setRoleCd(roleCd);
					queryQueue.insert(orUserRoleRVo);
				}
			}
			
			
			///////////////////////////////////////////
			//
			//     사용자개인정보상세(OR_USER_PINFO_D) 테이블
			
			orUserPinfoDVo = new OrUserPinfoDVo();
			orUserPinfoDVo.setOdurUid(odurUid);
			
			value = toPhone(map.get("mbno"), tabNm+errMbno, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setMbnoEnc(cryptoSvc.encryptPersanal(value));
			}
			value = map.get("email");
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setEmailEnc(cryptoSvc.encryptPersanal(value));
			}
			value = map.get("extnEmail");
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setExtnEmailEnc(cryptoSvc.encryptPersanal(value));
			}
			value = toPhone(map.get("homePhon"), tabNm+errHomePhon, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setHomePhonEnc(cryptoSvc.encryptPersanal(value));
			}
			value = toPhone(map.get("homeFno"), tabNm+errHomeFno, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setHomeFnoEnc(cryptoSvc.encryptPersanal(value));
			}
			
			value = toZipNo(map.get("homeZipNo"), tabNm+errHomeZip, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setHomeZipNoEnc(cryptoSvc.encryptPersanal(value));
			}
			value = map.get("homeAdr");
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setHomeAdrEnc(cryptoSvc.encryptPersanal(value));
			}
			value = map.get("homeDetlAdr");
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setHomeDetlAdrEnc(cryptoSvc.encryptPersanal(value));
			}
			value = toPhone(map.get("compPhon"), tabNm+errCompPhon, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setCompPhonEnc(cryptoSvc.encryptPersanal(value));
			}
			value = toPhone(map.get("compFno"), tabNm+errCompFno, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setCompFnoEnc(cryptoSvc.encryptPersanal(value));
			}
			
			value = toZipNo(map.get("compZipNo"), tabNm+errOfficeZip, errorList);
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setCompZipNoEnc(cryptoSvc.encryptPersanal(value));
			}
			value = map.get("compAdr");
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setCompAdrEnc(cryptoSvc.encryptPersanal(value));
			}
			value = map.get("compDetlAdr");
			if(value != null && !value.isEmpty()){
				orUserPinfoDVo.setCompDetlAdrEnc(cryptoSvc.encryptPersanal(value));
			}
			
			if(storedOrOdurBVo==null){
				queryQueue.insert(orUserPinfoDVo);
			} else {
				queryQueue.store(orUserPinfoDVo);
			}
			
			
			///////////////////////////////////////////
			//
			//     사용자비밀번호상세(OR_USER_PW_D) 테이블
			
			orUserPwDVo = new OrUserPwDVo();
			orUserPwDVo.setOdurUid(odurUid);
			
			orUserPwDVo.setPwTypCd("SYS");
			orUserPwDVo.setPwEnc(cryptoSvc.encryptPw(map.get("pw").trim(), odurUid));
			orUserPwDVo.setModDt("sysdate");
			orUserPwDVo.setModrUid(userUid);
			
			if(storedOrOdurBVo==null){
				queryQueue.insert(orUserPwDVo);
			} else {
				queryQueue.store(orUserPwDVo);
			}
		}
		
		// 사용자 업로드 - 단계3
		// 동기화단계코드:EX_INIT 으로 남아 있는 데이터 삭제
		
		// 원직자 리소스 - 삭제
		orRescBVo = new OrRescBVo();
		orRescBVo.setWhereSqllet("AND RESC_ID IN (SELECT RESC_ID FROM OR_ODUR_B WHERE SYNC_STEP_CD = 'EX_INIT')");
		queryQueue.delete(orRescBVo);
		
		// 사용자이미지상세 - 삭제
		orUserImgDVo = new OrUserImgDVo();
		orUserImgDVo.setWhereSqllet("AND USER_UID IN ("+
				"SELECT USER_UID FROM OR_USER_B WHERE ODUR_UID IN "+
				"(SELECT ODUR_UID FROM OR_ODUR_B WHERE SYNC_STEP_CD = 'EX_INIT')"+
				")");
		queryQueue.delete(orUserImgDVo);

		// 사용자비밀번호상세 - 삭제
		orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setWhereSqllet("AND ODUR_UID IN ("+
				"SELECT ODUR_UID FROM OR_ODUR_B WHERE SYNC_STEP_CD = 'EX_INIT'"+
				")");
		queryQueue.delete(orUserPwDVo);

		// 사용자개인정보상세 - 삭제
		orUserPinfoDVo = new OrUserPinfoDVo();
		orUserPinfoDVo.setWhereSqllet("AND ODUR_UID IN ("+
				"SELECT ODUR_UID FROM OR_ODUR_B WHERE SYNC_STEP_CD = 'EX_INIT'"+
				")");
		queryQueue.delete(orUserPinfoDVo);

		// 사용자기본 - 삭제
		orUserBVo = new OrUserBVo();
		orUserBVo.setWhereSqllet("AND ODUR_UID IN ("+
				"SELECT ODUR_UID FROM OR_ODUR_B WHERE SYNC_STEP_CD = 'EX_INIT'"+
				")");
		queryQueue.delete(orUserBVo);

		// 원직자기본 - 삭제
		orOdurBVo = new OrOdurBVo();
		orOdurBVo.setWhereSqllet("AND SYNC_STEP_CD = 'EX_INIT'");
		queryQueue.delete(orOdurBVo);
		

		// 사용자 업로드 - 단계4
		// 동기화 코드 초기화
		orOdurBVo = new OrOdurBVo();
		orOdurBVo.setSyncStepCd("");
		orOdurBVo.setWhereSqllet("AND SYNC_STEP_CD = 'EX_UPDATED'");
		queryQueue.update(orOdurBVo);
		
	}
	
	/** 저장된 원직자기본, 사용자기본 테이블 로드함 */
	private void loadUserMap(Map<Integer, OrOdurBVo> orOdurBVoMap, String langTypCd) throws SQLException{
		
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setQueryLang(langTypCd);
		@SuppressWarnings("unchecked")
		List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonDao.queryList(orOdurBVo);
		if(orOdurBVoList != null){
			for(OrOdurBVo storedOrOdurBVo : orOdurBVoList){
				orOdurBVoMap.put(hash(storedOrOdurBVo.getLginId()), storedOrOdurBVo);
			}
		}

//		//Map<Integer, OrUserBVo> orUserBVoMap, 
//		// 사용자기본(OR_USER_B) 테이블
//		OrUserBVo orUserBVo = new OrUserBVo();
//		orUserBVo.setQueryLang(langTypCd);
//		@SuppressWarnings("unchecked")
//		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
//		if(orUserBVoList != null){
//			for(OrUserBVo storedOrUserBVo : orUserBVoList){
//				orUserBVoMap.put(hash(storedOrUserBVo.getUserUid()), storedOrUserBVo);
//			}
//		}
	}
	
	/** 엑셀 쉬트에서 데이터를 가져옴 */
	public List<Map<String, String>> processSheet(String[][] sheet,
			Map<String, String> attrIdMap,
			String treeAttrId, String treeAttrNm,
			String[] mandatorys){
		if(sheet==null || sheet.length<1) return null;
		
		List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();
		
		int r, c, i, rowCnt = sheet.length, colCnt = sheet[0].length, colSize;
		
		// 데이터 모을 attrId로 전환
		String[] attrIds = new String[colCnt];
		String[] treeNos = null;
		int treeAttrNmLen = 0;
		if(treeAttrNm!=null){
			treeAttrNmLen = treeAttrNm.length();
			treeNos = new String[colCnt];
		}
		for(i=0;i<colCnt;i++){
			if(treeAttrNmLen>0 && sheet[0][i].startsWith(treeAttrNm) && sheet[0][i].indexOf('[')<0){
				attrIds[i] = treeAttrId;
				treeNos[i] = sheet[0][i].substring(treeAttrNmLen);
			} else {
				attrIds[i] = attrIdMap.get(sheet[0][i]);
				if(treeNos!=null) treeNos[i] = null;
			}
		}
		
		boolean valid;
		String[] row;
		String attrId, value;
		Map<String, String> map;
		for(r = 1; r<rowCnt; r++){
			map = new HashMap<String, String>();
			row = sheet[r];
			if(row==null){
				continue;
			}
			colSize = row.length;
			
			// map 에 엑셀 데이터 담기
			for(c=0;c<colCnt;c++){
				attrId = attrIds[c];
				if(c<row.length){
					value = row[c];
					if(value!=null) value = value.trim();
					if(c<colSize && attrId != null && value != null && !value.isEmpty()) {
						map.put(attrId, value);
						if(treeNos != null && treeNos[c] != null){
							map.put("treeLevel", treeNos[c]);
						}
					}
				}
			}
			
			// 필수 항목 있는지 검사
			valid = true;
			for(i=0; i<mandatorys.length; i++){
				value = map.get(mandatorys[i]);
				if(value==null || value.isEmpty()){
					valid = false;
					break;
				}
			}
			
			if(valid){
				returnList.add(map);
			}
		}
		
		return returnList;
	}

	/** 날짜형으로 변경 - YMD */
	private String toYmdString(String text){
		if(text==null || text.isEmpty()) return null;
		int len = text.length();
		if(len==8){
			return text.substring(0, 4)+"-"+text.substring(4,6)+"-"+text.substring(6);
		} else if(len==10){
			return text.substring(0, 4)+"-"+text.substring(5,7)+"-"+text.substring(8);
		} else {
			return null;
		}
	}
	
	/** 전화번호 포멧으로 변경 */
	private String toPhone(String text, String errCat, List<String> errorList){
		if(text==null || text.isEmpty()) return null;
		if(exPhoneEnable){
			return text.trim();
		}
		
		text = escapeExcelNumber(text);
		if(text.isEmpty()) return null;
		
		text = text.replace("-", "").trim();
		if(text.isEmpty()) return null;
		int len = text.length();
		
		// 엑셀 첫자리 0 지워지는것 방지
		if(len>8 && text.charAt(0)!='0'){
			text = "0"+text;
			len++;
		}
		
		if(len>5 && len<12 && isNumeric(text)){
			if(len<9){
				return "-"+text.substring(0, len-4)+"-"+text.substring(len-4);
			} else if(text.startsWith("02")){
				if(len<11){
					return text.substring(0, 2)+"-"+text.substring(2, len-4)+"-"+text.substring(len-4);
				}
			} else if(len<12){
				return text.substring(0, 3)+"-"+text.substring(3, len-4)+"-"+text.substring(len-4);
			}
		}
		
		String errorMsg = errCat+" : "+text;
		if(!errorList.contains(errorMsg)){
			errorList.add(errorMsg);
		}
		return null;
	}
	
	/** 엑셀의 숫자 변형 - 원복 */
	private String escapeExcelNumber(String text){
		text = text.trim();
		if(text.endsWith(".0")){
			text = text.substring(0, text.length()-2);
		} else if(text.length()>2 && text.charAt(1)=='.' && text.indexOf('E')>3){
			text = text.charAt(0) + text.substring(2, text.indexOf('E'));
		}
		return text;
	}
	
	/** 우편번호 포멧으로 변경 */
	private String toZipNo(String text, String errCat, List<String> errorList){
		if(text==null || text.isEmpty()) return null;
		text = escapeExcelNumber(text);
		if(text.isEmpty()) return null;
		
		int len = text.length();
		if(len==5 || len==6){
			return text;
		}
		
//		// 엑셀 첫자리 0 지워지는것 방지
//		if(len==5 && text.charAt(0)!='0'){
//			text = "0"+text;
//			len++;
//		}
//		
//		if(len==7){
//			if(text.charAt(3)=='-' && isNumeric(text.substring(0, 3)) && isNumeric(text.substring(4))){
//				return text;
//			}
//		} else if(len==6){
//			if(isNumeric(text)){
//				return text.substring(0, 3)+"-"+text.substring(3);
//			}
//		}
		
		String errorMsg = errCat+" : "+text;
		if(!errorList.contains(errorMsg)){
			errorList.add(errorMsg);
		}
		return null;
	}
	
	/** 숫자인지 테스트 */
	private static boolean isNumeric(String s) {
	    return java.util.regex.Pattern.matches("\\d+", s);
	}
	
	/** 해쉬 - String의 내장 hash 는 겹치는 경우가 많이 발생해서 전환함 */
	private static int hash(String text){
		int hashValue = 0;
		for(char c : text.toCharArray()){
			hashValue = c + (hashValue << 6) + (hashValue << 16) - hashValue;
		}
		return hashValue;
	}
}
