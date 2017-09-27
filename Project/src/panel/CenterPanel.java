package panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import etc.Contents;
import etc.GameFinishCheck;
import etc.PipeImage;

public class CenterPanel extends JPanel implements MouseListener {
	PipeImage[] lbPipeImage = new PipeImage[100];
	GameFinishCheck finishCheck;
	public CenterPanel() {
		init();
		finishCheck = new GameFinishCheck();
		new countThread().start();
	}

	private void init() {
		this.setBackground(Color.LIGHT_GRAY);
		this.setPreferredSize(new Dimension(600, 600));
		super.setLayout(new GridLayout(10, 10, 1, 1));

		for (int i = 0; i < 100; i++) {
			if (i == 70) {
				lbPipeImage[i] = new PipeImage(Contents.PIPE_VALVE);
				lbPipeImage[i].setEast(true);
			} else if (i == 29) {
				lbPipeImage[i] = new PipeImage(Contents.PIPE_VALVE);
			} else {
				lbPipeImage[i] = new PipeImage(Contents.PIPE_NONE);
			}
			lbPipeImage[i].addMouseListener(this);
			add(lbPipeImage[i]);
			
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (Contents.gameEnd==false) {
			if (e.getSource() instanceof PipeImage) {
				StringTokenizer stk = new StringTokenizer(((PipeImage) e.getSource()).getIcon().toString());
				((PipeImage) e.getSource()).setIcon(new ImageIcon(stk.nextToken(".") + "_pressed.png"));
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (Contents.gameEnd==false) {
			if (e.getSource() instanceof PipeImage) {
				if (((PipeImage) e.getSource()).getIcon().toString().equals("images/pipe_valve_pressed.png")) {
					((PipeImage) e.getSource()).setIcon(new ImageIcon(Contents.PIPE_VALVE));
					// System.out.println(finishCheck.start(lbPipeImage));
					finishCheck.start(lbPipeImage);
					Contents.gameEarlyEnd = true;
				} else {
					((PipeImage) e.getSource()).setIcon(new ImageIcon(RightPanel.getInstance().getLbWatingImage()));
					((PipeImage) e.getSource()).setEntrance(((PipeImage) e.getSource()).getIcon().toString());
					RightPanel.getInstance().updateWatingImage();
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	class countThread extends Thread{
		@Override
		public void run() {
			try {
				sleep(Contents.difficulty*1000);
				if (Contents.gameEarlyEnd==false)
					finishCheck.start(lbPipeImage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
