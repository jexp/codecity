package org.codecity.datasource.java;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClassInfoBean implements ClassInfo, Serializable {
    private final String name;
    private final Map<String, MethodInfo> methods = new HashMap<String, MethodInfo>(20);
    private ClassInfo superClass;
    private final Collection<ClassInfo> interfaces = new LinkedList<ClassInfo>();
    private Map<String, FieldInfo> fieldInfos = new HashMap<String, FieldInfo>(15);
    private int id;

    public ClassInfoBean(final String name) {
        if (name == null) throw new IllegalArgumentException("Name must not be null");
        this.name = ClassInspectUtils.toClassName(name);
    }

    public ClassInfoBean(final String name, int id) {
        this(name);
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlashName() {
        return ClassInspectUtils.toSlashName(name);
    }

    public Map<String, MethodInfo> getMethods() {
        return methods;
    }

    public void addMethod(MethodInfo methodInfo) {
        methods.put(methodInfo.getSignature(), methodInfo);
    }

    public void addInterface(final ClassInfo classInfo) {
        interfaces.add(classInfo);
    }

    public void setSuperClass(final ClassInfo classInfo) {
        this.superClass = classInfo;
    }

    public ClassInfo getSuperClass() {
        return superClass;
    }

    public Collection<ClassInfo> getInterfaces() {
        return interfaces;
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClassInfoBean classInfo = (ClassInfoBean) o;

        return name.equals(classInfo.name);

    }

    public int hashCode() {
        return name.hashCode();
    }

    public void addField(final FieldInfo fieldInfo) {
        fieldInfos.put(fieldInfo.getName(), fieldInfo);
    }

    public Map<String, FieldInfo> getFields() {
        return fieldInfos;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder(name);
        if (superClass != null && !superClass.isRoot()) sb.append(" extends ").append(superClass);
        if (!interfaces.isEmpty())
            sb.append("implements ").append(interfaces);
        return sb.toString();
    }

    public boolean isRoot() {
        return name.equals("java.lang.Object");
    }

    @Override
    public String getPackage() {
        return name.substring(0,name.lastIndexOf("."));
    }
    public String getSimpleName() {
        return name.substring(name.lastIndexOf(".")+1);
    }
}
