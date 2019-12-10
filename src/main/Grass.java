import java.util.Objects;

public class Grass implements IWorldMapElement {
    private Position position;
    private int energy;

    public Grass(Position position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }
}
