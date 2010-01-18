package org.codecity.datasource.java;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

public class StubFileTest {
    @Test
    public void testPath() {
        final URL resourceUrl = getClass().getResource("/test.jar");
        System.out.println("resourceUrl = " + resourceUrl.getPath());
        final ClassFileLocator locator = new ClassFileLocator(resourceUrl.getPath());
        final String type = "test.Test";
        final URL resolvedClassUrl = locator.resolveClassName(ClassInspectUtils.toSlashName(type));
        System.out.println("resolvedClassUrl = " + resolvedClassUrl);
        assertNotNull("found class " + type, resolvedClassUrl);
    }
}