/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Capa extends JatekElem {


    public Capa(int x, int y) {

		// super(x, y, '>',  new ImageIcon("Images/capa2.png").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, '>', Raft.capaImg);
    }

}
