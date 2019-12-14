package pl.cwikla.po.evolutionaryProject.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;


    @JsonCreator
    public Position(@JsonProperty("x") int x,@JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y +")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Position add(Position other){
        return new Position(
                this.x+other.x,
                this.y+other.y
        );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
