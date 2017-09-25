/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Hal extends JatekElem {

    public Hal(int x, int y) {

        // super(x, y, '^', new ImageIcon("Images/hal.png").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, '^', Raft.halImg);

    }
}
