package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.model.Animal;
import pl.cwikla.po.evolutionaryProject.model.AnimalGenotype;
import pl.cwikla.po.evolutionaryProject.model.Position;
import pl.cwikla.po.evolutionaryProject.model.TorusWorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapPanel extends JPanel {
    private TorusWorldMap map;
    private Map<Position, JButton> buttonMap;
    private int startEnergy;

    private Animal trackedAnimal;

    private List<Animal> dominantGenotypeAnimals;
    private AtomicBoolean showDominantGenotypeAnimals = new AtomicBoolean(false);

    public MapPanel(TorusWorldMap map, int startEnergy) {
        this.map = map;
        buttonMap = new HashMap<>();
        this.startEnergy = startEnergy;
        this.setMinimumSize(new Dimension(540, 540));
        this.setPreferredSize(new Dimension(map.getWidth() * 13, map.getHeight() * 13));
        this.setLayout(new GridLayout(map.getWidth(), map.getHeight()));
        initializeButtons();
    }


    private void initializeButtons() {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                JButton button = new JButton();
                Position position = new Position(i, j);
                buttonMap.put(position, button);
                this.add(button);
            }
        }
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        buttonMap.forEach((position, button) -> {
            button.setToolTipText("You really thinks that`s an animal?");
            for (ActionListener actionListener : button.getActionListeners()) {
                button.removeActionListener(actionListener);
            }
            if (map.isInJungle(position)) {
                button.setBackground(new Color(102, 255, 102));
            } else {
                button.setBackground(new Color(255, 255, 153));
            }
            if (map.getAllPositions().contains(position)) {
                if (map.isGrassAt(position)) {
                    button.setBackground(new Color(0, 80, 0));
                    button.setToolTipText("Zielsko xD");
                }
                Animal animal = map.getTheStrongestAnimal(position).orElse(null);
                if (animal != null) {
                    if (!animal.equals(trackedAnimal)) {
                        button.setBackground(animal.getEnergy() > startEnergy ? new Color(0,0,150) : new Color(51,204,255));
                        button.setToolTipText(animal.getGenotype().toString());
                        button.addActionListener(e -> {
                            trackedAnimal = animal;
                            button.setBackground(new Color(255,0,0));
                        });
                    }
                }
            }

        });
        if (showDominantGenotypeAnimals.get()) {
            dominantGenotypeAnimals.forEach(animal -> {
                JButton button = buttonMap.get(animal.getPosition());
                button.setBackground(new Color(200,0,200));
            });
        }
        if(trackedAnimal != null){
            buttonMap.get(trackedAnimal.getPosition()).setBackground(new Color(255,0,0));
        }
        notifyAll();
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public void setDominantGenotypeAnimals(List<Animal> dominantGenotypeAnimals) {
        this.dominantGenotypeAnimals = dominantGenotypeAnimals;
    }

    public void toggleShowDominantGenotypes(){
        showDominantGenotypeAnimals.set(!showDominantGenotypeAnimals.get());
        this.repaint();
    }
}
