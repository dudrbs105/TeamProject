package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;
import model.AccountVO;

// method list -----
//      1. boolean checkId(String checkId)                      : ���̵� �ߺ� Ȯ�� 
//      2. boolean insertInfo(AccountVO info)                   : ȸ������
//      3. void updatePw(String Pw1, String Id)                 : �н����� ����
//      4. int selectFindPw(String selId, String selBirth)      : ��й�ȣ ã������ ������� ��
//      5. int searchAccount(String selId, String selPw)        : ȸ�� ���� �˻� --> �α��ο�
//      6. AccountVO selectAccount(String selId)                : ȸ�� �˻�
//      7. List<AccountVO> selectAccountList(String myId)       : ģ��  ����Ʈ ��ȸ


public class AccountDao {
    private static AccountDao instance = null;
    
    private Connection con = null;
   
    public static AccountDao getInstance() {
        if(instance == null) {
            instance = new AccountDao();
        }
        
        return instance;
    }

    public boolean checkId(String checkId) {
        // ���̵� �ߺ��˻�
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "SELECT * FROM accounts WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, checkId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                // ���̵� �����Ұ��
                return true;
            }
        } catch(SQLException e) {
            System.out.println("checkId ����");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        // ���̵� �������
        return false;
    }
    public boolean insertInfo(AccountVO info) {
        // ȸ������
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            
            String sql = "INSERT INTO accounts(id,pw,nickname,birth) VALUES(?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, info.getId());
            pstmt.setString(2, info.getPw());
            pstmt.setString(3, info.getNickname());
            pstmt.setString(4, info.getBirth());

            int result = pstmt.executeUpdate();
            if (result != 0) 
                return true;

        } catch(SQLException e) {
            System.out.println("insertinfo����");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return false;
    }
    public int selectFindPw(String selId, String selBirth) {
        // ��й�ȣ ã������ ������� ��
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT * FROM accounts WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (selBirth.equals(rs.getString("birth")) == true) { 
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return 1;
    }// end selectFindPw Method
    
    public void updatePw(String Pw1, String Id) {
        // �н����� ����
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();

            String sql = "UPDATE accounts SET pw=? WHERE id IS NOT NULL AND id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Pw1);
            pstmt.setString(2, Id); // findPw ���� �Էµ� ���̵�
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }// end updatePw Method
    public int searchAccount(String selId, String selPw) {
        // ȸ�� ���� �˻� --> �α��ο�
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT id, pw" + " FROM accounts WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selId);
            rs = pstmt.executeQuery();
            // �α��� ���� 0
            // ���̵� ���ų� Ʋ���ϴ� 1
            // ��й�ȣ�� Ʋ���ϴ� 2
            if(rs.next()) {
                String selectedPw = rs.getString("pw");
                if(selPw.equals(selectedPw) == true) {
                    // ��й�ȣ �������
                    return 0;
                } else if(selPw.equals(selectedPw) == false) {
                    return 2;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return 1;
    }// end searchAccount Method
    
    public AccountVO selectAccount(String selId) {
        // ȸ�� �˻�
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT * FROM accounts WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selId);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                String id = rs.getString("id");
                String pw = rs.getString("pw");
                String nickname = rs.getString("nickname");
                String birth = rs.getString("birth");
                
                return new AccountVO(id, pw, nickname, birth);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return null;
    }// end selectAccount Method

    public List<AccountVO> selectAccountList(String myId) {
        // ģ��  ����Ʈ ��ȸ
        ArrayList<AccountVO> result = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            
            String sql = "SELECT * FROM friends WHERE from_id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, myId);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                String toId = rs.getString("to_id");
                
                String sql2 = "SELECT * FROM accounts WHERE id=?";
                pstmt = con.prepareStatement(sql2);
                pstmt.setString(1, toId);
                ResultSet subRs = pstmt.executeQuery();
                
                if(subRs.next()) {
                    String friendId = subRs.getString("id");
                    String friendPw = subRs.getString("pw");
                    String friendNick = subRs.getString("nickname");
                    String friendBirth = subRs.getString("birth");

                    AccountVO account = new AccountVO(friendId, friendPw, friendNick, friendBirth);
                    result.add(account);
                }
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
