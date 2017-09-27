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
						Thread.sleep(1000); // 0.5�ʸ��� ������ �ݺ�
					} catch (InterruptedException e) {
						System.out.println("ProgressBar�� ���� �۵����� �ʽ��ϴ�.");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
