package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Большой объект в ячейке: установка робота в пустую ячейку, извлечение робота из ячейки, запрет установки второго робота в занятую ячейку, запрет повторной установки того же робота
 * Позиция большого объекта: после установки робота его позиция становится текущей ячейкой, после извлечения робота его позиция сбрасывается в null
 * Связь между соседними ячейками: установка соседа в одном направлении, установление двусторонней связи между ячейками, корректное получение соседа по направлению, отсутствие соседа при его неустановленности
 * Ограничения при задании соседей: запрет назначения двух разных соседей в одном направлении, запрет добавления одной и той же ячейки в разные направления при конфликте, запрет назначения ячейки самой себе соседом
 * Установка препятствий между ячейками: установка стенки в заданном направлении, корректная привязка препятствия к текущей ячейке, корректная привязка препятствия к соседней ячейке при её наличии
 * Ограничения на препятствия: запрет установки нескольких стенок в одном направлении, запрет повторного использования одной и той же стенки в разных направлениях при конфликте
 * Согласованность состояния: одинаковое представление препятствий с обеих сторон, сохранение согласованных связей между ячейками и препятствиями
 * Граничные случаи: отсутствие соседа, отсутствие препятствия, корректная обработка некорректных операций
 */

class AbstractCellTest {

    private AbstractCell cell;

    public AbstractCellTest() { // зачем конструктор
    }


    @BeforeEach
    public void testSetup() {

        cell = new NormalCell();
    }

    //region Хорошие тесты
    @Test
    public void test_setRobot_InEmptyCell() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_takeRobot_FromCellWithRobot() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertEquals(robot, cell.takeBigObject());
        assertNull(robot.getPosition());
        assertNull(cell.getBigObject());
    }

    @Test
    public void test_setRobot_ToCellWithRobot() {
        Robot robot = new Robot(new Battery());
        Robot newRobot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertFalse(cell.setBigObject(newRobot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }

    @Test
    public void test_setRobot_ToCellAgain() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertFalse(cell.setBigObject(robot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }
    //endregion

    @Test
    public void test_setNeighborCell() {   // Проверка пакетного метода, а не публичного контракта, несколько блоков arrange Возможно стоит перенести в филд
        // Можно создать фабричный метод для создания ячейки по направлению, чтобы упростить блок arrange
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>(); // соседи в направлениях
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null); // по умолчанию и так нулл блок act должен быть атомарным
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }


    @Test
    public void test_setNeighborCell_doubleSided() { // Проверка пакетного метода, несколько блоков arrange
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null); // !!!
        map.put(direction, neighborCell);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        cell.setNeighbors(map);
        map2.put(direction.getOppositeDirection(), cell);

        assertTrue(neighborCell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_twoTimesInOneDirection() { // неправильная структура теста
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

    @Test
    public void test_setNeighborCell_alreadyNeighborWithAnotherDirection() { // !!!
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

    @Test
    public void test_setNeighborCell_setSelfAsNeighbor() { // фабричный метод
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, cell);

        assertFalse(cell.setNeighbors(map));
        assertNull(cell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellExists() { // !!!
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellNotExists() {
        assertNull(cell.getNeighborCell(Direction.NORTH));
    }

    @Test
    public void test_setWall_inOneSingleCell() { // хороший
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighbors(null); // !!!
        cell.setNeighborObstacle(direction, wallSegment);
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InSingleWithSameWallAndAnotherDirection() { // хороший
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighbors(null); // !!!
        cell.setNeighborObstacle(direction, wallSegment);
        assertFalse(cell.setNeighborObstacle(Direction.SOUTH, wallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InSingleWithSameDirectionAndAnotherWallSegment() { // хороший
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighbors(null); // !!!
        cell.setNeighborObstacle(direction, wallSegment);
        assertFalse(cell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_inNeighborCells() { // фабричный метод для соседей
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

    @Test
    public void test_setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment() { // фабричный метод для соседей
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

    @Test
    public void test_setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection() { // фабричный метод для соседей
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

    @Test
    public void test_neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(cell.getNeighborArea(direction));
    }
}