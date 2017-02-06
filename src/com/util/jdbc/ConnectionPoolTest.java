package com.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolTest {
    public static void main(String[] args) throws SQLException{
        String sql = "select * from user";
        ConnectionPool pool = null;
        
        for (int i = 0; i < 2; i++) {
            pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3));
            }
            rs.close();
            stmt.close();
            pool.release(conn);
        }
        pool.closePool();
    }
}