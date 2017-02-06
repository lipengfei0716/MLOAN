package com.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.util.base.ActionBase;

//import org.apache.log4j.Logger;

/**
 * 数据库操作辅助类
 */
public class LocalJndi extends ActionBase{
	
	private static String jndiName = "java:/comp/env/jdbc/mloan";

	public void setJndiName(String jndiName) {
		LocalJndi.jndiName = jndiName;
	}
	
	static void setPreparedStatementParam(PreparedStatement pstmt, List<Object> paramList) throws Exception {
		if(pstmt == null || paramList == null || paramList.isEmpty()) {
			return;
		}
		DateFormat df = DateFormat.getDateTimeInstance();
		for (int i = 0; i < paramList.size(); i++) {
			if(paramList.get(i) instanceof Integer) {
				int paramValue = ((Integer)paramList.get(i)).intValue();
				pstmt.setInt(i+1, paramValue);
			} else if(paramList.get(i) instanceof Float) {
				float paramValue = ((Float)paramList.get(i)).floatValue();
				pstmt.setFloat(i+1, paramValue);
			} else if(paramList.get(i) instanceof Double) {
				double paramValue = ((Double)paramList.get(i)).doubleValue();
				pstmt.setDouble(i+1, paramValue);
			} else if(paramList.get(i) instanceof Date) {
				pstmt.setString(i+1, df.format((Date)paramList.get(i)));
			} else if(paramList.get(i) instanceof Long) {
				long paramValue = ((Long)paramList.get(i)).longValue();
				pstmt.setLong(i+1, paramValue);
			} else if(paramList.get(i) instanceof String) {
				pstmt.setString(i+1, (String)paramList.get(i));
			}
		}
		return;
	}
	
	/**
	 * 获得数据库连接
	 * @return
	 * @throws Exception
	 */
	static Connection getConnection() throws Exception {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource) cxt.lookup(jndiName);
		if ( ds == null ) {
		   throw new Exception("Data source not found!");
		}
		
		return ds.getConnection();
	}
	
	static PreparedStatement getPreparedStatement(Connection conn, String sql) throws Exception {
		if(conn == null || sql == null || sql.trim().equals("")) {
			return null;
		}
		PreparedStatement pstmt = conn.prepareStatement(sql.trim());
		return pstmt;
	}
	
	/**
	 * 获得数据库查询结果集
	 * @param pstmt
	 * @return
	 * @throws Exception
	 */
	ResultSet getResultSet(PreparedStatement pstmt) throws Exception {
		if(pstmt == null) {
			return null;
		}
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 */
	static void closeConn(Connection conn) {
		if(conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			//logger.info(e.getMessage());
		}
	}
	
	/**
	 * 关闭
	 * @param stmt
	 */
	static void closeStatement(Statement stmt) {
		if(stmt == null) {
			return;
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			//logger.info(e.getMessage());
		}
	}
	
	/**
	 * 关闭
	 * @param rs
	 */
	void closeResultSet(ResultSet rs) {
		if(rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			//logger.info(e.getMessage());
		}
	}
	
	/**
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	List<Map<String, String>> getQueryList(ResultSet rs) throws Exception {
		if(rs == null) {
			return null;
		}
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int columnCount = rsMetaData.getColumnCount();
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		while (rs.next()) {
			Map<String, String> dataMap = new HashMap<String, String>();
			for (int i = 0; i < columnCount; i++) {
				Object obj = rs.getObject(i+1);
				if(obj!=null)
					dataMap.put(rsMetaData.getColumnName(i+1).toLowerCase(), obj.toString());
				else
					dataMap.put(rsMetaData.getColumnName(i+1).toLowerCase(), null);
			}
			dataList.add(dataMap);
		}
		return dataList;
	}
}
