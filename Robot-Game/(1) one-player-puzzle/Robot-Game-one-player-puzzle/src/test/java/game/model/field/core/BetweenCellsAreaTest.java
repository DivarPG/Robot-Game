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

    private AbstractCell abstractCell;
    private AbstractCell neighborAbstractCell;

    @BeforeEach
    public void testSetup() {
        abstractCell = new NormalCell();
        abstractCell.setNeighbors(null);

        neighborAbstractCell = new NormalCell();
    }

    // отсутствует предварительная установка соседей что делает проверки бессмысленными
    @Test
    public void test_setHorizontalNeighbors() { // отдельно выделить act
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.EAST);

        assertTrue(betweenCellsArea.setHorizontalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors() { // отдельно выделить act
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.SOUTH);

        assertTrue(betweenCellsArea.setVerticalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }

    @Test
    public void test_setHorizontalNeighbors_alreadyHasVerticalNeighbors() {  // отдельно выделить act
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.EAST);

        assertFalse(betweenCellsArea.setVerticalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertNull(betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors_alreadyHasHorizontalNeighbors() { // отдельно выделить act
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.SOUTH);

        assertFalse(betweenCellsArea.setHorizontalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertNull(betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }
}