package game.ui;

import game.model.field.core.ExitCell;
import game.model.field.core.NormalCell;
import game.model.field.core.*;
import game.model.field.core.Robot;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.obstacle.wallcomponent.WallPieceWidget;
import game.ui.obstacle.wallcomponent.WallWidget;
import game.ui.resource.*;
import game.ui.resource.image.ClasspathImageResourceProvider;
import game.ui.resource.image.ImageResource;
import org.jetbrains.annotations.NotNull;
import game.ui.cell.*;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {

    private final Map<AbstractCell, CellWidget> cells = new HashMap<>();
    private final Map<CellObject, CellItemWidget> cellObjects = new HashMap<>();
    private final Map<BetweenCellsArea, BetweenCellsWidget> betweenCellsAreas = new HashMap<>();

    private final ResourceProvider<BufferedImage, ImageResource> imageResourceProvider =
            new CachedResourceProvider<>(new ClasspathImageResourceProvider());

    /*---------- AbstractCell ----------*/
    public CellWidget create(@NotNull AbstractCell cell) {
        if (cells.containsKey(cell)) return cells.get(cell);

        CellWidget item = (cell instanceof ExitCell) ? new ExitWidget(imageResourceProvider) : new CellWidget();

        Robot robot = cell.getBigObject();
        if (robot != null) {
            CellItemWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        if (cell instanceof NormalCell) {
            Battery battery = ((NormalCell) cell).getSmallObject();

            if (battery != null) {
                CellItemWidget batteryWidget = create(battery);
                item.addItem(batteryWidget);
            }
        }


        cells.put(cell, item);
        return item;
    }

    public CellWidget getWidget(@NotNull AbstractCell cell) {
        return cells.get(cell);
    }

    public void remove(@NotNull AbstractCell cell) {
        cells.remove(cell);
    }

    /*---------- CellObject ----------*/
    public CellItemWidget create(@NotNull CellObject cellObject) {
        if (cellObjects.containsKey(cellObject)) return cellObjects.get(cellObject);

        CellItemWidget createdWidget = null;
        if (cellObject instanceof Robot) {
            createdWidget = new RobotWidget((Robot) cellObject, imageResourceProvider);
        } else if (cellObject instanceof Battery) {
            createdWidget = new BatteryWidget((Battery) cellObject, imageResourceProvider);
        } else {
            throw new IllegalArgumentException();
        }

        cellObjects.put(cellObject, createdWidget);
        return createdWidget;
    }

    public CellItemWidget getWidget(@NotNull CellObject cellObject) {
        return cellObjects.get(cellObject);
    }

    public void remove(@NotNull CellObject cellObject) {
        cellObjects.remove(cellObject);
    }

    /*---------- BetweenCellArea ----------*/
    public BetweenCellsWidget create(@NotNull BetweenCellsArea betweenCellsArea) {
        if (betweenCellsAreas.containsKey(betweenCellsArea)) return betweenCellsAreas.get(betweenCellsArea);

        BetweenCellsWidget createdWidget = new BetweenCellsWidget(betweenCellsArea, imageResourceProvider);

        if (betweenCellsArea.getObstacle() != null) {
            buildWall(betweenCellsArea, createdWidget);
        }

        betweenCellsAreas.put(betweenCellsArea, createdWidget);

        return createdWidget;
    }


    private void buildWall(BetweenCellsArea area, BetweenCellsWidget widget) {

        for (Direction d : Direction.values()) {

            AbstractCell a = area.getNeighborCell(d);
            AbstractCell b = area.getNeighborCell(d);

            if (a == null || b == null) continue;

            CellWidget wa = getOrCreateCellWidget(a);
            CellWidget wb = getOrCreateCellWidget(b);

            WallPieceWidget waPiece = new WallPieceWidget(wa, imageResourceProvider, d);
            WallPieceWidget wbPiece = new WallPieceWidget(wb, imageResourceProvider, d);

            wa.addItem(waPiece);
            wb.addItem(wbPiece);

            if (widget.getComponentCount() == 0) {
                widget.setObstacle(new WallWidget(toOrientation(d), imageResourceProvider));
            }
        }
    }

    private CellWidget getOrCreateCellWidget(AbstractCell cell) {
        CellWidget widget = cells.get(cell);
        if (widget != null) return widget;

        widget = create(cell);
        cells.put(cell, widget);
        return widget;
    }

    private Orientation toOrientation(Direction d) {
        return (d == Direction.EAST || d == Direction.WEST)
                ? Orientation.VERTICAL
                : Orientation.HORIZONTAL;
    }

    public BetweenCellsWidget getWidget(@NotNull BetweenCellsArea betweenCellsArea) {
        return betweenCellsAreas.get(betweenCellsArea);
    }

    public void remove(@NotNull BetweenCellsArea betweenCellsArea) {
        betweenCellsAreas.remove(betweenCellsArea);
    }
}
