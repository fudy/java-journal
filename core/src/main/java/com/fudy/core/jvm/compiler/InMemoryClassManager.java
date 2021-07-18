package com.fudy.core.jvm.compiler;

import com.fudy.core.jvm.classloader.InMemoryClassLoader;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Locale;

public class InMemoryClassManager extends ForwardingJavaFileManager<JavaFileManager> {
    private InMemoryClassLoader classLoader;

    public InMemoryClassManager(JavaCompiler compiler, InMemoryClassLoader classLoader) {
        super(compiler.getStandardFileManager(null,Locale.getDefault(), Charset.defaultCharset()));
        this.classLoader = classLoader;
    }

    /**
     * specify the output java file
     */
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className,
                                               JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (location == StandardLocation.CLASS_OUTPUT && kind == JavaFileObject.Kind.CLASS) {
            return toJavaClassFile(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    public JavaFileObject toJavaSourceFile(String className, String javaSource) {
        return new SimpleJavaFileObject(this.toURI(className, JavaFileObject.Kind.SOURCE), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                return javaSource;
            }
        };
    }

    /**
     * fix the error:
     * java.lang.Exception: /com.fudy.HelloWorld.java:1: 错误: 类 HelloWorld 是公共的, 应在名为 HelloWorld.java 的文件中声明
     */
    private URI toURI(String className, JavaFileObject.Kind kind) {
        return URI.create("memory:///" + className.replace('.', '/') + kind.extension);
    }

    private JavaFileObject toJavaClassFile(String className) {
        return new SimpleJavaFileObject(this.toURI(className, JavaFileObject.Kind.CLASS), JavaFileObject.Kind.CLASS) {
            @Override
            public OutputStream openOutputStream() {
                return new ByteArrayOutputStream() {
                    @Override
                    public void close() throws IOException {
                        super.close();
                        if (null != classLoader) {
                            classLoader.putClass(className, toByteArray());
                        }
                    }
                };
            }
        };
    }
}
