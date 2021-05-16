package com.fudy.core.jvm.classloader;

import org.junit.Test;

import com.sun.net.httpserver.HttpServer;
import java.util.HashMap;

public class ClassLoaderTest {
    @Test
    public void test1() {
        System.out.println(HashMap.class.getClassLoader()); // null
        System.out.println(HttpServer.class.getClassLoader()); //PlatformClassLoader
        System.out.println(this.getClass().getClassLoader()); //AppClassLoader
    }
}
