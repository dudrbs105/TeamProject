package frame.join;

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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import button.CloseButton;
import button.MinimizeButton;
import dao.AccountDao;
import frame.login.LoginFrame;
import model.AccountVO;

public class JoinFrame extends JFrame implements ActionListener {
	private JPanel contentPane;
	private JTextField textID;
	private JTextField textNick;
	private JPasswordField textPW;
	private JPasswordField textPW2;
	private JButton btnOverlab;
	private JButton btnJoin;
	private JTextField textBirth;
	
	private MouseEvent pressedE;
	private MouseEvent draggedE;
	
	public JoinFrame() {
	    setTitle("회원가입");
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
		
		JLabel labelJoin = new JLabel("회원가입");
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
		textID.setBounds(135, 140, 100, 20);
		contentPane.add(textID);
		textID.setColumns(10);
		
		btnOverlab = new JButton("중복검사");
		btnOverlab.setForeground(Color.WHITE);
		btnOverlab.setBackground(Color.GREEN);
		btnOverlab.setFont(new Font("Dialog", Font.BOLD, 11));
		btnOverlab.setBounds(245, 140, 80, 20);
		btnOverlab.addActionListener(this);
		contentPane.add(btnOverlab);
		
		JLabel labelNick = new JLabel("닉네임");
		labelNick.setFont(new Font("Dialog", Font.BOLD, 12));
		labelNick.setForeground(Color.WHITE);
		labelNick.setBounds(45, 180, 85, 20);
		contentPane.add(labelNick);
		
		textNick = new JTextField();
		textNick.setBounds(135, 180, 100, 20);
		contentPane.add(textNick);
		textNick.setColumns(10);
		
		JLabel labelPW = new JLabel("비밀번호");
		labelPW.setFont(new Font("Dialog", Font.BOLD, 12));
		labelPW.setForeground(Color.WHITE);
		labelPW.setBounds(45, 220, 85, 20);
		contentPane.add(labelPW);
		
		textPW = new JPasswordField(20);
		textPW.setBounds(135, 220, 120, 20);
		contentPane.add(textPW);
		textPW.setColumns(10);
		
		JLabel labelPW2 = new JLabel("비밀번호 확인");
		labelPW2.setFont(new Font("Dialog", Font.BOLD, 12));
		labelPW2.setForeground(Color.WHITE);
		labelPW2.setBounds(45, 260, 85, 20);
		contentPane.add(labelPW2);
		
		textPW2 = new JPasswordField(20);
		textPW2.setBounds(135, 260, 120, 20);
		contentPane.add(textPW2);
		textPW2.setColumns(10);
		
		JLabel labelBirth = new JLabel("생년월일");
        labelBirth.setFont(new Font("Dialog", Font.BOLD, 12));
        labelBirth.setForeground(Color.WHITE);
        labelBirth.setBounds(45, 300, 85, 20);
        
        contentPane.add(labelBirth);
        
        textBirth = new JTextField(20);
        textBirth.setBounds(135, 300, 120, 20);
        contentPane.add(textBirth);
        textBirth.setColumns(10);
        textBirth.setText("ex)2000-12-12");
        textBirth.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                if(textBirth.getText().length() <=0 ) {
                    textBirth.setText("ex)2000-12-12");
                }
            }
            public void focusGained(FocusEvent e) {
                textBirth.setText("");
            }
        });
		
		btnJoin = new JButton("가입");
		btnJoin.setForeground(Color.WHITE);
		btnJoin.setBackground(Color.GREEN);
		btnJoin.setFont(new Font("Dialog", Font.BOLD, 12));
		btnJoin.setBounds(135, 400, 100, 30);
		btnJoin.addActionListener(this);
		contentPane.add(btnJoin);
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
        if (e.getSource() == btnOverlab) {
            boolean isExisted = AccountDao.getInstance().checkId(textID.getText());

            if (isExisted == false) {
                // 아이디 없으니까 회원가입진행 ~~~
                JOptionPane.showMessageDialog(null, "가입할 수 있는 ID입니다.");
                btnJoin.setEnabled(true);
            } else {
                // 아이디 존재하니까 딴거해~~~~~~~~~~~~
                JOptionPane.showMessageDialog(null, "중복된 ID입니다.");
            }
        } else if (e.getSource() == btnJoin) {
            if(textID.getText().length() <= 0 || textNick.getText().length()<=0 || textPW.getText().length()<=0 || textPW2.getText().length()<=0 ) {
                JOptionPane.showMessageDialog(null, "다 채우세요");
                return;
            }

            String pattern = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
            if(!textBirth.getText().matches(pattern)) {
                JOptionPane.showMessageDialog(null, "생년월일 양식에 맞지 않습니다.");
                return;
            }
            
            if (textPW.getText().equals(textPW2.getText())) {
                JOptionPane.showMessageDialog(null, "비밀번호가 일치합니다");
                AccountVO account = new AccountVO();
                account.setId(textID.getText());
                account.setPw(textPW.getText());
                account.setNickname(textNick.getText());
                account.setBirth(textBirth.getText());
                
                if(AccountDao.getInstance().insertInfo(account)) {
                    new LoginFrame();
                    dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지않습니다.");
            }
        }

    }
}
