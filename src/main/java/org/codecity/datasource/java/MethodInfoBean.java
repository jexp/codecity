package org.codecity.datasource.java;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.io.Serializable;
import java.util.Set;

public class MethodInfoBean implements MethodInfo, Serializable {
    private final String name;
    private final Collection<String> params;
    private final String returnType;
    private final Set<Modifiers> modifiers;

    public MethodInfoBean(Set<Modifiers> modifiers, String name, String returnType, final Collection<String> params) {
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.name = name;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public Set<Modifiers> getModifiers() {
        return modifiers;
    }

    public Collection<String> getParams() {
        return params;
    }

    @Override public String toString() {
        return modifiers.toString() + " " + getSignature() + "\n";
    }

    public String getSignature() {
        return ClassInspectUtils.getSignature(this);
    }
}
