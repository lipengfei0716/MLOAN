package com.util.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataBaseUtil {

    private static DataSource ds;
    
    static {
        try {
            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jndi-web");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
	protected synchronized static Connection getConn(){
        
        try {
            return ds.getConnection() != null ? ds.getConnection() : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
	 protected static void closeRs(ResultSet rs) {
	        try{
	            if(rs != null) {
	            	rs.close();
	            	rs = null;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
    protected static void closeStmt(Statement stmt) {
        try{
            if(stmt != null) {
            	stmt.close();
            	stmt = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	 protected static void closeConn(Connection conn) {
	        try{
	            if(conn != null) {
	                conn.close();
	                conn = null;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}