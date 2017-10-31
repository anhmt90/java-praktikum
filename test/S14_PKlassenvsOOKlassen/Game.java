package S14_PKlassenvsOOKlassen;

/**
 *
 * @author Anne Brüggemann-Klein
 */

public class Game {
    private static String[][] gameBoard;
    public static String[][] initBoard (int x, int y) {
        String[][] gameBoard = new String[x][y];
        // do something more ...
        return gameBoard;
    }
    public static void play() {
        // do something, updating gameBoard
        System.out.println("Playing ...");
        // eventually:
        System.out.println("Game over");
    }
    public static void main(String[] args) {
        gameBoard=initBoard(8,10);
        play();
    }
}
