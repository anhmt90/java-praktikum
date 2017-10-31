
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

/**
 *
 * @author TuanAnh
 */
public class Game extends Thread {

    private static GameObject[][] gameBoard;
    private static Terminal terminal;
    private static TerminalSize ts;
    private static Player player;
    private static boolean isOver = false;
    private static ArrayList<Monster> monArr;
    private static boolean suspend;
    private static Object LOCK;
    private static boolean killMonThreads = false;
    private static char c;  //Variable zur Kontrolle des Kontrolfluss des Programms
    private Monster monster;
    static int[] appear = new int[2];
    private static int displayX = 0;       //x von links obersten Koordinate des TerminalSize-Blocks
    private static int displayY = 0;       //y von links obersten Koordinate des TerminalSize-Blocks

    Game game;

    public Game(Terminal terminal) {
        this.terminal = terminal;
        terminal.setCursorVisible(false);
        ts = terminal.getTerminalSize();
        monArr = new ArrayList<>();
        isOver = false;
        LOCK = new Object();
    }

    //privater Konstruktor zum Erzeugen der Monsters
    private Game(Monster monster) {
        this.monster = monster;
    }

    //message() zur Verarbeitung der Stringausgaben
    public void message(String s, Terminal ter) {
        char[] message = new char[s.length()];
        s.getChars(0, s.length(), message, 0);
        for (int i = 0; i < message.length; i++) {
            ter.putCharacter(message[i]);
        }
    }

    //writeStatus() zur Anzeige des Zustands vom Player
    public void writeStatus() {
        terminal.moveCursor(0, 0);
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        message("Life: " + player.getLife(), terminal);
        terminal.moveCursor(20, 0);
        if (player.isGotKey()) {
            message("Key: ", terminal);
            terminal.applyForegroundColor(Terminal.Color.YELLOW);
            message("" + '\u2640', terminal);
        } else {
            message("Key: (none)", terminal);
        }

    }

    //legend() zum Ausdrucken der Lengedentabelle
    public void legend() {
        pause();
        Terminal legend = TerminalFacade.createSwingTerminal(75, 16);
        legend.enterPrivateMode();
        legend.setCursorVisible(false);
        legend.moveCursor(10, 3);
        legend.applyForegroundColor(Terminal.Color.GREEN);
        legend.putCharacter('\u263A');
        message("     Player", legend);
        legend.applyForegroundColor(Terminal.Color.DEFAULT);
        message(" (It's you :))", legend);

        legend.moveCursor(10, 5);
        legend.applyForegroundColor(Terminal.Color.RED);
        legend.putCharacter('\u2620');
        message("     Monster", legend);
        legend.applyForegroundColor(Terminal.Color.DEFAULT);
        message(" (They will chase and kill you)", legend);

        legend.moveCursor(10, 7);
        legend.applyForegroundColor(Terminal.Color.RED);
        legend.putCharacter('\u2600');
        message("     Trap", legend);
        legend.applyForegroundColor(Terminal.Color.DEFAULT);
        message(" (You will die when you step on)", legend);

        legend.moveCursor(10, 9);
        legend.applyForegroundColor(Terminal.Color.YELLOW);
        legend.putCharacter('\u2640');
        message("     Golden key", legend);
        legend.applyForegroundColor(Terminal.Color.DEFAULT);
        message(" (You must have one to open the exit door)", legend);

        legend.moveCursor(10, 11);
        legend.applyForegroundColor(Terminal.Color.MAGENTA);
        legend.putCharacter('O');
        message("     Exit door", legend);
        legend.applyForegroundColor(Terminal.Color.DEFAULT);
        message(" (If you can open one of them, you will win)", legend);

        //warte auf eine Keyeingabe von dem Spieler
        Key key = null;
        while (key == null) {
            key = legend.readInput();
        }
        legend.exitPrivateMode();
    }

    //popUp() zur Anzeige der Meldungen mit einem kleineren zusaetzlichen Terminal 
    public void popUp(String s, ArrayList<String> strArr, int col, int row) {
        pause();
        Terminal popUp = TerminalFacade.createSwingTerminal(col, row);
        popUp.enterPrivateMode();
        popUp.setCursorVisible(false);

        if (strArr == null) {
            popUp.moveCursor(3, 1);
            message(s, popUp);
        } else {
            for (int i = 0; i < strArr.size(); i++) {
                popUp.moveCursor(10, i + 3);
                message(strArr.get(i), popUp);
            }
        }
        Key key = null;
        while (key == null) {
            key = popUp.readInput();
        }
        goOn();
        popUp.exitPrivateMode();
    }

