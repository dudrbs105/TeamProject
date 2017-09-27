package frame.remote;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import setting.ServerSettings;

public class RemoteClientFrame extends JFrame {
    private static RemoteClientFrame instance = null;
    
    private Robot robot = null; // Used to capture screen
    boolean isContinued;
    
    private String otherId;
    private JLabel stateLabel;
    
    private ObjectOutputStream msgOos;
    
    public static RemoteClientFrame getInstance() {
        if(instance == null)
            instance = new RemoteClientFrame();
        
        return instance;
    }
    private RemoteClientFrame() {
        setTitle("제어 창");
        
        setUndecorated(true);
        setAlwaysOnTop(true);
        
        setLayout(new BorderLayout());
        
        isContinued = true;
        
        JButton button = new JButton("종료");
        init();
        stateLabel.setFont(new Font("굴림", Font.BOLD, 15));
        stateLabel.setHorizontalAlignment(JLabel.CENTER);

        setBounds(0, 0, 150, 150);

        add(button, BorderLayout.CENTER);
        add(stateLabel, BorderLayout.SOUTH);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    msgOos.writeObject("[DISCONNECT]"+otherId);
                    isContinued = false;
                } catch(IOException ioe) {
                    // TODO Auto-generated catch block
                    ioe.printStackTrace();
                }
            }
        });
    }
    
    public void init() {
        stateLabel = new JLabel("연결중...");
        otherId = null;
        isContinued = true;
        dispose();
    }
    public void remoteStart(Robot robot, Rectangle rectangle, String ip) throws IOException {
        this.robot = robot;
        
        Socket imgSocket = new Socket(ip, ServerSettings.REMOTE_SERVER_PORT.getInteger());
        ObjectOutputStream imgOos = new ObjectOutputStream(imgSocket.getOutputStream());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isContinued) {
                    try {
                        BufferedImage bufImage = robot.createScreenCapture(rectangle);
                        ImageIcon imageIcon = new ImageIcon(bufImage);
                        imgOos.writeObject(imageIcon);
                        imgOos.reset();
                        
                        Thread.sleep(50);
                    } catch(IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                try {
                    imgOos.close();
                    imgSocket.close();
                    init();
                } catch(IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        
    }
    
    public Robot getRobot() {
        return robot;
    }
    public JLabel getStateLabel() {
        return stateLabel;
    }
    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }
    public void setMsgOos(ObjectOutputStream msgOos) {
        this.msgOos = msgOos;
    }
}
