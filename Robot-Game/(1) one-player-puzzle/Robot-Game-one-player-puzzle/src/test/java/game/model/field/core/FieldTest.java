package game.model.field.core;

import game.model.events.FieldActionEvent;
import game.model.events.FieldActionListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Создание поля: корректное создание поля с заданными размерами, корректное размещение ячеек, назначение ячейки выхода в указанной позиции
 * Связь между ячейками поля: корректное установление соседей по всем направлениям, согласованность связей между ячейками
 * Проверка параметров создания: запрет создания поля с отрицательной шириной, запрет создания поля с нулевой шириной, запрет создания поля с отрицательной высотой, запрет создания поля с нулевой высотой, запрет создания поля с некорректной точкой выхода
 * Работа с роботами на поле: отсутствие роботов на пустом поле, корректное получение робота при его наличии
 * Телепортация на поле: телепортация робота при попадании в ячейку выхода, корректное сохранение состояния телепортированного робота
 * События поля: генерация события при телепортации робота, корректное количество срабатываний события
 * Граничные случаи: отсутствие роботов на поле, обработка некорректных параметров при создании поля
 */


public class FieldTest {

    private static final Point EXIT_POINT = new Point(1, 1);

    private int eventCount;
    private FieldActionEvent lastEvent;
    private Field field;

    class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            eventCount += 1;
            lastEvent = event;
        }
    }

    @BeforeEach
    public void testSetup() {
        eventCount = 0;
        lastEvent = null;
        field = new Field(2, 2, EXIT_POINT);
        field.addFieldActionListener(new FieldObserver());
    }

    @Test
    public void test_create_withCorrectParams() {
        AbstractCell cell00 = field.getCell(new Point(0, 0));
        AbstractCell cell10 = field.getCell(new Point(1, 0));
        AbstractCell cell01 = field.getCell(new Point(0, 1));
        AbstractCell cell11 = field.getCell(EXIT_POINT);

        assertEquals(2, field.getWidth());
        assertEquals(2, field.getHeight());
        assertSame(cell01, cell00.getNeighborCell(Direction.SOUTH));
        assertSame(cell11, cell10.getNeighborCell(Direction.SOUTH));
        assertSame(cell10, cell11.getNeighborCell(Direction.NORTH));
        assertSame(cell00, cell01.getNeighborCell(Direction.NORTH));
        assertSame(cell10, cell00.getNeighborCell(Direction.EAST));
        assertSame(cell11, cell01.getNeighborCell(Direction.EAST));
        assertSame(cell00, cell10.getNeighborCell(Direction.WEST));
        assertSame(cell01, cell11.getNeighborCell(Direction.WEST));
        assertTrue(cell11 instanceof ExitCell);
        assertTrue(cell00 instanceof NormalCell);
    }

    @Test
    public void test_create_withNegativeWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Field(-1, 1, new Point(0, 0)));
    }

    @Test
    public void test_create_withZeroWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Field(0, 1, new Point(0, 0)));
    }

    @Test
    public void test_create_withNegativeHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, -1, new Point(0, 0)));
    }

    @Test
    public void test_create_withZeroHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 0, new Point(0, 0)));
    }

    @Test
    public void test_create_withIncorrectExitPoint_xOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 1, new Point(2, 0)));
    }

    @Test
    public void test_create_withIncorrectExitPoint_yOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 1, new Point(0, 2)));
    }

    @Test
    public void test_getRobotsOnField_empty() {
        assertNull(field.getRobot());
    }

    @Test
    public void test_getRobotsOnField_oneRobot() {
        Robot robot = new Robot(new Battery());
        field.getCell(new Point(0, 0)).setBigObject(robot);

        assertSame(robot, field.getRobot());
    }

    @Test
    public void test_TeleportedRobots_oneRobot() {
        Robot robot = new Robot(new Battery());
        ExitCell exitCell = (ExitCell) field.getCell(EXIT_POINT);

        boolean result = exitCell.setBigObject(robot);

        assertTrue(result);
        assertSame(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_teleportEvent_oneRobot() {
        Robot robot = new Robot(new Battery());
        AbstractCell exitCell = field.getCell(EXIT_POINT);

        boolean result = exitCell.setBigObject(robot);

        assertTrue(result);
        assertEquals(1, eventCount);
        assertNotNull(lastEvent);
        assertSame(field, lastEvent.getSource());
        assertSame(robot, lastEvent.getRobot());
        assertSame(exitCell, lastEvent.getTeleport());
    }

    @Test
    public void test_teleportEvent_notTriggeredWhenRobotNotPlacedInExitCell() {
        Robot robot = new Robot(new Battery());

        boolean result = field.getCell(new Point(0, 0)).setBigObject(robot);

        assertTrue(result);
        assertEquals(0, eventCount);
        assertNull(lastEvent);
    }
}
