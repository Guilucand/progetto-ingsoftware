package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IRoom;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeavePatient extends JFrame{
    private JLabel patientletterdimisisontitle;
    private JTextArea textArea1;
    private JButton button1;
    private JPanel MainPanel;


    public LeavePatient(IRoom stanza, String user){

        super("Lettera-Dimissioni");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),user,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        Dimension preferredDimension = new Dimension(500, 600);
        MainPanel.setPreferredSize(preferredDimension);

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new java.awt.event.WindowAdapter(){

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                if ( JOptionPane.showConfirmDialog(null, "Sei sicuro di voler chiudere il programma Nonostante non isa stata ancora inviata la lettera di dimissione?") == 0){

                    dispose();

                }
            }

        });

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String leavepatientletter = textArea1.getText();

                System.out.println(leavepatientletter);

                if(JOptionPane.showConfirmDialog(null,"Confermi la dimissione del paziente?") == 0){

                    //dimettipaziente

                    dispose();

                }

            }
        });
    }

}
