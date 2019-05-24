--
--      AD8227 를 설치할 고객 코드로 변경해서 실행할것
--      sa 계정으로 실행(권한부여)

-- >> 1. ID 치환함수 생성 - 그룹웨어
-- USE [그룹웨어DB명]

CREATE FUNCTION [dbo].[FN_TOINT] (@ID varchar(8))
RETURNS INT
AS
BEGIN
	declare @v_sum INT=0;
	declare @v_len int;
	declare @v_ascii int;
	declare @v_power BIGINT = 1;
	declare @v_index int = 1;
	
	SET @v_len = LEN(@ID); -- 문자열길이	
	WHILE @v_index < @v_len
	BEGIN
		SET @v_ascii = ASCII(SUBSTRING(@ID, @v_len-@v_index+1,1));
		IF @v_ascii >= 97
			SET @v_ascii = @v_ascii - 87;
		ELSE IF @v_ascii >= 65
			SET @v_ascii = @v_ascii - 55;
		ELSE IF @v_ascii >= 48
			SET @v_ascii = @v_ascii - 48;
		ELSE
			SET @v_ascii = 0;
		SET @v_sum = @v_sum + (@v_ascii * @v_power);
		SET @v_power = @v_power * 36;				
		SET @v_index += 1;		
	END;	
	RETURN @v_sum;

END;

-- 함수 실행 권한 부여 - sa
-- GRANT EXECUTE ON dbo.FN_TOINT TO [메일계정];

-- 함수 실행 권한 회수 - sa
-- REVOKE EXECUTE ON dbo.FN_TOINT FROM [메일계정];

-- 함수 삭제 - 그룹웨어
-- DROP FUNCTION FN_TOINT;


-- >> 2.그룹웨어 계정 접속해서 VIEW 생성- 그룹웨어(첨부파일 참조) : 03.3개인, 03.4공용



-- >> 3. 보안 -> 로그인 -> 메일계정 속성 --> 사용자매핑 선택 --> 그룹웨어DB 선택 --> 하단 권한에서 db_datareader 체크 --> 저장

-- >> 4. 뷰 SELECT 권한 부여(메일 계정) - sa ==> 3번 실행시 생략
-- USE [GW_AD8227]
-- GO

-- 개인
-- GRANT SELECT ON dbo.ZT_ADDRESS_GROUP TO [메일계정];
-- GRANT SELECT ON dbo.ZT_ADDRESS_PERSON TO [메일계정];
-- GRANT SELECT ON dbo.ZT_GROUP_PERSON TO [메일계정];
-- 공용
-- GRANT SELECT ON dbo.ZT_C_ADDRESS_GROUP TO [메일계정];
-- GRANT SELECT ON dbo.ZT_C_ADDRESS_PERSON TO [메일계정];
-- GRANT SELECT ON dbo.ZT_C_GROUP_PERSON TO [메일계정];

--4. SYNONYM 생성 - sa
USE [메일DB명]
GO
--개인
CREATE SYNONYM dbo.ZT_ADDRESS_GROUP FOR [그룹웨어DB].dbo.ZT_ADDRESS_GROUP;
CREATE SYNONYM dbo.ZT_ADDRESS_PERSON FOR [그룹웨어DB].dbo.ZT_ADDRESS_PERSON;
CREATE SYNONYM dbo.ZT_GROUP_PERSON FOR [그룹웨어DB].dbo.ZT_GROUP_PERSON;
--공용
CREATE SYNONYM dbo.ZT_C_ADDRESS_GROUP FOR [그룹웨어DB].dbo.ZT_C_ADDRESS_GROUP;
CREATE SYNONYM dbo.ZT_C_ADDRESS_PERSON FOR [그룹웨어DB].dbo.ZT_C_ADDRESS_PERSON;
CREATE SYNONYM dbo.ZT_C_GROUP_PERSON FOR [그룹웨어DB].dbo.ZT_C_GROUP_PERSON;


--------------------------------- 삭제 --------------------------------------------

-- SYNONYM 삭제 - sa
--USE [메일DB명]
--GO
--개인
--DROP SYNONYM dbo.ZT_ADDRESS_GROUP;
--DROP SYNONYM dbo.ZT_ADDRESS_PERSON;
--DROP SYNONYM dbo.ZT_GROUP_PERSON;
--공용
--DROP SYNONYM dbo.ZT_C_ADDRESS_GROUP;
--DROP SYNONYM dbo.ZT_C_ADDRESS_PERSON;
--DROP SYNONYM dbo.ZT_C_GROUP_PERSON;

-- 권한 회수 - sa
--USE [메일DB명]
-- REVOKE SELECT ON dbo.ZT_ADDRESS_GROUP FROM [메일계정];
-- REVOKE SELECT ON dbo.ZT_ADDRESS_PERSON FROM [메일계정];
-- REVOKE SELECT ON dbo.ZT_GROUP_PERSON FROM [메일계정];
-- REVOKE SELECT ON dbo.ZT_C_ADDRESS_GROUP FROM [메일계정];
-- REVOKE SELECT ON dbo.ZT_C_ADDRESS_PERSON FROM [메일계정];
-- REVOKE SELECT ON dbo.ZT_C_GROUP_PERSON FROM [메일계정];

-- VIEW 삭제 - 그룹웨어
-- USE [그룹웨어DB명]
-- DROP VIEW ZT_ADDRESS_GROUP;
-- DROP VIEW ZT_ADDRESS_PERSON;
-- DROP VIEW ZT_GROUP_PERSON;
-- DROP VIEW ZT_C_ADDRESS_GROUP;
-- DROP VIEW ZT_C_ADDRESS_PERSON;
-- DROP VIEW ZT_C_GROUP_PERSON;

-- 기존 테이블명 변경
-- SP_RENAME 'ZT_ADDRESS_GROUP','ZT_ADDRESS_GROUP_TEMP';
-- SP_RENAME 'ZT_ADDRESS_PERSON','ZT_ADDRESS_PERSON_TEMP';
-- SP_RENAME 'ZT_GROUP_PERSON','ZT_GROUP_PERSON_TEMP';
-- SP_RENAME 'ZT_C_ADDRESS_GROUP','ZT_C_ADDRESS_GROUP_TEMP';
-- SP_RENAME 'ZT_C_ADDRESS_PERSON','ZT_C_ADDRESS_PERSON_TEMP';
-- SP_RENAME 'ZT_C_GROUP_PERSON','ZT_C_GROUP_PERSON_TEMP';
