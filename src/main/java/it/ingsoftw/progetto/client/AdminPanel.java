package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends JFrame{

    private JPanel MainPanel;
    private JList medList;
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
    private JPanel ListPanel1;
    private JList nurseList;
    private JScrollPane ListPanel2;
    private JLabel errorAddLabel;
    private JLabel errorAddLabel1;
    private JLabel errorAddLabel2;
    private JLabel errorAddLabel3;
    private JButton removeNurseButton;
    private JButton removeMedicButton;
    private ILogin.LoginStatus status;
    private DefaultListModel listaMedici;
    private DefaultListModel listaInfermieri;
    private List<User> allUsers;
    private List<User> medListDB;
    private List<User> nurseListDB;
    private IAdmin adminInterface;
    private JPopupMenu popupMenu;
    private int removeline;

    public AdminPanel(ILogin.LoginStatus status, IAdmin adminInterface) throws RemoteException {

        super("Admin-Panel");

        this.status = status;
        this.adminInterface=adminInterface;

        this.listaMedici = new DefaultListModel();
        this.medList.setModel(listaMedici);

        this.listaInfermieri = new DefaultListModel<>();
        this.nurseList.setModel(listaInfermieri);


        this.popupMenu = new JPopupMenu();
        AddPopup();


        //HO BISOGNO DELLA LISTA DEGLI UTENTI NEL DB

        InitList();

        Dimension prefdim = new Dimension(200,700);
        RightPanel.setPreferredSize(prefdim);

        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)medList.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        LeftPanel.setPreferredSize(prefdim);

        renderer =  (DefaultListCellRenderer)nurseList.getCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension preferredDimension = new Dimension(1000, 700);
        this.MainPanel.setPreferredSize(preferredDimension);

        this.editButton.setVisible(false);

        //AGGIUNTE E RIMOZIONI IN BASE AI PRIVILEGI AMMINISTRATIVI

        System.out.println(status);

        if(status!=ILogin.LoginStatus.PRIMARY_LOGGED){

            this.EditPanel.remove(adminCheckBox);

        }



        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);


        medList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JList theList = (JList) e.getSource();
                super.mouseClicked(e);

                ClearPanel(false);

                if(e.getClickCount() == 2){

                    int index = theList.locationToIndex(e.getPoint());

                    if (index >= 0) {


                        System.out.println("Double-clicked on: " + medListDB.get(index).getName());

                        aggiungiButton.setVisible(false);
                        editButton.setVisible(true);

                        idTextField.setText(medListDB.get(index).getId());
                        nameTextField.setText(medListDB.get(index).getName());
                        surnameTextField.setText(medListDB.get(index).getSurname());
                        mailTextField.setText(medListDB.get(index).getEmail());

                        if(medListDB.get(index).getUserType() == User.UserType.Admin)adminCheckBox.setSelected(true);
                        else adminCheckBox.setSelected(false);

                        if(medListDB.get(index).getUserType() == User.UserType.Medic)typeBox.setSelectedIndex(0);
                        else if (medListDB.get(index).getUserType() == User.UserType.Nurse)typeBox.setSelectedIndex(1);

                        CenterPanel.repaint();
                        CenterPanel.revalidate();

                        EditPanel.repaint();
                        EditPanel.revalidate();

                    }

                }else if(e.getButton() == MouseEvent.BUTTON3){

                    System.out.println("premuto il tasto destro ");


                    medList.setSelectedIndex(medList.locationToIndex(e.getPoint()));

                    removeline = medList.getSelectedIndex();

                    if(SwingUtilities.isRightMouseButton(e) && medList.locationToIndex(e.getPoint()) == removeline){

                        if(! medList.isSelectionEmpty()){

                            popupMenu.show(medList,e.getX(),e.getY());

                        }

                    }
                    //medList.setSelectedIndex(medList.locationToIndex(e.getPoint()));

                }



            }
        });



        nurseList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList theList = (JList) e.getSource();
                super.mouseClicked(e);
                ClearPanel(false);
                if(e.getClickCount() == 2){

                    int index = theList.locationToIndex(e.getPoint());

                    if (index >= 0) {

                        System.out.println("Double-clicked on: " + nurseListDB.get(index).getName());

                        aggiungiButton.setVisible(false);
                        editButton.setVisible(true);

                        idTextField.setText(nurseListDB.get(index).getId());
                        nameTextField.setText(nurseListDB.get(index).getName());
                        surnameTextField.setText(nurseListDB.get(index).getSurname());
                        mailTextField.setText(nurseListDB.get(index).getEmail());

                        CenterPanel.remove(adminCheckBox);

                        if(nurseListDB.get(index).getUserType() == User.UserType.Medic)typeBox.setSelectedIndex(0);
                        else if (nurseListDB.get(index).getUserType() == User.UserType.Nurse)typeBox.setSelectedIndex(1);

                        CenterPanel.repaint();
                        CenterPanel.revalidate();

                        EditPanel.repaint();
                        EditPanel.revalidate();

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
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        AddUser();
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    AddUser();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("salvo cambiamento");
                String idUtente = idTextField.getText();
                EditUser(idUtente);
            }
        });


        EditPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ClearPanel(true);
                nameTextField.requestFocusInWindow();


            }
        });
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();

            }
        });
        editButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_UP) idTextField.requestFocusInWindow();
            }
        });

    }


    private void AddPopup(){

        JMenuItem delete = new JMenuItem("Rimuovi");

        popupMenu.add(delete);

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                listaMedici.removeElementAt(removeline);
            }
        });


    }


    private void medListMouseClicked(MouseEvent e){



    }

    private void InitList() throws RemoteException {

        allUsers = adminInterface.getUsers();

        medListDB = new ArrayList<>();
        nurseListDB = new ArrayList<>();

        for(int i = 0; i<allUsers.size(); i++){

            User utente = allUsers.get(i);
            if(utente.getUserType() == User.UserType.Medic || utente.getUserType() == User.UserType.Admin){if(!medListDB.contains(utente)){medListDB.add(utente);}}
            else if(utente.getUserType() == User.UserType.Nurse){if(!nurseListDB.contains(utente)){nurseListDB.add(utente);}}

        }

        for (int i = 0; i < medListDB.size(); i++) {
            listaMedici.add(i, ""+medListDB.get(i).getId());
        }

        for (int i = 0; i<nurseListDB.size(); i++){
            listaInfermieri.add(i,""+nurseListDB.get(i).getId());
        }

    }


    /**
     *
     * Metodo per aggiungere utenti (personale) al database
     *
     */
    private void AddUser() throws RemoteException {

        //CONTROLLI

        boolean b = true;

        if (idTextField.getText().equals("") || idTextField.getText().length()>=64) {
            b = false;
            errorAddLabel2.setText("Manca l'ID utente o la lunghezza è superiore a 64");
            errorAddLabel2.setForeground(Color.RED);

            idTextField.setBackground(new Color(254,105,88));
            idTextField.requestFocusInWindow();
        }else {errorAddLabel2.setText("");idTextField.setBackground(Color.WHITE);}

        if (nameTextField.getText().equals("") || nameTextField.getText().length()>=64) {
            b = false;
            errorAddLabel.setText("Manca il nome utente o la lunghezza è superiore a 64");
            errorAddLabel.setForeground(Color.RED);

            nameTextField.setBackground(new Color(254,105,88));
            nameTextField.requestFocusInWindow();
        }else{errorAddLabel2.setText("");nameTextField.setBackground(Color.WHITE);}

        if (mailTextField.getText().equals("") || mailTextField.getText().length()>=64) {
            b = false;
            errorAddLabel3.setText("Manca la mail utente o la lunghezza è superiore a 64");
            errorAddLabel3.setForeground(Color.RED);

            mailTextField.setBackground(new Color(254,105,88));
            mailTextField.requestFocusInWindow();
        }else{errorAddLabel2.setText("");mailTextField.setBackground(Color.WHITE);}

        if (surnameTextField.getText().equals("") || surnameTextField.getText().length()>=64) {
            b = false;
            errorAddLabel1.setText("Manca il cognome utente o la lunghezza è superiore a 64");
            errorAddLabel1.setForeground(Color.RED);

            surnameTextField.setBackground(new Color(254,105,88));
            surnameTextField.requestFocusInWindow();
        }else{errorAddLabel2.setText("");surnameTextField.setBackground(Color.WHITE);}


        if(b) {

            User.UserType ut = User.UserType.Nurse;

            if(adminCheckBox.isSelected()){ut = User.UserType.Admin;}
            else if(typeBox.getSelectedIndex() == 0){ut = User.UserType.Medic;}
            else if(typeBox.getSelectedIndex() == 1){ut = User.UserType.Nurse;}


            User newUser = new User(idTextField.getText(),nameTextField.getText(),surnameTextField.getText(),mailTextField.getText(),ut);
            if(adminInterface.addUser(newUser) == false){

                JOptionPane.showMessageDialog(null,"Non è stato possiile aggiungere l'utente ");
                return;
            }

            JOptionPane.showMessageDialog(null,"utente \""+idTextField.getText()+"\" aggiunto");
            ClearPanel(true);
            InitList();

        }

    }

    private void EditUser(String idUtente){



    }

    private void ClearPanel(boolean completo){


        this.errorAddLabel.setText("");

        this.nameTextField.setBackground(Color.WHITE);
        this.mailTextField.setBackground(Color.WHITE);
        this.idTextField.setBackground(Color.WHITE);
        this.surnameTextField.setBackground(Color.WHITE);
        this.errorAddLabel.setBackground(Color.WHITE);
        this.errorAddLabel1.setBackground(Color.WHITE);
        this.errorAddLabel2.setBackground(Color.WHITE);
        this.errorAddLabel3.setBackground(Color.WHITE);
        this.errorAddLabel.setText("");
        this.errorAddLabel1.setText("");
        this.errorAddLabel2.setText("");
        this.errorAddLabel3.setText("");

        if(completo){

            this.nameTextField.setText("");
            this.mailTextField.setText("");
            this.idTextField.setText("");
            this.surnameTextField.setText("");

            this.editButton.setVisible(false);
            this.aggiungiButton.setVisible(true);

            this.adminCheckBox.setSelected(false);
            this.typeBox.setSelectedIndex(0);
        }

    }


}
