package ru.rsreu.is.gis.action;

import ru.rsreu.is.gis.view.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompareActionListener implements ActionListener {

    private MainWindow mainWindow;

    public CompareActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(mainWindow, "Sorry, but this action hasn't been implemented yet.",
                "Soryan", JOptionPane.INFORMATION_MESSAGE);
    }
}
