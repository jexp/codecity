package org.codecity.datasource.java;

import java.util.Collection;

public interface InfoFactory {
    ClassInfo createClassInfo(String name);

    MethodInfo createMethodInfo(int access, String name, Collection<String> params, String returnType);

    FieldInfo createFieldInfo(int access, String name, String typeName);
}
