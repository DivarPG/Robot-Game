package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import game.model.events.*;

import java.util.*;

/**
 * Поле.
 */
public class Field {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор.
     *
     * @param width  ширина. Должна быть > 0.
     * @param height высота. Должна быть > 0.
     * @throws IllegalArgumentException если ширина, высота или координата ячейки переданы некорректные.
     */
    public Field(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Field width must be more than 0");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("Field height must be more than 0");
        }

        this.width = width;
        this.height = height;

        buildField();
    }

    /**
     * Построить игровое поле.
     *
     *
     */
    private void buildField() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Point p = new Point(x, y);
                Cell cell = new Cell();

                Map<Direction, Cell> neighborCells = new HashMap<>();

                if (x > 0) {
                    neighborCells.put(Direction.WEST, getCell(p.to(Direction.WEST, 1)));
                }

                if (y > 0) {
                    neighborCells.put(Direction.NORTH, getCell(p.to(Direction.NORTH, 1)));
                }

                boolean success = cell.setNeighbors(neighborCells);
                assert success : "Cell " + cell + " not successfully set";

                cells.put(p, cell);
            }
        }
    }

    //endregion

    //region СВОЙСТВА

    //region ШИРИНА

    /**
     * Ширина поля.
     */
    private final int width;

    /**
     * Получить ширину поля {@link Field#width}.
     *
     * @return ширина поля.
     */
    public int getWidth() {
        return width;
    }

    //endregion

    //region ВЫСОТА

    /**
     * Высота поля.
     */
    private final int height;

    /**
     * Получить высоту поля {@link Field#height}.
     *
     * @return высота поля.
     */
    public int getHeight() {
        return height;
    }

    //endregion

    //region ТОЧКА ВЫХОДА

    /**
     * Получить точку выхода на поле.
     *
     * @return точка выхода на поле.
     */
    public ExitPoint getExitPoint() {
        for (Cell cell : cells.values()) {
            ExitPoint exit = (ExitPoint) cell.getObject(ExitPoint.class);
            if (exit != null) {
                return exit;
            }
        }
        return null;
    }

    //endregion

    //endregion

    //region ЯЧЕЙКИ

    /**
     * Ячейки поля.
     */
    private final Map<Point, Cell> cells = new HashMap<>();

    /**
     * Получить ячейку по заданной координате.
     *
     * @param point координата.
     * @return ячейка.
     */
    public Cell getCell(@NotNull Point point) {
        return cells.get(point);
    }

    //endregion

    //region РОБОТ

    /**
     * Получить робота на поле.
     *
     * @return робот на поле.
     */
    public Robot getRobot() {
        for (Cell cell : cells.values()) {
            Robot robot = (Robot) cell.getObject(Robot.class);
            if (robot != null) {
                return robot;
            }
        }
        return null;
    }

    //endregion

    //region СЛУШАТЕЛИ

    /**
     * Класс, реализующий наблюдение за событиями {@link ExitPointActionListener}.
     */
    class ExitPointObserver implements ExitPointActionListener {

        @Override
        public void robotIsTeleported(@NotNull ExitPointActionEvent event) {
            fireRobotIsTeleported(event.getTeleport());
        }
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события поля.
     */
    private final ArrayList<FieldActionListener> fieldListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void addFieldActionListener(FieldActionListener listener) {
        fieldListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void removeFieldCellActionListener(FieldActionListener listener) {
        fieldListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что робот телепортировался.
     *
     * @param teleport телепорт.
     */
    private void fireRobotIsTeleported(@NotNull ExitPoint teleport) {
        FieldActionEvent event = new FieldActionEvent(this);
        event.setRobot((teleport.getTeleportedRobot()));
        event.setTeleport(teleport);

        for (FieldActionListener listener : fieldListListener) {
            listener.robotIsTeleported(event);
        }
    }

    //endregion

    //region OBJECT

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Field field = (Field) o;

        return width == field.width && height == field.height && Objects.equals(cells, field.cells) && Objects.equals(getExitPoint(), field.getExitPoint());
    }

    @Override
    public String toString() {
        return "Field{" + "cells=" + cells + ", width=" + width + ", height=" + height + ", exitPoint=" + getExitPoint() + '}';
    }

    //endregion
}
