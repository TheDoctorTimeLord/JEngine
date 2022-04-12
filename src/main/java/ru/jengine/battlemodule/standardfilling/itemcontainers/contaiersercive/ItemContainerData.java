package ru.jengine.battlemodule.standardfilling.itemcontainers.contaiersercive;

import com.google.common.base.Objects;

/**
 * Метаданные, соответствующие некоторому одному контейнеру
 */
public class ItemContainerData {
    /** Контейнер, которому принадлежат метаданные */
    private final String containerCode;

    protected ItemContainerData(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getContainerCode() {
        return containerCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ItemContainerData that))
            return false;
        return Objects.equal(containerCode, that.containerCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(containerCode);
    }
}
