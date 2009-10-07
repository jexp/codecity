package org.codecity.datasource.java;

import java.util.Map;
import java.util.Collection;

public interface ClassInfo {
    int getId();
    
    String getName();

    Map<String, MethodInfo> getMethods();

    void addMethod(MethodInfo methodInfo);

    void addInterface(ClassInfo classInfo);

    void setSuperClass(ClassInfo classInfo);

    ClassInfo getSuperClass();

    Collection<ClassInfo> getInterfaces();

    void addField(FieldInfo fieldInfo);

    Map<String, FieldInfo> getFields();

    boolean isRoot();

    String getPackage();
    String getSimpleName();
}
