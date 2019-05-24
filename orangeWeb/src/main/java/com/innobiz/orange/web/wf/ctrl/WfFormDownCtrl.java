package com.innobiz.orange.web.wf.ctrl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.UploadHandler;
import com.innobiz.orange.web.cm.files.UploadManager;
import com.innobiz.orange.web.cm.files.ZipUtil;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.ParamUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wf.svc.WfAdmSvc;
import com.innobiz.orange.web.wf.svc.WfCmSvc;
import com.innobiz.orange.web.wf.svc.WfFormSvc;
import com.innobiz.orange.web.wf.svc.WfRescSvc;
import com.innobiz.orange.web.wf.utils.CompressStringUtil;
import com.innobiz.orange.web.wf.utils.FormUtil;
import com.innobiz.orange.web.wf.vo.WfFormBVo;
import com.innobiz.orange.web.wf.vo.WfFormColmLVo;
import com.innobiz.orange.web.wf.vo.WfFormLstDVo;
import com.innobiz.orange.web.wf.vo.WfFormRegDVo;
import com.innobiz.orange.web.wf.vo.WfRescBVo;
import com.innobiz.orange.web.wf.vo.WfWorksFileDVo;

@Controller
public class WfFormDownCtrl {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(WfFormDownCtrl.class);
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "wfRescSvc")
	private WfRescSvc wfRescSvc;
	
	/** 관리 서비스 */
	@Resource(name = "wfAdmSvc")
	private WfAdmSvc wfAdmSvc;
	
	/** 양식 서비스 */
	@Resource(name = "wfFormSvc")
	private WfFormSvc wfFormSvc;
	
	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 공통 서비스 */
	@Autowired
	private WfCmSvc wfCmSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 업로드 메니저 */
	@Resource(name = "uploadManager")
	private UploadManager uploadManager;
	
	/** 압축 파일관련 서비스 */
	@Resource(name = "zipUtil")
	private ZipUtil zipUtil;
	
	/** 파일내용(json) 압축 여부*/
	private static final boolean COMPRESS_STRING=false;
	
	/** 양식 다운로드 - [JSON] */
	@RequestMapping(value = "/wf/adm/form/formDownload", method = RequestMethod.POST)
	public ModelAndView formDownload(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			@RequestParam(value = "formNos", required = false) String formNos,
			ModelMap model) throws Exception {
		
		BufferedWriter fw = null;
		try {
			
			if ((formNo==null || formNo.isEmpty()) && (formNos==null || formNos.isEmpty())) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 양식번호 목록 비어있는지 여부
			boolean isEmptyArray=formNos==null || formNos.isEmpty();
			// 양식번호 목록
			List<String> formNoList = ArrayUtil.toList(isEmptyArray ? formNo : formNos , ",", true);
			
			if(formNoList==null || formNoList.size()==0){
				// cm.msg.noData=해당하는 데이터가 없습니다.
				throw new CmException("cm.msg.noData", request);
			}
			
			// 세션의 언어코드
			String langTypCd = LoginSession.getLangTypCd(request);
			
			// 등록화면 데이터만 내보내기
			//boolean regDataOnly = formNo!=null && !formNo.isEmpty();
			boolean regDataOnly = false;
			
			// 오류메세지 목록
			//List<String> errorList = new ArrayList<String>();
			
			WfFormBVo wfFormBVo;
			WfFormRegDVo wfFormRegDVo;;
			
			// 다운로드 데이터 목록
			List<Map<String,String>> dataMapList = new ArrayList<Map<String,String>>();
			
			// 다운로드 데이터 맵
			Map<String,String> dataMap=null;
			
			// 양식 기본데이터 속성명[양식명, 양식설명, 양식구분(본문삽입,메뉴등록), 외부모듈, 전사여부, 목록구분코드(기본형,앨범형)]
			final String[] formDataAttrs = new String[]{"formNm", "formDesc", "formTyp", "mdTypCd", "allCompYn", "lstTypCd"};
			
			// 등록화면 속성명[레이아웃, 속성, 탭]
			final String[] regDataAttrs = new String[]{"loutVa", "attrVa", "tabVa"};
			
			// 등록화면 컬럼목록 속성명[컬럼구분코드, 컬럼명, 항목명, 정렬순서, 사용여부, 목록여부]
			final String[] colmDataAttrs = new String[]{"colmTypCd", "colmNm", "itemNm", "sortOrdr", "useYn", "lstYn"};
						
						
			// 파일쓰기할 내용
			StringBuilder sb = null;
			
			JSONObject jsonObj;
			
			// json 콤마 여부
			boolean isComma;
			
			String value;
			// 데이터 맵
			Map<String, String> jsonToMap;
			
			// 데이터 맵
			List<Map<String, String>> jsonToMapList;
			
			//  리소스VO 목록
			List<WfRescBVo> wfRescBVoList;
			
			// 현재 테이블 컬럼목록 조회
			List<WfFormColmLVo> colmVoList;
			
			for(String no : formNoList){
				// 양식 데이터 검증
				wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, no, "Y");
				if(wfFormBVo==null || wfFormBVo.getRegYn()==null || !"Y".equals(wfFormBVo.getRegYn())) continue; // 등록화면이 없을 경우 continue
				
				dataMap=new HashMap<String, String>();
				
				// 양식명
				dataMap.put("formNm", wfFormBVo.getFormNm());
				
				sb = new StringBuilder();
				sb.append("{");
				isComma=false;
				// 전체 다운로드[양식 기본 데이터]
				if(!regDataOnly){
					jsonToMap=new HashMap<String, String>();
					//jsonObj=new JSONObject();
					for(String attr : formDataAttrs){
						value=(String)VoUtil.getValue(wfFormBVo, attr);
						if(value!=null){
							jsonToMap.put(attr, value);
						}
					}
					
					if (wfFormBVo.getRescId() != null) { // 양식리소스명
						// 리소스기본(WL_RESC_B) 테이블 - 조회, json 으로 세팅
						wfRescBVoList=wfRescSvc.getWfRescBVoList(wfFormBVo.getRescId());
						if(wfRescBVoList!=null && wfRescBVoList.size()>0){
							for(WfRescBVo storedWfRescBVo : wfRescBVoList){
								jsonToMap.put("rescVa_"+storedWfRescBVo.getLangTypCd(), storedWfRescBVo.getRescVa());
							}
						}
					}
					
					if(jsonToMap.size()>0){
						jsonObj=new JSONObject(jsonToMap); // 맵을 json 으로 변환
						setJsonString(sb, "formData", jsonObj.toJSONString(), false); // formData 를 키로 json string 을 string builder 에 담음
						isComma=true;
					}
				}
				
				// 양식등록상세 조회
				wfFormRegDVo = wfAdmSvc.getWfFormRegDVo(null, no, false, null);
				if(wfFormRegDVo!=null){
					if(isComma) sb.append(",");
					sb.append("\"").append("regData").append("\"").append(":");
					sb.append("{");
					for(String attr : regDataAttrs){
						value=(String)VoUtil.getValue(wfFormRegDVo, attr);
						if(value != null) {
							//value=value.replaceAll("\\\\","");
							value=value.replaceAll("\r\n","\\r\\n");
							setJsonString(sb, attr, value, false);
					    }
					}
					sb.append("}");
				}
				
				// 현재 테이블 컬럼목록 조회
				colmVoList=wfAdmSvc.getColmVoList(no, null);
				if(colmVoList!=null){
					jsonToMapList = new ArrayList<Map<String, String>>();
					
					// 컬럼 속성을 맵에 담고 맵을 list에 담음
					for(WfFormColmLVo storedWfFormColmLVo : colmVoList){
						jsonToMap=new HashMap<String, String>();
						for(String attr : colmDataAttrs){ // 컬럼 속성 목록
							value=VoUtil.getValue(storedWfFormColmLVo, attr)+"";
							if(value!=null){
								jsonToMap.put(attr, value);
							}
						}
						if(jsonToMap.size()>0){
							jsonToMapList.add(jsonToMap);
							
						}
					}
					
					// colmData 를 키로 jsonArray 형태로 string builder 에 담음
					if(jsonToMapList.size()>0){
						if(isComma) sb.append(",");
						sb.append("\"").append("colmData").append("\"").append(":");
						sb.append("[");
						for(int i=0;i<jsonToMapList.size();i++){
							jsonToMap=jsonToMapList.get(i);
							if(i>0) sb.append(",");
							jsonObj=new JSONObject(jsonToMap);
							sb.append(jsonObj.toJSONString());
						}
						sb.append("]");
					}
				}
				
				sb.append("}");
				
				// System.out.println("sb.toString() : "+sb.toString());
				
				// json 데이터 맵에 담음
				dataMap.put("jsonData", sb.toString());
				
				// 데이터 목록에 추가
				dataMapList.add(dataMap);
			}
			
			if(dataMapList.size()>0){
				
				String ext = "json";
				
				// 임시저장경로
				String tmpDir=uploadManager.getTempDir();
				
				File dir=new File(tmpDir);
				if(!dir.isDirectory()) dir.mkdirs();
				
				String newfileName=null;
				String savePath;
				// 파일VO 목록
				List<CommonFileVo> fileVoList = new ArrayList<CommonFileVo>();
				WfWorksFileDVo wfFormFileDVo;
				
				String jsonData;
				File file;
				for(Map<String, String> map : dataMapList){
					newfileName=escapeFileName(map.get("formNm")); // 양식명을 파일명으로 지정
					newfileName +="."+ext;
					
					// 파일 저장 경로
					savePath=tmpDir+File.separator+newfileName;
					savePath = savePath.replace('\\', '/');
					
					// BufferedWriter 와 FileWriter를 조합하여 사용 (속도 향상)
					//BufferedWriter fw = new BufferedWriter(new FileWriter(savePath, true));
					// 한글깨짐으로 인해 BufferedWriter ... 조합으로 변경
					fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savePath), "UTF-8"));
					
					jsonData=map.get("jsonData");
					if(COMPRESS_STRING) // String 압축여부
						jsonData=CompressStringUtil.compressString(jsonData);
					// 파일안에 문자열 쓰기
					fw.write(jsonData);
					fw.flush();
					
					// 객체 닫기
					fw.close(); 
					
					// 파일 객체
					file = new File(savePath);
					if (file.isFile()) { // 해당 파일이 있으면
						wfFormFileDVo = new WfWorksFileDVo();
						wfFormFileDVo.setSavePath(savePath); // 저장경로
						wfFormFileDVo.setDispNm(newfileName); // 파일명
						
						fileVoList.add(wfFormFileDVo);
					}
					
				}
				
				// 파일이 몇개인가
				if (fileVoList.size() == 0) {
					ModelAndView mv = new ModelAndView("cm/result/commonResult");
					Locale locale = SessionUtil.getLocale(request);
					// cm.msg.file.fileNotFound=요청한 파일을 찾을 수 없습니다.
					mv.addObject("message", messageProperties.getMessage("cm.msg.file.fileNotFound", locale));
					return mv;
					
				} else if (fileVoList.size() == 1) {
					CommonFileVo fileVo = fileVoList.get(0);
					savePath = fileVo.getSavePath();
					file = new File(savePath);
					ModelAndView mv = new ModelAndView("fileDownloadView");
					mv.addObject("downloadFile", file);
					mv.addObject("realFileName", fileVo.getDispNm());
					return mv;
					
				} else {
					Format sdf = FastDateFormat.getInstance( "yyyyMMddhhmm", Locale.getDefault());
					Calendar cal = Calendar.getInstance();
					//파일명 생성
					String zipFileNm = sdf.format(cal.getTime())+".zip";
					
					File zipFile = zipUtil.makeZipFile(fileVoList, zipFileNm);
					ModelAndView mv = new ModelAndView("fileDownloadView");
					mv.addObject("downloadFile", zipFile);
					mv.addObject("realFileName", zipFile.getName());
					return mv;
				}
			}
			
			return null;
			
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		} finally {
			if (fw != null) try { fw.close(); } catch (Exception e) {}
		}
		return null;
	}
	
	
	/** [팝업] 양식 업로드 */
	@RequestMapping(value = "/wf/adm/form/setFormUploadPop")
	public String setFormUploadPop(HttpServletRequest request,
			@RequestParam(value = "formNo", required = false) String formNo,
			ModelMap model) throws Exception {
		
		return LayoutUtil.getJspPath("/wf/adm/form/setFormUploadPop");
	}
	
	/** json 양식 업로드 */
	@RequestMapping(value = "/wf/adm/form/transFormJson")
	public String transFormJson(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws Exception {

		UploadHandler uploadHandler = null;
		
		BufferedReader br = null;
		try{
			uploadHandler = uploadManager.createHandler(request, "temp", "wf");
			Map<String, File> fileMap = uploadHandler.upload();//업로드 파일 정보
						
			File file = fileMap.get("json");
			if(file==null || !file.isFile() || !file.canRead()){
				// cm.msg.noFileSelected=선택한 파일이 없습니다.
				throw new CmException("cm.msg.noFileSelected", locale);
			}
			
			// MultipartRequest
			request = uploadHandler.getMultipartRequest();
			
			// 양식번호
			String grpId = ParamUtil.getRequestParam(request, "grpId", false);
						
			// 양식번호
			String formNo = ParamUtil.getRequestParam(request, "formNo", false);
			
			// 신규 여부
			boolean isNew = formNo==null || formNo.isEmpty();
			
			// 양식데이터 여부
			String formDataYn = ParamUtil.getRequestParam(request, "formDataYn", false);
			
			// 신규이면서 양식번호가 없으면 양식 기본정보를 무조건 생성한다.
			if(isNew && (formNo==null || formNo.isEmpty()))
				formDataYn="Y";
			
			if(formDataYn==null || formDataYn.isEmpty())
				formDataYn="N";
			
			// 등록화면 여부
			String regDataYn = ParamUtil.getRequestParam(request, "regDataYn", false);
			
			if(regDataYn==null || regDataYn.isEmpty())
				regDataYn="N";
			
			// 목록화면 여부
			String listDataYn = ParamUtil.getRequestParam(request, "listDataYn", false);
			
			// 파일 내용을 담을 객체
			StringBuilder sb = new StringBuilder(); 
			
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
                sb.append(line);
            }
			// 파일스트링
			String fileString=sb.toString();
			if(COMPRESS_STRING){
				fileString=CompressStringUtil.decompressString(fileString);
				
				if(fileString==null || fileString.isEmpty()){
					// cm.msg.noData=해당하는 데이터가 없습니다.
					throw new CmException("cm.msg.noData", request);
				}
			}
			
			JSONParser parser = new JSONParser();
			
			// json 객체 변환
			Object object=null;
			try{
				object=parser.parse(fileString);
			}catch(ParseException pe){
				// wf.msg.fail.convert=데이터 변환을 실패하였습니다.
				throw new CmException("wf.msg.fail.convert", request);
			}
					
			JSONObject jsonObject = (JSONObject) object;
			if(jsonObject!=null){
				
				QueryQueue queryQueue = new QueryQueue();
				
				JSONObject jsonData;
				JSONArray jsonArray;
				String key;
				
				// 양식 기본 저장
				if(formDataYn!=null && "Y".equals(formDataYn)){
					jsonData = (JSONObject) jsonObject.get("formData");
					if(jsonData==null || jsonData.isEmpty()){
						// cm.msg.noData=해당하는 데이터가 없습니다.
						throw new CmException("cm.msg.noData", request);
					}
						
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					// 세션의 사용자 정보
					UserVo userVo = LoginSession.getUser(request);
					
					// 소속회사의 언어코드 목록 조회
					List<PtCdBVo> langTypCdList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
					
					// 리소스 데이터맵
					Map<String, Object> paramMap = new HashMap<String, Object>();
					
					// 리소스키, 리소스값
					String rescKey, rescVa;
					for(PtCdBVo storedPtCdBVo : langTypCdList){
						rescKey="rescVa_"+storedPtCdBVo.getCd();
						rescVa=(String)jsonData.get(rescKey);
						if(rescVa==null || rescVa.isEmpty()) rescVa=(String)jsonData.get("formNm");
						paramMap.put(rescKey, rescVa);
					}
					
					String rescId=null;
					
					// 수정이면 해당 양식의 리소스ID 조회
					if(!isNew){
						WfFormBVo wfFormBVo = wfFormSvc.getWfFormBVo(null, langTypCd, formNo, null);
						if(wfFormBVo!=null && wfFormBVo.getRescId()!=null)
							rescId=wfFormBVo.getRescId();
						
						grpId=wfFormBVo.getGrpId();
					}
					if(rescId!=null) paramMap.put("rescId", rescId);
					
					// 리소스VO
					WfRescBVo whRescBVo = wfRescSvc.collectWfRescBVo(request, "", queryQueue, paramMap);
					//System.out.println("formData : "+jsonData);
					
					WfFormBVo wfFormBVo=new WfFormBVo();
					
					Iterator<?> iter=jsonData.keySet().iterator();
					while (iter.hasNext()){
						key = (String)iter.next();
						VoUtil.setValue(wfFormBVo, key, jsonData.get(key));
					}
					
					if(isNew) {
						wfFormBVo.setRegYn(regDataYn); // 등록 여부
						wfFormBVo.setUseYn("Y"); // 사용여부
						wfFormBVo.setLstYn("N"); // 목록 여부
						if(grpId!=null && !grpId.isEmpty()) wfFormBVo.setGrpId(grpId);
					}
					
					// 수정이면 양식번호 세팅
					if(!isNew) wfFormBVo.setFormNo(formNo);
					// 양식 저장
					formNo=wfAdmSvc.saveForm(request, queryQueue, wfFormBVo, whRescBVo, formNo);
				}
				
				// 등록화면 저장
				if(regDataYn!=null && "Y".equals(regDataYn)){
					if(!isNew && "N".equals(formDataYn)){
						WfFormBVo wfFormBVo=new WfFormBVo();
						wfFormBVo.setFormNo(formNo);
						wfFormBVo.setRegYn(regDataYn); // 등록 여부
						queryQueue.update(wfFormBVo);
						
					}
					jsonData = (JSONObject) jsonObject.get("regData");
					if(jsonData==null || jsonData.isEmpty()){
						// cm.msg.noData=해당하는 데이터가 없습니다.
						throw new CmException("cm.msg.noData", request);
					}
					//System.out.println("regData : "+jsonData);
					
					Object obj;
					String value;
					WfFormRegDVo wfFormRegDVo = new WfFormRegDVo();
					Iterator<?> iter=jsonData.keySet().iterator();
					while (iter.hasNext()){
						key = (String)iter.next();
						obj = jsonData.get(key);
				    	if(obj instanceof JSONArray) {
				    		value=((JSONArray) obj).toJSONString();
				        }else{
				        	value=((JSONObject) obj).toJSONString();
				        }
						//	value=value.replaceAll("\"", "\\\"");
						VoUtil.setValue(wfFormRegDVo, key, value);
					}
					
					// 양식번호
					wfFormRegDVo.setFormNo(formNo);
					
					// 등록화면 저장
					queryQueue.store(wfFormRegDVo);
					
					// db 컬럼 목록
					jsonArray = (JSONArray) jsonObject.get("colmData");
					
					if(jsonArray!=null && jsonArray.size()>0){
						// 양식 컬럼 목록
						WfFormColmLVo wfFormColmLVo=null;
						// 수정이면 기존 db컬럼, 조회컬럼 전체 삭제
						if(!isNew){
							// 양식 컬럼 목록 삭제
							wfFormColmLVo= new WfFormColmLVo();
							wfFormColmLVo.setFormNo(formNo);
							queryQueue.delete(wfFormColmLVo);
							
							// 조회 컬럼 목록 삭제
							WfFormLstDVo wfFormLstDVo= new WfFormLstDVo();
							wfFormLstDVo.setFormNo(formNo);
							queryQueue.delete(wfFormLstDVo);
							
						}
						
						// Json to Map
						Map<String, Object> colmMap;
						
						JSONObject json;
						
						int i, size = jsonArray==null ? 0 : jsonArray.size();
						
						for(i=0;i<size;i++){
							wfFormColmLVo = new WfFormColmLVo();
							json=(JSONObject)jsonArray.get(i);
							colmMap = JsonUtil.jsonToMap(json);
							
							// jsonMap => Vo
							FormUtil.setParamToVo(colmMap, wfFormColmLVo, null);
							if(colmMap.containsKey("sortOrdr")) wfFormColmLVo.setSortOrdr(Integer.parseInt(colmMap.get("sortOrdr")+""));
							wfFormColmLVo.setFormNo(formNo);
							wfFormColmLVo.setColmId(wfCmSvc.createId("WF_FORM_COLM_L"));
							
							wfFormColmLVo.setUseYn("Y");
							wfFormColmLVo.setLstYn("Y");
							queryQueue.insert(wfFormColmLVo);
						}
					}
					
					
				}
				
				// 목록화면 저장
				if(listDataYn!=null && "Y".equals(listDataYn)){
					jsonData = (JSONObject) jsonObject.get("listData");
					if(jsonData!=null){
						System.out.println("listData : "+jsonData);
					}
				}
				
				// 전체 저장
				if(!queryQueue.isEmpty()) commonSvc.execute(queryQueue);
			}
			
			// 팝업ID
			String dialogId = ParamUtil.getRequestParam(request, "dialogId", false);
			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));			
			model.put("todo", "parent.reloadForm(null, '"+dialogId+"');");
			
		} catch(CmException e){
			e.printStackTrace();
			model.put("message", e.getMessage());
			model.put("todo", "parent.errPopClose();");
		} catch(NullPointerException e){
			e.printStackTrace();
			model.put("message", "[JSON Format Error] - Download sample file and fill the data !");
			model.put("todo", "parent.errPopClose();");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			model.put("message", ioe.getMessage());
			model.put("todo", "parent.errPopClose();");
            //System.out.println(ioe.getMessage());
        } catch(Exception e){
			e.printStackTrace();
			model.put("todo", "parent.errPopClose();");
			model.put("message", e.getMessage());			
		} finally {
			try {if (br!=null) br.close();} catch (IOException igonre) {}
			if(uploadHandler!=null) uploadHandler.removeTempDir();
		}
		
		return LayoutUtil.getResultJsp();
	}
	
	/** json key + value 세팅 */
	public void setJsonString(StringBuilder sb, String key, String value, boolean isQuot){
		if(value==null || value.isEmpty()) return;
		sb.append("\"").append(key).append("\"").append(":");
		if(isQuot) sb.append("\"");
		sb.append(value);
		if(isQuot) sb.append("\"");
	}
	
	/** 파일명 제외 문자 */
	private char[] fileNameEscapes = "#%{}\\<>*?/ $!'\":@".toCharArray();
	/** 파일명 제외 문자 제거 - 언더바(_)로 치환함 */
	private String escapeFileName(String fileName){
		for(char c : fileNameEscapes){
			fileName = fileName.replace(c, '_');
		}
		return fileName;
	}
}
