import java.util.ArrayList;

public class King extends ConcretePiece{
	private String type ;
	private ConcretePlayer owner;
	private String name;
	private ArrayList<Position> HistoryMove;
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
	public void setHistory(Position p){
		HistoryMove.add(p);
	}
	public void setNameKing(String nameKing) {
		this.name = nameKing;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public King(Player player) {
		HistoryMove=new ArrayList<Position>();
		super.owner = player;
		this.type = "\u2654";
	}
}
