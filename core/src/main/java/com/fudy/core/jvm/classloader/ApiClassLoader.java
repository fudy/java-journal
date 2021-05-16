package com.fudy.core.jvm.classloader;

import java.io.*;

public class ApiClassLoader extends ClassLoader {

    private static final String baseDir ="../api/target/classes/";

    /**
     * this method will be called in java.lang.ClassLoader#loadClass(java.lang.String, boolean)
     * */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b;
        try {
            b = loadClassByName(name);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
        return defineClass(name, b, 0, b.length);
    }

    /**
     * find the class file by className and convert it to byte array
     */
    private byte[] loadClassByName(String className) throws IOException {
        String classFile = new StringBuilder(baseDir)
                .append(className.replace('.', File.separatorChar))
                .append(".class")
                .toString();
        InputStream is = new FileInputStream(classFile);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len;
        while((len = is.read(buff)) != -1) {
            os.write(buff, 0, len);
        }
        return os.toByteArray();
    }
}
