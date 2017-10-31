
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author TuanAnh
 */
public class Play {

    public static void main(String[] args) {
        Terminal terminal = TerminalFacade.createSwingTerminal(100, 30);
        terminal.enterPrivateMode();
        Game game = new Game(terminal);
        char flow = 'm';    //steh fuer mainMenu
        try {
            flow = game.play("mainMenu", 'm');
        } catch (Exception e) {

        }
        while (true) {
            try {
                Thread.currentThread().sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (flow) {
                case 'n':
                    game = new Game(terminal);
                    try {
                        Generate.main(args);
                        flow = game.play("level_big_dense", 'n');
                    } catch (Exception e) {
                    }
                    break;
                case 'l':
                    game = new Game(terminal);
                    try {
                        flow = game.play("save_game", 'n');
                    } catch (Exception e) {

                    }
                    break;
                case 'm':
                    game = new Game(terminal);
                    try {
                        flow = game.play("mainMenu", 'm');
                    } catch (Exception e) {

                    }
                    break;
                case 'e':
                    break;
            }
            if(flow=='e')
                break;
        }

    System.exit(0);
    }
}
