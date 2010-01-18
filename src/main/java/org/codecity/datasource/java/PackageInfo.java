package org.codecity.datasource.java;

public interface PackageInfo {
    int getId();
    String getName();

    String getSuperPackage();

    boolean isRoot();

    boolean isStub();
}