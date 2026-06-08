package game.ui.cell;

import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.model.field.core.Direction;
import game.model.field.core.Robot;
import game.ui.resource.audio.SoundResource;
import game.ui.utils.ChargeColorResolver;
import game.ui.resource.image.ImageResource;
import game.ui.utils.ImageScaler;
import game.ui.resource.ResourceProvider;
import game.ui.resource.audio.SoundPlayer;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 * Виджет робота.
 *
 * @see Robot
 */
public class RobotWidget extends CellItemWidget {
    // можно добавить вынести класс для состояний робота - а этот бы только запршибал  какое и отрисовывал
    // иначе респонсибилити разъезжается немного

    /**
     * Визуальное состояние робота.
     */
    private enum State{
        IDLE,
        TELEPORTED,
        PICK_BATTERY
    }

    /**
     * Робот.
     */
    private final Robot robot;

    /**
     * Текущее визуальное состояние робота.
     */
    private State state = State.IDLE;

    /**
     * Угол поворота анимации телепортации.
     */
    private int rotationAngle = 0;

    /**
     * Таймер текущей анимации.
     */
    private Timer animationTimer;

    /**
     * Размер элемента.
     */
    private static final Dimension SIZE = new Dimension(120, 120);

    /**
     * Поставщик изображений
     */
    private final  ResourceProvider<BufferedImage,ImageResource> imageProvider;

    /**
     * Конструктор.
     *
     * @param robot модель робота.
     * @param imageProvider поставщик изображений.
     * @param soundPlayer проигрыватель звуковых эффектов.
     */
    public RobotWidget(Robot robot, ResourceProvider<BufferedImage,ImageResource> imageProvider, SoundPlayer soundPlayer) {
        super();
        this.robot = robot;

        this.imageProvider = imageProvider;
        setMouseTransparent(false);

        setState(State.IDLE);

        robot.addRobotActionListener(new RobotActionListener() {
            @Override
            public void robotIsMoved(@NotNull RobotActionEvent event) {
                setState(State.IDLE);
                soundPlayer.playSound(SoundResource.MOVE);
            }

            @Override
            public void robotChangedBattery(@NotNull RobotActionEvent event) {
                setState( State.PICK_BATTERY);
                soundPlayer.playSound(SoundResource.PICK_BATTERY);
            }

        });

        setFocusable(true);
        addKeyListener(new KeyController());

//        setOpaque(true);
//        setBackground(Color.BLACK);
    }

    @Override
    public int getZIndex() {
        return 100;
    }

    @Override
    public CellLayout.Zone getZone() {
        return CellLayout.Zone.PRIMARY;
    }

    @Override
    protected void draw(Graphics g) {
        switch (state){
            case IDLE -> drawForType(g, ImageResource.ROBOT);
            case PICK_BATTERY -> drawForType(g, ImageResource.ROBOT_PICK_BATTERY);
            case TELEPORTED -> drawTeleportedState(g); // не вызывается - в модели нет события на телепортацию робота у слуштелей робота
            // можно запускать через получение ивента через поле или через сам exitWidget (не оч слишком большое знание о других чуваках)
            // или можно сделать открытый метод на смену состояния (на сост телепортация) и вызывать его в поле в оброботчике события телепортации
        }
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

    protected void drawForType(Graphics g, ImageResource imageType){
        BufferedImage img = getImage(imageType);

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

    protected void drawTeleportedState(Graphics g){
        BufferedImage img = getImage(ImageResource.ROBOT);

        int x = (getWidth() - img.getWidth()) / 2;
        int y = (getHeight() - img.getHeight()) / 2;

        Graphics2D g2 = (Graphics2D) g.create();

        int centerX = x + img.getWidth() / 2;
        int centerY = y + img.getHeight() / 2;

        g2.rotate(
                Math.toRadians(rotationAngle),
                centerX,
                centerY
        );

        g2.drawImage(
                img,
                x,
                y,
                null
        );

        g2.dispose();
    }


    private BufferedImage getImage(ImageResource imageType) {
        BufferedImage original = imageProvider.get(imageType);
        return ImageScaler.resize(original, 110, 110);
    }

    /**
     * Изменить визуальное состояние робота.
     * При необходимости запускает соответствующую анимацию.
     *
     * @param newState новое состояние.
     */
    private void setState(State newState) {

        if (animationTimer != null) {
            animationTimer.stop();
        }

        state = newState;

        switch (newState) {

            case IDLE -> {
                rotationAngle = 0;
            }

            case PICK_BATTERY -> {

                animationTimer = new Timer(400, e -> {
                    setState(State.IDLE);
                });

                animationTimer.setRepeats(false);
                animationTimer.start();
            }

            case TELEPORTED -> {

                rotationAngle = 0;

                animationTimer = new Timer(16, e -> {

                    rotationAngle += 15;

                    repaint();

                    if (rotationAngle >= 720) {

                        //((Timer)e.getSource()).stop();

                        setState(State.IDLE);
                    }
                });

                animationTimer.start();
            }
        }

        repaint();
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
