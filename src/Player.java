/**
 *
 * @author TuanAnh
 */
public class Player extends GameObject{
    private final char symbol = '\u263A';    
    private boolean gotKey = false;
    private int life=5;
    
    public Player (ObjectPosition playerPos){
        super(playerPos);
    }
    
    public void setGotKey(boolean gotKey){
        this.gotKey=gotKey; 
    }

    public void setLife(int life) {
        this.life = life;
    }

    public boolean isGotKey() {
        return gotKey;
    }

    public int getLife() {
        return life;
    }
    
    public void setPos(ObjectPosition op){
        this.pos.setPos(op);
    }
    
    public void moveUp(){
        this.pos.moveUp();
    }
    public void moveDown(){
        this.pos.moveDown();
    }
    public void moveRight(){
        this.pos.moveRight();
    }
    public void moveLeft(){
        this.pos.moveLeft();
    }
    
    public void run(){
        
    }
    
    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
