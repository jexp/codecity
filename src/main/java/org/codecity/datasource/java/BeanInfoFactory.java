package org.codecity.datasource.java;

import java.util.Collection;
import java.util.Set;

public class BeanInfoFactory implements InfoFactory {
    private int id=2;

    public ClassInfo createClassInfo(final String name) {
        return new ClassInfoBean(name,id++);
    }

    public MethodInfo createMethodInfo(final Set<Modifiers> modifiers, final String name, final Collection<String> params, final String returnType) {
        return new MethodInfoBean(modifiers, name, returnType, params);
    }

    public FieldInfo createFieldInfo(final Set<Modifiers> modifiers, final String name, final String typeName) {
        return new FieldInfoBean(modifiers,name, typeName);
    }
}
