package com.fudy.core.jvm.compiler;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class StandardJavaFileCompilerTest {
    @Test
    public void test1() throws Exception {
        StandardJavaFileCompiler compiler = new StandardJavaFileCompiler();
        List<String> optionList = new ArrayList<>();
        optionList.add("-classpath");
        String classPath = "/tmp:" + System.getProperty("java.class.path");
        optionList.add(classPath);
        compiler.compile(new File("/tmp/HelloWorld.java"), optionList);

        URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("/tmp").toURI().toURL()});
        Class<?> helloWoldClass = classLoader.loadClass("HelloWorld");
        Object helloWorld = helloWoldClass.newInstance();
        helloWoldClass.getMethod("execute", null).invoke(helloWorld, null);
    }
}
