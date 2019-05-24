package com.innobiz.orange.web.ap.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.innobiz.orange.web.ap.svc.ApBxSvc;
import com.innobiz.orange.web.ap.vo.ApOngdApvLnDVo;
import com.innobiz.orange.web.cm.exception.ApSecuException;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.Hash;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 결재선 정보 관리용 유틸 - 전체 경로선을 가지고 루트경로, 현재경로, 부서별 경로 등을 리턴함 */
public class ApvLines {

	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(ApvLines.class);

	/** 현재 부모 라인 번호 */
	private String apvLnPno = null;
	/** 전체 경로 */
	private List<ApOngdApvLnDVo> lines = null;
	/** 루트 경로 */
	private List<ApOngdApvLnDVo> rootLn = null;
	/** 현재 경로 */
	private List<ApOngdApvLnDVo> currLn = null;
	
	/** 생성자 */
	public ApvLines(List<ApOngdApvLnDVo> lines, String apvLnPno){
		this.lines = lines;
		this.apvLnPno = apvLnPno==null || apvLnPno.isEmpty() ? "0" : apvLnPno;
	}
	/** 최상위 라인 구하기 */
	public List<ApOngdApvLnDVo> getRootLn(){
		if(lines==null) return null;
		if(rootLn==null){
			List<ApOngdApvLnDVo> rootLn = new ArrayList<ApOngdApvLnDVo>();
			ApOngdApvLnDVo parentApOngdApvLnDVo = null;
			
			HashMap<Integer, ApOngdApvLnDVo> parentMap = new HashMap<Integer, ApOngdApvLnDVo>();
			
			for(ApOngdApvLnDVo apOngdApvLnDVo : lines){
				if("0".equals(apOngdApvLnDVo.getApvLnPno())){
					rootLn.add(apOngdApvLnDVo);
					parentMap.put(Integer.valueOf(apOngdApvLnDVo.getApvLnNo()), apOngdApvLnDVo);
				} else {
					parentApOngdApvLnDVo = parentMap.get(Integer.valueOf(apOngdApvLnDVo.getApvLnPno()));
					
					// 하위에 의견 있는지 체크함
					if(parentApOngdApvLnDVo != null && parentApOngdApvLnDVo.getSubOpinYn() == null){
						if(apOngdApvLnDVo.getApvOpinCont() != null && !apOngdApvLnDVo.getApvOpinCont().isEmpty()){
							if(ArrayUtil.isInArray(ApConstant.CMPL_APV_STAT_CDS, apOngdApvLnDVo.getApvStatCd())){
								parentApOngdApvLnDVo.setSubOpinYn("Y");
							}
						}
					}
				}
			}
			this.rootLn = rootLn;
		}
		return rootLn.isEmpty() ? null : rootLn;
	}
	/** 현재 라인 구하기 */
	public List<ApOngdApvLnDVo> getCurrLn(){
		if(lines==null) return null;
		if("0".equals(apvLnPno)){
			return getRootLn();
		} else if(currLn!=null){
			return currLn.isEmpty() ? null : currLn;
		} else {
			List<ApOngdApvLnDVo> currLn = new ArrayList<ApOngdApvLnDVo>();
			for(ApOngdApvLnDVo apOngdApvLnDVo : lines){
				if(apvLnPno.equals(apOngdApvLnDVo.getApvLnPno())){
					currLn.add(apOngdApvLnDVo);
				}
			}
			this.currLn = currLn;
		}
		return currLn.isEmpty() ? null : currLn;
	}
	/** 서브 라인 구하기 */
	public List<ApOngdApvLnDVo> getSubLnByApvLnPno(String apvLnPno){
		if(lines==null) return null;
		List<ApOngdApvLnDVo> subLn = new ArrayList<ApOngdApvLnDVo>();
		for(ApOngdApvLnDVo apOngdApvLnDVo : lines){
			if(apvLnPno.equals(apOngdApvLnDVo.getApvLnPno())){
				subLn.add(apOngdApvLnDVo);
			}
		}
		return subLn.isEmpty() ? null : subLn;
	}
	/** 서브라인 첫번째 정보 리턴 */
	public ApOngdApvLnDVo getSubLnFirst(String apvLnPno){
		if(lines==null) return null;
		for(ApOngdApvLnDVo apOngdApvLnDVo : lines){
			if(apvLnPno.equals(apOngdApvLnDVo.getApvLnPno())){
				return apOngdApvLnDVo;
			}
		}
		return null;
	}
	
