package com.innobiz.orange.web.mail.jpa;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlBuilder {
	
	protected static final int INSERT = 1;
	protected static final int UPDATE = 2;
	protected static final int DELETE = 3;
	protected static final int SELECT = 4;
	protected static final int UPDATE_INSERT = 5;
	
	protected final static String[] NULL_STOARGE = { null };
	protected final static String FORMAT_STR = "0000-01-01 00:00:00";
	
	protected int sqlCommand = 0;
	
	protected String table;
	protected String orderByColumns = null;
	protected String groupByColumns = null;
	protected int pageNo = 0;
	protected int countPerPage = 0;
	
	protected List<Object> valueList;
	protected List<String> valueNameList;
	protected List<Object> whereList;
	protected List<String> whereNameList;
	protected List<String> selectColumnList;
	
	protected boolean paramCamelNotation = false;
	
	protected Query query;
	
	protected boolean isMysql;
	protected boolean isOracle;
	protected boolean isMssql;
	
	public static SqlBuilder create(Query query, String database){
		SqlBuilder ins = new SqlBuilder();
		ins.query = query;
		ins.isMysql = "mysql".equals(database);
		ins.isOracle = "oracle".equals(database);
		ins.isMssql = "mssql".equals(database);
		return ins;
	}
	
	public SqlBuilder insert(String table){
		this.sqlCommand = INSERT;
		this.table = table;
		initParams();
		return this;
	}
	public SqlBuilder insertOrUpdate(String table){
		this.sqlCommand = UPDATE_INSERT;
		this.table = table;
		initParams();
		return this;
	}
	public SqlBuilder update(String table){
		this.sqlCommand = UPDATE;
		this.table = table;
		initParams();
		return this;
	}
	public SqlBuilder delete(String table){
		this.sqlCommand = DELETE;
		this.table = table;
		initParams();
		return this;
	}
	public SqlBuilder select(String table){
		this.sqlCommand = SELECT;
		this.table = table;
		initParams();
		return this;
	}
	public SqlBuilder columns(String columns){
		if(selectColumnList==null){
			selectColumnList = new ArrayList<String>();
		}
		selectColumnList.add(columns);
		return this;
	}
	public SqlBuilder columnsOuter(String columns){
		if(selectColumnList==null){
			selectColumnList = new ArrayList<String>();
		}
		selectColumnList.add("@OUT^"+columns);
		return this;
	}
	public SqlBuilder setPage(int pageNo, int countPerPage){
		this.pageNo = pageNo;
		this.countPerPage = countPerPage;
		return this;
	}
	protected void initParams(){
		removeList(valueList);
		removeList(valueNameList);
		removeList(whereList);
		removeList(whereNameList);
		removeList(selectColumnList);
		orderByColumns = null;
		pageNo = 0;
		countPerPage = 0;
	}
	protected void removeList(List<?> list){
		if(list!=null){
			for(int i = list.size()-1; i>=0; i--){
				list.remove(i);
			}
		}
	}
	
	public SqlBuilder setHeaderCamelNotation(boolean headerCamelNotation){
		query.setHeaderCamelNotation(headerCamelNotation);
		return this;
	}
	public SqlBuilder setPraramCamelNotation(boolean paramCamelNotation){
		this.paramCamelNotation = paramCamelNotation;
		return this;
	}
	public SqlBuilder orderBy(String orderByColumns){
		this.orderByColumns = orderByColumns;
		return this;
	}
	
	public SqlBuilder values(String columnName, Object value){
		if(valueList==null){
			valueList = new ArrayList<Object>();
			valueNameList = new ArrayList<String>();
		}
		valueList.add(value);
		valueNameList.add(paramCamelNotation ? Query.deCamelNotation(columnName) : columnName);
		return this;
	}
	public SqlBuilder valueStorage(String columnName, Object value, String storage){
		if(valueList==null){
			valueList = new ArrayList<Object>();
			valueNameList = new ArrayList<String>();
		}
		valueList.add(value);
		String valueName = "@STOR^"+storage+"^"+(paramCamelNotation ? Query.deCamelNotation(columnName) : columnName);
		valueNameList.add(valueName);
		return this;
	}
	public SqlBuilder valueDate(String columnName, Object value){
		if(valueList==null){
			valueList = new ArrayList<Object>();
			valueNameList = new ArrayList<String>();
		}
		valueList.add("@DATE^"+value);
		valueNameList.add(paramCamelNotation ? Query.deCamelNotation(columnName) : columnName);
		return this;
	}
	public SqlBuilder valuesFromMap(String[] columnNames, Map<String, ?> paramMap){
		if(columnNames==null) return this;
		if(valueList==null){
			valueList = new ArrayList<Object>();
			valueNameList = new ArrayList<String>();
		}
		for(String columnName : columnNames){
			valueList.add(paramMap.get(columnName));
			valueNameList.add(paramCamelNotation ? Query.deCamelNotation(columnName) : columnName);
		}
		return this;
	}
	
	public SqlBuilder wheres(String columnName, Object value){
		if(whereList==null){
			whereList = new ArrayList<Object>();
			whereNameList = new ArrayList<String>();
		}
		whereList.add(value);
		whereNameList.add(paramCamelNotation ? Query.deCamelNotation(columnName) : columnName);
		return this;
	}
	public SqlBuilder wheresExpression(String expression){
		return wheresExpression(expression, Void.class);
	}
	public SqlBuilder wheresExpression(String expression, Object value){
		if(whereList==null){
			whereList = new ArrayList<Object>();
			whereNameList = new ArrayList<String>();
		}
		whereList.add(value);
		String whereName = "@EXP^"+expression;
		whereNameList.add(whereName);
		return this;
	}
	
	public SqlBuilder wheresFromMap(String[] columnNames, Map<String, ?> paramMap){
		if(columnNames==null) return this;
		if(whereList==null){
			whereList = new ArrayList<Object>();
			whereNameList = new ArrayList<String>();
		}
		for(String columnName : columnNames){
			whereList.add(paramMap.get(columnName));
			whereNameList.add(paramCamelNotation ? Query.deCamelNotation(columnName) : columnName);
		}
		return this;
	}
	public int execute() throws SQLException{
		if(sqlCommand == UPDATE_INSERT){
			sqlCommand = UPDATE;
			int result = executeEx(null);
			if(result == 0){
				sqlCommand = INSERT;
				result = executeEx(null);
			}
			sqlCommand = UPDATE_INSERT;
			return result;
		} else {
			return executeEx(null);
		}
	}
	public int executeStorage(String[] storages) throws SQLException{
		if(sqlCommand == UPDATE_INSERT){
			throw new SQLException("NOT Support !");
		}
		return executeEx(storages);
	}
	private int executeEx(String[] storages) throws SQLException{
		StringBuilder builder;
		
		boolean first;
		int i, size, resultCount=0;
		String column, tableEx;
		List<Object> paramList;
		
		for(String storage : storages==null ? NULL_STOARGE : storages){
			if(sqlCommand == INSERT){
				if(valueNameList==null || valueNameList.isEmpty()) return 0;
				
				first = true;
				paramList = new ArrayList<Object>();
				
				tableEx = storage==null ? table : table.replace("$", storage);
				builder = new StringBuilder(256);
				builder.append("insert into ").append(tableEx).append(" (\r\n");
				size = valueNameList.size();
				
				StringBuilder valueBuilder = new StringBuilder(256);
				valueBuilder.append("values (\r\n");
				
				for(i=0; i<size; i++){
					
					column = valueNameList.get(i);
					column = getStorageColumn(column, storage);
					if(column == null) continue;
					
					if(first){
						first = false;
						builder.append("  ").append(column);
						valueBuilder.append("  ");
					} else {
						if(i%5==0){
							builder.append(",\r\n  ").append(column);
							valueBuilder.append(",\r\n  ");
						} else {
							builder.append(", ").append(column);
							valueBuilder.append(", ");
						}
					}
					setValueObject(valueBuilder, paramList, valueList.get(i));
				}
				builder.append(" )\r\n");
				valueBuilder.append(" )");
				
				resultCount = query.executeUpdate(builder.append(valueBuilder).toString(), paramList.toArray());
			} else if(sqlCommand == DELETE){
				paramList = new ArrayList<Object>();
				tableEx = storage==null ? table : table.replace("$", storage);
				builder = new StringBuilder(256);
				builder.append("delete from ").append(tableEx);
				parseWhere(builder, paramList, storage);
				resultCount = query.executeUpdate(builder.toString(), paramList.toArray());
			} else if(sqlCommand == UPDATE){
				if(valueNameList==null || valueNameList.isEmpty()) return 0;
				
				first = true;
				paramList = new ArrayList<Object>();
				tableEx = storage==null ? table : table.replace("$", storage);
				builder = new StringBuilder(256);
				builder.append("update ").append(tableEx).append(" set\r\n");
				size = valueNameList.size();
				
				for(i=0; i<size; i++){
					
					column = valueNameList.get(i);
					column = getStorageColumn(column, storage);
					if(column == null) continue;
					
					if(first){
						first = false;
						builder.append("  ");
					} else {
						builder.append(", ");
					}
					builder.append(column).append(" = ");
					setValueObject(builder, paramList, valueList.get(i));
				}
				parseWhere(builder, paramList, storage);
				resultCount = query.executeUpdate(builder.toString(), paramList.toArray());
			}
		}
		
		return resultCount;
	}
	
	private void setValueObject(StringBuilder builder, List<Object> paramList, Object valueObject){
		String valueString;
		if(valueObject instanceof String){
			valueString = valueObject.toString();
		} else if(valueObject instanceof java.util.Date){
			valueString = Query.DEFAULT_FORMATTER.format((java.util.Date)valueObject);
		} else {
			valueString = null;
		}
		
		if(valueString != null){
			if(valueString.equals("@sysdate") || valueString.equals("@DATE^sysdate")){
				if(isMysql){
					builder.append("NOW()");
				} else if(isOracle){
					builder.append("SYSDATE");
				} else if(isMssql){
					builder.append("GETDATE()");
				}
			} else if(valueString.startsWith("@DATE")){
				valueString = valueString.substring(6);
				builder.append(cvtStringToDate(valueString));
				
				int len = valueString.length();
				if("".equals(valueString) || len==19){
					paramList.add(valueString);
				} else if(len>19){
					paramList.add(valueString.substring(0, 19));
				} else {
					paramList.add(valueString+FORMAT_STR.substring(valueString.length()));
				}
			} else {
				builder.append("?");
				paramList.add(valueString);
			}
		} else {
			builder.append("?");
			paramList.add(valueObject);
		}
	}
	
	private String getStorageColumn(String column, String storage){
		if(storage!=null && column.startsWith("@STOR")){
			String[] columnExts = column.split("\\^");
			if(columnExts.length<3 || !columnExts[1].equals(storage)){
				return null;
			}
			if(column.length()>3){
				StringBuilder temp = new StringBuilder();
				for(int j=2; j<columnExts.length; j++){
					if(j>2) temp.append("^");
					temp.append(columnExts[j]);
				}
				column = temp.toString();
			} else {
				column = columnExts[2];
			}
		}
		return column;
	}
	
	private String cvtStringToDate(String dateValue) {
		if("".equals(dateValue)){
			return "?";
		} else if(isMysql){
			return "STR_TO_DATE(?, '%Y-%m-%d %H:%i:%s')";
		} else if(isOracle){
			return "TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')";
		} else if(isMssql){
			return "CONVERT(DATETIME, ?, 120)";
		}
		return "?";
	}

	protected void parseWhere(StringBuilder builder, List<Object> paramList, String storage) throws SQLException{
		String column, expression;
		Object valueObject;
		Object[] objects, subObjects;
		int i, size = whereNameList==null ? 0 : whereNameList.size();
		int inCnt, qCnt, p;
		boolean first=true, subFirst;
		for(i=0; i<size; i++){
			
			column = whereNameList.get(i);
			column = getStorageColumn(column, storage);
			if(column == null) continue;
			
			valueObject = whereList.get(i);
			
			if(first){
				builder.append("\r\nwhere ");
				first = false;
			} else {
				builder.append("\r\n  and ");
			}
			
			objects = null;
			if(valueObject instanceof List){
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>)valueObject;
				objects = list.toArray();
			}
			if(valueObject instanceof Object[]){
				objects = (Object[])valueObject;
			}
			
			if(column.startsWith("@EXP")){
				expression = column.substring(5);
				
				if(expression.toLowerCase().startsWith("and")){
					expression = expression.substring(3).trim();
				}
				
				inCnt = countParam(expression, "@IN");
				qCnt = countParam(expression, "?");
				if(objects==null){
					objects = new Object[]{ valueObject };
				} else if(qCnt==0 && inCnt==1){
					objects = new Object[]{ valueObject };
				}
				
				p = 0;
				for(Object obj : objects){
					
					if(obj instanceof Object[]){
						subObjects = (Object[])obj;
						
						p = expression.indexOf("@IN");
						if(p<0){
							throw new SQLException("parsing error - check '@IN' !");
						}
						builder.append(expression, 0, p);
						expression = expression.substring(p+3);
						
						subFirst = true;
						builder.append("in (");
						for(Object subObject : subObjects){
							if(subObject==null) continue;
							if(subFirst){
								subFirst = false;
							} else {
								builder.append(", ");
							}
							builder.append("?");
							paramList.add(subObject);
						}
						builder.append(")");
					} else if(obj != Void.class){
						p = expression.indexOf("?");
						if(p<0){
							throw new SQLException("parsing error - check and count '?' !");
						}
						builder.append(expression, 0, p);
						expression = expression.substring(p+1);
						setValueObject(builder, paramList, obj);
					}
				}
				builder.append(expression);
				
			} else {
				if(countNotNull(objects) == 1){
					valueObject = objects[0];
					objects = null;
				}
				if(objects != null){
					objects = (Object[])valueObject;
					builder.append(column).append(" IN ( ");
					subFirst = true;
					for(int j=0; j<objects.length; j++){
						if(objects[j]==null) continue;
						if(subFirst) subFirst = false;
						else builder.append(", ");
						builder.append('?');
						paramList.add(objects[j]);
					}
					builder.append(" )");
				} else {
					builder.append(column).append(" = ?");
					paramList.add(valueObject);
				}
			}
		}
	}
	
	private int countNotNull(Object[] objects){
		if(objects==null) return 0;
		int cnt = 0;
		for(Object object : objects){
			if(object != null) cnt++;
		}
		if(cnt==1 && objects.length>1){
			for(Object object : objects){
				if(object != null){
					objects[0] = object;
				}
			}
		}
		return cnt;
	}
	
	
	public Map<String, String> queryMap() throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryMap(builder.toString(), paramList.toArray());
	}
	
	public List<Map<String, String>> queryList() throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryList(builder.toString(), paramList.toArray());
	}
	
	public String queryString() throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryString(builder.toString(), paramList.toArray());
	}
	
	public Long queryLong() throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryLong(builder.toString(), paramList.toArray());
	}
	
	public Integer queryInt() throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryInt(builder.toString(), paramList.toArray());
	}
	
	public <T> T queryObject(Class<T> resultClass) throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryObject(builder.toString(), paramList.toArray(), resultClass);
	}
	
	public <T> List<T> queryObjectList(Class<T> resultClass) throws SQLException, IOException{
		if(sqlCommand != SELECT) return null;
		StringBuilder builder = new StringBuilder(256);
		List<Object> paramList = new ArrayList<Object>();
		prepareSelect(builder, paramList);
		return query.queryObjectList(builder.toString(), paramList.toArray(), resultClass);
	}
	
	protected void prepareSelect(StringBuilder builder, List<Object> paramList) throws SQLException{
		
		int outColumnCount = 0;
		if(selectColumnList != null){
			for(String col : selectColumnList){
				if(col.startsWith("@OUT")){
					outColumnCount++;
				}
			}
		}
		
		if(countPerPage>0){
			
			if(isMysql){
				if(outColumnCount>0){
					builder.append("SELECT M.*,");
					// outer column
					for(String cols : selectColumnList){
						if(cols.startsWith("@OUT")){
							cols = cols.substring(5).trim();
							builder.append("\r\n  ").append(cols);
							if(!cols.endsWith(",")) builder.append(",");
						}
					}
					// remove last comma
					builder.delete(builder.length()-1, builder.length()).append("\r\n");
					builder.append("FROM (\r\nselect");
				} else {
					builder.append("select");
				}
			} else if(isMssql){
				
				if(orderByColumns==null){
					throw new SQLException("MSSQL need 'order by' clause");
				}
				
				if(outColumnCount>0){
					builder.append("SELECT M.*,");
					// outer column
					for(String cols : selectColumnList){
						if(cols.startsWith("@OUT")){
							cols = cols.substring(5).trim();
							builder.append("\r\n  ").append(cols);
							if(!cols.endsWith(",")) builder.append(",");
						}
					}
					// remove last comma
					builder.delete(builder.length()-1, builder.length()).append("\r\n");
					
					builder.append("FROM (SELECT * FROM (SELECT TOP (? * ?) ROW_NUMBER() OVER (ORDER BY ")
					.append(orderByColumns)
					.append(") AS RNUM, N.* FROM (\r\nselect");
					
					paramList.add(pageNo);
					paramList.add(countPerPage);
					
				} else {
					builder.append("SELECT * FROM (SELECT\r\n");
					builder.append("TOP (? * ?) ROW_NUMBER() OVER (ORDER BY ")
						.append(orderByColumns)
						.append(") AS RNUM, N.* FROM (\r\nselect");
					
					paramList.add(pageNo);
					paramList.add(countPerPage);
				}
				
			} else if(isOracle){
				if(outColumnCount>0){
					builder.append("SELECT M.*,");
					for(String cols : selectColumnList){
						if(cols.startsWith("@OUT")){
							cols = cols.substring(5).trim();
							builder.append("\r\n  ").append(cols);
							if(!cols.endsWith(",")) builder.append(",");
						}
					}
					// remove last comma
					builder.delete(builder.length()-1, builder.length()).append("\r\n");
					builder.append("FROM (SELECT * FROM (SELECT N.*, ROWNUM RNUM FROM (\r\nselect");
				} else {
					builder.append("SELECT * FROM (SELECT * FROM (SELECT N.*, ROWNUM RNUM FROM (\r\nselect");
				}
			}
		} else {
			builder.append("select");
		}
		
		// columns
		if(selectColumnList==null || selectColumnList.isEmpty()){
			builder.append(" *\r\nfrom ").append(table);
		} else {
			
			String cols;
			int i, size = selectColumnList.size();
			for(i=0; i<size; i++){
				cols = selectColumnList.get(i).trim();
				if(cols.startsWith("@OUT")){
					if(countPerPage > 0) continue;
					cols = cols.substring(5);
				}
				builder.append("\r\n  ").append(cols);
				if(!cols.endsWith(",")) builder.append(",");
			}
			// remove last comma
			builder.delete(builder.length()-1, builder.length()).append("\r\n");
			builder.append("from ").append(table);
		}
		parseWhere(builder, paramList, null);
		
		if(orderByColumns != null && !(isMssql && countPerPage > 0)){
			builder.append("\r\norder by ").append(orderByColumns);
		}
		if(groupByColumns != null){
			builder.append("\r\ngroup by ").append(groupByColumns);
		}
		
		if(countPerPage > 0){
			builder.append("\r\n");
			if(isMysql){
				if(outColumnCount>0){
					builder.append("LIMIT ?, ? ) M");
					paramList.add((pageNo -1)*countPerPage + 1);
					paramList.add(countPerPage);
				} else {
					builder.append("LIMIT ?, ?");
					paramList.add((pageNo -1)*countPerPage + 1);
					paramList.add(countPerPage);
				}
			} else if(isMssql){
				if(outColumnCount>0){
					builder.append(") N ) M WHERE RNUM > (? - 1) * ? AND RNUM <= ? * ? ) M");
					paramList.add(pageNo);
					paramList.add(countPerPage);
					paramList.add(pageNo);
					paramList.add(countPerPage);
				} else {
					builder.append(") N ) M WHERE RNUM > (? - 1) * ? AND RNUM <= ? * ?");
					paramList.add(pageNo);
					paramList.add(countPerPage);
					paramList.add(pageNo);
					paramList.add(countPerPage);
				}
				builder.append("\r\norder by ").append(orderByColumns);
			} else if(isOracle){
				if(outColumnCount>0){
					builder.append(") N WHERE ROWNUM <= ? * ?) WHERE RNUM > (? - 1) * ? ) M");
					paramList.add(pageNo);
					paramList.add(countPerPage);
					paramList.add(pageNo);
					paramList.add(countPerPage);
				} else {
					builder.append(") N WHERE ROWNUM <= ? * ?) WHERE RNUM > (? - 1) * ? ) M");
					paramList.add(pageNo);
					paramList.add(countPerPage);
					paramList.add(pageNo);
					paramList.add(countPerPage);
				}
			}
		}
	}
	private int countParam(String expression, String finding){
		int p=0, cnt=0, len = finding.length();
		while((p = expression.indexOf(finding, p+len)) > 0){
			cnt++;
		}
		return cnt;
	}
}
