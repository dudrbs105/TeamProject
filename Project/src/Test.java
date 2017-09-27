import java.awt.Image;
import java.net.MalformedURLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Test {
	public static void main(String[] args){
		JFrame jf = new JFrame();
		jf.setSize(500, 500);
		
		JLabel jl = new JLabel("12");
		jf.add(jl);
		
		Icon icon = new ImageIcon("images/progress/pipe_valve.gif");
//		jf.add(new JLabel(icon));
		jl.setIcon(icon);
//		jf.setContentPane(new JLabel(icon));
		
		jf.setVisible(true);
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		
	}
}
