package com.common.utils.utils;

import com.common.utils.modle.JndiApp;
import com.fang.common.project.redis.PropertiesConfigUtils;
import org.apache.commons.lang3.StringUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by  on 2017/2/28.
 */
public class SystemGlobals {

    /** 域 preferences. */
    private static Properties preferences = null;

    /** 域 queries. */
    private static Properties queries = new Properties();

    private static JndiApp jndiAppConfig = null;

    private static String configFile = "SystemGlobals.properties";


    private synchronized static void init()
    {
        if ( preferences != null ) return;
        preferences = new Properties();

        try {
            String fileName = configFile;
            try {
                Context initCtx = new InitialContext();
                Context envCtx = (Context) initCtx.lookup("java:comp/env");
                JndiApp bean = (JndiApp) envCtx.lookup("bean/MyBeanFactory");
                jndiAppConfig = bean;

                if ( jndiAppConfig != null ) {
                    String domain = jndiAppConfig.getAppDomain();
                    if ( StringUtils.isNotBlank(domain) ) {
                        String[] configFileArr = configFile.split("\\.");
                        fileName = configFileArr[0]+"_" + domain + "."+configFileArr[1];
                    }
                }
            } catch (Exception e) {
//				e.printStackTrace();
            }

            InputStream is = SystemGlobals.class.getClassLoader().getResourceAsStream( fileName );
            if( is!=null ){
                Properties p = new Properties();
                p.load(is);
                Enumeration enu = p.keys();
                String key = "";
                while( enu.hasMoreElements() ){
                    key = (String) enu.nextElement();
                    preferences.put( key, (String)p.get(key) );
                }
            }
        }
        catch (Exception e) {
//     	   e.printStackTrace();
        }
    }


    public static String getDomain()
    {
        if ( preferences == null ) {
            init();
        }

        String domain = null;
        if ( jndiAppConfig != null ) {
            domain = jndiAppConfig.getAppDomain();
        }

        return domain;
    }


    /**
     * 获取域  Preference.
     *
     * @param key the key
     * @return  Preference
     */
    public static String getPreference( String key ) {
        if ( preferences == null ) {
            init();
        }
        String s = preferences.getProperty(key);
        if ( s != null ) {s = s.trim();}else{
            s = PropertiesConfigUtils.getProperty(key);
        }
        return s;
    }


    public static String getPreference( String key, String defaultValue ) {
        if ( preferences == null ) {
            init();
        }
        String s = preferences.getProperty(key);
        if ( s==null ) {
            s = PropertiesConfigUtils.getProperty(key,defaultValue);
        }
        return s;
    }


    /**
     * 获取域  Preference.
     *
     * @param key the key
     * @param params the params
     * @return  Preference
     */
    public static String getPreference( String key, Object...params ) {
        if ( preferences == null ) {
            init();
        }
        String message = preferences.getProperty(key);
        if ( message!=null ) message = message.trim();
        else{
            message = PropertiesConfigUtils.getProperty(key);
        }

        if ( params==null || params.length==0 ) return message;


        String[] ss = new String[params.length];
        Object o = null;
        for ( int i=0; i<params.length; i++ ) {
            o = params[i];
            if ( o==null ) {
                ss[i] = "";
            }
            else {
                ss[i] = o.toString();
            }
        }

        return replacePlaceHolder( message, ss );
    }


    /**
     * Sets the preference.
     *
     * @param key the key
     * @param value the value
     */
    public static void setPreference( String key, String value ) {
        if ( preferences == null ) {
            init();
        }
        if ( value!=null ) {
            value = value.trim();
            preferences.setProperty( key, value );
        }
        else {
            preferences.remove( key );
        }
    }


    public static int getIntPreference( String key, int defaultValue ) {
        if ( preferences == null ) {
            init();
        }
        String s = getPreference( key );
        if ( StringUtils.isBlank(s) ) return defaultValue;
        else return Integer.parseInt(s);
    }


    public static void setIntPreference( String key, int value ) {
        if ( preferences == null ) {
            init();
        }
        setPreference( key, String.valueOf(value) );
    }


    public static long getLongPreference( String key, long defaultValue ) {
        if ( preferences == null ) {
            init();
        }
        String s = getPreference( key );
        if ( StringUtils.isBlank(s) ) return defaultValue;
        else return Long.parseLong(s);
    }


    public static void setLongPreference( String key, long value ) {
        if ( preferences == null ) {
            init();
        }
        setPreference( key, String.valueOf(value) );
    }


    public static boolean getBooleanPreference( String key, boolean defaultValue ) {
        if ( preferences == null ) {
            init();
        }
        String s = getPreference( key );
        if ( StringUtils.isBlank(s) ) return defaultValue;
        else return Boolean.parseBoolean(s);
    }

    public static void setBooleanPreference( String key, boolean value ) {
        if ( preferences == null ) {
            init();
        }
        setPreference( key, String.valueOf(value) );
    }


    /**
     * (这里用一句话描述这个方法的作用) replacePlaceHolder.
     * (这里描述这个方法适用条件 – 可选)<br>
     * (这里描述这个方法的执行流程 – 可选)<br>
     * (这里描述这个方法的使用方法 – 可选)<br>
     * (这里描述这个方法的注意事项 – 可选)<br>
     *
     * @param message the message
     * @param params the params
     * @return the String
     */
    private static String replacePlaceHolder( String message, String[] params )
    {
        if ( StringUtils.isBlank(message) ) return message;
        if ( params==null || params.length==0 ) return message;

        Map<String, String> map = new HashMap<String, String>();
        int index = -1;

        Pattern p = Pattern.compile("\\{(\\d+)\\}");
        Matcher m = p.matcher( message );

        while ( m.find() ) {
            if ( m.groupCount()<1 ) continue;
            index = Integer.parseInt( m.group(1) );
            if ( index<0 || index>=params.length ) continue;

            map.put( m.group(0), params[index] );
        }

        if ( map.isEmpty() ) return message;

        for ( Map.Entry<String, String> entry : map.entrySet() ) {
            message = message.replace( entry.getKey(), entry.getValue() );
        }

        return message;
    }


    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main( String[] args ) {
        String s = "thia is a {4} or a {1} {0} hahah";
        String[] params = {"AA","BB","CC"};

        Map<String, String> map = new HashMap<String, String>();
        int index = -1;

        Pattern p = Pattern.compile("\\{(\\d+)\\}");
        Matcher m = p.matcher( s );

        while ( m.find() ) {
            if ( m.groupCount()<1 ) continue;
            index = Integer.parseInt( m.group(1) );
            if ( index<0 || index>=params.length ) continue;

            map.put( m.group(0), params[index] );
        }

        for ( Map.Entry<String, String> entry : map.entrySet() ) {
            s = s.replace( entry.getKey(), entry.getValue() );
        }

        System.out.println( s );
    }



}
