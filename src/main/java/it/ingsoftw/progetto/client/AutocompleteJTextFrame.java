package it.ingsoftw.progetto.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.DirectColorModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import javafx.scene.layout.Border;


public class AutocompleteJTextFrame extends JComboBox {

    private final Searchable searchable;
    private JTextField textField;


    public interface Searchable {
        List<String> query(String s);
        void choose(String s);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 0);
    }

    private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    public AutocompleteJTextFrame(JTextField textField, Searchable searcher) {
        super();

        this.textField = textField;
        this.textField.setLayout(new BorderLayout());
        this.textField.add(this, BorderLayout.SOUTH);


        this.searchable = searcher;
        setEditable(true);

        textField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void update() {
                //perform separately, as listener conflicts between the editing component
                //and JComboBox will result in an IllegalStateException due to editing
                //the component when it is locked.

                setAdjusting(AutocompleteJTextFrame.this, true);
                AutocompleteJTextFrame.this.removeAllItems();
                String input = textField.getText();

                boolean addedItem = false;
                for (String result : searcher.query(input)) {
                    addItem(result);
                    addedItem = true;
                }

                AutocompleteJTextFrame.this.setPopupVisible(addedItem);
                setAdjusting(AutocompleteJTextFrame.this, false);

//                    SwingUtilities.invokeLater(() -> {
//                    });
            }
        });

        //When the text component changes, focus is gained
        //and the menu disappears. To account for this, whenever the focus
        //is gained by the JTextComponent and it has searchable values, we show the popup.
        textField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent arg0) {
                if (textField.getText().length() > 0) {
                    setPopupVisible(true);
                }
            }

            @Override
            public void focusLost(FocusEvent arg0) {
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setAdjusting(AutocompleteJTextFrame.this, true);

                int keyCode = e.getKeyCode();

                switch (keyCode) {
                    case KeyEvent.VK_ENTER:
                        String selectedItem = AutocompleteJTextFrame.this.getSelectedItem().toString();
                        textField.setText(selectedItem);
                        AutocompleteJTextFrame.this.setPopupVisible(false);
                        searcher.choose(selectedItem);

                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                        e.setSource(AutocompleteJTextFrame.this);
                        AutocompleteJTextFrame.this.dispatchEvent(e);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        AutocompleteJTextFrame.this.setPopupVisible(false);
                        break;
                }
                setAdjusting(AutocompleteJTextFrame.this, false);
            }
        });
    }
}

