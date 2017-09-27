package etc;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PipeImage extends JLabel {
	private boolean north=false;
	private boolean south=false;
	private boolean east=false;
	private boolean west=false;
	private String imagePath;
	public PipeImage(String imagePath) {
		this.imagePath = imagePath;
		this.setIcon(new ImageIcon(imagePath));
	}
	
	public void setEntrance(String pathToChcek) {
		if(pathToChcek.equals(Contents.PIPE_VALVE)) {
			this.east = true;
		}
		else if(pathToChcek.equals(Contents.PIPE0)) {
			this.south = true;
			this.east = true;
			this.north = false;
			this.west = false;
		}  
		else if(pathToChcek.equals(Contents.PIPE1)) {
			this.west = true;
			this.east = true;
			this.north = false;
			this.south = false;
		}
		else if(pathToChcek.equals(Contents.PIPE2)) {
			this.west = true;
			this.south = true;
			this.east = false;
			this.north = false;
		}
		else if(pathToChcek.equals(Contents.PIPE3)) {
			this.north = true;
			this.west = true;
			this.east = false;
			this.south = false;
		}
		else if(pathToChcek.equals(Contents.PIPE4)) {
			this.north = true;
			this.east = true;
			this.west = false;
			this.south = false;
		}
		else if(pathToChcek.equals(Contents.PIPE5)) {
			this.north = true;
			this.south = true;
		}
	}
	
	public boolean isNorth() {
		return north;
	}
	public void setNorth(boolean north) {
		this.north = north;
	}
	public boolean isSouth() {
		return south;
	}
	public void setSouth(boolean south) {
		this.south = south;
	}
	public boolean isEast() {
		return east;
	}
	public void setEast(boolean east) {
		this.east = east;
	}
	public boolean isWest() {
		return west;
	}
	public void setWest(boolean west) {
		this.west = west;
	}
	
	
}
