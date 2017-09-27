package panel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import etc.GameProBar;
import etc.GameTimer;

public class TopPanel extends JPanel {
	private GameTimer timerPanel;
	private GameProBar proBarPanel;

	public TopPanel() {

		timerPanel = new GameTimer();
		setLayout(null);
		this.add(timerPanel);
		timerPanel.setBounds(10, 5, 120, 55);

		proBarPanel = new GameProBar();
		this.add(proBarPanel);
		proBarPanel.setBounds(313, 10, 300, 40);

		this.setBackground(Color.RED);
		this.setSize(800, 100);
		this.setPreferredSize(new Dimension(900, 60));
		this.setOpaque(false);

	}

}