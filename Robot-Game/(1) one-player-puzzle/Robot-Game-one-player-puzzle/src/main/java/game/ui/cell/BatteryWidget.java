package game.ui.cell;

import game.model.field.core.Battery;
import game.ui.resource.*;
import game.ui.resource.audio.SoundResource;
import game.ui.resource.image.ImageResource;
import game.ui.utils.ChargeColorResolver;
import game.ui.utils.ImageScaler;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет батареи.
 *
 * @see Battery
 */
public class BatteryWidget extends CellItemWidget {

    /**
     * Источник питания.
     */
    protected final Battery battery;

    /**
     * Размер элемента.
     */
    private static final Dimension SIZE = new Dimension(90, 90);

    /**
     * Конструктор.
     *
     * @param battery источник питания.
     */
    public BatteryWidget(@NotNull Battery battery, @NotNull ResourceProvider<BufferedImage, ImageResource> imageProvider) {

        super(imageProvider);

        this.battery = battery;

        setMouseTransparent(true);

        setToolTipText("Заряд: " + battery.getCharge() + "/" + battery.getCapacity());
    }


    @Override
    public int getZIndex() {
        return 10;
    }

    @Override
    public BufferedImage getImage(@NotNull ResourceProvider<BufferedImage,ImageResource> provider) {

        BufferedImage original = provider.get(getImageType());

        return ImageScaler.resize(original,120,100);
    }

    @Override
    public ImageResource getImageType() {
        double percent =
                (double) battery.getCharge()
                        / battery.getCapacity();

        if (percent > 0.66) {
            return ImageResource.BATTERY_FULL;
        }

        if (percent > 0.33) {
            return ImageResource.BATTERY_MEDIUM;
        }

        return ImageResource.BATTERY_LOW;
    }

    /**
     * Получить текст заряда источника питания.
     *
     * @return текст заряда источника питания.
     */
    private String powerSupplyChargeText() {
        return battery.getCharge() + "/" + battery.getCapacity();
    }

    /**
     * Получить цвет текста заряда источника питания.
     *
     * @return цвет текста заряда источника питания.
     */
    private Color powerSupplyChargeTextColor() {
        return ChargeColorResolver.resolve(battery.getCharge(),battery.getCapacity());
    }

    @Override
    protected void drawOverlay(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        String text = powerSupplyChargeText();

        Font font = new Font("Arial", Font.BOLD, 20);
        g2.setFont(font);

        FontMetrics metrics = g2.getFontMetrics();

        int x = (getWidth() - metrics.stringWidth(text)) / 2;

        int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

        Color color = powerSupplyChargeTextColor();

        g2.setColor(color.darker().darker().darker().darker().darker());

        g2.drawString(text, x - 2, y);
        g2.drawString(text, x + 2, y);
        g2.drawString(text, x, y - 2);
        g2.drawString(text, x, y + 2);

        g2.setColor(color);

        g2.drawString(text, x, y);
    }


    @Override
    protected Dimension getDimension() {
        return SIZE;
    }


    @Override
    public CellLayout.Zone getZone() {
        return CellLayout.Zone.SECONDARY;
    }
}
