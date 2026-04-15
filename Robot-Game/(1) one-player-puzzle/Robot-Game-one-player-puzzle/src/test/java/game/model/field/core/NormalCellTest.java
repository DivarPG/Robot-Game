package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Малый объект в ячейке: установка батареи в пустую ячейку, запрет установки второй батареи в занятую ячейку
 * Ограничения размещения батареи: запрет установки батареи, если она уже размещена в другой ячейке
 * Извлечение батареи: корректное извлечение батареи из ячейки, очистка ячейки после извлечения
 * Позиция батареи: после извлечения батареи её позиция сбрасывается в null
 * Граничные случаи: попытка извлечения батареи из пустой ячейки
 */

public class NormalCellTest extends AbstractCellTest {

    private NormalCell cell;
    private Battery battery;

    @BeforeEach
    public void testSetup() {
        cell = new NormalCell();
        battery = new Battery();
    }

    @Test
    public void test_setBattery_inEmptyCell() {
        cell.setSmallObject(battery);
        assertEquals(battery, cell.getSmallObject());
    }

    @Test
    public void test_setBattery_inNormalCell() {
        cell.setSmallObject(battery);
        Battery anotherBattery = new Battery();

        assertFalse(cell.setSmallObject(anotherBattery));
    }

    @Test
    public void test_setBattery_alreadySetBatteryToAnotherCell() {
        cell.setSmallObject(battery);

        NormalCell anotherCell = new NormalCell();

        assertFalse(anotherCell.setSmallObject(battery));
    }

    @Test
    public void test_takeBattery_fromNormalCell() {
        cell.setSmallObject(battery);

        assertEquals(battery, cell.takeSmallObject());
        assertNull(cell.getSmallObject());
        assertNull(battery.getPosition());
    }

    @Test
    public void test_takeBattery_fromCellWithoutBattery() {
        assertNull(cell.takeSmallObject());
    }





    @Override @Test
    void setRobot_InEmptyCell() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }

    @Override @Test
    void takeRobot_FromCellWithRobot() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertEquals(robot, cell.takeBigObject());
        assertNull(robot.getPosition());
        assertNull(cell.getBigObject());
    }

    @Override @Test
    void setRobot_ToCellWithRobot() {
        Robot robot = new Robot(new Battery());
        Robot newRobot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertFalse(cell.setBigObject(newRobot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }

    @Override @Test
    void setRobot_ToCellAgain() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertFalse(cell.setBigObject(robot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }

    @Override @Test
    void setNeighborCell() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_doubleSided() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        cell.setNeighbors(map);
        map2.put(direction.getOppositeDirection(), cell);

        assertTrue(neighborCell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_twoTimesInOneDirection() {
        // arrange
        AbstractCell neighborCell = new NormalCell();
        AbstractCell anotherCell = new NormalCell();
        Direction direction = Direction.NORTH;

        // arrange
        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null); // !!!
        // act
        map.put(direction, neighborCell);

        // act
        cell.setNeighbors(map);

        // arrange
        Map<Direction, AbstractCell> map2 = new HashMap<>();
        anotherCell.setNeighbors(null); // !!!
        // act
        map2.put(direction, anotherCell);

        // assert
        assertFalse(cell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_alreadyNeighborWithAnotherDirection() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;
        Direction anotherDirection = Direction.SOUTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        map2.put(anotherDirection, neighborCell);

        assertFalse(cell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setNeighborCell_setSelfAsNeighbor() {
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, cell);

        assertFalse(cell.setNeighbors(map));
        assertNull(cell.getNeighborCell(direction));
    }

    @Override @Test
    void isNeighbor_WhenNeighborCellExists() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
    }

    @Override @Test
    void isNeighbor_WhenNeighborCellNotExists() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighbors(null); // !!!
        cell.setNeighborObstacle(direction, wallSegment);
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setWall_InSingleWithSameWallAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighbors(null); // !!!
        cell.setNeighborObstacle(direction, wallSegment);
        assertFalse(cell.setNeighborObstacle(Direction.SOUTH, wallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setWall_InSingleWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighbors(null); // !!!
        cell.setNeighborObstacle(direction, wallSegment);
        assertFalse(cell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Override @Test
    void setWall_inNeighborCells() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null); // !!!
        cell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        cell.setNeighborObstacle(direction, wallSegment);
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell, wallSegment.getPosition().getNeighborCell(direction));
    }

    @Override @Test
    void setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighborObstacle(direction, wallSegment);

        assertFalse(cell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell, wallSegment.getPosition().getNeighborCell(direction));
    }

    @Override @Test
    void setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        Direction anotherDirection = direction.getOppositeDirection();

        cell.setNeighborObstacle(direction, wallSegment);

        assertFalse(cell.setNeighborObstacle(anotherDirection, wallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell, wallSegment.getPosition().getNeighborCell(direction));
    }

    @Override @Test
    void neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(cell.getNeighborArea(direction));
    }
}
