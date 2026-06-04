package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Ячейка.
 */
public class Cell {

    //region ОБЪЕКТ В ЯЧЕЙКЕ
    private final int MAX_OBJECT_COUNT = 2;
    /**
     * Список объектов, расположенных в ячейке.
     */
    private Map<Class<? extends CellObject>, CellObject> _objects = new HashMap<>();

    /**
     * Получить объект по типу.
     *
     * @param type класс объекта, который нужно получить.
     * @return первый найденный запрашиваемый объект, null - если объект не содержится в ячейке {@link Cell#_objects}.
     */
    public CellObject getObject(Class<? extends CellObject> type) {
        return _objects.getOrDefault(type, null);
    }

    /**
     * Поместить объект в ячейку {@link Cell#_objects}.
     *
     * @param object объект, добавляемый в ячейку.
     * @return успешность.
     * @throws IllegalArgumentException если запрашиваемый класс не является поддерживаемым абстрактным классом.
     */
    public boolean setObject(CellObject object) {
        Class<? extends CellObject> type = object.getClass();

        if (!canSetObject(type)) {
            return false;
        }

        boolean success = object.setPosition(this);
        if (!success) {
            return false;
        }

        _objects.put(type, object);

        tryActivateSelfActivatingObject();
        return true;
    }

    /**
     * Может принять объект.
     *
     * @param type класс объекта, который нужно проверить.
     * @return может принять объект.
     */
    public boolean canSetObject(@NotNull Class<? extends CellObject> type) {
        boolean canCoexist = true;

        for (CellObject obj : _objects.values()) {
            canCoexist = canCoexist && obj.canCoexistWith(type);
        }

        return (_objects.size() < MAX_OBJECT_COUNT) && canCoexist;
    }

    /**
     * Изъять объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link Cell#_objects}.
     */
    public CellObject takeObject(Class<? extends CellObject> type) {
        CellObject result = _objects.remove(type);

        if (result != null) {
            result.unsetPosition();
        }

        return result;
    }

    /**
     * Получить список типов всех объектов, хранящихся в ячейке.
     *
     * @return список типов всех объектов, хранящихся в ячейке.
     */
    public Set<Class<? extends CellObject>> objectTypes() {
        return Collections.unmodifiableSet(_objects.keySet());
    }
    //endregion

    //region САМОАКТИВИРУЮЩИЙСЯ ОБЪЕКТ

    /**
     * Вызывает взаимодействие для самоактивирующегося объекта, если есть самоактивирующийся объект
     * и количество объектов в ячейке больше одного.
     */
    private void tryActivateSelfActivatingObject() {
        ExitPoint exitPoint = (ExitPoint) getObject(ExitPoint.class);
        if (exitPoint != null && _objects.size() > 1) {
            for (CellObject obj : _objects.values()) {
                if (!obj.equals(exitPoint)) {
                    exitPoint.execute(obj);
                }
            }
        }
    }

    //endregion

    //region СОСЕДНИЕ ЯЧЕЙКИ

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    public Cell getNeighborCell(@NotNull Direction direction) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area == null) {
            return null;
        } else {
            return area.getNeighborCell(direction);
        }
    }

    /**
     * Установить соседние ячейки.
     *
     * @param neighborCells список ячеек с соответствующими направлениями соседства.
     * @return успешность.
     */
    boolean setNeighbors(Map<Direction, Cell> neighborCells) {
        if (neighborCells != null) {
            for (Direction direction : neighborCells.keySet()) {
                if (!setNeighbor(neighborCells.get(direction), direction)) return false;
            }
        }

        surroundSelfWithBetweenCellsAreas();
        return true;
    }

    /**
     * Установить ячейку соседней.
     * Взять её область между ячейками.
     *
     * @param neighborCell соседняя ячейка.
     * @param direction    направление.
     * @return успешность.
     * @throws IllegalArgumentException если переданная ячейка не может быть соседней.
     */
    private boolean setNeighbor(@NotNull Cell neighborCell, @NotNull Direction direction) {
        if (this == neighborCell) {
            return false;
        }
        BetweenCellsArea area = neighborCell.getNeighborArea(direction.getOppositeDirection());
        return switch (direction) {
            case NORTH -> area.setVerticalNeighbors(neighborCell, this);
            case SOUTH -> area.setVerticalNeighbors(this, neighborCell);
            case EAST -> area.setHorizontalNeighbors(this, neighborCell);
            case WEST -> area.setHorizontalNeighbors(neighborCell, this);
        };
    }

    //endregion

    //region ОБЛАСТИ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Области, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellsArea> neighborAreas = new EnumMap<>(Direction.class);

    /**
     * Получить соседнюю область, располагающуюся между ячейками {@link Cell#neighborAreas} в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя область, располагающийся между ячейками в заданном направлении.
     */
    public BetweenCellsArea getNeighborArea(@NotNull Direction direction) {
        return neighborAreas.get(direction);
    }

    /**
     * Задать соседнюю область между ячейками.
     *
     * @param direction    направление.
     * @param neighborArea соседняя область между ячейками.
     * @return успешность.
     */
    boolean setNeighborArea(@NotNull Direction direction, @NotNull BetweenCellsArea neighborArea) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area != null && !area.equals(neighborArea)) return false;
        neighborAreas.put(direction, neighborArea);
        return true;
    }

    /**
     * Окружить себя промежуточными областями.
     */
    private void surroundSelfWithBetweenCellsAreas() {
        Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
        directions.removeAll(neighborAreas.keySet());

        for (Direction direction : directions) {
            BetweenCellsArea area = new BetweenCellsArea();
            switch (direction) {
                case NORTH -> area.setVerticalNeighbors(null, this);
                case SOUTH -> area.setVerticalNeighbors(this, null);
                case EAST -> area.setHorizontalNeighbors(this, null);
                case WEST -> area.setHorizontalNeighbors(null, this);
            }
        }
    }

    //endregion

    //region ПРЕПЯТСТВИЯ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Получить соседнее препятствие.
     *
     * @param direction направление.
     * @return соседнее препятствие, располагающееся между ячейками в заданном направлении.
     */
    public BetweenCellObject getNeighborObstacle(@NotNull Direction direction) {
        return getNeighborArea(direction).getObstacle();
    }

    /**
     * Установить соседнее препятствие.
     *
     * @param direction направление.
     * @param obstacle  соседнее препятствие.
     * @return успешность.
     */
    public boolean setNeighborObstacle(@NotNull Direction direction, @NotNull BetweenCellObject obstacle) {
        return getNeighborArea(direction).setObstacle(obstacle);
    }

    //endregion
}
