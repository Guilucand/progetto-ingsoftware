package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditPatient extends JFrame{


    private JPanel MainPanel;
    private JLabel frequenceParameter;
    private JLabel Immagine;
    private JLabel nome;
    private JLabel dbpParameter;
    private JLabel cognome;
    private JLabel sbpParameter;
    private JLabel tempParameter;
    private JLabel sbp;
    private JLabel dbp;
    private JLabel frequenza;
    private JLabel temperatura;
    private JButton fineButton;
    private JTextField nomePaziente;
    private JTextField pathImg;
    private JTextField cognomePaziente;





    public EditPatient( PatientMonitor EditPat) {

        super("Edit-Patient");


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.setVisible(true);



        fineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i = EndEdit(EditPat);

                if(i == 0){JOptionPane.showMessageDialog(null,"Nessun cambiamento fatto");}

                else{
                    JOptionPane.showMessageDialog(null,"Le modifiche sono state applicate");
                }

                Close();

            }
        });

    }


    private int EndEdit(PatientMonitor EditPat){

        int i = 0;

        if(nomePaziente.getText() != null){EditPat.setName(nomePaziente.getText());i++;}
        if(cognomePaziente.getText() != null){EditPat.setSurname(cognomePaziente.getText());i++;}
        if(pathImg.getText() != null){EditPat.setPath(pathImg.getText());i++;}

        return i;

    }

    private void Close(){
        this.dispose();
    }


}
