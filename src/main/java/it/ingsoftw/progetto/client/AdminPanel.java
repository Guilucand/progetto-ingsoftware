package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends JFrame{

    private JPanel MainPanel;
    private JList nurseList;
    private JList medList;
    private JLabel listnamenurse;
    private JLabel listnamemed;
    private JTextField nameTextField;
    private JTextField idTextField;
    private JTextField surnameTextField;
    private JTextField mailTextField;
    private JLabel mailLabel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JComboBox typeBox;
    private JCheckBox adminCheckBox;
    private JButton aggiungiButton;
    private JButton chiudiButton;
    private JPanel EditPanel;
    private JPanel CenterPanel;
    private JPanel RightPanel;
    private JPanel LeftPanel;
    private JButton editButton;
    private JLabel typeLabel;
    private ILogin.LoginStatus status;
    private DefaultListModel listaMedici;
    private List<User> medListDB;



    public AdminPanel(ILogin.LoginStatus status){

        super("Admin-Panel");

        this.status = status;

        this.listaMedici = new DefaultListModel();
        this.medList.setModel(listaMedici);


        //HO BISOGNO DELLA LISTA DEGLI UTENTI NEL DB

        this.medListDB = new ArrayList<>();

        medListDB.add(new User("Med01","pippo","baudo","pippobaudo@gmail.com",User.UserType.Medic));
        medListDB.add(new User("Med02","pippo2","baudo2","pippobaudo2@gmail.com",User.UserType.Admin));


        for (int i = 0; i < medListDB.size(); i++) {
            listaMedici.add(i, ""+medListDB.get(i).getId());

        }



        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension preferredDimension = new Dimension(1000, 700);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.EditPanel.remove(editButton);

        //AGGIUNTE E RIMOZIONI IN BASE AI PRIVILEGI AMMINISTRATIVI

        System.out.println(status);

        if(status!=ILogin.LoginStatus.PRIMARY_LOGGED){

            this.EditPanel.remove(adminCheckBox);

        }



        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        medList.addComponentListener(new ComponentAdapter() {
        });
        medList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JList theList = (JList) e.getSource();
                super.mouseClicked(e);
                if(e.getClickCount() == 2){

                    int index = theList.locationToIndex(e.getPoint());

                    if (index >= 0) {


                        System.out.println("Double-clicked on: " + medListDB.get(index).getName());

                        EditPanel.remove(aggiungiButton);
                        EditPanel.add(editButton);

                        idTextField.setText(medListDB.get(index).getId());
                        nameTextField.setText(medListDB.get(index).getName());
                        surnameTextField.setText(medListDB.get(index).getSurname());
                        mailTextField.setText(medListDB.get(index).getEmail());

                        if(medListDB.get(index).getUserType() == User.UserType.Admin)adminCheckBox.setSelected(true);
                        else adminCheckBox.setSelected(false);

                        if(medListDB.get(index).getUserType() == User.UserType.Medic)typeBox.setSelectedIndex(0);
                        else if (medListDB.get(index).getUserType() == User.UserType.Nurse)typeBox.setSelectedIndex(1);


                    }

                }
            }
        });
        nameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_RIGHT) surnameTextField.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) idTextField.requestFocusInWindow();
            }
        });
        surnameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_LEFT) nameTextField.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) mailTextField.requestFocusInWindow();
            }
        });

        idTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_UP) nameTextField.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) mailTextField.requestFocusInWindow();
            }
        });

        mailTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_LEFT) idTextField.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) aggiungiButton.requestFocusInWindow();
                else if (e.getKeyCode() == KeyEvent.VK_UP) surnameTextField.requestFocusInWindow();
            }
        });


        aggiungiButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) AddUser();
            }
        });

        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AddUser();

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                System.out.println("salvo cambaiemnto");
            }
        });
    }


    /**
     *
     * Metodo per aggiungere utenti (personale) al database
     *
     */
    private void AddUser() {


        //CONTROLLI

        boolean b = true;

        if (idTextField.getText().equals("") || idTextField.getText().length()>25) {
            b = false;
            JOptionPane.showMessageDialog(null,"Manca ID utente e la lunghezza deve essere inferiore a 15");
            idTextField.setBackground(new Color(254,105,88));
            idTextField.requestFocusInWindow();
        }

        if(b) {


            listaMedici.add(listaMedici.size(),""+idTextField.getText());
            JOptionPane.showMessageDialog(null,"utente \""+idTextField.getText()+"\" aggiunto");

            idTextField.setBackground(new Color(255,255,255));
            idTextField.setText("");


        }


    }


}
