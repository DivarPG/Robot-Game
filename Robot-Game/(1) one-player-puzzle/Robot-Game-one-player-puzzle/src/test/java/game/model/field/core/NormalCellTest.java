package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Малый объект в ячейке: установка батареи в пустую ячейку, запрет установки второй батареи в занятую ячейку
 * Ограничения размещения батареи: запрет установки батареи, если она уже размещена в другой ячейке
 * Извлечение батареи: корректное извлечение батареи из ячейки, очистка ячейки после извлечения
 * Позиция батареи: после извлечения батареи её позиция сбрасывается в null
 * Граничные случаи: попытка извлечения батареи из пустой ячейки
 */

public class NormalCellTest { // не хватает наследования

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
}
