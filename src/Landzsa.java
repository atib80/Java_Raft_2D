/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Landzsa extends JatekElem {

    private int frameCounter;
    private boolean isEldobottLandzsaAktiv;

    public Landzsa(int x, int y) {

        // super(x, y, 'A', new ImageIcon("Images/landzsa.png").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, 'A', Raft.landzsaImg);

        frameCounter = 0;
        isEldobottLandzsaAktiv = false;
    }

    public boolean getIsEldobottLandzsaAktiv() {
        return isEldobottLandzsaAktiv;
    }


    public void setIsEldobottLandzsaAktiv(boolean eldobottLandzsaAktiv) {
        isEldobottLandzsaAktiv = eldobottLandzsaAktiv;
    }

    public boolean updateEldobottLandzsaFrameCounter() {

        if (isEldobottLandzsaAktiv) {

            frameCounter++;

            if (frameCounter == 5) {

                isEldobottLandzsaAktiv = false;
                frameCounter = 0;
                return true;

            }

        }

        return false;

    }
}
