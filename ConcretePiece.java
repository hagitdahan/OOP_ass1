import java.util.ArrayList;

public abstract class ConcretePiece implements Piece{
	protected Player owner;
	protected String type;
    private ArrayList<Position> HistoryMove;
    private String name;
    private int numSteps;

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int steps) {
        this.numSteps = numSteps + steps;
    }
    public ArrayList getHistory(){
        return this.HistoryMove;
    }
    public String getName(){
        return name;
    }
    public void setHistory(Position p){
        HistoryMove.add(p);
    }
	public ConcretePiece() {
        HistoryMove=new ArrayList<Position>();

	}
    @Override
    public Player getOwner() {
        return this.owner;
    }
    @Override
    public String getType() {
    	return type;
    }
    
    
}
