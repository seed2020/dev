--
--      GW_ORANGE 를 설치할 고객 코드로 변경해서 실행할것
--      시스템 계정으로 실행(권한부여)


-- >> 3. 뷰 SELECT 권한 부여 - 시스템계정 ==> 메일 계정

-- ## 원격 ##
-- 개인
GRANT SELECT ON GW_ORANGE.ZT_ADDRESS_GROUP TO '[메일계정]'@'%';
GRANT SELECT ON GW_ORANGE.ZT_ADDRESS_PERSON TO '[메일계정]'@'%';
GRANT SELECT ON GW_ORANGE.ZT_GROUP_PERSON TO '[메일계정]'@'%';
-- 공용
GRANT SELECT ON GW_ORANGE.ZT_C_ADDRESS_GROUP TO '[메일계정]'@'%';
GRANT SELECT ON GW_ORANGE.ZT_C_ADDRESS_PERSON TO '[메일계정]'@'%';
GRANT SELECT ON GW_ORANGE.ZT_C_GROUP_PERSON TO '[메일계정]'@'%';

-- ## 로컬 ##
-- 개인
GRANT SELECT ON GW_ORANGE.ZT_ADDRESS_GROUP TO '[메일계정]'@'localhost';
GRANT SELECT ON GW_ORANGE.ZT_ADDRESS_PERSON TO '[메일계정]'@'localhost';
GRANT SELECT ON GW_ORANGE.ZT_GROUP_PERSON TO '[메일계정]'@'localhost';
-- 공용
GRANT SELECT ON GW_ORANGE.ZT_C_ADDRESS_GROUP TO '[메일계정]'@'localhost';
GRANT SELECT ON GW_ORANGE.ZT_C_ADDRESS_PERSON TO '[메일계정]'@'localhost';
GRANT SELECT ON GW_ORANGE.ZT_C_GROUP_PERSON TO '[메일계정]'@'localhost';

-- flush privileges;

-- ------------------------------- 삭제 --------------------------------------------

-- 권한 회수 - sa
-- REVOKE SELECT ON GW_ORANGE.ZT_ADDRESS_GROUP FROM [메일계정];
-- REVOKE SELECT ON GW_ORANGE.ZT_ADDRESS_PERSON FROM [메일계정];
-- REVOKE SELECT ON GW_ORANGE.ZT_GROUP_PERSON FROM [메일계정];
-- REVOKE SELECT ON GW_ORANGE.ZT_C_ADDRESS_GROUP FROM [메일계정];
-- REVOKE SELECT ON GW_ORANGE.ZT_C_ADDRESS_PERSON FROM [메일계정];
-- REVOKE SELECT ON GW_ORANGE.ZT_C_GROUP_PERSON FROM [메일계정];

-- VIEW 삭제 - 그룹웨어
-- USE [그룹웨어DB명]
-- DROP VIEW ZT_ADDRESS_GROUP;
-- DROP VIEW ZT_ADDRESS_PERSON;
-- DROP VIEW ZT_GROUP_PERSON;
-- DROP VIEW ZT_C_ADDRESS_GROUP;
-- DROP VIEW ZT_C_ADDRESS_PERSON;
-- DROP VIEW ZT_C_GROUP_PERSON;

-- 기존 테이블명 변경
-- RENAME TABLE zt_address_group TO zt_address_group_temp;
-- RENAME TABLE zt_address_person TO zt_address_person_temp;
-- RENAME TABLE zt_group_person TO zt_group_person_temp;
-- RENAME TABLE zt_c_address_group TO zt_c_address_group_temp;
-- RENAME TABLE zt_c_address_person TO zt_c_address_person_temp;
-- RENAME TABLE zt_c_group_person TO zt_c_group_person_temp;



