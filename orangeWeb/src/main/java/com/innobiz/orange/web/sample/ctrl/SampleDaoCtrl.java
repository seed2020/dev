package com.innobiz.orange.web.sample.ctrl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.innobiz.orange.web.cm.svc.CommonSvc;
import com.innobiz.orange.web.cm.utils.LayoutUtil;
import com.innobiz.orange.web.pt.vo.PtRescDVo;

@Controller
public class SampleDaoCtrl {
	
	@Resource(name = "commonSvc")
	private CommonSvc commonSvc;
	
	/**
	 * select, insert, update, delete, select-list, count
	 * 해당 테이블 Vo에 키정보 넣은 뒤 queryVo, queryList, queryInt 실행
	 * 
	 * */
	@RequestMapping(value = "/sample/dao/test1")
	public String test1(ModelMap model) throws Exception {
		
		PtRescDVo ptRescDVo;
		// 단건 조회
		ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId("s");
		ptRescDVo.setRescId("AAA");
		ptRescDVo = (PtRescDVo)commonSvc.queryVo(ptRescDVo);
		System.out.println(ptRescDVo);
		
		// 건수 조회
		ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId("s");
		int count = commonSvc.count(ptRescDVo);
		System.out.println(count);
		
		// 목록조회
		ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId("s");
		@SuppressWarnings("unchecked")
		List<PtRescDVo> ptRescDVoList = (List<PtRescDVo>)commonSvc.queryList(ptRescDVo);
		System.out.println("ptRescDVoList.size() : "+ptRescDVoList.size());
		
		return "sample/tables/index";
	}
	
	/**
	 * 일괄(여러개의 insert, update) 실행시
	 *   1. Ctrl 에서 QueryQueue 를 만듬
	 *   2. QueryQueue 에 insert, update로 Vo 를 담음
	 *   3. commonSvc.execute 실행함
	 * 
	 * */
	@RequestMapping(value = "/sample/dao/test2")
	public String test2(ModelMap model) throws Exception {
		
		/*
		QueryQueue queryQueue = new QueryQueue();
		
		PtRescDVo ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId("s");
		ptRescDVo.setRescId("AAA");
		ptRescDVo.setLangTypCd("ko");
		ptRescDVo.setRescVa("ㅏㅏㅏㅏㅏㅏ");
		queryQueue.insert(ptRescDVo);
		
		ptRescDVo = new PtRescDVo();
		ptRescDVo.setCompId("s");
		ptRescDVo.setRescId("AAB");
		ptRescDVo.setLangTypCd("ko");
		ptRescDVo.setRescVa("ㅏㅏㅏㅏㅏㅏ");
		queryQueue.insert(ptRescDVo);
		
		
		commonSvc.execute(queryQueue);
		*/
		return "sample/tables/index";
	}
	
	/**
	 * 시퀀스 사용법
	 *   1. commonSvc 에서 호출 할것.(ms-sql에서는 commonDao 의 함수를 사용하지 않음)
	 *   2. Long 을 조회할때 : nextVal
	 *   3. Id를 생성 할때 : createId
	 * 
	 * */
	@RequestMapping(value = "/sample/dao/test3")
	public String test3(ModelMap model) throws Exception {
		
		Long seq = commonSvc.nextVal("PT_SAMPLE_B");
		System.out.println(seq);
		
		String id = commonSvc.createId("PT_SAMPLE_B", 'T', 6);
		System.out.println(id);
		
		return "sample/tables/index";
	}
	
	/** 임시1 */
	@RequestMapping(value = "/sample/org/{path1}")
	public String boardLv1(HttpServletRequest request,
					@PathVariable("path1") String path1,
					ModelMap model) throws Exception {
		return LayoutUtil.getJspPath("/sample/org/" + path1);
	}
}
