package thread;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JOptionPane;

import frame.main.MainFrame;
import frame.remote.RemoteClientFrame;
import frame.remote.RemoteServerFrame;

public class MsgRcvThread extends Thread {
    private MainFrame mf;
    
    public MsgRcvThread(MainFrame mf) {
        this.mf = mf;
    }

    public void run() {
        String message = null;
        boolean isStop = false;
        RemoteServerFrame remoteServerFrame = null;
        
        while(!isStop) {
            try {
                message = (String)mf.getMsgOis().readObject();
            } catch (Exception e) {
                System.out.println("Ŭ���̾�Ʈ �޼��� ���� ����");
                e.printStackTrace();
                isStop = true;
            }

            if(message.startsWith("[CONNECT_REQUEST]")) {
                String[] msgArr = message.substring("[CONNECT_REQUEST]".length()).split("#");   // �޼�����
                                                                                                // 0: �����ϴ� ���̵�
                                                                                                // 1: ���� ���� ���̵�
                                                                                                // 2: ���� �ϴ� ip �ּ�
                int result = JOptionPane.showConfirmDialog(null, "( " + msgArr[0] +" )���� ���� ���� ��û �����Ͻðڽ��ϱ�?");
                
                String remoteRequestLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "( " + msgArr[0] +" )���� ���� ���� ��û";
                mf.getLogArea().setText(remoteRequestLog);
                
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle rectangle = new Rectangle(dim);
                String returnMsg = "[CONNECT_RESPONSE]"+ result + "#"+ msgArr[0] + "#" +
                                   rectangle.getWidth() + "#" + rectangle.getHeight();
                try {
                    mf.getMsgOos().writeObject(returnMsg);
                } catch(IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                if(result == 0) {
                    // �������� ��� --> Ŭ�� �ȴ�.
                    String remoteResponseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "( " + msgArr[0] + " ) �������� �����.";
                    mf.getLogArea().setText(remoteResponseLog);
                    
                    RemoteClientFrame.getInstance().setVisible(true);
                    RemoteClientFrame.getInstance().getStateLabel().setText("( " + msgArr[0] + " ) �����.");
                    RemoteClientFrame.getInstance().setOtherId(msgArr[0]);
                    
                    //Get default screen device
                    GraphicsEnvironment gEnv=GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice gDev=gEnv.getDefaultScreenDevice();
                    //Prepare Robot object
                    try {
                        RemoteClientFrame.getInstance().remoteStart(new Robot(gDev), rectangle, msgArr[2]);
                    } catch(AWTException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    String remoteResponseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "( " + msgArr[0] + " ) �������� ����.";
                    mf.getLogArea().setText(remoteResponseLog);
                }
            } else if(message.startsWith("[CONNECT_RESPONSE]")) {
                String[] msgArr = message.substring("[CONNECT_RESPONSE]".length()).split("#");  // �޼��� �з�
                                                                                                // 0: �����
                                                                                                // 1: ���� ���� ���̵�
                if(msgArr[0].equals("0")) {
                    // ���� �޾������ --> ������ �ȴ�.
                    String remoteResponseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "���� ���� ����.";
                    mf.getLogArea().setText(remoteResponseLog);
                    
                    Dimension di = new Dimension();
                    di.setSize(Double.parseDouble(msgArr[2]), Double.parseDouble(msgArr[3]));
                    remoteServerFrame = new RemoteServerFrame(mf.getMsgOos(), di);
                    try {
                        remoteServerFrame.refreshScreen();
                    } catch(IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    String remoteRequeseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "���� ���� ��������.";
                    mf.getLogArea().setText(remoteRequeseLog);
                    
                    JOptionPane.showMessageDialog(null, "���� ��û ��������.");
                } 
            } else if(message.startsWith("[DISCONNECT]")) {
                String remoteDisconnectLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "���� ���� ����.";
                mf.getLogArea().setText(remoteDisconnectLog);
                
                remoteServerFrame.setContinued(false);
            } else if(message.startsWith("[MOUSE_MOVE]")) {
                String[] pointArr = message.substring("[MOUSE_MOVE]".length()).split("#");
                int xPoint = Integer.parseInt(pointArr[0]);
                int yPoint = Integer.parseInt(pointArr[1]);
                RemoteClientFrame.getInstance().getRobot().mouseMove(xPoint, yPoint);
            } else if(message.startsWith("[MOUSE_RELEASE]")) {
                String strActionCommand = message.substring("[MOUSE_RELEASE]".length());
                int actionCommand = Integer.parseInt(strActionCommand);
                RemoteClientFrame.getInstance().getRobot().mouseRelease(actionCommand);
            } else if(message.startsWith("[MOUSE_PRESS]")) {
                String strActionCommand = message.substring("[MOUSE_PRESS]".length());
                int actionCommand = Integer.parseInt(strActionCommand);
                RemoteClientFrame.getInstance().getRobot().mousePress(actionCommand);
            } else if(message.startsWith("[KEY_PRESS]")) {
                String strActionCommand = message.substring("[KEY_PRESS]".length());
                int actionCommand = Integer.parseInt(strActionCommand);
                RemoteClientFrame.getInstance().getRobot().keyPress(actionCommand);
            } else if(message.startsWith("[KEY_RELEASE]")) {
                String strActionCommand = message.substring("[KEY_RELEASE]".length());
                int actionCommand = Integer.parseInt(strActionCommand);
                RemoteClientFrame.getInstance().getRobot().keyRelease(actionCommand);
            } 
        }
    }
}