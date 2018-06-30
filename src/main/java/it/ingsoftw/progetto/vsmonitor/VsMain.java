package it.ingsoftw.progetto.vsmonitor;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * Classe principale dell'interfaccia di simulazione delle
 * macchine di monitoraggio
 * Inizializza e visualizza un numero definito di macchine per il monitoraggio
 */
public class VsMain {

    /**
     * Numero di istanze da far partire
     */
    static final int INSTANCES_COUNT = 10;


    public static void main(String[] args) {


        VsInstance[] instances = new VsInstance[INSTANCES_COUNT];
        for (int i = 1; i <= INSTANCES_COUNT; i++) {
            instances[i-1] = new VsInstance(String.valueOf(i));
        }

        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            Terminal terminal = defaultTerminalFactory.createTerminal();

            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            // Create window to hold the panel
            BasicWindow window = new BasicWindow();

            GridLayout gridLayout = new GridLayout(3);
            gridLayout.setHorizontalSpacing(2);
            gridLayout.setVerticalSpacing(1);

            Panel mainPanel = new Panel(gridLayout);

            // Create gui and start gui
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.DEFAULT));

            for (int i = 0; i < INSTANCES_COUNT; i++) {
                instances[i].GUI.setMainWindow(gui);
                mainPanel.addComponent(instances[i].GUI.getPanel());
            }

            window.setComponent(mainPanel);


            gui.addWindowAndWait(window);
//            terminal.close();
            System.exit(0);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}
