package panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import etc.PipeImage;

public class RightPanel extends JPanel{
	JLabel[] lbWatingImage = new JLabel[5];
	private static RightPanel instance = new RightPanel();
	private RightPanel() {
		init();
	}
	
	
	public static RightPanel getInstance () {
		return instance;
	}
	
	private void init() {
		this.setPreferredSize(new Dimension(150, 500));
		this.setOpaque(false);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		for(int i=0; i<5; i++) {
			int ranNumber = (int) (Math.random() * 6);
			lbWatingImage[i] = new PipeImage("images/pipe"+ranNumber+".png");
			lbWatingImage[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			add(lbWatingImage[i]);
			this.add(Box.createVerticalStrut(1));
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.setFont(new Font(null, Font.BOLD, 17));
		g.drawString("END", 0, 155);
		g.setFont(new Font(null, Font.BOLD, 25));
		g.drawString("ก่", 65, 345);
	}
	
	public String getLbWatingImage() {
		return lbWatingImage[4].getIcon().toString();
	}
	
	public void updateWatingImage() {
		for(int i=4; i>=0; i--) {
			if(i==0) {
				int ranNumber = (int) (Math.random() * 6);
				lbWatingImage[i].setIcon(new ImageIcon("images/pipe"+ranNumber+".png"));
			}
			else {
				lbWatingImage[i].setIcon(new ImageIcon(lbWatingImage[i-1].getIcon().toString()));
			}
		}
	}
	
}
