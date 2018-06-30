package it.ingsoftw.progetto.client;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import java.awt.event.ActionListener;

public class JDatePickerBirthDate implements JDatePicker {


    @Override
    public void setTextEditable(boolean b) {

    }

    @Override
    public boolean isTextEditable() {
        return false;
    }

    @Override
    public void setButtonFocusable(boolean b) {

    }

    @Override
    public boolean getButtonFocusable() {
        return false;
    }

    @Override
    public void setShowYearButtons(boolean b) {

    }

    @Override
    public boolean isShowYearButtons() {
        return false;
    }

    @Override
    public void setDoubleClickAction(boolean b) {

    }

    @Override
    public boolean isDoubleClickAction() {
        return false;
    }

    @Override
    public DateModel<?> getModel() {
        return null;
    }

    @Override
    public void addActionListener(ActionListener actionListener) {

    }

    @Override
    public void removeActionListener(ActionListener actionListener) {

    }
}
