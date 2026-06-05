package game.ui.cell;

import game.model.field.core.ExitPoint;
import game.ui.resource.ResourceProvider;
import game.ui.resource.gif.GifResource;
import game.ui.resource.image.ImageResource;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет ячейки выхода.
 *
 * @see ExitPoint
 */

public class ExitWidget extends CellItemWidget {

    // лучше хранить через провайдера или через ресурс ?
    private final ImageIcon gif;

    public ExitWidget(
            ResourceProvider<ImageIcon, GifResource> provider) {

        this.gif = provider.get(GifResource.PORTAL);

        new Timer(40, e -> repaint()).start();
    }

    @Override
    protected void draw(Graphics g) {

        int width = 100;
        int height = 100;

        int x = (getWidth() - width) / 2;
        int y = (getHeight() - height) / 2;

        g.drawImage(
                gif.getImage(),
                x,
                y,
                width,
                height,
                this
        );
    }

    @Override
    public CellLayout.Zone getZone() {
        return CellLayout.Zone.PRIMARY;
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(150, 150);
    }

    @Override
    public int getZIndex() {
        return 50;
    }
}
