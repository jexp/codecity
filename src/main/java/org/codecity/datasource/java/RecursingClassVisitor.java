package org.codecity.datasource.java;

import org.objectweb.asm.ClassVisitor;

public interface RecursingClassVisitor<R> extends ClassVisitor {
    R get();
}
