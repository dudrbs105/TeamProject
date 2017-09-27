package jdbc;

public class Setting {
//    public enum ServerSettings {
//        DB_SEVER_IP("127.0.0.1"),
//        CMD_SERVER_IP("127.0.0.1"),
//        IMG_UPLOAD_SERVER_IP("127.0.0.1"),
//        IMG_LOAD_SERVER_IP("127.0.0.1"),
//        
//        DB_SERVER_PORT(1521),
//        CMD_SERVER_PORT(7777),
//        IMG_UPLOAD_SERVER_PORT(8888),
//        IMG_LOAD_SERVER_PORT(9999),
//        REMOTE_SERVER_PROT(6666),
//        
//        DB_ID("lim8143"),
//        DB_PW("1234");
//        
//        private String str;
//        private int port;
//        
//        private ServerSettings(String str) {
//            this.str = str;
//        }
//        private ServerSettings(int port) {
//            this.port = port;
//        }
//        
//        public int getPort(){
//            return port;
//        }
//        public String getIp(){
//            return str;
//        }
//        public String getUserInfo() {
//            return str;
//        }
//    }
//    
    
    private static Setting instance = null;
    public static Setting getSetting() {
        if(instance == null) {
            instance = new Setting();
        }
        return instance;
    }
    
    private Setting() {}

}
