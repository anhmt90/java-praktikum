/**
 *
 * @author TuanAnh
 */
public abstract class GameObject{

    protected ObjectPosition pos;

    public GameObject(ObjectPosition pos) {
        this.pos = new ObjectPosition(pos.getX(), pos.getY());
    }

    
    public ObjectPosition getPos() {
        return pos;
    }

   
    public int getX() {
        return pos.getX();
    }

    
    public int getY() {
        return pos.getY();
    }

    
    public void setPos(int x, int y) {
        pos.setPos(x, y);
    }

    
    public abstract char getSymbol();
    //public abstract String toString();
}
