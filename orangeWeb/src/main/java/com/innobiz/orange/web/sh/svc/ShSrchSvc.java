package com.innobiz.orange.web.sh.svc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.ap.svc.ApCmSvc;
import com.innobiz.orange.web.dm.svc.DmAdmSvc;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.svc.PtSecuSvc;
import com.innobiz.orange.web.pt.svc.PtSysSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.sh.vo.ShKwdVo;
import com.innobiz.orange.web.sh.vo.ShSrchVo;

/** 검색 서비스 */
@Service("shSrchSvc")
public class ShSrchSvc {

	/** 포털 보안 서비스 */
	@Autowired
	private PtSecuSvc ptSecuSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 결재 공통 서비스 */
	@Autowired
	private ApCmSvc apCmSvc;
	
//	/** 결재 문서 서비스 */
//	@Autowired
//	private DmDocSvc dmDocSvc;
	
	/** 결재 관리자 서비스 */
	@Autowired
	private DmAdmSvc dmAdmSvc;
	
	/** 숫자 콤마 패턴 */
	private static Pattern NUMBER_PATTERN;
	
	static {
		//String regEx = "[\\+\\-]?[1-9]{0,3}([\\d]{3}[\\,]{1})?([\\d]{3}[\\,]{1})?([\\d]{3}[\\,]{1})?([\\d]{3}[\\,]{1})?([\\d]{3}[\\,]{1})?([\\.][\\d]*)?";
		String regEx = "^[+-]?[\\d,]*(\\.?\\d*)$";
		NUMBER_PATTERN = Pattern.compile(regEx);
	}
	
	public ShSrchVo createSrchVo(String mdRid, String word, UserVo userVo, String mdBxId) throws SQLException{
		
		if(word==null) return null;
		word = word.trim();
		if(word.isEmpty()) return null;
		
		String langTypCd = userVo.getLangTypCd();
		String compId = userVo.getCompId();
		String seculCd = userVo.getSeculCd();
		
		// 검색 VO
		ShSrchVo shSrchVo = new ShSrchVo();
		shSrchVo.setCompId(compId);
		shSrchVo.setMdRid(mdRid);
		
		// 키워드 목록
		List<ShKwdVo> kwdList = createKwdList(word, mdRid);
		if(kwdList==null) return null;
		shSrchVo.setKwdList(kwdList);
		
		// 대장, 게시판 설정
		if(mdBxId!=null && !mdBxId.isEmpty()){
			shSrchVo.setMdBxId(mdBxId);
		} else {
			// 메뉴에 붙어 있고 권한 있는 게시판, 결재 대장 목록
			List<String> mdBxIdList = ptSecuSvc.getAuthedMdIdsByMdRid(userVo, mdRid, null);
			if(mdBxIdList==null) return null;
			shSrchVo.setMdBxIdList(mdBxIdList);
		}
		
		// 보안등급 적용 여부
		boolean applySeculCd = false;
		
		// 결재의 경우 - 옵션 조회하여 보안등급 적용 대상 세팅
		if("AP".equals(mdRid)){
			Map<String, String> optConfigMap = apCmSvc.getOptConfigMap(null, compId);
			// 보안등급 적용 대상
			boolean isRegRecLst = "Y".equals(optConfigMap.get("regRecLstSecuLvl"));//등록 대장
			boolean isRecvRecLst = "Y".equals(optConfigMap.get("recvRecLstSecuLvl"));//접수 대장
			applySeculCd = isRegRecLst || isRecvRecLst;
			
			// 보안등급코드 적용 모듈함ID 목록
			List<String> seculAplyMdBxIdList = new ArrayList<String>();
			if(!isRegRecLst && isRecvRecLst){
				seculAplyMdBxIdList.add("regRecLst");
				shSrchVo.setSeculAplyMdBxIdList(seculAplyMdBxIdList);
			} else if(isRegRecLst && !isRecvRecLst){
				seculAplyMdBxIdList.add("recvRecLst");
				shSrchVo.setSeculAplyMdBxIdList(seculAplyMdBxIdList);
			}
			
		// 문서관리 경우
		} else if("DM".equals(mdRid)){
			applySeculCd = "B".equals(dmAdmSvc.getEnvConfigVal(compId, "listOpt"));
			
		// 게시는 보안등급 사용 안함
		} else if("BB".equals(mdRid)){
			applySeculCd = false;
		}
		
		// 보안 등급 적용 할 경우
		if(applySeculCd){
			
			List<String> seculCdList = new ArrayList<String>();
			
			// 자신보다 하위 보안등급 - 더하기
			if(seculCd!=null && !seculCd.isEmpty()){
				List<PtCdBVo> cdVoList = null;
				// 시스템 정책 조회
				Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
				// 회사별 보안등급코드 사용
				if("Y".equals(sysPlocMap.get("seculByCompEnable"))){
					// 보안등급코드
					cdVoList = ptCmSvc.getCdListEqCompId("SECUL_CD", langTypCd, compId, null);
				} else {
					// 보안등급코드
					cdVoList = ptCmSvc.getCdListByCompId("SECUL_CD", langTypCd, compId, null);
				}
				
				if(cdVoList!=null){
					boolean matched = false;
					for(PtCdBVo ptCdBVo : cdVoList){
						if(matched){
							seculCdList.add(ptCdBVo.getCd());
						} else if(seculCd.equals(ptCdBVo.getCd())){
							matched = true;
							seculCdList.add(ptCdBVo.getCd());
						}
					}
				}
			}
			
			seculCdList.add("none");
			shSrchVo.setSeculCdList(seculCdList);
		}
		
		///////////////////////////////////////
		// 권한 처리
		// 사용자
		shSrchVo.setUserUid(userVo.getUserUid());
		// 부서
		shSrchVo.setDeptId(userVo.getDeptId());
		
		if("BB".equals(mdRid)){
			// 부서하위포함
			String[] orgPids = userVo.getOrgPids();
			if(orgPids!=null && orgPids.length>0){
				List<String> pidList = new ArrayList<String>();
				for(String pid : orgPids){
					pidList.add(pid);
				}
				shSrchVo.setSubDeptList(pidList);
			}
		}
		
		// 조회 언어 설정
		shSrchVo.setQueryLang(langTypCd);
		
		return shSrchVo;
	}
	
	/** 검색어 목록 생성 */
	private List<ShKwdVo> createKwdList(String word, String mdRid){
		if(word==null) return null;
		word = word.replace('\t', ' ');
		word = word.replace('\n', ' ');
		word = word.replace('%', ' ');
		word = word.trim().toLowerCase();
		if(word.isEmpty()) return null;
		
		ShKwdVo shKwdVo;
		List<ShKwdVo> kwdList = new ArrayList<ShKwdVo>();
		for(String kwd : word.toLowerCase().split(" ")){
			kwd = kwd.trim();
			// 숫자 콤마형 - 콤마 제거
			if(NUMBER_PATTERN.matcher(kwd).matches()){
				kwd = kwd.replace(",", "");
			}
			shKwdVo = ShKwdVo.create(kwd, mdRid);
			if(shKwdVo!=null){
				kwdList.add(shKwdVo);
			}
		}
		return kwdList.isEmpty() ? null : kwdList;
	}
}
