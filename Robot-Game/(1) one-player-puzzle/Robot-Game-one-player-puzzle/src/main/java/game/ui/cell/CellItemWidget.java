package game.ui.cell;

import javax.swing.*;
import java.awt.*;


/**
 * Виджет объекта для виджета ячейки.
 */
public abstract class CellItemWidget extends JPanel implements Placeable {

    /**
     * Конструктор
     */
    public CellItemWidget() {
        setOpaque(false);
    }

    /**
     * Проверить, содержит ли виджет указанную точку
     *
     * @param x координата по оси X
     * @param y координата по оси Y
     * @return {@code true}, если точка принадлежит виджету
     */
    @Override
    public boolean contains(int x, int y) {

        if (mouseTransparent) {
            return false;
        }

        return super.contains(x, y);
    }

    /**
     * Получить порядок отображения виджета
     *
     * @return значение Z-индекса
     */
    public int getZIndex() {
        return 0;
    }

    private boolean mouseTransparent = false;

    /**
     * Установить прозрачность для событий мыши
     *
     * @param state состояние прозрачности
     */
    protected void setMouseTransparent(boolean state) {
        mouseTransparent = state;
    }

    /**
     * Получить размеры виджета
     *
     * @return размеры виджета
     */
    protected abstract Dimension getDimension();

    /**
     * Отрисовать содержимое виджета
     *
     * @param g графический контекст
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getComponentCount() == 0) {
            draw(g);
            drawOverlay(g);
        }
    }

    /**
     * Выполнить отрисовку основного содержимого виджета
     *
     * @param g графический контекст
     */
    protected abstract void draw(Graphics g);


    /**
     * Выполнить отрисовку дополнительного содержимого поверх основного
     *
     * @param g графический контекст
     */
    protected void drawOverlay(Graphics g){
        // !!! если не абстрактный то пустой хз - иначе делать всем текст зачем
    }

}
