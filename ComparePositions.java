import java.util.Comparator;

public class ComparePositions implements Comparator<Position> {
    @Override
    public int compare(Position o1, Position o2) {
        if(o1.getPiecesAtPosition().size()==o2.getPiecesAtPosition().size())
        {
            if(o1.get_x()==o2.get_x())
                if(o1.get_y()>o2.get_y())
                    return 1;
                else return -1;
            else if(o1.get_x()>o2.get_x())
                return 1;
            return -1;
        }
        else if(o1.getPiecesAtPosition().size()<o2.getPiecesAtPosition().size())
            return 1;
        else return -1;

    }
}
