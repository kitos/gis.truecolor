package ru.rsreu.is.gis.view;

import java.awt.*;
import java.util.Objects;

public class JImage extends Component {

    private Image image;

    public JImage() {
    }

    public JImage(Image image) {
        setImage(image);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            // центруем изображение
            int x = (getWidth() - image.getWidth(null)) / 2;
            int y = (getHeight() - image.getHeight(null)) / 2;
            g.drawImage(image, x, y, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return image != null
                ? new Dimension(image.getWidth(null), image.getHeight(null))
                : new Dimension(400, 300);
    }

    public void setImage(Image image) {
        Objects.requireNonNull(image);
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
