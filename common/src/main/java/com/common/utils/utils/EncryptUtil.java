package com.common.utils.utils;

/**
 * Created by fn on 2017/4/28.
 */

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.codec.digest.DigestUtils;

public class EncryptUtil {
    private static final byte[] desKey = new byte[]{(byte)64, (byte)-77, (byte)35, (byte)-45, (byte)16, (byte)-22, (byte)121, (byte)-15};

    public EncryptUtil() {
    }

    public static final String md5(String content) {
        return DigestUtils.md5Hex(content);
    }

    public static void main(String[] args) {
        String code = "72e3f530a7d642266d7accc8c0fb9f239e2f30fb0eb70deaf4d33a1d21d97e94779552d0c9e135dd79974ea9d19cd758";
        System.out.println(decryptString(code));
    }

//    //public static final String MD5(String content) {
//        return DigestUtils.md5Hex(content).toUpperCase();
//    }

    public static final String encryptString(String value, byte[] desKey) {
        String tmp = value;

        try {
            Cipher e = Cipher.getInstance("ES");
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            e.init(1, key);
            byte[] cipherByte = e.doFinal(value.getBytes());
            tmp = encodeHex(cipherByte);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return tmp;
    }

    public static final String encryptSSOId(String userName, String password) {
        String tmp = userName + '\u0002' + password;

        try {
            Cipher e = Cipher.getInstance("ES");
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            e.init(1, key);
            byte[] cipherByte = e.doFinal(tmp.getBytes());
            tmp = encodeHex(cipherByte);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return tmp;
    }

    public static final int decryptSSOIdUserId(String ssoId) {
        String deInfo = decryptString(ssoId);
        int pos = deInfo.indexOf(2);
        if(pos != -1) {
            int userId = getInt(deInfo.substring(0, pos));
            if(userId > 0) {
                return userId;
            }
        }

        return 0;
    }

    public static int getInt(Object obj) {
        if(obj instanceof Integer) {
            return ((Integer)obj).intValue();
        } else if(obj instanceof Double) {
            Double b2 = (Double)obj;
            return b2.intValue();
        } else if(obj instanceof String) {
            String b1 = (String)obj;
            if( isBlank(b1)) {
                return 0;
            } else {
                b1 = b1.trim();

                try {
                    if(b1.indexOf(".") != -1) {
                        Double d = Double.valueOf(Double.parseDouble(b1));
                        return d.intValue();
                    } else {
                        return Integer.parseInt(b1);
                    }
                } catch (NumberFormatException var3) {
                    return 0;
                }
            }
        } else if(obj instanceof Boolean) {
            boolean b = ((Boolean)obj).booleanValue();
            return b?1:0;
        } else {
            return 0;
        }
    }


    public static boolean isBlank(String str) {
        if(str == null) {
            return true;
        } else {
            str = str.trim();
            return str.equals("") || str.toLowerCase().equals("null");
        }
    }

    public static final String decryptSSOIdPass(String ssoId) {
        String deInfo = decryptString(ssoId);
        int pos = deInfo.indexOf(2);
        if(pos != -1) {
            return deInfo.substring(pos + 1);
        } else {
            return null;
            //throw new Exception("7008" + "ssoId:" + ssoId   );
        }
    }

    public static final String encryptString(String value) {
        String tmp = value;

        try {
            Cipher e = Cipher.getInstance("ES");
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            e.init(1, key);
            byte[] cipherByte = e.doFinal(value.getBytes());
            tmp = encodeHex(cipherByte);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return tmp;
    }

    public static final String decryptString(String source) {
        String tmp = source;

        try {
            byte[] e = decodeHex(tmp);
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher c1 = Cipher.getInstance("ES");
            c1.init(2, key);
            byte[] cipherByte = c1.doFinal(e);
            tmp = new String(cipherByte);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return tmp;
    }

    public static final String decryptString(String source, byte[] desKey) {
        String tmp = source;

        try {
            byte[] e = decodeHex(tmp);
            DESKeySpec dks = new DESKeySpec(desKey);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(dks);
            Cipher c1 = Cipher.getInstance("ES");
            c1.init(2, key);
            byte[] cipherByte = c1.doFinal(e);
            tmp = new String(cipherByte);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return tmp;
    }

    public static final String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if((bytes[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buf.toString();
    }

    public static final byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;

        for(int i = 0; i < chars.length; i += 2) {
            byte newByte = 0;
            int var6 = newByte | hexCharToByte(chars[i]);
            var6 <<= 4;
            var6 |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte)var6;
            ++byteCount;
        }

        return bytes;
    }

    public static final String genAutoLoginParams(String ssoHost, String userName, String nextPage, boolean isLimit) {
        StringBuilder sb = new StringBuilder();
        sb.append("userName=").append(userName).append("&t=").append(System.currentTimeMillis()).append("&next_page=" + nextPage);
        if(!isLimit) {
            sb.append("&lim=0");
        }

        String enValue = encryptString(sb.toString());
        if(!ssoHost.endsWith("/")) {
            ssoHost = ssoHost + "/";
        }

        if(!ssoHost.endsWith("sso/")) {
            ssoHost = ssoHost + "sso/";
        }

        String url = ssoHost + "autoLogin.do?request=" + enValue;
        return url;
    }

    private static final byte hexCharToByte(char ch) {
        switch(ch) {
            case '0':
                return (byte)0;
            case '1':
                return (byte)1;
            case '2':
                return (byte)2;
            case '3':
                return (byte)3;
            case '4':
                return (byte)4;
            case '5':
                return (byte)5;
            case '6':
                return (byte)6;
            case '7':
                return (byte)7;
            case '8':
                return (byte)8;
            case '9':
                return (byte)9;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            default:
                return (byte)0;
            case 'a':
                return (byte)10;
            case 'b':
                return (byte)11;
            case 'c':
                return (byte)12;
            case 'd':
                return (byte)13;
            case 'e':
                return (byte)14;
            case 'f':
                return (byte)15;
        }
    }
}
