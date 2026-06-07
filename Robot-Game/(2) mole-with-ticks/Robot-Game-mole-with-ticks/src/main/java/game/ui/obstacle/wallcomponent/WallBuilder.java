package game.ui.obstacle.wallcomponent;

import game.model.field.core.BetweenCellsArea;
import game.model.field.core.Cell;
import game.model.field.core.Direction;
import game.model.field.core.Orientation;
import game.ui.WidgetFactory;
import game.ui.cell.CellWidget;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.ImageResource;

import java.awt.image.BufferedImage;


public class WallBuilder {

    private final ResourceProvider<BufferedImage, ImageResource> provider;

    public WallBuilder(ResourceProvider<BufferedImage, ImageResource> provider) {
        this.provider = provider;
    }

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