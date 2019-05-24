package com.innobiz.orange.web.cu.svc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.innobiz.orange.web.cm.crypto.CryptoSvc;
import com.innobiz.orange.web.cm.dao.CommonDao;
import com.innobiz.orange.web.cm.exception.CmException;
import com.innobiz.orange.web.cm.files.DistHandler;
import com.innobiz.orange.web.cm.files.DistManager;
import com.innobiz.orange.web.cm.utils.ArrayUtil;
import com.innobiz.orange.web.cm.utils.JsonUtil;
import com.innobiz.orange.web.cm.utils.SessionUtil;
import com.innobiz.orange.web.cm.utils.StringUtil;
import com.innobiz.orange.web.cm.utils.VoUtil;
import com.innobiz.orange.web.cm.vo.QueryQueue;
import com.innobiz.orange.web.cu.vo.CuPsnCardBVo;
import com.innobiz.orange.web.cu.vo.CuPsnImgDVo;
import com.innobiz.orange.web.cu.vo.CuPsnInfoDVo;
import com.innobiz.orange.web.em.svc.EmFileUploadSvc;
import com.innobiz.orange.web.em.vo.EmTmpFileTVo;
import com.innobiz.orange.web.or.svc.OrCmSvc;
import com.innobiz.orange.web.or.vo.OrOdurBVo;
import com.innobiz.orange.web.or.vo.OrUserBVo;
import com.innobiz.orange.web.or.vo.OrUserImgDVo;
import com.innobiz.orange.web.pt.svc.PtCmSvc;
import com.innobiz.orange.web.pt.utils.SysSetupUtil;
import com.innobiz.orange.web.pt.vo.PtCompBVo;

/** 게시파일 서비스 */
@Service
public class CuNaraInsaSvc {

	/** Logger */
	//private static final Logger LOGGER = Logger.getLogger(CuNaraInsaSvc.class);

	/** 공통 DB 처리용 서비스 */
	@Resource(name = "commonDao")
	private CommonDao commonDao;
	
	/** 파일업로드 서비스 */
	@Resource(name = "emFileUploadSvc")
	private EmFileUploadSvc emFileUploadSvc;
	
	/** 배포 매니저 */
	@Resource(name = "distManager")
	private DistManager distManager;
	
	/** 암호화 서비스 */
	@Autowired
	private CryptoSvc cryptoSvc;
	
	/** 포털 공통 서비스 */
	@Autowired
	private PtCmSvc ptCmSvc;
	
	/** 조직 공통 서비스 */
	@Autowired
	private OrCmSvc orCmSvc;
	
	/** 개인정보 속성명 */
	private String[] encAttrs=new String[]{"ssn", "birth", "homeZipNo", "homeAdr", "homeDetlAdr", "mbno"};
	
	/** 개인정보 저장 - 암호화 */
	public void savePsnInfo(HttpServletRequest request, QueryQueue queryQueue, String cardNo) throws SQLException, IOException, CmException{
		String key, value, encValue;
		CuPsnInfoDVo cuPsnInfoDVo = new CuPsnInfoDVo();
		
		// barConnectedTypes : 서버 사이드에서 데이터 조합시 "-" 를 붙여서 조합함 
		// connectedTypes : 서버 사이드에서 데이터 조합시를 그냥 연결함 
		String[] barConnectedTypes = request.getParameterValues("barConnectedTypes");
		String[] connectedTypes = request.getParameterValues("connectedTypes");
		
		for(String attNm : encAttrs){
			key=StringUtil.fromCamelNotation(attNm);
			key="exc"+StringUtil.toCamelNotation(key, true);
			if(barConnectedTypes!= null && ArrayUtil.isInArray(barConnectedTypes, key)){
				value = connectParam(request, key, "-");
			} else if(connectedTypes!= null && ArrayUtil.isInArray(connectedTypes, key)){
				value = connectParam(request, key, null);
			}else {
				value = request.getParameter(key);
			}
			if(value==null) continue;
			encValue = value.isEmpty() ? "" : cryptoSvc.encryptPersanal(value);			
			key=attNm+"Enc";
			//System.out.println("key2 : "+key);
			VoUtil.setValue(cuPsnInfoDVo, key, encValue);
		}
		cuPsnInfoDVo.setCardNo(Integer.parseInt(cardNo));
		queryQueue.store(cuPsnInfoDVo);
	}
	
