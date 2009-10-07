package org.codecity.datasource.java;

import org.objectweb.asm.*;

import java.util.Collection;
import java.util.ArrayList;

public class RecordingClassVisitor extends ClassAdapter implements RecursingClassVisitor<ClassInfo> {
    private ClassInfo classInfo;
    private final ClassInspector<ClassInfo> classInspector;
    private final InfoFactory infoFactory;
    private static final ClassWriter NULL_WRITER = new ClassWriter(0);

    public RecordingClassVisitor(ClassInspector<ClassInfo> classInspector, final InfoFactory infoFactory) {
        super(NULL_WRITER);
        this.classInspector = classInspector;
        this.infoFactory = infoFactory;
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        if (classInfo == null) classInfo = infoFactory.createClassInfo(name);
        if (superName != null) { // todo filter
            classInfo.setSuperClass(classInspector.inspectClass(superName));
        }
        if (interfaces != null) {
            for (String interfaceName : interfaces) {
                classInfo.addInterface(classInspector.inspectClass(interfaceName));
            }
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        final Collection<String> params = getParamTypes(desc);
        final String returnType = getReturnType(desc);
        final MethodInfo methodInfo = infoFactory.createMethodInfo(access, name, params, returnType);
        classInfo.addMethod(methodInfo);
        return null;
    }

    @Override public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        final String typeName = Type.getType(desc).getClassName();
        final FieldInfo fieldInfo = infoFactory.createFieldInfo(access, name, typeName);
        classInfo.addField(fieldInfo);
        return null;
    }

    private Collection<String> getParamTypes(final String desc) {
        Collection<String> params=new ArrayList<String>();
        for (Type paramType : Type.getArgumentTypes(desc)) {
            params.add(paramType.getClassName());
        }
        return params;
    }

    private String getReturnType(final String desc) {
        final Type returnType = Type.getReturnType(desc);
        return returnType.getClassName();
    }

    public ClassInfo get() {
        return classInfo;
    }
}
