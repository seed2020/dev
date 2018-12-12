package com.common.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CodeGenerator {
	
	/** 테이블 스크립트 */
	@SuppressWarnings("unchecked")
	public String createTableScript(Properties prop, Map<String, Object> tableMap) throws Exception{
		StringBuilder sb = new StringBuilder();
		String dbms = prop.getProperty("dbms");
		
		String toDate = StringUtil.getCurrDateTime();
		
		// 테이블 목록
		List<String> tableList = (List<String>)tableMap.get("tableList");
		
		// 컬럼 목록맵
		Map<String, List<Map<String,String>>> colmListMap = (Map<String, List<Map<String,String>>>)tableMap.get("colmListMap");
		
		String tableId;
		String[] tables;
		List<Map<String,String>> colmList;
		
		// pk 목록맵
		Map<String, List<String>> pkMapList = (Map<String, List<String>>)tableMap.get("pkMapList");
		
		// pkId 목록
		List<String> pkIdList;
		
		int pkIdx;
		for(String tableString : tableList){
			if(tableString==null) {
				throw new Exception("[ERROR] - createTableScript ==> tableString is null!!");
			}
			tables=tableString.split(":");
			tableId=tables[0];
			colmList=colmListMap.get(tableId);
			if(colmList==null) continue;
			if("mssql".endsWith(dbms)){
				sb.append("/****** Object:  Table [dbo].[").append(tableId).append("]    Script Date: ").append(toDate).append(" ******/\n");
				sb.append("CREATE TABLE [dbo].[").append(tableId).append("](").append("\n\t");
				addColmScript(dbms, colmList, sb);
				
				if(pkMapList.containsKey(tableId)) {
					pkIdx=0;
					pkIdList=pkMapList.get(tableId);
					sb.append(",\n");
					sb.append("CONSTRAINT [PK_").append(tableId).append("] PRIMARY KEY CLUSTERED \n(\n\t");
					for(String pkId : pkIdList) {
						sb.append("[").append(pkId).append("] ASC");
						if((pkIdx+1)!=pkIdList.size()) sb.append(",");
						sb.append("\n");
						if((pkIdx+1)!=pkIdList.size()) sb.append("\t");
						pkIdx++;
					}
					sb.append(") WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]\n");
					sb.append(") ON [PRIMARY]\n");
					sb.append("GO\n\n");
				}else {
					sb.append(")\n");
					sb.append("GO\n\n");
				}
				
				boolean isColmComment=false;
				
				// 테이블 코멘트
				if(tables.length>1) {
					sb.append("EXEC sp_addextendedproperty @name = N'Caption', @value = N'").append(tables[1]).append("', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = '").append(tableId).append("';\n");
					if(!isColmComment) isColmComment=true;
				}
				
				for(Map<String,String> colmMap : colmList){
					if(!colmMap.containsKey("colmNm")) continue;
					if(!isColmComment) isColmComment=true;
					sb.append("EXEC sp_addextendedproperty @name = N'Caption', @value = N'").append(colmMap.get("colmNm")).append("', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = '");
					sb.append(tableId).append("', @level2Type = N'Column', @Level2Name = '").append(colmMap.get("colmId")).append("';\n");
				}
				if(isColmComment) sb.append("GO\n\n");
				
				
			}else if("mysql".endsWith(dbms)){
				sb.append("/****** Object:  Table ").append(tableId).append("  Script Date: ").append(toDate).append(" ******/\n");
				sb.append("CREATE TABLE ").append(tableId).append("(").append("\n\t");
				addColmScript(dbms, colmList, sb);
				if(pkMapList.containsKey(tableId)) {
					pkIdx=0;
					pkIdList=pkMapList.get(tableId);
					sb.append(",\n\t");
					sb.append("PRIMARY KEY (");
					
					for(String pkId : pkIdList) {
						sb.append(pkId);
						if((pkIdx+1)!=pkIdList.size()) sb.append(",");						
						pkIdx++;
					}
					sb.append(")\n");
				}
				
				sb.append(")\n");
				// 테이블 코멘트
				if(tables.length>1) {
					sb.append("COMMENT = '").append(tables[1]).append("';\n");
				}else {
					sb.append(";");
				}
				sb.append("\n\n\n");
				
			}else if("oracle".endsWith(dbms)){
				sb.append("/****** Object:  Table ").append(tableId).append("  Script Date: ").append(toDate).append(" ******/\n");
				sb.append("CREATE TABLE ").append(tableId).append("(").append("\n\t");
				addColmScript(dbms, colmList, sb);
				sb.append("\n)\n");
				
				sb.append("TABLESPACE TS_GW_WK\n");
				sb.append("STORAGE (INITIAL 1M NEXT 1M MAXEXTENTS UNLIMITED)\n");
				sb.append("LOGGING\n");
				sb.append("NOPARALLEL;\n");
				
				if(pkMapList.containsKey(tableId)) {
					pkIdx=0;
					pkIdList=pkMapList.get(tableId);
					sb.append("\n");
					sb.append("CREATE UNIQUE INDEX PK_").append(tableId).append(" ON ").append(tableId).append("\n");
					sb.append("(\n\t");
					
					for(String pkId : pkIdList) {
						sb.append(pkId).append(" ASC");
						if((pkIdx+1)!=pkIdList.size()) sb.append(",");
						sb.append("\n");
						if((pkIdx+1)!=pkIdList.size()) sb.append("\t");
						pkIdx++;
					}
					sb.append(")\n");
					sb.append("TABLESPACE TS_GW_WK\n");
					sb.append("LOGGING\n");
					sb.append("NOPARALLEL;\n\n");
					
					sb.append("ALTER TABLE ").append(tableId).append(" ADD\n");
					sb.append("(CONSTRAINT PK_").append(tableId).append(" PRIMARY KEY (");
					pkIdx=0;
					for(String pkId : pkIdList) {
						sb.append(pkId);
						if((pkIdx+1)!=pkIdList.size()) sb.append(",");
						pkIdx++;
					}
					sb.append("));");
				}
				sb.append("\n\n");
				// 테이블 코멘트
				if(tables.length>1) {
					sb.append("COMMENT ON TABLE ").append(tableId).append(" IS '").append(tables[1]).append("';\n");
				}
				boolean isColmComment=false;
				for(Map<String,String> colmMap : colmList){
					if(!colmMap.containsKey("colmNm")) continue;
					if(!isColmComment) isColmComment=true;
					sb.append("COMMENT ON COLUMN ").append(tableId).append(".").append(colmMap.get("colmId")).append(" IS '").append(colmMap.get("colmNm")).append("';\n");
				}
				
				if(isColmComment) sb.append("\n\n");
				
				
			}
		}
		
		
		return sb.toString();
		
	}
	
	/** 컬럼 스크립트 추가 */
	public void addColmScript(String dbms, List<Map<String,String>> colmList, StringBuilder sb) {
		int cnt=0;
		for(Map<String,String> colmMap : colmList){
			if("mssql".equals(dbms)) sb.append("[");
			sb.append(colmMap.get("colmId"));
			if("mssql".equals(dbms)) sb.append("]");
			sb.append("\t").append(toDbmsDataType(dbms, colmMap));
			if(colmMap.get("nullYn")!=null && "N".equals(colmMap.get("nullYn")))
				sb.append("\tNOT NULL");
			
			if("mysql".equals(dbms) && colmMap.get("colmNm")!=null) sb.append("\tCOMMENT '").append(colmMap.get("colmNm")).append("'");
			
			if((cnt+1)!=colmList.size()) sb.append(",\n\t");
			cnt++;
		}
	}
	
	/** DBMS 별 컬럼타입, 길이 변환 - 기준 MS-SQL */
	private String toDbmsDataType(String dbms, Map<String,String> colmMap){
		String dataTyp = colmMap.get("dataTyp");
		String dataLen = colmMap.get("dataLen");
		if(dataLen!=null && !dataLen.isEmpty() && dataLen.indexOf(".")>-1) dataLen=dataLen.substring(0, dataLen.indexOf("."));
		String returnString=null;
		if("oracle".equals(dbms)) {
			if("VARCHAR".equals(dataTyp)){
				returnString="VARCHAR2";
				if(dataLen!=null) returnString+="("+dataLen+")";
			} else if("NVARCHAR".equals(dataTyp)){
				returnString="NVARCHAR2";
				if(dataLen!=null) returnString+="("+dataLen+")";
			} else if("INT".equals(dataTyp)){
				returnString="NUMBER";
				returnString+="(10)";
			} else if("BIGINT".equals(dataTyp)){
				returnString="NUMBER";
				returnString+="(20)";
			} else if("DATETIME".equals(dataTyp)){
				returnString="DATE";
			} else if("TEXT".equals(dataTyp)){
				returnString="CLOB";
			} else if("NTEXT".equals(dataTyp)){
				returnString="NCLOB";
			} else if("IMAGE".equals(dataTyp)){
				returnString="BLOB";
			} else {
				returnString="VARCHAR2";
			}
		} else if("mysql".equals(dbms)){
			if("INT".equals(dataTyp) || "BIGINT".equals(dataTyp) || "DATETIME".equals(dataTyp)){
				returnString=dataTyp;
			} else if("VARCHAR".equals(dataTyp) || "NVARCHAR".equals(dataTyp)){
				returnString="VARCHAR";
				if(dataLen!=null) returnString+="("+dataLen+")";
			} else if("TEXT".equals(dataTyp) || "NTEXT".equals(dataTyp)){
				returnString="LONGTEXT";
			} else if("IMAGE".equals(dataTyp) || "FILE".equals(dataTyp) || "VARBINARY".equals(dataTyp)){
				returnString="LONGBLOB";
			} else {
				returnString="VARCHAR";
			}
		}else {
			returnString="["+dataTyp+"]";
			if(dataLen!=null) returnString+="("+dataLen+")";
			
		}
		return returnString;
	}
	
	/** set get vo 클래스 생성 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> createVoClass(Properties prop, Map<String, Object> tableMap) throws Exception{
		
		// vo코드 목록
		List<Map<String,String>> codeList = new ArrayList<Map<String,String>>();
		
		// vo맵
		Map<String,String> codeMap;
		
		
		// 테이블 목록
		List<String> tableList = (List<String>)tableMap.get("tableList");
		
		// 컬럼 목록맵
		Map<String, List<Map<String,String>>> colmListMap = (Map<String, List<Map<String,String>>>)tableMap.get("colmListMap");
		
		// 컬럼 목록
		List<Map<String,String>> colmList;
		
		// 클래스명, vo 데이터구분, vo 컬럼명
		String classNm, voNm, voDataTyp, voColmNm;
		
		// 컬럼id, 컬럼 데이터 구분
		String colmId, colmDataTyp, colmComment;
		// 테이블ID
		String tableId;
		// vo 패키지명
		String voPackage = prop.getProperty("vo.package");
		// vo 에 추가할 확장 클래스명
		String extendsClass = prop.getProperty("vo.extends");
		// sqlId 추가여부
		String sqlIdYn = prop.getProperty("vo.sqlIdYn");
		// sqlId 모듈명
		String mdNm = prop.getProperty("vo.sqlId.mdNm");
		
		String[] tables;
		for(String tableString : tableList){
			tables=tableString.split(":");
			tableId=tables[0];
			colmList=colmListMap.get(tableId);
			if(colmList==null) continue;
			
			StringBuilder code = new StringBuilder();
			
			if(voPackage!=null) {
				code.append("package ").append(voPackage).append("\n\n");
			}
			
			code.append("/****** Object:  Vo - Date: ").append(StringUtil.getCurrDateTime()).append(" ******/\n");
			if(tables.length>1) {
				code.append("/**\n");
				code.append("* ").append(tables[1]).append("(").append(tableId).append(") 테이블 VO \n");
				code.append("*/\n");
			}
			
			code.append("public class ");
			classNm=StringUtil.toCamelNotation(tableId, true);
			voNm=classNm+"Vo";
			code.append(voNm); // 클래스명
			if(extendsClass!=null) {
				code.append(" extends ").append(extendsClass);
			}
			code.append(" { \n");
			
			// 컬럼 목록 생성
			for(Map<String,String> colmMap : colmList){
				colmDataTyp=colmMap.get("dataTyp");
				if(colmDataTyp==null) {
					throw new Exception("[ERROR] - createVoClass ==> column data type is null!!");
				}
				
				if(colmDataTyp.startsWith("NUM")){
					voDataTyp = "int";
				}else if(colmDataTyp.startsWith("VAR") 
						|| colmDataTyp.startsWith("CHAR")
						|| colmDataTyp.startsWith("CLOB") 
						|| colmDataTyp.startsWith("DATE")
						|| colmDataTyp.startsWith("NVAR") 
						|| colmDataTyp.startsWith("INT")
						|| colmDataTyp.startsWith("TEX")
						){
					voDataTyp = "String";
				}else if(colmDataTyp.startsWith("BICDEC")){
					voDataTyp = "long";
				}else {
					voDataTyp = "String";
				}
				colmId=colmMap.get("colmId");
				if(colmId==null) {
					throw new Exception("[ERROR] - createVoClass ==> columnId is null!!");
				}
				
				voColmNm=StringUtil.toCamelNotation(colmId, false);
				colmComment = colmMap.get("colmNm");
				
				if(colmComment!=null) {
					code.append("\t/** " + colmComment + " */ \n");
				} else {
					code.append("\n");
				}
				code.append("\tprivate " + voDataTyp + " " + voColmNm + ";\n"); 
				code.append("\n ");
			}
			
		
			// set get 메소드 생성
			for(Map<String,String> colmMap : colmList){
				colmDataTyp=colmMap.get("dataTyp");
				
				if(colmDataTyp.startsWith("NUM")){
					voDataTyp="int";
				}else if(colmDataTyp.startsWith("VAR") 
						|| colmDataTyp.startsWith("CHAR")
						|| colmDataTyp.startsWith("CLOB") 
						|| colmDataTyp.startsWith("DATE")
						|| colmDataTyp.startsWith("NVAR") 
						|| colmDataTyp.startsWith("INT")
						){
					voDataTyp = "String";
				}else if(colmDataTyp.startsWith("BICDEC")){
					voDataTyp = "long";
				}else {
					voDataTyp = "String";
				}
				colmId=colmMap.get("colmId");
				voColmNm=StringUtil.toCamelNotation(colmId, false);
				colmComment = colmMap.get("colmNm");
				
				//setter
				code.append("\tpublic void set" + StringUtil.changeInitCap(voColmNm) + "(" + voDataTyp + " " + voColmNm + ") { \n");
				code.append("\t\tthis." + voColmNm + " = " + voColmNm + ";\n");
				code.append("\t}\n");
				
				//comment
				if(colmComment!=null) {
					code.append("\t/** " + colmComment + " */ \n");
				} else {
					code.append("\n");
				}
				//getter
				code.append("\tpublic " + voDataTyp + " get" + StringUtil.changeInitCap(voColmNm) + "() { \n");
				code.append("\t\treturn " + voColmNm + ";\n");
				code.append("\t}\n\n");
			}
			
			// sqlId 추가
			if(sqlIdYn!=null && "Y".equals(sqlIdYn) && mdNm!=null) {
				code.append("\t/** SQL ID 리턴 */\n");
				code.append("\tpublic String getQueryId(QueryType queryType) {\n");
				code.append("\t\tif(getInstanceQueryId()!=null) return getInstanceQueryId();\n");
				
				String[] queryList = new String[]{"select", "insert", "update", "delete", "count"};
				for(int i=0;i<queryList.length;i++) {
					if(i>0) code.append("else ");
					else code.append("\t\t");
					code.append("if(QueryType.").append(queryList[i].toUpperCase()).append("==queryType){\n");
					code.append("\t\t\treturn \"com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao.").append(queryList[i]).append(classNm).append("\";\n");
					code.append("\t\t}");
				}
				code.append("\n\t\treturn null;\n\t}\n\n");
			}
			tables=tableString.split(":");
			
			if(tables.length>1) {
				//toString gen
				code.append("\t/** String으로 변환 */ \n");
				code.append("\tpublic String toString() { \n");
				code.append("\t\tStringBuilder builder = new StringBuilder(512);\n");
				code.append("\t\tbuilder.append('[').append(this.getClass().getName()).append(\":"+tables[1]+"]\");\n");
				code.append("\t\ttoString(builder, null);\n");
				code.append("\t\treturn builder.toString();\n");
				code.append("\t}\n\n");
			}
			
			//toString builder
			code.append("\t/** String으로 변환, builder에 append 함 */ \n");
			code.append("\tpublic void toString(StringBuilder builder, String tab) { \n");
			for(Map<String,String> colmMap : colmList){
				colmDataTyp=colmMap.get("dataTyp");
				colmId=colmMap.get("colmId");
				voColmNm=StringUtil.toCamelNotation(colmId, false);
				colmComment = colmMap.get("colmNm");
				
				code.append("\t\tif(" + voColmNm + "!=null) { if(tab!=null) builder.append(tab); builder.append(\""+voColmNm+"("+colmComment+"):\").append("+voColmNm+").append(\'\\n\'); }\n");
			}
			code.append("\t\tsuper.toString(builder, tab);\n");			
			code.append("\t}\n\n");
			
			code.append("}\n");	
			
			codeMap=new HashMap<String,String>();
			codeMap.put("voNm", voNm);
			codeMap.put("voString", code.toString());
			codeList.add(codeMap);
		}
		return codeList;
	}
	
	
	/** MyBatis XML 생성 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> createMyBatisXML(Properties prop, Map<String, Object> tableMap) throws Exception{
		
		// vo코드 목록
		List<Map<String,String>> codeList = new ArrayList<Map<String,String>>();
		
		// vo맵
		Map<String,String> codeMap;
		
		
		// 테이블 목록
		List<String> tableList = (List<String>)tableMap.get("tableList");
		
		// 컬럼 목록맵
		Map<String, List<Map<String,String>>> colmListMap = (Map<String, List<Map<String,String>>>)tableMap.get("colmListMap");
		
		// 컬럼 목록
		List<Map<String,String>> colmList;
		
		// pk 목록맵
		Map<String, List<String>> pkMapList = (Map<String, List<String>>)tableMap.get("pkMapList");
		
		// pkId 목록
		List<String> pkIdList;
		
		int pkIdx;
		
		// 클래스명, vo 데이터구분, vo 컬럼명
		String mapId, classNm;
		
		// 컬럼id, 컬럼 데이터 구분
		String colmId, colmProId, colmComment, pkColmId, srchTyp;
		// 테이블ID
		String tableId;
		
		// sqlId 모듈명
		String mdNm = prop.getProperty("vo.sqlId.mdNm");
		
		boolean isPk;
		
		StringBuilder code = null;
		String[] tables;
		
		// DBMS
		String dbms = prop.getProperty("dbms");
		
		for(String tableString : tableList){
			tables=tableString.split(":");
			tableId=tables[0];
			colmList=colmListMap.get(tableId);
			if(colmList==null) continue;
			
			code = new StringBuilder();
			
			code.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>/\n");
			code.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n\n");
			
			code.append("<!-- TABLE : ").append(tableId);
			if(tables.length>1) {
				code.append("[").append(tables[1]).append("] -->");
			}
			code.append("\n");
			
			mapId=StringUtil.toCamelNotation(tableId, false);
			classNm=StringUtil.changeInitCap(mapId);
			
			code.append("<mapper namespace=\"com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao\">\n\n");
			code.append("\t<resultMap id=\"").append(mapId).append("Map\" type=\"").append(classNm).append("Vo\">\n");
			
			// 컬럼 목록
			for(Map<String,String> colmMap : colmList){
				
				colmId=colmMap.get("colmId");
				if(colmId==null) {
					throw new Exception("[ERROR] - createVoClass ==> columnId is null!!");
				}
				
				colmProId=StringUtil.toCamelNotation(colmId, false);
				colmComment = colmMap.get("colmNm");
				isPk=colmMap.get("pkYn")!=null && "Y".equals(colmMap.get("pkYn"));
				code.append("\t\t<").append(isPk ? "id" : "result").append(" property=\"").append(colmProId).append("\" column=\"").append(colmId).append("\" />");
				if(colmComment!=null) {
					code.append("<!-- ").append(colmComment).append(" -->\n");
				} else {
					code.append("\n");
				}
				
			}
			code.append("\t</resultMap>\n\n");
			code.append("\t<!-- 조회조건 -->\n");
			code.append("\t<sql id=\"select").append(classNm).append("Where\">\n");
			code.append("\t\t<where>\n");
			
			// 컬럼 목록
			for(Map<String,String> colmMap : colmList){
				isPk=colmMap.get("pkYn")!=null && "Y".equals(colmMap.get("pkYn"));
				colmId=colmMap.get("colmId");
				colmProId=StringUtil.toCamelNotation(colmId, false);
				srchTyp=colmMap.get("srchTyp");
				if(!isPk && (srchTyp==null || srchTyp.isEmpty())) continue;
				if(isPk || "eq".equals(srchTyp)) {
					code.append("\t\t\t<if test=\"").append(colmProId).append(" != null and ").append(colmProId).append(" !=''\"> AND T.").append(colmId).append(" = #{").append(colmProId).append(", jdbcType=").append(toMyBatisDataType(colmMap)).append("}</if>\n");
				}else if("like".equals(srchTyp)) {
					
				}
			}
			
			code.append("\t\t\t<if test=\"whereSqllet != null and whereSqllet != ''\"> ${whereSqllet}</if>\n");
			code.append("\t\t</where>\n");
			code.append("\t</sql>\n\n");
			
			code.append("\t<!-- 목록조회 -->\n");
			
			code.append("\t<select id=\"select").append(classNm).append("\" resultMap=\"").append(mapId).append("Map\" parameterType=\"").append(classNm).append("Vo\" >\n");
			code.append("\t\t/* com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao.select").append(classNm).append(" */\n");
			if("mssql".equals(dbms) || "oracle".equals(dbms)) {
				code.append("\t\t<if test=\"pageNo != null and pageRowCnt != null\">\n");
				code.append("\t\tSELECT * FROM (\n");
				code.append("\t\t</if>\n");
			}
			
			code.append("\t\tSELECT\n");
			code.append("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">\n");
			
			if("mssql".equals(dbms) || "oracle".equals(dbms)) {
				
				code.append("\t\t\t<if test=\"pageNo != null and pageRowCnt != null\">\n");
				
				if("mssql".equals(dbms)) {
					code.append("\t\t\tTOP (#{pageNo, jdbcType=NUMERIC} * #{pageRowCnt, jdbcType=NUMERIC}) ROW_NUMBER() OVER (ORDER BY <if test=\"orderBy != null\">${orderBy}</if><if test=\"orderBy == null\">");
					
					if(pkMapList.containsKey(tableId)) {
						pkIdx=0;
						pkIdList=pkMapList.get(tableId);
						for(String pkId : pkIdList) {
							if(pkIdx>0) code.append(", ");
							code.append("N.").append(pkId).append(" ASC");
							pkIdx++;
						}
					}else {
						code.append("N.SORT_ORDR ASC");
					}
					code.append("</if>) AS RNUM, N.* FROM (SELECT\n");
				}else if("oracle".equals(dbms)) {
					code.append("\t\t\t* FROM (SELECT N.*, ROWNUM RNUM FROM (SELECT\n");
				}
				
				code.append("\t\t\t</if>\n");
				
			}
			
			// 컬럼 목록
			for(Map<String,String> colmMap : colmList){
				colmId=colmMap.get("colmId");
				code.append("\t\t\tT.").append(colmId).append(",\n");
			}
			code.append("\t\t</trim>\n");
			
			code.append("\t\tFROM ").append(tableId).append(" T\n");
			code.append("\t\t<include refid=\"select").append(classNm).append("Where\"/>\n");
			
			if("mssql".equals(dbms)) code.append("\t\t<if test=\"pageNo == null or pageRowCnt == null\">\n");
			if("mssql".equals(dbms)) code.append("\t");
			code.append("\t\t<if test=\"orderBy != null\">ORDER BY ${orderBy}</if>\n");
			if("mssql".equals(dbms)) code.append("\t");
			code.append("\t\t<if test=\"orderBy == null\">ORDER BY T.SORT_ORDR ASC</if>\n");
				
			if("mssql".equals(dbms)) code.append("\t\t</if>\n");
			
			code.append("\t\t<if test=\"pageNo != null and pageRowCnt != null\">\n");
			if("mssql".equals(dbms)) {
				code.append("\t\t<![CDATA[) N ) M WHERE RNUM > (#{pageNo, jdbcType=NUMERIC} - 1) * #{pageRowCnt, jdbcType=NUMERIC} AND RNUM <= #{pageNo, jdbcType=NUMERIC} * #{pageRowCnt, jdbcType=NUMERIC}]]>\n");
			}else if("oracle".equals(dbms)) {
				code.append("\t\t<![CDATA[) N WHERE ROWNUM <= #{pageNo, jdbcType=NUMERIC} * #{pageRowCnt, jdbcType=NUMERIC}) WHERE RNUM > (#{pageNo, jdbcType=NUMERIC} - 1) * #{pageRowCnt, jdbcType=NUMERIC}]]> ) M\n");
			}else if("mysql".equals(dbms)) {
				code.append("\t\tLIMIT #{pageStrt, jdbcType=NUMERIC}, #{pageRowCnt, jdbcType=NUMERIC}\n");
			}
			
			code.append("\t\t</if>\n");
			code.append("\t</select>\n\n");
			
			
			code.append("\t<!-- 목록조회 건수-->\n");
			code.append("\t<select id=\"count").append(classNm).append("D\" resultType=\"Integer\" parameterType=\"").append(classNm).append("Vo\" >\n");
			code.append("\t\t/* com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao.count").append(classNm).append(" */\n");
			code.append("\t\tSELECT COUNT(*) CNT\n");
			code.append("\t\tFROM WE_CD_D T\n");
			code.append("\t\t<include refid=\"select").append(classNm).append("Where\"/>\n");
			code.append("\t</select>\n\n");
			
			
			code.append("\t<!-- 등록 저장 -->\n");
			code.append("\t<insert id=\"insert").append(classNm).append("\" parameterType=\"").append(classNm).append("Vo\" >\n");
			
			code.append("\t\t/* com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao.insert").append(classNm).append(" */\n");
			code.append("\t\tINSERT INTO ").append(tableId).append("\n");
			
			code.append("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">/\n");
			
			// 컬럼 목록
			for(Map<String,String> colmMap : colmList){
				colmId=colmMap.get("colmId");
				code.append("\t\t\t").append(colmId).append(",\n");
			}
			code.append("\t\t</trim>\n");
			
			code.append("\t\t<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\" >\n");
			
			// 컬럼 목록
			for(Map<String,String> colmMap : colmList){
				colmId=colmMap.get("colmId");
				colmProId=StringUtil.toCamelNotation(colmId, false);
				if(colmProId.endsWith("Uid")) { // UID
					code.append("\t\t\t<if test=\"").append(colmProId).append(" != null\">#{").append(colmProId).append(", jdbcType=VARCHAR},</if><if test=\"").append(colmProId).append(" == null\">NULL,</if>\n");
				}else if(colmProId.endsWith("Dt")) { // 날짜
					
					code.append("\t\t\t<if test=\"").append(colmProId).append(" == 'sysdate'\">");
					code.append(toDbDateType(dbms));
					code.append(",</if><if test=\"").append(colmProId).append(" == null or ").append(colmProId).append(" == ''\">NULL,</if>\n");
					code.append("\t\t\t<if test=\"").append(colmProId).append(" != null and ").append(colmProId).append(" != '' and ").append(colmProId).append(" != 'sysdate'\">");
					
					if("mssql".equals(dbms)) code.append("CONVERT(DATETIME, #{").append(colmProId).append(", jdbcType=VARCHAR}, 120)");
					else if("oracle".equals(dbms)) code.append("TO_DATE(#{").append(colmProId).append(", jdbcType=VARCHAR},'YYYY-MM-DD HH24:MI:SS')");
					else if("mysql".equals(dbms)) code.append("STR_TO_DATE(#{").append(colmProId).append(", jdbcType=VARCHAR}, '%Y-%m-%d %H:%i:%s')");
					
					code.append(",</if>\n");
				}else {
					code.append("\t\t\t#{").append(colmProId).append(", jdbcType=").append(toMyBatisDataType(colmMap)).append("},\n");
				}
				
				
			}
			code.append("\t\t</trim>\n");			
			code.append("\t</insert>\n\n");
			
			
			code.append("\t<!-- 수정 -->\n");
			code.append("\t<update id=\"update").append(classNm).append("\" parameterType=\"").append(classNm).append("Vo\" >\n");
			code.append("\t\t/* com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao.update").append(classNm).append(" */\n");
			
			code.append("\t\tUPDATE ").append(tableId).append("\n");
			
			code.append("\t\t<set>\n");
			
			// 컬럼 목록
			for(Map<String,String> colmMap : colmList){
				colmId=colmMap.get("colmId");
				colmProId=StringUtil.toCamelNotation(colmId, false);
				isPk=colmMap.get("pkYn")!=null && "Y".equals(colmMap.get("pkYn"));
				if(isPk) continue;
				code.append("\t\t\t<if test=\"").append(colmProId).append(" != null\"> ");
				if(colmProId.endsWith("Dt")) { // 날짜
					code.append("\n");
					code.append("\t\t\t\t<if test=\"").append(colmProId).append(" == 'sysdate'\"> ").append(colmId).append(" = ");
					code.append(toDbDateType(dbms));
					code.append(",</if>\n");
					code.append("\t\t\t\t<if test=\"").append(colmProId).append(" == ''\"> ").append(colmId).append(" = NULL,</if>\n");
					code.append("\t\t\t\t<if test=\"").append(colmProId).append(" != '' and ").append(colmProId).append(" != 'sysdate'\"> ").append(colmId).append(" = ");
					if("mssql".equals(dbms)) code.append("CONVERT(DATETIME, #{").append(colmProId).append(", jdbcType=VARCHAR}, 120)");
					else if("oracle".equals(dbms)) code.append("TO_DATE(#{").append(colmProId).append(", jdbcType=VARCHAR},'YYYY-MM-DD HH24:MI:SS')");
					else if("mysql".equals(dbms)) code.append("STR_TO_DATE(#{").append(colmProId).append(", jdbcType=VARCHAR}, '%Y-%m-%d %H:%i:%s')");
					code.append(",</if>\n");
					code.append("\t\t\t");
				}else {
					code.append(colmId).append(" = #{").append(colmProId).append(", jdbcType=").append(toMyBatisDataType(colmMap)).append("},");
					
				}
				code.append("</if>\n");
				
			}
			code.append("\t\t</set>\n");
			code.append("\t\t<where>\n");
			
			if(pkMapList.containsKey(tableId)) {
				pkIdList=pkMapList.get(tableId);
				for(String pkId : pkIdList) {
					pkColmId=StringUtil.toCamelNotation(pkId, false);
					code.append("\t\t\t<if test=\"").append(pkColmId).append(" != null and ").append(pkColmId).append(" !=''\"> AND ").append(pkId).append(" = #{").append(pkColmId).append(", jdbcType=VARCHAR}</if>\n");
				}
			}
			
			code.append("\t\t</where>\n");
			code.append("\t</update>\n\n");
			
			
			code.append("\t<!-- 삭제 -->\n");
			code.append("\t<delete id=\"delete").append(classNm).append("\" parameterType=\"").append(classNm).append("Vo\" >\n");
			code.append("\t\t/* com.innobiz.orange.web.").append(mdNm).append(".dao.").append(classNm).append("Dao.delete").append(classNm).append(" */\n");
			code.append("\t\tDELETE FROM ").append(tableId).append("\n");
			code.append("\t\t<where>\n");
			if(pkMapList.containsKey(tableId)) {
				pkIdList=pkMapList.get(tableId);
				for(String pkId : pkIdList) {
					pkColmId=StringUtil.toCamelNotation(pkId, false);
					code.append("\t\t\t<if test=\"").append(pkColmId).append(" != null and ").append(pkColmId).append(" !=''\"> AND ").append(pkId).append(" = #{").append(pkColmId).append(", jdbcType=VARCHAR}</if>\n");
				}
			}
			code.append("\t\t</where>\n");
			code.append("\t</delete>\n\n");
			
			code.append("</mapper>");
			
			
			codeMap=new HashMap<String,String>();
			codeMap.put("xmlNm", classNm);
			codeMap.put("xmlString", code.toString());
			codeList.add(codeMap);
		}
		return codeList;
	}
	
	/** DBMS 날짜 데이터 타입 */
	private String toDbDateType(String dbms) {
		if("mssql".equals(dbms)) return "GETDATE()";
		else if("oracle".equals(dbms)) return "SYSDATE";
		else if("mysql".equals(dbms)) return "NOW()";
		
		return "";
		
	}
	
	/** MyBatis DBMS 데이터 타입 */
	private String toMyBatisDataType(Map<String,String> colmMap) {
		String dataTyp = colmMap.get("dataTyp");
		
		if(dataTyp.startsWith("CHAR")) return "CHAR";
		else return "VARCHAR";
		
		
	}
	
}
