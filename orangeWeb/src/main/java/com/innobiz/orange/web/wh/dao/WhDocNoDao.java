package com.innobiz.orange.web.wh.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Component;

/** 문서 ID 생성용 프로시저 호출 <br/>
 * 
<pre> 
[ 프로시저 내용]
--
--  문서 번호 생성 시퀀스
--
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

--
--  문서 번호 생성 시퀀스
--
CREATE PROCEDURE [dbo].[SP_WH_DOC_ID] (
	@STOR_ID VARCHAR(30),	
	@YY INT,
	@ORG_ID VARCHAR(30),	
	@DOC_ID BIGINT OUTPUT
)
AS
BEGIN
	BEGIN TRAN SP_WH_DOC_ID_TRANS;
	
	UPDATE WH_DOC_NO_D SET DOC_ID = DOC_ID + 1 WHERE STOR_ID = @STOR_ID AND YY = @YY AND ORG_ID = @ORG_ID;
	
	DECLARE CUR_SP_WH_DOC_ID CURSOR FOR
		SELECT DOC_ID FROM WH_DOC_NO_D WHERE STOR_ID = @STOR_ID AND YY = @YY AND ORG_ID = @ORG_ID;

	OPEN CUR_SP_WH_DOC_ID;
	
	FETCH NEXT FROM CUR_SP_WH_DOC_ID INTO @DOC_ID;
	
	IF @@FETCH_STATUS <> 0
	BEGIN
		INSERT INTO WH_DOC_NO_D VALUES ( @STOR_ID, @YY, @ORG_ID, 0 );
		SET @DOC_ID = 0;
	END
	
	CLOSE CUR_SP_WH_DOC_ID;
	DEALLOCATE CUR_SP_WH_DOC_ID;
	
	COMMIT TRAN SP_WH_DOC_ID_TRANS;
END

GO


</pre>
 * */
@Component
public class WhDocNoDao {
	
	/** DataSource */
	private DataSource dataSource;
	
	/** DataSource 세팅 */
	@Resource(name = "gwDataSource")
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	/** 다음 문서 시퀀스 조회 */
	public Long nextDocNo(String year, String orgId) throws SQLException{
		Long val = 0L;
		for(int i=0; i<3; i++){
			try {
				val = recursiveCall(year, orgId);
				if(val != null && val.longValue() != 0L) return val;
			} catch(SQLException ignore){
				try { Thread.sleep(20); } catch(Exception e){}
			}
		}
		return val;
	}
	
	/** 프로시저 호출 */
	public Long recursiveCall(String year, String orgId) throws SQLException{
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = dataSource.getConnection();
			cstmt = conn.prepareCall("{ CALL SP_WH_DOC_SEQ(?, ?, ?) }");
			cstmt.setInt(1, Integer.parseInt(year));
			cstmt.setString(2, orgId);
			cstmt.registerOutParameter(3, Types.BIGINT);
			cstmt.execute();
			return cstmt.getLong(3);
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
