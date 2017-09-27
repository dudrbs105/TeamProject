import java.io.IOException;

import javax.swing.JOptionPane;

import server.img.ImgServer;
import server.img.ImgUploadServer;
import server.msg.MsgServer;

public class Server {
    public static void main(String[] args) throws IOException {
        String msgPort = JOptionPane.showInputDialog("MSG SERVER ��Ʈ �Է�(0~9999)");
        if(!msgPort.matches("[0-9]{1,4}")) {
            JOptionPane.showMessageDialog(null, "��ȿ�������� ��Ʈ ����");
            System.exit(0);
        }

        String imgUploadPort = JOptionPane.showInputDialog("IMG UPLOAD SERVER ��Ʈ �Է�(0~9999)");
        if(!imgUploadPort.matches("[0-9]{1,4}")) {
            JOptionPane.showMessageDialog(null, "��ȿ�������� ��Ʈ ����");
            System.exit(0);
        }
        
        String imgLoadPort = JOptionPane.showInputDialog("IMG LOAD SERVER ��Ʈ �Է�(0~9999)");
        if(!imgLoadPort.matches("[0-9]{1,4}")) {
            JOptionPane.showMessageDialog(null, "��ȿ�������� ��Ʈ ����");
            System.exit(0);
        }
        
        new MsgServer(Integer.parseInt(msgPort)).start();
        new ImgUploadServer(Integer.parseInt(imgUploadPort)).start();
        new ImgServer(Integer.parseInt(imgLoadPort)).start();
    }
}
