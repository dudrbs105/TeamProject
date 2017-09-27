package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;


//method list -----
//  1.  boolean loginConnection(String id)                  : ������ ����Ʈ �߰�
//  2.  boolean exitConnection(String id)                   : ������ ����Ʈ���� ������
//  3.  String searchIpAddress(String id)                   : ������ ã��
//  4.  String searchId(String ip)                          : ���̵� ã��
public class ConnectionDao {
    private static ConnectionDao instance = null;
    public static ConnectionDao getInstance() {
        if(instance == null) {
            instance = new ConnectionDao();
        }
        return instance;
    }

    private Connection con = null;

    public boolean loginConnection(String id, String ip)  {
        // ������ ����Ʈ �߰�
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "INSERT INTO connectionlist(id, login_date, ip_address) VALUES(?, to_char(sysdate, 'YYYY/MM/DD HH24:MI:SS'), ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, ip);
            int result = pstmt.executeUpdate();
            
         
            if(result != 0) return true;
        } catch(SQLException e) {
            System.out.println("login list add ����");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        return false;
    }
    public boolean exitConnection(String id) {
        // ����
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "DELETE FROM connectionlist WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            int result = pstmt.executeUpdate();

            if(result != -1)    return true;
        } catch(SQLException e) {
            System.out.println("remove connection list  ����");
            e.printStackTrace();
        } finally {
            if(pstmt != null)   JdbcUtil.close(pstmt);
            if(con != null)     JdbcUtil.close(con);
        }
        
        return false;
    }
    public String searchIpAddress(String id) {
        // ������ ã��
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT ip_address FROM connectionlist WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getString(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return null;
    }
    public String searchId(String ip) {
        // ���̵� ã��
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT id FROM connectionlist WHERE ip_address=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, ip);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getString(1);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return null;
    }
    
}
