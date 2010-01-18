package org.codecity.datasource.java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import static java.lang.System.currentTimeMillis;

/**
 * @author Michael Hunger
 * @since 07.10.2009
 */
public class MooseWriter {
    private final Map<String, ClassInfo> classes;
    private Set<PackageInfo> namespaces;
    private Collection<PackageInfo> packages;
    private int index;

    public MooseWriter(final Map<String, ClassInfo> classes) {
        this.classes = classes;
        index = getMaxIndex(classes.values()) + 1;
        System.out.println("max.class.idx = " + index);
        this.namespaces = extractNamespaces(classes, index);
        index += namespaces.size();
        System.out.println("max.ns.idx = " + index);
        this.packages = extractPackages(namespaces, index);
        index += packages.size();
        System.out.println("max.pkg.idx = " + index);
    }

    private int getMaxIndex(Collection<ClassInfo> classes) {
        int max = 0;
        for (ClassInfo info : classes) {
            if (info.getId() > max) max = info.getId();
        }
        return max;
    }

    private Collection<PackageInfo> extractPackages(Collection<PackageInfo> namespaces, int startIdx) {
        Collection<PackageInfo> result = new HashSet<PackageInfo>(namespaces.size());
        for (PackageInfo namespace : namespaces) {
            PackageInfo packageInfo = new
                    PackageInfoBean(namespace.getName(), startIdx++);
            result.add(packageInfo);
            while (!packageInfo.isRoot()) {
                final PackageInfo superPackage = new PackageInfoBean(packageInfo.getSuperPackage(), startIdx);
                if (!result.contains(superPackage)) {
                    result.add(superPackage);
                    startIdx++;
                }
                packageInfo = superPackage;
            }
        }
        return result;
    }

    private Set<PackageInfo> extractNamespaces(final Map<String, ClassInfo> classes, int startIdx) {
        final Set<PackageInfo> result = new HashSet<PackageInfo>();
        for (final ClassInfo info : classes.values()) {
            final PackageInfoBean packageInfo = new PackageInfoBean(info.getPackage(), startIdx);
            if (!result.contains(packageInfo)) {
                result.add(packageInfo);
                startIdx++;
            }
        }
        return result;
    }

    public static void main(final String[] args) throws Exception {
        System.err.println("Usage: java " + MooseWriter.class.getName() + " file.mse fil.ter.pkg pkg.SearchObject");
        final String file = args[0];
        final String filter = args.length > 1 ? args[1] : null;
        long time = currentTimeMillis();
        String param = args.length > 2 ? args[2] : null;
        final Map<String, ClassInfo> classes = new ClassParser().loadClasses(param);
        time = currentTimeMillis() - time;
        System.out.println("reading took " + time + " ms");
        time = currentTimeMillis();
        new MooseWriter(filter(classes, filter)).writeFile(filter, file);
        time = currentTimeMillis() - time;
        System.out.println("\nwriting took " + time + " ms");
    }

    private static Map<String, ClassInfo> filter(Map<String, ClassInfo> classes, String filter) {
        if (filter == null) return classes;
        Map<String, ClassInfo> result = new HashMap<String, ClassInfo>(classes.size());
        for (ClassInfo info : classes.values()) {
            if (info.getName().startsWith(filter)) {
                result.put(info.getName(), info);
                while (!info.isRoot()) {
                    info = info.getSuperClass();
                    result.put(info.getName(), info);
                }
            }
        }
        System.out.printf("Filtered %d to %d%n", classes.size(), result.size());
        return result;
    }

