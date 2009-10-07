package org.codecity.datasource.java;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClassFileLocatorTest {
    @Test
    public void testPath() {
        final ClassFileLocator locator = new ClassFileLocator(System.getProperty("java.class.path"));
        assertNotNull("found class " + getClass(), locator.resolveClassName(ClassInspectUtils.toSlashName(getClass())));
    }
}
