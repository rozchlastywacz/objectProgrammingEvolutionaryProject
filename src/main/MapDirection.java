import java.util.LinkedHashMap;

public enum MapDirection {
    N("north"),
    NE("north-east"),
    E("east"),
    SE("south-east"),
    S("south"),
    SW("south-west"),
    W("west"),
    NW("northwest");
    private String message;

    MapDirection(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return message;
    }

    public MapDirection rotateBy(int angle) {
        return (MapDirection.values()[(this.ordinal()+angle)%8]);
    }
}
