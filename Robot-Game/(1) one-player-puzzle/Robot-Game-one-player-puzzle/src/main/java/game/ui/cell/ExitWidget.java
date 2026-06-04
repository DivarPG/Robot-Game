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

    private final ResourceProvider<ImageIcon, GifResource> provider;

    public ExitWidget(ResourceProvider<ImageIcon, GifResource> provider) {

        super(null);

        this.provider = provider;

        setLayout(new BorderLayout());
        add(createContent(), BorderLayout.CENTER);
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

    @Override
    protected JComponent createContent() {

        ImageIcon gif = provider.get(GifResource.PORTAL);

        JComponent component = new JComponent() {

            {
                setOpaque(false);

                new Timer(40, e -> repaint()).start();
            }

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                g.drawImage(
                        gif.getImage(),
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };

        return component;
    }

    @Override
    public ImageResource getImageType() {
        return null;
    }

    @Override
    public BufferedImage getImage(ResourceProvider<BufferedImage, ImageResource> provider) {
        return null;
    }
}