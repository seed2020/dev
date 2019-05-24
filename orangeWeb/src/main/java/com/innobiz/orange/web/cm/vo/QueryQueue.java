package com.innobiz.orange.web.cm.vo;

import java.util.ArrayList;

/** 쿼리의 일괄 SQL 실행을 위한  Vo 보관 객체 */
public class QueryQueue {
	
	/** CommonVo를 보관할 List */
	private ArrayList<CommonVo> list = new ArrayList<CommonVo>();
	
	/** 마지막 호출 인덱스 */
	private int lastCalledIndex = -1; 
	
	/** QueryType 지정 없이 Vo를 저장함 */
	public void add(CommonVo vo){
		list.add(vo);
	}
	
	/** QueryType 지정 없이 Vo를 저장함 */
	public void addFirst(CommonVo vo){
		list.add(0, vo);
	}
	
	/** insert 타입으로 Vo를 저장함 */
	public void insert(CommonVo vo){
		vo.setQueryType(QueryType.INSERT);
		list.add(vo);
	}
	
	/** update 타입으로 Vo를 저장함 */
	public void update(CommonVo vo){
		vo.setQueryType(QueryType.UPDATE);
		list.add(vo);
	}
	
	/** delete 타입으로 Vo를 저장함 */
	public void delete(CommonVo vo){
		vo.setQueryType(QueryType.DELETE);
		list.add(vo);
	}
	
	/** store 타입으로 Vo를 저장함, store 타입으로 데이터가 저장되면 update 실행하여 변경된 데이터가 없으면 insert 함 */
	public void store(CommonVo vo){
		vo.setQueryType(QueryType.STORE);
		list.add(vo);
	}
	
	/** QueryQueue에 보관된 Vo의 갯수를 리턴 */
	public int size(){
		return list.size();
	}
	
	/** QueryQueue에 보관된 Vo가 없는지 확인 */
	public boolean isEmpty(){
		return list.isEmpty();
	}
	
	/** QueryQueue에 보관된 해당 index 번째 Vo를 리턴 */
	public CommonVo get(int index){
		lastCalledIndex = index;
		return list.get(index);
	}
	
	/** QueryQueue에 보관된 해당 index 번째 Vo를 제거함 */
	public CommonVo remove(int index){
		return list.remove(index);
	}
	
	/** QueryQueue에 보관된 Vo 전체를 제거함 */
	public void removeAll(){
		for(int index = list.size()-1; index >= 0; index--){
			list.remove(index);
		}
	}
	
	/** 디버그용, Queue에 담긴 쿼리,VO 정보 스트링을 리턴함 */
	public String getDebugString(){
		StringBuffer buffer = new StringBuffer(2048);
		int i, size = list==null ? 0 : list.size();
		String queryId;
		CommonVo commonVo;
		QueryType queryType;
		for(i=0;i<size;i++){
			
			if(lastCalledIndex==i){
				buffer.append(">>>> LAST CALL -------\n");
			}
			
			commonVo = list.get(i);
			queryType = commonVo.getQueryType();
			if(queryType==null){
				continue;
			} else if(QueryType.INSERT == queryType){
				queryId = commonVo.getQueryId(QueryType.INSERT);
			} else if(QueryType.UPDATE == queryType){
				queryId = commonVo.getQueryId(QueryType.UPDATE);
			} else if(QueryType.DELETE == queryType){
				queryId = commonVo.getQueryId(QueryType.DELETE);
			} else if(QueryType.STORE == queryType){
				queryId = commonVo.getQueryId(QueryType.UPDATE)+" or "+commonVo.getQueryId(QueryType.INSERT);
			} else {
				continue;
			}
			
			buffer.append("QueryId : ").append(queryId).append('\n');
			buffer.append(commonVo.toString());
			buffer.append('\n');
			
		}
		
		return buffer.toString();
	}
	
	public int getLastIndex(){
		return lastCalledIndex;
	}
}
