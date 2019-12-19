package pl.cwikla.po.evolutionaryProject.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TorusWorldMap implements AnimalObserver {
    private final int width;
    private final int height;
    private final Jungle jungle;
    public final Map<Position, Cell> map;
    private final Set<Position> savannaEmptyPositions;
    private final Set<Position> jungleEmptyPositions;
    private final AtomicInteger plantsCounter = new AtomicInteger(0);

    //region Initialization
    private TorusWorldMap(int width, int height, Jungle jungle, Set<Position> savannaEmptyPositions, Set<Position> jungleEmptyPositions) {
        this.width = width;
        this.height = height;
        this.jungle = jungle;
        this.map = new ConcurrentHashMap<>();
        this.savannaEmptyPositions = savannaEmptyPositions;
        this.jungleEmptyPositions = jungleEmptyPositions;
    }

    public static TorusWorldMap create(Config.MapConfig config) {
        Jungle jungle = new Jungle(
                config.getWidth(),
                config.getHeight(),
                config.getJungleRatio());
        Set<Position> savannaEmptyPositions = new TreeSet<>(Comparator.comparing(Position::getX).thenComparing(Position::getY));
        Set<Position> jungleEmptyPositions = new TreeSet<>(Comparator.comparing(Position::getX).thenComparing(Position::getY));

        initializeEmptyPositionSets(config, jungle, savannaEmptyPositions, jungleEmptyPositions);

        return new TorusWorldMap(
                config.getWidth(),
                config.getHeight(),
                jungle,
                savannaEmptyPositions,
                jungleEmptyPositions
        );
    }

    private static void initializeEmptyPositionSets(Config.MapConfig config, Jungle jungle, Set<Position> savannaEmptyPositions, Set<Position> jungleEmptyPositions) {
        for (int i = 0; i < config.getWidth(); i++) {
            for (int j = 0; j < config.getHeight(); j++) {
                Position position = new Position(i, j);
                if (jungle.contains(position)) {
                    jungleEmptyPositions.add(position);
                } else {
                    savannaEmptyPositions.add(position);
                }
            }
        }
    }

    //endregion
    //region Empty Positions handling
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

    //endregion
    //region Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public AtomicInteger getPlantsCounter() {
        return plantsCounter;
    }

    public Set<Position> getSavannaEmptyPositions() {
        return savannaEmptyPositions;
    }

    public Set<Position> getJungleEmptyPositions() {
        return jungleEmptyPositions;
    }

    public Set<Animal> getAnimalsAt(Position position) {
        if (map.get(position) != null)
            return map.get(position).occupants;
        return Collections.emptySet();
    }

    public Set<Position> getAllPositions() {
        return map.keySet();
    }

    //endregion
    //region Grass handling
    public void plantGrass(Position position) {
        getOrCreate(position).hasPlant.set(true);
        plantsCounter.incrementAndGet();
    }

    public void removeGrassFrom(Position position) {
        map.get(position).hasPlant.set(false);
        plantsCounter.decrementAndGet();
        removeIfEmpty(position);
    }

    public boolean isGrassAt(Position position) {
        return map.get(position) != null && !map.get(position).isEmpty() && map.get(position).hasPlant.get();
    }

    //endregion
    //region Adjacent Position Getter
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

    //endregion
    //region Animal and other things handling
    @Override
    public void onPositionChanged(Animal animal, Position oldPosition) {
        removeFrom(animal, oldPosition);
        place(animal);
    }

    public boolean sufficientNumberOfAnimals(Position position) {
        return map.get(position).occupants.size() > 1;
    }

    public void placeAnimal(Animal animal) {
        place(animal);
        animal.register(this);
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

    public void removeAnimal(Animal animal) {
        removeFrom(animal, animal.getPosition());
        animal.unregister(this);
    }


    private void removeFrom(Animal animal, Position position) {
        Set<Animal> occupants = map.get(position).occupants;
        if (!occupants.remove(animal)) {
            throw new IllegalStateException();
        }

        removeIfEmpty(position);
    }

    private void removeIfEmpty(Position position) {
        if (map.get(position).isEmpty()) {
            map.remove(position);
            addEmptyPosition(position);
        }
    }


    public boolean isInJungle(Position position) {
        return jungle.contains(position);
    }

    public Optional<Animal> getTheStrongestAnimal(Position position) {
        return map.get(position).alpha();
    }

    public Collection<Animal> getTheStrongestGroup(Position position){
        Animal alpha = getTheStrongestAnimal(position).get();
        return map.get(position).occupants.stream().filter(a -> a.getEnergy() == alpha.getEnergy()).collect(Collectors.toList());
    }

    public Animal[] getTheStrongestPair(Position position){
        return map.get(position).alphaMaleAndFemale();
    }

    //endregion
    //region private classes
    public class Cell {
        private AtomicBoolean hasPlant = new AtomicBoolean(false);
        public final Set<Animal> occupants = new HashSet<>();


        public Optional<Animal> alpha() {
            return occupants.stream().max(Comparator.comparing(Animal::getEnergy));
        }

        public Animal[] alphaMaleAndFemale(){
            return occupants.stream().sorted(Comparator.comparing(Animal::getEnergy).reversed()).limit(2).toArray(Animal[]::new);
        }
        public boolean isEmpty() {
            return !hasPlant.get() && occupants.isEmpty();
        }

    }

    private static class Jungle {
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
    //endregion
}
