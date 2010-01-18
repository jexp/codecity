package org.codecity.datasource.java;

import junit.framework.TestCase;
import org.junit.Test;

public class ClassInspectorTest extends TestCase {
    @Test
    public void testInspectObject() {
        final ClassInfo classInfo = new RecordingInspector().inspectClass(String.class);
        assertEquals("isString","java.lang.String",classInfo.getName());
        System.out.println("classInfo = " + classInfo);
        System.out.println("classInfo.getMethods() = " + classInfo.getMethods().values());
        System.out.println("classInfo.getFields() = " + classInfo.getFields().values());
        assertTrue("concat",classInfo.getMethods().containsKey("java.lang.String concat(java.lang.String)"));
        assertEquals("java.lang.Object",classInfo.getSuperClass().getName());
    }
}
