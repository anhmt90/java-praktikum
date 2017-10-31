/**
 *
 * @author TuanAnh
 */
public class Entrance extends GameObject{
    static final char symbol = 'E';
    
    public Entrance (ObjectPosition enPos){
        super(enPos);
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
    
    
}
