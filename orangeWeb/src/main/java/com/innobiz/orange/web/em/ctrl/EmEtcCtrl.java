package com.innobiz.orange.web.em.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.tiles.autotag.core.runtime.annotation.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.innobiz.orange.web.cm.config.CacheConfig;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.CmMultipartFile;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ExcelReader;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCacheExpireSvc;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtRescSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.pt.vo.PtRescBVo;
import com.innobiz.orange.web.wb.vo.WbBcBVo;

/** 참석자(사용자,명함등 조회), 기타(코드일괄저장...) */
@Controller
public class EmEtcCtrl {		
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(EmEtcCtrl.class);
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Autowired
	private PtRescSvc ptRescSvc;
	
	/** 캐쉬 만료 처리용 서비스 */
	@Autowired
	private PtCacheExpireSvc ptCacheExpireSvc;
	
	/** 메세지 처리용 프라퍼티 - 다국어 */
	@Autowired
	private MessageProperties messageProperties;
	
	/** 조직도 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** [AJAX] 사용자, 명함 조회 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = {"/wb/srchUserAjx","/wc/srchUserAjx","/wr/srchUserAjx","/ct/schdl/srchUserAjx", "/cu/send/srchUserAjx", "/cu/recv/srchUserAjx"})
	public String srchUserAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {
		
		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String srchTyp = (String) object.get("srchTyp");
			String srchName = (String) object.get("srchName");
			if ( srchTyp == null || srchTyp.isEmpty() || srchName == null || srchName.isEmpty()) {
				return JsonUtil.returnJson(model);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);
			String compId=userVo.getCompId();
			String returnString=null;
			if("user".equals(srchTyp)){
				OrUserBVo orUserBVo = new OrUserBVo();
				orUserBVo.setQueryLang(langTypCd);
				orUserBVo.setRescNm(srchName);
				if(compId!=null) orUserBVo.setCompId(compId);
				orUserBVo.setUserStatCd("02");
				// 목록 조회
				List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonSvc.queryList(orUserBVo);
				if(orUserBVoList!=null && orUserBVoList.size()==1){
					String userUid = orUserBVoList.get(0).getUserUid();
					if(userUid!=null){
						// 사용자 정보 조회
						Map<String, Object> userMap = orCmSvc.getUserMapForSSO(userUid, langTypCd);
						if(userMap!=null)	returnString=JsonUtil.toJson(userMap);
					}
				}
			}else if("bc".equals(srchTyp)){ // 명함
				String schBcRegrUid = (String) object.get("paramUserUid"); // 대리자UID
				
				// 조회조건 매핑
				WbBcBVo wbBcBVo = new WbBcBVo();
				VoUtil.bind(request, wbBcBVo);
				wbBcBVo.setQueryLang(langTypCd);
				wbBcBVo.setCompId(compId);
				wbBcBVo.setSchCat("bcNm");
				wbBcBVo.setSchWord(srchName);
				
				//개인명함
				wbBcBVo.setRegrUid((schBcRegrUid != null && !"".equals(schBcRegrUid)) ? schBcRegrUid : userVo.getUserUid());
				
				List<WbBcBVo> wbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
				if(wbBcBVoList==null){
					wbBcBVo.setRegrUid(null);
					// 공개명함
					wbBcBVo.setSchUserUid(userVo.getUserUid());//사용자UID
					wbBcBVo.setSchCompId(userVo.getCompId());//사용자 회사코드
					wbBcBVo.setSchDeptId(userVo.getDeptId());//사용자 부서코드
					String[] schOpenTypCds =new String[]{"allPubl","deptPubl","apntrPubl"};
					wbBcBVo.setSchOpenTypCds(schOpenTypCds);
					wbBcBVoList = (List<WbBcBVo>)commonSvc.queryList(wbBcBVo);
				}
				if(wbBcBVoList!=null && wbBcBVoList.size()==1){
					returnString=JsonUtil.toJson(wbBcBVoList.get(0));
				}
			}
			
			if(returnString!=null) model.put("returnString", returnString);
			

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	/** 코드 저장 - 엑셀 */
	@RequestMapping(value = "/cm/cdExcelUploadPop")
	public String cdExcelUploadPop(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/em/cdExcelUploadPop");
	}
	
	
	/** 코드 저장 - 엑셀 */
	@RequestMapping(value = "/cm/cdExcelUploadFrm")
	public String cdExcelUploadFrm(HttpServletRequest request,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "excel");
			uploadHandler.upload();
			
			MultipartHttpServletRequest multiRequest = uploadHandler.getMultipartRequest();
			
			String clsCd = ParamUtil.getRequestParam(multiRequest, "clsCd", false);
			
