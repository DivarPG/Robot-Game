package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Связь между ячейками через область: установка соседей по горизонтали, установка соседей по вертикали, корректное определение соседних ячеек по направлениям
 * Ориентация области: установка вертикальной ориентации при горизонтальной связи, установка горизонтальной ориентации при вертикальной связи
 * Ограничения установки соседей: запрет установки вертикальных соседей, если уже задана горизонтальная связь, запрет установки горизонтальных соседей, если уже задана вертикальная связь
 * Согласованность состояния: сохранение корректной ориентации после неудачной операции, частичное заполнение связей без нарушения уже установленных данных
 * Граничные случаи: отсутствие второго соседа при некорректной установке, корректная обработка конфликтующих операций
 */

class BetweenCellsAreaTest {

    private AbstractCell leftCell;
    private AbstractCell rightCell;
    private AbstractCell topCell;
    private AbstractCell bottomCell;

    @BeforeEach
    public void testSetup() {
        leftCell = new NormalCell();
        rightCell = new NormalCell();
        topCell = new NormalCell();
        bottomCell = new NormalCell();
    }

    @Test
    public void test_setHorizontalNeighbors() {
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea();

        boolean result = betweenCellsArea.setHorizontalNeighbors(leftCell, rightCell);

        assertTrue(result);
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(leftCell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertEquals(rightCell, betweenCellsArea.getNeighborCell(Direction.EAST));
        assertSame(betweenCellsArea, leftCell.getNeighborArea(Direction.EAST));
        assertSame(betweenCellsArea, rightCell.getNeighborArea(Direction.WEST));
    }

    @Test
    public void test_setVerticalNeighbors() {
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea();

        boolean result = betweenCellsArea.setVerticalNeighbors(topCell, bottomCell);

        assertTrue(result);
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(topCell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertEquals(bottomCell, betweenCellsArea.getNeighborCell(Direction.SOUTH));
        assertSame(betweenCellsArea, topCell.getNeighborArea(Direction.SOUTH));
        assertSame(betweenCellsArea, bottomCell.getNeighborArea(Direction.NORTH));
    }

    @Test
    public void test_setHorizontalNeighbors_alreadyHasVerticalNeighbors() {
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea();
        betweenCellsArea.setVerticalNeighbors(topCell, bottomCell);

        boolean result = betweenCellsArea.setHorizontalNeighbors(leftCell, rightCell);

        assertFalse(result);
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(topCell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertEquals(bottomCell, betweenCellsArea.getNeighborCell(Direction.SOUTH));
        assertNull(betweenCellsArea.getNeighborCell(Direction.WEST));
        assertNull(betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors_alreadyHasHorizontalNeighbors() {
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea();
        betweenCellsArea.setHorizontalNeighbors(leftCell, rightCell);

        boolean result = betweenCellsArea.setVerticalNeighbors(topCell, bottomCell);

        assertFalse(result);
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(leftCell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertEquals(rightCell, betweenCellsArea.getNeighborCell(Direction.EAST));
        assertNull(betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertNull(betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }
}
