package game.model.field.core;

import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.model.field.between_cells_objects.WallSegment;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Размещение робота: возможность размещения робота в пустой ячейке, запрет размещения в ячейке с другим роботом, возможность размещения в ячейке с батареей
 * Перемещение робота: перемещение в соседнюю ячейку при наличии пути, обновление позиции робота после перемещения, освобождение предыдущей ячейки
 * Ограничения перемещения: отсутствие перемещения при отсутствии соседней ячейки, отсутствие перемещения при наличии стены, отсутствие перемещения при недостаточном заряде
 * Расход заряда: уменьшение заряда при успешном перемещении, отсутствие расхода заряда при невозможности перемещения
 * События перемещения: генерация события при успешном перемещении, отсутствие событий при неуспешных действиях
 * Работа с батареей: замена батареи при наличии батареи в ячейке, отсутствие изменений при отсутствии батареи в ячейке
 * Состояние робота: сохранение активности робота при достаточных условиях, возможность функционирования при нулевом заряде при наличии батареи в ячейке
 * Граничные случаи: попытка перемещения в недоступном направлении, попытка действий при нулевом заряде, отсутствие батареи для замены
 */

class RobotTest {

    private static final int DEFAULT_TEST_BATTERY_CHARGE = 10;
    private static final int AMOUNT_OF_CHARGE_FOR_MOVE = 1;

    private AbstractCell cell;
    private AbstractCell neighborCell;
    private final Direction direction = Direction.NORTH;

    private Robot robot;
    private int moveEventCount;
    private int changeBatteryEventCount;
    private RobotActionEvent lastMoveEvent;
    private RobotActionEvent lastChangeBatteryEvent;

    private class EventsListener implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            moveEventCount += 1;
            lastMoveEvent = event;
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            changeBatteryEventCount += 1;
            lastChangeBatteryEvent = event;
        }
    }

    @BeforeEach
    public void testSetup() {
        moveEventCount = 0;
        changeBatteryEventCount = 0;
        lastMoveEvent = null;
        lastChangeBatteryEvent = null;

        robot = new Robot(new Battery());
        robot.addRobotActionListener(new EventsListener());

        cell = new NormalCell();
        neighborCell = new NormalCell();

        Map<Direction, AbstractCell> neighbors = new HashMap<>();
        neighborCell.setNeighbors(null);
        neighbors.put(direction, neighborCell);
        cell.setNeighbors(neighbors);
    }

    @Test
    public void test_canStayAtPosition_emptyCell() {
        assertTrue(robot.canSetPosition(cell));
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_canStayAtPosition_cellWithRobot() {
        cell.setBigObject(robot);

        assertFalse(robot.canSetPosition(cell));
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_canStayAtPosition_cellWithBattery() {
        ((NormalCell) cell).setSmallObject(new Battery());

        assertTrue(robot.canSetPosition(cell));
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndEnoughCharge() {
        cell.setBigObject(robot);

        boolean result = robot.move(direction);

        assertTrue(result);
        assertSame(robot, neighborCell.getBigObject());
        assertSame(neighborCell, robot.getPosition());
        assertNull(cell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - AMOUNT_OF_CHARGE_FOR_MOVE, robot.getCharge());
        assertEquals(1, moveEventCount);
        assertNotNull(lastMoveEvent);
        assertSame(robot, lastMoveEvent.getRobot());
        assertSame(cell, lastMoveEvent.getFromCell());
        assertSame(neighborCell, lastMoveEvent.getToCell());
        assertSame(robot, lastMoveEvent.getSource());
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_move_noCellInDirectionAndRobotActiveAndEnoughCharge() {
        neighborCell.setBigObject(robot);

        boolean result = robot.move(Direction.NORTH);

        assertFalse(result);
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertSame(neighborCell, robot.getPosition());
        assertSame(robot, neighborCell.getBigObject());
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_move_emptyCellInDirectionWithWallAndRobotActiveAndEnoughCharge() {
        cell.setBigObject(robot);
        cell.setNeighborObstacle(direction, new WallSegment());

        boolean result = robot.move(direction);

        assertFalse(result);
        assertSame(robot, cell.getBigObject());
        assertSame(cell, robot.getPosition());
        assertNull(neighborCell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndNotEnoughCharge() {
        cell.setBigObject(robot);

        robot.unsetBattery();
        robot.setBattery(new Battery(0));

        boolean result = robot.move(direction);

        assertFalse(result);
        assertSame(robot, cell.getBigObject());
        assertSame(cell, robot.getPosition());
        assertNull(neighborCell.getBigObject());
        assertEquals(0, robot.getCharge());
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_changeBattery_robotIsActiveCellContainsBattery() {
        cell.setBigObject(robot);
        Battery newBattery = new Battery();
        ((NormalCell) cell).setSmallObject(newBattery);

        boolean result = robot.changeBattery();

        assertTrue(result);
        assertNull(((NormalCell) cell).getSmallObject());
        assertEquals(newBattery.getCharge(), robot.getCharge());
        assertEquals(0, moveEventCount);
        assertEquals(1, changeBatteryEventCount);
        assertNotNull(lastChangeBatteryEvent);
        assertSame(robot, lastChangeBatteryEvent.getRobot());
        assertSame(newBattery, lastChangeBatteryEvent.getBattery());
        assertSame(robot, lastChangeBatteryEvent.getSource());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellNotContainsBattery() {
        cell.setBigObject(robot);

        boolean result = robot.changeBattery();

        assertFalse(result);
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_changeBattery_robotInExitCell_returnsFalse() {
        ExitCell exitCell = new ExitCell();
        exitCell.setBigObject(robot);

        boolean result = robot.changeBattery();

        assertFalse(result);
        assertEquals(0, moveEventCount);
        assertEquals(0, changeBatteryEventCount);
    }

    @Test
    public void test_robotIsCapable_zeroChargeAndCellWithBattery() {
        NormalCell normalCell = new NormalCell();
        normalCell.setBigObject(robot);

        robot.unsetBattery();
        robot.setBattery(new Battery(0));
        normalCell.setSmallObject(new Battery());

        assertTrue(robot.isCapable());
    }

    @Test
    public void test_robotIsCapable_zeroChargeAndCellWithoutBattery() {
        NormalCell normalCell = new NormalCell();
        normalCell.setBigObject(robot);

        robot.unsetBattery();
        robot.setBattery(new Battery(0));

        assertFalse(robot.isCapable());
    }

    @Test
    public void test_robotIsCapable_teleportedRobot_returnsFalse() {
        ExitCell exitCell = new ExitCell();
        exitCell.setBigObject(robot);

        assertFalse(robot.isCapable());
    }
}
