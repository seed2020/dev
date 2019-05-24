package com.innobiz.orange.web.ap.utils;


/** 문서유틸 - JSP 와 JAVA 의 동일한 로직 사용을 위해 만듬 */
public class ApDocUtil {
	
	/** 결재중 결재자 상태 인지 확인 - inApv:결재중, inAgr:합의중, hold:보류, cncl:취소, reRevw:재검토, inInfm:통보중, inVw:공람중 */
	public static boolean isInApvOfApvStat(String apvStatCd){
		if("inApv".equals(apvStatCd) || "inAgr".equals(apvStatCd)
				|| "hold".equals(apvStatCd) || "cncl".equals(apvStatCd) || "reRevw".equals(apvStatCd)
				|| "inInfm".equals(apvStatCd) || "inVw".equals(apvStatCd)) return true;
		return false;
	}
	
	/** 결재완료 결재자 상태 인지 확인 - apvd:승인, rejt:반려, cons:반대, pros:찬성 */
	public static boolean isCmplOfApvStat(String apvStatCd){
		if("apvd".equals(apvStatCd) || "rejt".equals(apvStatCd) || "cons".equals(apvStatCd) || "pros".equals(apvStatCd)) return true;
		return false;
	}
	
	/** 보류 상태인지 */
	public static boolean isHoldStat(String apvStatCd){
		return "hold".equals(apvStatCd);
	}
	
	/** 기안 인지 확인 - byOne:1인결재, mak:기안 */
	public static boolean isMakOfApvrRole(String apvrRoleCd){
		if("byOne".equals(apvrRoleCd) || "mak".equals(apvrRoleCd)) return true;
		return false;
	}
	
	/** 개인통보 인지 확인 - postApvd:후열, psnInfm:개인통보, entu:위임 */
	public static boolean isPsnInfmOfApvrRole(String apvrRoleCd){
		if("postApvd".equals(apvrRoleCd) || "psnInfm".equals(apvrRoleCd) || "entu".equals(apvrRoleCd)) return true;
		return false;
	}
	
	/** 통보 인지 확인 - postApvd:후열, psnInfm:개인통보, entu:위임, deptInfm:부서통보 */
	public static boolean isInfmOfApvrRole(String apvrRoleCd){
		if("postApvd".equals(apvrRoleCd) || "psnInfm".equals(apvrRoleCd)
				|| "entu".equals(apvrRoleCd) || "deptInfm".equals(apvrRoleCd)) return true;
		return false;
	}
	
	/** 기안중 문서 인지 */
	public static boolean isAtMak(String apvrRoleCd, String apvStatCd){
		return isMakOfApvrRole(apvrRoleCd) && isInApvOfApvStat(apvStatCd);
	}
	
	/** 처리할 문서인지 - 검토/합의/결재 중 문서인지 */
	public static boolean isAtWait(String apvrRoleCd, String apvStatCd){
		return !isPsnInfmOfApvrRole(apvrRoleCd) && isInApvOfApvStat(apvStatCd);
	}
	
	/** 병렬합의인지 - psnParalAgr:개인병렬합의, deptParalAgr:부서병렬합의 */
	public static boolean isParalAgrOfApvrRole(String apvrRoleCd){
		if("psnParalAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)) return true;
		return false;
	}
	
	/** 순차인지 - psnOrdrdAgr:개인순차합의, deptOrdrdAgr:부서순차합의, */
	public static boolean isOrdrdAgrOfApvrRole(String apvrRoleCd){
		if("psnOrdrdAgr".equals(apvrRoleCd) || "deptOrdrdAgr".equals(apvrRoleCd)) return true;
		return false;
	}
	
	/** 합의인지 - psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 */
	public static boolean isAgrOfApvrRole(String apvrRoleCd){
		if("psnOrdrdAgr".equals(apvrRoleCd) || "psnParalAgr".equals(apvrRoleCd)
				|| "deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)) return true;
		return false;
	}

	/** 공람인지 - makVw:담당, fstVw:선람, pubVw:공람, paralPubVw:동시공람 */
	public static boolean isVwOfApvrRole(String apvrRoleCd){
		if("makVw".equals(apvrRoleCd) || "fstVw".equals(apvrRoleCd)
				|| "pubVw".equals(apvrRoleCd) || "paralPubVw".equals(apvrRoleCd)) return true;
		return false;
	}

	/** 완료된 문서승인상태 인지 - apvd:승인, rejt:반려, pubVw:공람 */
	public static boolean isCmplOfDocProsStat(String docProsStatCd){
		if("apvd".equals(docProsStatCd) || "rejt".equals(docProsStatCd) || "pubVw".equals(docProsStatCd)) return true;
		return false;
	}
	
	/** 진행중인 문서승인상태 인지 - mak:기안, ongo:결재중*/
	public static boolean isOngoOfDocProsStat(String docProsStatCd){
		if("mak".equals(docProsStatCd) || "ongo".equals(docProsStatCd)) return true;
		return false;
	}
	
	/** 부서의 역할 인지(부서대기함 목록) - deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서 */
	public static boolean isDeptRole(String apvrRoleCd){
		return "deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd)
				|| "prcDept".equals(apvrRoleCd);
	}
	
	/** 부서합의 인지 - deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의 */
	public static boolean isDeptAgrRole(String apvrRoleCd){
		return "deptOrdrdAgr".equals(apvrRoleCd) || "deptParalAgr".equals(apvrRoleCd);
	}
	
	/** 최상위 결재 라인인지 체크 */
	public static boolean isRootLine(String apvLnPno){
		return "0".equals(apvLnPno);
	}
	
	/** 의견을 같는지 여부 - revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, makAgr:합의기안, apv:결재, pred:전결 */
	public static boolean hasOpin(String apvrRoleCd){
		return "revw".equals(apvrRoleCd)
				|| "revw2".equals(apvrRoleCd)
				|| "revw3".equals(apvrRoleCd)
				|| "psnOrdrdAgr".equals(apvrRoleCd)
				|| "psnParalAgr".equals(apvrRoleCd)
				|| "deptOrdrdAgr".equals(apvrRoleCd)
				|| "deptParalAgr".equals(apvrRoleCd)
				|| "makAgr".equals(apvrRoleCd)
				|| "apv".equals(apvrRoleCd)
				|| "pred".equals(apvrRoleCd);
	}

	/** 다음 결재자자 역할인지 체크 - revw:검토, psnOrdrdAgr:개인순차합의, psnParalAgr:개인병렬합의, deptOrdrdAgr:부서순차합의, deptParalAgr:부서병렬합의, prcDept:처리부서, makAgr:합의기안, apv:결재, pred:전결  */
	public static boolean isNextApvr(String apvrRoleCd){
		return "revw".equals(apvrRoleCd)
				|| "revw2".equals(apvrRoleCd)
				|| "revw3".equals(apvrRoleCd)
				|| "psnOrdrdAgr".equals(apvrRoleCd)
				|| "psnParalAgr".equals(apvrRoleCd)
				|| "deptOrdrdAgr".equals(apvrRoleCd)
				|| "deptParalAgr".equals(apvrRoleCd)
				|| "prcDept".equals(apvrRoleCd)
				|| "makAgr".equals(apvrRoleCd)
				|| "apv".equals(apvrRoleCd)
				|| "pred".equals(apvrRoleCd);
	}
	
//	/** 언어 설정 함 */
//	public static void setLang(HttpServletRequest request, String langTypCd){
//		Locale locale = SessionUtil.toLocale(langTypCd);
//		request.getSession(true).setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
//	}
}
