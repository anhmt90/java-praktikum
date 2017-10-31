/**
 *
 * @author TuanAnh
 */
public class Trap extends GameObject{
    private int x;
    private int y;
    final char symbol = '\u2600';
    
    public Trap(ObjectPosition trapPos){
        super(trapPos);
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
    
    
}
