package model;

public class FriendRequest {
    private String fromId;
    private String toId;
    private String date;
    
    public FriendRequest(String fromId, String toId, String date) {
        this.fromId = fromId;
        this.toId = toId;
        this.date = date;
    }
    
    public String getToId() {
        return toId;
    }
    public void setToId(String toId) {
        this.toId = toId;
    }
    public String getFromId() {
        return fromId;
    }
    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FriendRequest [fromId=" + fromId + ", toId=" + toId + ", date=" + date + "]";
    }

}
