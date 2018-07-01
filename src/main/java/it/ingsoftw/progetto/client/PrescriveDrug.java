package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.Drug;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;


public class PrescriveDrug extends JFrame{
    private JPanel MainPanel;
    private JList drugList;
    private JTextField therapyDurationTextField;
    private JTextField dailyDosesTextField;
    private JPasswordField quantityTextField;
    private JButton findDrugButton;
    private JTextField drugNameTextField;
    private JButton SaveDoseButton;
    private JTextArea noteArea;
    private JCheckBox nomeCheckBox;
    private JCheckBox tipoConfezioneCheckBox;
    private JCheckBox aziendaCheckBox;
    private JCheckBox principioAttivoCheckBox;
    private JTextField drugName;
    private JScrollPane scrollpaneTable;
    private JTable table1;
    private DrugsQuery drugsQuery;
    private DefaultListModel listafarmaci;
    private Drug[] drugListDB;
    private Drug selectedDrug;
    private JTable tabella;

    public PrescriveDrug(){

        super("Prescrizione dei farmaci");

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.drugsQuery = new DrugsQuery();

        this.listafarmaci = new DefaultListModel<>();
        this.drugList.setModel(listafarmaci);

        nomeCheckBox.setSelected(true);
        tipoConfezioneCheckBox.setSelected(true);

        drugList.setCellRenderer(new DrugListRendererextends());

        initTable();

        Dimension preferredDimension = new Dimension(900, 750);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        findDrugButton.addActionListener(e -> {

            drugListDB = drugsQuery.queryDatabase(drugNameTextField.getText(),DrugsQuery.QueryType.Drug,true);

            initTable();

            listafarmaci.clear();
            for(Drug drug : drugListDB){

                listafarmaci.addElement(drug);

            }
        });

        SaveDoseButton.addActionListener(e -> {

            Date dataPrecrizione= new Date();
            System.out.println(dataPrecrizione);
            String durataPrescrizione = therapyDurationTextField.getText();
            String doseGiornalieraPrescrizione = dailyDosesTextField.getText();
            String quantitàFarmaco = quantityTextField.getSelectedText();

            System.out.println("Salvataggio prescrizione");

        });
        nomeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                drugList.repaint();
                drugList.revalidate();

            }
        });

        tipoConfezioneCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                drugList.repaint();
                drugList.revalidate();

            }
        });

        aziendaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                drugList.repaint();
                drugList.revalidate();

            }
        });

        principioAttivoCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                drugList.repaint();
                drugList.revalidate();

            }
        });
        drugList.addMouseListener(new MouseAdapter() {
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


    private void initTable(){

        String [] nomicolonne = new String[]{"Nome commerciale","Tipo confezione","Principio attivo","Azienda"};

        /*String [][] data = new String[drugListDB.length][4];


        for(int i = 0; i<drugListDB.length; i++){

            data[i][0] = drugListDB[0].commercialName;
            data[i][1] = drugListDB[0].packageDescription;
            data[i][2] = drugListDB[0].activePrinciple;
            data[i][3] = drugListDB[0].company;

        }*/

        String [][] data = new String[][]{

                {"ciao","ciao","ciao","ciao"},
                {"ciao","ciao","ciao","ciao"}

        };


        tabella = new JTable(data,nomicolonne);
        tabella.setPreferredScrollableViewportSize(new Dimension(500,50));
        tabella.setFillsViewportHeight(true);

        this.scrollpaneTable.removeAll();
        this.scrollpaneTable.add(tabella);


    }


    public class DrugListRendererextends extends DefaultListCellRenderer {


        public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Drug farmaco = (Drug)value;

            String stringout = "";

            if(nomeCheckBox.isSelected()){stringout = farmaco.commercialName;}
            if(tipoConfezioneCheckBox.isSelected()){stringout = stringout+" "+farmaco.packageDescription;}
            if(principioAttivoCheckBox.isSelected()){stringout = stringout+" "+farmaco.activePrinciple;}
            if(aziendaCheckBox.isSelected()){stringout = stringout+" "+farmaco.company;}

            setText(stringout);

            return this;
        }

    }

}
