import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TorusWorldMap implements IWorldMap {
    private int height;
    private int width;
    private Map<Position, IWorldMapElement> map;
    private LinkedList<Animal> listOfAnimals;

    public TorusWorldMap(int height, int width) {
        this.height = height;
        this.width = width;
        this.map = new HashMap<>();
        this.listOfAnimals = new LinkedList<>();
    }

    public LinkedList<Animal> getAnimals(){return listOfAnimals;}

    @Override
    public void placeAt(IWorldMapElement element, Position position) {
        map.put(position, element);
        if (element instanceof Animal) listOfAnimals.add((Animal) element);
    }

    @Override
    public Object objectAt(Position position) {
        return map.get(position);
    }

    @Override
    public Position adjacent(Position position, MapDirection direction) {
        Position vector = moveVector(direction);
        int x = ((((position.getX() + vector.getX()) % width) + width) % width);
        int y = ((((position.getY() + vector.getY()) % height) + height) % height);
        return new Position(x, y);
    }

    private Position moveVector(MapDirection direction) {
        switch (direction) {
            case N:
                return new Position(0, 1);
            case NE:
                return new Position(1, 1);
            case E:
                return new Position(1, 0);
            case SE:
                return new Position(1, -1);
            case S:
                return new Position(0, -1);
            case SW:
                return new Position(-1, -1);
            case W:
                return new Position(-1, 0);
            case NW:
                return new Position(-1, 1);
            default:
                throw new IllegalArgumentException("what kind of direction you want to map into vector?");
        }
    }
}
