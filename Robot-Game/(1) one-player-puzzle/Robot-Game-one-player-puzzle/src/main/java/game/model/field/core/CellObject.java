package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Объект, располагающийся в ячейке.
 */
public abstract class CellObject {

    //region ПОЗИЦИЯ

    /**
     * Позиция объекта.
     */
    private Cell position;

    /**
     * Получить позицию объекта {@link CellObject#position}.
     *
     * @return позиция объекта.
     */
    public Cell getPosition() {
        return position;
    }

    /**
     * Установить позицию объекта {@link CellObject#position}.
     *
     * @param position позиция.
     * @return установлена ли позиция.
     */
    boolean setPosition(@NotNull Cell position) {
        if (!canSetPosition(position)) {
            return false;
        }

        this.position = position;
        return true;
    }

    /**
     * Может ли объект располагаться в указанной позиции.
     *
     * @param cell позиция.
     * @return может ли объект располагаться в указанной позиции.
     */
    protected boolean canSetPosition(@NotNull Cell cell) {
        return canCoexistWithObjectsIn(cell) && canChangePosition(cell);
    }

    /**
     * Может ли объект сосуществовать с объектами, находящимися в указанной позиции.
     *
     * @param cell позиция.
     * @return может ли объект сосуществовать с объектами, находящимися в указанной позиции.
     */
    private boolean canCoexistWithObjectsIn(@NotNull Cell cell) {
        Set<Class<? extends CellObject>> types = cell.objectTypes();
        boolean canCoexist = true;
        for (Class type : types) {
            canCoexist = canCoexist && canCoexistWith(type);
        }
        return canCoexist;
    }

    /**
     * Может ли объект сменить позицию с текущей на указанную.
     *
     * @param cell позиция.
     * @return может ли объект сменить позицию с текущей на указанную.
     */
    protected abstract boolean canChangePosition(@NotNull Cell cell);

    /**
     * Удалить позицию у объекта {@link CellObject#position}.
     */
    void unsetPosition() {
        this.position = null;
    }

    //endregion

    //region СОСУЩЕСТВОВАНИЕ С ДРУГИМИ ОБЪЕКТАМИ
    abstract boolean canCoexistWith(Class<? extends CellObject> type);
    //endregion
}
