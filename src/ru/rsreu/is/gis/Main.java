package ru.rsreu.is.gis;

import ru.rsreu.is.gis.view.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException
                | InstantiationException
                | UnsupportedLookAndFeelException
                | IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, "Unable to set system Look&Feel.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }
}