package ru.jengine.battlemodule.core.contentregistrars;

/**
 * Определяет интерфейс, который отвечает за регистрацию части контента в бою.
 */
public interface ContentRegistrar {
    /**
     * Регистрирует часть контента боя
     * @param context контекст, используемый при регистрации контента
     */
    void register(RegistrarsContext context);
}
