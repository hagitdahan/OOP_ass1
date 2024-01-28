import java.util.Comparator;

public class ComarePieceWithHistorMove implements Comparator<ConcretePiece> {
    private ConcretePlayer winner;
    public ComarePieceWithHistorMove(ConcretePlayer winner){
        super();
        this.winner=winner;
    }
    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        String n1 = o1.getName();
        String n2 = o2.getName();
        n1 = n1.substring(1);
        n2 = n2.substring(1);
        int numPlayer1 = Integer.parseInt(n1);
        int numPlayer2 = Integer.parseInt(n2);
        if (o1.getOwner() == o2.getOwner()) {
            if (o1.getHistory().size() == o2.getHistory().size()) {
                if(numPlayer1 > numPlayer2) return 1;
                else return -1;
            }
            else if(o1.getHistory().size()>o2.getHistory().size())
                return 1;
            else return -1;
        }
        else {
            if (o2.getOwner()==winner) {
                return 1;
            }
        }
        return -1;
    }

}
