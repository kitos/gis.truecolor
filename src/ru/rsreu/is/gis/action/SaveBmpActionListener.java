package ru.rsreu.is.gis.action;

import ru.rsreu.is.gis.io.bmp.BmpFile;
import ru.rsreu.is.gis.view.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class SaveBmpActionListener implements ActionListener {

    private MainWindow mainWindow;

    public SaveBmpActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (mainWindow.getImageComponent().getImage() == null) {
            JOptionPane.showMessageDialog(mainWindow, "Nothing to save.", "Warning", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            JFileChooser saveFileChooser = new JFileChooser(".");
            saveFileChooser.setAcceptAllFileFilterUsed(false);
            saveFileChooser.setFileFilter(new FileNameExtensionFilter("BMP images", BmpFile.EXTENSION));
            saveFileChooser.setSelectedFile(new File("./unnamed.bmp"));

            if (saveFileChooser.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                Image image = mainWindow.getImageComponent().getImage();
                ImageIO.write((RenderedImage) image, "bmp", saveFileChooser.getSelectedFile());
                mainWindow.imageLoaded(image, saveFileChooser.getSelectedFile());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainWindow, String.format("Something went wrong.\n%s", e.getMessage()),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
