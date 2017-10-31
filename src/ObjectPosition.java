/**
 *
 * @author TuanAnh
 */
public class ObjectPosition{
    private int x, y;
    private int gScore;
    private double hScore, fScore;
    private ObjectPosition parent;

    public ObjectPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public ObjectPosition(int x, int y, ObjectPosition goal){
        this.x = x;
        this.y = y;
        setHScore(goal);
        setGScore(10000);
        this.parent = null;
    }
    
    public ObjectPosition(ObjectPosition other){
        this.x = other.getX();
        this.y = other.getY();
    }
    
    public void setPos (int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void setPos(ObjectPosition op){
        this.x = op.x;
        this.y = op.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
//    public ObjectPosition getPos(){
//        return this;
//    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void moveLeft(){
        setY(y-1);
    }
    
    public void moveRight(){
        setY(y+1);
    }
    
    public void moveUp(){
        setX(x-1);
    }
    
    public void moveDown(){
        setX(x+1);
    }
    
    
    @Override
    public boolean equals(Object o){
        ObjectPosition other = (ObjectPosition) o;
        if(o==null)
            return false;
        else
            return x == other.x && y==other.y;
    }
    
    public boolean isSamePosition(ObjectPosition other){
        if(other == null) return false;
        return ((this.x == other.x)&&(this.y==other.y));
    }
    
    public String toString(){
        return this.x+","+this.y;
    }

    public double getFScore() {
        return fScore;
    }

    public void setFScore() {
        this.fScore = this.hScore + this.gScore;
    }

    public int getGScore() {
        return gScore;
    }

    public void setGScore(int gScore) {
        this.gScore = gScore;
    }

    public double getHScore() {
        return hScore;
    }

//    public void setHScore(ObjectPosition goal) {
//        this.hScore = Math.abs(this.x - goal.getX()) + Math.abs(this.y - goal.getY());;
//    }
    
    public void setHScore(ObjectPosition goal) {
        this.hScore = Math.sqrt((this.x - goal.getX())*(this.x - goal.getX()) + (this.y - goal.getY())*(this.y - goal.getY()));;
    }

    public ObjectPosition getParent() {
        return parent;
    }

    public void setParent(ObjectPosition parent) {
        this.parent = parent;
    }
    
    
//    public int compareTo(Object o){
//        ObjectPosition other = (ObjectPosition) o;
//        return this.fScore - other.fScore;
//    }
     
   
}
