package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.*;

import java.util.Objects;


/**
 * Класс точки
 */
public class Point {
    /**
     * Координаты точки
     */
    public final Vector2d pos;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;
    /**
     * Лежит ли точка в окружности
     */
    @JsonIgnore
    public boolean isIn;

    /**
     * Конструктор точки
     *
     * @param pos положение точки
     */
    @JsonCreator
    public Point(@JsonProperty("pos") Vector2d pos) {
        this.pos = pos;
        this.isIn = false;
    }

    /**
     * Получить положение
     *
     * @return положение
     */
    public Vector2d getPos() {
        return pos;
    }


    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Point{" +
                ", pos=" + pos + '}';
    }

    /**
     * Проверка двух объектов на равенство
     *
     * @param o объект, с которым сравниваем текущий
     * @return флаг, равны ли два объекта
     */
    @Override
    public boolean equals(Object o) {
        // если объект сравнивается сам с собой, тогда объекты равны
        if (this == o) return true;
        // если в аргументе передан null или классы не совпадают, тогда объекты не равны
        if (o == null || getClass() != o.getClass()) return false;
        // приводим переданный в параметрах объект к текущему классу
        Point point = (Point) o;
        return Objects.equals(pos, point.pos);
    }

    /**
     * Расстояние до точки
     */
    public double dist(Vector2d pos2) {
        return Math.sqrt((pos.x - pos2.x) * (pos.x - pos2.x) + (pos.y - pos2.y) * (pos.y - pos2.y));
    }

    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}