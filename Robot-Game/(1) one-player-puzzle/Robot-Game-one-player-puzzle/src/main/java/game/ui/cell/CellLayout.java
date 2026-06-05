package game.ui.cell;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


// * делится на три зоны зоны - основную (одна), вторичную (одня) и стенную (четыре)
// *
//         * основная
// * - зона для основных элементов клетки (робот, портал)
// *
//         * вторичная
// *  - зона для предметов (батарейка)
// *
//         *  стенная
// *  - зона для компонентов межкклеточных элементов (стены)

public class CellLayout implements LayoutManager2 {

    public enum Zone {
        PRIMARY,
        SECONDARY,
        WALL_TOP,
        WALL_BOTTOM,
        WALL_LEFT,
        WALL_RIGHT
    }

    private final Map<Component, Zone> zones = new HashMap<>();


    @Override
    public void addLayoutComponent( Component comp, Object constraints) {


        zones.put(comp, (Zone) constraints);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        zones.remove(comp);
    }

    @Override
    public void layoutContainer(Container parent) {

        int w = parent.getWidth();
        int h = parent.getHeight();

        for (Map.Entry<Component, Zone> entry : zones.entrySet()) {

            Component c = entry.getKey();

            Rectangle bounds = getBounds(entry.getValue(), w, h);

            Dimension pref = c.getPreferredSize();

            int x = bounds.x + (bounds.width - pref.width) / 2;
            int y = bounds.y + (bounds.height - pref.height) / 2;

            //c.setBounds(x, y, bounds.width,bounds.height);
            c.setBounds(bounds);
        }
    }


    private static final int BORDER = 3;

    private Rectangle getBounds(Zone zone, int w, int h) {

        int innerX = BORDER;
        int innerY = BORDER;

        int innerW = w - BORDER * 2;
        int innerH = h - BORDER * 2;

        return switch (zone) {

            case PRIMARY ->
                    new Rectangle(
                            innerX,
                            innerY,
                            innerW,
                            innerH * 7 / 10
                    );

            case SECONDARY ->
                    new Rectangle(
                            innerX,
                            innerY + innerH * 7 / 10,
                            innerW,
                            innerH * 3 / 10
                    );

            case WALL_TOP ->
                    new Rectangle(
                            0,
                            0,
                            w,
                            BORDER
                    );

            case WALL_BOTTOM ->
                    new Rectangle(
                            0,
                            h - BORDER,
                            w,
                            BORDER
                    );

            case WALL_LEFT ->
                    new Rectangle(
                            0,
                            0,
                            BORDER,
                            h
                    );

            case WALL_RIGHT ->
                    new Rectangle(
                            w - BORDER,
                            0,
                            BORDER,
                            h
                    );
        };
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return parent.getPreferredSize();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }
}