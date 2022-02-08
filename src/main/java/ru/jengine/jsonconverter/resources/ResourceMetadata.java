package ru.jengine.jsonconverter.resources;

import javax.annotation.Nullable;

import com.google.common.base.Objects;

public class ResourceMetadata {
    private final String namespace;
    private final String objectType;
    private final String path;

    public ResourceMetadata(String namespace, String path) {
        this(namespace, null, path);
    }

    public ResourceMetadata(String namespace, @Nullable String objectType, String path) {
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

    public String getPath() {
        return path;
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
        return "ResourceMetadata{" +
                "namespace='" + namespace + '\'' +
                ", objectType='" + objectType + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
