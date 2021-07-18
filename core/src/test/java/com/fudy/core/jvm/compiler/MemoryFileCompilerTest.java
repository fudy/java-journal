package com.fudy.core.jvm.compiler;

import org.junit.Test;

import java.util.Map;

public class MemoryFileCompilerTest {
    String javaSource = new StringBuilder("package com.fudy;")
            .append("public class HelloWorld {")
            .append("public void execute() {")
            .append("System.out.println(\"Hello World \");")
            .append("}")
            .append("}")
            .toString();

    String javaSource2 = new StringBuilder("package com.fudy;")
            .append("public class HelloWorld {")
            .append("public void execute() {")
            .append("System.out.println(\"Hello Fudy\");")
            .append("}")
            .append("}")
            .toString();

    @Test
    /** https://stackoverflow.com/questions/34414906/classloading-using-different-versions-of-the-same-class-java-lang-linkageerror
     * */
    public void test1() throws Exception {
        Object helloWorld = InMemoryCompiler.compile("com.fudy.HelloWorld", javaSource, null);
        helloWorld.getClass().getMethod("execute", null).invoke(helloWorld, null);
        Object helloWorld2 = InMemoryCompiler.compile("com.fudy.HelloWorld", javaSource2, null);
        helloWorld2.getClass().getMethod("execute", null).invoke(helloWorld2, null);
    }
}
