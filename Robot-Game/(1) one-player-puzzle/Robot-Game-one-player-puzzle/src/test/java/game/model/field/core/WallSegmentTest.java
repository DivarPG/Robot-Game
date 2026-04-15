package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Сравнение стенок без позиции: различие между двумя стенками без установленной позиции
 * Сравнение стенок с позицией: различие между стенками, установленными в разных позициях
 * Сравнение стенки с позицией и без позиции: различие между стенкой, привязанной к ячейке, и стенкой без позиции
 * Позиция стенки: учет позиции стенки при сравнении объектов
 * Граничные случаи: сравнение стенок в различных состояниях (с позицией и без неё)
 */

public class WallSegmentTest {

    private AbstractCell cell1;
    private AbstractCell neighborCell1;
    private AbstractCell cell2;
    private AbstractCell neighborCell2;
    private WallSegment wallSegment1;
    private WallSegment wallSegment2;

    @BeforeEach
    public void testSetup() {
        cell1 = new NormalCell();
        neighborCell1 = new NormalCell();
        cell2 = new NormalCell();
        neighborCell2 = new NormalCell();
        wallSegment1 = new WallSegment();
        wallSegment2 = new WallSegment();

        Map<Direction, AbstractCell> neighbors1 = new HashMap<>();
        neighbors1.put(Direction.WEST, neighborCell1);
        neighborCell1.setNeighbors(null);
        cell1.setNeighbors(neighbors1);

        Map<Direction, AbstractCell> neighbors2 = new HashMap<>();
        neighbors2.put(Direction.WEST, neighborCell2);
        neighborCell2.setNeighbors(null);
        cell2.setNeighbors(neighbors2);
    }

    @Test
    public void test_equalsForWallsWithoutPosition() {
        assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithDifferentPosition() {
        cell1.setNeighborObstacle(Direction.WEST, wallSegment1);
        cell2.setNeighborObstacle(Direction.WEST, wallSegment2);

        assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithPositionAndWallWithoutPosition() {
        cell1.setNeighborObstacle(Direction.WEST, wallSegment1);

        assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallsWithSamePosition() {
        cell1.setNeighborObstacle(Direction.WEST, wallSegment1);

        assertEquals(wallSegment1, cell1.getNeighborObstacle(Direction.WEST));
        assertEquals(wallSegment1, neighborCell1.getNeighborObstacle(Direction.EAST));
    }

    @Test
    public void test_setWallSegment_sameDirectionAndSameSegment_returnsTrue() {
        boolean firstResult = cell1.setNeighborObstacle(Direction.WEST, wallSegment1);
        boolean secondResult = cell1.setNeighborObstacle(Direction.WEST, wallSegment1);

        assertTrue(firstResult);
        assertTrue(secondResult);
        assertSame(wallSegment1, cell1.getNeighborObstacle(Direction.WEST));
    }

    @Test
    public void test_setWallSegment_anotherDirectionAfterFirst_returnsFalse() {
        cell1.setNeighborObstacle(Direction.WEST, wallSegment1);

        boolean result = cell1.setNeighborObstacle(Direction.EAST, wallSegment1);

        assertFalse(result);
        assertSame(wallSegment1, cell1.getNeighborObstacle(Direction.WEST));
        assertNull(cell1.getNeighborObstacle(Direction.EAST));
    }
}
