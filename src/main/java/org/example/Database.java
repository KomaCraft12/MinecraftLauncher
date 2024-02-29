package org.example;

import java.sql.*;

public class Database {
    private Connection conn;

    public Database() {
        conn = null;
    }

    public void alternativeConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://komacloud.asuscomm.com/mclauncher", "jano", "Katica.bogar2002");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://komacloud.synology.me/mclauncher", "jano", "Katica.bogar2002");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            alternativeConnect();
        }
    }

    private void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public ResultSet getTable(String sql) {
        connect();
        ResultSet result = null;
        try {
            Statement stmt = conn.createStatement();
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public int insert(String sql, Object[] data) {
        int lastId = -1;
        connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < data.length; i++) {
                pstmt.setObject(i + 1, data[i]);
            }
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                lastId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        disconnect();
        return lastId;
    }

    public boolean update(String sql, Object[] data) {
        connect();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < data.length; i++) {
                pstmt.setObject(i + 1, data[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        disconnect();
        return true;
    }
}


