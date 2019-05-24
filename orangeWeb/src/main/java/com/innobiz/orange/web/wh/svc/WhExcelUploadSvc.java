package com.innobiz.orange.web.wh.svc;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.ExcelReader;
import com.innobiz.orange.web.cm.utils.IdUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wh.vo.WhMdBVo;
import com.innobiz.orange.web.wh.vo.WhReqBVo;
import com.innobiz.orange.web.wh.vo.WhReqCmplDVo;
import com.innobiz.orange.web.wh.vo.WhReqOngdDVo;
import com.innobiz.orange.web.wh.vo.WhReqStatDVo;
import com.innobiz.orange.web.wh.vo.WhRescBVo;

@Service
public class WhExcelUploadSvc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WhExcelUploadSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 서비스 */
	@Autowired
	private WhCmSvc whCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "whRescSvc")
	private WhRescSvc whRescSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 헬프데스크 서비스 */
	@Resource(name = "whHpSvc")
	private WhHpSvc whHpSvc;
	
	/** 헬프데스크 관리 서비스 */
	@Resource(name = "whAdmSvc")
	private WhAdmSvc whAdmSvc;
	
	/** 문서번호 서비스 */
	@Resource(name = "whDocNoSvc")
	private WhDocNoSvc whDocNoSvc;
	
	/** 저장된 전체 모듈ID 목록 */
	private List<String> storedMdList = null;
	
	/** 모듈ID 속성ID */
	private String[] mdIdAttrIds = {"no", "pno"};
	
	/** 확장 언어
	 * th : 태국어
	 *  */
	private String[] extLangs = { "th" };
	
	/** 엑셀 일괄 저장 */
	public void processExcel(File file, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws IOException, SQLException, CmException{
		
		String langTypCd = userVo.getLangTypCd();
		
		boolean isKo = "ko".equals(langTypCd);
		String[] tabNames = isKo ? new String[]{"모듈", "데이터"} : new String[]{"Module", "Data"};
		Map<String, String[][]> map = ExcelReader.readToMap(file, tabNames);
		String[][] sheet;
		
		String[] mandatorys = isKo ? new String[]{"모듈"} : new String[]{"Module"};
		
		// 탭 체크
		for(int i=0;i<mandatorys.length;i++){
			sheet = map.get(mandatorys[i]);
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
		for(int i=0;i<tabNames.length;i++){
			sheet = map.get(tabNames[i]);
			if(sheet==null) continue;
			if(i==0){
				processMd(sheet, queryQueue, userVo, errorList);
			}else if(i==1){
				processData(sheet, queryQueue, userVo, errorList);
			}
			if(!errorList.isEmpty()) break;
		}
	}
	
	/** 엑셀 일괄 저장 */
	public void processExcelData(File file, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws IOException, SQLException, CmException{
		
		String langTypCd = userVo.getLangTypCd();
		
		boolean isKo = "ko".equals(langTypCd);
		String[] tabNames = isKo ? new String[]{"데이터"} : new String[]{"Data"};
		Map<String, String[][]> map = ExcelReader.readToMap(file, tabNames);
		String[][] sheet;
		
		// 탭 체크
		for(int i=0;i<1;i++){
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
		
		// 모듈ID 목록
		if(storedMdList==null)
			storedMdList=new ArrayList<String>();
		
		// 정렬순서 맵
		loadMdMap(null, null, storedMdList, langTypCd, userVo.getCompId());
				
		// 엑셀 작업
		for(int i=0;i<1;i++){
			sheet = map.get(tabNames[i]);
			if(sheet==null) continue;
			if(i==0){
				processData(sheet, queryQueue, userVo, errorList);
			}
			if(!errorList.isEmpty()) break;
		}
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
	
	/** 모듈 엑셀 데이터 처리 */
	private void processMd(String[][] sheet, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws SQLException{
		String langTypCd = userVo.getLangTypCd();
		boolean isKo = "ko".equals(langTypCd);
		
		List<String> langList = null;
		
		// 타이틀 항목 전환 맵
		Map<String, String> attrIdMap = new HashMap<String, String>();
		if(isKo){
			attrIdMap.put("번호", "no");
			attrIdMap.put("모듈명", "mdNm-ko");
			attrIdMap.put("모듈명[국문]", "mdNm-ko");
			attrIdMap.put("모듈명[영문]", "mdNm-en");
			attrIdMap.put("모듈명[일문]", "mdNm-ja");
			attrIdMap.put("모듈명[중문]", "mdNm-zh");
			for(String lang : extLangs){
				attrIdMap.put("모듈명["+lang+"]", "mdNm-"+lang);
			}
			attrIdMap.put("상위모듈번호", "pno");
			attrIdMap.put("모듈구분코드", "mdTypCd");
			attrIdMap.put("사용여부", "useYn");
			
			//TODO
			langList = getKoLangList();
		} else {
			attrIdMap.put("No", "no");
			attrIdMap.put("Parent No", "pno");
			attrIdMap.put("Module Type Cd", "mdTypCd");
			attrIdMap.put("UseYn", "useYn");
			// 코드명
			langList = parseHeaderLang(sheet[0], "Module Name", "mdNm", langTypCd, attrIdMap);
		}
		// 필수 항목
		String[] mandatorys =  new String[]{"no", "pno", "mdTypCd", "mdNm-"+langTypCd};
		
		// 엑셀 리스트
		List<Map<String, String>> list = processSheet(sheet, attrIdMap, null, null, mandatorys);
		if(list==null || list.isEmpty()) return;
		
		String tabNm = isKo ? "[모듈 탭] - " : "[Module Sheet] - ";
		String errDupNo = isKo ? "중복된 [번호]" : "Duplicated [Number]";
		
		String no, pno, rescId, useYn, rescVa, userUid = userVo.getUserUid(), compId=userVo.getCompId();		
		
		Integer sortOrdr = 0, hashValue;
		// 중복 목록
		List<Integer> dupCheckList = new ArrayList<Integer>();
		String errMsg;
		boolean hasError = false;
		
		//Entry<String, String> entry;
		// 오류 체크
		for(Map<String, String> map : list){
			no = map.get("no");
			hashValue = hash(no);
			if(dupCheckList.contains(hashValue)){
				errMsg = tabNm+errDupNo+" : "+getStringToInt(no);
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
			} else {
				dupCheckList.add(hashValue);
			}
			
			/*Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				System.out.println("key : "+entry.getKey()+"\t value : "+getStringId(entry.getKey(), entry.getValue()));
			}*/
		}
		if(hasError) return;
		
		//List<String> pnoList= new ArrayList<String>();
		WhMdBVo whMdBVo;
		WhRescBVo whRescBVo;
		WhMdBVo storedWhMdBVo=null;
		Map<String,Object> paramMap=null;
		
		// 모듈 로드
		Map<Integer, WhMdBVo> storedMdMap = new HashMap<Integer, WhMdBVo>();
		
		// 모듈ID 목록
		if(storedMdList==null)
			storedMdList=new ArrayList<String>();
		
		// 정렬순서 맵
		Map<String, Integer> sortOrderMap = new HashMap<String, Integer>();
		loadMdMap(sortOrderMap, storedMdMap, storedMdList, langTypCd, compId);
		
		for(Map<String, String> map : list){
			// 모듈ID 로 변환
			no=getStringId("no", map.get("no"));
			pno=getStringId("pno", map.get("pno"));
			
			// 상위모듈ID 기준
			if(sortOrderMap.get(pno)==null) sortOrdr = 0;	
			else sortOrdr = sortOrderMap.get(pno);
			sortOrderMap.put(pno, ++sortOrdr);
			
			whMdBVo = new WhMdBVo();
			
			storedWhMdBVo=storedMdMap.get(hash(no));
			// 리소스 매핑
			rescId = storedWhMdBVo==null ? null : storedWhMdBVo.getRescId();
			if(!hasError){
				paramMap=new HashMap<String,Object>();
				for(String lang : langList){
					rescVa = map.get("mdNm-"+lang);
					if(rescVa!=null && !rescVa.isEmpty()){						
						paramMap.put("mdRescVa_"+lang, rescVa);
					}
				}
				whRescBVo=null;
				if(paramMap.size()>0){
					if(rescId!=null) paramMap.put("mdRescId", rescId);
					whRescBVo=collectWhRescBVo(langTypCd, userVo, "md", queryQueue, paramMap);
				}
				
				// 모듈에 리소스ID, 값 세팅
				if(whRescBVo!=null){
					whMdBVo.setRescId(whRescBVo.getRescId());
					whMdBVo.setMdNm(whRescBVo.getRescVa());
				}				
			}
			
			useYn=map.get("useYn");
			if(useYn==null) useYn="Y";
			whMdBVo.setMdId(no);
			whMdBVo.setCompId(compId);
			whMdBVo.setMdPid(pno);
			whMdBVo.setMdTypCd(map.get("mdTypCd"));
			whMdBVo.setCatGrpId(null);
			whMdBVo.setUseYn(useYn);
			whMdBVo.setSortOrdr(sortOrdr);
			
			if(storedWhMdBVo==null){
				storedMdList.add(no);
				whMdBVo.setRegrUid(userUid);
				whMdBVo.setRegDt("sysdate");
				queryQueue.insert(whMdBVo);
			} else {
				whMdBVo.setModrUid(userUid);
				whMdBVo.setModDt("sysdate");
				queryQueue.store(whMdBVo);
			}
			
			/*Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				System.out.println("key : "+entry.getKey()+"\t value : "+getStringId(entry.getKey(), entry.getValue()));
			}*/
		}
		
	}
	
	
	/** 요청 엑셀 데이터 처리 */
	private void processData(String[][] sheet, QueryQueue queryQueue, UserVo userVo, List<String> errorList) throws SQLException, CmException{
		String langTypCd = userVo.getLangTypCd();
		boolean isKo = "ko".equals(langTypCd);
		
		// 타이틀 항목 전환 맵
		Map<String, String> attrIdMap = new HashMap<String, String>();
		attrIdMap.put("번호", "no");
		attrIdMap.put("문서번호", "docNo");
		attrIdMap.put("요청자UID", "regrUid");
		attrIdMap.put("요청자명", "regrNm");
		attrIdMap.put("제목", "subj");
		attrIdMap.put("내용", "cont");
		attrIdMap.put("요청일", "reqDt");
		attrIdMap.put("프로그램ID", "progrmId");
		attrIdMap.put("프로그램명", "progrmNm");
		attrIdMap.put("의뢰서번호", "writternReqNo");
		attrIdMap.put("모듈번호", "mdNo");
		attrIdMap.put("상태코드", "statCd");
		attrIdMap.put("담당자UID", "pichUid");
		attrIdMap.put("담당자명", "pichNm");
		attrIdMap.put("접수자UID", "recvUid");
		attrIdMap.put("접수자명", "recvNm");
		attrIdMap.put("접수일", "recvDt");
		attrIdMap.put("접수내용", "recvCont");
		attrIdMap.put("처리자UID", "hdlrUid");
		attrIdMap.put("처리자명", "hdlrNm");
		attrIdMap.put("유형번호", "catNo");
		attrIdMap.put("완료예정일", "cmplDueDt");
		attrIdMap.put("완료일", "cmplDt");
		attrIdMap.put("처리내용", "hdlCont");
		attrIdMap.put("공수값", "devHourVa");
		attrIdMap.put("처리업체명", "hdlCompNm");
		attrIdMap.put("처리비용", "hdlCost");
		attrIdMap.put("테스트시작일", "strtDt");
		attrIdMap.put("테스트종료일", "endDt");
		attrIdMap.put("테스트담당자UID", "testPichUid");
		attrIdMap.put("테스트담당자명", "testPichNm");
		attrIdMap.put("테스트진행내용", "testOngoCont");
		attrIdMap.put("테스트결과내용", "testResCont");
		
		// 필수 항목
		String[] mandatorys =  new String[]{"no", "regrUid", "subj", "reqDt", "mdNo", "hdlrUid", "catNo", "cmplDt"};
		
		// 완료 상태일 겨웅
		
		// 엑셀 리스트
		List<Map<String, String>> list = processSheet(sheet, attrIdMap, null, null, mandatorys);
		if(list==null || list.isEmpty()) return;
		
		String tabNm = isKo ? "[데이터 탭] - " : "[Module Sheet] - ";
		String errDupNo = isKo ? "중복된 [번호]" : "Duplicated [Number]";
		String errNotUser = isKo ? "사용자 [없음]" : "User [Empty]";
		String errNotMd = isKo ? "모듈 [없음]" : "Module [Empty]";
		
		
		String no, mdNo, mdId, regrUid, compId=userVo.getCompId();
		
		Integer hashValue;
		// 중복 목록
		List<Integer> dupCheckList = new ArrayList<Integer>();
		String errMsg;
		boolean hasError = false;
		
		Map<String, OrUserBVo> userMap = new HashMap<String, OrUserBVo>();
		OrUserBVo orUserBVo=null;
		
		//Entry<String, String> entry;
		
		/*for(String testId : storedMdList){
			System.out.println("testId : "+testId);
		}*/
		
		// 오류 체크
		for(Map<String, String> map : list){
			no = getIntToString(map.get("no"));
			hashValue = hash(no);
			// 중복체크
			if(dupCheckList.contains(hashValue)){
				errMsg = tabNm+errDupNo+" : "+getStringToInt(no);
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
			} else {
				dupCheckList.add(hashValue);
			}
			
			// 사용자 조회
			regrUid=map.get("regrUid");
			orUserBVo=getOrUserBVo(langTypCd, regrUid);
			if(orUserBVo==null || (orUserBVo!=null && !orUserBVo.getCompId().equals(compId))){
				errMsg = tabNm+errNotUser+" : "+getStringToInt(no);
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
			}else{
				userMap.put(regrUid, orUserBVo);
			}
			
			// 모듈ID 체크
			mdNo=map.get("mdNo");
			mdId=mdNo.charAt(0)=='C' || mdNo.charAt(0)=='M' ? mdNo : getStringId("no", mdNo);
			
			if(storedMdList!=null && !storedMdList.contains(mdId)){
				errMsg = tabNm+errNotMd+" : "+getStringToInt(no);
				if(!errorList.contains(errMsg)){
					errorList.add(errMsg);
				}
				hasError = true;
			}
			/*Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				System.out.println("key : "+entry.getKey()+"\t value : "+entry.getValue());
			}*/
		}
		if(hasError) return;
		
		WhReqBVo whReqBVo = null;
		WhReqBVo storedWhReqBVo=null;
		
		// 데이터 로드
		Map<Integer, WhReqBVo> storedDataMap = new HashMap<Integer, WhReqBVo>();
		loadDataMap(storedDataMap, userVo.getCompId(), langTypCd);
		
		WhReqStatDVo whReqStatDVo;
		WhReqOngdDVo whReqOngdDVo;
		WhReqCmplDVo whReqCmplDVo;
		String reqNo,  writtenReqNo, docNo;
		
		String newCreate = isKo ? "신규" : "new";
		
		// 환경설정 - model에 추가(파일사용여부, 결과평가사용여부)
		Map<String, String> configMap=whHpSvc.getEnvConfigAttr(null, userVo.getCompId(), null);
		
		boolean isEditor;
		for(Map<String, String> map : list){
			regrUid=map.get("regrUid");
			orUserBVo=userMap.get(regrUid);
			no=getIntToString(map.get("no"));
			storedWhReqBVo=storedDataMap.get(hash(no));
			
			// 요청번호
			reqNo=storedWhReqBVo==null ? whCmSvc.createNo("WH_REQ_B").toString() : storedWhReqBVo.getReqNo();
			map.put("reqNo", reqNo);
			// 모듈ID 체크
			mdNo=map.get("mdNo");
			mdId=mdNo.charAt(0)=='C' || mdNo.charAt(0)=='M' ? mdNo : getStringId("no", mdNo);
			map.put("mdId", mdId);
			
			whReqBVo = new WhReqBVo();
			
			// 데이터 매핑
			setParamToVo(map, whReqBVo);
			
			whReqBVo.setCompId(orUserBVo.getCompId());
			whReqBVo.setDeptId(orUserBVo.getDeptId());
			writtenReqNo=map.get("writtenReqNo");
			if(writtenReqNo==null)
				whReqBVo.setWrittenReqNo("");
			whReqBVo.setReqNo(reqNo);
			whReqBVo.setMdId(mdId);
			whReqBVo.setUploadNo(no);
			
			if(storedWhReqBVo==null){
				docNo=map.get("docNo");
				if(docNo!=null && docNo.equals(newCreate))
					whDocNoSvc.setDocNo(whReqBVo, langTypCd, orUserBVo.getCompId(), orUserBVo.getOrgId(), whReqBVo.getMdId(), regrUid);
				whReqBVo.setRegrUid(regrUid);
				whReqBVo.setRegDt("sysdate");
				queryQueue.insert(whReqBVo);
			} else {
				whReqBVo.setModrUid(regrUid);
				whReqBVo.setModDt("sysdate");
				queryQueue.store(whReqBVo);
			}
			
			// HTML 사용여부 저장 - 요청
			whReqStatDVo=new WhReqStatDVo();
			if(storedWhReqBVo==null){
				isEditor=configMap.containsKey("reqEditorYn") && "Y".equals(configMap.get("reqEditorYn"));
				if(isEditor) whReqStatDVo.setHtmlYn("Y");
			}
			whHpSvc.saveHtmlYn(null, queryQueue, reqNo, "R", whReqStatDVo); // html여부 저장
			
			// 진행상세
			whReqOngdDVo=new WhReqOngdDVo();
			// 데이터 매핑
			setParamToVo(map, whReqOngdDVo);
			whReqOngdDVo.setStatCd("C"); // 상태코드 '완료' 로 저장
			if(map.get("recvUid")==null)
				whReqOngdDVo.setPrevStatCd("R"); // 이전 상태코드 '요청' 으로 저장
			
			// 진행상세 저장
			whHpSvc.saveProgress(null, queryQueue, reqNo, whReqOngdDVo);
			
			// HTML 사용여부 저장 - 완료
			whReqStatDVo=new WhReqStatDVo();
			if(storedWhReqBVo==null){
				isEditor=configMap.containsKey("cmplEditorYn") && "Y".equals(configMap.get("cmplEditorYn"));
				if(isEditor) whReqStatDVo.setHtmlYn("Y");
			}
			whHpSvc.saveHtmlYn(null, queryQueue, reqNo, "C", whReqStatDVo); // html여부 저장
			
			whReqCmplDVo=new WhReqCmplDVo();
			// 데이터 매핑
			setParamToVo(map, whReqCmplDVo);
			whHpSvc.saveComplete(null, queryQueue, reqNo, whReqCmplDVo);
			
			
			/*Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				System.out.println("key : "+entry.getKey()+"\t value : "+entry.getValue());
			}*/
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
	
	/** [Parameter Map]리소스기본(DM_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WhRescBVo collectWhRescBVo(String langTypCd, UserVo userVo, String prefix, QueryQueue queryQueue, Map<String,Object> paramMap) throws SQLException {
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		// 리소스 prefix 설정
		String rescPrefix = prefix == null || prefix.isEmpty() ? "resc" : prefix + "Resc";
		// rescId 받음 : 없으면 생성
		String rescId = (String)paramMap.get(rescPrefix + "Id"), rescVa;
		if (rescId == null || rescId.isEmpty()) {
			rescId = whCmSvc.createId("WH_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WhRescBVo whRescBVo, firstWhRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = (String)paramMap.get(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				whRescBVo = new WhRescBVo();
				whRescBVo.setRescId(rescId);
				whRescBVo.setLangTypCd(ptCdBVo.getCd());
				whRescBVo.setRescVa(rescVa);

				if (firstWhRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstWhRescBVo = whRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(whRescBVo);
				} else {
					queryQueue.store(whRescBVo);
				}
			}
		}
		
		return firstWhRescBVo;
	}
	
	/** 모듈데이터 로드 */
	private void loadMdMap(Map<String, Integer> sortOrderMap, Map<Integer, WhMdBVo> storedMdMap, List<String> storedMdList, String langTypCd, String compId) throws SQLException{
		
		// 목록 조회
		WhMdBVo whMdBVo = new WhMdBVo();
		whMdBVo.setQueryLang(langTypCd);
		whMdBVo.setOrderBy("MD_PID, SORT_ORDR");
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		if(compId!=null) whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whMdBVo, false);
				
		@SuppressWarnings("unchecked")
		List<WhMdBVo> whMdBVoList = (List<WhMdBVo>)commonDao.queryList(whMdBVo);
		
		if(whMdBVoList != null){
			for(WhMdBVo storedWhMdBVo : whMdBVoList){
				if(storedMdMap!=null)
					storedMdMap.put(hash(storedWhMdBVo.getMdId()), storedWhMdBVo);
				storedMdList.add(storedWhMdBVo.getMdId());
				if(storedWhMdBVo.getMdId().startsWith("C")) continue;
				if(sortOrderMap!=null)
					sortOrderMap.put(storedWhMdBVo.getMdPid(), Integer.valueOf(storedWhMdBVo.getSortOrdr()));
			}
		}
	}
	
	/** 요청데이터 로드 */
	private void loadDataMap(Map<Integer, WhReqBVo> storedDataMap, String compId, String langTypCd) throws SQLException{
		
		// 목록 조회
		WhReqBVo whReqBVo = new WhReqBVo();
		whReqBVo.setQueryLang(langTypCd);
		whReqBVo.setWhereSqllet(" AND T.UPLOAD_NO IS NOT NULL");
		// 회사 ID 조회조건 추가[계열사 설정 확인]
		whAdmSvc.setCompAffiliateIdList(compId, langTypCd, whReqBVo, false);
		@SuppressWarnings("unchecked")
		List<WhReqBVo> whReqBVoList = (List<WhReqBVo>)commonDao.queryList(whReqBVo);
		if(whReqBVoList != null){
			for(WhReqBVo storedWhReqBVo : whReqBVoList){
				storedDataMap.put(hash(storedWhReqBVo.getUploadNo()), storedWhReqBVo);
			}
		}
	}
	
	/** Map to Vo */
	public void setParamToVo(Map<String, String> paramMap, CommonVo commonVo){
		if(paramMap==null || commonVo==null) return;
		
		// 숫자 컬럼 목록
		String[] noChkList = new String[]{"no", "catNo"};
				
		// set header
		Entry<String, String> entry;
		Iterator<Entry<String, String>> iterator = paramMap.entrySet().iterator();
		String key;
		while(iterator.hasNext()){
			entry = iterator.next();
			key = entry.getKey();
			if(ArrayUtil.isInArray(noChkList, key))
				VoUtil.setValue(commonVo, key, getIntToString(entry.getValue()));				
			else
				VoUtil.setValue(commonVo, key, entry.getValue());
		}
	}
	
	/** 사용자 정보 조회 */
	public OrUserBVo getOrUserBVo(String langTypCd, String userUid) throws SQLException{
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setUserUid(userUid);
		orUserBVo.setQueryLang(langTypCd);
		return (OrUserBVo)commonDao.queryVo(orUserBVo);
	}
	
	/** 숫자로 변환 */
	private int getStringToInt(String value){
		Double seq=Double.parseDouble(value);
		return seq.intValue();
	}
	
	/** 숫자로 변환 후 문자열로 리턴*/
	private String getIntToString(String value){
		Double seq=Double.parseDouble(value);
		return String.valueOf(seq.intValue());
	}
	
	/** 모듈ID 로 변환 */
	private String getStringId(String key, String value){
		if(!ArrayUtil.isInArray(mdIdAttrIds, key)) return value;
		int seq = getStringToInt(value);
		if("pno".equals(key) && seq==0) return "ROOT";
		return IdUtil.createId('C', seq, 8);
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
