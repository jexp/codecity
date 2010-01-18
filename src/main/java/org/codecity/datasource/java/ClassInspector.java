package org.codecity.datasource.java;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class ClassInspector<R> {
    ClassFileLocator classFileLocator; // todo
    private final Map<String, R> classes = new HashMap<String, R>(500);

    public ClassInspector(final String classPath) {
        if (classPath == null) throw new IllegalArgumentException("ClassPath must not be null");
        classFileLocator = new ClassFileLocator(classPath);
    }

    public R inspectClass(Class type) {
        if (type == null) throw new IllegalArgumentException("Type must not be null");
        return inspectClass(ClassInspectUtils.toSlashName(type));
    }

    public R inspectClass(String slashClassName) {
        if (slashClassName == null) throw new IllegalArgumentException("SlashClassName must not be null");
        if (classes.containsKey(slashClassName)) {
            return classes.get(slashClassName);
        }
        ClassReader classReader = createClassReader(slashClassName);
        if (classReader==null) return null;
        final RecursingClassVisitor<R> classVisitor = createVisitor();
        classReader.accept(classVisitor, 0);
        classes.put(slashClassName, classVisitor.get());
        return classVisitor.get();
    }

    protected abstract RecursingClassVisitor<R> createVisitor();

    private ClassReader createClassReader(final String slashClassName) {
        assert slashClassName != null;
        URL classResource = classFileLocator.resolveClassName(slashClassName);
        try {
            return new ClassReader(classFileLocator.getStreamFromURL(classResource));
        } catch (Exception e) {
            System.err.println("Error creating ClassReader for class " + slashClassName);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Map<String, R> getClasses() {
        return classes;
    }

    public boolean isStub(String name) {
        return !classFileLocator.inScope(name);
    }
}
