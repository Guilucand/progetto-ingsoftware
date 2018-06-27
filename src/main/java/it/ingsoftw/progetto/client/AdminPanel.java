package it.ingsoftw.progetto.client;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JFrame{

    private JPanel MainPanel;
    private JList nurseList;
    private JList medList;
    private JLabel listnamenurse;
    private JLabel listnamemed;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JLabel mailLabel;
    private JLabel idLabel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JComboBox comboBox1;
    private JCheckBox adminCheckBox;
    private JButton aggiungiButton;
    private JButton chiudiButton;

    public AdminPanel(){

        super("Admin-Panel");

        DefaultListModel<String> model = new DefaultListModel<>();

        model.addElement("prova1");
        model.addElement("prova2");
        JList<String> list = new JList<>( model );


//        this.medList.addListSelectionListener();
        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension preferredDimension = new Dimension(1000, 700);
        this.MainPanel.setPreferredSize(preferredDimension);


        this.pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setVisible(true);







    }

}
