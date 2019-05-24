--
--      GW_ORANGE 를 설치할 고객 코드로 변경해서 실행할것
--      시스템 계정으로 실행(권한부여)

-- 고객코드 계정으로
-- >> 1. ID 치환함수 생성(그룹웨어 계정)

DELIMITER //
CREATE FUNCTION FN_TOINT (ID VARCHAR(30))
RETURNS INT
BEGIN
	DECLARE V_SUM INT DEFAULT 0;
	DECLARE V_LEN INT;
	DECLARE V_ASCII INT;
	DECLARE V_POWER BIGINT DEFAULT 1;
	DECLARE V_INDEX INT DEFAULT 1;
	
	SET V_LEN = LENGTH(ID);
	SWHILE:WHILE V_INDEX < V_LEN DO		
		SET V_ASCII = ASCII(SUBSTRING(ID, V_LEN-V_INDEX+1,1));			
		IF V_ASCII >= 97
			THEN SET V_ASCII = V_ASCII - 87;
		ELSEIF V_ASCII >= 65
			THEN SET V_ASCII = V_ASCII - 55;
		ELSEIF V_ASCII >= 48
			THEN SET V_ASCII = V_ASCII - 48;
		ELSE
			SET V_ASCII = 0;
		END IF;
		SET V_SUM = V_SUM + (V_ASCII * V_POWER);
		SET V_POWER = V_POWER * 36;				
		SET V_INDEX = V_INDEX+1;		
	END WHILE SWHILE;	
	RETURN V_SUM;

END; //
DELIMITER ;

--      GW_ORANGE 를 설치할 고객 코드로 변경해서 실행할것
-- 함수 실행 권한부여 - sa
-- GRANT EXECUTE ON GW_ORANGE.FN_TOINT TO '[메일계정]'@'%';
-- GRANT EXECUTE ON GW_ORANGE.FN_TOINT TO [메일계정];

-- 함수 실행 권한회수 - sa
-- REVOKE EXECUTE ON GW_ORANGE.FN_TOINT FROM [메일계정];

-- 함수 삭제 - 그룹웨어
-- DROP FUNCTION FN_TOINT;

-- >> 2.그룹웨어 계정 접속해서 VIEW 생성- 그룹웨어 계정(첨부파일 참조) : 03.3개인, 03.4공용

