package com.innobiz.orange.web.dm.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.dm.dao.DmDocNoDao;
import com.innobiz.orange.web.dm.utils.DmConstant;
import com.innobiz.orange.web.dm.vo.DmDocLVo;
import com.innobiz.orange.web.dm.vo.DmDocNoDVo;
import com.innobiz.orange.web.dm.vo.DmFldBVo;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

@Service
public class DmDocNoSvc {
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 문서 ID 생성용 프로시저 호출 */
	@Autowired
	private DmDocNoDao dmDocNoDao;
	
	/** 관리 서비스 */
	@Resource(name = "dmAdmSvc")
	private DmAdmSvc dmAdmSvc;
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 문서번호 세팅 - (statCd - T(임시저장),S(상신),O(인수대기),M(이관대기),C(정상),D(삭제),W(작성중)) */
	public void setDocNo(DmDocLVo dmDocLVo, String storId, String langTypCd, String statCd, String compId, String fldId, String regrUid) throws SQLException, CmException{
		Map<String, String> envConfigMap = dmAdmSvc.getEnvConfigMap(null, compId);
		
		StringBuilder builder = new StringBuilder(128);
		
		boolean first = true;
		
		// 문서번호 설정 목록
		List<String> list = new ArrayList<String>();
		list.add(envConfigMap.get("docNoOpt1"));
		list.add(envConfigMap.get("docNoOpt2"));
		list.add(envConfigMap.get("docNoOpt3"));
		String date = commonDao.querySysdate(null);
		// 회계 기준년 - recoMt:회계기준월, recoDt:회계기준일
		String year = getRecordYear(date, envConfigMap);
		// 회사 또는 부서 폴더 인지 확인
		DmFldBVo topFldVo = dmAdmSvc.getTopTreeVo(storId, fldId, langTypCd);
		boolean isComp = DmConstant.FLD_COMP.equals(topFldVo.getFldGrpId());
		// 조직ID
		String orgId = null;
		DmFldBVo dmFldBVo = dmAdmSvc.getFldBVo(storId, fldId, langTypCd);
		OrOrgBVo orOrgBVo = null;
		if(dmFldBVo!=null){
			orOrgBVo = dmAdmSvc.getOrgInfo(compId, langTypCd, dmFldBVo.getFldGrpId(), null);
			if(orOrgBVo != null)	orgId = orOrgBVo.getOrgId();
		}
		if(orgId == null){
			// 등록자 정보 조회
			OrUserBVo orUserBVo = dmAdmSvc.getOrUserBVo(regrUid, langTypCd);
			orgId = orUserBVo.getOrgId();
			orOrgBVo = dmAdmSvc.getOrgInfo(compId, langTypCd, orgId, null);
		}
		for(String va : list){
			if("notUse".equals(va)) continue;
			if("fld".equals(va)){//폴더
				if(dmFldBVo==null) continue;
				if(first) first = false;
				else builder.append(' ');
				builder.append(dmFldBVo.getFldNm());
			}else if(va.startsWith("org")){//부서[명,약어]
				if(isComp){// 회사
					//회사정보 조회
					PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo(compId, langTypCd);
					if(first) first = false;
					else builder.append(' ');
					builder.append(ptCompBVo.getRescNm());
				}else{
					if("orgNm".equals(va)){
						if(first) first = false;
						else builder.append(' ');
						builder.append(orOrgBVo.getRescNm());
					}else if("orgAbs".equals(va)){
						if(first) first = false;
						else builder.append(' ');
						builder.append(orOrgBVo.getOrgAbbrRescNm()==null?orOrgBVo.getOrgNm():orOrgBVo.getOrgAbbrRescNm());
					}
				}
				
			}else if(va.startsWith("YY")){//연도[YY,YYYY]
				// 4자리, 2자리
				if("YYYY".equals(va)){
					if(first) first = false;
					else builder.append(' ');
					builder.append(year);
				} else if("YY".equals(va)){
					if(first) first = false;
					else builder.append(' ');
					builder.append(year.substring(2));
				}
			}else{
				if(first) first = false;
				else builder.append(' ');
				builder.append(va);
			}
		}
		
		//  (statCd - T(임시저장),R(상신),O(인수대기),M(이관대기),C(정상),D(삭제),W(작성중))
		if(statCd != null && "C".equals(statCd)){
			// 문서채번 기준(연도)
			String docNoDftYear = envConfigMap.get("docNoDftYear");
			if(docNoDftYear == null || "N".equals(docNoDftYear)) year = null;
			
			// 문서채번 기준(조직)
			String docNoDftOrg = envConfigMap.get("docNoDftOrg");
			if(docNoDftOrg == null || "N".equals(docNoDftOrg)) orgId = compId;
			
			// 연도 설정이 없을경우 기본연도(0000)으로 채번한다.
			if(year == null) year = DmConstant.DOC_NO_YEAR;
			// 프로시저
			Long seq = dmDocNoDao.nextDocNo(storId, year, isComp ? compId : orgId);
			if(seq.intValue() != 0){
				// 채번조건이 하나 이상 있을경우 ID 앞에 공백 추가 
				if(!builder.toString().isEmpty()) builder.append('-');
				// 고정길이 사용
				if("Y".equals(envConfigMap.get("docNoFxLen"))){
					String len = envConfigMap.get("docNoSeqLen");
					builder.append(String.format("%0"+len+"d", seq));
				} else {
					builder.append(seq);
				}
			}
		}
		dmDocLVo.setDocNo(builder.toString());
	}
	
