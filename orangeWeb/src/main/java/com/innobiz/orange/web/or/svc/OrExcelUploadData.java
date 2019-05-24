package com.innobiz.orange.web.or.svc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.innobiz.orange.web.or.vo.OrOrgBVo;

/** 엑셀 업로드용 데이터 객체 */
public class OrExcelUploadData {
	
	/** 코드맵 - 분류코드 별 코드명-코드 맵 */
	private Map<String, Map<String, String>> nmCdMapByClsMap = new HashMap<String, Map<String, String>>();
	/** 코드맵 - 분류코드 별 코드-코드명 맵 */
	private Map<String, Map<String, String>> cdNmMapByClsMap = new HashMap<String, Map<String, String>>();
	/** 코드목록 */
	private Map<String, List<String>> cdListMap = new HashMap<String, List<String>>();
	/** 생성자 */
	public OrExcelUploadData(String langTypCd){
		init(langTypCd);
	}
	/** 초기화 함수 */
	private void init(String langTypCd) {
		
		if("ko".equals(langTypCd)){

			addCd("CodeCategory", new String[][]{
					{"직급", "GRADE_CD"},
					{"직책", "TITLE_CD"},
					{"직위", "POSIT_CD"},
					{"직무", "DUTY_CD"},
					{"보안등급", "SECUL_CD"},
					{"역할", "ROLE_CD"}
			});
			addCd("OrganizationType", new String[][]{
					{"회사", "C"},
					{"기관", "G"},
					{"부서", "D"},
					{"파트", "P"}
			});
			addCd("UserStatusCd", new String[][]{
					{"사용신청", "01"},
					{"근무중", "02"},
					{"휴직", "03"},
					{"정직", "04"},
					{"퇴직", "05"},
					{"해제", "11"},
					{"삭제", "99"}
			});
			addCd("Gender", new String[][]{
					{"남성", "M"},
					{"여성", "F"}
			});
		} else {

			addCd("CodeCategory", new String[][]{
					{"Grade", "GRADE_CD"},
					{"Title", "TITLE_CD"},
					{"Position", "POSIT_CD"},
					{"Duty", "DUTY_CD"},
					{"Security Level", "SECUL_CD"},
					{"Role", "ROLE_CD"}
			});
			addCd("OrganizationType", new String[][]{
					{"Company", "C"},
					{"Organization", "G"},
					{"Department", "D"},
					{"Part", "P"}
			});
			addCd("UserStatusCd", new String[][]{
					{"Applicated", "01"},
					{"On duty", "02"},
					{"Layoff", "03"},
					{"Suspend", "04"},
					{"Retired", "05"},
					{"Dismissed", "11"},
					{"Deleted", "99"}
			});
			addCd("Gender", new String[][]{
					{"Male", "M"},
					{"Female", "F"}
			});
		}
	}
	/** 코드명 조회 - 로그 또는 메세지용 */
	public String getCodeName(String clsCd, String cd){
		Map<String, String> cdNmMap = cdNmMapByClsMap.get(clsCd);
		return cdNmMap==null ? null : cdNmMap.get(cd);
	}
	/** 코드목록 더하기 - 초기화 함수용 */
	private void addCd(String clsNm, String[][] codeArray){
		Map<String, String> nmCdMap = new HashMap<String, String>();
		Map<String, String> cdNmMap = new HashMap<String, String>();
		List<String> cdList = new ArrayList<String>();
		for(String[] codes : codeArray){
			nmCdMap.put(codes[0], codes[1]);
			cdNmMap.put(codes[1], codes[0]);
			cdList.add(codes[1]);
		}
		nmCdMapByClsMap.put(clsNm, nmCdMap);
		cdNmMapByClsMap.put(clsNm, cdNmMap);
		cdListMap.put(clsNm, cdList);
	}
	/** 코드 세팅 */
	public void setCd(String clsNm, String cdVa, String cd){
		
		List<String> cdList = cdListMap.get(clsNm);
		if(cdList == null){
			cdList = new ArrayList<String>();
			cdListMap.put(clsNm, cdList);
		}
		if(cdList.contains(cd)){
			return;
		}
		cdList.add(cd);
		
		Map<String, String> map = nmCdMapByClsMap.get(clsNm);
		if(map == null){
			map = new HashMap<String, String>();
			nmCdMapByClsMap.put(clsNm, map);
		}
		map.put(cdVa, cd);
	}
	/** 코드 조회 */
	public String getCd(String clsNm, String cdVa, String errorCat, List<String> errorList){
		if(cdVa==null || cdVa.isEmpty()) return null;
		
		// 코드인지 확인해서 코드면 리턴
		List<String> cdList = cdListMap.get(clsNm);
		if(cdList != null && cdList.contains(cdVa)){
			return cdVa;
		}
		// 코드명 확인해 코드 리턴
		Map<String, String> map = nmCdMapByClsMap.get(clsNm);
		if(map != null){
			String cd = map.get(cdVa);
			if(cd!=null) return cd;
		}
		
		String errorMsg = errorCat + " : " + cdVa;
		if(!errorList.contains(errorMsg)){
			errorList.add(errorMsg);
		}
		return null;
	}
	
	/** 조직도 정보를 담고 있는 객채 */
	private Map<String, OrOrgBVo> orgMap = new HashMap<String, OrOrgBVo>();
	
	/** 조직 세팅 */
	public void setOrg(OrOrgBVo parentOrOrgBVo, OrOrgBVo orOrgBVo){
		orgMap.put(orOrgBVo.getRescNm(), orOrgBVo);
		orgMap.put(orOrgBVo.getRescNm()+"@"+(parentOrOrgBVo==null ? "ROOT" : parentOrOrgBVo.getRescNm()), orOrgBVo);
	}
	/** 조직 조회 */
	public OrOrgBVo getOrg(String orgNm){
		if(orgNm==null || orgNm.isEmpty()) return null;
		return orgMap.get(orgNm);
	}
}
