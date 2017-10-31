/**
 *
 * @author TuanAnh
 */
public class Wall extends GameObject{
    //final char symbol = '\u2610';
    final char symbol = 'X';
    
    public Wall(ObjectPosition wallPos){
        super(wallPos);
    }
    
    @Override
    public char getSymbol() {
        return symbol;
    }

//    public void setPos(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
}
