package frame.password;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import button.CloseButton;
import button.MinimizeButton;
import dao.AccountDao;
import frame.login.LoginFrame;

public class ChangePwFrame extends JFrame implements ActionListener {
	private String ID;
	private JPanel contentPane;
	private TextField textPw1;
	private TextField textPw2;
	private JButton forgotBtn;
	
	private MouseEvent pressedE;
	private MouseEvent draggedE;

	public ChangePwFrame(String Id) {
		ID = Id;
		setTitle("비밀번호 변경");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        

		JLabel labelJoin = new JLabel("비밀번호 변경");
		labelJoin.setForeground(Color.WHITE);
		labelJoin.setBounds(45, 10, 280, 96);
		contentPane.add(labelJoin);
		labelJoin.setHorizontalAlignment(SwingConstants.CENTER);
		labelJoin.setFont(new Font("Dialog", Font.BOLD, 40));

		JLabel labelPw1 = new JLabel("변경할 비밀번호");
		labelPw1.setFont(new Font("Dialog", Font.BOLD, 12));
		labelPw1.setForeground(Color.WHITE);
		labelPw1.setBounds(45, 140, 100, 20);
		contentPane.add(labelPw1);

		textPw1 = new TextField();
		textPw1.setBounds(150, 140, 100, 20);
		contentPane.add(textPw1);
		textPw1.setColumns(10);
		textPw1.setEchoChar('*');

		JLabel labelPw2 = new JLabel("비밀번호 확인");
		labelPw2.setFont(new Font("Dialog", Font.BOLD, 12));
		labelPw2.setForeground(Color.WHITE);
		labelPw2.setBounds(45, 180, 95, 20);
		contentPane.add(labelPw2);

		textPw2 = new TextField(20);
		textPw2.setBounds(150, 180, 100, 20);
		contentPane.add(textPw2);
		textPw2.setColumns(10);
		textPw2.setEchoChar('*');
		
		forgotBtn = new JButton("변경하기");
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
			if (textPw1.getText().length() <= 0 || textPw2.getText().length() <= 0) {
				JOptionPane.showMessageDialog(null, "다 채우세요");
				return;
			}
		}
		if (textPw1.getText().equals(textPw2.getText())) {
			AccountDao.getInstance().updatePw(textPw2.getText(), ID);
			JOptionPane.showMessageDialog(null, "비밀번호가 변경되었습니다");
			new LoginFrame();
			dispose();
		} else {
			JOptionPane.showMessageDialog(null, "비밀번호가 서로 다릅니다");
			return;
		}
	}
}