	/** 개인정보 세팅 - 복호화 */
	public void setPsnInfo(CuPsnCardBVo cuPsnCardBVo, String cardNo) throws SQLException, IOException, CmException{
		CuPsnInfoDVo cuPsnInfoDVo = new CuPsnInfoDVo();
		cuPsnInfoDVo.setCardNo(Integer.parseInt(cardNo));
		cuPsnInfoDVo = (CuPsnInfoDVo)commonDao.queryVo(cuPsnInfoDVo);			
		
		String key, value, decValue;
		for(String attNm : encAttrs){
			key=attNm+"Enc";
			value=(String)VoUtil.getValue(cuPsnInfoDVo, key);
			if(value==null) continue;
			decValue = cryptoSvc.decryptPersanal(value);
			VoUtil.setValue(cuPsnCardBVo, attNm, decValue);
		}
	}
	/** 사용자정보 세팅 */
	public void setOrUser(ModelMap model, Map<String, Object> userMap) throws SQLException{
		if(userMap==null) return;
		
		boolean exPhoneEnable = "Y".equals(SysSetupUtil.getSysPlocMap().get("exPhoneEnable"));
		// 사번, 한글명, 부서명, 직급, 주소, 휴대폰번호, 입사일자
		String[][] atts=new String[][]{{"ein", "ein"}, {"koNm", "rescNm"}, {"deptNm", "deptRescNm"}, {"positNm", "positNm"}, {"homeZipNo", "homeZipNo"}, {"homeAdr", "homeAdr"}, {"mbno", "mbno"}, {"joinDt", "entraYmd"}};
		CuPsnCardBVo cuPsnCardBVo = new CuPsnCardBVo();
		for(String[] attNm : atts){
			if(!userMap.containsKey(attNm[1]) || userMap.get(attNm[1])==null) continue;
			if(!exPhoneEnable && attNm[0].equals("mbno")){
				Map<String,String> map = new HashMap<String,String>();
				String[] mbnos=StringUtil.splitPhone((String)userMap.get(attNm[1]));
				if(mbnos!=null && mbnos.length>0){
					for(int i=0;i<mbnos.length;i++){
						map.put("mbno"+(i+1), mbnos[i]);
					}
					cuPsnCardBVo.setMbnos(JsonUtil.toJson(map));
				}
			}
			VoUtil.setValue(cuPsnCardBVo, attNm[0], (String)userMap.get(attNm[1]));
		}
		
		// 회사, 기관명
		String compNm = orCmSvc.getOrgRescNmByOrgTypCd((String)userMap.get("orgId"), "G", "ko");
		if(compNm==null || compNm.isEmpty()){
			//회사정보 조회
			PtCompBVo ptCompBVo = ptCmSvc.getPtCompBVo((String)userMap.get("compId"), "ko");
			compNm=ptCompBVo.getRescNm();
		}
		cuPsnCardBVo.setCompNm(compNm);
		model.put("cuPsnCardBVo", cuPsnCardBVo);
	}
	
	/** 사용자 이미지정보 세팅 */
	public void setOrUserImg(ModelMap model, String userUid) throws SQLException{
		if(userUid==null) return;
		// 이미지 조회
		CuPsnImgDVo cuPsnImgDVo = null;
					
		// 사용자이미지상세(OR_USER_IMG_D) 테이블
		OrUserImgDVo orUserImgDVo = new OrUserImgDVo();
		// 겸직자 이미지 조회
		orUserImgDVo.setUserUid(userUid);
		@SuppressWarnings("unchecked")
		List<OrUserImgDVo> orUserImgDVoList = (List<OrUserImgDVo>)commonDao.queryList(orUserImgDVo);
		if(orUserImgDVoList!=null){
			// userImgTypCd : 사용자이미지구분코드 - KEY - 01:도장, 02:싸인, 03:사진
			for(OrUserImgDVo storedOrUserImgDVo : orUserImgDVoList){
				if(!(storedOrUserImgDVo.getUserImgTypCd()!=null && 
						"03".equals(storedOrUserImgDVo.getUserImgTypCd()))) continue;
				cuPsnImgDVo = new CuPsnImgDVo();
				cuPsnImgDVo.setImgPath(storedOrUserImgDVo.getImgPath());
				cuPsnImgDVo.setImgWdth(Integer.parseInt(storedOrUserImgDVo.getImgWdth()));
				cuPsnImgDVo.setImgHght(Integer.parseInt(storedOrUserImgDVo.getImgHght()));
			}
			if(cuPsnImgDVo!=null) model.put("cuPsnImgDVo", cuPsnImgDVo);
		}
	}
	
