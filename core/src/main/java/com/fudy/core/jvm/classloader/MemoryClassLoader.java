package com.fudy.core.jvm.classloader;

import com.fudy.core.jvm.compiler.MemoryFileCompiler;

import javax.tools.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader {
    private Map<String, byte[]> sourceFileClasses;
    public MemoryClassLoader(Map<String, byte[]> sourceFileClasses, ClassLoader parent) {
        super(parent);
        this.sourceFileClasses = sourceFileClasses;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = sourceFileClasses.get(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}
