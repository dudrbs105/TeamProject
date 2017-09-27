import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Misc {
    BufferedImage[] crazyDuckFrames;
 
    public static void main(String[] args) {
        new Misc();
    }
 
    public Misc() {
        loadSprites();
        Animation a = new Animation(crazyDuckFrames);
        javax.swing.Timer t = new javax.swing.Timer(250, a);
        t.start();
    }
 
    private void loadSprites(){
        crazyDuckFrames = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            try {
                ImageIO.read(new File("sprites/" + i + "duck.png"));
            } catch (IOException ex) {
                Logger.getLogger(Misc.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Do this for each sprite type
    }
 
    private class Animation implements ActionListener{
        BufferedImage[] frames;
        int index = 0;
 
        public Animation(BufferedImage[] frames){
            this.frames = frames;
        }
 
        public void actionPerformed(ActionEvent e) {
            if(index < frames.length-1){
                index++;
            }else{
                index = 0;
                // or you could reverse direction and cycle back and forth to
                // save frames.
            }
        }
 
        public BufferedImage getFrame(){
            return frames[index];
        }
 
    }
}