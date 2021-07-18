package com.fudy.core.jvm.classloader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryClassLoader extends ClassLoader {
    private Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    public InMemoryClassLoader() {
        super(InMemoryClassLoader.class.getClassLoader());
    }

    public void putClass(String className, byte[] bytes) {
        Class<?> c = defineClass(className, bytes, 0, bytes.length);
        classMap.put(className, c);
    }

    public <T> T newInstance(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = this.loadClass(className);
        return (T)clazz.newInstance();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> c = classMap.get(name);
        if (null == c) {
            throw new ClassNotFoundException(name);
        }
        return c;
    }
}
