package com.innobiz.orange.web.cm.utils;

import java.util.ArrayList;
import java.util.List;

import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.or.vo.OrUserBVo;

/** 라이센스 체크용 */
public class FinderUtil {

	/** 라이센스 사용자 체크용 */
	private static Finder ins = null;
	
	/** 사용자의 userUid 목록을 세팅함 */
	public static void setUsers(List<OrUserBVo> orUserBVoList) {
		List<String> idList = new ArrayList<String>();
		if(orUserBVoList!=null){
			for(OrUserBVo storedOrUserBVo : orUserBVoList){
				idList.add(storedOrUserBVo.getUserUid());
			}
		}
		if(ins==null) ins = (Finder)License.ins;
		ins.setIds(idList);
	}

	/** 목록에 사용자가 없으면 -1 있으면 0 또는 그보다 큰수 리턴 */
	public static Integer find(String id, boolean withMessage) {
		return (ins!=null) ? ins.find(id, withMessage) : -1;
	}
	
	/** 초기화 여부 */
	public static boolean initialized(){
		return (ins!=null);
	}
	
	/** 관리용 UID 세팅 */
	public static void setMngUid(String uid){
		if(ins==null) ins = (Finder)License.ins;
		ins.setAdminUid(uid);
	}
}
