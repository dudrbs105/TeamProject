package panel.friend;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.RequestDao;
import frame.friend.FriendRequestFrame;
import model.FriendRequest;

public class FriendRequestPanel extends JPanel {
    private JButton oBtn, xBtn;// ģ����û ���� ��ư
    private JLabel infoLabel, dateLabel;
    // �̸�
    // ��¥

    public FriendRequestPanel(FriendRequest friendRequest) {
        setLayout(null);
        oBtn = new JButton("O");
        oBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(RequestDao.getInstance().submitRequest(friendRequest.getToId(), friendRequest.getFromId())) {
                    RequestDao.getInstance().removeRequest(friendRequest.getFromId(), friendRequest.getToId());
                    JOptionPane.showMessageDialog(null, "ģ����� ����");
                } else {
                    JOptionPane.showMessageDialog(null, "ģ����� ����");
                }
                FriendRequestFrame.getInstance().setRequestList(RequestDao.getInstance().selectRequestList(friendRequest.getToId()));
            }
        });
        xBtn = new JButton("X");
        xBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(RequestDao.getInstance().removeRequest(friendRequest.getFromId(), friendRequest.getToId())) {
                    JOptionPane.showMessageDialog(null, "ģ���ź� �Ϸ�");
                } else {
                    JOptionPane.showMessageDialog(null, "ģ���ź� ����");
                }
                FriendRequestFrame.getInstance().setRequestList(RequestDao.getInstance().selectRequestList(friendRequest.getToId()));
            }
        });
        
        infoLabel = new JLabel(friendRequest.getFromId());
        dateLabel = new JLabel(friendRequest.getDate());

        dateLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        dateLabel.setForeground(Color.black);
        dateLabel.setBounds(0, 20, 120, 30);

        infoLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        infoLabel.setForeground(new Color(0, 167, 58));
        infoLabel.setBounds(50, 30, 100, 50);

        oBtn.setBounds(130, 30, 50, 30);
        xBtn.setBounds(180, 30, 50, 30);

        setBackground(Color.WHITE);
        add(dateLabel);
        add(infoLabel);
        add(xBtn);
        add(oBtn);
    }
}
