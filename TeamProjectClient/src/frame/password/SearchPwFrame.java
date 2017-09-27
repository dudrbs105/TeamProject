package frame.password;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import button.CloseButton;
import button.MinimizeButton;
import dao.AccountDao;
import frame.login.LoginFrame;

public class SearchPwFrame extends JFrame implements ActionListener {
	private JPanel contentPane;
	private JTextField textID;
	private JTextField textBirth;
	private JButton forgotBtn;
	
	private MouseEvent pressedE;
	private MouseEvent draggedE;

	public SearchPwFrame() {
		setTitle("비밀번호 찾기");

		setBounds(100, 100, 380, 550);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 168, 59));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		setUndecorated(true);
		CloseButton closeBtn = new CloseButton(null, this, LoginFrame.class.getName(), getWidth(), 0);
		contentPane.add(closeBtn);
        contentPane.add(new MinimizeButton(closeBtn.getX(), closeBtn.getY(), this));
        
		
		JLabel labelJoin = new JLabel("비밀번호 찾기");
		labelJoin.setForeground(Color.WHITE);
		labelJoin.setBounds(45, 10, 280, 96);
		contentPane.add(labelJoin);
		labelJoin.setHorizontalAlignment(SwingConstants.CENTER);
		labelJoin.setFont(new Font("Dialog", Font.BOLD, 40));

		JLabel labelID = new JLabel("아이디");
		labelID.setFont(new Font("Dialog", Font.BOLD, 12));
		labelID.setForeground(Color.WHITE);
		labelID.setBounds(45, 140, 85, 20);
		contentPane.add(labelID);

		textID = new JTextField();
		textID.setBounds(140, 140, 100, 20);
		contentPane.add(textID);
		textID.setColumns(10);
		
		JLabel labelBirth = new JLabel("생년월일");
        labelBirth.setFont(new Font("Dialog", Font.BOLD, 12));
        labelBirth.setForeground(Color.WHITE);
        labelBirth.setBounds(45, 180, 95, 20);
        contentPane.add(labelBirth);
        
        textBirth = new JTextField(20);
        textBirth.setBounds(140, 180, 100, 20);
        contentPane.add(textBirth);
        textBirth.setColumns(10);
        textBirth.setText("ex)2000-12-12");
        textBirth.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent arg0) {        
                if(textBirth.getText().length() <=0 ) {
                    textBirth.setText("ex)2000-12-12");
                }
            }
            public void focusGained(FocusEvent arg0) {
                textBirth.setText("");
            }
        });
        
		forgotBtn = new JButton("찾기");
		forgotBtn.setForeground(Color.WHITE);
		forgotBtn.setBackground(Color.GREEN);
		forgotBtn.setFont(new Font("Dialog", Font.BOLD, 12));
		forgotBtn.setBounds(135, 400, 100, 30);
		forgotBtn.addActionListener(this);
		contentPane.add(forgotBtn);
		
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

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == forgotBtn) {
			if (textID.getText().length() <= 0 || textBirth.getText().length() <= 0) {
				JOptionPane.showMessageDialog(null, "다 채우세요");
				return;
			}
		}
        switch (AccountDao.getInstance().selectFindPw(textID.getText(), textBirth.getText())) {
            case 0:
                new ChangePwFrame(textID.getText());
                dispose();
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "올바르지 않은 정보입니다");
                break;
        }
	}
}
