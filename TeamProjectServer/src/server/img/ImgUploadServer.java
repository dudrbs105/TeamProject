package server.img;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import dao.AccountDao;
import dao.ConnectionDao;

public class ImgUploadServer extends Thread {
    private Socket socket;
    private ServerSocket serverSocket = null;
    private boolean isStop = false;
    private int port;
    
    public ImgUploadServer(int port){
        this.port = port;
    }
    @Override
    public void run() {
        ObjectInputStream ois = null;
        
        try {
            serverSocket = new ServerSocket(port);
            
            while(!isStop) {
                System.out.println("Img Upload Server ready....");
                socket = serverSocket.accept();
                System.out.println("img Upload Server ¼ö¶ô : " + socket.getInetAddress().getHostAddress());
                
                ois = new ObjectInputStream(socket.getInputStream());
                
                String id = ConnectionDao.getInstance().searchId(socket.getInetAddress().getHostAddress());
                ImageIcon imageIcon = (ImageIcon)ois.readObject();
                BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(imageIcon.getImage(), 0, 0, null);
                g.dispose();
                
                String path = "profile//"+id+".png";
                ImageIO.write(bufferedImage, "png", new File(path));
                AccountDao.getInstance().updateImgPath(id, path);
            }
            
            serverSocket.close();
            
        } catch(IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
