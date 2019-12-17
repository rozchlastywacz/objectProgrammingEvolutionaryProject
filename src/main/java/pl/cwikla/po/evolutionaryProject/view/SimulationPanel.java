package pl.cwikla.po.evolutionaryProject.view;

import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel {
    JPanel mapPanel;
    JList animalList;
    JPanel animalInfo;
    JPanel simulationInfo;

    public SimulationPanel(){
        this.setLayout(new BorderLayout());

        mapPanel = new JPanel();

        DefaultListModel dataModel = new DefaultListModel();
        dataModel.addElement("A1");
        dataModel.addElement("A2");
        dataModel.addElement("A3");
        animalList = new JList(dataModel);
        animalList.setCellRenderer(new ListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JPanel pp = new JPanel(new FlowLayout());
                pp.add(new JTextField(value.toString()));
                pp.add(new JTextField("15"));
                pp.add(new JTextField("1567"));
                return pp;
            }
        });
        this.add(animalList, BorderLayout.CENTER);

        animalInfo = new JPanel();
        animalInfo.add(new Button("animal info"));
        this.add(animalInfo, BorderLayout.LINE_END);

        simulationInfo = new JPanel();
        simulationInfo.add(new Button("simulation inf0 1"));
        simulationInfo.add(new Button("simulation inf0 2"));
        simulationInfo.add(new Button("simulation inf0 3"));
        this.add(simulationInfo, BorderLayout.PAGE_END);

    }
}