	/** 결재 라인의 처리중인 자 - 병렬의 경우 그 중 첫번째 - 찾기 */
	public ApOngdApvLnDVo findFirstActiveApvr(String apvLnPno){
		if(lines==null || lines.isEmpty() || apvLnPno==null) return null;
		String apvStatCd;
		for(ApOngdApvLnDVo apvr : lines){
			if(apvLnPno.equals(apvr.getApvLnPno())){
				apvStatCd = apvr.getApvStatCd();
				if("inApv".equals(apvStatCd) || "reRevw".equals(apvStatCd) || "hold".equals(apvStatCd)){
					return apvr;
				} else if("inAgr".equals(apvStatCd)){
					return apvr;
				}
			}
		}
		return null;
	}
	
	/** 자신의 결재라인 찾기 */
	public ApOngdApvLnDVo findMyApvrLn(String apvLnPno, String apvLnNo, UserVo userVo, Locale locale) throws CmException{
		return findMyApvrLn(apvLnPno, apvLnNo, userVo, lines, locale);
	}
	/** 자신의 결재라인 찾기 */
	public ApOngdApvLnDVo findMyApvrLn(String apvLnPno, String apvLnNo, UserVo userVo, List<ApOngdApvLnDVo> apOngdApvLnDVoList, Locale locale) throws CmException{
		if(apOngdApvLnDVoList==null) return null;
		for(ApOngdApvLnDVo apOngdApvLnDVo : apOngdApvLnDVoList){
			if(apvLnPno.equals(apOngdApvLnDVo.getApvLnPno())
					&& apvLnNo.equals(apOngdApvLnDVo.getApvLnNo())){
				
				if(userVo != null){
					if("Y".equals(apOngdApvLnDVo.getApvrDeptYn())){
						if(!userVo.getDeptId().equals(apOngdApvLnDVo.getApvDeptId())){
							
							// 부서이력에 존재 여부
							boolean inHisList = false;
							try {
								// 부서이력
								List<String> apvDeptIdHisList = ApBxSvc.ins.getDeptHistoryList(userVo.getDeptId());
								if(apvDeptIdHisList!=null){
									for(String deptIdHisId : apvDeptIdHisList){
										if(apOngdApvLnDVo.getApvDeptId().equals(deptIdHisId)){
											inHisList = true;
											break;
										}
									}
								}
							} catch (SQLException ignore) {
							}
							
							if(!inHisList){
								LOGGER.error("Fail to find apvr !  apvNo:"+apOngdApvLnDVo.getApvNo()
										+"  apvLnPno:"+apvLnPno
										+"  apvLnNo:"+apvLnNo
										+"  apvDeptId:"+apOngdApvLnDVo.getApvDeptId()
										+"  userDeptId:"+userVo.getDeptId());
								//ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
								throw new ApSecuException("ap.msg.noApvr", locale);
							}
						}
					} else {
						if(	!userVo.getUserUid().equals(apOngdApvLnDVo.getApvrUid())
						&&  !ArrayUtil.isIn2Array(userVo.getAdurs(), 1, apOngdApvLnDVo.getApvrUid())
						&&	!ArrayUtil.isInArray(userVo.getAgntUids(), apOngdApvLnDVo.getApvrUid()) ){
							
							LOGGER.error("Fail to find apvr !  apvNo:"+apOngdApvLnDVo.getApvNo()
									+"  apvLnPno:"+apvLnPno
									+"  apvLnNo:"+apvLnNo
									+"  apvrUid:"+apOngdApvLnDVo.getApvrUid()
									+"  userUid:"+userVo.getUserUid()
									+ (userVo.getAgntUids()==null ? "" : "  agntUids:"+ArrayUtil.toString(userVo.getAgntUids())));
							//ap.msg.noApvr=결재자 정보를 확인 할 수 없습니다.
							throw new ApSecuException("ap.msg.noApvr", locale);
						}
					}
				}
				return apOngdApvLnDVo;
			}
		}
		return null;
	}
	
