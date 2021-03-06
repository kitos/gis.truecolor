package ru.rsreu.is.gis.view;

import ru.rsreu.is.gis.action.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {

    public static final String WINDOW_TITLE = "Wavelet encoder 1.0";
    private JMenuBar menuBar;
    private JImage imageComponent;

    private File currentFile;

    public MainWindow() {
        super(WINDOW_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initComponents();

        pack();
        отцентроватьОкно();
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public JImage getImageComponent() {
        return imageComponent;
    }

    public void imageLoaded(Image image, File imageFile) {
        currentFile = imageFile;
        if (imageFile != null) {
            setTitle(WINDOW_TITLE + " - " + imageFile.getAbsolutePath());
        }
        imageComponent.setImage(image);
        pack();
        отцентроватьОкно();
        repaint();
    }

    private void отцентроватьОкно() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.getWidth() - getWidth()) / 2;
        int y = (int) (screenSize.getHeight() - getHeight()) / 2;
        setLocation(x, y);
    }

    private void initComponents() {
        imageComponent = new JImage();
        JScrollPane scrollPane = new JScrollPane(imageComponent);
        add(scrollPane);

        initMenuBar();
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem("Open");
        fileMenu.add(openMenuItem);
        openMenuItem.addActionListener(new OpenBmpActionListener(this));

        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        saveMenuItem.addActionListener(new SaveBmpActionListener(this));
        fileMenu.addSeparator();

        JMenuItem saveAsWavelet = new JMenuItem("Compress");
        fileMenu.add(saveAsWavelet);
        saveAsWavelet.addActionListener(new EncodeActionListener(this));

        JMenuItem decodeMenuItem = new JMenuItem("Decode");
        fileMenu.add(decodeMenuItem);
        decodeMenuItem.addActionListener(new DecodeActionListener(this));

        JMenuItem compareMenuItem = new JMenuItem("Compare");
        fileMenu.add(compareMenuItem);
        compareMenuItem.addActionListener(new CompareActionListener(this));
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
