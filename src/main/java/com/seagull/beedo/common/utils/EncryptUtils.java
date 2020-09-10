/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.seagull.beedo.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;

public class EncryptUtils {

    private static final String UTF8 = "utf-8";

    /**
     * MD5数字签名
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String md5Digest(String src) {
        try {
            // 定义数字签名方法, 可用：MD5, SHA-1
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(src.getBytes(UTF8));
            return byte2HexStr(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64编码
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String base64Encoder(String src) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(src.getBytes(UTF8));
    }

    /**
     * BASE64解码
     *
     * @param dest
     * @return
     * @throws Exception
     */
    public static String base64Decoder(String dest) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(dest), UTF8);
    }

    /**
     * 字节数组转化为大写16进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * SHA1数据加密
     *
     * @param str
     * @return
     */
    public static String encryptSHA1(String str) {
        if (str == null) {
            return null;
        }
        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String pswEncrypt(String password) {
        return encryptSHA1(md5Digest(password));
    }
}
