package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

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

        this.MainPanel.setOpaque(true);
        this.MainPanel.setBackground(new Color(255,255,255));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container c = this.getContentPane();

        c.setLayout(new GridLayout(3,1));

        TopPanel = new JPanel(new GridLayout(1,4));
        MidPanel = new JPanel(new GridLayout(1,4));
        BottomPanel = new JPanel(new GridLayout(1,4));

        this.TopPanel.setOpaque(true);
        this.MidPanel.setOpaque(true);
        this.BottomPanel.setOpaque(true);

        this.TopPanel.setBackground(new Color(255,255,255));
        this.MidPanel.setBackground(new Color(255,255,255));
        this.BottomPanel.setBackground(new Color(255,255,255));

        c.add("0",TopPanel);
        c.add("1",MidPanel);
        c.add("2",BottomPanel);

        for(int i = 0; i<4; i++){

            TopPanel.add(new PatientMonitor(i+1),i);
            MidPanel.add(new PatientMonitor(i+5),i);
            if(i == 0){

                EmptyPanelAdmin epa = new EmptyPanelAdmin();

               // if(status == ILogin.LoginStatus.PRIMARY_LOGGED){
                BottomPanel.add(epa.getPanel(),i);//}
            }
            else if(i == 3){

                EmptyPanelAdmin epa = new EmptyPanelAdmin();

                JPanel panel = epa.getPanel();
                panel.remove(epa.getButton());

                BottomPanel.add(epa.getPanel(),i);

            }
            else BottomPanel.add(new PatientMonitor(i+8),i);

        }


        /*if(status == ILogin.LoginStatus.MEDIC_LOGGED){

            JPanel AdminPanel = new JPanel(new GridLayout());

            AdminPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            Dimension preferredDimension = new Dimension(400, 200);

            AdminPanel.setPreferredSize(preferredDimension);
            AdminPanel.add(new JButton("Apri pannello amministrativo"),0);

            BottomPanel.add(AdminPanel,0);

        }*/

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);


    }


}
