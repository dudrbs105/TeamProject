package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;


//method list -----
//  1.  boolean checkConnection()               : 접속해있는지 조회
//  2.  List<String> selectConnectionList()     : 접속자  리스트 조회

public class ConnectionDao {
    private static ConnectionDao instance = null;
    public static ConnectionDao getInstance() {
        if(instance == null) {
            instance = new ConnectionDao();
        }
        return instance;
    }

    private Connection con = null;

    public boolean checkConnection(String checkId) {
        // 아이디 중복검사
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "SELECT * FROM connectionlist WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, checkId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                // 이미 접속해있는경우
                return false;
            }
        } catch(SQLException e) {
            System.out.println("checkConnection 에러");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        // 접속하지않은경우
        return true;
    }
    public List<String> selectConnectionList() {
        // 접속자 리스트 조회
        ArrayList<String> result = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "SELECT * FROM connectionlist";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                String connectedId = rs.getString("id");
                result.add(connectedId);
            }
        } catch(SQLException e) {
            System.out.println("selectRequest Failed");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        return result;
    }
}
