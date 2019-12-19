package pl.cwikla.po.evolutionaryProject.controller;

import pl.cwikla.po.evolutionaryProject.model.Config;
import pl.cwikla.po.evolutionaryProject.view.SimulationPanel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulator implements Runnable {
    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private SimulationEngine simulationEngine;
    private final SimulationPanel simulationPanel;
    private int stepDelay;

    public Simulator(Config config) {
        simulationEngine = SimulationEngine.create(config);
        simulationPanel = new SimulationPanel(this, config);
        stepDelay = 0;
    }

    public void setStepDelay(int stepDelay) {
        this.stepDelay = stepDelay;
    }

    public SimulationEngine getSimulationEngine() {
        return simulationEngine;
    }

    public SimulationPanel getSimulationPanel() {
        return simulationPanel;
    }

    public void toggleSimulation() {
        this.isRunning.set(!isRunning.get());
    }

    public void run() {
        while (true) {
            if (isRunning.get()) {
                simulationEngine.step();
                simulationPanel.repaintMapPanel();
                try {
                    TimeUnit.MILLISECONDS.sleep(stepDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