    private void writeFile(final String name, final String file) {
        try {
            final Writer os = new BufferedWriter(new FileWriter(file));
            writeHeader(os, name);
            writePackages(os);
            writeClasses(os, index);
            writeFooter(os);
            os.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private void writeClasses(Writer os, int index) throws IOException {
        System.out.println("\nWriting classes");
        for (ClassInfo info : classes.values()) {
            os.write("\t\t(FAMIX.Class (id: " + info.getId() + ")\n" +
                    "\t\t\t(name '" + info.getSimpleName() + "')\n" +
                    "\t\t\t(belongsTo (idref: " + getPackageId(namespaces, info.getPackage()) + "))\n" +
                    "\t\t\t(NOM " + info.getMethods().size() + ")\n" +
                    "\t\t\t(isInterface " + info.isInterface() + ")\n" +
                    "\t\t\t(isAbstract " + info.isAbstract() + ")\n" +
                    "\t\t\t(accessControlQualifier '" + getAccessControl(info.getModifiers()) + "')\n" +
                    "\t\t\t(stub " + info.isStub() + ")\n" +

                    "\t\t\t(packagedIn (idref: " + getPackageId(packages, info.getPackage()) + ")))\n");
            if (!info.isRoot()) {
                os.write("\t\t(FAMIX.InheritanceDefinition (id: " + (index++) + ")\n" +
                        "\t\t\t(subclass (idref: " + info.getId() + "))\n" +
                        "\t\t\t(superclass (idref: " + info.getSuperClass().getId() + ")))\n");
            }
            index = writeAttributes(os, info, index);
            index = writeMethods(os, info, index);
            // todo info.getMemberCount or sth like
        }
    }

    private int writeMethods(Writer os, ClassInfo info, int index) throws IOException {
        for (MethodInfo method : info.getMethods().values()) {
            os.write("\t\t(FAMIX.Method (id: " + (index++) + ")\n" +
                    "\t\t\t(name '" + method.getName() + "')\n" +
                    "\t\t\t(accessControlQualifier '" + getAccessControl(method.getModifiers()) + "')\n" +
                    "\t\t\t(belongsTo (idref: " + info.getId() + "))\n" +
                    /*
                     "\t\t\t(CC 1.0)\n" +
                     "\t\t\t(CM 1.0)\n" +
                     "\t\t\t(CYCLO 0.0)\n" +
                     "\t\t\t(hasClassScope false)\n" +
                     "\t\t\t(isCovered false)\n" +
                     "\t\t\t(LOC 0.0)\n" +
                     "\t\t\t(NI 0.0)\n" +
                     "\t\t\t(NMAA 0.0)\n" +
                     "\t\t\t(NOCmts 0.0)\n" +
                     "\t\t\t(NOCond 0.0)\n" +
                     "\t\t\t(NOS 0.0)\n" +
                     "\t\t\t(signature 'getClass()')\n" +
                     "\t\t\t(source '')\n" +
                     "\t\t\t(sourceAnchor '_UNKNOWN_PATH_\\_UNKNOWN_FILE_')\n" +
              */
                    "\t\t\t)\n");
        }
        return index;
    }

    private int writeAttributes(Writer os, ClassInfo info, int index) throws IOException {
        for (FieldInfo field : info.getFields().values()) {
            os.write("(FAMIX.Attribute (id: " + (index++) + ")\n" +
                    "\t\t\t(name '" + field.getName() + "')\n" +
                    "\t\t\t(accessControlQualifier '" + getAccessControl(field.getModifiers()) + "')\n" +
                    "\t\t\t(belongsTo (idref: " + info.getId() + "))\n" +
                    //"\t\t\t(hasClassScope true)\n" +
                    "\t\t\t)\n");
        }
        return index;
    }

    private String getAccessControl(Set<Modifiers> modifiers) {
        return ClassInspectUtils.getAccessControl(modifiers).name().toLowerCase();
    }

    private int getPackageId(Collection<PackageInfo> packages, String pkgName) {
        for (PackageInfo namespace : packages) {
            if (namespace.getName().equals(pkgName)) return namespace.getId();
        }
        throw new IllegalArgumentException("Unknown package " + pkgName);
    }

    private void writePackages(Writer os) throws IOException {
        System.out.println("\nWriting namespaces");
        for (PackageInfo info : namespaces) {
            os.write("\t\t(FAMIX.Namespace (id: " + info.getId() + ")\n" +
                    "\t\t\t(name '" + toMoose(info.getName()) + "')" +
                    "\t\t\t(stub " + info.isStub() + ")\n" +
                    ")\n");
        }
        System.out.println("\nWriting packages");
        for (PackageInfo info : packages) {
            os.write("\t\t(FAMIX.Package (id: " + info.getId() + ")\n" +
                    "\t\t\t(name '" + toMoose(info.getName()) + "')\n" +
                    "\t\t\t(DIH " + dih(info) + ")\n" +
                    "\t\t\t(stub " + info.isStub() + ")\n" +

                    (info.isRoot() ? "" : "\t\t\t(packagedIn (idref: " + getPackageId(packages, info.getSuperPackage()) + "))")
                    + ")\n");
        }
    }

    private int dih(PackageInfo aPackage) {
        return aPackage.getName().split("\\.").length;
    }

    private String toMoose(String pkg) {
        return pkg.replaceAll("\\.", "::");
    }

    private void writeFooter(Writer os) throws IOException {
        os.write(")\n" +
                "\t(LOC 1000)\n" +
                "\t(NOC " + classes.size() + ")\n" +
                "\t(NOP " + namespaces.size() + ")\n" +
                "\t(sourceLanguage 'Java'))\n");
    }

    private void writeHeader(Writer os, String name) throws IOException {
        if (name == null) name = "unknown";
        os.write("(Moose.Model (id: 1)\n" +
                "\t(name '" + name + "')\n" +
                "\t(entity");
    }

    static class ClassParser {
        public ClassParser() {
        }

        private String getJarLocation(ClassFileIterator fileIterator, String param) {
            try {
                if (param != null && param.endsWith(".jar")) {
                    if (param.startsWith("file:"))
                        return fileIterator.getPathFromUrl(new java.net.URL(param));
                    else
                        return fileIterator.getJarLocationFromClassPath(param);
                }
                final Class<?> type = param != null ? Class.forName(param) : Object.class;
                return fileIterator.getJarLocationByClass(type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Map<String, ClassInfo> loadClasses(String param) {
            final RecordingInspector inspector = new RecordingInspector();
            final ClassFileIterator fileIterator = new ClassFileIterator();
            String jarFileLocation = getJarLocation(fileIterator, param);
            ;
            for (final String classFileName : fileIterator.getClassFileNames(jarFileLocation)) {
                inspector.inspectClass(classFileName);
            }
            return inspector.getClasses();
        }
    }
}
