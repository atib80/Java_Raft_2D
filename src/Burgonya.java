/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Burgonya extends JatekElem {

    public Burgonya(int x, int y) {

        // super(x, y, 'B', new ImageIcon("Images/burgonya.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, 'B', Raft.burgonyaImg);

    }
}
