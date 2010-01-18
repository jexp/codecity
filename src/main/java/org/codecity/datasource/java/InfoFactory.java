package org.codecity.datasource.java;

import java.util.Collection;
import java.util.Set;

public interface InfoFactory {
    ClassInfo createClassInfo(String name);

    MethodInfo createMethodInfo(Set<Modifiers> modifiers, String name, Collection<String> params, String returnType);

    FieldInfo createFieldInfo(Set<Modifiers> modifiers, String name, String typeName);
}
