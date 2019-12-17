package pl.cwikla.po.evolutionaryProject.view;

import pl.cwikla.po.evolutionaryProject.model.Position;
import pl.cwikla.po.evolutionaryProject.model.TorusWorldMap;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    private TorusWorldMap map;
    private int mapHeight;
    private int mapWidth;
    private JPanel[][] panelHolder;

    public MapPanel(TorusWorldMap map){
        this.map = map;
        this.mapHeight = map.getHeight();
        this.mapWidth = map.getWidth();
        this.panelHolder = new JPanel[mapHeight][mapWidth];
        this.setLayout(new GridLayout(mapHeight, mapWidth));
        this.setMinimumSize(new Dimension(600, 600));
        this.add(new Button(), 0, 0);
        initializePanelHolder();
    }

    private void initializePanelHolder(){
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                panelHolder[i][j] = new JPanel();
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int mapPanelWidth = this.getWidth();
        int mapPanelHeight = this.getHeight();
        map.getAllPositions().forEach(position -> {
            //TODO
        });
    }



    private class MapPanelCell extends JPanel{
        private Position position;

        public MapPanelCell(Position position){
            this.position = position;
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            if(map.isInJungle(position)) g.setColor(Color.ORANGE);
            else g.setColor(Color.PINK);

            if(map.sufficientNumberOfAnimals(position)){
                
            }
        }
    }

}
