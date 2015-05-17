package ru.rsreu.is.gis.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    private JMenuBar menuBar;
    private JImage imageComponent;

    public MainWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initComponents();

        pack();
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
            JFileChooser openImageFileChooser = new JFileChooser();
            if (openImageFileChooser.showDialog(this, "Open image") == JFileChooser.APPROVE_OPTION) {
                File selectedFile = openImageFileChooser.getSelectedFile();
                if (selectedFile.exists()) {
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(selectedFile);
                        imageComponent.setImage(image);
                        pack();
                    } catch (IOException e) {
                        e.printStackTrace();
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
