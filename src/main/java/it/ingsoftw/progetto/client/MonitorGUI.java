package it.ingsoftw.progetto.client;


import it.ingsoftw.progetto.common.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JMenuBar menuBar;
    private ILogin.LoginStatus status;


    public MonitorGUI(ILogin loginInterface, String username, ILogin.LoginStatus status,IClientRmiFactory serverFactory) throws RemoteException {

        super("Monitor");

        this.loginInterface = loginInterface;
        this.username = username;
        this.status = status;

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

        //menubar con cambia password

        menuBar = new JMenuBar();

        JMenu menu = new JMenu("menu");
        JMenuItem changepassword = new JMenuItem("cambia password");
        JMenuItem showreport = new JMenuItem("vedi report");
        JMenuItem admin = new JMenuItem("apri pannello amministrativo");
        JMenuItem stampareport = new JMenuItem(("stampa report"));

        menu.add(changepassword);
        menu.add(showreport);

        if(this.status == ILogin.LoginStatus.PRIMARY_LOGGED|| this.status == ILogin.LoginStatus.ADMIN_LOGGED){

            menu.add(admin);
            menu.add(stampareport);

        }


        JMenuItem exit = new JMenuItem("esci");
        menu.add(exit);

        changepassword.addActionListener(e -> new ChangePassword(loginInterface));

        showreport.addActionListener(e -> new ReportFrame(status,username));

        admin.addActionListener(e -> {

            try {
                new AdminPanel(status,adminInterface);
            } catch (RemoteException ee) {
                ee.printStackTrace();
            }

        });

        exit.addActionListener(e -> {

            if(JOptionPane.showConfirmDialog(null,"Conferma Log-out") == 0){

                Chiudi();

            }

        });

        stampareport.addActionListener( e -> {

            //PrintableReport pr = new PrintableReport(true);
            //pr.printToPdf("/Desktop/fileprova.pdf");


        });


        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        this.pack();
        this.setLocation((dim.width/2-this.getSize().width/2), (dim.height/2-this.getSize().height/2));
        this.setVisible(true);
    }

    private void Chiudi() {

        System.exit(0);

    }


}
