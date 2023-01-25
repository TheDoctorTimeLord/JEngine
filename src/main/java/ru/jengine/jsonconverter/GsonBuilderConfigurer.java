package ru.jengine.jsonconverter;

import com.google.gson.GsonBuilder;

public interface GsonBuilderConfigurer {
    void configure(GsonBuilder gsonBuilder);
}
