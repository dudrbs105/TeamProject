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
                System.out.println("클라이언트 메세지 수신 에러");
                e.printStackTrace();
                isStop = true;
            }

            if(message.startsWith("[CONNECT_REQUEST]")) {
                String[] msgArr = message.substring("[CONNECT_REQUEST]".length()).split("#");   // 메세지들
                                                                                                // 0: 제어하는 아이디
                                                                                                // 1: 제어 당할 아이디
                                                                                                // 2: 제어 하는 ip 주소
                int result = JOptionPane.showConfirmDialog(null, "( " + msgArr[0] +" )님이 원격 제어 요청 수락하시겠습니까?");
                
                String remoteRequestLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "( " + msgArr[0] +" )님이 원격 제어 요청";
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
                    // 승인했을 경우 --> 클라가 된다.
                    String remoteResponseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "( " + msgArr[0] + " ) 원격제어 연결됨.";
                    mf.getLogArea().setText(remoteResponseLog);
                    
                    RemoteClientFrame.getInstance().setVisible(true);
                    RemoteClientFrame.getInstance().getStateLabel().setText("( " + msgArr[0] + " ) 연결됨.");
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
                    String remoteResponseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "( " + msgArr[0] + " ) 원격제어 거절.";
                    mf.getLogArea().setText(remoteResponseLog);
                }
            } else if(message.startsWith("[CONNECT_RESPONSE]")) {
                String[] msgArr = message.substring("[CONNECT_RESPONSE]".length()).split("#");  // 메세지 분류
                                                                                                // 0: 결과값
                                                                                                // 1: 응답 받을 아이디
                if(msgArr[0].equals("0")) {
                    // 승인 받았을경우 --> 서버가 된다.
                    String remoteResponseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "원격 제어 시작.";
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
                    String remoteRequeseLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "원격 제어 거절당함.";
                    mf.getLogArea().setText(remoteRequeseLog);
                    
                    JOptionPane.showMessageDialog(null, "원격 요청 거절당함.");
                } 
            } else if(message.startsWith("[DISCONNECT]")) {
                String remoteDisconnectLog = mf.getLogArea().getText() + System.getProperty("line.separator") + "원격 제어 종료.";
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