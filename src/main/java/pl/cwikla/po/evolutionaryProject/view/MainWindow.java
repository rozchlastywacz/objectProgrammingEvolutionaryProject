package pl.cwikla.po.evolutionaryProject.view;

import javax.swing.*;
import java.awt.*;

public class MainWindow {
    public static void main(String[] args) {
        JFrame window = new JFrame("EvolutionaryProject");
        window.setLayout(new FlowLayout());
        //region Menu Bar

        JMenuBar menuBar = new JMenuBar();
        JMenu configMenu = new JMenu("Configuration");
        JMenuItem addSimulationMenuItem = new JMenuItem("Add new simulation");

        configMenu.add(addSimulationMenuItem);
        menuBar.add(configMenu);

        window.setJMenuBar(menuBar);

        //endregion

        //region Simulation Panel
        JPanel simulationPanel = new JPanel();
        simulationPanel.setLayout(new BorderLayout());

        JPanel mapPanel = new JPanel();
        mapPanel.add(new Button("mapPanel"));
        simulationPanel.add(mapPanel, BorderLayout.LINE_START);

        JList animalList = new JList();
        animalList.add(new Button("animal 1"));
        animalList.add(new Button("animal 2"));
        animalList.add(new Button("animal 3"));
        simulationPanel.add(animalList, BorderLayout.CENTER);

        JPanel animalInfo = new JPanel();
        animalInfo.add(new Button("animal info"));
        simulationPanel.add(animalInfo, BorderLayout.LINE_END);

        JPanel simulationInfo = new JPanel();
        simulationInfo.add(new Button("simulation inf0 1"));
        simulationInfo.add(new Button("simulation inf0 2"));
        simulationInfo.add(new Button("simulation inf0 3"));
        simulationPanel.add(simulationInfo, BorderLayout.PAGE_END);

        //endregion

        window.add(simulationPanel);
        window.add(new SimulationPanel());

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(400, 600));
        window.pack();
        window.setVisible(true);
    }
}
