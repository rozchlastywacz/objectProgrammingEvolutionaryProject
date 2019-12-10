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

    public MapDirection rotateBy(int angle){
        return null;
    }
}
