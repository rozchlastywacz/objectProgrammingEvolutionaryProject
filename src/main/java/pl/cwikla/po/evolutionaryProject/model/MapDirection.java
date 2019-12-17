package pl.cwikla.po.evolutionaryProject.model;

import java.util.Random;

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
    private static final Random RANDOM = new Random();

    MapDirection(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return message;
    }

    public MapDirection rotateBy(int angle) {
        return (values()[(ordinal()+angle)%values().length]);
    }

    public static MapDirection random(){
        return values()[RANDOM.nextInt(values().length)];
    }

}
