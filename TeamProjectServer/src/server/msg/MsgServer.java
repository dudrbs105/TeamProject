package server.msg;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MsgServer extends Thread {
    private ArrayList<MsgServerThread> list;
    private Socket socket;
    private ServerSocket serverSocket = null;
    private boolean isStop = false;
    private int port;
    
    public MsgServer(int port)  {
        list = new ArrayList<MsgServerThread>();
        this.port = port;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            MsgServerThread mst = null;
            
            while(!isStop) {
                System.out.println("Server ready....");
                socket = serverSocket.accept();
                System.out.println("msg ¼ö¶ô");
                
                mst = new MsgServerThread(this);
                list.add(mst);
                new Thread(mst).start();
            }
            serverSocket.close();
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public ArrayList<MsgServerThread> getList() {
        return list;
    }
    public Socket getSocket() {
        return socket;
    }
}
