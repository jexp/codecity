package org.codecity.datasource.java;

import java.lang.reflect.Modifier;
import java.io.Serializable;
import java.util.Set;

public class FieldInfoBean implements FieldInfo, Serializable {
    private final Set<Modifiers> modifiers;
    private final String name;
    private final String type;

    public FieldInfoBean(final Set<Modifiers> modifiers, final String name, final String type) {
        this.modifiers = modifiers;
        this.name = name;
        this.type = type;
    }

    public Set<Modifiers> getModifiers() {
        return modifiers;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override public String toString() {
        return modifiers.toString()+" "+ getSignature()+"\n";
    }

    public String getSignature() {
        return ClassInspectUtils.getSignature(this);
    }
}
