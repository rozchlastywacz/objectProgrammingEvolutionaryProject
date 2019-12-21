package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.controller.SimulationEngine;
import pl.cwikla.po.evolutionaryProject.controller.Simulator;
import pl.cwikla.po.evolutionaryProject.model.Animal;
import pl.cwikla.po.evolutionaryProject.model.Config;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class SimulationPanel extends JPanel {
    private Simulator simulator;
    private MapPanel mapPanel;
    private JSlider delaySlider;
    private Button toggle;
    private JLabel numberOfAnimals;
    private JLabel numberOfPlants;
    private JLabel dominantGenotype;
    private JLabel dominantGenotypeNumber;
    private JLabel averageEnergy;
    private JLabel averageLifetime;
    private JLabel averageNumberOfChildren;
    private JLabel currentDay;

    private JLabel trackedAnimalID;
    private JLabel trackedAnimalChildren;
    private JLabel trackedAnimalDescendants;
    private JLabel trackedAnimalDeathTime;

    private Button toggleDominantGenes;
    private Button saveStatisticsToFile;


    public SimulationPanel(Simulator simulator, Config config) {
        this.simulator = simulator;
        mapPanel = new MapPanel(simulator.getSimulationEngine().getWorldMap(), config.getAnimals().getStartEnergy());
        initUI();
    }

    private void initUI() {
        delaySlider = new JSlider(0, 500, 0);
        toggle = new Button("Toggle");
        numberOfAnimals = new JLabel();
        numberOfPlants = new JLabel();
        dominantGenotype = new JLabel();
        dominantGenotypeNumber = new JLabel();
        dominantGenotype.setHorizontalAlignment(JLabel.CENTER);
        averageEnergy = new JLabel();
        averageLifetime = new JLabel();
        averageNumberOfChildren = new JLabel();
        currentDay = new JLabel();

        trackedAnimalID = new JLabel();
        trackedAnimalDescendants = new JLabel();
        trackedAnimalChildren = new JLabel();
        trackedAnimalDeathTime = new JLabel();

        toggleDominantGenes = new Button("Toggle dominant genes");

        saveStatisticsToFile = new Button("Save statistics to file");

        delaySlider.addChangeListener((ChangeEvent e) -> simulator.setStepDelay(delaySlider.getValue()));
        this.setLayout(new FlowLayout());
        this.add(mapPanel);
        this.setBorder(new BorderUIResource.LineBorderUIResource(Color.BLACK));
        JPanel cockpit = new JPanel();
        //region stats and so on
        cockpit.setLayout(new GridBagLayout());
        cockpit.setBorder(new BorderUIResource.LineBorderUIResource(Color.BLACK));
        GridBagConstraints constraints = new GridBagConstraints();

        toggle.addActionListener(e -> simulator.toggleSimulation());
        constraints.gridwidth = 1;
        constraints.weightx = 0.5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        cockpit.add(toggle, constraints);

        constraints.weightx = 1;

        constraints.gridy = 1;
        cockpit.add(delaySlider, constraints);

        constraints.gridx = 1;
        JLabel delayLabel = new JLabel(">0ms per frame");
        cockpit.add(delayLabel, constraints);

        delaySlider.addChangeListener(c ->{
            delayLabel.setText(">" + delaySlider.getValue() + "ms per frame");
        });


        //region labels and text fields
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.gridy = 2;
        constraints.weightx = 0.5;
        cockpit.add(new JLabel("Number of animals"), constraints);

        constraints.gridx = 1;
        cockpit.add(numberOfAnimals, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Number of plants"), constraints);

        constraints.gridx = 1;
        cockpit.add(numberOfPlants, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        cockpit.add(new JLabel("Dominant Genotype"), constraints);

        constraints.gridx = 1;
        cockpit.add(dominantGenotypeNumber, constraints);

        constraints.gridwidth = 2;
        constraints.gridy = 5;
        constraints.gridx = 0;
        cockpit.add(dominantGenotype, constraints);

        constraints.gridy = 6;
        constraints.gridwidth = 1;
        cockpit.add(new JLabel("Average energy"), constraints);

        constraints.gridx = 1;
        cockpit.add(averageEnergy, constraints);

        constraints.gridy = 7;
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

        constraints.gridy = 10;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        cockpit.add(new JLabel("Tracked Animal", SwingConstants.CENTER), constraints);

        constraints.gridy = 11;
        constraints.gridwidth = 1;
        cockpit.add(new JLabel("ID"), constraints);

        constraints.gridx = 1;
        cockpit.add(trackedAnimalID, constraints);

        constraints.gridy = 12;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Children"), constraints);

        constraints.gridx = 1;
        cockpit.add(trackedAnimalChildren, constraints);

        constraints.gridy = 13;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Descendants"), constraints);

        constraints.gridx = 1;
        cockpit.add(trackedAnimalDescendants, constraints);

        constraints.gridy = 14;
        constraints.gridx = 0;
        cockpit.add(new JLabel("Day of death"), constraints);

        constraints.gridx = 1;
        cockpit.add(trackedAnimalDeathTime, constraints);

        toggleDominantGenes.addActionListener(e -> {
            mapPanel.toggleShowDominantGenotypes();
            mapPanel.setDominantGenotypeAnimals(simulator.getSimulationEngine().getAnimalsWithDominantGenotype().getValue());
        });
        constraints.gridy = 0;
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        cockpit.add(toggleDominantGenes, constraints);


        saveStatisticsToFile.addActionListener(e -> {
            simulator.getSimulationEngine().getStatistics().saveToFile();
        });
        constraints.gridy = 15;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        cockpit.add(saveStatisticsToFile, constraints);
        //endregion
        //endregion
        this.add(cockpit);
    }

    public void repaintMapPanel() {
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


    private void refreshStats() {
        SimulationEngine simulationEngine = simulator.getSimulationEngine();
        numberOfAnimals.setText(String.valueOf(simulationEngine.getAliveAnimalList().size()));
        numberOfPlants.setText(String.valueOf(simulationEngine.getWorldMap().getPlantsCounter()));
        dominantGenotypeNumber.setText(String.valueOf(simulationEngine.getAnimalsWithDominantGenotype().getValue().size()));
        dominantGenotype.setText(simulationEngine.getDominantGenotype().toString());
        averageEnergy.setText(String.format("%.2f", simulationEngine.getAverageEnergy()));
        averageLifetime.setText(String.format("%.2f", simulationEngine.getAverageLifetime()));
        averageNumberOfChildren.setText(String.format("%.2f", simulationEngine.getAverageNumberOfChildren()));
        currentDay.setText(String.valueOf(simulationEngine.getNumberOfCurrentDay()));
        Animal trackedAnimal = mapPanel.getTrackedAnimal();
        if (trackedAnimal != null) {
            trackedAnimalID.setText(String.valueOf(trackedAnimal.getId()));
            trackedAnimalChildren.setText(String.valueOf(trackedAnimal.getNumberOfChildren()));
            trackedAnimalDescendants.setText(String.valueOf(trackedAnimal.getNumberOfDescendants()));
            if (trackedAnimal.isDead())
                trackedAnimalDeathTime.setText(String.valueOf(trackedAnimal.getDayOfDeath()));
            else
                trackedAnimalDeathTime.setText(String.valueOf(simulationEngine.getDay()));
        }
    }
}
