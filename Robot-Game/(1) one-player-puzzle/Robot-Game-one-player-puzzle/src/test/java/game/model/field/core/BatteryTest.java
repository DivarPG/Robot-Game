package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Заряд батареи: создание батареи с начальным зарядом, корректное получение текущего заряда
 * Расход заряда: уменьшение заряда при списании меньшего количества, полное обнуление заряда при списании всего доступного, запрет списания заряда больше доступного
 * Ограничения при расходе заряда: невозможность списания заряда, если запрашиваемое количество превышает текущий заряд
 * Размещение батареи: возможность размещения батареи в допустимой ячейке, запрет размещения в недопустимой ячейке
 * Ограничения размещения (связь с объектами): запрет размещения батареи, если она уже связана с другим объектом
 * Ограничения размещения (занятость ячейки): запрет размещения батареи в ячейке, где уже есть другая батарея, запрет повторного размещения батареи в той же ячейке
 * Тип ячейки: запрет размещения батареи в неподходящем типе ячейки
 * Граничные случаи: попытка списания заряда больше доступного, попытка размещения батареи в некорректных условиях
 */

/*
совсем никак не покрываются disconnect(), destroy()
Нет тестов на исключения: при вызове getCharge() или drainCharge() на уничтоженной батарее должно выбрасываться RuntimeException.
Нет тестов на корректность работы с позицией (методы setPosition и unsetPosition унаследованы от CellObject, но не проверяются).
*/

class BatteryTest {

    private static final int DEFAULT_TEST_BATTERY_CHARGE = 10;

    private Battery battery;

    @BeforeEach
    public void testSetup() {
        battery = new Battery();
        Robot robot = new Robot(battery);
    }

    @Test
    public void test_Battery_createAndGetCharge() {
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeAmountLessCharge() {
        int chargeAmount = 5;
        assertTrue(battery.drainCharge(chargeAmount));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - chargeAmount, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeEqualsCharge() { // хороший, но drainCharge хочется в result как в test_canLocateAtPosition_isConnected
        int chargeAmount = DEFAULT_TEST_BATTERY_CHARGE;
        assertTrue(battery.drainCharge(chargeAmount));
        assertEquals(0, battery.getCharge());
    }

    @Test
    public void test_canLocateAtPosition_isConnected() { // хороший
        NormalCell cellWithPowerSupply = new NormalCell();

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_disconnected() { // хороший
        NormalCell cellWithPowerSupply = new NormalCell();
        Battery battery = new Battery();

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertTrue(result);
    }


    @Test
    public void test_canLocateAtPosition_inCellWithBattery() { // хороший
        Battery anotherBattery = new Battery();
        NormalCell cellWithPowerSupply = new NormalCell();
        cellWithPowerSupply.setSmallObject(anotherBattery);

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_alreadyHavePosition() { // хороший
        NormalCell cellWithPowerSupply = new NormalCell();
        cellWithPowerSupply.setSmallObject(battery);

        boolean result = battery.canSetPosition(cellWithPowerSupply);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_inNotCellWithPowerSupply() { // хороший
        AbstractCell cell = new NormalCell();

        boolean result = battery.canSetPosition(cell);

        assertFalse(result);
    }

    @Test
    public void test_releaseCharge_whenChargeAmountMoreThanCharge() {  // хороший
        int chargeAmount = 11;
        assertFalse(battery.drainCharge(chargeAmount));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }
}