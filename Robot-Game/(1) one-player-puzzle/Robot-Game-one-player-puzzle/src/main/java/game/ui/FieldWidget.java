package game.ui;

import game.model.events.*;
import game.model.field.core.*;
import game.model.field.core.Point;
import game.model.field.core.Robot;
import org.jetbrains.annotations.NotNull;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.cell.*;
import javax.swing.*;
import java.awt.*;

/**
 * Основной виджет игрового поля
 * Отвечает за построение UI представления модели {@link Field}
 * и синхронизацию изменений модели с визуальным слоем
 *
 * Поле состоит из строк клеток и промежуточных (межклеточных) зон между ними
 */
public class FieldWidget extends JPanel {

    /**
     * Игровое поле (модель)
     */
    private final Field field;

    /**
     * Фабрика виджетов
     */
    private final WidgetFactory widgetFactory;

    /**
     * Конструктор
     *
     * @param field игровое поле (модель)
     * @param widgetFactory фабрика UI-виджетов для элементов поля
     */
    public FieldWidget(@NotNull Field field, @NotNull  WidgetFactory widgetFactory) {
        this.field = field;
        this.widgetFactory = widgetFactory;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        fillField();
        subscribeOnRobots();
        field.addFieldActionListener(new FieldController());
    }

    /**
     * Построить визуальное представление всего игрового поля
     *
     * Создает:
     * - строки клеток
     * - промежуточные зоны между клетками
     * - верхние и нижние границы (стены)
     */
    private void fillField() {

        if(field.getHeight() > 0) {

            JPanel startRowWalls = createRowWalls(0, Direction.NORTH);

            add(startRowWalls);
        }

        for (int i = 0; i < field.getHeight(); ++i) {

            JPanel row = createRow(i);

            add(row);
            JPanel rowWalls = createRowWalls(i, Direction.SOUTH);
            add(rowWalls);

        }
    }


    /**
     * Создать визуальную строку клеток поля
     *
     * Включает:
     * - клетки {@link Cell}
     * - боковые и межклеточные зоны {@link BetweenCellsWidget}
     *
     * @param rowIndex индекс строки
     * @return панель строки
     */
    private JPanel createRow(int rowIndex) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));


        for(int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);
            CellWidget cellWidget = widgetFactory.create(cell);


            if(i == 0)  {
                BetweenCellsWidget westCellWidget = widgetFactory.create(cell.getNeighborArea(Direction.WEST));

                westCellWidget.setAlignmentY(Component.CENTER_ALIGNMENT);

                row.add(westCellWidget);

            }


            cellWidget.setAlignmentY(Component.CENTER_ALIGNMENT);

            row.add(cellWidget);


            BetweenCellsWidget eastCellWidget = widgetFactory.create(cell.getNeighborArea(Direction.EAST));

            eastCellWidget.setAlignmentY(Component.CENTER_ALIGNMENT);

            row.add(eastCellWidget);

        }
        return row;
    }

    /**
     * Создать горизонтальный ряд межклеточных зон (стен)
     *
     * Используется для верхней и нижней границы поля
     *
     * @param rowIndex индекс строки
     * @param direction направление стен (NORTH или SOUTH)
     * @return панель с wall-зонами
     *
     * @throws IllegalArgumentException если direction не NORTH/SOUTH
     */
    private JPanel createRowWalls(int rowIndex, Direction direction) {

        if(direction == Direction.EAST || direction == Direction.WEST) throw new IllegalArgumentException();

        JPanel row = new JPanel();

        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));


        for(int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);

            BetweenCellsWidget betweenCellWidget = widgetFactory.create(cell.getNeighborArea(direction));

            row.add(betweenCellWidget);

        }

        return row;
    }

    /**
     * Подписать UI на события робота из модели поля
     */
    private void subscribeOnRobots() {
        Robot robot = field.getRobot();
        robot.addRobotActionListener(new RobotController());
    }

    /**
     * Обработчик событий робота
     */
    private class RobotController implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            CellItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
            CellWidget from = widgetFactory.getWidget(event.getFromCell());
            CellWidget to = widgetFactory.getWidget(event.getToCell());
            from.removeItem(robotWidget);
            if (!event.getRobot().isTeleported()) {
                to.addItem(robotWidget);
            }
            robotWidget.requestFocus();
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            CellWidget cellWidget = widgetFactory.getWidget(robot.getPosition());
            CellItemWidget batteryWidget = widgetFactory.getWidget(event.getBattery());
            cellWidget.removeItem(batteryWidget);
            widgetFactory.remove(event.getBattery());
        }
    }

    /**
     * Обработчик событий поля
     */
    private class FieldController implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            Robot robot = event.getRobot();
            Cell teleport = event.getTeleport().getPosition();
            CellWidget teleportWidget = widgetFactory.getWidget(teleport);
            CellItemWidget robotWidget = widgetFactory.getWidget(robot);
            teleportWidget.removeItem(robotWidget);
        }
    }

}
