package com.innobiz.orange.web.wh.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.wh.dao.WhDocNoDao;
import com.innobiz.orange.web.wh.utils.WhConstant;
import com.innobiz.orange.web.wh.vo.WhDocNoDVo;
import com.innobiz.orange.web.wh.vo.WhMdBVo;
import com.innobiz.orange.web.wh.vo.WhReqBVo;

@Service
public class WhDocNoSvc {
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 문서 ID 생성용 프로시저 호출 */
	@Autowired
	private WhDocNoDao whDocNoDao;
	
	/** 관리 서비스 */
	@Resource(name = "whAdmSvc")
	private WhAdmSvc whAdmSvc;
	
//	/** 포탈 공통 서비스 */
//	@Autowired
//	private PtCmSvc ptCmSvc;
	
	/** 문서번호 세팅 */
	public void setDocNo(WhReqBVo whReqBVo, String langTypCd, String compId, String orgId, String mdId, String regrUid) throws SQLException, CmException{
		Map<String, String> envConfigMap = whAdmSvc.getEnvConfigMap(null, compId);
		
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
		// 조직ID
		OrOrgBVo orOrgBVo = null;
		if(orgId!=null){
			orOrgBVo = whAdmSvc.getOrgInfo(compId, langTypCd, orgId, null);
			if(orOrgBVo != null)	orgId = orOrgBVo.getOrgId();
		}else{
			// 등록자 정보 조회
			OrUserBVo orUserBVo = whAdmSvc.getOrUserBVo(regrUid, langTypCd);
			orgId = orUserBVo.getOrgId();
			orOrgBVo = whAdmSvc.getOrgInfo(compId, langTypCd, orgId, null);
		}
		
		char noSpChar = "Y".equals(envConfigMap.get("useDash")) ? '-' : ' ';
		
		WhMdBVo whMdBVo = null;
		for(String va : list){
			if("notUse".equals(va)) continue;
			if("mdNm".equals(va)){ // 모듈명
				if(mdId==null) continue;
				whMdBVo=whAdmSvc.getTopTreeVo(compId, mdId, langTypCd);
				if(whMdBVo==null) continue;
				if(first) first = false;
				else builder.append(noSpChar);
				builder.append(whMdBVo.getMdNm()); // 최상위 모듈명
			}else if(va.startsWith("org")){//부서[명,약어]
				if("orgNm".equals(va)){
					if(first) first = false;
					else builder.append(noSpChar);
					builder.append(orOrgBVo.getRescNm());
				}else if("orgAbs".equals(va)){
					if(first) first = false;
					else builder.append(noSpChar);
					builder.append(orOrgBVo.getOrgAbbrRescNm()==null?orOrgBVo.getOrgNm():orOrgBVo.getOrgAbbrRescNm());
				}
				
			}else if(va.startsWith("YY")){//연도[YY,YYYY], 연월일[YYYYMMDD]
				// 4자리, 2자리, 8자리
				if("YYYY".equals(va)){
					if(first) first = false;
					else builder.append(noSpChar);
					builder.append(year);
				} else if("YY".equals(va)){
					if(first) first = false;
					else builder.append(noSpChar);
					builder.append(year.substring(2));
				} else if("YYYYMMDD".equals(va)){
					if(first) first = false;
					else builder.append(noSpChar);
					builder.append(date.replaceAll("[-: ]", "").substring(0,8));
				}
			}else{
				if(first) first = false;
				else builder.append(noSpChar);
				builder.append(va);
			}
		}
		
		// 문서채번 기준(연도)
		String docNoDftYear = envConfigMap.get("docNoDftYear");
		if(docNoDftYear == null || "N".equals(docNoDftYear)) year = null;
		
		// 문서채번 기준(조직)
		String docNoDftOrg = envConfigMap.get("docNoDftOrg");
		if(docNoDftOrg == null || "N".equals(docNoDftOrg)) orgId = compId;
		
		// 연도 설정이 없을경우 기본연도(0000)으로 채번한다.
		if(year == null) year = WhConstant.DOC_NO_YEAR;
		// 프로시저
		Long seq = whDocNoDao.nextDocNo(year, orgId);
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
		whReqBVo.setDocNo(builder.toString());
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
	public WhDocNoDVo getWhDocNoDVo(Map<String, String> envConfigMap, WhDocNoDVo whDocNoDVo) throws SQLException{
		// 문서채번 기준(연도)
		String docNoDftYear = envConfigMap.get("docNoDftYear");
		if(docNoDftYear != null && "Y".equals(docNoDftYear)) {
			String date = commonDao.querySysdate(null);
			// 회계 기준년 - recoMt:회계기준월, recoDt:회계기준일
			String year = getRecordYear(date, envConfigMap);
			whDocNoDVo.setYy(year);
		}else{
			whDocNoDVo.setYy(WhConstant.DOC_NO_YEAR);
		}
					
		return (WhDocNoDVo)commonDao.queryVo(whDocNoDVo);
	}
	
	/** 다음 문서 시퀀스 조회 */
	/*public Integer nextDocNo(String storId, String seqTyp, String year, String orgId) throws SQLException{
		WhDocNoDVo whDocNoDVo = new WhDocNoDVo();
		whDocNoDVo.setStorId(storId);
		whDocNoDVo.setSeqTyp(seqTyp);
		if(year != null) whDocNoDVo.setYy(year);
		if(orgId != null) whDocNoDVo.setOrgId(orgId);
		
		if(commonDao.count(whDocNoDVo) == 0){
			whDocNoDVo.setDocId(1);
			commonDao.insert(whDocNoDVo);
			whDocNoDVo.setDocId(null);
		}
		
		WhDocNoDVo returnVo = (WhDocNoDVo)commonDao.queryVo(whDocNoDVo);
		return returnVo.getDocId();
	}*/
	
}
