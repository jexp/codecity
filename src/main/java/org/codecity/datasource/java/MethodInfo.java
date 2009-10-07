package org.codecity.datasource.java;

import java.util.Collection;

public interface MethodInfo {
    String getName();

    String getReturnType();

    int getAccess();

    String getSignature();

    Collection<String> getParams();
}
