public interface IWorldMap {
    boolean canMoveTo(Position position);
    void place(IWorldMapElement element);
    boolean isOccupied(Position position);
    Object objectAt(Position position);
}