package ru.rsreu.is.gis.action;

import ru.rsreu.is.gis.io.bmp.BmpFile;
import ru.rsreu.is.gis.util.ArrayUtils;
import ru.rsreu.is.gis.util.KursachUtils;
import ru.rsreu.is.gis.view.MainWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.file.Files;

public class EncodeActionListener implements ActionListener {

    private MainWindow mainWindow;

    public EncodeActionListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (mainWindow.getCurrentFile() == null) {
            JOptionPane.showMessageDialog(mainWindow, "No bmp file was opened. Nothing to encode.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            JFileChooser saveFileChooser = new JFileChooser(".");
            saveFileChooser.setAcceptAllFileFilterUsed(false);
            saveFileChooser.setFileFilter(new FileNameExtensionFilter("Wavelet encoded bmp", "wvl"));
            if (saveFileChooser.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
                BmpFile bmp = new BmpFile(mainWindow.getCurrentFile());
                byte[][] encode = KursachUtils.encode(bmp.getPixels(), 4);
                bmp.getFileHeader().avgLength = (short) encode[0].length;
                bmp.setPixels(ArrayUtils.concat(encode));
                Files.write(saveFileChooser.getSelectedFile().toPath(), bmp.bytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
