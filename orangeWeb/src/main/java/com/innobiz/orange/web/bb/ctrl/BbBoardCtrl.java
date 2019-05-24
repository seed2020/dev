package com.innobiz.orange.web.bb.ctrl;


/** 게시판 */
//@Controller
public class BbBoardCtrl {

//	/** 임시1 */
//	@RequestMapping(value = "/bb/{path1}")
//	public String boardLv1(HttpServletRequest request,
//					@PathVariable("path1") String path1,
//					ModelMap model) throws Exception {
//		
//		// set, list 로 시작하는 경우 처리
//		checkPath(request, path1, model);
//		
//		return LayoutUtil.getJspPath("/bb/" + path1);
//	}
//
//	private void checkPath(HttpServletRequest request, String path1,
//			ModelMap model) throws SQLException {
//		// 페이지 정보 세팅
//		CommonVo commonVo = new CommonVoImpl();
//		PersonalUtil.setPaging(request, commonVo, 25);
//		model.put("recodeCount", 25);
//		
//		// setXXX 이면
//		// 에디터 사용
//		model.addAttribute("JS_OPTS", new String[]{"editor"});
//	}
//	
//	/** 임시2 */
//	@RequestMapping(value = "/bb/{path1}/{path2}")
//	public String boardLv2(HttpServletRequest request,
//					@PathVariable("path1") String path1,
//					@PathVariable("path2") String path2,
//					ModelMap model) throws Exception {
//		
//		// set, list 로 시작하는 경우 처리
//		checkPath(request, path2, model);
//		
//		return LayoutUtil.getJspPath("/bb/" + path1 + "/" + path2);
//	}
}
