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
//  1.  boolean checkConnection()               : �������ִ��� ��ȸ
//  2.  List<String> selectConnectionList()     : ������  ����Ʈ ��ȸ

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
        // ���̵� �ߺ��˻�
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "SELECT * FROM connectionlist WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, checkId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                // �̹� �������ִ°��
                return false;
            }
        } catch(SQLException e) {
            System.out.println("checkConnection ����");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        // ���������������
        return true;
    }
    public List<String> selectConnectionList() {
        // ������ ����Ʈ ��ȸ
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
