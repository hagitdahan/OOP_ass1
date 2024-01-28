import java.util.Comparator;

public class ComparePiecesWithKills implements Comparator<ConcretePiece> {
    private ConcretePlayer winner;
    public ComparePiecesWithKills(ConcretePlayer winner){
        super();
        this.winner=winner;
    }

    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        Pawn p1 = ((Pawn) o1);
        Pawn p2 = ((Pawn) o2);
        String n1 = p1.getName();
        String n2 = p2.getName();
        n1 = n1.substring(1);
        n2 = n2.substring(1);
        int numPlayer1 = Integer.parseInt(n1);
        int numPlayer2 = Integer.parseInt(n2);

        if (p1.getNumEat() == p2.getNumEat()) {
            if (numPlayer1 == numPlayer2) {
                if (p1.getOwner()==winner) {
                    return 1;
                } else return -1;
            } else if (numPlayer1 < numPlayer2)
                return 1;
            else return -1;
        }
        else if (p1.getNumEat()>p2.getNumEat())
            return 1;
        return -1;
    }
}


