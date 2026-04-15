package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/* Аспекты тестирования
 * Большой объект в ячейке: установка робота в пустую ячейку, извлечение робота из ячейки, запрет установки второго робота в занятую ячейку, запрет повторной установки того же робота
 * Позиция большого объекта: после установки робота его позиция становится текущей ячейкой, после извлечения робота его позиция сбрасывается в null
 * Связь между соседними ячейками: установка соседа в одном направлении, установление двусторонней связи между ячейками, корректное получение соседа по направлению, отсутствие соседа при его неустановленности
 * Ограничения при задании соседей: запрет назначения двух разных соседей в одном направлении, запрет добавления одной и той же ячейки в разные направления при конфликте, запрет назначения ячейки самой себе соседом
 * Установка препятствий между ячейками: установка стенки в заданном направлении, корректная привязка препятствия к текущей ячейке, корректная привязка препятствия к соседней ячейке при её наличии
 * Ограничения на препятствия: запрет установки нескольких стенок в одном направлении, запрет повторного использования одной и той же стенки в разных направлениях при конфликте
 * Согласованность состояния: одинаковое представление препятствий с обеих сторон, сохранение согласованных связей между ячейками и препятствиями
 * Граничные случаи: отсутствие соседа, отсутствие препятствия, корректная обработка некорректных операций
 */

abstract class AbstractCellTest {

    @Test
    abstract void setRobot_InEmptyCell();

    @Test
    abstract void takeRobot_FromCellWithRobot();

    @Test
    abstract void setRobot_ToCellWithRobot();

    @Test
    abstract void setRobot_ToCellAgain();

    @Test
    abstract void setNeighborCell();

    @Test
    abstract void setNeighborCell_doubleSided();

    @Test
    abstract void setNeighborCell_twoTimesInOneDirection();

    @Test
    abstract void setNeighborCell_alreadyNeighborWithAnotherDirection();

    @Test
    abstract void setNeighborCell_setSelfAsNeighbor();

    @Test
    abstract void isNeighbor_WhenNeighborCellExists();

    @Test
    abstract void isNeighbor_WhenNeighborCellNotExists();

    @Test
    abstract void setWall_InSingleWithSameWallAndAnotherDirection();

    @Test
    abstract void setWall_InSingleWithSameDirectionAndAnotherWallSegment();

    @Test
    abstract void setWall_inNeighborCells();

    @Test
    abstract void setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment();

    @Test
    abstract void setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection();

    @Test
    abstract void neighborWall_wallNotExists();
}