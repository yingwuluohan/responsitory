package com.common.utils.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by fn on 2017/2/28.
 */
public class CommonUtil {
    public static List<String> strToList(String str, String delimiter) {
        if(StringUtils.isBlank(str)) {
            return null;
        } else {
            StringTokenizer token = new StringTokenizer(str, delimiter, false);
            ArrayList result = new ArrayList();
            String s = null;

            while(token.hasMoreElements()) {
                s = ((String)token.nextElement()).trim();
                if(s.length() > 0) {
                    result.add(s);
                }
            }

            return result;
        }
    }
}
