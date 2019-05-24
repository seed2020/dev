
-- [ 사이트별 변경 사항 ]
-- ABC123 >> [고객코드]

-- 주의 : root 계정으로 실행 할 것

------------------------[ 메일 연계 사용자 ]------------------------

create user 'GW_MAIL'@'%' identified by 'mail123$%^';
create user 'GW_MAIL'@'localhost' identified by 'mail123$%^';
flush privileges;

GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_B TO 'GW_MAIL'@'%';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_FILE_D TO 'GW_MAIL'@'%';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.EM_SEND_EMAIL_B TO 'GW_MAIL'@'%';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.EM_SEND_EMAIL_FILE_D TO 'GW_MAIL'@'%';

GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_B TO 'GW_MAIL'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_FILE_D TO 'GW_MAIL'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.EM_SEND_EMAIL_B TO 'GW_MAIL'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.EM_SEND_EMAIL_FILE_D TO 'GW_MAIL'@'localhost';
flush privileges;

------------------------[ ERP 연계 사용자 ]------------------------

create user 'GW_ERP'@'%' identified by 'erp123$%^';
create user 'GW_ERP'@'localhost' identified by 'erp123$%^';
flush privileges;

-- --
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.AP_ERP_INTG_B TO 'GW_ERP'@'%';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.AP_ERP_INTG_FILE_D TO 'GW_ERP'@'%';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.AP_ERP_INTG_CHIT_D TO 'GW_ERP'@'%';

GRANT SELECT ON GW_ORANGE.AP_APV_LN_GRP_B TO 'GW_ERP'@'%';
GRANT SELECT ON GW_ORANGE.OR_ODUR_B TO 'GW_ERP'@'%';
GRANT SELECT ON GW_ORANGE.OR_USER_B TO 'GW_ERP'@'%';
GRANT SELECT ON GW_ORANGE.OR_ORG_B TO 'GW_ERP'@'%';

GRANT SELECT ON GW_ORANGE.AP_ONGD_APV_LN_D TO 'GW_ERP'@'%';
GRANT SELECT ON GW_ORANGE.PT_RESC_B TO 'GW_ERP'@'%';
GRANT SELECT ON GW_ORANGE.PT_CD_B TO 'GW_ERP'@'%';

-- 시퀀스
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_B TO 'GW_ERP'@'%';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_FILE_D TO 'GW_ERP'@'%';
GRANT EXECUTE ON PROCEDURE GW_ORANGE.SP_SEQ TO 'GW_ERP'@'%';
-- --
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.AP_ERP_INTG_B TO 'GW_ERP'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.AP_ERP_INTG_FILE_D TO 'GW_ERP'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.AP_ERP_INTG_CHIT_D TO 'GW_ERP'@'localhost';

GRANT SELECT ON GW_ORANGE.AP_APV_LN_GRP_B TO 'GW_ERP'@'localhost';
GRANT SELECT ON GW_ORANGE.OR_ODUR_B TO 'GW_ERP'@'localhost';
GRANT SELECT ON GW_ORANGE.OR_USER_B TO 'GW_ERP'@'localhost';
GRANT SELECT ON GW_ORANGE.OR_ORG_B TO 'GW_ERP'@'localhost';

GRANT SELECT ON GW_ORANGE.AP_ONGD_APV_LN_D TO 'GW_ERP'@'localhost';
GRANT SELECT ON GW_ORANGE.PT_RESC_B TO 'GW_ERP'@'localhost';
GRANT SELECT ON GW_ORANGE.PT_CD_B TO 'GW_ERP'@'localhost';

-- 시퀀스
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_B TO 'GW_ERP'@'localhost';
GRANT SELECT, UPDATE, DELETE, INSERT ON GW_ORANGE.CM_EMAIL_FILE_D TO 'GW_ERP'@'localhost';
GRANT EXECUTE ON PROCEDURE GW_ORANGE.SP_SEQ TO 'GW_ERP'@'localhost';
-- --
flush privileges;

------------------ [[ 메일용 계정 / 테이블 스페이스 생성 ]] -----------------------
create user 'MAIL_ABC123_USER'@'%' identified by 'asdqwe123$%^';
create user 'MAIL_ABC123_USER'@'localhost' identified by 'asdqwe123$%^';
flush privileges;

create database MAIL_ORANGE DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
grant all privileges on MAIL_ORANGE.* to 'MAIL_ABC123_USER'@'%';
grant all privileges on MAIL_ORANGE.* to 'MAIL_ABC123_USER'@'localhost';

-- UPDATE user SET authentication_string=PASSWORD('mail123$%^') WHERE User = 'GW_MAIL';
-- UPDATE user SET authentication_string=PASSWORD('erp123$%^') WHERE User = 'GW_ERP';
-- UPDATE user SET authentication_string=PASSWORD('asdqwe123$%^') WHERE User = 'MAIL_ABC123_USER';

