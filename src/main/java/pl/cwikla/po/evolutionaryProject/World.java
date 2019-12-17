package pl.cwikla.po.evolutionaryProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cwikla.po.evolutionaryProject.controller.SimulationEngine;
import pl.cwikla.po.evolutionaryProject.model.Config;
import pl.cwikla.po.evolutionaryProject.model.Position;

import java.io.IOException;
import java.util.*;

public class World {
    public static void main(String[] args) throws IOException {

        Config config = new ObjectMapper()
                .readValue(ClassLoader
                        .getSystemResourceAsStream("config.json"), Config.class);
        SimulationEngine simulationEngine = SimulationEngine.create(config);
        simulationEngine.getWorldMap().getAllPositions().forEach(System.out::println);
        int i = 1000;
        while(i-- > 0) simulationEngine.step();
        System.out.println();
        simulationEngine.getWorldMap().getAllPositions().forEach(System.out::println);
    }
}
