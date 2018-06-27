package it.ingsoftw.progetto.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class EmptyPanelAdmin extends JPanel{

    private JButton openAdminPanel;
    private JPanel MainPanel;

    public EmptyPanelAdmin(){

        Dimension preferredDimension = new Dimension(400, 200);

        MainPanel.setPreferredSize(preferredDimension);

        openAdminPanel.addActionListener(e -> new AdminPanel());


    }

    public JPanel getPanel(){

        return this.MainPanel;

    }

    public JButton getButton(){

        return openAdminPanel;

    }

}
