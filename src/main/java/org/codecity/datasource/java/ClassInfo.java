package org.codecity.datasource.java;

import java.util.Map;
import java.util.Collection;
import java.util.Set;

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

    boolean isInterface();

    boolean isAbstract();

    void setModifiers(Set<Modifiers> modifier);

    boolean isStub();

    Set<Modifiers> getModifiers();

    void setIsStub();
}
