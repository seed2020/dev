package com.innobiz.orange.web.bb.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.svc.BbCmSvc;
import com.innobiz.orange.web.bb.svc.BbRescSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaCdDVo;
import com.innobiz.orange.web.bb.vo.BaCdGrpBVo;
import com.innobiz.orange.web.bb.vo.BaRescBVo;
import com.innobiz.orange.web.bb.vo.BaTblColmDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.PersonalUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/* 게시물 (관리자) */
@Controller
public class BaCdCtrl {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(BaCdCtrl.class);

	/** 메세지 */
	@Autowired
	private MessageProperties messageProperties;

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** 게시판 공통 서비스 */
	@Autowired
	private BbCmSvc bbCmSvc;
	
	/** 리소스 조회 저장용 서비스 */
	@Resource(name = "bbRescSvc")
	private BbRescSvc bbRescSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** context.properties */
	@Resource(name = "contextProperties")
	private Properties contextProperties;
	
	/** 코드관리 목록조회 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/cd/listCd")
	public String listCd(HttpServletRequest request,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		// 코드그룹(BA_CD_GRP_B) 테이블 - BIND
		BaCdGrpBVo baCdGrpBVo = new BaCdGrpBVo();
		baCdGrpBVo.setQueryLang(langTypCd);
		VoUtil.bind(request, baCdGrpBVo);
		
		// 시스템 관리자 여부
		//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
		// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
		//if(!isSysAdmin){
			baCdGrpBVo.setCompId(userVo.getCompId());
		//}
				
		// 코드그룹(BA_CD_GRP_B) 테이블 - COUNT
		Integer recodeCount = commonSvc.count(baCdGrpBVo);
		PersonalUtil.setPaging(request, baCdGrpBVo, recodeCount);

		// 코드그룹(BA_CD_GRP_B) 테이블 - SELECT
		List<BaCdGrpBVo> baCdGrpBVoList = (List<BaCdGrpBVo>) commonSvc.queryList(baCdGrpBVo);

		for (BaCdGrpBVo storedBaCdGrpBVo : baCdGrpBVoList) {
			// 코드(BA_CD_D) 테이블 - SELECT
			BaCdDVo baCdDVo = new BaCdDVo();
			baCdDVo.setQueryLang(langTypCd);
			baCdDVo.setCdGrpId(storedBaCdGrpBVo.getCdGrpId());
			List<BaCdDVo> cdVoList = (List<BaCdDVo>) commonSvc.queryList(baCdDVo);
			storedBaCdGrpBVo.setBaCdDVoList(cdVoList);
		}

		model.put("recodeCount", recodeCount);
		model.put("baCdGrpBVoList", baCdGrpBVoList);

		return LayoutUtil.getJspPath("/bb/adm/cd/listCd");
	}

	/** [팝업] 코드관리 수정용 조회 및 등록 화면 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/cd/setCdPop")
	public String setCdPop(HttpServletRequest request,
			@RequestParam(value = "cdGrpId", required = false) String cdGrpId,
			ModelMap model) throws Exception {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);

		// 코드그룹(BA_CD_GRP_B) 테이블 - BIND
		BaCdGrpBVo baCdGrpBVo = new BaCdGrpBVo();
		baCdGrpBVo.setCdGrpId(cdGrpId);
		baCdGrpBVo.setQueryLang(langTypCd);

		// 수정인 경우
		if (cdGrpId != null && !cdGrpId.isEmpty()) {
			// 코드그룹(BA_CD_GRP_B) 테이블 - SELECT
			baCdGrpBVo = (BaCdGrpBVo) commonSvc.queryVo(baCdGrpBVo);
			if (baCdGrpBVo.getRescId() != null) {
				// 리소스기본(BA_RESC_B) 테이블 - 조회, 모델에 추가
				bbRescSvc.queryRescBVo(baCdGrpBVo.getRescId(), model);
			}

			// 코드(BA_CD_D) 테이블 - SELECT
			BaCdDVo baCdDVo = new BaCdDVo();
			baCdDVo.setQueryLang(langTypCd);
			baCdDVo.setCdGrpId(baCdGrpBVo.getCdGrpId());
			List<BaCdDVo> cdVoList = (List<BaCdDVo>) commonSvc.queryList(baCdDVo);
			for (BaCdDVo cdVo : cdVoList) {
				if (cdVo.getRescId() != null) {
					// 리소스기본(BA_RESC_B) 테이블 - 조회
					bbRescSvc.queryRescBVo(cdVo.getRescId(), model);
				}
			}

			// 화면 구성용 빈 vo 넣음
			cdVoList.add(new BaCdDVo());

			baCdGrpBVo.setBaCdDVoList(cdVoList);
		} else {
			List<BaCdDVo> cdVoList = new ArrayList<BaCdDVo>();

			// 화면 구성용 빈 vo 넣음
			cdVoList.add(new BaCdDVo());

			baCdGrpBVo.setBaCdDVoList(cdVoList);
		}

		model.put("baCdGrpBVo", baCdGrpBVo);
		
		// 회사목록
		List<PtCompBVo> ptCompBVoList = ptCmSvc.getFilteredCompList(null, "Y", langTypCd);
		model.put("ptCompBVoList", ptCompBVoList);
				
		return LayoutUtil.getJspPath("/bb/adm/cd/setCdPop");
	}

	/** 코드관리 저장 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/cd/transCd")
	public String transCd(HttpServletRequest request,
			ModelMap model) {

		try {
			// 세션의 사용자 정보
			UserVo userVo = LoginSession.getUser(request);

			// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT
			QueryQueue queryQueue = new QueryQueue();
			BaRescBVo baRescBVo = bbRescSvc.collectBaRescBVo(request, "grp", queryQueue);
			if (baRescBVo == null) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}

			// 코드관리(BA_CD_GRP_B) 테이블 - BIND
			BaCdGrpBVo baCdGrpBVo = new BaCdGrpBVo();
			VoUtil.bind(request, baCdGrpBVo);

			// 리소스 조회 후 리소스ID와 리소스명 세팅
			baCdGrpBVo.setRescId(baRescBVo.getRescId());
			baCdGrpBVo.setCdGrpNm(baRescBVo.getRescVa());

			// 수정자, 수정일시
			baCdGrpBVo.setModrUid(userVo.getUserUid());
			baCdGrpBVo.setModDt("sysdate");
			
			// 시스템 관리자 여부
			//boolean isSysAdmin = SecuUtil.hasAuth(request, "SYS", null);
			// 시스템 관리자가 아닌 경우에는 - 회사ID 추가
			//if(!isSysAdmin){
				baCdGrpBVo.setCompId(userVo.getCompId());
			//}
						
			if (baCdGrpBVo.getCdGrpId() == null || baCdGrpBVo.getCdGrpId().isEmpty()) {
				// 코드ID 생성
				baCdGrpBVo.setCdGrpId(bbCmSvc.createId("BA_CD_GRP_B"));
				// 코드관리(BA_CD_GRP_B) 테이블 - INSERT
				queryQueue.insert(baCdGrpBVo);

				// 코드(BA_CD_D) 테이블 - BIND
				List<BaCdDVo> boundBaCdDVoList = (List<BaCdDVo>) VoUtil.bindList(request, BaCdDVo.class, new String[]{"valid"});

				// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT, 코드(BA_CD_D) 테이블 - BIND
				bbRescSvc.collectBaRescBVoList(request, queryQueue, boundBaCdDVoList, "valid", "rescId", "cdVa");

				// 코드(BA_CD_D) 테이블 - INSERT
				for (BaCdDVo cdVo : boundBaCdDVoList) {
					// 코드ID, 코드그룹ID
					cdVo.setCdId(bbCmSvc.createId("BA_CD_D"));
					cdVo.setCdGrpId(baCdGrpBVo.getCdGrpId());
					// 수정자, 수정일시
					cdVo.setModrUid(baCdGrpBVo.getModrUid());
					cdVo.setModDt("sysdate");

					queryQueue.insert(cdVo);
				}

				commonSvc.execute(queryQueue);

			} else {
				// 코드관리(BA_CD_GRP_B) 테이블 - UPDATE
				queryQueue.update(baCdGrpBVo);

				// 코드(BA_CD_D) 테이블 - BIND
				List<BaCdDVo> boundBaCdDVoList = (List<BaCdDVo>) VoUtil.bindList(request, BaCdDVo.class, new String[]{"valid"});

				// 리소스기본(BA_RESC_B) 테이블 - UPDATE or INSERT, 코드(BA_CD_D) 테이블 - BIND
				bbRescSvc.collectBaRescBVoList(request, queryQueue, boundBaCdDVoList, "valid", "rescId", "cdVa");

				// 코드(BA_CD_D) 테이블
				for (BaCdDVo cdVo : boundBaCdDVoList) {
					// 코드그룹ID
					cdVo.setCdGrpId(baCdGrpBVo.getCdGrpId());
					// 수정자, 수정일시
					cdVo.setModrUid(baCdGrpBVo.getModrUid());
					cdVo.setModDt("sysdate");

					String deleted = cdVo.getDeleted();
					if (cdVo.getCdId() == null || cdVo.getCdId().isEmpty()) {
						// 코드ID
						cdVo.setCdId(bbCmSvc.createId("BA_CD_D"));
						// 코드(BA_CD_D) 테이블 - INSERT
						queryQueue.insert(cdVo);
					} else if ("Y".equals(deleted)) {
						// 리소스기본(BA_RESC_B) 테이블 - DELETE
						BaRescBVo rescVo = new BaRescBVo();
						rescVo.setRescId(cdVo.getRescId());
						queryQueue.delete(rescVo);
						// 코드(BA_CD_D) 테이블 - DELETE
						queryQueue.delete(cdVo);
					} else {
						// 코드(BA_CD_D) 테이블 - UPDATE
						queryQueue.update(cdVo);
					}
				}
				commonSvc.execute(queryQueue);
			}

			// cm.msg.save.success=저장 되었습니다.
			model.put("message", messageProperties.getMessage("cm.msg.save.success", request));
			model.put("todo", "parent.location.replace(parent.location.href);");

		} catch (CmException e) {
			LOGGER.error(e.getMessage());
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("exception", e);
		}

		return LayoutUtil.getResultJsp();
	}

	/** [AJAX] 코드관리 삭제 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/cd/transCdDelAjx")
	public String transCdDelAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			String message = null;

			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String cdGrpId = (String) object.get("cdGrpId");
			if (cdGrpId == null || cdGrpId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
			BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
			baTblColmDVo.setWhereSqllet(" AND T.COLM_TYP IN('CODE', 'CODECHK', 'CODERADIO') AND T.COLM_TYP_VAL = '"+cdGrpId+"'" );
			
			if (commonSvc.count(baTblColmDVo) > 0) {
				// bb.msg.codeExists=해당 코드그룹을 사용하는 테이블이 존재합니다.
				throw new CmException("bb.msg.codeExists", request);
			}

			// 코드그룹(BA_CD_GRP_B) 테이블 - SELECT
			QueryQueue queryQueue = new QueryQueue();
			BaCdGrpBVo baCdGrpBVo = new BaCdGrpBVo();
			baCdGrpBVo.setCdGrpId(cdGrpId);
			BaCdGrpBVo storedBaCdGrpBVo = (BaCdGrpBVo) commonSvc.queryVo(baCdGrpBVo);

			// 리소스기본(BA_RESC_B) 테이블 - DELETE
			if (storedBaCdGrpBVo.getRescId() != null && !storedBaCdGrpBVo.getRescId().isEmpty()) {
				BaRescBVo baRescBVo = new BaRescBVo();
				baRescBVo.setRescId(storedBaCdGrpBVo.getRescId());
				queryQueue.delete(baRescBVo);
			}

			// 코드그룹(BA_CD_GRP_B) 테이블 - DELETE
			queryQueue.delete(baCdGrpBVo);

			// 코드(BA_CD_D) 테이블 - SELECT
			BaCdDVo baCdDVo = new BaCdDVo();
			baCdDVo.setCdGrpId(cdGrpId);
			List<BaCdDVo> cdVoList = (List<BaCdDVo>) commonSvc.queryList(baCdDVo);

			// 리소스기본(BA_RESC_B) 테이블 - DELETE
			for (BaCdDVo cdVo : cdVoList) {
				if (cdVo.getRescId() != null && !cdVo.getRescId().isEmpty()) {
					BaRescBVo baRescBVo = new BaRescBVo();
					baRescBVo.setRescId(cdVo.getRescId());
					queryQueue.delete(baRescBVo);
				}
			}

			// 코드(BA_CD_D) 테이블 - DELETE
			queryQueue.delete(baCdDVo);

			commonSvc.execute(queryQueue);

			// cm.msg.del.success=삭제 되었습니다.
			message = messageProperties.getMessage("cm.msg.del.success", request);
			model.put("message", message);
			model.put("result", "ok");

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	
	
	/** [AJAX] 코드관리 삭제시 사용여부 체크 (관리자) */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bb/adm/cd/chkUseCdAjx")
	public String chkUseCdAjx(HttpServletRequest request,
			@RequestParam(value = "data", required = false) String data,
			ModelMap model) throws Exception {

		try {
			// 파라미터 검사
			JSONObject object = (JSONObject) JSONValue.parse(data);
			String cdGrpId = (String) object.get("cdGrpId");
			String cdId = (String) object.get("cdId");
			String msgCode = (String) object.get("msgCode");
			if (cdGrpId == null || cdGrpId.isEmpty() || cdId == null || cdId.isEmpty()) {
				// pt.msg.nodata.passed=데이터가 제대로 전달되지 않았습니다.
				throw new CmException("pt.msg.nodata.passed", request);
			}
			
			// 테이블컬럼(BA_TBL_COLM_D) 테이블 - SELECT
			BaTblColmDVo baTblColmDVo = new BaTblColmDVo();
			baTblColmDVo.setExColmYn("Y");//확장 컬럼 'Y'
			baTblColmDVo.setWhereSqllet(" AND T.COLM_TYP IN('CODE', 'CODECHK', 'CODERADIO') AND T.COLM_TYP_VAL = '"+cdGrpId+"'" );
			
			List<BaTblColmDVo> baTblColmDVoList = (ArrayList<BaTblColmDVo>)commonSvc.queryList(baTblColmDVo);
			
			if(baTblColmDVoList.size() == 0) return JsonUtil.returnJson(model);
			
			//해당 코드그룹을 사용하는 테이블 목록이 있을경우
			if(baTblColmDVoList.size() > 0){
				//테이블 id를 기준으로 컬럼정보 Map에 담음
				Map<String,List<String>> tblMap = new HashMap<String,List<String>>();
				boolean duplFlag;
				List<String> colmList = null;
				//컬럼 목록 
				for(BaTblColmDVo storedBaTblColmDVo : baTblColmDVoList){
					duplFlag = false;
					if(tblMap.containsKey(storedBaTblColmDVo.getTblId())){
						//테이블ID 맵이 있을경우 컬럼명 중복 체크[만일을 대비해 중복되지 않은 컬럼명만 맵에 담음]
						colmList = tblMap.get(storedBaTblColmDVo.getTblId());
						for(String colmNm : colmList){
							if(colmNm.equals(storedBaTblColmDVo.getColmNm())) {duplFlag = true; break;}
						}
						if(!duplFlag) tblMap.get(storedBaTblColmDVo.getTblId()).add(storedBaTblColmDVo.getColmNm());
					}else{
						//컬럼 목록을 생성하여 맵에 담음
						colmList = new ArrayList<String>();
						colmList.add(storedBaTblColmDVo.getColmNm());
						tblMap.put(storedBaTblColmDVo.getTblId(),colmList);
					}
				}
				
				if(tblMap.size() > 0){
					// 세션의 언어코드
					String langTypCd = LoginSession.getLangTypCd(request);
					
					// 게시판관리(BA_BRD_B) 테이블 - SELECT
					BaBrdBVo baBrdBVo = new BaBrdBVo();
					baBrdBVo.setQueryLang(langTypCd);
					
					Iterator<Entry<String, List<String>>> iterator = tblMap.entrySet().iterator();
					
					List<BaBrdBVo> baBrdBVoList = new ArrayList<BaBrdBVo>();
					Entry<String, List<String>> entry;
					BaBrdBVo brdVo = null;
					int count;
					while(iterator.hasNext()){
						entry = iterator.next();
						baBrdBVo.setTblId(entry.getKey());//테이블ID
						//코드그룹 테이블을 사용하는 게시판 목록을 조회한다
						baBrdBVoList = (List<BaBrdBVo>) commonSvc.queryList(baBrdBVo);
						if(baBrdBVoList.size() > 0){
							colmList = entry.getValue();
							for(BaBrdBVo storedBaBrdBVo : baBrdBVoList){
								// 게시판관리(BA_BRD_B) 테이블 - SELECT
								brdVo = bbBrdSvc.getBaBrdBVo(langTypCd, storedBaBrdBVo.getBrdId());
								// 게시물(BB_X000X_L) 테이블 - BIND
								BbBullLVo paramBullVo = bbBullSvc.newBbBullLVo(brdVo, true);
								if(colmList.size() > 0){
									for(String colm : colmList){
										setDbmsWhereLike(paramBullVo, colm, cdId);
										//paramBullVo.setWhereSqllet(" AND "+colm+" = '"+cdId+"'");
										count = 0;
										try{
											count = commonSvc.count(paramBullVo);
										} catch(Exception ignore){
											ignore.printStackTrace();
										}
										if(count>0){
											//pt.msg.not.del.cd.inUse=사용중인 코드는 삭제 할 수 없습니다.
											if(msgCode == null) msgCode = "pt.msg.not.del.cd.inUse";
											String msg = messageProperties.getMessage(msgCode, request)
													+ "\n- "+storedBaBrdBVo.getBrdNm();
											LOGGER.error(msg);
											throw new CmException(msg);
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (CmException e) {
			model.put("message", e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			model.put("message", e.getMessage());
		}

		return JsonUtil.returnJson(model);
	}
	
	/** 배열 중복 제거 */
	public String[] removeDuplicateArray(String[] array){
	    Object[] removeArray=null;
	    TreeSet<String> ts=new TreeSet<String>();
	    for(int i=0; i<array.length; i++){
		    ts.add(array[i]);
	    }
	    removeArray= ts.toArray();
	    return Arrays.copyOf(removeArray, removeArray.length, String[].class);
   }
	
	/** dbms 별 where 세팅 */
	public void setDbmsWhereLike(BbBullLVo paramBullVo, String colmNm, String value) {
		String dbms = contextProperties.getProperty("dbms");
		
		if("oracle".equals(dbms)) {
			paramBullVo.setWhereSqllet(" AND "+colmNm+" LIKE '%"+value+"%'");
		}else if("mysql".equals(dbms)) {
			paramBullVo.setWhereSqllet(" AND "+colmNm+" LIKE CONCAT('%', '"+value+"', '%')");
		}else {
			paramBullVo.setWhereSqllet(" AND "+colmNm+" LIKE '%"+value+"%'");
		}
	}
	
}
