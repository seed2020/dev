package com.innobiz.orange.web.cm.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import com.innobiz.orange.web.cm.utils.IdUtil;

/** MS-SQL 시퀀스용 DAO 
 * 
 * MS-SQL 서버에서 시퀀스를 지원하지 않기 때문에 [시퀀스용 테이블] 및 [시퀀스용 스토어드프로시저]를 만들고 시퀀스 기능을 구현함
 * MyBatis 에서 CallableStatement 호출 방법에 대한 기술문서가 부족하여 Old-Way 로 구현함
 * 
[[시퀀스용 테이블]]
-- DROP TABLE [dbo].[CM_SEQ_B]
CREATE TABLE [dbo].[CM_SEQ_B](
    [SEQ_ID]                      [varchar]     (30) NOT NULL,
    [SEQ_NO]                      [bigint]           NULL,
    CONSTRAINT [PK_CM_SEQ_B] PRIMARY KEY CLUSTERED (
    [SEQ_ID] ASC
) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

EXEC sp_addextendedproperty @name = N'Caption', @value = N'일련번호기본', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SEQ_B';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일련번호ID', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SEQ_B', @level2Type = N'Column', @Level2Name = 'SEQ_ID';
EXEC sp_addextendedproperty @name = N'Caption', @value = N'일련번호번호', @level0Type = N'Schema', @Level0Name = dbo, @level1Type = N'Table', @Level1Name = 'CM_SEQ_B', @level2Type = N'Column', @Level2Name = 'SEQ_NO';
GO

[[시퀀스용 프로시저]]
CREATE PROCEDURE [dbo].[SP_SEQ] (
	@SEQ_ID VARCHAR(30),
	@SEQ_NO BIGINT OUTPUT
)
AS
BEGIN
	BEGIN TRAN SP_SEQ_TRANS;
	
	UPDATE GW_POTL.dao.CM_SEQ_B SET SEQ_NO = SEQ_NO + 1 WHERE SEQ_ID = @SEQ_ID;
	
	DECLARE CUR_SP_SEQ CURSOR FOR
		SELECT SEQ_NO FROM GW_POTL.dao.CM_SEQ_B WHERE SEQ_ID = @SEQ_ID;

	OPEN CUR_SP_SEQ;
	
	FETCH NEXT FROM CUR_SP_SEQ INTO @SEQ_NO;
	
	IF @@FETCH_STATUS <> 0
	BEGIN
		SET @SEQ_NO = 0;
	END
	
	CLOSE CUR_SP_SEQ;
	DEALLOCATE CUR_SP_SEQ;
	
	COMMIT TRAN SP_SEQ_TRANS;
END

 * 
 * */
public class CommonSeqDao {

	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public Long nextVal(String tableName) throws SQLException{
		Connection conn = null;
		CallableStatement cstmt = null;
		try {
			conn = dataSource.getConnection();
			cstmt = conn.prepareCall("{ CALL SP_SEQ(?, ?) }");
			cstmt.setString(1, tableName);
			cstmt.registerOutParameter(2, Types.BIGINT);
			cstmt.execute();
			long returnValue = cstmt.getLong(2);
			if(returnValue==0){
				throw new SQLException("You should insert SEQUENCE-DATA first !\n"
						+"INSERT INTO CM_SEQ_B VALUES('"+tableName+"',0);");
			}
			return returnValue;
			
		} finally {
			if(cstmt!=null){
				try { cstmt.close(); } catch(Exception ignore){}
			}
			if(conn!=null){
				try { conn.close(); } catch(Exception ignore){}
			}
		}
	}
	
	public String createId(String seqName, char prefix, int length) throws SQLException {
		Long seq = nextVal(seqName);
		return IdUtil.createId(prefix, seq, length);
	}
}