	/** [부서합의]의 서브라인을 현재 지정된 부서합의와 맞춤, - 가정:[합의기안]자는 해당 부서의 사용자 이어야 가능함 */
	public void transDeptAgrLn(QueryQueue queryQueue, List<ApOngdApvLnDVo> deptAgrList){
		// deptAgrList - 부서합의가 들어옴 - apvLnPno:0, apvLnNo:해당번호
		if(lines==null || deptAgrList==null || deptAgrList.isEmpty()) return;
		
		// 부서합의 합의기안자 를 담을 맵
		Map<Integer, ApOngdApvLnDVo> deptAgrMap = new HashMap<Integer, ApOngdApvLnDVo>();
		
		// 부서합의의 합의기안자를 맵에 담기
		for(ApOngdApvLnDVo apOngdApvLnDVo : lines){
			if(!"0".equals(apOngdApvLnDVo.getApvLnPno()) && "1".equals(apOngdApvLnDVo.getApvLnNo())){
				deptAgrMap.put(Hash.hashUid(apOngdApvLnDVo.getApvDeptId()), apOngdApvLnDVo);
			}
		}
		
		ApOngdApvLnDVo subVo, subTransVo;
		List<ApOngdApvLnDVo> subVoList;
		// 합의 부서 목록으로 루프 돌면서
		for(ApOngdApvLnDVo apOngdApvLnDVo : deptAgrList){
			subVo = deptAgrMap.get(Hash.hashUid(apOngdApvLnDVo.getApvDeptId()));
			// 저장된 경로에 부서ID 에 해당하는 부서합의가 있으면 
			if(subVo != null && !subVo.getApvLnPno().equals(apOngdApvLnDVo.getApvLnNo())){
				// 부서합의에 해당하는 저장된 경로를 구함
				subVoList = getSubLnByApvLnPno(subVo.getApvLnPno());
				// 저장된 경로가 있으면
				if(subVoList != null){
					
					// 저장된 경로 삭제
					subTransVo = new ApOngdApvLnDVo();
					subTransVo.setApvNo(subVo.getApvNo());
					subTransVo.setApvLnPno(subVo.getApvLnPno());
					queryQueue.delete(subTransVo);
					
					// 입력할 서브 경로 삭제 - 다른 데이터가 있을 수 도 있으므로 삭제
					subTransVo = new ApOngdApvLnDVo();
					subTransVo.setApvNo(subVo.getApvNo());
					subTransVo.setApvLnPno(apOngdApvLnDVo.getApvLnNo());
					queryQueue.delete(subTransVo);
					
					// 저장된 경로를 [결재라인부모번호]를 바꾸어 새로 입력
					for(ApOngdApvLnDVo stored : subVoList){
						stored.setApvLnPno(apOngdApvLnDVo.getApvLnNo());
						queryQueue.insert(stored);
					}
				}
			}
		}
	}
	/** 부모 결재라인 구하기 */
	public ApOngdApvLnDVo getParent(String apvLnPno){
		if(lines==null) return null;
		for(ApOngdApvLnDVo apOngdApvLnDVo : getRootLn()){
			if(apOngdApvLnDVo.getApvLnNo().equals(apvLnPno)){
				return apOngdApvLnDVo;
			}
		}
		return null;
	}
	/** 기안자 리턴 */
	public ApOngdApvLnDVo getMakr(){
		return lines==null || lines.isEmpty() ? null : lines.get(0);
	}
	/** 현재 부모라인 번호 리턴 */
	public String getCurrApvLnPno(){
		return apvLnPno;
	}
	/** 전체 라인 리턴 */
	public List<ApOngdApvLnDVo> getAllApvLn(){
		return lines;
	}
}
