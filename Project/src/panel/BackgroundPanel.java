package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	ImageIcon icon;

	public BackgroundPanel() {
		init();
	}

	private void init() {
		this.setBackground(Color.black);
		this.setLayout(new BorderLayout(0, 0));
		this.add(new CenterPanel(), BorderLayout.CENTER);
		this.add(new LeftPanel(), BorderLayout.LINE_START);
		this.add(RightPanel.getInstance(), BorderLayout.LINE_END);
		this.add(new TopPanel(), BorderLayout.PAGE_START);
		this.add(new BottomPanel(), BorderLayout.PAGE_END);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		icon = new ImageIcon("images/img_mainPanel.jpg");
		Dimension d = getSize();
		g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
		setOpaque(false);
		super.paintComponent(g);
		
	}
}
