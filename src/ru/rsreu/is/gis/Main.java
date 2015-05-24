package ru.rsreu.is.gis;

import ru.rsreu.is.gis.io.bmp.BitMapFileHeader;
import ru.rsreu.is.gis.io.bmp.BitMapInfoHeader;
import ru.rsreu.is.gis.util.ArrayUtils;
import ru.rsreu.is.gis.util.FilterUtils;
import ru.rsreu.is.gis.util.KursachUtils;
import ru.rsreu.is.gis.util.SerializationUtils;
import ru.rsreu.is.gis.view.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

    /**
     * Применяет всякие маски к 8-ми битной матрице изображения (передавать в args) и сохраняет его в bmp
     *
     * @param args
     */
    public static void mainOld(String... args) {
        try {
            Path output = Paths.get("./dst.bmp");
            int imageWidth = 640;
            int imageHeight = 480;

            BitMapFileHeader fileHeader = new BitMapFileHeader();
            fileHeader.bfSize = imageWidth * imageHeight + fileHeader.bfOffbits + 256 * 4;
            fileHeader.bfOffbits = 54 + 256 * 4;
            byte[] fileHeaderBytes = SerializationUtils.serialize(fileHeader, ByteOrder.LITTLE_ENDIAN);
            Files.write(output, fileHeaderBytes);

            BitMapInfoHeader infoHeader = new BitMapInfoHeader();
            infoHeader.biHeight = imageHeight;
            infoHeader.biWidth = imageWidth;
            infoHeader.biBitCount = 8;
            byte[] infoHeaderBytes = SerializationUtils.serialize(infoHeader, ByteOrder.LITTLE_ENDIAN);
            Files.write(output, infoHeaderBytes, StandardOpenOption.APPEND);

            byte[] arr = FilterUtils.generateColors();
            Files.write(output, arr, StandardOpenOption.APPEND);

            byte[] bytes = Files.readAllBytes(Paths.get(args[0]));
            //bytes = FilterUtils.applyMask(bytes, FilterUtils.contur2, imageWidth);
            byte[][] encoded = KursachUtils.encode(bytes, 4);
            bytes = ArrayUtils.concat(encoded);//KursachUtils.decode(encoded);
            Files.write(output, bytes, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}