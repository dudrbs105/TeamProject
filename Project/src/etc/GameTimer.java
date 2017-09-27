package etc;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameTimer extends JPanel {
	private JLabel lbMinute;
	private JLabel lbTmp;
	private JLabel lbSecond;

	public GameTimer() {
		lbMinute = new JLabel("2");
		lbTmp = new JLabel(":");
		lbSecond = new JLabel("00");

		lbMinute.setForeground(Color.WHITE);
		lbMinute.setFont(new Font("", Font.BOLD, 40));
		lbTmp.setForeground(Color.WHITE);
		lbTmp.setFont(new Font("", Font.BOLD, 40));
		lbSecond.setForeground(Color.WHITE);
		lbSecond.setFont(new Font("", Font.BOLD, 40));

		add(lbMinute);
		add(lbTmp);
		add(lbSecond);

		setBackground(Color.BLACK);

		new TimerThread().start();
	}

	class TimerThread extends Thread {
		@Override
		public void run() {
			for (int i = Contents.difficulty; i >= 0; i--) {
				if (Contents.gameEarlyEnd==false) {
					int min = i / 60; // 분 계산
					int sec = i % 60; // 초 계산
					lbMinute.setText(min + "");
					lbSecond.setText(sec + "");

					try {
						Thread.sleep(1000); // 1초마다 스레드 반복
					} catch (InterruptedException e) {
						System.out.println("타이머가 정상 작동하지 않습니다.");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
