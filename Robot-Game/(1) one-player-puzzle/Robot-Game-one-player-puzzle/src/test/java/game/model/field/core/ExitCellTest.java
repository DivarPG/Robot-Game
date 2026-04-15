package game.model.field.core;

import game.model.events.ExitCellActionEvent;
import game.model.events.ExitCellActionListener;
import game.model.field.between_cells_objects.WallSegment;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Телепортация робота: телепортация робота при помещении в ячейку выхода, сохранение ссылки на телепортированного робота
 * События телепортации: генерация события при телепортации робота, корректное количество срабатываний события
 * Ограничения на размещение роботов: запрет размещения второго робота после телепортации первого, сохранение состояния первого робота
 * Состояние робота: установка признака телепортации у робота после попадания в ячейку выхода
 * Получение телепортированного робота: возврат телепортированного робота после успешной операции, отсутствие робота при отсутствии телепортации
 * Граничные случаи: попытка добавить второго робота, отсутствие телепортированных роботов
 */

public class ExitCellTest extends AbstractCellTest{ // не хватает наследования // expectedCountEvents как будто лишнее, но в целом хорошие

    private ExitCell exitCell;
    private Robot robot;

    private int countEvents = 0;

    @Override @Test
    void setRobot_InEmptyCell() {
        exitCell.setBigObject(robot);

        assertEquals(robot, exitCell.getBigObject());
        assertEquals(exitCell, robot.getPosition());
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
        assertEquals(1, countEvents);
    }

    @Override @Test
    void takeRobot_FromCellWithRobot() {
        exitCell.setBigObject(robot);

        assertEquals(robot, exitCell.takeBigObject());
        assertNull(robot.getPosition());
        assertNull(exitCell.getBigObject());
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
        assertEquals(1, countEvents);
    }

    @Override @Test
    void setRobot_ToCellWithRobot() {
        Robot newRobot = new Robot(new Battery());

        exitCell.setBigObject(robot);

        assertFalse(exitCell.setBigObject(newRobot));
        assertEquals(robot, exitCell.getBigObject());
        assertEquals(exitCell, robot.getPosition());
        assertNull(newRobot.getPosition());
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
        assertEquals(1, countEvents);
    }

    @Override @Test
    void setRobot_ToCellAgain() {
        exitCell.setBigObject(robot);

        assertFalse(exitCell.setBigObject(robot));
        assertEquals(robot, exitCell.getBigObject());
        assertEquals(exitCell, robot.getPosition());
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
        assertEquals(1, countEvents);
    }

    @Override @Test
    void setNeighborCell() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        exitCell.setNeighbors(map);

        assertEquals(neighborCell, exitCell.getNeighborCell(direction));
        assertEquals(exitCell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_doubleSided() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        exitCell.setNeighbors(map);
        map2.put(direction.getOppositeDirection(), exitCell);

        assertTrue(neighborCell.setNeighbors(map2));
        assertEquals(neighborCell, exitCell.getNeighborCell(direction));
        assertEquals(exitCell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_twoTimesInOneDirection() {
        AbstractCell neighborCell = new NormalCell();
        AbstractCell anotherCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        exitCell.setNeighbors(map);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        anotherCell.setNeighbors(null);
        map2.put(direction, anotherCell);

        assertFalse(exitCell.setNeighbors(map2));
        assertEquals(neighborCell, exitCell.getNeighborCell(direction));
        assertEquals(exitCell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_alreadyNeighborWithAnotherDirection() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;
        Direction anotherDirection = Direction.SOUTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        exitCell.setNeighbors(map);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        map2.put(anotherDirection, neighborCell);

        assertFalse(exitCell.setNeighbors(map2));
        assertEquals(neighborCell, exitCell.getNeighborCell(direction));
        assertEquals(exitCell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_setSelfAsNeighbor() {
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, exitCell);

        assertFalse(exitCell.setNeighbors(map));
        assertNull(exitCell.getNeighborCell(direction));
    }

    @Override @Test
    void isNeighbor_WhenNeighborCellExists() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        exitCell.setNeighbors(map);

        assertEquals(neighborCell, exitCell.getNeighborCell(direction));
    }

    @Override @Test
    void isNeighbor_WhenNeighborCellNotExists() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        exitCell.setNeighbors(null);
        exitCell.setNeighborObstacle(direction, wallSegment);
        assertEquals(wallSegment, exitCell.getNeighborObstacle(direction));
        assertEquals(exitCell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setWall_InSingleWithSameWallAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        exitCell.setNeighbors(null);
        exitCell.setNeighborObstacle(direction, wallSegment);
        assertFalse(exitCell.setNeighborObstacle(Direction.SOUTH, wallSegment));
        assertEquals(wallSegment, exitCell.getNeighborObstacle(direction));
        assertEquals(exitCell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setWall_InSingleWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        exitCell.setNeighbors(null);
        exitCell.setNeighborObstacle(direction, wallSegment);
        assertFalse(exitCell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, exitCell.getNeighborObstacle(direction));
        assertEquals(exitCell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setWall_inNeighborCells() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        exitCell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        exitCell.setNeighborObstacle(direction, wallSegment);
        assertEquals(wallSegment, exitCell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(exitCell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell, wallSegment.getPosition().getNeighborCell(direction));
    }

    @Override @Test
    void setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        exitCell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        exitCell.setNeighborObstacle(direction, wallSegment);

        assertFalse(exitCell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, exitCell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(exitCell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell, wallSegment.getPosition().getNeighborCell(direction));
    }

    @Override @Test
    void setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        exitCell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        Direction anotherDirection = direction.getOppositeDirection();

        exitCell.setNeighborObstacle(direction, wallSegment);

        assertFalse(exitCell.setNeighborObstacle(anotherDirection, wallSegment));
        assertEquals(wallSegment, exitCell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(exitCell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell, wallSegment.getPosition().getNeighborCell(direction));
    }

    @Override @Test
    void neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(exitCell.getNeighborArea(direction));
    }

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
