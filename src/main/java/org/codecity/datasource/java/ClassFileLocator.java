package org.codecity.datasource.java;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassFileLocator {
    ClassLoader cl;
    private List<URL> urls;

    // todo filesystem, jar files, mvn dependencies -> pom
    public ClassFileLocator(String path) {
        if (path == null) throw new IllegalArgumentException("Path must not be null");
        this.urls = pathToUrls(path);
        this.cl = new URLClassLoader(urls.toArray(new URL[urls.size()])); //, ClassLoader.getSystemClassLoader());
    }

    private List<URL> pathToUrls(final String path) {
        final String[] files = path.split(File.pathSeparator);
        final List<URL> urls = new ArrayList<URL>(files.length);
        for (int i = 0; i < files.length; i++) {
            final String file = files[i];
            try {
                if (file.matches("^\\w://.+"))
                    urls.add(new URL(file));
                else
                    urls.add(new File(file).toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException("Error creating URL from " + file, e);
            }
        }
        return urls;
    }

    public InputStream getStreamFromURL(final URL url) {
        try {
            return new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Error reading stream from url " + url, e);
        }
    }

    public URL resolveClassName(final String slashClassName) {
        final String fileClassName = slashClassName.concat(".class");
        return cl.getResource(fileClassName);
    }

    public boolean inScope(final String slashClassName) {
        final URL url = resolveClassName(slashClassName);
        if (url == null) return false;
        String urlString = url.toExternalForm();
        urlString = urlString.substring(0, urlString.length() - (slashClassName + ".class").length());
        try {
            URL lookupUrl = new URL(urlString);
            if (lookupUrl.getProtocol().equals("jar")) {
                String jarUrl = lookupUrl.getFile();
                if (jarUrl.endsWith("!/")) jarUrl=jarUrl.substring(0,jarUrl.length()-2);
                lookupUrl = new URL(jarUrl);
            }
            return urls.contains(lookupUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("error creating url", e);
        }
    }
}
