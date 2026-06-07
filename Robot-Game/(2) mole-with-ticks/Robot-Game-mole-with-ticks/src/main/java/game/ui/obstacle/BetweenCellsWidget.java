package game.ui.obstacle;

import game.model.field.core.BetweenCellsArea;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Orientation;
import javax.swing.*;
import java.awt.*;


/**
 * Виджет контейнера для виджетов между ячейками {@link BetweenCellsWidget}.
 */
public class BetweenCellsWidget extends JPanel {

    /**
     * Ориентация.
     */
    private final Orientation orientation;

    /**
     * Размер элемента.
     */
    private static final Dimension SIZE_HORIZONTAL = new Dimension(5, 5);

    private static final Dimension SIZE_VERTICAL = new Dimension(5, 5);

    /**
     * Конструктор.
     *
     * @param betweenCellsArea область между клетками.
     */
    public BetweenCellsWidget(@NotNull BetweenCellsArea betweenCellsArea) {
        super(new BorderLayout());

        this.orientation = betweenCellsArea.getOrientation();
        // if (betweenCellsArea.getObstacle() != null) { setItem(new WallWidget(orientation,provider)); }
        setPreferredSize(getDimensionByOrientation());
        setBackground(Color.DARK_GRAY);

        setOpaque(true);

    }

    public void setObstacle(@NotNull ObstacleWidget obstacleWidget) {
        setItem(obstacleWidget);

        revalidate();
        repaint();
    }

    /**
     * Установить элемент.
     *
     * @param obstacleWidget элемент.
     * @throws IllegalArgumentException если ориентация объекта не совпадает с ориентацией контейнера.
     */
    private void setItem(@NotNull ObstacleWidget obstacleWidget) {
        if (obstacleWidget.getOrientation() != orientation) {
            throw new IllegalArgumentException("Obstacle orientation mismatch");
        }

        add(obstacleWidget, BorderLayout.CENTER);
    }

    /**
     * Получить размеры виджета по ориентации {@link BetweenCellsWidget#orientation}
     *
     * @return размеры.
     */
    private Dimension getDimensionByOrientation() {
        return (orientation == Orientation.VERTICAL) ? SIZE_VERTICAL : SIZE_HORIZONTAL;
    }
}
