package com.fudy.core.jvm.classloader;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * before run the method, pls run mvn package first in api sub-module
 * */
public class UrlClassLoaderTest {
    @Test
    public void test1() throws Exception {
        URL url = new File("../api/target/api-1.0.0.jar").toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
        Class<?> fudyClass = loader.loadClass("com.fudy.api.Fudy");
        Object fudy = fudyClass.newInstance();
        fudyClass.getMethod("say").invoke(fudy);

    }

    @Test
    public void test2()  throws Exception {
        URL url = new File("../api/target/classes/").toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
        Class<?> fudyClass = loader.loadClass("com.fudy.api.Fudy");
        Object fudy = fudyClass.newInstance();
        fudyClass.getMethod("say").invoke(fudy);
    }

    @Test
    public void test3()  throws Exception {
        URL url = new File("/tmp/").toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
        Class<?> fudyClass = loader.loadClass("HelloWorld");
        Object fudy = fudyClass.newInstance();
        fudyClass.getMethod("execute").invoke(fudy);
    }
}
