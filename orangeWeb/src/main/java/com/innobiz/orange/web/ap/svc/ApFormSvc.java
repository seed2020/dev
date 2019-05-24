package com.innobiz.orange.web.ap.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.xml.sax.SAXException;

import com.innobiz.orange.web.ap.utils.SAXHandler;
import com.innobiz.orange.web.ap.utils.XMLElement;
import com.innobiz.orange.web.ap.vo.ApErpFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApFormBVo;
import com.innobiz.orange.web.ap.vo.ApFormCombDVo;
import com.innobiz.orange.web.ap.vo.ApFormImgDVo;
import com.innobiz.orange.web.ap.vo.ApFormItemDVo;
import com.innobiz.orange.web.ap.vo.ApFormItemLVo;
import com.innobiz.orange.web.ap.vo.ApFormTxtDVo;
import com.innobiz.orange.web.ap.vo.ApFormXmlDVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormBVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormCombDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormImgDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormItemDVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormItemLVo;
import com.innobiz.orange.web.ap.vo.ApOngoFormTxtDVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.wf.svc.WfMdFormSvc;

/** 결재 양식 서비스 */
@Service
public class ApFormSvc {
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	@Autowired
	private WfMdFormSvc wfMdFormSvc;
	
//	/** 메세지 */
//	@Autowired
//    private MessageProperties messageProperties;

