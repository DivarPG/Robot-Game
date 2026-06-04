package game.ui.utils;

import java.awt.*;

public final class ChargeColorResolver {

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