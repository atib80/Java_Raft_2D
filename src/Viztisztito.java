/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Viztisztito extends JatekElem {

    private Fohos fohos;
    private int frameCounter;
    private int poharItalokSzama;

    public Viztisztito(int x, int y, Fohos f) {

        // super(x, y, '%', new ImageIcon("Images/viztisztito.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, '%', Raft.viztisztitoImg);

        this.fohos = f;
        this.frameCounter = 0;
        this.poharItalokSzama = 0;

    }


    public int getPoharItalokSzama() {
        return poharItalokSzama;
    }


    public void updateViztisztito() {

        frameCounter++;

        if (frameCounter == 25) {
            poharItalokSzama++;
            frameCounter = 0;

        }
    }

    public boolean consumeOneDrink() {

        if (poharItalokSzama == 0) return false;

        poharItalokSzama--;

        fohos.csokkenSzomjusag(40);

        return true;

    }
}