	/** 설정된 양식을 불러옴 - 기안, 양식 편집용  */
	public void setSetupForm(String formId, String langTypCd, HttpServletRequest request, ModelMap model) throws SQLException, SAXException, IOException, ParserConfigurationException {

		String formApvLnTypCd = null;
		
		// 양식기본(AP_FORM_B) 테이블 - 조회(본문 HTML)
		ApFormBVo apFormBVo = new ApFormBVo();
		apFormBVo.setFormId(formId);
		apFormBVo.setQueryLang(langTypCd);
		apFormBVo = (ApFormBVo)commonDao.queryVo(apFormBVo);
		if(apFormBVo!=null){
			model.put("apFormBVo", apFormBVo);
			
			String printView = apFormBVo.getFormWdthTypCd();
			if(printView!=null && !printView.isEmpty()){
				if(model.get("printView") == null){
					model.put("printView", printView);//인쇄 넓이 설정
				}
			} else {
				if(model.get("printView") == null){
					model.put("printView", "printAp100");//인쇄 넓이 설정
				}
			}
			
			// 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재
			formApvLnTypCd = apFormBVo.getFormApvLnTypCd();
			model.put("formApvLnTypCd", formApvLnTypCd);
			// apvLn + 양식구성일련번호 로 UI 구성용 데이터 세팅함
			
			// 결재방 앞쪽 타이틀에 해당하는 리소스 세팅
			// 결재라인타이틀구분코드 - apv:결재, agr:합의, req:신청, prc:처리
			if("apvLn".equals(formApvLnTypCd) || "apvLnMixd".equals(formApvLnTypCd) || "apvLnOnecard".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv"});
			} else if("apvLn1LnAgr".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv", "agr"});
			} else if("apvLnDbl".equals(formApvLnTypCd) || "apvLnWrtn".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"req", "prc"});
			} else if("apvLn2LnAgr".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv"});
				model.put("apvLn2", new String[]{"agr"});
			// apvLnList=결재자 리스트, apvLnOneTopList=최종결재 리스트, apvLnMultiTopList=도장방 리스트
			} else if("apvLnList".equals(formApvLnTypCd) || "apvLnOneTopList".equals(formApvLnTypCd) || "apvLnMultiTopList".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv"});
			}
		}
		
		String erpFormId = apFormBVo==null ? null : apFormBVo.getErpFormId();
		String erpFormTypCd = apFormBVo==null ? null : apFormBVo.getErpFormTypCd();
		if(erpFormId!=null && !erpFormId.isEmpty()){
			
			ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
			apErpFormBVo.setErpFormId(erpFormId);
			apErpFormBVo = (ApErpFormBVo)commonDao.queryVo(apErpFormBVo);
			if(apErpFormBVo!=null){
				
				String regUrl = apErpFormBVo.getRegUrl();
				if(regUrl!=null && !regUrl.isEmpty()){
					if(erpFormTypCd!=null && (
							"xmlFromAp".equals(erpFormTypCd) || "xmlEditFromAp".equals(erpFormTypCd)
							|| erpFormTypCd.startsWith("wfForm")
							)){
						model.put("apErpFormBVo", apErpFormBVo);
					}
					
					// 업무관리 - 양식
					if(erpFormTypCd.startsWith("wfForm")){
						model.put("wfForm", Boolean.TRUE);
						
						String wfFormNo=null;
						for(String param : regUrl.substring(regUrl.indexOf('?')+1).split("\\&")){
							if(param.startsWith("formNo=")){
								wfFormNo = param.substring(7);
								model.put("wfFormNo", wfFormNo);
							}
						}
						
						if(wfFormNo!=null){
							boolean isFromEdit = Boolean.TRUE == model.get("isFromEdit");
							// 업무관리 - 양식 구성 서비스 호출
							String wfWorkNo = null;// 양식편집 또는 최초 기안 - : wfWorkNo 없음
							wfMdFormSvc.setFormByAP(request, model, wfFormNo, formId, wfWorkNo, isFromEdit);
						}
					}
				}
				// 양식 XML 세팅 - 본문 (편집양식)
				if("xmlEditFromAp".equals(erpFormTypCd)){
					setFormEditBodyXml(formId, model);
				}
			}
			
//			if("xmlFromAp".equals(erpFormTypCd) || "xmlEditFromAp".equals(erpFormTypCd)){
//				ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
//				apErpFormBVo.setErpFormId(erpFormId);
//				apErpFormBVo = (ApErpFormBVo)commonDao.queryVo(apErpFormBVo);
//				if(apErpFormBVo!=null && apErpFormBVo.getRegUrl()!=null && !apErpFormBVo.getRegUrl().isEmpty()){
//					model.put("apErpFormBVo", apErpFormBVo);
//				}
//				
//				// 양식 XML 세팅 - 본문 (편집양식)
//				if("xmlEditFromAp".equals(erpFormTypCd)){
//					setFormEditBodyXml(formId, model);
//				}
//			}
		}
		
		// 양식구성상세(AP_FORM_COMB_D) 테이블
		ApFormCombDVo apFormCombDVo = new ApFormCombDVo();
		apFormCombDVo.setFormId(formId);
		apFormCombDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApFormCombDVo> apFormCombDVoList = (List<ApFormCombDVo>)commonDao.queryList(apFormCombDVo);
		if(apFormCombDVoList!=null){
			model.put("apFormCombDVoList", apFormCombDVoList);
			
			// ERP 양식이 설정되어 있으면 - html 본문이 있는지 여부 세팅
			if(erpFormId!=null && !erpFormId.isEmpty()){
				for(ApFormCombDVo storedApFormCombDVo : apFormCombDVoList){
					if("bodyHtml".equals(storedApFormCombDVo.getFormCombId())){
						model.put("hasBodyHtml", Boolean.TRUE);
						break;
					}
				}
			}
		}
		
		// 영역별 텍스트구분ID, 이미지구분ID 세팅
		
		// docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글
		model.put("headerTxts", new String[]{"docHeader", "docName"});
		model.put("senderTxts", new String[]{"docSender", "docReceiver"});
		model.put("footerTxts", new String[]{"docFooter"});
		// docLogo:로고, docSymbol:심볼
		model.put("headerImgs", new String[]{"docHeaderImg", "docLogo", "docSymbol"});
		
		// 양식텍스트상세(AP_FORM_TXT_D) 테이블 조회
		ApFormTxtDVo apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormId(formId);
		@SuppressWarnings("unchecked")
		List<ApFormTxtDVo> apFormTxtDVoList = (List<ApFormTxtDVo>)commonDao.queryList(apFormTxtDVo);
		if(apFormTxtDVoList!=null){
			for(ApFormTxtDVo storedApFormTxtDVo : apFormTxtDVoList){
				model.put(storedApFormTxtDVo.getFormTxtTypCd(), storedApFormTxtDVo);
				if("docFooter".equals(storedApFormTxtDVo.getFormTxtTypCd())){
					model.put("footerVa", storedApFormTxtDVo.getTxtCont());
				}
			}
		}
		
		// 양식이미지상세(AP_FORM_IMG_D) 테이블 조회
		ApFormImgDVo apFormImgDVo = new ApFormImgDVo();
		apFormImgDVo.setFormId(formId);
		@SuppressWarnings("unchecked")
		List<ApFormImgDVo> apFormImgDVoList = (List<ApFormImgDVo>)commonDao.queryList(apFormImgDVo);
		if(apFormTxtDVoList!=null){
			for(ApFormImgDVo storedApFormImgDVo : apFormImgDVoList){
				model.put(storedApFormImgDVo.getFormImgTypCd(), storedApFormImgDVo);
			}
		}
		
		// 결재라인 최대수 - 스트립트용 JSON
		StringBuilder apvLnMaxCntBuilder = new StringBuilder(64);
		boolean apvLnMaxCntFirst = true;
		apvLnMaxCntBuilder.append('{');
		
		// 양식결재라인상세(AP_FORM_APV_LN_D) 테이블
		ApFormApvLnDVo apFormApvLnDVo = new ApFormApvLnDVo();
		apFormApvLnDVo.setFormId(formId);
		@SuppressWarnings("unchecked")
		List<ApFormApvLnDVo> apFormApvLnDVoList = (List<ApFormApvLnDVo>)commonDao.queryList(apFormApvLnDVo);
		
		int maxSum = 0, maxCnt = 0;
		if(apFormApvLnDVoList!=null){
			
			for(ApFormApvLnDVo storedApFormApvLnDVo : apFormApvLnDVoList){
				if(storedApFormApvLnDVo.getMaxCnt()!=null && !storedApFormApvLnDVo.getMaxCnt().isEmpty()){
					
					// 도장방 2줄로 표시할 경우 - 둘중 큰 값 세팅
					if("apvLn2LnAgr".equals(formApvLnTypCd)){
						maxCnt = Integer.parseInt(storedApFormApvLnDVo.getMaxCnt());
						if(maxSum < maxCnt) maxSum = maxCnt;
					// 도장방 1줄 표시 - 도장방 갯수 합 세팅
					} else {
						maxSum += Integer.parseInt(storedApFormApvLnDVo.getMaxCnt());
					}
				}
			}
			
			for(ApFormApvLnDVo storedApFormApvLnDVo : apFormApvLnDVoList){
				if(storedApFormApvLnDVo.getMaxCnt()!=null && !storedApFormApvLnDVo.getMaxCnt().isEmpty()){
					
					if(apvLnMaxCntFirst) apvLnMaxCntFirst = false;
					else apvLnMaxCntBuilder.append(',');
					apvLnMaxCntBuilder.append('"').append(storedApFormApvLnDVo.getApvLnTitlTypCd()).append('"');
					apvLnMaxCntBuilder.append(':').append(storedApFormApvLnDVo.getMaxCnt());
					
					// 서명+리스트 일 경우 - 성명란 표시 숫자 외의 것을 숨기기 위한 것
					model.put("apvLnMaxCnt_"+storedApFormApvLnDVo.getApvLnTitlTypCd(), storedApFormApvLnDVo.getMaxCnt());
				}
				
				// apvLnWrtn=신청+처리(서면결재) - 처리(서면)영역 - 빈칸 출력용 빈 데이터 넣기
				if("apvLnWrtn".equals(formApvLnTypCd) && "prc".equals(storedApFormApvLnDVo.getApvLnTitlTypCd())){
					if(storedApFormApvLnDVo.getMaxCnt() != null && !storedApFormApvLnDVo.getMaxCnt().isEmpty()){
						try{
							maxCnt = Integer.parseInt(storedApFormApvLnDVo.getMaxCnt());
						} catch(Exception e){
							maxCnt = 0;
						}
					} else {
						maxCnt = 0;
					}
					if(maxCnt>0){
						List<ApOngdApvLnDVo> apvrList = new ArrayList<ApOngdApvLnDVo>();
						for(int i=0;i<maxCnt;i++){
							apvrList.add(new ApOngdApvLnDVo());
						}
						model.put("prcApvrList", apvrList);
					}
				}
				
				// 합계 최대(또는 라인의 최대값) 저장
				storedApFormApvLnDVo.setMaxSum(maxSum);
				model.put(storedApFormApvLnDVo.getApvLnTitlTypCd(), storedApFormApvLnDVo);
			}
		}
		apvLnMaxCntBuilder.append('}');
		// 결재선 지정시 최대 설정 수 체크용
		model.put("apvLnMaxCnt", apvLnMaxCntBuilder.toString());
		
		// 항목지정
		// 양식항목상세(AP_FORM_ITEM_D) 테이블
		ApFormItemDVo apFormItemDVo = new ApFormItemDVo();
		apFormItemDVo.setFormId(formId);
		apFormItemDVo.setOrderBy("FORM_COMB_SEQ");
		@SuppressWarnings("unchecked")
		List<ApFormItemDVo> apFormItemDVoList = (List<ApFormItemDVo>) commonDao.queryList(apFormItemDVo);
		
		// 양식항목내역(AP_FORM_ITEM_L) 테이블
		ApFormItemLVo apFormItemLVo = new ApFormItemLVo();
		apFormItemLVo.setFormId(formId);
		apFormItemLVo.setOrderBy("FORM_COMB_SEQ, ROW_NO, COL_NO");
		@SuppressWarnings("unchecked")
		List<ApFormItemLVo> apFormItemLVoList = (List<ApFormItemLVo>) commonDao.queryList(apFormItemLVo);
		
		// 의견 항목이 있는지 여부
		boolean opinInDoc = false;
		
		// ApFormItemDVo 의 childList 세팅
		String itemSeq = null, maxItemSeq = null;
		int itemSeqNo = 0;
		List<ApFormItemLVo> childList;
		
		if(apFormItemDVoList!=null){
			for(ApFormItemDVo storedApFormItemDVo : apFormItemDVoList){
				
				itemSeq = storedApFormItemDVo.getFormCombSeq();
				itemSeqNo = Integer.parseInt(itemSeq);
				
				// 항목 지정 외 다른곳에서 설정한 경우 - 확장 고려 케이스
				if(itemSeqNo > 90000){
					
					// 최종결재+리스트 (결재라인 설정) - 에서 설정한 항목
					if("99999".equals(itemSeq)){
						ApFormItemLVo[] oneTopItems = new ApFormItemLVo[5];
						int no;
						
						if(apFormItemLVoList!=null){
							for(ApFormItemLVo storedApFormItemLVo : apFormItemLVoList){
								if(itemSeq.equals(storedApFormItemLVo.getFormCombSeq())){
									no = Integer.parseInt(storedApFormItemLVo.getRowNo());
									if(no>0 && no<6){
										oneTopItems[no-1] = storedApFormItemLVo;
									}
								}
								if("opin".equals(storedApFormItemLVo.getItemId())){
									opinInDoc = true;
								}
							}
						}
						model.put("oneTopItems", oneTopItems);
					}
					
					
				// 항목지정 에서 설정한 항목
				} else {
					maxItemSeq = itemSeq;
					
					childList = new ArrayList<ApFormItemLVo>();
					if(apFormItemLVoList!=null){
						for(ApFormItemLVo storedApFormItemLVo : apFormItemLVoList){
							if(itemSeq.equals(storedApFormItemLVo.getFormCombSeq())){
								childList.add(storedApFormItemLVo);
							}
							if("opin".equals(storedApFormItemLVo.getItemId())){
								opinInDoc = true;
							}
						}
					}
					if(!childList.isEmpty()){
						storedApFormItemDVo.setChildList(childList);
					}
					model.put("items"+itemSeq, storedApFormItemDVo);
				}
				
			}
		}
		model.put("opinInDoc", opinInDoc ? "Y" : "N");
		
		if(maxItemSeq != null) model.put("maxItemSeq", maxItemSeq);
	}
	
	/** 양식에 저장된 formBodyXML 설정 */
	public void setFormEditBodyXml(String formId, ModelMap model) throws SQLException, SAXException, IOException, ParserConfigurationException {
		ApFormXmlDVo apFormXmlDVo = new ApFormXmlDVo();
		apFormXmlDVo.setFormId(formId);
		@SuppressWarnings("unchecked")
		List<ApFormXmlDVo> apFormXmlDVoList = (List<ApFormXmlDVo>)commonDao.queryList(apFormXmlDVo);
		if(apFormXmlDVoList != null){
			
			XMLElement xmlElement;
			for(ApFormXmlDVo storedApFormXmlDVo : apFormXmlDVoList){
				if(storedApFormXmlDVo.getFormXmlCont()!=null
						&& !storedApFormXmlDVo.getFormXmlCont().isEmpty()){
					
					xmlElement = SAXHandler.parse(storedApFormXmlDVo.getFormXmlCont());
					if("formEditBodyXML".equals(storedApFormXmlDVo.getFormXmlCd())){
						model.put("formBodyXML", xmlElement);
					} else {
						model.put(storedApFormXmlDVo.getFormXmlCd(), xmlElement);
					}
				}
			}
		}
	}
	
	/** 양식을 불러옴 - 완결, 진행중 문서의 양식을 불러옴 */
	public void setOngoForm(String formId, String formSeq, HttpServletRequest request, ModelMap model) throws SQLException {

		String formApvLnTypCd = null;
		
		// 양식기본(AP_FORM_B) 테이블 - 조회(본문 HTML)
		ApOngoFormBVo apOngoFormBVo = new ApOngoFormBVo();
		apOngoFormBVo.setFormId(formId);
		apOngoFormBVo.setFormSeq(formSeq);
		apOngoFormBVo = (ApOngoFormBVo)commonDao.queryVo(apOngoFormBVo);
		if(apOngoFormBVo!=null){
			model.put("apFormBVo", apOngoFormBVo);
			//apFormBVo
			String printView = apOngoFormBVo.getFormWdthTypCd();
			if(printView!=null && !printView.isEmpty()){
				if(model.get("printView") == null){
					model.put("printView", printView);//인쇄 넓이 설정
				}
			} else {
				if(model.get("printView") == null){
					model.put("printView", "printAp100");//인쇄 넓이 설정
				}
			}
			
			// 양식결재라인구분코드 - apvLn:결재(합의표시안함), apvLnMixd:결재(합의1칸), apvLn1LnAgr:결재+합의(1줄), apvLn2LnAgr:결재+합의(2줄), apvLnDbl:이중결재
			formApvLnTypCd = apOngoFormBVo.getFormApvLnTypCd();
			model.put("formApvLnTypCd", formApvLnTypCd);
			// apvLn + 양식구성일련번호 로 UI 구성용 데이터 세팅함
			
			// 결재방 앞쪽 타이틀에 해당하는 리소스 세팅
			// 결재라인타이틀구분코드 - apv:결재, agr:합의, req:신청, prc:처리
			if("apvLn".equals(formApvLnTypCd) || "apvLnMixd".equals(formApvLnTypCd) || "apvLnOnecard".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv"});
			} else if("apvLn1LnAgr".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv", "agr"});
			} else if("apvLnDbl".equals(formApvLnTypCd) || "apvLnDblList".equals(formApvLnTypCd) || "apvLnWrtn".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"req", "prc"});
			} else if("apvLn2LnAgr".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv"});
				model.put("apvLn2", new String[]{"agr"});
			// apvLnList=결재자 리스트, apvLnOneTopList=최종결재 리스트, apvLnMultiTopList=도장방 리스트
			} else if("apvLnList".equals(formApvLnTypCd) || "apvLnOneTopList".equals(formApvLnTypCd) || "apvLnMultiTopList".equals(formApvLnTypCd)){
				model.put("apvLn1", new String[]{"apv"});
			}
		}
		
		String erpFormId = apOngoFormBVo==null ? null : apOngoFormBVo.getErpFormId();
		if(erpFormId!=null && !erpFormId.isEmpty()){
			ApErpFormBVo apErpFormBVo = new ApErpFormBVo();
			apErpFormBVo.setErpFormId(erpFormId);
			apErpFormBVo = (ApErpFormBVo)commonDao.queryVo(apErpFormBVo);
			if(apErpFormBVo!=null){
				String regUrl = apErpFormBVo.getRegUrl();
				if(regUrl!=null && !regUrl.isEmpty()){
					model.put("apErpFormBVo", apErpFormBVo);
				}
				
				String erpFormTypCd = apOngoFormBVo.getErpFormTypCd();
				if(erpFormTypCd!=null && erpFormTypCd.startsWith("wfForm")){
					model.put("wfForm", Boolean.TRUE);
					
//					String wfFormNo=null;
//					for(String param : regUrl.substring(regUrl.indexOf('?')+1).split("\\&")){
//						if(param.startsWith("formNo=")){
//							wfFormNo = param.substring(7);
//							model.put("wfFormNo", wfFormNo);
//						}
//					}
//					
//					if(wfFormNo!=null){// 저장된 결재 문서의 업무관리 양식 로드
//						String wfWorkNo = (String)model.get("wfWorkNo");
//						boolean isFromEdit = false;//양식 편집 에서만 true
//						wfMdFormSvc.setFormByAP(request, model, wfWorkNo, formId, wfWorkNo, isFromEdit);
//					}
				}
			}
		}
		
		// 양식구성상세(AP_FORM_COMB_D) 테이블
		ApOngoFormCombDVo apOngoFormCombDVo = new ApOngoFormCombDVo();
		apOngoFormCombDVo.setFormId(formId);
		apOngoFormCombDVo.setFormSeq(formSeq);
		apOngoFormCombDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApOngoFormCombDVo> apOngoFormCombDVoList = (List<ApOngoFormCombDVo>)commonDao.queryList(apOngoFormCombDVo);
		if(apOngoFormCombDVoList!=null){
			model.put("apFormCombDVoList", apOngoFormCombDVoList);
			
			// ERP 양식이 설정되어 있으면 - html 본문이 있는지 여부 세팅
			if(erpFormId!=null && !erpFormId.isEmpty()){
				for(ApOngoFormCombDVo storedApOngoFormCombDVo : apOngoFormCombDVoList){
					if("bodyHtml".equals(storedApOngoFormCombDVo.getFormCombId())){
						model.put("hasBodyHtml", Boolean.TRUE);
						break;
					}
				}
			}
		}
		
		// 영역별 텍스트구분ID, 이미지구분ID 세팅
		
		// docHeader:머리글, docName:양식명, docSender:발신명의, docReceiver:수신처, docFooter:바닥글
		model.put("headerTxts", new String[]{"docHeader", "docName"});
		model.put("senderTxts", new String[]{"docSender", "docReceiver"});
		model.put("footerTxts", new String[]{"docFooter"});
		// docLogo:로고, docSymbol:심볼
		model.put("headerImgs", new String[]{"docLogo", "docSymbol"});
		
		// 양식텍스트상세(AP_FORM_TXT_D) 테이블 조회
		ApOngoFormTxtDVo apOngoFormTxtDVo = new ApOngoFormTxtDVo();
		apOngoFormTxtDVo.setFormId(formId);
		apOngoFormTxtDVo.setFormSeq(formSeq);
		@SuppressWarnings("unchecked")
		List<ApOngoFormTxtDVo> apOngoFormTxtDVoList = (List<ApOngoFormTxtDVo>)commonDao.queryList(apOngoFormTxtDVo);
		if(apOngoFormTxtDVoList!=null){
			for(ApOngoFormTxtDVo storedApOngoFormTxtDVo : apOngoFormTxtDVoList){
				model.put(storedApOngoFormTxtDVo.getFormTxtTypCd(), storedApOngoFormTxtDVo);
			}
		}
		
		// 양식이미지상세(AP_FORM_IMG_D) 테이블 조회
		ApOngoFormImgDVo apOngoFormImgDVo = new ApOngoFormImgDVo();
		apOngoFormImgDVo.setFormId(formId);
		apOngoFormImgDVo.setFormSeq(formSeq);
		@SuppressWarnings("unchecked")
		List<ApOngoFormImgDVo> apOngoFormImgDVoList = (List<ApOngoFormImgDVo>)commonDao.queryList(apOngoFormImgDVo);
		if(apOngoFormTxtDVoList!=null){
			for(ApOngoFormImgDVo storedApOngoFormImgDVo : apOngoFormImgDVoList){
				model.put(storedApOngoFormImgDVo.getFormImgTypCd(), storedApOngoFormImgDVo);
			}
		}
		
		// 결재라인 최대수 - 스트립트용 JSON
		StringBuilder apvLnMaxCntBuilder = new StringBuilder(64);
		boolean apvLnMaxCntFirst = true;
		apvLnMaxCntBuilder.append('{');
		
		// 양식결재라인상세(AP_FORM_APV_LN_D) 테이블
		ApOngoFormApvLnDVo apOngoFormApvLnDVo = new ApOngoFormApvLnDVo();
		apOngoFormApvLnDVo.setFormId(formId);
		apOngoFormApvLnDVo.setFormSeq(formSeq);
		@SuppressWarnings("unchecked")
		List<ApOngoFormApvLnDVo> apOngoFormApvLnDVoList = (List<ApOngoFormApvLnDVo>)commonDao.queryList(apOngoFormApvLnDVo);
		
		int maxSum = 0, maxCnt = 0;
		if(apOngoFormApvLnDVoList!=null){
			
			for(ApOngoFormApvLnDVo storedApOngoFormApvLnDVo : apOngoFormApvLnDVoList){
				if(storedApOngoFormApvLnDVo.getMaxCnt()!=null && !storedApOngoFormApvLnDVo.getMaxCnt().isEmpty()){
					
					if("apvLn2LnAgr".equals(formApvLnTypCd)){
						maxCnt = Integer.parseInt(storedApOngoFormApvLnDVo.getMaxCnt());
						if(maxSum < maxCnt) maxSum = maxCnt;
					} else {
						maxSum += Integer.parseInt(storedApOngoFormApvLnDVo.getMaxCnt());
					}
				}
			}
			
			for(ApOngoFormApvLnDVo storedApOngoFormApvLnDVo : apOngoFormApvLnDVoList){
				if(storedApOngoFormApvLnDVo.getMaxCnt()!=null && !storedApOngoFormApvLnDVo.getMaxCnt().isEmpty()){
					
					if(apvLnMaxCntFirst) apvLnMaxCntFirst = false;
					else apvLnMaxCntBuilder.append(',');
					apvLnMaxCntBuilder.append('"').append(storedApOngoFormApvLnDVo.getApvLnTitlTypCd()).append('"');
					apvLnMaxCntBuilder.append(':').append(storedApOngoFormApvLnDVo.getMaxCnt());
					
					// 서명+리스트 일 경우 - 성명란 표시 숫자 외의 것을 숨기기 위한 것
					model.put("apvLnMaxCnt_"+storedApOngoFormApvLnDVo.getApvLnTitlTypCd(), storedApOngoFormApvLnDVo.getMaxCnt());
				}
				
				storedApOngoFormApvLnDVo.setMaxSum(maxSum);
				model.put(storedApOngoFormApvLnDVo.getApvLnTitlTypCd(), storedApOngoFormApvLnDVo);
				
				// apvLnWrtn=신청+처리(서면결재) - 처리(서면)영역 - 빈칸 출력용 빈 데이터 넣기
				if("apvLnWrtn".equals(formApvLnTypCd) && "prc".equals(storedApOngoFormApvLnDVo.getApvLnTitlTypCd())){
					if(storedApOngoFormApvLnDVo.getMaxCnt() != null && !storedApOngoFormApvLnDVo.getMaxCnt().isEmpty()){
						try{
							maxCnt = Integer.parseInt(storedApOngoFormApvLnDVo.getMaxCnt());
						} catch(Exception e){
							maxCnt = 0;
						}
					} else {
						maxCnt = 0;
					}
					if(maxCnt>0){
						List<ApOngdApvLnDVo> apvrList = new ArrayList<ApOngdApvLnDVo>();
						for(int i=0;i<maxCnt;i++){
							apvrList.add(new ApOngdApvLnDVo());
						}
						model.put("prcApvrList", apvrList);
					}
				}
				
			}
		}
		apvLnMaxCntBuilder.append('}');
		// 결재선 지정시 최대 설정 수 체크용
		model.put("apvLnMaxCnt", apvLnMaxCntBuilder.toString());
		
		// 항목지정
		// 양식항목상세(AP_FORM_ITEM_D) 테이블
		ApOngoFormItemDVo apOngoFormItemDVo = new ApOngoFormItemDVo();
		apOngoFormItemDVo.setFormId(formId);
		apOngoFormItemDVo.setFormSeq(formSeq);
		apOngoFormItemDVo.setOrderBy("FORM_COMB_SEQ");
		@SuppressWarnings("unchecked")
		List<ApOngoFormItemDVo> apOngoFormItemDVoList = (List<ApOngoFormItemDVo>) commonDao.queryList(apOngoFormItemDVo);
		
		// 양식항목내역(AP_FORM_ITEM_L) 테이블
		ApOngoFormItemLVo apOngoFormItemLVo = new ApOngoFormItemLVo();
		apOngoFormItemLVo.setFormId(formId);
		apOngoFormItemLVo.setFormSeq(formSeq);
		apOngoFormItemLVo.setOrderBy("FORM_COMB_SEQ, ROW_NO, COL_NO");
		@SuppressWarnings("unchecked")
		List<ApOngoFormItemLVo> apOngoFormItemLVoList = (List<ApOngoFormItemLVo>) commonDao.queryList(apOngoFormItemLVo);
		
		// 의견 항목이 있는지 여부
		boolean opinInDoc = false;
		
		// 다운로드 관련
		boolean refDocNmInDoc = false;
		boolean attFileInDoc  = false;
		boolean downDocYn = "Y".equals(model.get("downDocYn"));
		
		// ApOngoFormItemDVo 의 childList 세팅
		String itemSeq = null, maxItemSeq = null, itemId = null;
		int itemSeqNo = 0;
		List<ApOngoFormItemLVo> childList;
		if(apOngoFormItemDVoList!=null){
			for(ApOngoFormItemDVo storedApOngoFormItemDVo : apOngoFormItemDVoList){
				itemSeq = storedApOngoFormItemDVo.getFormCombSeq();
				
				itemSeqNo = Integer.parseInt(itemSeq);
				
				// 항목 지정 외 다른곳에서 설정한 경우 - 확장 고려 케이스
				if(itemSeqNo > 90000){
					
					// 최종결재+리스트 (결재라인 설정) - 에서 설정한 항목
					if("99999".equals(itemSeq)){
						ApOngoFormItemLVo[] oneTopItems = new ApOngoFormItemLVo[5];
						int no;
						
						if(apOngoFormItemLVoList!=null){
							for(ApOngoFormItemLVo storedApOngoFormItemLVo : apOngoFormItemLVoList){
								if(itemSeq.equals(storedApOngoFormItemLVo.getFormCombSeq())){
									no = Integer.parseInt(storedApOngoFormItemLVo.getRowNo());
									if(no>0 && no<6){
										oneTopItems[no-1] = storedApOngoFormItemLVo;
									}
								}
								itemId = storedApOngoFormItemLVo.getItemId();
								if("opin".equals(itemId)){
									opinInDoc = true;
								}
							}
						}
						model.put("oneTopItems", oneTopItems);
					}
					
					
				// 항목지정 에서 설정한 항목
				} else {
					maxItemSeq = itemSeq;
					
					childList = new ArrayList<ApOngoFormItemLVo>();
					if(apOngoFormItemLVoList!=null){
						for(ApOngoFormItemLVo storedApOngoFormItemLVo : apOngoFormItemLVoList){
							if(itemSeq.equals(storedApOngoFormItemLVo.getFormCombSeq())){
								childList.add(storedApOngoFormItemLVo);
							}
							itemId = storedApOngoFormItemLVo.getItemId();
							if("opin".equals(itemId)){
								opinInDoc = true;
							}
							if("refDocNm".equals(itemId)){
								refDocNmInDoc = true;
							}
							if("attFile".equals(itemId)){
								attFileInDoc = true;
							}
						}
					}
					if(!childList.isEmpty()){
						storedApOngoFormItemDVo.setChildList(childList);
					}
					model.put("items"+itemSeq, storedApOngoFormItemDVo);
				}
			}
		}
		model.put("opinInDoc", opinInDoc ? "Y" : "N");

		if(downDocYn){
			// 본문에 첨부나 참조문서가 없을때 다운로드용
			if(!refDocNmInDoc || !attFileInDoc){
				
				apOngoFormItemDVo = new ApOngoFormItemDVo();
				int rowCnt = (refDocNmInDoc ? 0 : 1) + (attFileInDoc ? 0 : 1);
				apOngoFormItemDVo.setRowCnt(Integer.toString(rowCnt));
				apOngoFormItemDVo.setColCnt("1");
				
				childList = new ArrayList<ApOngoFormItemLVo>();
				
				int row = 0;
				if(!attFileInDoc){
					apOngoFormItemLVo = new ApOngoFormItemLVo();
					apOngoFormItemLVo.setRowNo(Integer.toString(++row));
					apOngoFormItemLVo.setColNo("1");
					apOngoFormItemLVo.setCspnVa("1");
					apOngoFormItemLVo.setItemId("attFile");
					childList.add(apOngoFormItemLVo);
				}
				if(!refDocNmInDoc){
					apOngoFormItemLVo = new ApOngoFormItemLVo();
					apOngoFormItemLVo.setRowNo(Integer.toString(++row));
					apOngoFormItemLVo.setColNo("1");
					apOngoFormItemLVo.setCspnVa("1");
					apOngoFormItemLVo.setItemId("refDocNm");
					childList.add(apOngoFormItemLVo);
				}
				apOngoFormItemDVo.setChildList(childList);
				model.put("itemsDown", apOngoFormItemDVo);
			}
		}
		
		if(maxItemSeq != null) model.put("maxItemSeq", maxItemSeq);
	}
	
	/** 진행양식에 해당양식이 있는지 확인하여 없으면 추가고 formSeq 리턴  */
	public String storeOngoForm(QueryQueue queryQueue, String formId, ModelMap model) throws SQLException {
		
		// 양식기본(AP_FORM_B) 테이블 - 기존 양식 조회 - formSeq 확인용
		ApFormBVo apFormBVo = new ApFormBVo();
		apFormBVo.setFormId(formId);
		apFormBVo = (ApFormBVo)commonDao.queryVo(apFormBVo);
		if(apFormBVo==null) return null;
		String formSeq = apFormBVo.getFormSeq();
		
		boolean hasFormSeq = false;
		
		// 진행양식기본(AP_ONGO_FORM_B) 테이블 - 데이터 유무 검사
		ApOngoFormBVo apOngoFormBVo = new ApOngoFormBVo();
		apOngoFormBVo.setFormId(formId);
		apOngoFormBVo.setFormSeq(formSeq);
		if(commonDao.count(apOngoFormBVo)>0){
			hasFormSeq = true;
		}
		
		if(hasFormSeq){
			//  바닥글 조회
			ApOngoFormTxtDVo apOngoFormTxtDVo = new ApOngoFormTxtDVo();
			apOngoFormTxtDVo.setFormId(formId);
			apOngoFormTxtDVo.setFormSeq(formSeq);
			apOngoFormTxtDVo.setFormTxtTypCd("docFooter");
			apOngoFormTxtDVo = (ApOngoFormTxtDVo)commonDao.queryVo(apOngoFormTxtDVo);
			if(apOngoFormTxtDVo != null){
				String footerVa = apOngoFormTxtDVo.getTxtCont();
				if(footerVa!=null && !footerVa.isEmpty()){
					// 바닥글 값 설정 - 호출한 서비스에서 바닥글 설정함
					model.put("footerVa", footerVa);
				}
			}
			return formSeq;
		}
		
		// 진행양식기본(AP_ONGO_FORM_B) 테이블 - 복사
		apOngoFormBVo = new ApOngoFormBVo();
		apOngoFormBVo.fromMap(apFormBVo.toMap());
		queryQueue.insert(apOngoFormBVo);
		
		// 진행양식구성상세(AP_ONGO_FORM_COMB_D) 테이블 - 복사
		ApOngoFormCombDVo apOngoFormCombDVo;
		ApFormCombDVo apFormCombDVo = new ApFormCombDVo();
		apFormCombDVo.setFormId(formId);
		apFormCombDVo.setOrderBy("SORT_ORDR");
		@SuppressWarnings("unchecked")
		List<ApFormCombDVo> apFormCombDVoList = (List<ApFormCombDVo>)commonDao.queryList(apFormCombDVo);
		if(apFormCombDVoList!=null){
			for(ApFormCombDVo storedApFormCombDVo : apFormCombDVoList){
				apOngoFormCombDVo = new ApOngoFormCombDVo();
				apOngoFormCombDVo.fromMap(storedApFormCombDVo.toMap());
				apOngoFormCombDVo.setFormSeq(formSeq);
				queryQueue.insert(apOngoFormCombDVo);
			}
		}

		// 진행양식텍스트상세(AP_ONGO_FORM_TXT_D) 테이블 - 복사
		ApOngoFormTxtDVo apOngoFormTxtDVo;
		ApFormTxtDVo apFormTxtDVo = new ApFormTxtDVo();
		apFormTxtDVo.setFormId(formId);
		apFormTxtDVo.setOrderBy("FORM_TXT_TYP_CD");
		@SuppressWarnings("unchecked")
		List<ApFormTxtDVo> apFormTxtDVoList = (List<ApFormTxtDVo>)commonDao.queryList(apFormTxtDVo);
		if(apFormTxtDVoList!=null){
			for(ApFormTxtDVo storedApFormTxtDVo : apFormTxtDVoList){
				apOngoFormTxtDVo = new ApOngoFormTxtDVo();
				apOngoFormTxtDVo.fromMap(storedApFormTxtDVo.toMap());
				apOngoFormTxtDVo.setFormSeq(formSeq);
				queryQueue.insert(apOngoFormTxtDVo);
				
				if("docFooter".equals(storedApFormTxtDVo.getFormTxtTypCd())){
					String footerVa = storedApFormTxtDVo.getTxtCont();
					if(footerVa!=null && !footerVa.isEmpty()){
						// 바닥글 값 설정 - 호출한 서비스에서 바닥글 설정함
						model.put("footerVa", footerVa);
					}
				}
			}
		}
		
		// 진행양식이미지상세(AP_ONGO_FORM_IMG_D) 테이블 - 복사
		ApOngoFormImgDVo apOngoFormImgDVo;
		ApFormImgDVo apFormImgDVo = new ApFormImgDVo();
		apFormImgDVo.setFormId(formId);
		apFormImgDVo.setOrderBy("FORM_IMG_TYP_CD");
		@SuppressWarnings("unchecked")
		List<ApFormImgDVo> apFormImgDVoList = (List<ApFormImgDVo>)commonDao.queryList(apFormImgDVo);
		if(apFormTxtDVoList!=null){
			for(ApFormImgDVo storedApFormImgDVo : apFormImgDVoList){
				apOngoFormImgDVo = new ApOngoFormImgDVo();
				apOngoFormImgDVo.fromMap(storedApFormImgDVo.toMap());
				apOngoFormImgDVo.setFormSeq(formSeq);
				queryQueue.insert(apOngoFormImgDVo);
			}
		}
		
		// 진행양식결재라인상세(AP_ONGO_FORM_APV_LN_D) 테이블 - 복사
		ApOngoFormApvLnDVo apOngoFormApvLnDVo;
		ApFormApvLnDVo apFormApvLnDVo = new ApFormApvLnDVo();
		apFormApvLnDVo.setFormId(formId);
		apFormApvLnDVo.setOrderBy("FORM_COMB_SEQ, APV_LN_TITL_TYP_CD");
		@SuppressWarnings("unchecked")
		List<ApFormApvLnDVo> apFormApvLnDVoList = (List<ApFormApvLnDVo>)commonDao.queryList(apFormApvLnDVo);
		if(apFormApvLnDVoList!=null){
			for(ApFormApvLnDVo storedApFormApvLnDVo : apFormApvLnDVoList){
				apOngoFormApvLnDVo = new ApOngoFormApvLnDVo();
				apOngoFormApvLnDVo.fromMap(storedApFormApvLnDVo.toMap());
				apOngoFormApvLnDVo.setFormSeq(formSeq);
				queryQueue.insert(apOngoFormApvLnDVo);
			}
		}
		
		// 진행양식항목상세(AP_ONGO_FORM_ITEM_D) 테이블 - 복사
		ApOngoFormItemDVo apOngoFormItemDVo;
		ApFormItemDVo apFormItemDVo = new ApFormItemDVo();
		apFormItemDVo.setFormId(formId);
		apFormItemDVo.setOrderBy("FORM_COMB_SEQ");
		@SuppressWarnings("unchecked")
		List<ApFormItemDVo> apFormItemDVoList = (List<ApFormItemDVo>) commonDao.queryList(apFormItemDVo);
		if(apFormItemDVoList!=null){
			for(ApFormItemDVo storedApFormItemDVo : apFormItemDVoList){
				apOngoFormItemDVo = new ApOngoFormItemDVo();
				apOngoFormItemDVo.fromMap(storedApFormItemDVo.toMap());
				apOngoFormItemDVo.setFormSeq(formSeq);
				queryQueue.insert(apOngoFormItemDVo);
			}
		}
		
		// 진행양식항목내역(AP_ONGO_FORM_ITEM_L) 테이블 - 복사
		ApOngoFormItemLVo apOngoFormItemLVo;
		ApFormItemLVo apFormItemLVo = new ApFormItemLVo();
		apFormItemLVo.setFormId(formId);
		apFormItemLVo.setOrderBy("FORM_COMB_SEQ, ROW_NO, COL_NO");
		@SuppressWarnings("unchecked")
		List<ApFormItemLVo> apFormItemLVoList = (List<ApFormItemLVo>) commonDao.queryList(apFormItemLVo);
		if(apFormItemLVoList!=null){
			for(ApFormItemLVo storedApFormItemLVo : apFormItemLVoList){
				apOngoFormItemLVo = new ApOngoFormItemLVo();
				apOngoFormItemLVo.fromMap(storedApFormItemLVo.toMap());
				apOngoFormItemLVo.setFormSeq(formSeq);
				queryQueue.insert(apOngoFormItemLVo);
			}
		}
		
		return formSeq;
	}
	
}
