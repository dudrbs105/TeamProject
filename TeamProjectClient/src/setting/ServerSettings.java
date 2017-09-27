package setting;

public enum ServerSettings {
    DB_SEVER_IP("70.12.115.52"),
    SERVER_IP("70.12.115.57"),

    DB_ID("test"),
    DB_PW("sds1501"),
        
    DB_SERVER_PORT(1521),
    MSG_SERVER_PORT(7777),
    IMG_UPLOAD_SERVER_PORT(8888),
    IMG_LOAD_SERVER_PORT(9999),
    REMOTE_SERVER_PORT(6666);
    
    private String str;
    private int port;
    
    private ServerSettings(String str) {
        this.str = str;
    }
    private ServerSettings(int port) {
        this.port = port;
    }
    
    public int getInteger(){
        return port;
    }
    public String getString() {
        return str;
    }
    
}