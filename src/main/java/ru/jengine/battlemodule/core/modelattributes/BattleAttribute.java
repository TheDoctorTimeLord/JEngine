package ru.jengine.battlemodule.core.modelattributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Базовый класс всех атрибутов объекта. Атрибут — это некоторые данные об объекте, которые могут быть изменены в
 * процессе боя. Атрибуты позволяют хранить различную дополнительную информацию об объекте, а также уточнять
 * некоторые данные о нём.<br>
 * Атрибут содержит два обязательных поля:
 * <ol>
 * <li><b>Code</b> - код атрибута. Не может быть изменён.</li>
 * <li><b>Path</b> - путь до атрибута внутри контейнера атрибутов</li>
 * </ol>
 * При изменении атрибута НЕОБХОДИМО производить дополнительные операции, если вы хотите как-то уведомить некоторые
 * ваши системы об изменении. По умолчанию атрибуты не имеют автоматического уведомления каких-либо систем
 */
public abstract class BattleAttribute implements Cloneable {
    private final String code;
    private List<String> path = Collections.emptyList();

    protected BattleAttribute(String code) {
        this.code = Objects.requireNonNull(code, "Code of attribute must be not null");
    }

    public String getCode() {
        return code;
    }

    public BattleAttribute setPath(List<String> path) {
        this.path = path;
        return this;
    }

    /**
     * Возвращает копию пути до данного атрибута из {@link AttributesContainer} по цепочке атрибутов вложенных атрибутов
     */
    public List<String> getPath() {
        return new ArrayList<>(path);
    }

    @Override
    public abstract BattleAttribute clone();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BattleAttribute attribute = (BattleAttribute)o;
        return Objects.equals(path, attribute.path) && Objects.equals(code,
                attribute.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, code);
    }
}
