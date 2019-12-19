package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.controller.SimulationEngine;
import pl.cwikla.po.evolutionaryProject.controller.Simulator;
import pl.cwikla.po.evolutionaryProject.model.Config;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class SimulationPanel extends JPanel {
    private Simulator simulator;
    private MapPanel mapPanel;
    private JSlider delaySlider;
    private Button toggle;
    private JLabel numberOfAnimals;
    private JLabel numberOfPlants;
    private JLabel dominantGenotype;
    private JLabel averageEnergy;
    private JLabel averageLifetime;
    private JLabel averageNumberOfChildren;
    private JLabel currentDay;

    public SimulationPanel(Simulator simulator, Config config){
        this.simulator = simulator;
        mapPanel = new MapPanel(simulator.getSimulationEngine().getWorldMap(), config.getAnimals().getStartEnergy());
        initUI();
    }

    private void initUI() {
        delaySlider = new JSlider(0,100,0);
        toggle = new Button("Toggle");
        numberOfAnimals = new JLabel();
        numberOfPlants = new JLabel();
        dominantGenotype = new JLabel();
        dominantGenotype.setHorizontalAlignment(JLabel.CENTER);
        averageEnergy = new JLabel();
        averageLifetime = new JLabel();
        averageNumberOfChildren = new JLabel();
        currentDay = new JLabel();


        delaySlider.addChangeListener((ChangeEvent e) -> simulator.setStepDelay(delaySlider.getValue()));
        this.setLayout(new FlowLayout());
        this.add(mapPanel);
//        mapPanel.setPreferredSize(new Dimension(600,600));

        //region stats and so on

        JPanel cockpit = new JPanel();
        cockpit.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        toggle.addActionListener(e -> simulator.toggleSimulation());
//        constraints.weightx = 0.5;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        cockpit.add(toggle, constraints);

        constraints.gridy = 1;
        cockpit.add(delaySlider, constraints);

        //region labels and text fields
        constraints.gridwidth = 1;
        constraints.gridy = 2;
        constraints.weightx = 0.5;
        cockpit.add(new JLabel("Number of animals:"), constraints);

        constraints.gridx = 1;
        cockpit.add(numberOfAnimals, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Number of plants:"), constraints);

        constraints.gridx = 1;
        cockpit.add(numberOfPlants, constraints);


        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        cockpit.add(new JLabel("Dominant Genotype"), constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        cockpit.add(dominantGenotype, constraints);

        constraints.gridy = 6;
        constraints.gridwidth = 1;
        cockpit.add(new JLabel("Average energy"), constraints);

        constraints.gridx =1;
        cockpit.add(averageEnergy, constraints);

        constraints.gridy= 7;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Average lifetime"), constraints);

        constraints.gridx = 1;
        cockpit.add(averageLifetime, constraints);

        constraints.gridy = 8;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Average children"), constraints);

        constraints.gridx = 1;
        cockpit.add(averageNumberOfChildren, constraints);

        constraints.gridy = 9;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Day"), constraints);

        constraints.gridx = 1;
        cockpit.add(currentDay, constraints);
        //endregion


        //endregion


        this.add(cockpit);
    }

    public void repaintMapPanel(){
        synchronized (mapPanel) {
            mapPanel.repaint();
            try {
                mapPanel.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        refreshStats();
    }


    private void refreshStats(){
        SimulationEngine simulationEngine = simulator.getSimulationEngine();
        numberOfAnimals.setText(String.valueOf(simulationEngine.getAliveAnimalList().size()));
        numberOfPlants.setText(String.valueOf(simulationEngine.getWorldMap().getPlantsCounter()));
        dominantGenotype.setText(simulationEngine.getDominantGenotype().toString());
        averageEnergy.setText(String.format("%.2f", simulationEngine.getAverageEnergy()));
        averageLifetime.setText(String.format("%.2f", simulationEngine.getAverageLifetime()));
        averageNumberOfChildren.setText(String.format("%.2f", simulationEngine.getAverageNumberOfChildren()));
        currentDay.setText(String.valueOf(simulationEngine.getNumberOfCurrentDay()));
    }
}
