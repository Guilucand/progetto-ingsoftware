package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.*;

public class MonitorGUI extends JFrame{
    private JLabel nome;
    private JLabel cognome;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JLabel battiti;
    private JLabel pressione;
    private JLabel altro;
    private JButton aggiungi2;

    private JPanel [] stanze = new JPanel [10];

    private JPanel Stanza1;
    private JPanel Stanza2;
    private JPanel stanza3;
    private JPanel stanza4;
    private JPanel stanza5;
    private JPanel stanza6;
    private JPanel stanza7;
    private JPanel stanza8;
    private JPanel stanza9;
    private JPanel stanza10;
    private JLabel nomestanza1;
    private JLabel cognomestanza1;
    private JPanel MainPanel;
    private JButton aggiungi3;


    public MonitorGUI(){

        super("Monitor");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.setVisible(true);


    }


    public void AddPatient(){

        stanza3.remove(aggiungi3);
        //stanza3.add(new JLabel("immagine paz 3" ));


       // stanza3.add(new JLabel ("rino "));
       // stanza3.add(new JLabel ("gattuso "));

        System.out.println("aggiungo");

    }


}
