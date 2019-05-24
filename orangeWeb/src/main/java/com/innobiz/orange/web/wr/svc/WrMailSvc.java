package com.innobiz.orange.web.wr.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.svc.BbBrdSvc;
import com.innobiz.orange.web.bb.svc.BbBullFileSvc;
import com.innobiz.orange.web.bb.svc.BbBullSvc;
import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.utils.MessageProperties;
import com.innobiz.orange.web.cm.vo.CommonFileVo;
import com.innobiz.orange.web.wr.vo.WrRescMngBVo;
import com.innobiz.orange.web.wr.vo.WrRezvBVo;

@Service
public class WrMailSvc {
	
//	/** 시스템설정 서비스 */
//	@Autowired
//	private PtSysSvc ptSysSvc;	
	
	/** 메세지 */
	@Autowired
    private MessageProperties messageProperties;
	
	/** 게시판관리 서비스 */
	@Resource(name = "bbBrdSvc")
	private BbBrdSvc bbBrdSvc;

	/** 게시물 서비스 */
	@Resource(name = "bbBullSvc")
	private BbBullSvc bbBullSvc;
	
	/** 게시파일 서비스 */
	@Resource(name = "bbBullFileSvc")
	private BbBullFileSvc bbBullFileSvc;
	
	/** 메일 내용에 추가할 파라미터맵 */
	public Map<String, String> getParamMap(WrRescMngBVo wrRescMngBVo){
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("md", "WR"); // 자원예약
		paramMap.put("kndNm", wrRescMngBVo.getKndNm()); // 자원종류
		paramMap.put("rescNm", wrRescMngBVo.getRescNm()); // 자원명
		//paramMap.put("rescLoc", wrRescMngBVo.getRescLoc()); // 자원위치
		
		return paramMap;
		
	}
	
	/** 자원 메일 내용 */
	public String getRescMailHTML(HttpServletRequest request, Locale recvLocale, WrRescMngBVo wrRescMngBVo, WrRezvBVo wrRezvBVo, String userNm, String msgUrl, boolean isDisc) throws SQLException, IOException, CmException{
		
		StringBuilder builder = new StringBuilder(1024);
		
		builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">\n");
		
		String discStatNm=null;
		// 심의여부
		if(isDisc){
			discStatNm = messageProperties.getMessage("wr.jsp.discStatCd"+wrRezvBVo.getDiscStatCd()+".title", recvLocale);
		}else{ // 요청
			discStatNm = messageProperties.getMessage("wr.msg.resq.title", recvLocale);
		}
		
		builder.append(messageProperties.getMessage("wr.msg.emailTitle.tx01", new String[]{discStatNm}, recvLocale));
		builder.append("</p>\n<br/>\n");
		
		builder.append("<table style=\"border:0px\" border=\"0\">\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("cols.rescKnd", recvLocale)).append("</strong></td>\n"); //자원종류
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		builder.append(wrRescMngBVo.getKndNm());		
		builder.append("</td></tr>\n");
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("cols.rescNm", recvLocale)).append("</strong></td>\n"); // 자원명
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
		builder.append(wrRescMngBVo.getRescNm());		
		builder.append("</td></tr>\n");
	
		
		builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
			.append(messageProperties.getMessage("cols.rezvDt", recvLocale)).append("</strong></td>\n"); // 예약일시
		
		String startDt = wrRezvBVo.getRezvStrtDt();
		String endDt = wrRezvBVo.getRezvEndDt();
		String priod = startDt+"~"+endDt;
		
		builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
		builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">").append(priod).append("</td></tr>\n");
		
		if(isDisc){
			builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
				.append(messageProperties.getMessage("wr.cols.disc.result", recvLocale)).append("</strong></td>\n"); // 심의결과
			builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
			builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			builder.append(discStatNm);		
			builder.append("</td></tr>\n");
			
			builder.append("<tr style=\"padding-top:6px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\"><td><strong>")
				.append(messageProperties.getMessage("cols.discOpin", recvLocale)).append("</strong></td>\n"); // 심의의견
			builder.append("<td style=\"text-align:center; width=14px; font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">:</td>\n");
			builder.append("<td style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">");
			builder.append(wrRezvBVo.getDiscCont());
			builder.append("</td></tr>\n");
		}
		
		builder.append("</table>\n");
		
		if(msgUrl!=null && !msgUrl.isEmpty()){
			builder.append("<br/>\n");
			builder.append("<p style=\"font-family:Gulim,Arial,geneva,sans-serif; font-size:12px;\">\n");
			builder.append("<a href=\""+msgUrl+"\" target=\"_blank\"><strong>"+messageProperties.getMessage("cols.shcut", recvLocale)+"</strong></a>"); // 바로가기
			if(!isDisc && userNm!=null) builder.append("<span> (").append(messageProperties.getMessage("cm.msg.user.onlyCanUse", new String[]{userNm}, recvLocale)).append(")</span>");
			builder.append("</p>\n");
		}
		
		return builder.toString();
	}
	
	/** [한화제약] - 자원예약 승인완료시 특정자원에 한해 메일에 내용 추가  */
	public String getHanwhaMailHTML(String langTypCd, String brdId, String bullId) throws SQLException{
		// 게시판관리(BA_BRD_B) - SELECT
		BaBrdBVo baBrdBVo = bbBrdSvc.getBaBrdBVo(langTypCd, brdId);
		if (baBrdBVo == null) return null;
		
		// 게시물(BB_X000X_L) 테이블 - SELECT
		BbBullLVo bbBullLVo = bbBullSvc.getBbBullLVo(baBrdBVo, Integer.parseInt(bullId), langTypCd, true);
		if (bbBullLVo == null || bbBullLVo.getCont()==null) return null;
		
		return bbBullLVo.getCont();
	}
	
	/** [한화제약] - 자원예약 승인완료시 특정자원에 한해 메일에 파일 추가  */
	public List<CommonFileVo> getHanwhaMailFiles(String bullId) throws SQLException{
		return bbBullFileSvc.getFileVoList(bullId);
	}
	
}
