package game.ui.obstacle;

import game.model.field.core.Orientation;
import javax.swing.*;
import java.awt.*;



/**
 * Виджет препятствия, расположенного между ячейками.
 */
public abstract class ObstacleWidget extends JPanel {

    /**
     * Ориентация.
     */
    protected final Orientation orientation;


    /**
     * Размер элемента.
     */
    private static final Dimension SIZE_HORIZONTAL = new Dimension(10, 5);

    private static final Dimension SIZE_VERTICAL = new Dimension(5, 10);

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
    public ObstacleWidget(Orientation orientation) {
        this.orientation = orientation;

        setPreferredSize(getDimensionByOrientation());
        setOpaque(false);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawImage(getImage(provider), 0, 0, null);
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getComponentCount() == 0) {
            draw(g);
        }
    }

    protected abstract void draw(Graphics g);


    /**
     * Получить размеры виджеты по ориентации.
     *
     * @return размеры виджета.
     */
    protected Dimension getDimensionByOrientation() {
        return (orientation == Orientation.VERTICAL) ? SIZE_VERTICAL : SIZE_HORIZONTAL;
    }
}
