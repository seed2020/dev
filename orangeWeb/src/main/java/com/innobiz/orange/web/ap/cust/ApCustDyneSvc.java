package com.innobiz.orange.web.ap.cust;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.wo.vo.WoOnecApvLnDVo;
import com.innobiz.orange.web.wo.vo.WoOnecAuthDVo;
import com.innobiz.orange.web.wo.vo.WoOnecBVo;
import com.innobiz.orange.web.wo.vo.WoOnecHisLVo;

/** 고객사별 기능 서비스 - 다인소재 */
@Service
public class ApCustDyneSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApCustDyneSvc.class);

	/** 공통 DAO */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	
	
	/** [다인소재] One Card - UPDATE */
	public void updateHanwhaErp(Map<String, String> exMap) throws SQLException, IOException{
		
		//{onecNo=2, intgStatCd=cncl, intgTypCd=ERP_ONECARD, ver=1, apvNo=1441}
		//{onecNo=2, intgStatCd=ongo, intgTypCd=ERP_ONECARD, ver=1, apvNo=1442}
		
		//System.out.println(exMap);
		
		String onecNo		= (String)exMap.get("onecNo");
		String ver			= (String)exMap.get("ver");
		String apvNo		= (String)exMap.get("apvNo");
		String intgStatCd	= (String)exMap.get("intgStatCd");
		
		WoOnecBVo woOnecBVo;
		QueryQueue queryQueue = new QueryQueue();
		
		String statCd = "ongo".equals(intgStatCd) ? "askApv" : intgStatCd;
		
		// 완결이 아니면
		if(!"apvd".equals(intgStatCd)){
			
			// 이력 테이블 - 상태 변경
			woOnecBVo = new WoOnecBVo();
			woOnecBVo.setOnecNo(onecNo);
			woOnecBVo.setVer(ver);
			woOnecBVo.setHistory();
			
			woOnecBVo.setStatCd(statCd);
			
			if("ongo".equals(intgStatCd)){
				woOnecBVo.setApvNo(apvNo);
				woOnecBVo.setApvAskDt("sysdate");
			} else if("cncl".equals(intgStatCd) || "rejt".equals(intgStatCd)){
				woOnecBVo.setApvCmplDt("sysdate");
			}
			
			commonSvc.update(woOnecBVo);
			
			// 본 테이블 - 현재 상태 변경
			woOnecBVo = new WoOnecBVo();
			woOnecBVo.setOnecNo(onecNo);
			
			woOnecBVo.setStatCd(statCd);
			
			if("ongo".equals(intgStatCd)){
				woOnecBVo.setApvNo(apvNo);
				woOnecBVo.setApvAskDt("sysdate");
			} else if("cncl".equals(intgStatCd) || "rejt".equals(intgStatCd)){
				woOnecBVo.setApvCmplDt("sysdate");
			}
			
			commonSvc.update(woOnecBVo);
			
		// 결재 완결
		} else {// - if(!"apvd".equals(intgStatCd)){
			
			String apvdHisNo = "";
			// 원카드이력내역(WO_ONEC_HIS_L) 테이블 - 최종 이력 조회
			WoOnecHisLVo woOnecHisLVo = new WoOnecHisLVo();
			woOnecHisLVo.setOnecNo(onecNo);
			woOnecHisLVo.setOrderBy("HIS_NO DESC");
			woOnecHisLVo.setPageNo(1);
			woOnecHisLVo.setPageRowCnt(2);
			@SuppressWarnings("unchecked")
			List<WoOnecHisLVo> hisList = (List<WoOnecHisLVo>)commonSvc.queryList(woOnecHisLVo);
			if(hisList != null && !hisList.isEmpty()){
				apvdHisNo = hisList.get(0).getHisNo();
			}
			
			// 이력 테이블 - 상태 변경
			woOnecBVo = new WoOnecBVo();
			woOnecBVo.setOnecNo(onecNo);
			woOnecBVo.setVer(ver);
			woOnecBVo.setHistory();
			
			woOnecBVo.setStatCd(statCd);
			woOnecBVo.setApvCmplDt("sysdate");
			woOnecBVo.setApvdHisNo(apvdHisNo);
			commonSvc.update(woOnecBVo);
			
			// 본 테이블 데이터 삭제
			woOnecBVo = new WoOnecBVo();
			woOnecBVo.setOnecNo(onecNo);
			queryQueue.delete(woOnecBVo);
			
			// 이력 테이블 조회
			woOnecBVo = new WoOnecBVo();
			woOnecBVo.setOnecNo(onecNo);
			woOnecBVo.setVer(ver);
			woOnecBVo.setHistory();
			woOnecBVo = (WoOnecBVo)commonSvc.queryVo(woOnecBVo);
			if(woOnecBVo == null){
				LOGGER.error("No data WO_ONEC_BH - onecNo:"+onecNo+" ver:"+ver+" >> no approval action !");
				return;
			}
			
			woOnecBVo.setStatCd("apvd");
			woOnecBVo.setCurVer(woOnecBVo.getVer());
			woOnecBVo.setRegDt("sysdate");
			woOnecBVo.setApvCmplDt("sysdate");
			woOnecBVo.setApvdHisNo(apvdHisNo);
			
			queryQueue.insert(woOnecBVo);
			
			//결재선 조회
			ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
			apOngdApvLnDVo.setApvNo(apvNo);
			apOngdApvLnDVo.setOrderBy("APV_LN_PNO, APV_LN_NO");
			@SuppressWarnings("unchecked")
			List<ApOngdApvLnDVo> apvLnList = (List<ApOngdApvLnDVo>)commonSvc.queryList(apOngdApvLnDVo);
			
			if(apvLnList != null){
				
				// 결재선 - 권한에 추가
				WoOnecAuthDVo woOnecAuthDVo;
				String roleCd;
				for(ApOngdApvLnDVo vo : apvLnList){
					if(vo.getApvrUid()!=null && !vo.getApvrUid().isEmpty()){
						roleCd = vo.getApvrRoleCd();
						//byOne:1인결재, mak:기안, revw:검토, 
						//psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, 
						//byOneAgr:합의1인결재, makAgr:합의기안, 
						//abs:공석, apv:결재, 
						//pred:전결, entu:결재안함(위임), postApvd:사후보고(후열)
						if("byOne".equals(roleCd) || "mak".equals(roleCd)
								|| "revw".equals(roleCd) || "revw2".equals(roleCd) || "revw3".equals(roleCd)
								|| "psnOrdrdAgr".equals(roleCd) || "psnParalAgr".equals(roleCd)
								|| "byOneAgr".equals(roleCd) || "makAgr".equals(roleCd)
								|| "abs".equals(roleCd) || "apv".equals(roleCd)
								|| "pred".equals(roleCd) || "entu".equals(roleCd) || "postApvd".equals(roleCd)){
							
							woOnecAuthDVo = new WoOnecAuthDVo();
							woOnecAuthDVo.setOnecNo(onecNo);
							woOnecAuthDVo.setUserUid(vo.getApvrUid());
							woOnecAuthDVo.setAuthTypCd("byApv");
							woOnecAuthDVo.setRegDt("sysdate");
							queryQueue.store(woOnecAuthDVo);
						}
					}
				}
				
				// 결재선 복사
				WoOnecApvLnDVo toVo;
				for(ApOngdApvLnDVo vo : apvLnList){
					toVo = new WoOnecApvLnDVo();
					VoUtil.fromMap(toVo, VoUtil.toMap(vo, null));
					toVo.setOnecNo(onecNo);
					toVo.setVer(ver);
					queryQueue.insert(toVo);
				}
			}
			
		}
		
		// 일괄 실행
		commonSvc.execute(queryQueue);
	}
	
}
