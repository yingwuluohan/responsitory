package com.fang.common.project.redis;

/**
 * 2015/11/4.
 */
public class Constants {
    public static final String SYSTEM_GLOBAL = "SystemGlobals.properties";
    public static final String CONFIG_FILE = "properties.config.file";

    public static final int EXECUTOR_POOL_CORE_SIZE = 10;
    public static final int EXECUTOR_POOL_MAX_SIZE = 20;
    public static final int EXECUTOR_POOL_ARRAY_QUEUE_SIZE = 10000;

    public static final int HEALTH_CHECK_INTERVAL = 10;
    public static final int HEALTH_CHECK_ERROR_TIMES = 2;

    public static final String NONE = "none";
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String APPLOCATION = "appLocation";

    public static final int MAX_RANDOM_TIMES = 10000;


    public final static int DUBBO_METHOD_START = 1;
    public final static int DUBBO_METHOD_END = 2;
    public final static int CONTROLLER_METHOD_START = 3;
    public final static int CONTROLLER_METHOD_END = 4;
    public final static int CACHE_METHOD_START = 5;
    public final static int CACHE_METHOD_END = 6;
    public final static int SQL_METHOD_START = 7;
    public final static int SQL_METHOD_END = 8;
    public final static int HTTP_METHOD_START = 9;
    public final static int HTTP_METHOD_END = 10;
    public final static int FILTER_METHOD_START = 11;
    public final static int FILTER_METHOD_END = 12;

    public final static int SQL_CONNECTION_START = 13;
    public final static int SQL_CONNECTION_END = 14;

    public final static int SERVICE_METHOD_START = 15;

    public final static int SERVICE_METHOD_END = 16;

    public final static int JOB_METHOD_START = 17;

    public final static int JOB_METHOD_END = 18;

    public final static int JSP_METHOD_START = 19;

    public final static int JSP_METHOD_END = 20;

    public final static int DUBBO_PROVIDER_METHOD_START = 21;
    public final static int DUBBO_PROVIDER_METHOD_END = 22;

    public final static int DUBBO_METHOD_ERROR = -1;
    public final static int CONTROLLER_METHOD_ERROR = -2;
    public final static int CACHE_METHOD_ERROR = -3;
    public final static int SQL_METHOD_ERROR = -4;
    public final static int HTTP_METHOD_ERROR = -5;
    public final static int FILTER_METHOD_ERROR = -6;
    public final static int SQL_CONNECTION_ERROR = -7;
    public final static int SERVICE_METHOD_ERROR = -8;
    public final static int JOB_METHOD_ERROR = -9;
    public final static int JSP_METHOD_ERROR = -10;
    public final static int DUBBO_PROVIDER_METHOD_ERROR = -11;
}
