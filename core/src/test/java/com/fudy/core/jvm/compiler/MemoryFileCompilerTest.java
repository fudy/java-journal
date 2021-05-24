package com.fudy.core.jvm.compiler;

import com.fudy.core.jvm.classloader.MemoryClassLoader;
import org.junit.Test;

import java.util.Map;

public class MemoryFileCompilerTest {
    @Test
    public void test1() throws Exception {
        String javaSource = new StringBuilder("package com.fudy;")
                .append("public class HelloWorld {")
                .append("public void execute() {")
                .append("System.out.println(\"Hello World \");")
                .append("}")
                .append("}")
                .toString();
        MemoryFileCompiler compiler = MemoryFileCompiler.getMemoryFileCompiler();
        Map<String, byte[]> classMap = compiler.compile("com.fudy.HelloWorld", javaSource, null);
        MemoryClassLoader classLoader = new MemoryClassLoader(classMap, MemoryClassLoader.class.getClassLoader());
        Class<?> helloWorldClass = classLoader.loadClass("com.fudy.HelloWorld");
        Object helloWorld = helloWorldClass.newInstance();
        helloWorldClass.getMethod("execute", null).invoke(helloWorld, null);
    }
}