	/** 기준 월일 전은 1년을 뺀 년도를 나머지는 해당 년도를 리턴 */
	public String getRecordYear(String fullDate, Map<String, String> envConfigMap){
		// [옵션] 회계기준일 - recoMt=월 : 1 ~ 12
		String baseMonth = envConfigMap.get("recoMt");
		// [옵션] 회계기준일 - recoDt=월 : 1 ~ 31
		String baseDate = envConfigMap.get("recoDt");
		String baseDt = (baseMonth.length()==1 ? "0" : "") + baseMonth + "-" + (baseDate.length()==1 ? "0" : "") + baseDate;
		if(baseDt.compareTo(fullDate.substring(5,10)) <= 0){
			return fullDate.substring(0, 4);
		} else {
			return Integer.toString(Integer.parseInt(fullDate.substring(0, 4)) - 1);
		}
	}
	
	/** 문서일련번호 조회 */
	public DmDocNoDVo getDmDocNoDVo(Map<String, String> envConfigMap, DmDocNoDVo dmDocNoDVo) throws SQLException{
		// 문서채번 기준(연도)
		String docNoDftYear = envConfigMap.get("docNoDftYear");
		if(docNoDftYear != null && "Y".equals(docNoDftYear)) {
			String date = commonDao.querySysdate(null);
			// 회계 기준년 - recoMt:회계기준월, recoDt:회계기준일
			String year = getRecordYear(date, envConfigMap);
			dmDocNoDVo.setYy(year);
		}else{
			dmDocNoDVo.setYy(DmConstant.DOC_NO_YEAR);
		}
					
		return (DmDocNoDVo)commonDao.queryVo(dmDocNoDVo);
	}
	
	/** 다음 문서 시퀀스 조회 */
	/*public Integer nextDocNo(String storId, String seqTyp, String year, String orgId) throws SQLException{
		DmDocNoDVo dmDocNoDVo = new DmDocNoDVo();
		dmDocNoDVo.setStorId(storId);
		dmDocNoDVo.setSeqTyp(seqTyp);
		if(year != null) dmDocNoDVo.setYy(year);
		if(orgId != null) dmDocNoDVo.setOrgId(orgId);
		
		if(commonDao.count(dmDocNoDVo) == 0){
			dmDocNoDVo.setDocId(1);
			commonDao.insert(dmDocNoDVo);
			dmDocNoDVo.setDocId(null);
		}
		
		DmDocNoDVo returnVo = (DmDocNoDVo)commonDao.queryVo(dmDocNoDVo);
		return returnVo.getDocId();
	}*/
	
}
