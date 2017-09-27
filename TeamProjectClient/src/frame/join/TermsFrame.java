package frame.join;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import button.CloseButton;
import button.MinimizeButton;
import frame.login.LoginFrame;
 
public class TermsFrame extends JFrame{
    private JPanel contentPane;
    private MouseEvent pressedE;
    private MouseEvent draggedE;
    
    public TermsFrame() {
        setTitle("회원가입");
        setUndecorated(true);
        setBounds(100, 100, 380, 550);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 168, 59));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel label = new JLabel("약관");
        label.setBounds(45, 10, 280, 96);
        contentPane.add(label);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 40));
        
        JTextArea txtrn = new JTextArea();
        txtrn.setFont(new Font("Dialog", Font.PLAIN, 13));
        contentPane.add(txtrn);
        txtrn.setLineWrap(true);
        txtrn.setBounds(45, 110, 280, 200);
        txtrn.setWrapStyleWord(true);
        txtrn.setText("이 프로그램의 사용에 관한 모든 책임은 사용자에게 있습니다.제작자는 이 프로그램의 사용에 관한 어떠한 책임도 지지 않습니다.");
        
        JButton btnNo = new JButton("거부");
        btnNo.setBackground(Color.GREEN);
        btnNo.setForeground(Color.WHITE);
        btnNo.setBounds(70, 400, 100, 30);
        contentPane.add(btnNo);
        btnNo.setFont(new Font("Dialog", Font.BOLD, 25));
        
        JButton btnYes = new JButton("확인");
        btnYes.setBackground(Color.GREEN);
        btnYes.setForeground(Color.WHITE);
        btnYes.setBounds(200, 400, 100, 30);
        contentPane.add(btnYes);
        btnYes.setFont(new Font("Dialog", Font.BOLD, 25));
        setUndecorated(true);
        setVisible(true);
        CloseButton closeBtn = new CloseButton(null, this, LoginFrame.class.getName(), getWidth(), 0);
        add(closeBtn);
        add(new MinimizeButton(closeBtn.getX(), closeBtn.getY(), this));
                
        btnNo.addActionListener(new ActionListener() {	//거부 액션 버튼
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnNo) {
					new LoginFrame();	//	로그인 창 실행
					dispose();	        //	이전창 닫기
				}
			}
		});
        btnYes.addActionListener(new ActionListener() {	//확인 버튼 액션
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnYes) {
					new JoinFrame();	//	회원가입 창 실행
					dispose();	        //	이전창 닫기
				}
			}
		});
        
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
}