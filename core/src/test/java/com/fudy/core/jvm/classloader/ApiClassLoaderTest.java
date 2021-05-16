package com.fudy.core.jvm.classloader;

import org.junit.Test;

/**
 * before run the method, pls run mvn compile first in api sub-module
 * */
public class ApiClassLoaderTest {

    @Test
    public void test1() throws Exception {
        ApiClassLoader loader = new ApiClassLoader();
        Class<?> fudyClass = loader.loadClass("com.fudy.api.Fudy");
        Object human = fudyClass.newInstance();
        fudyClass.getMethod("say", null)
                .invoke(human, null); //output "hello human" \n "hello Fudy"
    }
}
