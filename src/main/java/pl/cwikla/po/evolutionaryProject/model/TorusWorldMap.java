package pl.cwikla.po.evolutionaryProject.model;

import java.util.*;

public class TorusWorldMap implements AnimalObserver {
    private final int width;
    private final int height;
    private final Jungle jungle;
    private final Map<Position, Cell> map;
    private final Set<Position> savannaEmptyPositions;
    private final Set<Position> jungleEmptyPositions;

    public TorusWorldMap(int width, int height, double ratio) {
        this.width = width;
        this.height = height;
        jungle = new Jungle(width, height, ratio);
        map = new HashMap<>();
        savannaEmptyPositions = new TreeSet<>(Comparator.comparing(Position::getX).thenComparing(Position::getY));
        jungleEmptyPositions = new TreeSet<>(Comparator.comparing(Position::getX).thenComparing(Position::getY));

        initializeEmptyPositionSets();
    }

    public Set<Position> getSavannaEmptyPositions() {
        return savannaEmptyPositions;
    }

    public Set<Position> getJungleEmptyPositions() {
        return jungleEmptyPositions;
    }

    public void plantGrassAt(Position position) {
        getOrCreate(position).hasPlant = true;
    }

    public void removeGrassFrom(Position position) {
        map.get(position).hasPlant = false;
        removeIfEmpty(position);
    }

    public boolean isGrassAt(Position position) {
        return map.get(position).hasPlant;
    }

    public void placeAnimal(Animal animal){
        place(animal);
        animal.register(this);
    }

    public TreeSet<Animal> getAnimalsAt(Position position) {
        return map.get(position).occupants;
    }


    private void initializeEmptyPositionSets() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Position position = new Position(i, j);
                if (jungle.contains(position)) {
                    jungleEmptyPositions.add(position);
                } else {
                    savannaEmptyPositions.add(position);
                }
            }
        }
    }


    public Position adjacent(Position position, MapDirection direction) {
        Position vector = unitVectorIn(direction);
        int x = ((((position.getX() + vector.getX()) % width) + width) % width);
        int y = ((((position.getY() + vector.getY()) % height) + height) % height);
        return new Position(x, y);
    }

    private Position unitVectorIn(MapDirection direction) {
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

    @Override
    public void onPositionChanged(Animal animal, Position oldPosition) {
        removeFrom(animal, oldPosition);
        place(animal);
    }

    private void removeFrom(Animal animal, Position position) {
        map.get(position).occupants.remove(animal);
        removeIfEmpty(position);
    }

    private void removeIfEmpty(Position position) {
        if (map.get(position).isEmpty()) {
            map.remove(position);
            addEmptyPosition(position);
        }
    }

    private void place(Animal animal) {
        getOrCreate(animal.getPosition()).occupants.add(animal);
    }


    private Cell getOrCreate(Position position) {
        return map.computeIfAbsent(position, k -> {
            removeEmptyPosition(position);
            return new Cell();
        });
    }

    private void addEmptyPosition(Position position) {
        if (jungle.contains(position)) {
            jungleEmptyPositions.add(position);
        } else {
            savannaEmptyPositions.add(position);
        }
    }

    private void removeEmptyPosition(Position position) {
        if (jungle.contains(position)) {
            jungleEmptyPositions.remove(position);
        } else {
            savannaEmptyPositions.remove(position);
        }
    }


    private static class Cell {
        private boolean hasPlant = false;
        private final TreeSet<Animal> occupants = new TreeSet<>(Comparator.comparing(Animal::getEnergy).thenComparing(Animal::getId));

        public boolean isEmpty() {
            return !hasPlant && occupants.isEmpty();
        }

    }

    private class Jungle {
        private final int lowX;
        private final int highX;
        private final int lowY;
        private final int highY;


        public Jungle(int width, int height, double ratio) {
            int jungleWidth = (int) Math.round(width * Math.sqrt(ratio));
            lowX = (width - jungleWidth) / 2;
            highX = lowX + jungleWidth - 1;
            int jungleHeight = (int) Math.round(height * Math.sqrt(ratio));
            lowY = (height - jungleHeight) / 2;
            highY = lowY + jungleHeight - 1;
        }

        public boolean contains(Position position) {
            int x = position.getX();
            int y = position.getY();
            return lowX <= x && x <= highX
                    && lowY <= y && y <= highY;
        }
    }
}
