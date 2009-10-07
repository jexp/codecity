package org.codecity.datasource.java;

import java.lang.reflect.Modifier;
import java.io.Serializable;

public class FieldInfoBean implements FieldInfo, Serializable {
    private final int access;
    private final String name;
    private final String type;

    public FieldInfoBean(final int access, final String name, final String type) {
        this.access = access;
        this.name = name;
        this.type = type;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override public String toString() {
        return Modifier.toString(access)+" "+ getSignature()+"\n";
    }

    public String getSignature() {
        return ClassInspectUtils.getSignature(this);
    }
}
