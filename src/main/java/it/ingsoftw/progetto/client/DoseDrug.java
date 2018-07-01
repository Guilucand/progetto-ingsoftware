package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoseDrug {
    private JPanel MainPanel;
    private JList list1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField quantityTextField;
    private JButton aggiungiButton;

    public DoseDrug() {

        ComboBoxModel<Integer> cbm =


        this.comboBox1.setModel();

        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("aggiungi somministrazione");
            }
        });
    }
}
