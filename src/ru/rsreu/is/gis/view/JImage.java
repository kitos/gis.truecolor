package ru.rsreu.is.gis.view;

import java.awt.*;
import java.util.Objects;

class JImage extends Component {

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
            g.drawImage(image, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return image != null
                ? new Dimension(image.getWidth(null), image.getHeight(null))
                : new Dimension(100, 100);
    }

    public void setImage(Image image) {
        Objects.requireNonNull(image);
        this.image = image;
    }
}
