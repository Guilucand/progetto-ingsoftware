package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IClientListener;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.server.ServerConfig;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class MonitorGUI extends JFrame{

    private JPanel MainPanel;
    private JPanel TopPanel;
    private JPanel MidPanel;
    private JPanel BottomPanel;

    public MonitorGUI(ILogin.LoginStatus status,String username){

        super("Monitor");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),""+username,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));


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

            TopPanel.add(new PatientMonitor(i+1));
            MidPanel.add(new PatientMonitor(i+5));
            if(i<2)BottomPanel.add(new PatientMonitor(i+9));

        }

        if(status == ILogin.LoginStatus.MEDIC_LOGGED){

            JPanel AdminPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

            AdminPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Dimension preferredDimension = new Dimension(400, 200);

            AdminPanel.setPreferredSize(preferredDimension);
            AdminPanel.add(new JButton("Apri pannello amministrativo"),0);

            BottomPanel.add(AdminPanel,FlowLayout.LEFT);

        }

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);


    }


}
