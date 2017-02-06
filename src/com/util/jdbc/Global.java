package com.util.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Global {
	
	private static Logger log = Logger.getLogger(Global.class);  
	
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
	public static int execute(String sql) throws Exception {
		if(sql == null || sql.trim().equals("")) {
			log.info("sql指令不存在");
			return -1;
		}
		log.info("sql指令：\n"+sql);
		Statement stmt = null;
		Connection conn = null;
		int rs = -1;
		try {
			conn = DataBaseUtil.getConn();
			stmt = conn.createStatement();
			rs =stmt.executeUpdate(sql);
		} catch (RuntimeException e) {
			log.info("parameter is valid!");
			throw new Exception(e);
		} finally {
			DataBaseUtil.closeStmt(stmt);
			DataBaseUtil.closeConn(conn);
		}
		return rs;
	}

	/**
	 * @param sql：查询语句
	 * @return	ResultSet
	 */
	public List<Map<String, String>> getQueryList(String sql) throws Exception {
		
		if(sql == null || sql.trim().equals("")) {
			log.info("sql指令不存在");
			return null;
		}
		log.info("sql指令：\n"+sql);
		Statement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		List<Map<String, String>> queryList = null;
		try {
			conn = DataBaseUtil.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			queryList = localJndi.getQueryList(rs);
		} catch (RuntimeException e) {
			log.info("parameter is valid!");
			throw new Exception(e);
		} finally {
			DataBaseUtil.closeRs(rs);
			DataBaseUtil.closeStmt(stmt);
			DataBaseUtil.closeConn(conn);
		}
		return queryList;
	}
	/**
	 * @param sql：查询语句
	 * @return	int记录数
	 */
	public int getQueryNumber(String sql) throws Exception {
		
		int i = 0;
		if(sql == null || sql.trim().equals("")) {
			log.info("sql指令不存在");
			return -1;
		}
		log.info("sql指令：\n"+sql);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DataBaseUtil.getConn();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				i++;
			}
		} catch (RuntimeException e) {
			log.info("parameter is valid!");
			throw new Exception(e);
		} finally {
			DataBaseUtil.closeRs(rs);
			DataBaseUtil.closeStmt(stmt);
			DataBaseUtil.closeConn(conn);
		}
		return i;
	}
}
