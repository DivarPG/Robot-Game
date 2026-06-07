package game.ui.cell;

import game.model.events.ExitPointActionEvent;
import game.model.events.ExitPointActionListener;
import game.model.field.core.ExitPoint;
import game.ui.resource.ResourceProvider;
import game.ui.resource.audio.SoundResource;
import game.ui.resource.gif.GifResource;
import game.ui.resource.audio.SoundPlayer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Виджет ячейки выхода.
 *
 * @see ExitPoint
 */

public class ExitWidget extends CellItemWidget {

    // лучше хранить через провайдера или через ресурс ?
    private final ImageIcon gif;

    public ExitWidget( ExitPoint exitPoint,
                       ResourceProvider<ImageIcon, GifResource> provider, SoundPlayer soundPlayer ){

        this.gif = provider.get(GifResource.PORTAL);

        new Timer(40, e -> repaint()).start();

        exitPoint.addExitPointActionListener(new ExitPointActionListener() {
            @Override
            public void robotIsTeleported(@NotNull ExitPointActionEvent event) {
                soundPlayer.playSound(SoundResource.TELEPORT);
            }
        });
    }

    @Override
    protected void draw(Graphics g) {

        int width = 100;
        int height = 100;

        int x = (getWidth() - width) / 2;
        int y = (getHeight() - height) / 2;

        g.drawImage(
                gif.getImage(),
                x,
                y,
                width,
                height,
                this
        );
    }

    @Override
    public CellLayout.Zone getZone() {
        return CellLayout.Zone.PRIMARY;
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(150, 150);
    }

    @Override
    public int getZIndex() {
        return 50;
    }
}
