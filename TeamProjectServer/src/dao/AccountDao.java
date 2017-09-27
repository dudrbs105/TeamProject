package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;

// method list -----
//      1. boolean updateImgPath(String id)                     : �̹��� ��� ������Ʈ
//      2. String readImgPath(String id)                        : �̹��� ��� �о����
public class AccountDao {
    private static AccountDao instance = null;
    
    private Connection con = null;
   
    public static AccountDao getInstance() {
        if(instance == null) {
            instance = new AccountDao();
        }
        
        return instance;
    }
    public boolean updateImgPath(String id, String imgPath) {
        // �̹��� ��� ������Ʈ
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "UPDATE accounts SET img_path=? WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, imgPath);
            pstmt.setString(2, id);
            int result = pstmt.executeUpdate();
            
            if(result != 0)
                return true;
        } catch(SQLException e) {
            System.out.println("checkId ����");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        // ���̵� �������
        return false;
    }
    public String readImgPath(String id) {
        // �̹��� ��� �ҷ�����
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT img_path FROM accounts WHERE id=?";
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
}
