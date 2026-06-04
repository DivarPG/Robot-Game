package game.ui.obstacle.wallcomponent;

import game.model.field.core.Direction;
import game.ui.cell.CellItemWidget;
import game.ui.cell.CellLayout;
import game.ui.cell.CellWidget;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.ImageResource;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WallPieceWidget extends CellItemWidget{

    private final CellWidget cell;
    private final Direction direction;

    private static final Dimension HORIZONTAL = new Dimension(90, 5);
    private static final Dimension VERTICAL = new Dimension(5, 90);

    public WallPieceWidget(
            @NotNull CellWidget cell,
            @NotNull ResourceProvider<BufferedImage, ImageResource> imageProvider,
            @NotNull Direction direction
    ) {
        super(imageProvider);
        this.cell = cell;
        this.direction = direction;
    }

    @Override
    protected Dimension getDimension() {
        return switch (direction) {
            case NORTH, SOUTH -> HORIZONTAL;
            case EAST, WEST -> VERTICAL;
        };
    }

    @Override
    public CellLayout.Zone getZone() {
        return switch (direction) {
            case NORTH -> CellLayout.Zone.WALL_BOTTOM;
            case SOUTH -> CellLayout.Zone.WALL_TOP;
            case WEST  -> CellLayout.Zone.WALL_RIGHT;
            case EAST  -> CellLayout.Zone.WALL_LEFT;
        };
    }

    @Override
    public ImageResource getImageType() {
        return switch (direction) {
            case NORTH, SOUTH -> ImageResource.WALL_HORIZONTAL;
            case EAST, WEST   -> ImageResource.WALL_VERTICAL;
        };
    }

    @Override
    public BufferedImage getImage(ResourceProvider<BufferedImage, ImageResource> provider) {
        return provider.get(getImageType());
    }
}