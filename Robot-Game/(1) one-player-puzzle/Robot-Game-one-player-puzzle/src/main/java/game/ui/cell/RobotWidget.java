package game.ui.cell;

import game.model.field.core.Direction;
import game.model.field.core.Robot;
import game.ui.utils.ChargeColorResolver;
import game.ui.resource.image.ImageResource;
import game.ui.utils.ImageScaler;
import game.ui.resource.ResourceProvider;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Виджет робота.
 *
 * @see Robot
 */
public class RobotWidget extends CellItemWidget {

    /**
     * Робот.
     */
    private final Robot robot;

    /**
     * Размер элемента.
     */
    private static final Dimension SIZE = new Dimension(120, 120);

    private final  ResourceProvider<BufferedImage,ImageResource> imageProvider;

    /**
     * Конструтор.
     *
     * @param robot робот.
     */
    public RobotWidget(Robot robot, ResourceProvider<BufferedImage,ImageResource> imageProvider) {
        super();
        this.robot = robot;

        this.imageProvider = imageProvider;
        setMouseTransparent(false);

        setFocusable(true);
        addKeyListener(new KeyController());

//        setOpaque(true);
//        setBackground(Color.BLACK);
    }


    private BufferedImage getImage( ) {
        BufferedImage original = imageProvider.get(getImageType());
        return ImageScaler.resize(original, 110, 110);
    }

    @Override
    protected void draw(Graphics g) {

        BufferedImage img = getImage();


        int cw = getWidth();
        int ch = getHeight();

        int iw = img.getWidth();
        int ih = img.getHeight();

        int x = (cw - iw) / 2;
        int y = (ch - ih) / 2;

        g.drawImage(
                img,
                x,
                y,
                iw,
                ih,
                null
        );
    }

    private ImageResource getImageType() {
        return ImageResource.ROBOT;
    }

    /**
     * Сделать виджет активным
     *
     * @param state состояние активности.
     */
    public void setActive(boolean state) {
        setFocusable(state);
        requestFocus();
        repaint();
    }

    @Override
    protected Dimension getDimension() {
        return SIZE;
    }

    /**
     * Получить текст заряда робота.
     *
     * @return текст заряда робота.
     */
    private String robotChargeText() {
//        return robot.getCharge() + "/" + robot.getChargeCapacity();
        return String.valueOf(robot.getCharge());
    }

    /**
     * Получить цвет текста заряда.
     *
     * @return цвет текста заряда.
     */
    private Color robotChargeTextColor() {
        return ChargeColorResolver.resolve(robot.getCharge(),robot.getChargeCapacity());
    }

    @Override
    public int getZIndex() {
        return 100;
    }

    @Override
    protected void drawOverlay(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(new Font("Arial", Font.BOLD, 20));

        String text = robotChargeText();

        FontMetrics metrics = g2.getFontMetrics();

        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = getHeight() / 2;

        g2.setColor(robotChargeTextColor());

        g2.drawString(text, x, y+5);
    }

    @Override
    public CellLayout.Zone getZone() {
        return CellLayout.Zone.PRIMARY;
    }

    /**
     * Внутренний класс-обработчик событий. Придает специфическое поведение виджету.
     */
    private class KeyController extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent ke) {
            int keyCode = ke.getKeyCode();

            moveAction(keyCode);
            changeBatteryAction(keyCode);

            repaint();
        }

        private void changeBatteryAction(int keyCode) {
            if (keyCode == KeyEvent.VK_F) {
                boolean success = robot.changeBattery();
                if (!success) System.out.println("Can't take battery");
            }
        }

        private void moveAction(int keyCode) {
            Direction direction = directionByKeyCode(keyCode);
            if (direction != null) {
                System.out.println("Go to " + direction);
                boolean success = robot.move(direction);
                if (!success) System.out.println("Can't move " + direction);

            }
        }

        private Direction directionByKeyCode(int keyCode) {
            return switch (keyCode) {
                case KeyEvent.VK_W -> Direction.NORTH;
                case KeyEvent.VK_S -> Direction.SOUTH;
                case KeyEvent.VK_A -> Direction.WEST;
                case KeyEvent.VK_D -> Direction.EAST;
                default -> null;
            };
        }
    }
}
