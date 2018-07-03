package it.ingsoftw.progetto.client;

import it.ingsoftw.progetto.common.messages.IMessage;
import it.ingsoftw.progetto.common.messages.MessageObject;
import it.ingsoftw.progetto.common.messages.MonitorDataChangedMessage;

import java.rmi.RemoteException;

public class MessagesDispatcher {
    private IMessage messageInterface;
    private MonitorGUI monitorGUI;
    private Thread dispatchThread;

    public MessagesDispatcher(IMessage messageInterface, MonitorGUI monitorGUI) {
        this.messageInterface = messageInterface;
        this.monitorGUI = monitorGUI;
        dispatchThread = new Thread(this::dispatchMessages);
        dispatchThread.start();
    }

    private void dispatchMessages() {
        while (true) {
            try {
                MessageObject message =  messageInterface.getNextMessage();

                String room = message.getRoom();
                if (room == null)
                    continue;

                PatientMonitor patientMonitor = monitorGUI.getPatientMonitorByRoom(room);
                patientMonitor.processMessage(message);


                System.out.println(message.getMessageText());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
