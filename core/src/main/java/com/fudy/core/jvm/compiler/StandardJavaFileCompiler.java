package com.fudy.core.jvm.compiler;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StandardJavaFileCompiler {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostic, Locale.getDefault(), Charset.defaultCharset());


    public void compile(File javaFile, List<String> optionList) throws Exception {
        Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(javaFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostic, optionList, null, compilationUnit);

        if (!task.call()) {
            StringBuilder error = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> d : diagnostic.getDiagnostics()) {
                error.append("Error on line %d in %s%n").append(d.getLineNumber()).append(d.getSource().toUri());
            }
            throw new Exception(error.toString());
        }
//        fileManager.close();
    }
}
