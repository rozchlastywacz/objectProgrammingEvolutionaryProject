package pl.cwikla.po.evolutionaryProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cwikla.po.evolutionaryProject.model.Animal;
import pl.cwikla.po.evolutionaryProject.model.Config;
import pl.cwikla.po.evolutionaryProject.model.TorusWorldMap;

import java.util.List;

public class SimulationEngine {
    private final TorusWorldMap worldMap;
    private final List<Animal> animalList;
    private int moveEnergy;
    private int plantEnergy;

    private SimulationEngine(TorusWorldMap worldMap, List<Animal> animalList, int moveEnergy, int plantEnergy) {
        this.worldMap = worldMap;
        this.animalList = animalList;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
    }

    public static SimulationEngine create(String fileName) throws Exception {
        Config config = new ObjectMapper()
                .readValue(ClassLoader
                        .getSystemResourceAsStream("config.json"), Config.class);

        TorusWorldMap worldMap = new TorusWorldMap(
                config.getMap().getWidth(),
                config.getMap().getHeight(),
                config.getMap().getJungleRatio()
                );
        List<Animal> animalList = createAnimalList(config.getAnimals().getNumberOfAnimalsAtStart(), config.getAnimals().getStartEnergy());

        return new SimulationEngine(worldMap, animalList, config.getAnimals().getMoveEnergy(), config.getAnimals().getPlantEnergy());
    }

    private static List<Animal> createAnimalList(int numberOfAnimalsAtStart, int startEnergy) {
        //TODO
        return null;
    }
}
