package game.ui.cell;

import game.ui.resource.ResourceProvider;
import game.ui.resource.gif.GifResource;

import javax.swing.*;
import java.awt.*;

public class HoleWidget extends CellItemWidget {

    private static final Dimension SIZE = new Dimension(120, 120);

    private final ImageIcon gif;

    public HoleWidget(ResourceProvider<ImageIcon, GifResource> provider) {

        this.gif = provider.get(GifResource.DIGGING);

        new Timer(40, e -> repaint()).start();
    }

    @Override
    protected void draw(Graphics g) {

        int width = 120;
        int height = 120;

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