package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.model.Animal;
import pl.cwikla.po.evolutionaryProject.model.Position;
import pl.cwikla.po.evolutionaryProject.model.TorusWorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MapPanel extends JPanel {
    private TorusWorldMap map;
    private Set<Position> positions;


    public MapPanel(TorusWorldMap map) {
        this.map = map;
        this.setBackground(Color.BLACK);
        positions = new HashSet<>();
        initializeSet();
    }

    private void initializeSet(){
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                positions.add(new Position(i,j));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int xScale = this.getWidth() / map.getWidth();
        int yScale = this.getHeight() / map.getHeight();
        positions.forEach(position -> {
            if (map.isInJungle(position)) {
                g2.setColor(Color.CYAN);
            } else {
                g2.setColor(Color.BLUE);
            }
            g2.fillRect(position.getX() * xScale, position.getY() * yScale, xScale, yScale);
            if(map.getAllPositions().contains(position)) {
                if (map.isGrassAt(position)) {
                    g2.setColor(Color.GREEN);
                }
                Animal animal = map.getTheStrongestAnimal(position);
                if (animal != null) {
                    g2.setColor(Color.RED);
                }
                g2.fillRect(position.getX() * xScale + xScale / 4,
                        position.getY() * yScale + yScale / 4,
                        xScale / 2,
                        yScale / 2
                );
            }
        });
    }
}
