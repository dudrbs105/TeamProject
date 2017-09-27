package frame.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import button.CloseButton;
import button.MinimizeButton;
import dao.AccountDao;
import dao.ConnectionDao;
import dao.RequestDao;
import frame.friend.FriendRequestFrame;
import model.AccountVO;
import panel.friend.AccountPanel;
import setting.ServerSettings;
import thread.ImgUploadThread;
import thread.MsgRcvThread;

public class MainFrame extends JFrame implements ChangeListener {
    private final int FRIEND_TAB = 0;
    private final int LOG_TAB = 1;
    // 네트워크 통신 ----------
    private Socket msgSocket;
    private ObjectInputStream msgOis;
    private ObjectOutputStream msgOos;
    private Socket imgSocket;
    private ObjectInputStream imgOis;
    private ObjectOutputStream imgOos;
    // 각종 컴포넌트 ----------
    private String myId;
	private JPanel contentPane;
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JPanel mainPanel = new JPanel();
	private JPanel schedulerPanel = new JPanel();
	private JScrollPane scrollBarMain;
	private JScrollPane scrollBarScheduler;
	private JButton addFriendBtn;
	private JButton requestListBtn;
	private JButton myImgBtn;
	private JLabel myIdLabel;
	private JTextArea logArea;
	// 마우스 이벤트 ---------
	private MouseEvent pressedE;
	private MouseEvent draggedE;
	// 친구리스트 ----------
	private JPanel friendListPanel;
	private List<AccountVO> friendList;
	private List<String> connectionList;
	
