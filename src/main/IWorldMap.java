public interface IWorldMap {
    Position adjacent(Position position, MapDirection direction);
    void placeAt(IWorldMapElement element, Position position);
    Object objectAt(Position position);
}