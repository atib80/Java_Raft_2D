/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import javax.swing.ImageIcon;
import java.awt.Image;

public class Tuzhely extends JatekElem {

    private Fohos fohos;
    private int frameCounter;
    private int elkeszitettEtelekSzama;
    private boolean isEtelFozesFolyamatElinditva;

    public Tuzhely(int x, int y, Fohos f) {

        // super(x, y, 'W', new ImageIcon("Images/tuzhely2.jpg").getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_DEFAULT));
		super(x, y, 'W', Raft.tuzhelyImg);

        this.fohos = f;
        this.frameCounter = 0;
        this.elkeszitettEtelekSzama = 0;
        this.isEtelFozesFolyamatElinditva = false;

    }

    public int getElkeszitettEtelekSzama() {
        return elkeszitettEtelekSzama;
    }

    public boolean getIsEtelFozesFolyamatElinditva() {
        return isEtelFozesFolyamatElinditva;
    }

    public void setIsEtelFozesFolyamatElinditva(boolean etelFozesFolyamatElinditva) {

        if (isEtelFozesFolyamatElinditva && !etelFozesFolyamatElinditva) frameCounter = 0;

        else if (!isEtelFozesFolyamatElinditva && etelFozesFolyamatElinditva) frameCounter = 0;

        isEtelFozesFolyamatElinditva = etelFozesFolyamatElinditva;
    }

    public void updateTuzhely() {

        if (isEtelFozesFolyamatElinditva) {

            frameCounter++;

            if (frameCounter == 25) {
                elkeszitettEtelekSzama++;
                frameCounter = 0;
                isEtelFozesFolyamatElinditva = false;

            }
        }

    }

    public boolean consumeOneMeal() {

        if (elkeszitettEtelekSzama == 0) return false;

        elkeszitettEtelekSzama--;

        fohos.csokkenEhseg(60);

        return true;

    }
}
