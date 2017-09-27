package button;

import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

public class CloseButton extends Button {
    ObjectOutputStream oos;
    public CloseButton(ObjectOutputStream oos, JFrame nowFrame, String nextFrameName ,int x, int y) {
        this.oos = oos;
        setLabel("X");
        setBackground(new Color(0, 168, 59));
        setForeground(Color.WHITE);
        
        setBounds(x-25, y, 25, 23);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    if(oos != null) {
                        oos.writeObject("[EXIT]");
                    } else {
                        if(nowFrame != null) {
                            if(nextFrameName != null) {
                                Class.forName(nextFrameName).newInstance();
                            }
                            nowFrame.dispose();
                            return;
                        }
                    }
                } catch(IOException ioe) {
                    // TODO Auto-generated catch block
                    ioe.printStackTrace();
                } catch(InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch(IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }
}
