package frame.remote;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class RemoteServerFrame implements KeyListener, MouseMotionListener, MouseListener {
    private JFrame frame;
    private Image image;
    private Dimension di;
    
    private boolean isContinued;
    
    private ObjectOutputStream msgOos;
    public RemoteServerFrame(ObjectOutputStream msgOos, Dimension di) {
        this.msgOos = msgOos;
        this.di = di;
        isContinued = true;
        
        frame = new RemoteFrame();
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.addKeyListener(this);
    }
    
    public void refreshScreen() throws IOException {
        ServerSocket imgServerSocket = new ServerSocket(6666);
        
        Socket client = imgServerSocket.accept();
        ObjectInputStream imgServerOis = new ObjectInputStream(client.getInputStream());

        new Thread(new Runnable() {
            public void run() {
                while(isContinued) {
                    try {
                        ImageIcon imageIcon = (ImageIcon)imgServerOis.readObject();
                        image = imageIcon.getImage();
                        image = image.getScaledInstance(frame.getWidth(),frame.getHeight(),Image.SCALE_FAST);
                        Thread.sleep(50);
                    }  catch (InterruptedException | IOException | ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    frame.validate();
                    frame.invalidate();
                    frame.revalidate();
                    frame.repaint();
                }
                try {
                    imgServerOis.close();
                    client.close();
                    imgServerSocket.close();
                    isContinued = true;
                    frame.dispose();
                } catch(IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void setContinued(boolean isContinued) {
        this.isContinued = isContinued;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        int xButton = 16;
        if (button == 3) {
            xButton = 4;
        }
        try {
            msgOos.writeObject("[MOUSE_PRESS]" + xButton);
        } catch(IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        int xButton = 16;
        if (button == 3) {
            xButton = 4;
        }
        try {
            msgOos.writeObject("[MOUSE_RELEASE]" + xButton);
        } catch(IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void mouseMoved(MouseEvent e) {
        double xScale = di.getWidth()/frame.getWidth();
        double yScale = di.getHeight()/frame.getHeight();
        
        int xPoint = (int)(e.getX() * xScale);
        int yPoint = (int)(e.getY() * yScale);
        try {
            msgOos.writeObject("[MOUSE_MOVE]" + xPoint + "#" + yPoint);
        } catch(IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void keyPressed(KeyEvent e) {
        try {
            msgOos.writeObject("[KEY_PRESS]" + e.getKeyCode());
        } catch(IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void keyReleased(KeyEvent e) {
        try {
            msgOos.writeObject("[KEY_RELEASE]" + e.getKeyCode());
        } catch(IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    public void keyTyped(KeyEvent e) {}
    //////////////////////////////////////////////////////////////////////////////////////////
    class RemoteFrame extends JFrame {
        public RemoteFrame() {
            setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if(image == null)
                return;

            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
