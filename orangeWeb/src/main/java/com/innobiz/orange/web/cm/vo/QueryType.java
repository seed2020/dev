package com.innobiz.orange.web.cm.vo;

/** 쿼리의 형태를 지정하는 enum, STORE는 update 또는 insert를 실행함 */
public enum QueryType {
	INSERT,
	UPDATE,
	DELETE,
	SELECT,
	STORE,
	COUNT
}
