package org.codecity.datasource.java;

import static org.codecity.datasource.java.ClassInspectUtils.getClassPath;

public class RecordingInspector extends ClassInspector<ClassInfo> {
    private InfoFactory infoFactory;

    public RecordingInspector() {
        this(getClassPath());
    }

    public RecordingInspector(final String classPath) {
        super(classPath);
        infoFactory = new BeanInfoFactory();
    }

    public void setInfoFactory(final InfoFactory infoFactory) {
        if (infoFactory == null) throw new IllegalArgumentException("InfoFactory must not be null");
        this.infoFactory = infoFactory;
    }

    protected RecursingClassVisitor<ClassInfo> createVisitor() {
        return new RecordingClassVisitor(this, infoFactory);
    }
}
