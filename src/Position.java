/**
 * @author Kovács Attila
 * @version 1.0 *
 */

public class Position {

    public int x;
    public int y;

    public Position(int x, int y) {

        this.x = x;
        this.y = y;

    }

    @Override
    public String toString() {
        return String.format("Position: {x=%d, y=%d}", x, y);
    }


}
