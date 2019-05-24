package com.innobiz.orange.web.ap.utils;

import java.util.ArrayList;
import java.util.List;

import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 문서 저장에서 사용할 UTIL */
public class ApDocTransUtil {

	/** 결재라인VO목록 에서 결재라인번호에 해당하는 결재라인VO 찾기 */
	public static ApOngdApvLnDVo findApvLnByApvLnNo(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnNo){
		if(apOngdApvLnDVoList==null) return null;
		for(ApOngdApvLnDVo apOngdApvLnDVo : apOngdApvLnDVoList){
			if(apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())) return apOngdApvLnDVo;
		}
		return null;
	}
	
	/** index 찾기 */
	public static int findMyIndex(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnNo){
		if(apOngdApvLnDVoList==null) return -1;
		ApOngdApvLnDVo apOngdApvLnDVo = null;
		int i, size = apOngdApvLnDVoList.size();
		for(i=0; i<size;i++){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			if(apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
				return i;
			}
		}
		return -1;
	}
	
	/** 완료되지 않은 병렬합의 찾기 */
	public static ApOngdApvLnDVo findFirstNotDoneParalAgr(List<ApOngdApvLnDVo> apOngdApvLnDVoList, int myIndex, boolean withMe){
		if(apOngdApvLnDVoList==null || myIndex<0) return null;
		
		ApOngdApvLnDVo apOngdApvLnDVo, paralApOngdApvLnDVo = null;
		
		for(int i=myIndex-1; i>0; i--){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			if(ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd())){
				// inAgr:합의중
				if("inAgr".equals(apOngdApvLnDVo.getApvStatCd())){
					paralApOngdApvLnDVo = apOngdApvLnDVo;
				}
			} else {
				break;
			}
		}
		if(paralApOngdApvLnDVo != null) return paralApOngdApvLnDVo;
		if(withMe){
			return apOngdApvLnDVoList.get(myIndex);
		}
		
		// 내 다음 목록
		int i, size = apOngdApvLnDVoList.size();
		for(i=myIndex+1; i<size; i++){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			if(ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd())){
				// inAgr:합의중
				if("inAgr".equals(apOngdApvLnDVo.getApvStatCd())){
					return apOngdApvLnDVo;
				}
			} else {
				break;
			}
		}
		return null;
	}
	
	/** 동시공람이 완료되었는지 체크 */
	public static ApOngdApvLnDVo findNotDonSyncVw(List<ApOngdApvLnDVo> apOngdApvLnDVoList, int myIndex){
		if(apOngdApvLnDVoList==null || myIndex<0) return null;
		
		ApOngdApvLnDVo apOngdApvLnDVo;
		// 내 이전 공람자 체크
		for(int i=myIndex-1; i>0; i--){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			if("paralPubVw".equals(apOngdApvLnDVo.getApvrRoleCd())){
				if("inVw".equals(apOngdApvLnDVo.getApvStatCd())){
					return apOngdApvLnDVo;
				}
			} else {
				break;
			}
		}
		
		// 내 다음 공람자 체크
		int i, size = apOngdApvLnDVoList.size();
		for(i=myIndex+1; i<size; i++){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			if("paralPubVw".equals(apOngdApvLnDVo.getApvrRoleCd())){
				if("inVw".equals(apOngdApvLnDVo.getApvStatCd())){
					return apOngdApvLnDVo;
				}
			} else {
				break;
			}
		}
		return null;
	}
	
	/** 결재라인에서 이전 결재자 찾기 */
	public static ApOngdApvLnDVo getPrevApOngdApvLnDVo(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnNo){
		if(apOngdApvLnDVoList==null) return null;
		
		ApOngdApvLnDVo apOngdApvLnDVo;
		boolean afterMyTurn = false;
		for(int i=apOngdApvLnDVoList.size()-1; i>=0; i--){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(i);
			if(afterMyTurn){
				if(!"abs".equals(apOngdApvLnDVo.getApvrRoleCd())){
					return apOngdApvLnDVo;
				}
			} else {
				if(apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
					afterMyTurn = true;
				}
			}
		}
		return null;
	}
	
	/** 의견을 메일 통보할 결재자 목록 */
	public static List<ApOngdApvLnDVo> getOpinMailApvrList(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnPno, String apvLnNo){
		
		if(apOngdApvLnDVoList==null || apvLnPno==null || apvLnNo==null) return null;
		// 리턴할 목록
		List<ApOngdApvLnDVo> returnList = new ArrayList<ApOngdApvLnDVo>();
		List<ApOngdApvLnDVo> collectList = new ArrayList<ApOngdApvLnDVo>();
		
		ApOngdApvLnDVo apOngdApvLnDVo, nextAgrApOngdApvLnDVo=null;
		
		boolean afterMyTurn = false;
		boolean isSubLn = false;
		boolean isParall = false;
		int size = apOngdApvLnDVoList.size(), idx = size-1;
		
		String[] removeRoles = {"abs", "entu", "postApvd", "psnInfm", "deptInfm"};
		
		for(;idx>=0;idx--){
			apOngdApvLnDVo = apOngdApvLnDVoList.get(idx);
			
			if(afterMyTurn){
				
				if(isSubLn){
					
					if(apvLnPno.equals(apOngdApvLnDVo.getApvLnNo())){
						
						isSubLn = false;
						if(ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd())){
							isParall = true;
						}
					}
				} else if(isParall){
					if(!ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd())){
						isParall = false;
						if(!ArrayUtil.isInArray(removeRoles, apOngdApvLnDVo.getApvrRoleCd())){
							collectList.add(apOngdApvLnDVo);
						}
					}
					
				} else {
					if(!ArrayUtil.isInArray(removeRoles, apOngdApvLnDVo.getApvrRoleCd())){
						collectList.add(apOngdApvLnDVo);
					}
				}
				
			} else {
				
				if(apvLnPno.equals(apOngdApvLnDVo.getApvLnPno())){
					
					if(apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
						afterMyTurn = true;
						
						if(!"0".equals(apvLnPno)){
							if(nextAgrApOngdApvLnDVo!=null){
								return null;
							}
							isSubLn = true;
						} else {
							if(ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd())){
								isParall = true;
							}
						}
					} else {
						if(ApDocUtil.isNextApvr(apOngdApvLnDVo.getApvrRoleCd())){
							nextAgrApOngdApvLnDVo = apOngdApvLnDVo;
						}
					}
				}
				
			}
		}
		
		idx = collectList.size()-1;
		for(;idx>=0;idx--){
			apOngdApvLnDVo = collectList.get(idx);
			if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())){
				// 부서합의 라인
				for(ApOngdApvLnDVo forSubApOngdApvLnDVo : apOngdApvLnDVoList){
					if(apOngdApvLnDVo.getApvLnNo().equals(forSubApOngdApvLnDVo.getApvLnPno())){
						if(!ArrayUtil.isInArray(removeRoles, forSubApOngdApvLnDVo.getApvrRoleCd())){
//System.out.println("opin mail to : "+forSubApOngdApvLnDVo.getApvrNm());
							returnList.add(forSubApOngdApvLnDVo);
						}
					}
				}
			} else {
				// 루트 라인
//System.out.println("opin mail to : "+apOngdApvLnDVo.getApvrNm());
				returnList.add(apOngdApvLnDVo);
			}
		}
		
		return returnList;
	}
	
	/** 다음 결재자 목록 리턴 - 일반적으로 없거나 1명이지만 공석이 있을 때 에는 하나 이상이 가능 */
	public static List<ApOngdApvLnDVo> getNextApvrList(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnNo, boolean withoutAbs){
		if(apOngdApvLnDVoList==null) return null;
		// 리턴할 목록
		List<ApOngdApvLnDVo> returnList = new ArrayList<ApOngdApvLnDVo>();
		
		String apvrRoleCd;
		boolean afterMyTurn = false;
		// 내가 병렬합의 일 경우 - 내 다음의 병렬합의는 건너 뛰려고 세팅하는 변수 
		boolean currParalAgr = false;
		// 내 다음 결재자가 병렬합의 일 경우 - 다음 병렬합의 까지 포함하려고 세팅하는 변수
		boolean nextParalAgr = false;
		boolean nextFirst = true;
		boolean firstAbs = true;
		
		for(ApOngdApvLnDVo apOngdApvLnDVo: apOngdApvLnDVoList){
			
			if(afterMyTurn){
				apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();

				if(currParalAgr){
					// 내차례가 병렬이면 - 다음 결재자도 병렬 인지 체크
					if(ApDocUtil.isParalAgrOfApvrRole(apvrRoleCd)){
						continue;
					} else {
						currParalAgr = false;
					}
				}
				
				// abs:공석 - 더해줌
				if("abs".equals(apvrRoleCd)){
					if(firstAbs){// 앞의 공석이면, 병렬 뒤 공석 제외하기 위한것
						if(!withoutAbs){
							returnList.add(apOngdApvLnDVo);
						}
						continue;
					} else {
						break;
					}
				} else {
					firstAbs = false;
				}
				
				// 내 다음 차례가 병렬합의 인지
				if(nextFirst){
					nextParalAgr = ApDocUtil.isParalAgrOfApvrRole(apvrRoleCd);
					nextFirst = false;
				}
				
				// 내 다음 차례가 병렬합의 이면
				if(nextParalAgr){
					// 병렬합의만 더하고 나머지는 루프 종료
					if(ApDocUtil.isParalAgrOfApvrRole(apvrRoleCd)){
						returnList.add(apOngdApvLnDVo);
						continue;
					} else {
						break;
					}
				}
				
				// 결재 가능한 역할이면 - 더해줌
				if(ApDocUtil.isNextApvr(apvrRoleCd)){
					returnList.add(apOngdApvLnDVo);
				}
				break;
				
			} else {
				// 내 차례가 안되었을때 - 내차례가 되면
				if(apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
					// 내차례 세팅
					afterMyTurn = true;
					// 내차례가 병렬합의인지 세팅
					currParalAgr = ApDocUtil.isParalAgrOfApvrRole(apOngdApvLnDVo.getApvrRoleCd());
				}
			}
		}
		
		return returnList.isEmpty() ? null : returnList;
	}
	
	/** 다음 공람자 목록 리턴 */
	public static List<ApOngdApvLnDVo> getNextVwList(List<ApOngdApvLnDVo> apOngdApvLnDVoList, String apvLnNo){
		if(apOngdApvLnDVoList==null) return null;
		// 리턴할 목록
		List<ApOngdApvLnDVo> returnList = new ArrayList<ApOngdApvLnDVo>();
		
		String apvrRoleCd;
		boolean afterMyTurn = false;
		// 이전 차례가 동시공람인지 여부
		boolean prevParalVw = false;
		// 내 다음 차례가 동시공람인지 여부 - 다음 병렬합의 까지 포함하려고 세팅하는 변수
		boolean nextParalVw = false;
		boolean nextFirst = true;
		for(ApOngdApvLnDVo apOngdApvLnDVo: apOngdApvLnDVoList){
			if(afterMyTurn){
				apvrRoleCd = apOngdApvLnDVo.getApvrRoleCd();
				
				if(prevParalVw){
					// 이전(내차례 또는 이전 결재자가) 병렬이면 - 현재 결재자도 병렬 인지 체크
					if("paralPubVw".equals(apvrRoleCd)){
						continue;
					} else {
						prevParalVw = false;
					}
				}
				
				// 내 다음 차례가 병렬 인지
				if(nextFirst){
					nextParalVw = "paralPubVw".equals(apvrRoleCd);
					nextFirst = false;
				}
				
				// 내 다음 차례가 병렬 이면
				if(nextParalVw){
					// 병렬만 더하고 나머지는 루프 종료
					if("paralPubVw".equals(apvrRoleCd)){
						returnList.add(apOngdApvLnDVo);
						continue;
					} else {
						break;
					}
				}
				
				returnList.add(apOngdApvLnDVo);
				break;
				
			} else {
				// 내 차례가 안되었을때 - 내차례가 되면
				if(apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
					// 내차례 세팅
					afterMyTurn = true;
					// 동시공람인지 세팅
					prevParalVw = "paralPubVw".equals(apOngdApvLnDVo.getApvrRoleCd());
				}
			}
		}
		
		return returnList.isEmpty() ? null : returnList;
	}
	
	/** 결재자가 읽었는지 확인 */
	public static boolean wasRead(ApOngdApvLnDVo apOngdApvLnDVo){
		// cncl:취소, reRevw:재검토 - 읽음 처리 - 취소, 재검토 때에는 현재 작업할 것 이라는 걸 표시하기 위해 읽은날짜 지워줌
		if("cncl".equals(apOngdApvLnDVo.getApvStatCd())
				|| "reRevw".equals(apOngdApvLnDVo.getApvStatCd())) return true;
		// 공석이 아니고 조회일시가 있으면 - 공석은 읽었어도 무시함
		if(!"abs".equals(apOngdApvLnDVo.getApvrRoleCd())
				&& apOngdApvLnDVo.getVwDt()!=null && !apOngdApvLnDVo.getVwDt().isEmpty()){
			return true;
		}
		return false;
	}
	
	/** 합의기안자 생성 - 세션 정보로 합의기안자 VO 생성함 */
	public static ApOngdApvLnDVo createMakAgrVo(UserVo userVo, String apvLnPno){
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvLnPno(apvLnPno);
		apOngdApvLnDVo.setApvLnNo("1");
		apOngdApvLnDVo.setApvrUid(userVo.getUserUid());
		apOngdApvLnDVo.setApvDeptId(userVo.getDeptId());
		apOngdApvLnDVo.setApvrRoleCd("makAgr");
		apOngdApvLnDVo.setApvrDeptYn("N");
		apOngdApvLnDVo.setApvStatCd("inApv");
		return apOngdApvLnDVo;
	}
	
	/** 처리부서 기안자 생성 - 세션 정보로 처리부서 기안자 VO 생성함 */
	public static ApOngdApvLnDVo createPrcDeptVo(UserVo userVo, ApOngdApvLnDVo apOngdApvLnDVo){
		ApOngdApvLnDVo returnVo = new ApOngdApvLnDVo();
		returnVo.setApvLnPno(apOngdApvLnDVo.getApvLnPno());
		returnVo.setApvLnNo(apOngdApvLnDVo.getApvLnNo());
		returnVo.setApvrUid(userVo.getUserUid());
		returnVo.setApvDeptId(userVo.getDeptId());
		returnVo.setDblApvTypCd("prcDept");
		returnVo.setApvrRoleCd("revw");
		returnVo.setApvrDeptYn("N");
		returnVo.setApvStatCd("inApv");
		return returnVo;
	}
	
	/** 담당자 생성(접수문서의 기안자) - 세션 정보로 담당자 VO 생성함 */
	public static ApOngdApvLnDVo createMakVwVo(UserVo userVo, String apvNo){
		ApOngdApvLnDVo apOngdApvLnDVo = new ApOngdApvLnDVo();
		apOngdApvLnDVo.setApvNo(apvNo);
		apOngdApvLnDVo.setApvLnPno("0");
		apOngdApvLnDVo.setApvLnNo("1");
		apOngdApvLnDVo.setApvrUid(userVo.getUserUid());
		apOngdApvLnDVo.setApvDeptId(userVo.getDeptId());
		apOngdApvLnDVo.setApvrRoleCd("makVw");
		apOngdApvLnDVo.setApvrDeptYn("N");
		apOngdApvLnDVo.setApvStatCd("inVw");
		return apOngdApvLnDVo;
	}
}
