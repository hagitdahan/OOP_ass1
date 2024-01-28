import java.util.ArrayList;
import java.util.Collection;

public class GameLogic implements PlayableLogic{

		private final int BOARD_SIZE = 11;
		private final int BOARD_CENTER = BOARD_SIZE/2;
		ConcretePiece [][] board = new ConcretePiece[BOARD_SIZE][BOARD_SIZE];
		ConcretePlayer attacker = new ConcretePlayer(false);
		ConcretePlayer defender = new ConcretePlayer(true);
		private boolean firstPlayerTurn;
		int xKing;
		int yKing;
	    Position [][] positions=new Position[11][11];
	    ArrayList<ConcretePiece> boardState;
	    ArrayList <Position> pos=new ArrayList<Position>();
		int deadAttacker;
	    int deadDefender;
		boolean gameFinish;
		boolean winner; //false == attacker , true==defender
		public void initPosition(){
			for(int i=0;i<11;i++){
				for (int j=0;j<11;j++){
					positions[i][j]=new Position(i,j);
				}
			}
		}
		public GameLogic() {
			reset();
		}
		public  boolean ThreePawnCircleKing(int x,int y){
			if(x==0 && board[x + 1][y] != null && !board[x + 1][y].getOwner().isPlayerOne()){
				if ((y + 1) <= 10 && board[x][y+1] != null && !board[x][y+1].getOwner().isPlayerOne()) {
					if (y - 1 >= 0 && board[x][y-1] != null && !board[x][y - 1].getOwner().isPlayerOne()) {
						return true;
					}
				}
			}
			if(x==BOARD_SIZE-1 && board[x - 1][y] != null && ! board[x - 1][y].getOwner().isPlayerOne()){
				if ((y + 1) <= 10 && board[x][y+1] != null && !board[x][y+1].getOwner().isPlayerOne()) {
					if (y - 1 >= 0 && board[x][y-1] != null && !board[x][y - 1].getOwner().isPlayerOne()) {
						return true;

					}
				}
			}
			if(y==0 && board[x][y + 1] != null &&! board[x][y + 1].getOwner().isPlayerOne()){
				if (x + 1 <= 10 && board[x + 1][y] != null && !board[x + 1][y].getOwner().isPlayerOne()) {
					if (x - 1 >= 0 && board[x - 1][y] != null && ! board[x - 1][y + 1].getOwner().isPlayerOne()) {
						return true;
					}
				}
			}
			if(y==BOARD_SIZE && board[x][y - 1] != null &&! board[x][y - 1].getOwner().isPlayerOne()){
				if (x + 1 <= 10 && board[x + 1][y] != null && !board[x + 1][y].getOwner().isPlayerOne()) {
					if (x - 1 >= 0 && board[x - 1][y] != null && ! board[x - 1][y + 1].getOwner().isPlayerOne()) {
						return true;
					}
				}
			}
			return false;
		}
		public boolean fourPawnCircleKing(int x,int y) {
			boolean up = y + 1 <= 10 && board[x][y + 1] != null && !board[x][y + 1].getOwner().isPlayerOne();
			boolean down = y - 1 >= 0 && board[x][y - 1] != null && !board[x][y - 1].getOwner().isPlayerOne();
			boolean left = x + 1 <= 10 && board[x + 1][y] != null && !board[x + 1][y].getOwner().isPlayerOne();
			boolean right = x - 1 >= 0 && board[x - 1][y] != null && !board[x - 1][y].getOwner().isPlayerOne();
			return up && down && left && right;
		}
		public boolean isCorner (int x,int y){
			if(x==0 && y==0 || x==BOARD_SIZE-1 && y==0 || x==0 && y ==BOARD_SIZE-1 || x==BOARD_SIZE-1 && y==BOARD_SIZE-1)
				return true;
			return false;
		}

