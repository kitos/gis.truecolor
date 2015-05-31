package ru.rsreu.is.gis.action;

import ru.rsreu.is.gis.io.bmp.BmpFile;
import ru.rsreu.is.gis.util.KursachUtils;
import ru.rsreu.is.gis.view.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DecodeActionListener implements ActionListener {

    private MainWindow mainWindow;

    public DecodeActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JFileChooser saveFileChooser = new JFileChooser(".");
        saveFileChooser.setAcceptAllFileFilterUsed(false);
        saveFileChooser.setFileFilter(new FileNameExtensionFilter("Wavelet encoded bmp", "wvl"));

        if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = saveFileChooser.getSelectedFile();
            BmpFile bmp = new BmpFile(selectedFile);
            bmp.setPixels(KursachUtils.decode(bmp.getPixels(), bmp.getFileHeader().avgLength));
            Path tempFile = null;
            try {
                tempFile = Files.createTempFile(Paths.get("."), "temp", BmpFile.EXTENSION);
                Files.write(tempFile, bmp.bytes());
                BufferedImage image = ImageIO.read(tempFile.toFile());
                mainWindow.imageLoaded(image, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainWindow, String.format("Something went wrong\n.%s", e.getMessage()),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (tempFile != null) {
                    try {
                        Files.deleteIfExists(tempFile);
                    } catch (IOException e) {
                    }
                }

            }
        }
    }
}
