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

public class VsMain {

    public static void main(String[] args) {

        int instancesCount = 10;

        VsInstance[] instances = new VsInstance[instancesCount];
        for (int i = 0; i < instancesCount; i++) {
            instances[i] = new VsInstance(String.valueOf(i));
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

            for (int i = 0; i < instancesCount; i++) {
                instances[i].GUI.setMainWindow(gui);
                mainPanel.addComponent(instances[i].GUI.getPanel());
            }

            window.setComponent(mainPanel);


            gui.addWindowAndWait(window);
            terminal.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}