			// Multipart 파일
			MultipartFile mpFile = multiRequest.getFileMap().get("excelFile");
			CmMultipartFile cmf = (CmMultipartFile) mpFile;
			File file = new File(cmf.getSavePath());
			if(file.isFile()){
//				String[][][] cellValue = null;
//				if("xls".equals(cmf.getExt())){
//					cellValue = ExcelReaderUtil.xlsReader(file);
//				}else if("xlsx".equals(cmf.getExt())){
//					cellValue = ExcelReaderUtil.xlsxReader(file);
//				}
				
				List<String[][]> sheetList = ExcelReader.readToList(file);
				
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				if(sheetList != null && !sheetList.isEmpty()){
					
					int size = sheetList.size();
					String[][] sheet;
					
					List<PtCdBVo> ptCdBVoList = new ArrayList<PtCdBVo>();
//					List<PtRescBVo> ptRescBVoList = new ArrayList<PtRescBVo>();
					PtCdBVo ptCdBVo = null;
					PtRescBVo ptRescBVo = null;
					for(int s=0;s<size;s++){/** 시트 */
						sheet = sheetList.get(s);
						
				    	for(int i=1;i<sheet.length;i++){/** 엑셀 로우 */
				        	/** 배열 value 매핑(셀 내용) */
				    		for( int j=0;j<sheet[i].length;j++){
				    			String compareValue = sheet[i][j];
				    			if(j == 1 ){
				    				if(langTypCd.equals(compareValue)){
				    					ptCdBVo = new PtCdBVo();
					    				ptCdBVo.setCd(sheet[i][0]);
					    				ptCdBVo.setClsCd(clsCd);//분류코드
					    				ptCdBVo.setCdVa(sheet[i][2]);
					    				ptCdBVo.setRescNm(sheet[i][2]);
					    				ptCdBVo.setUseYn("Y");
					    				ptCdBVo.setSortOrdr(i+"");
					    				ptCdBVoList.add(ptCdBVo);
				    				}
				    				ptRescBVo = new PtRescBVo();//리소스 객체 생성
				    				ptRescBVo.setLangTypCd(compareValue);
				    			}
				    			
				    			if( j == 2 ){
				    				ptRescBVo.setRescVa(compareValue);
				    				//ptRescBVoList.add(ptRescBVo);
				    				model.put(sheet[i][0]+"_"+ptRescBVo.getLangTypCd(), ptRescBVo.getRescVa());
				    			}
				    		}
				    	}
			    	}
//					System.out.println("CD SIZE : "+ptCdBVoList.size());
					// 화면 구성용 2개의 빈 vo 넣음
					/*ptCdBVoList.add(new PtCdBVo());
					ptCdBVoList.add(new PtCdBVo());*/
					model.put("ptCdBVoList", ptCdBVoList);
					
					PtCdBVo commonVo = new PtCdBVo();
					PersonalUtil.setPaging(request, commonVo, ptCdBVoList.size());
					model.put("recodeCount", ptCdBVoList.size());
				}
				
			}
			model.put("clsCd", clsCd);
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			//model.put("exception", e);
		}finally {
			if(uploadHandler!=null ) uploadHandler.removeTempDir();
		}
		
		return LayoutUtil.getJspPath("/em/cdExcelUploadFrm");
	}
	
	/** 코드 저장 - 엑셀 [미리보기 없이 저장 : 임시]*/
	@RequestMapping(value = "/cm/transCdExcelUpload")
	public String transCdExcelUpload(HttpServletRequest request,
			@Parameter(name="clsCd", required=true) String clsCd,
			ModelMap model) throws Exception {
		
		UploadHandler uploadHandler = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "excel");
			uploadHandler.upload();
			MultipartHttpServletRequest multiRequest = uploadHandler.getMultipartRequest();
			// Multipart 파일
			MultipartFile mpFile = multiRequest.getFileMap().get("excelFile");
			CmMultipartFile cmf = (CmMultipartFile) mpFile;
			File file = new File(cmf.getSavePath());
			if(file.isFile()){
//				String[][][] cellValue = null;
//				if("xls".equals(cmf.getExt())){
//					cellValue = ExcelReaderUtil.xlsReader(file);
//				}else if("xlsx".equals(cmf.getExt())){
//					cellValue = ExcelReaderUtil.xlsxReader(file);
//				}
				
				List<String[][]> sheetList = ExcelReader.readToList(file);
				
				// 세션의 언어코드
				String langTypCd = LoginSession.getLangTypCd(request);
				
				if(sheetList != null && !sheetList.isEmpty()){
					
					int size = sheetList.size();
					String[][] sheet;
					
					List<PtCdBVo> ptCdBVoList = new ArrayList<PtCdBVo>();
//					List<PtRescBVo> ptRescBVoList = new ArrayList<PtRescBVo>();
					PtCdBVo ptCdBVo = null;
					PtRescBVo ptRescBVo = null;
					//for(int s=0;s<cellValue.length;s++){/** 시트 */
					for(int s=0; s<size; s++){/** 시트 */
						sheet = sheetList.get(s);
						
				    	for(int i=1;i<sheet.length;i++){/** 엑셀 로우 */
				        	/** 배열 value 매핑(셀 내용) */
				    		for( int j=0;j<sheet[i].length;j++){
				    			ptRescBVo = new PtRescBVo();//리소스
				    			String compareValue = sheet[i][j];
				    			if(j == 1 && langTypCd.equals(compareValue)){
				    				ptCdBVo = new PtCdBVo();
				    				ptCdBVo.setCd(sheet[i][0]);
				    				ptCdBVo.setClsCd(clsCd);//분류코드
				    				ptCdBVo.setCdVa(sheet[i][2]);
				    				ptCdBVo.setRescNm(sheet[i][2]);
				    				ptCdBVo.setUseYn("Y");
				    				ptCdBVo.setSortOrdr(i+"");
				    				ptCdBVoList.add(ptCdBVo);
				    			}
				    			
				    			if( j == 1 ){
				    				model.put(ptRescBVo.getRescId()+"_"+compareValue, sheet[i][2]);
				    			}
				    		}
				    	}
			    	}
					
					// 화면 구성용 2개의 빈 vo 넣음
					ptCdBVoList.add(new PtCdBVo());
					ptCdBVoList.add(new PtCdBVo());
					model.put("ptCdBVoList", ptCdBVoList);
				}
				
				// 코드 리소스 model에 저장
				PtCdBVo ptCdBVo = new PtCdBVo();
				ptCdBVo.setClsCd(clsCd);
				ptCdBVo.setInstanceQueryId("com.innobiz.orange.web.pt.dao.PtCdBDao.selectPtCdRescB");
				@SuppressWarnings("unchecked")
				List<PtRescBVo> ptRescBVoList = (List<PtRescBVo>)commonSvc.queryList(ptCdBVo);
				if(ptRescBVoList!=null){
					for(PtRescBVo ptRescBVo : ptRescBVoList){
						model.put(ptRescBVo.getRescId()+"_"+ptRescBVo.getLangTypCd(), ptRescBVo.getRescVa());
					}
				}
				
			}
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			//model.put("exception", e);
		}finally {
			if(uploadHandler!=null ) uploadHandler.removeTempDir();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	
	
	/** 코드 저장 - 일괄저장 */
	@RequestMapping(value = "/cm/transCd")
	public String transCd(HttpServletRequest request,
			@Parameter(name="clsCd", required=false)String clsCd,
			@Parameter(name="delList", required=false)String delList,
			@Parameter(name="menuId", required=false) String menuId,
			@Parameter(name="noFrameYn", required=false) String noFrameYn,
			ModelMap model) throws Exception {
		
		try{
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			QueryQueue queryQueue = new QueryQueue();
			
			// 리소스기본(PT_RESC_B) 테이블
//			PtRescBVo ptRescBVo;
			
			// 삭제 목록의 것 삭제 SQL 처리
			// 코드기본(PT_CD_B) 테이블
			PtCdBVo ptCdBVo, storedPtCdBVo;
			String[] delCds = delList==null || delList.isEmpty() ? null : delList.split(",");
			int i, size = delCds==null ? 0 : delCds.length;
			
			// 코드기본(PT_CD_B) 테이블 VO에 데이터 담음
			@SuppressWarnings("unchecked")
			List<PtCdBVo> ptCdBVoList = (List<PtCdBVo>) VoUtil.bindList(request, PtCdBVo.class, new String[]{"cd"});
			
			// 사용안함 처리한 시스템코드 체크
			size = ptCdBVoList==null ? 0 : ptCdBVoList.size();
			for(i=0;i<size;i++){
				ptCdBVo = ptCdBVoList.get(i);
				storedPtCdBVo = ptCmSvc.getCd(ptCdBVo.getClsCd(), langTypCd, ptCdBVo.getCd());
				if("N".equals(ptCdBVo.getUseYn()) && storedPtCdBVo!=null && "Y".equals(storedPtCdBVo.getSysCdYn())){
					//pt.msg.not.use.cd.sys=시스템 코드는 사용안함 처리 할 수 없습니다.
					String msg = messageProperties.getMessage("pt.msg.not.use.cd.sys", request)
							+ " -  code:"+storedPtCdBVo.getCd()+"  value:"+storedPtCdBVo.getRescNm();
					LOGGER.error(msg);
					throw new CmException(msg);
				}
			}
			
			// 리소스 정보 queryQueue에 담음
			ptRescSvc.collectPtRescBVoList(request, ptCdBVoList, queryQueue);

			// 캐쉬 삭제
			String dbTime = ptCacheExpireSvc.getDbTime();
			ptCacheExpireSvc.expireAll(queryQueue, dbTime, CacheConfig.CODE, CacheConfig.CODE_MAP);
			commonSvc.execute(queryQueue);
			ptCacheExpireSvc.checkNow(CacheConfig.CODE, CacheConfig.CODE_MAP);
			
			//cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.popOnClose();");
			
		} catch(CmException e){
			//LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
			model.put("exception", e);
		}
		
		return LayoutUtil.getResultJsp();
	}
	
}
