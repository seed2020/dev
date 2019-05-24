package com.innobiz.orange.web.cm.utils;

import java.util.List;

/** 라이센스 체크용 인터페이스 */
public interface Finder {
	/** 사용자 UID 세팅 */
	public void setIds(List<String> idList);
	/** 사용자 UID 있는지 검사 */
	public Integer find(String id, boolean withMessage);
	/** 관리용 UID 세팅 */
	public void setAdminUid(String uid);
}