	public MainFrame(String id) throws IOException {
	    init();            // 네트워크 통신

	    myId = id;
	    
		setBackground(new Color(0, 168, 59));
		setBounds(100, 100, 380, 550);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 168, 59));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		
		addFriendBtn = new JButton("추가");
		addFriendBtn.setBounds(280, 60, 60, 30);
		addFriendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                String inputId = JOptionPane.showInputDialog("아이디를 입력하세요");
                
                if(inputId == null)     return;
                if(inputId.length()<=0) {
                    JOptionPane.showMessageDialog(null, "입력해주세요.");
                    return;
                }
                if(myId.equals(inputId)) {
                    JOptionPane.showMessageDialog(null, "자기 자신을 친구추가 할 수 없습니다.");
                    return;
                }
                
                
                boolean isExisted = AccountDao.getInstance().checkId(inputId);
                if(isExisted == true) {
                    JOptionPane.showMessageDialog(null, "친구 추가 요청을 보냈습니다. 상대방이 수락하면 추가 완료.");
                    boolean isSuccess = RequestDao.getInstance().requestFriend(inputId, myId);
                    System.out.println(isSuccess);
                } else {
                    JOptionPane.showMessageDialog(null, "존재하지 않는 아이디 입니다.");
                }
            }
        });
		contentPane.add(addFriendBtn);
		
		requestListBtn = new JButton("요청");
	    requestListBtn.setBounds(208, 60, 60, 30);
	    requestListBtn.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            FriendRequestFrame.getInstance().setRequestList(RequestDao.getInstance().selectRequestList(myId));
	            FriendRequestFrame.getInstance().setVisible(true);
	        }
	    });
	    contentPane.add(requestListBtn);      
		setUndecorated(true);
        CloseButton closeBtn = new CloseButton(getMsgOos(), null, null, getWidth(), 0);
        contentPane.add(closeBtn);
        contentPane.add(new MinimizeButton(closeBtn.getX(), closeBtn.getY(), this));

		tabbedPane.setBounds(40, 100, 300, 410);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setForeground(new Color(0, 168, 59));
		tabbedPane.addChangeListener(this);
		contentPane.add(tabbedPane);

		myImgBtn = new JButton();
		myImgBtn.addMouseListener(new BtnShadow());
		myImgBtn.setBorderPainted(false);
		myImgBtn.setBounds(tabbedPane.getX(), requestListBtn.getY()+requestListBtn.getHeight()-50, 50, 50);
		ImageIcon imageIcon = null;
        try {
            imgOos.writeObject("[PROFILE]"+id);
            imageIcon = (ImageIcon)imgOis.readObject();
            Image resizeImage = imageIcon.getImage().getScaledInstance(myImgBtn.getWidth(), myImgBtn.getHeight(), Image.SCALE_DEFAULT);
            imageIcon = new ImageIcon(resizeImage);
        } catch(IOException | ClassNotFoundException ioe) {
            // TODO Auto-generated catch block
            ioe.printStackTrace();
        }
		myImgBtn.setIcon(imageIcon);
		contentPane.add(myImgBtn);
		myIdLabel = new JLabel(id);
        myIdLabel.setBounds(myImgBtn.getX()+myImgBtn.getWidth() + 5, myImgBtn.getY(), 100, 50);
        myIdLabel.setVerticalAlignment(JLabel.BOTTOM);
        myIdLabel.setHorizontalAlignment(JLabel.LEFT);
        contentPane.add(myIdLabel);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("프로필 사진 선택");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG(*.png), JPG(*.jpg)", "png", "jpg"));
        myImgBtn.addActionListener(new ActionListener() {
            @Override
            public synchronized void actionPerformed(ActionEvent e) {
                fileChooser.setSelectedFile(null);
                int result = fileChooser.showOpenDialog(null);
                
                if(result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
                    try {
                        Image image = ImageIO.read(fileChooser.getSelectedFile());
                        Image resizeImage = image.getScaledInstance(myImgBtn.getWidth(), myImgBtn.getHeight(), Image.SCALE_DEFAULT);
                        ImageIcon imageIcon = new ImageIcon(resizeImage);
                        myImgBtn.setIcon(imageIcon);
                        
                        
                        Socket imgSocket = new Socket(ServerSettings.SERVER_IP.getString(), ServerSettings.IMG_UPLOAD_SERVER_PORT.getInteger());
                        
                        new ImgUploadThread(imgSocket, imageIcon).start();
                        
                        String remoteResponseLog = logArea.getText() + System.getProperty("line.separator") + "프로필 사진 변경 완료.";
                        logArea.setText(remoteResponseLog);
                        
                        JOptionPane.showMessageDialog(null, "^_^ 프로필 사진 변경 완료");
                    } catch(IOException e1) {
                        JOptionPane.showMessageDialog(null, "ㅜ_ㅜ 프로필 사진 변경 실패");
                        
                        String remoteResponseLog = logArea.getText() + System.getProperty("line.separator") + "프로필 사진 변경 실패.";
                        logArea.setText(remoteResponseLog);
                        
                        e1.printStackTrace();
                    }
                }
            }
        });
		
		mainPanel.setName("친구");
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(null);
		tabbedPane.addTab("친 구", mainPanel);


		schedulerPanel.setBackground(Color.WHITE);
		schedulerPanel.setLayout(null);
		tabbedPane.addTab("로 그", schedulerPanel);

		
		logArea = new JTextArea();
		logArea.setEditable(false);
		scrollBarScheduler = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBarScheduler.setBounds(0, 0, tabbedPane.getWidth()-5, tabbedPane.getHeight()-30);
		scrollBarScheduler.setAutoscrolls(true);
		schedulerPanel.add(scrollBarScheduler);

		friendListPanel = new JPanel(new GridLayout(0, 1));
		scrollBarMain = new JScrollPane(friendListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBarMain.setBounds(0, 0, tabbedPane.getWidth()-5, tabbedPane.getHeight()-30);
		mainPanel.add(scrollBarMain);
		
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

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    while(true) {
                        setFriendList(AccountDao.getInstance().selectAccountList(myId), ConnectionDao.getInstance().selectConnectionList());
                        FriendRequestFrame.getInstance().setRequestList(RequestDao.getInstance().selectRequestList(myId));
                        Thread.sleep(1000 * 3);
                    }
                } catch(InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        
		setVisible(true);
	}

    @Override
    public void stateChanged(ChangeEvent ce) {
        JTabbedPane tab = (JTabbedPane)ce.getSource();
        int index = tab.getSelectedIndex();
        
        if(index == FRIEND_TAB) {
            addFriendBtn.setVisible(true);
            requestListBtn.setVisible(true);
        } else if(index == LOG_TAB) {
            addFriendBtn.setVisible(false);
            requestListBtn.setVisible(false);
        }
    }
    
    public void setFriendList(List<AccountVO> friendList, List<String> connectionList) {
        synchronized(friendList) {
            this.friendList = friendList;
            this.connectionList = connectionList;
            refreshList();
        }
    }
    public void refreshList() {
        friendListPanel.removeAll();
        
        int panelWidth = scrollBarMain.getWidth();
        int panelHeight = friendList.size()*80;
        friendListPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        
        for(AccountVO account : friendList) {
            boolean isConnected = connectionList.contains(account.getId());
            
            ImageIcon imageIcon = null;
            try {
                imgOos.writeObject("[PROFILE]"+account.getId());
                imageIcon = (ImageIcon)imgOis.readObject();
            } catch(IOException | ClassNotFoundException ioe) {
                // TODO Auto-generated catch block
                ioe.printStackTrace();
            }
            
            AccountPanel temp = new AccountPanel(account, isConnected, imageIcon);
            temp.addMouseListener(new MouseAdapter() {
                @Override
                public synchronized void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() >= 2) {
                        UserInfoFrame.getInstance().setAccount(AccountDao.getInstance().selectAccount(account.getId()));

                        ImageIcon imageIcon = null;
                        try {
                            imgOos.writeObject("[PROFILE]" + account.getId());
                            imageIcon = (ImageIcon)imgOis.readObject();
                        } catch(IOException | ClassNotFoundException ioe) {
                            // TODO Auto-generated catch block
                            System.out.println("img load fail.");
                            ioe.printStackTrace();
                        }
                        UserInfoFrame.getInstance().setConnected(isConnected);
                        UserInfoFrame.getInstance().setVisible(true);
                        JLabel profile = UserInfoFrame.getInstance().getProfile();
                        Image resizeImage = imageIcon.getImage().getScaledInstance(profile.getWidth(), profile.getHeight(), Image.SCALE_DEFAULT);
                        profile.setIcon(new ImageIcon(resizeImage));
                    }
                }
            });
            temp.setBorder(new LineBorder(new Color(0, 167, 58), 1));
            friendListPanel.add(temp);
        }
        friendListPanel.validate();
        friendListPanel.invalidate();
        friendListPanel.revalidate();
        friendListPanel.repaint();
    }
    
    public void init() throws UnknownHostException, IOException  {
        msgSocket = new Socket(ServerSettings.SERVER_IP.getString(), ServerSettings.MSG_SERVER_PORT.getInteger());
        System.out.println("msg server connected...");
        msgOos = new ObjectOutputStream(msgSocket.getOutputStream());
        msgOis = new ObjectInputStream(msgSocket.getInputStream());
        
        imgSocket = new Socket(ServerSettings.SERVER_IP.getString(), ServerSettings.IMG_LOAD_SERVER_PORT.getInteger());
        System.out.println("img server connected...");
        imgOos = new ObjectOutputStream(imgSocket.getOutputStream());
        imgOis = new ObjectInputStream(imgSocket.getInputStream());
        
        
        new MsgRcvThread(this).start();
    }
    
    public synchronized ObjectInputStream getMsgOis() {
        return msgOis;
    }
    public synchronized ObjectOutputStream getMsgOos() {
        return msgOos;
    }
    
    public JPanel getFriendListPanel() {
        return friendListPanel;
    }
    public String getMyId() {
        return myId;
    }
    public JTextArea getLogArea() {
        return logArea;
    }


    class BtnShadow extends MouseAdapter {
        @Override
        public void mouseExited(MouseEvent e) {
            if(e.getComponent() == myImgBtn) {
                myImgBtn.setBorderPainted(false);
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            if(e.getComponent() == myImgBtn) {
                myImgBtn.setBorderPainted(true);
            }
        }
    }

}
