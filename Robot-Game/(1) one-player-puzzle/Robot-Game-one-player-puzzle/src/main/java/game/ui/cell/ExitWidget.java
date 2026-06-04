package game.ui.cell;

import game.model.field.core.ExitCell;
import game.ui.resource.*;
import game.ui.resource.image.ImageResource;
import game.ui.resource.image.WithImageResource;
import game.ui.utils.ImageScaler;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет ячейки выхода.
 *
 * @see ExitCell
 */
public class ExitWidget extends CellWidget implements WithImageResource {

    private final BufferedImage image;

    public ExitWidget( ResourceProvider<BufferedImage, ImageResource> provider) {

        this.image  = getImage(provider);
        setPreferredSize(new Dimension(150, 150));
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public ImageResource getImageType() {
        return ImageResource.EXIT;
    }

    @Override
    public BufferedImage getImage( ResourceProvider<BufferedImage,ImageResource> provider) {
       BufferedImage original = provider.get(getImageType());
       return ImageScaler.resize(original, 120, 120);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        try {
//            BufferedImage image = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "exit.png"));
//            image = ImageUtils.resizeImage(image, 120, 120);
//            g.drawImage(image, 0, 0, null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
