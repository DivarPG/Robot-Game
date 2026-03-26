package game.model.field.core;

import game.model.events.ExitCellActionEvent;
import game.model.events.ExitCellActionListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Телепортация робота: телепортация робота при помещении в ячейку выхода, сохранение ссылки на телепортированного робота
 * События телепортации: генерация события при телепортации робота, корректное количество срабатываний события
 * Ограничения на размещение роботов: запрет размещения второго робота после телепортации первого, сохранение состояния первого робота
 * Состояние робота: установка признака телепортации у робота после попадания в ячейку выхода
 * Получение телепортированного робота: возврат телепортированного робота после успешной операции, отсутствие робота при отсутствии телепортации
 * Граничные случаи: попытка добавить второго робота, отсутствие телепортированных роботов
 */

public class ExitCellTest { // не хватает наследования // expectedCountEvents как будто лишнее, но в целом хорошие

    private ExitCell exitCell;
    private Robot robot;

    private int countEvents = 0;

    private class EventListener implements ExitCellActionListener {

        @Override
        public void robotIsTeleported(@NotNull ExitCellActionEvent event) {
            countEvents += 1;
        }
    }

    @BeforeEach
    public void testSetup() {
        // Clear events count
        countEvents = 0;

        // setting up robot
        robot = new Robot(new Battery());

        exitCell = new ExitCell();
        exitCell.addExitCellActionListener(new EventListener());
    }

    @Test
    public void test_setRobot_oneRobot() {
        exitCell.setBigObject(robot);

        int expectedCountEvents = 1;

        assertEquals(expectedCountEvents, countEvents);
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_setRobot_setTeleportedRobot() {
        exitCell.setBigObject(robot);

        int expectedCountEvents = 1;

        assertFalse(exitCell.setBigObject(new Robot(new Battery())));
        assertEquals(expectedCountEvents, countEvents);
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_getTeleportedRobots_empty() {
        assertNull(exitCell.getTeleportedRobot());
    }
}
