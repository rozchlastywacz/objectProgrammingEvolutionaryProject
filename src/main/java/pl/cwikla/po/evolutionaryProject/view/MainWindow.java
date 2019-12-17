package pl.cwikla.po.evolutionaryProject.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cwikla.po.evolutionaryProject.controller.SimulationEngine;
import pl.cwikla.po.evolutionaryProject.model.Config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainWindow {
    public static void main(String[] args) throws IOException, InterruptedException {
        Config config = new ObjectMapper()
                .readValue(ClassLoader
                .getSystemResourceAsStream("config.json"), Config.class);
        SimulationEngine simulationEngine = SimulationEngine.create(config);

        JFrame window = new JFrame("EvolutionaryProject");
        window.setLayout(new FlowLayout());

        MapPanel mapPanel = new MapPanel(simulationEngine.getWorldMap());
        mapPanel.setPreferredSize(new Dimension(1000, 1000));
        window.add(mapPanel);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        int i = 1000;
        TimeUnit.SECONDS.sleep(5);
        while (i-- > 0) {
            simulationEngine.step();
            TimeUnit.MILLISECONDS.sleep(100);
            mapPanel.repaint();
        System.out.println(i);
            System.out.println(simulationEngine.getAliveAnimalList().size());
        }
    }
}
