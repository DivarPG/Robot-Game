package game.ui.obstacle.wallcomponent;

import game.model.field.core.*;
import game.ui.cell.CellWidget;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.ImageResource;
import java.awt.image.BufferedImage;
import game.ui.WidgetFactory;

/**
 * Строитель визуального представления стены
 * Создает виджет препятствия между ячейками и добавляет
 * соответствующие компоненты стены в соседние ячейки
 */
public class WallBuilder {

    /**
     * Поставщик изображений
     */
    private final ResourceProvider<BufferedImage, ImageResource> provider;

    /**
     * Конструктор
     *
     * @param provider поставщик изображений
     */
    public WallBuilder(ResourceProvider<BufferedImage, ImageResource> provider) {
        this.provider = provider;
    }

    /**
     * Построить визуальное представление стены
     * Создает виджет препятствия между ячейками и добавляет
     * компоненты стены в соседние ячейки игрового поля
     *
     * @param area область между ячейками
     * @param between контейнер препятствия между ячейками
     * @param factory фабрика виджетов
     */
    public void build(BetweenCellsArea area, BetweenCellsWidget between, WidgetFactory factory) {

        for (Direction d : Direction.values()) {

            Cell cell = area.getNeighborCell(d);

            if (cell == null) {continue;}

            //гарантируем что он всегда есть
            CellWidget widget = factory.create(cell);

            widget.addItem(new WallPieceWidget(provider, d));

            if (between.getComponentCount() == 0) {
                between.setObstacle(new WallWidget(toOrientation(d), provider));
            }
        }
    }

    private Orientation toOrientation(Direction d) {
        return (d == Direction.EAST || d == Direction.WEST)
                ? Orientation.VERTICAL
                : Orientation.HORIZONTAL;
    }
}