package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.IAdmin;
import it.ingsoftw.progetto.common.IClientRmiFactory;
import it.ingsoftw.progetto.common.ILogin;
import it.ingsoftw.progetto.common.IMonitor;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class MonitorGUI extends JFrame{

    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel midPanel;
    private JPanel bottomPanel;
    private ILogin loginInterface;
    private String username;
    private IClientRmiFactory serverFactory;
    private IMonitor monitorInterface;
    private IAdmin adminInterface;

    public MonitorGUI(ILogin loginInterface, String username, IClientRmiFactory serverFactory) throws RemoteException {

        super("Monitor");

        this.loginInterface = loginInterface;
        this.username = username;

        this.serverFactory = serverFactory;
        this.monitorInterface =serverFactory.getMonitorInterface();
        this.adminInterface = serverFactory.getAdminInterface();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black),""+username,TitledBorder.TOP,TitledBorder.CENTER));
        ((javax.swing.border.TitledBorder) this.mainPanel.getBorder()).setTitleFont(new Font("Droid Serif", Font.ITALIC, 14));

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(new java.awt.event.WindowAdapter(){

            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                if (JOptionPane.showConfirmDialog(null, "Sei sicuro di voler chiudere il programma?") == 0){

                    Chiudi();

                }

            }

        });

        this.mainPanel.setOpaque(true);
        this.mainPanel.setBackground(Color.WHITE);

        Container c = this.getContentPane();

        c.setLayout(new GridLayout(3,1));

        topPanel = new JPanel(new GridLayout(1,4));
        midPanel = new JPanel(new GridLayout(1,4));
        bottomPanel = new JPanel(new GridLayout(1,4));


        c.add("0", topPanel);
        c.add("1", midPanel);
        c.add("2", bottomPanel);

        for(int i = 0; i<4; i++){

            topPanel.add(new PatientMonitor(i+1, monitorInterface.getRoomByNumber(i+1), loginInterface,username),i);
            midPanel.add(new PatientMonitor(i+5, monitorInterface.getRoomByNumber(i+5), loginInterface,username),i);
            if(i == 0){

                EmptyPanelAdmin epa = new EmptyPanelAdmin(loginInterface.isLogged(), adminInterface, username);

                switch (loginInterface.isLogged()) {

                    case PRIMARY_LOGGED:
                    case ADMIN_LOGGED:
                        break;

                    default:
                        JPanel panel = epa.getPanel();
                        panel.remove(epa.getButton());
                }
                bottomPanel.add(epa.getPanel(), i);

            }
            else if(i == 3){

                EmptyPanelAdmin epa = new EmptyPanelAdmin(this,loginInterface.isLogged(), username, adminInterface);
                epa.getPanel().remove(epa.getButtonreport());

                bottomPanel.add(epa.getPanel(), i);

            }
            else bottomPanel.add(new PatientMonitor(i+8, monitorInterface.getRoomByNumber(i+8),loginInterface,username),i);

        }


        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);
    }

    private void Chiudi() {

        System.exit(0);

    }


}
