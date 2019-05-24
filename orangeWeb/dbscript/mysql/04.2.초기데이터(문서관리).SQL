-- SET DEFINE OFF;

-- TABLE : CM_SEQ_B
INSERT INTO CM_SEQ_B VALUES ('DM_BUMK_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_CAT_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_CD_D', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_CD_GRP_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_CLS_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_COMM_FLD_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_DOC_L', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_TRAN_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_FILE_D', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_FLD_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_ITEM_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_ITEM_DISP_D', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_PSN_DOC_L', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_RESC_B', '1296');
INSERT INTO CM_SEQ_B VALUES ('DM_STOR_B', '1296');


-- TABLE : PT_CD_B
INSERT INTO PT_CD_B VALUES ('PAGE_REC_SETUP_CD', 'dm', '문서', 'R00000RJ', '', '', '', '', '10', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('PLT_CAT_CD', 'dm', '문서', 'R00000RK', '', '', '', '', '10', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('GW_DM', 'DM_AUTH_CD', '권한코드', 'R00000RM', '', '', '', '', '2', 'Y', 'Y', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('GW_DM', 'DM_AUTH_GRP_CD', '권한그룹코드', 'R00000RL', '', '', '', '', '1', 'Y', 'Y', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('GW_DM', 'TAK_STAT_CD', '인수인계상태코드', 'R00000RN', '', '', 'DM_TAKOVR_B', '', '3', 'Y', 'Y', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('GW_DM', 'VIEW_STAT_CD', '열람요청상태코드', 'R00000RP', '', '', '', '', '5', 'Y', 'Y', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('GW_DM', 'VIEW_TGT_CD', '열람요청대상코드', 'R00000RO', '', '', '', '', '4', 'Y', 'Y', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('DM_AUTH_CD', 'REGR', '작성자', 'R00000RQ', '', '', '', '', '1', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('DM_AUTH_CD', 'OWNR', '소유자', 'R00000RR', '', '', '', '', '2', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('DM_AUTH_GRP_CD', 'SECUL_CD', '보안등급', 'R00000RS', '', '', '', '', '1', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('DM_AUTH_GRP_CD', 'DM_AUTH_CD', '소유자', 'R00000RT', '', '', '', '', '2', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('TAK_STAT_CD', 'O', '대기', 'R00000RU', '', '', '', '', '1', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('TAK_STAT_CD', 'A', '승인', 'R00000RV', '', '', '', '', '2', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('TAK_STAT_CD', 'R', '반려', 'R00000RW', '', '', '', '', '3', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('VIEW_TGT_CD', 'U', '사용자', 'R00000RX', '', '', '', '', '1', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('VIEW_TGT_CD', 'D', '부서', 'R00000RY', '', '', '', '', '2', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('VIEW_STAT_CD', 'S', '대기', 'R00000RZ', '', '', '', '', '1', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('VIEW_STAT_CD', 'A', '승인', 'R00000S0', '', '', '', '', '2', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_CD_B VALUES ('VIEW_STAT_CD', 'R', '반려', 'R00000S1', '', '', '', '', '3', 'Y', 'N', 'N', 'U0000001', NOW(), 'U0000001', NOW());


-- RESOURCE FOR : PT_CD_B
INSERT INTO PT_RESC_B VALUES('R00000RJ', 'en', 'Document');
INSERT INTO PT_RESC_B VALUES('R00000RJ', 'ko', '문서');
INSERT INTO PT_RESC_B VALUES('R00000RK', 'en', 'Document');
INSERT INTO PT_RESC_B VALUES('R00000RK', 'ko', '문서');
INSERT INTO PT_RESC_B VALUES('R00000RL', 'en', 'Authority Group Code');
INSERT INTO PT_RESC_B VALUES('R00000RL', 'ko', '권한그룹코드');
INSERT INTO PT_RESC_B VALUES('R00000RM', 'en', 'Authority Code');
INSERT INTO PT_RESC_B VALUES('R00000RM', 'ko', '권한코드');
INSERT INTO PT_RESC_B VALUES('R00000RN', 'en', 'Take over State');
INSERT INTO PT_RESC_B VALUES('R00000RN', 'ko', '인수인계상태코드');
INSERT INTO PT_RESC_B VALUES('R00000RO', 'en', 'View Request Target Code');
INSERT INTO PT_RESC_B VALUES('R00000RO', 'ko', '열람요청대상코드');
INSERT INTO PT_RESC_B VALUES('R00000RP', 'en', 'View Request State Code');
INSERT INTO PT_RESC_B VALUES('R00000RP', 'ko', '열람요청상태코드');
INSERT INTO PT_RESC_B VALUES('R00000RQ', 'ko', '작성자');
INSERT INTO PT_RESC_B VALUES('R00000RQ', 'en', 'REGISTER');
INSERT INTO PT_RESC_B VALUES('R00000RR', 'ko', '소유자');
INSERT INTO PT_RESC_B VALUES('R00000RR', 'en', 'OWNER');
INSERT INTO PT_RESC_B VALUES('R00000RS', 'ko', '보안등급');
INSERT INTO PT_RESC_B VALUES('R00000RS', 'en', 'Security Grade');
INSERT INTO PT_RESC_B VALUES('R00000RT', 'ko', '소유자');
INSERT INTO PT_RESC_B VALUES('R00000RT', 'en', 'Owner');
INSERT INTO PT_RESC_B VALUES('R00000RU', 'ko', '대기');
INSERT INTO PT_RESC_B VALUES('R00000RU', 'en', 'Waiting');
INSERT INTO PT_RESC_B VALUES('R00000RV', 'ko', '승인');
INSERT INTO PT_RESC_B VALUES('R00000RV', 'en', 'Approved');
INSERT INTO PT_RESC_B VALUES('R00000RW', 'ko', '반려');
INSERT INTO PT_RESC_B VALUES('R00000RW', 'en', 'Reject');
INSERT INTO PT_RESC_B VALUES('R00000RX', 'ko', '사용자');
INSERT INTO PT_RESC_B VALUES('R00000RX', 'en', 'User');
INSERT INTO PT_RESC_B VALUES('R00000RY', 'ko', '부서');
INSERT INTO PT_RESC_B VALUES('R00000RY', 'en', 'Department');
INSERT INTO PT_RESC_B VALUES('R00000RZ', 'ko', '대기');
INSERT INTO PT_RESC_B VALUES('R00000RZ', 'en', 'Waiting');
INSERT INTO PT_RESC_B VALUES('R00000S0', 'ko', '승인');
INSERT INTO PT_RESC_B VALUES('R00000S0', 'en', 'Approved');
INSERT INTO PT_RESC_B VALUES('R00000S1', 'ko', '반려');
INSERT INTO PT_RESC_B VALUES('R00000S1', 'en', 'Reject');



-- RESOURCE FOR : PT_CD_B - 중국어
INSERT INTO PT_RESC_B VALUES('R00000RJ', 'zh', N'文件');
INSERT INTO PT_RESC_B VALUES('R00000RK', 'zh', N'文件');
INSERT INTO PT_RESC_B VALUES('R00000RL', 'zh', N'权限小组代码');
INSERT INTO PT_RESC_B VALUES('R00000RM', 'zh', N'权限代码');
INSERT INTO PT_RESC_B VALUES('R00000RN', 'zh', N'交接状态代码');
INSERT INTO PT_RESC_B VALUES('R00000RO', 'zh', N'阅览邀请对象代码');
INSERT INTO PT_RESC_B VALUES('R00000RP', 'zh', N'阅览邀请状态代码');
INSERT INTO PT_RESC_B VALUES('R00000RQ', 'zh', N'制定者');
INSERT INTO PT_RESC_B VALUES('R00000RR', 'zh', N'所有者');
INSERT INTO PT_RESC_B VALUES('R00000RS', 'zh', N'保安等级');
INSERT INTO PT_RESC_B VALUES('R00000RT', 'zh', N'所有者');
INSERT INTO PT_RESC_B VALUES('R00000RU', 'zh', N'等候');
INSERT INTO PT_RESC_B VALUES('R00000RV', 'zh', N'批准');
INSERT INTO PT_RESC_B VALUES('R00000RW', 'zh', N'返还');
INSERT INTO PT_RESC_B VALUES('R00000RX', 'zh', N'使用者');
INSERT INTO PT_RESC_B VALUES('R00000RY', 'zh', N'部门');
INSERT INTO PT_RESC_B VALUES('R00000RZ', 'zh', N'等候');
INSERT INTO PT_RESC_B VALUES('R00000S0', 'zh', N'批准');
INSERT INTO PT_RESC_B VALUES('R00000S1', 'zh', N'返还');


-- TABLE : PT_MNU_GRP_B
INSERT INTO PT_MNU_GRP_B VALUES ('G000000Q', 'A', '문서관리', 'R00000S2', 'N', '03', '6', '', '', '', '0', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_GRP_B VALUES ('G000000R', 'U', '문서관리', 'R00000S3', 'N', '03', '13', '', '', '', '0', 'DM', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_GRP_B VALUES ('G000000S', 'M', '문서관리', 'R00000S4', 'N', '03', '11', '', '', '', '0', 'DM', 'Y', 'U0000001', NOW(), 'U0000001', NOW());

-- RESOURCE FOR : PT_MNU_GRP_B
INSERT INTO PT_RESC_B VALUES('R00000S2', 'en', 'Document management');
INSERT INTO PT_RESC_B VALUES('R00000S2', 'ko', '문서관리');
INSERT INTO PT_RESC_B VALUES('R00000S3', 'en', 'Document management');
INSERT INTO PT_RESC_B VALUES('R00000S3', 'ko', '문서관리');
INSERT INTO PT_RESC_B VALUES('R00000S4', 'en', 'Document ');
INSERT INTO PT_RESC_B VALUES('R00000S4', 'ko', '문서관리');


-- RESOURCE FOR : PT_MNU_GRP_B - 중국어
INSERT INTO PT_RESC_B VALUES('R00000S2', 'zh', N'文件管理');
INSERT INTO PT_RESC_B VALUES('R00000S3', 'zh', N'文件管理');
INSERT INTO PT_RESC_B VALUES('R00000S4', 'zh', N'文件管理');



-- TABLE : PT_MNU_D
INSERT INTO PT_MNU_D VALUES ('M000005L', 'A', 'G000000Q', '문서유형관리', 'R00000S5', 'G000000Q', '1', '', '', '', '', 'Y', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005M', 'A', 'G000000Q', '폴더관리', 'R00000S6', 'G000000Q', '2', '/dm/adm/fld/setDocFld.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005N', 'A', 'G000000Q', '분류체계관리', 'R00000S7', 'G000000Q', '3', '/dm/adm/cls/setCls.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005O', 'A', 'G000000Q', '문서권한관리', 'R00000S8', 'G000000Q', '4', '/dm/adm/auth/setAuth.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005P', 'A', 'G000000Q', '문서관리', 'R00000S9', 'G000000Q', '5', '', '', '', '', 'Y', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005Q', 'A', 'G000000Q', '문서열람승인', 'R00000SA', 'G000000Q', '6', '/dm/adm/doc/listOpenApvDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005R', 'A', 'G000000Q', '인수인계', 'R00000SB', 'G000000Q', '7', '', '', '', '', 'Y', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005S', 'A', 'G000000Q', '저장소관리', 'R00000SC', 'G000000Q', '8', '/dm/adm/stor/listStor.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005T', 'A', 'G000000Q', '환경설정', 'R00000SD', 'G000000Q', '9', '/dm/adm/env/setEnv.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005U', 'A', 'G000000Q', '문서유형', 'R00000SE', 'M000005L', '1', '/dm/adm/cat/listCat.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005V', 'A', 'G000000Q', '코드관리', 'R00000SF', 'M000005L', '2', '/dm/adm/cat/listCd.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005W', 'A', 'G000000Q', '문서조회', 'R00000SG', 'M000005P', '1', '/dm/adm/doc/listDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005X', 'A', 'G000000Q', '보존연한문서', 'R00000SH', 'M000005P', '2', '/dm/adm/doc/listKprdDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005Y', 'A', 'G000000Q', '이관문서조회', 'R00000SI', 'M000005P', '3', '/dm/adm/doc/listTransferDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000005Z', 'A', 'G000000Q', '휴지통', 'R00000SJ', 'M000005P', '4', '/dm/adm/doc/listRecycleDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000060', 'A', 'G000000Q', '개인문서조회', 'R00000SK', 'M000005P', '5', '/dm/adm/doc/listPsnDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000061', 'A', 'G000000Q', '인계문서조회', 'R00000SL', 'M000005R', '1', '/dm/adm/doc/listTransTgtDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000062', 'A', 'G000000Q', '인계대기문서', 'R00000SM', 'M000005R', '2', '/dm/adm/doc/listTransWaitDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000063', 'A', 'G000000Q', '인수문서', 'R00000SN', 'M000005R', '3', '/dm/adm/doc/listTakovrDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000064', 'U', 'G000000R', '문서조회', 'R00000SO', 'G000000R', '1', '/dm/doc/listDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000065', 'U', 'G000000R', '문서열람요청', 'R00000SP', 'G000000R', '2', '/dm/doc/listOpenReqDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000066', 'U', 'G000000R', '최신문서', 'R00000SQ', 'G000000R', '3', '/dm/doc/listNewDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000067', 'U', 'G000000R', '소유문서', 'R00000SR', 'G000000R', '4', '/dm/doc/listOwnDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000068', 'U', 'G000000R', '즐겨찾기', 'R00000SS', 'G000000R', '5', '/dm/doc/listBumkDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M0000069', 'U', 'G000000R', '보존연한문서', 'R00000ST', 'G000000R', '6', '/dm/doc/listKprdDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006A', 'U', 'G000000R', '임시저장함', 'R00000SU', 'G000000R', '7', '/dm/doc/listTmpDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006B', 'U', 'G000000R', '상신문서', 'R00000SV', 'G000000R', '8', '/dm/doc/listSubmDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006C', 'U', 'G000000R', '문서심의', 'R00000SW', 'G000000R', '9', '/dm/doc/listDiscDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006D', 'U', 'G000000R', '휴지통', 'R00000SX', 'G000000R', '10', '/dm/doc/listRecycleDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006E', 'U', 'G000000R', '개인문서', 'R00000SY', 'G000000R', '11', '', '', '', '', 'Y', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006F', 'U', 'G000000R', '이관문서조회', 'R00000SZ', 'G000000R', '12', '/dm/doc/listTransferDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006G', 'U', 'G000000R', '인수인계', 'R00000T0', 'G000000R', '13', '', '', '', '', 'Y', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006H', 'U', 'G000000R', '폴더관리', 'R00000T1', 'G000000R', '14', '/dm/fld/setDocFld.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006I', 'U', 'G000000R', '개인문서조회', 'R00000T2', 'M000006E', '1', '/dm/doc/listPsnDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006J', 'U', 'G000000R', '개인폴더관리', 'R00000T3', 'M000006E', '2', '/dm/fld/setFld.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006K', 'U', 'G000000R', '인계문서조회', 'R00000T4', 'M000006G', '1', '/dm/doc/listTransTgtDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006L', 'U', 'G000000R', '인계대기문서', 'R00000T5', 'M000006G', '2', '/dm/doc/listTransWaitDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006M', 'U', 'G000000R', '인수문서', 'R00000T6', 'M000006G', '3', '/dm/doc/listTakovrDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006N', 'M', 'G000000S', '문서조회', 'R00000T7', 'G000000S', '1', '/dm/doc/listDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006O', 'M', 'G000000S', '문서열람요청', 'R00000T8', 'G000000S', '2', '/dm/doc/listOpenReqDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006P', 'M', 'G000000S', '개인문서', 'R00000T9', 'G000000S', '3', '/dm/doc/listPsnDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006Q', 'M', 'G000000S', '최신문서', 'R00000TA', 'G000000S', '4', '/dm/doc/listNewDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006R', 'M', 'G000000S', '소유문서', 'R00000TB', 'G000000S', '5', '/dm/doc/listOwnDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006S', 'M', 'G000000S', '보존연한문서', 'R00000TC', 'G000000S', '6', '/dm/doc/listKprdDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006T', 'M', 'G000000S', '즐겨찾기', 'R00000TD', 'G000000S', '7', '/dm/doc/listBumkDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006U', 'M', 'G000000S', '상신문서', 'R00000TE', 'G000000S', '8', '/dm/doc/listSubmDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006V', 'M', 'G000000S', '문서심의', 'R00000TF', 'G000000S', '9', '/dm/doc/listDiscDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006W', 'M', 'G000000S', '이관문서조회', 'R00000TG', 'G000000S', '10', '/dm/doc/listTransferDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006X', 'M', 'G000000S', '임시저장함', 'R00000TH', 'G000000S', '11', '/dm/doc/listTmpDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());
INSERT INTO PT_MNU_D VALUES ('M000006Y', 'M', 'G000000S', '휴지통', 'R00000TI', 'G000000S', '12', '/dm/doc/listRecycleDoc.do', 'IN_URL', '', '', 'N', 'N', '', '', 'Y', 'U0000001', NOW(), 'U0000001', NOW());


-- RESOURCE FOR : PT_MNU_D
INSERT INTO PT_RESC_B VALUES('R00000S5', 'en', 'Type of document ');
INSERT INTO PT_RESC_B VALUES('R00000S5', 'ko', '문서유형관리');
INSERT INTO PT_RESC_B VALUES('R00000S6', 'en', 'Folder management ');
INSERT INTO PT_RESC_B VALUES('R00000S6', 'ko', '폴더관리');
INSERT INTO PT_RESC_B VALUES('R00000S7', 'en', 'Classification');
INSERT INTO PT_RESC_B VALUES('R00000S7', 'ko', '분류체계관리');
INSERT INTO PT_RESC_B VALUES('R00000S8', 'en', 'Document authority');
INSERT INTO PT_RESC_B VALUES('R00000S8', 'ko', '문서권한관리');
INSERT INTO PT_RESC_B VALUES('R00000S9', 'en', 'Document management');
INSERT INTO PT_RESC_B VALUES('R00000S9', 'ko', '문서관리');
INSERT INTO PT_RESC_B VALUES('R00000SA', 'en', 'Document Open Approval');
INSERT INTO PT_RESC_B VALUES('R00000SA', 'ko', '문서열람승인');
INSERT INTO PT_RESC_B VALUES('R00000SB', 'en', 'Taking over');
INSERT INTO PT_RESC_B VALUES('R00000SB', 'ko', '인수인계');
INSERT INTO PT_RESC_B VALUES('R00000SC', 'en', 'Storage Management');
INSERT INTO PT_RESC_B VALUES('R00000SC', 'ko', '저장소관리');
INSERT INTO PT_RESC_B VALUES('R00000SD', 'en', 'Environment setting ');
INSERT INTO PT_RESC_B VALUES('R00000SD', 'ko', '환경설정');
INSERT INTO PT_RESC_B VALUES('R00000SE', 'en', 'Type of document ');
INSERT INTO PT_RESC_B VALUES('R00000SE', 'ko', '문서유형');
INSERT INTO PT_RESC_B VALUES('R00000SF', 'en', 'Code management');
INSERT INTO PT_RESC_B VALUES('R00000SF', 'ko', '코드관리');
INSERT INTO PT_RESC_B VALUES('R00000SG', 'en', 'Document View');
INSERT INTO PT_RESC_B VALUES('R00000SG', 'ko', '문서조회');
INSERT INTO PT_RESC_B VALUES('R00000SH', 'en', 'Over bojonnyeonhan');
INSERT INTO PT_RESC_B VALUES('R00000SH', 'ko', '보존연한문서');
INSERT INTO PT_RESC_B VALUES('R00000SI', 'en', 'Search of transfer Document ');
INSERT INTO PT_RESC_B VALUES('R00000SI', 'ko', '이관문서조회');
INSERT INTO PT_RESC_B VALUES('R00000SJ', 'en', 'Wastebasket');
INSERT INTO PT_RESC_B VALUES('R00000SJ', 'ko', '휴지통');
INSERT INTO PT_RESC_B VALUES('R00000SK', 'en', 'Personal Document Search');
INSERT INTO PT_RESC_B VALUES('R00000SK', 'ko', '개인문서조회');
INSERT INTO PT_RESC_B VALUES('R00000SL', 'en', 'Transfer Document Search');
INSERT INTO PT_RESC_B VALUES('R00000SL', 'ko', '인계문서조회');
INSERT INTO PT_RESC_B VALUES('R00000SM', 'en', 'Transfer Wait Search');
INSERT INTO PT_RESC_B VALUES('R00000SM', 'ko', '인계대기문서');
INSERT INTO PT_RESC_B VALUES('R00000SN', 'en', 'Take over Document');
INSERT INTO PT_RESC_B VALUES('R00000SN', 'ko', '인수문서');
INSERT INTO PT_RESC_B VALUES('R00000SO', 'en', 'Document View');
INSERT INTO PT_RESC_B VALUES('R00000SO', 'ko', '문서조회');
INSERT INTO PT_RESC_B VALUES('R00000SP', 'en', 'Document View Request');
INSERT INTO PT_RESC_B VALUES('R00000SP', 'ko', '문서열람요청');
INSERT INTO PT_RESC_B VALUES('R00000SQ', 'en', 'New Document');
INSERT INTO PT_RESC_B VALUES('R00000SQ', 'ko', '최신문서');
INSERT INTO PT_RESC_B VALUES('R00000SR', 'en', 'Own Document');
INSERT INTO PT_RESC_B VALUES('R00000SR', 'ko', '소유문서');
INSERT INTO PT_RESC_B VALUES('R00000SS', 'en', 'Bookmark');
INSERT INTO PT_RESC_B VALUES('R00000SS', 'ko', '즐겨찾기');
INSERT INTO PT_RESC_B VALUES('R00000ST', 'en', 'Over bojonnyeonhan');
INSERT INTO PT_RESC_B VALUES('R00000ST', 'ko', '보존연한문서');
INSERT INTO PT_RESC_B VALUES('R00000SU', 'en', 'Temporary Box');
INSERT INTO PT_RESC_B VALUES('R00000SU', 'ko', '임시저장함');
INSERT INTO PT_RESC_B VALUES('R00000SV', 'en', 'Approval Document');
INSERT INTO PT_RESC_B VALUES('R00000SV', 'ko', '상신문서');
INSERT INTO PT_RESC_B VALUES('R00000SW', 'en', 'Approval');
INSERT INTO PT_RESC_B VALUES('R00000SW', 'ko', '문서심의');
INSERT INTO PT_RESC_B VALUES('R00000SX', 'en', 'Wastebasket');
INSERT INTO PT_RESC_B VALUES('R00000SX', 'ko', '휴지통');
INSERT INTO PT_RESC_B VALUES('R00000SY', 'en', 'Private document');
INSERT INTO PT_RESC_B VALUES('R00000SY', 'ko', '개인문서');
INSERT INTO PT_RESC_B VALUES('R00000SZ', 'en', 'Taking Over Search');
INSERT INTO PT_RESC_B VALUES('R00000SZ', 'ko', '이관문서조회');
INSERT INTO PT_RESC_B VALUES('R00000T0', 'en', 'Taking over');
INSERT INTO PT_RESC_B VALUES('R00000T0', 'ko', '인수인계');
INSERT INTO PT_RESC_B VALUES('R00000T1', 'en', 'Folder Management');
INSERT INTO PT_RESC_B VALUES('R00000T1', 'ko', '폴더관리');
INSERT INTO PT_RESC_B VALUES('R00000T2', 'en', 'Personal Document View');
INSERT INTO PT_RESC_B VALUES('R00000T2', 'ko', '개인문서조회');
INSERT INTO PT_RESC_B VALUES('R00000T3', 'en', 'Personal folder ');
INSERT INTO PT_RESC_B VALUES('R00000T3', 'ko', '개인폴더관리');
INSERT INTO PT_RESC_B VALUES('R00000T4', 'en', 'Transfer Document Search');
INSERT INTO PT_RESC_B VALUES('R00000T4', 'ko', '인계문서조회');
INSERT INTO PT_RESC_B VALUES('R00000T5', 'en', 'Transfer Wait Search');
INSERT INTO PT_RESC_B VALUES('R00000T5', 'ko', '인계대기문서');
INSERT INTO PT_RESC_B VALUES('R00000T6', 'en', 'Take over Document');
INSERT INTO PT_RESC_B VALUES('R00000T6', 'ko', '인수문서');
INSERT INTO PT_RESC_B VALUES('R00000T7', 'en', 'View Document');
INSERT INTO PT_RESC_B VALUES('R00000T7', 'ko', '문서조회');
INSERT INTO PT_RESC_B VALUES('R00000T8', 'en', 'Document View Request');
INSERT INTO PT_RESC_B VALUES('R00000T8', 'ko', '문서열람요청');
INSERT INTO PT_RESC_B VALUES('R00000T9', 'en', 'Personal Document');
INSERT INTO PT_RESC_B VALUES('R00000T9', 'ko', '개인문서');
INSERT INTO PT_RESC_B VALUES('R00000TA', 'en', 'New Document');
INSERT INTO PT_RESC_B VALUES('R00000TA', 'ko', '최신문서');
INSERT INTO PT_RESC_B VALUES('R00000TB', 'en', 'Own Document');
INSERT INTO PT_RESC_B VALUES('R00000TB', 'ko', '소유문서');
INSERT INTO PT_RESC_B VALUES('R00000TC', 'en', 'Keep Period Document');
INSERT INTO PT_RESC_B VALUES('R00000TC', 'ko', '보존연한문서');
INSERT INTO PT_RESC_B VALUES('R00000TD', 'en', 'BookMark');
INSERT INTO PT_RESC_B VALUES('R00000TD', 'ko', '즐겨찾기');
INSERT INTO PT_RESC_B VALUES('R00000TE', 'en', 'Approving Document');
INSERT INTO PT_RESC_B VALUES('R00000TE', 'ko', '상신문서');
INSERT INTO PT_RESC_B VALUES('R00000TF', 'en', 'Document Approving');
INSERT INTO PT_RESC_B VALUES('R00000TF', 'ko', '문서심의');
INSERT INTO PT_RESC_B VALUES('R00000TG', 'en', 'Taking Over Search');
INSERT INTO PT_RESC_B VALUES('R00000TG', 'ko', '이관문서조회');
INSERT INTO PT_RESC_B VALUES('R00000TH', 'en', 'Draft Postings');
INSERT INTO PT_RESC_B VALUES('R00000TH', 'ko', '임시저장함');
INSERT INTO PT_RESC_B VALUES('R00000TI', 'en', 'Wastebasket');
INSERT INTO PT_RESC_B VALUES('R00000TI', 'ko', '휴지통');



-- RESOURCE FOR : PT_MNU_D -  중국어
INSERT INTO PT_RESC_B VALUES('R00000S5', 'zh', N'文件类型管理');
INSERT INTO PT_RESC_B VALUES('R00000S6', 'zh', N'文件夹管理');
INSERT INTO PT_RESC_B VALUES('R00000S7', 'zh', N'分类体系管理');
INSERT INTO PT_RESC_B VALUES('R00000S8', 'zh', N'文件权限管理');
INSERT INTO PT_RESC_B VALUES('R00000S9', 'zh', N'文件管理');
INSERT INTO PT_RESC_B VALUES('R00000SA', 'zh', N'文件阅览许可');
INSERT INTO PT_RESC_B VALUES('R00000SB', 'zh', N'交接');
INSERT INTO PT_RESC_B VALUES('R00000SC', 'zh', N'仓库管理');
INSERT INTO PT_RESC_B VALUES('R00000SD', 'zh', N'设置');
INSERT INTO PT_RESC_B VALUES('R00000SE', 'zh', N'文件类型');
INSERT INTO PT_RESC_B VALUES('R00000SF', 'zh', N'代码管理');
INSERT INTO PT_RESC_B VALUES('R00000SG', 'zh', N'文件查询');
INSERT INTO PT_RESC_B VALUES('R00000SH', 'zh', N'保存年限文件');
INSERT INTO PT_RESC_B VALUES('R00000SI', 'zh', N'搜查转移文件');
INSERT INTO PT_RESC_B VALUES('R00000SJ', 'zh', N'垃圾箱');
INSERT INTO PT_RESC_B VALUES('R00000SK', 'zh', N'个人文件查询');
INSERT INTO PT_RESC_B VALUES('R00000SL', 'zh', N'交接文件查询');
INSERT INTO PT_RESC_B VALUES('R00000SM', 'zh', N'交接等候文件');
INSERT INTO PT_RESC_B VALUES('R00000SN', 'zh', N'接受文件');
INSERT INTO PT_RESC_B VALUES('R00000SO', 'zh', N'文件查询');
INSERT INTO PT_RESC_B VALUES('R00000SP', 'zh', N'文件阅览邀请');
INSERT INTO PT_RESC_B VALUES('R00000SQ', 'zh', N'最新文件');
INSERT INTO PT_RESC_B VALUES('R00000SR', 'zh', N'占有文件');
INSERT INTO PT_RESC_B VALUES('R00000SS', 'zh', N'收藏夹');
INSERT INTO PT_RESC_B VALUES('R00000ST', 'zh', N'保存年限文件');
INSERT INTO PT_RESC_B VALUES('R00000SU', 'zh', N'临时保存箱');
INSERT INTO PT_RESC_B VALUES('R00000SV', 'zh', N'上报文件');
INSERT INTO PT_RESC_B VALUES('R00000SW', 'zh', N'文件审核');
INSERT INTO PT_RESC_B VALUES('R00000SX', 'zh', N'垃圾箱');
INSERT INTO PT_RESC_B VALUES('R00000SY', 'zh', N'个人文件');
INSERT INTO PT_RESC_B VALUES('R00000SZ', 'zh', N'搜查转移文件');
INSERT INTO PT_RESC_B VALUES('R00000T0', 'zh', N'交接');
INSERT INTO PT_RESC_B VALUES('R00000T1', 'zh', N'文件夹管理');
INSERT INTO PT_RESC_B VALUES('R00000T2', 'zh', N'个人文件查询');
INSERT INTO PT_RESC_B VALUES('R00000T3', 'zh', N'个人文件夹管理');
INSERT INTO PT_RESC_B VALUES('R00000T4', 'zh', N'交接文件查询');
INSERT INTO PT_RESC_B VALUES('R00000T5', 'zh', N'交接等候文件');
INSERT INTO PT_RESC_B VALUES('R00000T6', 'zh', N'接受文件');
INSERT INTO PT_RESC_B VALUES('R00000T7', 'zh', N'文件查询');
INSERT INTO PT_RESC_B VALUES('R00000T8', 'zh', N'文件阅览邀请');
INSERT INTO PT_RESC_B VALUES('R00000T9', 'zh', N'个人文件');
INSERT INTO PT_RESC_B VALUES('R00000TA', 'zh', N'最新文件');
INSERT INTO PT_RESC_B VALUES('R00000TB', 'zh', N'占有文件');
INSERT INTO PT_RESC_B VALUES('R00000TC', 'zh', N'保存年限文件');
INSERT INTO PT_RESC_B VALUES('R00000TD', 'zh', N'收藏夹');
INSERT INTO PT_RESC_B VALUES('R00000TE', 'zh', N'上报文件');
INSERT INTO PT_RESC_B VALUES('R00000TF', 'zh', N'文件审核');
INSERT INTO PT_RESC_B VALUES('R00000TG', 'zh', N'搜查转移文件');
INSERT INTO PT_RESC_B VALUES('R00000TH', 'zh', N'临时保存箱');
INSERT INTO PT_RESC_B VALUES('R00000TI', 'zh', N'垃圾箱');


-- TABLE : PT_PLT_D
INSERT INTO PT_PLT_D VALUES ('P000000E', 'dm', '0', '문서조회', 'R00000TJ', '/dm/plt/listDocPlt.do', '/dm/doc/listDoc.do', '/dm/plt/setDocPltSetupPop.do', '400', '245', '', 'Y', 'Y', 'Y', 'Y', 'U0000001', NOW(), 'U0000001', NOW());

-- RESOURCE FOR : PT_PLT_D
INSERT INTO PT_RESC_B VALUES('R00000TJ', 'en', 'Document Views');
INSERT INTO PT_RESC_B VALUES('R00000TJ', 'ko', '문서조회');


-- RESOURCE FOR : PT_PLT_D - 중국어
INSERT INTO PT_RESC_B VALUES('R00000TJ', 'zh', N'文件查询');


-- TABLE : PT_MNU_LOUT_D
INSERT INTO PT_MNU_LOUT_D VALUES ('A01', 'L0000019', 'A', 'adm', 'adm', 'G', 'G000000Q', 'R00000S2', '', '문서관리', '', '7');

-- TABLE : PT_AUTH_GRP_MNU_PLT_R
INSERT INTO PT_AUTH_GRP_MNU_PLT_R VALUES ('A01', 'IP_IN', 'A0000001', 'G000000Q', 'U', 'G000000Q', 'MG', 'W');


-- SET DEFINE ON;