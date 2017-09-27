package panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import etc.Contents;

public class LeftPanel extends JPanel{
	private JLabel nickName1;
	private JLabel nickName2;
	private JLabel level1;
	private JLabel level2;
	private JLabel score1;
	private scoreTimer score2;
	private String difficulty;
	private int score=0;
	
	public LeftPanel() {
		init();
	}
	
	public void init() {
		this.setSize(100,500);
		this.setPreferredSize(new Dimension(150, 500));
		this.setOpaque(false);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		if (Contents.difficulty==120)
			difficulty = "EASY";
		if (Contents.difficulty==60)
			difficulty = "NORMAL";
		if (Contents.difficulty==30)
			difficulty = "HARD";
		if (Contents.difficulty==10)
			difficulty = "NIGHTMARE";
		
		nickName1 = new JLabel("NICK NAME");
		nickName2 = new JLabel(Contents.nickName);
		level1 = new JLabel("LEVEL");
		level2 = new JLabel(difficulty);
		score1 = new JLabel("TIME SCORE");
		score2 = new scoreTimer();
		
		nickName1.setAlignmentX(Component.CENTER_ALIGNMENT);
		nickName2.setAlignmentX(Component.CENTER_ALIGNMENT);
		level1.setAlignmentX(Component.CENTER_ALIGNMENT);
		level2.setAlignmentX(Component.CENTER_ALIGNMENT);
		score1.setAlignmentX(Component.CENTER_ALIGNMENT);
		score2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		nickName1.setFont(new Font(null, Font.BOLD, 23));
		nickName2.setFont(new Font(null, Font.BOLD, 23));
		level1.setFont(new Font(null, Font.BOLD, 23));
		level2.setFont(new Font(null, Font.BOLD, 23));
		score1.setFont(new Font(null, Font.BOLD, 23));
		score2.setFont(new Font(null, Font.BOLD, 23));
		
		level1.setForeground(Color.MAGENTA);
		level2.setForeground(Color.MAGENTA);
		score1.setForeground(Color.BLUE);
		score2.setForeground(Color.BLUE);
		
		this.add(Box.createVerticalStrut(20));
		add(nickName1);
		add(nickName2);
		this.add(Box.createVerticalStrut(20));
		add(level1);
		add(level2);
		this.add(Box.createVerticalStrut(20));
		add(score1);
		add(score2);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.red);
		g.setFont(new Font(null, Font.BOLD, 17));
		g.drawString("START", 93, 455);
//		g.setFont(new Font(null, Font.BOLD, 25));
//		g.drawString("ก่", 65, 345);
	}
}

class scoreTimer extends JLabel {
	public scoreTimer() {
		new scoreTimeThread().start();
	}
	
	class scoreTimeThread extends Thread {
		@Override
		public void run() {
			while (Contents.scoreTime >= 0) {
				if (Contents.gameEarlyEnd == false) {
					setText((int) Contents.scoreTime + "");
					Contents.scoreTime = Contents.scoreTime - (Contents.scoreTimeTotal / Contents.difficulty);
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
				Contents.scoreTime = 0;
				setText((int) Contents.scoreTime + "");
		}
	}
}