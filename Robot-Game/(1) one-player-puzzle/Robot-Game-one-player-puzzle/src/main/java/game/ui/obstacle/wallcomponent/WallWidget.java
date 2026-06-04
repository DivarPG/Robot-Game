package game.ui.obstacle.wallcomponent;

import game.model.field.core.Orientation;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.obstacle.ObstacleWidget;
import game.ui.resource.image.ImageResource;
import game.ui.resource.ResourceProvider;

import java.awt.image.BufferedImage;

/**
 * Виджет стены.
 * @see game.model.field.between_cells_objects.WallSegment
 */
public class WallWidget extends ObstacleWidget {

    /**
     * Конструктор.
     * @param orientation ориентация.
     */
    public WallWidget( Orientation orientation, ResourceProvider<BufferedImage,ImageResource> provider) {

        super(orientation, provider);
    }

    @Override
    public BufferedImage getImage( ResourceProvider<BufferedImage,ImageResource> provider) {

        BufferedImage original = provider.get(getImageType());

        //BufferedImage resized =ImageUtils.resizeImage(original, 120, 120);

        return original;
    }

    @Override
    public ImageResource getImageType() {
        return (orientation == Orientation.VERTICAL) ? ImageResource.WALL_VERTICAL : ImageResource.WALL_HORIZONTAL;
    }

}
