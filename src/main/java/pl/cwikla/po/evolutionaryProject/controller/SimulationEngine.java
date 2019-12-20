package pl.cwikla.po.evolutionaryProject.controller;

import pl.cwikla.po.evolutionaryProject.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.*;

public class SimulationEngine {
    private final TorusWorldMap worldMap;
    private final List<Animal> aliveAnimalList;
    private final List<Animal> deadAnimalList;
    private final Set<Position> positionsOfInterest;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int startEnergy;
    private final AtomicInteger day = new AtomicInteger(0);

    private static final Random RANDOM = new Random();

    //region creating engine
    private SimulationEngine(TorusWorldMap worldMap, List<Animal> aliveAnimalList, int moveEnergy, int plantEnergy, int startEnergy) {
        this.worldMap = worldMap;
        this.aliveAnimalList = aliveAnimalList;
        this.deadAnimalList = new LinkedList<>();
        this.positionsOfInterest = new HashSet<>();
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
    }

    public static SimulationEngine create(Config config) {

        TorusWorldMap worldMap = TorusWorldMap.create(config.getMap());
        List<Animal> generationZero = adamsAndEves(
                config.getAnimals().getNumberOfAnimalsAtStart(),
                config.getAnimals().getStartEnergy(),
                config.getMap().getWidth(),
                config.getMap().getHeight()
        );

        generationZero.forEach(worldMap::placeAnimal);

        growPlants(worldMap);

        return new SimulationEngine(
                worldMap,
                generationZero,
                config.getAnimals().getMoveEnergy(),
                config.getAnimals().getPlantEnergy(),
                config.getAnimals().getStartEnergy()
        );
    }

    //endregion

    //region getters

    public TorusWorldMap getWorldMap() {
        return worldMap;
    }

    public List<Animal> getAliveAnimalList() {
        return aliveAnimalList;
    }

    public Map.Entry<AnimalGenotype, List<Animal>> getAnimalsWithDominantGenotype(){
        return aliveAnimalList.stream()
                .collect(groupingBy(Animal::getGenotype))
                .entrySet().stream()
                .max(Comparator.comparing(a -> a.getValue().size()))
                .get();
    }

    public AnimalGenotype getDominantGenotype(){
        return getAnimalsWithDominantGenotype().getKey();
    }


    public double getAverageEnergy(){
        return aliveAnimalList.stream().mapToInt(Animal::getEnergy).average().orElse(-1);
    }

    public double getAverageLifetime(){
        return deadAnimalList.stream().mapToInt(Animal::getAge).average().orElse(-1);
    }

    public double getAverageNumberOfChildren(){
        return aliveAnimalList.stream().mapToInt(Animal::getNumberOfChildren).average().orElse(-1);
    }

    public int getNumberOfCurrentDay(){
        return day.get();
    }
    //endregion

    //region growing plants
    private static void growPlants(TorusWorldMap worldMap) {
        TreeSet<Position> jungleEmptyPositions = (TreeSet<Position>) worldMap.getJungleEmptyPositions();
        TreeSet<Position> savannaEmptyPositions = (TreeSet<Position>) worldMap.getSavannaEmptyPositions();
        Position inJungle = findEmptySpace(jungleEmptyPositions);
        Position onSavanna = findEmptySpace(savannaEmptyPositions);
        if (inJungle != null) {
            worldMap.plantGrass(inJungle);

        }
        if (onSavanna != null) {
            worldMap.plantGrass(onSavanna);
        }
    }

    private static Position findEmptySpace(TreeSet<Position> Positions) {
        if (Positions.isEmpty()) return null;
        int random = RANDOM.nextInt(Positions.size());
        int i = 0;
        Position emptyPosition = null;
        for (Position position : Positions) {
            if (i == random) emptyPosition = position;
            i++;
        }
        return emptyPosition;
    }

    //endregion

    //region first animals creation
    private static List<Animal> adamsAndEves(int numberOfAnimalsAtStart, int startEnergy, int width, int height) {
        Set<Position> randomPositions = new HashSet<>();
        while (randomPositions.size() != numberOfAnimalsAtStart) {
            randomPositions.add(new Position(RANDOM.nextInt(width), RANDOM.nextInt(height)));
        }
        List<Animal> randomAnimals = new LinkedList<>();

        randomPositions.forEach(
                position -> randomAnimals.add(
                        new Animal(
                                AnimalGenotype.random(),
                                MapDirection.random(),
                                position,
                                startEnergy,
                                null,
                                null)
                )
        );

        return randomAnimals;
    }
    //endregion

    //region one step forward and two steps back
    public void step() {
        positionsOfInterest.clear();

        removeDeadAnimals();
        moveEveryAnimal();
        eatPlants();
        procreate();
        growPlants(worldMap);
        day.incrementAndGet();
    }

    private void removeDeadAnimals() {
        aliveAnimalList.removeIf(animal -> {
            if(animal.isDead()){
                worldMap.removeAnimal(animal);
                deadAnimalList.add(animal);
                animal.setDayOfDeath(day.get());
                return true;
            }
            return false;
        });
    }

    private void moveEveryAnimal() {
        aliveAnimalList.forEach(animal -> {
            Position oldPosition = animal.getPosition();
            MapDirection oldOrientation = animal.getOrientation();
            MapDirection newOrientation = oldOrientation.rotateBy(animal.getGenotype().angle());
            Position newPosition = worldMap.adjacent(oldPosition, newOrientation);
            animal.moveTo(newPosition, newOrientation, moveEnergy);
            positionsOfInterest.add(newPosition);
        });
    }

    private void eatPlants() {
        positionsOfInterest.stream()
                .filter(worldMap::isGrassAt)
                .forEach(position -> {
                    Collection<Animal> theStrongestGroup = worldMap.getTheStrongestGroup(position);
                    int energy = plantEnergy/ theStrongestGroup.size();
                    theStrongestGroup.forEach(animal -> animal.eatGrass(energy));

                    worldMap.removeGrassFrom(position);
                });
    }

    private void procreate() {
        positionsOfInterest.stream()
                .filter(worldMap::sufficientNumberOfAnimals)
                .forEach(position -> {
                    Animal[] parents = worldMap.getTheStrongestPair(position);
                    Animal firstParent = parents[0];
                    Animal secondParent = parents[1];
                    if(enoughEnergy(firstParent, secondParent)){
                        Animal child = firstParent.makeAChild(secondParent, worldMap.adjacent(position, MapDirection.random()));
                        aliveAnimalList.add(child);
                        worldMap.placeAnimal(child);
                    }
                });
    }

    private boolean enoughEnergy(Animal firstParent, Animal secondParent) {
        return firstParent != null && secondParent != null &&
                firstParent.getEnergy() >= startEnergy/2 && secondParent.getEnergy() >= startEnergy/2;
    }
    //endregion

}
