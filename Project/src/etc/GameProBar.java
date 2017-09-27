package etc;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class GameProBar extends JPanel {

	private JProgressBar jbr;

	public GameProBar() {

		jbr = new JProgressBar(0, Contents.difficulty);
		jbr.setValue(Contents.difficulty);
		jbr.setStringPainted(true);
		jbr.setForeground(Color.BLACK);
		
		setLayout(new BorderLayout());
		add(jbr);

		new ProBarThread().start();
	}

	class ProBarThread extends Thread {
		@Override
		public void run() {
			for (int i = Contents.difficulty; i >= 0; i--) {
				if (Contents.gameEarlyEnd == false) {
					jbr.setValue(i);

					try {
						Thread.sleep(1000); // 0.5초마다 스레드 반복
					} catch (InterruptedException e) {
						System.out.println("ProgressBar가 정상 작동하지 않습니다.");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
