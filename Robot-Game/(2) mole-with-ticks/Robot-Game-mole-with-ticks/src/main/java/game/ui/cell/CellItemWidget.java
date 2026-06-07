package game.ui.cell;

import javax.swing.*;
import java.awt.*;


/**
 * Виджет объекта для виджета ячейки.
 */
public abstract class CellItemWidget extends JPanel implements Placeable {

    private boolean mouseTransparent = false;

    //private final  ResourceProvider<BufferedImage,ImageResource> imageProvider;

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
    public CellItemWidget() {
        //this.imageProvider = imageProvider;
        setOpaque(false);
    }


    /**
     * Получить размеры виджета.
     *
     * @return размеры виджета.
     */
    protected abstract Dimension getDimension();

//    protected void drawImage(Graphics g) {
//
//        BufferedImage img = getImage(imageProvider);
//
//        int cw = getWidth();
//        int ch = getHeight();
//
//        int iw = img.getWidth();
//        int ih = img.getHeight();
//
//        int x = (cw - iw) / 2;
//        int y = (ch - ih) / 2;
//
//        g.drawImage(
//                  img,
//                  x,
//                  y,
//                  iw,
//                  ih,
//                  null);
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getComponentCount() == 0) {
            draw(g);
            drawOverlay(g);
        }
    }

    protected abstract void draw(Graphics g);


    // если не абстрактный то пустой хз - иначе делать всем текст зачем
    protected void drawOverlay(Graphics g){

    }

    public int getZIndex() {
        return 0;
    }
}
