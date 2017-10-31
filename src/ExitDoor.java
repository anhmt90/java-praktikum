/**
 *
 * @author TuanAnh
 */
public class ExitDoor extends GameObject{

    final char symbol = 'O';
    
    public ExitDoor(ObjectPosition edPos){
        super(edPos);
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
