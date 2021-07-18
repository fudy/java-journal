package com.fudy.core.jvm.compiler;

import com.fudy.core.jvm.classloader.InMemoryClassLoader;

import javax.tools.*;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class InMemoryCompiler {

    public static Object compile(String className, String javaSource, List<String> optionList) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // new classloader to fix duplicate class definition exception
        InMemoryClassLoader classLoader = new InMemoryClassLoader();
        InMemoryClassManager fileManager = new InMemoryClassManager(compiler, classLoader);
        try {
            JavaFileObject sourceFile = fileManager.toJavaSourceFile(className, javaSource);
            StringWriter sw = new StringWriter();
            JavaCompiler.CompilationTask task = compiler.getTask(sw, fileManager, null, optionList,
                    null, Arrays.asList(sourceFile));
            if (!task.call()) {
                throw new Exception(sw.toString());
            }
            return classLoader.newInstance(className);
        } finally {
            fileManager.close();
        }
    }

    /**
     * URI.getPath() will return the path of java class
     */
    private static URI toURI(String className, JavaFileObject.Kind kind) {
        return URI.create("memory:///" + className.replace('.', '/') + kind.extension);
    }
}
