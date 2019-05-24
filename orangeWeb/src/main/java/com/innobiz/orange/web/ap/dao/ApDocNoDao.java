package com.innobiz.orange.web.ap.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

/** 결재문서 일련번호 생성용 프로시저 호출 <br/>
 * 
<pre> 
[ 프로시저 내용]
--
--  문서 번호 생성 시퀀스
--
CREATE PROCEDURE [dbo].[SP_AP_DOC_SEQ] (
	@YY INT,
	@REC_LST_ID VARCHAR(30),
	@ORG_ID VARCHAR(30),
	@ORG_RESC_ID VARCHAR(30),
	@DOC_SEQ BIGINT OUTPUT
)
AS
BEGIN
	BEGIN TRAN SP_AP_DOC_SEQ_TRANS;
	
	UPDATE AP_DOC_NO_D SET DOC_SEQ = DOC_SEQ + 1 WHERE YY = @YY AND REC_LST_ID = @REC_LST_ID AND ORG_ID = @ORG_ID;
	
	DECLARE CUR_SP_AP_DOC_SEQ CURSOR FOR
		SELECT DOC_SEQ FROM AP_DOC_NO_D WHERE YY = @YY AND REC_LST_ID = @REC_LST_ID AND ORG_ID = @ORG_ID;

	OPEN CUR_SP_AP_DOC_SEQ;
	
	FETCH NEXT FROM CUR_SP_AP_DOC_SEQ INTO @DOC_SEQ;
	
	IF @@FETCH_STATUS <> 0
	BEGIN
		INSERT INTO AP_DOC_NO_D VALUES ( @YY, @REC_LST_ID, @ORG_ID, @ORG_RESC_ID, 0 );
		SET @DOC_SEQ = 0;
	END
	
	CLOSE CUR_SP_AP_DOC_SEQ;
	DEALLOCATE CUR_SP_AP_DOC_SEQ;
	
	COMMIT TRAN SP_AP_DOC_SEQ_TRANS;
END
</pre>
 * */
@Component
public class ApDocNoDao {
	
	/** DataSource */
	private DataSource dataSource;
	
	/** DataSource 세팅 */
	@Resource(name = "gwDataSource")
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	/** 다음 문서 시퀀스 조회 */
	public Long nextDocNo(String year, String recLstId, String orgId, String orgRescId) throws SQLException{
		Long val = 0L;
		for(int i=0; i<3; i++){
			try {
				val = recursiveCall(year, recLstId, orgId, orgRescId);
				if(val != null && val.longValue() != 0L) return val;
			} catch(SQLException ignore){
				try { Thread.sleep(20); } catch(Exception e){}
			}
		}
		return val;
	}
	
	/** 프로시저 호출 */
	public Long recursiveCall(String year, String recLstId, String orgId, String orgRescId) throws SQLException{
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = dataSource.getConnection();
			cstmt = conn.prepareCall("{ CALL SP_AP_DOC_SEQ(?, ?, ?, ?, ?) }");
			cstmt.setInt(1, Integer.parseInt(year));
			cstmt.setString(2, recLstId);
			cstmt.setString(3, orgId);
			cstmt.setString(4, orgRescId);
			cstmt.registerOutParameter(5, Types.BIGINT);
			cstmt.execute();
			return cstmt.getLong(5);
		} finally {
			if(cstmt!=null){
				try { cstmt.close(); } catch(Exception ignore){}
			}
			if(conn!=null){
				try { conn.close(); } catch(Exception ignore){}
			}
		}
	}
}
