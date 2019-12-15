package pl.cwikla.po.evolutionaryProject.controller;

import pl.cwikla.po.evolutionaryProject.model.*;

import java.util.*;

public class SimulationEngine {
    private final TorusWorldMap worldMap;
    private final List<Animal> animalList;
    private final Set<Position> positionsOfInterest;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int startEnergy;

    private static final Random RANDOM = new Random();

    //region creating engine
    private SimulationEngine(TorusWorldMap worldMap, List<Animal> animalList, int moveEnergy, int plantEnergy, int startEnergy) {
        this.worldMap = worldMap;
        this.animalList = animalList;
        this.positionsOfInterest = new HashSet<>();
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
    }

    public static SimulationEngine create(Config config) {
//        Config config = new ObjectMapper()
//                .readValue(ClassLoader
//                        .getSystemResourceAsStream(fileName), Config.class);

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
    //region growing plants
    private static void growPlants(TorusWorldMap worldMap) {
        TreeSet<Position> jungleEmptyPositions = (TreeSet<Position>) worldMap.getJungleEmptyPositions();
        TreeSet<Position> savannaEmptyPositions = (TreeSet<Position>) worldMap.getSavannaEmptyPositions();
        Position inJungle = findEmptySpace(jungleEmptyPositions);
        Position onSavanna = findEmptySpace(savannaEmptyPositions);
        if (inJungle != null) worldMap.plantGrass(inJungle);
        if (onSavanna != null) worldMap.plantGrass(onSavanna);
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
                                startEnergy)
                )
        );

        return randomAnimals;
    }
    //endregion

    public void step() {
        removeDeadAnimals();
        moveEveryAnimal();
        eatPlants();
        procreate();
        growPlants(worldMap);
    }

    private void removeDeadAnimals() {
        animalList.stream().filter(Animal::isDead).forEach(animal -> {
            worldMap.removeAnimal(animal);
            animalList.remove(animal);
        });
    }

    private void moveEveryAnimal() {
        animalList.forEach(animal -> {
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
                    TreeSet<Animal> animals = worldMap.getAnimalsAt(position);
                    Animal strongest = animals.last();
                    Set<Animal> equallyStrong = new TreeSet<>();
                    equallyStrong.add(strongest);
                    Animal secondStrongest = animals.lower(strongest);

                    while (secondStrongest != null && secondStrongest.getEnergy() == strongest.getEnergy()) {
                        equallyStrong.add(secondStrongest);
                        secondStrongest = animals.lower(secondStrongest);
                    }

                    int energy = plantEnergy/equallyStrong.size();
                    equallyStrong.forEach(animal -> animal.eatGrass(energy));
                });
    }

    private void procreate() {
        positionsOfInterest.stream()
                .filter(worldMap::sufficientNumberOfAnimals)
                .forEach(position -> {
                    Animal firstParent = worldMap.getAnimalsAt(position).last();
                    Animal secondParent = worldMap.getAnimalsAt(position).lower(firstParent);
                    if(enoughEnergy(firstParent, secondParent)){
                        Animal child = makeAChild(firstParent, secondParent);
                        animalList.add(child);
                        worldMap.placeAnimal(child);
                    }
                });
    }

    private Animal makeAChild(Animal firstParent, Animal secondParent) {
        return new Animal(
                AnimalGenotype.mix(firstParent.getGenotype(), secondParent.getGenotype()),
                MapDirection.random(),
                worldMap.adjacent(firstParent.getPosition(), MapDirection.random()),
                firstParent.getEnergy()/2+secondParent.getEnergy()/2
        );
    }

    private boolean enoughEnergy(Animal firstParent, Animal secondParent) {
        return firstParent.getEnergy() >= startEnergy/2 && secondParent.getEnergy() >= startEnergy/2;
    }


}
