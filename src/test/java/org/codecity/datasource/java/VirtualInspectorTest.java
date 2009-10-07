package org.codecity.datasource.java;

import junit.framework.TestCase;
import org.junit.Test;

public class VirtualInspectorTest extends TestCase {
    @Test
    public void testInspectObject() {
        new ClassInspector<Void>(System.getProperty("java.class.path")) {
            protected RecursingClassVisitor<Void> createVisitor() {
                return VirtualVisitor.visitor(RecursingClassVisitor.class, this, 0);
            }
        }.inspectClass(Object.class);
    }

    @Test
    public void testInspectTest() {
        new ClassInspector<Void>(System.getProperty("java.class.path")) {
            protected RecursingClassVisitor<Void> createVisitor() {
                return VirtualVisitor.visitor(RecursingClassVisitor.class, this, 0);
            }
        }.inspectClass(getClass());
    }
}