package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class Storico {
    private JPanel main_panel;
    private JScrollBar scrollbar_lat;
    private JLabel sbp;
    private JLabel dbp;
    private JLabel frequenza;
    private JLabel temperatura;
    private JLabel val1;
    private JLabel val2;
    private JLabel val3;
    private JLabel immagine1;

    public Storico() {
        scrollbar_lat.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                main_panel.revalidate();
                main_panel.repaint();
            }
        });
    }
}
