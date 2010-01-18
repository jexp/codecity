package org.codecity.datasource.java;

import java.util.Collection;
import java.util.Set;

public interface MethodInfo {
    String getName();

    String getReturnType();

    Set<Modifiers> getModifiers();

    String getSignature();

    Collection<String> getParams();
}
