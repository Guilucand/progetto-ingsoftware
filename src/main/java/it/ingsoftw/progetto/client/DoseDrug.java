package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.Drug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class DoseDrug extends JFrame{
    private JPanel MainPanel;
    private JList drugsList;
    private JComboBox hourComboBox;
    private JComboBox minuteComboBox;
    private JTextField quantityTextField;
    private JButton aggiungiButton;
    private JTextArea noteArea;
    private JTextField drugName;
    private Drug selectedDrug;
    private Drug[] drugListDB;
    private DefaultListModel listafarmaci;

    public DoseDrug() {

        super("Somministrazione-Farmaci");

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ComboBoxModel<String> cbm = new DefaultComboBoxModel<>();

        for(int i = 0; i<24;i++){

            if(i<10)((DefaultComboBoxModel<String>) cbm).addElement("0"+i);
            else ((DefaultComboBoxModel<String>) cbm).addElement(""+i);

        }
        this.hourComboBox.setModel(cbm);

        cbm = new DefaultComboBoxModel<>();

        for(int i = 0; i<60;i++){

            if(i<10)((DefaultComboBoxModel<String>) cbm).addElement("0"+i);
            else ((DefaultComboBoxModel<String>) cbm).addElement(""+i);

        }

        this.minuteComboBox.setModel(cbm);

        this.listafarmaci = new DefaultListModel<>();
        this.drugsList.setModel(listafarmaci);


        Date data = new Date();

        String datastring = data.toString();

        String [] split = datastring.split(" ");

        String [] split1 = split[3].split(":");

        hourComboBox.setSelectedItem(split1[0]);
        minuteComboBox.setSelectedItem(split1[1]);

        System.out.println(data.toString());





        Dimension preferredDimension = new Dimension(900, 750);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



                System.out.println("aggiungi somministrazione");

            }
        });
        drugsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                JList theList = (JList) e.getSource();

                if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){

                    int index = theList.locationToIndex(e.getPoint());

                    selectedDrug = drugListDB[index];

                    drugName.setText(selectedDrug.commercialName);

                }
            }
        });
    }
}
