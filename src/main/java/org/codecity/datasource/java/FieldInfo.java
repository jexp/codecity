package org.codecity.datasource.java;

import java.util.Set;

public interface FieldInfo {
    Set<Modifiers> getModifiers();

    String getName();

    String getType();

    String getSignature();
}
