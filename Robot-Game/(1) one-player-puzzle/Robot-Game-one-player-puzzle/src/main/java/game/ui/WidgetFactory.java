package game.ui;

import game.model.field.core.*;
import game.model.field.core.Robot;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.obstacle.wallcomponent.WallBuilder;
import game.ui.resource.*;
import game.ui.resource.image.ClasspathImageResourceProvider;
import game.ui.resource.image.ImageResource;
import org.jetbrains.annotations.NotNull;
import game.ui.cell.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<CellObject, CellItemWidget> cellObjects = new HashMap<>();
    private final Map<BetweenCellsArea, BetweenCellsWidget> betweenCellsAreas = new HashMap<>();

    private final ResourceProvider<BufferedImage, ImageResource> imageResourceProvider =
            new CachedResourceProvider<>(new ClasspathImageResourceProvider());

    private final WallBuilder wallBuilder = new WallBuilder(imageResourceProvider);

    /*---------- AbstractCell ----------*/
    public CellWidget create(@NotNull Cell cell) {
        if (cells.containsKey(cell)) return cells.get(cell);

        CellWidget item = new CellWidget();

        Robot robot = (Robot) cell.getObject(Robot.class);
        if (robot != null) {
            CellItemWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        Battery battery = (Battery) cell.getObject(Battery.class);

        if (battery != null) {
            CellItemWidget batteryWidget = create(battery);
            item.addItem(batteryWidget);
        }

        ExitPoint exitPoint = (ExitPoint) cell.getObject(ExitPoint.class);
        if (exitPoint != null) {
            CellItemWidget exitWidget = create(exitPoint);
            item.addItem(exitWidget);
        }

        cells.put(cell, item);
        return item;
    }

    public CellWidget getWidget(@NotNull Cell cell) {
        return cells.get(cell);
    }

    public void remove(@NotNull Cell cell) {
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
        } else if (cellObject instanceof ExitPoint) {
            createdWidget = new ExitWidget(imageResourceProvider);
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

//        if (betweenCellsArea.getObstacle() != null) {
//            buildWall(betweenCellsArea, createdWidget);
//        }

        if (betweenCellsArea.getObstacle() != null) {
            wallBuilder.build(
                    betweenCellsArea,
                    createdWidget,
                    this
            );
        }

        betweenCellsAreas.put(betweenCellsArea, createdWidget);

        return createdWidget;
    }

    public BetweenCellsWidget getWidget(@NotNull BetweenCellsArea betweenCellsArea) {
        return betweenCellsAreas.get(betweenCellsArea);
    }

    public void remove(@NotNull BetweenCellsArea betweenCellsArea) {
        betweenCellsAreas.remove(betweenCellsArea);
    }
}
