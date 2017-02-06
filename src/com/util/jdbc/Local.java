package com.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class Local {
	
	private static LocalJndi localJndi = new LocalJndi();
	/**
	 * 该语句必须是一个 SQL INSERT、UPDATE 或 DELETE 语句
	 * @param sql
	 * @param paramList：参数，与SQL语句中的占位符一一对应
	 * @return
	 * @throws Exception
	   	DbUtils dbUtils = new DbUtils();
		String insertSql = "insert into user(username,email) values(?,?)";
		List<Object> paramList = new ArrayList<Object>();
		paramList.add("李四");
		paramList.add("李四@163.com");
		int i = dbUtils.execute(insertSql, paramList);
	 */
	@SuppressWarnings("static-access")
	public static int execute(String sql, List<Object> paramList) throws Exception {
		if(sql == null || sql.trim().equals("")) {
			System.out.println("sql指令不存在");
		}
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = localJndi.getConnection();
			pstmt = localJndi.getPreparedStatement(conn, sql);
			localJndi.setPreparedStatementParam(pstmt, paramList);
			if(pstmt == null) {
				return -1;
			}
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			//logger.info(e.getMessage());
			throw new Exception(e);
		} finally {
			localJndi.closeStatement(pstmt);
			localJndi.closeConn(conn);
		}

		return result;
	}
	/**
	 * 将查询数据库获得的结果集转换为Map对象
	 * @param sql：查询语句
	 * @param paramList：参数
	 * @return
	 */
	@SuppressWarnings("static-access")
	public List<Map<String, String>> getQueryList(String sql, List<Object> paramList) throws Exception {
		if(sql == null || sql.trim().equals("")) {
			//logger.info("parameter is valid!");
			return null;
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, String>> queryList = null;
		try {
			conn = localJndi.getConnection();
			pstmt = LocalJndi.getPreparedStatement(conn, sql);
			localJndi.setPreparedStatementParam(pstmt, paramList);
			if(pstmt == null) {
				return null;
			}
			rs = localJndi.getResultSet(pstmt);
			queryList = localJndi.getQueryList(rs);
		} catch (RuntimeException e) {
			System.out.println("parameter is valid!");
			throw new Exception(e);
		} finally {
			localJndi.closeResultSet(rs);
			localJndi.closeStatement(pstmt);
			localJndi.closeConn(conn);
		}
		return queryList;
	}
}
