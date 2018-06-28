package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.IMonitor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class MonitorGUI extends JFrame{

    private JPanel MainPanel;
    private JPanel TopPanel;
    private JPanel MidPanel;
    private JPanel BottomPanel;
    private ILogin.LoginStatus status;
    private String username;
    private IMonitor iMonitorInterface;

    public MonitorGUI(ILogin.LoginStatus status, String username, IMonitor iMonitorInterface) throws RemoteException {

        super("Monitor");

        this.status = status;
        this.username = username;
        this.iMonitorInterface=iMonitorInterface;

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.MainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),""+username,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.MainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        this.setContentPane(MainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new java.awt.event.WindowAdapter(){

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                if ( JOptionPane.showConfirmDialog(null, "Sei sicuro di voler chiudere il programma?", "Really Closing?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == 0){

                    Chiudi();

                }
            }

        });



        this.MainPanel.setOpaque(true);
        this.MainPanel.setBackground(Color.WHITE);

        Container c = this.getContentPane();

        c.setLayout(new GridLayout(3,1));

        TopPanel = new JPanel(new GridLayout(1,4));
        MidPanel = new JPanel(new GridLayout(1,4));
        BottomPanel = new JPanel(new GridLayout(1,4));


        c.add("0",TopPanel);
        c.add("1",MidPanel);
        c.add("2",BottomPanel);

        for(int i = 0; i<4; i++){

            TopPanel.add(new PatientMonitor(i+1, this.iMonitorInterface ),i);
            MidPanel.add(new PatientMonitor(i+5, this.iMonitorInterface),i);
            if(i == 0){

                EmptyPanelAdmin epa = new EmptyPanelAdmin(status);

                if(this.status == ILogin.LoginStatus.PRIMARY_LOGGED || this.status == ILogin.LoginStatus.ADMIN_LOGGED) {
                    BottomPanel.add(epa.getPanel(), i);

                }else{

                    JPanel panel = epa.getPanel();
                    panel.remove(epa.getButton());

                    BottomPanel.add(epa.getPanel(), i);


                }


            }
            else if(i == 3){

                EmptyPanelAdmin epa = new EmptyPanelAdmin(this,status);

                BottomPanel.add(epa.getPanel(), i);

            }
            else BottomPanel.add(new PatientMonitor(i+8,this.iMonitorInterface),i);

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

    private void Chiudi() {

        this.dispose();

    }


}
