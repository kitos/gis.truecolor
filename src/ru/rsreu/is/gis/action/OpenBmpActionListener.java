package ru.rsreu.is.gis.action;

import ru.rsreu.is.gis.io.bmp.BmpFile;
import ru.rsreu.is.gis.view.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OpenBmpActionListener implements ActionListener {
    private MainWindow mainWindow;

    public OpenBmpActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JFileChooser openImageFileChooser = new JFileChooser(".");
        openImageFileChooser.setAcceptAllFileFilterUsed(false);
        openImageFileChooser.setFileFilter(new FileNameExtensionFilter("BMP images", BmpFile.EXTENSION));
        if (openImageFileChooser.showDialog(mainWindow, "Open image") == JFileChooser.APPROVE_OPTION) {
            File selectedFile = openImageFileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                try {
                    BufferedImage image = ImageIO.read(selectedFile);
                    mainWindow.imageLoaded(image, selectedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
