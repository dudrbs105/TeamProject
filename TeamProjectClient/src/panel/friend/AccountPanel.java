package panel.friend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.AccountVO;

public class AccountPanel extends JPanel {
    private JLabel profile;
    private JLabel idLabel, nickLabel;
    private JLabel connectionLabel;
    
    public AccountPanel(AccountVO account, boolean isConnected, ImageIcon profileImg) {
        setLayout(null);
        setName(account.getId());

        profile = new JLabel();
        profile.setBounds(1, 30, 50, 50);
        Image resizeImage = profileImg.getImage().getScaledInstance(profile.getWidth(), profile.getHeight(), Image.SCALE_DEFAULT);
        profile.setIcon(new ImageIcon(resizeImage));
        
        nickLabel = new JLabel(account.getNickname());
        idLabel = new JLabel("("+account.getId()+")");
        connectionLabel = new JLabel(isConnected? "立加吝":"厚立加吝");
    

        nickLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        nickLabel.setForeground(Color.black);
        nickLabel.setBounds((int)profile.getBounds().getMaxX(), profile.getY(), 100, 50);
        nickLabel.setHorizontalAlignment(JLabel.CENTER);
        nickLabel.setVerticalAlignment(JLabel.CENTER);
        
        idLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        idLabel.setForeground(new Color(0, 167, 58));
        idLabel.setBounds((int)nickLabel.getBounds().getMaxX(), (int)nickLabel.getBounds().getY(), 75, 50);
        idLabel.setHorizontalAlignment(JLabel.CENTER);
        idLabel.setVerticalAlignment(JLabel.CENTER);
        
        connectionLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        connectionLabel.setForeground(new Color(0, 167, 58));
        connectionLabel.setBounds((int)idLabel.getBounds().getMaxX(), (int)idLabel.getBounds().getY(), 70, 50);

        setBackground(Color.WHITE);
        add(profile);
        add(nickLabel);
        add(idLabel);
        add(connectionLabel);
    }
    public JLabel getIdLabel() {
        return idLabel;
    }
}
