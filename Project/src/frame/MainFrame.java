package frame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import panel.BottomPanel;
import panel.CenterPanel;
import panel.LeftPanel;
import panel.BackgroundPanel;
import panel.RightPanel;
import panel.TopPanel;

public class MainFrame extends JFrame {
	public MainFrame() {
		this.init();
	}

	public void init() {
		this.add(new BackgroundPanel());
		this.setSize(1000, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.pack();
		this.setLocationRelativeTo(null); //실행할때 화면 중앙에 뜨게 함	}
	}

}
