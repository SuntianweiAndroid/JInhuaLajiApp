package com.speedata.jinhualajidemo.printerdemo.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by banketree on 2017/7/11.
 */

public class StringUtil {
    //闈炴硶瀛楃
    public static boolean isString(String str) {
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~锛丂#锟�%鈥︹��&*锛堬級鈥斺��+|{}銆愩�戔�橈紱锛氣�濃�溾�欙紵銆娿�媉]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    //闈炴硶瀛楃
    public static boolean isName(String str) {
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;'\\[\\]<>/?~锛丂#锟�%鈥︹��&*锛堬級鈥斺��+|{}銆愩�戔�橈紱锛氣�濃�溾�欙紵-]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    //闈炴硶瀛楃
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    /*
     * 鎵嬫満鍙烽獙璇�
     *
     * @param str
     *
     * @return 楠岃瘉閫氳繃杩斿洖true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 楠岃瘉鎵嬫満鍙�
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isLandline(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;


        p = Pattern.compile("(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)"); // 楠岃瘉鍥哄畾鐢佃瘽
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    //韬唤璇�
    public static boolean isPersion(String str) {
        Pattern p = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String getText(String value) {
        if (TextUtils.isEmpty(value) || "null".equals(value)) {
            return "";
        }
        return value;
    }


    /**
     * 淇濈暀涓棿鐨勭┖鏍硷紝杩囨护瀛楃涓插墠鍚庣殑绌烘牸
     *
     * @param str
     * @return
     */
    public static String saveStrInnerSpace(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        str = str.trim();
        while (str.startsWith(" ")) {
            str = str.substring(1, str.length()).trim();
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length() - 1).trim();
        }
        return str;
    }

    public static boolean isValueEmpty(String value) {
        if (TextUtils.isEmpty(value) || "null".equalsIgnoreCase(value)) {
            return true;
        }

        return false;
    }

    /**  
      * bytes转换成十六进制字符串  
      * @param byte[] b byte数组  
      * @return String 每个Byte值之间空格分隔  
      */
    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            Integer.toHexString(1);
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**   
      * 字符串转换成十六进制字符串  
      * @param String str 待转换的ASCII字符串  
      * @return String 每个Byte之间空格分隔，如: [61 6C 6B]  
      */
    public static String str2HexStr(String str)
    {
        char[] chars="0123456789ABCDEF".toCharArray();
        StringBuilder sb=new StringBuilder("");
        byte[] bs=str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    private static int parse(char c) {
        if (c>= 'a')
            return (c - 'a' + 10) & 0x0f;
        if(c>='A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
    // 从十六进制字符串到字节数组转换  
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static char[] stringToChar(String str) {
        char[] sendStr;
        String[] itemStr = str.split(" ");
        sendStr = new char[itemStr.length];
        for (int i = 0; i < itemStr.length; i++) {
            char ch = (char) Integer.parseInt(itemStr[i], 16);
            sendStr[i] = ch;
        }
        return sendStr;
    }
}