	////////////////
	/** 이미지 세팅 - 인사기록카드 */
	public void setPsnImgDVo(HttpServletRequest request, QueryQueue queryQueue, String cardNo, String tmpImgId) throws SQLException, IOException, CmException{
		EmTmpFileTVo emTmpFileTVo = emFileUploadSvc.getEmTmpFileVo(Integer.parseInt(tmpImgId));
		if(emTmpFileTVo==null) return;
		
		CuPsnImgDVo cuPsnImgDVo = new CuPsnImgDVo();
		cuPsnImgDVo.setCardNo(Integer.parseInt(cardNo));
		String filePath = emTmpFileTVo.getSavePath();
		String path = "images/upload/bb/img/photo";
		
		// 파일 새이름으로 복사 후 파일 배포
		String newSavePath = copyAndWebDisk(request, path, filePath);
		cuPsnImgDVo.setImgPath(newSavePath);
		BufferedImage bimg = ImageIO.read(new File(filePath));
		cuPsnImgDVo.setImgWdth(Integer.parseInt(Integer.toString(bimg.getWidth())));
		cuPsnImgDVo.setImgHght(Integer.parseInt(Integer.toString(bimg.getHeight())));
		queryQueue.store(cuPsnImgDVo);
	}
	
	/** 파일을 새이름으로 복사 후 파일 배포 (WEB DISK) */
	private String copyAndWebDisk(HttpServletRequest request, String path, String savePath) throws IOException, CmException {
		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정

		// 새이름
		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/"+ getFileName('F' , savePath);
		// 파일복사
		distHandler.copyFile(savePath, newSavePath);
				
		String distPath = distHandler.addWebList(newSavePath);
		
		distHandler.distribute();
		//return webCopyBaseDir + distPath;
		return distPath;
	}
	
	/** 파일명 새로 생성 */
	public String getFileName(char prefix , String orginalDir){
		String fileName;
		int p = orginalDir.replace('\\', '/').lastIndexOf('/');
		if (p >= 0) fileName = orginalDir.substring(p + 1);
		else fileName = orginalDir;
		p = fileName.lastIndexOf('.');
		String ext = p <= 0 ? "" : fileName.substring(p);
		String newfileName = prefix + StringUtil.getNextHexa()+ext;
		
		return newfileName;
	}
	
	/** 이미지 삭제 */
	public String getImgPath(QueryQueue queryQueue, String cardNo) throws SQLException{
		// 사진(BA_PSN_IMG_D) 테이블 - SELECT
		CuPsnImgDVo cuPsnImgDVo = new CuPsnImgDVo();
		cuPsnImgDVo.setCardNo(Integer.parseInt(cardNo));
		cuPsnImgDVo = (CuPsnImgDVo) commonDao.queryVo(cuPsnImgDVo);

		if (cuPsnImgDVo != null) {
			return cuPsnImgDVo.getImgPath();
		}
		return null;
	}
	
	/** 파일 삭제 (DISK) */
	public void deleteDiskFiles(List<String> deletedFileList) {
		if (deletedFileList == null || deletedFileList.size() == 0) return;
		// baseDir
		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
		if (wasCopyBaseDir == null) {
			distManager.init();
			wasCopyBaseDir = distManager.getWasCopyBaseDir();
		}
		File file=null;
		for (String path : deletedFileList) {
			file=new File(wasCopyBaseDir+path);
			if(!file.isFile()) continue;
			file.delete();		
		}
	}
	
	/** 파라미터 연결해서 리턴함 : 전화번호, 우편번호, 주민번호, 팩스번호... */
	private static String connectParam(HttpServletRequest request, String attribute, String connectString) {
		StringBuilder builder = new StringBuilder();
		String value;
		int i = 0;
		for(i=1;i<10;i++){
			value = request.getParameter(attribute+i);
			if(value==null) break;
			if(value.isEmpty()) return "";
			if(i>1 && connectString!=null) builder.append(connectString);
			builder.append(value);
		}
		return builder.toString();
	}
	
	/** 사번으로 원직자 USERUID 조회*/
	public String getUserUid(String compId, String ein) throws SQLException{
		if(ein==null) return null;
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setEin(ein);
		@SuppressWarnings("unchecked")
		List<OrOdurBVo> orOdurBVoList=(List<OrOdurBVo>)commonDao.queryList(orOdurBVo);
		//orOdurBVo = (OrOdurBVo)commonSvc.queryVo(orOdurBVo);
		
		if(orOdurBVoList==null || orOdurBVoList.size()==0 || orOdurBVoList.size()>1){
			return null;
		}
		orOdurBVo=orOdurBVoList.get(0);
		
		String odurUid = orOdurBVo.getOdurUid();
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setOdurUid(orOdurBVo.getOdurUid()); // 원직자ID
		orUserBVo.setCompId(compId); // 회사ID
		//orUserBVo.setQueryLang(langTypCd);
		
		@SuppressWarnings("unchecked")
		List<OrUserBVo> list = (List<OrUserBVo>) commonDao.queryList(orUserBVo);
		if(list==null || list.size()==0){
			return null;
		}
		
		String userUid=null;
		for(OrUserBVo storedOrUserBVo : list){
			if(storedOrUserBVo.getUserUid().equals(odurUid)){ // 원직자 UID
				userUid=storedOrUserBVo.getUserUid();
				break;
			}
		}
		
		return userUid;
	}
	
