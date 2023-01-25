package ru.jengine.jsonconverter.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Objects;

public class ResourceMetadata {
    private final String namespace;
    private final String objectType;
    private final List<String> path;

    public ResourceMetadata(String namespace, @Nullable String objectType, String... path) {
        this(namespace, objectType, Arrays.asList(path));
    }

    public ResourceMetadata(String namespace, @Nullable String objectType, List<String> path) {
        this.namespace = namespace;
        this.objectType = objectType;
        this.path = path;
    }

    public String getNamespace() {
        return namespace;
    }

    @Nullable
    public String getObjectType() {
        return objectType;
    }

    public List<String> getPath() {
        return new ArrayList<>(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ResourceMetadata that = (ResourceMetadata)o;
        return Objects.equal(namespace, that.namespace) && Objects.equal(
                objectType, that.objectType) && Objects.equal(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(namespace, objectType, path);
    }

    @Override
    public String toString() {
        return namespace + ":" + (objectType == null ? "" : objectType) + ":" + String.join(".", path);
    }
}
