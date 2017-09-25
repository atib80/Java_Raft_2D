/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Viz extends JatekElem {

    public Viz(int x, int y) {
        // super(x, y, ' ', new ImageIcon("Images/viz.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, ' ', Raft.vizImg);

    }

}
