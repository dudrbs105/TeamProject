package thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

public class ImgUploadThread extends Thread {
    private Socket imgSocket;
    private ImageIcon profile;

    public ImgUploadThread(Socket imgSocket, ImageIcon profile) {
        this.imgSocket = imgSocket;
        this.profile = profile;
    }

    public void run() {
        ObjectOutputStream imgOos = null;
        
        try {
            imgOos = new ObjectOutputStream(imgSocket.getOutputStream());
            imgOos.writeObject(profile);
            imgOos.reset();
            
            imgOos.close();
        } catch(IOException e) {
            e.printStackTrace();
        } 
    }
}
