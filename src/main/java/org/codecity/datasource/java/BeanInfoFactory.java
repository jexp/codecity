package org.codecity.datasource.java;

import java.util.Collection;

public class BeanInfoFactory implements InfoFactory {
    private int id=2;

    public ClassInfo createClassInfo(final String name) {
        return new ClassInfoBean(name,id++);
    }

    public MethodInfo createMethodInfo(final int access, final String name, final Collection<String> params, final String returnType) {
        return new MethodInfoBean(access, name, returnType, params);
    }

    public FieldInfo createFieldInfo(final int access, final String name, final String typeName) {
        return new FieldInfoBean(access,name, typeName);
    }
}
