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

    private int eventCount = 0;

    class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            eventCount += 1;
        }
    }

    private Field field;

    @BeforeEach
    public void testSetup() {
        eventCount = 0;
        field = new Field(2, 2, new Point(1, 1));
        field.addFieldActionListener(new FieldObserver());
    }

    @Test
    public void test_create_withCorrectParams() { // соседство уже проверялось в AbstractCellTest
        AbstractCell abstractCell_0_0 = field.getCell(new Point(0, 0));
        AbstractCell abstractCell_0_1 = field.getCell(new Point(1, 0));
        AbstractCell abstractCell_1_0 = field.getCell(new Point(0, 1));
        AbstractCell abstractCell_1_1 = field.getCell(new Point(1, 1));

        assertEquals(abstractCell_1_0, abstractCell_0_0.getNeighborCell(Direction.SOUTH));
        assertEquals(abstractCell_1_1, abstractCell_0_1.getNeighborCell(Direction.SOUTH));
        assertEquals(abstractCell_0_1, abstractCell_1_1.getNeighborCell(Direction.NORTH));
        assertEquals(abstractCell_0_0, abstractCell_1_0.getNeighborCell(Direction.NORTH));
        assertEquals(abstractCell_0_1, abstractCell_0_0.getNeighborCell(Direction.EAST));
        assertEquals(abstractCell_1_1, abstractCell_1_0.getNeighborCell(Direction.EAST));
        assertEquals(abstractCell_0_0, abstractCell_0_1.getNeighborCell(Direction.WEST));
        assertEquals(abstractCell_1_0, abstractCell_1_1.getNeighborCell(Direction.WEST));
        assertTrue(abstractCell_1_1 instanceof ExitCell);
    }

    //region тестирование конструктора параметризованные тесты
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
    public void test_create_withIncorrectExitPoint() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 1, new Point(2, 2)));
    }
    //endregion

    @Test
    public void test_getRobotsOnField_empty() {
        assertNull(field.getRobot());
    }

    @Test
    public void test_getRobotsOnField_oneRobot() { // хороший
        Robot robot = new Robot(new Battery());
        field.getCell(new Point(0, 0)).setBigObject(robot);

        assertEquals(robot, field.getRobot());
    }

    @Test
    public void test_TeleportedRobots_oneRobot() { // хороший
        Robot robot = new Robot(new Battery());
        ExitCell cell = (ExitCell) field.getCell(new Point(1, 1));
        cell.setBigObject(robot);

        assertEquals(robot, cell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_teleportEvent_oneRobot() { // хороший
        int expectedEventCount = 1;
        Robot robot = new Robot(new Battery());

        field.getCell(new Point(1, 1)).setBigObject(robot);

        assertEquals(expectedEventCount, eventCount);
    }
}
