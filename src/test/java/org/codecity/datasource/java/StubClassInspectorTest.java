package org.codecity.datasource.java;

import junit.framework.TestCase;
import org.junit.Test;

import static org.codecity.datasource.java.ClassInspectUtils.toSlashName;

public class StubClassInspectorTest extends TestCase {
    @Test
    public void testInspectObject() {
        final String testJarPath = getClass().getResource("/test.jar").getPath();
        final RecordingInspector inspector = new RecordingInspector(testJarPath);
        final ClassInfo classInfo = inspector.inspectClass("test/Test");
        assertEquals("isString","test.Test",classInfo.getName());
        System.out.println("classInfo = " + classInfo);
        assertEquals(true,inspector.getClassFileLocator().inScope(toSlashName(classInfo.getName())));
        System.out.println("classInfo.getMethods() = " + classInfo.getMethods().values());
        System.out.println("classInfo.getFields() = " + classInfo.getFields().values());
        assertTrue("helloWorld",classInfo.getMethods().containsKey("void helloWorld()"));
        final ClassInfo superClass = classInfo.getSuperClass();
        System.out.println("superClass.getName() = " + toSlashName(superClass.getName()));
        assertEquals(false,inspector.getClassFileLocator().inScope(toSlashName(superClass.getName())));
        System.out.println("superClass = " + superClass.getFields());
        System.out.println("superClass = " + superClass.getMethods());
        System.out.println("superClass = " + superClass.getModifiers());
        assertEquals("java.lang.Object", superClass.getName());
    }
}