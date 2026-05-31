package game.model.labyrinths;

import game.model.field.core.*;
import game.model.field.between_cells_objects.WallSegment;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Лабиринт.
 */
public abstract class Labyrinth {

    //region СБОРЩИК ПОЛЯ

    /**
     * Построить поле.
     *
     * @return поле.
     */
    public Field createField() {

        Field field = new Field(fieldWidth(), fieldHeight());

        populateField(field);

        return field;
    }

    //endregion

    //region ЗАСЕЛЕНИЕ ПОЛЯ

    /**
     * Заселить поле.
     *
     * @param field поле.
     */
    private void populateField(@NotNull Field field) {
        populateWalls(field);
        populateRobot(field);
        populateBatteries(field);
        populateExitPoint(field);
    }

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    private void populateWalls(@NotNull Field field) {
        Map<WallSegment, AbstractMap.SimpleEntry<Cell, Direction>> walls = createWalls(field);

        for (WallSegment wall : walls.keySet()) {
            Cell cell = walls.get(wall).getKey();
            Direction direction = walls.get(wall).getValue();
            boolean result = cell.setNeighborObstacle(direction, wall);
            assert result : "Wall segment " + wall + " not set at " + cell + " with direction " + direction;
        }
    }

    /**
     * Добавить роботов на поле.
     *
     * @param field поле.
     */
    private void populateRobot(@NotNull Field field) {
        // Get information about single robot on field
        AbstractMap.SimpleEntry<Robot, Cell> robotInfo = createRobot(field);
        Robot robot = robotInfo.getKey();
        Cell robotCell = robotInfo.getValue();

        // Establish connection between the cell and the robot
        boolean correct = robotCell.setObject(robot);
        assert correct; // Check connection status
    }

    /**
     * Добавить источники питания на поле.
     *
     * @param field поле.
     */
    private void populateBatteries(@NotNull Field field) {
        Map<Battery, Cell> batteries = createBatteries(field);

        for (Battery battery : batteries.keySet()) {
            Cell cell = batteries.get(battery);
            boolean correct = cell.setObject(battery);
            assert correct : "Battery can't set at cell";
        }
    }

    /**
     * Добавить точки выхода на поле.
     *
     * @param field поле.
     */
    private void populateExitPoint(@NotNull Field field) {
        Map<ExitPoint, Cell> exitPoints = createExitPoints(field);

        for (ExitPoint exitPoint : exitPoints.keySet()) {
            Cell cell = exitPoints.get(exitPoint);
            boolean correct = cell.setObject(exitPoint);
            assert correct : "Exit Point can't set at cell";
        }
    }

    //endregion

    //region СВОЙСТВА ПОЛЯ

    /**
     * Высота поля.
     *
     * @return высота поля.
     */
    protected abstract int fieldHeight();

    /**
     * Ширина поля.
     *
     * @return ширина поля.
     */
    protected abstract int fieldWidth();

    /**
     * Координаты ячейки выхода.
     *
     * @return координаты ячейки выхода.
     */
    protected abstract Point exitPoint();

    //endregion

    //region СОЗДАНИЕ ОБЪЕКТОВ

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    protected abstract Map<WallSegment, AbstractMap.SimpleEntry<Cell, Direction>> createWalls(@NotNull Field field);

    /**
     * Добавить роботов на поле.
     *
     * @param field поле.
     */
    protected abstract AbstractMap.SimpleEntry<Robot, Cell> createRobot(@NotNull Field field);

    /**
     * Добавить источники питания на поле.
     *
     * @param field поле.
     */
    protected abstract Map<Battery, Cell> createBatteries(@NotNull Field field);

    /**
     * Добавить точки выхода на поле.
     *
     * @param field поле.
     */
    protected abstract Map<ExitPoint, Cell> createExitPoints(@NotNull Field field);
    //endregion
}
