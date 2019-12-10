import java.util.Objects;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
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

    public boolean precedes(Position other){
        return this.x <= other.x && this.y <= other.y;
    }


    public boolean follows(Position other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Position upperRight(Position other){
        return new Position(
                Math.max(this.x, other.x),
                Math.max(this.y, other.y)
        );
    }

    public Position lowerLeft(Position other){
        return new Position(
                Math.min(this.x, other.x),
                Math.min(this.y, other.y)
        );
    }

    public Position add(Position other){
        return new Position(
                this.x+other.y,
                this.y+other.y
        );
    }

    public Position substract(Position other){
        return new Position(
                this.x - other.x,
                this.y - other.y
        );
    }

    public Position opposite(){
        return new Position(
                -this.x,
                -this.y
        );
    }

}
