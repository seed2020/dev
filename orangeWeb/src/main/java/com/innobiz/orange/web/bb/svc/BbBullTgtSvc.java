package com.innobiz.orange.web.bb.svc;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.bb.vo.BaBrdBVo;
import com.innobiz.orange.web.bb.vo.BaBullTgtDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.UserVo;

/** 게시대상 서비스 */
@Service
public class BbBullTgtSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;

	/** 게시대상 저장 */
	public void saveBullTgt(HttpServletRequest request, Integer bullId, QueryQueue queryQueue) {
		String[] orgIds = request.getParameterValues("orgId");
		String[] withSubYns = request.getParameterValues("withSubYn");

		// 게시대상(BA_BULL_TGT_D) 테이블 - DELETE
		deleteBullTgt(bullId, queryQueue);
		
		if (orgIds != null && orgIds.length > 0) {
			for (int i = 0; i < orgIds.length; i++) {
				// 게시대상(BA_BULL_TGT_D) 테이블 - INSERT
				BaBullTgtDVo bullTgtVo = new BaBullTgtDVo();
				bullTgtVo.setBullId(bullId);
				bullTgtVo.setTgtTyp("D");
				bullTgtVo.setTgtId(orgIds[i]);
				bullTgtVo.setWithSubYn(withSubYns[i]);
				queryQueue.insert(bullTgtVo);
			}
		}
		
		String[] userUids = request.getParameterValues("userUid");
		
		if (userUids != null && userUids.length > 0) {
			for (String userUid : userUids) {
				// 게시대상(BA_BULL_TGT_D) 테이블 - INSERT
				BaBullTgtDVo bullTgtVo = new BaBullTgtDVo();
				bullTgtVo.setBullId(bullId);
				bullTgtVo.setTgtTyp("U");
				bullTgtVo.setTgtId(userUid);
				queryQueue.insert(bullTgtVo);
			}
		}
	}

	/** 게시대상 model에 추가 */
	@SuppressWarnings("unchecked")
	public void putTgtListToModel(BbBullLVo bbBullLVo, ModelMap model) throws SQLException {
		if ("Y".equals(bbBullLVo.getTgtDeptYn())) {
			// 게시대상(BA_BULL_TGT_D) 테이블 - SELECT
			BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
			baBullTgtDVo.setBullId(bbBullLVo.getBullId());
			baBullTgtDVo.setTgtTyp("D");
			List<BaBullTgtDVo> baBullTgtDVoList = (List<BaBullTgtDVo>) commonDao.queryList(baBullTgtDVo);
			model.put("bullTgtDeptVoList", baBullTgtDVoList);
		}
		if ("Y".equals(bbBullLVo.getTgtUserYn())) {
			// 게시대상(BA_BULL_TGT_D) 테이블 - SELECT
			BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
			baBullTgtDVo.setBullId(bbBullLVo.getBullId());
			baBullTgtDVo.setTgtTyp("U");
			List<BaBullTgtDVo> baBullTgtDVoList = (List<BaBullTgtDVo>) commonDao.queryList(baBullTgtDVo);
			model.put("bullTgtUserVoList", baBullTgtDVoList);
		}
	}

	/** 게시대상에 포함되는지 여부 리턴 */
	@SuppressWarnings("unchecked")
	public void checkTgt(HttpServletRequest request, BbBullLVo bbBullLVo, ModelMap model) throws SQLException, CmException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);

		// 작성자는 패스
		if (userVo.getUserUid().equals(bbBullLVo.getRegrUid())) return;

		// 답변 원문의 작성자는 패스 
		if (userVo.getUserUid().equals(bbBullLVo.getReplyGrpRegrUid())) return;

		boolean isTgt = true;
		if ("Y".equals(bbBullLVo.getTgtDeptYn())) {
			isTgt = false;
			List<BaBullTgtDVo> bullTgtDeptVoList = (List<BaBullTgtDVo>) model.get("bullTgtDeptVoList");
			for (BaBullTgtDVo baBullTgtDVo : bullTgtDeptVoList) {
				if ("Y".equals(baBullTgtDVo.getWithSubYn())) {
					String[] orgPids = userVo.getOrgPids();
					for (String orgPid : orgPids) {
						if (orgPid.equals(baBullTgtDVo.getTgtId())) {
							isTgt = true;break;
						}
					}
				} else {
					if (userVo.getOrgId().equals(baBullTgtDVo.getTgtId())) {
						isTgt = true;break;
					}
				}
			}
		}
		if (("Y".equals(bbBullLVo.getTgtUserYn()) && "N".equals(bbBullLVo.getTgtDeptYn()) && isTgt ) || ("Y".equals(bbBullLVo.getTgtDeptYn()) && "Y".equals(bbBullLVo.getTgtUserYn()) && !isTgt)) {
			isTgt = false;
			List<BaBullTgtDVo> bullTgtUserVoList = (List<BaBullTgtDVo>) model.get("bullTgtUserVoList");
			for (BaBullTgtDVo baBullTgtDVo : bullTgtUserVoList) {
				if (userVo.getUserUid().equals(baBullTgtDVo.getTgtId())) {
					isTgt = true;
				}
			}
		}
		if (!isTgt) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}

	/** 게시대상 삭제 */
	public void deleteBullTgt(Integer bullId, QueryQueue queryQueue) {
		// 게시대상(BA_BULL_TGT_D) 테이블 - DELETE
		BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
		baBullTgtDVo.setBullId(bullId);
		queryQueue.delete(baBullTgtDVo);
	}

	/** 게시물ID 수정 */
	public void updateBullId(Integer bullId, Integer newBullId, QueryQueue queryQueue) throws SQLException {
		BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
		baBullTgtDVo.setBullId(bullId);
		baBullTgtDVo.setNewBullId(newBullId);
		queryQueue.update(baBullTgtDVo);
	}
	
	/** 비공개 사용자 저장 */
	public void savePrivUser(QueryQueue queryQueue, String privYn, Integer bullId, String userUid) {
		// 게시대상(BA_BULL_TGT_D) 테이블 - DELETE
		deleteBullTgt(bullId, queryQueue);
		if(privYn==null || !"Y".equals(privYn)) return;
		// 게시대상(BA_BULL_TGT_D) 테이블 - INSERT
		BaBullTgtDVo bullTgtVo = new BaBullTgtDVo();
		bullTgtVo.setBullId(bullId);
		bullTgtVo.setTgtTyp("U");
		bullTgtVo.setTgtId(userUid);
		queryQueue.insert(bullTgtVo);
	}
	
	/** 비공개 여부 세팅 */
	public void setPrivYn(ModelMap model, BaBrdBVo baBrdBVo, BbBullLVo bbBullLVo, String bullId, String userUid) throws SQLException{
		if(bbBullLVo==null || bbBullLVo.getRegrUid()==null) return;
		// 비공개 여부
		String privUseYn=baBrdBVo.getOptMap()!=null ? (String)baBrdBVo.getOptMap().get("privUseYn") : null;
		if(privUseYn==null || privUseYn.isEmpty()) return;
				
		// 게시대상 - 부서가 있을경우 리턴
		String tgtDeptYn = bbBullLVo.getTgtDeptYn();
		if(tgtDeptYn!=null && "Y".equals(tgtDeptYn)) return;
		
		// 게시대상 - 사용자만 있을경우 진행
		String tgtUserYn = bbBullLVo.getTgtUserYn();
		if(tgtUserYn==null || tgtUserYn.isEmpty() || "N".equals(tgtUserYn)) return;
		
		// 게시대상(BA_BULL_TGT_D) 테이블 - SELECT
		BaBullTgtDVo baBullTgtDVo = new BaBullTgtDVo();
		baBullTgtDVo.setBullId(Integer.parseInt(bullId));
		baBullTgtDVo.setTgtTyp("U");
		@SuppressWarnings("unchecked")
		List<BaBullTgtDVo> baBullTgtDVoList = (List<BaBullTgtDVo>) commonDao.queryList(baBullTgtDVo);
		// 게시대상 사용자 비교 - 비공개 여부
		if(baBullTgtDVoList!=null && baBullTgtDVoList.size()==1 && bbBullLVo.getRegrUid().equals(baBullTgtDVoList.get(0).getTgtId()))
			model.put("privYn", "Y");
	}
}
