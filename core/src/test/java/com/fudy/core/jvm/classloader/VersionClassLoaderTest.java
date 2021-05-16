package com.fudy.core.jvm.classloader;

import org.junit.Test;

/**
 * before run the method, pls run mvn package first in api sub-module
 * */
public class VersionClassLoaderTest {
    @Test
    public void test1() throws Exception {
        VersionClassLoader loader = new VersionClassLoader("1.0.0");
        Class<?> fudyClass = loader.loadClass("com.fudy.api.Fudy");
        Object human = fudyClass.newInstance();
        fudyClass.getMethod("say", null)
                .invoke(human, null); //output "hello human" \n "hello Fudy"
    }
}
