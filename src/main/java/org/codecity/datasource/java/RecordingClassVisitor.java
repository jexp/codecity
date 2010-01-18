package org.codecity.datasource.java;

import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import static org.codecity.datasource.java.Modifiers.*;

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
        classInfo.setModifiers(accessToModifiers(access));
        if (classInspector.isStub(name)) classInfo.setIsStub();
    }

    private Set<Modifiers> accessToModifiers(int access)
    {
        Set<Modifiers> modifiers = EnumSet.noneOf(Modifiers.class);

        if ((access & Opcodes.ACC_FINAL) != 0)
            modifiers.add(FINAL);

        if ((access & Opcodes.ACC_NATIVE) != 0)
            modifiers.add(NATIVE);

        if ((access & Opcodes.ACC_INTERFACE) != 0)
            modifiers.add(INTERFACE);

        if ((access & Opcodes.ACC_ABSTRACT) != 0)
            modifiers.add(ABSTRACT);

        if ((access & Opcodes.ACC_PRIVATE) != 0)
            modifiers.add(PRIVATE);

        if ((access & Opcodes.ACC_PROTECTED) != 0)
            modifiers.add(PROTECTED);

        if ((access & Opcodes.ACC_PUBLIC) != 0)
            modifiers.add(PUBLIC);

        if ((access & Opcodes.ACC_STATIC) != 0)
            modifiers.add(STATIC);

        if ((access & Opcodes.ACC_STRICT) != 0)
            modifiers.add(STRICT);

        if ((access & Opcodes.ACC_SYNCHRONIZED) != 0)
            modifiers.add(SYNCHRONIZED);

        if ((access & Opcodes.ACC_TRANSIENT) != 0)
            modifiers.add(TRANSIENT);

        if ((access & Opcodes.ACC_VOLATILE) != 0)
            modifiers.add(VOLATILE);

        return modifiers;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        final Collection<String> params = getParamTypes(desc);
        final String returnType = getReturnType(desc);
        final MethodInfo methodInfo = infoFactory.createMethodInfo(accessToModifiers(access), name, params, returnType);
        classInfo.addMethod(methodInfo);
        return null;
    }

    @Override public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        final String typeName = Type.getType(desc).getClassName();
        final FieldInfo fieldInfo = infoFactory.createFieldInfo(accessToModifiers(access), name, typeName);
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
