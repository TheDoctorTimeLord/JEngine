package ru.jengine.jsonconverter.additional;

public class F {
    private int notTransientField;
    private transient int transientField;

    public int getNotTransientField() {
        return notTransientField;
    }

    public int getTransientField() {
        return transientField;
    }
}
