package com.common.utils;

import com.common.utils.redis.Constants;
import com.common.utils.zookeeper.ZookeeperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 系统配置参数类
 */
public class PropertiesConfigUtils {

    private static final Logger logger = Logger.getLogger(PropertiesConfigUtils.class);

    private PropertiesConfigUtils() {
    }

    private static volatile Properties PROPERTIES;

    private final static String SYSTEMGLOBALS = "/SystemGlobals/";

    public static Properties getProperties() {
        if (PROPERTIES == null) {
            synchronized (PropertiesConfigUtils.class) {
                if (PROPERTIES == null) {
                    String path = System.getProperty(Constants.SYSTEM_GLOBAL);
                    if (path == null || path.length() == 0) {
                        path = System.getenv(Constants.SYSTEM_GLOBAL);
                        if (path == null || path.length() == 0) {
                            path = Constants.SYSTEM_GLOBAL;
                        }
                    }
                    PROPERTIES = loadProperties(path, false, true);
                }
            }
        }
        return PROPERTIES;
    }

    public static void addProperties(Properties properties) {
        if (properties != null) {
            getProperties().putAll(properties);
        }
    }

    public static void setProperties(Properties properties) {
        if (properties != null) {
            PROPERTIES = properties;
        }
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && value.trim().length() == 0) {
            return null;
        }
        if (value != null) {
            return value;
        }
        Properties properties = getProperties();
        value = properties.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            try {
                ZooKeeper zooKeeper = ZookeeperClient.getZooKeeper();

                String path = SYSTEMGLOBALS + ZookeeperClient.getAppName() + "/" + key;
                if (zooKeeper.exists(path, null) != null) {
                    value = new String(zooKeeper.getData(path, false, null));
                }
            }
            catch (KeeperException e) {
                e.printStackTrace();
                logger.error("load systemglobals property error", e);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                logger.error("load systemglobals property error", e);
            }
            catch (Exception e) {
                e.printStackTrace();
                logger.error("load systemglobals property error", e);
                if (defaultValue == null) {
                    properties.setProperty(key, "");
                } else {
                    properties.setProperty(key, defaultValue);
                }
            }
            if (StringUtils.isNotBlank(value)) {
                properties.setProperty(key, value);
                return value;
            } else {
                return defaultValue;
            }
        }
    }

    public static Properties loadProperties(String fileName) {
        return loadProperties(fileName, false, false);
    }

    public static Properties loadProperties(String fileName, boolean allowMultiFile) {
        return loadProperties(fileName, allowMultiFile, false);
    }

    public static Properties loadProperties(String fileName, boolean allowMultiFile, boolean optional) {
        Properties properties = new Properties();
        if (fileName.startsWith("/")) {
            try {
                FileInputStream input = new FileInputStream(fileName);
                try {
                    properties.load(input);
                }
                finally {
                    input.close();
                }
            }
            catch (Throwable e) {
                logger.warn(
                        "Failed to load " + fileName + " file from " + fileName + "(ingore this file): "
                                + e.getMessage(), e);
            }
            return properties;
        }

        List<java.net.URL> list = new ArrayList<java.net.URL>();
        try {
            Enumeration<java.net.URL> urls = PropertiesConfigUtils.class.getClassLoader().getResources(fileName);
            list = new ArrayList<java.net.URL>();
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
        }
        catch (Throwable t) {
            logger.warn("Fail to load " + fileName + " file: " + t.getMessage(), t);
        }

        if (list.size() == 0) {
            if (!optional) {
                logger.warn("No " + fileName + " found on the class path.");
            }
            return properties;
        }

        if (!allowMultiFile) {
            if (list.size() > 1) {
                String errMsg = String.format(
                        "only 1 %s file is expected, but %d dubbo.properties files found on class path: %s", fileName,
                        list.size(), list.toString());
                logger.warn(errMsg);
                // throw new IllegalStateException(errMsg); // see
                // http://code.alibabatech.com/jira/browse/DUBBO-133
            }

            // fall back to use method getResourceAsStream
            try {
                properties.load(PropertiesConfigUtils.class.getClassLoader().getResourceAsStream(fileName));
            }
            catch (Throwable e) {
                logger.warn(
                        "Failed to load " + fileName + " file from " + fileName + "(ingore this file): "
                                + e.getMessage(), e);
            }
            return properties;
        }

        logger.info("load " + fileName + " properties file from " + list);

        for (java.net.URL url : list) {
            try {
                Properties p = new Properties();
                InputStream input = url.openStream();
                if (input != null) {
                    try {
                        p.load(input);
                        properties.putAll(p);
                    }
                    finally {
                        try {
                            input.close();
                        }
                        catch (Throwable t) {}
                    }
                }
            }
            catch (Throwable e) {
                logger.warn("Fail to load " + fileName + " file from " + url + "(ingore this file): " + e.getMessage(),
                        e);
            }
        }

        return properties;
    }

    public static void setProperty(String key, String value) {

        Properties properties = getProperties();
        properties.setProperty(key, value);
    }
}
