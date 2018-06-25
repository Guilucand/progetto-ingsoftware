package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonitorGUI extends JFrame{

    private JPanel MainPanel;
    private JPanel TopPanel;
    private JPanel MidPanel;
    private JPanel BottomPanel;

    public MonitorGUI(ILogin.LoginStatus status){

        super("Monitor");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = this.getContentPane();

        c.setLayout(new GridLayout(3,1));

        TopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        MidPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        BottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));


        c.add("0",TopPanel);
        c.add("1",MidPanel);
        c.add("2",BottomPanel);

        for(int i = 0; i<4; i++){

            TopPanel.add(new PatientMonitor(),FlowLayout.LEFT);
            MidPanel.add(new PatientMonitor(),FlowLayout.LEFT);
            if(i<2)BottomPanel.add(new PatientMonitor(),FlowLayout.LEFT);

        }

        this.pack();

        this.setVisible(true);


    }


}
