package pl.cwikla.po.evolutionaryProject.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @Test
    void name() throws Exception{
        Config config = new ObjectMapper()
                .readValue(
                        ClassLoader.getSystemResourceAsStream("config.json"),
                        Config.class);
        config.getAnimals();
    }
}