package game.ui.cell;

import game.model.field.core.ExitPoint;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.ImageResource;
import game.ui.utils.ImageScaler;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет ячейки выхода.
 *
 * @see ExitPoint
 */
public class ExitWidget extends CellItemWidget {

    /**
     * Конструктор.
     *
     * @param imageProvider
     */
    public ExitWidget(ResourceProvider<BufferedImage, ImageResource> imageProvider) {
        super(imageProvider);
    }

    @Override
    public ImageResource getImageType() {
        return ImageResource.EXIT;
    }

    @Override
    public BufferedImage getImage( ResourceProvider<BufferedImage,ImageResource> provider) {
       BufferedImage original = provider.get(getImageType());
       return ImageScaler.resize(original, 120, 120);
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
    public int getZIndex() {return 50;}




}
