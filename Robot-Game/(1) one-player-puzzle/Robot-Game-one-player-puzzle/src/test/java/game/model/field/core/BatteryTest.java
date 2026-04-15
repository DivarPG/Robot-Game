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

class BatteryTest {

    private static final int DEFAULT_TEST_BATTERY_CHARGE = 10;

    private Battery battery;
    private Robot robot;

    @BeforeEach
    public void testSetup() {
        battery = new Battery();
        robot = new Robot(battery);
    }

    @Test
    public void test_Battery_createAndGetCharge() {
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeAmountLessCharge() {
        int chargeAmount = 5;

        boolean result = battery.drainCharge(chargeAmount);

        assertTrue(result);
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - chargeAmount, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeEqualsCharge() {
        int chargeAmount = DEFAULT_TEST_BATTERY_CHARGE;

        boolean result = battery.drainCharge(chargeAmount);

        assertTrue(result);
        assertEquals(0, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenChargeAmountMoreThanCharge() {
        int chargeAmount = DEFAULT_TEST_BATTERY_CHARGE + 1;

        boolean result = battery.drainCharge(chargeAmount);

        assertFalse(result);
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, battery.getCharge());
    }

    @Test
    public void test_releaseCharge_whenDisconnected_throwsRuntimeException() {
        battery.disconnect();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> battery.drainCharge(1));

        assertEquals("Not connected to user", exception.getMessage());
    }

    @Test
    public void test_canLocateAtPosition_isConnected() {
        NormalCell normalCell = new NormalCell();

        boolean result = battery.canSetPosition(normalCell);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_disconnected() {
        NormalCell normalCell = new NormalCell();
        battery.disconnect();

        boolean result = battery.canSetPosition(normalCell);

        assertTrue(result);
    }

    @Test
    public void test_canLocateAtPosition_inCellWithBattery() {
        Battery anotherBattery = new Battery();
        NormalCell normalCell = new NormalCell();
        normalCell.setSmallObject(anotherBattery);
        battery.disconnect();

        boolean result = battery.canSetPosition(normalCell);

        assertTrue(result);
    }

    @Test
    public void test_canLocateAtPosition_alreadyHavePosition() {
        NormalCell normalCell = new NormalCell();
        battery.disconnect();
        normalCell.setSmallObject(battery);

        boolean result = battery.canSetPosition(normalCell);

        assertFalse(result);
    }

    @Test
    public void test_canLocateAtPosition_inNormalCell() {
        AbstractCell cell = new NormalCell();
        battery.disconnect();

        boolean result = battery.canSetPosition(cell);

        assertTrue(result);
    }

    @Test
    public void test_disconnect_connectedBattery_disconnectsFromRobot() {
        boolean result = battery.disconnect();

        assertTrue(result);
        assertFalse(battery.isConnected());
        assertTrue(robot.unsetBattery());
    }

    @Test
    public void test_setPosition_disconnectedBattery_setsPosition() {
        NormalCell normalCell = new NormalCell();
        battery.disconnect();

        boolean result = battery.setPosition(normalCell);

        assertTrue(result);
        assertEquals(normalCell, battery.getPosition());
    }

    @Test
    public void test_unsetPosition_batteryInCell_clearsPosition() {
        NormalCell normalCell = new NormalCell();
        battery.disconnect();
        normalCell.setSmallObject(battery);

        battery.unsetPosition();

        assertNull(battery.getPosition());
    }

    @Test
    public void test_destroy_batteryBecomesDestroyedAndDisconnected() {
        battery.destroy();

        assertTrue(battery.isDestroy());
        assertThrows(RuntimeException.class, battery::isConnected);
        assertTrue(robot.unsetBattery());
    }

    @Test
    public void test_getCharge_destroyedBattery_throwsRuntimeException() {
        battery.destroy();

        RuntimeException exception = assertThrows(RuntimeException.class, battery::getCharge);

        assertEquals("Battery is destroyed", exception.getMessage());
    }

    @Test
    public void test_drainCharge_destroyedBattery_throwsRuntimeException() {
        battery.destroy();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> battery.drainCharge(1));

        assertEquals("Battery is destroyed", exception.getMessage());
    }
}
