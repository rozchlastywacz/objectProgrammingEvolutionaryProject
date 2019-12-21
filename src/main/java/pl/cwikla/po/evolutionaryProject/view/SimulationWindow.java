package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.controller.Simulator;
import pl.cwikla.po.evolutionaryProject.model.Config;

import javax.swing.*;
import java.awt.*;

public class SimulationWindow {
    private Config config;
    private JFrame mainWindow;
    private JPanel innerWindow;

    public SimulationWindow(Config config) {
        this.config = config;
        mainWindow = new JFrame("Evolutionary Project");
        mainWindow.setLayout(new FlowLayout());
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setMinimumSize(new Dimension(400, 400));

        innerWindow = new JPanel();
        innerWindow.setLayout(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(innerWindow);
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.VERTICAL));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1800, 1000));
        mainWindow.add(scrollPane);
        //region Menu Bar

        JMenuBar menuBar = new JMenuBar();
        JMenu configMenu = new JMenu("Click here, leader of Muses!");
        JMenuItem addSimulationMenuItem = new JMenuItem("Add new simulation based on config.json");
        addSimulationMenuItem.addActionListener(e -> addSimulation());
        configMenu.add(addSimulationMenuItem);
        menuBar.add(configMenu);

        mainWindow.setJMenuBar(menuBar);

        //endregion
        mainWindow.setVisible(true);
    }

    public void addSimulationPanel(SimulationPanel simulationPanel) {
        innerWindow.add(simulationPanel);
        mainWindow.pack();
    }

    public void addSimulation() {
        Simulator simulator = new Simulator(config);
        this.addSimulationPanel(simulator.getSimulationPanel());
        Thread simulation = new Thread(simulator);
        simulation.start();
    }
}
