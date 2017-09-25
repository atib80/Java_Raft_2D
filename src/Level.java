/**
 * @author Kovács Attila
 * @version 1.0
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Level extends JatekElem {

    public Level(int x, int y) {

        // super(x, y, 'V', new ImageIcon("Images/level.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, 'V', Raft.levelImg);
		
    }
}
