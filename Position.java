import java.util.ArrayList;

public class Position {
	
    private int _x, _y;
    private ArrayList<ConcretePiece> piecesAtPosition;
    public Position(int x,int y)
    {
        this._x = x;
        this._y = y;
        piecesAtPosition=new ArrayList<ConcretePiece>();
    }

    public void setPiecesAtPosition(ConcretePiece c) {
        this.piecesAtPosition.add(c);
    }

    public ArrayList getPiecesAtPosition() {
        return this.piecesAtPosition;
    }

    public int get_x(){
        return this._x;
    }
    
    public int get_y(){
        return this._y;
    }
    public void set_x(int x) {
        this._x = x;
    }
    
    public void set_y(int y) {
        this._y = y;
    }    
  
    public boolean isDiagonal (Position b){
    	if (this.get_x() == b.get_x() || this.get_y() == b.get_y())
    		return false;
    	return true;
    }

    @Override
    public String toString() {
        return "("+get_x() +", " +get_y()+")";
    }
}
