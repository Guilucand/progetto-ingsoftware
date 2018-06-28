package it.ingsoftw.progetto.client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class Storico extends  JFrame{
    private JPanel MainPanel;
    private JPanel StoricPanel;
    private JLabel immagine1;
    private JLabel sbp;
    private JLabel temperatura;
    private JLabel dbp;
    private JLabel frequenza;
    private JLabel val1;
    private JLabel val2;
    private JLabel val3;
    private JScrollPane ScrollPane;
    private JTextArea textArea1;
    private JCheckBox checkBox1;
    private JTextArea textArea2;
    private JScrollPane scrollpanel1;


    public Storico() {

        super("Storico");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),"nome",TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        Dimension preferredDimension = new Dimension(600, 800);
        MainPanel.setPreferredSize(preferredDimension);


        this.ScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.ScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.ScrollPane.setBounds(50, 30, 300, 50);

        this.scrollpanel1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollpanel1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollpanel1.setBounds(50, 30, 300, 50);


        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);

    }
}
