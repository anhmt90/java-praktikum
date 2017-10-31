
/**
 *
 * @author TuanAnh
 */
public class Monster extends GameObject { 

    //static ObjectPosition other;

    final char symbol = '\u2620';
    private String monName;
    
    public Monster(ObjectPosition monPos, String monName) {
        super(monPos);
        this.monName = monName;
    }

    @Override
    public char getSymbol() {
        return this.symbol;
    }

    public String getMonName() {
        return monName;
    }

    public void setMonName(String monName) {
        this.monName = monName;
    }

//    public void moveUp() {
//        this.pos.moveUp();
//    }
//
//    public void moveDown() {
//        this.pos.moveDown();
//    }
//
//    public void moveRight() {
//        this.pos.moveRight();
//    }
//
//    public void moveLeft() {
//        this.pos.moveLeft();
//    }

//    public void chase(ObjectPosition other){
//        this.other = other;
//    }
//    
//    public void run(){
//        while(this.getX()!=other.getX() && this.getY() != getY()){
//            if(this.getX()>other.getX())  this.moveUp();
//            else if(this.getX()<other.getX())  this.moveDown();
//            
//            if(this.getY()>other.getY())  this.moveLeft();
//            else if(this.getY()<other.getY())  this.moveRight();
//        }
//        
//    }
}
