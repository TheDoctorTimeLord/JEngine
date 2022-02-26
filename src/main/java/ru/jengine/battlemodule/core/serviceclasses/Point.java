package ru.jengine.battlemodule.core.serviceclasses;

import com.google.common.base.Objects;

/**
 * Класс, хранящий координаты. Класс является immutable (только поддерживает наследование)
 */
public class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Получение X координаты точки
     */
    public int getX() {
        return x;
    }

    /**
     * Получение Y координаты точки
     */
    public int getY() {
        return y;
    }

    /**
     * Покоординатно складывает текущую точку с другой точкой
     * @param point точка, с которой производится сложение
     * @return новая точка — результат покоординатного сложения
     */
    public Point add(Point point) {
        return point != null ? PointPool.obtain(x + point.getX(), y + point.getY()) : this;
    }

    /**
     * Покоординатно вычитает из текущей точки другую точку
     * @param point вычитаемая точка
     * @return новая точка — результат покоординатного вычитания
     */
    public Point sub(Point point) {
        return point != null ? PointPool.obtain(x - point.getX(), y - point.getY()) : this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point point = (Point)o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
