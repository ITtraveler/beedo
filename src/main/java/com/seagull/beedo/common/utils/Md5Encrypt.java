/**
 * yingyinglicai.com Inc.
 * Copyright (c) 2013-2016 All Rights Reserved.
 */
package com.seagull.beedo.common.utils;


import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author guosheng.huang
 * @version $Id: Md5Encrypt.java, v 0.1 2018年1月20日 下午6:09:44 yylc Exp $
 */
@Slf4j
public class Md5Encrypt {


    private static MessageDigest digest = null;

    /**
     * MD5的加密次数。默认将字符串进行两次MD5加密。
     */
    private static final int ENCRYPT_COUNT = 2;

    /**
     * 高强度加密，把MD5字符串进行多次加密，防止破译(与P2C算法统一)
     *
     * @param str 需要加密的字符串
     * @return String 加密后的字符串
     */
    public static final String toMD5High(String str) {
        int count = ENCRYPT_COUNT;
        while (count-- > 0)
            str = toMD5(str);
        return str;
    }

    private static final String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * 用MD5加密字符串(与P2C算法统一)
     *
     * @param str 需要加密的字符串
     * @return String 加密后的字符串
     */
    public static final String toMD5(String str) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                log.warn("Md5Encrypt toMD5 NoSuchAlgorithmException,param:" + str, e);
                e.printStackTrace();
            }
        }
        digest.update(str.getBytes());
        return toHex(digest.digest());
    }

    public static final String toHex(byte hash[]) {
        StringBuilder builder = new StringBuilder(hash.length * 2);
        for (int i = 0; i < hash.length; i++) {
            if (((int) hash[i] & 0xff) < 0x10) {
                builder.append("0");
            }
            builder.append(Long.toString((int) hash[i] & 0xff, 16));
        }
        return builder.toString();
    }

    public static String StringFilter(String str) {
        try {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String md5Simple(String content) {
        return md5("", content);
    }

    public static String md5Simple(String content, String charset) {
        return md5("", content, charset);
    }

    public static String md5(String key, String text) {
        return md5(key, text, null);
    }

    public static String md5(byte[] key, String text) {
        return md5(key, text, null);
    }

    public static String md5(String key, String text, String charset) {
        return md5(StringUtils.isEmpty(key) ? null : key.getBytes(), text, charset);
    }

    public static String md5(byte[] key, String text, String charset) {
        if (StringUtils.isEmpty(text))
            return null;
        byte[] data = null;
        if (!StringUtils.isEmpty(charset)) {
            try {
                data = text.getBytes(charset);
            } catch (Exception e) {
                log.error("MD5异常", e);
                return null;
            }
        } else {
            data = text.getBytes();
        }
        return md5(key, data);
    }

    public static String md5(byte[] key, byte[] content) {

        MessageDigest msgDigest = null;

        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }
        if (key != null && key.length > 0) {
            msgDigest.update(key);
        }
        try {
            msgDigest.update(content);
        } catch (Exception e) {
            // 不会发生
            log.error("MD5异常", e);
            return null;
        }

        byte[] bytes = msgDigest.digest();

        byte tb;
        char low;
        char high;
        char tmpChar;

        String md5Str = new String();

        for (int i = 0; i < bytes.length; i++) {
            tb = bytes[i];

            tmpChar = (char) ((tb >>> 4) & 0x000f);

            if (tmpChar >= 10) {
                high = (char) (('a' + tmpChar) - 10);
            } else {
                high = (char) ('0' + tmpChar);
            }

            md5Str += high;
            tmpChar = (char) (tb & 0x000f);

            if (tmpChar >= 10) {
                low = (char) (('a' + tmpChar) - 10);
            } else {
                low = (char) ('0' + tmpChar);
            }

            md5Str += low;
        }

        return md5Str;
    }

    /******************
     * 以下为16位MD5算法
     ****************/
    private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++) {
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }

    public static String md5(String input) {
        return md5(input, "UTF-8", 32);
    }

    public static String md5(String input, int bit) {
        return md5(input, "UTF-8", bit);
    }

    public static byte[] md5ToBytes(byte[] input) {

        try {
            MessageDigest md = MessageDigest
                    .getInstance(System.getProperty("MD5.algorithm", "MD5"));
            return md.digest(input);
        } catch (Exception e) {
            // 不会发生
            log.error("MD5异常", e);
            return null;
        }
    }

    public static String md5(String input, String charset, int bit) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest
                    .getInstance(System.getProperty("MD5.algorithm", "MD5"));
            if (bit == 16)
                return bytesToHex(md.digest(input.getBytes(charset))).substring(8, 24);
            return bytesToHex(md.digest(input.getBytes(charset)));
        } catch (Exception e) {
            // 不会发生
            log.error("MD5异常", e);
            return null;
        }
    }
}