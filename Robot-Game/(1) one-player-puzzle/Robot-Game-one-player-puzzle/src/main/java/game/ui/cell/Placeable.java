package game.ui.cell;

/**
 * Интерфейс объекта, который может быть размещен
 * в определенной зоне {@link CellLayout}.
 */
public interface Placeable {

    /**
     * Получить зону размещения объекта.
     *
     * @return зона размещения.
     */
    CellLayout.Zone getZone();
}
