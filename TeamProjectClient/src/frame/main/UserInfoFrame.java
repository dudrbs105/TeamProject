package frame.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import button.CloseButton;
import button.MinimizeButton;
import model.AccountVO;

public class UserInfoFrame extends JFrame implements ActionListener {
	private static UserInfoFrame instance = null;
    private ObjectOutputStream msgOos;
    
    private String myId;
    private boolean isConnected;
    
    private JLabel profile;
    private JPanel contentPane;
	
    private JButton btnConnect;    // 연결버튼
    private JButton btnChat;       // 채팅버튼
    
	private JLabel idLabel;
	private JLabel nickLabel;
    
	private MouseEvent pressedE;
	private MouseEvent draggedE;
	
	private AccountVO account;
	
	public static UserInfoFrame getInstance() {
	    if(instance == null) {
	        instance = new UserInfoFrame();
	    }
	    
	    return instance;
	}
	
	private UserInfoFrame() {
		setBackground(new Color(238, 119, 0));
		setBounds(500, 100, 380, 550);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(238, 119, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		setUndecorated(true);
		CloseButton closeBtn = new CloseButton(null, this, null, getWidth(), 0);
		closeBtn.setBackground(new Color(238, 119, 0));
        contentPane.add(closeBtn);
        MinimizeButton miniBtn = new MinimizeButton(closeBtn.getX(), closeBtn.getY(), this);
        miniBtn.setBackground(new Color(238, 119, 0));
        contentPane.add(miniBtn);
        
        JPanel panelBtn = new JPanel();
		panelBtn.setBounds(0, 275, 380, 275);
		panelBtn.setBackground(Color.WHITE);
		contentPane.add(panelBtn);
		
		profile = new JLabel();
		profile.setBounds(120, 50, 140, 140);
		contentPane.add(profile);
            
		idLabel = new JLabel();
		idLabel.setFont(new Font("굴림", Font.BOLD, 20));
		idLabel.setHorizontalAlignment(JLabel.CENTER);
		idLabel.setBounds((int)profile.getBounds().getX()+profile.getWidth()/2-50, (int)profile.getBounds().getMaxY() + 10, 100, 30);
		nickLabel = new JLabel();
		nickLabel.setHorizontalAlignment(JLabel.CENTER);
		nickLabel.setBounds((int)idLabel.getBounds().getX(), (int)idLabel.getBounds().getMaxY() +10, 100, 30);
		contentPane.add(idLabel);
		contentPane.add(nickLabel);

		btnChat= new JButton();
        btnChat.setBounds(70, 95, 80, 80);
        ImageIcon chatImageIcon = new ImageIcon("icon//chat.png");
        Image resizeChatImage = chatImageIcon.getImage().getScaledInstance(btnChat.getWidth(), btnChat.getHeight(), Image.SCALE_DEFAULT);
        chatImageIcon.setImage(resizeChatImage);
        btnChat.setIcon(chatImageIcon);
        btnChat.setBorderPainted(false);
        btnChat.addMouseListener(new BtnShadow());
        btnChat.addActionListener(this);
        panelBtn.setLayout(null);
        panelBtn.add(btnChat);
		
        btnConnect = new JButton();
        btnConnect.setBounds(btnChat.getBounds().x+btnChat.getWidth()+80, btnChat.getBounds().y, btnChat.getWidth(), btnChat.getHeight());
        ImageIcon connectImageIcon = new ImageIcon("icon//connect.jpg");
        Image resizeConnectImage = connectImageIcon.getImage().getScaledInstance(btnConnect.getWidth(), btnConnect.getHeight(), Image.SCALE_DEFAULT);
        connectImageIcon.setImage(resizeConnectImage);
        btnConnect.setIcon(connectImageIcon);
        btnConnect.setBorderPainted(false);
        btnConnect.addMouseListener(new BtnShadow());
        btnConnect.addActionListener(this);
        panelBtn.setLayout(null);
        panelBtn.add(btnConnect);
        
		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressedE = e;
                
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draggedE = e;
                setLocation(draggedE.getLocationOnScreen().x-pressedE.getX(),
                            draggedE.getLocationOnScreen().y-pressedE.getY());
                repaint();
            }
        });
	}

    class BtnShadow extends MouseAdapter {
        @Override
        public void mouseExited(MouseEvent e) {
            ((AbstractButton)e.getComponent()).setBorderPainted(false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ((AbstractButton)e.getComponent()).setBorderPainted(true);
        }
    }

    
	public void setMsgOos(ObjectOutputStream msgOos) {
	    if(this.msgOos == null)   this.msgOos = msgOos;
    }
    public void setAccount(AccountVO account) {
	    this.account = account;
	    idLabel.setText(this.account.getId());
	    nickLabel.setText(this.account.getNickname());

	    validate();
	    invalidate();
	    revalidate();
	    repaint();
	}
    public void setMyId(String myId) {
        this.myId = myId;
    }
    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
    public JLabel getProfile() {
        return profile;
    }

    @Override
	public void actionPerformed(ActionEvent e) {
        if(!isConnected) {
            // 접속되있지 않은상태면
            JOptionPane.showMessageDialog(this, "비접속 상태입니다.");
            return;
        }
        
        
	    if(e.getSource() == btnConnect) {
	        try {
	            msgOos.writeObject("[CONNECT_REQUEST]" + myId + "#" +idLabel.getText());
	        } catch(IOException ioe) {
	            // TODO Auto-generated catch block
	            ioe.printStackTrace();
	        }
	    } else if(e.getSource() == btnChat) {
	        
	    }
	}
    
}
