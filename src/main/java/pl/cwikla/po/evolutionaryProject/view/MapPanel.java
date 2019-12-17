package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.model.Animal;
import pl.cwikla.po.evolutionaryProject.model.TorusWorldMap;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private TorusWorldMap map;


    public MapPanel(TorusWorldMap map) {
        this.map = map;
//        this.setMinimumSize(new Dimension(600, 600));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int xScale = this.getWidth() / map.getWidth();
        int yScale = this.getHeight() / map.getHeight();
        map.getAllPositions().forEach(position -> {
            if (map.isInJungle(position)) {
                g2.setColor(Color.CYAN);
            } else {
                g2.setColor(Color.BLUE);
            }
            g2.fillRect(position.getX(), position.getY(), xScale, yScale);
            if (map.isGrassAt(position)) {
                g2.setColor(Color.GREEN);
            }
            Animal animal = map.getTheStrongestAnimal(position);
            if (animal != null) {
                g2.setColor(Color.RED);
            }
            g2.fillRect(position.getX(), position.getY(), xScale / 2, yScale / 2);
        });
    }


}
