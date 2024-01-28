import java.util.ArrayList;

public class Pawn extends ConcretePiece{
	private String type;
	private ConcretePlayer owner;
	private int numEat;
	private String name;
	private int numSteps;

	public int getNumSteps() {
		return numSteps;
	}

	public void setNumSteps(int steps) {
		this.numSteps = numSteps + steps;
	}
	private ArrayList<Position> HistoryMove;
	public void setNamePawn(String namePawn) {
		this.name = namePawn;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public ArrayList getHistory(){
		return this.HistoryMove;
	}
	public void setHistory(Position p){
		HistoryMove.add(p);
	}
	public void setNumEat(int numeat){
		this.numEat=this.numEat+numeat;	}
	public int getNumEat(){
		return numEat;
	}
	public Pawn (Player player){
		HistoryMove=new ArrayList<Position>();
		super.owner=player;
		this.type=("\u2659");
		this.numEat=0;
	}
}
