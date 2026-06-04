package game.ui.obstacle.wallcomponent;

import game.model.field.core.Direction;
import game.model.field.core.Orientation;
import game.ui.cell.CellWidget;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.obstacle.ObstacleWidget;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.ImageResource;

import java.awt.image.BufferedImage;

public class WallView {

    private final WallPieceWidget left;
    private final ObstacleWidget middle;
    private final WallPieceWidget right;

    public WallView(
            CellWidget cellA,
            CellWidget cellB,
            BetweenCellsWidget between,
            Orientation orientation,
            ResourceProvider<BufferedImage, ImageResource> provider
    ) {

        this.middle = new WallWidget(orientation, provider);

        this.left = new WallPieceWidget(cellA, provider, Direction.EAST);
        this.right = new WallPieceWidget(cellB, provider, Direction.WEST);

        install(cellA, cellB, between);
    }

    private void install(CellWidget a,
                         CellWidget b,
                         BetweenCellsWidget between) {

        a.addItem(left);
        between.setObstacle(middle);
        b.addItem(right);
    }
}