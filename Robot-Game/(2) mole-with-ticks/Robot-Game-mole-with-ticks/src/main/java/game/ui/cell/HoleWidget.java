package game.ui.cell;

import game.ui.resource.ResourceProvider;
import game.ui.resource.gif.GifResource;
import game.ui.resource.image.ImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HoleWidget extends CellItemWidget {

    private static final Dimension SIZE = new Dimension(120, 120);

    private ImageIcon gif;
    private final BufferedImage hole;

    private boolean showGif = true;

    public HoleWidget(
            ResourceProvider<ImageIcon, GifResource> gifProvider,
            ResourceProvider<BufferedImage, ImageResource> imageProvider) {

        this.gif = gifProvider.get(GifResource.DIGGING);
        this.hole = imageProvider.get(ImageResource.HOLE);

        Timer timer = new Timer(2300, e -> {
            showGif = false;
            repaint();
            ((Timer) e.getSource()).stop();
        });

        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void draw(Graphics g) {

        int w = 120;
        int h = 120;

        int x = (getWidth() - w) / 2;
        int y = (getHeight() - h) / 2;

        if (showGif) {
            g.drawImage(gif.getImage(), x, y, w, h, this);
        } else {
            g.drawImage(hole, x, y, w, h, null);
        }
    }

    @Override
    protected Dimension getDimension() {
        return SIZE;
    }

    @Override
    public CellLayout.Zone getZone() {
        return CellLayout.Zone.PRIMARY;
    }

    @Override
    public int getZIndex() {
        return 120;
    }
}