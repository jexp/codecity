package org.codecity.datasource.java;

/**
 * @author Michael Hunger
 * @since 07.10.2009
 */
public class PackageInfoBean implements PackageInfo {
    private final int id;
    private final String name;

    public PackageInfoBean(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return name.equals(((PackageInfoBean) o).name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String getSuperPackage() {
        int idx=name.indexOf('.');
        if (idx==-1) return null;
        return name.substring(0,idx);
    }

    @Override
    public boolean isRoot() {
        return name.indexOf('.')==-1;
    }

    @Override
    public String toString() {
        return name+" "+id;
    }
}
