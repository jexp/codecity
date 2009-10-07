package org.codecity.datasource.java;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.io.Serializable;

public class MethodInfoBean implements MethodInfo, Serializable {
    private final String name;
    private final Collection<String> params;
    private final String returnType;
    private final int access;

    public MethodInfoBean(int access, String name, String returnType, final Collection<String> params) {
        this.access = access;
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

    public int getAccess() {
        return access;
    }

    public Collection<String> getParams() {
        return params;
    }

    @Override public String toString() {
        return Modifier.toString(access) + " " + getSignature() + "\n";
    }

    public String getSignature() {
        return ClassInspectUtils.getSignature(this);
    }
}
