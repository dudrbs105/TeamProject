package server.img;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ImgServer extends Thread {
    private ArrayList<ImgServerThread> list;
    private Socket socket;
    private ServerSocket serverSocket = null;
    private boolean isStop = false;
    private int port; 
    public ImgServer(int port){
        list = new ArrayList<ImgServerThread>();
        this.port = port;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            ImgServerThread mst = null;
            while(!isStop) {
                System.out.println("Img Server ready....");
                socket = serverSocket.accept();
                System.out.println("img Server ¼ö¶ô : " + socket.getInetAddress().getHostAddress());
                
                mst = new ImgServerThread(this);
                list.add(mst);
                new Thread(mst).start();
            }
            serverSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<ImgServerThread> getList() {
        return list;
    }
    public Socket getSocket() {
        return socket;
    }
}
