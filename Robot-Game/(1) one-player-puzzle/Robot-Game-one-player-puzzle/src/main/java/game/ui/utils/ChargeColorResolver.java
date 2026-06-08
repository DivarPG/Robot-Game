package game.ui.utils;

import java.awt.*;

/**
 * Утилита для определения цвета по уровню заряда
 */
public final class ChargeColorResolver {

    /**
     * Определить цвет, соответствующий уровню заряда
     *
     * @param charge текущий заряд
     * @param maxCharge максимальный заряд
     * @return цвет уровня заряда
     */
    public static Color resolve(int charge, int maxCharge) {

        double percent = (double) charge / maxCharge;

        if (percent >= 0.7) {
            return Color.GREEN;
        }

        if (percent >= 0.3) {
            return Color.YELLOW;
        }

        return Color.RED;
    }
}