package com.innobiz.orange.web.bb.svc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innobiz.orange.web.bb.vo.BaCmtDVo;
import com.innobiz.orange.web.bb.vo.BbBullLVo;
import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.crypto.License;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrOrgHstRVo;
import com.innobiz.orange.web.or.vo.OrUserPwDVo;
import com.innobiz.orange.web.pt.secu.LoginSession;
import com.innobiz.orange.web.pt.secu.SecuUtil;
import com.innobiz.orange.web.pt.secu.UserVo;
import com.innobiz.orange.web.pt.svc.PtSysSvc;

/** 게시판 공통 서비스 */
@Service
public class BbCmSvc {

	/** 공통 DAO */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 공통 DB 처리용 SVC */
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/** RSA 암호화 - 로그인 구간 암호화 처리 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 시스템설정 서비스 */
	@Autowired
	private PtSysSvc ptSysSvc;
	
	/** ID 통합 생성 */
	public String createId(String tableName) throws SQLException {
		if ("BA_RESC_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'R', 8);
		} else if ("BA_TBL_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'T', 8);
		} else if ("BA_TBL_COLM_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'C', 8);
		} else if ("BA_BRD_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'B', 8);
		} else if ("BA_CAT_GRP_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'G',  8);
		} else if ("BA_CAT_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'A',  8);
		} else if ("BA_CD_GRP_B".equals(tableName)) {
			return commonSvc.createId(tableName, 'P',  8);
		} else if ("BA_CD_D".equals(tableName)) {
			return commonSvc.createId(tableName, 'D',  8);
		}
		
		return null;
	}

	/** 게시물 ID 생성 */
	public Integer createBullId() throws SQLException {
		return commonSvc.nextVal("BB_BULL_L").intValue();
	}

	/** 한줄답변 ID 생성 */
	public Integer createCmtId() throws SQLException {
		return commonSvc.nextVal("BA_CMT_D").intValue();
	}

	/** 게시물첨부파일 ID 생성 */
	public Integer createFileId() throws SQLException {
		return commonSvc.nextVal("BA_BULL_FILE_D").intValue();
	}
	
	/** 사용자 권한 체크 */
	public void checkUserAuth(HttpServletRequest request, String auth, String regrUid) throws CmException {
		if (!SecuUtil.hasAuth(request, auth, regrUid)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}

	/** 사용자 부서 권한 체크 */
	public void checkUserDeptAuth(HttpServletRequest request, String deptId) throws CmException, SQLException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		List<String> deptIdList=getOrgHstIdList("ko", userVo);
		if (userVo.getDeptId() == null || !ArrayUtil.isInArray(ArrayUtil.toArray(deptIdList), deptId)) {
			// cm.msg.errors.403=해당 기능에 대한 권한이 없습니다.
			throw new CmException("cm.msg.errors.403", request);
		}
	}

	/** 비밀번호 확인 */
	public boolean isValidUserPw(HttpServletRequest request, String secu) throws SQLException, IOException, CmException {
		// 세션의 사용자 정보
		UserVo userVo = LoginSession.getUser(request);
		JSONObject jsonObject = null;
		try {
			jsonObject = cryptoSvc.processRsa(request,secu);
		} catch(CmException e){
			//LOGGER.error("Login-Fail : "+e.getMessage());
			//pt.login.fail.decrypt=복호화에 실패하였습니다.
			return false;
		}
		String pw = (String)jsonObject.get("pw");
		// 원직자기본(OR_ODUR_B) 테이블 - 로그인 아이디로 사용자 정보 조회
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setLginId(userVo.getLginId());
		orOdurBVo = (OrOdurBVo) commonSvc.queryVo(orOdurBVo);
		if (orOdurBVo == null) {
			return false;
		}
		// 원직자UID
		String odurUid = orOdurBVo.getOdurUid();
		// 로그인 패스워드 암호화
		String encPw = License.ins.encryptPw(pw, odurUid);

		// 사용자비밀번호상세(OR_USER_PW_D) 테이블 - 조회
		OrUserPwDVo orUserPwDVo = new OrUserPwDVo();
		orUserPwDVo.setOdurUid(odurUid);
		orUserPwDVo.setPwTypCd("SYS");  // SYS:시스템 비밀번호, APV:결재 비밀번호
		orUserPwDVo = (OrUserPwDVo) commonSvc.queryVo(orUserPwDVo);
		if (orUserPwDVo == null) {
			return false;
		}
		// 패스워드 비교
		if (encPw == null || !encPw.equals(orUserPwDVo.getPwEnc())) {
			return false;
		}

		// 사용자상태코드 체크 - 01:사용신청, 02:근무중, 03:휴직, 04:정직, 05:퇴직, 99:삭제
		return "02".equals(orOdurBVo.getUserStatCd());
	}
	
