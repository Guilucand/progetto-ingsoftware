package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DoseDrug extends JFrame{
    private JPanel MainPanel;
    private JList drugsList;
    private JComboBox hourComboBox;
    private JComboBox minuteComboBox;
    private JTextField quantityTextField;
    private JButton aggiungiButton;
    private JTextArea noteArea;
    private JTextField drugName;
    private DrugPrescription selectedDrug;
    private Drug[] drugListDB;
    private DefaultListModel listafarmaci;
    private IRecovery patient;
    private List<DrugPrescription> drugPrescriptionList;

    public DoseDrug(IRecovery patient) {

        super("Somministrazione-Farmaci");
        this.patient = patient;

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

        drugPrescriptionList = new ArrayList<>();
        try {
            drugPrescriptionList = patient.getCurrentPrescriptions();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        if (drugPrescriptionList != null) {
            for (int i = 0; i < drugPrescriptionList.size(); i++) {
                this.listafarmaci.add(i, drugPrescriptionList.get(i));
            }
        }

        this.drugsList.setModel(listafarmaci);
        this.drugsList.setCellRenderer(new DrugPrescriveListRendererextends());


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

                LocalDateTime adesso = LocalDateTime.now();

                int anno = adesso.getYear();
                java.time.Month mese = adesso.getMonth();
                int giorno = adesso.getDayOfMonth();
                int ora = adesso.getHour();

                if (ora < hourComboBox.getSelectedIndex()) {

                    if(giorno == 1) {

                        if(mese == Month.JANUARY){

                            anno = anno-1;

                        } else {

                            mese = mese.minus(1);

                        }

                    } else {
                        giorno = giorno - 1;
                    }
                }

                try {
                    patient.addDrugAdministration(new DrugAdministration(
                            selectedDrug.key,
                            LocalDateTime.of(
                                    anno,
                                    mese,
                                    giorno,
                                    hourComboBox.getSelectedIndex(),
                                    minuteComboBox.getSelectedIndex()),
                            quantityTextField.getText(),
                            noteArea.getText()));
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
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

                    selectedDrug = drugPrescriptionList.get(index);

                    drugName.setText(selectedDrug.drug.commercialName);

                }
            }
        });
    }


    public class DrugPrescriveListRendererextends extends DefaultListCellRenderer {


        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            DrugPrescription farmaco = (DrugPrescription)value;

            setText(farmaco.drug.commercialName+" "+farmaco.drug.packageDescription);

            return this;
        }

    }
}
