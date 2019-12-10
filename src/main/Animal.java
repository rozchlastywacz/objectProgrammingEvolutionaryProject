import java.util.Set;

public class Animal implements IWorldMapElement {
    private RectangularWorldMap worldMap;
    private MapDirection direction;
    private Position position;
    private int[] genotype;
    private int energy;
    private Set observers;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString(){
        return "a";
    }

}
