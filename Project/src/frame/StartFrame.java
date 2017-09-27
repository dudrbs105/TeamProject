package frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import panel.StartPanel;

public class StartFrame extends JFrame{

	public StartFrame() {
		init();
	}
	
	private void init() {
		this.add(new StartPanel());
		this.setSize(900, 755);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
		addKeyListener(listener);
	}
	
	KeyListener listener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
		}
		
		@Override
		public void keyReleased(KeyEvent e) {			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			StartFrame.this.dispose(); // 해당 프레임만 종료 = dispose()메소드
			new MainFrame();
		}
	};
	
}
