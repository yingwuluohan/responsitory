package com.common.utils.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by  on 2017/3/1.
 */
public class Md5Util {
    /**
     *
     * @param plainText
     *            明文
     * @return 32位密文
     */
    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                md.update(plainText.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    public static String getSignature(Map<String, Object> map) {
        if (map.containsKey("signature")) {
            map.remove("signature");
        }
        // 根据senderId获取sign
        Object senderId = map.get("sender_id");
        if (senderId == null)
            return null;
        String sign = Md5Util.encryption(String.valueOf(senderId));
        // 生成signature
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map);
        StringBuilder signatureBuiler = new StringBuilder();
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            signatureBuiler.append(entry.getKey()).append("=")
                    .append(entry.getValue()).append("&");
        }
        signatureBuiler.append("sign=").append(sign);
        String signature = Md5Util.encryption(signatureBuiler.toString());
        return signature;
    }
    public static void main( String[] args ){
        System.out.println(  );
    }

}
