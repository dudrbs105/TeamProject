package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class StartPanel extends JPanel{
	ImageIcon icon;
	
	public StartPanel() {
		init();
	}
	
	private void init() {
		this.setBackground(Color.black);
		this.setLayout(new BorderLayout(0, 0));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		icon = new ImageIcon("images/Pipe_Dream_main.gif");
		Dimension d = getSize();
		g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
		setOpaque(false);
		super.paintComponent(g);
	}
	
}
