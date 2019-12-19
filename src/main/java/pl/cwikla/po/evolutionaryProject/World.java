package pl.cwikla.po.evolutionaryProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cwikla.po.evolutionaryProject.model.Config;
import pl.cwikla.po.evolutionaryProject.view.SimulationWindow;

import java.io.IOException;

public class World {
    private static Config config;
    private static SimulationWindow simulationWindow;
    public static void main(String[] args) throws IOException {
        config = new ObjectMapper()
                .readValue(ClassLoader
                        .getSystemResourceAsStream("config.json"), Config.class);

        simulationWindow = new SimulationWindow(config);
    }

}
