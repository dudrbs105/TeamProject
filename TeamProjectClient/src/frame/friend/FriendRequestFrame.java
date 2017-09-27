package frame.friend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import button.CloseButton;
import button.MinimizeButton;
import model.FriendRequest;
import panel.friend.FriendRequestPanel;

public class FriendRequestFrame extends JFrame {
    private static FriendRequestFrame instance;
	private JPanel contentPane;
	private JPanel panel = new JPanel();
	private JScrollPane scrollBarPanel;
	private JPanel requestPanel;
	private List<FriendRequest> requestList;
	
	private MouseEvent pressedE;
	private MouseEvent draggedE;
	
	public static FriendRequestFrame getInstance() {
	    if(instance == null)   {
	        instance = new FriendRequestFrame();
	    }
	    return instance;
	}
	
    private FriendRequestFrame() {
		setUndecorated(true);

		setBackground(new Color(0, 168, 59));
		setBounds(100, 100, 380, 550);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 168, 59));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		CloseButton closeBtn = new CloseButton(null, this, null, getWidth(), 0);
		contentPane.add(closeBtn);
		contentPane.add(new MinimizeButton(closeBtn.getX(), closeBtn.getY(), this));

		panel.setBounds(40, 100, 300, 410);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);
		panel.setLayout(null);

		requestPanel = new JPanel(new GridLayout(0, 1));
		
		scrollBarPanel = new JScrollPane(requestPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBarPanel.setBounds(0, 0, panel.getWidth(), panel.getHeight());

		requestPanel.setPreferredSize(new Dimension(scrollBarPanel.getWidth(), scrollBarPanel.getHeight()));
		
		panel.add(scrollBarPanel);

		JLabel titleLabel = new JLabel("요청 목록");
		titleLabel.setFont(new Font("굴림", Font.BOLD, 15));
		titleLabel.setBounds(40, 70, 100, 20);
		titleLabel.setForeground(Color.WHITE);
		contentPane.add(titleLabel);
		
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
   
    public List<FriendRequest> getRequestList() {
        return requestList;
    }
    public void setRequestList(List<FriendRequest> requestList) {
        synchronized(requestList) {
            this.requestList = requestList;
            refreshList();
        }
    }
    
    public void refreshList() {
        requestPanel.removeAll();
        
        int panelWidth = scrollBarPanel.getWidth();
        int panelHeight = requestList.size()*80;
        requestPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        
        for(FriendRequest fr : requestList) {
            FriendRequestPanel temp = new FriendRequestPanel(fr);
            temp.setBorder(new LineBorder(new Color(0, 167, 58), 1));
            requestPanel.add(temp);
        }
        requestPanel.validate();
        requestPanel.invalidate();
        requestPanel.revalidate();
        requestPanel.repaint();
    }
}

