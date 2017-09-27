package etc;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SortingFocusTraversalPolicy;

public class GameFinishCheck {
	ActionListener animate;
	ArrayList<Integer> arrayLocation = new ArrayList<>();
	ArrayList<String> arrayTile = new ArrayList<>();
	PipeImage[] p;
	private String result="GAME OVER";
	
	public GameFinishCheck() {
		
	}

	public boolean start(PipeImage[] pipes) {
		Contents.gameEnd=true;
		p = pipes;
		int location = 70;
		boolean gameSuccess = true;
		int test= 0;
		String code1="V", code2;
//		ImageIcon icon = new ImageIcon("images/progress/pipe_valve.gif");
//		pipes[location].setIcon(icon);
//		icon.getImage().flush(); 
		while (gameSuccess) {
			System.out.print(location);
			arrayLocation.add(location);
			System.out.print(arrayLocation);


			if (pipes[location].isNorth()) {
				code2 = "N";
				System.out.println(code1+code2);
				arrayTile.add(code1+code2);
//				System.out.println(arrayTile);
				
				if ((location - 10) >= 0) {
					location = location - 10;
					if (pipes[location].isSouth()) {
						code1 = "S";
						pipes[location].setSouth(false);
					} else
						break;
				} else {
					break;
				}
			} else if (pipes[location].isSouth()) {
				code2 = "S";
				System.out.println(code1+code2);
				arrayTile.add(code1+code2);
//				System.out.println(arrayTile);
				if ((location + 10) <= 99) {
					location = location + 10;
					if (pipes[location].isNorth()) {
						code1 = "N";
						pipes[location].setNorth(false);
						
					} else
						break;
				} else {
					break;
				}
			} else if (pipes[location].isEast()) {
				code2 = "E";
				System.out.println(code1+code2);
				arrayTile.add(code1+code2);
//				System.out.println(arrayTile);
				if ((location + 1) <= 99) {
					location = location + 1;
					if (pipes[location].isWest()) {
						code1 = "W";
						pipes[location].setWest(false);
					} else
						break;
				} else {
					break;
				}
			}

			else if (pipes[location].isWest()) {
				code2 = "W";
				System.out.println(code1+code2);
				arrayTile.add(code1+code2);
//				System.out.println(arrayTile);
				if ((location - 1) >= 0) {
					location = location - 1;
					if (pipes[location].isEast()) {
						code1 = "E";
						pipes[location].setEast(false);
					} else
						break;
				}
			} else {
				break;
			}
		}
		
		System.out.println(arrayTile);
//		System.out.println(arrayTile.get(0));
		
//		for (int i = 1; i<arrayLocation.size(); i++) {
//			System.out.println(i);
//			pipes[arrayLocation.get(i)].setIcon(new ImageIcon("images/progress/"+arrayTile.get(i)+".gif"));
//		}
		new startThread().start();
//		icon.getImage().flush();
		return gameSuccess;
	} // end start
	
	class startThread extends Thread{
		@Override
		public void run() {
			ImageIcon icon = new ImageIcon("images/progress/pipe_valve.gif");
			p[70].setIcon(icon); // Ã¹¹øÂ°Ä­ ¹°
			for (int i = 1; i<arrayLocation.size(); i++) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(i);
				ImageIcon img = new ImageIcon("images/progress/"+arrayTile.get(i)+".gif");
				p[arrayLocation.get(i)].setIcon(img);
				img.getImage().flush(); // flush animated gif
			}
			if (arrayLocation.get(arrayLocation.size()-1)==28) {
				if (arrayTile.get(arrayTile.size()-1).substring(1).equals(new String("E"))) {
					result = "WIN";
				}
			}
			System.out.println(arrayLocation.size());
			System.out.println("RESULT : " + result);
			
			if (result.equals(new String("WIN"))) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				p[29].setIcon(new ImageIcon("images/progress/pipe_valve2.gif"));
				Contents.finalScore = (int)(Contents.scoreTime + arrayLocation.size()*Contents.countEasy*10/Contents.difficulty);
				System.out.println("FINAL SCORE : " + Contents.finalScore);
			} 
		}
	}
	
	
	
}
