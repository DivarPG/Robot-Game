package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/* Аспекты тестирования
 * Сравнение стенок без позиции: различие между двумя стенками без установленной позиции
 * Сравнение стенок с позицией: различие между стенками, установленными в разных позициях
 * Сравнение стенки с позицией и без позиции: различие между стенкой, привязанной к ячейке, и стенкой без позиции
 * Позиция стенки: учет позиции стенки при сравнении объектов
 * Граничные случаи: сравнение стенок в различных состояниях (с позицией и без неё)
 */

public class WallSegmentTest {

    private AbstractCell abstractCell1;
    private AbstractCell neighbourAbstractCell1;
    private WallSegment wallSegment1;

    private AbstractCell abstractCell2;
    private AbstractCell neighbourAbstractCell2;
    private WallSegment wallSegment2;

    @BeforeEach
    public void testSetup() {
        abstractCell1 = new NormalCell();
        neighbourAbstractCell1 = new NormalCell();
        wallSegment1 = new WallSegment();
        Map<Direction, AbstractCell> map1 = new HashMap<>();
        map1.put(Direction.WEST, neighbourAbstractCell1);
        neighbourAbstractCell1.setNeighbors(null);
        abstractCell1.setNeighbors(map1);


        abstractCell2 = new NormalCell();
        neighbourAbstractCell2 = new NormalCell();
        wallSegment2 = new WallSegment();
        Map<Direction, AbstractCell> map2 = new HashMap<>();
        map2.put(Direction.WEST, neighbourAbstractCell2);
        neighbourAbstractCell2.setNeighbors(null);
        abstractCell2.setNeighbors(map2);
    }

    @Test
    public void test_equalsForWallsWithoutPosition() {
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithDifferentPosition() {
        abstractCell1.setNeighborObstacle(Direction.WEST, wallSegment1);
        abstractCell2.setNeighborObstacle(Direction.WEST, wallSegment2);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithPositionAndWallWithoutPosition() {
        abstractCell1.setNeighborObstacle(Direction.WEST, wallSegment1);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }
}
