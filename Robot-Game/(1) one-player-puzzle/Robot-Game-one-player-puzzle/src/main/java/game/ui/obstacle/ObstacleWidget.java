package game.ui.obstacle;

import game.model.field.core.Orientation;
import game.ui.resource.image.ImageResource;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.WithImageResource;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Виджет препятствия, расположенного между ячейками.
 */
public abstract class ObstacleWidget extends JPanel implements WithImageResource {

    /**
     * Ориентация.
     */
    protected final Orientation orientation;


    /**
     * Размер элемента.
     */
    private static final Dimension SIZE_HORIZONTAL = new Dimension(10, 5);

    private static final Dimension SIZE_VERTICAL = new Dimension(5, 10);



    private final  ResourceProvider<BufferedImage,ImageResource> provider;

    /**
     * Получить ориентацию {@link ObstacleWidget#orientation}.
     *
     * @return ориентация.
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Конструктор.
     *
     * @param orientation ориентация.
     */
    public ObstacleWidget(Orientation orientation,  ResourceProvider<BufferedImage, ImageResource> provider) {
        this.orientation = orientation;
        this.provider = provider;
        setPreferredSize(getDimensionByOrientation());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getImage(provider), 0, 0, null);
    }



    /**
     * Получить размеры виджеты по ориентации.
     *
     * @return размеры виджета.
     */
    protected Dimension getDimensionByOrientation() {
        return (orientation == Orientation.VERTICAL) ? SIZE_VERTICAL : SIZE_HORIZONTAL;
    }
}