    //saveGame() zum Speichern des laufenden Spiels
    public void saveGame() throws IOException {
        File saveFile = new File("save_game.properties");
        FileWriter writer = new FileWriter(saveFile);;
        Properties prop = new Properties();
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                String s = "";
                if (gameBoard[i][j] == null) {
                    continue;
                } else if (gameBoard[i][j] instanceof Wall) {
                    s = "0";
                } else if (gameBoard[i][j] instanceof ExitDoor) {
                    s = "2";
                } else if (gameBoard[i][j] instanceof Trap) {
                    s = "3";
                } else if (gameBoard[i][j] instanceof Monster) {
                    s = "4";
                } else if (gameBoard[i][j] instanceof GoldenKey) {
                    s = "5";
                } else if (gameBoard[i][j] instanceof Player) {
                    s = "Player";
                }
                prop.setProperty(j + "," + i, s);
            }
        }
        prop.setProperty(appear[1] + "," + appear[0], "1");
        prop.setProperty("Life", "" + player.getLife());
        prop.setProperty("gotKey", "" + player.isGotKey());
        prop.setProperty("Height", "" + gameBoard.length);
        prop.setProperty("Width", "" + gameBoard[0].length);
        prop.store(writer, "--No-Comment--");
    }

    //loadGame() zum Laden des gespeicherten Spiels
    public void loadGame() {
        goOn();
        killMonThreads = true;
        isOver = true;
        c = 'l';
    }

    //playMenu() zur Anzeige des Spielmenü/Option wenn der Spieler die TAB-Taste drueckt
    public void playMenu() {
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.clearScreen();
        int choose = 8;
        terminal.moveCursor(45, 6);
        message("OPTION", terminal);
        terminal.moveCursor(30, 8);
        message("Resume", terminal);
        terminal.moveCursor(30, 10);
        message("Load Game", terminal);
        terminal.moveCursor(30, 12);
        message("Save Game", terminal);
        terminal.moveCursor(30, 14);
        message("Main menu (unsaved game will be lost!)", terminal);
        terminal.moveCursor(30, 20);
        message("[Press ENTER to choose]", terminal);
        terminal.moveCursor(27, choose);
        terminal.putCharacter('>');
        boolean outMenu = false;
        while (outMenu == false) {
            Key key = terminal.readInput();
            while (key == null) {
                key = terminal.readInput();
            }
            //terminal.putCharacter(key.getCharacter());
            switch (key.getKind()) {
                case Escape:        //Beende das Programm
                    terminal.exitPrivateMode();
                    outMenu = true;
                    isOver = true;
                    c = 'e';
                    break;
                case Tab:       //Mach das playMenu aus und weiter spielen
                    terminal.clearScreen();
                    outMenu = true;
                    break;
                case ArrowDown:
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter(' ');
                    choose = Math.min(choose + 2, 14);
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter('>');
                    break;
                case ArrowUp:
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter(' ');
                    choose = Math.max(choose - 2, 8);
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter('>');
                    break;
                case Enter:
                    switch (choose) {
                        case 8:
                            terminal.clearScreen();
                            outMenu = true;
                            break;
                        case 10:
                            outMenu = true;
                            goOn();
                            loadGame();
                            break;
                        case 12:
                            try {
                                saveGame();
                                popUp("Game saved!", null, 40, 4);
                                outMenu = true;
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            terminal.clearScreen();
                            break;
                        case 14:
                            outMenu = true;
                            killMonThreads = true;
                            isOver = true;
                            monArr.clear();
                            c = 'm';
                            break;
                    }
            }
        }
    }

    //constructPath() zum Aufbau von dem kuerzsten Weg vom Monster bis Player
    //Diese Funktion ist ein Teil von dem A* Algorithmus 
    public LinkedList<ObjectPosition> constructPath(ObjectPosition currSquare) {
        ObjectPosition node = currSquare;
        LinkedList<ObjectPosition> path = new LinkedList<ObjectPosition>();
        while (node.getParent() != monster.getPos()) {
            path.addFirst(node);
            node = node.getParent();
        }
        path.add(monster.getPos());
        return path;
    }

    //run(): die erzeugten Monster-Threads laufen in dieser Methode
    @Override
    public void run() {
        gameBoard[monster.getX()][monster.getY()] = monster;
        drawObject(monster.getX() % ts.getRows(), monster.getY() % ts.getColumns(), monster.getSymbol());
        int monSpeed = 700;  //je mehr desto langsamer 

        while (true) {
            synchronized (this) {
                if (isOver == true || killMonThreads == true) {
                    return;
                }
            }

            System.out.print("\n" + Thread.currentThread().getName() + " runs...");
            int goalX = player.getPos().getX();
            int goalY = player.getPos().getY();

            ////////A* Algorithmus Implimentierung
            LinkedList<ObjectPosition> openList = new LinkedList<>();
            LinkedList<ObjectPosition> closedList = new LinkedList<>();

            ObjectPosition currSquare = monster.getPos();
            currSquare.setGScore(0);
            currSquare.setHScore(player.getPos());
            currSquare.setFScore();
            currSquare.setParent(currSquare);
            openList.add(monster.getPos());

            while (openList.size() > 0) {
                double minF = 0;
                boolean first = true;
                for (ObjectPosition curr : openList) {
                    if (first) {
                        minF = curr.getFScore();
                        first = false;
                    }
                    if (minF >= curr.getFScore()) {
                        minF = curr.getFScore();
                        currSquare = curr;      // Get the square with the lowest F score
                    }
                }
                openList.remove(currSquare);    // remove it to the open list
                if (currSquare.isSamePosition(player.getPos())) {
                    closedList = constructPath(currSquare);
                    break;  // PATH FOUND
                }

                ArrayList<ObjectPosition> adjacentSquares = new ArrayList<>();
                try {
                    if (gameBoard[currSquare.getX() + 1][currSquare.getY()] == null
                            || gameBoard[currSquare.getX() + 1][currSquare.getY()] instanceof Player) {  // below tile
                        adjacentSquares.add(new ObjectPosition(currSquare.getX() + 1, currSquare.getY(), player.getPos()));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    if (gameBoard[currSquare.getX() - 1][currSquare.getY()] == null
                            || gameBoard[currSquare.getX() - 1][currSquare.getY()] instanceof Player) { //above tile
                        adjacentSquares.add(new ObjectPosition(currSquare.getX() - 1, currSquare.getY(), player.getPos()));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    if (gameBoard[currSquare.getX()][currSquare.getY() + 1] == null
                            || gameBoard[currSquare.getX()][currSquare.getY() + 1] instanceof Player) { //right tile
                        adjacentSquares.add(new ObjectPosition(currSquare.getX(), currSquare.getY() + 1, player.getPos()));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    if (gameBoard[currSquare.getX()][currSquare.getY() - 1] == null
                            || gameBoard[currSquare.getX()][currSquare.getY() - 1] instanceof Player) { // left tile
                        adjacentSquares.add(new ObjectPosition(currSquare.getX(), currSquare.getY() - 1, player.getPos()));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                for (ObjectPosition adj : adjacentSquares) {

                    int dist = currSquare.getGScore() + 1;
                    //////////////////////////////////////////////
                    boolean inOpenList = false;
                    int index = -1;
                    for (ObjectPosition curr : openList) {
                        if (curr.isSamePosition(adj)) {
                            index = openList.indexOf(curr);
                            inOpenList = true;
                            break;
                        }
                    }
                    if (inOpenList && adj.getGScore() > dist) {
                        adj.setGScore(dist);
                        adj.setFScore();
                        adj.setParent(currSquare);
                    } else if (adj.getParent() == null) {
                        adj.setGScore(dist);
                        adj.setFScore();
                        adj.setParent(currSquare);
                        openList.add(adj);
                    }

                }
            }

            for (ObjectPosition toward : closedList) {
                if (goalX != player.getPos().getX() || goalY != player.getPos().getY()) {
                    closedList.clear();
                    break;
                }
                moveMonster(toward.getX(), toward.getY());
                synchronized (this) {
                    if (isOver == true || killMonThreads == true) {
                        return;
                    }
                }
                try {
                    Thread.sleep(monSpeed);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

        }
    }

    //respawn() the player. the player will stand at the entrance after he has lost 1 life
    public void respawn() {
        terminal.moveCursor(player.getY(), player.getX());
        terminal.putCharacter(' ');
        gameBoard[player.getX()][player.getY()] = null;

        player.setPos(appear[0], appear[1]);
        gameBoard[appear[0]][appear[1]] = player;
        drawObject(appear[0], appear[1], player.getSymbol());
        terminal.flush();

    }

    //moveMonster() fuehrt die Bewegung der Monsters aus
    public void moveMonster(int x, int y) {
        synchronized (this) {
            if (gameBoard[x][y] != null) {
                char sym = gameBoard[x][y].getSymbol();     //nehm das Symbol das naechste Object
                switch (sym) {
                    case 'X': //Wall
                    case 'O':  //ExitDoor
                    case '\u2600':  //Trap
                    case '\u2640':  //Key
                        return;
                    case '\u263A':  //Player
                        player.setLife(player.getLife() - 1);
                        respawn();
                        refresh();
                        popUp("You lost 1 life!", null, 50, 10);
                        if (player.getLife() == 0) {
                            isOver = true;
                            popUp("You have no life more! Game Over!", null, 50, 10);
                            exit();
                        }
                        break;
                    case '\u2620':  //Monster
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                }
            }
        }

        synchronized (LOCK) {
            System.out.println("suspend = " + suspend);
            if (suspend) {
                try {
                    System.out.println("wait: " + Thread.currentThread().getName());
                    LOCK.wait();
                    System.out.println("waiting completed: " + Thread.currentThread().getName());
                    Thread.currentThread().sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        synchronized (this) {
            if (x > gameBoard.length - 1 || x < 0 || (y > gameBoard[0].length - 1) || y < 0
                    || (x == appear[0] && y == appear[1])) {
                return;
            }

            if ((ts.getRows() >= gameBoard.length && ts.getColumns() >= gameBoard[0].length) == true) {
                if (!(monster.getX() < displayX && monster.getX() > displayX + ts.getRows())
                        && !(monster.getY() < displayY && monster.getY() > displayY + ts.getColumns())) {      // wenn x von Player zwischen jeweils displayX und displayX+ts.Rows liegt. Analog für y
                    //loesche Monster in der alten Zell
                    drawObject(monster.getX(), monster.getY(), ' ');
                    drawObject(x, y, monster.getSymbol());
                }
            }

            gameBoard[monster.getX()][monster.getY()] = null;
            //copy und male Monster zum naechten Zell
            gameBoard[x][y] = monster;
            monster.setPos(x, y);
            //drawObject(x % ts.getRows(), y % ts.getColumns(), monster.getSymbol());

            //drawObject(monster.getX() % ts.getRows(), monster.getY() % ts.getColumns(), ' ');
        }
    }

    //move() fuehrt die Bewegung des Spielers aus
    public void move(char d) {     //d stands for direction
        int x = player.getX();
        int y = player.getY();
        int lifeTemp = player.getLife();
        switch (d) {
            case 'u':
                x -= 1;
                break;
            case 'd':
                x += 1;
                break;
            case 'r':
                y += 1;
                break;
            case 'l':
                y -= 1;
                break;
        }

        if (x > gameBoard.length - 1 || x < 0 || (y > gameBoard[0].length - 1) || y < 0) {
            return;
        }

        if (gameBoard[x][y] != null) {
            char sym = gameBoard[x][y].getSymbol();     //nehm das Symbol das naechste Object
            switch (sym) {
                case 'X': //Wall
                    return;
                case 'O':  //ExitDoor
                    if (player.isGotKey() == false) {
                        popUp("You must find a key!", null, 30, 4);
                        return;
                    } else {
                        popUp("You win!", null, 30, 4);
                        exit();
                        break;
                    }
                case '\u2600':  //Trap
                case '\u2620':  //Monster
                    player.setLife(player.getLife() - 1);
                    popUp("You have lost 1 life", null, 50, 10);
                    refresh();
                    if (player.getLife() == 0) {
                        popUp("You have no life more! Game Over!", null, 50, 10);
                        exit();
                    }
                    break;
                case '\u2640':  //Key
                    player.setGotKey(true);
                    refresh();
                    popUp("You found a key!", null, 30, 4);
                    break;
            }
            if (player.getLife() < lifeTemp) {
                terminal.moveCursor(player.getY(), player.getX());
                terminal.putCharacter(' ');
                gameBoard[player.getX()][player.getY()] = null;

                player.setPos(appear[0], appear[1]);
                gameBoard[appear[0]][appear[1]] = player;
                drawObject(appear[0], appear[1], player.getSymbol());
                return;
            }
        }
        ///////////////////////////////////////////////////

        gameBoard[x][y] = gameBoard[player.getX()][player.getY()];
        drawObject(x % ts.getRows(), y % ts.getColumns(), player.getSymbol());

        gameBoard[player.getX()][player.getY()] = null;
        drawObject(player.getX() % ts.getRows(), player.getY() % ts.getColumns(), ' ');
        player.setPos(x, y);

//        try{
//            sleep(200);
//        }catch(Exception e){
//            
//        }
    }

    //loadField() zum Erzeugen des gameBoards und zum Laden aller GameObjekte in den gameBoard
    public void loadField(Properties prop) {
        gameBoard = new GameObject[Integer.parseInt(prop.getProperty("Height"))][Integer.parseInt(prop.getProperty("Width"))];
        Enumeration en = prop.keys();
        int monNo = 0;
        while (en.hasMoreElements()) {
            String s = (String) en.nextElement();
            if (s.equals("Height") || s.equals("Width") || s.equals("Life") || s.equals("gotKey")) {
                continue;
            }
            String[] posStr = s.split("\\W");
            int[] posInt = {Integer.parseInt(posStr[1]), Integer.parseInt(posStr[0])}; //Achtung: 1 zuerst dann 0

            switch (prop.getProperty(s)) {
                case "0":
                    Wall wall = new Wall(new ObjectPosition(posInt[0], posInt[1]));
                    gameBoard[posInt[0]][posInt[1]] = wall;
                    break;
                case "1":
                    Entrance entrance = new Entrance(new ObjectPosition(posInt[0], posInt[1]));
                    appear[0] = entrance.getX();
                    appear[1] = entrance.getY();
                    break;
                case "2":
                    ExitDoor ed = new ExitDoor(new ObjectPosition(posInt[0], posInt[1]));
                    gameBoard[posInt[0]][posInt[1]] = ed;
                    break;
                case "3":
                    Trap trap = new Trap(new ObjectPosition(posInt[0], posInt[1]));
                    gameBoard[posInt[0]][posInt[1]] = trap;
                    break;
                case "4":
                    Monster mon = new Monster(new ObjectPosition(posInt[0], posInt[1]), "Monster-" + (monNo++));
                    monArr.add(mon);
                    break;
                case "5":
                    GoldenKey gk = new GoldenKey(new ObjectPosition(posInt[0], posInt[1]));
                    gameBoard[posInt[0]][posInt[1]] = gk;
                    break;
                case "Player":
                    player = new Player(new ObjectPosition(posInt[0], posInt[1]));
                    player.setLife(Integer.parseInt(prop.getProperty("Life")));
                    player.setGotKey(Boolean.parseBoolean(prop.getProperty("gotKey")));
                    gameBoard[posInt[0]][posInt[1]] = player;
                    break;
            }
        }
        if (!(prop.containsKey("Life"))) {
            player = new Player(new ObjectPosition(appear[0], appear[1]));
            gameBoard[appear[0]][appear[1]] = player;
        }
    }

    //drawObject() zum Zeichnen jede einzige Objecktsymbol
    public void drawObject(int x, int y, char c) {
        terminal.moveCursor(y, x);
        terminal.applyBackgroundColor(Terminal.Color.WHITE);
        switch (c) {
            case 'X':               // Wall
                terminal.applyForegroundColor(Terminal.Color.BLACK);
                terminal.applyBackgroundColor(Terminal.Color.BLACK);
                break;
            case 'O':               //ExitDoor
                terminal.applyForegroundColor(Terminal.Color.MAGENTA);
                break;
            case '\u2600':          //Trap
                terminal.applyForegroundColor(Terminal.Color.RED);
                break;
            case '\u2620':          //Monster
                terminal.applyForegroundColor(Terminal.Color.RED);
                break;
            case '\u2640':          //Golden Key
                terminal.applyForegroundColor(Terminal.Color.YELLOW);
                break;
            case '\u263A':          //Player
                terminal.applyForegroundColor(Terminal.Color.BLUE);
                break;
        }
        terminal.putCharacter(c);
    }

    //drawGame() zur Kontrolle drawObject(), um das ganze Spiel darzustellen
    public void drawGame(int fromX, int fromY) {

        int a = gameBoard.length;
        int b = ts.getRows() * (player.getX() / (ts.getRows()) + 1);
        int c = gameBoard[0].length;
        int d = ts.getColumns() * (player.getY() / ts.getColumns() + 1);

        for (int i = fromX; i < Math.min(a, b); i++) {
            for (int j = fromY; j < Math.min(c, d); j++) {
                if (gameBoard[i][j] == null) {
                    drawObject(i % ts.getRows(), j % ts.getColumns(), ' ');
                    continue;
                }
                drawObject(i % ts.getRows(), j % ts.getColumns(), gameBoard[i][j].getSymbol()); //z.B: (2%30, 7%100) oder (62%30, 107%100)
            }
        }
        writeStatus();
        //drawObject(player.getX() % ts.getRows(), player.getY() % ts.getColumns(), player.getSymbol());
    }

    //control() zur Kontrolle des Spielers und auch teilweise der Darstellung
    public void control() throws Exception {
        boolean flag = true;
        System.out.println("control " + ts);
        if (ts.getRows() >= gameBoard.length && ts.getColumns() >= gameBoard[0].length) {
            flag = false;
            drawGame(0, 0);
        }

        System.out.println("monArr's size in control(): " + monArr.size());
        for (Monster mon : monArr) {
            Thread t = new Game(mon);
            System.out.println(mon.getMonName() + ". Monster erzeugt in [" + mon.getPos());
            killMonThreads = false;
            t.start();
        }
        while (true) {
            if (isOver) {
                break;
            }
            if ((player.getX() >= ((ts.getRows()) * (player.getX() / (ts.getRows()))) //Wenn der Eingang nicht im TerminalSize-Block von gameBoard ist, dann muessen wir den Player in dem passenden Block stellen
                    || player.getY() > ts.getColumns() * (player.getY() / ts.getColumns())) && flag == true) {
                displayX = (player.getX() - player.getX() % (ts.getRows()));     //x von links obersten des TerminalSize-Blocks
                displayY = (player.getY() - player.getY() % ts.getColumns());  //y von links obersten des TerminalSize-Blocks
                terminal.clearScreen();
                drawGame(displayX, displayY); //bestimmt die link oberste Koodinaten von Block(ts.getCol x ts.getRow) im  Array
                drawObject(player.getX() % ts.getRows(), player.getY() % ts.getColumns(), player.getSymbol());

            }

            Key key = null;
            while (key == null) {
                key = terminal.readInput();
            }
            switch (key.getKind()) {
                case ArrowUp:
                    move('u');
                    break;
                case ArrowDown:
                    move('d');
                    break;
                case ArrowRight:
                    move('r');
                    break;
                case ArrowLeft:
                    move('l');
                    break;
                case Escape:
                    exit();
                    break;
                case Tab:
                    pause();
                    System.out.println(Thread.currentThread().getName());
                    playMenu();
                    if (c == 'l' || c == 'm') {
                        killMonThreads = true;
                        break;
                    }
                    refresh();
                    goOn();
                    break;
                case F1:
                    legend();
                    break;
                case F5:    //Refresh Terminal
                    refresh();
            }
            if ((player.getX() >= displayX && player.getX() < displayX + (ts.getRows()))
                    && (player.getY() >= displayY && player.getY() < displayY + ts.getColumns())) {      // wenn x von Player zwischen jeweils displayX und displayX+ts.Rows liegt. Analog für y
                flag = false;        //Wenn Player noch im Terminal, brauchen wir den Teil vom Laby nicht wieder zu zeichnen
            } else {
                flag = true;
            }

        }
    }

    //refresh() zum Erfrischen das Spielfeld 
    public void refresh() {
        terminal.clearScreen();
        drawGame(player.getX() - player.getX() % ts.getRows(), player.getY() - player.getY() % ts.getColumns());
    }

    //pause() bringt alle Threads ausser main-Thread zur Pause
    public void pause() {
        suspend = true;
    }

    //goOn() bringt alle Threads ausser main-Thread wieder zur Bewegung 
    public void goOn() {
        synchronized (LOCK) {
            suspend = false;
            LOCK.notifyAll();
        }
    }

    //readFileGame() zum Lesen von Properties-File
    public FileReader readGameFile(String fileName) {
        FileReader reader = null;
        try {
            reader = new FileReader(fileName + ".properties");
        } catch (FileNotFoundException fnf) {
            popUp("You have no save now!", null, 30, 4);
            return null;
        }
        return reader;
    }

    //play() bring das Programm zum Laufen und kontrollt die Auswahl im Hauptmenü
    public char play(String fileName, char flow) throws FileNotFoundException, IOException {
        terminal.clearScreen();
        c = flow;
        if (flow == 'm') {
            return mainMenu();
        }
        Properties prop = new Properties();
        FileReader reader = readGameFile(fileName);
        if (reader == null) {
            exit();
            flow=c;
        }
        prop.load(reader);
        if (flow == 'n') { //New game
            loadField(prop);    //Lade das neue gameBoard
            try {
                control();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (flow == 'l') {
            monArr.clear();
            System.out.println("monArr's size : " + monArr.size());
            loadField(prop);
            try {
                control();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        reader.close();
        System.out.println("c =" + c);
        return c;

    }

    //playMenu() zur Anzeige des Hauptmenüs vom Anfang des Spiels
    public char mainMenu() {
        terminal.applyForegroundColor(Terminal.Color.DEFAULT);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        int choose = 8;
        terminal.moveCursor(45, 6);
        message("MAIN MENU", terminal);
        terminal.moveCursor(30, 8);
        message("New game", terminal);
        terminal.moveCursor(30, 10);
        message("Load Game", terminal);
        terminal.moveCursor(30, 12);
        message("About", terminal);
        terminal.moveCursor(30, 14);
        message("Exit", terminal);
        terminal.moveCursor(30, 20);
        message("[Press ENTER to choose]", terminal);
        terminal.moveCursor(27, choose);
        terminal.putCharacter('>');
        boolean outMenu = false;
        while (outMenu == false && isOver == false) {
            Key key = terminal.readInput();
            while (key == null) {
                key = terminal.readInput();
            }
            switch (key.getKind()) {
                case Escape:
                    outMenu = true;
                    exit();
                    break;
                case Tab:
                    terminal.clearScreen();
                    outMenu = true;
                    break;
                case ArrowDown:
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter(' ');
                    choose = Math.min(choose + 2, 14);
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter('>');
                    break;
                case ArrowUp:
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter(' ');
                    choose = Math.max(choose - 2, 8);
                    terminal.moveCursor(27, choose);
                    terminal.putCharacter('>');
                    break;
                case Enter:
                    switch (choose) {
                        case 8:
                            outMenu = true;
                            c = 'n';
                            return c;
                        case 10:
                            outMenu = true;
                            loadGame();
                            return c;
                        case 12:
                            ArrayList<String> about = new ArrayList<>();
                            about.add("The Capstone Project of WS 14/15");
                            about.add("Game 'SILENT MAZE' v.1.0.0 (Alpha)");
                            about.add(" ");
                            about.add("Technical University of Munich");
                            about.add("Faculty of Information Technology");
                            about.add("Gruppe 47  Tutor: Son Nguyen");
                            about.add("Autor: Tuan Anh Ma");
                            about.add(" ");
                            about.add("Last Update: 21.01.2015");
                            popUp("", about, 90, 15);
                            break;
                        case 14:
                            exit();
                            outMenu = true;
                            return c;
                    }
            }
        }
        return c;
    }

    //exit() wird aufgerufen, wenn der Spieler das Programm beenden moechten
    public void exit() {
        isOver = true;
        killMonThreads = true;
        terminal.exitPrivateMode();
        c = 'e';
    }
    
    
}
