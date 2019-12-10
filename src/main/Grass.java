import java.util.Objects;

public class Grass implements IWorldMapElement {
    private Position position;
    private int energy;

    public Grass(Position position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grass)) return false;
        Grass grass = (Grass) o;
        return energy == grass.energy &&
                Objects.equals(position, grass.position);
    }

    @Override
    public int hashCode() {

        return Objects.hash(position, energy);
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