		public boolean pieceIsContain(Position pos,ConcretePiece p){
			for(int i=0;i<pos.getPiecesAtPosition().size();i++){
				if(p==pos.getPiecesAtPosition().get(i))
					return true;
		    }
		    return false;
		}
		public void arrToArray(Position [][] arr){
			for(int i=0;i<11;i++){
				for (int j=0;j<11;j++){
					pos.add(positions[i][j]);
				}
			}
		}
		@Override
		public boolean move(Position a, Position b) {
			if(a.isDiagonal(b)) {
				return false;
			}
			if(this.isCorner(b.get_x(),b.get_y())&& !(board[a.get_x()][a.get_y()] instanceof King))
				return false;
			if(board[a.get_x()][a.get_y()].getOwner().isPlayerOne() && !firstPlayerTurn )
				return false;
			if(!board[a.get_x()][a.get_y()].getOwner().isPlayerOne() && firstPlayerTurn )
				return false;
			else {
				if(a.get_x()==b.get_x()) {
					if(a.get_y()>b.get_y()) {
						for(int i=b.get_y();i<a.get_y();i++) {
							if(board[a.get_x()][i]!=null)
								return false;
						}
					}
					else {
						for(int i=a.get_y()+1;i<=b.get_y();i++) {
							if(board[a.get_x()][i]!=null)
								return false;
						}
					}
				}
				if(a.get_y()==b.get_y()) {
					if (a.get_x() > b.get_x()) {
						for (int i = b.get_x(); i < a.get_x(); i++) {
							if (board[i][a.get_y()] != null)
								return false;
						}
					} else {
						for (int i = a.get_x() + 1; i <= b.get_x(); i++) {
							if (board[i][a.get_y()] != null)
								return false;
						}
					}
				}
			}
			firstPlayerTurn=!firstPlayerTurn;
			doMove(a,b);
			int num=0;
			if (board[b.get_x()][b.get_y()] instanceof Pawn) {
				if(isSecondPlayerTurn()) { //defender kills the attacker
					num=isRegularKill(b)+isKillInTheSides(b)+killWithCorners(b);
					deadAttacker = deadAttacker + num;
					((Pawn) board[b.get_x()][b.get_y()]).setNumEat(num);
				}
				else {//attacker kills the defender
					num=isRegularKill(b)+isKillInTheSides(b)+killWithCorners(b);
					deadDefender = deadDefender+num;
					((Pawn) board[b.get_x()][b.get_y()]).setNumEat(num);
				}
			}
			if(isGameFinished()) {
				if(winner==true) {
					SortByPawnHistory(defender);
					for (int i = 0; i < boardState.size(); i++) {
						if (boardState.get(i) instanceof King)
							boardState.remove(i);
					}
					SortByNumKills(defender);
					boardState.add(board[xKing][yKing]);
					countSteps();
					SortBySteps(defender);
					arrToArray(positions);
					SortByPosition();
				}
				if(winner==false){
					SortByPawnHistory(attacker);
					for (int i=0;i<boardState.size();i++){
						if(boardState.get(i) instanceof King)
							boardState.remove(i);
					}
					SortByNumKills(attacker);
					boardState.add(board[xKing][yKing]);
					countSteps();
					SortBySteps(attacker);
					arrToArray(positions);
					SortByPosition();
				}
				gameFinish=true;
			}
			return true;
		}
		public void doMove(Position a, Position b) {
			if(board[a.get_x()][a.get_y()].getHistory().isEmpty()) {
				board[a.get_x()][a.get_y()].setHistory(a);
			}
			ConcretePiece tmp = board[a.get_x()][a.get_y()];
			board[b.get_x()][b.get_y()] = tmp;
			board[a.get_x()][a.get_y()] = null;
			if(!pieceIsContain(positions[b.get_x()][b.get_y()],board[b.get_x()][b.get_y()]))
			{
			    positions[b.get_x()][b.get_y()].setPiecesAtPosition(board[b.get_x()][b.get_y()]);
			}

			if (tmp instanceof Pawn) {
				((Pawn)board[b.get_x()][b.get_y()]).setHistory(b);
			}
			else {
				((King)board[b.get_x()][b.get_y()]).setHistory(b);
				xKing = b.get_x();
				yKing = b.get_y();
				}
		}
		public int isKillInTheSides(Position b){
			int flagWasKill=0;
			int x=b.get_x();
			int y=b.get_y();
			boolean typeB = board[x][y].getOwner().isPlayerOne();
			if(y==BOARD_SIZE-2 && board[x][BOARD_SIZE-1]!=null && typeB!= board[x][BOARD_SIZE-1].getOwner().isPlayerOne()) {
				if(board[x][BOARD_SIZE-1] instanceof Pawn) {
					board[x][BOARD_SIZE - 1] = null;
					flagWasKill++;
				}
			}
			if(y==1 && board[x][0] !=null && typeB !=board[x][0].getOwner().isPlayerOne()){
				if(board[x][0] instanceof Pawn) {
					board[x][0] = null;
					flagWasKill++;
				}
			}
			if(x==BOARD_SIZE-2 && board[BOARD_SIZE-1][y] !=null && typeB != board[BOARD_SIZE-1][y].getOwner().isPlayerOne()) {
				if(board[BOARD_SIZE-1][y] instanceof Pawn ) {
					board[BOARD_SIZE-1][y] = null;
					flagWasKill++;
				}
			}
			if(x==1 && board[0][y]!=null && typeB != board[0][y].getOwner().isPlayerOne()){
				if(board[0][y] instanceof Pawn) {
					board[0][y] = null;
					flagWasKill++;
				}
			}

        return flagWasKill;
		}
		public int isRegularKill(Position b) {
			//eat from 2 sides
			//save how much eat
			int flagWasKill=0;
			int x=b.get_x();
			int y=b.get_y();
			boolean typeB = board[x][y].getOwner().isPlayerOne();
			if(x+1<=10 && board[x+1][y]!=null && typeB !=board[x+1][y].getOwner().isPlayerOne()){
				if((x+2)<=10 && board[x+2][y]!=null && typeB==board[x+2][y].getOwner().isPlayerOne()) {
					if(board[x+1][y] instanceof Pawn) {
						board[x + 1][y] = null;
						flagWasKill ++;
					}
				}
			}
			if(x-1>=0 && board[x-1][y]!=null && typeB !=board[x-1][y].getOwner().isPlayerOne()){
				if(x-2>=0 &&board[x-2][y]!=null && typeB==board[x-2][y].getOwner().isPlayerOne()) {
					if(board[x-1][y] instanceof Pawn) {
						board[x - 1][y] = null;
						flagWasKill ++;
					}
				}
			}
			if(y+1<=10 && board[x][y+1]!=null && typeB !=board[x][y+1].getOwner().isPlayerOne()){
				if(y+2<=10 && board[x][y+2]!=null && typeB==board[x][y+2].getOwner().isPlayerOne()) {
					if(board[x][y+1] instanceof Pawn) {
						board[x][y + 1] = null;
						flagWasKill ++;
					}
				}
			}
			if(y-1>=0 &&board[x][y-1]!=null && typeB !=board[x][y-1].getOwner().isPlayerOne()){
				if(y-2>=0 &&board[x][y-2]!=null && typeB==board[x][y-2].getOwner().isPlayerOne()) {
					if(board[x][y-1] instanceof Pawn) {
						board[x][y - 1] = null;
						flagWasKill ++;
					}
				}
			}
			return flagWasKill;
		}
		public int killWithCorners(Position b) {
			int flagWasKill = 0;
			int x = b.get_x();
			int y = b.get_y();
			boolean typeB = board[x][y].getOwner().isPlayerOne();
			if (x-2 >=0 && isCorner(x-2,y) && board[x-1][y]!=null && typeB!=board[x-1][y].getOwner().isPlayerOne()) {
				if(board[x-1][y] instanceof Pawn) {
					board[x - 1][y] = null;
					flagWasKill ++;
				}
			}
			if (x+2 <=10 && isCorner(x+2,y) && board[x+1][y]!=null && typeB!=board[x+1][y].getOwner().isPlayerOne()){
				if(board[x+1][y] instanceof Pawn) {
					board[x + 1][y] = null;
					flagWasKill ++;
				}
			}
			if (y-2 >=0 && isCorner(x,y-2) && board[x][y-1]!=null && typeB!=board[x][y-1].getOwner().isPlayerOne()){
				if(board[x][y-1] instanceof Pawn) {
					board[x][y - 1] = null;
					flagWasKill ++;
				}
			}
			if (y+2 <=10 && isCorner(x,y+2) && board[x][y+1]!=null && typeB!=board[x][y+1].getOwner().isPlayerOne()){
				if(board[x][y+1] instanceof Pawn) {
					board[x][y + 1] = null;
					flagWasKill ++;
				}
			}
			return flagWasKill;
		}
		@Override
		public Piece getPieceAtPosition(Position position) {
			ConcretePiece atPosition = board[position.get_x()][position.get_y()];
			return atPosition;
		}
		@Override
		public Player getFirstPlayer() {return this.defender;}
		@Override
		public Player getSecondPlayer() {return this.attacker;}
		@Override
		public boolean isGameFinished() {
			if(isCorner(xKing,yKing) || deadAttacker==24) {
				defender.setWins();
				winner=true;
				return true;
			}
			if(fourPawnCircleKing(xKing,yKing) || ThreePawnCircleKing(xKing,yKing) ) {
				attacker.setWins();
				winner=false;
				return true;
			}

			return false;
		}
		public void countSteps(){
			for(int i=0;i<boardState.size();i++){
				ConcretePiece c=boardState.get(i);
				if(c.getHistory().size()>1){
					for(int j=0;j<c.getHistory().size()-1;j++){
						Position a= (Position) c.getHistory().get(j);
						Position b= (Position) c.getHistory().get(j+1);
						if(a.get_x()>b.get_x()) c.setNumSteps(a.get_x()-b.get_x());
						else c.setNumSteps(b.get_x()-a.get_x());
						if(a.get_y()>b.get_y()) c.setNumSteps(a.get_y()-b.get_y());
						else c.setNumSteps(b.get_y()-a.get_y());
					}
				}

			}
		}
		public void SortByNumKills(ConcretePlayer player){
			boardState.sort(new ComparePiecesWithKills(player));
			Pawn p;
			for(int i=boardState.size()-1;i>=0;i--){
				if(boardState.get(i)!=null) {
					p = ((Pawn) boardState.get(i));
					if (p.getNumEat() > 0)
						System.out.println(p.getName() + ": " + p.getNumEat() + " kills");
				}
			}
			System.out.println("***************************************************************************");
		}
		public void SortByPawnHistory(ConcretePlayer player){
			boardState.sort(new ComarePieceWithHistorMove(player));
			for (int i=0;i<boardState.size();i++){
				if (boardState.get(i)!=null){
					if(boardState.get(i).getHistory().size()>1){
						ConcretePiece c=boardState.get(i);
						System.out.print(c.getName()+": " +"[");
						for(int j=0;j<c.getHistory().size();j++)
						{
							if(j==c.getHistory().size()-1)
								System.out.print(c.getHistory().get(j).toString());
							else System.out.print(c.getHistory().get(j).toString()+", ");
						}
						System.out.println("]");
					}
				}
			}
			System.out.println("***************************************************************************");
		}
		public void SortByPosition(){
			pos.sort(new ComparePositions());
			for(int i=0;i<pos.size();i++){
				Position p=pos.get(i);
				if(p.getPiecesAtPosition().size()>1)
				{
					System.out.println(p.toString()+p.getPiecesAtPosition().size()+" pieces");
				}
			}
			System.out.println("***************************************************************************");
		}
		public void SortBySteps(ConcretePlayer player){
			boardState.sort(new CompareSteps(player));
			for(int i=0;i<boardState.size();i++){
				ConcretePiece c=boardState.get(i);
				if(c.getNumSteps()>0){
					System.out.println(c.getName() +": "+c.getNumSteps()+" squares");
				}
			}
			System.out.println("***************************************************************************");
		}
		@Override
		public boolean isSecondPlayerTurn() {
		  return !firstPlayerTurn;
		}
		//rest the game with x,y to all the points in the board
		//than put all the pieces in the board
		@Override
		public void reset() {
			initPosition();
			deadDefender=0;
			deadAttacker=0;
			boardState = new ArrayList<ConcretePiece>();

			for(int i=0;i<BOARD_SIZE;i++) {
				for(int j=0;j<BOARD_SIZE;j++) {
					board[i][j] = null;
				}
			}
			xKing=5;
			yKing=5;
			//attacker
			board[3][0] = new Pawn(this.attacker);
			boardState.add(board[3][0]);
			((Pawn)board[3][0]).setNamePawn("A1");
			positions[3][0].setPiecesAtPosition(board[3][0]);
			board[4][0] = new Pawn(this.attacker);
			boardState.add(board[4][0]);
			((Pawn)board[4][0]).setNamePawn("A2");
			positions[4][0].setPiecesAtPosition(board[4][0]);
			board[5][0] = new Pawn(this.attacker);
			boardState.add(board[5][0]);
			((Pawn)board[5][0]).setNamePawn("A3");
			positions[5][0].setPiecesAtPosition(board[5][0]);
			board[6][0] = new Pawn(this.attacker);
			boardState.add(board[6][0]);
			((Pawn)board[6][0]).setNamePawn("A4");
			positions[6][0].setPiecesAtPosition(board[6][0]);
			board[7][0] = new Pawn(this.attacker);
			boardState.add(board[7][0]);
			((Pawn)board[7][0]).setNamePawn("A5");
			positions[7][0].setPiecesAtPosition(board[7][0]);
			board[5][1] = new Pawn(this.attacker);
			boardState.add(board[5][1]);
			((Pawn)board[5][1]).setNamePawn("A6");
			positions[5][1].setPiecesAtPosition(board[5][1]);
			board[0][3] = new Pawn(this.attacker);
			boardState.add(board[0][3]);
			((Pawn)board[0][3]).setNamePawn("A7");
			positions[0][3].setPiecesAtPosition(board[0][3]);
			board[10][3] = new Pawn(this.attacker);
			boardState.add(board[10][3]);
			((Pawn)board[10][3]).setNamePawn("A8");
			positions[10][3].setPiecesAtPosition(board[10][3]);
			board[0][4] = new Pawn(this.attacker);
			boardState.add(board[0][4]);
			((Pawn)board[0][4]).setNamePawn("A9");
			positions[0][4].setPiecesAtPosition(board[0][4]);
			board[10][4] = new Pawn(this.attacker);
			boardState.add(board[10][4]);
			((Pawn)board[10][4]).setNamePawn("A10");
			positions[10][4].setPiecesAtPosition(board[10][4]);
			board[0][5] = new Pawn(this.attacker);
			boardState.add(board[0][5]);
			((Pawn)board[0][5]).setNamePawn("A11");
			positions[0][5].setPiecesAtPosition(board[0][5]);
			board[1][5] = new Pawn(this.attacker);
			boardState.add(board[1][5]);
			((Pawn)board[1][5]).setNamePawn("A12");
			positions[1][5].setPiecesAtPosition(board[1][5]);
			board[9][5] = new Pawn(this.attacker);
			boardState.add(board[9][5]);
			((Pawn)board[9][5]).setNamePawn("A13");
			positions[9][5].setPiecesAtPosition(board[9][5]);
			board[10][5] = new Pawn(this.attacker);
			boardState.add(board[10][5]);
			((Pawn)board[10][5]).setNamePawn("A14");
			positions[10][5].setPiecesAtPosition(board[10][5]);
			board[0][6] = new Pawn(this.attacker);
			boardState.add(board[0][6]);
			((Pawn)board[0][6]).setNamePawn("A15");
			positions[0][6].setPiecesAtPosition(board[0][6]);
			board[10][6] = new Pawn(this.attacker);
			boardState.add(board[10][6]);
			((Pawn)board[10][6]).setNamePawn("A16");
			positions[10][6].setPiecesAtPosition(board[10][6]);
			board[0][7] = new Pawn(this.attacker);
			boardState.add(board[0][7]);
			((Pawn)board[0][7]).setNamePawn("A17");
			positions[0][7].setPiecesAtPosition(board[0][7]);
			board[10][7] = new Pawn(this.attacker);
			boardState.add(board[10][7]);
			((Pawn)board[10][7]).setNamePawn("A18");
			positions[10][7].setPiecesAtPosition(board[10][7]);
			board[5][9] = new Pawn(this.attacker);
			boardState.add(board[5][9]);
			((Pawn)board[5][9]).setNamePawn("A19");
			positions[5][9].setPiecesAtPosition(board[5][9]);
			board[3][10] = new Pawn(this.attacker);
			boardState.add(board[3][10]);
			((Pawn)board[3][10]).setNamePawn("A20");
			positions[3][10].setPiecesAtPosition(board[3][10]);
			board[4][10] = new Pawn(this.attacker);
			boardState.add(board[4][10]);
			((Pawn)board[4][10]).setNamePawn("A21");
			positions[4][10].setPiecesAtPosition(board[4][10]);
			board[5][10] = new Pawn(this.attacker);
			boardState.add(board[5][10]);
			((Pawn)board[5][10]).setNamePawn("A22");
			positions[5][10].setPiecesAtPosition(board[5][10]);
			board[6][10] = new Pawn(this.attacker);
			boardState.add(board[6][10]);
			((Pawn)board[6][10]).setNamePawn("A23");
			positions[6][10].setPiecesAtPosition(board[6][10]);
			board[7][10] = new Pawn(this.attacker);
			boardState.add(board[7][10]);
			((Pawn)board[7][10]).setNamePawn("A24");
			positions[7][10].setPiecesAtPosition(board[7][10]);
			//defender
			board[5][3] = new Pawn(this.defender);
			boardState.add(board[5][3]);
			((Pawn)board[5][3]).setNamePawn("D1");
			positions[5][3].setPiecesAtPosition(board[5][3]);
			board[4][4] = new Pawn(this.defender);
			boardState.add(board[4][4]);
			((Pawn)board[4][4]).setNamePawn("D2");
			positions[4][4].setPiecesAtPosition(board[4][4]);
			board[5][4] = new Pawn(this.defender);
			boardState.add(board[5][4]);
			((Pawn)board[5][4]).setNamePawn("D3");
			positions[5][4].setPiecesAtPosition(board[5][4]);
			board[6][4] = new Pawn(this.defender);
			boardState.add(board[6][4]);
			((Pawn)board[6][4]).setNamePawn("D4");
			positions[6][4].setPiecesAtPosition(board[6][4]);
			board[3][5] = new Pawn(this.defender);
			boardState.add(board[3][5]);
			((Pawn)board[3][5]).setNamePawn("D5");
			positions[3][5].setPiecesAtPosition(board[3][5]);
			board[4][5] = new Pawn(this.defender);
			boardState.add(board[4][5]);
			((Pawn)board[4][5]).setNamePawn("D6");
			positions[4][5].setPiecesAtPosition(board[4][5]);
			board[5][5] = new King(this.defender);
			boardState.add(board[5][5]);
			((King)board[5][5]).setNameKing("K7");
			positions[5][5].setPiecesAtPosition(board[5][5]);
			board[6][5] = new Pawn(this.defender);
			boardState.add(board[6][5]);
			((Pawn)board[6][5]).setNamePawn("D8");
			positions[6][5].setPiecesAtPosition(board[6][5]);
			board[7][5] = new Pawn(this.defender);
			boardState.add(board[7][5]);
			((Pawn)board[7][5]).setNamePawn("D9");
			positions[7][5].setPiecesAtPosition(board[7][5]);
			board[4][6] = new Pawn(this.defender);
			boardState.add(board[4][6]);
			((Pawn)board[4][6]).setNamePawn("D10");
			positions[4][6].setPiecesAtPosition(board[4][6]);
			board[5][6] = new Pawn(this.defender);
			boardState.add(board[5][6]);
			((Pawn)board[5][6]).setNamePawn("D11");
			positions[5][6].setPiecesAtPosition(board[5][6]);
			board[6][6] = new Pawn(this.defender);
			boardState.add(board[6][6]);
			((Pawn)board[6][6]).setNamePawn("D12");
			positions[6][6].setPiecesAtPosition(board[6][6]);
			board[5][7] = new Pawn(this.defender);
			boardState.add(board[5][7]);
			((Pawn)board[5][7]).setNamePawn("D13");
			positions[5][7].setPiecesAtPosition(board[5][7]);
			this.firstPlayerTurn=false;
			gameFinish=false;


		}

		@Override
		public void undoLastMove() {
		}
		@Override
		public int getBoardSize() {
			return BOARD_SIZE;
		}

	}
