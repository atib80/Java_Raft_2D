/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Hordo extends JatekElem {

    public Hordo(int x, int y) {

        // super(x, y, 'U', new ImageIcon("Images/hordo.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, 'U', Raft.hordoImg);
    }
}
