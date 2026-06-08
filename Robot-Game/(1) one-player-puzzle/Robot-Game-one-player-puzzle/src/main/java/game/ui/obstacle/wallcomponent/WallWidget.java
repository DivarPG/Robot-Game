package game.ui.obstacle.wallcomponent;

import game.model.field.core.Orientation;
import game.ui.obstacle.ObstacleWidget;
import game.ui.resource.image.ImageResource;
import game.ui.resource.ResourceProvider;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет стены.
 * @see game.model.field.between_cells_objects.WallSegment
 */
public class WallWidget extends ObstacleWidget {

    /**
     * Поставщик изображений
     */
    private final  ResourceProvider<BufferedImage,ImageResource> imageProvider;

    /**
     * Конструктор.
     *
     * @param orientation ориентация стены
     * @param imageProvider поставщик изображений
     */
    public WallWidget( Orientation orientation, ResourceProvider<BufferedImage,ImageResource> imageProvider) {

        super(orientation);
        this.imageProvider = imageProvider;
    }

    @Override
    protected void draw(Graphics g) {
        BufferedImage img = getImage();

        g.drawImage(img, 0, 0, null);
    }

    private BufferedImage getImage() {

        //BufferedImage resized =ImageUtils.resizeImage(original, 120, 120);
        return imageProvider.get(getImageType());
    }

    private ImageResource getImageType() {
        return (orientation == Orientation.VERTICAL) ? ImageResource.WALL_VERTICAL : ImageResource.WALL_HORIZONTAL;
    }

}
