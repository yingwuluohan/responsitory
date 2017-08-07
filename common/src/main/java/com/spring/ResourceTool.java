package com.spring;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 */
public class ResourceTool {
    /**
     * 获取默认的类加载器
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }

        return cl;
    }

    /**
     * 获取配置文件资源对象
     *
     * @param location
     * @return
     * @throws IOException
     */
    public static List<URL> findAllClassPathResources(String location) throws IOException {
        String path = location;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        Enumeration<URL> resourceUrls = getDefaultClassLoader().getResources(location);
        List<URL> result = Lists.newArrayList();
        while (resourceUrls.hasMoreElements()) {

            result.add(resourceUrls.nextElement());
        }

        return result;
    }

    /**
     * 获取指定路径下的指定文件列表
     *
     * @param rootFile      文件路径
     * @param extensionName 文件扩展名
     * @return
     */
    public static List<File> getFiles(File rootFile, String extensionName) {
        List<File> fileList = Lists.newArrayList();

        String tail = null;
        if (extensionName == null) {
            tail = "";
        } else {
            tail = "." + extensionName;
        }

        if (rootFile == null) {
            return fileList;

        } else if (rootFile.isFile() && rootFile.getName().endsWith(tail)) {
            fileList.add(rootFile);
            return fileList;

        } else if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(tail)) {
                    fileList.add(file);

                } else if (file.isDirectory()) {
                    fileList.addAll(getFiles(file, extensionName));
                }
            }
        }

        return fileList;
    }

    public static List<URL> getJarUrl(URL rootUrl, String extensionName) throws IOException {
        List<URL> result = Lists.newArrayList();

        if (rootUrl == null || !"jar".equals(rootUrl.getProtocol())) {
            return result;
        }

        if (StringUtils.isNotBlank(extensionName)) {
            extensionName = "." + extensionName;
        }

        if (extensionName == null) {
            extensionName = "";
        }

        URLConnection con = rootUrl.openConnection();

        JarURLConnection jarCon = (JarURLConnection) con;
        jarCon.setUseCaches(false);
        JarFile jarFile = jarCon.getJarFile();
        JarEntry jarEntry = jarCon.getJarEntry();
        String rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");

        if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
            rootEntryPath = rootEntryPath + "/";
        }

        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            String entryPath = entry.getName();

            if (entryPath.startsWith(rootEntryPath)) {
                String relativePath = entryPath.substring(rootEntryPath.length());
                if (relativePath.endsWith(".service")) {
                    result.add(createRelative(rootUrl, relativePath));

                }
            }
        }

        return result;

    }

    private static URL createRelative(URL url, String relativePath) throws MalformedURLException {
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }

        return new URL(url, relativePath);
    }
}