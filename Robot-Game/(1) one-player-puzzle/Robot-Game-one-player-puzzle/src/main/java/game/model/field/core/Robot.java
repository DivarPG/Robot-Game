package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;

import java.util.ArrayList;
import java.util.Set;

/**
 * Робот.
 */
public class Robot extends CellObject {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор.
     *
     * @param battery внутренний источник питания.
     */
    public Robot(@NotNull Battery battery) {
        setBattery(battery);
    }

    //endregion

    //region ПЕРЕМЕЩЕНИЕ

    /**
     * Переместить объект в заданном направлении.
     *
     * @param direction направление.
     */
    public boolean move(@NotNull Direction direction) {
        if (!isCapable()) {
            return false;
        }

        if (getPosition().getNeighborObstacle(direction) != null) {
            System.out.println("Wall");
            return false;
        }

        Cell newPosition = getPosition().getNeighborCell(direction);

        if (newPosition == null || !newPosition.canSetObject(Robot.class)) {
            return false;
        }

        battery.drainCharge(AMOUNT_OF_CHARGE_FOR_MOVE);

        Cell oldPosition = getPosition();

        oldPosition.takeObject(Robot.class);

        boolean success = newPosition.setObject(this);

        if (!success) {
            throw new RuntimeException("Robot can't move to the " + newPosition);
        }

        fireRobotIsMoved(oldPosition, newPosition);

        return true;
    }

    @Override
    protected boolean canChangePosition(@NotNull Cell newPosition) {
        return getPosition() == null;
    }

    @Override
    boolean canCoexistWith(Class<? extends CellObject> type) {
        return type != Robot.class;
    }

    /**
     * Получить дееспособность робота
     *
     * @return дееспособен ли робот
     */
    public boolean isCapable() {
        Cell cell = getPosition();
        return cell.getObject(Robot.class) != null && !isTeleported() && hasEnoughCharge();
    }

    //endregion

    //region БАТАРЕЙКА

    /**
     * Количество заряда для перемещения.
     */
    private static final int AMOUNT_OF_CHARGE_FOR_MOVE = 1;

    /**
     * Внутренний источник питания робота.
     */
    private Battery battery;

    /**
     * Установить источник питания {@link Robot#battery}
     *
     * @param battery источник питания.
     */
    boolean setBattery(@NotNull Battery battery) {
        if (battery == this.battery) return true;

        if (this.battery != null) return false;

        this.battery = battery;

        boolean success = battery.connectTo(this);

        assert success;
        if (!success) {
            this.battery = null;
            return false;
        }

        return true;
    }

    /**
     * Изъять источник питания {@link Robot#battery}
     *
     * @return успешность изъятия
     */
    boolean unsetBattery() {
        if (this.battery == null) return true;

        Battery battery = this.battery;
        this.battery = null;

        boolean success = battery.disconnect();

        assert success : "Disconnect failed";
        if (!success) {
            this.battery = battery;
            return false;
        }

        return true;
    }

    /**
     * Заменить источник питания {@link Robot#battery}.
     */
    public boolean changeBattery() {
        if (getPosition().getObject(ExitPoint.class) != null) {
            return false;
        }

        Battery battery = (Battery) getPosition().takeObject(Battery.class);

        if (battery == null) {
            return false;
        }

        Battery oldBattery = this.battery;
        unsetBattery();

        boolean success = setBattery(battery);

        if (!success) {
            setBattery(oldBattery);
            return false;
        }

        oldBattery.destroy();

        fireRobotChangeBattery(battery);

        return true;
    }

    /**
     * Проверить наличие достаточного для перемещения количества заряда {@link Robot#battery}.
     */
    private boolean hasEnoughCharge() {
        return getCharge() >= AMOUNT_OF_CHARGE_FOR_MOVE;
    }

    //region ЁМКОСТЬ

    /**
     * Получить заряд {@link Battery#getCharge()}.
     *
     * @return заряд.
     */
    public int getCharge() {
        return battery.getCharge();
    }

    /**
     * Получить емкость заряда {@link Battery#getCapacity()}.
     *
     * @return емкость заряда.
     */
    public int getChargeCapacity() {
        return battery.getCapacity();
    }

    //endregion

    //endregion

    //region ТЕЛЕПОРТАЦИЯ

    /**
     * Робот телепортирован.
     */
    private boolean isTeleported = false;

    /**
     * Считать, что робот телепортирован.
     */
    void setTeleported() {
        isTeleported = true;
    }

    /**
     * Телепортирован ли робот
     *
     * @return телепортирован ли робот
     */
    public boolean isTeleported() {
        return isTeleported;
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события игры.
     */
    private final ArrayList<RobotActionListener> robotListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void addRobotActionListener(RobotActionListener listener) {
        robotListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void removeRobotActionListener(RobotActionListener listener) {
        robotListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что робот переместился.
     *
     * @param oldPosition ячейка откуда переместился робот.
     * @param newPosition ячейка куда переместился робот.
     */
    private void fireRobotIsMoved(@NotNull Cell oldPosition, @NotNull Cell newPosition) {
        RobotActionEvent event = new RobotActionEvent(this);
        event.setRobot(this);
        event.setFromCell(oldPosition);
        event.setToCell(newPosition);

        for (RobotActionListener listener : robotListListener) {
            listener.robotIsMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что робот сменил источник питания.
     *
     * @param battery новый источник питания.
     */
    private void fireRobotChangeBattery(Battery battery) {
        RobotActionEvent event = new RobotActionEvent(this);
        event.setRobot(this);
        event.setBattery(battery);

        for (RobotActionListener listener : robotListListener) {
            listener.robotChangedBattery(event);
        }
    }

    //endregion
}