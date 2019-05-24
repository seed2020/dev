package com.innobiz.orange.web.ap.svc;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.dao.ApDocNoDao;
import com.innobiz.orange.web.ap.vo.ApClsInfoDVo;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.ap.vo.ApOngdBVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOrgBVo;
import com.innobiz.orange.web.or.vo.OrOrgTreeVo;

/** 문서번호 체번 서비스 */
@Service
public class ApDocNoSvc {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApDocNoSvc.class);
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 결재문서 일련번호 생성용 프로시저 호출 */
	@Autowired
	private ApDocNoDao apDocNoDao;

	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
/*
 * 옵션
 * 
[문서번호 채번]
docNoDept=부서
	notUse		사용안함
	deptAbs		부서약어
	deptNm		부서명
docNoCat=분류번호
	notUse		사용안함
	use			사용
docNoYr=년도
	notUse		사용안함
	YYYY		YYYY
docNoSeqLen=일련번호
	3			3자리
	4			4자리
	5			5자리
	6			6자리
	7			7자리
	8			8자리
docNoFxLen=고정 길이 일련번호

[채번 기준일]
regRecLstBaseDt=등록대장
	stDt	기안일
	enDt	완료일
recvRecLstBaseDt=접수(배부) 대장
	sendDt	발송일
	recvDt	접수(배부)일

[회계 기준일]
recoMt=월
	1 ~ 12
recoDt=일
	1 ~ 31
//String docLangTypCd, 
*/
	
	/** 문서번호 세팅 - (recLstId - ongoing:진행문서, regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장) */
	public void setDocNo(ApOngdBVo apOngdBVo, ApOngdApvLnDVo apOngdApvLnDVo, 
			Map<String, String> optConfigMap, String recLstId, Locale locale) throws SQLException, CmException{
		StringBuilder builder = new StringBuilder(128);
		String cfgVa, va;
		
		boolean first = true;
		char noSpChar = "Y".equals(optConfigMap.get("useDash")) ? '-' : ' ';
		
		// [옵션] docNoInst=기관 - notUse:사용안함, instAbs:기관약어, instNm:기관명
		cfgVa = optConfigMap.get("docNoInst");
		if("instAbs".equals(cfgVa) || "instNm".equals(cfgVa)){
			OrOrgTreeVo orOrgTreeVo = orCmSvc.getOrgByOrgTypCd(apOngdApvLnDVo.getApvDeptId(), "G", apOngdBVo.getDocLangTypCd());
			if(orOrgTreeVo != null){
				va = "instAbs".equals(cfgVa) ? orOrgTreeVo.getOrgAbbrRescNm() : null;
				if(va==null || va.isEmpty()) va = orOrgTreeVo.getRescNm();
				builder.append(va);
				builder.append(noSpChar);
			}
		}
		
		// [옵션] docNoDept=부서 - notUse:사용안함, deptAbs:부서약어, deptNm:부서명
		cfgVa = optConfigMap.get("docNoDept");
		if("deptNm".equals(cfgVa)){//deptNm:부서명
			va = apOngdApvLnDVo.getApvDeptNm();
			if(va!=null && !va.isEmpty()) {
				if(first) first = false;
				else builder.append(noSpChar);
				builder.append(va);
			}
		} else if("deptAbs".equals(cfgVa)){//deptAbs:부서약어
			va = apOngdApvLnDVo.getApvDeptAbbrNm();
			if(va!=null && !va.isEmpty()){
				if(first) first = false;
				else builder.append(noSpChar);
				builder.append(va);
			// 부서약어 없으면 부서명으로
			} else {
				va = apOngdApvLnDVo.getApvDeptNm();
				if(va!=null && !va.isEmpty()) {
					if(first) first = false;
					else builder.append(noSpChar);
					builder.append(va);
				}
			}
		}
		
		// [옵션] docNoCat=분류번호 - notUse:사용안함, use:사용
		cfgVa = optConfigMap.get("docNoCat");
		// recLstId - ongoing:진행문서, regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		if("use".equals(cfgVa) && !"distRecLst".equals(recLstId)){// 배부대장 제외
			va = apOngdBVo.getClsInfoId();
			if(va==null || va.isEmpty()){
				//ap.trans.clsInfo=분류정보
				//ap.trans.notProcess={0}가(이) 설정되지 않아서 진행 할 수 없습니다.
				throw new CmException("ap.trans.notProcess", new String[]{"#ap.trans.clsInfo"}, locale);
			}
			ApClsInfoDVo apClsInfoDVo = new ApClsInfoDVo();
			apClsInfoDVo.setOrgId(apOngdApvLnDVo.getApvDeptId());
			apClsInfoDVo.setClsInfoId(va);
			apClsInfoDVo.setQueryLang(apOngdBVo.getDocLangTypCd());
			apClsInfoDVo = (ApClsInfoDVo)commonDao.queryVo(apClsInfoDVo);
			
			if(apClsInfoDVo==null){
				//ap.trans.clsInfoLngNm=분류정보의 어권별 명칭
				//ap.trans.notProcess={0}가(이) 설정되지 않아서 진행 할 수 없습니다.
				throw new CmException("ap.trans.notProcess", new String[]{"#ap.trans.clsInfoLngNm"}, locale);
			}
			
			va = apClsInfoDVo.getRescNm();
			if(va!=null && !va.isEmpty()) {
				if(first) first = false;
				else builder.append(noSpChar);
				builder.append(va);
			} else {
				//ap.trans.clsInfoLngNm=분류정보의 어권별 명칭
				//ap.trans.notProcess={0}가(이) 설정되지 않아서 진행 할 수 없습니다.
				throw new CmException("ap.trans.notProcess", new String[]{"#ap.trans.clsInfoLngNm"}, locale);
			}
		}

		// [옵션] docNoYr=년도 - notUse:사용안함, YYYY:4자리 년도, YY:2자리 년도
		String docNoYear = optConfigMap.get("docNoYr");
		String date = null;
		
		// recLstId - ongoing:진행문서, regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		if("regRecLst".equals(recLstId)){// regRecLst:등록 대장
			// [옵션] regRecLstBaseDt=등록대장 - stDt:기안일, enDt:완료일
			String optBaseDt = optConfigMap.get("regRecLstBaseDt");
			if("stDt".equals(optBaseDt)){//stDt:기안일
				date = apOngdBVo.getMakDt();
			} else {//enDt:완료일
				date = apOngdBVo.getCmplDt();
			}
		} else if("recvRecLst".equals(recLstId)){// recvRecLst:접수 대장
			// [옵션] recvRecLstBaseDt=접수(배부) 대장 - enDt:완료일, recvDt:접수(배부)일
			String optBaseDt = optConfigMap.get("recvRecLstBaseDt");
			if("enDt".equals(optBaseDt)){// enDt:완료일
				date = apOngdBVo.getCmplDt();
			} else {// recvDt:접수(배부)일
				date = apOngdBVo.getRecvDt();
			}
		} else if("distRecLst".equals(recLstId)){// distRecLst:배부 대장
			// [옵션] recvRecLstBaseDt=접수(배부) 대장 - enDt:완료일, recvDt:접수(배부)일
			String optBaseDt = optConfigMap.get("recvRecLstBaseDt");
			if("enDt".equals(optBaseDt)){// enDt:완료일
				date = apOngdBVo.getCmplDt();
			} else {// recvDt:접수(배부)일
				date = apOngdBVo.getRecvDt();
			}
		} else if("ongoing".equals(recLstId)){// ongoing:진행문서
			// 현재일
			date = commonDao.querySysdate(null);
		}
		
		if("sysdate".equals(date)){
			date = commonDao.querySysdate(null);
		}
		
		// (년도 기준일)회계 기준년 - recoMt:회계기준월, recoDt:회계기준일
		String year = getRecordYear(date, optConfigMap);
		
		// 4자리, 2자리
		if("YYYY".equals(docNoYear)){
			if(first) first = false;
			else builder.append(noSpChar);
			builder.append(year);
		} else if("YY".equals(docNoYear)){
			if(first) first = false;
			else builder.append(noSpChar);
			builder.append(year.substring(2));
		}
		
		// 진행중 문서가 아닐 경우
		if(!"ongoing".equals(recLstId)){
			// 조직도 조회
			OrOrgBVo orOrgBVo = new OrOrgBVo();
			orOrgBVo.setOrgId(apOngdApvLnDVo.getApvDeptId());
			orOrgBVo = (OrOrgBVo)commonDao.queryVo(orOrgBVo);
			if(orOrgBVo == null){
				LOGGER.error("Fail trans - no org data - orgId:"+apOngdApvLnDVo.getApvDeptId());
				//ap.msg.notFound={0} 정보를 찾지 못했습니다. - cols.dept=부서
				throw new CmException("ap.msg.notFound", new String[]{"#cols.dept"}, locale);
			}
			
			Long seq = apDocNoDao.nextDocNo(year, recLstId, apOngdApvLnDVo.getApvDeptId(), orOrgBVo.getRescId());
			if(seq.intValue() != 0){
				builder.append('-');
				// 고정길이 사용
				if("Y".equals(optConfigMap.get("docNoFxLen"))){
					String len = optConfigMap.get("docNoSeqLen");
					builder.append(String.format("%0"+len+"d", seq));
				} else {
					builder.append(seq);
				}
			}
		}
		
		// recLstId - ongoing:진행문서, regRecLst:등록 대장, recvRecLst:접수 대장, distRecLst:배부 대장
		if("recvRecLst".equals(recLstId) || "distRecLst".equals(recLstId)){// recvRecLst:접수 대장, distRecLst:배부 대장
			apOngdBVo.setRecvDocNo(builder.toString());
		} else {
			apOngdBVo.setDocNo(builder.toString());
		}
		
	}
	
	/** 기준 월일 전은 1년을 뺀 년도를 나머지는 해당 년도를 리턴 */
	public String getRecordYear(String fullDate, Map<String, String> optConfigMap){
		// [옵션] 회계기준일 - recoMt=월 : 1 ~ 12
		String baseMonth = optConfigMap.get("recoMt");
		// [옵션] 회계기준일 - recoDt=월 : 1 ~ 31
		String baseDate = optConfigMap.get("recoDt");
		String baseDt = (baseMonth.length()==1 ? "0" : "") + baseMonth + "-" + (baseDate.length()==1 ? "0" : "") + baseDate;
		if(baseDt.compareTo(fullDate.substring(5,10)) <= 0){
			return fullDate.substring(0, 4);
		} else {
			return Integer.toString(Integer.parseInt(fullDate.substring(0, 4)) - 1);
		}
	}
	
}
