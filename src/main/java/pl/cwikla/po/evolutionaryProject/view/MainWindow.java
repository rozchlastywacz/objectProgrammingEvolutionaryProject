package pl.cwikla.po.evolutionaryProject.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.cwikla.po.evolutionaryProject.controller.SimulationEngine;
import pl.cwikla.po.evolutionaryProject.model.Config;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow {
    public static void main(String[] args) throws IOException {
        Config config = new ObjectMapper()
                .readValue(ClassLoader
                .getSystemResourceAsStream("config.json"), Config.class);
        SimulationEngine simulationEngine = SimulationEngine.create(config);

        JFrame window = new JFrame("EvolutionaryProject");
        window.setLayout(new FlowLayout());
        //region Menu Bar

//        JMenuBar menuBar = new JMenuBar();
//        JMenu configMenu = new JMenu("Configuration");
//        JMenuItem addSimulationMenuItem = new JMenuItem("Add new simulation");
//
//        configMenu.add(addSimulationMenuItem);
//        menuBar.add(configMenu);
//
//        window.setJMenuBar(menuBar);

        //endregion

        //region Simulation Panel
//        JPanel simulationPanel = new JPanel();
//        simulationPanel.setLayout(new BorderLayout());

        MapPanel mapPanel = new MapPanel(simulationEngine.getWorldMap());
        window.add(mapPanel);

        //endregion

//        window.add(simulationPanel);
//        window.add(new SimulationPanel());

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(1000, 1000));
        window.pack();
        window.setVisible(true);
        mapPanel.repaint();
    }
}