	/** 조직ID로 원직자 사번 목록 조회*/
	public List<String> getEinList(String orgId) throws SQLException{
		if(orgId==null) return null;
		
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setOrgId(orgId);
		// 사용자기본(OR_USER_B) 테이블
		@SuppressWarnings("unchecked")
		List<OrUserBVo> orUserBVoList = (List<OrUserBVo>)commonDao.queryList(orUserBVo);
		if(orUserBVoList==null || orUserBVoList.size()==0) return null;
		
		// 원직자UID 목록
		List<String> odurUidList = new ArrayList<String>();
		for(OrUserBVo storedOrUserBVo : orUserBVoList){
			if(storedOrUserBVo.getUserUid().equals(storedOrUserBVo.getOdurUid())) // 원직자
				odurUidList.add(storedOrUserBVo.getOdurUid());
		}
		if(odurUidList.size()==0) return null;
		
		// 원직자기본(OR_ODUR_B) 테이블
		OrOdurBVo orOdurBVo = new OrOdurBVo();
		orOdurBVo.setOdurUidList(odurUidList);
		@SuppressWarnings("unchecked")
		List<OrOdurBVo> orOdurBVoList = (List<OrOdurBVo>)commonDao.queryList(orOdurBVo);
		if(orOdurBVoList==null || orOdurBVoList.size()==0) return null;		
		
		// 사번 목록
		List<String> einList=new ArrayList<String>();
		for(OrOdurBVo storedOrOdurBVo : orOdurBVoList){
			if(storedOrOdurBVo.getEin()!=null && !storedOrOdurBVo.getEin().isEmpty()) 
				einList.add(storedOrOdurBVo.getEin());
		}
		
		return einList;
	}
	
	/** 사번 체크 */
	public boolean isEinAuthChk(String compId, String orgId, String ein) throws SQLException{
		String userUid=getUserUid(compId, ein); // 사번으로 USERUID 조회
		if(userUid==null) return false;
		
		// 사용자기본(OR_USER_B) 테이블
		OrUserBVo orUserBVo = new OrUserBVo();
		orUserBVo.setCompId(compId);
		orUserBVo.setUserUid(userUid);
		orUserBVo=(OrUserBVo)commonDao.queryVo(orUserBVo);
		if(orUserBVo==null) return false;
		
		// 조직ID 비교
		if(orgId.equals(orUserBVo.getOrgId())) return true;
		
		return false;
		
	}
	
//	/** 파일을 새이름으로 복사 후 파일 배포 (DISK) */
//	private String copyAndDist(HttpServletRequest request, String path, String savePath, String ext) throws IOException, CmException {
//		DistHandler distHandler = distManager.createHandler(path, SessionUtil.getLocale(request));  // 업로드 경로 설정
//
//		// 새이름
//		String newSavePath = savePath.replace('\\', '/').substring(0, savePath.lastIndexOf('/')) + "/F" + StringUtil.getNextHexa() + "." + ext;
//
//		/**
//		 * 게시판 복사 기능 처리시 첨부파일 대상 경로의 동기화가 필요하여 순서가 바뀜 
//		 *  새로운 첨부 경로를 받아온 후 파일복사 해야함.
//		 */
//		
//		// baseDir
//		String wasCopyBaseDir = distManager.getWasCopyBaseDir();
//		if (wasCopyBaseDir == null) {
//			distManager.init();
//			wasCopyBaseDir = distManager.getWasCopyBaseDir();
//		}
//		
//		System.out.println("savePath:"+savePath);
//		System.out.println("newSavePath:"+newSavePath);
//		
//		
//		String distPath = distHandler.addWasList(newSavePath);
//		if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("distPath = " + distPath);
//		}
//		
//		System.out.println("wasCopyBaseDir+distPath:"+wasCopyBaseDir+distPath);
//		
//		// 파일복사
//		distHandler.copyFile(savePath, wasCopyBaseDir+distPath);
//		
//		distHandler.distribute();
//		
//		return distPath;
//	}
}
