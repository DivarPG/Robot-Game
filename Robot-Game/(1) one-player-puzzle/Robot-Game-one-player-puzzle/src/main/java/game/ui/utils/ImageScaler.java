package game.ui.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Утилита для масштабирования изображений
 */
public final class ImageScaler {

    /**
     * Изменить размер изображения
     *
     * @param img исходное изображение
     * @param width новая ширина
     * @param height новая высота
     * @return масштабированное изображение
     */
    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmpImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(tmpImg, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
}
