package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.model.Animal;
import pl.cwikla.po.evolutionaryProject.model.Position;
import pl.cwikla.po.evolutionaryProject.model.TorusWorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MapPanel extends JPanel {
    private TorusWorldMap map;
    private Map<Position, JButton> buttonMap;
    private int startEnergy;


    public MapPanel(TorusWorldMap map, int startEnergy) {
        this.map = map;
        this.setBackground(Color.BLACK);
        buttonMap = new HashMap<>();
        this.startEnergy = startEnergy;
        initializeSet();
        this.setMinimumSize(new Dimension(540,540));
        this.setPreferredSize(new Dimension(map.getWidth()*13,map.getHeight()*13));
        this.setLayout(null);
    }

    private void initializeSet() {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                buttonMap.put(new Position(i, j), new JButton());
            }
        }
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int xScale = this.getWidth() / map.getWidth();
        int yScale = this.getHeight() / map.getHeight();
        buttonMap.keySet().forEach(position -> {
            if (map.isInJungle(position)) {
                g2.setColor(new Color(102,255,102));
            } else {
                g2.setColor(new Color(255,255,153));
            }
            g2.fillRect(position.getX() * xScale, position.getY() * yScale, xScale, yScale);
            if (map.getAllPositions().contains(position)) {
                if (map.isGrassAt(position)) {
                    g2.setColor(new Color(0,80,0));
                }
                Animal animal = map.getTheStrongestAnimal(position).orElse(null);
                if (animal != null) {
                    g2.setColor(animal.getEnergy() > startEnergy ? Color.MAGENTA : Color.RED);
                }
                g2.fillOval(position.getX() * xScale + xScale / 4,
                        position.getY() * yScale + yScale / 4,
                        xScale / 2,
                        yScale / 2
                );
            }
        });

        notifyAll();
    }


}
