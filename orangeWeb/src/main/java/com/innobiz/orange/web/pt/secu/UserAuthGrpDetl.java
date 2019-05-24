package com.innobiz.orange.web.pt.secu;

import java.util.ArrayList;
import java.util.List;

import com.innobiz.orange.web.cm.utils.ArrayUtil;

/** 사용자가 속한 그룹의(사용자권한그룹, 관리자권한그룹, 사용자그룹) 포함대상, 제외대상 목록 */
public class UserAuthGrpDetl {

	/** 제외대상 리스트 */
	private List<String> excList = null;
	
	/** 제외대상 배열 - 더할때는 list 사용하고 array 변환 후 array로 조회 함 */
	private String[] excls = null;
	
	/** 포함대상 리스트 */
	private List<String> incList = null;
	
	/** 포함대상 배열 - 더할때는 list 사용하고 array 변환 후 array로 조회 함 */
	private String[] incls = null;
	
	/** 권한그룹ID를 더함 */
	public void add(boolean excl, String authGrpId){
		if(excl){
			if(excList==null) excList = new ArrayList<String>();
			excList.add(authGrpId);
		} else {
			if(incList==null) incList = new ArrayList<String>();
			incList.add(authGrpId);
		}
	}
	
	/** 해당 사용자 리스트 리턴 */
	public String[] getAuthGrps(boolean excl){
		if(excl){
			return excls;
		} else {
			return incls;
		}
	}
	
	/** 리스트 배열로 변환 */
	public void prepare(){
		if(excList!=null){
			excls = ArrayUtil.toArray(excList);
			excList = null;
		}
		if(incList!=null){
			incls = ArrayUtil.toArray(incList);
			incList = null;
		}
	}
	
	/** StringBuilder에 해당 정보 더함 - 디버그용 */
	public void appendTo(String compId, String authGrpTypCd, boolean excl, StringBuilder builder){
		if(excl){
			if(excls!=null){//appendArrayTo
				builder.append(compId).append(':').append(authGrpTypCd).append(":EX:");
				ArrayUtil.appendArrayTo(builder, excls);
				builder.append('\n');
			}
			
		} else {
			if(incls!=null){
				builder.append(compId).append(':').append(authGrpTypCd).append(":IN:");
				ArrayUtil.appendArrayTo(builder, incls);
				builder.append('\n');
			}
		}
	}
}
