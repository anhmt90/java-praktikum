/**
 *
 * @author TuanAnh
 */
public class GoldenKey extends GameObject{
    ObjectPosition keyPos;
    final char symbol = '\u2640';
    
    public GoldenKey(ObjectPosition keyPos){
        super(keyPos);
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
