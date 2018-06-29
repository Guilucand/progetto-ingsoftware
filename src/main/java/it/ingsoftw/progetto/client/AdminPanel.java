package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.EditableUser;
import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.User;
import org.w3c.dom.stylesheets.MediaList;

import javax.swing.*;
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
    private User ModUser;
    private User DeleteUser;

    public AdminPanel(ILogin.LoginStatus status, IAdmin adminInterface) throws RemoteException {

        super("Admin-Panel");

        this.status = status;
        this.adminInterface=adminInterface;

        this.listaMedici = new DefaultListModel();
        this.medList.setModel(listaMedici);

        this.listaInfermieri = new DefaultListModel<>();
        this.nurseList.setModel(listaInfermieri);


        this.popupMenu = new JPopupMenu();
        addPopup();


        //HO BISOGNO DELLA LISTA DEGLI UTENTI NEL DB

        initList();

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

                clearPanel(false);

                if(e.getClickCount() == 2){

                    int index = theList.locationToIndex(e.getPoint());

                    if (index >= 0) {

                        ModUser = medListDB.get(index);

                        System.out.println("Double-clicked on: " + medListDB.get(index).getName());

                        aggiungiButton.setVisible(false);
                        editButton.setVisible(true);

                        idTextField.setText(ModUser.getId());
                        nameTextField.setText(ModUser.getName());
                        surnameTextField.setText(ModUser.getSurname());
                        mailTextField.setText(ModUser.getEmail());

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

                    medList.setSelectedIndex(medList.locationToIndex(e.getPoint()));

                    int index = theList.locationToIndex(e.getPoint());
                    DeleteUser = medListDB.get(index);

                    removeline = medList.getSelectedIndex();

                    if(SwingUtilities.isRightMouseButton(e) && medList.locationToIndex(e.getPoint()) == removeline){

                        if(! medList.isSelectionEmpty()){

                            popupMenu.show(medList,e.getX(),e.getY());

                        }

                    }

                }



            }
        });


        nurseList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList theList = (JList) e.getSource();
                super.mouseClicked(e);
                clearPanel(false);
                int index;

                if(e.getClickCount() == 2){

                    System.out.println("premo tasto sinistrox2 mouse in lista infermieri");
                    index = theList.locationToIndex(e.getPoint());

                    if (index >= 0) {

                        ModUser = nurseListDB.get(index);

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

                }else if(e.getButton() == MouseEvent.BUTTON3){

                    System.out.println("premo tasto destro mouse in lista infermieri");
                    nurseList.setSelectedIndex(nurseList.locationToIndex(e.getPoint()));
                    index = theList.locationToIndex(e.getPoint());

                    DeleteUser = nurseListDB.get(index);

                    removeline = nurseList.getSelectedIndex();

                    if(SwingUtilities.isRightMouseButton(e) && nurseList.locationToIndex(e.getPoint()) == removeline){

                        if(! nurseList.isSelectionEmpty()){

                            popupMenu.show(nurseList,e.getX(),e.getY());

                        }

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
                        addUser();
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
                    addUser();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    editUser();
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });


        EditPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                clearPanel(true);
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

        typeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(typeBox.getSelectedIndex() == 1) adminCheckBox.setVisible(false);
                else if (typeBox.getSelectedIndex() == 0) adminCheckBox.setVisible(true);

            }
        });
    }


    private void addPopup(){

        JMenuItem delete = new JMenuItem("Rimuovi");
        JMenuItem modifica = new JMenuItem("modifica");

        popupMenu.add(delete);
        //popupMenu.add(modifica);

        delete.addActionListener(e -> {

            try {
                removeUser();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            try {
                initList();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });
/*
        modifica.addActionListener(e -> {

            try {
                editUser();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            try {
                initList();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });
*/

    }


    private void initList() throws RemoteException {

        allUsers = adminInterface.getUsers();

        this.listaMedici.removeAllElements();
        this.listaInfermieri.removeAllElements();

        medListDB = new ArrayList<>();
        nurseListDB = new ArrayList<>();

       // this.nurseList.

        for(int i = 0; i<allUsers.size(); i++){

            User utente = allUsers.get(i);
            if(utente.getUserType() == User.UserType.Medic || utente.getUserType() == User.UserType.Admin){if(!medListDB.contains(utente)){medListDB.add(utente);}}
            else if(utente.getUserType() == User.UserType.Nurse){if(!nurseListDB.contains(utente)){nurseListDB.add(utente);}}

        }

        for (int i = 0; i < medListDB.size(); i++) {
            listaMedici.add(i, ""+medListDB.get(i));
        }

        for (int i = 0; i<nurseListDB.size(); i++){
            listaInfermieri.add(i,""+nurseListDB.get(i));
        }

    }


    /**
     *
     * Metodo per aggiungere utenti (personale) al database
     *
     */
    private void addUser() throws RemoteException {

        if(checkParameters()) {

            User.UserType ut = User.UserType.Nurse;

            if(adminCheckBox.isSelected()){ut = User.UserType.Admin;}
            else if(typeBox.getSelectedIndex() == 0){ut = User.UserType.Medic;}
            else if(typeBox.getSelectedIndex() == 1){ut = User.UserType.Nurse;}


            User newUser = new User(idTextField.getText(),nameTextField.getText(),surnameTextField.getText(),mailTextField.getText(),ut);
            if(adminInterface.addUser(newUser) == false){

                JOptionPane.showMessageDialog(null,"Non è stato possiile aggiungere l'utente poichè è già nel Database");
                return;
            }

            JOptionPane.showMessageDialog(null,"utente \""+idTextField.getText()+"\" aggiunto");
            clearPanel(true);
            initList();

        }

    }


    private boolean checkParameters(){

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

        return b;


    }

    private void removeUser() throws RemoteException {

        EditableUser delUser = adminInterface.getEditableUser(DeleteUser.getId());

        if(!adminInterface.deleteUser(delUser)){

            JOptionPane.showMessageDialog(null,"Non è stato possiile rimuovere l'utente");


        }else{

            JOptionPane.showMessageDialog(null,"L'utente è stato rimosso correttamente");
        }



    }


    private void editUser() throws RemoteException {


        if(checkParameters()) {

            EditableUser EditUser = adminInterface.getEditableUser(ModUser.getId());

            EditUser.setId(idTextField.getText());
            EditUser.setName(nameTextField.getText());
            EditUser.setSurname(surnameTextField.getText());
            EditUser.setEmail(mailTextField.getText());

            User.UserType ut = User.UserType.Nurse;

            if(adminCheckBox.isSelected()){ut = User.UserType.Admin;}
            else if(typeBox.getSelectedIndex() == 0){ut = User.UserType.Medic;}
            else if(typeBox.getSelectedIndex() == 1){ut = User.UserType.Nurse;}

            EditUser.setUserType(ut);

            if(!adminInterface.commitUserChanges(EditUser)){

                JOptionPane.showMessageDialog(null,"Non è stato possiile modificare l'utente");
                return;

            }else JOptionPane.showMessageDialog(null,"Le modifiche sono state Applicate");

            initList();
            clearPanel(true);

        }

    }

    private void clearPanel(boolean completo){


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
