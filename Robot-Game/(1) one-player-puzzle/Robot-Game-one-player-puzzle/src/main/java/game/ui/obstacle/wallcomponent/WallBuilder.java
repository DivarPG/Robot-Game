package game.ui.obstacle.wallcomponent;

import game.model.field.core.*;
import game.ui.cell.CellWidget;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.ImageResource;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class WallBuilder {

    private final ResourceProvider<BufferedImage, ImageResource> provider;

    public WallBuilder(ResourceProvider<BufferedImage, ImageResource> provider) {
        this.provider = provider;
    }

    public void build(@NotNull BetweenCellsArea area, CellWidget a,
                      CellWidget b,@NotNull BetweenCellsWidget between) {

        Direction dir = detectDirection(area);
        if (dir == null) return;


        WallPieceWidget aWall = new WallPieceWidget(a, provider, dir);
        WallPieceWidget bWall = new WallPieceWidget(b, provider, dir);

        WallWidget middle = new WallWidget(toOrientation(dir), provider);

        a.addItem(aWall);
        b.addItem(bWall);
        between.setObstacle(middle);
    }

    private Direction detectDirection(BetweenCellsArea area) {
        for (Direction d : Direction.values()) {
            if (area.getNeighborCell(d) != null &&
                    area.getNeighborCell(d) != null) {
                return d;
            }
        }
        return null;
    }

    private Orientation toOrientation(Direction d) {
        return (d == Direction.EAST || d == Direction.WEST)
                ? Orientation.VERTICAL
                : Orientation.HORIZONTAL;
    }
}