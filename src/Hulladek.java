/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Hulladek extends JatekElem {

    public Hulladek(int x, int y) {

        // super(x, y, '@', new ImageIcon("Images/hulladek.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, '@', Raft.hulladekImg);

    }
}
