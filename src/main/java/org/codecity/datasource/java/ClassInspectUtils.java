package org.codecity.datasource.java;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public class ClassInspectUtils {
    public static String toSlashName(final Class<?> type) {
        return toSlashName(type.getName());
    }

    public static String toSlashName(final String typeName) {
        return typeName.replace('.','/');
    }

    public static String toClassName(String name) {
        return name.replace('/', '.');
    }

    public static String getSignature(final MethodInfo methodInfo) {
        return methodInfo.getReturnType() + " " + methodInfo.getName() + "(" + paramList(methodInfo.getParams()) + ")";
    }

    private static String paramList(final Collection<String> params) {
        if (params == null || params.isEmpty()) return "";
        final StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append(", ").append(param);
        }
        return sb.substring(2);
    }

    public static String getSignature(final FieldInfo fieldInfo) {
        return fieldInfo.getType()+" "+fieldInfo.getName();
    }

    public static String getSignature(final ClassInfo classInfo) {
        StringBuilder sb = new StringBuilder(classInfo.getName());
        final ClassInfo superClass = classInfo.getSuperClass();
        if (superClass != null && !superClass.isRoot()) sb.append(" extends ").append(superClass);
        final Collection<ClassInfo> interfaces = classInfo.getInterfaces();
        if (!interfaces.isEmpty())
            sb.append("implements ").append(interfaces);
        return sb.toString();
    }

    static String getClassPath() {
        return System.getProperty("java.class.path");
    }

    static String arrayToClassName(String typeName) {
        if (isArrayType(typeName)) typeName=typeName.substring(0,typeName.length()-2);
        return typeName;
    }

    public static boolean isArrayType(final String typeName) {
        return typeName.endsWith("[]");
    }


    public static Modifiers getAccessControl(Set<Modifiers> modifiers) {
        EnumSet<Modifiers> accessModifiers=EnumSet.of(Modifiers.PUBLIC, Modifiers.PROTECTED, Modifiers.PRIVATE);
        accessModifiers.retainAll(modifiers);
        return accessModifiers.isEmpty() ? Modifiers.PACKAGE : accessModifiers.iterator().next();
    }
}
