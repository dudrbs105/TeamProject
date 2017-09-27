package etc;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class AnimationThread implements Runnable {
	PipeImage[] pipes;
	public AnimationThread(PipeImage[] pipes) {
		this.pipes = pipes;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
//	@Override
//	public void run() {
//		int location = 70;
		//animation = new AnimationThread(pipes);
		//animation.run();
//		boolean gameSuccess = true;
//		while (true) {
			
//			System.out.println(location);
//			imageAnimation(pipes[location]);
//			if (pipes[location].isNorth()) {
//				if ((location - 10) >= 0) {
//					location = location - 10;
//					if (pipes[location].isSouth()) {
//						pipes[location].setSouth(false);
//					} else
//						return;
//				} else {
//					return;
//				}
//			} else if (pipes[location].isSouth()) {
//				if ((location + 10) <= 99) {
//					location = location + 10;
//					if (pipes[location].isNorth()) {
//						pipes[location].setNorth(false);
//					} else
//						return;
//				} else {
//					return;
//				}
//			} else if (pipes[location].isEast()) {
//				if ((location + 1) <= 99) {
//					location = location + 1;
//					if (pipes[location].isWest()) {
//						pipes[location].setWest(false);
//					} else
//						return;
//				} else {
//					return;
//				}
//			}
//
//			else if (pipes[location].isWest()) {
//				if ((location - 1) >= 0) {
//					location = location - 1;
//					if (pipes[location].isEast()) {
//						pipes[location].setEast(false);
//					} else
//						return;
//				}
//			} else {
//				return;
//			}
//		}
//	}
//	
//	private void imageAnimation(PipeImage pipe) {
//		String currentImage = pipe.getIcon().toString();
//		String progressImage = null;
//		
//		if(currentImage.equals(Contents.PIPE_VALVE)) {
//			progressImage = "images/progress/pipe_valve";
//		}
//		else if(currentImage.equals(Contents.PIPE0)) {
//		}  
//		else if(currentImage.equals(Contents.PIPE1)) {
//		}
//		else if(currentImage.equals(Contents.PIPE2)) {
//		}
//		else if(currentImage.equals(Contents.PIPE3)) {
//		}
//		else if(currentImage.equals(Contents.PIPE4)) {
//		}
//		else if(currentImage.equals(Contents.PIPE5)) {
//		}
//		for(int i=0; i<20; i++) {
//			System.out.println("test");
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			pipe.setIcon(new ImageIcon(progressImage+"_"+i+".png"));
//			System.out.println(progressImage+"_"+i+".png");
//		}
//	}
//}
//