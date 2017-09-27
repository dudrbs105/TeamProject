package server.img;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import dao.AccountDao;

public class ImgServerThread implements Runnable {
    private Socket socket;
    private ImgServer ms;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ImgServerThread(ImgServer ms) {
        this.ms = ms;
    }

    public synchronized void run() {
        boolean isStop = false;
        try {
            socket = ms.getSocket();
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            String message = null;
            while(!isStop) {
                message = (String)ois.readObject();

                if(message.startsWith("[PROFILE]")) {
                    String id = message.substring("[PROFILE]".length());
                    
                    String imgPath = AccountDao.getInstance().readImgPath(id);
                    Image image = ImageIO.read(new File(imgPath));
                    ImageIcon imageIcon = new ImageIcon(image);
                    oos.writeObject(imageIcon);
                    oos.reset();
                }
            }
            System.out.println("list size : " + ms.getList().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(socket.getInetAddress() + "비정상적으로 종료하셨습니다");
            System.out.println("list size : " + ms.getList().size());
        } finally {
            ms.getList().remove(this);

        }
    }
}
