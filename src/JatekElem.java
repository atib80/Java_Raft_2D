import java.awt.Image;

/**
 * @author Kovács Attila
 * @version 1.0 *
 */

public abstract class JatekElem {

    public Position position;
    private char drawChar;
    private Image drawImage;

    public JatekElem(int x, int y, char drawChar, Image img) {

        this.position = new Position(x, y);
        this.drawChar = drawChar;
        this.drawImage = img;

    }

    public char getDrawChar() {
        return drawChar;
    }

    public void setDrawChar(char drawChar) {
        this.drawChar = drawChar;
    }

    public Image getDrawImage() {
        return this.drawImage;
    }

    public void setDrawImage(Image image) {
        this.drawImage = image;
    }
}
