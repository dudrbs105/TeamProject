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
//      1. boolean checkId(String checkId)                      : 아이디 중복 확인 
//      2. boolean insertInfo(AccountVO info)                   : 회원가입
//      3. void updatePw(String Pw1, String Id)                 : 패스워드 변경
//      4. int selectFindPw(String selId, String selBirth)      : 비밀번호 찾기위한 생년월일 비교
//      5. int searchAccount(String selId, String selPw)        : 회원 정보 검색 --> 로그인용
//      6. AccountVO selectAccount(String selId)                : 회원 검색
//      7. List<AccountVO> selectAccountList(String myId)       : 친구  리스트 조회


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
        // 아이디 중복검사
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "SELECT * FROM accounts WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, checkId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                // 아이디가 존재할경우
                return true;
            }
        } catch(SQLException e) {
            System.out.println("checkId 에러");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        // 아이디가 없을경우
        return false;
    }
    public boolean insertInfo(AccountVO info) {
        // 회원가입
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
            System.out.println("insertinfo에러");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return false;
    }
    public int selectFindPw(String selId, String selBirth) {
        // 비밀번호 찾기위한 생년월일 비교
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
        // 패스워드 변경
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();

            String sql = "UPDATE accounts SET pw=? WHERE id IS NOT NULL AND id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Pw1);
            pstmt.setString(2, Id); // findPw 에서 입력된 아이디
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
    }// end updatePw Method
    public int searchAccount(String selId, String selPw) {
        // 회원 정보 검색 --> 로그인용
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();

            String sql = "SELECT id, pw" + " FROM accounts WHERE id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, selId);
            rs = pstmt.executeQuery();
            // 로그인 성공 0
            // 아이디가 없거나 틀립니다 1
            // 비밀번호가 틀립니다 2
            if(rs.next()) {
                String selectedPw = rs.getString("pw");
                if(selPw.equals(selectedPw) == true) {
                    // 비밀번호 같을경우
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
        // 회원 검색
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
        // 친구  리스트 조회
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
