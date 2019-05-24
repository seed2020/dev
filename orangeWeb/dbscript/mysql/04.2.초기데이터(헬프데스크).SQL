-- SET DEFINE OFF;

/** 시퀀스 */
INSERT INTO CM_SEQ_B VALUES ('WH_MD_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_RESC_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_CAT_GRP_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_CAT_GRP_L', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_RES_EVAL_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_REQ_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_REQ_FILE_D', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_REQ_CMPL_H', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_PICH_GRP_B', '1');
INSERT INTO CM_SEQ_B VALUES ('WH_REQ_ONGD_H', '1');



-- TABLE : PT_CD_B
INSERT INTO PT_CD_B VALUES ('PAGE_REC_SETUP_CD', 'wh', '헬프데스크', 'R00000TY', '', '', '', '', '11', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('PLT_CAT_CD', 'wh', '헬프데스크', 'R00000TZ', '', '', '', '', '11', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());



-- RESOURCE FOR : PT_CD_B
INSERT INTO PT_RESC_B VALUES('R00000TY', 'en', 'Helpdesk');
INSERT INTO PT_RESC_B VALUES('R00000TY', 'ko', '헬프데스크');
INSERT INTO PT_RESC_B VALUES('R00000TZ', 'en', 'Helpdesk');
INSERT INTO PT_RESC_B VALUES('R00000TZ', 'ko', '헬프데스크');



-- RESOURCE FOR : PT_CD_B - 중국어
-- INSERT INTO PT_RESC_B VALUES('R00000TY', 'zh', N'帮助台');
-- INSERT INTO PT_RESC_B VALUES('R00000TZ', 'zh', N'帮助台');


-- TABLE : PT_MNU_GRP_B
INSERT INTO PT_MNU_GRP_B VALUES ('G000000V', 'A', '헬프데스크관리', 'R00000U0', 'N', '03', '6', '', '', '', '0', 'WH', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_GRP_B VALUES ('G000000W', 'U', '헬프데스크', 'R00000U1', 'N', '03', '13', '', '', '', '0', 'WH', 'Y', 'U0000001', NOW(), 'U0000001', NOW());


-- RESOURCE FOR : PT_MNU_GRP_B
INSERT INTO PT_RESC_B VALUES('R00000U0', 'en', 'Helpdesk management');
INSERT INTO PT_RESC_B VALUES('R00000U0', 'ko', '헬프데스크관리');
INSERT INTO PT_RESC_B VALUES('R00000U1', 'en', 'Helpdesk');
INSERT INTO PT_RESC_B VALUES('R00000U1', 'ko', '헬프데스크');


-- RESOURCE FOR : PT_MNU_GRP_B - 중국어
-- INSERT INTO PT_RESC_B VALUES('R00000U0', 'zh', N'帮助台管理');
-- INSERT INTO PT_RESC_B VALUES('R00000U1', 'zh', N'帮助台');


-- TABLE : PT_MNU_D
INSERT INTO PT_MNU_D VALUES ('M0000071', 'A', 'G000000V', '시스템 모듈 관리', 'R00000U2', 'G000000V', '1', '/wh/adm/md/setModule.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000072', 'A', 'G000000V', '요청관리', 'R00000U3', 'G000000V', '2', '/wh/adm/help/listReq.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000073', 'A', 'G000000V', '환경설정', 'R00000U4', 'G000000V', '3', '/wh/adm/env/setEnv.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());

INSERT INTO PT_MNU_D VALUES ('M0000074', 'U', 'G000000W', '유지보수 요청', 'R00000U5', 'G000000W', '1', '/wh/help/listReq.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000075', 'U', 'G000000W', '유지보수 접수', 'R00000U6', 'G000000W', '2', '/wh/help/listRecv.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000076', 'U', 'G000000W', '유지보수 처리', 'R00000U7', 'G000000W', '3', '/wh/help/listHdl.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000077', 'U', 'G000000W', '처리 현황', 'R00000U8', 'G000000W', '4', '/wh/help/listDashBrd.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());


-- RESOURCE FOR : PT_MNU_D
INSERT INTO PT_RESC_B VALUES('R00000U2', 'en', 'System Module Management');
INSERT INTO PT_RESC_B VALUES('R00000U2', 'ko', '시스템 모듈 관리');
INSERT INTO PT_RESC_B VALUES('R00000U3', 'en', 'Request Management');
INSERT INTO PT_RESC_B VALUES('R00000U3', 'ko', '요청관리');
INSERT INTO PT_RESC_B VALUES('R00000U4', 'en', 'Environment setting');
INSERT INTO PT_RESC_B VALUES('R00000U4', 'ko', '환경설정');
INSERT INTO PT_RESC_B VALUES('R00000U5', 'en', 'HelpDesk Request');
INSERT INTO PT_RESC_B VALUES('R00000U5', 'ko', '유지보수 요청');
INSERT INTO PT_RESC_B VALUES('R00000U6', 'en', 'HelpDesk Receive');
INSERT INTO PT_RESC_B VALUES('R00000U6', 'ko', '유지보수 접수');
INSERT INTO PT_RESC_B VALUES('R00000U7', 'en', 'HelpDesk Handle');
INSERT INTO PT_RESC_B VALUES('R00000U7', 'ko', '유지보수 처리');
INSERT INTO PT_RESC_B VALUES('R00000U8', 'en', 'Handle State');
INSERT INTO PT_RESC_B VALUES('R00000U8', 'ko', '처리 현황');


-- RESOURCE FOR : PT_MNU_D -  중국어
-- INSERT INTO PT_RESC_B VALUES('R00000U2', 'zh', N'系统模块管理');
-- INSERT INTO PT_RESC_B VALUES('R00000U3', 'zh', N'请求管理');
-- INSERT INTO PT_RESC_B VALUES('R00000U4', 'zh', N'环境设置');
-- INSERT INTO PT_RESC_B VALUES('R00000U5', 'zh', N'维护请求');
-- INSERT INTO PT_RESC_B VALUES('R00000U6', 'zh', N'服务台接收');
-- INSERT INTO PT_RESC_B VALUES('R00000U7', 'zh', N'维持治疗');
-- INSERT INTO PT_RESC_B VALUES('R00000U8', 'zh', N'处理状态');


-- TABLE : PT_PLT_D
INSERT INTO PT_PLT_D VALUES ('P000000G', 'wh', '0', '헬프데스크', 'R00000U9', '/wh/plt/listHelpPlt.do', '/wh/help/listHelp.do', '/wh/plt/setHelpPltSetupPop.do', '400', '245', '', 'Y', 'Y', 'Y', 'Y', 'U0000001', NOW(), 'U0000001', NOW());

-- RESOURCE FOR : PT_PLT_D
INSERT INTO PT_RESC_B VALUES('R00000U9', 'en', 'HelpDesk');
INSERT INTO PT_RESC_B VALUES('R00000U9', 'ko', '헬프데스크');


-- RESOURCE FOR : PT_PLT_D - 중국어
-- INSERT INTO PT_RESC_B VALUES('R00000U9', 'zh', N'帮助台');


-- TABLE : PT_MNU_LOUT_D
INSERT INTO PT_MNU_LOUT_D VALUES ('A01', 'L000001A', 'A', 'adm', 'adm', 'G', 'G000000V', 'R00000U0', '', '헬프데스크관리', '', '11');


-- TABLE : PT_AUTH_GRP_MNU_PLT_R
-- INSERT INTO PT_AUTH_GRP_MNU_PLT_R VALUES ('A01', 'IP_IN', 'A0000001', 'G000000W', 'U', 'G000000W', 'MG', 'W');


-- SET DEFINE ON;