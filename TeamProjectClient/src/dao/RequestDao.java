package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;
import model.FriendRequest;


// method list -----
//      1. boolean requestFriend(String toId, String fromId)    : 친구 추가 요청
//      2. List<FriendRequest> selectRequestList(String myId)   : 친구 요청 목록 리스트 조회
//      3. boolean submitRequest(String otherId, String myId)   : 친구 추가 승인
//      4. boolean removeRequest(String otherId, String myId)   : 친구 추가 거부
public class RequestDao {
    private static RequestDao instance = null;
    public static RequestDao getInstance() {
        if(instance == null) {
            instance = new RequestDao();
        }
        
        return instance;
    }
    
    private Connection con = null;
    
    public boolean requestFriend(String toId, String fromId) {
        // 친구 추가 요청
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "INSERT INTO friendrequests(to_id, from_id, request_date) VALUES(?, ?, to_char(sysdate, 'YYYY/MM/DD HH24:MI:SS'))";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, toId);
            pstmt.setString(2, fromId);

            int result = pstmt.executeUpdate();
            if (result != 0) 
                return true;
        } catch(SQLException e) {
            System.out.println("reqestFriend 에러");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        return false;
    }
    public List<FriendRequest> selectRequestList(String myId) {
        // 친구 요청 목록 리스트 조회
        ArrayList<FriendRequest> result = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "SELECT * FROM friendrequests WHERE to_id=? ORDER BY request_date ASC";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, myId);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
                String toId = rs.getString("to_id");
                String fromId = rs.getString("from_id");
                String date = rs.getString("request_date");
                FriendRequest request = new FriendRequest(fromId, toId, date);
                
                result.add(request);
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
    public boolean submitRequest(String otherId, String myId)  {
        // 친구 추가 승인
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "INSERT INTO friends(to_id, from_id) VALUES(?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, otherId);
            pstmt.setString(2, myId);

            int result1 = pstmt.executeUpdate();
            pstmt.close();
            
            String sql2 = "INSERT INTO friends(to_id, from_id) VALUES(?, ?)";
            pstmt = con.prepareStatement(sql2);
            pstmt.setString(1, myId);
            pstmt.setString(2, otherId);

            int result2 = pstmt.executeUpdate();
        
            if(result1 != 0 && result2 != 0) return true;
        } catch(SQLException e) {
            System.out.println("add Friends 에러");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }

        return false;
    }
    public boolean removeRequest(String otherId, String myId) {
        // 친구 추가 거부
        PreparedStatement pstmt = null;

        try {
            con = ConnectionProvider.getConnection();
            String sql = "DELETE FROM friendrequests WHERE from_id=? AND to_id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, otherId);
            pstmt.setString(2, myId);
            int result = pstmt.executeUpdate();

            if(result != -1)    return true;
        } catch(SQLException e) {
            System.out.println("removeRequest 오류");
            e.printStackTrace();
        } finally {
            JdbcUtil.close(pstmt);
            JdbcUtil.close(con);
        }
        
        return false;
    }
}
