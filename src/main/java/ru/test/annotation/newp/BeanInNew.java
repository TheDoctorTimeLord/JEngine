package ru.test.annotation.newp;

import ru.test.annotation.Bean3;

public class BeanInNew {
    private final Bean3 bean;

    public BeanInNew(Bean3 bean) {
        this.bean = bean;
    }

    public boolean isF() {
        return bean.isF();
    }
}
