package com.innobiz.orange.web.wb.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.vo.PtCdBVo;
import com.innobiz.orange.web.wb.vo.WmRescBVo;

@Service
public class WbBcRescSvc {
	
	/** 포탈 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 명함 공통 서비스 */
	@Autowired
	private WbCmSvc wbCmSvc;
	
	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	
	/** 리소스기본(BA_RESC_B) 테이블에 저장할 단건의 리소스 데이터를 QueryQueue 에 저장 */
	public WmRescBVo collectBaRescBVo(HttpServletRequest request, String prefix, QueryQueue queryQueue) throws SQLException {

		// 세션의 언어코드
		String langTypCd = LoginSession.getLangTypCd(request);
		UserVo userVo = LoginSession.getUser(request);
		
		// 리소스ID 가 없음
		boolean emptyId = false;
		// 리소스 prefix 설정
		String rescPrefix = prefix == null || prefix.isEmpty() ? "resc" : prefix + "Resc";
		// rescId 받음 : 없으면 생성
		String rescId = request.getParameter(rescPrefix + "Id"), rescVa;
		if (rescId == null || rescId.isEmpty()) {
			rescId = wbCmSvc.createId("WM_RESC_B");
			emptyId = true;
		}
		
		// 첫번째 리소스 값
		WmRescBVo wmRescBVo, firstBaRescBVo = null;
		
		// rescVa 받아서 not empty 면 QueryQueue 에 담음
		PtCdBVo ptCdBVo;
		List<PtCdBVo> ptCdBVoList = ptCmSvc.getLangTypCdListByCompId(userVo.getCompId(), langTypCd);
		int i, size = ptCdBVoList == null ? 0 : ptCdBVoList.size();
		for (i = 0; i < size; i++) {
			ptCdBVo = ptCdBVoList.get(i);
			rescVa = request.getParameter(rescPrefix + "Va_" + ptCdBVo.getCd());
			if (rescVa != null && !rescVa.isEmpty()) {

				wmRescBVo = new WmRescBVo();
				wmRescBVo.setCompId(userVo.getCompId());
				wmRescBVo.setRescId(rescId);
				wmRescBVo.setLangTypCd(ptCdBVo.getCd());
				wmRescBVo.setRescVa(rescVa);

				if (firstBaRescBVo == null || "ko".equals(ptCdBVo.getCd())) {
					firstBaRescBVo = wmRescBVo;
				}

				if (emptyId) {
					queryQueue.insert(wmRescBVo);
				} else {
					queryQueue.store(wmRescBVo);
				}
			}
		}
		
		return firstBaRescBVo;
	}
	
	/** 리소스기본(WM_RESC_B) 테이블 조회 : 모델에 rescId+"_"+langTypCd 로 세팅함 */
	public void queryRescBVo(String rescId, ModelMap model) throws SQLException {
		
		// 회사명 언어별 리소스 조회
		if (rescId != null && !rescId.isEmpty()) {
			WmRescBVo wmRescBVo = new WmRescBVo();
			wmRescBVo.setRescId(rescId);
			@SuppressWarnings("unchecked")
			List<WmRescBVo> wmRescBVoList = (List<WmRescBVo>) commonDao.queryList(wmRescBVo);

			// JSP 출력을 위해 출력용 파라미터로 넘김
			int i, size = wmRescBVoList == null ? 0 : wmRescBVoList.size();
			for (i = 0; i < size; i++) {
				wmRescBVo = wmRescBVoList.get(i);
				model.put(wmRescBVo.getRescId() + "_" + wmRescBVo.getLangTypCd(), wmRescBVo.getRescVa());
			}
		}
	}
	
}
