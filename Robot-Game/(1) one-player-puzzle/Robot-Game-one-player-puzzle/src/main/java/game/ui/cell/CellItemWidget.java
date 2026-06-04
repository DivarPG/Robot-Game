package game.ui.cell;

import game.ui.resource.image.ImageResource;
import game.ui.resource.ResourceProvider;
import game.ui.resource.image.WithImageResource;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Виджет объекта для виджета ячейки.
 */
public abstract class CellItemWidget extends JPanel implements WithImageResource,Placeable {

    private boolean mouseTransparent = false;

    private final  ResourceProvider<BufferedImage,ImageResource> imageProvider;

    protected void setMouseTransparent(boolean state) {
        mouseTransparent = state;
    }


    @Override
    public boolean contains(int x, int y) {

        if (mouseTransparent) {
            return false;
        }

        return super.contains(x, y);
    }

    /**
     * Конструктор.
     */
    public CellItemWidget(ResourceProvider<BufferedImage, ImageResource> imageProvider) {
        this.imageProvider = imageProvider;
        setOpaque(false);
    }

    /**
     * Получить слой на котором располагается виджет.
     *
     * @return слой на котором располагается виджет.
     */
    //public abstract Layer getLayer();

    /**
     * Получить размеры виджета.
     *
     * @return размеры виджета.
     */
    protected abstract Dimension getDimension();

    protected void drawImage(Graphics g) {

        BufferedImage img = getImage(imageProvider);

        int cw = getWidth();
        int ch = getHeight();

        int iw = img.getWidth();
        int ih = img.getHeight();

        int x = (cw - iw) / 2;
        int y = (ch - ih) / 2;

        g.drawImage(img, x, y, iw, ih, null);
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//       drawImage(g);
//       drawOverlay(g);
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getComponentCount() == 0) {
            drawImage(g);
            drawOverlay(g);
        }
    }

    protected JComponent createContent() {
        return null;
    }

    // если не абстрактный то пустой хз - иначе делать всем текст зачем
    protected void drawOverlay(Graphics g){

    }

    public int getZIndex() {
        return 0;
    }
}
