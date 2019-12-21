package pl.cwikla.po.evolutionaryProject;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cwikla.po.evolutionaryProject.model.Config;
import pl.cwikla.po.evolutionaryProject.view.SimulationWindow;

import java.io.IOException;

public class World {

    public static void main(String[] args) throws IOException {
        Config config = new ObjectMapper()
                .readValue(ClassLoader
                        .getSystemResourceAsStream("config.json"), Config.class);

        new SimulationWindow(config);
    }

}
