package ru.jengine.beancontainer.service;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K newValue) {
        key = newValue;
    }

    public void setValue(V newValue) {
        value = newValue;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}