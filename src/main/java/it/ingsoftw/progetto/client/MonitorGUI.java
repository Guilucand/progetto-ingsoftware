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

    private JPanel Stanza1;
    private JPanel Stanza2;

    private JLabel nomestanza1;
    private JLabel cognomestanza1;
    private JPanel MainPanel;
    private JButton modificaButtonStanza1;
    private JPanel Stanza5;
    private JPanel Stanza3;
    private JPanel Stanza4;
    private JButton addBotton3;
    private JButton addBotton2;
    private JButton addBotton4;
    private JButton aggiungi2;
    private JPanel Stanza7;
    private JPanel Stanza8;
    private JPanel Stanza9;
    private JPanel Stanza10;
    private JButton addBotton7;
    private JButton addBotton8;
    private JButton addBotton9;
    private JButton addBotton10;
    private JPanel Stanza6;
    private JButton addBotton6;


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

        //Stanza2.remove(aggiungi2);

        /*JPanel NewStanza = Stanza2;


        NewStanza.add(new JLabel("immagine paz 3" , SwingConstants.CENTER));
        NewStanza.add(new JLabel ("rino ", SwingConstants.RIGHT));
        NewStanza.add(new JLabel ("gattuso ", SwingConstants.RIGHT));

        NewStanza.revalidate();
        NewStanza.repaint();

        this.add(NewStanza);

        this.pack();
*/
        System.out.println("aggiungo");

    }

    public static boolean EditPatient(JPanel room){


        /*switch (room){

            case Stanza1:





        }*/








        return true;

    }


}
