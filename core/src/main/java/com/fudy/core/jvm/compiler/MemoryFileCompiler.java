package com.fudy.core.jvm.compiler;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.*;

public class MemoryFileCompiler {
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private DiagnosticCollector<JavaFileObject> diagnostic = new DiagnosticCollector<>();
    private StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostic, Locale.getDefault(), Charset.defaultCharset());
    private MemoryFileManager fileManager = new MemoryFileManager(standardFileManager);

    private MemoryFileCompiler() {
    }

    public static MemoryFileCompiler getMemoryFileCompiler() {
        return new MemoryFileCompiler();
    }

    public Map<String, byte[]> compile(String className, String javaSource, List<String> optionList) throws Exception{
        JavaFileObject javaFileObject = fileManager.toJavaFileObject(className, javaSource);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostic, optionList,
                null, Arrays.asList(javaFileObject));
        if (!task.call()) {
            StringBuilder error = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> d : diagnostic.getDiagnostics()) {
                error.append("Error on line %d in %s%n").append(d.getLineNumber()).append(d.getSource().toUri());
            }
            throw new Exception(error.toString());
        }
        return fileManager.getSourceFileClasses();
    }

    private class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        private final Map<String, byte[]> sourceFileClasses = new HashMap<>();

        MemoryFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public Map<String, byte[]> getSourceFileClasses() {
            return sourceFileClasses;
        }
        /**
         * specify the output java file
         */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className,
                                                   JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            if (location == StandardLocation.CLASS_OUTPUT && kind == JavaFileObject.Kind.CLASS) {
                return createInMemoryClassFile(className);
            } else {
                return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        }

        public JavaFileObject toJavaFileObject(String className, String javaSource) {
            return new SimpleJavaFileObject(this.toURI(className, JavaFileObject.Kind.SOURCE), JavaFileObject.Kind.SOURCE) {
                @Override
                public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                    return CharBuffer.wrap(javaSource);
                }
            };
        }

        /**
         * URI.getPath() will return the path of java class
         */
        private URI toURI(String className, JavaFileObject.Kind kind) {
            return URI.create("memory:///" + className.replace('.', '/') + kind.extension);
        }

        private JavaFileObject createInMemoryClassFile(String className) {
            return new SimpleJavaFileObject(this.toURI(className, JavaFileObject.Kind.CLASS), JavaFileObject.Kind.CLASS) {
                @Override
                public OutputStream openOutputStream() {
                    return new ByteArrayOutputStream() {
                        @Override
                        public void close() throws IOException {
                            super.close();
                            sourceFileClasses.put(className, toByteArray());
                        }
                    };
                }
            };
        }
    }

}
