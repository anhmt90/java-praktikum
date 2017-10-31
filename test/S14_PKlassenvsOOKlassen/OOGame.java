package S14_PKlassenvsOOKlassen;

/**
 *
 * @author Anne Brüggemann-Klein
 */
public class OOGame {
    private String[][] gameBoard;
    private String[][] initBoard (int x, int y) {
        String[][] gameBoard = new String[x][y];
        // do something more ...
        return gameBoard;
    }
    public void play() {
        // do something, updating gameBoard
        System.out.println("Playing ...");
        // eventually:
        System.out.println("Game over");
    }
    public OOGame(int x, int y) {
        gameBoard=initBoard(x,y);
    }
}
