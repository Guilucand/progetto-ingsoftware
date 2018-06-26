package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
    private JTextField cognomePaziente;


    public EditPatient( PatientMonitor EditPat) {

        super("Edit-Patient");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();
        this.setVisible(true);


        fineButton.addActionListener(e -> EndEdit(EditPat));

        nomePaziente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_DOWN) cognomePaziente.requestFocusInWindow();
            }
        });
        cognomePaziente.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) EndEdit(EditPat);
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) fineButton.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_UP) nomePaziente.requestFocusInWindow();

            }
        });
        fineButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) EndEdit(EditPat);
                else if(e.getKeyCode() == KeyEvent.VK_UP) cognomePaziente.requestFocusInWindow();
            }
        });
    }


    private void EndEdit(PatientMonitor EditPat){

        int i = 0;

        if(!nomePaziente.getText().equals("")){EditPat.setName(nomePaziente.getText());i++;}
        if(!cognomePaziente.getText().equals("")){EditPat.setSurname(cognomePaziente.getText());i++;}

        if(i == 0){JOptionPane.showMessageDialog(null,"Nessun cambiamento fatto");}

        else{

            JOptionPane.showMessageDialog(null,"Le modifiche sono state applicate");

        }

        this.dispose();

    }



}
