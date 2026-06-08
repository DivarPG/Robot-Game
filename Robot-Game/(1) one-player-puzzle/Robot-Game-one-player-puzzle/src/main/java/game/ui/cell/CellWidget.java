package game.ui.cell;

import game.model.field.core.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Виджет ячейки.
 *
 * @see Cell
 */

public class CellWidget extends JPanel {

    /**
     * Размер виджета ячейки.
     */
    private static final int CELL_SIZE = 150;


    /**
     * Конструктор.
     */
    public CellWidget() {

        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setBackground(Color.decode("#888888"));

        setLayout(new CellLayout());

        setOpaque(true);
    }

    /**
     * Добавить элемент в ячейку.
     * Элемент размещается в зоне, определяемой интерфейсом
     * {@link Placeable}, после чего порядок отображения
     * пересчитывается в соответствии со значением z-index.
     *
     * @param item добавляемый элемент.
     * @throws IllegalArgumentException если элемент не поддерживает
     *         размещение в зонах ячейки.
     */
    public void addItem(CellItemWidget item) {

        if (!(item instanceof Placeable placeable)) {
            throw new IllegalArgumentException("ЗАБЫЛА ПЛЕСАБЛЕ");
        }

        //items.add(item);

        add(item, placeable.getZone());

        applyZOrder();

        repaint();
        revalidate();
    }

    /**
     * Удалить виджет из ячейки.
     *
     * @param item удаляемый виджет.
     */
    public void removeItem(CellItemWidget item) {

        //items.remove(item);

        remove(item);

        repaint();
        revalidate();
    }

    /**
     * Обновить порядок отображения элементов ячейки.
     * Элементы с большим значением z-index отображаются поверх
     * элементов с меньшим значением.
     */
    private void applyZOrder() {

        List<CellItemWidget> widgets = new ArrayList<>();

        for (Component c : getComponents()) {
            if (c instanceof CellItemWidget w) {
                widgets.add(w);
            }
        }

        widgets.sort((a, b) -> Integer.compare(b.getZIndex(), a.getZIndex()));

        for (int i = 0; i < widgets.size(); i++) {
            setComponentZOrder(widgets.get(i), i);
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(150, 150);
//    }

}
