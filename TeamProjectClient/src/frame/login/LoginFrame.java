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
    // 화면에 사용할 변수들 선언

    private JButton loginBtn, signupBtn, findpassBtn;   // 로그인, 회원가입, 비밀번호찾기 버튼
    private JTextField loginText;                       // 아이디 입력 텍스트
    private TextField passText;                         // 패스워드 텍스트 echochar('*') 사용을 위해 awt사용함
    private JLabel label;                               // 로고 담을 레이블
    
    private MouseEvent pressedE;
    private MouseEvent draggedE;
    public LoginFrame() {
        setLayout(null);// 컴포넌트가 절대위치를 가지기 위해 배치관리자 없앰 ex)setbounds, setsize, setlocation
        loginBtn = new JButton("로그인");
        signupBtn = new JButton("회원가입");
        findpassBtn = new JButton("비밀번호 찾기");
        loginText = new JTextField();
        passText = new TextField();
        label = new JLabel();

        loginBtn.setIcon(new ImageIcon("icon/login.png"));// 로그인 이미지 삽입
        label.setIcon(new ImageIcon("icon/logo.png"));// 로고 삽입
        passText.setEchoChar('*');

        setTitle("Login");

        getContentPane().setBackground(new Color(0, 167, 58));
        signupBtn.setBorderPainted(false);
        // signupBtn.setContentAreaFilled(false);
        findpassBtn.setBorderPainted(false);// 버튼의 외곽선 삭제
        // findpassBtn.setContentAreaFilled(false);//버튼의 내용영역 채우기 안함 [클릭이 안됨]
        signupBtn.setFocusPainted(false);// 버튼이 선택되었을때 생기는 테두리 사용안함
        findpassBtn.setFocusPainted(false);
        signupBtn.setBackground(new Color(0, 167, 58));// 배경색을 frame의 배경색과 동일하게
        findpassBtn.setBackground(new Color(0, 167, 58));

        loginText.setFont(new Font("굴림", ABORT, 40));
        passText.setFont(new Font("굴림", ABORT, 40));
        label.setBounds(125, 30, 150, 150);
        loginText.setBounds(50, 220, 260, 60);
        passText.setBounds(50, 282, 260, 60);
        loginBtn.setBounds(50, 370, 260, 40);
        signupBtn.setBounds(60, 420, 120, 30);
        findpassBtn.setBounds(180, 420, 120, 30);
        // 절대위치 설정된 컴포넌트들 프레임에 추가
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
        

        loginBtn.addActionListener(new ActionListener() { // 로그인 버튼 액션
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(loginText.getText().length() <= 0 || passText.getText().length() <= 0) {
                    JOptionPane.showMessageDialog(null, "ID 혹은 비밀번호를 입력해주세요");
                    return;
                }
                switch ((AccountDao.getInstance().searchAccount(loginText.getText(), passText.getText()))) {
                    case 0:
                        try {
                            if(!ConnectionDao.getInstance().checkConnection(loginText.getText())) {
                                // 이미 접속해있는경우
                                JOptionPane.showMessageDialog(null, "이미 접속해 있습니다.");
                                break;
                            }
                            
                            MainFrame mainFrame = new MainFrame(loginText.getText());
                            mainFrame.getMsgOos().writeObject("[LOGIN]"+loginText.getText());
                            UserInfoFrame.getInstance().setMsgOos(mainFrame.getMsgOos());
                            UserInfoFrame.getInstance().setMyId(loginText.getText());
                            RemoteClientFrame.getInstance().setMsgOos(mainFrame.getMsgOos());
                            dispose();
                        } catch (UnknownHostException e) {
                            JOptionPane.showMessageDialog(null, "아이피 혹은 포트가 다름.");
                            e.printStackTrace();
                        } catch(IOException e) {
                            JOptionPane.showMessageDialog(null, "서버가 꺼져있음.");
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "아이디가 없거나 틀립니다");
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, "비밀번호가 틀립니다");
                        break;
                }
            }
        });

        signupBtn.addActionListener(new ActionListener() { // 회원가입 버튼 액션
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == signupBtn) {
                    new TermsFrame(); // 약관 실행
                    dispose(); // 이전창 닫기
                }
            }
        });
        findpassBtn.addActionListener(new ActionListener() { // 비밀번호 찾기 버튼 액션
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
