package button;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class MinimizeButton extends Button {
    public MinimizeButton(int x, int y, JFrame miniBtn) {
        setLabel("_");
        setBackground(new Color(0, 168, 59));
        setForeground(Color.WHITE);
        
        setBounds(x-25, y, 25, 23);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                miniBtn.setState(JFrame.ICONIFIED);
            }
        }); 
    }
}
