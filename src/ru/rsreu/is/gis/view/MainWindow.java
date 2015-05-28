package ru.rsreu.is.gis.view;

import ru.rsreu.is.gis.io.bmp.BmpFile;
import ru.rsreu.is.gis.util.ArrayUtils;
import ru.rsreu.is.gis.util.KursachUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainWindow extends JFrame {

    private JMenuBar menuBar;
    private JImage imageComponent;

    private File currentFile;

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initComponents();

        pack();
        отцентроватьОкно();
    }

    private void отцентроватьОкно() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.getWidth() - getWidth()) / 2;
        int y = (int) (screenSize.getHeight() - getHeight()) / 2;
        setLocation(x, y);
    }

    private void initComponents() {
        imageComponent = new JImage();
        add(imageComponent);

        initMenuBar();
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem("Open");
        fileMenu.add(openMenuItem);
        openMenuItem.addActionListener(event -> {
            JFileChooser openImageFileChooser = new JFileChooser(".");
            openImageFileChooser.setAcceptAllFileFilterUsed(false);
            openImageFileChooser.setFileFilter(new FileNameExtensionFilter("BMP Files", "bmp"));
            if (openImageFileChooser.showDialog(this, "Open image") == JFileChooser.APPROVE_OPTION) {
                File selectedFile = openImageFileChooser.getSelectedFile();
                if (selectedFile.exists()) {
                    BufferedImage image = null;
                    try {
                        currentFile = selectedFile;
                        image = ImageIO.read(selectedFile);
                        imageComponent.setImage(image);
                        pack();
                        отцентроватьОкно();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        fileMenu.addSeparator();
        JMenuItem saveAsWavelet = new JMenuItem("Compress");
        fileMenu.add(saveAsWavelet);
        saveAsWavelet.addActionListener(event -> {
            try {
                BmpFile bmp = new BmpFile(currentFile);
                JFileChooser saveFileChooser = new JFileChooser(".");
                saveFileChooser.setAcceptAllFileFilterUsed(false);
                saveFileChooser.setFileFilter(new FileNameExtensionFilter("Wavelet encoded bmp", "wvl"));
                if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = saveFileChooser.getSelectedFile();
                    byte[][] encode = KursachUtils.encode(bmp.getPixels(), 4);
                    bmp.getFileHeader().avgLength = (short) encode[0].length;
                    bmp.setPixels(ArrayUtils.concat(encode));
                    Files.write(selectedFile.toPath(), bmp.bytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        JMenuItem decodeMenuItem = new JMenuItem("Decode");
        fileMenu.add(decodeMenuItem);
        decodeMenuItem.addActionListener(event -> {
            JFileChooser saveFileChooser = new JFileChooser(".");
            saveFileChooser.setAcceptAllFileFilterUsed(false);
            saveFileChooser.setFileFilter(new FileNameExtensionFilter("Wavelet encoded bmp", "wvl"));

            if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = saveFileChooser.getSelectedFile();
                BmpFile bmp = new BmpFile(selectedFile);
                bmp.setPixels(KursachUtils.decode(bmp.getPixels(), bmp.getFileHeader().avgLength));
                Path tempFile = null;
                try {
                    tempFile = Files.createTempFile(Paths.get("."), "temp", "bmp");
                    Files.write(tempFile, bmp.bytes());
                    imageComponent.setImage(ImageIO.read(tempFile.toFile()));
                    pack();
                    отцентроватьОкно();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (tempFile != null) {
                        try {
                            Files.deleteIfExists(tempFile);
                        } catch (IOException e) {
                        }
                    }

                }
            }
        });
        fileMenu.addSeparator();

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(event -> System.exit(0));

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem aboutMenuItem = new JMenuItem("About");
        helpMenu.add(aboutMenuItem);
    }
}
