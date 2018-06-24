package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton modificaButtonStanza1;


    public MonitorGUI(){

        super("Monitor");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.setVisible(true);


        modificaButtonStanza1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                EditPatient(Stanza1);

            }
        });
        modificaButtonStanza1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {



            }
        });
    }


    public void AddPatient(){

        Stanza2.remove(aggiungi3);
        stanza3.add(new JLabel("immagine paz 3" , SwingConstants.CENTER));
        stanza3.add(new JLabel ("rino ", SwingConstants.RIGHT));
        stanza3.add(new JLabel ("gattuso ", SwingConstants.RIGHT));

        stanza3.revalidate();
        stanza3.repaint();

        this.pack();

        System.out.println("aggiungo");

    }

    public static boolean EditPatient(JPanel room){


        /*switch (room){

            case Stanza1:





        }*/








        return true;

    }


}
