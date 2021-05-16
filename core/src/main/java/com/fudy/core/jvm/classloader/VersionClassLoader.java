package com.fudy.core.jvm.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class VersionClassLoader extends URLClassLoader {
    private static final String baseDir ="../api/target/";

    public VersionClassLoader(String version){
        super(new URL[]{}, null);
        try {
            loadJarByVersion(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJarByVersion(String version) throws Exception {
        File file = new File(baseDir+"api-"+version+".jar");
        this.addURL(file.toURI().toURL());
    }


}
