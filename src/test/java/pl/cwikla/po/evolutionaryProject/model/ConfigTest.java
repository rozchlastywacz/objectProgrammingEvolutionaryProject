package pl.cwikla.po.evolutionaryProject.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

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