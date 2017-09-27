package frame.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import button.CloseButton;
import button.MinimizeButton;
import dao.AccountDao;
import dao.ConnectionDao;
import frame.join.TermsFrame;
import frame.main.MainFrame;
import frame.main.UserInfoFrame;
import frame.password.SearchPwFrame;
import frame.remote.RemoteClientFrame;

public class LoginFrame extends JFrame {
    // ȭ�鿡 ����� ������ ����

    private JButton loginBtn, signupBtn, findpassBtn;   // �α���, ȸ������, ��й�ȣã�� ��ư
    private JTextField loginText;                       // ���̵� �Է� �ؽ�Ʈ
    private TextField passText;                         // �н����� �ؽ�Ʈ echochar('*') ����� ���� awt�����
    private JLabel label;                               // �ΰ� ���� ���̺�
    
    private MouseEvent pressedE;
    private MouseEvent draggedE;
    public LoginFrame() {
        setLayout(null);// ������Ʈ�� ������ġ�� ������ ���� ��ġ������ ���� ex)setbounds, setsize, setlocation
        loginBtn = new JButton("�α���");
        signupBtn = new JButton("ȸ������");
        findpassBtn = new JButton("��й�ȣ ã��");
        loginText = new JTextField();
        passText = new TextField();
        label = new JLabel();

        loginBtn.setIcon(new ImageIcon("icon/login.png"));// �α��� �̹��� ����
        label.setIcon(new ImageIcon("icon/logo.png"));// �ΰ� ����
        passText.setEchoChar('*');

        setTitle("Login");

        getContentPane().setBackground(new Color(0, 167, 58));
        signupBtn.setBorderPainted(false);
        // signupBtn.setContentAreaFilled(false);
        findpassBtn.setBorderPainted(false);// ��ư�� �ܰ��� ����
        // findpassBtn.setContentAreaFilled(false);//��ư�� ���뿵�� ä��� ���� [Ŭ���� �ȵ�]
        signupBtn.setFocusPainted(false);// ��ư�� ���õǾ����� ����� �׵θ� ������
        findpassBtn.setFocusPainted(false);
        signupBtn.setBackground(new Color(0, 167, 58));// ������ frame�� ������ �����ϰ�
        findpassBtn.setBackground(new Color(0, 167, 58));

        loginText.setFont(new Font("����", ABORT, 40));
        passText.setFont(new Font("����", ABORT, 40));
        label.setBounds(125, 30, 150, 150);
        loginText.setBounds(50, 220, 260, 60);
        passText.setBounds(50, 282, 260, 60);
        loginBtn.setBounds(50, 370, 260, 40);
        signupBtn.setBounds(60, 420, 120, 30);
        findpassBtn.setBounds(180, 420, 120, 30);
        // ������ġ ������ ������Ʈ�� �����ӿ� �߰�
        add(label);
        add(loginText);
        add(loginBtn);
        add(passText);
        add(signupBtn);
        add(findpassBtn);
        setBounds(100, 100, 380, 550);
        setResizable(false);
        
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
        

        setUndecorated(true);
        CloseButton closeBtn = new CloseButton(null, null, null,getWidth(), 0);
        add(closeBtn);
        add(new MinimizeButton(closeBtn.getX(), closeBtn.getY(), this));
        

        loginBtn.addActionListener(new ActionListener() { // �α��� ��ư �׼�
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(loginText.getText().length() <= 0 || passText.getText().length() <= 0) {
                    JOptionPane.showMessageDialog(null, "ID Ȥ�� ��й�ȣ�� �Է����ּ���");
                    return;
                }
                switch ((AccountDao.getInstance().searchAccount(loginText.getText(), passText.getText()))) {
                    case 0:
                        try {
                            if(!ConnectionDao.getInstance().checkConnection(loginText.getText())) {
                                // �̹� �������ִ°��
                                JOptionPane.showMessageDialog(null, "�̹� ������ �ֽ��ϴ�.");
                                break;
                            }
                            
                            MainFrame mainFrame = new MainFrame(loginText.getText());
                            mainFrame.getMsgOos().writeObject("[LOGIN]"+loginText.getText());
                            UserInfoFrame.getInstance().setMsgOos(mainFrame.getMsgOos());
                            UserInfoFrame.getInstance().setMyId(loginText.getText());
                            RemoteClientFrame.getInstance().setMsgOos(mainFrame.getMsgOos());
                            dispose();
                        } catch (UnknownHostException e) {
                            JOptionPane.showMessageDialog(null, "������ Ȥ�� ��Ʈ�� �ٸ�.");
                            e.printStackTrace();
                        } catch(IOException e) {
                            JOptionPane.showMessageDialog(null, "������ ��������.");
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "���̵� ���ų� Ʋ���ϴ�");
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "��й�ȣ�� Ʋ���ϴ�");
                        break;
                }
            }
        });

        signupBtn.addActionListener(new ActionListener() { // ȸ������ ��ư �׼�
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == signupBtn) {
                    new TermsFrame(); // ��� ����
                    dispose(); // ����â �ݱ�
                }
            }
        });
        findpassBtn.addActionListener(new ActionListener() { // ��й�ȣ ã�� ��ư �׼�
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == findpassBtn) {
                    new SearchPwFrame();
                    dispose();
                }
            }
        });


        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }// end ProjectFrame Construct
}