	/** 게시물 목록을 파라미터로 받아 - 한줄답변 수를 조회하여 세팅함 */
	public void setCmtCount(List<? extends BbBullLVo> bbBullLVoList) throws SQLException{
		if(bbBullLVoList==null || bbBullLVoList.isEmpty()) return;
		
		List<Integer> bullIdList = new ArrayList<Integer>();
		Map<Integer, BbBullLVo> bbBullLVoMap = new HashMap<Integer, BbBullLVo>();
		Map<Integer, BbBullLVo> bbBullLVoDupMap = new HashMap<Integer, BbBullLVo>();
		Integer zero = 0;
		for(BbBullLVo bbBullLVo : bbBullLVoList){
			if(bbBullLVo.getBullId()!=null){
				bullIdList.add(bbBullLVo.getBullId());
				if(bbBullLVoMap.get(bbBullLVo.getBullId()) != null){
					bbBullLVoDupMap.put(bbBullLVo.getBullId(), bbBullLVo);
				} else {
					bbBullLVoMap.put(bbBullLVo.getBullId(), bbBullLVo);
				}
				bbBullLVo.setCmtCnt(zero);
			}
		}
		if(bullIdList.isEmpty()) return;
		
		BaCmtDVo baCmtDVo = new BaCmtDVo();
		baCmtDVo.setBullIdList(bullIdList);
		baCmtDVo.setInstanceQueryId("com.innobiz.orange.web.bb.dao.BaCmtDDao.countBaCmtDByBullId");
		@SuppressWarnings("unchecked")
		List<BaCmtDVo> baCmtDVoList = (List<BaCmtDVo>)commonDao.queryList(baCmtDVo);
		if(baCmtDVoList != null){
			BbBullLVo bbBullLVo;
			for(BaCmtDVo cmtVo : baCmtDVoList){
				bbBullLVo = bbBullLVoMap.get(cmtVo.getBullId());
				if(bbBullLVo != null) bbBullLVo.setCmtCnt(cmtVo.getCmtCnt());
				bbBullLVo = bbBullLVoDupMap.get(cmtVo.getBullId());
				if(bbBullLVo != null) bbBullLVo.setCmtCnt(cmtVo.getCmtCnt());
			}
		}
	}
	
	/** 조직 이력 ID 목록 조회 */
	public List<String> getOrgHstIdList(String langTypCd, UserVo userVo) throws SQLException{
		// 조직 이력 ID 목록 조회
		List<String> orgIdList=new ArrayList<String>();
		// 시스템 정책 조회
		Map<String, String> sysPlocMap = ptSysSvc.getSysPlocMap();
		String orgId=userVo.getDeptId();
		orgIdList.add(orgId);
		if(sysPlocMap.containsKey("deptBrdHstYn") && "Y".equals(sysPlocMap.get("deptBrdHstYn"))){			
			// 조직이력관계(OR_ORG_HST_R) 테이블
			OrOrgHstRVo orOrgHstRVo = new OrOrgHstRVo();
			orOrgHstRVo.setOrgId(orgId);
			orOrgHstRVo.setQueryLang(langTypCd);
			@SuppressWarnings("unchecked")
			List<OrOrgHstRVo> orOrgHstRVoList = (List<OrOrgHstRVo>)commonDao.queryList(orOrgHstRVo);
			if(orOrgHstRVoList != null && !orOrgHstRVoList.isEmpty()){
				for(OrOrgHstRVo storedOrOrgHstRVo : orOrgHstRVoList){
					orgIdList.add(storedOrOrgHstRVo.getHstOrgId());
				}
			}
			
		}
		
		return orgIdList;
	}
}
